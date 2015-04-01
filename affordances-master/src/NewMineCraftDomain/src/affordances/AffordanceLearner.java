package affordances;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import minecraft.MapIO;
import minecraft.NameSpace;
import minecraft.MinecraftBehavior.MinecraftBehavior;
import minecraft.MinecraftBehavior.Planners.MinecraftPlanner;
import minecraft.MinecraftBehavior.Planners.VIPlanner;
import minecraft.WorldGenerator.MapFileGenerator;
import minecraft.WorldGenerator.WorldTypes.DeepTrenchWorld;
import minecraft.WorldGenerator.WorldTypes.PlaneGoldMineWorld;
import minecraft.WorldGenerator.WorldTypes.PlaneGoldSmeltWorld;
import minecraft.WorldGenerator.WorldTypes.PlaneWallWorld;
import minecraft.WorldGenerator.WorldTypes.PlaneWorld;
import burlap.behavior.affordances.Affordance;
import burlap.behavior.affordances.AffordanceDelegate;
import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.QValue;
import burlap.behavior.singleagent.planning.OOMDPPlanner;
import burlap.behavior.singleagent.planning.ValueFunctionPlanner;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.State;
import burlap.oomdp.logicalexpressions.LogicalExpression;
import burlap.oomdp.singleagent.Action;
import burlap.oomdp.singleagent.GroundedAction;

public class AffordanceLearner {
	
	private KnowledgeBase 			affordanceKB;
	private Map<Integer,LogicalExpression> lgds;
	private MinecraftBehavior 		mcb;
	private int 					numWorldsPerLGD = 5;
	private boolean					useOptions;
	private boolean					useMAs;
	private double 					fractOfStatesToUse;

	
	/**
	 * An object that is responsible for learning affordances.
	 * @param mcb: a MinecraftBehavior associated with the relevant domain and planners.
	 * @param kb: a KnowledgeBase of affordances containing the proper lgds/predicates.
	 * @param lgds: the list of goal descriptions to learn with
	 * @param countTotalActions: a boolean indicating if the learner should count the total number of action applications or number of worlds in which an action was optimal
	 * @param numWorlds: the number of worlds per task type to learn on
	 * @param useOptions: a boolean indicating if we should learn with options
	 * @param useMAs: a boolean indicating if we should learn with macroactions
	 * @param fractOfStatesToUse: the fraction of the state space to learn with
	 */
	public AffordanceLearner(MinecraftBehavior mcb, KnowledgeBase kb, Map<Integer,LogicalExpression> lgds, int numWorlds, boolean useOptions, boolean useMAs, double fractOfStatesToUse, List<AbstractGroundedAction> allActions) {
		this.lgds = lgds;
		this.mcb = mcb;
		this.affordanceKB = kb;
		this.useOptions = useOptions;
		this.useMAs = useMAs;
		this.fractOfStatesToUse = fractOfStatesToUse;
		this.numWorldsPerLGD = numWorlds;
	}
	
	/**
	 * Runs the full learning algorithm
	 * @param createMaps: boolean to indicate if we should create new maps or use the existing ones
	 */
	public void learn(boolean createMaps, String jobID) {
		
		// Setup map objects and create maps if we need to
		List<MapIO> maps = new ArrayList<MapIO>();
		String learningMapDir = NameSpace.PATHMAPS + "/learning/" + jobID + "/";

		if(createMaps) {
			createLearningMaps(learningMapDir);
		}
		
		File learningDir = new File(learningMapDir);
		String[] learningMaps = learningDir.list();
				
		// Create the mapIO objects (represent maps)
		for(String map : learningMaps) {
			MapIO learningMap = new MapIO(learningMapDir + map);
			maps.add(learningMap);
		}

		// Run learning on all the generated maps
		for(MapIO map : maps) {
			int lgdInt = map.getHeaderHashMap().get("G");
			affordanceKB.getAffordancesController().setCurrentGoal(this.lgds.get(lgdInt));
			learnMap(map);
		}
	}

	/**
	 * Creates learning maps
	 * @param learningMapDir: the number of maps to create for each goal type
	 */
	public void createLearningMaps(String learningMapDir) {
		MapFileGenerator mapMaker = new MapFileGenerator(2, 3, 4, learningMapDir);
		
		// Get rid of old maps
		mapMaker.clearMapsInDirectory();
		
		int numLavaBlocks = 1;
		
//		System.out.println("Generating maps..." + this.numWorldsPerLGD);
		mapMaker.generateNMaps(this.numWorldsPerLGD, new DeepTrenchWorld(1, numLavaBlocks), 1, 3, 5);
		mapMaker.generateNMaps(this.numWorldsPerLGD, new PlaneGoldMineWorld(numLavaBlocks), 1, 3, 4);
		mapMaker.generateNMaps(this.numWorldsPerLGD, new PlaneGoldSmeltWorld(numLavaBlocks), 2, 2, 4);
//		mapMaker.generateNMaps(this.numWorldsPerLGD, new PlaneWallWorld(1, numLavaBlocks), 3, 1, 4);
		mapMaker.generateNMaps(this.numWorldsPerLGD, new PlaneWorld(numLavaBlocks), 4, 4, 3);
	}
	
	/**
	 * Run the learning algorithm on the given map
	 * @param map: the MapIO object to learn on
	 */
	private void learnMap(MapIO map) {
		// Update behavior with new map
		this.mcb.updateMap(map);
		
		// Create planners
		MinecraftPlanner mcPlanner = new VIPlanner(mcb, this.useOptions, this.useMAs);
		OOMDPPlanner planner = mcPlanner.retrievePlanner();
		
		// Synthesize a policy on the given map
		Policy p = mcb.solve(planner);
		
		// Updates the action counts (alpha)
		updateActionCounts(planner, p, true);
	}
	
	/**
	 * Updates the the counts for each action, for each affordance
	 * @param planner: a planner object that has already solved the given OO-MDP
	 * @param policy: a policy used to get sample trajectories
	 * @param countTotalActions: a boolean indicating to count total number of actions or worlds in which an action was used
	 */
	public void updateActionCounts(OOMDPPlanner planner, Policy policy, boolean countTotalActions) {
		
		// Get the fraction of states states from the policy that we're learning with
		List<State> allStates = ((ValueFunctionPlanner)planner).getAllStates();
		int numStates = allStates.size();
		int numStatesToCount = (int) Math.floor(this.fractOfStatesToUse*numStates);

		// Randomize the states so the fraction we get is sampled randomly 
		Collections.shuffle(allStates, new SecureRandom());
		
		// Generate the initial state
		State initialState = mcb.getInitialState();
		double initVal = ((ValueFunctionPlanner)planner).value(initialState);
		 
		// Loop over each state and count when actions are optimal
		int numStatesCounted = 0;
		for (State st : allStates) {
			if (numStatesCounted >= numStatesToCount) {
				break;
			}
			
			// Do not count actions in extremely bad states
			double stateVal = ((ValueFunctionPlanner)planner).value(st); 
			if (stateVal < 10 * initVal) {  // TODO: 10 is randomly selected
				continue;
			}
			
			// Do not count values in the terminal state
			if (mcb.getTerminalFunction().isTerminal(st)) {
				continue;
			}
			
			// We're actually counting this state, so increment counter
			++numStatesCounted;
			
			// Get the optimal actions for that state and update affordance counts
			List<GroundedAction> optimalActions = getOptimalActionsFromPolicy(planner, st);			
			for (AffordanceDelegate affDelegate: affordanceKB.getAffordances()) {
				// If affordance is active
				if(affDelegate.isActive(st)) {
					for (GroundedAction optimalAct : optimalActions) {
						// Update counts for optimal actions AND affordance active
						affDelegate.getAffordance().incrementActionOptimalAffActive(optimalAct);
					}
				}
				for (GroundedAction optimalAct : optimalActions) {
					// Update counts for optimal actions
					affDelegate.getAffordance().incrementTotalActionOptimal(optimalAct);
				}
			}
		}
	}
	
	/**
	 * Retrieves the list of optimal actions according to the given policy, for the provided state
	 * @param p: a Policy object to determine optimality of actions
	 * @param st: a State object
	 * @return: List<GroundedAction> containing all of the optimal actions for this state
	 */
	private List<GroundedAction> getOptimalActionsFromPolicy(OOMDPPlanner planner, State st){ 
		List<AbstractGroundedAction> allActions = getAllActions(this.mcb, false, false);
		
		double maxQ = Double.NEGATIVE_INFINITY;
		for(AbstractGroundedAction ga : allActions) {
			QValue qv = ((ValueFunctionPlanner)planner).getQ(st, ga);
			if (qv.q > maxQ) {
				maxQ = qv.q;
			}
		}

		// Get optimal actions based on max prob
		List<GroundedAction> optimalActions = new ArrayList<GroundedAction>();
		for(AbstractGroundedAction ga : allActions) {
			QValue qv = ((ValueFunctionPlanner)planner).getQ(st, ga);
			if (qv.q == maxQ) {
				optimalActions.add((GroundedAction)ga);
			}
		}
		
		return optimalActions;
	}

	/**
	 * Helper method that prints out the counts for each affordance
	 */
	public void printCounts() {
		for (AffordanceDelegate affDelegate: this.affordanceKB.getAffordances()) {
			affDelegate.printCounts();
			System.out.println("");
		}
	}
	
	/**
	 * Learns and returns a minecraft specific knowledge base of affordances
	 * @param mcBeh: The MinecraftBehavior object to plan with.
	 * @param numWorlds: The number of maps of each task type to learn on.
	 * @param learningRate: A boolean indicating if this KB is for learning rate purposes (changes location of KB).
	 * @param useOptions: A boolean indicating if we should learn with options
	 * @param useMAs: A boolean indicating if we should learn with macroactions
	 * @param fracOfStateSpace: The fraction of the state space to learn with.
	 * @return
	 */
	public static KnowledgeBase learnMinecraftKB(MinecraftBehavior mcBeh, int numWorlds, boolean learningRate, boolean useOptions, boolean useMAs, double fracOfStateSpace, String jobID) {
		
		// Get Actions
		List<AbstractGroundedAction> allGroundedActions = getAllActions(mcBeh, useOptions, useMAs);
		
		// Create lgd list, predicate list, and knowledge base template.
		Map<Integer, LogicalExpression> lgds = getMinecraftGoals(mcBeh);
		KnowledgeBase affKnowledgeBase = generateBlankAffordanceKB(getMinecraftPredicates(mcBeh), lgds, allGroundedActions);
		
		// Initialize Learner
		AffordanceLearner affLearn = new AffordanceLearner(mcBeh, affKnowledgeBase, lgds, numWorlds, useOptions, useMAs, fracOfStateSpace, allGroundedActions);

		String kbName;
		String kbDir = "";
		if(learningRate) {
			kbName = "lr_" + String.format(NameSpace.DOUBLEFORMAT, affLearn.fractOfStatesToUse) + ".kb";
			kbDir = "learning_rate/";
		}
		else {
			kbName = "learned/" + jobID + "_learned" + affLearn.numWorldsPerLGD;
			if(useMAs) kbName += "_ma";
			else if(useOptions) kbName += "_op";
			else kbName += "_prim_acts";
			kbName += ".kb";
		}

		// Learn
		affLearn.learn(true, jobID);
		affKnowledgeBase.setName(kbDir + kbName);
		return affKnowledgeBase;
	}
	
	/**
	 * Generates an affordance knowledge base object
	 * @param predicates: the list of predicates to use
	 * @param lgds: a list of goals to use
	 * @param allActions: the set of possible actions (OO-MDP action set)
	 * @return
	 */
	public static KnowledgeBase generateBlankAffordanceKB(List<LogicalExpression> predicates, Map<Integer, LogicalExpression> lgds, List<AbstractGroundedAction> allActions) {
		KnowledgeBase affordanceKB = new KnowledgeBase();
		
		for (LogicalExpression pf : predicates) {
			for (LogicalExpression lgd : lgds.values()) {
				Affordance aff = new Affordance(pf, lgd, allActions);
				AffordanceDelegate affDelegate = new AffordanceDelegate(aff);	
				affordanceKB.add(affDelegate);
			}
		}
		
		return affordanceKB;
	}
	
	/**
	 * Retrieves the list of minecraft lifted goal descriptions
	 * @param mcBeh
	 * @return a Map<Integer, LogicalExpression>
	 */
	private static Map<Integer,LogicalExpression> getMinecraftGoals(MinecraftBehavior mcBeh) {
		// Set up goal description list
		Map<Integer,LogicalExpression> lgds = new HashMap<Integer,LogicalExpression>();
		
		PropositionalFunction atGoal = mcBeh.pfAgentAtGoal;
		LogicalExpression atGoalLE = AffordanceUtils.pfAtomFromPropFunc(atGoal);
		
		PropositionalFunction hasGoldOre = mcBeh.pfAgentHasAtLeastXGoldOre;
		LogicalExpression goldOreLE = AffordanceUtils.pfAtomFromPropFunc(hasGoldOre);
		
		PropositionalFunction hasGoldBlock = mcBeh.pfAgentHasAtLeastXGoldBar;
		LogicalExpression goldBlockLE = AffordanceUtils.pfAtomFromPropFunc(hasGoldBlock);
		
		// Add goals
		lgds.put(0,atGoalLE);
		lgds.put(1,goldOreLE);
		lgds.put(2,goldBlockLE);
		
		return lgds;
	}
	
	private static List<LogicalExpression> getMinecraftPredicates(MinecraftBehavior mcBeh) {
		// Set up precondition list
		List<LogicalExpression> predicates = new ArrayList<LogicalExpression>();
		
		// AgentInAir PFAtom
		PropositionalFunction agentInAir = mcBeh.pfAgentInMidAir;
		LogicalExpression agentInAirLE = AffordanceUtils.pfAtomFromPropFunc(agentInAir);
		
		// EndOfMapInFrontOfAgent PFAtom
		PropositionalFunction endOfMapInFrontOfAgent = mcBeh.pfEndOfMapInFrontOfAgent;
		LogicalExpression endOfMapLE = AffordanceUtils.pfAtomFromPropFunc(endOfMapInFrontOfAgent);
		
		// Trench in front of the agent PFAtom
		PropositionalFunction trenchInFrontOf = mcBeh.pfTrenchInFrontOfAgent;
		LogicalExpression trenchLE = AffordanceUtils.pfAtomFromPropFunc(trenchInFrontOf);
		
		// Area in front of agent is clear PFAtom
		PropositionalFunction agentCanWalk = mcBeh.pfAgentCanWalk;
		LogicalExpression agentCanWalkLE = AffordanceUtils.pfAtomFromPropFunc(agentCanWalk);

		// Gold is in front of the agent
		PropositionalFunction goldFrontAgent = mcBeh.pfGoldBlockFrontOfAgent;
		LogicalExpression goldFrontAgentLE = AffordanceUtils.pfAtomFromPropFunc(goldFrontAgent);
		
		// Wall (dest) front of agent
		PropositionalFunction wallFrontAgent = mcBeh.pfWallInFrontOfAgent;
		LogicalExpression wallFrontAgentLE = AffordanceUtils.pfAtomFromPropFunc(wallFrontAgent);
		
		// Furnace front of agent
		PropositionalFunction furnaceFrontAgent = mcBeh.pfFurnaceInFrontOfAgent;
		LogicalExpression furnaceFrontAgentLE = AffordanceUtils.pfAtomFromPropFunc(furnaceFrontAgent);
		
		// Hurdle front of agent
		PropositionalFunction hurdleFrontAgent = mcBeh.pfHurdleInFrontOfAgent;
		LogicalExpression hurdleFrontAgentLE = AffordanceUtils.pfAtomFromPropFunc(hurdleFrontAgent);
		
		// Lava front of agent
		PropositionalFunction lavaFrontAgent = mcBeh.pfLavaFrontAgent;
		LogicalExpression lavaFrontAgentLE = AffordanceUtils.pfAtomFromPropFunc(lavaFrontAgent);
		
		// Agent look at lava
		PropositionalFunction agentLookLava = mcBeh.pfAgentLookLava;
		LogicalExpression agentLookLavaLE = AffordanceUtils.pfAtomFromPropFunc(agentLookLava);
		
		// Agent look at (dest) block
		PropositionalFunction agentLookDestWall = mcBeh.pfAgentLookDestWall;
		LogicalExpression agentLookDestWallLE = AffordanceUtils.pfAtomFromPropFunc(agentLookDestWall);
		
		// Agent look at (ind) wall
		PropositionalFunction agentLookIndWall = mcBeh.pfAgentLookIndWall;
		LogicalExpression agentLookIndWallLE = AffordanceUtils.pfAtomFromPropFunc(agentLookIndWall);
		
		// Agent in lava
		PropositionalFunction agentInLava = mcBeh.pfAgentInLava;
		LogicalExpression agentInLavaLE = AffordanceUtils.pfAtomFromPropFunc(agentInLava);
		
		// Looking toward goal
		PropositionalFunction agentLookTowardGoal = mcBeh.pfAgentLookTowardGoal;
		LogicalExpression agentLookTowardGoalLE = AffordanceUtils.pfAtomFromPropFunc(agentLookTowardGoal);
		
		// Looking toward gold
		PropositionalFunction agentLookTowardGold = mcBeh.pfAgentLookTowardGold;
		LogicalExpression agentLookTowardGoldLE = AffordanceUtils.pfAtomFromPropFunc(agentLookTowardGold);
		
		// Looking toward furnace
		PropositionalFunction agentLookTowardFurnace = mcBeh.pfAgentLookTowardFurnace;
		LogicalExpression agentLookTowardFurnaceLE = AffordanceUtils.pfAtomFromPropFunc(agentLookTowardFurnace);
			
		// Agent can jump
		PropositionalFunction agentCanJump = mcBeh.pfAgentCanJump;
		LogicalExpression agentCanJumpLE = AffordanceUtils.pfAtomFromPropFunc(agentCanJump);
		
		// Add LEs to list
		predicates.add(agentInAirLE);
		predicates.add(endOfMapLE);
		predicates.add(trenchLE);
		predicates.add(agentCanWalkLE);
		predicates.add(goldFrontAgentLE);
		predicates.add(wallFrontAgentLE);
		predicates.add(hurdleFrontAgentLE);
		predicates.add(lavaFrontAgentLE);
		predicates.add(agentLookLavaLE);
		predicates.add(agentLookDestWallLE);
		predicates.add(agentLookIndWallLE);
		predicates.add(agentInLavaLE);
		predicates.add(agentLookTowardGoalLE);
//		predicates.add(agentLookTowardGoldLE);
//		predicates.add(agentLookTowardFurnaceLE);
//		predicates.add(agentCanJumpLE);
		predicates.add(furnaceFrontAgentLE);
		
		return predicates;
	}
	
	private static List<AbstractGroundedAction> getAllActions(MinecraftBehavior mcBeh, boolean useOptions, boolean useMAs) {
		
		List<AbstractGroundedAction> allGroundedActions = new ArrayList<AbstractGroundedAction>();
		
		// Create Grounded Action instances for each primitive action
		List<Action> primitiveActions = mcBeh.getDomain().getActions();
		for(Action a : primitiveActions) {
			String[] freeParams = AffordanceUtils.makeFreeVarListFromObjectClasses(a.getParameterClasses());
			GroundedAction ga = new GroundedAction(a, freeParams);
			allGroundedActions.add(ga);
		}
		
		// Create Grounded Action instances for each temporally extended action
		List<Action> temporallyExtendedActions = new ArrayList<Action>(MinecraftPlanner.getMapOfMAsAndOptions(mcBeh, useOptions, useMAs).values());
		for(Action a : temporallyExtendedActions) {
			String[] freeParams = AffordanceUtils.makeFreeVarListFromObjectClasses(a.getParameterClasses());
			GroundedAction ga = new GroundedAction(a, freeParams);
			allGroundedActions.add(ga);
		}
		
		return allGroundedActions;
		
	}

	public static void main(String[] args) {
//		String templatePath = Test.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//		System.out.println(new File(".").getAbsolutePath());
//		List<Integer> l = new ArrayList<Integer>();
//		
//		File input;
//		try {
//			input = new File(l.getClass().getResource("/src/minecraft/maps/learning/DeepTrenchWorld0.map").toURI());
//			System.out.println("INPUT: " + input);
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		
		boolean addOptions = false;
		boolean addMAs = false;
		if(args.length > 1) {
			addOptions = args[2].equals("true") ? true : false;
			addMAs = args[1].equals("true") ? true : false;
		}
		double fractionOfStateSpaceToLearnWith = 1.0; //(Double.parseDouble(args[0]) / 10.0) - 0.1;
		final int numWorldsToLearnWith = 1;
		MinecraftBehavior mcBeh = new MinecraftBehavior();
		String jobID = "local"; //args[0];
		if(addMAs) jobID += "_ma";
		if(addOptions) jobID += "_o";
		learnMinecraftKB(mcBeh, numWorldsToLearnWith, false, addOptions, addMAs, fractionOfStateSpaceToLearnWith, jobID);
	}
	
}
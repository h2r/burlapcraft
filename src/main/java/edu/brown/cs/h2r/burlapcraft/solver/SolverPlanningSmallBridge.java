package edu.brown.cs.h2r.burlapcraft.solver;

import java.util.List;

import net.minecraft.block.Block;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.learning.modellearning.DomainMappedPolicy;
import burlap.behavior.singleagent.planning.StateConditionTest;
import burlap.behavior.singleagent.planning.deterministic.DDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner;
import burlap.behavior.singleagent.planning.deterministic.informed.NullHeuristic;
import burlap.behavior.singleagent.planning.deterministic.informed.astar.AStar;
import burlap.behavior.singleagent.planning.deterministic.uninformed.bfs.BFS;
import burlap.behavior.statehashing.DiscreteStateHashFactory;
import burlap.behavior.statehashing.NameDependentStateHashFactory;
import burlap.domain.singleagent.gridworld.GridWorldStateParser;
import burlap.oomdp.auxiliary.StateParser;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.explorer.TerminalExplorer;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorReal;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorSimulated;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerFMLEvents;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

public class SolverPlanningSmallBridge {


	DomainGeneratorSimulated 	dwdg;
	Domain						domain;
	StateParser 				sp;
	RewardFunction 				rf;
	TerminalFunction			tf;
	StateConditionTest			goalCondition;
	State 						initialState;
	NameDependentStateHashFactory hashingFactory;
	DomainGeneratorReal 		rdg;
	Domain						realDomain;
	
	private int[][][] map;
	private int[][] movementMap;
	private int length;
	private int width;
	private int height;
	
	public SolverPlanningSmallBridge(int[][][] map) {
		
		// set the length, width and height of the dungeon
		this.length = map[0].length;
		this.width = map[0][0].length;
		this.height = map.length;
		
		//create the domain
		dwdg = new DomainGeneratorSimulated(map);
		rdg = new DomainGeneratorReal(map[0].length, map[0][0].length, map.length);

		domain = dwdg.generateDomain();
		realDomain = rdg.generateDomain();
		
		//create the state parser
		sp = new GridWorldStateParser(domain); 
		
		//set up the initial state of the task
		rf = new BridgeRF();
		tf = new MovementTF();
		goalCondition = new GoalCondition();
		
		
		initialState = StateGenerator.getCurrentState(domain, BurlapCraft.dungeonMap.get("small_bridge"));
		
		//set up the state hashing system
		hashingFactory = new NameDependentStateHashFactory();
	}
	
	public void ASTAR() {


		System.out.println("Running dynamic ASTAR for Small bridge");
		DeterministicPlanner planner = new AStar(domain, rf, goalCondition, hashingFactory, new NullHeuristic());
		planner.planFromState(initialState);
		planner.setTf(tf);
		
//		TerminalExplorer exp = new TerminalExplorer(domain);
//		exp.exploreFromState(initialState);

		Policy p = new DDPlannerPolicy(planner);
		DomainMappedPolicy rp = new DomainMappedPolicy( realDomain, p);
		EpisodeAnalysis ea = p.evaluateBehavior(initialState, rf, tf, 100);
		for (int i = 0; i < ea.numTimeSteps() - 1; i++) {
			System.out.println(ea.getState(i).toString());
			System.out.println(ea.getAction(i).toString());
		}
		/*
		Policy p = new SDPlannerPolicy(planner);
		
		EpisodeAnalysis ea = p.evaluateBehavior(initialState, rf, tf);
		
		for (int i = 0; i < ea.numTimeSteps() - 1; i++) {
			System.out.println(ea.getState(i).toString());
			System.out.println(ea.getAction(i).toString());
		}
		HandlerFMLEvents.actions = ea.getActionSequenceString().split("; ");
		HandlerFMLEvents.actionsLeft = HandlerFMLEvents.actions.length;
		HandlerFMLEvents.evaluateActions = true;
		*/
	}
	
	public void BFS() {
		
		
		DeterministicPlanner planner = new BFS(domain, goalCondition, hashingFactory);
		planner.planFromState(initialState);
		
		//Policy p = new SDPlannerPolicy(planner);
		Policy p = new DDPlannerPolicy(planner);
		EpisodeAnalysis ea = p.evaluateBehavior(initialState, rf, tf);
		
		for (int i = 0; i < ea.numTimeSteps() - 1; i++) {
			System.out.println(ea.getState(i).toString());
			System.out.println(ea.getAction(i).toString());
		}
//		HandlerFMLEvents.actions = ea.getActionSequenceString().split("; ");
//		HandlerFMLEvents.actionsLeft = HandlerFMLEvents.actions.length;
//		HandlerFMLEvents.evaluateActions = true;
	}
	
		

	public static class BridgeRF implements RewardFunction {
		
		@Override
		public double reward(State s, GroundedAction a, State sprime) {
			
			//get location of agent in next state
			ObjectInstance agent = sprime.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
			int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
			int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
			int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);
			
			List<ObjectInstance> blocks = sprime.getObjectsOfClass(HelperNameSpace.CLASSBLOCK);
			for (ObjectInstance block : blocks) {
				if (HelperActions.blockIsOneOf(Block.getBlockById(block.getIntValForAttribute(HelperNameSpace.ATBTYPE)), HelperActions.dangerBlocks)) {
					int dangerX = block.getIntValForAttribute(HelperNameSpace.ATX);
					int dangerY = block.getIntValForAttribute(HelperNameSpace.ATY);
					int dangerZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
					if ((ax == dangerX) && (ay - 1 == dangerY) && (az == dangerZ) || (ax == dangerX) && (ay == dangerY) && (az == dangerZ)) {
						return -10.0;
					}
				} 
			}
			return -1.0;
		}
	}
	

	/**
	 * Find the gold block and return its pose. 
	 * @param s the state
	 * @return the pose of the agent being one unit above the gold block.
	 */
	public static Pose getGoalPose(State s) {
		List<ObjectInstance> blocks = s.getObjectsOfClass(HelperNameSpace.CLASSBLOCK);
		for (ObjectInstance block : blocks) {
			if (block.getIntValForAttribute(HelperNameSpace.ATBTYPE) == 41) {
				int goalX = block.getIntValForAttribute(HelperNameSpace.ATX);
				int goalY = block.getIntValForAttribute(HelperNameSpace.ATY);
				int goalZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
				
				return Pose.fromXyz(goalX,  goalY + 1,  goalZ);
			} 
		}
		return null;
	}

	public static class MovementTF implements TerminalFunction{
		
		@Override
		public boolean isTerminal(State s) {
			ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
			int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
			int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
			int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);
			
			List<ObjectInstance> blocks = s.getObjectsOfClass(HelperNameSpace.CLASSBLOCK);
			for (ObjectInstance block : blocks) {
				if (HelperActions.blockIsOneOf(Block.getBlockById(block.getIntValForAttribute(HelperNameSpace.ATBTYPE)), HelperActions.dangerBlocks)) {
					int dangerX = block.getIntValForAttribute(HelperNameSpace.ATX);
					int dangerY = block.getIntValForAttribute(HelperNameSpace.ATY);
					int dangerZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
					if ((ax == dangerX) && (ay - 1 == dangerY) && (az == dangerZ) || (ax == dangerX) && (ay == dangerY) && (az == dangerZ)) {
						return true;
					}
				} 
			}
					
			Pose agentPose = Pose.fromXyz(ax, ay, az);
			int rotDir = agent.getIntValForAttribute(HelperNameSpace.ATROTDIR);
			int vertDir = agent.getIntValForAttribute(HelperNameSpace.ATVERTDIR);
			
			Pose goalPose = getGoalPose(s);
			
			//are they at goal location or dead
			double distance = goalPose.distance(agentPose);
			//System.out.println("Distance: " + distance + " goal at: " + goalPose);
			
			if (goalPose.distance(agentPose) < 0.5) {
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	public class GoalCondition implements StateConditionTest {

		@Override
		public boolean satisfies(State s) {
			ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
			int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
			int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
			int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);
			
					
			Pose agentPose = Pose.fromXyz(ax, ay, az);
			int rotDir = agent.getIntValForAttribute(HelperNameSpace.ATROTDIR);
			int vertDir = agent.getIntValForAttribute(HelperNameSpace.ATVERTDIR);
			
			Pose goalPose = getGoalPose(s);
			
			//are they at goal location or dead
			double distance = goalPose.distance(agentPose);
			//System.out.println("Distance: " + distance + " goal at: " + goalPose);
			
			if (goalPose.distance(agentPose) < 0.5) {
				return true;
			} else {
				return false;
			}			
			
		}
		
	}
	
}
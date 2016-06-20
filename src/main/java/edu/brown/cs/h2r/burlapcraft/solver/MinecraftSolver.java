package edu.brown.cs.h2r.burlapcraft.solver;


import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.learning.modellearning.rmax.PotentialShapedRMax;
import burlap.behavior.singleagent.planning.Planner;
import burlap.behavior.singleagent.planning.deterministic.DDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner;
import burlap.behavior.singleagent.planning.deterministic.SDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.informed.Heuristic;
import burlap.behavior.singleagent.planning.deterministic.informed.astar.AStar;
import burlap.behavior.singleagent.planning.deterministic.uninformed.bfs.BFS;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.debugtools.MyTimer;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.GoldBlockTF;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.MinecraftDomainGenerator;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.environment.MinecraftEnvironment;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry;
import edu.brown.cs.h2r.burlapcraft.state.BCAgent;
import edu.brown.cs.h2r.burlapcraft.state.MinecraftStateGeneratorHelper;

import static edu.brown.cs.h2r.burlapcraft.domaingenerator.GoldBlockTF.getGoalPose;
import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.CLASS_AGENT;

/**
 * @author James MacGlashan.
 */
public class MinecraftSolver {

	static PotentialShapedRMax lastLearningAgent = null;
	static Dungeon lastDungeon;
	static SADomain lastDomain;


	static StateConditionTest gc = new TFGoalCondition(new GoldBlockTF());
	static MyTimer newTimer = new MyTimer();

	/**
	 * Runs planning for the current dungeon. Note that that the player needs to have first teleported to a dungeon
	 * before this will work.
	 * @param plannerToUse 0: BFS; 1: A*
	 * @param closedLoop if true then a closed loop policy will be followed; if false, then open loop.
	 */
	public static void plan(int plannerToUse, boolean closedLoop, boolean place){

		MinecraftDomainGenerator simdg = new MinecraftDomainGenerator();
		
		if (!place) {
			simdg.setActionWhiteListToNavigationAndDestroy();
		}
		
		SADomain domain = simdg.generateDomain();

		State initialState = MinecraftStateGeneratorHelper.getCurrentState(BurlapCraft.currentDungeon);

		DeterministicPlanner planner = null;
		if(plannerToUse == 0){
			planner = new BFS(domain, gc, new SimpleHashableStateFactory());

		}
		else if(plannerToUse == 1){
			Heuristic mdistHeuristic = new Heuristic() {
				
				@Override
				public double h(State s) {

					OOState os = (OOState)s;

					BCAgent a = (BCAgent)os.object(CLASS_AGENT);


					HelperGeometry.Pose goalPose = getGoalPose(s);
					
					int gx = (int) goalPose.getX();
					int gy = (int) goalPose.getY();
					int gz = (int) goalPose.getZ();
					
					//compute Manhattan distance
					double mdist = Math.abs(a.x-gx) + Math.abs(a.y-gy) + Math.abs(a.z-gz);
					
					return -mdist;
				}
			};
			planner = new AStar(domain, gc, new SimpleHashableStateFactory(), mdistHeuristic);
		}
		else{
			throw new RuntimeException("Error: planner type is " + planner + "; use 0 for BFS or 1 for A*");
		}
		
		planner.planFromState(initialState);

		Policy p = closedLoop ? new DDPlannerPolicy(planner) : new SDPlannerPolicy(planner);

		MinecraftEnvironment me = new MinecraftEnvironment();

		PolicyUtils.rollout(p, me);
		
	}
	
	public static void stocasticPlan(double gamma){

		MinecraftDomainGenerator simdg = new MinecraftDomainGenerator();
		
		SADomain domain = simdg.generateDomain();

		State initialState = MinecraftStateGeneratorHelper.getCurrentState(BurlapCraft.currentDungeon);
		
		Planner planner = new ValueIteration(domain, gamma, new SimpleHashableStateFactory(false), 0.001, 1000);
		
		Policy p = planner.planFromState(initialState);
		
		MinecraftEnvironment me = new MinecraftEnvironment();
		PolicyUtils.rollout(p, me);
	}

	public static void learn(){

		if(BurlapCraft.currentDungeon != lastDungeon || lastLearningAgent == null){
			MinecraftDomainGenerator mdg = new MinecraftDomainGenerator();
			mdg.setActionWhiteListToNavigationOnly();
			
			lastDomain = mdg.generateDomain();
			lastLearningAgent = new PotentialShapedRMax(lastDomain, 0.99, new SimpleHashableStateFactory(), 0, 1, 0.01, 200);
			lastDungeon = BurlapCraft.currentDungeon;
			
			System.out.println("Starting new RMax");
		}

		MinecraftEnvironment me = new MinecraftEnvironment();
		
		newTimer.start();
		lastLearningAgent.runLearningEpisode(me);
		newTimer.stop();
		
		System.out.println(newTimer.getTotalTime());


	}


}

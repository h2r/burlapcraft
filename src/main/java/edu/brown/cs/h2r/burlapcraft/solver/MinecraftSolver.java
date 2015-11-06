package edu.brown.cs.h2r.burlapcraft.solver;

import java.util.ArrayList;
import java.util.List;

import burlap.oomdp.singleagent.Action;
import net.minecraft.block.Block;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.modellearning.rmax.PotentialShapedRMax;
import burlap.oomdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.behavior.singleagent.planning.deterministic.DDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner;
import burlap.behavior.singleagent.planning.deterministic.SDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.informed.Heuristic;
import burlap.behavior.singleagent.planning.deterministic.informed.NullHeuristic;
import burlap.behavior.singleagent.planning.deterministic.informed.astar.AStar;
import burlap.behavior.singleagent.planning.deterministic.uninformed.bfs.BFS;
import burlap.debugtools.MyTimer;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.MinecraftDomainGenerator;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.environment.MinecraftEnvironment;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

/**
 * @author James MacGlashan.
 */
public class MinecraftSolver {

	static PotentialShapedRMax lastLearningAgent = null;
	static Dungeon lastDungeon;
	static Domain lastDomain;

	static RewardFunction rf = new GotoRF();
	static TerminalFunction tf = new GotoTF();
	static StateConditionTest gc = new GotoGoalCondition();
	static MyTimer newTimer = new MyTimer();

	/**
	 * Runs planning for the current dungeon. Note that that the player needs to have first teleported to a dungeon
	 * before this will work.
	 * @param plannerToUse 0: BFS; 1: A*
	 * @param closedLoop if true then a closed loop policy will be followed; if false, then open loop.
	 */
	public static void plan(int plannerToUse, boolean closedLoop, boolean place){

		int [][][] map = StateGenerator.getMap(BurlapCraft.currentDungeon);

		MinecraftDomainGenerator simdg = new MinecraftDomainGenerator(map);
		
		if (!place) {
			simdg.setActionWhiteListToNavigationAndDestroy();
		}
		
		Domain domain = simdg.generateDomain();

		State initialState = StateGenerator.getCurrentState(domain, BurlapCraft.currentDungeon);

		DeterministicPlanner planner = null;
		if(plannerToUse == 0){
			planner = new BFS(domain, gc, new SimpleHashableStateFactory(false));

		}
		else if(plannerToUse == 1){
			Heuristic mdistHeuristic = new Heuristic() {
				
				@Override
				public double h(State s) {
					
					ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
					int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
					int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
					int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);

					HelperGeometry.Pose goalPose = getGoalPose(s);
					
					int gx = (int) goalPose.getX();
					int gy = (int) goalPose.getY();
					int gz = (int) goalPose.getZ();
					
					//compute Manhattan distance
					double mdist = Math.abs(ax-gx) + Math.abs(ay-gy) + Math.abs(az-gz);
					
					return -mdist;
				}
			};
			planner = new AStar(domain, rf, gc, new SimpleHashableStateFactory(false), mdistHeuristic);
		}
		else{
			throw new RuntimeException("Error: planner type is " + planner + "; use 0 for BFS or 1 for A*");
		}
//		planner.setTf(tf);
		
		planner.planFromState(initialState);

		Policy p = closedLoop ? new DDPlannerPolicy(planner) : new SDPlannerPolicy(planner);

		MinecraftEnvironment me = new MinecraftEnvironment(domain);
		me.setTerminalFunction(tf);
		
		p.evaluateBehavior(me);
		
	}

	public static void learn(){

		if(BurlapCraft.currentDungeon != lastDungeon || lastLearningAgent == null){
			int [][][] map = StateGenerator.getMap(BurlapCraft.currentDungeon);
			MinecraftDomainGenerator mdg = new MinecraftDomainGenerator(map);
			mdg.setActionWhiteListToNavigationOnly();
			
			lastDomain = mdg.generateDomain();
			lastLearningAgent = new PotentialShapedRMax(lastDomain, 0.99, new SimpleHashableStateFactory(false), 0, 1, 0.01, 200);
			lastDungeon = BurlapCraft.currentDungeon;
			
			System.out.println("Starting new RMax");
		}

		State initialState = StateGenerator.getCurrentState(lastDomain, BurlapCraft.currentDungeon);
		MinecraftEnvironment me = new MinecraftEnvironment(lastDomain);
		me.setRewardFunction(rf);
		me.setTerminalFunction(tf);
		
		newTimer.start();
		lastLearningAgent.runLearningEpisode(me);
		newTimer.stop();
		
		System.out.println(newTimer.getTotalTime());


	}

	public static class GotoRF implements RewardFunction {

		@Override
		public double reward(State s, GroundedAction a, State sprime) {

			//get location of agent in next state
			ObjectInstance agent = sprime.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
			int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
			int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
			int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);

			return -1.0;
		}
	}


	/**
	 * Find the gold block and return its pose.
	 * @param s the state
	 * @return the pose of the agent being one unit above the gold block.
	 */
	public static HelperGeometry.Pose getGoalPose(State s) {
		List<ObjectInstance> blocks = s.getObjectsOfClass(HelperNameSpace.CLASSBLOCK);
		for (ObjectInstance block : blocks) {
			if (block.getIntValForAttribute(HelperNameSpace.ATBTYPE) == 41) {
				int goalX = block.getIntValForAttribute(HelperNameSpace.ATX);
				int goalY = block.getIntValForAttribute(HelperNameSpace.ATY);
				int goalZ = block.getIntValForAttribute(HelperNameSpace.ATZ);

				return HelperGeometry.Pose.fromXyz(goalX, goalY + 1, goalZ);
			}
		}
		return null;
	}

	public static class GotoTF implements TerminalFunction {

		@Override
		public boolean isTerminal(State s) {
			ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
			int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
			int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
			int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);

			HelperGeometry.Pose agentPose = HelperGeometry.Pose.fromXyz(ax, ay, az);
			int rotDir = agent.getIntValForAttribute(HelperNameSpace.ATROTDIR);
			int vertDir = agent.getIntValForAttribute(HelperNameSpace.ATVERTDIR);

			HelperGeometry.Pose goalPose = getGoalPose(s);

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

	public static class GotoGoalCondition implements StateConditionTest {

		@Override
		public boolean satisfies(State s) {
			ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
			int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
			int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
			int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);


			HelperGeometry.Pose agentPose = HelperGeometry.Pose.fromXyz(ax, ay, az);
			int rotDir = agent.getIntValForAttribute(HelperNameSpace.ATROTDIR);
			int vertDir = agent.getIntValForAttribute(HelperNameSpace.ATVERTDIR);

			HelperGeometry.Pose goalPose = getGoalPose(s);

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

package edu.brown.cs.h2r.burlapcraft.solver;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.learning.modellearning.rmax.PotentialShapedRMax;
import burlap.behavior.singleagent.planning.deterministic.DDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner;
import burlap.behavior.singleagent.planning.deterministic.SDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.informed.Heuristic;
import burlap.behavior.singleagent.planning.deterministic.informed.astar.AStar;
import burlap.behavior.singleagent.planning.deterministic.uninformed.bfs.BFS;
import burlap.debugtools.MyTimer;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.Domain;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.GroundedAction;
import burlap.mdp.singleagent.RewardFunction;
import burlap.mdp.statehashing.SimpleHashableStateFactory;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.MinecraftDomainGenerator;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.environment.MinecraftEnvironment;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCAgent;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCBlock;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

import java.util.List;

import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.CLASS_AGENT;

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


			return -1.0;
		}
	}


	/**
	 * Find the gold block and return its pose.
	 * @param s the state
	 * @return the pose of the agent being one unit above the gold block.
	 */
	public static HelperGeometry.Pose getGoalPose(State s) {

		OOState os = (OOState)s;

		List<ObjectInstance> blocks = os.objectsOfClass(HelperNameSpace.CLASS_BLOCK);
		for (ObjectInstance oblock : blocks) {
			BCBlock block = (BCBlock)oblock;
			if (block.type == 41) {
				int goalX = block.x;
				int goalY = block.y;
				int goalZ = block.z;

				return HelperGeometry.Pose.fromXyz(goalX, goalY + 1, goalZ);
			}
		}
		return null;
	}

	public static class GotoTF implements TerminalFunction {

		@Override
		public boolean isTerminal(State s) {
			OOState os = (OOState)s;

			BCAgent a = (BCAgent)os.object(CLASS_AGENT);

			HelperGeometry.Pose agentPose = HelperGeometry.Pose.fromXyz(a.x, a.y, a.z);
			int rotDir = a.rdir;
			int vertDir = a.vdir;

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

			OOState os = (OOState)s;

			BCAgent a = (BCAgent)os.object(CLASS_AGENT);



			HelperGeometry.Pose agentPose = HelperGeometry.Pose.fromXyz(a.x, a.y, a.z);

			HelperGeometry.Pose goalPose = getGoalPose(s);


			if (goalPose.distance(agentPose) < 0.5) {
				return true;
			} else {
				return false;
			}

		}

	}
}

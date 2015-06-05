package edu.brown.cs.h2r.burlapcraft.solver;

import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.modellearning.rmax.PotentialShapedRMax;
import burlap.behavior.singleagent.planning.StateConditionTest;
import burlap.behavior.singleagent.planning.deterministic.DDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner;
import burlap.behavior.singleagent.planning.deterministic.SDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.informed.NullHeuristic;
import burlap.behavior.singleagent.planning.deterministic.informed.astar.AStar;
import burlap.behavior.singleagent.planning.deterministic.uninformed.bfs.BFS;
import burlap.behavior.statehashing.DiscreteStateHashFactory;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorReal;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorSimulated;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import net.minecraft.block.Block;

import java.util.List;

/**
 * @author James MacGlashan.
 */
public class GoToSolver {

	static LearningAgent lastLearningAgent = null;
	static HelperNameSpace.DungeonEnum lastDungeon;
	static Domain lastDomain;

	static RewardFunction rf = new GotoRF();
	static TerminalFunction tf = new GotoTF();
	static StateConditionTest gc = new GotoGoalCondition();


	/**
	 * Runs planning for the current dungeon. Note that that the player needs to have first teleported to a dungeon
	 * before this will work.
	 * @param plannerToUse 0: BFS; 1: A*
	 * @param closedLoop if true then a closed loop policy will be followed; if false, then open loop.
	 */
	public static void plan(int plannerToUse, boolean closedLoop){

		int [][][] map = StateGenerator.getMap(BurlapCraft.currentDungeon);

		DomainGeneratorSimulated simdg = new DomainGeneratorSimulated(map);
		Domain domain = simdg.generateDomain();

		DomainGenerator realdg = new DomainGeneratorReal(map[0].length, map[0][0].length, map.length);
		Domain realDomain = realdg.generateDomain();

		State initialState = StateGenerator.getCurrentState(domain, BurlapCraft.currentDungeon);

		DeterministicPlanner planner = null;
		if(plannerToUse == 0){
			planner = new BFS(domain, gc, new DiscreteStateHashFactory());

		}
		else if(plannerToUse == 1){
			planner = new AStar(domain, rf, gc, new DiscreteStateHashFactory(), new NullHeuristic());
		}
		else{
			throw new RuntimeException("Error: planner type is " + planner + "; use 0 for BFS or 1 for A*");
		}
		planner.setTf(tf);


		planner.planFromState(initialState);
		Policy p = closedLoop ? new DDPlannerPolicy(planner) : new SDPlannerPolicy(planner);
		p.evaluateBehavior(initialState, rf, tf);

	}

	public static void learn(){

		if(BurlapCraft.dungeonID != lastDungeon || lastLearningAgent == null){
			int [][][] map = StateGenerator.getMap(BurlapCraft.currentDungeon);
			DomainGenerator realdg = new DomainGeneratorReal(map[0].length, map[0][0].length, map.length);
			lastDomain = realdg.generateDomain();
			lastLearningAgent = new PotentialShapedRMax(lastDomain, rf, tf, 0.99, new DiscreteStateHashFactory(), 0, 1, 0.01, 200);

			lastDungeon = BurlapCraft.dungeonID;
		}

		State initialState = StateGenerator.getCurrentState(lastDomain, BurlapCraft.currentDungeon);


	}





	public static class GotoRF implements RewardFunction {

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

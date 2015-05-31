package com.kcaluru.burlapcraft.solver;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.learning.modellearning.rmax.PotentialShapedRMax;
import burlap.behavior.singleagent.planning.StateConditionTest;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner;
import burlap.behavior.singleagent.planning.deterministic.SDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.TFGoalCondition;
import burlap.behavior.singleagent.planning.deterministic.informed.NullHeuristic;
import burlap.behavior.singleagent.planning.deterministic.informed.astar.AStar;
import burlap.behavior.singleagent.planning.deterministic.uninformed.bfs.BFS;
import burlap.behavior.statehashing.DiscreteStateHashFactory;
import burlap.domain.singleagent.gridworld.GridWorldStateParser;
import burlap.oomdp.auxiliary.StateParser;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.common.UniformCostRF;
import burlap.oomdp.singleagent.explorer.TerminalExplorer;

import com.kcaluru.burlapcraft.domaingenerator.DomainGeneratorReal;
import com.kcaluru.burlapcraft.domaingenerator.DomainGeneratorSimulated;
import com.kcaluru.burlapcraft.helper.HelperActions;
import com.kcaluru.burlapcraft.helper.HelperNameSpace;
import com.kcaluru.burlapcraft.helper.HelperPos;
import com.kcaluru.burlapcraft.solver.SolverLearningBridge.BridgeTF;
import com.kcaluru.burlapcraft.stategenerator.StateGenerator;

public class SolverPlanningBridge {

	DomainGeneratorSimulated 			dwdg;
	public Domain						domain;
	StateParser 				sp;
	RewardFunction 				rf;
	TerminalFunction			tf;
	StateConditionTest			goalCondition;
	public State 						initialState;
	DiscreteStateHashFactory	hashingFactory;
	
	private int[][][] map;
	private int[][] movementMap;
	private int length;
	private int width;
	private int height;
	
	public SolverPlanningBridge(int[][][] map) {
		
		// set the length, width and height of the dungeon
		this.length = map[0].length;
		this.width = map[0][0].length;
		this.height = map.length;
		
		//create the domain
		dwdg = new DomainGeneratorSimulated(map);

		domain = dwdg.generateDomain();
		
		//create the state parser
		sp = new GridWorldStateParser(domain); 
		
		//set up the initial state of the task
		rf = new BridgeRF();
		tf = new BridgeTF();
		goalCondition = new TFGoalCondition(tf);
		
		initialState = StateGenerator.getCurrentState(domain, 2);
		
		//set up the state hashing system
		hashingFactory = new DiscreteStateHashFactory();
	}
	
	public void ASTAR() {
		
		DeterministicPlanner planner = new AStar(domain, rf, goalCondition, hashingFactory, new NullHeuristic());
		planner.planFromState(initialState);
		
		Policy p = new SDPlannerPolicy(planner);
		
		EpisodeAnalysis ea = p.evaluateBehavior(initialState, rf, tf);
		
		for (int i = 0; i < ea.numTimeSteps() - 1; i++) {
			System.out.println(ea.getState(i).toString());
			System.out.println(ea.getAction(i).toString());
		}
		
	}
	
	public void BFS() {
		DeterministicPlanner planner = new BFS(domain, goalCondition, hashingFactory);
		planner.planFromState(initialState);
		
		Policy p = new SDPlannerPolicy(planner);
		
		EpisodeAnalysis ea = p.evaluateBehavior(initialState, rf, tf);
		
		for (int i = 0; i < ea.numTimeSteps() - 1; i++) {
			System.out.println(ea.getState(i).toString());
			System.out.println(ea.getAction(i).toString());
		}
	}
	
	public static class BridgeRF implements RewardFunction {
		
		@Override
		public double reward(State s, GroundedAction a, State sprime) {
			
			//get location of agent in next state
			ObjectInstance agent = sprime.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
			int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
			int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
			int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);
			
			List<ObjectInstance> blocks = sprime.getObjectsOfTrueClass(HelperNameSpace.CLASSBLOCK);
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
	
	public static class BridgeTF implements TerminalFunction{

		int goalX;
		int goalY;
		int goalZ;
		
		@Override
		public boolean isTerminal(State s) {
			
			//get location of agent in next state
			ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
			int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
			int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
			int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);
			int rotDir = agent.getIntValForAttribute(HelperNameSpace.ATROTDIR);
			int vertDir = agent.getIntValForAttribute(HelperNameSpace.ATVERTDIR);
			
			List<ObjectInstance> blocks = s.getObjectsOfTrueClass(HelperNameSpace.CLASSBLOCK);
			for (ObjectInstance block : blocks) {
				if (block.getIntValForAttribute(HelperNameSpace.ATBTYPE) == 41) {
					goalX = block.getIntValForAttribute(HelperNameSpace.ATX);
					goalY = block.getIntValForAttribute(HelperNameSpace.ATY);
					goalZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
				} 
			}
			
			//are they at goal location or dead?
			if(((ax == (this.goalX) && az == (this.goalZ - 1) && rotDir == 0 && vertDir == 1) || (ax == (this.goalX) && az == (this.goalZ + 1) && rotDir == 2 && vertDir == 1)
					|| (ax == (this.goalX - 1) && az == (this.goalZ) && rotDir == 3 && vertDir == 1) || (ax == (this.goalX + 1) && az == (this.goalZ) && rotDir == 1 && vertDir == 1)) 
					|| (HelperActions.getMinecraft().thePlayer.getHealth() == 0)  || (HelperActions.getMinecraft().thePlayer.isBurning())) {
				return true;
			}
			
			return false;
		}
		
	}
	
	
}

package com.kcaluru.burlapbot.solver;

import java.util.List;

import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.learning.modellearning.rmax.PotentialShapedRMax;
import burlap.behavior.singleagent.planning.StateConditionTest;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner;
import burlap.behavior.singleagent.planning.deterministic.SDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.TFGoalCondition;
import burlap.behavior.statehashing.DiscreteStateHashFactory;
import burlap.domain.singleagent.gridworld.GridWorldStateParser;
import burlap.oomdp.auxiliary.StateParser;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.common.UniformCostRF;
import burlap.behavior.singleagent.planning.deterministic.uninformed.bfs.BFS;

import com.kcaluru.burlapbot.domaingenerator.DomainGeneratorReal;
import com.kcaluru.burlapbot.domaingenerator.DomainGeneratorSimulated;
import com.kcaluru.burlapbot.helper.HelperActions;
import com.kcaluru.burlapbot.helper.HelperNameSpace;
import com.kcaluru.burlapbot.solver.SolverLearningFinder.MovementTF;
import com.kcaluru.burlapbot.stategenerator.StateGenerator;

public class SolverPlanningFinder {


	DomainGeneratorSimulated 			dwdg;
	public Domain						domain;
	StateParser 				sp;
	RewardFunction 				rf;
	TerminalFunction			tf;
	StateConditionTest			goalCondition;
	State 						initialState;
	DiscreteStateHashFactory	hashingFactory;
	
	private int[][][] map;
	private int[][] movementMap;
	private int length;
	private int width;
	private int height;
	
	public SolverPlanningFinder(int[][][] map) {
		
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
		rf = new UniformCostRF();
		tf = new MovementTF();
		goalCondition = new TFGoalCondition(tf);
		
		initialState = StateGenerator.getCurrentState(domain, 1);
		
		//set up the state hashing system
		hashingFactory = new DiscreteStateHashFactory();
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
	
	public static class MovementTF implements TerminalFunction{

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
			
			//are they at goal location or dead
			if(((ax == (this.goalX) && az == (this.goalZ - 1) && rotDir == 0 && vertDir == 1) || (ax == (this.goalX) && az == (this.goalZ + 1) && rotDir == 2 && vertDir == 1)
					|| (ax == (this.goalX - 1) && az == (this.goalZ) && rotDir == 3 && vertDir == 1) || (ax == (this.goalX + 1) && az == (this.goalZ) && rotDir == 1 && vertDir == 1)) ) {
				return true;
			}
			
			return false;
		}
		
	}
	
}

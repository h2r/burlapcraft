package edu.brown.cs.h2r.burlapcraft.solver;

import java.util.ArrayList;
import java.util.List;

import burlap.behavior.singleagent.learning.modellearning.DomainMappedPolicy;
import burlap.behavior.singleagent.planning.deterministic.DDPlannerPolicy;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
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
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorReal;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorSimulated;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerFMLEvents;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.Dungeon;
import edu.brown.cs.h2r.burlapcraft.solver.SolverLearningBridge.BridgeTF;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

public class SolverPlanningTinyBridge {

	DomainGeneratorSimulated 	dwdg;
	Domain						domain;
	//StateParser 				sp;
	RewardFunction 				rf;
	TerminalFunction			tf;
	StateConditionTest			goalCondition;
	State 						initialState;
	DiscreteStateHashFactory	hashingFactory;

	DomainGeneratorReal			rdg;
	Domain						realDomain;
	
	private int[][][] map;
	private int[][] movementMap;
	private int length;
	private int width;
	private int height;
	
	public SolverPlanningTinyBridge(int[][][] map) {
		
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
		//sp = new GridWorldStateParser(domain);
		
		//set up the initial state of the task
		rf = new BridgeRF();
		tf = new BridgeTF();
		goalCondition = new TFGoalCondition(tf);
		
		initialState = StateGenerator.getCurrentState(domain, Dungeon.TINY_BRIDGE);
		
		//set up the state hashing system
		hashingFactory = new DiscreteStateHashFactory();
	}
	
	public void ASTAR() {

		System.out.println("IN ASTAR WITH DYNAMIC");

		DeterministicPlanner planner = new AStar(domain, rf, goalCondition, hashingFactory, new NullHeuristic());
		planner.planFromState(initialState);



		Policy p = new DDPlannerPolicy(planner);
		DomainMappedPolicy rp = new DomainMappedPolicy( realDomain, p);
		rp.evaluateBehavior(initialState, rf, tf, 100);


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
		
		Policy p = new SDPlannerPolicy(planner);
		
		EpisodeAnalysis ea = p.evaluateBehavior(initialState, rf, tf);
		
		for (int i = 0; i < ea.numTimeSteps() - 1; i++) {
			System.out.println(ea.getState(i).toString());
			System.out.println(ea.getAction(i).toString());
		}
		HandlerFMLEvents.actions = ea.getActionSequenceString().split("; ");
		HandlerFMLEvents.actionsLeft = HandlerFMLEvents.actions.length;
		HandlerFMLEvents.evaluateActions = true;
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
			
			List<ObjectInstance> blocks = s.getObjectsOfClass(HelperNameSpace.CLASSBLOCK);
			for (ObjectInstance block : blocks) {
				if (block.getIntValForAttribute(HelperNameSpace.ATBTYPE) == 41) {
					goalX = block.getIntValForAttribute(HelperNameSpace.ATX);
					goalY = block.getIntValForAttribute(HelperNameSpace.ATY);
					goalZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
				} 
				if (HelperActions.blockIsOneOf(Block.getBlockById(block.getIntValForAttribute(HelperNameSpace.ATBTYPE)), HelperActions.dangerBlocks)) {
					int dangerX = block.getIntValForAttribute(HelperNameSpace.ATX);
					int dangerY = block.getIntValForAttribute(HelperNameSpace.ATY);
					int dangerZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
					if ((ax == dangerX) && (ay - 1 == dangerY) && (az == dangerZ) || (ax == dangerX) && (ay == dangerY) && (az == dangerZ)) {
						return true;
					}
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

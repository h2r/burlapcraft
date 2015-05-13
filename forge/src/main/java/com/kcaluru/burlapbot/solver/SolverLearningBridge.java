package com.kcaluru.burlapbot.solver;

import java.util.ArrayList;
import java.util.List;

import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.learning.modellearning.rmax.PotentialShapedRMax;
import burlap.behavior.singleagent.planning.StateConditionTest;
import burlap.behavior.singleagent.planning.deterministic.TFGoalCondition;
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

import com.kcaluru.burlapbot.domaingenerator.DomainGeneratorReal;
import com.kcaluru.burlapbot.helpers.BurlapAIHelper;
import com.kcaluru.burlapbot.helpers.NameSpace;
import com.kcaluru.burlapbot.helpers.Pos;
import com.kcaluru.burlapbot.solver.SolverLearningFinder.MovementTF;
import com.kcaluru.burlapbot.stategenerator.StateGenerator;

public class SolverLearningBridge {

	DomainGeneratorReal 			dwdg;
	public Domain						domain;
	StateParser 				sp;
	RewardFunction 				rf;
	TerminalFunction			tf;
	StateConditionTest			goalCondition;
	public State 						initialState;
	DiscreteStateHashFactory	hashingFactory;
	
	private int length;
	private int height;
	private int width;
	final int nConfident = 1;
	final double maxVIDelta = 0.1;
	final int maxVIPasses = 20;
	final double maxReward = 0;
	
	public SolverLearningBridge(int length, int width, int height) {
		
		// set the length, width and height of the dungeon
		this.length = length;
		this.width = width;
		this.height = height;
		
		//create the domain
		dwdg = new DomainGeneratorReal(length, width, height);

		domain = dwdg.generateDomain();
		
		//create the state parser
		sp = new GridWorldStateParser(domain); 
		
		//set up the initial state of the task
		rf = new UniformCostRF();
		tf = new BridgeTF();
		goalCondition = new TFGoalCondition(tf);
		
		initialState = StateGenerator.getCurrentState(domain, 2);
		
		//set up the state hashing system
		hashingFactory = new DiscreteStateHashFactory();
	}
	
	public void RMAX() {
		
		PotentialShapedRMax planner = new PotentialShapedRMax(domain, rf, tf, 0.95, hashingFactory, maxReward, nConfident, maxVIDelta, maxVIPasses);
		
		int maxSteps = 1;
		for (int i = 0; i < maxSteps; i++) {
			EpisodeAnalysis ea = planner.runLearningEpisodeFrom(initialState, 500);
		}
	
	}
	
	public static class BridgeRF implements RewardFunction {
		
		
		@Override
		public double reward(State s, GroundedAction a, State sprime) {
			
			//get location of agent in next state
			ObjectInstance agent = s.getFirstObjectOfClass(NameSpace.CLASSAGENT);
			int ax = agent.getDiscValForAttribute(NameSpace.ATX);
			int ay = agent.getDiscValForAttribute(NameSpace.ATY);
			int az = agent.getDiscValForAttribute(NameSpace.ATZ);
			int rotDir = agent.getDiscValForAttribute(NameSpace.ATROTDIR);
			int vertDir = agent.getDiscValForAttribute(NameSpace.ATVERTDIR);
			
			List<ObjectInstance> blocks = s.getObjectsOfTrueClass(NameSpace.CLASSBLOCK);
			List<Pos> dangerCoords = new ArrayList<Pos>();
			for (ObjectInstance block : blocks) {
				if (block.getDiscValForAttribute(NameSpace.ATBTYPE) == 11) {
					int dangerX = block.getDiscValForAttribute(NameSpace.ATX);
					int dangerY = block.getDiscValForAttribute(NameSpace.ATY);
					int dangerZ = block.getDiscValForAttribute(NameSpace.ATZ);
					dangerCoords.add(new Pos(dangerX, dangerY, dangerZ));
				} 
			}
			
			for (Pos lavaPos : dangerCoords) {
				if ((ax == lavaPos.x) && (ay - 1 == lavaPos.y) && (az == lavaPos.z)) {
					return -10.0;
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
			ObjectInstance agent = s.getFirstObjectOfClass(NameSpace.CLASSAGENT);
			int ax = agent.getDiscValForAttribute(NameSpace.ATX);
			int ay = agent.getDiscValForAttribute(NameSpace.ATY);
			int az = agent.getDiscValForAttribute(NameSpace.ATZ);
			int rotDir = agent.getDiscValForAttribute(NameSpace.ATROTDIR);
			int vertDir = agent.getDiscValForAttribute(NameSpace.ATVERTDIR);
			
			List<ObjectInstance> blocks = s.getObjectsOfTrueClass(NameSpace.CLASSBLOCK);
			for (ObjectInstance block : blocks) {
				if (block.getDiscValForAttribute(NameSpace.ATBTYPE) == 41) {
					goalX = block.getDiscValForAttribute(NameSpace.ATX);
					goalY = block.getDiscValForAttribute(NameSpace.ATY);
					goalZ = block.getDiscValForAttribute(NameSpace.ATZ);
				} 
			}
			
			//are they at goal location or dead?
			if(((ax == (this.goalX) && az == (this.goalZ - 1) && rotDir == 0 && vertDir == 1) || (ax == (this.goalX) && az == (this.goalZ + 1) && rotDir == 2 && vertDir == 1)
					|| (ax == (this.goalX - 1) && az == (this.goalZ) && rotDir == 3 && vertDir == 1) || (ax == (this.goalX + 1) && az == (this.goalZ) && rotDir == 1 && vertDir == 1)) 
					|| (BurlapAIHelper.getMinecraft().thePlayer.getHealth() == 0)  || (BurlapAIHelper.getMinecraft().thePlayer.isBurning())) {
				return true;
			}
			
			return false;
		}
		
	}
	
}

package edu.brown.cs.h2r.burlapcraft.solver;

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

import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorReal;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;
import edu.brown.cs.h2r.burlapcraft.solver.SolverLearningFinder.MovementTF;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

public class SolverLearningBridge {

	DomainGeneratorReal 		dwdg;
	public Domain				domain;
	StateParser 				sp;
	RewardFunction 				rf;
	TerminalFunction			tf;
	StateConditionTest			goalCondition;
	public State 				initialState;
	DiscreteStateHashFactory	hashingFactory;
	
	private int length;
	private int height;
	private int width;
	final int nConfident = 1;
	final double maxVIDelta = 0.1;
	final int maxVIPasses = 20;
	final double maxReward = 0;
	
	public SolverLearningBridge(int[][][] map) {
		
		// set the length, width and height of the dungeon
		this.length = map[0].length;
		this.width = map[0][0].length;
		this.height = map.length;
		
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
			ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
			int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
			int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
			int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);
			int rotDir = agent.getIntValForAttribute(HelperNameSpace.ATROTDIR);
			int vertDir = agent.getIntValForAttribute(HelperNameSpace.ATVERTDIR);
			
			List<ObjectInstance> blocks = s.getObjectsOfTrueClass(HelperNameSpace.CLASSBLOCK);
			List<HelperPos> dangerCoords = new ArrayList<HelperPos>();
			for (ObjectInstance block : blocks) {
				if (block.getIntValForAttribute(HelperNameSpace.ATBTYPE) == 11) {
					int dangerX = block.getIntValForAttribute(HelperNameSpace.ATX);
					int dangerY = block.getIntValForAttribute(HelperNameSpace.ATY);
					int dangerZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
					dangerCoords.add(new HelperPos(dangerX, dangerY, dangerZ));
				} 
			}
			
			for (HelperPos lavaPos : dangerCoords) {
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

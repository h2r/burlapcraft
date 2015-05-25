package com.kcaluru.burlapcraft.solver;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.kcaluru.burlapcraft.domaingenerator.DomainGeneratorReal;
import com.kcaluru.burlapcraft.handler.HandlerDungeonGeneration;
import com.kcaluru.burlapcraft.helper.HelperActions;
import com.kcaluru.burlapcraft.helper.HelperNameSpace;
import com.kcaluru.burlapcraft.item.ItemFinderWand;
import com.kcaluru.burlapcraft.stategenerator.StateGenerator;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.Policy.PolicyUndefinedException;
import burlap.behavior.singleagent.learning.modellearning.models.TabularModel;
import burlap.behavior.singleagent.learning.modellearning.rmax.PotentialShapedRMax;
import burlap.behavior.singleagent.planning.OOMDPPlanner;
import burlap.behavior.singleagent.planning.StateConditionTest;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner;
import burlap.behavior.singleagent.planning.deterministic.SDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.TFGoalCondition;
import burlap.behavior.singleagent.planning.deterministic.uninformed.bfs.BFS;
import burlap.behavior.statehashing.DiscreteStateHashFactory;
import burlap.domain.singleagent.gridworld.GridWorldDomain;
import burlap.domain.singleagent.gridworld.GridWorldStateParser;
import burlap.oomdp.auxiliary.StateParser;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.singleagent.Action;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.common.SinglePFTF;
import burlap.oomdp.singleagent.common.UniformCostRF;

public class SolverLearningFinder {

	DomainGeneratorReal 			dwdg;
	public Domain						domain;
	StateParser 				sp;
	RewardFunction 				rf;
	TerminalFunction			tf;
	StateConditionTest			goalCondition;
	State 						initialState;
	DiscreteStateHashFactory	hashingFactory;
	
	private int length;
	private int height;
	private int width;
	final int nConfident = 1;
	final double maxVIDelta = 0.1;
	final int maxVIPasses = 20;
	final double maxReward = 0;
	
	public SolverLearningFinder(int length, int width, int height) {
		
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
		tf = new MovementTF();
		goalCondition = new TFGoalCondition(tf);
		
		initialState = StateGenerator.getCurrentState(domain, 1);
		
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
					|| (ax == (this.goalX - 1) && az == (this.goalZ) && rotDir == 3 && vertDir == 1) || (ax == (this.goalX + 1) && az == (this.goalZ) && rotDir == 1 && vertDir == 1)) 
					|| (HelperActions.getMinecraft().thePlayer.getHealth() == 0) || (HelperActions.getMinecraft().thePlayer.isBurning())) {
				return true;
			}
			
			return false;
		}
		
	}
	
}
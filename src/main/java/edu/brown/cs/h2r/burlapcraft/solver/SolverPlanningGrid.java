package edu.brown.cs.h2r.burlapcraft.solver;

import java.util.List;

import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.learning.modellearning.DomainMappedPolicy;
import burlap.behavior.singleagent.planning.StateConditionTest;
import burlap.behavior.singleagent.planning.deterministic.DDPlannerPolicy;
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
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.common.UniformCostRF;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorReal;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorSimulated;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerFMLEvents;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.Dungeon;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

public class SolverPlanningGrid {


	DomainGeneratorSimulated 	dwdg;
	Domain						domain;
	StateParser 				sp;
	RewardFunction 				rf;
	TerminalFunction			tf;
	StateConditionTest			goalCondition;
	State 						initialState;
	DiscreteStateHashFactory	hashingFactory;

	DomainGeneratorReal rdg;
	Domain						realDomain;
	
	private int[][][] map;
	private int[][] movementMap;
	private int length;
	private int width;
	private int height;
	
	public SolverPlanningGrid(int[][][] map) {
		
		// set the length, width and height of the dungeon
		this.length = map[0].length;
		this.width = map[0][0].length;
		this.height = map.length;
		
		//create the domain
		dwdg = new DomainGeneratorSimulated(map);

		domain = dwdg.generateDomain();

		rdg = new DomainGeneratorReal(map[0].length, map[0][0].length, map.length);
		realDomain = rdg.generateDomain();
		
		//create the state parser
		sp = new GridWorldStateParser(domain); 
		
		//set up the initial state of the task
		rf = new UniformCostRF();
		tf = new MovementTF();
		goalCondition = new TFGoalCondition(tf);
		
		initialState = StateGenerator.getCurrentState(domain, Dungeon.GRID);
		
		//set up the state hashing system
		hashingFactory = new DiscreteStateHashFactory();
	}
	
	public void ASTAR() {

		System.out.println("****Beignning Grid AStar");

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
		
		//Policy p = new SDPlannerPolicy(planner);
		Policy p = new DDPlannerPolicy(planner);
		EpisodeAnalysis ea = p.evaluateBehavior(initialState, rf, tf);
		
		for (int i = 0; i < ea.numTimeSteps() - 1; i++) {
			System.out.println(ea.getState(i).toString());
			System.out.println(ea.getAction(i).toString());
		}
		HandlerFMLEvents.actions = ea.getActionSequenceString().split("; ");
		HandlerFMLEvents.actionsLeft = HandlerFMLEvents.actions.length;
		HandlerFMLEvents.evaluateActions = true;
	}
	
	public static class MovementTF implements TerminalFunction{

		/**
		 * Find the gold block and return its pose. 
		 * @param s the state
		 * @return the pose of the agent being one unit above the gold block.
		 */
		Pose getGoalPose(State s) {
			List<ObjectInstance> blocks = s.getObjectsOfClass(HelperNameSpace.CLASSBLOCK);
			for (ObjectInstance block : blocks) {
				if (block.getIntValForAttribute(HelperNameSpace.ATBTYPE) == 41) {
					int goalX = block.getIntValForAttribute(HelperNameSpace.ATX);
					int goalY = block.getIntValForAttribute(HelperNameSpace.ATY);
					int goalZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
					
					return Pose.fromXyz(goalX,  goalY + 1,  goalZ);
				} 
			}
			return null;
		}
		
		@Override
		public boolean isTerminal(State s) {
			
			//get location of agent in next state
			ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
			int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
			int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
			int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);
			Pose agentPose = Pose.fromXyz(ax, ay, az);
			int rotDir = agent.getIntValForAttribute(HelperNameSpace.ATROTDIR);
			int vertDir = agent.getIntValForAttribute(HelperNameSpace.ATVERTDIR);
			
			Pose goalPose = getGoalPose(s);
			
			//are they at goal location or dead
			double distance = goalPose.distance(agentPose);
			System.out.println("Distance: " + distance + " goal at: " + goalPose);
			
			if (goalPose.distance(agentPose) < 0.5) {
				return true;
			} else {
				return false;
			}
		}
		
	}
	
}

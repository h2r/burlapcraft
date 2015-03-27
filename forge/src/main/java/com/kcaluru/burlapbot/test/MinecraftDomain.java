package com.kcaluru.burlapbot.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Attribute.AttributeType;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.TransitionProbability;
import burlap.oomdp.singleagent.Action;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.singleagent.explorer.TerminalExplorer;
import burlap.oomdp.singleagent.explorer.VisualExplorer;
import burlap.oomdp.visualizer.ObjectPainter;
import burlap.oomdp.visualizer.StateRenderLayer;
import burlap.oomdp.visualizer.StaticPainter;
import burlap.oomdp.visualizer.Visualizer;

public class MinecraftDomain implements DomainGenerator {

	public static final String ATTX = "x";
	public static final String ATTZ = "z";

	public static final String CLASSAGENT = "agent";
	public static final String CLASSLOCATION = "location";
	
	public static final String ACTIONNORTH = "north";
	public static final String ACTIONSOUTH = "south";
	public static final String ACTIONEAST = "east";
	public static final String ACTIONWEST = "west";
	
	public static final String PFAT = "at";
	
	public static void main(String [] args){
		
		MinecraftDomain gen = new MinecraftDomain();
		Domain domain = gen.generateDomain();
		
		State initialState = MinecraftDomain.getExampleState(domain);
		
		TerminalExplorer exp = new TerminalExplorer(domain);
		exp.exploreFromState(initialState);
				
	}
	
	@Override
	public Domain generateDomain() {
		SADomain domain = new SADomain();
		
		Attribute xatt = new Attribute(domain, ATTX, AttributeType.INT);
		xatt.setLims(0, 15);
		
		Attribute zatt = new Attribute(domain, ATTZ, AttributeType.INT);
		zatt.setLims(0, 15);

		ObjectClass agentClass = new ObjectClass(domain, CLASSAGENT);
		agentClass.addAttribute(xatt);
		agentClass.addAttribute(zatt);
		
		ObjectClass locationClass = new ObjectClass(domain, CLASSLOCATION);
		locationClass.addAttribute(xatt);
		locationClass.addAttribute(zatt);
		
		new Movement(ACTIONEAST, domain, 0);
		new Movement(ACTIONWEST, domain, 1);
		new Movement(ACTIONNORTH, domain, 2);
		new Movement(ACTIONSOUTH, domain, 3);
		
		new AtLocation(domain);
		
		return domain;
	}
	
	public static State getExampleState(Domain domain){
		State s = new State();
		ObjectInstance agent = new ObjectInstance(domain.getObjectClass(CLASSAGENT), "agent0");
		agent.setValue(ATTX, 0);
		agent.setValue(ATTZ, 0);
		
		ObjectInstance location = new ObjectInstance(domain.getObjectClass(CLASSLOCATION), "location0");
		location.setValue(ATTX, 15);
		location.setValue(ATTZ, 15);
		
		s.addObject(agent);
		s.addObject(location);
		
		return s;
	}
	
	
	protected class Movement extends Action {

		//0: north; 1: south; 2:east; 3: west
		protected double [] directionProbs = new double[4];
		
		
		public Movement(String actionName, Domain domain, int direction) {
			super(actionName, domain, "");
			for(int i = 0; i < 4; i++){
				if(i == direction){
					directionProbs[i] = 0.8;
				}
				else{
					directionProbs[i] = 0.2/3.;
				}
			}
		}
		
		@Override
		protected State performActionHelper(State s, String[] params) {
			
			//get agent and current position
			ObjectInstance agent = s.getFirstObjectOfClass(CLASSAGENT);
			int curX = agent.getDiscValForAttribute(ATTX);
			int curZ = agent.getDiscValForAttribute(ATTZ);
			
			//sample directon with random roll
			double r = Math.random();
			double sumProb = 0.;
			int dir = 0;
			for(int i = 0; i < this.directionProbs.length; i++){
				sumProb += this.directionProbs[i];
				if(r < sumProb){
					dir = i;
					break; //found direction
				}
			}
			
			//get resulting position
			int[] newPos = this.moveResult(curX, curZ, dir);
			
			//set the new position
			agent.setValue(ATTX, newPos[0]);
			agent.setValue(ATTZ, newPos[1]);
			
			//return the state we just modified
			return s;
		}
		
		
		@Override
		public List<TransitionProbability> getTransitions(State s, String [] params){
			
			//get agent and current position
			ObjectInstance agent = s.getFirstObjectOfClass(CLASSAGENT);
			int curX = agent.getDiscValForAttribute(ATTX);
			int curZ = agent.getDiscValForAttribute(ATTZ);
			
			List<TransitionProbability> tps = new ArrayList<TransitionProbability>(4);
			TransitionProbability noChangeTransition = null;
			for(int i = 0; i < this.directionProbs.length; i++){
				int [] newPos = this.moveResult(curX, curZ, i);
				if(newPos[0] != curX || newPos[1] != curZ){
					//new possible outcome
					State ns = s.copy();
					ObjectInstance nagent = ns.getFirstObjectOfClass(CLASSAGENT);
					nagent.setValue(ATTX, newPos[0]);
					nagent.setValue(ATTZ, newPos[1]);
					
					//create transition probability object and add to our list of outcomes
					tps.add(new TransitionProbability(ns, this.directionProbs[i]));
				}
				else{
					//this direction didn't lead anywhere new
					//if there are existing possible directions
					//that wouldn't lead anywhere, aggregate with them
					if(noChangeTransition != null){
						noChangeTransition.p += this.directionProbs[i];
					}
					else{
						//otherwise create this new state and transition
						noChangeTransition = new TransitionProbability(s.copy(),
									 this.directionProbs[i]);
						tps.add(noChangeTransition);
					}
				}
			}
				
			return tps;
		}
		
		
		protected int [] moveResult(int curX, int curZ, int direction){
			
			//first get change in x and y from direction using 0: north; 1: south; 2:east; 3: west
			int xdelta = 0;
			int zdelta = 0;
			if(direction == 0){
				zdelta = 1;
			}
			else if(direction == 1){
				zdelta = -1;
			}
			else if(direction == 2){
				xdelta = 1;
			}
			else{
				xdelta = -1;
			}
			
			int nx = curX + xdelta;
			int nz = curZ + zdelta;
			
			int width = 16;
			int height = 16;
			
			//make sure new position is valid (not a wall or off bounds)
			if(nx < 0 || nx >= width || nz < 0 || nz >= height){
				nx = curX;
				nz = curZ;
			}
				
			
			return new int[]{nx,nz};
			
		}	
	}
	
	
	protected class AtLocation extends PropositionalFunction{

		public AtLocation(Domain domain){
			super(PFAT, domain, new String []{CLASSAGENT,CLASSLOCATION});
		}
		
		@Override
		public boolean isTrue(State s, String[] params) {
			ObjectInstance agent = s.getObject(params[0]);
			ObjectInstance location = s.getObject(params[1]);
			
			int ax = agent.getDiscValForAttribute(ATTX);
			int az = agent.getDiscValForAttribute(ATTZ);
			
			int lx = location.getDiscValForAttribute(ATTX);
			int lz = location.getDiscValForAttribute(ATTZ);
			
			return ax == lx && az == lz;
		}	
	}
	
	public static class ExampleRF implements RewardFunction{

		int goalX;
		int goalZ;
		
		public ExampleRF(int goalX, int goalZ){
			this.goalX = goalX;
			this.goalZ = goalZ;
		}
		
		@Override
		public double reward(State s, GroundedAction a, State sprime) {
			
			//get location of agent in current state
			ObjectInstance currAgent = sprime.getFirstObjectOfClass(CLASSAGENT);
			int cx = currAgent.getDiscValForAttribute(ATTX);
			int cz = currAgent.getDiscValForAttribute(ATTZ);
			
			//get location of agent in next state
			ObjectInstance nextAgent = sprime.getFirstObjectOfClass(CLASSAGENT);
			int nx = nextAgent.getDiscValForAttribute(ATTX);
			int nz = nextAgent.getDiscValForAttribute(ATTZ);
			
			//are they at goal location?
			if (nx == this.goalX && nz == this.goalZ){
				return 320.;
			}
			//did the agent move north or east
			else if (nx == cx+1 || nz == cz+1) {
				return 16.; 
			}
			
			return -1;
		}	
	}
	
	public static class ExampleTF implements TerminalFunction{

		int goalX;
		int goalZ;
		
		public ExampleTF(int goalX, int goalZ){
			this.goalX = goalX;
			this.goalZ = goalZ;
		}
		
		@Override
		public boolean isTerminal(State s) {
			
			//get location of agent in next state
			ObjectInstance agent = s.getFirstObjectOfClass(CLASSAGENT);
			int ax = agent.getDiscValForAttribute(ATTX);
			int az = agent.getDiscValForAttribute(ATTZ);
			
			//are they at goal location?
			if(ax == this.goalX && az == this.goalZ){
				return true;
			}
			
			return false;
		}	
	}

}

package com.kcaluru.burlapbot.domaingenerator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.RegistryNamespaced;
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

import com.kcaluru.burlapbot.helpers.BurlapAIHelper;
import com.kcaluru.burlapbot.helpers.NameSpace;

import cpw.mods.fml.common.registry.GameData;

/**
 * Class to generate burlap domain for minecraft
 * @author Krishna Aluru
 *
 */

public class DungeonWorldDomain implements DomainGenerator {
		
	protected int length;
	protected int width;
	protected int height;
	protected final int fixedFeetY;
	
	protected int [][][] map;
	protected int [][] movementMap = {
			{7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7},
			{7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{7,0,0,0,0,0,0,0,0,0,0,0,7,7,7,7},
			{7,7,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{7,7,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
			{7,7,0,0,0,0,0,0,7,0,0,7,7,0,0,7},
			{7,0,0,0,0,0,0,0,7,0,0,0,0,0,0,7},
			{7,0,0,0,0,0,0,0,7,0,0,0,0,0,0,7},
			{7,0,0,0,0,0,0,0,7,0,0,0,41,0,0,7},
			{7,0,0,7,7,0,0,0,7,0,0,0,0,0,0,7},
			{7,0,0,0,0,0,0,0,7,7,0,0,0,0,0,7},
			{7,0,0,0,0,0,0,0,7,7,7,7,7,0,0,7},
			{7,0,0,7,7,7,0,0,0,0,0,0,0,0,0,7},
			{7,0,0,7,0,0,0,0,0,0,0,0,0,0,0,7},
			{7,0,0,7,0,0,0,0,0,0,7,7,0,0,0,7},
			{7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7}
		};
	
	public DungeonWorldDomain(int [][][] map, int feetY) {
		this.setMaps(map);
		this.fixedFeetY = feetY;
	}
	
	public void setMaps(int [][][] map) {
		this.height = map.length;
		this.length = map[0].length;
		this.width = map[0][0].length;
		this.map = map;
	}
	
	@Override
	public Domain generateDomain() {
		
		SADomain domain = new SADomain();
		
		// Attributes
		// x-position attribute
		Attribute xAtt = new Attribute(domain, NameSpace.ATX, AttributeType.INT);
		xAtt.setLims(0, this.length - 1);
		// y-position attribute
		Attribute yAtt = new Attribute(domain, NameSpace.ATY, AttributeType.INT);
		yAtt.setLims(0, 3);
		// z-position attribute
		Attribute zAtt = new Attribute(domain, NameSpace.ATZ, AttributeType.INT);
		zAtt.setLims(0, this.width - 1);
		// block broken attribute
		Attribute isBroken = new Attribute(domain, NameSpace.ISBROKEN, AttributeType.BOOLEAN);
		
		// Object classes
		// agent
		ObjectClass agentClass = new ObjectClass(domain, NameSpace.CLASSAGENT);
		agentClass.addAttribute(xAtt);
		agentClass.addAttribute(yAtt);
		agentClass.addAttribute(zAtt);
		// location
		ObjectClass locationClass = new ObjectClass(domain, NameSpace.CLASSLOCATION);
		locationClass.addAttribute(xAtt);
		locationClass.addAttribute(yAtt);
		locationClass.addAttribute(zAtt);
		// blocks
//		ObjectClass blockClass = new ObjectClass(domain, NameSpace.CLASSBLOCK);
//		blockClass.addAttribute(xAtt);
//		blockClass.addAttribute(yAtt);
//		blockClass.addAttribute(zAtt);
//		blockClass.addAttribute(isBroken);
		
		
		// Actions
		new MovementAction(NameSpace.ACTIONSOUTH, domain, 0);
		new MovementAction(NameSpace.ACTIONWEST, domain, 1);
		new MovementAction(NameSpace.ACTIONNORTH, domain, 2);
		new MovementAction(NameSpace.ACTIONEAST, domain, 3);
//		new FaceAction(NameSpace.ACTIONFACE, domain);
		
		// Propositional Functions
		new AtLocationPF(NameSpace.PFATGOAL, domain, new String[]{NameSpace.CLASSAGENT, NameSpace.CLASSLOCATION});
		
		return domain;
		
	}
	
	public static State getInitialState(Domain domain, int startX, int startZ, int destX, int destZ) {
		State s = new State();
		ObjectInstance agent = new ObjectInstance(domain.getObjectClass(NameSpace.CLASSAGENT), "agent0");
		agent.setValue(NameSpace.ATX, startX);
		agent.setValue(NameSpace.ATY, 1);
		agent.setValue(NameSpace.ATZ, startZ);
		
		ObjectInstance location = new ObjectInstance(domain.getObjectClass(NameSpace.CLASSLOCATION), "location0");
		location.setValue(NameSpace.ATX, destX);
		location.setValue(NameSpace.ATY, 1);
		location.setValue(NameSpace.ATZ, destZ);
		s.addObject(agent);
		s.addObject(location);
		
		return s;
	}
	
	public class MovementAction extends Action {

		/**
		 * Probabilities of the actual direction the agent will go
		 * 0: south, 1: west, 2: north, 3: east 
		 */
		protected double [] directionProbs = new double[4];
		
		public MovementAction(String actionName, Domain domain, int direction) {
			super(actionName, domain, "");
			for(int i = 0; i < 4; i++) {
				if (i == direction) {
					directionProbs[i] = 0.9;
				}
				else {
					directionProbs[i] = 0.1/3.;
				}
			}
		}
		@Override
		protected State performActionHelper(State s, String[] params) {
			ObjectInstance agent = s.getFirstObjectOfClass(NameSpace.CLASSAGENT);
			int curX = agent.getDiscValForAttribute(NameSpace.ATX);
			int curZ = agent.getDiscValForAttribute(NameSpace.ATZ);
			
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
			int [] newPos = this.moveResult(curX, curZ, dir);
			
			//set the new position
			agent.setValue(NameSpace.ATX, newPos[0]);
			agent.setValue(NameSpace.ATY, 1);
			agent.setValue(NameSpace.ATZ, newPos[1]);
			
			//return the state we just modified
			return s;
		}
		
		protected int [] moveResult(int curX, int curZ, int direction) {
			int xdelta = 0;
			int zdelta = 0;
			if(direction == 0){
				zdelta = 1;
			}
			else if(direction == 1){
				xdelta = -1;
			}
			else if(direction == 2){
				zdelta = -1;
			}
			else{
				xdelta = 1;
			}
			
			int nx = curX + xdelta;
			int nz = curZ + zdelta;
			
			int length = DungeonWorldDomain.this.movementMap.length;
			int width = DungeonWorldDomain.this.movementMap[0].length;
			
			//make sure new position is valid (not a wall or out of bounds)
			if(nx < 0 || nx >= length || nz < 0 || nz >= width ||  
				DungeonWorldDomain.this.movementMap[nx][nz] >= 1){
				nx = curX;
				nz = curZ;
			}
				
			
			return new int[]{nx,nz};
		}
		
		@Override
		public List<TransitionProbability> getTransitions(State s, String [] params){
			
			//get agent and current position
			ObjectInstance agent = s.getFirstObjectOfClass(NameSpace.CLASSAGENT);
			int curX = agent.getDiscValForAttribute(NameSpace.ATX);
			int curZ = agent.getDiscValForAttribute(NameSpace.ATZ);
			
			List<TransitionProbability> tps = new ArrayList<TransitionProbability>(4);
			TransitionProbability noChangeTransition = null;
			for(int i = 0; i < this.directionProbs.length; i++){
				int [] newPos = this.moveResult(curX, curZ, i);
				if(newPos[0] != curX || newPos[1] != curZ) {
					//new possible outcome
					State ns = s.copy();
					ObjectInstance nagent = ns.getFirstObjectOfClass(NameSpace.CLASSAGENT);
					nagent.setValue(NameSpace.ATX, newPos[0]);
					nagent.setValue(NameSpace.ATZ, newPos[1]);
					
					//create transition probability object and add to our list of outcomes
					tps.add(new TransitionProbability(ns, this.directionProbs[i]));
				}
				else{
					//this direction didn't lead anywhere new
					//if there are existing possible directions that wouldn't lead anywhere, 
					//aggregate with them
					if(noChangeTransition != null){
						noChangeTransition.p += this.directionProbs[i];
					}
					else{
						//otherwise create this new state outcome
						noChangeTransition = new TransitionProbability(s.copy(), this.directionProbs[i]);
						tps.add(noChangeTransition);
					}
				}
			}
			
			
			return tps;
		}
		
	}	
	
	public class AtLocationPF extends PropositionalFunction {

		public AtLocationPF(String name, Domain domain, String[] parameterClasses) {
			super(name, domain, parameterClasses);
		}
		
		@Override
		public boolean isTrue(State s, String[] params) {
			ObjectInstance agent = s.getObject(params[0]);
			ObjectInstance location = s.getObject(params[1]);
			
			int ax = agent.getDiscValForAttribute(NameSpace.ATX);
			int az = agent.getDiscValForAttribute(NameSpace.ATZ);
			
			int lx = location.getDiscValForAttribute(NameSpace.ATX);
			int lz = location.getDiscValForAttribute(NameSpace.ATZ);
			
			return (ax == (lx - 1) && az == (lz - 1)) || (ax == (lx + 1) && az == (lz + 1)) || (ax == (lx - 1) && az == (lz + 1))
					|| (ax == (lx + 1) && az == (lz - 1)) || (ax == (lx - 1) && az == (lz)) || (ax == (lx + 1) && az == (lz)) 
					|| (ax == (lx) && az == (lz - 1)) || (ax == (lx) && az == (lz + 1));
		}
			
	}
		
//	public static void main(String [] args) {
//		
//		DungeonWorldDomain gen = new DungeonWorldDomain(finderMap, 1);
//		Domain domain = gen.generateDomain();
//		
//		State initialState = DungeonWorldDomain.getInitialState(domain, 7, 2, 8, 12);
//		
//		TerminalExplorer exp = new TerminalExplorer(domain);
//		exp.exploreFromState(initialState);
//	}
	
//	public static class MovementRF implements RewardFunction{
//
//		int goalX;
//		int goalZ;
//		
//		public MovementRF(int goalX, int goalZ) {
//			this.goalX = goalX;
//			this.goalZ = goalZ;
//		}
//		
//		@Override
//		public double reward(State s, GroundedAction a, State sprime) {
//			
//			//get location of agent in next state
//			ObjectInstance agent = sprime.getFirstObjectOfClass(NameSpace.CLASSAGENT);
//			int ax = agent.getDiscValForAttribute(NameSpace.ATX);
//			int az = agent.getDiscValForAttribute(NameSpace.ATZ);
//			
//			//are they at goal location?
//			if(ax == (this.goalX - 1) && az == (this.goalZ - 1) || ax == (this.goalX + 1) && az == (this.goalZ + 1) || ax == (this.goalX + 1) && az == (this.goalZ - 1)
//					|| ax == (this.goalX - 1) && az == (this.goalZ + 1) || ax == (this.goalX) && az == (this.goalZ - 1)
//					|| ax == (this.goalX) && az == (this.goalZ + 1) || ax == (this.goalX - 1) && az == (this.goalZ) || ax == (this.goalX + 1) && az == (this.goalZ)) {
//				return 100.;
//			}
//			
//			return -1;
//		}
//		
//		
//	}
//	
//	public static class MovementTF implements TerminalFunction{
//
//		int goalX;
//		int goalZ;
//		
//		public MovementTF(int goalX, int goalZ) {
//			this.goalX = goalX;
//			this.goalZ = goalZ;
//		}
//		
//		@Override
//		public boolean isTerminal(State s) {
//			
//			//get location of agent in next state
//			ObjectInstance agent = s.getFirstObjectOfClass(NameSpace.CLASSAGENT);
//			int ax = agent.getDiscValForAttribute(NameSpace.ATX);
//			int az = agent.getDiscValForAttribute(NameSpace.ATZ);
//			
//			//are they at goal location?
//			if(ax == (this.goalX - 1) && az == (this.goalZ - 1) || ax == (this.goalX + 1) && az == (this.goalZ + 1) || ax == (this.goalX + 1) && az == (this.goalZ - 1)
//					|| ax == (this.goalX - 1) && az == (this.goalZ + 1) || ax == (this.goalX) && az == (this.goalZ - 1)
//					|| ax == (this.goalX) && az == (this.goalZ + 1) || ax == (this.goalX - 1) && az == (this.goalZ) || ax == (this.goalX + 1) && az == (this.goalZ)) {
//				return true;
//			}
//			
//			return false;
//		}
//		
//	}

}

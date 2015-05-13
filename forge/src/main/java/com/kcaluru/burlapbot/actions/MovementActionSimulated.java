package com.kcaluru.burlapbot.actions;

import java.util.ArrayList;
import java.util.List;

import com.kcaluru.burlapbot.domaingenerator.DomainGeneratorSimulated;
import com.kcaluru.burlapbot.helpers.NameSpace;
import com.kcaluru.burlapbot.helpers.Pos;
import com.kcaluru.burlapbot.stategenerator.StateGenerator;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TransitionProbability;

public class MovementActionSimulated extends AgentActionSimulated {
	
	private int[][][] map;

	public MovementActionSimulated(String name, Domain domain, int[][][] map) {
		super(name, domain);
		this.map = map;
	}

	@Override
	State doAction(State s) {
		
		//get agent and current position
		ObjectInstance agent = s.getFirstObjectOfClass(NameSpace.CLASSAGENT);
		int curX = agent.getDiscValForAttribute(NameSpace.ATX);
		int curY = agent.getDiscValForAttribute(NameSpace.ATY);
		int curZ = agent.getDiscValForAttribute(NameSpace.ATZ);
		int rotDir = agent.getDiscValForAttribute(NameSpace.ATROTDIR);
		
		//get objects and their positions
		List<ObjectInstance> blocks = s.getObjectsOfTrueClass(NameSpace.CLASSBLOCK);
		List<Pos> coords = new ArrayList<Pos>();
		for (ObjectInstance block : blocks) {
			int blockX = block.getDiscValForAttribute(NameSpace.ATX);
			int blockY = block.getDiscValForAttribute(NameSpace.ATY);
			int blockZ = block.getDiscValForAttribute(NameSpace.ATZ);
			coords.add(new Pos(blockX, blockY, blockZ));
		}
		
		//get resulting position
		Pos newPos = this.moveResult(curX, curY, curZ, rotDir, coords);
		
		//set the new position
		agent.setValue(NameSpace.ATX, newPos.x);
		agent.setValue(NameSpace.ATY, newPos.y);
		agent.setValue(NameSpace.ATZ, newPos.z);
		
		//return the state we just modified
		return s;

	}
	
	protected Pos moveResult(int curX, int curY, int curZ, int direction, List<Pos> coords) {
		
		//first get change in x and z from direction using 0: south; 1: west; 2: north; 3: east
		int xdelta = 0;
		int zdelta = 0;
		if(direction == 0){
			zdelta = 1;
		}
		else if(direction == 1){
			xdelta = 1;
		}
		else if(direction == 2){
			zdelta = -1;
		}
		else{
			xdelta = -1;
		}
		
		int nx = curX + xdelta;
		int nz = curZ + zdelta;
		
		int length = this.map[curY].length;
		int width = this.map[curY][curX].length;
		
		//make sure new position is valid (not a wall or off bounds)
		if(nx < 0 || nx >= length || nz < 0 || nz >= width ||  
			map[curY][nx][nz] >= 1){
			nx = curX;
			nz = curZ;
		}
		
		for(Pos coord : coords) {
			if (nx == coord.x && curY == coord.y && nz == coord.z) {
				nx = curX;
				nz = curZ;
			}
		}
		
		return new Pos(nx, curY, nz);
		
	}
	
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		
		return this.deterministicTransition(s, params);
		
	}

}

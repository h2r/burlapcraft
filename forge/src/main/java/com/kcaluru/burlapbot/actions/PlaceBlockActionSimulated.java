package com.kcaluru.burlapbot.actions;

import java.util.List;

import com.kcaluru.burlapbot.helpers.NameSpace;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TransitionProbability;

public class PlaceBlockActionSimulated extends AgentActionSimulated {
	
	private int[][][] map;

	public PlaceBlockActionSimulated(String name, Domain domain, int[][][] map) {
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
		
		//get block objects and their positions
		List<ObjectInstance> blocks = s.getObjectsOfTrueClass(NameSpace.CLASSBLOCK);
		
		return s;
	}
	
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		
		return this.deterministicTransition(s, params);
		
	}

}

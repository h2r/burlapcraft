package com.kcaluru.burlapbot.actions;

import java.util.List;

import com.kcaluru.burlapbot.helpers.BurlapAIHelper;
import com.kcaluru.burlapbot.helpers.NameSpace;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TransitionProbability;

public class RotateVertActionSimulated extends AgentActionSimulated {

	private int vertDirection;
	
	public RotateVertActionSimulated(String name, Domain domain, int rotateVertDirection) {
		super(name, domain);
		this.vertDirection = rotateVertDirection;
	}

	@Override
	State doAction(State s) {
		
		//get agent
		ObjectInstance agent = s.getFirstObjectOfClass(NameSpace.CLASSAGENT);
		
		//set agent's vert dir
		agent.setValue(NameSpace.ATVERTDIR, this.vertDirection);
		
		//return the state we just modified
		return s;
		
	}
	
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		
		return this.deterministicTransition(s, params);
		
	}

}

package com.kcaluru.burlapbot.actions;

import java.util.List;

import com.kcaluru.burlapbot.helpers.BurlapAIHelper;
import com.kcaluru.burlapbot.helpers.NameSpace;
import com.kcaluru.burlapbot.helpers.Pos;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TransitionProbability;

public class RotateActionSimulated extends AgentActionSimulated {

	private int direction;
	
	public RotateActionSimulated(String name, Domain domain, int direction) {
		super(name, domain);
		this.direction = direction;
	}

	@Override
	State doAction(State s) {
		
		//get agent and current position
		ObjectInstance agent = s.getFirstObjectOfClass(NameSpace.CLASSAGENT);
		
		int rotDir = ((this.direction + agent.getDiscValForAttribute(NameSpace.ATROTDIR)) % 4);

		//set the new rotation direction
		agent.setValue(NameSpace.ATROTDIR, rotDir);
		
		//return the state we just modified
		return s;
		
	}
	
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		
		return this.deterministicTransition(s, params);
		
	}

}

package com.kcaluru.burlapcraft.action;

import java.util.List;

import com.kcaluru.burlapcraft.helper.HelperActions;
import com.kcaluru.burlapcraft.helper.HelperNameSpace;
import com.kcaluru.burlapcraft.helper.HelperPos;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TransitionProbability;

public class ActionChangeYawSimulated extends ActionAgentSimulated {

	private int direction;
	
	public ActionChangeYawSimulated(String name, Domain domain, int direction) {
		super(name, domain);
		this.direction = direction;
	}

	@Override
	State doAction(State s) {
		
		//get agent and current position
		ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
		
		int rotDir = ((this.direction + agent.getIntValForAttribute(HelperNameSpace.ATROTDIR)) % 4);

		//set the new rotation direction
		agent.setValue(HelperNameSpace.ATROTDIR, rotDir);
		
		//return the state we just modified
		return s;
		
	}
	
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		
		return this.deterministicTransition(s, params);
		
	}

}

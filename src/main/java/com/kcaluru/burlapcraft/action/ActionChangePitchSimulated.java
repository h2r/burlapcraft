package com.kcaluru.burlapcraft.action;

import java.util.List;

import com.kcaluru.burlapcraft.helper.HelperActions;
import com.kcaluru.burlapcraft.helper.HelperNameSpace;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TransitionProbability;

public class ActionChangePitchSimulated extends ActionAgentSimulated {

	private int vertDirection;
	
	public ActionChangePitchSimulated(String name, Domain domain, int rotateVertDirection) {
		super(name, domain);
		this.vertDirection = rotateVertDirection;
	}

	@Override
	State doAction(State s) {
		
		//get agent
		ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
		
		//set agent's vert dir
		agent.setValue(HelperNameSpace.ATVERTDIR, this.vertDirection);
		
		//return the state we just modified
		return s;
		
	}
	
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		
		return this.deterministicTransition(s, params);
		
	}

}

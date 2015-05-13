package com.kcaluru.burlapbot.actions;

import java.util.concurrent.TimeUnit;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.Action;

import com.kcaluru.burlapbot.domaingenerator.DomainGeneratorReal;
import com.kcaluru.burlapbot.stategenerator.StateGenerator;

public abstract class AgentActionSimulated extends Action {

	abstract State doAction(State state);
	
	/**
	 * 
	 * @param name
	 * @param domain
	 */
	public AgentActionSimulated(String name, Domain domain) {
		super(name, domain, "");
	}
	
	protected AgentActionSimulated getAction() {
		return this;
	}

	@Override
	protected State performActionHelper(State s, String[] params) {
		AgentActionSimulated action = this.getAction();
		
		State newState = action.doAction(s);
		
		return newState;
	}
	
}

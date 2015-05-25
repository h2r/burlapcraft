package com.kcaluru.burlapcraft.action;

import java.util.concurrent.TimeUnit;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.Action;

import com.kcaluru.burlapcraft.domaingenerator.DomainGeneratorReal;
import com.kcaluru.burlapcraft.stategenerator.StateGenerator;

public abstract class ActionAgentSimulated extends Action {

	abstract State doAction(State state);
	
	/**
	 * 
	 * @param name
	 * @param domain
	 */
	public ActionAgentSimulated(String name, Domain domain) {
		super(name, domain, "");
	}
	
	protected ActionAgentSimulated getAction() {
		return this;
	}

	@Override
	protected State performActionHelper(State s, String[] params) {
		ActionAgentSimulated action = this.getAction();
		
		State newState = action.doAction(s);
		
		return newState;
	}
	
}

package edu.brown.cs.h2r.burlapcraft.action;

import java.util.concurrent.TimeUnit;

import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorReal;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.Action;

public abstract class ActionAgentReal extends Action {
	
	abstract void doAction(State state);
	
	/**
	 * 
	 * @param name
	 * @param domain
	 */
	public ActionAgentReal(String name, Domain domain) {
		super(name, domain, "");
	}
	
	protected ActionAgentReal getAction() {
		return this;
	}

	@Override
	protected State performActionHelper(State s, String[] params) {
		ActionAgentReal action = this.getAction();
		
		System.out.println(action.toString());
		
		action.doAction(s);
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		State newState = StateGenerator.getCurrentState(domain, BurlapCraft.dungeonID);
		
		System.out.println(newState.toString());
		
		return newState;
	}

}

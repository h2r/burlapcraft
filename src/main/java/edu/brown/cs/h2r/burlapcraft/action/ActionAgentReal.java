package edu.brown.cs.h2r.burlapcraft.action;

import java.util.concurrent.TimeUnit;

import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorReal;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import burlap.debugtools.MyTimer;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.Action;

public abstract class ActionAgentReal extends Action {

	protected int sleepMS = 1000;

	abstract void doAction(State state);
	
	/**
	 * 
	 * @param name
	 * @param domain
	 */
	public ActionAgentReal(String name, Domain domain) {
		super(name, domain, "");
	}

	public ActionAgentReal(String name, Domain domain, int sleepMS) {
		super(name, domain, "");
		this.sleepMS = sleepMS;
	}

	public int getSleepMS() {
		return sleepMS;
	}

	public void setSleepMS(int sleepMS) {
		this.sleepMS = sleepMS;
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
			TimeUnit.MILLISECONDS.sleep(this.sleepMS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		State newState = StateGenerator.getCurrentState(domain, BurlapCraft.currentDungeon);
		
		//System.out.println(newState.toString());
		
		return newState;
	}

}

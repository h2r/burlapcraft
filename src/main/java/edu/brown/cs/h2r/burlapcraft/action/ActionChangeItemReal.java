package edu.brown.cs.h2r.burlapcraft.action;

import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;

public class ActionChangeItemReal extends ActionAgentReal {

	public ActionChangeItemReal(String name, Domain domain) {
		super(name, domain);
	}
	
	public ActionChangeItemReal(String name, Domain domain, int sleepMS) {
		super(name, domain, sleepMS);
	}

	@Override
	void doAction(State state) {
		HelperActions.changeItem();
	}

}

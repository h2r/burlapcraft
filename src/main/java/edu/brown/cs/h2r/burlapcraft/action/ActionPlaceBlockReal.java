package edu.brown.cs.h2r.burlapcraft.action;

import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;

public class ActionPlaceBlockReal extends ActionAgentReal {

	public ActionPlaceBlockReal(String name, Domain domain) {
		super(name, domain);
	}

	public ActionPlaceBlockReal(String name, Domain domain, int sleepMS) {
		super(name, domain, sleepMS);
	}

	@Override
	void doAction(State state) {
		
		System.out.println("Place Block");
		HelperActions.placeBlock();

	}

}

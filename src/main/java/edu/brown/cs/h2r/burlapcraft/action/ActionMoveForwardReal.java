package edu.brown.cs.h2r.burlapcraft.action;

import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.SADomain;

public class ActionMoveForwardReal extends ActionAgentReal {

	public ActionMoveForwardReal(String name, Domain domain) {
		
		super(name, domain);
		
	}

	public ActionMoveForwardReal(String name, Domain domain, int sleepMS) {

		super(name, domain, sleepMS);

	}

	@Override
	void doAction(State state) {
		
		System.out.println("Move Forward");
		HelperActions.moveForward(false);
		
	}

}

package com.kcaluru.burlapcraft.action;

import com.kcaluru.burlapcraft.helper.HelperActions;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;

public class ActionDestroyBlockReal extends ActionAgentReal {

	public ActionDestroyBlockReal(String name, Domain domain) {
		super(name, domain);
	}

	@Override
	void doAction(State state) {
		
		System.out.println("Destroy Block");
		HelperActions.destroyBlock();

	}

}

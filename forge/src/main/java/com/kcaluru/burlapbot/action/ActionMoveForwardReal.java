package com.kcaluru.burlapbot.action;

import com.kcaluru.burlapbot.helper.HelperActions;
import com.kcaluru.burlapbot.helper.HelperNameSpace;
import com.kcaluru.burlapbot.helper.HelperPos;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.SADomain;

public class ActionMoveForwardReal extends ActionAgentReal {

	public ActionMoveForwardReal(String name, Domain domain) {
		
		super(name, domain);
		
	}

	@Override
	void doAction(State state) {
		
		System.out.println("Move Forward");
		HelperActions.moveForward(false);
		
	}

}

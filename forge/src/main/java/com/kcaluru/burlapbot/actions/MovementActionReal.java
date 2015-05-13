package com.kcaluru.burlapbot.actions;

import com.kcaluru.burlapbot.helpers.BurlapAIHelper;
import com.kcaluru.burlapbot.helpers.NameSpace;
import com.kcaluru.burlapbot.helpers.Pos;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.SADomain;

public class MovementActionReal extends AgentActionReal {

	public MovementActionReal(String name, Domain domain) {
		
		super(name, domain);
		
	}

	@Override
	void doAction(State state) {
		
		System.out.println("Move Forward");
		BurlapAIHelper.moveForward(false);
		
	}

}

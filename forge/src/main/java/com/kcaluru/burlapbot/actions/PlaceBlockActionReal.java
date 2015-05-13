package com.kcaluru.burlapbot.actions;

import com.kcaluru.burlapbot.helpers.BurlapAIHelper;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;

public class PlaceBlockActionReal extends AgentActionReal {

	public PlaceBlockActionReal(String name, Domain domain) {
		super(name, domain);
	}

	@Override
	void doAction(State state) {
		
		System.out.println("Place Block");
		BurlapAIHelper.placeBlock();

	}

}

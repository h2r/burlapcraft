package com.kcaluru.burlapbot.actions;

import com.kcaluru.burlapbot.helpers.BurlapAIHelper;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;

public class PlaceBlockAction extends AgentAction {

	public PlaceBlockAction(String name, Domain domain) {
		super(name, domain);
	}

	@Override
	void doAction(State state) {
		
		System.out.println("Place Block");
		BurlapAIHelper.placeBlock();

	}

}

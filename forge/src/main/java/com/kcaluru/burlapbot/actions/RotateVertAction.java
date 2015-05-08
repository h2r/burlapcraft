package com.kcaluru.burlapbot.actions;

import com.kcaluru.burlapbot.helpers.BurlapAIHelper;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;

public class RotateVertAction extends AgentAction {
	
	private int vertDirection;

	public RotateVertAction(String name, Domain domain, int vertDirection) {
		
		super(name, domain);
		this.vertDirection = vertDirection;
		
	}

	@Override
	void doAction(State state) {
		
		switch(this.vertDirection) {
		case 0:
			BurlapAIHelper.faceAhead();
		case 1:
			BurlapAIHelper.faceDownOne();
		case 2:
			BurlapAIHelper.faceDownTwo();
		case 3:
			BurlapAIHelper.faceDownThree();
		default:
			break;
		}

	}

}

package com.kcaluru.burlapbot.actions;

import com.kcaluru.burlapbot.helpers.BurlapAIHelper;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;

public class RotateVertActionReal extends AgentActionReal {
	
	private int vertDirection;

	public RotateVertActionReal(String name, Domain domain, int rotateVertDirection) {
		
		super(name, domain);
		this.vertDirection = rotateVertDirection;
		
	}

	@Override
	void doAction(State state) {

		if (this.vertDirection == 1) {
			BurlapAIHelper.faceDownOne();
			System.out.println("Face Down One");
		}
		else {
			BurlapAIHelper.faceAhead();
			System.out.println("Face Ahead");
		}

	}

}

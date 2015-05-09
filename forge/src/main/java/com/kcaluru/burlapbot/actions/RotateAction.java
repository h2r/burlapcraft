package com.kcaluru.burlapbot.actions;

import com.kcaluru.burlapbot.helpers.BurlapAIHelper;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;

public class RotateAction extends AgentAction {

	private int direction;
	
	public RotateAction(String name, Domain domain, int direction) {
		
		super(name, domain);
		this.direction = direction;
		
	}

	@Override
	void doAction(State state) {
		
		switch (this.direction) {
		case 0:
			BurlapAIHelper.faceSouth();
			System.out.println("Face South");
		case 1:
			BurlapAIHelper.faceWest();
			System.out.println("Face West");
		case 2:
			BurlapAIHelper.faceNorth();
			System.out.println("Face North");
		case 3:
			BurlapAIHelper.faceEast();
			System.out.println("Face East");
		default:
			break;
		}
		
	}

}

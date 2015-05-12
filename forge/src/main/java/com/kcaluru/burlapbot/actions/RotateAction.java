package com.kcaluru.burlapbot.actions;

import com.kcaluru.burlapbot.helpers.BurlapAIHelper;
import com.kcaluru.burlapbot.helpers.NameSpace;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;

public class RotateAction extends AgentAction {

	private int direction;
	
	public RotateAction(String name, Domain domain, int direction) {
		
		super(name, domain);
		this.direction = direction;
		
	}

	@Override
	void doAction(State state) {
		
		ObjectInstance agent = state.getObjectsOfTrueClass(NameSpace.CLASSAGENT).get(0);
		
		int rotDir = ((this.direction + agent.getDiscValForAttribute(NameSpace.ATROTDIR)) % 4);
		
		System.out.println(rotDir);
		
		switch (rotDir) {
		case 0:
			System.out.println("Face South");
			BurlapAIHelper.faceSouth();
			break;
		case 1:
			System.out.println("Face West");
			BurlapAIHelper.faceWest();
			break;
		case 2:
			System.out.println("Face North");
			BurlapAIHelper.faceNorth();
			break;
		case 3:
			System.out.println("Face East");
			BurlapAIHelper.faceEast();
			break;
		default:
			break;
		}
		
	}

}

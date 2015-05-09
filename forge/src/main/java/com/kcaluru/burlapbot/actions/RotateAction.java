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
		
		int rotDir = (this.direction + agent.getDiscValForAttribute(NameSpace.ATROTDIR) % 4);
		
		if (rotDir == 0) {
			BurlapAIHelper.faceSouth();
			System.out.println("Face South");
		}
		else if (rotDir == 1) {
			BurlapAIHelper.faceWest();
			System.out.println("Face West");
		}
		else if (rotDir == 2) {
			BurlapAIHelper.faceNorth();
			System.out.println("Face North");
		}
		else {
			BurlapAIHelper.faceEast();
			System.out.println("Face East");
		}
		
	}

}

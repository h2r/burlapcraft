package edu.brown.cs.h2r.burlapcraft.action;

import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;

public class ActionChangeYawReal extends ActionAgentReal {

	private int direction;
	
	public ActionChangeYawReal(String name, Domain domain, int direction) {
		
		super(name, domain);
		this.direction = direction;
		
	}

	public ActionChangeYawReal(String name, Domain domain, int sleepMS, int direction) {

		super(name, domain, sleepMS);
		this.direction = direction;

	}

	@Override
	void doAction(State state) {
		
		ObjectInstance agent = state.getObjectsOfTrueClass(HelperNameSpace.CLASSAGENT).get(0);
		
		int rotDir = ((this.direction + agent.getIntValForAttribute(HelperNameSpace.ATROTDIR)) % 4);
		
		System.out.println(rotDir);
		
		switch (rotDir) {
		case 0:
			System.out.println("Face South");
			HelperActions.faceSouth();
			break;
		case 1:
			System.out.println("Face West");
			HelperActions.faceWest();
			break;
		case 2:
			System.out.println("Face North");
			HelperActions.faceNorth();
			break;
		case 3:
			System.out.println("Face East");
			HelperActions.faceEast();
			break;
		default:
			break;
		}
		
	}

}

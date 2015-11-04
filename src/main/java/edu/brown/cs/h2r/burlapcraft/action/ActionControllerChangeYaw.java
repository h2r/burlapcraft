package edu.brown.cs.h2r.burlapcraft.action;

import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.environment.Environment;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;

public class ActionControllerChangeYaw implements ActionController {

	protected int delayMS;
	protected Environment environment;
	protected int direction;
	
	public ActionControllerChangeYaw(int delayMS, Environment e, int d) {
		this.delayMS = delayMS;
		this.environment = e;
		this.direction = d;
	}

	@Override
	public int executeAction(GroundedAction ga) {
		
		ObjectInstance agent = this.environment.getCurrentObservation().getObjectsOfClass(HelperNameSpace.CLASSAGENT).get(0);
		
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
		
		return this.delayMS;
		
	}
	
}

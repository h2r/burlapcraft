package edu.brown.cs.h2r.burlapcraft.environment.controllers;


import burlap.mdp.core.Action;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.singleagent.environment.Environment;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCAgent;

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
	public int executeAction(Action a) {
		
		BCAgent agent = (BCAgent)((OOState)this.environment.currentObservation()).object(HelperNameSpace.CLASS_AGENT);
		
		int rotDir = ((this.direction + agent.rdir) % 4);
		
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

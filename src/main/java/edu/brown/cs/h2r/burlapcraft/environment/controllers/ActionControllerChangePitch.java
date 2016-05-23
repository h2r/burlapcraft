package edu.brown.cs.h2r.burlapcraft.environment.controllers;

import burlap.mdp.core.Action;
import burlap.mdp.singleagent.environment.Environment;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;


public class ActionControllerChangePitch implements ActionController {

	protected int delayMS;
	protected Environment environment;
	protected int direction;
	
	public ActionControllerChangePitch(int delayMS, Environment e, int d) {
		this.delayMS = delayMS;
		this.environment = e;
		this.direction = d;
	}
	
	@Override
	public int executeAction(Action a) {
		
		if (this.direction == 1) {
			System.out.println("Face Down One");
			HelperActions.faceDownOne();
		}
		else {
			System.out.println("Face Ahead");
			HelperActions.faceAhead();
		}
		
		return this.delayMS;
	}

	
	
}

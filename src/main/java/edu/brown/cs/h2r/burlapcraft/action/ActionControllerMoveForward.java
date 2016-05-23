package edu.brown.cs.h2r.burlapcraft.action;

import burlap.mdp.core.Action;
import burlap.mdp.singleagent.environment.Environment;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;


public class ActionControllerMoveForward implements ActionController {
	
	protected int delayMS;
	protected Environment environment;
	
	public ActionControllerMoveForward(int delayMS, Environment e) {
		this.delayMS = delayMS;
		this.environment = e;
	}

	@Override
	public int executeAction(Action ga) {
		
		System.out.println("Move Forward");
		HelperActions.moveForward(false);
		
		return this.delayMS;
		
	}

}

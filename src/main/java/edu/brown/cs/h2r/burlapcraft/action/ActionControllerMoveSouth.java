package edu.brown.cs.h2r.burlapcraft.action;

import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.environment.Environment;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;

public class ActionControllerMoveSouth implements ActionController {
	protected int delayMS;
	protected Environment environment;
	
	public ActionControllerMoveSouth(int delayMS, Environment e) {
		this.delayMS = delayMS;
		this.environment = e;
	}

	@Override
	public int executeAction(GroundedAction ga) {
		HelperActions.faceSouth();
		HelperActions.moveForward(false);

		return this.delayMS;
		
	}
}

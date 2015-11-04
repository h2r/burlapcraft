package edu.brown.cs.h2r.burlapcraft.action;

import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.environment.Environment;

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
	public int executeAction(GroundedAction ga) {
		
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

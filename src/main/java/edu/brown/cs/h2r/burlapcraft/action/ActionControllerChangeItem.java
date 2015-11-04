package edu.brown.cs.h2r.burlapcraft.action;

import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.environment.Environment;

public class ActionControllerChangeItem implements ActionController {

	protected int delayMS;
	protected Environment environment;
	
	public ActionControllerChangeItem(int delayMS, Environment e) {
		this.delayMS = delayMS;
		this.environment = e;
	}

	@Override
	public int executeAction(GroundedAction ga) {
		
		System.out.println("Change Item");
		HelperActions.changeItem();
		
		return this.delayMS;
	}
	
}

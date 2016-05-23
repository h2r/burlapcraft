package edu.brown.cs.h2r.burlapcraft.action;

import burlap.mdp.core.Action;
import burlap.mdp.singleagent.environment.Environment;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;


public class ActionControllerChangeItem implements ActionController {

	protected int delayMS;
	protected Environment environment;
	
	public ActionControllerChangeItem(int delayMS, Environment e) {
		this.delayMS = delayMS;
		this.environment = e;
	}

	@Override
	public int executeAction(Action a) {
		
		System.out.println("Change Item");
		HelperActions.changeItem();
		
		return this.delayMS;
	}
	
}

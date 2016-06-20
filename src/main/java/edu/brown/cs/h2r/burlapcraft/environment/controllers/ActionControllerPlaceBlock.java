package edu.brown.cs.h2r.burlapcraft.environment.controllers;


import burlap.mdp.core.action.Action;
import burlap.mdp.singleagent.environment.Environment;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;


public class ActionControllerPlaceBlock implements ActionController {

	protected int delayMS;
	protected Environment environment;
	
	public ActionControllerPlaceBlock(int delayMS, Environment e) {
		this.delayMS = delayMS;
		this.environment = e;
	}
	
	@Override
	public int executeAction(Action a) {
		
		System.out.println("Place Block");
		HelperActions.placeBlock();
		
		return this.delayMS;
	}

}

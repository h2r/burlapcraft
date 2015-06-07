package edu.brown.cs.h2r.burlapcraft.action;

import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;

public class ActionChangePitchReal extends ActionAgentReal {
	
	private int vertDirection;

	public ActionChangePitchReal(String name, Domain domain, int rotateVertDirection) {
		
		super(name, domain);
		this.vertDirection = rotateVertDirection;
		
	}

	public ActionChangePitchReal(String name, Domain domain, int sleepMS, int rotateVertDirection) {

		super(name, domain, sleepMS);
		this.vertDirection = rotateVertDirection;

	}

	@Override
	void doAction(State state) {

		if (this.vertDirection == 1) {
			HelperActions.faceDownOne();
			System.out.println("Face Down One");
		}
		else {
			HelperActions.faceAhead();
			System.out.println("Face Ahead");
		}

	}

}

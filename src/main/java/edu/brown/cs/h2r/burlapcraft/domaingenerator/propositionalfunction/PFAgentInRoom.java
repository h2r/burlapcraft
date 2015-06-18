package edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.State;

public class PFAgentInRoom extends PropositionalFunction {

	public PFAgentInRoom(String name, Domain domain, String[] parameterClasses) {
		super(name, domain, parameterClasses);

	}

	@Override
	public boolean isTrue(State s, String[] params) {
		// TODO Auto-generated method stub
		return false;
	}

}

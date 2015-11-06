package edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction;

import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.states.State;

public class PFRoomIsGreen extends PropositionalFunction {

	public PFRoomIsGreen(String name, Domain domain, String parameterClasses) {
		super(name, domain, parameterClasses);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isTrue(State s, String... params) {
		// TODO Auto-generated method stub
		ObjectInstance room = s.getObject(params[0]);
		if (room.getStringValForAttribute(HelperNameSpace.ATCOLOR).equals("green")) {
			return true;
		}
		
		return false;
	}

}

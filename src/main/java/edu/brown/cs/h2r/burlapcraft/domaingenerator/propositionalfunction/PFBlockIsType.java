package edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.states.State;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;

public class PFBlockIsType extends PropositionalFunction {

	protected int type;

	public PFBlockIsType(String name, Domain domain, String[] parameterClasses, int type) {
		super(name, domain, parameterClasses);
		this.type = type;
	}

	@Override
	public boolean isTrue(State s, String... params) {
		ObjectInstance block = s.getObject(params[0]);
		int blockType = block.getIntValForAttribute(HelperNameSpace.ATBTYPE);
		return blockType == this.type;
	}
	
}

package edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.states.State;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;

public class PFBlockIsColor extends PropositionalFunction {

	protected String color;

	public PFBlockIsColor(String name, Domain domain, String[] parameterClasses, String color) {
		super(name, domain, parameterClasses);
		this.color = color;
	}

	@Override
	public boolean isTrue(State s, String... params) {
		ObjectInstance block = s.getObject(params[0]);
		int blockType = block.getIntValForAttribute(HelperNameSpace.ATBTYPE);
		return HelperActions.blockColorMap.get(blockType).equals(this.color);
	}
}

package edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.states.State;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;

public class PFBlockIsShape extends PropositionalFunction {

	protected String shape;

	public PFBlockIsShape(String name, Domain domain, String[] parameterClasses, String shape) {
		super(name, domain, parameterClasses);
		this.shape = shape;
	}

	@Override
	public boolean isTrue(State s, String... params) {
		ObjectInstance block = s.getObject(params[0]);
		int blockType = block.getIntValForAttribute(HelperNameSpace.ATBTYPE);
		return HelperActions.blockShapeMap.get(blockType).equals(this.shape);
	}
	
}

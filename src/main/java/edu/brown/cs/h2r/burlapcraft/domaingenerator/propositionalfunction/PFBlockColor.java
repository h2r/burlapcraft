package edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.State;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;

/**
 * @author James MacGlashan.
 */
public class PFBlockColor extends PropositionalFunction {

	protected int blockType;

	public PFBlockColor(String name, Domain domain, int blockType) {
		super(name, domain, new String[]{HelperNameSpace.CLASSBLOCK});
		this.blockType = blockType;
	}

	@Override
	public boolean isTrue(State s, String[] params) {
		ObjectInstance block = s.getObject(params[0]);
		int blockType = block.getIntValForAttribute(HelperNameSpace.ATBTYPE);
		return blockType == this.blockType;
	}
}

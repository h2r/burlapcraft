package edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction;

import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.states.State;

public class PFAgentHasBlock extends PropositionalFunction {

	public PFAgentHasBlock(String name, Domain domain, String[] parameterClasses) {
		super(name, domain, parameterClasses);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isTrue(State s, String... params) {
		// TODO Auto-generated method stub
		ObjectInstance inventoryBlock = s.getObject(params[0]);
		ObjectInstance block = s.getObject(params[1]);
		
		if (inventoryBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE) == block.getIntValForAttribute(HelperNameSpace.ATBTYPE)) {
			return true;
		}
		
		return false;
	}

}

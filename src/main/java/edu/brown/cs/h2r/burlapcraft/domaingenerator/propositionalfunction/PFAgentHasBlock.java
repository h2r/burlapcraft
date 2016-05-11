package edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction;

import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;

public class PFAgentHasBlock extends PropositionalFunction {

	public PFAgentHasBlock(String name, OODomain domain, String[] parameterClasses) {
		super(name, domain, parameterClasses);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isTrue(OOState s, String... params) {
		// TODO Auto-generated method stub
//		ObjectInstance inventoryBlock = s.getObject(params[0]);
//		ObjectInstance block = s.getObject(params[1]);
//
//		if (inventoryBlock.getIntValForAttribute(HelperNameSpace.VAR_BT) == block.getIntValForAttribute(HelperNameSpace.VAR_BT)) {
//			return true;
//		}
		
		return false;
	}

}

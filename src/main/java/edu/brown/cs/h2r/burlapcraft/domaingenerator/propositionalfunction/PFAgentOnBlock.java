package edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction;

import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.states.State;

public class PFAgentOnBlock extends PropositionalFunction {

	public PFAgentOnBlock(String name, Domain domain, String[] parameterClasses) {
		super(name, domain, parameterClasses);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isTrue(State s, String... params) {
		// TODO Auto-generated method stub
		ObjectInstance agent = s.getObject(params[0]);
		ObjectInstance block = s.getObject(params[1]);
		int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
		int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
		int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);
		
		int bx = block.getIntValForAttribute(HelperNameSpace.ATX);
		int by = block.getIntValForAttribute(HelperNameSpace.ATY);
		int bz = block.getIntValForAttribute(HelperNameSpace.ATZ);
		
		if ((ax == bx) && ((by + 1) == ay) && az == bz) {
			return true;
		}
		
		return false;
	}
	
}

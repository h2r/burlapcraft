package edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction;

import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCAgent;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCBlock;


public class PFAgentOnBlock extends PropositionalFunction {

	public PFAgentOnBlock(String name, String[] parameterClasses) {
		super(name, parameterClasses);

	}

	@Override
	public boolean isTrue(OOState s, String... params) {
		BCAgent a = (BCAgent)s.object(params[0]);
		BCBlock b = (BCBlock)s.object(params[1]);

		if ((a.x == b.x) && ((b.y + 1) == a.y) && a.z == b.z) {
			return true;
		}
		
		return false;
	}
	
}

package edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction;


import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCBlock;

public class PFBlockIsType extends PropositionalFunction {

	protected int type;

	public PFBlockIsType(String name, OODomain domain, String[] parameterClasses, int type) {
		super(name, domain, parameterClasses);
		this.type = type;
	}

	@Override
	public boolean isTrue(OOState s, String... params) {
		BCBlock block = (BCBlock)s.object(params[0]);
		return block.type == this.type;
	}
	
}

package edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction;


import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCBlock;

public class PFBlockIsShape extends PropositionalFunction {

	protected String shape;

	public PFBlockIsShape(String name, OODomain domain, String[] parameterClasses, String shape) {
		super(name, domain, parameterClasses);
		this.shape = shape;
	}

	@Override
	public boolean isTrue(OOState s, String... params) {
		BCBlock block = (BCBlock)s.object(params[0]);
		return HelperActions.blockShapeMap.get(block.type).equals(this.shape);
	}
	
}

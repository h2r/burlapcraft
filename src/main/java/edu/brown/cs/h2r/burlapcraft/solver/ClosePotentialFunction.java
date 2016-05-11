package edu.brown.cs.h2r.burlapcraft.solver;

import burlap.behavior.singleagent.shaping.potential.PotentialFunction;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;

public class ClosePotentialFunction implements PotentialFunction {

	@Override
	public double potentialValue(State s) {
		ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
		ObjectInstance mob = s.getFirstObjectOfClass(HelperNameSpace.MOB);
		int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
		int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
		int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);
		int mx = mob.getIntValForAttribute(HelperNameSpace.ATX);
		int my = mob.getIntValForAttribute(HelperNameSpace.ATY);
		int mz = mob.getIntValForAttribute(HelperNameSpace.ATZ);

		double distance = Math.sqrt(Math.abs(ax - mx) + Math.abs(az - mz));

		if (distance == 0.0) {
			return 100;
		}
		return 100/distance;
	}

}

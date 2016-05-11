package edu.brown.cs.h2r.burlapcraft.solver;

import burlap.behavior.singleagent.learning.tdmethods.vfa.GradientDescentSarsaLam;
import burlap.behavior.singleagent.vfa.ValueFunctionApproximation;
import burlap.behavior.singleagent.vfa.cmac.CMACFeatureDatabase;
import burlap.oomdp.core.Domain;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;

public class Tile {
	public static GradientDescentSarsaLam getTileAgent(Domain domain) {
		int nTilings = 2;
		double resolution = 10.0;
		CMACFeatureDatabase cmac = new CMACFeatureDatabase(nTilings, CMACFeatureDatabase.TilingArrangement.RANDOMJITTER);
		cmac.addSpecificaitonForTiling(0, HelperNameSpace.POSITIONDELTA, domain.getAttribute(HelperNameSpace.XDELTA), FightSolver.COMBAT_DUNGEON_WIDTH/resolution);
		cmac.addSpecificaitonForTiling(1, HelperNameSpace.POSITIONDELTA, domain.getAttribute(HelperNameSpace.ZDELTA), FightSolver.COMBAT_DUNGEON_WIDTH/resolution);

		double defaultQ = 0.5;
		ValueFunctionApproximation vfa = cmac.generateVFA(defaultQ/nTilings);
		return new GradientDescentSarsaLam(domain, 0.99, vfa, 0.1, 0.5);
	}
}

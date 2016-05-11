package terminalfunctions;

import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import edu.brown.cs.h2r.burlapcraft.solver.FightSolver;

public class MobTerminalFunction implements TerminalFunction {	
	@Override
	public boolean isTerminal(State s) {
		if (FightSolver.isMobDead || FightSolver.isPlayerDead) {
			return true;
		}

		return false;
	}
}

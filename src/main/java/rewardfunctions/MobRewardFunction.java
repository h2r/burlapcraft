package rewardfunctions;

import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import edu.brown.cs.h2r.burlapcraft.solver.FightSolver;

public class MobRewardFunction implements RewardFunction {
	@Override
	public double reward(State s, GroundedAction a, State sprime) {		
		if (FightSolver.isPlayerDead) {
			return -100;
		}
		if (FightSolver.isMobDead) {
			return 100;
		}
		if (FightSolver.mob.getHealth() < FightSolver.previousMobHealth) {
			double healthDifference = FightSolver.previousMobHealth - FightSolver.mob.getHealth();
			FightSolver.previousMobHealth = FightSolver.mob.getHealth();			
			return healthDifference;
		}
		return -1;
	}
}

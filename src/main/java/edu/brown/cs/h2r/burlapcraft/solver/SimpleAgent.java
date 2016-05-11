package edu.brown.cs.h2r.burlapcraft.solver;

import java.util.List;

import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.valuefunction.QValue;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.Action;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.common.SimpleGroundedAction;
import burlap.oomdp.singleagent.environment.Environment;
import burlap.oomdp.singleagent.environment.EnvironmentOutcome;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;

public class SimpleAgent implements LearningAgent{

	Domain domain;

	public SimpleAgent(Domain domain) {
		this.domain = domain;
	}

	@Override
	public EpisodeAnalysis runLearningEpisode(Environment env) {
		return this.runLearningEpisode(env, -1);
	}

	@Override
	public EpisodeAnalysis runLearningEpisode(Environment env, int maxSteps) {
		EpisodeAnalysis ea = new EpisodeAnalysis(env.getCurrentObservation());
		State curState = env.getCurrentObservation();
		int steps = 0;
		while(!env.isInTerminalState() && (steps < maxSteps || maxSteps == -1)) {
			GroundedAction a = getSimpleAgentAction(curState);			
			EnvironmentOutcome eo = a.executeIn(env);
			ea.recordTransitionTo(a, eo.o, eo.r);
			
			curState = eo.op;
			steps++;
		}

		return ea;
	}

	private GroundedAction getSimpleAgentAction(State curState) {
		ObjectInstance agent = curState.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
		ObjectInstance mob = curState.getFirstObjectOfClass(HelperNameSpace.MOB);
		int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
		int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
		int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);
		int mx = mob.getIntValForAttribute(HelperNameSpace.ATX);
		int my = mob.getIntValForAttribute(HelperNameSpace.ATY);
		int mz = mob.getIntValForAttribute(HelperNameSpace.ATZ);

		int attackDistance = 0;
		if (FightSolver.CURRENT_MOB_NAME.equals("zombie")) {
			attackDistance = 3;
		} else if (FightSolver.CURRENT_MOB_NAME.equals("witch")) {
			attackDistance = 1;
		} else if (FightSolver.CURRENT_MOB_NAME.equals("spider")) {
			attackDistance = 1;
		} else if (FightSolver.CURRENT_MOB_NAME.equals("creeper")) {
			attackDistance = 1;
		} else if (FightSolver.CURRENT_MOB_NAME.equals("skeleton")) {
			attackDistance = 1;
		} else if (FightSolver.CURRENT_MOB_NAME.equals("blaze")) {
			attackDistance = 1;
		}

		if (Math.abs(ax - mx) <= attackDistance && Math.abs(az - mz) <= attackDistance) {
			return new SimpleGroundedAction(domain.getAction(HelperNameSpace.ATTACK));
		}

		if (Math.abs(ax - mx) > Math.abs(az - mz)) {
			if (ax > mx) {
				return new SimpleGroundedAction(domain.getAction(HelperNameSpace.ACTIONMOVEWEST));
			} else {
				return new SimpleGroundedAction(domain.getAction(HelperNameSpace.ACTIONMOVEEAST));
			}
		} else {
			if (az > mz) {
				return new SimpleGroundedAction(domain.getAction(HelperNameSpace.ACTIONMOVENORTH));
			} else {
				return new SimpleGroundedAction(domain.getAction(HelperNameSpace.ACTIONMOVESOUTH));
			}
		}
	}
}

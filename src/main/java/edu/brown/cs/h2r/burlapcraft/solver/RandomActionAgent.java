package edu.brown.cs.h2r.burlapcraft.solver;

import java.util.HashMap;
import java.util.List;

import burlap.behavior.policy.EpsilonGreedy;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.RandomPolicy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.valuefunction.QValue;
import burlap.behavior.valuefunction.ValueFunctionInitialization;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.environment.Environment;
import burlap.oomdp.singleagent.environment.EnvironmentOutcome;
import burlap.oomdp.statehashing.HashableState;
import burlap.oomdp.statehashing.HashableStateFactory;

public class RandomActionAgent implements LearningAgent{

	protected Policy learningPolicy;

	public RandomActionAgent(Domain domain) {
		this.learningPolicy = new RandomPolicy(domain);
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
			GroundedAction a = (GroundedAction) this.learningPolicy.getAction(curState);			
			EnvironmentOutcome eo = a.executeIn(env);
			ea.recordTransitionTo(a, eo.o, eo.r);
			
			curState = eo.op;
			steps++;
		}

		return ea;
	}
}

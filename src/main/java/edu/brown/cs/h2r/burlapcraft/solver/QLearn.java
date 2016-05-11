package edu.brown.cs.h2r.burlapcraft.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import burlap.behavior.policy.EpsilonGreedy;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.MDPSolver;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.valuefunction.QFunction;
import burlap.behavior.valuefunction.QValue;
import burlap.behavior.valuefunction.ValueFunctionInitialization;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.environment.Environment;
import burlap.oomdp.singleagent.environment.EnvironmentOutcome;
import burlap.oomdp.statehashing.HashableState;
import burlap.oomdp.statehashing.HashableStateFactory;

public class QLearn extends MDPSolver implements LearningAgent, QFunction {

	protected Map<HashableState, List<QValue>> qValues;
	protected ValueFunctionInitialization qinit;
	protected double learningRate;
	protected Policy learningPolicy;

	public QLearn(Domain domain, double gamma, HashableStateFactory hashingFactory, 
			ValueFunctionInitialization qinit, double learningRate, double epsilon) {
		this.solverInit(domain, null, null, gamma, hashingFactory);
		this.qinit = qinit;
		this.learningRate = learningRate;
		this.qValues = new HashMap<HashableState, List<QValue>>();
		this.learningPolicy = new EpsilonGreedy(this, epsilon);
	}

	@Override
	public double value(State s) {
		return QFunctionHelper.getOptimalValue(this, s);
	}

	@Override
	public List<QValue> getQs(State s) {
		HashableState hs = this.hashingFactory.hashState(s);
		List<QValue> qs = this.qValues.get(hs);
		if (qs == null) {
			List<GroundedAction> actions = this.getAllGroundedActions(s);
			qs = new ArrayList<QValue>(actions.size());
			for (GroundedAction ga : actions) {
				qs.add(new QValue(s, ga, this.qinit.qValue(s, ga)));
			}
			this.qValues.put(hs,  qs);
		}
		return qs;
	}

	@Override
	public QValue getQ(State s, AbstractGroundedAction a) {
		List<QValue> qs = this.getQs(s);
		for (QValue q : qs) {
			if (q.a.equals(a)) {
				return q;
			}
		}
		throw new RuntimeException("Could not find matching Q-value.");
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
			double maxQ = eo.terminated ? 0. : this.value(eo.op);
			QValue oldQ = this.getQ(curState, a);
			oldQ.q = oldQ.q + this.learningRate * (eo.r + this.gamma * maxQ - oldQ.q);
			curState = eo.op;
			steps++;
		}
		return ea;
	}

	@Override
	public void resetSolver() {
		this.qValues.clear();
	}
}

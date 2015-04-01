package minecraft.MinecraftBehavior.Planners;

import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.ValueFunctionInitialization;
import burlap.behavior.singleagent.ValueFunctionInitialization.ConstantValueFunctionInitialization;
import burlap.behavior.singleagent.planning.OOMDPPlanner;
import burlap.behavior.singleagent.planning.QComputablePlanner;
import burlap.behavior.singleagent.planning.commonpolicies.GreedyQPolicy;
import burlap.behavior.singleagent.planning.stochastic.rtdp.BoundedRTDP;
import burlap.behavior.singleagent.planning.stochastic.rtdp.RTDP;
import burlap.oomdp.core.State;
import minecraft.MinecraftBehavior.MinecraftBehavior;

public class BoundedRTDPPlanner extends MinecraftPlanner{
	private ValueFunctionInitialization lowerVInit;
	private ValueFunctionInitialization upperVInit;

	private int numRollouts;
	private double minDelta;
	private int maxDepth;
	private int numRolloutsWithSmallChangeToConverge;
	private int maxSteps;

	/**
	 * 
	 * @param mcBeh
	 * @param addOptions
	 * @param addMacroActions
	 */
	public BoundedRTDPPlanner(MinecraftBehavior mcBeh, boolean addOptions,
			boolean addMacroActions) {
		super(mcBeh, addOptions, addMacroActions);
//		System.out.println(mcBeh.vInit - 4);
		this.lowerVInit = new ConstantValueFunctionInitialization(mcBeh.lowerVInit);
		this.upperVInit = new ConstantValueFunctionInitialization(mcBeh.upperVInit);
		

		this.numRollouts = mcBeh.numRollouts;
		this.minDelta = mcBeh.minDelta;
		this.maxDepth = mcBeh.maxDepth;
		this.maxSteps = mcBeh.maxSteps;
	}

	@Override
	protected OOMDPPlanner getPlanner() {
		BoundedRTDP planner = new BoundedRTDP(domain, this.rf, this.tf, this.gamma, this.hashingFactory,
				this.lowerVInit, this.upperVInit, this.minDelta, this.numRollouts);
		return planner;
	}

	@Override
	protected double[] runPlannerHelper(OOMDPPlanner planner) {
		BoundedRTDP rPlanner = (BoundedRTDP) planner;
		
		long startTime = System.currentTimeMillis( );
		
		int bellmanUpdates = rPlanner.planFromStateAndCount(initialState);
		// Create a Q-greedy policy from the planner
		Policy p = new GreedyQPolicy((QComputablePlanner)planner);
		EpisodeAnalysis ea = p.evaluateBehavior(initialState, this.rf, this.tf, maxSteps);
		
		// Compute CPU time
		long totalPlanningTime  = System.currentTimeMillis( ) - startTime;
		
		// Count reward
		double totalReward = 0.;
		for(Double d : ea.rewardSequence){
			totalReward = totalReward + d;
		}
		
		// Check if task completed
		State finalState = ea.getState(ea.stateSequence.size() - 1);
		double completed = this.tf.isTerminal(finalState) ? 1.0 : 0.0;
		
//		System.out.println(ea.getActionSequenceString());

		double[] results = {bellmanUpdates, totalReward, completed, totalPlanningTime};

		return results;
	}
	
	

	

}

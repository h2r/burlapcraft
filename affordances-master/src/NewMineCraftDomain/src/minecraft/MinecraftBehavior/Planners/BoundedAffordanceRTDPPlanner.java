package minecraft.MinecraftBehavior.Planners;

import affordances.KnowledgeBase;
import burlap.behavior.affordances.AffordancesController;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.Policy;
import burlap.behavior.singleagent.ValueFunctionInitialization;
import burlap.behavior.singleagent.ValueFunctionInitialization.ConstantValueFunctionInitialization;
import burlap.behavior.singleagent.planning.OOMDPPlanner;
import burlap.behavior.singleagent.planning.QComputablePlanner;
import burlap.behavior.singleagent.planning.commonpolicies.AffordanceGreedyQPolicy;
import burlap.behavior.singleagent.planning.stochastic.rtdp.AffordanceBoundedRTDP;
import burlap.behavior.singleagent.planning.stochastic.rtdp.AffordanceRTDP;
import burlap.oomdp.core.State;
import minecraft.MinecraftBehavior.MinecraftBehavior;

public class BoundedAffordanceRTDPPlanner extends MinecraftPlanner {
	private ValueFunctionInitialization lowerVInit;
	private ValueFunctionInitialization upperVInit;
	private int numRollouts;
	private double minDelta;
	private int maxDepth;
	private int maxSteps;
	private AffordancesController affController;
	private KnowledgeBase affKB;

	/**
	 * 
	 * @param mcBeh
	 * @param addOptions
	 * @param addMacroActions
	 * @param kbPath
	 */
	public BoundedAffordanceRTDPPlanner(MinecraftBehavior mcBeh, boolean addOptions,
			boolean addMacroActions, KnowledgeBase affKB) {
		super(mcBeh, addOptions, addMacroActions);
		this.lowerVInit = new ConstantValueFunctionInitialization(mcBeh.lowerVInit);
		this.upperVInit = new ConstantValueFunctionInitialization(mcBeh.upperVInit);
		

		this.numRollouts = mcBeh.numRollouts;
		this.minDelta = mcBeh.minDelta;
		this.maxSteps = mcBeh.maxSteps;
		this.affKB = affKB;
		this.addOptionsAndMAsToOOMDPPlanner(this.getPlanner());
		
		this.affController = affKB.getAffordancesController();
		affController.setCurrentGoal(this.mcBeh.currentGoal); // Update goal to determine active affordances
	}

	@Override
	protected OOMDPPlanner getPlanner() {

		AffordanceBoundedRTDP planner = new AffordanceBoundedRTDP(domain, rf, tf, gamma, hashingFactory, lowerVInit, upperVInit, minDelta, numRollouts, affController);
		
		return planner;
	}

	@Override
	protected double[] runPlannerHelper(OOMDPPlanner planner) {
	
		AffordanceBoundedRTDP affPlanner = (AffordanceBoundedRTDP) planner;
		
		long startTime = System.currentTimeMillis( );
		
		int bellmanUpdates = affPlanner.planFromStateAndCount(initialState);

		// Create a Policy from the planner
		Policy p = new AffordanceGreedyQPolicy(this.affController, (QComputablePlanner)planner);
		EpisodeAnalysis ea = p.evaluateBehavior(this.initialState, this.rf, this.tf, this.maxSteps);
		
		// Compute CPU time
		long totalPlanningTime  = System.currentTimeMillis( ) - startTime;
		
		// Count reward.
		double totalReward = 0.;
		for(Double d : ea.rewardSequence){
			totalReward = totalReward + d;
		}
		
		// Check if task completed
		State finalState = ea.getState(ea.stateSequence.size() - 1);
		double completed = this.tf.isTerminal(finalState) ? 1.0 : 0.0;
		
		double[] results = {bellmanUpdates, totalReward, completed, totalPlanningTime};
		
		return results;
	}
	
	public void updateKB(KnowledgeBase affKB) {
		this.affKB = affKB;
		this.affController = affKB.getAffordancesController();
		affController.setCurrentGoal(this.mcBeh.currentGoal); // Update goal to determine active affordances
	}

}

package affordances;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import burlap.behavior.affordances.Affordance;
import burlap.behavior.affordances.AffordanceDelegate;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.GroundedProp;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.logicalexpressions.LogicalExpression;
import burlap.oomdp.logicalexpressions.PFAtom;
import minecraft.NameSpace;
import minecraft.MinecraftBehavior.MinecraftBehavior;
import minecraft.MinecraftDomain.PropositionalFunctions.AlwaysTruePF;

public class AffordanceUtils {
	
	/**
	 * Gets a list of free variables given an OOMDP object's parameter object classes and order groups
	 * @param orderGroups
	 * @param objectClasses
	 * @return: String[] - a list of free variables
	 */
	public static String[] makeFreeVarListFromObjectClasses(String[] objectClasses){
		List<String> groundedPropFreeVariablesList = new ArrayList<String>();
		
		// TODO: improve variable binding stuff
		// Make variables free
		for(String objectClass : objectClasses){
			String freeVar = "?" + objectClass.charAt(0);
			groundedPropFreeVariablesList.add(freeVar);
		}
		String[] groundedPropFreeVars = new String[groundedPropFreeVariablesList.size()];
		groundedPropFreeVars = groundedPropFreeVariablesList.toArray(groundedPropFreeVars);
		
		return groundedPropFreeVars;
	}

	/**
	 * Helper method that creates a PFAtom from a propositional function
	 * @param pf
	 * @return
	 */
	public static LogicalExpression pfAtomFromPropFunc(PropositionalFunction pf) {
		String[] pfFreeParams = makeFreeVarListFromObjectClasses(pf.getParameterClasses());
		GroundedProp blockGP = new GroundedProp(pf, pfFreeParams);
		return new PFAtom(blockGP);
	}
	
	private KnowledgeBase setupAlwaysTrueKB(MinecraftBehavior mcb, List<AbstractGroundedAction> allActions, Map<String,LogicalExpression> lgds) {
		PropositionalFunction alwaysTruePF = new AlwaysTruePF(NameSpace.PFALWAYSTRUE, mcb.getDomain(), new String[]{NameSpace.CLASSAGENT});
		LogicalExpression alwaysTrueLE = pfAtomFromPropFunc(alwaysTruePF); // For use in removing affordances that look too uniform
		KnowledgeBase alwaysTrueKB = new KnowledgeBase();
		for(LogicalExpression goal : lgds.values()) {
			Affordance aff = new Affordance(alwaysTrueLE, goal, allActions);
			AffordanceDelegate affD = new AffordanceDelegate(aff);
			alwaysTrueKB.add(affD);
		}
		
		return alwaysTrueKB;
	}
	
	/**
	 * Computes the entropy of each affordance and removes low-information (high entropy) affordances
	 */
	public KnowledgeBase removeLowInfoAffordances(KnowledgeBase affordanceKB, KnowledgeBase alwaysTrueKB) {
		List<AffordanceDelegate> toRemove = new ArrayList<AffordanceDelegate>();
		// Get counts for each affordance and queue zero count affs for removal
		for(AffordanceDelegate aff : affordanceKB.getAffordances()) {
			// Remove the always true predicate
			if(aff.getAffordance().preCondition.getClass().equals(AlwaysTruePF.class)) {
				toRemove.add(aff);
				continue;
			}
			List<Integer> counts = new ArrayList<Integer>(aff.getAffordance().getActionOptimalAffActiveCounts().values());

			// Remove if alpha counts are all 0
			double total = 0.0;
			for(Integer d : counts) {
				total += d;
			}
			if (total == 0.0) {				
				toRemove.add(aff);
				continue;
			}
			
			// Remove if variance is similar to uniform (less than @field this.lowVarianceThreshold)
			double[] multinomial = normalizeCounts(counts);

			if(isCloseToUniformAffordance(multinomial)) {
				toRemove.add(aff);
				continue;
			}
			
			// Remove if too similar to the AlwaysTrue affordance distribution (the true action distribution for the world)
			if(isTooSimilarToTrueActionDistr(aff, alwaysTrueKB)) {
				toRemove.add(aff);
				continue;
			}
		}
		
		// Actually remove (done separately to avoid modifying the iterable while looping)
		for(AffordanceDelegate affToRemove : toRemove) {
			affordanceKB.remove(affToRemove);
		}
		
		return affordanceKB;
		
	}
	
	/**
	 * Converts a list of counts to a multinomial distribution
	 * @param counts: the counts to inform the multinomial
	 * @return result (double[]): represents the multinomial distribution resulting from the counts given
	 */
	private double[] normalizeCounts(List<Integer> counts) {
		double[] result = new double[counts.size()];
		double total = 0.0;
		for(Integer i : counts) {
			total = total + i;
		}
		
		for(int i = 0; i < result.length; i++) {
			result[i] = counts.get(i) / total;
		}
		return result;
	}
	
	/**
	 * Determines if a given multinomial is statistically indistinguishable from the uniform distribution.
	 * @param multinomial
	 * @return
	 */
	private boolean isCloseToUniformAffordance(double[] multinomial) {
		// If there is a low variance, low information.
		Double variance = computeVariance(multinomial);
		double lowVarianceThreshold = 0.004;
		if(variance < lowVarianceThreshold) {
			return true;
		}
		return false;
	}
	
	/**
	 * Compares the given affordance to the always true affordance with the same goal (aff.g)
	 * and returns true if the mean square error between the two alpha counts are less than a
	 * particular threshold (defined in @field this.meanSquaredErrorThreshold
	 * @param aff
	 * @return
	 */
	private boolean isTooSimilarToTrueActionDistr(AffordanceDelegate aff, KnowledgeBase alwaysTrueKB) {
		// TODO: store always true KB in a way so we don't have to loop over and find goal matching aff
		AffordanceDelegate alwaysTrueAffToCompare = new AffordanceDelegate(null);
		for(AffordanceDelegate alwaysTrueAffDG : alwaysTrueKB.getAffordances()) {
			if(alwaysTrueAffDG.getAffordance().goalDescription.equals((aff.getAffordance().goalDescription))) {
				alwaysTrueAffToCompare = alwaysTrueAffDG;
			}
		}
		
		
		List<Integer> affordanceCounts = new ArrayList<Integer>(aff.getAffordance().getActionOptimalAffActiveCounts().values());
		double[] affToCompareDistribution = normalizeCounts(affordanceCounts);
		List<Integer> trueActionDistrCounts = new ArrayList<Integer>(alwaysTrueAffToCompare.getAffordance().getActionOptimalAffActiveCounts().values());
		double[] trueActionDistribution = normalizeCounts(trueActionDistrCounts);
		
		double sumSquared = 0;
		double meanSquaredError;
		
		for (int i = 0; i < affToCompareDistribution.length; ++i)
		{
	        sumSquared += Math.pow(affToCompareDistribution[i] - trueActionDistribution[i],2);
		}
		meanSquaredError = (double)sumSquared / affToCompareDistribution.length;
		double meanSquaredErrorThreshold = 0.004;
		if(meanSquaredError < meanSquaredErrorThreshold) {
			return true;
		}
		return false;
	}
	
	private static double computeVariance(double[] multinomial) {
		// Compute mean
		Double total = 0.0;
		for(int i = 0; i < multinomial.length; i++) {
			total += multinomial[i];
		}
		Double mean = total / multinomial.length;
		
		// Compute variance
		Double sumOfDifference = 0.0;
		for(int i = 0; i < multinomial.length; i++) {
			sumOfDifference += Math.pow((mean - multinomial[i]),2);
		}
		return sumOfDifference / (multinomial.length - 1);
	}

}

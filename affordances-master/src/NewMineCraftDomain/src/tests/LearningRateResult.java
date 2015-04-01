package tests;

import java.util.HashMap;

public class LearningRateResult {

	private String plannerName;	
	private HashMap<String, Result> results;

	/**
	 * Data structure for learning rate result objects
	 * @param plannerName: name of this planner
	 * @param maxKBSize: maximum size knowledge base to use with this planner
	 * @param learningIncrement: the amount to grow the knowledge base by
	 */
	public LearningRateResult(String plannerName) {
		this.plannerName = plannerName;
		this.results = new HashMap<String, Result>();
	}
	
	/**
	 * Adds a trial to a particular knowledge base size
	 * @param kbSize
	 * @param trial
	 */
	public void addTrialForKB(String kbSize, double[] trial) {
		// If this is not the first trial, update the existing result object
		if(this.results.containsKey(kbSize)) {
			Result result = this.results.get(kbSize);
			result.addTrial(trial);
			this.results.put(kbSize, result);
		}
		else {
			Result result = new Result(this.plannerName + "." + kbSize);
			result.addTrial(trial);
			this.results.put(kbSize, result);
		}
	}
	
	public HashMap<String, Result> getResults() {
		return this.results;
	}
	
	public void clear() {
		this.results = new HashMap<String,Result>();
	}

}

package tests;

import java.util.ArrayList;
import java.util.List;

import minecraft.NameSpace;

public class Result {

	private double avgBellmanUpdates = 0.0;
	private double avgReward = 0.0;
	private double completedRate = 0.0;
	private double avgCpuTime = 0.0;

	private List<Double> bellmanUpdateTrials = new ArrayList<Double>();
	private List<Double> rewardTrials = new ArrayList<Double>();
	private List<Double> completedTrials = new ArrayList<Double>();
	private List<Integer> cpuTrials = new ArrayList<Integer>();
	private int numTrials;
	
	private String plannerName;
	private double bellmanDeviation;
	private double rewardDeviation;
	private double cpuDeviation;
	
	public Result(String plannerName) {
		this.plannerName = plannerName;
	}
	
	/**
	 * Computes the average values given data across this.numTrials.
	 */
	private void computeAverages() {
		// If we haven't received any trials, return.
		if(numTrials == 0) {
			return;
		}
		if(numTrials == 1) {
			this.avgBellmanUpdates = this.bellmanUpdateTrials.get(0);
			this.avgReward = this.rewardTrials.get(0);
			this.avgCpuTime = this.cpuTrials.get(0);
			return;
		}
		
		double totalBellmanUpdates = 0.0;
		double totalReward = 0.0;
		double totalCpuTime = 0.0;
		double totalCompletedTrials = 0.0;
		for(int i = 0; i < this.numTrials; i++) {
			totalBellmanUpdates += this.bellmanUpdateTrials.get(i);
			totalReward += this.rewardTrials.get(i);
			totalCpuTime += this.cpuTrials.get(i);
			totalCompletedTrials += this.completedTrials.get(i);
		}

		this.avgBellmanUpdates = totalBellmanUpdates / this.numTrials;
		this.avgReward = totalReward / this.numTrials;
		this.completedRate = totalCompletedTrials / this.numTrials;
		this.avgCpuTime = (totalCpuTime / this.numTrials) / 1000.0; //convert from milliseconds to seconds
	}
	
	/**
	 * Computes the (sample) standard deviation of each data type
	 */
	private void computeDeviations() {
		// If we haven't received any trials, return.
		if(numTrials < 1) {
			return;
		}
		
		// If we only ran 1 trial, deviation is 0.
		if(this.numTrials == 1) {
			this.bellmanDeviation = 0.0;
			this.rewardDeviation = 0.0;
			this.cpuDeviation = 0.0;
			return;
		}
		

		double sumOfAvgDiffBellmanSqd = 0.0;
		double sumOfAvgDiffRewardSqd = 0.0;
		double sumOfAvgDiffCPUSqd = 0.0;
		for (int i = 0; i < this.numTrials; i++) {
			sumOfAvgDiffBellmanSqd += Math.pow((this.bellmanUpdateTrials.get(i) - this.avgBellmanUpdates),2);
			sumOfAvgDiffRewardSqd += Math.pow((this.rewardTrials.get(i) - this.avgReward),2);
			sumOfAvgDiffCPUSqd += Math.pow(((this.cpuTrials.get(i) / 1000.0) - this.avgCpuTime),2); // Divide by 1000 to convert from ms to seconds
		}
		this.bellmanDeviation = Math.sqrt(((double)sumOfAvgDiffBellmanSqd) / (this.numTrials - 1));
		this.rewardDeviation = Math.sqrt(((double)sumOfAvgDiffRewardSqd) / (this.numTrials - 1));
		this.cpuDeviation = Math.sqrt(((double)sumOfAvgDiffCPUSqd) / (this.numTrials - 1));
	}
	
	/**
	 * Adds a single trial to the result data and averages the totals.
	 * @param trial: a double list containing {bellmanUpdates, reward, completed, cpuTime}.
	 */
	public void addTrial(double[] trial) {
		
		// Only count bellmanUpdates, cpu, and reward if we succeeded on the map.
		this.bellmanUpdateTrials.add(trial[0]);
		this.rewardTrials.add(trial[1]);
		this.completedTrials.add(trial[2]);
		this.cpuTrials.add((int) trial[3]);
			
		++this.numTrials;
	}
	
	/**
	 * Clears all instance data.
	 */
	public void clear() {
		this.bellmanUpdateTrials = new ArrayList<Double>();
		this.rewardTrials = new ArrayList<Double>();
		this.completedTrials = new ArrayList<Double>();
		this.cpuTrials = new ArrayList<Integer>();
		
		this.numTrials = 0;
		this.avgBellmanUpdates = 0;
		this.avgReward = 0;
		this.completedRate = 0;
		this.avgCpuTime = 0;
	}
	
	// --- ACCESSORS ---
	
	public double getAvgBellmanUpdates() {
		return this.avgBellmanUpdates;
	}
	
	public double getAccumulatedReward() {
		return this.avgReward;
	}
	
	public double getCompletionRate() {
		return this.completedRate;
	}
	
	public double getAvgCpuTime() {
		return this.avgCpuTime;
	}
	
	public double getBellmanDeviation() {
		return this.bellmanDeviation;
	}
	
	public double getRewardDeviation() {
		return this.rewardDeviation;
	}
	
	public double getCPUDeviation() {
		return this.cpuDeviation;
	}
	
	/**
	 * Returns a string containing the results from every trial.
	 * @return
	 */
	public String getAllResults() {
		computeAverages();
		
		String bellman = "Bellman: ";
		String reward = "Reward: ";
		String completed = "Completed: ";
		String cpu = "CPU: ";
		for(int i = 0; i < this.numTrials; i++) {
			completed += String.format("%.2f", this.completedTrials.get(i)) + ",";
			reward += String.format("%.2f", this.rewardTrials.get(i)) + ",";
			bellman += String.format("%.2f", this.bellmanUpdateTrials.get(i)) + ",";
			cpu += (this.cpuTrials.get(i) / 1000.0) + "s,";
		}
		
		String result = plannerName + "\n" + bellman + "\n" + reward + "\n" + completed + "\n" + cpu + "\n";
		
		return result;
	}
	
	/**
	 * Returns a string containing the averages (with deviations) of the trials.
	 * @return
	 */
	public String getAverages() {
		return this.toString();
	}
	
	public String toString() {
		computeAverages();
		computeDeviations();
		String result = plannerName + " " + this.avgBellmanUpdates + ".(" 
		+ String.format(NameSpace.DOUBLEFORMAT, this.bellmanDeviation) + ") , " 
		+ String.format(NameSpace.DOUBLEFORMAT, this.avgReward) + ".(" 
		+ String.format(NameSpace.DOUBLEFORMAT, this.rewardDeviation) + ") , "  
		+ String.format(NameSpace.DOUBLEFORMAT, this.avgCpuTime) + "s.(" 
		+ String.format(NameSpace.DOUBLEFORMAT, this.cpuDeviation) + ") , "
		+ String.format(NameSpace.DOUBLEFORMAT, this.completedRate);
		
		return result;
	}

}

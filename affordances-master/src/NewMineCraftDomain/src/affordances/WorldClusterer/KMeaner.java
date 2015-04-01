package affordances.WorldClusterer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import minecraft.MapIO;
import burlap.oomdp.singleagent.GroundedAction;

public class KMeaner {
	
	private HashMap<MapIO, double[]> mapPositionInData;
	private HashMap<MapIO, Integer> clusterMembership;
	private HashMap<Integer, double[]> clusterCentroids;
	private List<GroundedAction> allActions;
	
	private Random rand;
		
	private int numClusters;
	
	/**
	 * @param normalizedActionCountsForWorlds hashMap from mapIOs to HM of actions to normalized counts
	 * @param numClusters
	 */
	public KMeaner(HashMap<MapIO, HashMap<GroundedAction, Double>> normalizedActionCountsForWorlds, int numClusters) {
		this.numClusters = numClusters;

		this.rand = new Random();
		this.allActions = allActions(normalizedActionCountsForWorlds);
		this.mapPositionInData = hashMapToArray(normalizedActionCountsForWorlds);
		System.out.println("Running k means...");
		try {
			runClustering();
			performRandomRestarts(100);
		} catch (NotEnoughMapsException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * runs k means
	 * @return the number of iterations required to converge
	 * @throws NotEnoughMapsException
	 */
	private int runClustering() throws NotEnoughMapsException {
		this.clusterMembership = new HashMap<MapIO, Integer>();
		this.clusterCentroids = new HashMap<Integer, double[]>();
		initializeClusters();
		boolean converged = false;
		int numIterations = 0;
		while(!converged) {
			converged = runOneIteration();
			numIterations += 1;
		}
		return numIterations;
	}
	
	/**
	 * @param numberOfRestarts
	 * @throws NotEnoughMapsException
	 */
	private void performRandomRestarts(int numberOfRestarts) throws NotEnoughMapsException {
		//Initialize data structures
		HashMap<MapIO, Integer> bestClusterMembership = this.clusterMembership;
		HashMap<Integer, double[]> bestClusterCentroids = this.clusterCentroids;
		double minLoss = getTotalClusteringLoss();
		//Do restarts
		for (int i = 0; i < numberOfRestarts; i++){
			runClustering();
			double currLoss = getTotalClusteringLoss();
			if (currLoss < minLoss) {
				minLoss = currLoss;
				bestClusterMembership = this.clusterMembership;
				bestClusterCentroids = this.clusterCentroids;
			}
		}
		//Select best one
		this.clusterMembership = bestClusterMembership;
		this.clusterCentroids = bestClusterCentroids;
	}
	
	/**
	 * Randomly assigns the first k clusters
	 * @throws NotEnoughMapsException
	 */
	private void initializeClusters() throws NotEnoughMapsException {
		HashSet<MapIO> usedMaps = new HashSet<MapIO>();
		if (this.numClusters > this.mapPositionInData.keySet().toArray().length) {
			throw new NotEnoughMapsException();
		}
		
		for(int clusterIndex = 0; clusterIndex < this.numClusters; clusterIndex++) {
			Object[] keys = this.mapPositionInData.keySet().toArray();
			int randomIndex = rand.nextInt(keys.length-1);
			MapIO randomMap = (MapIO) keys[randomIndex];
			while(usedMaps.contains(randomMap)) {
				randomIndex = rand.nextInt(keys.length);
				randomMap = (MapIO) keys[randomIndex];
			}
			
			this.clusterMembership.put(randomMap, clusterIndex);
			this.clusterCentroids.put(clusterIndex, this.mapPositionInData.get(randomMap));
			usedMaps.add(randomMap);
		}
	}
	
	/**
	 * Used when a cluster ends up empty
	 * @param clusterIndex
	 */
	private void randomizeCentroidForCluster(int clusterIndex) {
		Object[] keys = this.mapPositionInData.keySet().toArray();
		int randomIndex = rand.nextInt(keys.length-1);
		MapIO randomMap = (MapIO) keys[randomIndex];
		this.clusterMembership.put(randomMap, clusterIndex);
		this.clusterCentroids.put(clusterIndex, this.mapPositionInData.get(randomMap));
	}

	/**
	 * runs one iteration of k means
	 * @return whether or not k means has converged
	 */
	private boolean runOneIteration() {
		double oldLoss = this.getTotalClusteringLoss();
		
		//Update data points based on centroids
		updateClusterMembership();
		
		//Check for convergence
		if (oldLoss <= this.getTotalClusteringLoss()) {
			return true;
		}
		
		HashMap<Integer, Integer> numMembersOfCluster = getClusterMembershipCounts(this.clusterMembership);
		//Reinitialize empty clusters
		if(areEmptyClusters(numMembersOfCluster)) {
			reinitalizeEmptyClusters(numMembersOfCluster);
			return false;
		}
		
		//Update centroid
		updateCentroid(numMembersOfCluster);
		
		return false;
	}
	
	/**
	 * 
	 * @param numMembersOfCluster mapping from cluster index to num members of that cluster
	 */
	private void updateCentroid(HashMap<Integer, Integer> numMembersOfCluster){
		HashMap<Integer, double[]> newClusterCentroids = new HashMap<Integer, double[]> ();
		//by first sum up centroids member's data
		for(MapIO currMap : this.clusterMembership.keySet()) {
			int clusterMembership = this.clusterMembership.get(currMap);
			double[] dataForMap = this.mapPositionInData.get(currMap);
			double [] totalDataForCluster = newClusterCentroids.get(clusterMembership);
			//Add
			if (totalDataForCluster != null) {
				dataForMap = addDoubleVectors(dataForMap, totalDataForCluster);
			}
			newClusterCentroids.put(clusterMembership, dataForMap);

		}
		//then dividing by number of members
		for(int clusterIndex = 0; clusterIndex < this.numClusters; clusterIndex++) {
			double[] totalDataForCluster = newClusterCentroids.get(clusterIndex);
			int totalMembers = numMembersOfCluster.get(clusterIndex);
			totalDataForCluster = divideDoubleVectorBy(totalDataForCluster, totalMembers);
			newClusterCentroids.put(clusterIndex, totalDataForCluster);
		}
		
		this.clusterCentroids = newClusterCentroids;
	}
	
	/**
	 * Updates the clusters that maps belong to
	 */
	private void updateClusterMembership(){
		HashMap<MapIO, Integer> newClusterMembership = new HashMap<MapIO, Integer>();
		for(MapIO currMap : this.mapPositionInData.keySet()) {
			double[] mapData = this.mapPositionInData.get(currMap);
			Integer bestCluster = getBestFittingCentroidFromData(mapData);
			newClusterMembership.put(currMap, bestCluster);
			Integer oldClusterMembership = this.clusterMembership.get(currMap);
			if (oldClusterMembership == null) {
				oldClusterMembership = -1;
			}
		}
		this.clusterMembership = newClusterMembership;
	}
	
	/**
	 * 
	 * @param numMembersOfCluster mapping from cluster index to num members of that cluster
	 */
	private void reinitalizeEmptyClusters(HashMap<Integer, Integer> numMembersOfCluster) {
		//Get empty clusters
		List<Integer> emptyClusters = new ArrayList<Integer>();
		for(int clusterIndex = 0; clusterIndex < this.numClusters; clusterIndex++) {
			Integer totalMembers = numMembersOfCluster.get(clusterIndex);
			if (totalMembers == null) {
				emptyClusters.add(clusterIndex);
			}
		} 
		//Reinitalize those
		for (Integer emptyClusterIndex : emptyClusters) {
			randomizeCentroidForCluster(emptyClusterIndex);
		}
	}
	
	/**
	 * 
	 * @return mapping from cluster to number of members of that cluster
	 */
	private HashMap<Integer, Integer> getClusterMembershipCounts(HashMap<MapIO, Integer> clusterMembership){
		HashMap<Integer, Integer> toReturn = new HashMap<Integer, Integer>();//From cluster index to number of members of that cluster
		//by first sum up centroids member's data
		for(MapIO currMap : clusterMembership.keySet()) {
			int currMapClusterMembership = clusterMembership.get(currMap);
			//Update number of  members of cluster
			Integer oldMembers = toReturn.get(currMapClusterMembership);
			
			if (oldMembers == null) {
				oldMembers = 0;
			}
			toReturn.put(currMapClusterMembership, oldMembers+1);
		}
		return toReturn;
	}
	
	/**
	 * 
	 * @param numMembersOfCluster mapping from cluster to number of members of that cluster
	 * @return if there is an empty cluster
	 */
	private boolean areEmptyClusters(HashMap<Integer, Integer> numMembersOfCluster) {
		for (int clusterIndex = 0; clusterIndex < this.numClusters; clusterIndex++) {
			Integer numMembers = numMembersOfCluster.get(clusterIndex);
			if (numMembers == null || numMembers == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param vec1
	 * @param vec2
	 * @return a vector of the input vectors added together
	 */
	private double[] addDoubleVectors(double [] vec1, double[] vec2) {
		assert(vec1.length == vec2.length);
		double[] toReturn = new double[vec1.length];
		
		for(int i = 0; i < vec1.length; i++) {
			toReturn[i] = vec1[i] + vec2[i];
		}
		
		return toReturn;
	}
	
	/**
	 * 
	 * @param vector
	 * @param divideBy
	 * @return the input vector divided by divideBy
	 */
	private double[] divideDoubleVectorBy(double[] vector, int divideBy) {
		double[] toReturn = new double[vector.length];
		
		for(int i = 0; i < vector.length; i++) {
			toReturn[i] = vector[i] /(double) divideBy;
		}
		return toReturn;
	}
	
	/**
	 * 
	 * @return the current loss
	 */
	private double getTotalClusteringLoss() {
		double toReturn = 0;
		for (MapIO currMap : this.clusterMembership.keySet()) {
			int centroidIndex = this.clusterMembership.get(currMap);
			double [] mapData = this.mapPositionInData.get(currMap); 
			double [] centroidData = this.clusterCentroids.get(centroidIndex);
			toReturn += getLoss(mapData, centroidData);
		}
		
		return toReturn;
	}
	
	/**
	 * 
	 * @param mapData
	 * @return the best centroid for this mapData (ties are randomly broken)
	 */
	private int getBestFittingCentroidFromData(double[] mapData) {
		double minLoss = Double.POSITIVE_INFINITY;
		List<Integer> bestCentroids = new ArrayList<Integer>();
		
		for(int centroidIndex = 0; centroidIndex < this.numClusters; centroidIndex++) {
			double[] centroidData = this.clusterCentroids.get(centroidIndex);
			double currLoss = getLoss(mapData, centroidData);
			if (currLoss == minLoss) {
				bestCentroids.add(centroidIndex);
			}
			else if (currLoss < minLoss) {
				minLoss = currLoss;
				bestCentroids = new ArrayList<Integer>();
				bestCentroids.add(centroidIndex);
			}
			
		}
		int randIndex = rand.nextInt(bestCentroids.size());
		
		return bestCentroids.get(randIndex);
		
	}
	
	/**
	 * 
	 * @param i1
	 * @param i2
	 * @return loss between the two vectors
	 */
	private double getLoss(double[] i1, double[] i2) {
		assert(i1.length == i2.length);
		double totalLoss = 0;
		for (int i = 0; i < i1.length; i++) {
			double loss = Math.pow(i1[i] - i2[i], 2);
			totalLoss += loss;
		}
		return totalLoss;
		
	}
	

	/**
	 * 
	 * @param normalizedActionCountsForWorlds
	 * @return the input HM now mapping from a mapIO to an array of doubles
	 */
	private HashMap<MapIO, double[]> hashMapToArray(HashMap<MapIO, HashMap<GroundedAction, Double>> normalizedActionCountsForWorlds) {
		HashMap<MapIO, double[]> toReturn = new HashMap<MapIO, double[]>();
		
		for(MapIO currMapIO: normalizedActionCountsForWorlds.keySet()) {
			HashMap<GroundedAction, Double> actionCountsForMap = normalizedActionCountsForWorlds.get(currMapIO);
			double[] actionData = new double[allActions.size()];
			for(GroundedAction currAction : actionCountsForMap.keySet()) {
				int index = allActions.indexOf(currAction);
				double normCount = actionCountsForMap.get(currAction);
				actionData[index] = normCount;
			}
			
			toReturn.put(currMapIO, actionData);
			
		}
		
		return toReturn;
	}
	
	/**
	 * 
	 * @param normalizedCountsHashMap
	 * @return an ordered list of all the input actions in the input hm
	 */
	private List<GroundedAction> allActions(HashMap<MapIO, HashMap<GroundedAction, Double>> normalizedCountsHashMap) {
		//Get hash set of all action
		HashSet<GroundedAction> allActionsHS = new HashSet<GroundedAction>();
		for (MapIO currIO : normalizedCountsHashMap.keySet()) {
			HashMap<GroundedAction, Double> actionCountHM = normalizedCountsHashMap.get(currIO);
			for (GroundedAction currAction : actionCountHM.keySet()) {
				allActionsHS.add(currAction);
			}
			
		}
		//Turn into array list
		ArrayList<GroundedAction> toReturn = new ArrayList<GroundedAction>();
		
		for (GroundedAction currAction : allActionsHS) {
			toReturn.add(currAction);
		}
		
		return toReturn;
	}
	
	//ACCESSORS AND PRINTERS
	public void printClusters() {
		System.out.println("CENTROIDS:");
		for(int clusterIndex = 0; clusterIndex < this.numClusters; clusterIndex++) {
			System.out.println("\tIndex: " + clusterIndex);
			System.out.println("\t"+Arrays.toString(this.clusterCentroids.get(clusterIndex)));
		}
		
		System.out.println("MAPS");
		for(MapIO currMap: this.mapPositionInData.keySet()) {
			System.out.println("\tBelongs to cluster "+ this.clusterMembership.get(currMap) + ":");
			double[] data = this.mapPositionInData.get(currMap);
			System.out.println("\t" + Arrays.toString(data));
		}
	}
	
	/**
	 * @return mapping from integer of cluster index to list of clustered mapIOs
	 */
	public HashMap<Integer, List<MapIO>> getClusters() {
		HashMap<Integer, List<MapIO>> toReturn = new HashMap<Integer, List<MapIO>>();
		//Initialize empty lists
		for (int clusterIndex = 0; clusterIndex < this.numClusters; clusterIndex++) {
			toReturn.put(clusterIndex, new ArrayList<MapIO>());
		}
		
		for (MapIO currIO : this.clusterMembership.keySet()) {
			Integer IOCluster = this.clusterMembership.get(currIO);
			List<MapIO> oldList = toReturn.get(IOCluster);
			
			oldList.add(currIO);
			toReturn.put(IOCluster, oldList);
		}
		return toReturn;
	}
}

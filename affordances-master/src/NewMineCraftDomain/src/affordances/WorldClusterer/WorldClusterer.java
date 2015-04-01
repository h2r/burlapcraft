package affordances.WorldClusterer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.GroundedAction;
import minecraft.MapIO;

public class WorldClusterer {
	private HashMap<String, MapIO> mapFileToIO;
	private HashMap<MapIO, String> fileIOToMapString;
	
	private HashMap<MapIO, HashMap<GroundedAction, Integer>> actionCountsForWorlds = new HashMap<MapIO, HashMap<GroundedAction, Integer>>();
	private HashMap<MapIO, HashMap<GroundedAction, Double>> normalizedActionCountsForWorlds = new HashMap<MapIO, HashMap<GroundedAction, Double>>();
	public HashMap<Integer, List<MapIO>> clusters;
	public List<State> allStates;
	private static String mapFileExtension = "map";
	
	/**
	 * 
	 * @param filePath
	 * @param numClusters
	 * @param usePlanToCluster
	 */
	public WorldClusterer(String filePath, int numClusters, boolean usePlanToCluster) {
		//Read in IOs
		readInMapIOs(filePath);
		
		//Solve maps and perform action counts
		calculateCountsForAllMaps(usePlanToCluster);
		normalizeActionCounts();
		
		//Perform clustering
		KMeaner kMean = new KMeaner(this.normalizedActionCountsForWorlds, numClusters);
		this.clusters = kMean.getClusters();
		
	}

	
	/**
	 * 
	 * @param filePath path to directory to find the .map files in
	 * @return a mapping from .map filenames to MapIOs retrieved in the input directory
	 */
	private void readInMapIOs(String filePath) {
		File folder = new File(filePath);
		
		HashMap<String, MapIO> mapFileToIO = new HashMap<String, MapIO>();
		HashMap<MapIO, String> fileIOToMapString = new HashMap<MapIO, String>();
		
		for(File file : folder.listFiles()) {
			String fileName = file.getName();
			String[] fileNameSplit = fileName.split("\\.");
			if (fileNameSplit.length > 0) {
				String fileExtension = fileNameSplit[fileNameSplit.length-1];
				if (fileExtension.equals(mapFileExtension)){//Check that ends in .map
					String mapFilePath = filePath+file.getName();
					MapIO currIO = new MapIO(mapFilePath);
					mapFileToIO.put(file.getName(), currIO);
					fileIOToMapString.put(currIO, file.getName());
				}
			}
		}
		this.mapFileToIO = mapFileToIO;
		this.fileIOToMapString = fileIOToMapString;
	}
	

	/**
	 * Solves all the MapIOs and then adds up their counts for each action
	 */
	private void calculateCountsForAllMaps(boolean usePlan) {
		System.out.println("Calculating action counts for all maps for clustering...");
		Set<String> keys = this.mapFileToIO.keySet();
		int index = -1;
		for (String mapFileString: keys) {
			//Print progress
			index += 1;
			if (index % (keys.size()/Math.min(4, keys.size())) == 0) {
				System.out.println("\t" + index + "/" + keys.size());
			}
			
			//Suppress prints
			ByteArrayOutputStream theVoid = new ByteArrayOutputStream();
			System.setOut(new PrintStream(theVoid));
			//Get IO, behavior, planner and policy
			MapIO currIO = this.mapFileToIO.get(mapFileString);
			
			HashMap<GroundedAction, Integer> allCurrMapActions = currIO.getActionCountsForAllStates(usePlan);
			
			this.actionCountsForWorlds.put(currIO, allCurrMapActions);
		}
	}
	
	
	/**
	 * Normalizes action counts so that the they all sum to 1
	 */
	private void normalizeActionCounts() {
		for (String mapString: this.mapFileToIO.keySet()) {
			HashMap<GroundedAction, Double>thisMapsNormalizedCounts = new HashMap<GroundedAction, Double>();
			
			MapIO currIO = this.mapFileToIO.get(mapString);
			HashMap<GroundedAction, Integer> thisMapsCounts = this.actionCountsForWorlds.get(currIO);
			//Get total count
			int totalCount = 0;
			for (GroundedAction currAction : thisMapsCounts.keySet()) {
				Integer count = thisMapsCounts.get(currAction);
				totalCount += count;
			}	
			//Normalize
			for (GroundedAction currAction : thisMapsCounts.keySet()) {
				Integer count = thisMapsCounts.get(currAction);
				
				thisMapsNormalizedCounts.put(currAction, ((double)count/(double)totalCount));
			}
			this.normalizedActionCountsForWorlds.put(currIO, thisMapsNormalizedCounts);
		}
	}
	
	//ACCESSORS AND PRINTERS
	public void printAllMaps() {
		for (String fileName : this.mapFileToIO.keySet()) {
			System.out.println(fileName);
			
			MapIO currIO = this.mapFileToIO.get(fileName);
			System.out.println(currIO.getHeaderAsString() + currIO.getCharArrayAsString());
		}
	}
	
	public void printActionCounts() {
		System.out.println("ACTION COUNTS:");
		for (String mapString: this.mapFileToIO.keySet()) {
			MapIO currIO = this.mapFileToIO.get(mapString);
			System.out.println(mapString + ":");
			HashMap<GroundedAction, Integer> thisMapsCounts = this.actionCountsForWorlds.get(currIO);
			
			for (GroundedAction currAction : thisMapsCounts.keySet()) {
				Integer count = thisMapsCounts.get(currAction);
				System.out.println("\t" + currAction.actionName() + ": " + count);
			}			
		}
	}
	
	public void printNormActionCounts() {
		System.out.println("ACTION COUNTS:");
		for (String mapString: this.mapFileToIO.keySet()) {
			MapIO currIO = this.mapFileToIO.get(mapString);
			System.out.println(mapString + ":");
			HashMap<GroundedAction, Double> thisMapsCounts = this.normalizedActionCountsForWorlds.get(currIO);
			
			for (GroundedAction currAction : thisMapsCounts.keySet()) {
				Double count = thisMapsCounts.get(currAction);
				System.out.println("\t" + currAction.actionName() + ": " + count);
			}			
		}
	}
	

	
	/**
	 * @param byMapName
	 */
	public void printClusters(boolean byMapName) {
		for(Integer clusterNum: this.clusters.keySet()) {
			System.out.println("Cluster number: " + clusterNum);
			List<MapIO> members = this.clusters.get(clusterNum);
			for (MapIO member : members) {
				String mapString = "";
				if (!byMapName) {
					//Whole map
					mapString = member.getCharArrayAsString() + "\n";
				}
				else {
					//Just the map name
					mapString = this.fileIOToMapString.get(member);
				}
				System.out.println("\t"+mapString);
			}
		}
	}
	
	public static void main(String [] args) {
		String filePath = "src/minecraft/maps/randomMaps/";
		WorldClusterer test = new WorldClusterer(filePath, 3, true);
		test.printNormActionCounts();
		test.printClusters(true);
	}
}
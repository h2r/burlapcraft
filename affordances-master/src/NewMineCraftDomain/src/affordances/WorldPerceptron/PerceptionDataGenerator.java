package affordances.WorldPerceptron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.Action;
import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import minecraft.MapIO;
import minecraft.MinecraftBehavior.MinecraftBehavior;
import affordances.WorldClusterer.WorldClusterer;

/**
 * Takes in a directory path to maps and generates map from clusters to vectors of perception data
 * @author Dhershkowitz
 *
 */
public class PerceptionDataGenerator {
	private int xLookDistance;
	private int yLookDistance;
	private int zLookDistance;
	private int numClusters;
	private int sampleDownBy;
	private List<PerceptualData> clusterPerceptionsToLearn;
	private List<PerceptualData> clusterPerceptionsToTest;
	private boolean usePlanToCluster;
	private boolean usePlanForPerceptionData;
	private double percentToWithHoldForTesting;
	private HashSet<String> allTags;
	public HashSet<Integer> uniformIndices;
	
	/**
	 * 
	 * @param pathToMaps
	 * @param numClusters
	 * @param xLookDistance
	 * @param yLookDistance
	 * @param zLookDistance
	 * @param usePlanToCluster
	 * @param usePlanForPerceptionData
	 * @param sampleDownBy
	 * @param percentToWithHoldForTesting
	 */
	public PerceptionDataGenerator(String pathToMaps, int numClusters, int xLookDistance, int yLookDistance, int zLookDistance, boolean usePlanToCluster, boolean usePlanForPerceptionData, int sampleDownBy, double percentToWithHoldForTesting) {
		this.numClusters = numClusters;
		this.xLookDistance = xLookDistance;
		this.yLookDistance = yLookDistance;
		this.zLookDistance = zLookDistance;
		this.sampleDownBy = sampleDownBy;
		this.usePlanForPerceptionData = usePlanForPerceptionData;
		this.usePlanToCluster = usePlanToCluster;
		this.percentToWithHoldForTesting = percentToWithHoldForTesting;
		updateDirectory(pathToMaps);
	}
	
	/**
	 * Used to change the directory where the maps are associated with the perception data generator
	 * @param pathToMaps
	 */
	public void updateDirectory(String pathToMaps) {
		WorldClusterer clusterer = new WorldClusterer(pathToMaps, this.numClusters, this.usePlanToCluster);
		clusterer.printClusters(true);
		System.out.println("Getting perception data...");
		getClusterPerceptions(clusterer.clusters);
	}
	

	/**
	 * Updates the PerceptionDataGenerator's learning/test mappings from cluster to perceptions
	 * @param clusters
	 */
	public void getClusterPerceptions(HashMap<Integer, List<MapIO>> clusters) {
		this.clusterPerceptionsToLearn = new ArrayList<PerceptualData>();
		this.clusterPerceptionsToTest = new ArrayList<PerceptualData>();
		List<PerceptualData> allPerceptions = new ArrayList<PerceptualData>();
		//Get all cluster perceptions
		for (Integer clusterIndex: clusters.keySet()) {
			String clusterTag = "";//"cluster" + clusterIndex;
			
			System.out.println("\tOn cluster: " + (clusterIndex) + "/" + (clusters.keySet().size()-1));
			
			List<PerceptualData> allPercDataForClusterToLearn = new ArrayList<PerceptualData>();
			List<PerceptualData> allPercDataForClusterToTest = new ArrayList<PerceptualData>();
			List<PerceptualData> allPercDataForCluster = new ArrayList<PerceptualData>();
			
			List<MapIO> mapsForACluster = clusters.get(clusterIndex);
			
			//Loop over maps for cluster and withhold maps as appropriate
			int mapsToWithHoldToTest = (int) ((int) mapsForACluster.size()*this.percentToWithHoldForTesting);
			System.out.println("\t\t" + mapsToWithHoldToTest + " maps saved for testing.");
			int count = 0;
			for (MapIO currMap : mapsForACluster) {
				List<PerceptualData> percDataForMap = currMap.getAllPercDataForMap(clusterTag, this.xLookDistance, this.yLookDistance, this.zLookDistance, this.usePlanForPerceptionData);
				//If withholding for testing
				if (count < mapsToWithHoldToTest) {
					allPercDataForClusterToTest.addAll(percDataForMap);
				}
				//If not
				else {
					allPercDataForClusterToLearn.addAll(percDataForMap);
				}
				allPercDataForCluster.addAll(percDataForMap);
				count += 1;
			}
			this.clusterPerceptionsToLearn.addAll(allPercDataForClusterToLearn);
			this.clusterPerceptionsToTest.addAll(allPercDataForClusterToTest);
			allPerceptions.addAll(allPercDataForCluster);
		}
		
		//Get all tags
		this.allTags = new HashSet<String>();
		for (PerceptualData perception: allPerceptions) {
			String tag = perception.getTagString();
			this.allTags.add(tag);
		}
		
		
		//Throw out uniform features and resample
		this.uniformIndices = getUniformIndices(allPerceptions);

		removeUniformIndicesAndResample(this.uniformIndices, this.clusterPerceptionsToLearn, this.sampleDownBy);
		removeUniformIndicesAndResample(this.uniformIndices, this.clusterPerceptionsToTest, this.sampleDownBy);
	}
	
	/**
	 * 
	 * @param nonUniformIndices
	 * @param toRemoveFrom
	 * @return toRemoveFrom without uniform indices and resampled as appropriate
	 */
	private void removeUniformIndicesAndResample(HashSet<Integer> uniformIndices, List<PerceptualData> toRemoveFrom, int resampleBy){
		for (PerceptualData percData : toRemoveFrom) {
			percData.removeUniformIndicesAndResample(uniformIndices, resampleBy);
		}
	}
	
	/**
	 * 
	 * @param data
	 * @return hashSet of non-uniform indices
	 */
	private HashSet<Integer> getUniformIndices(List<PerceptualData> data) {
		HashSet<Integer> toReturn = new HashSet<Integer>();
		if (data.isEmpty()) return toReturn;
		for (int index = 0; index < data.get(0).getData().length; index++) {
			if (dataIsUniformAtIndex(index, data)) {
				toReturn.add(index);
			}
		}
		return toReturn;
	}
	
	/**
	 * @param index
	 * @param clustersToPercData
	 * @return boolean of whether data in clusters is uniform at input index
	 */
	private boolean dataIsUniformAtIndex(int index, List<PerceptualData> perceptualData) {
		Integer lastValue = null;
		for (PerceptualData currPercDataHolder: perceptualData) {
			int [] currPercData = currPercDataHolder.getData();
				int currDatum = currPercData[index];
				if (lastValue != null && currDatum != lastValue) {
					return false;
				}
				lastValue = currDatum;
			}	
		return true;
	}
	

	/**
	 * 
	 * @param directory
	 * @param fileName
	 */
	public void printTestDataArff(String directory, String fileName) {
		ArffHelpers.printArffFile(directory, fileName, this.clusterPerceptionsToTest, this.allTags, true);
	}
	
	/**
	 * 
	 * @param directory
	 * @param fileName
	 */
	public void printLearningDataArff(String directory, String fileName) {
		ArffHelpers.printArffFile(directory, fileName, this.clusterPerceptionsToLearn, this.allTags, true);
	}
	

	
	private void runWekaStuff(String dataOutputDirectory, String learnFileName, String testFileName, int numActions, int xLook, int yLook, int zLook) {
		System.out.println("Running weka stuff...");
		try {
			//Training stuff
			Instances train = ArffHelpers.fileToInstances(dataOutputDirectory +learnFileName + ".arff");
			train.setClassIndex(train.numAttributes()-1);
			
			Classifier classifier = new Logistic();
			classifier.buildClassifier(train);
	
			//Testing stuff
			HashSet<String> allTags = ArffHelpers.allClassValStringsFromInstance(train.firstInstance());
			PerceptualData test = new PerceptualData(train.firstInstance());
			
			String mapsPath = "src/minecraft/maps/toCluster/";
			String outputPath = "src/minecraft/planningOutput/";
			
			String mapName = "../bigPlane.map";
			
			MinecraftBehavior mcBeh = new MinecraftBehavior(mapsPath + mapName);
			int cols = mcBeh.getDomainGenerator().cols;
			int rows = mcBeh.getDomainGenerator().rows;
			int height = mcBeh.getDomainGenerator().height;
			
			
			State state = mcBeh.getInitialState();
			
			//Loop
			for (int i = 0; i < numActions; i++) {
				PerceptualData pd = new PerceptualData(state, rows, cols, height, xLook, yLook, zLook); 
				pd.removeUniformIndicesAndResample(this.uniformIndices, 1);
				Action action = pd.getActionFromClassifier(classifier, dataOutputDirectory, allTags, mcBeh.getDomain());
				state = action.performAction(state, "");			
				System.out.println(action.getName());
			}
			
//			Instances test = ArffHelpers.fileToInstances(dataOutputDirectory +testFileName + ".arff");
//			test.setClassIndex(train.numAttributes()-1);
//			Evaluation eval = new Evaluation(train);
//			eval.evaluateModel(nBayes, test);
//			System.out.println(eval.toSummaryString("\nResults\n======\n", false));
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	
	
	public static void main(String[] args) {
		//File names
		String filePath = "src/minecraft/maps/toCluster/";
		String learnFileName = "testPerceptionDataToLearn";
		String testFileName = "testPerceptionDataToTest";
		String dataOutputDirectory = "src/affordances/WorldPerceptron/";
		
		int xLook = 1;
		int yLook = 1;
		int zLook = 1;
		
		PerceptionDataGenerator dataGenerator = new PerceptionDataGenerator(filePath, 1, xLook, yLook, zLook, false, false, 1, 0);
		dataGenerator.printTestDataArff(dataOutputDirectory, testFileName);
		dataGenerator.printLearningDataArff(dataOutputDirectory, learnFileName);
		
		dataGenerator.runWekaStuff(dataOutputDirectory, learnFileName, testFileName, 10, xLook, yLook, zLook);
		System.out.println("Done with weka stuff");

		
		
	}

}

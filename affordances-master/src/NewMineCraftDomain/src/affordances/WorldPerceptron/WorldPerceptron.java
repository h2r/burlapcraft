package affordances.WorldPerceptron;

import affordances.WorldClusterer.WorldClusterer;

public class WorldPerceptron {
	
	
	public static void main(String[] args) {
		String filePath = "src/minecraft/maps/toCluster/";
		WorldClusterer test = new WorldClusterer(filePath, 3, false);
		test.printNormActionCounts();
		test.printClusters(true);
		
	}
}

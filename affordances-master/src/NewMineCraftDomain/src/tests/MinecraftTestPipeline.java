package tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import minecraft.MapIO;
import minecraft.NameSpace;
import minecraft.MinecraftBehavior.MinecraftBehavior;
import minecraft.MinecraftBehavior.Planners.AffordanceRTDPPlanner;
import minecraft.MinecraftBehavior.Planners.AffordanceVIPlanner;
import minecraft.MinecraftBehavior.Planners.MinecraftPlanner;
import minecraft.MinecraftBehavior.Planners.RTDPPlanner;
import minecraft.MinecraftBehavior.Planners.VIPlanner;
import minecraft.WorldGenerator.MapFileGenerator;
import minecraft.WorldGenerator.WorldTypes.DeepTrenchWorld;
import minecraft.WorldGenerator.WorldTypes.PlaneGoalShelfWorld;
import minecraft.WorldGenerator.WorldTypes.PlaneGoldMineWorld;
import minecraft.WorldGenerator.WorldTypes.PlaneGoldSmeltWorld;
import minecraft.WorldGenerator.WorldTypes.PlaneWallWorld;
import minecraft.WorldGenerator.WorldTypes.PlaneWorld;
import affordances.AffordanceLearnerSokoban;
import affordances.KnowledgeBase;

public class MinecraftTestPipeline {

	public MinecraftTestPipeline() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Generates learning and testing maps and learns an affordance knowledge base.
	 * @param mb: MinecraftBehavior
	 * @param testMapDir: Directory to put testing maps in
	 * @param numMaps: Number of maps of each type to generate
	 */
	public static void generateTestMaps(MinecraftBehavior mb, String testMapDir, int numMaps) {
		// Generate learning maps and learn a KB (note: mb's map gets updated several times here)
		
		MapFileGenerator mapMaker = new MapFileGenerator(1, 3, 5, testMapDir);
		
		// Get rid of old maps
		mapMaker.clearMapsInDirectory();
		
		int numLavaBlocks = 1;
		
		// Small
//		mapMaker.generateNMaps(numMaps, new DeepTrenchWorld(1, numLavaBlocks), 1, 3, 5);
//		mapMaker.generateNMaps(numMaps, new PlaneGoldMineWorld(numLavaBlocks), 2, 2, 4);
		mapMaker.generateNMaps(numMaps, new PlaneGoldSmeltWorld(numLavaBlocks), 2, 2, 4);
//		mapMaker.generateNMaps(numMaps, new PlaneWallWorld(1, numLavaBlocks), 1, 3, 4);
		mapMaker.generateNMaps(numMaps, new PlaneWorld(numLavaBlocks), 4, 4, 3);

		// Big-RTDP (SIZES LOCKED)
//		mapMaker.generateNMaps(numMaps, new DeepTrenchWorld(1, numLavaBlocks), 5, 5, 6);
//		mapMaker.generateNMaps(numMaps, new PlaneGoldMineWorld(numLavaBlocks), 4, 4, 4);
//		mapMaker.generateNMaps(numMaps, new PlaneGoldSmeltWorld(numLavaBlocks), 4, 4, 4);
//		mapMaker.generateNMaps(numMaps, new PlaneWallWorld(1, numLavaBlocks + 1), 4, 4, 4);
//		mapMaker.generateNMaps(numMaps, new PlaneWorld(numLavaBlocks), 8, 8, 4);
	}
	
	/**
	 * Run tests on all world types, for each of the planners and record results
	 * @param numMapsPerGoal
	 * @param nametag: name to indicate which results file corresponds to these tests
	 * @param planners: a list of strings indicating which planners to run
	 * @param addOptions: a boolean indicating whether or not to add options to planners
	 * @param addMacroActions: a boolean indicating whether or not to add macroactions to planners
	 * @param countStateSpaceSize: a boolean indicating whether or not to count the reachable state space size of each map
	 * @throws IOException 
	 */
	public static void runMinecraftTests(int numMapsPerGoal, String nametag, List<String> planners, boolean useOptions, boolean useMAs, boolean countStateSpaceSize) throws IOException {
		
		// Create behavior, planners, result objects, test maps
		MinecraftBehavior mcBeh = new MinecraftBehavior();
		String testMapDir = NameSpace.PATHMAPS + "test/";
		generateTestMaps(mcBeh, testMapDir, numMapsPerGoal);
		File testDir = new File(testMapDir);
		String[] testMaps = testDir.list();
		
		// Result objects
		Result BRTDPResults = new Result(NameSpace.BAFFRTDP);
		Result learnedAffBRTDPResults = new Result(NameSpace.BRTDP);

		// --- Load Knowledge Base ---
		String learnedKBName = "learned/learned10";
		if(useMAs) learnedKBName += "_ma";
		if(useOptions) learnedKBName += "_op";
		if(!useMAs && !useOptions) learnedKBName += "_prim_acts";
		learnedKBName += ".kb";
		
		// Load Affordance Knowledge base
		KnowledgeBase affKB = new KnowledgeBase();;
		if(planners.contains(NameSpace.BAFFRTDP)) {
			affKB.load(mcBeh.getDomain(), MinecraftPlanner.getMapOfMAsAndOptions(mcBeh, useOptions, useMAs), learnedKBName, false, countStateSpaceSize);
		}
		
		// --- FILE WRITER SETUP ---
		String outputFileName= NameSpace.PATHRESULTS + nametag;
		if(useOptions) outputFileName = outputFileName + "_opt";
		if(useMAs) outputFileName = outputFileName + "_ma";
		outputFileName = outputFileName + ".result";
		File resultsFile = new File(outputFileName);
		BufferedWriter resultsBW;
		FileWriter resultsFW;
		
		// Status file
		File statusFile = new File(NameSpace.PATHRESULTS + "status.txt");
		BufferedWriter statusBW;
		FileWriter statusFW;
		List<Integer> stateSpaceSizes = new ArrayList<Integer>();
		
		// Initialize Result objects and file writing objects
		resultsFW = new FileWriter(resultsFile.getAbsoluteFile());
		resultsBW = new BufferedWriter(resultsFW);
		statusFW = new FileWriter(statusFile.getAbsoluteFile());
		statusBW = new BufferedWriter(statusFW);
		int mapCounter = 1;

		// --- PLANNING ---
		
		// Loop over each map and solve for each planner given
		for(String map : testMaps) {
			
			System.out.println("Starting new map: " + map);
			statusBW.write("Running on map: " + map + "\n");
			statusBW.flush();
			
			// Update planners with new map
			mcBeh.updateMap(new MapIO(NameSpace.PATHMAPS + "test/" + map));
			
			if(countStateSpaceSize && mapCounter == 1) {
				// Count Reachable State Size
				stateSpaceSizes.add(mcBeh.countReachableStates());
			}
			
			// --- Plan for each planner given ---
			
			// BRTDP
			if(planners.contains(NameSpace.BRTDP)) {
					statusBW.write("\t...boundedRTDP");
					statusBW.flush();
//				BoundedRTDPPlanner rtdp = new BoundedRTDPPlanner(mcBeh, false, false);
//				BRTDPResults.addTrial(rtdp.runPlanner());
					statusBW.write(" Finished\n");
					statusBW.flush();
			}

			// Learned Soft RTDP
			if(planners.contains(NameSpace.BAFFRTDP)) {
					statusBW.write("\t...Learned Soft RTDP");
					statusBW.flush();
//				BoundedAffordanceRTDPPlanner affRTDP = new BoundedAffordanceRTDPPlanner(mcBeh, useOptions, useMAs, affKB);
//				affRTDP.updateKB(affKB);
//				learnedAffBRTDPResults.addTrial(affRTDP.runPlanner());
					statusBW.write(" Finished\n");
					statusBW.flush();
			}
			
				statusBW.write("mapCounter: " + mapCounter + "\n");
				statusBW.flush();
				
			// --- RECORD RESULTS TO FILE ---
			if(mapCounter == numMapsPerGoal) {
				// Count avg. state space size
				double avgStateSpaceSize = 0;
				if(countStateSpaceSize) {
					int total = 0;
					for(Integer size : stateSpaceSizes) {
						total += size;
					}
					avgStateSpaceSize = ((double)total) / mapCounter;
				}
			
				// Write map info to file
				resultsBW.write("map: " + map.substring(0, map.length() - 5) + "stateSpace=" + avgStateSpaceSize + "\n");
				
				// Write planner results to file
				if(planners.contains(NameSpace.BRTDP)) {
					resultsBW.write("\t" + BRTDPResults.getAllResults() + "AVERAGES: " + BRTDPResults + "\n");
					System.out.println(BRTDPResults.toString());
					BRTDPResults.clear();
				}
				if(planners.contains(NameSpace.BAFFRTDP)) {
					resultsBW.write("\t" + learnedAffBRTDPResults.getAllResults() + "AVERAGES: " + learnedAffBRTDPResults + "\n");
					System.out.println(learnedAffBRTDPResults.toString());
					learnedAffBRTDPResults.clear();
				}
				// Reset
				mapCounter = 1;
				continue;
			}
			
			mapCounter++;
		}
		
	}
	
	/**
	 * Run tests on all world types, for each of the 3 planners and record results
	 * @param numIterations: the number of times to perform testing
	 * @param nametag: a flag for the name of the results file
	 * @throws IOException 
	 */
	public static void runLearningRateTests(String nametag, int numLearningMapsPerLGD, int numTestingMaps, double minFractOfStateSpace, double maxFractOfStateSpace, double increment, boolean shouldLearn, boolean countStateSpaceSize, boolean useOptions, boolean useMAs, String jobID) throws IOException {
		// Generate Behavior and test maps
		MinecraftBehavior mcBeh = new MinecraftBehavior();
		String testMapDir = NameSpace.PATHMAPS + "/" + jobID + "/learningRateTest/";
		generateTestMaps(mcBeh, testMapDir, numTestingMaps);
		
		File testDir = new File(testMapDir);
		String[] testMaps = testDir.list();
		
		List<String> kbNames = new ArrayList<String>();
		
		if(shouldLearn){
			// --- Create Knowledge Bases ---
			for(double fractOfStateSpace = minFractOfStateSpace; fractOfStateSpace <= maxFractOfStateSpace; fractOfStateSpace = fractOfStateSpace + increment) {
				// Learn if we're supposed to learn a new KB
//				String learnedKBName = AffordanceLearnerSokoban.generateSokobanKB(mcBeh, numLearningMapsPerLGD, true, false, false, fractOfStateSpace, jobID);
//				kbNames.add(learnedKBName);
			}
		}
		else{
			// If we're not learning new knowledge bases, use the existing ones.
			kbNames = new ArrayList<String>();
			for(double fractOfStateSpace = minFractOfStateSpace; fractOfStateSpace <= maxFractOfStateSpace; fractOfStateSpace = fractOfStateSpace + increment) {
				String newKB = "lr_" + String.format("%.2f", fractOfStateSpace) + ".kb";
				kbNames.add(newKB);
			}
//			File learningRateKBDir = new File("src/minecraft/kb/learning_rate/");
//			String[] kbsToUse = learningRateKBDir.list();
//			kbNames = new ArrayList<String>(Arrays.asList(kbsToUse));
		}
		
		// Make knowledge base and result objects
		KnowledgeBase affKB = new KnowledgeBase();
		LearningRateResult learnedHardRTDPResults = new LearningRateResult(NameSpace.LearnedHardRTDP);
		
		// Run learning planners with varied size KBs
		for(String kbName : kbNames) {
			System.out.println("(MCTP) starting on kb: " + kbName);
			for (String map : testMaps) {
				System.out.println("(MCTP) starting on map: " + map);
				MapIO mapIO = new MapIO(testMapDir + map);
				mcBeh.updateMap(mapIO);
				
				// Hard
				affKB.load(mcBeh.getDomain(), MinecraftPlanner.getMapOfMAsAndOptions(mcBeh, useOptions, useMAs),  "learning_rate/" + kbName, false, true);
				AffordanceRTDPPlanner affHardRTDP = new AffordanceRTDPPlanner(mcBeh, false, false, affKB);
				learnedHardRTDPResults.addTrialForKB(kbName, affHardRTDP.runPlanner());
			}
		}
		
//		String outputFileName= NameSpace.PATHRESULTS + "learning_rate/lr.results";
//		File resultsFile = new File(outputFileName);
//		FileWriter resultsFW = new FileWriter(resultsFile.getAbsoluteFile());
//		BufferedWriter resultsBW = new BufferedWriter(resultsFW);
		
//		resultsBW.write("Learning Rate Results: " + minFractOfStateSpace + "-" + maxFractOfStateSpace + "\n");
		System.out.println("Learning Rate Results: " + minFractOfStateSpace + "-" + maxFractOfStateSpace + "\n");
		for(String kbName : learnedHardRTDPResults.getResults().keySet()) {
//			resultsBW.write("\t [" + kbName + "]\t" + learnedHardRTDPResults.getResults().get(kbName).toString() + "\n");
			System.out.println("\t [" + kbName + "]\t" + learnedHardRTDPResults.getResults().get(kbName).toString() + "\n");
		}
//		resultsBW.flush();
	}
	
	public static void main(String[] args) {

		
		// --- Basic Minecraft Results ---
		boolean learningFlag = false;
//		 Choose which planners to collect results for
		List<String> planners = new ArrayList<String>();
		planners.add(NameSpace.RTDP);
		planners.add(NameSpace.ExpertRTDP);
		planners.add(NameSpace.LearnedHardRTDP);
		planners.add(NameSpace.LearnedSoftRTDP);
		planners.add(NameSpace.VI);
		planners.add(NameSpace.ExpertVI);
		planners.add(NameSpace.LearnedHardVI);
		boolean addOptions = false;
		boolean addMacroActions = false;
		boolean countStateSpaceSize = false;
		try {
			runMinecraftTests(3, "3_legit", planners, addOptions, addMacroActions, countStateSpaceSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
//
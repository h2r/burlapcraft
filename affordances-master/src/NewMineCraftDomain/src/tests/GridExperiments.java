package tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import burlap.oomdp.singleagent.Action;
import minecraft.NameSpace;
import minecraft.MinecraftBehavior.MinecraftBehavior;
import minecraft.MinecraftBehavior.Planners.AffordanceRTDPPlanner;
import minecraft.MinecraftBehavior.Planners.MinecraftPlanner;
import affordances.AffordanceLearner;
import affordances.KnowledgeBase;

public class GridExperiments {

	
	public static void runMinecraftExperiments(String[] args) {
//		 Choose which planners to collect results for
		List<String> planners = new ArrayList<String>();
		planners.add(NameSpace.BRTDP);
		planners.add(NameSpace.BAFFRTDP);

		// Set relevant flags
		boolean addOptions = false;
		boolean addMacroActions = false;
		boolean countStateSpaceSize = false;
		
		int numWorldsToTestWith = 10;
		
		// Run experiments.
		try {
			MinecraftTestPipeline.runMinecraftTests(numWorldsToTestWith, "3_legit", planners, addOptions, addMacroActions, countStateSpaceSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void runLearning(String[] args) {
		boolean addOptions = false;
		boolean addMAs = false;
		
		double fractOfStateSpace = 1.0; //(Double.parseDouble(args[0]) / 10.0) - 0.1;
		final int numWorldsToLearnWith = 1;
		MinecraftBehavior mcBeh = new MinecraftBehavior();
		String jobID = args[0];
		if(addMAs) jobID += "_ma";
		if(addOptions) jobID += "_o";
		
		KnowledgeBase affKB = AffordanceLearner.learnMinecraftKB(mcBeh, numWorldsToLearnWith, false, addOptions, addMAs, fractOfStateSpace, jobID);
//		affKB.print();
		affKB.save();
	}
	
	public static void main(String[] args) {
		NameSpace.setGridPaths();
		runLearning(args);
//		runMinecraftExperiments(args);
	}
	
}

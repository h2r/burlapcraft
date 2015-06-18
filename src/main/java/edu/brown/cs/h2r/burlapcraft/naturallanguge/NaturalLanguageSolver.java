package edu.brown.cs.h2r.burlapcraft.naturallanguge;

import burlap.behavior.statehashing.DiscreteStateHashFactory;
import burlap.behavior.statehashing.StateHashFactory;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.GroundedProp;
import burlap.oomdp.core.State;
import commands.model3.GPConjunction;
import commands.model3.TaskModule;
import commands.model3.TrajectoryModule;
import commands.model3.mt.Tokenizer;
import commands.model3.weaklysupervisedinterface.MTWeaklySupervisedModel;
import commands.model3.weaklysupervisedinterface.WeaklySupervisedController;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorReal;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorSimulated;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import generativemodel.GMQueryResult;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James MacGlashan.
 */
public class NaturalLanguageSolver {

	protected static Domain referenceDomain;
	protected static List<GPConjunction> liftedTasks;
	protected static WeaklySupervisedController commandController;
	protected static StateHashFactory hashingFactory;
	protected static MTWeaklySupervisedModel languageModel;

	protected static boolean loaded = false;


	public static void initializeCommandController(String languageModelPath){

		loaded = false;

		DomainGeneratorReal realdg = new DomainGeneratorReal(100, 100, 100);
		referenceDomain = realdg.generateDomain();

		liftedTasks = new ArrayList<GPConjunction>(2);

		GPConjunction agentToRoom = new GPConjunction();
		agentToRoom.addGP(new GroundedProp(referenceDomain.getPropFunction(HelperNameSpace.PFINROOM),new String[]{"a", "r"}));
		liftedTasks.add(agentToRoom);

		hashingFactory = new DiscreteStateHashFactory();

		commandController = new WeaklySupervisedController(referenceDomain, liftedTasks, hashingFactory, true);

		Tokenizer tokenizer = new Tokenizer(true ,true);
		tokenizer.addDelimiter("-");
		languageModel = new MTWeaklySupervisedModel(commandController, tokenizer, 10);
		System.out.println("Loading language model");
		languageModel.loadModel(languageModelPath);
		System.out.println("Finished loading language model");

		commandController.setLanguageModel(languageModel);

		loaded = true;

	}

	public static void obeyNaturalLanguageCommand(String command){

		if(loaded == false){
			System.out.println("Cannot obey command because the language model either has not been loaded or has not finished loading.");
			return;
		}

		int [][][] map = StateGenerator.getMap(BurlapCraft.currentDungeon);

		DomainGeneratorSimulated simdg = new DomainGeneratorSimulated(map);
		DomainGeneratorReal realdg = new DomainGeneratorReal(map[0].length, map[0][0].length, map.length);

		Domain domain = simdg.generateDomain();
		Domain realDomain = realdg.generateDomain();

		State initialState = StateGenerator.getCurrentState(domain, BurlapCraft.currentDungeon);

		List<GMQueryResult> rfDist = commandController.getRFDistribution(initialState, command);
		GMQueryResult predicted = GMQueryResult.maxProb(rfDist);

		TaskModule.RFConVariableValue gr = (TaskModule.RFConVariableValue)predicted.getQueryForVariable(commandController.getGM().getRVarWithName(TaskModule.GROUNDEDRFNAME));
		TrajectoryModule.ConjunctiveGroundedPropTF tf = new TrajectoryModule.ConjunctiveGroundedPropTF(gr.rf);

		System.out.println("Obeying command to achieve " + tf.toString());

	}

}

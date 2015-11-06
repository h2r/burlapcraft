package edu.brown.cs.h2r.burlapcraft.naturallanguge;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.planning.deterministic.DDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.SDPlannerPolicy;
import burlap.behavior.singleagent.planning.deterministic.informed.NullHeuristic;
import burlap.behavior.singleagent.planning.deterministic.informed.astar.AStar;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.oomdp.legacy.StateJSONParser;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.GroundedProp;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.common.UniformCostRF;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;
import commands.data.TrainingElement;
import commands.data.TrainingElementParser;
import commands.data.TrajectoryParser;
import commands.model3.GPConjunction;
import commands.model3.TaskModule;
import commands.model3.TrajectoryModule;
import commands.model3.mt.Tokenizer;
import commands.model3.weaklysupervisedinterface.MTWeaklySupervisedModel;
import commands.model3.weaklysupervisedinterface.WeaklySupervisedController;
import commands.model3.weaklysupervisedinterface.WeaklySupervisedTrainingInstance;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.MinecraftDomainGenerator;
import edu.brown.cs.h2r.burlapcraft.environment.MinecraftEnvironment;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import generativemodel.GMQueryResult;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

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
	protected static SimpleHashableStateFactory hashingFactory;
	protected static MTWeaklySupervisedModel languageModel;

	protected static boolean loaded = false;


	public static void initializeCommandController(ICommandSender sender, String languageModelPath){

		loaded = false;

		MinecraftDomainGenerator realdg = new MinecraftDomainGenerator(100, 100, 100);
		referenceDomain = realdg.generateDomain();

		liftedTasks = getMinecraftTasks();

		hashingFactory = new SimpleHashableStateFactory(false);

		commandController = new WeaklySupervisedController(referenceDomain, liftedTasks, hashingFactory, true);

		Tokenizer tokenizer = new Tokenizer(true ,true);
		tokenizer.addDelimiter("-");
		languageModel = new MTWeaklySupervisedModel(commandController, tokenizer, 10);
		chatOrSystemPrint(sender, "Loading language model");
		languageModel.loadModel(languageModelPath);
		chatOrSystemPrint(sender, "Finished loading language model");

		commandController.setLanguageModel(languageModel);

		loaded = true;

	}

	public static void initializeAndTrainCommandController(ICommandSender sender, String pathToDatasetDirectory){

		loaded = false;

		//DomainGeneratorReal realdg = new DomainGeneratorReal(100, 100, 100);
		DomainGenerator mdg = new MinecraftDomainGenerator(StateGenerator.getMap(BurlapCraft.currentDungeon));
		referenceDomain = mdg.generateDomain();

		liftedTasks = getMinecraftTasks();

		hashingFactory = new SimpleHashableStateFactory(false);

		commandController = new WeaklySupervisedController(referenceDomain, liftedTasks, hashingFactory, true);
		List <WeaklySupervisedTrainingInstance> oldTrainingData = commandController.getWeaklySupervisedTrainingDataset();

		TrainingElementParser teParser = new TrainingElementParser(referenceDomain, new StateJSONParser(referenceDomain));
		List<TrainingElement> teData = teParser.getTrainingElementDataset(pathToDatasetDirectory, "txt");



		Tokenizer tokenizer = new Tokenizer(true ,true);
		tokenizer.addDelimiter("-");
		languageModel = new MTWeaklySupervisedModel(commandController, tokenizer, 10);
		commandController.setLanguageModel(languageModel);
		if(oldTrainingData != null){
			commandController.setWeaklySupervisedTrainingDataset(oldTrainingData);
		}


		chatOrSystemPrint(sender, "Beginning training...");

		//instantiate the weakly supervised language model dataset using IRL
		//commandController.createWeaklySupervisedTrainingDatasetFromTrajectoryDataset(teData);
		commandController.createOrAddWeaklySupervisedTrainingDatasetFromTrajectoryDataset(teData);

		//perform learning
		commandController.trainLanguageModel();

		chatOrSystemPrint(sender, "Finished training.");

		loaded = true;

	}

	public static void saveLanguageModel(ICommandSender sender, String pathToFile){

		if(languageModel == null){
			chatOrSystemPrint(sender, "Cannot save language model because it has not been trained!");
			return;
		}

		chatOrSystemPrint(sender, "Writing language model to disk...");
		commandController.dumpLanguageMode(pathToFile);
		chatOrSystemPrint(sender, "Finished writing language model.");

	}

	protected static List<GPConjunction> getRSSTasks(){
		liftedTasks = new ArrayList<GPConjunction>(2);

		GPConjunction agentToRoom = new GPConjunction();
		agentToRoom.addGP(new GroundedProp(referenceDomain.getPropFunction(HelperNameSpace.PFAGENTINROOM),new String[]{"a", "r"}));
		liftedTasks.add(agentToRoom);

		GPConjunction blockInRoom = new GPConjunction();
		blockInRoom.addGP(new GroundedProp(referenceDomain.getPropFunction(HelperNameSpace.PFBLOCKINROOM), new String[]{"b", "r"}));
		liftedTasks.add(blockInRoom);

		return liftedTasks;
	}
	
	protected static List<GPConjunction> getMinecraftTasks() {
		liftedTasks = new ArrayList<GPConjunction>(3);
		
		GPConjunction agentToRoom = new GPConjunction();
		agentToRoom.addGP(new GroundedProp(referenceDomain.getPropFunction(HelperNameSpace.PFAGENTINROOM),new String[]{"a", "r"}));
		liftedTasks.add(agentToRoom);

		GPConjunction blockInRoom = new GPConjunction();
		blockInRoom.addGP(new GroundedProp(referenceDomain.getPropFunction(HelperNameSpace.PFBLOCKINROOM), new String[]{"b", "r"}));
		liftedTasks.add(blockInRoom);
		
		GPConjunction agentOnBlock = new GPConjunction();
		agentOnBlock.addGP(new GroundedProp(referenceDomain.getPropFunction(HelperNameSpace.PFAGENTONBLOCK), new String[]{"a", "b"}));
		liftedTasks.add(agentOnBlock);

		return liftedTasks;
	}


	public static void chatOrSystemPrint(ICommandSender sender, String msg){
		if(sender != null){
			sender.addChatMessage(new ChatComponentText(msg));
		}
		else{
			System.out.println(msg);
		}
	}

	public static void obeyNaturalLanguageCommand(ICommandSender sender, String command){

		if(loaded == false){
			chatOrSystemPrint(sender, "Cannot obey command because the language model either has not been loaded or has not finished loading.");
			return;
		}

		int [][][] map = StateGenerator.getMap(BurlapCraft.currentDungeon);

		MinecraftDomainGenerator mdg = new MinecraftDomainGenerator(map);

		Domain domain = mdg.generateDomain();
		MinecraftEnvironment me = new MinecraftEnvironment(domain);

		State initialState = StateGenerator.getCurrentState(domain, BurlapCraft.currentDungeon);

		List<GMQueryResult> rfDist = commandController.getRFDistribution(initialState, command);
		GMQueryResult predicted = GMQueryResult.maxProb(rfDist);

		TaskModule.RFConVariableValue gr = (TaskModule.RFConVariableValue)predicted.getQueryForVariable(commandController.getGM().getRVarWithName(TaskModule.GROUNDEDRFNAME));
		TrajectoryModule.ConjunctiveGroundedPropTF tf = new TrajectoryModule.ConjunctiveGroundedPropTF(gr.rf);
		TFGoalCondition gc = new TFGoalCondition(tf);
		me.setTerminalFunction(tf);

		RewardFunction rf = new UniformCostRF();

		chatOrSystemPrint(sender, "Obeying command to achieve " + tf.toString());

		AStar planner = new AStar(domain, rf, gc, new SimpleHashableStateFactory(false), new NullHeuristic());
		planner.planFromState(initialState);
		Policy p = new DDPlannerPolicy(planner);

		
		p.evaluateBehavior(me);

	}

}

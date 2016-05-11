package edu.brown.cs.h2r.burlapcraft.solver;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.modellearning.rmax.PotentialShapedRMax;
import burlap.behavior.singleagent.learning.tdmethods.vfa.GradientDescentSarsaLam;
import burlap.behavior.singleagent.vfa.ValueFunctionApproximation;
import burlap.behavior.singleagent.vfa.cmac.CMACFeatureDatabase;
import burlap.behavior.valuefunction.ValueFunctionInitialization;
import burlap.debugtools.MyTimer;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.command.CommandTeleport;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.MinecraftDomainGenerator;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.DungeonCombat;
import edu.brown.cs.h2r.burlapcraft.environment.MinecraftEnvironment;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import mobs.PassiveSpider;
import mobs.PersistentBlaze;
import mobs.PersistentCreeper;
import mobs.PersistentMagmaCube;
import mobs.PersistentSkeleton;
import mobs.PersistentWitch;
import mobs.PersistentZombie;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import rewardfunctions.MobRewardFunction;
import terminalfunctions.MobTerminalFunction;

public class FightSolver {

	public static String CURRENT_MOB_NAME = "";
	public static String ALGORITHM_NAME = "";
	public static final int COMBAT_DUNGEON_WIDTH = 10;
	public static boolean isPlayerDead;
	public static boolean isMobDead;
	public static EntityMob mob;
	public static World mobWorld;
	public static double previousMobHealth;
	
	public static void run(World world, DungeonCombat combatDungeon, RewardFunction rf, TerminalFunction tf){
		int [][][] map = StateGenerator.getMap(BurlapCraft.currentDungeon);
		MinecraftDomainGenerator mdg = new MinecraftDomainGenerator(map);
		mdg.setActionWhiteListToNavigationAndAttack();

		Domain domain = mdg.generateDomain();

		QLearn qLearnAgent = new QLearn(domain, 0.99, new SimpleHashableStateFactory(), new ValueFunctionInitialization.ConstantValueFunctionInitialization(), 0.1, 0.1);
		SimpleAgent simpleAgent = new SimpleAgent(domain);
		RandomActionAgent randomActionAgent = new RandomActionAgent(domain);
		GradientDescentSarsaLam tileAgent = Tile.getTileAgent(domain);
		//PotentialShapedRMax rmaxAgent = new PotentialShapedRMax(domain, 0.99, new SimpleHashableStateFactory(), new ClosePotentialFunction(), 3, 0.1, 10);
		PotentialShapedRMax rmaxAgent = new PotentialShapedRMax(domain, 0.99, new SimpleHashableStateFactory(), new ClosePotentialFunction(), 1, 0.1, 10);

		MinecraftEnvironment me = new MinecraftEnvironment(domain);

		me.setRewardFunction(rf);
		me.setTerminalFunction(tf);

		int x = (int) combatDungeon.getPose().getX();
		int y = (int) combatDungeon.getPose().getY();
		int z = (int) combatDungeon.getPose().getZ();
		int w = COMBAT_DUNGEON_WIDTH;
		spawnMob(world, x, y, z, COMBAT_DUNGEON_WIDTH);

		if (ALGORITHM_NAME.equals("qlearn")) {
			runTrial(world, combatDungeon, rf, tf, me, qLearnAgent);
			qLearnAgent.resetSolver();
		} else if(ALGORITHM_NAME.equals("simple")) {
			runTrial(world, combatDungeon, rf, tf, me, simpleAgent);
		} else if(ALGORITHM_NAME.equals("random")) {
			runTrial(world, combatDungeon, rf, tf, me, randomActionAgent);
		} else if(ALGORITHM_NAME.equals("tile")) {
			runTrial(world, combatDungeon, rf, tf, me, tileAgent);
			tileAgent.resetSolver();
		} else if(ALGORITHM_NAME.equals("rmax")) {
			runTrial(world, combatDungeon, rf, tf, me, rmaxAgent);
			rmaxAgent.resetSolver();
		}

		if (!isMobDead) {
			removeCurrentMob();
		}
	}
	
	public static void resetLearningEpisode(World world, DungeonCombat combatDungeon, RewardFunction rf, TerminalFunction tf) {		
		combatDungeon.movePlayerToRandomLocation();

		int x = (int) combatDungeon.getPose().getX();
		int y = (int) combatDungeon.getPose().getY();
		int z = (int) combatDungeon.getPose().getZ();
		int w = COMBAT_DUNGEON_WIDTH;

		if(!isMobDead) {
			mob.setLocationAndAngles(x + w - 2, y + 1, z + w - 2, 0.0F, 0.0F);
			mob.setHealth(mob.getMaxHealth());
			previousMobHealth = mob.getHealth();
		}
		isPlayerDead = false;
		isMobDead = false;
	}
	
	public static void spawnMob(World world, int x, int y, int z, int w) {
		EntityMob newMob = null;
		if (CURRENT_MOB_NAME.equals("zombie")) {
			newMob = new PersistentZombie(world);
		} else if (CURRENT_MOB_NAME.equals("spider")) {
			newMob = new PassiveSpider(world);
		} else if (CURRENT_MOB_NAME.equals("witch")) {
			newMob = new PersistentWitch(world);
		} else if (CURRENT_MOB_NAME.equals("creeper")) {
			newMob = new PersistentCreeper(world);
		} else if (CURRENT_MOB_NAME.equals("skeleton")) {
			newMob = new PersistentSkeleton(world);
		} else if (CURRENT_MOB_NAME.equals("blaze")) {
			newMob = new PersistentBlaze(world);
		}

		newMob.setLocationAndAngles(x + w - 2, y + 1, z + w - 2, 0.0F, 0.0F);
		newMob.rotationYawHead = newMob.rotationYaw;
		newMob.renderYawOffset = newMob.rotationYaw;
		world.spawnEntityInWorld(newMob);

		mob = newMob;
		mobWorld = world;

		previousMobHealth = newMob.getHealth();
	}
	
	public static void removeCurrentMob() {
		mobWorld.removeEntity(mob);
		mob = null;
	}
	
	public static void runTrial(World world, DungeonCombat combatDungeon, RewardFunction rf, TerminalFunction tf, MinecraftEnvironment me, LearningAgent agent) {
		MyTimer timer = new MyTimer();

		int iterations = 100;
		List<EpisodeAnalysis> episodes = new ArrayList<EpisodeAnalysis>(iterations);
		List<Double> playerWinTimes = new ArrayList<Double>();
		List<Double> mobWinTimes = new ArrayList<Double>();
		List<Integer> playerWinRounds = new ArrayList<Integer>();
		List<Integer> mobWinRounds = new ArrayList<Integer>();

		for (int i = 0; i < iterations; i++) {
			HelperActions.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Episode " + i + " started"));

			resetLearningEpisode(world, combatDungeon, rf, tf);

			timer.start();
			episodes.add(agent.runLearningEpisode(me));
			timer.stop();
			if (isPlayerDead) {
				mobWinTimes.add(timer.getTime());
				mobWinRounds.add(i);
			} else if (isMobDead) {
				playerWinTimes.add(timer.getTime());
				playerWinRounds.add(i);
			}
		}

		double averagePlayerWinTime = 0.0;
		double averageMobWinTime = 0.0;
		for (double time : playerWinTimes) {
			averagePlayerWinTime = averagePlayerWinTime + time;
		}
		for (double time : mobWinTimes) {
			averageMobWinTime = averageMobWinTime + time;
		}
		averagePlayerWinTime = averagePlayerWinTime/playerWinTimes.size();
		averageMobWinTime = averageMobWinTime/mobWinTimes.size();
		System.out.println("Player rounds won: " + playerWinTimes.size());
		System.out.println("Average time for player won rounds: " + averagePlayerWinTime);
		System.out.println("Mob rounds won: " + mobWinTimes.size());
		System.out.println("Average time for mob won rounds: " + averageMobWinTime);
		System.out.println("Player rounds won list: " + playerWinRounds.toString());
		System.out.println("Mob rounds won list: " + mobWinRounds.toString());
	}
}

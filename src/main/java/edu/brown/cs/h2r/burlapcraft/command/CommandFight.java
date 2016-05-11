package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.List;

import burlap.behavior.valuefunction.ValueFunctionInitialization;
import burlap.debugtools.MyTimer;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.MinecraftDomainGenerator;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.DungeonCombat;
import edu.brown.cs.h2r.burlapcraft.environment.MinecraftEnvironment;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import edu.brown.cs.h2r.burlapcraft.solver.FightSolver;
import edu.brown.cs.h2r.burlapcraft.solver.MinecraftSolver;
import edu.brown.cs.h2r.burlapcraft.solver.QLearn;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import mobs.PersistentZombie;
import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import rewardfunctions.MobRewardFunction;
import terminalfunctions.MobTerminalFunction;

public class CommandFight implements ICommand {

	private final List aliases;
	
	private MobRewardFunction mrf = new MobRewardFunction();
	private MobTerminalFunction mtf = new MobTerminalFunction();
	private World world;
	private DungeonCombat combatDungeon;

	public CommandFight() {
		aliases = new ArrayList();
		aliases.add("fight");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "fight";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "fight <algorithm> <mob>";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {		
		world = sender.getEntityWorld();
		if (world.isRemote) {
			return;
		}

		combatDungeon = (DungeonCombat) BurlapCraft.dungeonMap.get("combat");
		Pose pose = combatDungeon.getPose();
		int x = (int)pose.getX();
		int y = (int)pose.getY();
		int z = (int)pose.getZ();
		int w = FightSolver.COMBAT_DUNGEON_WIDTH;

		FightSolver.ALGORITHM_NAME = args[0];
		FightSolver.CURRENT_MOB_NAME = args[1];

		Thread bthread = new Thread(new Runnable() {
			@Override
			public void run() {
				FightSolver.run(world, combatDungeon, mrf, mtf);
			}
		});
		bthread.start();
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

	@SubscribeEvent
	public void playerDied(PlayerDropsEvent event) {
		FightSolver.isPlayerDead = true;

		//Keeps the player from having to respawn on every learning episode
		event.entityPlayer.setHealth(event.entityPlayer.getMaxHealth());
		event.entityPlayer.getFoodStats().setFoodLevel(20);
		event.entityPlayer.clearActivePotions();
	}

	@SubscribeEvent
	public void mobDied(LivingDeathEvent event) {		
		if (event.entityLiving instanceof EntityPlayer) {			
			return;
		}

		DamageSource source = event.source;
		if (source.getSourceOfDamage() instanceof EntityPlayer) {			
			FightSolver.isMobDead = true;
			
			EntityPlayer player = (EntityPlayer) source.getSourceOfDamage();
			player.setHealth(player.getMaxHealth());
			player.getFoodStats().setFoodLevel(20);
			player.clearActivePotions();
			
			int x = (int) combatDungeon.getPose().getX();
			int y = (int) combatDungeon.getPose().getY();
			int z = (int) combatDungeon.getPose().getZ();
			int w = FightSolver.COMBAT_DUNGEON_WIDTH;
			FightSolver.spawnMob(world, x, y, z, w);
		}
	}

	@SubscribeEvent
	public void livingDropsEvent(LivingDropsEvent event) {
		event.setCanceled(true);
	}
}

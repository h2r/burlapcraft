package com.kcaluru.burlapcraft.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.kcaluru.burlapcraft.BurlapCraft;
import com.kcaluru.burlapcraft.handler.HandlerDungeonGeneration;
import com.kcaluru.burlapcraft.handler.HandlerEvents;
import com.kcaluru.burlapcraft.helper.HelperNameSpace;
import com.kcaluru.burlapcraft.solver.SolverLearningBridge;
import com.kcaluru.burlapcraft.solver.SolverLearningFinder;
import com.kcaluru.burlapcraft.stategenerator.StateGenerator;

public class CommandRMax implements ICommand {

	private final List aliases;
	
	public CommandRMax() {
		aliases = new ArrayList();
		aliases.add("rmax");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "rmax";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "rmax";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		World world = sender.getEntityWorld();
		if (!world.isRemote) {
			if (args.length > 0) {
				sender.addChatMessage(new ChatComponentText("This command takes no arguments"));
				return;
			}
			
			int dungeonID = BurlapCraft.dungeonLocID;
			
			if (dungeonID == 0) {
				sender.addChatMessage(new ChatComponentText("You are not inside a dungeon"));
				return;
			}
			
			switch (dungeonID) {
			
			case 1:
				// create the solver and give it the map
				SolverLearningFinder finderSolver = new SolverLearningFinder(StateGenerator.getMap(dungeonID));
				
				// run RMax
				finderSolver.RMAX();
				
				break;
			
			case 2:
				// create the solver and give it the map
				SolverLearningBridge bridgeSolver = new SolverLearningBridge(StateGenerator.getMap(dungeonID));
				
				// run RMax
				bridgeSolver.RMAX();
				
				break;
			
			}

		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,
			String[] p_71516_2_) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

	
}

package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import burlap.oomdp.core.Domain;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.solver.MinecraftSolver;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandEndLearn implements ICommand {

	private final List aliases;
	
	public CommandEndLearn() {
		aliases = new ArrayList();
		aliases.add("endLearn");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "endLearn";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "endLearn <string command here>";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length > 0) {
			sender.addChatMessage(new ChatComponentText("This command does not take any arguments."));
			return;
		}
		
		CommandLearn.endLearning = true;
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

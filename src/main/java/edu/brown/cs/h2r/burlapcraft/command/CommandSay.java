package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.brown.cs.h2r.burlapcraft.naturallanguge.NaturalLanguageSolver;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerDungeonGeneration;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerEvents;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;

public class CommandSay implements ICommand {
private final List aliases;
	
	public CommandSay() {
		aliases = new ArrayList();
		aliases.add("say");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "say";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "say <your command here>";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
			
		if (args.length < 1) {
			sender.addChatMessage(new ChatComponentText("This command takes a string argument. The string can contain multiple words."));
			return;
		}
		
		final String commandToExecute = StringUtils.join(args, " ");

		final ICommandSender fsender = sender;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				NaturalLanguageSolver.obeyNaturalLanguageCommand(fsender, commandToExecute);
			}
		});

		thread.start();
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

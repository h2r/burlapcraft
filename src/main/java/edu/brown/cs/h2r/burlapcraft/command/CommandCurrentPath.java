package edu.brown.cs.h2r.burlapcraft.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import edu.brown.cs.h2r.burlapcraft.naturallanguge.NaturalLanguageSolver;

public class CommandCurrentPath implements ICommand {
	public static final String ALIAS = "currentPath";

	@Override
	public String getCommandName() {
		return ALIAS;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return ALIAS;
	}

	@Override
	public List getCommandAliases() {
		return Arrays.asList(ALIAS);
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {

		String workingDir = System.getProperty("user.dir");
		sender.addChatMessage(new ChatComponentText("Current working directory : " + workingDir));

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

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}

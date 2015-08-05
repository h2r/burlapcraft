package edu.brown.cs.h2r.burlapcraft.command;

import edu.brown.cs.h2r.burlapcraft.naturallanguge.NaturalLanguageSolver;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.List;

/**
 * @author James MacGlashan.
 */
public class CommandWriteLanguageModel implements ICommand{

	public static final String ALIAS = "writeLanguageModel";

	@Override
	public String getCommandName() {
		return ALIAS;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return ALIAS + " path";
	}

	@Override
	public List getCommandAliases() {
		return Arrays.asList(ALIAS);
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {

		NaturalLanguageSolver.saveLanguageModel(sender, args[0]);

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

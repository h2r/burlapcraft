package edu.brown.cs.h2r.burlapcraft.command;

import edu.brown.cs.h2r.burlapcraft.naturallanguge.NaturalLanguageSolver;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James MacGlashan.
 */
public class CommandReadLanguageModel implements ICommand{

	static String commandName = "loadLanguageModel";

	@Override
	public String getCommandName() {
		return commandName;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commandName modelFilePath";
	}

	@Override
	public List getCommandAliases() {
		List<String> aliass = new ArrayList<String>(1);
		aliass.add(commandName);
		return aliass;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {


		final String path;
		if(args.length == 1){
			path = args[0];
		}
		else{
			path = "../languageModels/expertLangModelParams.yaml";
		}

		final ICommandSender fsender = sender;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				NaturalLanguageSolver.initializeCommandController(fsender, path);
			}
		});

		thread.start();

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

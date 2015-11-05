package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.h2r.burlapcraft.naturallanguge.NaturalLanguageSolver;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import burlap.oomdp.core.Domain;
import commands.data.TrainingElement;
import commands.data.TrainingElementParser;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.MinecraftDomainGenerator;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

public class CommandReadLanguageData implements ICommand {

	private final List aliases;
	
	public CommandReadLanguageData() {
		aliases = new ArrayList();
		aliases.add("readLanguageData");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "readLanguageData";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "readLanguageData <path>";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.addChatMessage(new ChatComponentText("Please enter the path to the directory to read from."));
			return;
		}
		
		final String path = args[0];

		final ICommandSender fsender = sender;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				NaturalLanguageSolver.initializeAndTrainCommandController(fsender, path);
			}
		});
		thread.start();


		/*
		DomainGeneratorReal rdg = new DomainGeneratorReal(30, 30, 30);
		Domain domain = rdg.generateDomain();
		
		StateParser sp = new StateJSONParser(domain);
		
		TrainingElementParser tep = new TrainingElementParser(domain, sp);
		List<TrainingElement> teList = tep.getTrainingElementDataset("languagedata", "txt");
		for (TrainingElement te : teList) {
			System.out.println(te.command);
			System.out.println(te.trajectory.actions);
		}
		*/
		
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

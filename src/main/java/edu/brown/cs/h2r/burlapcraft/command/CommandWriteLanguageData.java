package edu.brown.cs.h2r.burlapcraft.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import burlap.oomdp.auxiliary.StateParser;
import burlap.oomdp.auxiliary.common.StateJSONParser;
import burlap.oomdp.auxiliary.common.StateYAMLParser;
import burlap.oomdp.core.Domain;
import commands.data.TrainingElement;
import commands.data.TrainingElementParser;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorReal;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorSimulated;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandWriteLanguageData implements ICommand {

	private final List aliases;
	private int fileCount = 0;
	
	public CommandWriteLanguageData() {
		aliases = new ArrayList();
		aliases.add("writeLanguageData");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "writeLanguageData";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "writeLanguageData <path>";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.addChatMessage(new ChatComponentText("Please enter the path to the directory to write to."));
			return;
		}
		
		String path = args[0];
		
		DomainGeneratorReal rdg = new DomainGeneratorReal(30, 30, 30);
		Domain domain = rdg.generateDomain();
		
		StateParser sp = new StateJSONParser(domain);
		
		TrainingElementParser tep = new TrainingElementParser(domain, sp);
		
		fileCount = new File(path).list().length + 1;
		
		for (TrainingElement te : CommandLearn.teList) {
			tep.writeTrainingElementToFile(te, path + "/te" + fileCount + ".txt");
			fileCount += 1;
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

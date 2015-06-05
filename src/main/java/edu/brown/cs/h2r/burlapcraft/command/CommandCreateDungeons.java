package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerDungeonGeneration;

public class CommandCreateDungeons implements ICommand {

	private final List aliases;
	
	public CommandCreateDungeons() {
		aliases = new ArrayList();
		aliases.add(getCommandName());
	}
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommandName() {
		return "createDungeons";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "No arguments.";
	}

	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		System.out.println("Process command create dungeons: ");
		try {
		HandlerDungeonGeneration.doCreateDungeons(sender.getEntityWorld());
		} catch (Exception e) {
			System.out.println("Exception creating dungeons... printing stack trace, then ignoring.");
			e.printStackTrace();
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

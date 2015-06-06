package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerDungeonGeneration;

public class CommandResetDungeon implements ICommand {

	private final List aliases;
	
	public CommandResetDungeon() {
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
		return "resetDungeon";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "No arguments - all dungeon.  One argument, just the name of the dungeon.";
	}

	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		System.out.println("Resetting dungeons.");
		if (args.length == 0) {
			if (BurlapCraft.currentDungeon == null) {
				BurlapCraft.currentDungeon.regenerate(sender.getEntityWorld());
			}
		} else {
			for (String dname : args) {
				BurlapCraft.dungeonMap.get(dname).regenerate(sender.getEntityWorld());
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

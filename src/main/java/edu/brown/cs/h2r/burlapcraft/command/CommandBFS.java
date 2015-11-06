package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.solver.MinecraftSolver;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

public class CommandBFS implements ICommand {

	private final List aliases;
	
	public CommandBFS() {
		aliases = new ArrayList();
		aliases.add("bfs");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "bfs";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "bfs [closed|open] [all|noplace]\nIf closed/open not specified, closed is used.\nIf all/noplace not specified, all is used.";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		World world = sender.getEntityWorld();
		if (!world.isRemote) {
			if (args.length > 2) {
				sender.addChatMessage(new ChatComponentText("This command takes only 2 optional arguments: closed or open as the first, and all or noplace as the second."));
				return;
			}
			
			Dungeon dungeon = BurlapCraft.currentDungeon;
			
			if (dungeon == null) {
				sender.addChatMessage(new ChatComponentText("You are not inside a dungeon"));
				return;
			}


			boolean closed = true;
			boolean place = true;
			if(args.length == 1){
				if(args[0].equals("open")){
					closed = false;
				}
			}
			
			if(args.length == 2){
				if(args[0].equals("open")){
					closed = false;
				}
				if(args[1].equals("noplace")){
					place = false;
				}
			}

			final boolean fclosed = closed;
			final boolean fplace = place;

			Thread bthread = new Thread(new Runnable() {
				@Override
				public void run() {
					MinecraftSolver.plan(0, fclosed, fplace);
				}
			});

			bthread.start();


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

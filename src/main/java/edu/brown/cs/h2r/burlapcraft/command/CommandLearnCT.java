package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;

import burlap.oomdp.core.Domain;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorReal;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.helper.HelperCommandPair;
import edu.brown.cs.h2r.burlapcraft.solver.GotoSolver;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandLearnCT implements ICommand {

	private final List aliases;
	Domain domain;
	private List states;
	public static List learnList = new ArrayList<HelperCommandPair>();
	public static boolean endLearning = false;
	
	public CommandLearnCT() {
		aliases = new ArrayList();
		aliases.add("learnCT");
		DomainGeneratorReal rdg = new DomainGeneratorReal(30, 30, 30);
		domain = rdg.generateDomain();
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "learnCT";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "learnCT <string command here>";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.addChatMessage(new ChatComponentText("Please enter a command to learn transitions for."));
			return;
		}
		
		final String commandToLearn = StringUtils.join(args, " ");
		
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (endLearning) {
					HelperCommandPair pair = new HelperCommandPair(commandToLearn, states);
					learnList.add(pair);
					states = null;
					endLearning = false;
					timer.cancel();
				}
				else {
					states.add(StateGenerator.getCurrentState(domain, BurlapCraft.currentDungeon));
				}
			}
		}, 0, 1000);
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

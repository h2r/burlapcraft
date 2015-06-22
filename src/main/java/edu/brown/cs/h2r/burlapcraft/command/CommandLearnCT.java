package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.Action;
import burlap.oomdp.singleagent.GroundedAction;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorReal;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorSimulated;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.helper.HelperLanguageTriplet;
import edu.brown.cs.h2r.burlapcraft.solver.GotoSolver;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandLearnCT implements ICommand {

	private final List aliases;
	Domain domain;
	private ArrayList<HelperLanguageTriplet> learnList = new ArrayList<HelperLanguageTriplet>();
	public static boolean endLearning = false;
	
	public CommandLearnCT() {
		aliases = new ArrayList();
		aliases.add("learnCT");
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
		DomainGeneratorSimulated sdg = new DomainGeneratorSimulated(StateGenerator.getMap(BurlapCraft.currentDungeon));
		domain = sdg.generateDomain();
		final ArrayList<State> states = new ArrayList<State>();
		
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (endLearning) {
					inferActions(commandToLearn, states);
					endLearning = false;
					timer.cancel();
				}
				else {
					State curState = StateGenerator.getCurrentState(domain, BurlapCraft.currentDungeon);
					if (states.size() == 0) {
						states.add(curState);
					}
					else {
						if (!curState.equals(states.get(states.size() - 1))) {
							states.add(curState);
						}
					}
				}
			}
		}, 0, 500);
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

	public void inferActions(String commandToLearn, ArrayList<State> states) {
		ArrayList<Action> actions = new ArrayList<Action>();
		for (int i = 1; i < states.size(); i++) {
			State initialState = states.get(i - 1);
			State targetState = states.get(i);
			for (GroundedAction groundedAction : Action.getAllApplicableGroundedActionsFromActionList(domain.getActions(), initialState)) {
				if (groundedAction.executeIn(initialState).equals(targetState)) {
					actions.add(groundedAction.action);
				}
			}
		}
		HelperLanguageTriplet triplet = new HelperLanguageTriplet(commandToLearn, states, actions);
		System.out.println("StateList: " + states);
		System.out.println("ActionList: " + actions);
		learnList.add(triplet);
	}
	
}

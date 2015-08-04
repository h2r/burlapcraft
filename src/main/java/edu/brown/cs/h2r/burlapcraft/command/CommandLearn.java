package edu.brown.cs.h2r.burlapcraft.command;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;

import commands.data.TrainingElement;
import commands.data.Trajectory;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.Action;
import burlap.oomdp.singleagent.GroundedAction;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorReal;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorSimulated;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerEvents;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGestureTuple;
import edu.brown.cs.h2r.burlapcraft.solver.GotoSolver;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandLearn implements ICommand {

	private final List aliases;
	Domain domain;
	private ArrayList<TrainingElement> teList = new ArrayList<TrainingElement>();
	public static boolean endLearning = false;
	
	public CommandLearn() {
		aliases = new ArrayList();
		aliases.add("learn");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "learn";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "learn [-gesture] <if gesture is specified enter time in milliseconds here> <string command here if gesture is not specified>";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.addChatMessage(new ChatComponentText("Please enter a command to learn transitions for or use [-gesture] to track cursor trajectory."));
			return;
		}
		
		if (args.length == 2 && args[0].equals("-gesture")) {
			final ArrayList<HelperGestureTuple> gestureTuples = new ArrayList<HelperGestureTuple>();
			int time = Integer.valueOf(args[1]);
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (endLearning) {
						try {
							writeGestureTuplesToFile(gestureTuples);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						endLearning = false;
						timer.cancel();
					}
					else {
						EntityPlayer player = HandlerEvents.player;
						float pitch = player.rotationPitch;
						float yaw = player.rotationYaw;
						gestureTuples.add(new HelperGestureTuple(pitch, yaw));
					}
				}
			}, 0, time);
		}
		else {
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
			}, 0, 100);
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
	
	public void writeGestureTuplesToFile(ArrayList gestureTuples) throws IOException {
		FileWriter fw = new FileWriter("out.txt");
		for (int i = 0; i < gestureTuples.size(); i++) {
			HelperGestureTuple tuple = (HelperGestureTuple) gestureTuples.get(i);
			fw.write(tuple.getPitch() + " " + tuple.getYaw() + "\n");
		}
		fw.close();
	}

	public void inferActions(String commandToLearn, ArrayList states) {
		
		State start = (State) states.get(0);
		State end = (State) states.get(states.size() - 1);
		
		for (GroundedAction groundedAction : Action.getAllApplicableGroundedActionsFromActionList(domain.getActions(), start)) {
			if (groundedAction.executeIn(start).equals(end)) {
				ArrayList<State> stateList = new ArrayList<State>();
				stateList.add(start);
				stateList.add(end);
				ArrayList<GroundedAction> actionList = new ArrayList<GroundedAction>();
				actionList.add(groundedAction);
				TrainingElement te = new TrainingElement(commandToLearn, new Trajectory(stateList, actionList));
				teList.add(te);
				System.out.println(teList);
				return;
			}
		}
		
		ArrayList<ArrayList<State>> trackStates = new ArrayList<ArrayList<State>>();
		ArrayList<State> firstList = new ArrayList<State>();
		firstList.add(start);
		trackStates.add(firstList);
		for (int i = 0; i < states.size() - 1; i++) {
			ArrayList<State> nullList = new ArrayList<State>();
			trackStates.add(nullList);
		}
		
		for (int i = 0; i < states.size(); i++) {
			State curState = (State) states.get(i);
			ArrayList<State> curStateList = trackStates.get(i);
			for (int j = 0; j < i; j++) {
				State prevState = (State) states.get(j);
				ArrayList<State> prevStateList = trackStates.get(j);
				
				if (prevStateList.size() != 0) {
					if (prevState.equals(start) || prevStateList.get(0).equals(start)) {
						for (GroundedAction groundedAction : Action.getAllApplicableGroundedActionsFromActionList(domain.getActions(), prevState)) {
							if (groundedAction.executeIn(prevState).equals(curState)) {
								if (prevStateList.size() + 1 < curStateList.size() || curStateList.size() == 0) {
									ArrayList<State> combinedList = new ArrayList<State>();
									for (State s : prevStateList) {
										combinedList.add(s);
									}
									combinedList.add(curState);
									trackStates.set(i, combinedList);
								}
							} 
						}
					}
				}
			}
		}
		
		ArrayList<State> finalStateList = trackStates.get(states.size() - 1);
		ArrayList<GroundedAction> finalActionList = new ArrayList<GroundedAction>();
		
		for (int i = 0; i < finalStateList.size() - 1; i++) {
			State curState = finalStateList.get(i);
			State nextState = finalStateList.get(i + 1);
			for (GroundedAction groundedAction : Action.getAllApplicableGroundedActionsFromActionList(domain.getActions(), curState)) {
				if (groundedAction.executeIn(curState).equals(nextState)) {
					finalActionList.add(groundedAction);
					break;
				}
			}
		}
		
		TrainingElement te = new TrainingElement(commandToLearn, new Trajectory(finalStateList, finalActionList));
		teList.add(te);
	}
}

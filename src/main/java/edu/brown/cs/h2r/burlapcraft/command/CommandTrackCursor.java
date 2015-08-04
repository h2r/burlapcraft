package edu.brown.cs.h2r.burlapcraft.command;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import org.apache.commons.lang3.StringUtils;

import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.Action;
import burlap.oomdp.singleagent.GroundedAction;
import commands.data.TrainingElement;
import commands.data.Trajectory;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorSimulated;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerEvents;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGestureTuple;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

public class CommandTrackCursor implements ICommand {
	
	private final List aliases;
	
	public CommandTrackCursor() {
		aliases = new ArrayList();
		aliases.add("trackCursor");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "trackCursor";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "trackCursor <specify time in milliseconds here>";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.addChatMessage(new ChatComponentText("Please enter the interval in milliseconds between each recording."));
			return;
		}
		
		final ArrayList<HelperGestureTuple> gestureTuples = new ArrayList<HelperGestureTuple>();
		int time = Integer.valueOf(args[1]);
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (true) {
					try {
						writeGestureTuplesToFile(gestureTuples);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
	
}

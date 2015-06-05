package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerDungeonGeneration;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerEvents;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;

public class CommandSmoothMove implements ICommand {

	private final List aliases;
	
	public CommandSmoothMove() {
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
		return "smoothMove";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/smoothMove";
	}

	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		System.out.println("Process command smoothMove: ");
		
		if (args.length == 0) {
			sender.addChatMessage(new ChatComponentText("Invalid argument"));
			return;
		}
		
		if (args.length > 1) {
			sender.addChatMessage(new ChatComponentText("This command takes just one argument"));
			return;
		}
		
		String directionName = args[0];
		
		try {
		
			if (directionName.equals("n") || directionName.equals("north")) {
				HelperActions.faceNorth();
				sender.addChatMessage(new ChatComponentText("Facing north. Smoothly."));
			} else if (directionName.equals("s") || directionName.equals("south")) {
				HelperActions.faceSouth();
				sender.addChatMessage(new ChatComponentText("Facing south. Smoothly."));
			} else if (directionName.equals("e") || directionName.equals("east")) {
				HelperActions.faceEast();
				sender.addChatMessage(new ChatComponentText("Facing east. Smoothly."));
			} else if (directionName.equals("w") || directionName.equals("west")) {
				HelperActions.faceWest();
				sender.addChatMessage(new ChatComponentText("Facing west. Smoothly."));
			} else {
				sender.addChatMessage(new ChatComponentText("Invalid argument"));
			}
			
			
			/*final EntityPlayer player = HelperActions.player;
			final double yawTarget = -180;
			final double qDecay = 90.0;
			final double snapThresh = 0.025;
			final double minUpdate = 0.025;
			final long timerPeriod = 2;
			final Timer timer = new Timer();
			  timer.schedule(new TimerTask() {
				  @Override
				  public void run() {
					  if (player.rotationYaw == yawTarget) {
						  System.out.println("smoothMove: target reached.");
						  timer.cancel();
					  } else if (Math.abs(yawTarget - player.rotationYaw) > snapThresh){
						  double update = (yawTarget - player.rotationYaw)/qDecay;		  
						  if (Math.abs(update) < minUpdate) {
							  update = Math.signum(update) * minUpdate;
						  } else {
						  }
						  player.rotationYaw += update;
						  System.out.println("smoothMove: target = " + yawTarget + " current yaw = " + player.rotationYaw + " so adding.");
					  } else {
						  player.rotationYaw = (float)yawTarget;
					  }
				  }
			  }, 0, timerPeriod);*/
			
			
			
			
		} catch (Exception e) {
			System.out.println("Exception smoothMove... printing stack trace, then ignoring.");
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

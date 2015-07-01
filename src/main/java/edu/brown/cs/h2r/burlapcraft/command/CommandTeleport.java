package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerDungeonGeneration;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerEvents;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

public class CommandTeleport implements ICommand {
	
	private final List aliases;
	
	public CommandTeleport() {
		aliases = new ArrayList();
		aliases.add("teleport");
		aliases.add("tele");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "teleport";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "teleport <dungeon name>";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		
		if (!HandlerDungeonGeneration.dungeonsCreated) {
			sender.addChatMessage(new ChatComponentText("Dungeons aren't yet created."));
			return;
		}
		World world = sender.getEntityWorld();
		if (!world.isRemote) {
			if (args.length == 0) {
				sender.addChatMessage(new ChatComponentText("Invalid argument"));
				return;
			}
			
			if (args.length != 1 && args.length != 3) {
				sender.addChatMessage(new ChatComponentText("This command takes just one argument, or 3 integers for pose"));
				return;
			}
			EntityPlayer player = HandlerEvents.player;
			if (args.length == 3) {
				int x = Integer.valueOf(args[0]);
				int y = Integer.valueOf(args[1])	;
				int z = Integer.valueOf(args[2]);	
				Pose newp= Pose.fromXyz(x, y, z);
				HelperActions.setPlayerPosition(player, newp);
				
			}
			
			String dungeonName = args[0];

			
			if (dungeonName.equals("out")) {
				Pose spawnPoint = Pose.fromXyz(HandlerDungeonGeneration.playerSpawnPose.getX(), HandlerDungeonGeneration.playerSpawnPose.getY(), HandlerDungeonGeneration.playerSpawnPose.getZ());
				HelperActions.setPlayerPosition(player, spawnPoint);
				return;
			} 
			
			Dungeon d = BurlapCraft.dungeonMap.get(dungeonName);
			if (d == null) {
				ArrayList dungeonNames = new ArrayList(BurlapCraft.dungeonMap.keySet());
				Collections.sort(dungeonNames);
				sender.addChatMessage(new ChatComponentText("No dungeon '" + dungeonName + "'.  Dungeons are: "  + dungeonNames));
				return;
			}

			StateGenerator.blockNameMap.clear();
			StateGenerator.invBlockNameMap.clear();
			Pose offset = d.getPlayerStartOffset();
			Pose playerPose = d.getPose().add(offset);
			HelperActions.setPlayerPosition(player, playerPose);

			BurlapCraft.currentDungeon = d;
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

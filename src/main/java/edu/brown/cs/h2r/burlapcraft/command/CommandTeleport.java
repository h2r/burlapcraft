package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerDungeonGeneration;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerEvents;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.Dungeon;

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
			
			if (args.length > 1) {
				sender.addChatMessage(new ChatComponentText("This command takes just one argument"));
				return;
			}
			
			EntityPlayer player = HandlerEvents.player;
			String dungeonName = args[0];

			
			if (dungeonName.equals("out")) {
				Pose spawnPoint = Pose.fromXyz(HandlerDungeonGeneration.playerSpawnPos.x, HandlerDungeonGeneration.playerSpawnPos.y, HandlerDungeonGeneration.playerSpawnPos.z);
				HelperActions.setPlayerPosition(player, spawnPoint);
				return;
			}
			Dungeon dungeonID = Dungeon.fromString(dungeonName);			
			BurlapCraft.dungeonID = dungeonID;
			
			if (dungeonID == Dungeon.FINDER) {
				// start x, y and z of player within dungeon
				Pose offset = Pose.fromXyz(1.5, 5, 3);
				Pose playerPose = HandlerDungeonGeneration.finderPose.add(offset);
				// teleport the player to finder dungeon
				HelperActions.setPlayerPosition(player, playerPose);
			} else if (dungeonID == Dungeon.TINY_BRIDGE) {
				// start x, y and z of agent within dungeon
				//Pose offset = Pose.fromXyz(1.5,  5,  3	);
				Pose offset = Pose.fromXyz(1, 5, 3);
				// teleport the player to bridge dungeon
				Pose playerPose = HandlerDungeonGeneration.tinyBridgePose.add(offset);
				HelperActions.setPlayerPosition(player, playerPose);
			} else if (dungeonID == Dungeon.SMALL_BRIDGE) {
				// start x, y and z of agent within dungeon
				//Pose offset = Pose.fromXyz(1.5,  5,  3	);
				Pose offset = Pose.fromXyz(1.5, 5, 3);
				// teleport the player to bridge dungeon
				
				Pose playerPose = HandlerDungeonGeneration.smallBridgePose.add(offset);
				System.out.println("small bridge pose: " + HandlerDungeonGeneration.smallBridgePose);
				System.out.println("player pose: " + playerPose);
				HelperActions.setPlayerPosition(player, playerPose);
			} else if (dungeonID == Dungeon.GRID) {
				Pose offset = Pose.fromXyz(1.5, 5, 3);
				Pose playerPose = HandlerDungeonGeneration.gridPose.add(offset);
				HelperActions.setPlayerPosition(player, playerPose);
			} else {
				throw new IllegalStateException("Bad dungeon ID: " + dungeonID);
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

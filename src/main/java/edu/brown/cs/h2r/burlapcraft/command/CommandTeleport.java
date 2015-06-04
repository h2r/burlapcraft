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
			int dungeonID = 0;
			
			if (dungeonName.equals("out")) {
				Pose spawnPoint = Pose.fromXyz(HandlerDungeonGeneration.playerSpawnPos.x, HandlerDungeonGeneration.playerSpawnPos.y, HandlerDungeonGeneration.playerSpawnPos.z);
				HelperActions.setPlayerPosition(player, spawnPoint);
				return;
			}
			
			for (int i = 0; i < HelperNameSpace.dungeonNameList.length; i++) {
				if (dungeonName.equals(HelperNameSpace.dungeonNameList[i])) {
					dungeonID = i;
				}
			}
			
			
			if (dungeonID == 1) {
				// start x, y and z of player within dungeon
				double playerFinderX = 1.5; 
				double playerFinderY = 1;
				double playerFinderZ = 3;
				Pose playerPose = Pose.fromXyz(HandlerDungeonGeneration.finderX + playerFinderX, HandlerDungeonGeneration.finderY + playerFinderY + 5, HandlerDungeonGeneration.finderZ + playerFinderZ);
				// teleport the player to finder dungeon
				HelperActions.setPlayerPosition(player, playerPose);
						
				// update the dugeonLocID
				BurlapCraft.dungeonLocID = 1;
			} else if (dungeonID == 2) {
				// start x, y and z of agent within dungeon
				double playerBridgeX = 1.5; 
				double playerBridgeY = 2;
				double playerBridgeZ = 4;
					
				// teleport the player to bridge dungeon
				Pose playerPose = Pose.fromXyz(HandlerDungeonGeneration.bridgeX + playerBridgeX, HandlerDungeonGeneration.bridgeY + playerBridgeY + 5, HandlerDungeonGeneration.bridgeZ + playerBridgeZ);
				HelperActions.setPlayerPosition(player, playerPose);
				// update the dungeonLocID
				BurlapCraft.dungeonLocID = 2;
			} else if (dungeonID == 3) {
				double playerGridX = 1.5;
				double playerGridY = 1;
				double playerGridZ = 3;
				Pose playerPose = Pose.fromXyz(HandlerDungeonGeneration.gridX + playerGridX, 
						HandlerDungeonGeneration.gridY + playerGridY + 5,	 
						HandlerDungeonGeneration.gridZ + playerGridZ);
				HelperActions.setPlayerPosition(player, playerPose);
				BurlapCraft.dungeonLocID = 3;
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

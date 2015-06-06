package edu.brown.cs.h2r.burlapcraft.handler;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.FinderDungeon;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.GridDungeon;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.MazeDungeon;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.SmallBridgeDungeon;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.TinyBridgeDungeon;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;

public class HandlerDungeonGeneration implements IWorldGenerator {
	
	public static Pose playerSpawnPose;
	private static Minecraft mc = Minecraft.getMinecraft();
	public static boolean dungeonsCreated = false;
	public static boolean currentlyGeneratingDungeons = false;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		// TODO Auto-generated method stub

		switch (world.provider.dimensionId) {

		case -1:
			generateNether(world, random, chunkX * 16, chunkZ * 16);

		case 0:
			generateSurface(world, random, chunkX * 16, chunkZ * 16);

		case 1:
			generateEnd(world, random, chunkX * 16, chunkZ * 16);

		}

	}

	private void generateEnd(World world, Random random, int i, int j) {

	}

	private void generateNether(World world, Random random, int i, int j) {

	}

	private void generateSurface(World world, Random random, int i, int j) {
		if(mc.thePlayer != null && !dungeonsCreated) {
			doCreateDungeons(world);
		}
	}
	
	public static void doCreateDungeons(World world) {
		if (currentlyGeneratingDungeons) {
			return;
		} else {
			currentlyGeneratingDungeons = true;
		}
		ChunkCoordinates coordinates = world.getSpawnPoint();
		
		//playerSpawnPos = getPlayerPosition();
		playerSpawnPose = Pose.fromXyz(coordinates.posX, 30, coordinates.posZ);
		int height = 50;
		Pose finderPose = Pose.fromXyz(playerSpawnPose.getX(), playerSpawnPose.getY() + height, playerSpawnPose.getZ());
		Pose tinyBridgePose = Pose.fromXyz(playerSpawnPose.getX() + 20, playerSpawnPose.getY() + height, playerSpawnPose.getZ() + 10);
		Pose smallBridgePose = Pose.fromXyz(playerSpawnPose.getX() + 30, playerSpawnPose.getY() + height, playerSpawnPose.getZ() + 10);
		
		Pose gridPose = Pose.fromXyz(playerSpawnPose.getX() - 20, playerSpawnPose.getY() + height, playerSpawnPose.getZ() - 10);
		System.out.println("Setting gridPose: " + gridPose);
		
		Pose mazePose = Pose.fromXyz(playerSpawnPose.getX() - 40, playerSpawnPose.getY() + height, playerSpawnPose.getZ() - 10);
		System.out.println("Setting mazePose: " + mazePose);
		
		BurlapCraft.registerDungeon(new FinderDungeon(finderPose));
		BurlapCraft.registerDungeon(new TinyBridgeDungeon(tinyBridgePose));
		BurlapCraft.registerDungeon(new SmallBridgeDungeon(smallBridgePose));
		BurlapCraft.registerDungeon(new GridDungeon(gridPose));
		BurlapCraft.registerDungeon(new MazeDungeon(mazePose));
		
		for (Dungeon d : BurlapCraft.dungeons) {
			d.regenerate(world);
		}
		
		dungeonsCreated = true;
		currentlyGeneratingDungeons = false;
	}
	
	private static HelperPos getPlayerPosition() {
	  
		int x = MathHelper.floor_double(mc.thePlayer.posX);
		int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY + 0.05D);
		int z = MathHelper.floor_double(mc.thePlayer.posZ);
		
		return new HelperPos(x, y, z);
    
	}

}

package edu.brown.cs.h2r.burlapcraft.handler;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.DungeonGeneratorFinder;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.DungeonGeneratorGrid;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.DungeonGeneratorSmallBridge;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.DungeonGeneratorTinyBridge;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;

public class HandlerDungeonGeneration implements IWorldGenerator {
	public static Pose finderPose;
	public static Pose tinyBridgePose;
	public static Pose smallBridgePose;
	public static Pose gridPose;
	public static HelperPos playerSpawnPos;
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
		playerSpawnPos = getPlayerPosition();
		int height = 50;
		finderPose = Pose.fromXyz(playerSpawnPos.x, playerSpawnPos.y + height, playerSpawnPos.z);
		
		tinyBridgePose = Pose.fromXyz(playerSpawnPos.x + 20, playerSpawnPos.y + height, playerSpawnPos.z + 10);
		
		smallBridgePose = Pose.fromXyz(playerSpawnPos.x + 30, playerSpawnPos.y + height, playerSpawnPos.z + 10);
		
		
		gridPose = Pose.fromXyz(playerSpawnPos.x - 20, playerSpawnPos.y + height, playerSpawnPos.z - 10);
		System.out.println("Setting gridPose: " + gridPose);
		
		
		DungeonGeneratorFinder.generate(world, finderPose);
		DungeonGeneratorTinyBridge.generate(world, tinyBridgePose);
		DungeonGeneratorSmallBridge.generate(world, smallBridgePose);
		DungeonGeneratorGrid.generate(world, gridPose);
		
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

package edu.brown.cs.h2r.burlapcraft.handler;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.DungeonGeneratorFinder;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.DungeonGeneratorGrid;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.DungeonGeneratorTinyBridge;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class HandlerDungeonGeneration implements IWorldGenerator {
	public static Pose finderPose;
	public static Pose tinyBridgePose;
	public static Pose gridPose;
	public static HelperPos playerSpawnPos;
	private static Minecraft mc = Minecraft.getMinecraft();
	public static boolean dungeonsCreated = false;

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
			System.out.println("Generating dungeons.");
			playerSpawnPos = getPlayerPosition();
			
			finderPose = Pose.fromXyz(playerSpawnPos.x, playerSpawnPos.y + 50, playerSpawnPos.z);
			
	 		tinyBridgePose = Pose.fromXyz(playerSpawnPos.x + 10, playerSpawnPos.y + 50, playerSpawnPos.z + 10);
			
	 		gridPose = Pose.fromXyz(playerSpawnPos.x - 10, playerSpawnPos.y + 50, playerSpawnPos.z - 10);
			
			
			
			new DungeonGeneratorFinder().generate(world, random, finderPose);
			new DungeonGeneratorTinyBridge().generate(world, random, tinyBridgePose);
			new DungeonGeneratorGrid().generate(world, random, gridPose);
			
			dungeonsCreated = true;
			
		}
		
	}
	
	private HelperPos getPlayerPosition() {
	  
		int x = MathHelper.floor_double(mc.thePlayer.posX);
		int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY + 0.05D);
		int z = MathHelper.floor_double(mc.thePlayer.posZ);
		
		return new HelperPos(x, y, z);
    
	}

}

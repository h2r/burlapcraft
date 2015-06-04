package edu.brown.cs.h2r.burlapcraft.handler;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.DungeonGeneratorBridge;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.DungeonGeneratorFinder;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.DungeonGeneratorGrid;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;

public class HandlerDungeonGeneration implements IWorldGenerator {
	
	public static int finderX;
	public static int finderY;
	public static int finderZ;
	public static int bridgeX;
	public static int bridgeY;
	public static int bridgeZ;
	public static int gridX;
	public static int gridY;
	public static int gridZ;
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
			
			finderX = playerSpawnPos.x;
			finderY = playerSpawnPos.y + 30;
			finderZ = playerSpawnPos.z;
			
	 		bridgeX = playerSpawnPos.x + 10;
			bridgeY = playerSpawnPos.y + 30;
			bridgeZ = playerSpawnPos.z;
			
			gridX = playerSpawnPos.x - 10;
			gridY = playerSpawnPos.y + 30;
			gridZ = playerSpawnPos.z;
			
			
			
			new DungeonGeneratorFinder().generate(world, random, finderX, finderY, finderZ);
			new DungeonGeneratorBridge().generate(world, random, bridgeX, bridgeY, bridgeZ);
			new DungeonGeneratorGrid().generate(world, random, gridX, gridY, gridZ);
			
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

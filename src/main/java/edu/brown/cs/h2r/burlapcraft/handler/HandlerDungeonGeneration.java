package edu.brown.cs.h2r.burlapcraft.handler;

import java.util.Random;


import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.DungeonGeneratorBridge;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.DungeonGeneratorFinder;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;
import edu.brown.cs.h2r.burlapcraft.item.ItemFinderWand;
import net.minecraft.client.settings.KeyBinding;

public class HandlerDungeonGeneration implements IWorldGenerator {
	
	public static int finderX;
	public static int finderY;
	public static int finderZ;
	public static int bridgeX;
	public static int bridgeY;
	public static int bridgeZ;
	public static HelperPos playerSpawnPos;
	private static Minecraft mc = Minecraft.getMinecraft();

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
		// TODO Auto-generated method stub

	}

	private void generateNether(World world, Random random, int i, int j) {
		// TODO Auto-generated method stub

	}

	private void generateSurface(World world, Random random, int i, int j) {
		// TODO Auto-generated method stub
		
		if(mc.thePlayer != null) {
			
			playerSpawnPos = getPlayerPosition();
			
			finderX = playerSpawnPos.x;
			finderY = playerSpawnPos.y + 40;
			finderZ = playerSpawnPos.z;
			bridgeX = playerSpawnPos.x + 40;
			bridgeY = playerSpawnPos.y + 40;
			bridgeZ = playerSpawnPos.z;
			
			new DungeonGeneratorFinder().generate(world, random, finderX, finderY, finderZ);
			new DungeonGeneratorBridge().generate(world, random, bridgeX, bridgeY, bridgeZ);
			
		}
		
	}
	
	private HelperPos getPlayerPosition() {
	  
		int x = MathHelper.floor_double(mc.thePlayer.posX);
		int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY + 0.05D);
		int z = MathHelper.floor_double(mc.thePlayer.posZ);
		
		return new HelperPos(x, y, z);
    
	}

}

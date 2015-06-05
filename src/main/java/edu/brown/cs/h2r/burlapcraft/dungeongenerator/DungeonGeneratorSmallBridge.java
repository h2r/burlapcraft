package edu.brown.cs.h2r.burlapcraft.dungeongenerator;

import java.util.Random;

import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class DungeonGeneratorSmallBridge {

	public static boolean generate(World world, Pose dungeonPose) {
		int x = (int) dungeonPose.getX();
		int y = (int) dungeonPose.getY();
		int z = (int) dungeonPose.getZ();
		return generate(world, x, y, z);
	}

	public static boolean generate(World world, int x, int y, int z) {
		
		
		System.out.println("Making small bridge dungeon at " + x + "," + y + "," + z);
		int w = 8;
		for (int ax = 0; ax < w; ax++) {
			for (int az = 0; az < w; az++) {
				world.setBlock(x+ax, y+0, z+az, Block.getBlockById(7));
			}
		}
		for (int ax = 0; ax < 10; ax++) {
			world.setBlock(x+ax, y+0, z+0, Block.getBlockById(7));
			world.setBlock(x+ax, y+1, z+0, Block.getBlockById(7));
			world.setBlock(x+ax, y+2, z+0, Block.getBlockById(7));
			world.setBlock(x+ax, y+0, z+w, Block.getBlockById(7));
			world.setBlock(x+ax, y+1, z+w, Block.getBlockById(7));
			world.setBlock(x+ax, y+2, z+w, Block.getBlockById(7));
		}	

		for (int az = 0; az < 10; az++) {
			world.setBlock(x+0, y+0, z+az, Block.getBlockById(7));
			world.setBlock(x+0, y+1, z+az, Block.getBlockById(7));
			world.setBlock(x+0, y+2, z+az, Block.getBlockById(7));
			world.setBlock(x+w, y+0, z+az, Block.getBlockById(7));
			world.setBlock(x+w, y+1, z+az, Block.getBlockById(7));
			world.setBlock(x+w, y+2, z+az, Block.getBlockById(7));
		}
		world.setBlock(x + w - 1, y+0, z+w - 1, Block.getBlockById(41));
		
		return true;
		

	}

}

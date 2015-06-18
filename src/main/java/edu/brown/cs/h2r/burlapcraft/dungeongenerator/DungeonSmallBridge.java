package edu.brown.cs.h2r.burlapcraft.dungeongenerator;

import java.util.Random;

import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class DungeonSmallBridge extends Dungeon {
	
	public DungeonSmallBridge(Pose pose) {
		super("small_bridge", pose, 10, 10, 5, Pose.fromXyz(1.5, 5, 3));
		
	}

	protected void generate(World world, int x, int y, int z) {
		
		
		System.out.println("Making small bridge dungeon at " + x + "," + y + "," + z);
		int w = 6;
		makeAir(world, x, y, z, w);
		for (int ax = 0; ax < w; ax++) {
			for (int az = 0; az < w; az++) {
				world.setBlock(x+ax, y+0, z+az, Block.getBlockById(7));
				world.setBlock(x+ax, y+1, z+az, Block.getBlockById(7));
			}
		}
		for (int ax = 0; ax < w; ax++) {
			world.setBlock(x+ax, y+1, z+0, Block.getBlockById(7));
			world.setBlock(x+ax, y+2, z+0, Block.getBlockById(7));
			world.setBlock(x+ax, y+3, z+0, Block.getBlockById(7));
			world.setBlock(x+ax, y+1, z+w, Block.getBlockById(7));
			world.setBlock(x+ax, y+2, z+w, Block.getBlockById(7));
			world.setBlock(x+ax, y+3, z+w, Block.getBlockById(7));
		}	

		for (int az = 0; az < w; az++) {
			world.setBlock(x+0, y+1, z+az, Block.getBlockById(7));
			world.setBlock(x+0, y+2, z+az, Block.getBlockById(7));
			world.setBlock(x+0, y+3, z+az, Block.getBlockById(7));
			world.setBlock(x+w, y+1, z+az, Block.getBlockById(7));
			world.setBlock(x+w, y+2, z+az, Block.getBlockById(7));
			world.setBlock(x+w, y+3, z+az, Block.getBlockById(7));
		}
		
		
		for (int az = 1; az < w; az++) {
			world.setBlock(x + w/2, y + 1, z + az, Block.getBlockById(11));
			world.setBlock(x + w/2, y + 0, z + az, Block.getBlockById(7));
		}
		
		world.setBlock(x + w - 1, y + 1, z + w - 1, Block.getBlockById(41)); // gold goal block
		world.setBlock(x + 1, y + 2, z + w - 1, BurlapCraft.burlapStone); // 
		
	}

}

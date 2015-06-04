package edu.brown.cs.h2r.burlapcraft.dungeongenerator;

import java.util.Random;

import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class DungeonGeneratorSmallBridge extends WorldGenerator {

	public boolean generate(World world, Random rand, Pose dungeonPose) {
		int x = (int) dungeonPose.getX();
		int y = (int) dungeonPose.getY();
		int z = (int) dungeonPose.getX();
		return generate(world, rand, x, y, z);
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		
		world.setBlock(x+0, y+0, z+0, Block.getBlockById(7));
		world.setBlock(x+1, y+0, z+0, Block.getBlockById(7));
		world.setBlock(x+2, y+0, z+0, Block.getBlockById(7));
		world.setBlock(x+3, y+0, z+0, Block.getBlockById(7));
		world.setBlock(x+0, y+0, z+1, Block.getBlockById(7));
		world.setBlock(x+1, y+0, z+1, Block.getBlockById(7));
		world.setBlock(x+2, y+0, z+1, Block.getBlockById(7));
		world.setBlock(x+3, y+0, z+1, Block.getBlockById(7));
		world.setBlock(x+0, y+0, z+2, Block.getBlockById(7));
		world.setBlock(x+1, y+0, z+2, Block.getBlockById(7));
		world.setBlock(x+2, y+0, z+2, Block.getBlockById(7));
		world.setBlock(x+3, y+0, z+2, Block.getBlockById(7));
		world.setBlock(x+0, y+0, z+3, Block.getBlockById(7));
		world.setBlock(x+1, y+0, z+3, Block.getBlockById(7));
		world.setBlock(x+2, y+0, z+3, Block.getBlockById(7));
		world.setBlock(x+3, y+0, z+3, Block.getBlockById(7));
		world.setBlock(x+0, y+0, z+4, Block.getBlockById(7));
		world.setBlock(x+1, y+0, z+4, Block.getBlockById(7));
		world.setBlock(x+2, y+0, z+4, Block.getBlockById(7));
		world.setBlock(x+3, y+0, z+4, Block.getBlockById(7));

		world.setBlock(x+0, y+1, z+0, Block.getBlockById(7));
		world.setBlock(x+1, y+1, z+0, Block.getBlockById(7));
		world.setBlock(x+2, y+1, z+0, Block.getBlockById(7));
		world.setBlock(x+3, y+1, z+0, Block.getBlockById(7));
		world.setBlock(x+0, y+1, z+1, Block.getBlockById(7));
		world.setBlock(x+1, y+1, z+1, Block.getBlockById(7));
		world.setBlock(x+2, y+1, z+1, Block.getBlockById(7));
		world.setBlock(x+3, y+1, z+1, Block.getBlockById(7));
		world.setBlock(x+0, y+1, z+2, Block.getBlockById(7));
		world.setBlock(x+1, y+1, z+2, Block.getBlockById(11));
		world.setBlock(x+2, y+1, z+2, Block.getBlockById(11));
		world.setBlock(x+3, y+1, z+2, Block.getBlockById(7));
		world.setBlock(x+0, y+1, z+3, Block.getBlockById(7));
		world.setBlock(x+1, y+1, z+3, Block.getBlockById(7));
		world.setBlock(x+2, y+1, z+3, Block.getBlockById(7));
		world.setBlock(x+3, y+1, z+3, Block.getBlockById(7));
		world.setBlock(x+0, y+1, z+4, Block.getBlockById(7));
		world.setBlock(x+1, y+1, z+4, Block.getBlockById(7));
		world.setBlock(x+2, y+1, z+4, Block.getBlockById(7));
		world.setBlock(x+3, y+1, z+4, Block.getBlockById(7));

		world.setBlock(x+0, y+2, z+0, Block.getBlockById(7));
		world.setBlock(x+1, y+2, z+0, Block.getBlockById(7));
		world.setBlock(x+2, y+2, z+0, Block.getBlockById(7));
		world.setBlock(x+3, y+2, z+0, Block.getBlockById(7));
		world.setBlock(x+0, y+2, z+1, Block.getBlockById(7));
		world.setBlock(x+1, y+2, z+1, Block.getBlockById(41));
		world.setBlock(x+2, y+2, z+1, Block.getBlockById(0));
		world.setBlock(x+3, y+2, z+1, Block.getBlockById(7));
		world.setBlock(x+0, y+2, z+2, Block.getBlockById(7));
		world.setBlock(x+1, y+2, z+2, Block.getBlockById(0));
		world.setBlock(x+2, y+2, z+2, Block.getBlockById(0));
		world.setBlock(x+3, y+2, z+2, Block.getBlockById(7));
		world.setBlock(x+0, y+2, z+3, Block.getBlockById(7));
		world.setBlock(x+1, y+2, z+3, Block.getBlockById(0));
		world.setBlock(x+2, y+2, z+3, BurlapCraft.burlapStone);
		world.setBlock(x+3, y+2, z+3, Block.getBlockById(7));
		world.setBlock(x+0, y+2, z+4, Block.getBlockById(7));
		world.setBlock(x+1, y+2, z+4, Block.getBlockById(7));
		world.setBlock(x+2, y+2, z+4, Block.getBlockById(7));
		world.setBlock(x+3, y+2, z+4, Block.getBlockById(7));

		world.setBlock(x+0, y+3, z+0, Block.getBlockById(7));
		world.setBlock(x+1, y+3, z+0, Block.getBlockById(7));
		world.setBlock(x+2, y+3, z+0, Block.getBlockById(7));
		world.setBlock(x+3, y+3, z+0, Block.getBlockById(7));
		world.setBlock(x+0, y+3, z+1, Block.getBlockById(7));
		world.setBlock(x+1, y+3, z+1, Block.getBlockById(0));
		world.setBlock(x+2, y+3, z+1, Block.getBlockById(0));
		world.setBlock(x+3, y+3, z+1, Block.getBlockById(7));
		world.setBlock(x+0, y+3, z+2, Block.getBlockById(7));
		world.setBlock(x+1, y+3, z+2, Block.getBlockById(0));
		world.setBlock(x+2, y+3, z+2, Block.getBlockById(0));
		world.setBlock(x+3, y+3, z+2, Block.getBlockById(7));
		world.setBlock(x+0, y+3, z+3, Block.getBlockById(7));
		world.setBlock(x+1, y+3, z+3, Block.getBlockById(0));
		world.setBlock(x+2, y+3, z+3, Block.getBlockById(0));
		world.setBlock(x+3, y+3, z+3, Block.getBlockById(7));
		world.setBlock(x+0, y+3, z+4, Block.getBlockById(7));
		world.setBlock(x+1, y+3, z+4, Block.getBlockById(7));
		world.setBlock(x+2, y+3, z+4, Block.getBlockById(7));
		world.setBlock(x+3, y+3, z+4, Block.getBlockById(7));
		
		return true;
	}

}

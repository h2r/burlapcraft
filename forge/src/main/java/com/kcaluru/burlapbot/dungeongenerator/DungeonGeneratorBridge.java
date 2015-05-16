package com.kcaluru.burlapbot.dungeongenerator;

import java.util.Random;

import com.kcaluru.burlapbot.BurlapCraft;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.common.IWorldGenerator;

public class DungeonGeneratorBridge extends WorldGenerator implements IWorldGenerator
{
	public DungeonGeneratorBridge() { }

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) { }

	public void setBlock(World world, int x, int y, int z, Block block, int metadata)
	{
		Block b1 = world.getBlock(x, y, z);

		if(b1.isAir(world, x, y, z) || b1.isLeaves(world, x, y, z))
		{
			world.setBlock(x, y, z, block, metadata, 2);
		}
	}

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
package edu.brown.cs.h2r.burlapcraft.dungeongenerator;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;

public class DungeonFinder extends Dungeon {

	public DungeonFinder(Pose _pose) {
		super("finder", _pose, 5, 5, 3, Pose.fromXyz(1.5, 5, 3));
	}

	@Override
	protected void generate(World world, int x, int y, int z) {

		System.out.println("Making finder dungeon at " + x + "," + y + "," + z);

		world.setBlock(x+0, y+0, z+0, Block.getBlockById(7));
		world.setBlock(x+1, y+0, z+0, Block.getBlockById(7));
		world.setBlock(x+2, y+0, z+0, Block.getBlockById(7));
		world.setBlock(x+3, y+0, z+0, Block.getBlockById(7));
		world.setBlock(x+4, y+0, z+0, Block.getBlockById(7));
		world.setBlock(x+0, y+0, z+1, Block.getBlockById(7));
		world.setBlock(x+1, y+0, z+1, Block.getBlockById(7));
		world.setBlock(x+2, y+0, z+1, Block.getBlockById(7));
		world.setBlock(x+3, y+0, z+1, Block.getBlockById(7));
		world.setBlock(x+4, y+0, z+1, Block.getBlockById(7));
		world.setBlock(x+0, y+0, z+2, Block.getBlockById(7));
		world.setBlock(x+1, y+0, z+2, Block.getBlockById(7));
		world.setBlock(x+2, y+0, z+2, Block.getBlockById(7));
		world.setBlock(x+3, y+0, z+2, Block.getBlockById(7));
		world.setBlock(x+4, y+0, z+2, Block.getBlockById(7));
		world.setBlock(x+0, y+0, z+3, Block.getBlockById(7));
		world.setBlock(x+1, y+0, z+3, Block.getBlockById(7));
		world.setBlock(x+2, y+0, z+3, Block.getBlockById(7));
		world.setBlock(x+3, y+0, z+3, Block.getBlockById(7));
		world.setBlock(x+4, y+0, z+3, Block.getBlockById(7));
		world.setBlock(x+0, y+0, z+4, Block.getBlockById(7));
		world.setBlock(x+1, y+0, z+4, Block.getBlockById(7));
		world.setBlock(x+2, y+0, z+4, Block.getBlockById(7));
		world.setBlock(x+3, y+0, z+4, Block.getBlockById(7));
		world.setBlock(x+4, y+0, z+4, Block.getBlockById(7));
		world.setBlock(x+0, y+1, z+0, Block.getBlockById(7));
		world.setBlock(x+1, y+1, z+0, Block.getBlockById(7));
		world.setBlock(x+2, y+1, z+0, Block.getBlockById(7));
		world.setBlock(x+3, y+1, z+0, Block.getBlockById(7));
		world.setBlock(x+4, y+1, z+0, Block.getBlockById(7));
		world.setBlock(x+0, y+1, z+1, Block.getBlockById(7));
		world.setBlock(x+1, y+0, z+1, Block.getBlockById(41)); //goal block
		world.setBlock(x+2, y+1, z+1, Block.getBlockById(0));
		world.setBlock(x+3, y+1, z+1, Block.getBlockById(0));
		world.setBlock(x+4, y+1, z+1, Block.getBlockById(7));
		world.setBlock(x+0, y+1, z+2, Block.getBlockById(7));
		world.setBlock(x+1, y+1, z+2, Block.getBlockById(7));
		world.setBlock(x+2, y+1, z+2, Block.getBlockById(0));
		world.setBlock(x+3, y+1, z+2, Block.getBlockById(0));
		world.setBlock(x+4, y+1, z+2, Block.getBlockById(7));
		world.setBlock(x+0, y+1, z+3, Block.getBlockById(7));
		world.setBlock(x+1, y+1, z+3, Block.getBlockById(0));
		world.setBlock(x+2, y+1, z+3, Block.getBlockById(0));
		world.setBlock(x+3, y+1, z+3, Block.getBlockById(0));
		world.setBlock(x+4, y+1, z+3, Block.getBlockById(7));
		world.setBlock(x+0, y+1, z+4, Block.getBlockById(7));
		world.setBlock(x+1, y+1, z+4, Block.getBlockById(7));
		world.setBlock(x+2, y+1, z+4, Block.getBlockById(7));
		world.setBlock(x+3, y+1, z+4, Block.getBlockById(7));
		world.setBlock(x+4, y+1, z+4, Block.getBlockById(7));
		world.setBlock(x+0, y+2, z+0, Block.getBlockById(7));
		world.setBlock(x+1, y+2, z+0, Block.getBlockById(7));
		world.setBlock(x+2, y+2, z+0, Block.getBlockById(7));
		world.setBlock(x+3, y+2, z+0, Block.getBlockById(7));
		world.setBlock(x+4, y+2, z+0, Block.getBlockById(7));
		world.setBlock(x+0, y+2, z+1, Block.getBlockById(7));
		world.setBlock(x+1, y+2, z+1, Block.getBlockById(0));
		world.setBlock(x+2, y+2, z+1, Block.getBlockById(0));
		world.setBlock(x+3, y+2, z+1, Block.getBlockById(0));
		world.setBlock(x+4, y+2, z+1, Block.getBlockById(7));
		world.setBlock(x+0, y+2, z+2, Block.getBlockById(7));
		world.setBlock(x+1, y+2, z+2, Block.getBlockById(7));
		world.setBlock(x+2, y+2, z+2, Block.getBlockById(0));
		world.setBlock(x+3, y+2, z+2, Block.getBlockById(0));
		world.setBlock(x+4, y+2, z+2, Block.getBlockById(7));
		world.setBlock(x+0, y+2, z+3, Block.getBlockById(7));
		world.setBlock(x+1, y+2, z+3, Block.getBlockById(0));
		world.setBlock(x+2, y+2, z+3, Block.getBlockById(0));
		world.setBlock(x+3, y+2, z+3, Block.getBlockById(0));
		world.setBlock(x+4, y+2, z+3, Block.getBlockById(7));
		world.setBlock(x+0, y+2, z+4, Block.getBlockById(7));
		world.setBlock(x+1, y+2, z+4, Block.getBlockById(7));
		world.setBlock(x+2, y+2, z+4, Block.getBlockById(7));
		world.setBlock(x+3, y+2, z+4, Block.getBlockById(7));
		world.setBlock(x+4, y+2, z+4, Block.getBlockById(7));

	}
	
	

}

package edu.brown.cs.h2r.burlapcraft.dungeongenerator;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;

public class DungeonTinyBridge extends Dungeon {
	
	public DungeonTinyBridge(Pose pose) {
		super("tiny_bridge", pose, 4, 5, 4, Pose.fromXyz(1, 5, 3));
		
	}

	@Override
	protected void generate(World world, int x, int y, int z) {
		System.out.println("Making tiny bridge dungeon at " + x + "," + y + "," + z);
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

		
	}
	

}

package edu.brown.cs.h2r.burlapcraft.dungeongenerator;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;


public class DungeonGrid extends Dungeon {

	public DungeonGrid(Pose _pose) {
		super("grid", _pose, 10, 10, 4, Pose.fromXyz(1.5, 5, 3));
		
	}
	@Override
	protected void generate(World world, int x, int y, int z) {
		System.out.println("Making grid dungeon at " + x + "," + y + "," + z);
		int w = 10;
		for (int ax = 0; ax < w; ax++) {
			for (int az = 0; az < w; az++) {
				world.setBlock(x+ax, y+0, z+az, Block.getBlockById(7));
				world.markBlockForUpdate(x+ax, y+0, z+az);
			}
		}
		for (int ax = 0; ax < w; ax++) {
			world.setBlock(x+ax, y+0, z+0, Block.getBlockById(7));
			world.setBlock(x+ax, y+1, z+0, Block.getBlockById(7));
			world.setBlock(x+ax, y+2, z+0, Block.getBlockById(7));
			world.setBlock(x+ax, y+0, z+w, Block.getBlockById(7));
			world.setBlock(x+ax, y+1, z+w, Block.getBlockById(7));
			world.setBlock(x+ax, y+2, z+w, Block.getBlockById(7));
		}	

		for (int az = 0; az < w; az++) {
			world.setBlock(x+0, y+0, z+az, Block.getBlockById(7));
			world.setBlock(x+0, y+1, z+az, Block.getBlockById(7));
			world.setBlock(x+0, y+2, z+az, Block.getBlockById(7));
			world.setBlock(x+w, y+0, z+az, Block.getBlockById(7));
			world.setBlock(x+w, y+1, z+az, Block.getBlockById(7));
			world.setBlock(x+w, y+2, z+az, Block.getBlockById(7));
		}
		world.setBlock(x + w-1, y+0, z+ w-1, Block.getBlockById(41));


	}

}

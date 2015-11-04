package edu.brown.cs.h2r.burlapcraft.dungeongenerator;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;


public class DungeonMaze0 extends Dungeon {

	public DungeonMaze0(Pose _pose) {
		super("maze0", _pose, 14, 14, 4, Pose.fromXyz(10, 5, 2));
		
	}
	@Override
	protected void generate(World world, int x, int y, int z) {
		System.out.println("Making maze0 dungeon at " + x + "," + y + "," + z);
		
		int internalWidth = 12;
		int w = internalWidth+1;
		
		// floor
		for (int ax = 0; ax <= w; ax++) {
			for (int az = 0; az <= w; az++) {
				world.setBlock(x+ax, y+0, z+az, Block.getBlockById(7));
				world.markBlockForUpdate(x+ax, y+0, z+az);
			}
		}
		
		// walls
		for (int ax = 0; ax <= w; ax++) {
			world.setBlock(x+ax, y+0, z+0, Block.getBlockById(7));
			world.setBlock(x+ax, y+1, z+0, Block.getBlockById(7));
			world.setBlock(x+ax, y+2, z+0, Block.getBlockById(7));
			world.setBlock(x+ax, y+0, z+w, Block.getBlockById(7));
			world.setBlock(x+ax, y+1, z+w, Block.getBlockById(7));
			world.setBlock(x+ax, y+2, z+w, Block.getBlockById(7));
		}	
		for (int az = 0; az <= w; az++) {
			world.setBlock(x+0, y+0, z+az, Block.getBlockById(7));
			world.setBlock(x+0, y+1, z+az, Block.getBlockById(7));
			world.setBlock(x+0, y+2, z+az, Block.getBlockById(7));
			world.setBlock(x+w, y+0, z+az, Block.getBlockById(7));
			world.setBlock(x+w, y+1, z+az, Block.getBlockById(7));
			world.setBlock(x+w, y+2, z+az, Block.getBlockById(7));
		}
		
		// goal
		world.setBlock(x + 1, y+0, z+ w-1, Block.getBlockById(41));
		
		// maze
		String mazeMap = "001000111010" +
						 "000010010010" +
						 "001000100110" +
						 "011101001000" +
						 "100000001011" +
						 "101110101000" +
						 "100011101011" +
						 "111010100001" +
						 "001000111101" +
						 "010010100000" +
						 "000010101010" +
						 "000010001000";
		
		for (int ax = 1; ax <= internalWidth; ax++) {
			for (int az = 1; az <= internalWidth; az++) {
				int tZ = az-1; // 0 to internalWidth-1
				int mZ = internalWidth-tZ-1; // internalWidth-1 to 0
				int tX = ax-1; // 0 to internalWidth-1
				int mX = internalWidth-tX-1; // internalWidth-1 to 0
				if (mazeMap.charAt(mZ*internalWidth + mX) == '1') {
					world.setBlock(x+ax, y+0, z+az, Block.getBlockById(7));
					world.setBlock(x+ax, y+1, z+az, Block.getBlockById(7));
					world.setBlock(x+ax, y+2, z+az, Block.getBlockById(7));
				} else if (mazeMap.charAt(mZ*internalWidth + mX) == '2') {
					world.setBlock(x+ax, y+1, z+az, BurlapCraft.burlapStone);
				} else {
					// do nothing
				}
			}
		}
		


	}

}

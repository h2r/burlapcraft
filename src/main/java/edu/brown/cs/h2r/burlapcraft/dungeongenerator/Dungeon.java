package edu.brown.cs.h2r.burlapcraft.dungeongenerator;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import net.minecraft.block.Block;

public abstract class Dungeon {
	
	
	public static void makeAir(World world, int x, int y, int z, int width) {
		for (int dx = 0; dx < width; dx++) {
			for (int dy = 0; dy < width; dy++) {
				for (int dz = 0; dz < width	; dz++) {
					world.setBlock(x+dx, y+dy, z+dz, Block.getBlockById(0));
				}
			}
		}
	}

	
	private final Pose pose;
	
	private final String name;
	private final int length;
	private final int width;
	private final int height;
	
	private final Pose playerStartOffset;
	
	public Dungeon(String _name, Pose _pose, int _length, int _width, int _height, Pose _playerStartOffset) {
		name = _name;
		pose = _pose;
		length = _length;
		width = _width;
		height = _height;
		playerStartOffset = _playerStartOffset;
		
	}
	public int getLength() {
		return length;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
	public String getName() {
		return name;
	}
	
	public Pose getPose() {
		return pose;
	}
	public Pose getPlayerStartOffset() {
		return playerStartOffset;
	}
	
	public void generate(World world, Pose dungeonPose) {
		int x = (int) dungeonPose.getX();
		int y = (int) dungeonPose.getY();
		int z = (int) dungeonPose.getZ();
		makeAir(world, x, y, z, width);
		generate(world, x, y, z);
	}
	
	public void regenerate(World world) {
		generate(world, pose);
	}

	protected abstract void generate(World world, int x, int y, int z);
	
	
}
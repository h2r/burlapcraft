package edu.brown.cs.h2r.burlapcraft.dungeongenerator;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

import cpw.mods.fml.common.registry.EntityRegistry;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import edu.brown.cs.h2r.burlapcraft.solver.FightSolver;
import mobs.PersistentZombie;


public class DungeonCombat extends Dungeon {

	private World mobWorld;
	private Random rand = new Random();

	public DungeonCombat(Pose _pose) {
		super("combat", _pose, FightSolver.COMBAT_DUNGEON_WIDTH, FightSolver.COMBAT_DUNGEON_WIDTH, 4, Pose.fromXyz(1.5, 2, 3));
	}

	@Override
	protected void generate(World world, int x, int y, int z) {
		System.out.println("Making combat dungeon at " + x + "," + y + "," + z);
		int w = FightSolver.COMBAT_DUNGEON_WIDTH;
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
	}

	public void movePlayerToDefaultLocation() {
		Pose offset = getPlayerStartOffset();
		Pose playerPose = getPose().add(offset);
		HelperActions.setPlayerPosition(HelperActions.getMinecraft().thePlayer, playerPose);
	}

	public void movePlayerToRandomLocation() {
		int xOffset = rand.nextInt(9) + 1;
		int zOffset = rand.nextInt(9) + 1;
		Pose offset = Pose.fromXyz(xOffset, 2, zOffset);
		Pose playerPose = getPose().add(offset);
		HelperActions.setPlayerPosition(HelperActions.getMinecraft().thePlayer, playerPose);
	}
}

package mobs;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.world.World;

public class PersistentBlaze extends EntityBlaze {

	public PersistentBlaze(World world) {
		super(world);
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}
}

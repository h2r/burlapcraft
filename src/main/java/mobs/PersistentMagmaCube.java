package mobs;

import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.world.World;

public class PersistentMagmaCube extends EntityMagmaCube {
	public PersistentMagmaCube(World world) {
		super(world);
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}
}

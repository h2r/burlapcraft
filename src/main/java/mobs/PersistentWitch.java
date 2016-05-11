package mobs;

import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.world.World;

public class PersistentWitch extends EntityWitch {

	public PersistentWitch(World world) {
		super(world);
		this.isImmuneToFire = true;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}
}

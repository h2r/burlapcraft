package mobs;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PersistentZombie extends EntityZombie {

	public PersistentZombie(World world) {
		super(world);
		this.isImmuneToFire = true;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

}

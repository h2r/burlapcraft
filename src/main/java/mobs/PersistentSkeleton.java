package mobs;

import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PersistentSkeleton extends EntitySkeleton {
	public PersistentSkeleton(World world) {
		super(world);
		this.isImmuneToFire = true;
		this.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}
}

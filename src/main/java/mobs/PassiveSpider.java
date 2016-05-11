package mobs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.world.World;

public class PassiveSpider extends EntitySpider {

	public PassiveSpider(World world) {		
		super(world);
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected Entity findPlayerToAttack() {
		//Makes spider always passive
		return null;
	}
}

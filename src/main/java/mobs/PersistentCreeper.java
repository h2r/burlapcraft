package mobs;

import edu.brown.cs.h2r.burlapcraft.solver.FightSolver;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.World;

public class PersistentCreeper extends EntityCreeper {

	public PersistentCreeper(World world) {
		super(world);
	}
	
	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	@Override
	public void setDead() {
		this.isDead = true;
		FightSolver.isMobDead = true;
	}
}

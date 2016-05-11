package edu.brown.cs.h2r.burlapcraft.action;

import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.environment.Environment;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.solver.FightSolver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.monster.EntityMob;

public class ActionControllerAttack implements ActionController {
	protected int delayMS;
	protected Environment environment;
	
	public ActionControllerAttack(int delayMS, Environment e) {
		this.delayMS = delayMS;
		this.environment = e;
	}
	
	@Override
	public int executeAction(GroundedAction ga) {		
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		EntityMob mob = FightSolver.mob;

		double angle = 57.2958*Math.atan2(mob.posZ - player.posZ, mob.posX - player.posX);;
		double yawTarget = angle - 90;

		//Casting to int to normalize the actual angle
		//(Equality check in helper actions wasn't working because atan2 returns alot of decimal digits)
		HelperActions.moveYawToTarget((int) yawTarget);

		HelperActions.attack();

		return this.delayMS;
	}
}

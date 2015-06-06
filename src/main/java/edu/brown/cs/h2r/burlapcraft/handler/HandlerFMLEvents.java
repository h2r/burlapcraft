package edu.brown.cs.h2r.burlapcraft.handler;

import net.minecraft.client.Minecraft;
import burlap.behavior.singleagent.Policy;
import burlap.oomdp.core.AbstractGroundedAction;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

public class HandlerFMLEvents {

	public static int tickCount = 0;
	public static boolean evaluateActions = false;
	public static String[] actions;
	public static int actionsLeft = 0;
	private Policy currentPolicy;
	private Domain currentDomain;
	private Dungeon currentDungeon;
	
	public void execute(Domain d, Policy p, Dungeon _dungeon) {
		currentPolicy = p;
		currentDomain = d;
		currentDungeon = _dungeon;
	}

	 // Called when the server ticks. Usually 20 ticks a second. 
	 @SubscribeEvent
	 public void onServerTick(TickEvent.ServerTickEvent event) {
		 tickCount++;
		 
		 if (evaluateActions) {
			 if ((tickCount % 40 == 0) && actionsLeft > 0) {
				 evaluateAction(actions[actions.length - actionsLeft]);
				 actionsLeft--;
			 }
		 }
 		 if (currentPolicy != null && tickCount % 80 == 0) {
 			 try {
 				 State s = StateGenerator.getCurrentState(currentDomain, currentDungeon);

 				 AbstractGroundedAction a = currentPolicy.getAction(s);
 				 System.out.println("Executing: " + a);
 				 a.executeIn(s);
 			 } catch (Exception e) {
 				 e.printStackTrace();
 				 currentPolicy = null;
 			 }
			 
		 }
	 }
	 
	 public void evaluateAction(String action) {
		if (action.equals(HelperNameSpace.ACTIONMOVE)) {
			HelperActions.moveForward(false);
		}
		else if (action.equals(HelperNameSpace.ACTIONDOWNONE)) {
			HelperActions.faceDownOne();
		}
		else if (action.equals(HelperNameSpace.ACTIONAHEAD)) {
			HelperActions.faceAhead();
		}
		else if (action.equals(HelperNameSpace.ACTIONDESTBLOCK)) {
			HelperActions.destroyBlock();
		}
		else if (action.equals(HelperNameSpace.ACTIONPLACEBLOCK)) {
			HelperActions.placeBlock();
		}
		else if (action.equals(HelperNameSpace.ACTIONROTATELEFT)) {
			Minecraft.getMinecraft().thePlayer.rotationYaw -= 90;
		}
		else if (action.equals(HelperNameSpace.ACTIONROTATERIGHT)) {
			Minecraft.getMinecraft().thePlayer.rotationYaw += 90;
		}
	 }
}

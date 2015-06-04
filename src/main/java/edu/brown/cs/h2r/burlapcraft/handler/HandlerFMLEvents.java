package edu.brown.cs.h2r.burlapcraft.handler;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;

public class HandlerFMLEvents {

	public static int tickCount = 0;
	public static boolean evaluateActions = false;
	public static String[] actions;
	public static int actionsLeft = 0;

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

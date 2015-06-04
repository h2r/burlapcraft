package edu.brown.cs.h2r.burlapcraft.handler;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;

public class HandlerEvents {
	
	public static EntityPlayer player;
	
	@SubscribeEvent
	public void LivingUpdateEvent(LivingEvent event) {
		
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			if (!player.inventory.hasItem(Items.diamond_pickaxe)) {
				player.inventory.addItemStackToInventory(new ItemStack(Items.diamond_pickaxe));
			}
		}
	}
	
	@SubscribeEvent
	public void EntityJoinWorldEvent(EntityEvent event) {
		if (event.entity instanceof EntityPlayer) {
			player = (EntityPlayer) event.entity;
		}
	}
}

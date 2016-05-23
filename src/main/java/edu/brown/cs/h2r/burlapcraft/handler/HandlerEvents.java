package edu.brown.cs.h2r.burlapcraft.handler;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;
import edu.brown.cs.h2r.burlapcraft.state.MinecraftStateGeneratorHelper;

public class HandlerEvents {
	
	public static EntityPlayer player;
	
	@SubscribeEvent
	public void givePickaxeEvent(LivingEvent.LivingUpdateEvent event) {
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
	
	@SubscribeEvent
	public void blockBreakEvent(BlockEvent.BreakEvent event) {
		Block brokenBlock = event.block;
		String key = event.x + "," + event.y + "," + event.z;
		int blockID = Block.getIdFromBlock(brokenBlock);
		if (MinecraftStateGeneratorHelper.blockNameMap.containsKey(key)) {
			if (MinecraftStateGeneratorHelper.invBlockNameMap.containsKey(blockID)) {
				ArrayList<String> blockNames = MinecraftStateGeneratorHelper.invBlockNameMap.get(blockID);
				blockNames.add(MinecraftStateGeneratorHelper.blockNameMap.get(key));
			}
			else {
				ArrayList<String> blockNames = new ArrayList<String>();
				blockNames.add(MinecraftStateGeneratorHelper.blockNameMap.get(key));
				MinecraftStateGeneratorHelper.invBlockNameMap.put(blockID, blockNames);
			}
			MinecraftStateGeneratorHelper.blockNameMap.remove(key);

		}
	}
	
	@SubscribeEvent
	public void placeBlockEvent(PlayerInteractEvent event) {
		EntityPlayer p = event.entityPlayer;
	    if(event.action == Action.RIGHT_CLICK_BLOCK) {
	    	Block clickedBlock = HelperActions.getBlock(event.x, event.y, event.z);
	    	if (HelperActions.blockIsOneOf(clickedBlock, HelperActions.unbreakableBlocks) && event.y < p.posY) {
	    		List<HelperPos> blockPoss = new ArrayList<HelperPos>();
	    		blockPoss.add(new HelperPos(event.x, event.y + 1, event.z));
	    		blockPoss.add(new HelperPos(event.x, event.y, event.z + 1));
	    		blockPoss.add(new HelperPos(event.x, event.y, event.z - 1));
	    		blockPoss.add(new HelperPos(event.x + 1, event.y, event.z));
	    		blockPoss.add(new HelperPos(event.x - 1, event.y, event.z));
	    		
	    		for (HelperPos pos : blockPoss) {
	    			String key = pos.x + "," + pos.y + "," + pos.z;
	    			if (HelperActions.blockIsOneOf(HelperActions.getBlock(pos), HelperActions.mineableBlocks) && 
	    					!MinecraftStateGeneratorHelper.blockNameMap.containsKey(key)) {
	    				int blockID = HelperActions.getBlockId(pos.x, pos.y, pos.z);
	    				ArrayList<String> blockNames = MinecraftStateGeneratorHelper.invBlockNameMap.get(blockID);
	    				if (blockNames != null && blockNames.size() == 1) {
	    					MinecraftStateGeneratorHelper.blockNameMap.put(key, blockNames.get(0));
	    					MinecraftStateGeneratorHelper.invBlockNameMap.remove(blockID);
	    				}
	    				else {
							if(blockNames != null) {
								MinecraftStateGeneratorHelper.blockNameMap.put(key, blockNames.get(0));
								blockNames.remove(0);
							}
	    				}
	    				break;
	    			}
	    		}
	    	}
	    }
	}
	
}

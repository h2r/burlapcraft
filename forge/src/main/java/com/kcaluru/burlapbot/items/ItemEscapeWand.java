package com.kcaluru.burlapbot.items;

import com.kcaluru.burlapbot.BurlapMod;
import com.kcaluru.burlapbot.BurlapWorldGenHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ItemEscapeWand extends Item {
	
	// name of item
	private String name = "escapewand";
	
	public ItemEscapeWand() {
		// give the item a name
		setUnlocalizedName(BurlapMod.MODID + "_" + name);
		
		// add the item to misc tab
		setCreativeTab(CreativeTabs.tabCombat);
		
		// set texture
		setTextureName(BurlapMod.MODID + ":" + name);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {

		if (!world.isRemote) {
			System.out.println("Used Escape Wand");
			ItemFinderWand.finderInside = false;
			ItemBridgeWand.bridgeInside = false;
			player.setPositionAndUpdate((double) BurlapWorldGenHandler.playerSpawnPos.x, (double) BurlapWorldGenHandler.playerSpawnPos.y, (double) BurlapWorldGenHandler.playerSpawnPos.z);
		}
        return itemStack;
    }
}

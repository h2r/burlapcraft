package edu.brown.cs.h2r.burlapcraft.item;

import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerDungeonGeneration;

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
		setUnlocalizedName(BurlapCraft.MODID + "_" + name);
		
		// add the item to misc tab
		setCreativeTab(CreativeTabs.tabCombat);
		
		// set texture
		setTextureName(BurlapCraft.MODID + ":" + name);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {

		if (!world.isRemote) {
			System.out.println("Used Escape Wand");
			ItemFinderWand.finderInside = false;
			ItemBridgeWand.bridgeInside = false;
			player.setPositionAndUpdate((double) HandlerDungeonGeneration.playerSpawnPos.x, (double) HandlerDungeonGeneration.playerSpawnPos.y, (double) HandlerDungeonGeneration.playerSpawnPos.z);
		}
        return itemStack;
    }
}

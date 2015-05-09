package com.kcaluru.burlapbot.items;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.world.World;

import com.kcaluru.burlapbot.BurlapMod;
import com.kcaluru.burlapbot.BurlapWorldGenHandler;
import com.kcaluru.burlapbot.helpers.BurlapAIHelper;
import com.kcaluru.burlapbot.helpers.NameSpace;
import com.kcaluru.burlapbot.helpers.Pos;
import com.kcaluru.burlapbot.solver.SolverFinderDungeon;
import com.kcaluru.burlapbot.test.BFSMovement;

import cpw.mods.fml.common.registry.GameData;

public class ItemFinderWand extends Item {
	
	// name of item
	private String name = "finderwand";
	
	// start x, y and z of player within dungeon.
	private int dungeonX = 8; 
	private int dungeonY = 1;
	private int dungeonZ = 2;
	
	// indicate whether agent is in dungeon or not
	public static boolean finderInside;
	
	public ItemFinderWand() {
		
		// set finderInside to false
		finderInside = false;
		
		// give the item a name
		setUnlocalizedName(BurlapMod.MODID + "_" + name);
		
		// add the item to misc tab
		setCreativeTab(CreativeTabs.tabCombat);
		
		// set texture
		setTextureName(BurlapMod.MODID + ":" + name);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		
		if(!world.isRemote) {
			if (finderInside) {
				
				// find the goal
				int goldX = 0;
				int goldY = 0;
				int goldZ = 0;
				
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 11; j++) {
						for (int k = 0; k < 11; k++) {
							if (BurlapAIHelper.getBlockId(BurlapWorldGenHandler.finderX + j, BurlapWorldGenHandler.finderY + i, BurlapWorldGenHandler.finderZ + k) == 41) {
								goldY = i;
								goldX = j;
								goldZ = k;
							}
						}
					}
				}
				
				// create the solver and give it the goal coords
				SolverFinderDungeon solver = new SolverFinderDungeon(goldX, goldY, goldZ);
				
				// run RMax
				solver.RMAX();
				
	
			}
			else {
				ItemBridgeWand.bridgeInside = false;
				
				player.setPositionAndUpdate(BurlapWorldGenHandler.finderX + this.dungeonX, BurlapWorldGenHandler.finderY + this.dungeonY, BurlapWorldGenHandler.finderZ + this.dungeonZ);
				
				finderInside = true;
			}
		}
		
		return itemStack;
	}
}

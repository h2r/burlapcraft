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
import com.kcaluru.burlapbot.solver.FinderDungeonSolver;
import com.kcaluru.burlapbot.test.BFSMovement;

import cpw.mods.fml.common.registry.GameData;

public class ItemFinderWand extends Item {
	
	// name of item
	private String name = "finderwand";
	
	// start x, y and z of player within dungeon.
	private int dungeonX = 8; 
	private int dungeonY = 1;
	private int dungeonZ = 2;
	
	// gold x, y and z within the dungeon
	private int goldX = 2;
	private int goldZ = 1;
	
	// hardcoded finder dungeon map
	final int [][][] finderMap = {
			{
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7}
			},
			{
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,0,0,0,0,0,0,0,0,0,7},
				{7,41,0,0,0,7,0,0,0,0,7},
				{7,0,0,0,0,7,0,0,0,0,7},
				{7,0,0,0,0,0,0,0,0,0,7},
				{7,7,7,7,7,7,7,7,0,0,7},
				{7,0,0,0,0,7,0,0,0,0,7},
				{7,0,0,0,0,0,0,0,0,0,7},
				{7,0,0,0,0,0,0,0,0,0,7},
				{7,0,0,0,0,7,0,0,0,0,7},
				{7,7,7,7,7,7,7,7,7,7,7}
			},
			{
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,0,0,0,0,0,0,0,0,0,7},
				{7,7,0,0,0,7,0,0,0,0,7},
				{7,0,0,0,0,7,0,0,0,0,7},
				{7,0,0,0,0,0,0,0,0,0,7},
				{7,7,7,7,7,7,7,7,0,0,7},
				{7,0,0,0,0,7,0,0,0,0,7},
				{7,0,0,0,0,0,0,0,0,0,7},
				{7,0,0,0,0,0,0,0,0,0,7},
				{7,0,0,0,0,7,0,0,0,0,7},
				{7,7,7,7,7,7,7,7,7,7,7}
			},
			{
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7},
				{7,7,7,7,7,7,7,7,7,7,7}
			}
	};
	
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
		
		BurlapAIHelper.moveForward(false, (int) player.posX, (int) player.posY - 2, (int) player.posZ);
//		BurlapAIHelper.walkEast(false, (int) player.posX, (int) Math.ceil(player.posY) - 2, (int) player.posZ);
		
//		if(!world.isRemote) {
//			if (finderInside) {
//				
//				// player's current coordinates within the dungeon
//				int playerX = (int) Math.ceil(player.posX - BurlapWorldGenHandler.posX);
//				int playerZ = (int) Math.ceil(player.posZ - BurlapWorldGenHandler.posZ);
//				
//				// create the solver
//				FinderDungeonSolver solver = new FinderDungeonSolver(finderMap, playerX, playerZ, this.goldX, this.goldZ);
//				
//				
//				// run BFS
//				solver.BFS();
//	
//			}
//			else {
//				ItemBridgeWand.bridgeInside = false;
//				
//				
//				player.setPositionAndUpdate(BurlapWorldGenHandler.posX + this.dungeonX, BurlapWorldGenHandler.posY + 40 + this.dungeonY, BurlapWorldGenHandler.posZ + this.dungeonZ);
//				
//				finderInside = true;
//			}
//		}
		
		return itemStack;
	}
}

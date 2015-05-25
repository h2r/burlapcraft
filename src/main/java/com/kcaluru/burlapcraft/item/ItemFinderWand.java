package com.kcaluru.burlapcraft.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.world.World;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.singleagent.explorer.TerminalExplorer;

import com.kcaluru.burlapcraft.BurlapCraft;
import com.kcaluru.burlapcraft.domaingenerator.DomainGeneratorReal;
import com.kcaluru.burlapcraft.domaingenerator.DomainGeneratorSimulated;
import com.kcaluru.burlapcraft.handler.HandlerDungeonGeneration;
import com.kcaluru.burlapcraft.helper.HelperActions;
import com.kcaluru.burlapcraft.helper.HelperNameSpace;
import com.kcaluru.burlapcraft.helper.HelperPos;
import com.kcaluru.burlapcraft.solver.SolverLearningFinder;
import com.kcaluru.burlapcraft.solver.SolverPlanningFinder;
import com.kcaluru.burlapcraft.stategenerator.StateGenerator;

import cpw.mods.fml.common.registry.GameData;

public class ItemFinderWand extends Item {
	
	// name of item
	private String name = "finderwand";
	
	// length, width and height of dungeon
	final int length = 5;
	final int width = 5;
	final int height = 4;
	
	// start x, y and z of player within dungeon
	private double dungeonX = 1.5; 
	private double dungeonY = 1;
	private double dungeonZ = 3;
	
	// indicate whether agent is in dungeon or not
	public static boolean finderInside;
	
	public ItemFinderWand() {
		
		// set finderInside to false
		finderInside = false;
		
		// give the item a name
		setUnlocalizedName(BurlapCraft.MODID + "_" + name);
		
		// add the item to misc tab
		setCreativeTab(CreativeTabs.tabCombat);
		
		// set texture
		setTextureName(BurlapCraft.MODID + ":" + name);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		
		if(!world.isRemote) {
			if (finderInside) {
				
				// ---------- LEARNING ---------- //
//				
//				// set dungeonID to 1
//				DomainGeneratorReal.dungeonID = 1;
//				
//				// create the solver and give it the goal coords
//				SolverLearningFinder solver = new SolverLearningFinder(this.length, this.width, this.height);
//				
//				// run RMax
//				solver.RMAX();
//				
				// ------------------------------ //
				
				// ---------- PLANNING ---------- //
				
				// set dungeonID to 1
				DomainGeneratorSimulated.dungeonID = 1;
				
				// create the solver and give it the map
				SolverPlanningFinder solver = new SolverPlanningFinder(StateGenerator.getMap(DomainGeneratorSimulated.dungeonID));
				
				// run BFS
				solver.BFS();
				
				// ------------------------------ //
	
			}
			else {
				ItemBridgeWand.bridgeInside = false;
				
				player.setPositionAndUpdate(HandlerDungeonGeneration.finderX + this.dungeonX, HandlerDungeonGeneration.finderY + this.dungeonY, HandlerDungeonGeneration.finderZ + this.dungeonZ);
				
				finderInside = true;
			}
		}
		
		return itemStack;
	}
}

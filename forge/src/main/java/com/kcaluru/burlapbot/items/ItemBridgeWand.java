package com.kcaluru.burlapbot.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import burlap.oomdp.singleagent.explorer.TerminalExplorer;

import com.kcaluru.burlapbot.BurlapMod;
import com.kcaluru.burlapbot.BurlapWorldGenHandler;
import com.kcaluru.burlapbot.domaingenerator.DomainGeneratorReal;
import com.kcaluru.burlapbot.solver.SolverLearningBridge;

public class ItemBridgeWand extends Item {

	// name of item
	private String name = "bridgewand";
	
	// length, width and height of dungeon
	final int length = 5;
	final int width = 5;
	final int height = 5;
	
	// start x, y and z of agent within dungeon
	private double dungeonX = 1.5; 
	private double dungeonY = 2;
	private double dungeonZ = 4;
	
	
	// indicate whether agent is in dungeon or not
	public static boolean bridgeInside;
	
	public ItemBridgeWand() {
		
		// set brideInside to false
		bridgeInside = false;
		
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
			if (bridgeInside) {
				
				// create the solver and give it the goal coords
				SolverLearningBridge solver = new SolverLearningBridge(this.length, this.width, this.height);
				
				// set dungeonID to 2
				DomainGeneratorReal.dungeonID = 2;
				
				// run RMax
				solver.RMAX(); 
				
//				TerminalExplorer exp = new TerminalExplorer(solver.domain);
				
//				exp.exploreFromState(solver.initialState);
	
			}
			else {
				if (!world.isRemote) {
					ItemFinderWand.finderInside = false;
					player.setPositionAndUpdate((double) BurlapWorldGenHandler.bridgeX + this.dungeonX, (double) BurlapWorldGenHandler.bridgeY + this.dungeonY, (double) BurlapWorldGenHandler.bridgeZ + this.dungeonZ);
					bridgeInside = true;
				}
			}
		}
        return itemStack;
    }
}

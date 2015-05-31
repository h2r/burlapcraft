package com.kcaluru.burlapcraft;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

import com.kcaluru.burlapcraft.block.BlockBurlapStone;
import com.kcaluru.burlapcraft.command.CommandAStar;
import com.kcaluru.burlapcraft.command.CommandBFS;
import com.kcaluru.burlapcraft.command.CommandRMax;
import com.kcaluru.burlapcraft.command.CommandTeleport;
import com.kcaluru.burlapcraft.handler.HandlerDungeonGeneration;
import com.kcaluru.burlapcraft.handler.HandlerEvents;
import com.kcaluru.burlapcraft.item.ItemBridgeWand;
import com.kcaluru.burlapcraft.item.ItemEscapeWand;
import com.kcaluru.burlapcraft.item.ItemFinderWand;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = BurlapCraft.MODID, version = BurlapCraft.VERSION)
public class BurlapCraft {
	
	// mod information
    public static final String MODID = "burlapmod";
    public static final String VERSION = "1.0";
    
    // items
    public static Item finderWand;
    public static Item bridgeWand;
    public static Item escapeWand;
    
    // blocks
    public static Block burlapStone;
    
    // event handlers
    HandlerDungeonGeneration genHandler = new HandlerDungeonGeneration();   
    HandlerEvents eventHandler = new HandlerEvents();
    
    // player dungeon location | 0: None, 1: Finder, 2: Bridge
    public static int dungeonLocID = 0;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
    	finderWand = new ItemFinderWand();
    	bridgeWand = new ItemBridgeWand();
    	escapeWand = new ItemEscapeWand();
    	burlapStone = new BlockBurlapStone();
    	
    	// make sure minecraft knows
    	GameRegistry.registerItem(finderWand, "finderwand");
    	GameRegistry.registerItem(bridgeWand, "bridgewand");
    	GameRegistry.registerItem(escapeWand, "escapewand");
    	GameRegistry.registerBlock(burlapStone, "burlapstone");
    	GameRegistry.registerWorldGenerator(genHandler, 0);
    	MinecraftForge.EVENT_BUS.register(eventHandler);
    	
    }
    
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        // register server commands
    	event.registerServerCommand(new CommandTeleport());
    	event.registerServerCommand(new CommandAStar());
    	event.registerServerCommand(new CommandBFS());
    	event.registerServerCommand(new CommandRMax());
    	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	
    }
    
}

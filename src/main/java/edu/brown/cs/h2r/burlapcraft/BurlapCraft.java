package edu.brown.cs.h2r.burlapcraft;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import edu.brown.cs.h2r.burlapcraft.block.BlockBurlapStone;
import edu.brown.cs.h2r.burlapcraft.command.CommandAStar;
import edu.brown.cs.h2r.burlapcraft.command.CommandBFS;
import edu.brown.cs.h2r.burlapcraft.command.CommandRMax;
import edu.brown.cs.h2r.burlapcraft.command.CommandTeleport;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerDungeonGeneration;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerEvents;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerFMLEvents;
import edu.brown.cs.h2r.burlapcraft.item.ItemBridgeWand;
import edu.brown.cs.h2r.burlapcraft.item.ItemEscapeWand;
import edu.brown.cs.h2r.burlapcraft.item.ItemFinderWand;

@Mod(modid = BurlapCraft.MODID, version = BurlapCraft.VERSION)
public class BurlapCraft {
	
	// mod information
    public static final String MODID = "burlapcraftmod";
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
    HandlerFMLEvents fmlHandler = new HandlerFMLEvents();
    
    // player dungeon location | 0: None, 1: Finder, 2: Bridge
    public static int dungeonLocID = 0;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
    	//finderWand = new ItemFinderWand();
    	//bridgeWand = new ItemBridgeWand();
    	//escapeWand = new ItemEscapeWand();
    	burlapStone = new BlockBurlapStone();
    	
    	// make sure minecraft knows
    	//GameRegistry.registerItem(finderWand, "finderwand");
    	//GameRegistry.registerItem(bridgeWand, "bridgewand");
    	//GameRegistry.registerItem(escapeWand, "escapewand");
    	GameRegistry.registerBlock(burlapStone, "burlapstone");
    	GameRegistry.registerWorldGenerator(genHandler, 0);
    	
    	MinecraftForge.EVENT_BUS.register(eventHandler);
    	FMLCommonHandler.instance().bus().register(fmlHandler);
    	
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

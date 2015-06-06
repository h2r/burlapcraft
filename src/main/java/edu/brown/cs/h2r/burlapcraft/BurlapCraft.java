package edu.brown.cs.h2r.burlapcraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
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
import edu.brown.cs.h2r.burlapcraft.command.CommandCheckState;
import edu.brown.cs.h2r.burlapcraft.command.CommandCreateDungeons;
import edu.brown.cs.h2r.burlapcraft.command.CommandInventory;
import edu.brown.cs.h2r.burlapcraft.command.CommandRMax;
import edu.brown.cs.h2r.burlapcraft.command.CommandResetDungeon;
import edu.brown.cs.h2r.burlapcraft.command.CommandSmoothMove;
import edu.brown.cs.h2r.burlapcraft.command.CommandTeleport;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerDungeonGeneration;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerEvents;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerFMLEvents;

@Mod(modid = BurlapCraft.MODID, version = BurlapCraft.VERSION)
public class BurlapCraft {
	
	// mod information
    public static final String MODID = "burlapcraftmod";
    public static final String VERSION = "1.0";
    
    // items
    
    // blocks
    public static Block burlapStone;
    
    // event handlers
    HandlerDungeonGeneration genHandler = new HandlerDungeonGeneration();   
    HandlerEvents eventHandler = new HandlerEvents();
    public static HandlerFMLEvents fmlHandler = new HandlerFMLEvents();
    
    public static Dungeon currentDungeon;
    public static List<Dungeon> dungeons = new ArrayList<Dungeon>();
    public static Map<String, Dungeon> dungeonMap = new HashMap<String, Dungeon>();
    
    public static void registerDungeon(Dungeon d) {
    	dungeons.add(d);
    	dungeonMap.put(d.getName(), d);
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	

    	burlapStone = new BlockBurlapStone();
    	
    	// make sure minecraft knows
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
    	event.registerServerCommand(new CommandSmoothMove());
    	event.registerServerCommand(new CommandAStar());
    	event.registerServerCommand(new CommandBFS());
    	event.registerServerCommand(new CommandRMax());
    	event.registerServerCommand(new CommandCreateDungeons());
        event.registerServerCommand(new CommandInventory());
        event.registerServerCommand(new CommandCheckState());
        event.registerServerCommand(new CommandResetDungeon());
    	
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

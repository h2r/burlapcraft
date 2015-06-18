package edu.brown.cs.h2r.burlapcraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.h2r.burlapcraft.command.*;
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
import edu.brown.cs.h2r.burlapcraft.block.BlockBlueRock;
import edu.brown.cs.h2r.burlapcraft.block.BlockBurlapStone;
import edu.brown.cs.h2r.burlapcraft.block.BlockGreenRock;
import edu.brown.cs.h2r.burlapcraft.block.BlockOrangeRock;
import edu.brown.cs.h2r.burlapcraft.block.BlockRedRock;
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
    public static Block redRock;
    public static Block blueRock;
    public static Block greenRock;
    public static Block orangeRock;
    
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
    	redRock = new BlockRedRock();
    	blueRock = new BlockBlueRock();
    	orangeRock = new BlockOrangeRock();
    	greenRock = new BlockGreenRock();
    	
    	// make sure minecraft knows
    	GameRegistry.registerBlock(burlapStone, "burlapstone");
    	GameRegistry.registerBlock(redRock, "redrock");
    	GameRegistry.registerBlock(blueRock, "bluerock");
    	GameRegistry.registerBlock(greenRock, "greenrock");
    	GameRegistry.registerBlock(orangeRock, "orangerock");
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
        event.registerServerCommand(new CommandLoadLanguageModel());
        event.registerServerCommand(new CommandSay());
    	
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

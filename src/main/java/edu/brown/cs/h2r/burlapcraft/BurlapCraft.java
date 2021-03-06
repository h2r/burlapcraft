package edu.brown.cs.h2r.burlapcraft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import edu.brown.cs.h2r.burlapcraft.block.*;
import edu.brown.cs.h2r.burlapcraft.command.*;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerDungeonGeneration;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerEvents;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod(modid = BurlapCraft.MODID, version = BurlapCraft.VERSION)
public class BurlapCraft {
	
	// mod information
    public static final String MODID = "burlapcraftmod";
    public static final String VERSION = "1.1";
    
    // items
    
    // blocks
    public static Block burlapStone;
    public static Block redRock;
    public static Block blueRock;
    public static Block greenRock;
    public static Block orangeRock;
    public static Block mineableRedRock;
    public static Block mineableBlueRock;
    public static Block mineableGreenRock;
    public static Block mineableOrangeRock;
    
    // event handlers
    HandlerDungeonGeneration genHandler = new HandlerDungeonGeneration();   
    HandlerEvents eventHandler = new HandlerEvents();
    
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
    	mineableRedRock = new BlockMineableRedRock();
    	mineableOrangeRock = new BlockMineableOrangeRock();
    	mineableGreenRock = new BlockMineableGreenRock();
    	mineableBlueRock = new BlockMineableBlueRock();
    	
    	// make sure minecraft knows
    	GameRegistry.registerBlock(burlapStone, "burlapstone");
    	GameRegistry.registerBlock(redRock, "redrock");
    	GameRegistry.registerBlock(blueRock, "bluerock");
    	GameRegistry.registerBlock(greenRock, "greenrock");
    	GameRegistry.registerBlock(orangeRock, "orangerock");
    	GameRegistry.registerBlock(mineableRedRock, "mineableRedRock");
    	GameRegistry.registerBlock(mineableGreenRock, "mineableGreenRock");
    	GameRegistry.registerBlock(mineableBlueRock, "mineableBlueRock");
    	GameRegistry.registerBlock(mineableOrangeRock, "mineableOrangeRock");
    	GameRegistry.registerWorldGenerator(genHandler, 0);
    	
    	MinecraftForge.EVENT_BUS.register(eventHandler);
    	// FMLCommonHandler.instance().bus().register(fmlHandler);
    	
    }
    
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        // register server commands
    	event.registerServerCommand(new CommandTeleport());
    	event.registerServerCommand(new CommandSmoothMove());
    	event.registerServerCommand(new CommandAStar());
    	event.registerServerCommand(new CommandBFS());
    	event.registerServerCommand(new CommandVI());
    	event.registerServerCommand(new CommandRMax());
    	event.registerServerCommand(new CommandCreateDungeons());
        event.registerServerCommand(new CommandInventory());
        event.registerServerCommand(new CommandCheckState());
        event.registerServerCommand(new CommandResetDungeon());
        event.registerServerCommand(new CommandCheckProps());
    	event.registerServerCommand(new CommandTest());
    	event.registerServerCommand(new CommandTerminalExplore());
        event.registerServerCommand(new CommandCurrentPath());
		event.registerServerCommand(new CommandReachable());
        
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

package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.h2r.burlapcraft.solver.*;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.DungeonEnum;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

public class CommandAStar implements ICommand {
	
	private final List aliases;
	
	public CommandAStar() {
		aliases = new ArrayList();
		aliases.add("astar");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "astar";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "astar [closed|open]\nIf closed/open not specified, closed it used";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		World world = sender.getEntityWorld();
		if (!world.isRemote) {
			if (args.length > 1) {
				sender.addChatMessage(new ChatComponentText("This command takes only 1 optional argument: closed or open"));
				return;
			}
			
			DungeonEnum dungeonID = BurlapCraft.dungeonID;
			
			if (dungeonID == null) {
				sender.addChatMessage(new ChatComponentText("You are not inside a dungeon"));
				return;
			}

			boolean closed = true;
			if(args.length == 1){
				if(args[0].equals("open")){
					closed = false;
				}
			}

			GotoSolver.plan(1, closed);

			/*
			if (dungeonID == DungeonEnum.FINDER) {
				// create the solver and give it the map
				SolverPlanningFinder finderSolver = new SolverPlanningFinder(StateGenerator.getMap(dungeonID));
				
				// run ASTAR
				finderSolver.ASTAR();
				
			} else if (dungeonID == DungeonEnum.TINY_BRIDGE) {
				// create the solver and give it the map
				SolverPlanningTinyBridge bridgeSolver = new SolverPlanningTinyBridge(StateGenerator.getMap(dungeonID));
				
				// run ASTAR
				bridgeSolver.ASTAR();
			} else if (dungeonID == DungeonEnum.SMALL_BRIDGE) {
				// create the solver and give it the map
				SolverPlanningSmallBridge bridgeSolver = new SolverPlanningSmallBridge(StateGenerator.getMap(dungeonID));
				
				// run ASTAR
				bridgeSolver.ASTAR();
			} else if (dungeonID == DungeonEnum.GRID) {
				// create the solver and give it the map
				SolverPlanningGrid gridSolver = new SolverPlanningGrid(StateGenerator.getMap(dungeonID));
				
				// run ASTAR
				gridSolver.ASTAR();
				
			} else {
				throw new IllegalStateException("Bad dungeon ID: " + dungeonID);
			}
			*/

		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,
			String[] p_71516_2_) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

}

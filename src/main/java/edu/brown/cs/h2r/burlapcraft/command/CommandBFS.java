package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.DungeonEnum;
import edu.brown.cs.h2r.burlapcraft.solver.SolverPlanningFinder;
import edu.brown.cs.h2r.burlapcraft.solver.SolverPlanningGrid;
import edu.brown.cs.h2r.burlapcraft.solver.SolverPlanningSmallBridge;
import edu.brown.cs.h2r.burlapcraft.solver.SolverPlanningTinyBridge;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

public class CommandBFS implements ICommand {

	private final List aliases;
	
	public CommandBFS() {
		aliases = new ArrayList();
		aliases.add("bfs");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "bfs";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "bfs";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		World world = sender.getEntityWorld();
		if (!world.isRemote) {
			if (args.length > 0) {
				sender.addChatMessage(new ChatComponentText("This command takes no arguments"));
				return;
			}
			
			Dungeon dungeon = BurlapCraft.currentDungeon;
			
			if (dungeon == null) {
				sender.addChatMessage(new ChatComponentText("You are not inside a dungeon"));
				return;
			}
			
			if (dungeon.getName().equals("finder")) {
				// create the solver and give it the map
				SolverPlanningFinder finderSolver = new SolverPlanningFinder(StateGenerator.getMap(dungeon));
				
				// run BFS
				finderSolver.BFS();
			} else if (dungeon.getName().equals("tiny_bridge")) {
				// create the solver and give it the map
				SolverPlanningTinyBridge bridgeSolver = new SolverPlanningTinyBridge(StateGenerator.getMap(dungeon));
				
				// run BFS
				bridgeSolver.BFS();
			} else if (dungeon.getName().equals("small_bridge")) {
				// create the solver and give it the map
				SolverPlanningSmallBridge smallBridgeSolver = new SolverPlanningSmallBridge(StateGenerator.getMap(dungeon));
				
				// run BFS
				smallBridgeSolver.BFS();
			} else if (dungeon.getName().equals("grid")) {
				SolverPlanningGrid gridSolver = new SolverPlanningGrid(StateGenerator.getMap(dungeon));
				gridSolver.BFS();
			} else {
				throw new IllegalStateException("Bad dungeon ID: " + dungeon.getName());
					
			}
			
				

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

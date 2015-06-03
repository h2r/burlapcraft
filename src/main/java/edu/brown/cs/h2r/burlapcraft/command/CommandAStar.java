package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.solver.SolverPlanningBridge;
import edu.brown.cs.h2r.burlapcraft.solver.SolverPlanningFinder;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

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
		return "astar";
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
			
			int dungeonID = BurlapCraft.dungeonLocID;
			
			if (dungeonID == 0) {
				sender.addChatMessage(new ChatComponentText("You are not inside a dungeon"));
				return;
			}
			
			switch (dungeonID) {
			
			case 1:
				// create the solver and give it the map
				SolverPlanningFinder finderSolver = new SolverPlanningFinder(StateGenerator.getMap(dungeonID));
				
				// run ASTAR
				finderSolver.ASTAR();
				
				break;
			
			case 2:
				System.out.println(dungeonID);
				// create the solver and give it the map
				SolverPlanningBridge bridgeSolver = new SolverPlanningBridge(StateGenerator.getMap(dungeonID));
				
				// run ASTAR
				bridgeSolver.ASTAR();
				
				break;
			
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

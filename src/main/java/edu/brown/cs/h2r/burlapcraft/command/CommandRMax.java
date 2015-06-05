package edu.brown.cs.h2r.burlapcraft.command;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.h2r.burlapcraft.solver.GotoSolver;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.DungeonEnum;
import edu.brown.cs.h2r.burlapcraft.solver.SolverLearningBridge;
import edu.brown.cs.h2r.burlapcraft.solver.SolverLearningFinder;
import edu.brown.cs.h2r.burlapcraft.solver.SolverLearningGrid;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

public class CommandRMax implements ICommand {

	private final List aliases;
	
	public CommandRMax() {
		aliases = new ArrayList();
		aliases.add("rmax");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "rmax";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "rmax";
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
			
			DungeonEnum dungeonID = BurlapCraft.dungeonID;
			
			if (dungeonID == null) {
				sender.addChatMessage(new ChatComponentText("You are not inside a dungeon"));
				return;
			}

			GotoSolver.learn();

			/*
			if (dungeonID == DungeonEnum.FINDER) {
				// create the solver and give it the map
				SolverLearningFinder finderSolver = new SolverLearningFinder(StateGenerator.getMap(dungeonID));
				
				// run RMax
				finderSolver.RMAX();
				
			} else if (dungeonID == DungeonEnum.TINY_BRIDGE) {
				// create the solver and give it the map
				SolverLearningBridge bridgeSolver = new SolverLearningBridge(StateGenerator.getMap(dungeonID));
				
				// run RMax
				bridgeSolver.RMAX();
				
			} else if (dungeonID == DungeonEnum.GRID) {
				// create the solver and give it the map
				SolverLearningGrid gridSolver = new SolverLearningGrid(StateGenerator.getMap(dungeonID));
				
				// run RMax
				gridSolver.RMAX();
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

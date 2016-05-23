package edu.brown.cs.h2r.burlapcraft.command;


import burlap.mdp.core.state.State;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.MinecraftDomainGenerator;
import edu.brown.cs.h2r.burlapcraft.state.MinecraftStateGeneratorHelper;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James MacGlashan.
 */
public class CommandCheckState implements ICommand{

	List aliases = new ArrayList();

	public CommandCheckState(){
		this.aliases.add("checkState");
		MinecraftDomainGenerator mdg = new MinecraftDomainGenerator();
	}

	@Override
	public String getCommandName() {
		return "checkState";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "checkState";
	}

	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		State s = MinecraftStateGeneratorHelper.getCurrentState(BurlapCraft.currentDungeon);
		System.out.println("\n" + s.toString());
		System.out.println(MinecraftStateGeneratorHelper.invBlockNameMap);
		System.out.println(MinecraftStateGeneratorHelper.blockNameMap);
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}

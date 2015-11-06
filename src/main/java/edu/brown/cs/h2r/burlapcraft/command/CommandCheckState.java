package edu.brown.cs.h2r.burlapcraft.command;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.MinecraftDomainGenerator;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James MacGlashan.
 */
public class CommandCheckState implements ICommand{

	List aliases = new ArrayList();
	Domain domain;

	public CommandCheckState(){
		this.aliases.add("checkState");
		MinecraftDomainGenerator mdg = new MinecraftDomainGenerator(30, 30, 30);
		domain = mdg.generateDomain();
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
		State s = StateGenerator.getCurrentState(domain, BurlapCraft.currentDungeon);
		System.out.println("\n" + s.getCompleteStateDescriptionWithUnsetAttributesAsNull());
		System.out.println(StateGenerator.invBlockNameMap);
		System.out.println(StateGenerator.blockNameMap);
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

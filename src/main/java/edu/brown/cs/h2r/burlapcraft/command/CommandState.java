package edu.brown.cs.h2r.burlapcraft.command;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.State;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorReal;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James MacGlashan.
 */
public class CommandState implements ICommand{

	List aliass = new ArrayList();
	Domain domain;

	public CommandState(){
		this.aliass.add("checkState");
		DomainGeneratorReal rdg = new DomainGeneratorReal(30, 30, 30);
		domain = rdg.generateDomain();
	}

	@Override
	public String getCommandName() {
		return "checkState";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "check_state";
	}

	@Override
	public List getCommandAliases() {
		return aliass;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		State s = StateGenerator.getCurrentState(domain, BurlapCraft.dungeonID);
		System.out.println(s.getCompleteStateDescriptionWithUnsetAttributesAsNull());
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

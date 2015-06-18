package edu.brown.cs.h2r.burlapcraft.command;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.GroundedProp;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.State;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.DomainGeneratorReal;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

/**
 * @author James MacGlashan.
 */
public class CommandCheckProps implements ICommand{

	static String commandName = "checkProps";

	@Override
	public String getCommandName() {
		return commandName;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return commandName;
	}

	@Override
	public List getCommandAliases() {
		List<String>a = new ArrayList<String>();
		a.add(commandName);
		return a;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {

		DomainGeneratorReal rdg = new DomainGeneratorReal(100, 100, 100);
		Domain domain = rdg.generateDomain();

		State s = StateGenerator.getCurrentState(domain, BurlapCraft.currentDungeon);

		List<GroundedProp> gps = PropositionalFunction.getAllGroundedPropsFromPFList(domain.getPropFunctions(), s);
		StringBuffer buf = new StringBuffer();
		for(GroundedProp gp : gps){
			if(!gp.isTrue(s)){
				buf.append("NOT ");
			}
			buf.append(gp.toString());
			buf.append("\n");
		}
		


		sender.addChatMessage(new ChatComponentText(buf.toString()));

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

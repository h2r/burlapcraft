package edu.brown.cs.h2r.burlapcraft.command;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.GroundedProp;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.states.State;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.MinecraftDomainGenerator;
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
		return commandName + " [+not]";
	}

	@Override
	public List getCommandAliases() {
		List<String>a = new ArrayList<String>();
		a.add(commandName);
		return a;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {

		MinecraftDomainGenerator mdg = new MinecraftDomainGenerator(100, 100, 100);
		Domain domain = mdg.generateDomain();

		boolean printFalse = false;
		if(args.length > 0){
			if(args[0].equals("+not")){
				printFalse = true;
			}
		}

		State s = StateGenerator.getCurrentState(domain, BurlapCraft.currentDungeon);

		List<GroundedProp> gps = PropositionalFunction.getAllGroundedPropsFromPFList(domain.getPropFunctions(), s);
		StringBuffer buf = new StringBuffer();
		buf.append("\n");
		for(GroundedProp gp : gps){
			if(!gp.isTrue(s)){
				if(printFalse) {
					buf.append("NOT ");
					buf.append(gp.toString());
					buf.append("\n");
				}
			}
			else{
				buf.append(gp.toString());
				buf.append("\n");
			}

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

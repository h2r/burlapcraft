package edu.brown.cs.h2r.burlapcraft.command;

import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author James MacGlashan.
 */
public class CommandInventory implements ICommand{

	private final List aliases;

	public CommandInventory(){
		aliases = new ArrayList();
		aliases.add("c_inv");
	}


	@Override
	public String getCommandName() {
		return "c_inv";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "c_inv";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {

		Map<String, Integer> items = HelperActions.checkInventory();
		for(Map.Entry<String, Integer> i : items.entrySet()){
			System.out.println("has " + i.getValue() + " " + i.getKey());
		}

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

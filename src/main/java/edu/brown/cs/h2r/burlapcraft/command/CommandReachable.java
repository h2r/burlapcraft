package edu.brown.cs.h2r.burlapcraft.command;

import burlap.behavior.singleagent.auxiliary.StateReachability;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.MinecraftDomainGenerator;
import edu.brown.cs.h2r.burlapcraft.state.BCAgent;
import edu.brown.cs.h2r.burlapcraft.state.MinecraftStateGeneratorHelper;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.List;

import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.CLASS_AGENT;

/**
 * @author James MacGlashan.
 */
public class CommandReachable implements ICommand{


	@Override
	public String getCommandName() {
		return "reachable";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "";
	}

	@Override
	public List getCommandAliases() {
		return Arrays.asList("reachable");
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {

		MinecraftDomainGenerator mdg = new MinecraftDomainGenerator();
		SADomain domain = mdg.generateDomain();

		State in = MinecraftStateGeneratorHelper.getCurrentState(BurlapCraft.currentDungeon);
		List<State> reachable = StateReachability.getReachableStates(in, domain, new SimpleHashableStateFactory());
		for(State s : reachable){
			OOState os = (OOState)s;
			BCAgent a = (BCAgent)os.object(CLASS_AGENT);
			System.out.println(a.x + ", " + a.y + ", " + a.z + ", " + a.rdir + ", "+ a.vdir + ", " + a.selected);
		}
		System.out.println(reachable.size());

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

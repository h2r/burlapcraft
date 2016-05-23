package edu.brown.cs.h2r.burlapcraft.domaingenerator;

import burlap.mdp.core.Action;
import burlap.mdp.core.SimpleAction;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.oo.state.generic.GenericOOState;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.action.ActionType;
import burlap.mdp.singleagent.action.UniversalActionType;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCAgent;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCBlock;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.CLASS_AGENT;

/**
 * An {@link ActionType} for Minecraft actions that prevent action when the agent is on a danger block (implicit termination)
 * @author James MacGlashan.
 */
public class MinecraftActionType extends UniversalActionType {

	public MinecraftActionType(String typeName) {
		super(typeName);
	}

	@Override
	public List<Action> allApplicableActions(State s) {
		BCAgent a = (BCAgent)((GenericOOState)s).object(CLASS_AGENT);

		List<ObjectInstance> blocks = ((OOState)s).objectsOfClass(HelperNameSpace.CLASS_BLOCK);
		for (ObjectInstance block : blocks) {
			if (HelperActions.blockIsOneOf(Block.getBlockById(((BCBlock)block).type), HelperActions.dangerBlocks)) {
				int dangerX = ((BCBlock)block).x;
				int dangerY = ((BCBlock)block).y;
				int dangerZ = ((BCBlock)block).z;
				if ((a.x == dangerX) && (a.y - 1 == dangerY) && (a.z == dangerZ) || (a.x == dangerX) && (a.y == dangerY) && (a.z == dangerZ)) {
					return new ArrayList<Action>();
				}
			}
		}

		//otherwise we pass check
		return Arrays.<Action>asList(new SimpleAction(typeName));
	}
}

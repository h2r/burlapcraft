package edu.brown.cs.h2r.burlapcraft.action;

import burlap.mdp.core.Domain;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.oo.state.generic.GenericOOState;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.GroundedAction;
import burlap.mdp.singleagent.common.SimpleAction;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCAgent;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCBlock;
import net.minecraft.block.Block;

import java.util.List;

import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.CLASS_AGENT;

public class ActionChangeItemSimulated extends SimpleAction.SimpleDeterministicAction {

	public ActionChangeItemSimulated(String name, Domain domain) {
		super(name, domain);
	}

	@Override
	protected State sampleHelper(State s, GroundedAction groundedAction) {
		GenericOOState gs = (GenericOOState)s;

		//get agent and current position
		BCAgent agent = (BCAgent)gs.touch(CLASS_AGENT);
//		int currentEquippedItemID = agent.getIntValForAttribute(HelperNameSpace.VAR_SEL);
//		List<ObjectInstance> invBlocks = s.getObjectsOfClass(HelperNameSpace.CLASS_INVENTORY_BLOCK);
//		List<Integer> blockIDs = new ArrayList<Integer>();
//		for (ObjectInstance invBlock : invBlocks) {
//			int blockID = invBlock.getIntValForAttribute(HelperNameSpace.VAR_BT);
//			if (blockID != currentEquippedItemID) {
//				blockIDs.add(blockID);
//			}
//		}
//		if (currentEquippedItemID != 278) {
//			blockIDs.add(278);
//		}
//
//		Random rand = new Random();
//		if (blockIDs.size() > 0) {
//			agent.setValue(HelperNameSpace.VAR_SEL, blockIDs.get(rand.nextInt(blockIDs.size())));
//		}
		
		return s;
	}
	
	@Override
	public boolean applicableInState(State s, GroundedAction groundedAction) {
		BCAgent a = (BCAgent)((GenericOOState)s).object(CLASS_AGENT);

		List<ObjectInstance> blocks = ((OOState)s).objectsOfClass(HelperNameSpace.CLASS_BLOCK);
		for (ObjectInstance block : blocks) {
			if (HelperActions.blockIsOneOf(Block.getBlockById(((BCBlock)block).type), HelperActions.dangerBlocks)) {
				int dangerX = ((BCBlock)block).x;
				int dangerY = ((BCBlock)block).y;
				int dangerZ = ((BCBlock)block).z;
				if ((a.x == dangerX) && (a.y - 1 == dangerY) && (a.z == dangerZ) || (a.x == dangerX) && (a.y == dangerY) && (a.z == dangerZ)) {
					return false;
				}
			}
		}
		return true;
	}

}

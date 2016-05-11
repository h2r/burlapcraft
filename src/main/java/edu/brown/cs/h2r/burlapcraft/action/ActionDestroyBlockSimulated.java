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

public class ActionDestroyBlockSimulated extends SimpleAction.SimpleDeterministicAction {

	public ActionDestroyBlockSimulated(String name, Domain domain) {
		super(name, domain);
	}

	@Override
	protected State sampleHelper(State s, GroundedAction groundedAction) {
		//get agent and current position
		GenericOOState gs = (GenericOOState)s;

		//get agent and current position
		BCAgent agent = (BCAgent)gs.touch(CLASS_AGENT);

//		int curX = agent.getIntValForAttribute(HelperNameSpace.VAR_X);
//		int curY = agent.getIntValForAttribute(HelperNameSpace.VAR_Y);
//		int curZ = agent.getIntValForAttribute(HelperNameSpace.VAR_Z);
//		int rotDir = agent.getIntValForAttribute(HelperNameSpace.VAR_R_DIR);
//		int vertDir = agent.getIntValForAttribute(HelperNameSpace.VAR_V_DIR);
//		int currentItemID = agent.getIntValForAttribute(HelperNameSpace.VAR_SEL);
//
//		//get block objects and their positions
//		List<ObjectInstance> blocks = s.getObjectsOfClass(HelperNameSpace.CLASS_BLOCK);
//
//		//get inventory block objects
//		List<ObjectInstance> invBlocks = s.getObjectsOfClass(HelperNameSpace.CLASS_INVENTORY_BLOCK);
//
//		if (currentItemID == 278) {
//			return destroyResult(curX, curY, curZ, rotDir, vertDir, invBlocks, blocks, s);
//		}
		
		return s;
	}
	
	protected State destroyResult(int curX, int curY, int curZ, int rotDir, int vertDir, List<ObjectInstance> invBlocks, List<ObjectInstance> blocks, State s) {
		//System.out.println("Destroying at: " + curX + "," + curY + "," + curZ);
		// go through blocks and see if any of them can be mined
//		for (ObjectInstance block : blocks) {
//			int blockX = block.getIntValForAttribute(HelperNameSpace.VAR_X);
//			int blockY = block.getIntValForAttribute(HelperNameSpace.VAR_Y);
//			int blockZ = block.getIntValForAttribute(HelperNameSpace.VAR_Z);
//			if (((blockX == curX) && (blockZ == curZ + 1) && (blockY == curY) && (rotDir == 0) && (vertDir == 1))
//					|| ((blockX == curX) && (blockZ == curZ - 1) && (blockY == curY) && (rotDir == 2) && (vertDir == 1))
//					|| ((blockX == curX + 1) && (blockZ == curZ) && (blockY == curY) && (rotDir == 3) && (vertDir == 1))
//					|| ((blockX == curX - 1) && (blockZ == curZ) && (blockY == curY) && (rotDir == 1) && (vertDir == 1))) {
//				// get id of the block
//				int blockID = block.getIntValForAttribute(HelperNameSpace.VAR_BT);
//				// get the blockname
//				String blockName = block.getName();
//				boolean present = false;
//				for (ObjectInstance invBlock : invBlocks) {
//					// check if an invBlock instance of the same block type exists
//					if (blockID == invBlock.getIntValForAttribute(HelperNameSpace.VAR_BT)) {
//						// add the blockName to the blockNames relational attribute
//						invBlock.addRelationalTarget(HelperNameSpace.VAR_BLOCK_NAMES, blockName);
//						present = true;
//					}
//				}
//				if (!present) {
//					// create a new object instance if it is not present
//					ObjectInstance inventoryBlockInstance = new MutableObjectInstance(domain.getObjectClass(HelperNameSpace.CLASS_INVENTORY_BLOCK), "inventoryBlock" + invBlocks.size());
//					inventoryBlockInstance.setValue(HelperNameSpace.VAR_BT, blockID);
//					inventoryBlockInstance.addRelationalTarget(HelperNameSpace.VAR_BLOCK_NAMES, blockName);
//					s.addObject(inventoryBlockInstance);
//				}
//
//				s.removeObject(block);
//			}
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

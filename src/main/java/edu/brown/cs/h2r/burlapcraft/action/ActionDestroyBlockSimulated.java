package edu.brown.cs.h2r.burlapcraft.action;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.MutableObjectInstance;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.core.TransitionProbability;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.common.SimpleAction.SimpleDeterministicAction;

public class ActionDestroyBlockSimulated extends SimpleDeterministicAction {

	public ActionDestroyBlockSimulated(String name, Domain domain) {
		super(name, domain);
	}

	@Override
	protected State performActionHelper(State s, GroundedAction groundedAction) {
		//get agent and current position
		ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
		int curX = agent.getIntValForAttribute(HelperNameSpace.ATX);
		int curY = agent.getIntValForAttribute(HelperNameSpace.ATY);
		int curZ = agent.getIntValForAttribute(HelperNameSpace.ATZ);
		int rotDir = agent.getIntValForAttribute(HelperNameSpace.ATROTDIR);
		int vertDir = agent.getIntValForAttribute(HelperNameSpace.ATVERTDIR);
		int currentItemID = agent.getIntValForAttribute(HelperNameSpace.ATSELECTEDITEMID);
		
		//get block objects and their positions
		List<ObjectInstance> blocks = s.getObjectsOfClass(HelperNameSpace.CLASSBLOCK);
		
		//get inventory block objects
		List<ObjectInstance> invBlocks = s.getObjectsOfClass(HelperNameSpace.CLASSINVENTORYBLOCK);
		
		if (currentItemID == 278) {
			return destroyResult(curX, curY, curZ, rotDir, vertDir, invBlocks, blocks, s);
		}
		
		return s;
	}
	
	protected State destroyResult(int curX, int curY, int curZ, int rotDir, int vertDir, List<ObjectInstance> invBlocks, List<ObjectInstance> blocks, State s) {
		//System.out.println("Destroying at: " + curX + "," + curY + "," + curZ);
		// go through blocks and see if any of them can be mined
		for (ObjectInstance block : blocks) {
			int blockX = block.getIntValForAttribute(HelperNameSpace.ATX);
			int blockY = block.getIntValForAttribute(HelperNameSpace.ATY);
			int blockZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
			if (((blockX == curX) && (blockZ == curZ + 1) && (blockY == curY) && (rotDir == 0) && (vertDir == 1)) 
					|| ((blockX == curX) && (blockZ == curZ - 1) && (blockY == curY) && (rotDir == 2) && (vertDir == 1))
					|| ((blockX == curX + 1) && (blockZ == curZ) && (blockY == curY) && (rotDir == 3) && (vertDir == 1)) 
					|| ((blockX == curX - 1) && (blockZ == curZ) && (blockY == curY) && (rotDir == 1) && (vertDir == 1))) {
				// get id of the block
				int blockID = block.getIntValForAttribute(HelperNameSpace.ATBTYPE);
				// get the blockname
				String blockName = block.getName();
				boolean present = false;
				for (ObjectInstance invBlock : invBlocks) {
					// check if an invBlock instance of the same block type exists
					if (blockID == invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE)) {
						// add the blockName to the blockNames relational attribute
						invBlock.addRelationalTarget(HelperNameSpace.ATBLOCKNAMES, blockName);
						present = true;
					}
				}
				if (!present) {
					// create a new object instance if it is not present
					ObjectInstance inventoryBlockInstance = new MutableObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSINVENTORYBLOCK), "inventoryBlock" + invBlocks.size());
					inventoryBlockInstance.setValue(HelperNameSpace.ATBTYPE, blockID);
					inventoryBlockInstance.addRelationalTarget(HelperNameSpace.ATBLOCKNAMES, blockName);
					s.addObject(inventoryBlockInstance);
				}
				
				s.removeObject(block);
			}
		}
		
		return s;
		
	}
	
	@Override
	public boolean applicableInState(State s, GroundedAction groundedAction) {
		ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
		int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
		int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
		int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);
		List<ObjectInstance> blocks = s.getObjectsOfClass(HelperNameSpace.CLASSBLOCK);
		for (ObjectInstance block : blocks) {
			if (HelperActions.blockIsOneOf(Block.getBlockById(block.getIntValForAttribute(HelperNameSpace.ATBTYPE)), HelperActions.dangerBlocks)) {
				int dangerX = block.getIntValForAttribute(HelperNameSpace.ATX);
				int dangerY = block.getIntValForAttribute(HelperNameSpace.ATY);
				int dangerZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
				if ((ax == dangerX) && (ay - 1 == dangerY) && (az == dangerZ) || (ax == dangerX) && (ay == dangerY) && (az == dangerZ)) {
					return false;
				}
			}
		}
		return true;
	}

}

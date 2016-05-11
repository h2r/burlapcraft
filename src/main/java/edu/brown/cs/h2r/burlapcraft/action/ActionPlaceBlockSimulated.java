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


public class ActionPlaceBlockSimulated extends SimpleAction.SimpleDeterministicAction {
	
	private int[][][] map;

	public ActionPlaceBlockSimulated(String name, Domain domain, int[][][] map) {
		super(name, domain);
		this.map = map;
	}
	
	@Override
	protected State sampleHelper(State s, GroundedAction groundedAction) {
		//get agent and current position
//		ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASS_AGENT);
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
//		//get inventoryBlocks
//		List<ObjectInstance> invBlocks = s.getObjectsOfClass(HelperNameSpace.CLASS_INVENTORY_BLOCK);
//
//		for (ObjectInstance invBlock : invBlocks) {
//			if (invBlock.getIntValForAttribute(HelperNameSpace.VAR_BT) == currentItemID) {
//				return placeResult(curX, curY, curZ, rotDir, vertDir, blocks, invBlock, s, agent);
//			}
//		}
		
		return s;
	}
	
	protected State placeResult(int curX, int curY, int curZ, int rotDir, int vertDir, List<ObjectInstance> blocks, ObjectInstance invBlock, State s, ObjectInstance agent) {
		// check if agent is facing down
//		if (vertDir == 1) {
//			// check if space is empty based on rotDir using map and blocks list
//			switch (rotDir) {
//			case 0:
//				if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX][curZ + 1]), HelperActions.placeInBlocks)) {
//					boolean anchorsPresent = false;
//					boolean needToReplaceBlock = false;
//					ObjectInstance replaceBlock = null;
//					if (((curY - 2 >= 0) && map[curY - 2][curX][curZ + 1] > 0) && (map[curY - 1][curX][curZ + 2] > 0)) {
//						anchorsPresent = true;
//					}
//
//					for (ObjectInstance block : blocks) {
//						int blockX = block.getIntValForAttribute(HelperNameSpace.VAR_X);
//						int blockY = block.getIntValForAttribute(HelperNameSpace.VAR_Y);
//						int blockZ = block.getIntValForAttribute(HelperNameSpace.VAR_Z);
//						if (((blockX == curX) && (blockY == curY - 1) && (blockZ == curZ + 2))
//								|| ((blockX == curX) && (blockY == curY - 2) && (blockZ == curZ + 1))) {
//							anchorsPresent = true;
//						}
//						if ((blockX == curX) && (blockY == curY - 1) && (blockZ == curZ + 1)) {
//							needToReplaceBlock = true;
//							replaceBlock = block;
//						}
//					}
//
//					if (anchorsPresent) {
//						int blockID = invBlock.getIntValForAttribute(HelperNameSpace.VAR_BT);
//
//						Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.VAR_BLOCK_NAMES);
//						String blockName = blockNames.iterator().next();
//						if (blockNames.size() == 1) {
//							s.removeObject(invBlock);
//							agent.setValue(HelperNameSpace.VAR_SEL, -1);
//						}
//						else {
//							blockNames.remove(blockName);
//						}
//
//						ObjectInstance newBlock = new MutableObjectInstance(domain.getObjectClass(HelperNameSpace.CLASS_BLOCK), blockName);
//						newBlock.setValue(HelperNameSpace.VAR_X, curX);
//						newBlock.setValue(HelperNameSpace.VAR_Y, curY - 1);
//						newBlock.setValue(HelperNameSpace.VAR_Z, curZ + 1);
//						newBlock.setValue(HelperNameSpace.VAR_BT, blockID);
//						s.addObject(newBlock);
//
//						if (needToReplaceBlock) {
//							s.removeObject(replaceBlock);
//						}
//					}
//				}
//				else if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX][curZ + 1]), HelperActions.unbreakableBlocks)) {
//					int blockID = invBlock.getIntValForAttribute(HelperNameSpace.VAR_BT);
//
//					Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.VAR_BLOCK_NAMES);
//					String blockName = blockNames.iterator().next();
//					if (blockNames.size() == 1) {
//						s.removeObject(invBlock);
//						agent.setValue(HelperNameSpace.VAR_SEL, -1);
//					}
//					else {
//						blockNames.remove(blockName);
//					}
//
//					ObjectInstance newBlock = new MutableObjectInstance(domain.getObjectClass(HelperNameSpace.CLASS_BLOCK), blockName);
//					newBlock.setValue(HelperNameSpace.VAR_X, curX);
//					newBlock.setValue(HelperNameSpace.VAR_Y, curY);
//					newBlock.setValue(HelperNameSpace.VAR_Z, curZ + 1);
//					newBlock.setValue(HelperNameSpace.VAR_BT, blockID);
//					s.addObject(newBlock);
//				}
//				break;
//			case 1:
//				if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX - 1][curZ]), HelperActions.placeInBlocks)) {
//					boolean anchorsPresent = false;
//					boolean needToReplaceBlock = false;
//					ObjectInstance replaceBlock = null;
//					if (((curY - 2 >= 0) && map[curY - 2][curX - 1][curZ] > 0) && (map[curY - 1][curX - 2][curZ] > 0)) {
//						anchorsPresent = true;
//					}
//
//					for (ObjectInstance block : blocks) {
//						int blockX = block.getIntValForAttribute(HelperNameSpace.VAR_X);
//						int blockY = block.getIntValForAttribute(HelperNameSpace.VAR_Y);
//						int blockZ = block.getIntValForAttribute(HelperNameSpace.VAR_Z);
//						if (((blockX == curX - 2) && (blockY == curY - 1) && (blockZ == curZ))
//								|| ((blockX == curX - 1) && (blockY == curY - 2) && (blockZ == curZ))) {
//							anchorsPresent = true;
//						}
//						if ((blockX == curX - 1) && (blockY == curY - 1) && (blockZ == curZ)) {
//							needToReplaceBlock = true;
//							replaceBlock = block;
//						}
//					}
//
//					if (anchorsPresent) {
//						int blockID = invBlock.getIntValForAttribute(HelperNameSpace.VAR_BT);
//						Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.VAR_BLOCK_NAMES);
//						String blockName = blockNames.iterator().next();
//						if (blockNames.size() == 1) {
//							s.removeObject(invBlock);
//							agent.setValue(HelperNameSpace.VAR_SEL, -1);
//						}
//						else {
//							blockNames.remove(blockName);
//						}
//
//						ObjectInstance newBlock = new MutableObjectInstance(domain.getObjectClass(HelperNameSpace.CLASS_BLOCK), blockName);
//						newBlock.setValue(HelperNameSpace.VAR_X, curX - 1);
//						newBlock.setValue(HelperNameSpace.VAR_Y, curY - 1);
//						newBlock.setValue(HelperNameSpace.VAR_Z, curZ);
//						newBlock.setValue(HelperNameSpace.VAR_BT, blockID);
//						s.addObject(newBlock);
//
//						if (needToReplaceBlock) {
//							s.removeObject(replaceBlock);
//						}
//					}
//				}
//				else if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX - 1][curZ]), HelperActions.unbreakableBlocks)) {
//					int blockID = invBlock.getIntValForAttribute(HelperNameSpace.VAR_BT);
//					Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.VAR_BLOCK_NAMES);
//					String blockName = blockNames.iterator().next();
//					if (blockNames.size() == 1) {
//						s.removeObject(invBlock);
//						agent.setValue(HelperNameSpace.VAR_SEL, -1);
//					}
//					else {
//						blockNames.remove(blockName);
//					}
//
//					ObjectInstance newBlock = new MutableObjectInstance(domain.getObjectClass(HelperNameSpace.CLASS_BLOCK), blockName);
//					newBlock.setValue(HelperNameSpace.VAR_X, curX - 1);
//					newBlock.setValue(HelperNameSpace.VAR_Y, curY);
//					newBlock.setValue(HelperNameSpace.VAR_Z, curZ);
//					newBlock.setValue(HelperNameSpace.VAR_BT, blockID);
//					s.addObject(newBlock);
//				}
//				break;
//			case 2:
//				if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX][curZ - 1]), HelperActions.placeInBlocks)) {
//					boolean anchorsPresent = false;
//					boolean needToReplaceBlock = false;
//					ObjectInstance replaceBlock = null;
//					if (((curY - 2) >= 0 && (curZ - 1 >= 0) && map[curY - 2][curX][curZ - 1] > 0) && (map[curY - 1][curX][curZ - 2] > 0)) {
//						anchorsPresent = true;
//					}
//
//					for (ObjectInstance block : blocks) {
//						int blockX = block.getIntValForAttribute(HelperNameSpace.VAR_X);
//						int blockY = block.getIntValForAttribute(HelperNameSpace.VAR_Y);
//						int blockZ = block.getIntValForAttribute(HelperNameSpace.VAR_Z);
//						if (((blockX == curX) && (blockY == curY - 1) && (blockZ == curZ - 2))
//								|| ((blockX == curX) && (blockY == curY - 2) && (blockZ == curZ - 1))) {
//							anchorsPresent = true;
//						}
//						if ((blockX == curX) && (blockY == curY - 1) && (blockZ == curZ - 1)) {
//							needToReplaceBlock = true;
//							replaceBlock = block;
//						}
//					}
//
//					if (anchorsPresent) {
//						int blockID = invBlock.getIntValForAttribute(HelperNameSpace.VAR_BT);
//						Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.VAR_BLOCK_NAMES);
//						String blockName = blockNames.iterator().next();
//						if (blockNames.size() == 1) {
//							s.removeObject(invBlock);
//							agent.setValue(HelperNameSpace.VAR_SEL, -1);
//						}
//						else {
//							blockNames.remove(blockName);
//						}
//
//						ObjectInstance newBlock = new MutableObjectInstance(domain.getObjectClass(HelperNameSpace.CLASS_BLOCK), blockName);
//						newBlock.setValue(HelperNameSpace.VAR_X, curX);
//						newBlock.setValue(HelperNameSpace.VAR_Y, curY - 1);
//						newBlock.setValue(HelperNameSpace.VAR_Z, curZ - 1);
//						newBlock.setValue(HelperNameSpace.VAR_BT, blockID);
//						s.addObject(newBlock);
//
//						if (needToReplaceBlock) {
//							s.removeObject(replaceBlock);
//						}
//					}
//				}
//				else if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX][curZ - 1]), HelperActions.unbreakableBlocks)) {
//					int blockID = invBlock.getIntValForAttribute(HelperNameSpace.VAR_BT);
//					Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.VAR_BLOCK_NAMES);
//					String blockName = blockNames.iterator().next();
//					if (blockNames.size() == 1) {
//						s.removeObject(invBlock);
//						agent.setValue(HelperNameSpace.VAR_SEL, -1);
//					}
//					else {
//						blockNames.remove(blockName);
//					}
//
//					ObjectInstance newBlock = new MutableObjectInstance(domain.getObjectClass(HelperNameSpace.CLASS_BLOCK), blockName);
//					newBlock.setValue(HelperNameSpace.VAR_X, curX);
//					newBlock.setValue(HelperNameSpace.VAR_Y, curY);
//					newBlock.setValue(HelperNameSpace.VAR_Z, curZ - 1);
//					newBlock.setValue(HelperNameSpace.VAR_BT, blockID);
//					s.addObject(newBlock);
//				}
//				break;
//			case 3:
//				if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX + 1][curZ]), HelperActions.placeInBlocks)) {
//					boolean anchorsPresent = false;
//					boolean needToReplaceBlock = false;
//					ObjectInstance replaceBlock = null;
//					if (((curY - 2 >= 0) && map[curY - 2][curX + 1][curZ] > 0) && (map[curY - 1][curX + 2][curZ] > 0)) {
//						anchorsPresent = true;
//					}
//
//					for (ObjectInstance block : blocks) {
//						int blockX = block.getIntValForAttribute(HelperNameSpace.VAR_X);
//						int blockY = block.getIntValForAttribute(HelperNameSpace.VAR_Y);
//						int blockZ = block.getIntValForAttribute(HelperNameSpace.VAR_Z);
//						if (((blockX == curX + 2) && (blockY == curY - 1) && (blockZ == curZ))
//								|| ((blockX == curX + 1) && (blockY == curY - 2) && (blockZ == curZ))) {
//							anchorsPresent = true;
//						}
//						if ((blockX == curX + 1) && (blockY == curY - 1) && (blockZ == curZ)) {
//							needToReplaceBlock = true;
//							replaceBlock = block;
//						}
//					}
//
//					if (anchorsPresent) {
//						int blockID = invBlock.getIntValForAttribute(HelperNameSpace.VAR_BT);
//						Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.VAR_BLOCK_NAMES);
//						String blockName = blockNames.iterator().next();
//						if (blockNames.size() == 1) {
//							s.removeObject(invBlock);
//							agent.setValue(HelperNameSpace.VAR_SEL, -1);
//						}
//						else {
//							blockNames.remove(blockName);
//						}
//
//						ObjectInstance newBlock = new MutableObjectInstance(domain.getObjectClass(HelperNameSpace.CLASS_BLOCK), blockName);
//						newBlock.setValue(HelperNameSpace.VAR_X, curX + 1);
//						newBlock.setValue(HelperNameSpace.VAR_Y, curY - 1);
//						newBlock.setValue(HelperNameSpace.VAR_Z, curZ);
//						newBlock.setValue(HelperNameSpace.VAR_BT, blockID);
//						s.addObject(newBlock);
//
//						if (needToReplaceBlock) {
//							s.removeObject(replaceBlock);
//						}
//					}
//				}
//				else if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX + 1][curZ]), HelperActions.unbreakableBlocks)) {
//					int blockID = invBlock.getIntValForAttribute(HelperNameSpace.VAR_BT);
//					Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.VAR_BLOCK_NAMES);
//					String blockName = blockNames.iterator().next();
//					if (blockNames.size() == 1) {
//						s.removeObject(invBlock);
//						agent.setValue(HelperNameSpace.VAR_SEL, -1);
//					}
//					else {
//						blockNames.remove(blockName);
//					}
//
//					ObjectInstance newBlock = new MutableObjectInstance(domain.getObjectClass(HelperNameSpace.CLASS_BLOCK), blockName);
//					newBlock.setValue(HelperNameSpace.VAR_X, curX + 1);
//					newBlock.setValue(HelperNameSpace.VAR_Y, curY);
//					newBlock.setValue(HelperNameSpace.VAR_Z, curZ);
//					newBlock.setValue(HelperNameSpace.VAR_BT, blockID);
//					s.addObject(newBlock);
//				}
//				break;
//			default:
//				break;
//			}
//
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

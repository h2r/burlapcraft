package edu.brown.cs.h2r.burlapcraft.action;

import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TransitionProbability;

public class ActionPlaceBlockSimulated extends ActionAgentSimulated {
	
	private int[][][] map;

	public ActionPlaceBlockSimulated(String name, Domain domain, int[][][] map) {
		super(name, domain);
		this.map = map;
	}

	@Override
	State doAction(State s) {
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
		
		//get inventoryBlocks
		List<ObjectInstance> invBlocks = s.getObjectsOfClass(HelperNameSpace.CLASSINVENTORYBLOCK);
		
		for (ObjectInstance invBlock : invBlocks) {
			if (invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE) == currentItemID) {
				return placeResult(curX, curY, curZ, rotDir, vertDir, blocks, invBlock, s, agent);
			}
		}
		
		return s;
		
	}
	
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		
		return this.deterministicTransition(s, params);
		
	}
	
	protected State placeResult(int curX, int curY, int curZ, int rotDir, int vertDir, List<ObjectInstance> blocks, ObjectInstance invBlock, State s, ObjectInstance agent) {
		// check if agent is facing down
		if (vertDir == 1) {
			// check if space is empty based on rotDir using map and blocks list
			switch (rotDir) {
			case 0:
				if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX][curZ + 1]), HelperActions.placeInBlocks)) {
					boolean anchorsPresent = false;
					boolean needToReplaceBlock = false;
					ObjectInstance replaceBlock = null;
					if (((curY - 2 >= 0) && map[curY - 2][curX][curZ + 1] > 0) && (map[curY - 1][curX][curZ + 2] > 0)) {
						anchorsPresent = true;
					}
					
					for (ObjectInstance block : blocks) {
						int blockX = block.getIntValForAttribute(HelperNameSpace.ATX);
						int blockY = block.getIntValForAttribute(HelperNameSpace.ATY);
						int blockZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
						if (((blockX == curX) && (blockY == curY - 1) && (blockZ == curZ + 2)) 
								|| ((blockX == curX) && (blockY == curY - 2) && (blockZ == curZ + 1))) {
							anchorsPresent = true;
						}
						if ((blockX == curX) && (blockY == curY - 1) && (blockZ == curZ + 1)) {
							needToReplaceBlock = true;
							replaceBlock = block;
						}
					}
					
					if (anchorsPresent) {
						int blockID = invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE);
						
						Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.ATBLOCKNAMES);
						String blockName = blockNames.iterator().next();
						if (blockNames.size() == 1) {
							s.removeObject(invBlock);
							agent.setValue(HelperNameSpace.ATSELECTEDITEMID, -1);
						}
						else {
							blockNames.remove(blockName);
						}
						
						ObjectInstance newBlock = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSBLOCK), blockName);
						newBlock.setValue(HelperNameSpace.ATX, curX);
						newBlock.setValue(HelperNameSpace.ATY, curY - 1);
						newBlock.setValue(HelperNameSpace.ATZ, curZ + 1);
						newBlock.setValue(HelperNameSpace.ATBTYPE, blockID);
						s.addObject(newBlock);
						
						if (needToReplaceBlock) {
							s.removeObject(replaceBlock);
						} 
					}
				}
				else if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX][curZ + 1]), HelperActions.unbreakableBlocks)) {
					int blockID = invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE);
					
					Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.ATBLOCKNAMES);
					String blockName = blockNames.iterator().next();
					if (blockNames.size() == 1) {
						s.removeObject(invBlock);
						agent.setValue(HelperNameSpace.ATSELECTEDITEMID, -1);
					}
					else {
						blockNames.remove(blockName);
					}
					
					ObjectInstance newBlock = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSBLOCK), blockName);
					newBlock.setValue(HelperNameSpace.ATX, curX);
					newBlock.setValue(HelperNameSpace.ATY, curY);
					newBlock.setValue(HelperNameSpace.ATZ, curZ + 1);
					newBlock.setValue(HelperNameSpace.ATBTYPE, blockID);
					s.addObject(newBlock);
				}
				break;
			case 1:
				if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX - 1][curZ]), HelperActions.placeInBlocks)) {
					boolean anchorsPresent = false;
					boolean needToReplaceBlock = false;
					ObjectInstance replaceBlock = null;
					if (((curY - 2 >= 0) && map[curY - 2][curX - 1][curZ] > 0) && (map[curY - 1][curX - 2][curZ] > 0)) {
						anchorsPresent = true;
					}
				
					for (ObjectInstance block : blocks) {
						int blockX = block.getIntValForAttribute(HelperNameSpace.ATX);
						int blockY = block.getIntValForAttribute(HelperNameSpace.ATY);
						int blockZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
						if (((blockX == curX - 2) && (blockY == curY - 1) && (blockZ == curZ)) 
								|| ((blockX == curX - 1) && (blockY == curY - 2) && (blockZ == curZ))) {
							anchorsPresent = true;
						}
						if ((blockX == curX - 1) && (blockY == curY - 1) && (blockZ == curZ)) {
							needToReplaceBlock = true;
							replaceBlock = block;
						}
					}
					
					if (anchorsPresent) {
						int blockID = invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE);
						Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.ATBLOCKNAMES);
						String blockName = blockNames.iterator().next();
						if (blockNames.size() == 1) {
							s.removeObject(invBlock);
							agent.setValue(HelperNameSpace.ATSELECTEDITEMID, -1);
						}
						else {
							blockNames.remove(blockName);
						}
						
						ObjectInstance newBlock = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSBLOCK), blockName);
						newBlock.setValue(HelperNameSpace.ATX, curX - 1);
						newBlock.setValue(HelperNameSpace.ATY, curY - 1);
						newBlock.setValue(HelperNameSpace.ATZ, curZ);
						newBlock.setValue(HelperNameSpace.ATBTYPE, blockID);
						s.addObject(newBlock);
						
						if (needToReplaceBlock) {
							s.removeObject(replaceBlock);
						} 
					}
				}
				else if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX - 1][curZ]), HelperActions.unbreakableBlocks)) {
					int blockID = invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE);
					Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.ATBLOCKNAMES);
					String blockName = blockNames.iterator().next();
					if (blockNames.size() == 1) {
						s.removeObject(invBlock);
						agent.setValue(HelperNameSpace.ATSELECTEDITEMID, -1);
					}
					else {
						blockNames.remove(blockName);
					}
					
					ObjectInstance newBlock = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSBLOCK), blockName);
					newBlock.setValue(HelperNameSpace.ATX, curX - 1);
					newBlock.setValue(HelperNameSpace.ATY, curY);
					newBlock.setValue(HelperNameSpace.ATZ, curZ);
					newBlock.setValue(HelperNameSpace.ATBTYPE, blockID);
					s.addObject(newBlock);
				}
				break;
			case 2:
				if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX][curZ - 1]), HelperActions.placeInBlocks)) {
					boolean anchorsPresent = false;
					boolean needToReplaceBlock = false;
					ObjectInstance replaceBlock = null;
					if (((curY - 2) >= 0 && (curZ - 1 >= 0) && map[curY - 2][curX][curZ - 1] > 0) && (map[curY - 1][curX][curZ - 2] > 0)) {
						anchorsPresent = true;
					}
					
					for (ObjectInstance block : blocks) {
						int blockX = block.getIntValForAttribute(HelperNameSpace.ATX);
						int blockY = block.getIntValForAttribute(HelperNameSpace.ATY);
						int blockZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
						if (((blockX == curX) && (blockY == curY - 1) && (blockZ == curZ - 2)) 
								|| ((blockX == curX) && (blockY == curY - 2) && (blockZ == curZ - 1))) {
							anchorsPresent = true;
						}
						if ((blockX == curX) && (blockY == curY - 1) && (blockZ == curZ - 1)) {
							needToReplaceBlock = true;
							replaceBlock = block;
						}
					}
					
					if (anchorsPresent) {
						int blockID = invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE);
						Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.ATBLOCKNAMES);
						String blockName = blockNames.iterator().next();
						if (blockNames.size() == 1) {
							s.removeObject(invBlock);
							agent.setValue(HelperNameSpace.ATSELECTEDITEMID, -1);
						}
						else {
							blockNames.remove(blockName);
						}
						
						ObjectInstance newBlock = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSBLOCK), blockName);
						newBlock.setValue(HelperNameSpace.ATX, curX);
						newBlock.setValue(HelperNameSpace.ATY, curY - 1);
						newBlock.setValue(HelperNameSpace.ATZ, curZ - 1);
						newBlock.setValue(HelperNameSpace.ATBTYPE, blockID);
						s.addObject(newBlock);
						
						if (needToReplaceBlock) {
							s.removeObject(replaceBlock);
						} 
					}
				}
				else if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX][curZ - 1]), HelperActions.unbreakableBlocks)) {
					int blockID = invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE);
					Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.ATBLOCKNAMES);
					String blockName = blockNames.iterator().next();
					if (blockNames.size() == 1) {
						s.removeObject(invBlock);
						agent.setValue(HelperNameSpace.ATSELECTEDITEMID, -1);
					}
					else {
						blockNames.remove(blockName);
					}
					
					ObjectInstance newBlock = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSBLOCK), blockName);
					newBlock.setValue(HelperNameSpace.ATX, curX);
					newBlock.setValue(HelperNameSpace.ATY, curY);
					newBlock.setValue(HelperNameSpace.ATZ, curZ - 1);
					newBlock.setValue(HelperNameSpace.ATBTYPE, blockID);
					s.addObject(newBlock);
				}
				break;
			case 3:
				if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX + 1][curZ]), HelperActions.placeInBlocks)) {
					boolean anchorsPresent = false;
					boolean needToReplaceBlock = false;
					ObjectInstance replaceBlock = null;
					if (((curY - 2 >= 0) && map[curY - 2][curX + 1][curZ] > 0) && (map[curY - 1][curX + 2][curZ] > 0)) {
						anchorsPresent = true;
					}
					
					for (ObjectInstance block : blocks) {
						int blockX = block.getIntValForAttribute(HelperNameSpace.ATX);
						int blockY = block.getIntValForAttribute(HelperNameSpace.ATY);
						int blockZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
						if (((blockX == curX + 2) && (blockY == curY - 1) && (blockZ == curZ)) 
								|| ((blockX == curX + 1) && (blockY == curY - 2) && (blockZ == curZ))) {
							anchorsPresent = true;
						}
						if ((blockX == curX + 1) && (blockY == curY - 1) && (blockZ == curZ)) {
							needToReplaceBlock = true;
							replaceBlock = block;
						}
					}
					
					if (anchorsPresent) {
						int blockID = invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE);
						Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.ATBLOCKNAMES);
						String blockName = blockNames.iterator().next();
						if (blockNames.size() == 1) {
							s.removeObject(invBlock);
							agent.setValue(HelperNameSpace.ATSELECTEDITEMID, -1);
						}
						else {
							blockNames.remove(blockName);
						}
						
						ObjectInstance newBlock = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSBLOCK), blockName);
						newBlock.setValue(HelperNameSpace.ATX, curX + 1);
						newBlock.setValue(HelperNameSpace.ATY, curY - 1);
						newBlock.setValue(HelperNameSpace.ATZ, curZ);
						newBlock.setValue(HelperNameSpace.ATBTYPE, blockID);
						s.addObject(newBlock);
						
						if (needToReplaceBlock) {
							s.removeObject(replaceBlock);
						} 
					}
				}
				else if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX + 1][curZ]), HelperActions.unbreakableBlocks)) {
					int blockID = invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE);
					Set<String> blockNames = invBlock.getAllRelationalTargets(HelperNameSpace.ATBLOCKNAMES);
					String blockName = blockNames.iterator().next();
					if (blockNames.size() == 1) {
						s.removeObject(invBlock);
						agent.setValue(HelperNameSpace.ATSELECTEDITEMID, -1);
					}
					else {
						blockNames.remove(blockName);
					}
					
					ObjectInstance newBlock = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSBLOCK), blockName);
					newBlock.setValue(HelperNameSpace.ATX, curX + 1);
					newBlock.setValue(HelperNameSpace.ATY, curY);
					newBlock.setValue(HelperNameSpace.ATZ, curZ);
					newBlock.setValue(HelperNameSpace.ATBTYPE, blockID);
					s.addObject(newBlock);
				}
				break;
			default:
				break;
			}
			
		}
		
		return s;
		
	}
	
}

package com.kcaluru.burlapbot.action;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.kcaluru.burlapbot.helper.HelperActions;
import com.kcaluru.burlapbot.helper.HelperNameSpace;
import com.kcaluru.burlapbot.stategenerator.StateGenerator;

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
		
		//get block objects and their positions
		List<ObjectInstance> blocks = s.getObjectsOfTrueClass(HelperNameSpace.CLASSBLOCK);
		
		//get first inventory block object
		ObjectInstance invBlock = s.getFirstObjectOfClass(HelperNameSpace.CLASSINVENTORYBLOCK);
		
		if (invBlock != null) {
			return placeResult(curX, curY, curZ, rotDir, vertDir, blocks, invBlock, s);
		}
		
		return s;
		
	}
	
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		
		return this.deterministicTransition(s, params);
		
	}
	
	protected State placeResult(int curX, int curY, int curZ, int rotDir, int vertDir, List<ObjectInstance> blocks, ObjectInstance invBlock, State s) {
		// check if agent is facing down
		if (vertDir == 1) {
			// check if space is empty based on rotDir using map and blocks list
			switch (rotDir) {
			case 0:
				if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX][curZ + 1]), HelperActions.placeOnBlocks)) {
					boolean anchorsPresent = false;
					boolean needToReplaceBlock = false;
					ObjectInstance replaceBlock = null;
					if ((map[curY - 2][curX][curZ + 1] > 0) && (map[curY - 1][curX][curZ + 2] > 0)) {
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
						int blockCountID = StateGenerator.blockCount + 1;
						ObjectInstance newBlock = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSBLOCK), "block" + blockCountID);
						newBlock.setValue(HelperNameSpace.ATX, curX);
						newBlock.setValue(HelperNameSpace.ATY, curY - 1);
						newBlock.setValue(HelperNameSpace.ATZ, curZ + 1);
						newBlock.setValue(HelperNameSpace.ATBTYPE, invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE));
						s.addObject(newBlock);
						int quantity = invBlock.getIntValForAttribute(HelperNameSpace.ATIBQUANT) - 1;
						if (quantity == 0) {
							s.removeObject(invBlock);
						}
						else {
							invBlock.setValue(HelperNameSpace.ATIBQUANT, quantity);
						}
						
						if (needToReplaceBlock) {
							s.removeObject(replaceBlock);
						} 
					}
				}
				break;
			case 1:
				if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX - 1][curZ]), HelperActions.placeOnBlocks)) {
					boolean anchorsPresent = false;
					boolean needToReplaceBlock = false;
					ObjectInstance replaceBlock = null;
					if ((map[curY - 2][curX - 1][curZ] > 0) && (map[curY - 1][curX - 2][curZ] > 0)) {
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
						int blockCountID = StateGenerator.blockCount + 1;
						ObjectInstance newBlock = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSBLOCK), "block" + blockCountID);
						newBlock.setValue(HelperNameSpace.ATX, curX - 1);
						newBlock.setValue(HelperNameSpace.ATY, curY - 1);
						newBlock.setValue(HelperNameSpace.ATZ, curZ);
						newBlock.setValue(HelperNameSpace.ATBTYPE, invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE));
						s.addObject(newBlock);
						int quantity = invBlock.getIntValForAttribute(HelperNameSpace.ATIBQUANT) - 1;
						if (quantity == 0) {
							s.removeObject(invBlock);
						}
						else {
							invBlock.setValue(HelperNameSpace.ATIBQUANT, quantity);
						}
						
						if (needToReplaceBlock) {
							s.removeObject(replaceBlock);
						} 
					}
				}
				break;
			case 2:
				if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX][curZ - 1]), HelperActions.placeOnBlocks)) {
					boolean anchorsPresent = false;
					boolean needToReplaceBlock = false;
					ObjectInstance replaceBlock = null;
					if ((map[curY - 2][curX][curZ - 1] > 0) && (map[curY - 1][curX][curZ - 2] > 0)) {
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
						int blockCountID = StateGenerator.blockCount + 1;
						ObjectInstance newBlock = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSBLOCK), "block" + blockCountID);
						newBlock.setValue(HelperNameSpace.ATX, curX);
						newBlock.setValue(HelperNameSpace.ATY, curY - 1);
						newBlock.setValue(HelperNameSpace.ATZ, curZ - 1);
						newBlock.setValue(HelperNameSpace.ATBTYPE, invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE));
						s.addObject(newBlock);
						int quantity = invBlock.getIntValForAttribute(HelperNameSpace.ATIBQUANT) - 1;
						if (quantity == 0) {
							s.removeObject(invBlock);
						}
						else {
							invBlock.setValue(HelperNameSpace.ATIBQUANT, quantity);
						}
						
						if (needToReplaceBlock) {
							s.removeObject(replaceBlock);
						} 
					}
				}
				break;
			case 3:
				if (HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX + 1][curZ]), HelperActions.placeOnBlocks)) {
					boolean anchorsPresent = false;
					boolean needToReplaceBlock = false;
					ObjectInstance replaceBlock = null;
					if ((map[curY - 2][curX + 1][curZ] > 0) && (map[curY - 1][curX + 2][curZ] > 0)) {
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
						int blockCountID = StateGenerator.blockCount + 1;
						ObjectInstance newBlock = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSBLOCK), "block" + blockCountID);
						newBlock.setValue(HelperNameSpace.ATX, curX + 1);
						newBlock.setValue(HelperNameSpace.ATY, curY - 1);
						newBlock.setValue(HelperNameSpace.ATZ, curZ);
						newBlock.setValue(HelperNameSpace.ATBTYPE, invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE));
						s.addObject(newBlock);
						int quantity = invBlock.getIntValForAttribute(HelperNameSpace.ATIBQUANT) - 1;
						if (quantity == 0) {
							s.removeObject(invBlock);
						}
						else {
							invBlock.setValue(HelperNameSpace.ATIBQUANT, quantity);
						}
						
						if (needToReplaceBlock) {
							s.removeObject(replaceBlock);
						} 
					}
				}
				break;
			default:
				break;
			}
			
		}
		
		return s;
		
	}
	
}

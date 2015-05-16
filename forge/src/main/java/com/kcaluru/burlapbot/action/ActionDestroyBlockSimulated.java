package com.kcaluru.burlapbot.action;

import java.util.ArrayList;
import java.util.List;

import com.kcaluru.burlapbot.helper.HelperNameSpace;
import com.kcaluru.burlapbot.helper.HelperPos;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TransitionProbability;

public class ActionDestroyBlockSimulated extends ActionAgentSimulated {

	public ActionDestroyBlockSimulated(String name, Domain domain) {
		super(name, domain);
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
		
		//get inventory block objects
		List<ObjectInstance> invBlocks = s.getObjectsOfTrueClass(HelperNameSpace.CLASSINVENTORYBLOCK);
		
		return destroyResult(curX, curY, curZ, rotDir, vertDir, invBlocks, blocks, s);
		
	}
	
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params) {
		
		return this.deterministicTransition(s, params);
		
	}
	
	protected State destroyResult(int curX, int curY, int curZ, int rotDir, int vertDir, List<ObjectInstance> invBlocks, List<ObjectInstance> blocks, State s) {
		
		// go through blocks and see if any of them can be mined
		for (ObjectInstance block : blocks) {
			int blockX = block.getIntValForAttribute(HelperNameSpace.ATX);
			int blockY = block.getIntValForAttribute(HelperNameSpace.ATY);
			int blockZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
			if (((blockX == curX) && (blockZ == curZ + 1) && (blockY == curY) && (rotDir == 0) && (vertDir == 1)) 
					|| ((blockX == curX) && (blockZ == curZ - 1) && (blockY == curY) && (rotDir == 2) && (vertDir == 1))
					|| ((blockX == curX + 1) && (blockZ == curZ) && (blockY == curY) && (rotDir == 3) && (vertDir == 1)) 
					|| ((blockX == curX - 1) && (blockZ == curZ) && (blockY == curY) && (rotDir == 1) && (vertDir == 1))) {
				int blockID = block.getIntValForAttribute(HelperNameSpace.ATBTYPE);
				boolean present = false;
				for (ObjectInstance invBlock : invBlocks) {
					if (blockID == invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE)) {
						int quantity = invBlock.getIntValForAttribute(HelperNameSpace.ATIBQUANT) + 1;
						invBlock.setValue(HelperNameSpace.ATIBQUANT, quantity);
						present = true;
					}
				}
				if (!present) {
					ObjectInstance inventoryBlockInstance = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSINVENTORYBLOCK), "inventoryBlock" + blockID);
					inventoryBlockInstance.setValue(HelperNameSpace.ATBTYPE, blockID);
					inventoryBlockInstance.setValue(HelperNameSpace.ATIBQUANT, 1);
					s.addObject(inventoryBlockInstance);
				}
				s.removeObject(block);
			}
		}
		
		return s;
		
	}

}

package com.kcaluru.burlapbot.actions;

import java.util.ArrayList;
import java.util.List;

import com.kcaluru.burlapbot.helpers.NameSpace;
import com.kcaluru.burlapbot.helpers.Pos;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;

public class DestroyBlockActionSimulated extends AgentActionSimulated {

	public DestroyBlockActionSimulated(String name, Domain domain) {
		super(name, domain);
	}

	@Override
	State doAction(State s) {
		
		//get agent and current position
		ObjectInstance agent = s.getFirstObjectOfClass(NameSpace.CLASSAGENT);
		int curX = agent.getDiscValForAttribute(NameSpace.ATX);
		int curY = agent.getDiscValForAttribute(NameSpace.ATY);
		int curZ = agent.getDiscValForAttribute(NameSpace.ATZ);
		int rotDir = agent.getDiscValForAttribute(NameSpace.ATROTDIR);
		int vertDir = agent.getDiscValForAttribute(NameSpace.ATVERTDIR);
		
		//get block objects and their positions
		List<ObjectInstance> blocks = s.getObjectsOfTrueClass(NameSpace.CLASSBLOCK);
		
		return destroyResult(curX, curY, curZ, rotDir, vertDir, blocks, s);
		
	}
	
	protected State destroyResult(int curX, int curY, int curZ, int rotDir, int vertDir, List<ObjectInstance> blocks, State s) {
		
		// go through blocks and see if any of them can be mined
		for (ObjectInstance block : blocks) {
			int blockX = block.getDiscValForAttribute(NameSpace.ATX);
			int blockY = block.getDiscValForAttribute(NameSpace.ATY);
			int blockZ = block.getDiscValForAttribute(NameSpace.ATZ);
			if (((blockX == curX) && (blockZ == curZ - 1) && (blockY == curY) && (rotDir == 0) && (vertDir == 1)) 
					|| ((blockX == curX) && (blockZ == curZ + 1) && (blockY == curY) && (rotDir == 2) && (vertDir == 1))
					|| ((blockX == curX - 1) && (blockZ == curZ) && (blockY == curY) && (rotDir == 3) && (vertDir == 1)) 
					|| ((blockX == curX + 1) && (blockZ == curZ) && (blockY == curY) && (rotDir == 1) && (vertDir == 1))) {
				s.removeObject(block);
			}
		}
		
		return s;
		
	}

}

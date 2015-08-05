package edu.brown.cs.h2r.burlapcraft.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TransitionProbability;

public class ActionChangeItemSimulated extends ActionAgentSimulated {

	public ActionChangeItemSimulated(String name, Domain domain) {
		super(name, domain);
	}

	@Override
	State doAction(State state) {
		ObjectInstance agent = state.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
		int currentEquippedItemID = agent.getIntValForAttribute(HelperNameSpace.ATSELECTEDITEMID);
		List<ObjectInstance> invBlocks = state.getObjectsOfClass(HelperNameSpace.CLASSINVENTORYBLOCK);
		List<Integer> blockIDs = new ArrayList<Integer>();
		for (ObjectInstance invBlock : invBlocks) {
			int blockID = invBlock.getIntValForAttribute(HelperNameSpace.ATBTYPE);
			if (blockID != currentEquippedItemID) {
				blockIDs.add(blockID);
			}
		}
		if (currentEquippedItemID != 278) {
			blockIDs.add(278);
		}
		
		Random rand = new Random(); 
		if (blockIDs.size() > 0) {
			agent.setValue(HelperNameSpace.ATSELECTEDITEMID, blockIDs.get(rand.nextInt(blockIDs.size())));
		}
		
		return state;
	}
	
	@Override
	public boolean applicableInState(State s, String[] params) {
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
	
	@Override
	public List<TransitionProbability> getTransitions(State s, String [] params){
		
		return this.deterministicTransition(s, params);
		
	}

}

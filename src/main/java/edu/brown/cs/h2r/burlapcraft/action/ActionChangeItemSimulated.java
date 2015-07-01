package edu.brown.cs.h2r.burlapcraft.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	public List<TransitionProbability> getTransitions(State s, String [] params){
		
		return this.deterministicTransition(s, params);
		
	}

}

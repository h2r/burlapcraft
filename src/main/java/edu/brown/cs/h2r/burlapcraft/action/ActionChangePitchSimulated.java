package edu.brown.cs.h2r.burlapcraft.action;

import java.util.List;

import net.minecraft.block.Block;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.core.TransitionProbability;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.common.SimpleAction.SimpleDeterministicAction;

public class ActionChangePitchSimulated extends SimpleDeterministicAction {

	private int vertDirection;
	
	public ActionChangePitchSimulated(String name, Domain domain, int rotateVertDirection) {
		super(name, domain);
		this.vertDirection = rotateVertDirection;
	}
	
	@Override
	protected State performActionHelper(State s, GroundedAction groundedAction) {
		//get agent
		ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
		
		//set agent's vert dir
		agent.setValue(HelperNameSpace.ATVERTDIR, this.vertDirection);
		
		//return the state we just modified
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

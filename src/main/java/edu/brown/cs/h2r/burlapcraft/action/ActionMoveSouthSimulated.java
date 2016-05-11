package edu.brown.cs.h2r.burlapcraft.action;

import java.util.ArrayList;
import java.util.List;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

public class ActionMoveSouthSimulated extends ActionMoveForwardSimulated {
	private int[][][] map;
	private int rotDir = 0;

	public ActionMoveSouthSimulated(String name, Domain domain, int[][][] map) {
		super(name, domain, map);
		this.map = map;
	}

	@Override
	protected State performActionHelper(State s, GroundedAction groundedAction) {
		
		StateGenerator.validate(s);
		//get agent and current position
		ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
		int curX = agent.getIntValForAttribute(HelperNameSpace.ATX);
		int curY = agent.getIntValForAttribute(HelperNameSpace.ATY);
		int curZ = agent.getIntValForAttribute(HelperNameSpace.ATZ);

		//get objects and their positions
		List<ObjectInstance> blocks = s.getObjectsOfClass(HelperNameSpace.CLASSBLOCK);
		List<HelperPos> coords = new ArrayList<HelperPos>();
		for (ObjectInstance block : blocks) {
			int blockX = block.getIntValForAttribute(HelperNameSpace.ATX);
			int blockY = block.getIntValForAttribute(HelperNameSpace.ATY);
			int blockZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
			coords.add(new HelperPos(blockX, blockY, blockZ));
		}
		
		//get resulting position
		HelperPos newPos = this.moveResult(curX, curY, curZ, rotDir, coords);
		
		//set the new position
		agent.setValue(HelperNameSpace.ATX, newPos.x);
		agent.setValue(HelperNameSpace.ATY, newPos.y);
		agent.setValue(HelperNameSpace.ATZ, newPos.z);
		StateGenerator.validate(s);
		//return the state we just modified
		return s;
		
	}
}

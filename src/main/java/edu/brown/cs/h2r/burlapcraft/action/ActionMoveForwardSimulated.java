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
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCAgent;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCBlock;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCMap;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.CLASS_AGENT;
import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.CLASS_MAP;

public class ActionMoveForwardSimulated extends SimpleAction.SimpleDeterministicAction {


	public ActionMoveForwardSimulated(String name, Domain domain) {
		super(name, domain);
	}

	@Override
	protected State sampleHelper(State s, GroundedAction groundedAction) {

		GenericOOState gs = (GenericOOState)s;

		StateGenerator.validate(s);
		//get agent and current position
		BCAgent agent = (BCAgent)gs.touch(CLASS_AGENT);


		//get objects and their positions
		List<ObjectInstance> blocks = gs.objectsOfClass(HelperNameSpace.CLASS_BLOCK);
		List<HelperPos> coords = new ArrayList<HelperPos>();
		for (ObjectInstance block : blocks) {
			int blockX = ((BCBlock)block).x;
			int blockY = ((BCBlock)block).y;
			int blockZ = ((BCBlock)block).z;
			coords.add(new HelperPos(blockX, blockY, blockZ));
		}
		
		//get resulting position
		int [][][] map = ((BCMap)gs.object(CLASS_MAP)).map;
		HelperPos newPos = this.moveResult(agent.x, agent.y, agent.z, agent.rdir, coords, map);
		
		//set the new position
		agent.x = newPos.x;
		agent.y = newPos.y;
		agent.z = newPos.z;

		StateGenerator.validate(s);
		//return the state we just modified
		return s;
		
	}
	
	protected HelperPos moveResult(int curX, int curY, int curZ, int direction, List<HelperPos> coords, int [][][] map) {
		
		//first get change in x and z from direction using 0: south; 1: west; 2: north; 3: east
		int xdelta = 0;
		int zdelta = 0;
		if(direction == 0){
			zdelta = 1;
		}
		else if(direction == 1){
			xdelta = -1;
		}
		else if(direction == 2){
			zdelta = -1;
		}
		else{
			xdelta = 1;
		}
		
		int nx = curX + xdelta;
		int nz = curZ + zdelta;
		
		int length = map[curY].length;
		int width = map[curY][curX].length;
		
		//make sure new position is valid (not a wall or off bounds)
		if(nx < 0 || nx >= length || nz < 0 || nz >= width ||  
			map[curY][nx][nz] >= 1 || map[curY + 1][nx][nz] >= 1){
			nx = curX;
			nz = curZ;
		}
		
		for(HelperPos coord : coords) {
			if (nx == coord.x && (curY == coord.y || curY + 1 == coord.y) && nz == coord.z) {
				nx = curX;
				nz = curZ;
			}
		}
		
		return new HelperPos(nx, curY, nz);
		
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

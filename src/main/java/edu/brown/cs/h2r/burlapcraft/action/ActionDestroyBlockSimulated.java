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
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCInventory;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.*;

public class ActionDestroyBlockSimulated extends SimpleAction.SimpleDeterministicAction {

	public ActionDestroyBlockSimulated(String name, Domain domain) {
		super(name, domain);
	}

	@Override
	protected State sampleHelper(State s, GroundedAction groundedAction) {
		//get agent and current position
		GenericOOState gs = (GenericOOState)s;

		//get agent and current position
		BCAgent agent = (BCAgent)gs.object(CLASS_AGENT);
		BCInventory inv = (BCInventory)gs.object(CLASS_INVENTORY);
		if(agent.selected < 0 || agent.selected > 8 || agent.vdir != 1){
			return s;
		}

		int itemId = inv.inv[agent.selected].type;
		if(itemId != 278){
			return s;
		}

		List<ObjectInstance> oblocks = gs.objectsOfClass(CLASS_BLOCK);

		List<BCBlock> blocks = new ArrayList<BCBlock>(oblocks.size());
		for(ObjectInstance ob : oblocks){
			blocks.add((BCBlock)ob);
		}

		return this.destroyResult(agent.x, agent.y, agent.z, agent.rdir, blocks, gs);

	}
	
	protected State destroyResult(int curX, int curY, int curZ, int rotDir, List<BCBlock> blocks, GenericOOState s) {
		//System.out.println("Destroying at: " + curX + "," + curY + "," + curZ);
		// go through blocks and see if any of them can be mined
		for (BCBlock block : blocks) {

			if (((block.x == curX) && (block.z == curZ + 1) && (block.y == curY) && (rotDir == 0))
					|| ((block.x == curX) && (block.z == curZ - 1) && (block.y == curY) && (rotDir == 2))
					|| ((block.x == curX + 1) && (block.z == curZ) && (block.y == curY) && (rotDir == 3))
					|| ((block.x == curX - 1) && (block.z == curZ) && (block.y == curY) && (rotDir == 1))) {
				// get id of the block
				int blockID = block.type;

				boolean present = false;

				BCInventory inv = (BCInventory)s.touch(CLASS_INVENTORY);
				int invInd = inv.indexOfType(blockID);
				if(invInd == -1){
					invInd = inv.firstFree();
				}
				if(invInd == -1){
					return s; //no free slots!
				}
				BCInventory.BCIStack [] stack = inv.touch();
				stack[invInd].type = blockID;
				stack[invInd].inc();


				s.removeObject(block.name());
				break;
			}
		}
		
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

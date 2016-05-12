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
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCMap;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

import static edu.brown.cs.h2r.burlapcraft.helper.HelperActions.mineableBlocks;
import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.*;


public class ActionPlaceBlockSimulated extends SimpleAction.SimpleDeterministicAction {


	public ActionPlaceBlockSimulated(String name, Domain domain) {
		super(name, domain);
	}
	
	@Override
	protected State sampleHelper(State s, GroundedAction groundedAction) {

		GenericOOState gs = (GenericOOState)s;

		BCAgent agent = (BCAgent)gs.object(CLASS_AGENT);

		if(agent.vdir != 1 || agent.selected < 0 || agent.selected > 8){
			return s;
		}

		BCInventory inv = (BCInventory)gs.object(CLASS_INVENTORY);
		if(inv.inv[agent.selected].type == -1 || !HelperActions.blockIsOneOf(Block.getBlockById(inv.inv[agent.selected].type), mineableBlocks)){
			return s;
		}

		List<ObjectInstance> oblocks = gs.objectsOfClass(CLASS_BLOCK);

		List<BCBlock> blocks = new ArrayList<BCBlock>(oblocks.size());
		for(ObjectInstance ob : oblocks){
			blocks.add((BCBlock)ob);
		}

		int [][][] map = ((BCMap)gs.object(CLASS_MAP)).map;

		return placeResult(agent.x, agent.y, agent.z, agent.rdir, blocks, agent.selected, map, gs);

	}
	
	protected State placeResult(int curX, int curY, int curZ, int rotDir, List<BCBlock> blocks, int sel, int [][][] map, GenericOOState s) {

		int xd = 0;
		int zd = 0;
		switch(rotDir){

			case 0:
				zd = 1;
				break;

			case 1:
				xd = -1;
				break;

			case 2:
				zd = -1;
				break;

			case 3:
				xd = 1;
				break;

			default:
				break;
		}

		if(HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX + xd][curZ + zd]), HelperActions.placeInBlocks)){
			AnchorsAndReplace ar = this.anchorsPresent(curX, curY, curZ, blocks, xd, zd, map);
			if(ar.anchorsPresent){
				BCInventory inv = (BCInventory)s.touch(CLASS_INVENTORY);
				BCInventory.BCIStack[] stack = inv.touch();
				int oldType = stack[sel].type;
				stack[sel].dec();
				BCBlock nblock = new BCBlock(curX + xd, curY - 1, curZ + zd, oldType, freeBlockName(blocks));
				s.addObject(nblock);

				if(ar.toReplace != null){
					s.removeObject(ar.toReplace.name());
				}

			}
		}
		else if(HelperActions.blockIsOneOf(Block.getBlockById(map[curY - 1][curX + xd][curZ + zd]), HelperActions.unbreakableBlocks)){

			BCInventory inv = (BCInventory)s.touch(CLASS_INVENTORY);
			BCInventory.BCIStack[] stack = inv.touch();
			int oldType = stack[sel].type;
			stack[sel].dec();
			BCBlock nblock = new BCBlock(curX + xd, curY - 1, curZ + zd, oldType, freeBlockName(blocks));
			s.addObject(nblock);

		}

		
		return s;
		
	}

	protected String freeBlockName(List<BCBlock> blocks){
		boolean existing;
		int maxVal = blocks.size()-1;
		do{
			existing = false;
			maxVal++;
			String name = "block" + maxVal;
			for(BCBlock b : blocks){
				if(b.name().equals(name)){
					existing = true;
				}
			}

		}while(existing);

		return "block" + maxVal;
	}

	protected AnchorsAndReplace anchorsPresent(int curX, int curY, int curZ, List<BCBlock> blocks, int xd, int zd, int [][][] map){



		boolean anchorsPresent = false;
		BCBlock replace = null;
		if (((curY - 2 >= 0) && map[curY - 2][curX + xd][curZ + zd] > 0) && (map[curY - 1][curX + 2*xd][curZ + 2*zd] > 0)) {
			anchorsPresent = true;
		}


		for(BCBlock block : blocks){
			if (((block.x == curX + 2*xd) && (block.y == curY - 1) && (block.z == curZ + 2*zd))
					|| ((block.x == curX + xd) && (block.y == curY - 2) && (block.z == curZ + zd))) {
				anchorsPresent = true;
			}

			if ((block.x == curX + xd) && (block.y == curY - 1) && (block.z == curZ + zd)) {
				replace = block;
			}

		}

		return new AnchorsAndReplace(anchorsPresent, replace);

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

	protected static class AnchorsAndReplace{
		public boolean anchorsPresent;
		public BCBlock toReplace = null;

		public AnchorsAndReplace(boolean anchorsPresent) {
			this.anchorsPresent = anchorsPresent;
		}

		public AnchorsAndReplace(boolean anchorsPresent, BCBlock toReplace) {
			this.anchorsPresent = anchorsPresent;
			this.toReplace = toReplace;
		}
	}
	
}

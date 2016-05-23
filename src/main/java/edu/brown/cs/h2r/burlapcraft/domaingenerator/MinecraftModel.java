package edu.brown.cs.h2r.burlapcraft.domaingenerator;

import burlap.mdp.core.Action;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.oo.state.generic.GenericOOState;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCAgent;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCBlock;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCInventory;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCMap;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

import static edu.brown.cs.h2r.burlapcraft.helper.HelperActions.mineableBlocks;
import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.*;

/**
 * @author James MacGlashan.
 */
public class MinecraftModel implements FullStateModel {

	@Override
	public List<StateTransitionProb> stateTransitions(State s, Action a) {
		return FullStateModel.Helper.deterministicTransition(this, s, a);
	}

	@Override
	public State sample(State s, Action a) {

		GenericOOState gs = (GenericOOState)s.copy();

		String aname = a.actionName();
		if(aname.equals(HelperNameSpace.ACTION_MOVE)){
			simMove(gs);
		}
		else if(aname.equals(HelperNameSpace.ACTION_ROTATE_LEFT)){
			simRotate(gs, HelperNameSpace.RotDirection.size - 1);
		}
		else if(aname.equals(HelperNameSpace.ACTION_ROTATE_RIGHT)){
			simRotate(gs, 1);
		}
		else if(aname.equals(HelperNameSpace.ACTION_AHEAD)){
			simPitch(gs, 0);
		}
		else if(aname.equals(HelperNameSpace.ACTION_DOWN_ONE)){
			simPitch(gs, HelperNameSpace.VertDirection.size - 1);
		}
		else if(aname.equals(HelperNameSpace.ACTION_PLACE_BLOCK)){
			simPlace(gs);
		}
		else if(aname.equals(HelperNameSpace.ACTION_DEST_BLOCK)){
			simDestroy(gs);
		}
		else if(aname.equals(HelperNameSpace.ACTION_CHANGE_ITEM)){
			simChangeItem(gs);
		}
		else{
			throw new RuntimeException("MinecraftModel is not defined for action " + aname);
		}

		return gs;
	}


	protected void simMove(GenericOOState gs){

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

	}



	protected void simRotate(GenericOOState gs, int direction){
		//get agent and current position
		BCAgent agent = (BCAgent)gs.touch(CLASS_AGENT);

		int rotDir = (direction + agent.rdir) % 4;

		//set the new rotation direction
		agent.rdir = rotDir;
	}

	protected void simPitch(GenericOOState gs, int vertDirection){

		//get agent and current position
		BCAgent agent = (BCAgent)gs.touch(CLASS_AGENT);

		//set agent's vert dir
		agent.vdir = vertDirection;


	}

	protected void simPlace(GenericOOState gs){

		BCAgent agent = (BCAgent)gs.object(CLASS_AGENT);

		if(agent.vdir != 1 || agent.selected < 0 || agent.selected > 8){
			return;
		}

		BCInventory inv = (BCInventory)gs.object(CLASS_INVENTORY);
		if(inv.inv[agent.selected].type == -1 || !HelperActions.blockIsOneOf(Block.getBlockById(inv.inv[agent.selected].type), mineableBlocks)){
			return;
		}

		List<ObjectInstance> oblocks = gs.objectsOfClass(CLASS_BLOCK);

		List<BCBlock> blocks = new ArrayList<BCBlock>(oblocks.size());
		for(ObjectInstance ob : oblocks){
			blocks.add((BCBlock)ob);
		}

		int [][][] map = ((BCMap)gs.object(CLASS_MAP)).map;

		placeResult(agent.x, agent.y, agent.z, agent.rdir, blocks, agent.selected, map, gs);

	}


	protected void simDestroy(GenericOOState gs){

		//get agent and current position
		BCAgent agent = (BCAgent)gs.object(CLASS_AGENT);
		BCInventory inv = (BCInventory)gs.object(CLASS_INVENTORY);
		if(agent.selected < 0 || agent.selected > 8 || agent.vdir != 1){
			return;
		}

		int itemId = inv.inv[agent.selected].type;
		if(itemId != 278){
			return;
		}

		List<ObjectInstance> oblocks = gs.objectsOfClass(CLASS_BLOCK);

		List<BCBlock> blocks = new ArrayList<BCBlock>(oblocks.size());
		for(ObjectInstance ob : oblocks){
			blocks.add((BCBlock)ob);
		}

		this.destroyResult(agent.x, agent.y, agent.z, agent.rdir, blocks, gs);


	}

	protected void simChangeItem(GenericOOState gs){

		//get agent
		BCAgent agent = (BCAgent)gs.touch(CLASS_AGENT);
		int curItem = agent.selected;
		int nextItem = curItem >= 0 && curItem < 9 ? (curItem+1) % 9 : 0;
		agent.selected = nextItem;

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

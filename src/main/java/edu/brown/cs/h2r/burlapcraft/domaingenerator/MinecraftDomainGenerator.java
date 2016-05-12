package edu.brown.cs.h2r.burlapcraft.domaingenerator;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.singleagent.oo.OOSADomain;
import edu.brown.cs.h2r.burlapcraft.action.*;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction.PFAgentHasBlock;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction.PFAgentOnBlock;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction.PFBlockIsType;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCAgent;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCBlock;
import edu.brown.cs.h2r.burlapcraft.stategenerator.BCMap;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.*;

/**
 * Class to generate burlap domain for minecraft
 * @author Krishna Aluru
 *
 */

public class MinecraftDomainGenerator implements DomainGenerator {
		
	private int[][][] map;
	private int length;
	private int width;
	private int height;

	protected Set<String> whiteListActions = new HashSet<String>();
	
	protected ArrayList<String> colors = new ArrayList<String>() {{
	    add("red");
	    add("green");
	    add("blue");
	    add("orange");
	}};
	
	public MinecraftDomainGenerator(int[][][] map) {
		this.map = map;
		this.length = map[0].length;
		this.width = map[0][0].length;
		this.height = map.length;
	}
	
	public MinecraftDomainGenerator(int x, int y, int z) {
		this.length = x;
		this.width = z;
		this.height = y;
	}

	public void setActionWhiteListToNavigationOnly(){
		this.whiteListActions = new HashSet<String>();
		this.whiteListActions.add(HelperNameSpace.ACTION_MOVE);
		this.whiteListActions.add(HelperNameSpace.ACTION_ROTATE_LEFT);
		this.whiteListActions.add(HelperNameSpace.ACTION_ROTATE_RIGHT);
	}
	
	public void setActionWhiteListToNavigationAndDestroy(){
		this.whiteListActions = new HashSet<String>();
		this.whiteListActions.add(HelperNameSpace.ACTION_MOVE);
		this.whiteListActions.add(HelperNameSpace.ACTION_ROTATE_LEFT);
		this.whiteListActions.add(HelperNameSpace.ACTION_ROTATE_RIGHT);
		this.whiteListActions.add(HelperNameSpace.ACTION_DOWN_ONE);
		this.whiteListActions.add(HelperNameSpace.ACTION_DEST_BLOCK);
		this.whiteListActions.add(HelperNameSpace.ACTION_CHANGE_ITEM);
	}

	public Set<String> getWhiteListActions() {
		return whiteListActions;
	}

	public void setWhiteListActions(Set<String> whiteListActions) {
		this.whiteListActions = whiteListActions;
	}

	public void addActionToWhiteList(String actionName){
		this.whiteListActions.add(actionName);
	}

	@Override
	public OOSADomain generateDomain() {
		
		OOSADomain domain = new OOSADomain();

		domain.addStateClass(CLASS_AGENT, BCAgent.class)
				.addStateClass(CLASS_BLOCK, BCBlock.class)
				.addStateClass(CLASS_MAP, BCMap.class);

		
		// Actions
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_MOVE)) {
			new ActionMoveForwardSimulated(HelperNameSpace.ACTION_MOVE, domain);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_ROTATE_RIGHT)) {
			new ActionChangeYawSimulated(HelperNameSpace.ACTION_ROTATE_RIGHT, domain, 1);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_ROTATE_LEFT)) {
			new ActionChangeYawSimulated(HelperNameSpace.ACTION_ROTATE_LEFT, domain, HelperNameSpace.RotDirection.size - 1);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_AHEAD)) {
			new ActionChangePitchSimulated(HelperNameSpace.ACTION_AHEAD, domain, 0);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_DOWN_ONE)) {
			new ActionChangePitchSimulated(HelperNameSpace.ACTION_DOWN_ONE, domain, HelperNameSpace.VertDirection.size - 1);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_PLACE_BLOCK)) {
			new ActionPlaceBlockSimulated(HelperNameSpace.ACTION_PLACE_BLOCK, domain);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_DEST_BLOCK)) {
			new ActionDestroyBlockSimulated(HelperNameSpace.ACTION_DEST_BLOCK, domain);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTION_CHANGE_ITEM)) {
			new ActionChangeItemSimulated(HelperNameSpace.ACTION_CHANGE_ITEM, domain);
		}

		// Propositional Functions
		new PFAgentHasBlock(HelperNameSpace.PF_AGENT_HAS_BLOCK, domain, new String[] {HelperNameSpace.CLASS_INVENTORY, HelperNameSpace.CLASS_BLOCK});
		new PFAgentOnBlock(HelperNameSpace.PF_AGENT_ON_BLOCK, domain, new String[] {CLASS_AGENT, HelperNameSpace.CLASS_BLOCK});

		for(Block b : HelperActions.mineableBlocks) {
			int id = Block.getIdFromBlock(b);
			new PFBlockIsType(HelperNameSpace.PF_BLOCK_IS_TYPE +id, domain, new String[]{HelperNameSpace.CLASS_BLOCK}, id);
		}

		return domain;
		
	}
	
}
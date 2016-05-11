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
		this.whiteListActions.add(HelperNameSpace.ACTIONMOVE);
		this.whiteListActions.add(HelperNameSpace.ACTIONROTATELEFT);
		this.whiteListActions.add(HelperNameSpace.ACTIONROTATERIGHT);
	}
	
	public void setActionWhiteListToNavigationAndDestroy(){
		this.whiteListActions = new HashSet<String>();
		this.whiteListActions.add(HelperNameSpace.ACTIONMOVE);
		this.whiteListActions.add(HelperNameSpace.ACTIONROTATELEFT);
		this.whiteListActions.add(HelperNameSpace.ACTIONROTATERIGHT);
		this.whiteListActions.add(HelperNameSpace.ACTIONDOWNONE);
		this.whiteListActions.add(HelperNameSpace.ACTIONDESTBLOCK);
		this.whiteListActions.add(HelperNameSpace.ACTIONCHANGEITEM);
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
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONMOVE)) {
			new ActionMoveForwardSimulated(HelperNameSpace.ACTIONMOVE, domain);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONROTATERIGHT)) {
			new ActionChangeYawSimulated(HelperNameSpace.ACTIONROTATERIGHT, domain, 1);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONROTATELEFT)) {
			new ActionChangeYawSimulated(HelperNameSpace.ACTIONROTATELEFT, domain, HelperNameSpace.RotDirection.size - 1);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONAHEAD)) {
			new ActionChangePitchSimulated(HelperNameSpace.ACTIONAHEAD, domain, 0);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONDOWNONE)) {
			new ActionChangePitchSimulated(HelperNameSpace.ACTIONDOWNONE, domain, HelperNameSpace.VertDirection.size - 1);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONPLACEBLOCK)) {
			new ActionPlaceBlockSimulated(HelperNameSpace.ACTIONPLACEBLOCK, domain, this.map);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONDESTBLOCK)) {
			new ActionDestroyBlockSimulated(HelperNameSpace.ACTIONDESTBLOCK, domain);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONCHANGEITEM)) {
			new ActionChangeItemSimulated(HelperNameSpace.ACTIONCHANGEITEM, domain);
		}

		// Propositional Functions
		new PFAgentHasBlock(HelperNameSpace.PFAGENTHASBLOCK, domain, new String[] {HelperNameSpace.CLASS_INVENTORY_BLOCK, HelperNameSpace.CLASS_BLOCK});
		new PFAgentOnBlock(HelperNameSpace.PFAGENTONBLOCK, domain, new String[] {CLASS_AGENT, HelperNameSpace.CLASS_BLOCK});

		for(Block b : HelperActions.mineableBlocks) {
			int id = Block.getIdFromBlock(b);
			new PFBlockIsType(HelperNameSpace.PFBLOCKISTYPE+id, domain, new String[]{HelperNameSpace.CLASS_BLOCK}, id);
		}

		return domain;
		
	}
	
}
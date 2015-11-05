package edu.brown.cs.h2r.burlapcraft.domaingenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction.*;
import net.minecraft.block.Block;
import net.minecraft.util.RegistryNamespaced;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Attribute.AttributeType;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.TransitionProbability;
import burlap.oomdp.singleagent.Action;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.singleagent.explorer.TerminalExplorer;
import cpw.mods.fml.common.registry.GameData;
import edu.brown.cs.h2r.burlapcraft.action.ActionChangeItemSimulated;
import edu.brown.cs.h2r.burlapcraft.action.ActionChangePitchSimulated;
import edu.brown.cs.h2r.burlapcraft.action.ActionChangeYawSimulated;
import edu.brown.cs.h2r.burlapcraft.action.ActionDestroyBlockSimulated;
import edu.brown.cs.h2r.burlapcraft.action.ActionMoveForwardSimulated;
import edu.brown.cs.h2r.burlapcraft.action.ActionPlaceBlockSimulated;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

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
	public Domain generateDomain() {
		
		Domain domain = new SADomain();
		
		// Attributes
		// x-position attribute
		Attribute xAtt = new Attribute(domain, HelperNameSpace.ATX, AttributeType.INT);
		xAtt.setLims(0, this.length - 1);
		// y-position attribute
		Attribute yAtt = new Attribute(domain, HelperNameSpace.ATY, AttributeType.INT);
		yAtt.setLims(0, this.height - 1);
		// z-position attribute
		Attribute zAtt = new Attribute(domain, HelperNameSpace.ATZ, AttributeType.INT);
		zAtt.setLims(0, this.width - 1);
		// Rotational direction for agent
		Attribute rotDirAt = new Attribute(domain, HelperNameSpace.ATROTDIR, Attribute.AttributeType.DISC);
		rotDirAt.setDiscValuesForRange(0, HelperNameSpace.RotDirection.size - 1, 1);
		// Agent's vertical direction attribute
		Attribute vertDirAt = new Attribute(domain, HelperNameSpace.ATVERTDIR, Attribute.AttributeType.DISC);
		vertDirAt.setDiscValuesForRange(0, HelperNameSpace.VertDirection.size - 1, 1);
		// Block type
		Attribute bType = new Attribute(domain, HelperNameSpace.ATBTYPE, Attribute.AttributeType.INT);
		// Inventory Block quantity
		Attribute ibQuantity = new Attribute(domain, HelperNameSpace.ATIBQUANT, Attribute.AttributeType.INT);
		// Room xMin bound
		Attribute xMin = new Attribute(domain, HelperNameSpace.ATXMIN, Attribute.AttributeType.INT);
		// Room xMax bound
		Attribute xMax = new Attribute(domain, HelperNameSpace.ATXMAX, Attribute.AttributeType.INT);
		// Room yMin bound
		Attribute zMin = new Attribute(domain, HelperNameSpace.ATZMIN, Attribute.AttributeType.INT);
		// Room yMax bound
		Attribute zMax = new Attribute(domain, HelperNameSpace.ATZMAX, Attribute.AttributeType.INT);
		// Room color
		Attribute color = new Attribute(domain, HelperNameSpace.ATCOLOR, Attribute.AttributeType.DISC);
		color.setDiscValues(this.colors);
		// Current Item ID
		Attribute selectedItemID = new Attribute(domain, HelperNameSpace.ATSELECTEDITEMID, Attribute.AttributeType.INT);
		// names of blocks
		Attribute blockNames = new Attribute(domain, HelperNameSpace.ATBLOCKNAMES, Attribute.AttributeType.MULTITARGETRELATIONAL);
		
		
		// Object classes
		// agent
		ObjectClass agentClass = new ObjectClass(domain, HelperNameSpace.CLASSAGENT);
		agentClass.addAttribute(xAtt);
		agentClass.addAttribute(yAtt);
		agentClass.addAttribute(zAtt);
		agentClass.addAttribute(rotDirAt);
		agentClass.addAttribute(vertDirAt);
		agentClass.addAttribute(selectedItemID);
		// blocks
		ObjectClass blockClass = new ObjectClass(domain, HelperNameSpace.CLASSBLOCK);
		blockClass.addAttribute(xAtt);
		blockClass.addAttribute(yAtt);
		blockClass.addAttribute(zAtt);
		blockClass.addAttribute(bType);
		// inventory blocks
		ObjectClass inventoryBlockClass = new ObjectClass(domain, HelperNameSpace.CLASSINVENTORYBLOCK);
		inventoryBlockClass.addAttribute(bType);
		inventoryBlockClass.addAttribute(blockNames);
		// rooms
		ObjectClass roomClass = new ObjectClass(domain, HelperNameSpace.CLASSROOM);
		roomClass.addAttribute(xMax);
		roomClass.addAttribute(xMin);
		roomClass.addAttribute(zMax);
		roomClass.addAttribute(zMin);
		roomClass.addAttribute(color);
		
		// Actions
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONMOVE)) {
			new ActionMoveForwardSimulated(HelperNameSpace.ACTIONMOVE, domain, this.map);
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
		new PFAgentInRoom(HelperNameSpace.PFAGENTINROOM, domain, new String[]{HelperNameSpace.CLASSAGENT, HelperNameSpace.CLASSROOM});
		new PFRoomIsRed(HelperNameSpace.PFROOMRED, domain, HelperNameSpace.CLASSROOM);
		new PFRoomIsBlue(HelperNameSpace.PFROOMBLUE, domain, HelperNameSpace.CLASSROOM);
		new PFRoomIsGreen(HelperNameSpace.PFROOMGREEN, domain, HelperNameSpace.CLASSROOM);
		new PFRoomIsOrange(HelperNameSpace.PFROOMORANGE, domain, HelperNameSpace.CLASSROOM);
		new PFBlockInRoom(HelperNameSpace.PFBLOCKINROOM, domain, new String[]{HelperNameSpace.CLASSBLOCK, HelperNameSpace.CLASSROOM});
		new PFAgentHasBlock(HelperNameSpace.PFAGENTHASBLOCK, domain, new String[] {HelperNameSpace.CLASSINVENTORYBLOCK, HelperNameSpace.CLASSBLOCK});
		new PFAgentOnBlock(HelperNameSpace.PFAGENTONBLOCK, domain, new String[] {HelperNameSpace.CLASSAGENT, HelperNameSpace.CLASSBLOCK});

		for(Block b : HelperActions.mineableBlocks) {
			int id = Block.getIdFromBlock(b);
			new PFBlockIsType(HelperNameSpace.PFBLOCKISTYPE+id, domain, new String[]{HelperNameSpace.CLASSBLOCK}, id);
		}

		return domain;
		
	}
	
}
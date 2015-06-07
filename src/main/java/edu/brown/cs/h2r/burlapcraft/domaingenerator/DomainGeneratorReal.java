package edu.brown.cs.h2r.burlapcraft.domaingenerator;

import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Attribute.AttributeType;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.singleagent.SADomain;
import edu.brown.cs.h2r.burlapcraft.action.ActionChangePitchReal;
import edu.brown.cs.h2r.burlapcraft.action.ActionChangeYawReal;
import edu.brown.cs.h2r.burlapcraft.action.ActionDestroyBlockReal;
import edu.brown.cs.h2r.burlapcraft.action.ActionMoveForwardReal;
import edu.brown.cs.h2r.burlapcraft.action.ActionPlaceBlockReal;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;

import java.util.HashSet;
import java.util.Set;

/**
 * Class to generate burlap domain for minecraft
 * @author Krishna Aluru
 *
 */

public class DomainGeneratorReal implements DomainGenerator {
		
	protected int length;
	protected int width;
	protected int height;

	protected int sleepMS = 1000;

	protected Set<String> whiteListActions = new HashSet<String>();
	
	public DomainGeneratorReal(int length, int width, int height) {
		
		this.length = length;
		this.width = width;
		this.height = height;
		
	}

	public int getSleepMS() {
		return sleepMS;
	}

	public void setSleepMS(int sleepMS) {
		this.sleepMS = sleepMS;
	}

	public void setActionWhiteListToNavigationOnly(){
		this.whiteListActions = new HashSet<String>();
		this.whiteListActions.add(HelperNameSpace.ACTIONMOVE);
		this.whiteListActions.add(HelperNameSpace.ACTIONROTATELEFT);
		this.whiteListActions.add(HelperNameSpace.ACTIONROTATERIGHT);
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
		
		// Object classes
		// agent
		ObjectClass agentClass = new ObjectClass(domain, HelperNameSpace.CLASSAGENT);
		agentClass.addAttribute(xAtt);
		agentClass.addAttribute(yAtt);
		agentClass.addAttribute(zAtt);
		agentClass.addAttribute(rotDirAt);
		agentClass.addAttribute(vertDirAt);
		// blocks
		ObjectClass blockClass = new ObjectClass(domain, HelperNameSpace.CLASSBLOCK);
		blockClass.addAttribute(xAtt);
		blockClass.addAttribute(yAtt);
		blockClass.addAttribute(zAtt);
		blockClass.addAttribute(bType);

		// inventory blocks
		ObjectClass inventoryBlockClass = new ObjectClass(domain, HelperNameSpace.CLASSINVENTORYBLOCK);
		inventoryBlockClass.addAttribute(bType);
		inventoryBlockClass.addAttribute(ibQuantity);
		
		
		// Actions
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONMOVE)) {
			new ActionMoveForwardReal(HelperNameSpace.ACTIONMOVE, domain, this.sleepMS);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONROTATERIGHT)) {
			new ActionChangeYawReal(HelperNameSpace.ACTIONROTATERIGHT, domain, this.sleepMS, 1);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONROTATELEFT)) {
			new ActionChangeYawReal(HelperNameSpace.ACTIONROTATELEFT, domain, HelperNameSpace.RotDirection.size - 1);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONAHEAD)) {
			new ActionChangePitchReal(HelperNameSpace.ACTIONAHEAD, domain, this.sleepMS, 0);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONDOWNONE)) {
			new ActionChangePitchReal(HelperNameSpace.ACTIONDOWNONE, domain, this.sleepMS, HelperNameSpace.VertDirection.size - 1);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONPLACEBLOCK)) {
			new ActionPlaceBlockReal(HelperNameSpace.ACTIONPLACEBLOCK, domain, this.sleepMS);
		}
		if(this.whiteListActions.size() == 0 || this.whiteListActions.contains(HelperNameSpace.ACTIONDESTBLOCK)) {
			new ActionDestroyBlockReal(HelperNameSpace.ACTIONDESTBLOCK, domain, this.sleepMS);
		}
		
		return domain;
		
	}
	
}
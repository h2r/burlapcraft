package com.kcaluru.burlapbot.domaingenerator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.RegistryNamespaced;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Attribute.AttributeType;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.State;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.TransitionProbability;
import burlap.oomdp.singleagent.Action;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.singleagent.explorer.TerminalExplorer;

import com.kcaluru.burlapbot.action.ActionChangePitchReal;
import com.kcaluru.burlapbot.action.ActionChangePitchSimulated;
import com.kcaluru.burlapbot.action.ActionChangeYawReal;
import com.kcaluru.burlapbot.action.ActionChangeYawSimulated;
import com.kcaluru.burlapbot.action.ActionDestroyBlockReal;
import com.kcaluru.burlapbot.action.ActionDestroyBlockSimulated;
import com.kcaluru.burlapbot.action.ActionMoveForwardReal;
import com.kcaluru.burlapbot.action.ActionMoveForwardSimulated;
import com.kcaluru.burlapbot.action.ActionPlaceBlockReal;
import com.kcaluru.burlapbot.action.ActionPlaceBlockSimulated;
import com.kcaluru.burlapbot.helper.HelperActions;
import com.kcaluru.burlapbot.helper.HelperNameSpace;
import com.kcaluru.burlapbot.stategenerator.StateGenerator;

import cpw.mods.fml.common.registry.GameData;

/**
 * Class to generate burlap domain for minecraft
 * @author Krishna Aluru
 *
 */

public class DomainGeneratorSimulated implements DomainGenerator {
		
	private int[][][] map;
	private int length;
	private int width;
	private int height;
	private int numVertDirs = 2;
	public static int dungeonID;
	
	public DomainGeneratorSimulated(int[][][] map) {
		
		this.map = map;
		this.length = map[0].length;
		this.width = map[0][0].length;
		this.height = map.length;
		
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
		vertDirAt.setDiscValuesForRange(0, this.numVertDirs - 1, 1);
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
		new ActionMoveForwardSimulated(HelperNameSpace.ACTIONMOVE, domain, this.map);
		new ActionChangeYawSimulated(HelperNameSpace.ACTIONROTATERIGHT, domain, 1);
		new ActionChangeYawSimulated(HelperNameSpace.ACTIONROTATELEFT, domain, HelperNameSpace.RotDirection.size - 1);
		new ActionChangePitchSimulated(HelperNameSpace.ACTIONAHEAD, domain, 0);
		new ActionChangePitchSimulated(HelperNameSpace.ACTIONDOWNONE, domain, this.numVertDirs - 1);
		new ActionPlaceBlockSimulated(HelperNameSpace.ACTIONPLACEBLOCK, domain, this.map);
		new ActionDestroyBlockSimulated(HelperNameSpace.ACTIONDESTBLOCK, domain);
		
		return domain;
		
	}
	
}
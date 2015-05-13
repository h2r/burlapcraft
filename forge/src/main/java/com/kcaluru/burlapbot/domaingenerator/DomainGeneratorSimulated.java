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

import com.kcaluru.burlapbot.actions.DestroyBlockActionReal;
import com.kcaluru.burlapbot.actions.DestroyBlockActionSimulated;
import com.kcaluru.burlapbot.actions.MovementActionReal;
import com.kcaluru.burlapbot.actions.MovementActionSimulated;
import com.kcaluru.burlapbot.actions.PlaceBlockActionReal;
import com.kcaluru.burlapbot.actions.PlaceBlockActionSimulated;
import com.kcaluru.burlapbot.actions.RotateActionReal;
import com.kcaluru.burlapbot.actions.RotateActionSimulated;
import com.kcaluru.burlapbot.actions.RotateVertActionReal;
import com.kcaluru.burlapbot.actions.RotateVertActionSimulated;
import com.kcaluru.burlapbot.helpers.BurlapAIHelper;
import com.kcaluru.burlapbot.helpers.NameSpace;
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
		Attribute xAtt = new Attribute(domain, NameSpace.ATX, AttributeType.INT);
		xAtt.setLims(0, this.length - 1);
		// y-position attribute
		Attribute yAtt = new Attribute(domain, NameSpace.ATY, AttributeType.INT);
		yAtt.setLims(0, this.height - 1);
		// z-position attribute
		Attribute zAtt = new Attribute(domain, NameSpace.ATZ, AttributeType.INT);
		zAtt.setLims(0, this.width - 1);
		// Rotational direction for agent
		Attribute rotDirAt = new Attribute(domain, NameSpace.ATROTDIR, Attribute.AttributeType.DISC);
		rotDirAt.setDiscValuesForRange(0, NameSpace.RotDirection.size - 1, 1);
		// Agent's vertical direction attribute
		Attribute vertDirAt = new Attribute(domain, NameSpace.ATVERTDIR, Attribute.AttributeType.DISC);
		vertDirAt.setDiscValuesForRange(0, this.numVertDirs - 1, 1);
		// Block type
		Attribute bType = new Attribute(domain, NameSpace.ATBTYPE, Attribute.AttributeType.INT);
		
		
		// Object classes
		// agent
		ObjectClass agentClass = new ObjectClass(domain, NameSpace.CLASSAGENT);
		agentClass.addAttribute(xAtt);
		agentClass.addAttribute(yAtt);
		agentClass.addAttribute(zAtt);
		agentClass.addAttribute(rotDirAt);
		agentClass.addAttribute(vertDirAt);
		// blocks
		ObjectClass blockClass = new ObjectClass(domain, NameSpace.CLASSBLOCK);
		blockClass.addAttribute(xAtt);
		blockClass.addAttribute(yAtt);
		blockClass.addAttribute(zAtt);
		blockClass.addAttribute(bType);
		
		// Actions
		new MovementActionSimulated(NameSpace.ACTIONMOVE, domain, this.map);
		new RotateActionSimulated(NameSpace.ACTIONROTATERIGHT, domain, 1);
		new RotateActionSimulated(NameSpace.ACTIONROTATELEFT, domain, NameSpace.RotDirection.size - 1);
		new RotateVertActionSimulated(NameSpace.ACTIONAHEAD, domain, 0);
		new RotateVertActionSimulated(NameSpace.ACTIONDOWNONE, domain, this.numVertDirs - 1);
		new PlaceBlockActionSimulated(NameSpace.ACTIONPLACEBLOCK, domain, this.map);
		new DestroyBlockActionSimulated(NameSpace.ACTIONDESTBLOCK, domain);
		
		return domain;
		
	}
	
}
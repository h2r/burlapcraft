package com.kcaluru.burlapbot.domaingenerator;

import net.minecraft.util.RegistryNamespaced;
import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import burlap.oomdp.singleagent.Action;
import burlap.oomdp.singleagent.SADomain;

import com.kcaluru.burlapbot.helpers.BurlapAIHelper;
import com.kcaluru.burlapbot.helpers.NameSpace;

import cpw.mods.fml.common.registry.GameData;

/**
 * Class to generate burlap domain for minecraft
 * @author Krishna Aluru
 *
 */

public class DungeonWorldDomain implements DomainGenerator {
		
	protected int length;
	protected int width;
	protected int height;
	
	protected int [][][] map;
	
	protected double [][] transitionDynamics;
	
	public DungeonWorldDomain(int [][][] map) {
		this.setMap(map);
	}
	
	public void setMap(int [][][] map) {
		this.length = map.length;
		this.width = map[0].length;
		this.height = map[0][0].length;
		this.map = map.clone();
	}
	
	@Override
	public Domain generateDomain() {
		
		SADomain domain = new SADomain();
		
		// Attributes
		
		// x-position attribute
		Attribute xAtt = new Attribute(domain, NameSpace.ATX, Attribute.AttributeType.DISC);
		xAtt.setLims(0, this.length - 1);
		// y-position attribute
		Attribute yAtt = new Attribute(domain, NameSpace.ATY, Attribute.AttributeType.DISC);
		yAtt.setLims(0, 5);
		// z-position attribute
		Attribute zAtt = new Attribute(domain, NameSpace.ATZ, Attribute.AttributeType.DISC);
		zAtt.setLims(0, this.width - 1);
		
		
		// Object classes
		
		// agent
		ObjectClass agentClass = new ObjectClass(domain, NameSpace.CLASSAGENT);
		agentClass.addAttribute(xAtt);
		agentClass.addAttribute(yAtt);
		agentClass.addAttribute(zAtt);
		// blocks
		ObjectClass blockClass = new ObjectClass(domain, NameSpace.CLASSBLOCK);
		blockClass.addAttribute(xAtt);
		blockClass.addAttribute(yAtt);
		blockClass.addAttribute(zAtt);
		
		
		// Actions
		
//		new Movement(NameSpace.ACTIONEAST, domain, 0);
//		new Movement(NameSpace.ACTIONWEST, domain, 1);
//		new Movement(NameSpace.ACTIONNORTH, domain, 2);
//		new Movement(NameSpace.ACTIONSOUTH, domain, 3);
		
		return domain;
		
	}

}

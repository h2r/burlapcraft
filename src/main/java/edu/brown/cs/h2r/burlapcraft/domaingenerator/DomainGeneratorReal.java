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
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.Dungeon;

/**
 * Class to generate burlap domain for minecraft
 * @author Krishna Aluru
 *
 */

public class DomainGeneratorReal implements DomainGenerator {
		
	protected int length;
	protected int width;
	protected int height;
	public static Dungeon dungeonID;
	
	public DomainGeneratorReal(int length, int width, int height) {
		
		this.length = length;
		this.width = width;
		this.height = height;
		
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
		
		
		// Actions
		new ActionMoveForwardReal(HelperNameSpace.ACTIONMOVE, domain);
		new ActionChangeYawReal(HelperNameSpace.ACTIONROTATERIGHT, domain, 1);
		new ActionChangeYawReal(HelperNameSpace.ACTIONROTATELEFT, domain, HelperNameSpace.RotDirection.size - 1);
		new ActionChangePitchReal(HelperNameSpace.ACTIONAHEAD, domain, 0);
		new ActionChangePitchReal(HelperNameSpace.ACTIONDOWNONE, domain, HelperNameSpace.VertDirection.size - 1);
		new ActionPlaceBlockReal(HelperNameSpace.ACTIONPLACEBLOCK, domain);
		new ActionDestroyBlockReal(HelperNameSpace.ACTIONDESTBLOCK, domain);
		
		return domain;
		
	}
	
}
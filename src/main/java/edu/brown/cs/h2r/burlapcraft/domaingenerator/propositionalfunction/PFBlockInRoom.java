package edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction;

import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.states.State;

public class PFBlockInRoom extends PropositionalFunction {

	public PFBlockInRoom(String name, Domain domain, String[] parameterClasses) {
		super(name, domain, parameterClasses);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isTrue(State s, String... params) {
		// TODO Auto-generated method stub
		
		ObjectInstance block = s.getObject(params[0]);
		ObjectInstance room = s.getObject(params[1]);
		
		if (block == null) {
			return false;
		}
		
		int blockX = block.getIntValForAttribute(HelperNameSpace.ATX);
		int blockZ = block.getIntValForAttribute(HelperNameSpace.ATZ);
		
		int xMaxRoom = room.getIntValForAttribute(HelperNameSpace.ATXMAX);
		int xMinRoom = room.getIntValForAttribute(HelperNameSpace.ATXMIN);
		int zMaxRoom = room.getIntValForAttribute(HelperNameSpace.ATZMAX);
		int zMinRoom = room.getIntValForAttribute(HelperNameSpace.ATZMIN);
		
		if (blockX < xMaxRoom && blockX > xMinRoom && blockZ > zMinRoom && blockZ < zMaxRoom) {
			return true;
		}
		
		return false;
	}

}

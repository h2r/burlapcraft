package edu.brown.cs.h2r.burlapcraft.domaingenerator.propositionalfunction;

import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.PropositionalFunction;
import burlap.oomdp.core.states.State;

public class PFAgentInRoom extends PropositionalFunction {

	public PFAgentInRoom(String name, Domain domain, String[] parameterClasses) {
		super(name, domain, parameterClasses);

	}

	@Override
	public boolean isTrue(State s, String... params) {
		// TODO Auto-generated method stub
		
		ObjectInstance agent = s.getObject(params[0]);
		ObjectInstance room = s.getObject(params[1]);
		
		int agentX = agent.getIntValForAttribute(HelperNameSpace.ATX);
		int agentZ = agent.getIntValForAttribute(HelperNameSpace.ATZ);
		
		int xMaxRoom = room.getIntValForAttribute(HelperNameSpace.ATXMAX);
		int xMinRoom = room.getIntValForAttribute(HelperNameSpace.ATXMIN);
		int zMaxRoom = room.getIntValForAttribute(HelperNameSpace.ATZMAX);
		int zMinRoom = room.getIntValForAttribute(HelperNameSpace.ATZMIN);
		
		if (agentX < xMaxRoom && agentX > xMinRoom && agentZ > zMinRoom && agentZ < zMaxRoom) {
			return true;
		}
		
		return false;
	}

}

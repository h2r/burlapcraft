package edu.brown.cs.h2r.burlapcraft.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.common.SimpleAction.SimpleDeterministicAction;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import net.minecraft.block.Block;

public class ActionAttackSimulated extends SimpleDeterministicAction{
	public ActionAttackSimulated(String name, Domain domain) {
		super(name, domain);
	}

	@Override
	protected State performActionHelper(State s, GroundedAction groundedAction) {
		return s;
	}

	@Override
	public boolean applicableInState(State s, GroundedAction groundedAction) {
		return true;
	}
}

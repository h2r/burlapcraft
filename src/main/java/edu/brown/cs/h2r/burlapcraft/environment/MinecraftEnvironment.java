package edu.brown.cs.h2r.burlapcraft.environment;

import java.util.HashMap;

import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.action.ActionController;
import edu.brown.cs.h2r.burlapcraft.action.ActionControllerChangeItem;
import edu.brown.cs.h2r.burlapcraft.action.ActionControllerChangePitch;
import edu.brown.cs.h2r.burlapcraft.action.ActionControllerChangeYaw;
import edu.brown.cs.h2r.burlapcraft.action.ActionControllerDestroyBlock;
import edu.brown.cs.h2r.burlapcraft.action.ActionControllerMoveForward;
import edu.brown.cs.h2r.burlapcraft.action.ActionControllerPlaceBlock;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;
import burlap.oomdp.auxiliary.common.NullTermination;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.common.NullRewardFunction;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.environment.Environment;
import burlap.oomdp.singleagent.environment.EnvironmentOutcome;

public class MinecraftEnvironment implements Environment {
	
	protected Domain d;
	protected double lastReward = 0;
	protected HashMap<String, ActionController> actionControllerMap;
	protected RewardFunction rewardFunction;
	protected TerminalFunction terminalFunction;
	
	public MinecraftEnvironment(Domain d) {
		this.d = d;
		this.rewardFunction = new NullRewardFunction();
		this.terminalFunction = new NullTermination();
		int delayMS = 1500;
		actionControllerMap = new HashMap<String, ActionController>();
		actionControllerMap.put(HelperNameSpace.ACTIONMOVE, new ActionControllerMoveForward(delayMS, this));
		actionControllerMap.put(HelperNameSpace.ACTIONROTATERIGHT, new ActionControllerChangeYaw(delayMS, this, 1));
		actionControllerMap.put(HelperNameSpace.ACTIONROTATELEFT, new ActionControllerChangeYaw(delayMS, this, HelperNameSpace.RotDirection.size - 1));
		actionControllerMap.put(HelperNameSpace.ACTIONAHEAD, new ActionControllerChangePitch(delayMS, this, 0));
		actionControllerMap.put(HelperNameSpace.ACTIONDOWNONE, new ActionControllerChangePitch(delayMS, this, HelperNameSpace.VertDirection.size - 1));
		actionControllerMap.put(HelperNameSpace.ACTIONDESTBLOCK, new ActionControllerDestroyBlock(delayMS, this));
		actionControllerMap.put(HelperNameSpace.ACTIONPLACEBLOCK, new ActionControllerPlaceBlock(delayMS, this));
		actionControllerMap.put(HelperNameSpace.ACTIONCHANGEITEM, new ActionControllerChangeItem(delayMS, this));
	}
	
	@Override
	public State getCurrentObservation() {
		return StateGenerator.getCurrentState(this.d, BurlapCraft.currentDungeon);
	}

	@Override
	public EnvironmentOutcome executeAction(GroundedAction ga) {
		State startState = this.getCurrentObservation();
		
		ActionController ac = this.actionControllerMap.get(ga.actionName());
		int delay = ac.executeAction(ga);
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		State finalState = this.getCurrentObservation();
		
		this.lastReward = this.rewardFunction.reward(startState, ga, finalState);
		
		EnvironmentOutcome eo = new EnvironmentOutcome(startState, ga, finalState, this.lastReward, this.isInTerminalState());
		
		return eo;
	}

	@Override
	public double getLastReward() {
		return this.lastReward;
	}

	@Override
	public boolean isInTerminalState() {
		return this.terminalFunction.isTerminal(this.getCurrentObservation());
	}

	@Override
	public void resetEnvironment() {
		this.lastReward = 0;
	}
	
	public void setRewardFunction(RewardFunction rf) {
		this.rewardFunction = rf;
	}
	
	public void setTerminalFunction(TerminalFunction tf) {
		this.terminalFunction = tf;
	}

}

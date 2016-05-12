package edu.brown.cs.h2r.burlapcraft.environment;

import burlap.mdp.auxiliary.common.NullTermination;
import burlap.mdp.core.Domain;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.GroundedAction;
import burlap.mdp.singleagent.RewardFunction;
import burlap.mdp.singleagent.common.NullRewardFunction;
import burlap.mdp.singleagent.environment.Environment;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.action.*;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.stategenerator.StateGenerator;

import java.util.HashMap;


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
		actionControllerMap.put(HelperNameSpace.ACTION_MOVE, new ActionControllerMoveForward(delayMS, this));
		actionControllerMap.put(HelperNameSpace.ACTION_ROTATE_RIGHT, new ActionControllerChangeYaw(delayMS, this, 1));
		actionControllerMap.put(HelperNameSpace.ACTION_ROTATE_LEFT, new ActionControllerChangeYaw(delayMS, this, HelperNameSpace.RotDirection.size - 1));
		actionControllerMap.put(HelperNameSpace.ACTION_AHEAD, new ActionControllerChangePitch(delayMS, this, 0));
		actionControllerMap.put(HelperNameSpace.ACTION_DOWN_ONE, new ActionControllerChangePitch(delayMS, this, HelperNameSpace.VertDirection.size - 1));
		actionControllerMap.put(HelperNameSpace.ACTION_DEST_BLOCK, new ActionControllerDestroyBlock(delayMS, this));
		actionControllerMap.put(HelperNameSpace.ACTION_PLACE_BLOCK, new ActionControllerPlaceBlock(delayMS, this));
		actionControllerMap.put(HelperNameSpace.ACTION_CHANGE_ITEM, new ActionControllerChangeItem(delayMS, this));
	}
	
	@Override
	public State currentObservation() {
		return StateGenerator.getCurrentState(this.d, BurlapCraft.currentDungeon);
	}

	@Override
	public EnvironmentOutcome executeAction(GroundedAction ga) {
		State startState = this.currentObservation();
		
		ActionController ac = this.actionControllerMap.get(ga.actionName());
		int delay = ac.executeAction(ga);
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		State finalState = this.currentObservation();
		
		this.lastReward = this.rewardFunction.reward(startState, ga, finalState);
		
		EnvironmentOutcome eo = new EnvironmentOutcome(startState, ga, finalState, this.lastReward, this.isInTerminalState());
		
		return eo;
	}

	@Override
	public double lastReward() {
		return this.lastReward;
	}

	@Override
	public boolean isInTerminalState() {
		return this.terminalFunction.isTerminal(this.currentObservation());
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

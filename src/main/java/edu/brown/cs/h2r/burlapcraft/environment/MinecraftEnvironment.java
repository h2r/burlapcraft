package edu.brown.cs.h2r.burlapcraft.environment;

import burlap.mdp.auxiliary.StateGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.common.UniformCostRF;
import burlap.mdp.singleagent.environment.Environment;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.model.RewardFunction;
import edu.brown.cs.h2r.burlapcraft.domaingenerator.GoldBlockTF;
import edu.brown.cs.h2r.burlapcraft.environment.controllers.*;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.state.DungeonStateGenerator;

import java.util.HashMap;

/**
 * A Minecraft environment where interaction with it causes actual changes with Minecraft game by controlling
 * the player's avatar. Controllers exist for actions with specific names (those defined
 * in the {@link HelperNameSpace}). You can also change or add action controllers
 * by using the {@link #setController(String, ActionController)}.
 * <p>
 * The default reward function and terminal function are {@link burlap.mdp.singleagent.common.UniformCostRF}
 * and {@link edu.brown.cs.h2r.burlapcraft.domaingenerator.GoldBlockTF}. You can change them with the
 * {@link #setRewardFunction(RewardFunction)} and {@link #setTerminalFunction(TerminalFunction)} methods.
 * <p>
 * The state/observations of the environment are generated using an {@link DungeonStateGenerator} instance; however,
 * you can change the kinds of observation by changing the {@link StateGenerator} with the {@link #setStateGenerator(StateGenerator)}
 * method.
 */
public class MinecraftEnvironment implements Environment {

	protected double lastReward = 0;
	protected HashMap<String, ActionController> actionControllerMap;
	protected RewardFunction rewardFunction = new UniformCostRF();
	protected TerminalFunction terminalFunction = new GoldBlockTF();
	protected StateGenerator stateGenerator = new DungeonStateGenerator();
	
	public MinecraftEnvironment() {
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

	public void setController(String actionName, ActionController ac){
		this.actionControllerMap.put(actionName, ac);
	}
	
	@Override
	public State currentObservation() {
		return stateGenerator.generateState();
	}

	@Override
	public EnvironmentOutcome executeAction(Action a) {
		State startState = this.currentObservation();
		
		ActionController ac = this.actionControllerMap.get(a.actionName());
		int delay = ac.executeAction(a);
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		State finalState = this.currentObservation();
		
		this.lastReward = this.rewardFunction.reward(startState, a, finalState);
		
		EnvironmentOutcome eo = new EnvironmentOutcome(startState, a, finalState, this.lastReward, this.isInTerminalState());
		
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

	public StateGenerator getStateGenerator() {
		return stateGenerator;
	}

	public void setStateGenerator(StateGenerator stateGenerator) {
		this.stateGenerator = stateGenerator;
	}


}

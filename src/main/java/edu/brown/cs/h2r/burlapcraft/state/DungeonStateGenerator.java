package edu.brown.cs.h2r.burlapcraft.state;

import burlap.mdp.auxiliary.StateGenerator;
import burlap.mdp.core.state.State;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;

/**
 * A {@link StateGenerator} that generates a state by determining the current
 * dungeon the player is in, and then returns the result of {@link MinecraftStateGeneratorHelper#getCurrentState(Dungeon)}.
 * @author James MacGlashan.
 */
public class DungeonStateGenerator implements StateGenerator {

	@Override
	public State generateState() {
		return MinecraftStateGeneratorHelper.getCurrentState(BurlapCraft.currentDungeon);
	}
}

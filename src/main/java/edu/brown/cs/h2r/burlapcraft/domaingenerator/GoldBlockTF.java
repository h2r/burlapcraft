package edu.brown.cs.h2r.burlapcraft.domaingenerator;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.state.BCAgent;
import edu.brown.cs.h2r.burlapcraft.state.BCBlock;

import java.util.List;

import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.CLASS_AGENT;

/**
 * Sets terminal states to those when the agent is at a gold block (block type 41).
 * @author James MacGlashan.
 */
public class GoldBlockTF implements TerminalFunction {

	@Override
	public boolean isTerminal(State s) {
		OOState os = (OOState)s;

		BCAgent a = (BCAgent)os.object(CLASS_AGENT);

		HelperGeometry.Pose agentPose = HelperGeometry.Pose.fromXyz(a.x, a.y, a.z);

		HelperGeometry.Pose goalPose = getGoalPose(s);

		//are they at goal location or dead
		double distance = goalPose.distance(agentPose);
		//System.out.println("Distance: " + distance + " goal at: " + goalPose);

		if (distance < 0.5) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Find the gold block and return its pose.
	 * @param s the state
	 * @return the pose of the agent being one unit above the gold block.
	 */
	public static HelperGeometry.Pose getGoalPose(State s) {

		OOState os = (OOState)s;

		List<ObjectInstance> blocks = os.objectsOfClass(HelperNameSpace.CLASS_BLOCK);
		for (ObjectInstance oblock : blocks) {
			BCBlock block = (BCBlock)oblock;
			if (block.type == 41) {
				int goalX = block.x;
				int goalY = block.y;
				int goalZ = block.z;

				return HelperGeometry.Pose.fromXyz(goalX, goalY + 1, goalZ);
			}
		}
		return null;
	}
}

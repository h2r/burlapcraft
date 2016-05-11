package edu.brown.cs.h2r.burlapcraft.stategenerator;

import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.UnknownKeyException;
import burlap.mdp.core.state.annotations.DeepCopyState;

import java.util.Arrays;
import java.util.List;

import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.*;

/**
 * @author James MacGlashan.
 */
@DeepCopyState
public class BCAgent implements ObjectInstance {

	public int x;
	public int y;
	public int z;
	public int rdir;
	public int vdir;
	public int selected;

	private static final List<Object> keys = Arrays.<Object>asList(VAR_X, VAR_Y, VAR_Z, VAR_R_DIR, VAR_V_DIR, VAR_SEL);

	public BCAgent() {
	}

	public BCAgent(int x, int y, int z, int rdir, int vdir, int selected) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.rdir = rdir;
		this.vdir = vdir;
		this.selected = selected;
	}

	@Override
	public List<Object> variableKeys() {
		return keys;
	}

	@Override
	public Object get(Object variableKey) {
		if(variableKey.equals(VAR_X)){
			return x;
		}
		else if(variableKey.equals(VAR_Y)){
			return y;
		}
		else if(variableKey.equals(VAR_Z)){
			return z;
		}
		else if(variableKey.equals(VAR_R_DIR)){
			return rdir;
		}
		else if(variableKey.equals(VAR_V_DIR)){
			return vdir;
		}
		else if(variableKey.equals(VAR_SEL)){
			return selected;
		}
		throw new UnknownKeyException(variableKey);
	}

	@Override
	public String className() {
		return CLASS_AGENT;
	}

	@Override
	public String name() {
		return CLASS_AGENT;
	}

	@Override
	public BCAgent copyWithName(String objectName) {
		if(!objectName.equals(CLASS_AGENT)){
			throw new RuntimeException("Agent object must be named " + CLASS_AGENT);
		}
		return copy();
	}

	@Override
	public BCAgent copy() {
		return new BCAgent(x, y, z, rdir, vdir, selected);
	}

	@Override
	public String toString() {
		return OOStateUtilities.objectInstanceToString(this);
	}
}

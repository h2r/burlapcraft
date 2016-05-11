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
public class BCBlock implements ObjectInstance {

	public int x;
	public int y;
	public int z;
	public int type;

	protected String name;

	private static final List<Object> keys = Arrays.<Object>asList(VAR_X, VAR_Y, VAR_Z, VAR_BT);

	public BCBlock() {
	}

	public BCBlock(int x, int y, int z, int type, String name) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.name = name;
	}

	@Override
	public String className() {
		return CLASS_BLOCK;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public BCBlock copyWithName(String objectName) {
		return new BCBlock(x, y, z, type, objectName);
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
		else if(variableKey.equals(VAR_BT)){
			return type;
		}
		throw new UnknownKeyException(variableKey);
	}

	@Override
	public BCBlock copy() {
		return new BCBlock(x, y, z, type, name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return OOStateUtilities.objectInstanceToString(this);
	}
}

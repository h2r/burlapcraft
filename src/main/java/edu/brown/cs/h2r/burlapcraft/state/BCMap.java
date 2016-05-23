package edu.brown.cs.h2r.burlapcraft.state;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.annotations.ShallowCopyState;

import java.util.Arrays;
import java.util.List;

import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.CLASS_MAP;
import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.VAR_MAP;

/**
 * @author James MacGlashan.
 */
@ShallowCopyState
public class BCMap implements ObjectInstance{

	public int [][][] map;

	private static final List<Object> keys = Arrays.<Object>asList(VAR_MAP);

	public BCMap() {
	}

	public BCMap(int[][][] map) {
		this.map = map;
	}

	@Override
	public String className() {
		return CLASS_MAP;
	}

	@Override
	public String name() {
		return CLASS_MAP;
	}

	@Override
	public BCMap copyWithName(String objectName) {
		if(!objectName.equals(CLASS_MAP)){
			throw new RuntimeException("Map object must be named " + CLASS_MAP);
		}
		return copy();
	}

	@Override
	public List<Object> variableKeys() {
		return keys;
	}

	@Override
	public Object get(Object variableKey) {
		return map;
	}

	@Override
	public BCMap copy() {
		return new BCMap(map);
	}

	@Override
	public String toString() {
		return map.length + "x" + map[0].length + "x" + map[0][0].length;
	}
}

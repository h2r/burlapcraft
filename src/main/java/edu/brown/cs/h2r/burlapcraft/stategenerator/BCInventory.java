package edu.brown.cs.h2r.burlapcraft.stategenerator;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.annotations.ShallowCopyState;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;

import java.util.Arrays;
import java.util.List;

import static edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.CLASS_INVENTORY;

/**
 * @author James MacGlashan.
 */
@ShallowCopyState
public class BCInventory implements ObjectInstance {

	public BCIStack[] inv = new BCIStack[9];

	private static List<Object> keys = Arrays.<Object>asList(HelperNameSpace.VAR_INV_NUM);

	public BCInventory() {
	}

	public BCInventory(BCIStack[] inv) {
		this.inv = inv;
	}

	@Override
	public String className() {
		return CLASS_INVENTORY;
	}

	@Override
	public String name() {
		return CLASS_INVENTORY;
	}

	@Override
	public BCInventory copyWithName(String objectName) {
		if(!objectName.equals(CLASS_INVENTORY)){
			throw new RuntimeException("Inventory object must be named " + CLASS_INVENTORY);
		}
		return copy();
	}

	@Override
	public List<Object> variableKeys() {
		return keys;
	}

	@Override
	public Object get(Object variableKey) {
		return inv;
	}

	@Override
	public BCInventory copy() {
		return new BCInventory(inv);
	}

	@Override
	public String toString() {
		return Arrays.toString(inv);
	}

	public BCIStack[] touch(){
		BCIStack[] nstack = new BCIStack[this.inv.length];
		for(int i = 0; i < this.inv.length; i++){
			nstack[i] = this.inv[i].copy();
		}
		this.inv = nstack;
		return nstack;
	}

	public int indexOfType(int type){
		for(int i = 0; i < this.inv.length; i++){
			if(inv[i].type == type){
				return i;
			}
		}
		return -1;
	}

	public int firstFree(){
		return this.indexOfType(-1);
	}


	public static class BCIStack{
		public int type = -1;
		public int num = 0;

		public BCIStack() {
		}

		public BCIStack(int type, int num) {
			this.type = type;
			this.num = num;
		}

		public int dec(){
			if(this.num > 0){
				this.num--;
				if(this.num == 0){
					this.type = -1;
				}
				return this.num;
			}
			else{
				return 0;
			}
		}

		public int inc(){
			this.num++;
			return this.num;
		}

		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;

			BCIStack bciStack = (BCIStack) o;

			if(type != bciStack.type) return false;
			return num == bciStack.num;

		}

		@Override
		public int hashCode() {
			int result = type;
			result = 31 * result + num;
			return result;
		}

		public BCIStack copy(){
			return new BCIStack(this.type, this.num);
		}

		@Override
		public String toString() {
			return "(" + type + ": " + num + ")";
		}
	}



}

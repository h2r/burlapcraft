package edu.brown.cs.h2r.burlapcraft.helper;

public class HelperNameSpace {
	
	//-------------ATTRIBUTE STRINGS---------------------
	public static final String VAR_X = "x";
	public static final String VAR_Y = "y";
	public static final String VAR_Z = "z";
	public static final String VAR_BT = "blockType";
	public static final String VAR_R_DIR = "rotationalDirection";
	public static final String VAR_V_DIR = "verticalDirection";
	public static final String VAR_INV_NUM = "inventoryBlockQuantity";
	public static final String VAR_SEL = "selectedItemID";
	public static final String VAR_MAP = "map";

	//-------------BURLAP OBJECTCLASS STRINGS-------------
	public static final String CLASS_AGENT = "agent";
	public static final String CLASS_BLOCK = "block";
	public static final String CLASS_MAP = "map";
	public static final String CLASS_INVENTORY = "inventory";
	
	//-------------ACTIONS STRINGS-------------
	public static final String ACTION_MOVE = "moveForward";
	public static final String ACTION_ROTATE_RIGHT = "rotateRight";
	public static final String ACTION_ROTATE_LEFT = "rotateLeft";
	public static final String ACTION_DEST_BLOCK = "destroyBlock";
	public static final String ACTION_PLACE_BLOCK = "placeBlock";
	public static final String ACTION_AHEAD = "lookAhead";
	public static final String ACTION_DOWN_ONE = "lookDown";
	public static final String ACTION_CHANGE_ITEM = "changeItem";
	
	//-------------PROPOSITIONAL FUNCTION STRINGS-------------
	public static final String PF_AGENT_HAS_BLOCK = "agentHasBlock";
	public static final String PF_AGENT_ON_BLOCK = "agentOnBlock";

	public static final String PF_BLOCK_BASE = "blockIs";
	public static final String PF_BLOCK_IS_TYPE = PF_BLOCK_BASE + "TYPE";
	
	
	//-------------ENUMS-------------
	public enum RotDirection {
		NORTH(2), EAST(3), SOUTH(0), WEST(1);
		public static final int size = RotDirection.values().length;
		private final int intVal;
		
		RotDirection(int i) {
			this.intVal = i;
		}
		
		public int toInt() {
			return this.intVal;
		}
		
		public static RotDirection fromInt(int i) {
			switch (i) {
			case 2:
				return NORTH;
			case 0:
				return SOUTH;
			case 3:
				return EAST;
			case 1:
				return WEST;
			
			}
			return null;
		}
		
	}
	
	public enum VertDirection {
		AHEAD(0), DOWN(1);
		public static final int size = VertDirection.values().length;
		private final int intVal;
		
		VertDirection(int i) {
			this.intVal = i;
		}
		
		public int toInt() {
			return this.intVal;
		}
		
		public static VertDirection fromInt(int i) {
			switch (i) {
			case 1:
				return DOWN;
			case 0:
				return AHEAD;
			
			}
			return null;
		}
	}
	
}

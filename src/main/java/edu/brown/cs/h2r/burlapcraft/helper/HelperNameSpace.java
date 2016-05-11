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
	public static final String VAR_BLOCK_NAMES = "blockNames";
	public static final String VAR_MAP = "map";

	//-------------BURLAP OBJECTCLASS STRINGS-------------
	public static final String CLASS_AGENT = "agent";
	public static final String CLASS_BLOCK = "block";
	public static final String CLASS_MAP = "map";
	public static final String CLASS_INVENTORY_BLOCK = "inventoryBlock";
	
	//-------------ACTIONS STRINGS-------------
	public static final String							ACTIONMOVE = "moveForward";
	public static final String							ACTIONROTATERIGHT = "rotateRight";
	public static final String 							ACTIONROTATELEFT = "rotateLeft";
	public static final String 							ACTIONDESTBLOCK = "destroyBlock";
	public static final String							ACTIONPLACEBLOCK = "placeBlock";
	public static final String							ACTIONAHEAD = "lookAhead";
	public static final String							ACTIONDOWNONE = "lookDown";
	public static final String							ACTIONCHANGEITEM = "changeItem";
	
	//-------------PROPOSITIONAL FUNCTION STRINGS-------------
	public static final String							PFAGENTINROOM = "agentInRoom";
	public static final String							PFAGENTHASBLOCK = "agentHasBlock";
	public static final String							PFAGENTONBLOCK = "agentOnBlock";

	public static final String							PFBLOCKBASE = "blockIs";
	public static final String							PFBLOCKISTYPE = PFBLOCKBASE + "TYPE";
	
	
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

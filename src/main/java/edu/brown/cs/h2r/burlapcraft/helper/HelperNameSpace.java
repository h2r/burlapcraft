package edu.brown.cs.h2r.burlapcraft.helper;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.RegistryNamespaced;

public class HelperNameSpace {
	
	//-------------ATTRIBUTE STRINGS---------------------
	public static final String							ATX = "x";
	public static final String							ATY = "y";
	public static final String							ATZ = "z";
	public static final String							ATBTYPE = "blockType";
	public static final String 							ATROTDIR = "rotationalDirection";
	public static final String							ATVERTDIR = "verticalDirection";
	public static final String							ATIBQUANT = "inventoryBlockQuantity";
	public static final String							ATXMIN = "roomXMin";
	public static final String							ATXMAX = "roomXMax";
	public static final String							ATZMIN = "roomZMin";
	public static final String							ATZMAX = "roomZMax";
	public static final String							ATCOLOR = "roomColor";
	public static final String							ATSELECTEDITEMID = "selectedItemID";
	public static final String							ATBLOCKNAMES = "blockNames";
	
	//-------------BURLAP OBJECTCLASS STRINGS-------------
	public static final String							CLASSAGENT = "agent";
	public static final String							CLASSBLOCK = "block";
	public static final String							CLASSINVENTORYBLOCK = "inventoryBlock";
	public static final String							CLASSROOM = "room";
	
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
	public static final String							PFBLOCKINROOM = "blockInRoom";
	public static final String							PFROOMRED = "roomIsRed";
	public static final String							PFROOMBLUE = "roomIsBlue";
	public static final String							PFROOMGREEN = "roomIsGreen";
	public static final String							PFROOMORANGE = "roomIsOrange";
	public static final String							PFAGENTHASBLOCK = "agentHasBlock";
	public static final String							PFAGENTONBLOCK = "agentOnBlock";

	public static final String							PFBLOCKBASE = "blockIs";
	public static final String							PFBLOCKRED = PFBLOCKBASE+"Red";
	public static final String							PFBLOCKGREEN = PFBLOCKBASE+"Green";
	public static final String							PFBLOCKBLUE = PFBLOCKBASE+"Blue";
	public static final String							PFBLOCKORANGE = PFBLOCKBASE+"Orange";
	public static final String							PFBLOCKCHAIR = PFBLOCKBASE+"Chair";
	public static final String							PFBLOCKBAG = PFBLOCKBASE+"Bag";
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

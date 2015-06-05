package edu.brown.cs.h2r.burlapcraft.stategenerator;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerDungeonGeneration;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace.Dungeon;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;
import net.minecraft.block.Block;

public class StateGenerator {
	
	// tracking number of blocks to set blockIDs
	public static int blockCount = 0;
	
	// finder dungeon dimensions
	private static int finderLength = 5;
	private static int finderWidth = 5;
	private static int finderHeight = 3;
	// tiny bridge dungeon dimensions
	private static int tinyBridgeLength = 4;
	private static int tinyBridgeWidth = 5;
	private static int tinyBridgeHeight = 4;
	
	// small bridge dungeon dimensions
	private static int smallBridgeLength = 10;
	private static int smallBridgeWidth = 10;
	private static int smallBridgeHeight = 5;
	// grid dungeon dimensions
	private static int gridLength = 10;
	private static int gridWidth = 10;
	private static int gridHeight = 4;
	
	
	private static Pose dungeonPose;
	private static int length;
	private static int width;
	private static int height;

	public static State getCurrentState(Domain domain, Dungeon dungeonID) {
		System.out.println("Dungeon ID: " + dungeonID);
		if (dungeonID == Dungeon.FINDER) {
			dungeonPose = HandlerDungeonGeneration.finderPose;
			length = finderLength;
			width = finderWidth;
			height = finderHeight;
		} else if (dungeonID == Dungeon.TINY_BRIDGE) {
			dungeonPose = HandlerDungeonGeneration.tinyBridgePose;
			length = tinyBridgeLength;
			width = tinyBridgeWidth;
			height = tinyBridgeHeight;
		} else if (dungeonID == Dungeon.SMALL_BRIDGE) {
			dungeonPose = HandlerDungeonGeneration.smallBridgePose;
			length = smallBridgeLength;
			width = smallBridgeWidth;
			height = smallBridgeHeight;
		} else if (dungeonID == Dungeon.GRID) {
			dungeonPose = HandlerDungeonGeneration.gridPose;
			length = gridLength;
			width = gridWidth;
			height = gridHeight;
		} else {
			throw new IllegalArgumentException("Bad dungeon ID: " + dungeonID);
		}
		
		State s = new State();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < length; j++) {
				for (int k = 0; k < width; k++) {
					Block block = HelperActions.getBlock(dungeonPose.getX() + j, dungeonPose.getY() + i, dungeonPose.getZ() + k);
					if (HelperActions.blockIsOneOf(block, HelperActions.mineableBlocks) || HelperActions.blockIsOneOf(block, HelperActions.dangerBlocks)) {
						int blockID = HelperActions.getBlockId(dungeonPose.getX() + j, dungeonPose.getY() + i, dungeonPose.getZ() + k);
						blockCount += 1;
						// Note: name block after its x, y, and z coordinates
						ObjectInstance blockInstance = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSBLOCK), "block" + blockCount);
						blockInstance.setValue(HelperNameSpace.ATX, j);
						blockInstance.setValue(HelperNameSpace.ATY, i);
						blockInstance.setValue(HelperNameSpace.ATZ, k);
						blockInstance.setValue(HelperNameSpace.ATBTYPE, blockID);
						
						s.addObject(blockInstance);
					}
				}
			}
		}

		HelperPos curPos = HelperActions.getPlayerPosition();
		int rotateDirection = HelperActions.getYawDirection();
		int rotateVertDirection = HelperActions.getPitchDirection();
		System.out.println("Player position: " + curPos);
		System.out.println("Dungeon: " + dungeonPose);
		ObjectInstance agent = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSAGENT), "agent0");
		agent.setValue(HelperNameSpace.ATX, curPos.x - dungeonPose.getX());
		agent.setValue(HelperNameSpace.ATY, curPos.y - dungeonPose.getY());
		agent.setValue(HelperNameSpace.ATZ, curPos.z - dungeonPose.getZ());
		agent.setValue(HelperNameSpace.ATROTDIR, rotateDirection);
		agent.setValue(HelperNameSpace.ATVERTDIR, rotateVertDirection);
		
		s.addObject(agent);
		validate(s);
		return s;
	}
	
	public static void validate(State s) {
		
		ObjectInstance agent = s.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);		
		int ax = agent.getIntValForAttribute(HelperNameSpace.ATX);
		int ay = agent.getIntValForAttribute(HelperNameSpace.ATY);
		int az = agent.getIntValForAttribute(HelperNameSpace.ATZ);
		
		if (ax < 0) {
			throw new IllegalStateException("Invalid agent x: " + ax);
		}
		if (ay < 0) {
			throw new IllegalStateException("Invalid agent y: " + ay);
		}
		if (az < 0) {
			throw new IllegalStateException("Invalid agent z: " + az);
		}
		
		
	}
	
	public static int[][][] getMap(Dungeon dungeon) {
		
		if (dungeon == Dungeon.FINDER) {
			dungeonPose = HandlerDungeonGeneration.finderPose;
			length = finderLength;
			width = finderWidth;
			height = finderHeight;
		} else if (dungeon == Dungeon.TINY_BRIDGE) {
			dungeonPose = HandlerDungeonGeneration.tinyBridgePose;
			length = tinyBridgeLength;
			width = tinyBridgeWidth;
			height = tinyBridgeHeight;
		} else if (dungeon == Dungeon.SMALL_BRIDGE) {
			dungeonPose = HandlerDungeonGeneration.smallBridgePose;
			length = smallBridgeLength;
			width = smallBridgeWidth;
			height = smallBridgeHeight;
		} else if (dungeon == Dungeon.GRID) {
			dungeonPose = HandlerDungeonGeneration.gridPose;
			length = gridLength;
			width = gridWidth;
			height = gridHeight;
		} else {
			throw new IllegalArgumentException("Bad dungeon ID: " + dungeon);
		}
		
		int[][][] map = new int[height][length][width];
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < length; j++) {
				for (int k = 0; k < width; k++) {
					Block block = HelperActions.getBlock(dungeonPose.getX() + j, dungeonPose.getY() + i, dungeonPose.getZ() + k);
					if (!(HelperActions.blockIsOneOf(block, HelperActions.mineableBlocks) || HelperActions.blockIsOneOf(block, HelperActions.dangerBlocks))) {
						int blockID = HelperActions.getBlockId(dungeonPose.getX() + j, dungeonPose.getY() + i, dungeonPose.getZ() + k);
						map[i][j][k] = blockID;
					}
				}
			}
		}	
		return map;
	}
	
}

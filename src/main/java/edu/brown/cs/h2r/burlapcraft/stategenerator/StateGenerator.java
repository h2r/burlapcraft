package edu.brown.cs.h2r.burlapcraft.stategenerator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

import edu.brown.cs.h2r.burlapcraft.handler.HandlerDungeonGeneration;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;

public class StateGenerator {
	
	public static int blockCount = 0;
	
	// finder dungeon dimensions
	private static int finderLength = 5;
	private static int finderWidth = 5;
	private static int finderHeight = 3;
	// bridge dungeon dimensions
	private static int bridgeLength = 4;
	private static int bridgeWidth = 5;
	private static int bridgeHeight = 4;
	
	private static int dungeonX;
	private static int dungeonY;
	private static int dungeonZ;
	private static int length;
	private static int width;
	private static int height;

	public static State getCurrentState(Domain domain, int dungeonID) {
		
		if (dungeonID == 1) {
			dungeonX = HandlerDungeonGeneration.finderX;
			dungeonY = HandlerDungeonGeneration.finderY;
			dungeonZ = HandlerDungeonGeneration.finderZ;
			length = finderLength;
			width = finderWidth;
			height = finderHeight;
		}
		else if (dungeonID == 2) {
			dungeonX = HandlerDungeonGeneration.bridgeX;
			dungeonY = HandlerDungeonGeneration.bridgeY;
			dungeonZ = HandlerDungeonGeneration.bridgeZ;
			length = bridgeLength;
			width = bridgeWidth;
			height = bridgeHeight;
		}
		
		State s = new State();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < length; j++) {
				for (int k = 0; k < width; k++) {
					Block block = HelperActions.getBlock(dungeonX + j, dungeonY + i, dungeonZ + k);
					if (HelperActions.blockIsOneOf(block, HelperActions.mineableBlocks) || HelperActions.blockIsOneOf(block, HelperActions.dangerBlocks)) {
						int blockID = HelperActions.getBlockId(dungeonX + j, dungeonY + i, dungeonZ + k);
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
		
		ObjectInstance agent = new ObjectInstance(domain.getObjectClass(HelperNameSpace.CLASSAGENT), "agent0");
		agent.setValue(HelperNameSpace.ATX, curPos.x - dungeonX);
		agent.setValue(HelperNameSpace.ATY, curPos.y - dungeonY);
		agent.setValue(HelperNameSpace.ATZ, curPos.z - dungeonZ);
		agent.setValue(HelperNameSpace.ATROTDIR, rotateDirection);
		agent.setValue(HelperNameSpace.ATVERTDIR, rotateVertDirection);
		
		s.addObject(agent);
		
		return s;
	}
	
	public static int[][][] getMap(int dungeon) {
		
		if (dungeon == 1) {
			dungeonX = HandlerDungeonGeneration.finderX;
			dungeonY = HandlerDungeonGeneration.finderY;
			dungeonZ = HandlerDungeonGeneration.finderZ;
			length = finderLength;
			width = finderWidth;
			height = finderHeight;
		}
		else if (dungeon == 2) {
			dungeonX = HandlerDungeonGeneration.bridgeX;
			dungeonY = HandlerDungeonGeneration.bridgeY;
			dungeonZ = HandlerDungeonGeneration.bridgeZ;
			length = bridgeLength;
			width = bridgeWidth;
			height = bridgeHeight;
		}
		
		int[][][] map = new int[height][length][width];
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < length; j++) {
				for (int k = 0; k < width; k++) {
					Block block = HelperActions.getBlock(dungeonX + j, dungeonY + i, dungeonZ + k);
					if (!(HelperActions.blockIsOneOf(block, HelperActions.mineableBlocks) || HelperActions.blockIsOneOf(block, HelperActions.dangerBlocks))) {
						int blockID = HelperActions.getBlockId(dungeonX + j, dungeonY + i, dungeonZ + k);
						map[i][j][k] = blockID;
					}
				}
			}
		}
		
		return map;
	}
	
}

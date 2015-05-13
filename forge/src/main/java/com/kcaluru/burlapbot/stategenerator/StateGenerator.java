package com.kcaluru.burlapbot.stategenerator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

import com.kcaluru.burlapbot.BurlapWorldGenHandler;
import com.kcaluru.burlapbot.helpers.BurlapAIHelper;
import com.kcaluru.burlapbot.helpers.NameSpace;
import com.kcaluru.burlapbot.helpers.Pos;

import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectInstance;
import burlap.oomdp.core.State;

public class StateGenerator {
	
	private static int finderLength = 5;
	private static int finderWidth = 5;
	private static int finderHeight = 3;
	private static int bridgeLength = 4;
	private static int bridgeWidth = 5;
	private static int bridgeHeight = 4;
	private static int dungeonX;
	private static int dungeonY;
	private static int dungeonZ;
	private static int length;
	private static int width;
	private static int height;

	public static State getCurrentState(Domain domain, int dungeon) {
		
		if (dungeon == 1) {
			dungeonX = BurlapWorldGenHandler.finderX;
			dungeonY = BurlapWorldGenHandler.finderY;
			dungeonZ = BurlapWorldGenHandler.finderZ;
			length = finderLength;
			width = finderWidth;
			height = finderHeight;
		}
		else if (dungeon == 2) {
			dungeonX = BurlapWorldGenHandler.bridgeX;
			dungeonY = BurlapWorldGenHandler.bridgeY;
			dungeonZ = BurlapWorldGenHandler.bridgeZ;
			length = bridgeLength;
			width = bridgeWidth;
			height = bridgeHeight;
		}
		
		State s = new State();
		int blockCount = 0;
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < length; j++) {
				for (int k = 0; k < width; k++) {
					Block block = BurlapAIHelper.getBlock(dungeonX + j, dungeonY + i, dungeonZ + k);
					if (BurlapAIHelper.blockIsOneOf(block, BurlapAIHelper.mineableBlocks) || BurlapAIHelper.blockIsOneOf(block, BurlapAIHelper.dangerBlocks)) {
						int blockID = BurlapAIHelper.getBlockId(dungeonX + j, dungeonY + i, dungeonZ + k);
						blockCount += 1;
						ObjectInstance blockInstance = new ObjectInstance(domain.getObjectClass(NameSpace.CLASSBLOCK), "block" + blockCount);
						blockInstance.setValue(NameSpace.ATX, j);
						blockInstance.setValue(NameSpace.ATY, i);
						blockInstance.setValue(NameSpace.ATZ, k);
						blockInstance.setValue(NameSpace.ATBTYPE, blockID);
						
						s.addObject(blockInstance);
					}
				}
			}
		}

		Pos curPos = BurlapAIHelper.getPlayerPosition();
		int rotateDirection = BurlapAIHelper.getRotateDirection();
		int rotateVertDirection = BurlapAIHelper.getRotateVertDirection();
		
		ObjectInstance agent = new ObjectInstance(domain.getObjectClass(NameSpace.CLASSAGENT), "agent0");
		agent.setValue(NameSpace.ATX, curPos.x - dungeonX);
		agent.setValue(NameSpace.ATY, curPos.y - dungeonY);
		agent.setValue(NameSpace.ATZ, curPos.z - dungeonZ);
		agent.setValue(NameSpace.ATROTDIR, rotateDirection);
		agent.setValue(NameSpace.ATVERTDIR, rotateVertDirection);
		
		s.addObject(agent);
		
		return s;
	}
	
	public static int[][][] getMap(int dungeon) {
		
		if (dungeon == 1) {
			dungeonX = BurlapWorldGenHandler.finderX;
			dungeonY = BurlapWorldGenHandler.finderY;
			dungeonZ = BurlapWorldGenHandler.finderZ;
			length = finderLength;
			width = finderWidth;
			height = finderHeight;
		}
		else if (dungeon == 2) {
			dungeonX = BurlapWorldGenHandler.bridgeX;
			dungeonY = BurlapWorldGenHandler.bridgeY;
			dungeonZ = BurlapWorldGenHandler.bridgeZ;
			length = bridgeLength;
			width = bridgeWidth;
			height = bridgeHeight;
		}
		
		int[][][] map = new int[height][length][width];
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < length; j++) {
				for (int k = 0; k < width; k++) {
					Block block = BurlapAIHelper.getBlock(dungeonX + j, dungeonY + i, dungeonZ + k);
					if (!(BurlapAIHelper.blockIsOneOf(block, BurlapAIHelper.mineableBlocks) || BurlapAIHelper.blockIsOneOf(block, BurlapAIHelper.dangerBlocks))) {
						int blockID = BurlapAIHelper.getBlockId(dungeonX + j, dungeonY + i, dungeonZ + k);
						map[i][j][k] = blockID;
					}
				}
			}
		}
		
		return map;
	}
	
}

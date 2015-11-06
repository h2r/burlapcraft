package edu.brown.cs.h2r.burlapcraft.stategenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.objects.MutableObjectInstance;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.MutableState;
import burlap.oomdp.core.states.State;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;

public class StateGenerator {

	// tracking number of blocks to set blockIDs
	public static int blockCount = 0;
	public static HashMap<String, String> blockNameMap = new HashMap<String, String>();
	public static HashMap<Integer, ArrayList<String>> invBlockNameMap = new HashMap<Integer, ArrayList<String>>();

	private static Pose dungeonPose;
	private static int length;
	private static int width;
	private static int height;
	

	public static State getCurrentState(Domain domain, Dungeon d) {

		dungeonPose = d.getPose();
		length = d.getLength();
		width = d.getWidth();
		height = d.getHeight();

		State s = new MutableState();

		int xMaxBlue = -1;
		int xMinBlue = Integer.MAX_VALUE;
		int zMinBlue = Integer.MAX_VALUE;
		int zMaxBlue = -1;
		int xMaxOrange = -1;
		int xMinOrange = Integer.MAX_VALUE;
		int zMinOrange = Integer.MAX_VALUE;
		int zMaxOrange = -1;
		int xMaxRed = -1;
		int xMinRed = Integer.MAX_VALUE;
		int zMinRed = Integer.MAX_VALUE;
		int zMaxRed = -1;
		int xMaxGreen = -1;
		int xMinGreen = Integer.MAX_VALUE;
		int zMinGreen = Integer.MAX_VALUE;
		int zMaxGreen = -1;
		boolean fourRoomsFound = false;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < length; j++) {
				for (int k = 0; k < width; k++) {
					Block block = HelperActions.getBlock(
							dungeonPose.getX() + j, dungeonPose.getY() + i,
							dungeonPose.getZ() + k);

					if (d.getName().equals("fourrooms")
							|| d.getName().equals("cleanup")) {
						fourRoomsFound = true;
						if (block.equals(BurlapCraft.blueRock)) {
							if (j > xMaxBlue) {
								xMaxBlue = j;
							}
							if (j < xMinBlue) {
								xMinBlue = j;
							}
							if (k > zMaxBlue) {
								zMaxBlue = k;
							}
							if (k < zMinBlue) {
								zMinBlue = k;
							}
						} else if (block.equals(BurlapCraft.orangeRock)) {
							if (j > xMaxOrange) {
								xMaxOrange = j;
							}
							if (j < xMinOrange) {
								xMinOrange = j;
							}
							if (k > zMaxOrange) {
								zMaxOrange = k;
							}
							if (k < zMinOrange) {
								zMinOrange = k;
							}
						} else if (block.equals(BurlapCraft.greenRock)) {
							if (j > xMaxGreen) {
								xMaxGreen = j;
							}
							if (j < xMinGreen) {
								xMinGreen = j;
							}
							if (k > zMaxGreen) {
								zMaxGreen = k;
							}
							if (k < zMinGreen) {
								zMinGreen = k;
							}
						} else if (block.equals(BurlapCraft.redRock)) {
							if (j > xMaxRed) {
								xMaxRed = j;
							}
							if (j < xMinRed) {
								xMinRed = j;
							}
							if (k > zMaxRed) {
								zMaxRed = k;
							}
							if (k < zMinRed) {
								zMinRed = k;
							}
						}
					}

					if (HelperActions.blockIsOneOf(block,
							HelperActions.mineableBlocks)
							|| HelperActions.blockIsOneOf(block,
									HelperActions.dangerBlocks)) {
						int blockID = HelperActions.getBlockId(
								dungeonPose.getX() + j, dungeonPose.getY() + i,
								dungeonPose.getZ() + k);
						int keyX = (int) dungeonPose.getX() + j;
						int keyY = (int) dungeonPose.getY() + i;
						int keyZ = (int) dungeonPose.getZ() + k;
						String blockName;
						blockName = "block";
						String key = keyX + "," + keyY + "," + keyZ;
						if (blockNameMap.containsKey(key)) {
							blockName = blockNameMap.get(key);
						}
						else {
							blockName += blockCount;
							blockNameMap.put(key, blockName);
							blockCount += 1;
						}
						
						// Note: name block after its x, y, and z coordinates
						ObjectInstance blockInstance = new MutableObjectInstance(
								domain.getObjectClass(HelperNameSpace.CLASSBLOCK),
								blockName);
						blockInstance.setValue(HelperNameSpace.ATX, j);
						blockInstance.setValue(HelperNameSpace.ATY, i);
						blockInstance.setValue(HelperNameSpace.ATZ, k);
						blockInstance
								.setValue(HelperNameSpace.ATBTYPE, blockID);

						s.addObject(blockInstance);
					}
				}
			}

			if (fourRoomsFound) {
				if (xMaxBlue != -1 && xMinBlue != Integer.MAX_VALUE
						&& zMaxBlue != -1 && zMinBlue != Integer.MAX_VALUE) {
					ObjectInstance blueRoomInstance = new MutableObjectInstance(
							domain.getObjectClass(HelperNameSpace.CLASSROOM),
							"roomblue");
					blueRoomInstance.setValue(HelperNameSpace.ATXMAX, xMaxBlue);
					blueRoomInstance.setValue(HelperNameSpace.ATXMIN, xMinBlue);
					blueRoomInstance.setValue(HelperNameSpace.ATZMAX, zMaxBlue);
					blueRoomInstance.setValue(HelperNameSpace.ATZMIN, zMinBlue);
					blueRoomInstance.setValue(HelperNameSpace.ATCOLOR, "blue");
					s.addObject(blueRoomInstance);
				}
				if (xMaxRed != -1 && xMinRed != Integer.MAX_VALUE
						&& zMaxRed != -1 && zMinRed != Integer.MAX_VALUE) {
					ObjectInstance redRoomInstance = new MutableObjectInstance(
							domain.getObjectClass(HelperNameSpace.CLASSROOM),
							"roomred");
					redRoomInstance.setValue(HelperNameSpace.ATXMAX, xMaxRed);
					redRoomInstance.setValue(HelperNameSpace.ATXMIN, xMinRed);
					redRoomInstance.setValue(HelperNameSpace.ATZMAX, zMaxRed);
					redRoomInstance.setValue(HelperNameSpace.ATZMIN, zMinRed);
					redRoomInstance.setValue(HelperNameSpace.ATCOLOR, "red");
					s.addObject(redRoomInstance);
				}
				if (xMaxGreen != -1 && xMinGreen != Integer.MAX_VALUE
						&& zMaxGreen != -1 && zMinGreen != Integer.MAX_VALUE) {
					ObjectInstance greenRoomInstance = new MutableObjectInstance(
							domain.getObjectClass(HelperNameSpace.CLASSROOM),
							"roomgreen");
					greenRoomInstance.setValue(HelperNameSpace.ATXMAX,
							xMaxGreen);
					greenRoomInstance.setValue(HelperNameSpace.ATXMIN,
							xMinGreen);
					greenRoomInstance.setValue(HelperNameSpace.ATZMAX,
							zMaxGreen);
					greenRoomInstance.setValue(HelperNameSpace.ATZMIN,
							zMinGreen);
					greenRoomInstance
							.setValue(HelperNameSpace.ATCOLOR, "green");
					s.addObject(greenRoomInstance);
				}
				if (xMaxOrange != -1 && xMinOrange != Integer.MAX_VALUE
						&& zMaxOrange != -1 && zMinOrange != Integer.MAX_VALUE) {
					ObjectInstance orangeRoomInstance = new MutableObjectInstance(
							domain.getObjectClass(HelperNameSpace.CLASSROOM),
							"roomorange");
					orangeRoomInstance.setValue(HelperNameSpace.ATXMAX,
							xMaxOrange);
					orangeRoomInstance.setValue(HelperNameSpace.ATXMIN,
							xMinOrange);
					orangeRoomInstance.setValue(HelperNameSpace.ATZMAX,
							zMaxOrange);
					orangeRoomInstance.setValue(HelperNameSpace.ATZMIN,
							zMinOrange);
					orangeRoomInstance.setValue(HelperNameSpace.ATCOLOR,
							"orange");
					s.addObject(orangeRoomInstance);
				}
			}
		}

		HelperPos curPos = HelperActions.getPlayerPosition();
		int rotateDirection = HelperActions.getYawDirection();
		int rotateVertDirection = HelperActions.getPitchDirection();
		int selectedItemID = HelperActions.getCurrentItemID();
		System.out.println("Player position: " + curPos);
		System.out.println("Dungeon: " + dungeonPose);
		ObjectInstance agent = new MutableObjectInstance(
				domain.getObjectClass(HelperNameSpace.CLASSAGENT), "agent0");
		agent.setValue(HelperNameSpace.ATX, curPos.x - dungeonPose.getX());
		agent.setValue(HelperNameSpace.ATY, curPos.y - dungeonPose.getY());
		agent.setValue(HelperNameSpace.ATZ, curPos.z - dungeonPose.getZ());
		agent.setValue(HelperNameSpace.ATROTDIR, rotateDirection);
		agent.setValue(HelperNameSpace.ATVERTDIR, rotateVertDirection);
		agent.setValue(HelperNameSpace.ATSELECTEDITEMID, selectedItemID);
		
		List<ObjectInstance> invBlocks = s.getObjectsOfClass(HelperNameSpace.CLASSINVENTORYBLOCK);
		int invBlockCount = invBlocks.size();

		Map<String, Integer> items = HelperActions.checkInventory();
		HashMap<String, Integer> blockMap = new HashMap<String, Integer>();
		for (Block block : HelperActions.mineableBlocks) {
			blockMap.put(block.getUnlocalizedName(), block.getIdFromBlock(block));
		}
		for (Map.Entry<String, Integer> i : items.entrySet()) {
			for (String name : blockMap.keySet()) {
				if (i.getKey().equals(name)) {
					ObjectInstance o = new MutableObjectInstance(
							domain.getObjectClass(HelperNameSpace.CLASSINVENTORYBLOCK),
							"inventoryBlock" + invBlockCount);
					Integer key = blockMap.get(name);
					if (invBlockNameMap.containsKey(key)) {
						for (String invBlockName : invBlockNameMap.get(key)) {
							o.addRelationalTarget(HelperNameSpace.ATBLOCKNAMES, invBlockName);
						}
					}
					else {
						o.addRelationalTarget(HelperNameSpace.ATBLOCKNAMES, "block" + blockCount);
						blockCount++;
					}
					
					o.setValue(HelperNameSpace.ATBTYPE, blockMap.get(name));
					s.addObject(o);
				}
			}
		}

		s.addObject(agent);
		validate(s);
		return s;
	}

	public static void validate(State s) {

		ObjectInstance agent = s
				.getFirstObjectOfClass(HelperNameSpace.CLASSAGENT);
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

		Pose dungeonPose = dungeon.getPose();
		int length = dungeon.getLength();
		int width = dungeon.getWidth();
		int height = dungeon.getHeight();

		int[][][] map = new int[height][length][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < length; j++) {
				for (int k = 0; k < width; k++) {
					Block block = HelperActions.getBlock(
							dungeonPose.getX() + j, dungeonPose.getY() + i,
							dungeonPose.getZ() + k);
					if (!(HelperActions.blockIsOneOf(block,
							HelperActions.mineableBlocks) || HelperActions
							.blockIsOneOf(block, HelperActions.dangerBlocks))) {
						int blockID = HelperActions.getBlockId(
								dungeonPose.getX() + j, dungeonPose.getY() + i,
								dungeonPose.getZ() + k);
						map[i][j][k] = blockID;
					}
				}
			}
		}
		return map;
	}

}

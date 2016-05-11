package edu.brown.cs.h2r.burlapcraft.stategenerator;

import burlap.mdp.core.Domain;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.generic.GenericOOState;
import burlap.mdp.core.state.State;
import edu.brown.cs.h2r.burlapcraft.dungeongenerator.Dungeon;
import edu.brown.cs.h2r.burlapcraft.helper.HelperActions;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;
import edu.brown.cs.h2r.burlapcraft.helper.HelperNameSpace;
import edu.brown.cs.h2r.burlapcraft.helper.HelperPos;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.HashMap;

public class StateGenerator {

	// tracking number of blocks to set blockIDs
	public static int blockCount = 0;
	public static HashMap<String, String> blockNameMap = new HashMap<String, String>();
	public static HashMap<Integer, ArrayList<String>> invBlockNameMap = new HashMap<Integer, ArrayList<String>>();

	private static Pose dungeonPose;
	private static int length;
	private static int width;
	private static int height;

	private static int [][][] lastMap = null;
	private static Dungeon lastMapDungeon = null;
	

	public static State getCurrentState(Domain domain, Dungeon d) {

		dungeonPose = d.getPose();
		length = d.getLength();
		width = d.getWidth();
		height = d.getHeight();

		GenericOOState s = new GenericOOState();

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < length; j++) {
				for (int k = 0; k < width; k++) {
					Block block = HelperActions.getBlock(
							dungeonPose.getX() + j, dungeonPose.getY() + i,
							dungeonPose.getZ() + k);

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

						BCBlock blockInstance = new BCBlock(j, i, k, blockID, blockName);
						s.addObject(blockInstance);
					}
				}
			}

		}

		HelperPos curPos = HelperActions.getPlayerPosition();
		int rotateDirection = HelperActions.getYawDirection();
		int rotateVertDirection = HelperActions.getPitchDirection();
		int selectedItemID = HelperActions.getCurrentItemID();
		System.out.println("Player position: " + curPos);
		System.out.println("Dungeon: " + dungeonPose);

		BCAgent agent = new BCAgent(
				(int)(curPos.x - dungeonPose.getX()),
				(int)(curPos.y - dungeonPose.getY()),
				(int)(curPos.z - dungeonPose.getZ()),
				rotateDirection,
				rotateVertDirection,
				selectedItemID);

		
//		List<ObjectInstance> invBlocks = s.getObjectsOfClass(HelperNameSpace.CLASS_INVENTORY_BLOCK);
//		int invBlockCount = invBlocks.size();
//
//		Map<String, Integer> items = HelperActions.checkInventory();
//		HashMap<String, Integer> blockMap = new HashMap<String, Integer>();
//		for (Block block : HelperActions.mineableBlocks) {
//			blockMap.put(block.getUnlocalizedName(), block.getIdFromBlock(block));
//		}
//		for (Map.Entry<String, Integer> i : items.entrySet()) {
//			for (String name : blockMap.keySet()) {
//				if (i.getKey().equals(name)) {
//					ObjectInstance o = new MutableObjectInstance(
//							domain.getObjectClass(HelperNameSpace.CLASS_INVENTORY_BLOCK),
//							"inventoryBlock" + invBlockCount);
//					Integer key = blockMap.get(name);
//					if (invBlockNameMap.containsKey(key)) {
//						for (String invBlockName : invBlockNameMap.get(key)) {
//							o.addRelationalTarget(HelperNameSpace.VAR_BLOCK_NAMES, invBlockName);
//						}
//					}
//					else {
//						o.addRelationalTarget(HelperNameSpace.VAR_BLOCK_NAMES, "block" + blockCount);
//						blockCount++;
//					}
//
//					o.setValue(HelperNameSpace.VAR_BT, blockMap.get(name));
//					s.addObject(o);
//				}
//			}
//		}

		s.addObject(agent);


		BCMap map = new BCMap(getMap(d));
		s.addObject(map);

		validate(s);
		return s;
	}

	public static void validate(State s) {

		BCAgent a = (BCAgent)((OOState)s).object(HelperNameSpace.CLASS_AGENT);


		if (a.x < 0) {
			throw new IllegalStateException("Invalid agent x: " + a.x);
		}
		if (a.y < 0) {
			throw new IllegalStateException("Invalid agent y: " + a.y);
		}
		if (a.z < 0) {
			throw new IllegalStateException("Invalid agent z: " + a.z);
		}

	}

	public static int[][][] getMap(Dungeon dungeon) {

		if(lastMapDungeon == dungeon){
			return lastMap;
		}

		System.out.println("Generating map");

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

		lastMapDungeon = dungeon;
		lastMap = map;

		return map;
	}

}

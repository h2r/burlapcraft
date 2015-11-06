package edu.brown.cs.h2r.burlapcraft.helper;

import java.util.*;

import burlap.oomdp.singleagent.GroundedAction;
//import net.famzangl.minecraft.minebot.ai.command.AIChatController;
//import net.famzangl.minecraft.minebot.ai.task.AITask;
//import net.famzangl.minecraft.minebot.build.BuildManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.handler.HandlerEvents;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;

public class HelperActions {

	// minecraft and player
	private static Minecraft mc = Minecraft.getMinecraft();

	// click interactions
	private static MovementInput resetMovementInput;
	private static KeyBinding resetAttackKey;
	private static KeyBinding resetUseItemKey;
	private static boolean useItemKeyJustPressed;
	private static boolean attackKeyJustPressed;

	// categories of blocks
	public static final Block[] mineableBlocks = { BurlapCraft.burlapStone,
			Blocks.gold_block, BurlapCraft.mineableBlueRock,
			BurlapCraft.mineableGreenRock, BurlapCraft.mineableOrangeRock,
			BurlapCraft.mineableRedRock };
	
	// danger blocks
	public static final Block[] dangerBlocks = { Blocks.lava };

	// blocks that can be placed in
	public static final Block[] placeInBlocks = { Blocks.lava, Blocks.air };
	
	// blocks that cannot be mined
	public static final Block[] unbreakableBlocks = { Blocks.bedrock, BurlapCraft.redRock, BurlapCraft.blueRock,
		BurlapCraft.greenRock, BurlapCraft.orangeRock };

	// regular blocks
	public static final Block[] normalBlocks = { Blocks.bedrock,
			Blocks.bookshelf, Blocks.brick_block, Blocks.brown_mushroom_block,
			Blocks.cake, Blocks.coal_block, Blocks.coal_ore,
			Blocks.cobblestone, Blocks.crafting_table, Blocks.diamond_block,
			Blocks.diamond_ore, Blocks.dirt, Blocks.double_stone_slab,
			Blocks.double_wooden_slab, Blocks.emerald_block,
			Blocks.emerald_ore, Blocks.farmland, Blocks.glass,
			Blocks.glowstone, Blocks.grass, Blocks.gold_block, Blocks.gold_ore,
			Blocks.hardened_clay, Blocks.iron_block, Blocks.iron_ore,
			Blocks.lapis_block, Blocks.lapis_ore, Blocks.leaves,
			Blocks.leaves2, Blocks.log, Blocks.log2, Blocks.melon_block,
			Blocks.mossy_cobblestone, Blocks.mycelium, Blocks.nether_brick,
			Blocks.nether_brick_fence, Blocks.netherrack, Blocks.obsidian,
			Blocks.packed_ice, Blocks.planks, Blocks.pumpkin,
			Blocks.quartz_block, Blocks.quartz_ore, Blocks.red_mushroom_block,
			Blocks.redstone_block, Blocks.redstone_lamp, Blocks.redstone_ore,
			Blocks.sandstone, Blocks.snow, Blocks.soul_sand,
			Blocks.stained_glass, Blocks.stained_hardened_clay, Blocks.stone,
			Blocks.stonebrick, Blocks.web, Blocks.wool };

	// blocks that fall
	public static final Block[] fallingBlocks = { Blocks.gravel, Blocks.sand, Blocks.lava };

	// stair blocks
	public static final Block[] stairBlocks = { Blocks.acacia_stairs,
			Blocks.birch_stairs, Blocks.brick_stairs, Blocks.dark_oak_stairs,
			Blocks.jungle_stairs, Blocks.nether_brick_stairs,
			Blocks.oak_stairs, Blocks.sandstone_stairs, Blocks.spruce_stairs,
			Blocks.stone_brick_stairs, Blocks.stone_stairs, Blocks.stone_slab,
			Blocks.wooden_slab, Blocks.quartz_stairs };

	// blocks that can be walked on
	public static final Block[] walkableBlocks = { Blocks.tallgrass,
			Blocks.yellow_flower, Blocks.red_flower, Blocks.wheat,
			Blocks.carrots, Blocks.potatoes, Blocks.pumpkin_stem,
			Blocks.melon_stem, Blocks.torch, Blocks.carpet, Blocks.golden_rail,
			Blocks.detector_rail, Blocks.rail, Blocks.activator_rail,
			Blocks.double_plant, Blocks.red_mushroom, Blocks.brown_mushroom,
			Blocks.redstone_wire };

	// safe side blocks
	public static final Block[] safeSideBlocks = { Blocks.fence,
			Blocks.fence_gate, Blocks.cobblestone_wall, Blocks.cactus,
			Blocks.reeds };

	// all blocks
	public static final Block[] allBlocks = { Blocks.bedrock, Blocks.bookshelf,
			Blocks.brick_block, Blocks.brown_mushroom_block, Blocks.cake,
			Blocks.coal_block, Blocks.coal_ore, Blocks.cobblestone,
			Blocks.crafting_table, Blocks.diamond_block, Blocks.diamond_ore,
			Blocks.dirt, Blocks.double_stone_slab, Blocks.double_wooden_slab,
			Blocks.emerald_block, Blocks.emerald_ore, Blocks.farmland,
			Blocks.glass, Blocks.glowstone, Blocks.grass, Blocks.gold_block,
			Blocks.gold_ore, Blocks.hardened_clay, Blocks.iron_block,
			Blocks.iron_ore, Blocks.lapis_block, Blocks.lapis_ore,
			Blocks.leaves, Blocks.leaves2, Blocks.log, Blocks.log2,
			Blocks.melon_block, Blocks.mossy_cobblestone, Blocks.mycelium,
			Blocks.nether_brick, Blocks.nether_brick_fence, Blocks.netherrack,
			Blocks.obsidian, Blocks.packed_ice, Blocks.planks, Blocks.pumpkin,
			Blocks.quartz_block, Blocks.quartz_ore, Blocks.red_mushroom_block,
			Blocks.redstone_block, Blocks.redstone_lamp, Blocks.redstone_ore,
			Blocks.sandstone, Blocks.snow, Blocks.soul_sand,
			Blocks.stained_glass, Blocks.stained_hardened_clay, Blocks.stone,
			Blocks.stonebrick, Blocks.web, Blocks.wool, Blocks.gravel,
			Blocks.sand, Blocks.acacia_stairs, Blocks.birch_stairs,
			Blocks.brick_stairs, Blocks.dark_oak_stairs, Blocks.jungle_stairs,
			Blocks.nether_brick_stairs, Blocks.oak_stairs,
			Blocks.sandstone_stairs, Blocks.spruce_stairs,
			Blocks.stone_brick_stairs, Blocks.stone_stairs, Blocks.stone_slab,
			Blocks.wooden_slab, Blocks.quartz_stairs, Blocks.tallgrass,
			Blocks.yellow_flower, Blocks.red_flower, Blocks.wheat,
			Blocks.carrots, Blocks.potatoes, Blocks.pumpkin_stem,
			Blocks.melon_stem, Blocks.torch, Blocks.carpet, Blocks.golden_rail,
			Blocks.detector_rail, Blocks.rail, Blocks.activator_rail,
			Blocks.double_plant, Blocks.red_mushroom, Blocks.brown_mushroom,
			Blocks.redstone_wire, Blocks.fence, Blocks.fence_gate,
			Blocks.cobblestone_wall, Blocks.cactus, Blocks.reeds };
	
    public static final Map<Integer, String> blockColorMap;
    static
    {
        blockColorMap = new HashMap<Integer, String>();
        blockColorMap.put(176, "red");
    	blockColorMap.put(177, "green");
    	blockColorMap.put(178, "blue");
    	blockColorMap.put(179, "orange");
    }
    
    public static final Map<Integer, String> blockShapeMap;
    static
    {
    	blockShapeMap = new HashMap<Integer, String>();
    	blockShapeMap.put(176, "chair");
    	blockShapeMap.put(177, "bag");
    	blockShapeMap.put(178, "chair");
    	blockShapeMap.put(179, "bag");
    }
	

	public static Minecraft getMinecraft() {
		return mc;
	}

	public static HelperPos getPlayerPosition() {
		int x = MathHelper.floor_double(getMinecraft().thePlayer.posX);
		int y = MathHelper
				.floor_double(getMinecraft().thePlayer.boundingBox.minY + 0.05D);

		int z = MathHelper.floor_double(getMinecraft().thePlayer.posZ);
		return new HelperPos(x, y, z);
	}

	public static Block getBlock(int x, int y, int z) {
		return mc.theWorld.getBlock(x, y, z);
	}

	public static Block getBlock(double x, double y, double z) {
		return getBlock((int) x, (int) y, (int) z);
	}

	public static Block getBlock(HelperPos pos) {
		return getBlock(pos.x, pos.y, pos.z);
	}

	public static void setPlayerPosition(EntityPlayer player, Pose position) {
		player.setPositionAndUpdate(position.getX(), position.getY(),
				position.getZ());
		Chunk chunk = player.getEntityWorld().getChunkFromBlockCoords(
				(int) position.getX(), (int) position.getZ());
	}

	public static void overrideMovement(MovementInput i) {
		if (resetMovementInput == null) {
			resetMovementInput = mc.thePlayer.movementInput;
		}
		mc.thePlayer.movementInput = i;
	}

	public static void overrideUseItem() {
		if (resetUseItemKey == null) {
			resetUseItemKey = mc.gameSettings.keyBindUseItem;
		}
		mc.gameSettings.keyBindUseItem = new HelperClickInteractions(
				mc.gameSettings.keyBindUseItem.getKeyDescription(), 501,
				mc.gameSettings.keyBindUseItem.getKeyCategory(),
				useItemKeyJustPressed);
	}

	public static void overrideAttack() {
		if (resetAttackKey == null) {
			resetAttackKey = mc.gameSettings.keyBindAttack;
		}
		mc.gameSettings.keyBindAttack = new HelperClickInteractions(
				mc.gameSettings.keyBindAttack.getKeyDescription(), 502,
				mc.gameSettings.keyBindAttack.getKeyCategory(),
				attackKeyJustPressed);
	}

	public static void resetAllInputs() {
		if (resetMovementInput != null) {
			mc.thePlayer.movementInput = resetMovementInput;
		}
		attackKeyJustPressed = (resetAttackKey != null);
		if (resetAttackKey != null) {
			mc.gameSettings.keyBindAttack = resetAttackKey;
		}
		useItemKeyJustPressed = (resetAttackKey != null);
		if (resetUseItemKey != null) {
			mc.gameSettings.keyBindUseItem = resetUseItemKey;
		}
	}

	public static boolean blockIsOneOf(Block needle, Block[] haystack) {
		for (Block h : haystack) {
			if (Block.isEqualTo(needle, h)) {
				return true;
			}
		}
		return false;
	}

	public static int getBlockId(double x, double y, double z) {
		return getBlockId((int) x, (int) y, (int) z);
	}

	public static int getBlockId(int x, int y, int z) {
		Chunk chunk = mc.theWorld.getChunkFromChunkCoords(x >> 4, z >> 4);
		chunk.getBlock(x & 0xF, y, z & 0xF);
		int blockId = 0;

		ExtendedBlockStorage[] sa = chunk.getBlockStorageArray();
		if (y >> 4 < sa.length) {
			ExtendedBlockStorage extendedblockstorage = sa[(y >> 4)];

			if (extendedblockstorage != null) {
				int lx = x & 0xF;
				int ly = y & 0xF;
				int lz = z & 0xF;
				blockId = extendedblockstorage.getBlockLSBArray()[(ly << 8
						| lz << 4 | lx)] & 0xFF;
				NibbleArray blockMSBArray = extendedblockstorage
						.getBlockMSBArray();

				if (blockMSBArray != null) {
					blockId |= blockMSBArray.get(lx, ly, lz) << 8;
				}
			}
		}

		return blockId;
	}

	public static void snapToGrid() {
		EntityPlayer player = mc.thePlayer;
		HelperPos oldPosition = HelperActions.getPlayerPosition();
		// HelperPos holds ints now but this code will port when the rest is
		// switched to doubles...
		Pose newPosition = Pose.fromXyz(Math.floor(oldPosition.x) + 0.5,
				Math.floor(oldPosition.y), Math.floor(oldPosition.z) + 0.5);
		HelperActions.setPlayerPosition(player, newPosition);
	}

	public static boolean moveForward(boolean jump) {
		MovementInput movement = new MovementInput();
		movement.moveForward = (float) 0.55D;
		movement.jump = jump;

		snapToGrid();

		overrideMovement(movement);

		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				resetAllInputs();
				timer.cancel();
			}
		}, 440, 10);

		return true;
	}

	final private static double qDecay = 90.0;
	final private static double snapThresh = 0.025;
	final private static double minUpdate = 0.025;
	final private static long timerPeriod = 2;
	final private static long maxUpdates = 1000;

	public static void moveYawToTarget(final double yawTarget) {
		final EntityPlayer player = mc.thePlayer;
		// consider snapping to range -360 to 360 in here to avoid massive
		// unwinding...
		final Timer timer = new Timer();
		final ArrayList<Integer> numberOfIterations = new ArrayList<Integer>(1);
		numberOfIterations.add(0);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (player.rotationYaw == yawTarget) {
					System.out.println("moveYawToTarget: target reached.");
					timer.cancel();
				} else if (Math.abs(yawTarget - player.rotationYaw) > snapThresh) {
					double update = (yawTarget - player.rotationYaw) / qDecay;
					if (Math.abs(update) < minUpdate) {
						update = Math.signum(update) * minUpdate;
					} else {
						// do nothing
					}
					player.rotationYaw += update;
					int tmp = numberOfIterations.get(0);
					numberOfIterations.set(0, tmp + 1);
					// System.out.println("smoothMove: target = " + yawTarget +
					// " current yaw = " + player.rotationYaw + ", update = " +
					// update + ", performed " + numberOfIterations.get(0) +
					// " iterations.");
					if (tmp >= maxUpdates) {
						timer.cancel();
					} else {
						// do nothing
					}
				} else {
					player.rotationYaw = (float) yawTarget;
				}
			}
		}, 0, timerPeriod);
	}

	public static void movePitchToTarget(final double pitchTarget) {
		final EntityPlayer player = mc.thePlayer;
		final Timer timer = new Timer();
		final ArrayList<Integer> numberOfIterations = new ArrayList<Integer>(1);
		numberOfIterations.add(0);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (player.rotationPitch == pitchTarget) {
					System.out.println("movePitchToTarget: target reached.");
					timer.cancel();
				} else if (Math.abs(pitchTarget - player.rotationPitch) > snapThresh) {
					double update = (pitchTarget - player.rotationPitch)
							/ qDecay;
					if (Math.abs(update) < minUpdate) {
						update = Math.signum(update) * minUpdate;
					} else {
						// do nothing
					}
					player.rotationPitch += update;
					int tmp = numberOfIterations.get(0);
					numberOfIterations.set(0, tmp + 1);
					// System.out.println("smoothMove: target = " + pitchTarget
					// + " current pitch = " + player.rotationPitch +
					// ", update = " + update + ", performed " +
					// numberOfIterations.get(0) + " iterations.");
					if (tmp >= maxUpdates) {
						timer.cancel();
					} else {
						// do nothing
					}
				} else {
					player.rotationPitch = (float) pitchTarget;
				}
			}
		}, 0, timerPeriod);
	}

	public static void faceSouth() {
		// mc.thePlayer.rotationYaw = 0;

		// this is right on the branch cut so we need to be careful
		if (mc.thePlayer.rotationYaw < -180) {
			mc.thePlayer.rotationYaw += 360;
		} else if (mc.thePlayer.rotationYaw > 180) {
			mc.thePlayer.rotationYaw -= 360;
		} else {
			// do nothing
		}
		moveYawToTarget(0);
	}

	public static void faceWest() {
		// mc.thePlayer.rotationYaw = 90;
		if (mc.thePlayer.rotationYaw <= -90) {
			moveYawToTarget(-270);
		} else {
			moveYawToTarget(90);
		}
	}

	public static void faceNorth() {
		// mc.thePlayer.rotationYaw = -180;
		if (mc.thePlayer.rotationYaw <= 0) {
			moveYawToTarget(-180);
		} else {
			moveYawToTarget(180);
		}
	}

	public static void faceEast() {
		// mc.thePlayer.rotationYaw = -90;
		if (mc.thePlayer.rotationYaw <= 90) {
			moveYawToTarget(-90);
		} else {
			moveYawToTarget(270);
		}
	}

	public static int getYawDirection() {
		return (MathHelper
				.floor_double(getMinecraft().thePlayer.rotationYaw / 360.0F * 4.0F + 0.5D) & 0x3);
	}

	public static void faceDownOne() {
		// mc.thePlayer.rotationPitch = (float) 57;
		movePitchToTarget(57);
	}

	public static void faceDownTwo() {
		// mc.thePlayer.rotationPitch = (float) 49.5;
		movePitchToTarget(49.5);
	}

	public static void faceDownThree() {
		// mc.thePlayer.rotationPitch = (float) 32.5;
		movePitchToTarget(32.5);
	}

	public static void faceAhead() {
		// mc.thePlayer.rotationPitch = 0;
		movePitchToTarget(0);
	}

	public static int getPitchDirection() {
		float rotPitch = mc.thePlayer.rotationPitch;
		if (rotPitch <= 30) {
			return 0;
		} else {
			return 1;
		}
	}

	public static void placeBlock() {
		if (mc.thePlayer.inventory.getCurrentItem() != null) {
			overrideUseItem();
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					resetAllInputs();
					timer.cancel();
				}
			}, 200, 10);
		}
	}

	public static void destroyBlock() {
		int count = 9;
		for (int i = 0; i < count; i++) {
			if (mc.thePlayer.inventory.getCurrentItem() != null) {
				if (mc.thePlayer.getCurrentEquippedItem().getUnlocalizedName()
						.equals("item.pickaxeDiamond")) {
					overrideAttack();
					final Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							resetAllInputs();
							timer.cancel();
						}
					}, 200, 10);
					break;
				}
			}
			mc.thePlayer.inventory.changeCurrentItem(-1);
		}
	}

	public static Map<String, Integer> checkInventory() {

		Map<String, Integer> itemSets = new HashMap<String, Integer>();

		ItemStack[] items = mc.thePlayer.inventory.mainInventory;
		for (ItemStack i : items) {
			if (i != null) {
				itemSets.put(i.getUnlocalizedName(), i.stackSize);
			}
		}

		return itemSets;

	}
	
	public static int changeItem() {
		mc.thePlayer.inventory.changeCurrentItem(-1);
		if (mc.thePlayer.inventory.getCurrentItem() != null) {
			return getCurrentItemID();
		}
		return -1;
	}
	
	public static int getCurrentItemID() {
		if (mc.thePlayer.inventory.getCurrentItem() != null) {
			return Item.getIdFromItem(mc.thePlayer.inventory.getCurrentItem().getItem());
		}
		return -1;
	}

}
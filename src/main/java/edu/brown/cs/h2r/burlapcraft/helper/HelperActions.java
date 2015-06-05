package edu.brown.cs.h2r.burlapcraft.helper;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


//import net.famzangl.minecraft.minebot.ai.command.AIChatController;
//import net.famzangl.minecraft.minebot.ai.task.AITask;
//import net.famzangl.minecraft.minebot.build.BuildManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import edu.brown.cs.h2r.burlapcraft.helper.HelperGeometry.Pose;

public class HelperActions {
  private static Minecraft mc = Minecraft.getMinecraft();
  private final static Random rand = new Random();
  
  public static final Block[] mineableBlocks = { BurlapCraft.burlapStone, Blocks.gold_block };
  
  public static final Block[] dangerBlocks = { Blocks.lava };
  
  public static final Block[] placeOnBlocks = { Blocks.lava, Blocks.air };
  
  public static final Block[] normalBlocks = { Blocks.bedrock, Blocks.bookshelf, Blocks.brick_block, Blocks.brown_mushroom_block, Blocks.cake, Blocks.coal_block, Blocks.coal_ore, Blocks.cobblestone, Blocks.crafting_table, Blocks.diamond_block, Blocks.diamond_ore, Blocks.dirt, Blocks.double_stone_slab, Blocks.double_wooden_slab, Blocks.emerald_block, Blocks.emerald_ore, Blocks.farmland, Blocks.glass, Blocks.glowstone, Blocks.grass, Blocks.gold_block, Blocks.gold_ore, Blocks.hardened_clay, Blocks.iron_block, Blocks.iron_ore, Blocks.lapis_block, Blocks.lapis_ore, Blocks.leaves, Blocks.leaves2, Blocks.log, Blocks.log2, Blocks.melon_block, Blocks.mossy_cobblestone, Blocks.mycelium, Blocks.nether_brick, Blocks.nether_brick_fence, Blocks.netherrack, Blocks.obsidian, Blocks.packed_ice, Blocks.planks, Blocks.pumpkin, Blocks.quartz_block, Blocks.quartz_ore, Blocks.red_mushroom_block, Blocks.redstone_block, Blocks.redstone_lamp, Blocks.redstone_ore, Blocks.sandstone, Blocks.snow, Blocks.soul_sand, Blocks.stained_glass, Blocks.stained_hardened_clay, Blocks.stone, Blocks.stonebrick, Blocks.web, Blocks.wool };

  public static final Block[] fallingBlocks = { Blocks.gravel, Blocks.sand };

  public static final Block[] stairBlocks = { Blocks.acacia_stairs, Blocks.birch_stairs, Blocks.brick_stairs, Blocks.dark_oak_stairs, Blocks.jungle_stairs, Blocks.nether_brick_stairs, Blocks.oak_stairs, Blocks.sandstone_stairs, Blocks.spruce_stairs, Blocks.stone_brick_stairs, Blocks.stone_stairs, Blocks.stone_slab, Blocks.wooden_slab, Blocks.quartz_stairs };

  public static final Block[] walkableBlocks = { Blocks.tallgrass, Blocks.yellow_flower, Blocks.red_flower, Blocks.wheat, Blocks.carrots, Blocks.potatoes, Blocks.pumpkin_stem, Blocks.melon_stem, Blocks.torch, Blocks.carpet, Blocks.golden_rail, Blocks.detector_rail, Blocks.rail, Blocks.activator_rail, Blocks.double_plant, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.redstone_wire };

  public static final Block[] safeSideBlocks = { Blocks.fence, Blocks.fence_gate, Blocks.cobblestone_wall, Blocks.cactus, Blocks.reeds };
  
  public static final Block[] allBlocks = { Blocks.bedrock, Blocks.bookshelf, Blocks.brick_block, Blocks.brown_mushroom_block, Blocks.cake, Blocks.coal_block, Blocks.coal_ore, Blocks.cobblestone, Blocks.crafting_table, Blocks.diamond_block, Blocks.diamond_ore, Blocks.dirt, Blocks.double_stone_slab, Blocks.double_wooden_slab, Blocks.emerald_block, Blocks.emerald_ore, Blocks.farmland, Blocks.glass, Blocks.glowstone, Blocks.grass, Blocks.gold_block, Blocks.gold_ore, Blocks.hardened_clay, Blocks.iron_block, Blocks.iron_ore, Blocks.lapis_block, Blocks.lapis_ore, Blocks.leaves, Blocks.leaves2, Blocks.log, Blocks.log2, Blocks.melon_block, Blocks.mossy_cobblestone, Blocks.mycelium, Blocks.nether_brick, Blocks.nether_brick_fence, Blocks.netherrack, Blocks.obsidian, Blocks.packed_ice, Blocks.planks, Blocks.pumpkin, Blocks.quartz_block, Blocks.quartz_ore, Blocks.red_mushroom_block, Blocks.redstone_block, Blocks.redstone_lamp, Blocks.redstone_ore, Blocks.sandstone, Blocks.snow, Blocks.soul_sand, Blocks.stained_glass, Blocks.stained_hardened_clay, Blocks.stone, Blocks.stonebrick, Blocks.web, Blocks.wool, Blocks.gravel, Blocks.sand, Blocks.acacia_stairs, Blocks.birch_stairs, Blocks.brick_stairs, Blocks.dark_oak_stairs, Blocks.jungle_stairs, Blocks.nether_brick_stairs, Blocks.oak_stairs, Blocks.sandstone_stairs, Blocks.spruce_stairs, Blocks.stone_brick_stairs, Blocks.stone_stairs, Blocks.stone_slab, Blocks.wooden_slab, Blocks.quartz_stairs, Blocks.tallgrass, Blocks.yellow_flower, Blocks.red_flower, Blocks.wheat, Blocks.carrots, Blocks.potatoes, Blocks.pumpkin_stem, Blocks.melon_stem, Blocks.torch, Blocks.carpet, Blocks.golden_rail, Blocks.detector_rail, Blocks.rail, Blocks.activator_rail, Blocks.double_plant, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.redstone_wire, Blocks.fence, Blocks.fence_gate, Blocks.cobblestone_wall, Blocks.cactus, Blocks.reeds };
  
  private static MovementInput resetMovementInput;
  private static KeyBinding resetAttackKey;
  private static KeyBinding resetUseItemKey;
  private static boolean useItemKeyJustPressed;
  private static boolean attackKeyJustPressed;

  public static Minecraft getMinecraft()
  {
    return mc;
  }

  public static Block getBlock(int x, int y, int z)
  {
    return mc.theWorld.getBlock(x, y, z);
  }
  public static Block getBlock(double x, double y, double z)
  {
    return getBlock((int) x, (int) y, (int) z);
  }

  public static Block getBlock(HelperPos pos)
  {
    return getBlock(pos.x, pos.y, pos.z);
  }

  public static HelperPos getPlayerPosition()
  {
    int x = MathHelper.floor_double(getMinecraft().thePlayer.posX);
    int y = MathHelper.floor_double(getMinecraft().thePlayer.boundingBox.minY + 0.05D);

    int z = MathHelper.floor_double(getMinecraft().thePlayer.posZ);
    return new HelperPos(x, y, z);
  }
  
  public static void setPlayerPosition(EntityPlayer player, Pose position) {
	  Chunk chunk = player.getEntityWorld().getChunkFromBlockCoords((int) position.getX(), (int) position.getZ());
	  player.getEntityWorld().getChunkProvider().loadChunk((int) position.getX(), (int) position.getZ());
	  
	  for (int ix = -10; ix < 10; ix ++) {
		  for (int iy = -10; iy < 10; iy ++) {
			  for (int iz = -10; iz < 10; iz ++) {
				  player.getEntityWorld().markBlockForUpdate((int) position.getX() + ix, (int) position.getY() + iy, (int) position.getZ() + iz);
			  }
		  }
		  
	  }
	  player.setPositionAndUpdate(position.getX(), position.getY(), position.getZ());
  }

  public static void overrideMovement(MovementInput i)
  {
    if (resetMovementInput == null) {
      resetMovementInput = mc.thePlayer.movementInput;
    }
    mc.thePlayer.movementInput = i;
  }

  public static void overrideUseItem()
  {
    if (resetUseItemKey == null) {
      resetUseItemKey = mc.gameSettings.keyBindUseItem;
    }
    mc.gameSettings.keyBindUseItem = new HelperClickInteractions(mc.gameSettings.keyBindUseItem.getKeyDescription(), 501, mc.gameSettings.keyBindUseItem.getKeyCategory(), useItemKeyJustPressed);
  }

  public static void overrideAttack()
  {
    if (resetAttackKey == null) {
      resetAttackKey = mc.gameSettings.keyBindAttack;
    }
    mc.gameSettings.keyBindAttack = new HelperClickInteractions(mc.gameSettings.keyBindAttack.getKeyDescription(), 502, mc.gameSettings.keyBindAttack.getKeyCategory(), attackKeyJustPressed);
  }

  public static void resetAllInputs()
  {
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

  public static boolean blockIsOneOf(Block needle, Block[] haystack)
  {
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
  

  public static int getBlockId(int x, int y, int z)
  {
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

        blockId = extendedblockstorage.getBlockLSBArray()[(ly << 8 | lz << 4 | lx)] & 0xFF;

        NibbleArray blockMSBArray = extendedblockstorage.getBlockMSBArray();

        if (blockMSBArray != null) {
          blockId |= blockMSBArray.get(lx, ly, lz) << 8;
        }
      }
    }

    return blockId;
  }
  
  public static boolean moveForward(boolean jump) {
	  MovementInput movement = new MovementInput();
	  movement.moveForward = (float) 0.55D;
	  movement.jump = jump;
	  
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
  
  public static void faceSouth() {
	  mc.thePlayer.rotationYaw = 0;
  }
  
  public static void faceWest() {
	  mc.thePlayer.rotationYaw = 90;
  }
  
  public static void faceNorth() {
	  mc.thePlayer.rotationYaw = -180;
  }
  
  public static void faceEast() {
	  mc.thePlayer.rotationYaw = -90;
  }
  
  public static int getYawDirection()
  {
	  return (MathHelper.floor_double(getMinecraft().thePlayer.rotationYaw / 360.0F * 4.0F + 0.5D) & 0x3);
  }
  
  public static void faceDownOne() {
	  mc.thePlayer.rotationPitch = (float) 57;
  }
  
  public static void faceDownTwo() {
	  mc.thePlayer.rotationPitch = (float) 49.5;
  }
  
  public static void faceDownThree() {
	  mc.thePlayer.rotationPitch = (float) 32.5;
  }
  
  public static void faceAhead() {
	  mc.thePlayer.rotationPitch = 0;
  }
  
  public static int getPitchDirection() {
	  float rotPitch = mc.thePlayer.rotationPitch;
	  if (rotPitch <= 30) {
		  return 0;
	  }
	  else {
		  return 1;
	  }
  }
  
  public static void placeBlock() {
	  int count = 9;
	  for (int i = 0; i < count; i++) {
		  if (mc.thePlayer.inventory.getCurrentItem() != null) {
			  if (mc.thePlayer.getCurrentEquippedItem().getUnlocalizedName().equals("tile.burlapcraftmod_burlapstone")) {
				  overrideUseItem();
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
  
  public static void destroyBlock() {
	  int count = 9;
	  for (int i = 0; i < count; i++) {
		  if (mc.thePlayer.inventory.getCurrentItem() != null) {
			  if (mc.thePlayer.getCurrentEquippedItem().getUnlocalizedName().equals("item.pickaxeDiamond")) {
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
}
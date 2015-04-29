package com.kcaluru.burlapbot.helpers;

import java.io.PrintStream;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.kcaluru.burlapbot.helpers.Pos;
import com.kcaluru.burlapbot.items.ItemFilter;





//import net.famzangl.minecraft.minebot.ai.command.AIChatController;
//import net.famzangl.minecraft.minebot.ai.task.AITask;
//import net.famzangl.minecraft.minebot.build.BuildManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockWall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.util.ForgeDirection;

public class BurlapAIHelper {
  private static final double SNEAK_OFFSET = 0.2D;
  private static final double WALK_PER_STEP = 0.215D;
  private static final double MIN_DISTANCE_ERROR = 0.05D;
  private static Minecraft mc = Minecraft.getMinecraft();
  private final static Random rand = new Random();
  
  public static final Block[] normalBlocks = { Blocks.bedrock, Blocks.bookshelf, Blocks.brick_block, Blocks.brown_mushroom_block, Blocks.cake, Blocks.coal_block, Blocks.coal_ore, Blocks.cobblestone, Blocks.crafting_table, Blocks.diamond_block, Blocks.diamond_ore, Blocks.dirt, Blocks.double_stone_slab, Blocks.double_wooden_slab, Blocks.emerald_block, Blocks.emerald_ore, Blocks.farmland, Blocks.glass, Blocks.glowstone, Blocks.grass, Blocks.gold_block, Blocks.gold_ore, Blocks.hardened_clay, Blocks.iron_block, Blocks.iron_ore, Blocks.lapis_block, Blocks.lapis_ore, Blocks.leaves, Blocks.leaves2, Blocks.log, Blocks.log2, Blocks.melon_block, Blocks.mossy_cobblestone, Blocks.mycelium, Blocks.nether_brick, Blocks.nether_brick_fence, Blocks.netherrack, Blocks.obsidian, Blocks.packed_ice, Blocks.planks, Blocks.pumpkin, Blocks.quartz_block, Blocks.quartz_ore, Blocks.red_mushroom_block, Blocks.redstone_block, Blocks.redstone_lamp, Blocks.redstone_ore, Blocks.sandstone, Blocks.snow, Blocks.soul_sand, Blocks.stained_glass, Blocks.stained_hardened_clay, Blocks.stone, Blocks.stonebrick, Blocks.web, Blocks.wool };

  public static final Block[] fallingBlocks = { Blocks.gravel, Blocks.sand };

  public static final Block[] stairBlocks = { Blocks.acacia_stairs, Blocks.birch_stairs, Blocks.brick_stairs, Blocks.dark_oak_stairs, Blocks.jungle_stairs, Blocks.nether_brick_stairs, Blocks.oak_stairs, Blocks.sandstone_stairs, Blocks.spruce_stairs, Blocks.stone_brick_stairs, Blocks.stone_stairs, Blocks.stone_slab, Blocks.wooden_slab, Blocks.quartz_stairs };

  public static final Block[] walkableBlocks = { Blocks.tallgrass, Blocks.yellow_flower, Blocks.red_flower, Blocks.wheat, Blocks.carrots, Blocks.potatoes, Blocks.pumpkin_stem, Blocks.melon_stem, Blocks.torch, Blocks.carpet, Blocks.golden_rail, Blocks.detector_rail, Blocks.rail, Blocks.activator_rail, Blocks.double_plant, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.redstone_wire };

  public static final Block[] safeSideBlocks = { Blocks.fence, Blocks.fence_gate, Blocks.cobblestone_wall, Blocks.cactus, Blocks.reeds };
  
  public static final Block[] allBlocks = { Blocks.bedrock, Blocks.bookshelf, Blocks.brick_block, Blocks.brown_mushroom_block, Blocks.cake, Blocks.coal_block, Blocks.coal_ore, Blocks.cobblestone, Blocks.crafting_table, Blocks.diamond_block, Blocks.diamond_ore, Blocks.dirt, Blocks.double_stone_slab, Blocks.double_wooden_slab, Blocks.emerald_block, Blocks.emerald_ore, Blocks.farmland, Blocks.glass, Blocks.glowstone, Blocks.grass, Blocks.gold_block, Blocks.gold_ore, Blocks.hardened_clay, Blocks.iron_block, Blocks.iron_ore, Blocks.lapis_block, Blocks.lapis_ore, Blocks.leaves, Blocks.leaves2, Blocks.log, Blocks.log2, Blocks.melon_block, Blocks.mossy_cobblestone, Blocks.mycelium, Blocks.nether_brick, Blocks.nether_brick_fence, Blocks.netherrack, Blocks.obsidian, Blocks.packed_ice, Blocks.planks, Blocks.pumpkin, Blocks.quartz_block, Blocks.quartz_ore, Blocks.red_mushroom_block, Blocks.redstone_block, Blocks.redstone_lamp, Blocks.redstone_ore, Blocks.sandstone, Blocks.snow, Blocks.soul_sand, Blocks.stained_glass, Blocks.stained_hardened_clay, Blocks.stone, Blocks.stonebrick, Blocks.web, Blocks.wool, Blocks.gravel, Blocks.sand, Blocks.acacia_stairs, Blocks.birch_stairs, Blocks.brick_stairs, Blocks.dark_oak_stairs, Blocks.jungle_stairs, Blocks.nether_brick_stairs, Blocks.oak_stairs, Blocks.sandstone_stairs, Blocks.spruce_stairs, Blocks.stone_brick_stairs, Blocks.stone_stairs, Blocks.stone_slab, Blocks.wooden_slab, Blocks.quartz_stairs, Blocks.tallgrass, Blocks.yellow_flower, Blocks.red_flower, Blocks.wheat, Blocks.carrots, Blocks.potatoes, Blocks.pumpkin_stem, Blocks.melon_stem, Blocks.torch, Blocks.carpet, Blocks.golden_rail, Blocks.detector_rail, Blocks.rail, Blocks.activator_rail, Blocks.double_plant, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.redstone_wire, Blocks.fence, Blocks.fence_gate, Blocks.cobblestone_wall, Blocks.cactus, Blocks.reeds };

  private static boolean objectMouseOverInvalidated;
  protected static Pos pos1 = null;
  protected static Pos pos2 = null;
  private static MovementInput resetMovementInput;
  private static KeyBinding resetAttackKey;
  private static KeyBinding resetUseItemKey;
  private static boolean useItemKeyJustPressed;
  private static boolean attackKeyJustPressed;
  protected static boolean doUngrab;
  private static KeyBinding resetSneakKey;
  private static boolean sneakKeyJustPressed;

  public static Minecraft getMinecraft()
  {
    return mc;
  }

  public static Block getBlock(int x, int y, int z)
  {
    return mc.theWorld.getBlock(x, y, z);
  }

  public static Block getBlock(Pos pos)
  {
    return getBlock(pos.x, pos.y, pos.z);
  }

//  public abstract void addTask(AITask paramAITask);

//  public abstract void desync();

  public static Pos getPos1()
  {
    return pos1;
  }

  public static Pos getPos2()
  {
    return pos2;
  }
//
//  public void setPosition(Pos pos, boolean isPos2)
//  {
//    int posIndex;
//    if (isPos2) {
//      this.pos2 = pos;
//      posIndex = 2;
//    } else {
//      this.pos1 = pos;
//      posIndex = 1;
//    }
////    AIChatController.addChatLine("Set position" + posIndex + " to " + pos);
//  }

  public static void face(double x, double y, double z)
  {
    double d0 = x - mc.thePlayer.posX;
    double d1 = z - mc.thePlayer.posZ;
    double d2 = y - mc.thePlayer.posY;
    double d3 = d0 * d0 + d2 * d2 + d1 * d1;

    if (d3 >= 2.500000277905201E-07D) {
      float rotationYaw = mc.thePlayer.rotationYaw;
      float rotationPitch = mc.thePlayer.rotationPitch;

      float yaw = (float)(Math.atan2(d1, d0) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)-(Math.atan2(d2, Math.sqrt(d0 * d0 + d1 * d1)) * 180.0D / 3.141592653589793D);

      mc.thePlayer.setAngles((yaw - rotationYaw) / 0.15F, -(pitch - rotationPitch) / 0.15F);

      invalidateObjectMouseOver();
    }
  }

  public static boolean canSelectItem(ItemFilter f)
  {
    for (int i = 0; i < 9; i++) {
      if (f.matches(mc.thePlayer.inventory.getStackInSlot(i))) {
        return true;
      }
    }
    return false;
  }

  public static boolean selectCurrentItem(ItemFilter f)
  {
    if (f.matches(mc.thePlayer.inventory.getCurrentItem())) {
      return true;
    }
    for (int i = 0; i < 9; i++) {
      if (f.matches(mc.thePlayer.inventory.getStackInSlot(i))) {
        mc.thePlayer.inventory.currentItem = i;
        return true;
      }
    }
    return false;
  }

  public static Pos findBlock(Block blockType)
  {
    int cx = MathHelper.floor_double(mc.thePlayer.posX);
    int cy = MathHelper.floor_double(mc.thePlayer.posY);
    int cz = MathHelper.floor_double(mc.thePlayer.posZ);
    Pos pos = null;
    for (int x = cx - 2; x <= cx + 2; x++) {
      for (int z = cz - 2; z <= cz + 2; z++) {
        for (int y = cy - 1; y <= cy + 1; y++) {
          Block block = mc.theWorld.getBlock(x, y, z);
          if (Block.isEqualTo(block, blockType)) {
            pos = new Pos(x, y, z);
          }
        }
      }
    }
    return pos;
  }

  public static boolean isFacingBlock(int x, int y, int z)
  {
    MovingObjectPosition o = getObjectMouseOver();
    return (o != null) && (o.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) && (o.blockX == x) && (o.blockY == y) && (o.blockZ == z);
  }

  public static boolean isFacingBlock(int x, int y, int z, ForgeDirection blockSide, BlockSide half)
  {
    if (!isFacingBlock(x, y, z, sideToDir(blockSide))) {
      return false;
    }
    double fy = getObjectMouseOver().hitVec.yCoord - y;
    return ((half != BlockSide.LOWER_HALF) && (fy > 0.5D)) || ((half != BlockSide.UPPER_HALF) && (fy <= 0.5D));
  }

  public static boolean isFacingBlock(int x, int y, int z, ForgeDirection blockSide)
  {
    return isFacingBlock(x, y, z, sideToDir(blockSide));
  }

  public static boolean isFacingBlock(int x, int y, int z, int side)
  {
    MovingObjectPosition o = getObjectMouseOver();
    return (o != null) && (o.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) && (o.blockX == x) && (o.blockY == y) && (o.blockZ == z) && (o.sideHit == side);
  }

  public static void invalidateObjectMouseOver()
  {
    objectMouseOverInvalidated = true;
  }

  public static MovingObjectPosition getObjectMouseOver()
  {
    if (objectMouseOverInvalidated) {
      objectMouseOverInvalidated = false;
      mc.entityRenderer.getMouseOver(1.0F);
    }
    return mc.objectMouseOver;
  }

  public static boolean isStandingOn(int x, int y, int z)
  {
    return (Math.abs(x + 0.5D - mc.thePlayer.posX) < 0.2D) && (Math.abs(z + 0.5D - mc.thePlayer.posZ) < 0.2D) && (Math.abs(mc.thePlayer.boundingBox.minY - y) < 0.52D);
  }

  public static Pos getPlayerPosition()
  {
    int x = MathHelper.floor_double(getMinecraft().thePlayer.posX);
    int y = MathHelper.floor_double(getMinecraft().thePlayer.boundingBox.minY + 0.05D);

    int z = MathHelper.floor_double(getMinecraft().thePlayer.posZ);
    return new Pos(x, y, z);
  }

  public static void selectToolFor(final int x, final int y, final int z)
  {
    selectCurrentItem(new ItemFilter()
    {
      public boolean matches(ItemStack itemStack) {
        return (itemStack != null) && (itemStack.getItem() != null) && (itemStack.getItem().func_150893_a(itemStack, BurlapAIHelper.this.getBlock(x, y, z)) > 1.0F);
      }
    });
  }

  public static boolean isAirBlock(int x, int y, int z)
  {
    return Block.isEqualTo(mc.theWorld.getBlock(x, y, z), Blocks.air);
  }

  public static boolean isSafeUpwardsGround(int x, int y, int z)
  {
    return (isSafeGroundBlock(x, y, z)) || (isAirBlock(x, y, z));
  }

  public static boolean isSafeGroundBlock(int x, int y, int z)
  {
    Block block = mc.theWorld.getBlock(x, y, z);
    return isSafeStandableBlock(block);
  }

  public static boolean isSafeHeadBlock(int x, int y, int z)
  {
    Block block = mc.theWorld.getBlock(x, y, z);
    return (blockIsOneOf(block, stairBlocks)) || (blockIsOneOf(block, normalBlocks)) || (isAirBlock(x, y, z));
  }

  public static boolean isFallingBlock(int x, int y, int z)
  {
    Block block = mc.theWorld.getBlock(x, y, z);
    return blockIsOneOf(block, fallingBlocks);
  }

  public static boolean hasSafeSides(int cx, int cy, int cz)
  {
    return (isSafeSideBlock(cx - 1, cy, cz)) && (isSafeSideBlock(cx + 1, cy, cz)) && (isSafeSideBlock(cx, cy, cz + 1)) && (isSafeSideBlock(cx, cy, cz - 1));
  }

  public static boolean isSafeSideBlock(int x, int y, int z)
  {
    Block block = mc.theWorld.getBlock(x, y, z);
    return (isSafeStandableBlock(block)) || (canWalkOn(block)) || (canWalkThrough(block)) || (blockIsOneOf(block, safeSideBlocks)) || (isAirBlock(x, y, z));
  }

  public static boolean canWalkThrough(Block block)
  {
    return blockIsOneOf(block, new Block[] { Blocks.air, Blocks.torch, Blocks.double_plant, Blocks.redstone_torch });
  }

  public static boolean canWalkOn(Block block)
  {
    return (blockIsOneOf(block, walkableBlocks)) || (canWalkThrough(block));
  }

  @Deprecated
  public boolean isRailBlock(Block block)
  {
    return blockIsOneOf(block, new Block[] { Blocks.golden_rail, Blocks.detector_rail, Blocks.rail, Blocks.activator_rail });
  }

  public static boolean isSafeStandableBlock(Block block)
  {
    return (blockIsOneOf(block, normalBlocks)) || (blockIsOneOf(block, fallingBlocks)) || (blockIsOneOf(block, stairBlocks));
  }

  public static void faceAndDestroy(int x, int y, int z)
  {
    if (isFacingBlock(x, y, z)) {
      selectToolFor(x, y, z);
      overrideAttack();
    } else {
      faceBlock(x, y, z);
    }
  }

  public static void faceBlock(int x, int y, int z)
  {
    face(x + randBetween(0.1D, 0.9D), y + randBetween(0.1D, 0.9D), z + randBetween(0.1D, 0.9D));
  }

  public static void faceSideOf(int x, int y, int z, ForgeDirection sideToFace)
  {
    double faceX = x + randBetween(0.1D, 0.9D);
    double faceY = y + randBetween(0.1D, 0.9D);
    double faceZ = z + randBetween(0.1D, 0.9D);

    Block block = getBoundsBlock(x, y, z);
    switch (sideToFace) {
    case UP:
      faceY = y + block.getBlockBoundsMaxY();
      break;
    case DOWN:
      faceY = y + block.getBlockBoundsMinY();
      break;
    case EAST:
      faceX = x + block.getBlockBoundsMaxX();
      break;
    case WEST:
      faceX = x + block.getBlockBoundsMinX();
      break;
    case SOUTH:
      faceZ = z + block.getBlockBoundsMaxZ();
      break;
    case NORTH:
      faceZ = z + block.getBlockBoundsMinZ();
      break;
	default:
		break;
    }

    face(faceX, faceY, faceZ);
  }

  public static void faceSideOf(int x, int y, int z, ForgeDirection sideToFace, double minY, double maxY, double centerX, double centerZ, ForgeDirection xzdir)
  {
    System.out.println("x = " + x + " y=" + y + " z=" + z + " dir=" + sideToFace);

    Block block = getBoundsBlock(x, y, z);

    minY = Math.max(minY, block.getBlockBoundsMinY());
    maxY = Math.min(maxY, block.getBlockBoundsMaxY());
    double faceY = randBetweenNice(minY, maxY);
    double faceZ;
    double faceX;
    if (xzdir == ForgeDirection.EAST) {
      faceX = randBetween(Math.max(block.getBlockBoundsMinX(), centerX), block.getBlockBoundsMaxX());

      faceZ = centerZ;
    }
    else
    {
      if (xzdir == ForgeDirection.WEST) {
        faceX = randBetween(block.getBlockBoundsMinX(), Math.min(block.getBlockBoundsMaxX(), centerX));

        faceZ = centerZ;
      }
      else
      {
        if (xzdir == ForgeDirection.SOUTH) {
          faceZ = randBetween(Math.max(block.getBlockBoundsMinZ(), centerZ), block.getBlockBoundsMaxZ());

          faceX = centerX;
        }
        else
        {
          if (xzdir == ForgeDirection.NORTH) {
            faceZ = randBetween(block.getBlockBoundsMinZ(), Math.min(block.getBlockBoundsMaxZ(), centerZ));

            faceX = centerX;
          } else {
            faceX = randBetweenNice(block.getBlockBoundsMinX(), block.getBlockBoundsMaxX());

            faceZ = randBetweenNice(block.getBlockBoundsMinZ(), block.getBlockBoundsMaxZ());
          }
        }
      }
    }
    switch (sideToFace) {
    case UP:
      faceY = block.getBlockBoundsMaxY();
      break;
    case DOWN:
      faceY = block.getBlockBoundsMinY();
      break;
    case EAST:
      faceX = block.getBlockBoundsMaxX();
      break;
    case WEST:
      faceX = block.getBlockBoundsMinX();
      break;
    case SOUTH:
      faceZ = block.getBlockBoundsMaxZ();
      break;
    case NORTH:
      faceZ = block.getBlockBoundsMinZ();
      break;
	default:
		break;
    }

    face(faceX + x, faceY + y, faceZ + z);
  }

  private static double randBetweenNice(double minY, double maxY)
  {
    return maxY - minY < 0.1D ? (maxY + minY) / 2.0D : randBetween(minY + 0.03D, maxY - 0.03D);
  }

  private static double randBetween(double a, double b)
  {
    return rand.nextDouble() * (b - a) + a;
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
    mc.gameSettings.keyBindUseItem = new Interaction(mc.gameSettings.keyBindUseItem.getKeyDescription(), 501, mc.gameSettings.keyBindUseItem.getKeyCategory(), useItemKeyJustPressed);
  }

  public static void overrideAttack()
  {
    if (resetAttackKey == null) {
      resetAttackKey = mc.gameSettings.keyBindAttack;
    }
    mc.gameSettings.keyBindAttack = new Interaction(mc.gameSettings.keyBindAttack.getKeyDescription(), 502, mc.gameSettings.keyBindAttack.getKeyCategory(), attackKeyJustPressed);
  }

  public static void overrideSneak()
  {
    if (resetSneakKey == null) {
      resetSneakKey = mc.gameSettings.keyBindSneak;
    }
    mc.gameSettings.keyBindSneak = new Interaction(mc.gameSettings.keyBindSneak.getKeyDescription(), 503, mc.gameSettings.keyBindSneak.getKeyCategory(), sneakKeyJustPressed);
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
    sneakKeyJustPressed = (resetSneakKey != null);
    if (resetSneakKey != null)
      mc.gameSettings.keyBindSneak = resetSneakKey;
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

  public static double realBlockTopY(int x, int y, int z)
  {
    Block block = getBlock(x, y - 1, z);
    double maxY;
    if (((block instanceof BlockFence)) || ((block instanceof BlockFenceGate)) || ((block instanceof BlockWall)))
    {
      maxY = 1.5D;
    }
    else
    {
      if ((block instanceof BlockSlab)) {
        int blockMetadata = getMinecraft().theWorld.getBlockMetadata(x, y, z);

        maxY = (blockMetadata & 0x8) == 0 ? 0.5D : 1.0D;
      } else {
        maxY = block.getBlockBoundsMaxY();
      }
    }
    return y - 1 + maxY;
  }

  public static List<Entity> getEntities(int dist, IEntitySelector selector)
  {
    return mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.renderViewEntity, mc.renderViewEntity.boundingBox.addCoord(-dist, -dist, -dist).addCoord(dist, dist, dist).expand(1.0D, 1.0D, 1.0D), selector);
  }

  public static Entity getClosestEntity(int dist, IEntitySelector selector)
  {
    List<Entity> entities = getEntities(dist, selector);

    double mindist = Double.MAX_VALUE;
    Entity found = null;
    for (Entity e : entities) {
      double mydist = e.getDistanceSqToEntity((Entity)BurlapAIHelper.mc.thePlayer);
      if (mydist < mindist) {
        found = e;
        mindist = mydist;
      }
    }

    return found;
  }

  public static void ungrab()
  {
    doUngrab = true;
  }

  public static boolean sneakFrom(int blockX, int blockY, int blockZ, ForgeDirection inDirection)
  {
    Block block = getBoundsBlock(blockX, blockY, blockZ);
    double destX = blockX + 0.5D;
    double destZ = blockZ + 0.5D;
    switch (inDirection) {
    case EAST:
      destX = blockX + block.getBlockBoundsMaxX() + 0.2D;
      break;
    case WEST:
      destX = blockX + block.getBlockBoundsMinX() - 0.2D;
      break;
    case SOUTH:
      destZ = blockZ + block.getBlockBoundsMaxZ() + 0.2D;
      break;
    case NORTH:
      destZ = blockZ + block.getBlockBoundsMinZ() - 0.2D;
      break;
    default:
      throw new IllegalArgumentException("Cannot handle " + inDirection);
    }
    return walkTowards(destX, destZ, false);
  }

  private static Block getBoundsBlock(int blockX, int blockY, int blockZ) {
    Block block = getBlock(blockX, blockY, blockZ);
    if ((block instanceof BlockStairs))
    {
      block = Blocks.dirt;
    }
    block.setBlockBoundsBasedOnState(mc.theWorld, blockX, blockY, blockZ);
    return block;
  }

  public static boolean walkTowards(double x, double z, boolean jump)
  {
    double dx = x - mc.thePlayer.posX;
    double dz = z - mc.thePlayer.posZ;
    double distTo = Math.sqrt(dx * dx + dz * dz);
    if (distTo > 0.05D) {
      face(x, mc.thePlayer.posY, z);
      double speed = 1.0D;
      if (distTo < 0.86D) {
        speed = Math.max(distTo / 0.215D / 4.0D, 0.1D);
      }
      MovementInput movement = new MovementInput();
      movement.moveForward = ((float)speed);
      movement.jump = jump;
      overrideMovement(movement);
      if (distTo < 0.5D) {
        overrideSneak();
      }
      return false;
    }
    return true;
  }
  
  public static boolean walkEast(boolean jump, final int curX, final int curY, final int curZ) {
	  mc.thePlayer.rotationYaw = -90;
	  MovementInput movement = new MovementInput();
	  movement.moveForward = (float) 0.5D;
	  movement.jump = jump;
	  
	  overrideMovement(movement);
	  
	  final Timer timer = new Timer();
	  timer.scheduleAtFixedRate(new TimerTask() {
		  @Override
		  public void run() {
			  if (isStandingOn(curX + 1, curY + 1, curZ) || isStandingOn(curX - 1, curY + 1, curZ + 1) || isStandingOn(curX - 1, curY + 1, curZ - 1)) {
				  resetAllInputs();
				  timer.cancel();
			  }
		  }
		}, 100, 1);
	  return true;
  }
  
  public static boolean walkWest(boolean jump, final int curX, final int curY, final int curZ) {
	  mc.thePlayer.rotationYaw = 90;
	  MovementInput movement = new MovementInput();
	  movement.moveForward = (float) 0.5D;
	  movement.jump = jump;
	  
	  overrideMovement(movement);
	  
	  final Timer timer = new Timer();
	  timer.scheduleAtFixedRate(new TimerTask() {
		  @Override
		  public void run() {
			  if (isStandingOn(curX - 1, curY + 1, curZ) || isStandingOn(curX - 1, curY + 1, curZ + 1) || isStandingOn(curX - 1, curY + 1, curZ - 1)) {
				  resetAllInputs();
				  timer.cancel();
			  }
		  }
		}, 100, 1);
	  return true;
  }
  
  public static boolean walkNorth(boolean jump, final int curX, final int curY, final int curZ) {
	  mc.thePlayer.rotationYaw = -180;
	  MovementInput movement = new MovementInput();
	  movement.moveForward = (float) 0.5D;
	  movement.jump = jump;
	  
	  overrideMovement(movement);
	  
	  final Timer timer = new Timer();
	  timer.scheduleAtFixedRate(new TimerTask() {
		  @Override
		  public void run() {
			  if (isStandingOn(curX, curY + 1, curZ - 1) || isStandingOn(curX - 1, curY + 1, curZ) || isStandingOn(curX + 1, curY + 1, curZ)) {
				  resetAllInputs();
				  timer.cancel();
			  }
		  }
		}, 100, 1);
	  return true;
  }
  
  public static boolean walkSouth(boolean jump, final int curX, final int curY, final int curZ) {
	  mc.thePlayer.rotationYaw = 0;
	  MovementInput movement = new MovementInput();
	  movement.moveForward = (float) 0.5D;
	  movement.jump = jump;
	  
	  overrideMovement(movement);
	  
	  final Timer timer = new Timer();
	  timer.scheduleAtFixedRate(new TimerTask() {
		  @Override
		  public void run() {
			  if (isStandingOn(curX, curY + 1, curZ + 1) || isStandingOn(curX - 1, curY + 1, curZ) || isStandingOn(curX + 1, curY + 1, curZ)) {
				  resetAllInputs();
				  timer.cancel();
			  }
		  }
		}, 100, 1);
	  return true;
  }

  public static int sideToDir(ForgeDirection blockSide)
  {
    switch (blockSide) {
    case DOWN:
      return 0;
    case UP:
      return 1;
    case EAST:
      return 5;
    case WEST:
      return 4;
    case NORTH:
      return 2;
    case SOUTH:
      return 3;
	default:
		break;
    }
    throw new IllegalArgumentException("Cannot handle: " + blockSide);
  }

  public static boolean isJumping()
  {
    return !mc.thePlayer.onGround;
  }

  public static ForgeDirection getLookDirection()
  {
    switch (MathHelper.floor_double(getMinecraft().thePlayer.rotationYaw / 360.0F * 4.0F + 0.5D) & 0x3)
    {
    case 1:
      return ForgeDirection.WEST;
    case 2:
      return ForgeDirection.NORTH;
    case 3:
      return ForgeDirection.EAST;
    }
    return ForgeDirection.SOUTH;
  }

  public static boolean hasItemInInvetory(ItemFilter itemFiler)
  {
    for (ItemStack s : mc.thePlayer.inventory.mainInventory) {
      if (itemFiler.matches(s)) {
        return true;
      }
    }
    return false;
  }

  public static ForgeDirection getDirectionForXZ(int x, int z)
  {
    if ((x != 0) || (z != 0)) {
      for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
        if ((d.offsetX == x) && (d.offsetZ == z)) {
          return d;
        }
      }
    }
    throw new IllegalArgumentException("Cannot convert to direction: " + x + " " + z);
  }
}
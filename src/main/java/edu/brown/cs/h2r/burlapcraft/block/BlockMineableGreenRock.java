package edu.brown.cs.h2r.burlapcraft.block;

import java.util.Random;

import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BlockMineableGreenRock extends Block {

	// name of block
	private String name = "mineablegreenrock";

	public BlockMineableGreenRock() {
		
		super(Material.rock);
		setBlockName(BurlapCraft.MODID + "_" + name);
		setBlockTextureName(BurlapCraft.MODID + ":" + name);
		setCreativeTab(CreativeTabs.tabBlock);
		
		this.setHardness(0.5f);
		
		setStepSound(soundTypeStone);
		
		this.setHarvestLevel("pickaxe", 1);
		
	}
	
	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
	    return Item.getItemFromBlock(this);
	}

	@Override
	public int damageDropped(int metadata) {
	    return 0;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
	    return 1;
	}
	
}

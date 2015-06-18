package edu.brown.cs.h2r.burlapcraft.block;

import edu.brown.cs.h2r.burlapcraft.BurlapCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockOrangeRock extends Block {

	// name of block
	private String name = "orangerock";

	public BlockOrangeRock() {
		
		super(Material.rock);
		setBlockName(BurlapCraft.MODID + "_" + name);
		setBlockTextureName(BurlapCraft.MODID + ":" + name);
		setCreativeTab(CreativeTabs.tabBlock);
		
		this.setHardness(-1);
		
		setStepSound(soundTypeStone);
		
	}

}

package edu.brown.cs.h2r.burlapcraft.item;

import net.minecraft.item.ItemStack;

public abstract interface ItemFilter {
	
	public abstract boolean matches(ItemStack paramItemStack);

}

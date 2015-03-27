package com.kcaluru.burlapbot.items;

import net.minecraft.item.ItemStack;

public abstract interface ItemFilter {
	
	public abstract boolean matches(ItemStack paramItemStack);

}

package com.kcaluru.burlapbot.item;

import net.minecraft.item.ItemStack;

public abstract interface ItemFilter {
	
	public abstract boolean matches(ItemStack paramItemStack);

}

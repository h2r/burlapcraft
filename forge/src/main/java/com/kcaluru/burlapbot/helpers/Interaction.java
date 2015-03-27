package com.kcaluru.burlapbot.helpers;

import net.minecraft.client.settings.KeyBinding;

public class Interaction extends KeyBinding {
	private boolean isPressed;

	  public Interaction(String p_i45001_1_, int p_i45001_2_, String p_i45001_3_, boolean pressed)
	  {
	    super(p_i45001_1_, p_i45001_2_, p_i45001_3_);
	    this.isPressed = pressed;
	  }

	  public boolean func_151470_d()
	  {
	    return true;
	  }

	  public boolean func_151468_f()
	  {
	    boolean ret = this.isPressed;
	    this.isPressed = false;
	    return ret;
	  }
}

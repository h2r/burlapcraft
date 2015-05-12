package com.kcaluru.burlapbot.helpers;

import net.minecraft.client.settings.KeyBinding;

public class AutoInteract extends KeyBinding {
	private boolean isPressed;

	  public AutoInteract(String keyDescription, int keyCode, String keyCategory, boolean pressed)
	  {
	    super(keyDescription, keyCode, keyCategory);
	    this.isPressed = pressed;
	  }
	  
	  @Override
	  public boolean getIsKeyPressed()
	  {
	    return true;
	  }

	  @Override
	  public boolean isPressed()
	  {
	    boolean ret = this.isPressed;
	    this.isPressed = false;
	    return ret;
	  }
}

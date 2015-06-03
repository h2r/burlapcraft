package edu.brown.cs.h2r.burlapcraft.helper;

import net.minecraft.client.settings.KeyBinding;

public class HelperClickInteractions extends KeyBinding {
	private boolean isPressed;

	  public HelperClickInteractions(String keyDescription, int keyCode, String keyCategory, boolean pressed)
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

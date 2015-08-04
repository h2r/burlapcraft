package edu.brown.cs.h2r.burlapcraft.helper;

public class HelperGestureTuple {

	  public final float pitch; 
	  public final float yaw; 
	  
	  public HelperGestureTuple(float pitch, float yaw) { 
	    this.pitch = pitch; 
	    this.yaw = yaw; 
	  } 
 
	  public float getPitch() {
		  return this.pitch;
	  }
	  
	  public float getYaw() {
		  return this.yaw;
	  }
	
}

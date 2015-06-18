package edu.brown.cs.h2r.burlapcraft.helper;

public class HelperRoom {

	private final String color;
	private final int xMin;
	private final int xMax;
	private final int yMin;
	private final int yMax;
	
	public HelperRoom(int _xMin, int _xMax, int _yMin, int _yMax, String _color) {
		color = _color;
		xMin = _xMin;
		xMax = _xMax;
		yMin = _yMin;
		yMax = _yMax;
	}
	
	public int getXMin() {
		return xMin;
	}
	
	public int getXMax() {
		return xMax;
	}
	
	public int getYMin() {
		return yMin;
	}
	
	public int getYMax() {
		return yMax;
	}
	
	public String getColor() {
		return color;
	}
	
}

package edu.brown.cs.h2r.burlapcraft.helper;

public class Geometry {
	
	public static class Pose {
		double x;
		double y;
		double z;
		double qx;
		double qy;
		double qz;
		double qw;
		
		public static Pose fromXyz(double x, double y, double z) {
			return new Pose(x, y, z, 0, 0, 0, 0);
			
		}
		public Pose(double _x, double _y, double _z, double _qx, double _qy, double _qz, double _qw) {
			x = _x;
			y = _y;
			z = _z;
			qx = _qx;
			qy = _qy;
			qz = _qz;
			qw = _qw;
		}
		
		public double distance(Pose p2) {
			return Geometry.distance(x, y, z, p2.x, p2.y, p2.z);
		}
		public String toString() {
			StringBuffer result = new StringBuffer();
			result.append(String.valueOf(x));
			result.append(",");
			result.append(String.valueOf(y));
			result.append(",");
			result.append(String.valueOf(z));
			return result.toString();
		}
	}
	
	
	public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.pow(Math.pow((x2 - x1), 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2), 0.5);
	}
}

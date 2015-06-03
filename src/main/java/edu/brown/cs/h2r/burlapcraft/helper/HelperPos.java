package edu.brown.cs.h2r.burlapcraft.helper;

import net.minecraftforge.common.util.*;

public class HelperPos
{
    public int x;
    public int y;
    public int z;
    
    public HelperPos(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public String toString() {
        return "Pos [x=" + this.x + ", y=" + this.y + ", z=" + this.z + "]";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.x;
        result = 31 * result + this.y;
        result = 31 * result + this.z;
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final HelperPos other = (HelperPos)obj;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }
    
    public static HelperPos fromDir(final ForgeDirection dir) {
        return new HelperPos(dir.offsetX, dir.offsetY, dir.offsetZ);
    }
    
    public HelperPos add(final int x, final int y, final int z) {
        return new HelperPos(this.x + x, this.y + y, this.z + z);
    }
    
    public static HelperPos[] fromDir(final ForgeDirection[] standable) {
        final HelperPos[] res = new HelperPos[standable.length];
        for (int i = 0; i < res.length; ++i) {
            res[i] = fromDir(standable[i]);
        }
        return res;
    }
    
    public static HelperPos minPos(final HelperPos p1, final HelperPos p2) {
        return new HelperPos(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.min(p1.z, p2.z));
    }
    
    public static HelperPos maxPos(final HelperPos p1, final HelperPos p2) {
        return new HelperPos(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y), Math.max(p1.z, p2.z));
    }
    
    public HelperPos add(final HelperPos pos) {
        return this.add(pos.x, pos.y, pos.z);
    }
    
    public HelperPos subtract(final HelperPos pos) {
        return new HelperPos(this.x - pos.x, this.y - pos.y, this.z - pos.z);
    }
}

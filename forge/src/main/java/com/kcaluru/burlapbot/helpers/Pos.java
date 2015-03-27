package com.kcaluru.burlapbot.helpers;

import net.minecraftforge.common.util.*;

public class Pos
{
    public int x;
    public int y;
    public int z;
    
    public Pos(final int x, final int y, final int z) {
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
        final Pos other = (Pos)obj;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }
    
    public static Pos fromDir(final ForgeDirection dir) {
        return new Pos(dir.offsetX, dir.offsetY, dir.offsetZ);
    }
    
    public Pos add(final int x, final int y, final int z) {
        return new Pos(this.x + x, this.y + y, this.z + z);
    }
    
    public static Pos[] fromDir(final ForgeDirection[] standable) {
        final Pos[] res = new Pos[standable.length];
        for (int i = 0; i < res.length; ++i) {
            res[i] = fromDir(standable[i]);
        }
        return res;
    }
    
    public static Pos minPos(final Pos p1, final Pos p2) {
        return new Pos(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.min(p1.z, p2.z));
    }
    
    public static Pos maxPos(final Pos p1, final Pos p2) {
        return new Pos(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y), Math.max(p1.z, p2.z));
    }
    
    public Pos add(final Pos pos) {
        return this.add(pos.x, pos.y, pos.z);
    }
    
    public Pos subtract(final Pos pos) {
        return new Pos(this.x - pos.x, this.y - pos.y, this.z - pos.z);
    }
}

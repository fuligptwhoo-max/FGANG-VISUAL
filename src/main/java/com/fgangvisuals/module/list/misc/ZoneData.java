package com.fgangvisuals.module.list.misc;

public class ZoneData {
    public String warpCommand = "";
    public int minX, minZ, maxX, maxZ;

    public ZoneData() {}

    public ZoneData(String warpCommand, int minX, int minZ, int maxX, int maxZ) {
        this.warpCommand = warpCommand;
        this.minX = Math.min(minX, maxX);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxZ = Math.max(minZ, maxZ);
    }
}

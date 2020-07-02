package net.minecraft.util;

import net.minecraft.client.Minecraft;

public class Timer
{
    public int elapsedTicks;
    public float renderPartialTicks;
    public float elapsedPartialTicks;
    private long lastSyncSysClock;
    private float tickLength;

    public Timer(float p_i1018_1_)
    {
        this.tickLength = 1000.0F / p_i1018_1_;
        this.lastSyncSysClock = Minecraft.func_71386_F();
    }

    /**
     * Updates all fields of the Timer using the current time
     */
    public void updateTimer()
    {
        long i = Minecraft.func_71386_F();
        this.elapsedPartialTicks = (float)(i - this.lastSyncSysClock) / this.tickLength;
        this.lastSyncSysClock = i;
        this.renderPartialTicks += this.elapsedPartialTicks;
        this.elapsedTicks = (int)this.renderPartialTicks;
        this.renderPartialTicks -= (float)this.elapsedTicks;
    }
}

package net.minecraft.world.border;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class WorldBorder
{
    private final List<IBorderListener> listeners = Lists.<IBorderListener>newArrayList();
    private double centerX;
    private double centerZ;
    private double field_177754_d = 6.0E7D;
    private double field_177755_e;
    private long field_177752_f;
    private long field_177753_g;
    private int worldSize;
    private double damagePerBlock;
    private double damageBuffer;
    private int warningTime;
    private int warningDistance;

    public WorldBorder()
    {
        this.field_177755_e = this.field_177754_d;
        this.worldSize = 29999984;
        this.damagePerBlock = 0.2D;
        this.damageBuffer = 5.0D;
        this.warningTime = 15;
        this.warningDistance = 5;
    }

    public boolean contains(BlockPos pos)
    {
        return (double)(pos.getX() + 1) > this.minX() && (double)pos.getX() < this.maxX() && (double)(pos.getZ() + 1) > this.minZ() && (double)pos.getZ() < this.maxZ();
    }

    public boolean contains(ChunkPos range)
    {
        return (double)range.getXEnd() > this.minX() && (double)range.getXStart() < this.maxX() && (double)range.getZEnd() > this.minZ() && (double)range.getZStart() < this.maxZ();
    }

    public boolean contains(AxisAlignedBB bb)
    {
        return bb.maxX > this.minX() && bb.minX < this.maxX() && bb.maxZ > this.minZ() && bb.minZ < this.maxZ();
    }

    public double getClosestDistance(Entity entityIn)
    {
        return this.getClosestDistance(entityIn.posX, entityIn.posZ);
    }

    public double getClosestDistance(double x, double z)
    {
        double d0 = z - this.minZ();
        double d1 = this.maxZ() - z;
        double d2 = x - this.minX();
        double d3 = this.maxX() - x;
        double d4 = Math.min(d2, d3);
        d4 = Math.min(d4, d0);
        return Math.min(d4, d1);
    }

    public EnumBorderStatus getStatus()
    {
        if (this.field_177755_e < this.field_177754_d)
        {
            return EnumBorderStatus.SHRINKING;
        }
        else
        {
            return this.field_177755_e > this.field_177754_d ? EnumBorderStatus.GROWING : EnumBorderStatus.STATIONARY;
        }
    }

    public double minX()
    {
        double d0 = this.getCenterX() - this.getDiameter() / 2.0D;

        if (d0 < (double)(-this.worldSize))
        {
            d0 = (double)(-this.worldSize);
        }

        return d0;
    }

    public double minZ()
    {
        double d0 = this.getCenterZ() - this.getDiameter() / 2.0D;

        if (d0 < (double)(-this.worldSize))
        {
            d0 = (double)(-this.worldSize);
        }

        return d0;
    }

    public double maxX()
    {
        double d0 = this.getCenterX() + this.getDiameter() / 2.0D;

        if (d0 > (double)this.worldSize)
        {
            d0 = (double)this.worldSize;
        }

        return d0;
    }

    public double maxZ()
    {
        double d0 = this.getCenterZ() + this.getDiameter() / 2.0D;

        if (d0 > (double)this.worldSize)
        {
            d0 = (double)this.worldSize;
        }

        return d0;
    }

    public double getCenterX()
    {
        return this.centerX;
    }

    public double getCenterZ()
    {
        return this.centerZ;
    }

    public void setCenter(double x, double z)
    {
        this.centerX = x;
        this.centerZ = z;

        for (IBorderListener iborderlistener : this.getListeners())
        {
            iborderlistener.onCenterChanged(this, x, z);
        }
    }

    public double getDiameter()
    {
        if (this.getStatus() != EnumBorderStatus.STATIONARY)
        {
            double d0 = (double)((float)(System.currentTimeMillis() - this.field_177753_g) / (float)(this.field_177752_f - this.field_177753_g));

            if (d0 < 1.0D)
            {
                return this.field_177754_d + (this.field_177755_e - this.field_177754_d) * d0;
            }

            this.setTransition(this.field_177755_e);
        }

        return this.field_177754_d;
    }

    public long getTimeUntilTarget()
    {
        return this.getStatus() == EnumBorderStatus.STATIONARY ? 0L : this.field_177752_f - System.currentTimeMillis();
    }

    public double getTargetSize()
    {
        return this.field_177755_e;
    }

    public void setTransition(double newSize)
    {
        this.field_177754_d = newSize;
        this.field_177755_e = newSize;
        this.field_177752_f = System.currentTimeMillis();
        this.field_177753_g = this.field_177752_f;

        for (IBorderListener iborderlistener : this.getListeners())
        {
            iborderlistener.onSizeChanged(this, newSize);
        }
    }

    public void setTransition(double oldSize, double newSize, long time)
    {
        this.field_177754_d = oldSize;
        this.field_177755_e = newSize;
        this.field_177753_g = System.currentTimeMillis();
        this.field_177752_f = this.field_177753_g + time;

        for (IBorderListener iborderlistener : this.getListeners())
        {
            iborderlistener.onTransitionStarted(this, oldSize, newSize, time);
        }
    }

    protected List<IBorderListener> getListeners()
    {
        return Lists.newArrayList(this.listeners);
    }

    public void addListener(IBorderListener listener)
    {
        this.listeners.add(listener);
    }

    public void setSize(int size)
    {
        this.worldSize = size;
    }

    public int getSize()
    {
        return this.worldSize;
    }

    public double getDamageBuffer()
    {
        return this.damageBuffer;
    }

    public void setDamageBuffer(double bufferSize)
    {
        this.damageBuffer = bufferSize;

        for (IBorderListener iborderlistener : this.getListeners())
        {
            iborderlistener.onDamageBufferChanged(this, bufferSize);
        }
    }

    public double getDamagePerBlock()
    {
        return this.damagePerBlock;
    }

    public void setDamagePerBlock(double newAmount)
    {
        this.damagePerBlock = newAmount;

        for (IBorderListener iborderlistener : this.getListeners())
        {
            iborderlistener.onDamageAmountChanged(this, newAmount);
        }
    }

    public double getResizeSpeed()
    {
        return this.field_177752_f == this.field_177753_g ? 0.0D : Math.abs(this.field_177754_d - this.field_177755_e) / (double)(this.field_177752_f - this.field_177753_g);
    }

    public int getWarningTime()
    {
        return this.warningTime;
    }

    public void setWarningTime(int warningTime)
    {
        this.warningTime = warningTime;

        for (IBorderListener iborderlistener : this.getListeners())
        {
            iborderlistener.onWarningTimeChanged(this, warningTime);
        }
    }

    public int getWarningDistance()
    {
        return this.warningDistance;
    }

    public void setWarningDistance(int warningDistance)
    {
        this.warningDistance = warningDistance;

        for (IBorderListener iborderlistener : this.getListeners())
        {
            iborderlistener.onWarningDistanceChanged(this, warningDistance);
        }
    }
}

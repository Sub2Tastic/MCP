package net.minecraft.world;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.ISaveHandler;

public class WorldServerMulti extends WorldServer
{
    private final WorldServer field_175743_a;

    public WorldServerMulti(MinecraftServer p_i45923_1_, ISaveHandler p_i45923_2_, int p_i45923_3_, WorldServer p_i45923_4_, Profiler p_i45923_5_)
    {
        super(p_i45923_1_, p_i45923_2_, new DerivedWorldInfo(p_i45923_4_.getWorldInfo()), p_i45923_3_, p_i45923_5_);
        this.field_175743_a = p_i45923_4_;
        p_i45923_4_.getWorldBorder().addListener(new IBorderListener()
        {
            public void onSizeChanged(WorldBorder border, double newSize)
            {
                WorldServerMulti.this.getWorldBorder().setTransition(newSize);
            }
            public void onTransitionStarted(WorldBorder border, double oldSize, double newSize, long time)
            {
                WorldServerMulti.this.getWorldBorder().setTransition(oldSize, newSize, time);
            }
            public void onCenterChanged(WorldBorder border, double x, double z)
            {
                WorldServerMulti.this.getWorldBorder().setCenter(x, z);
            }
            public void onWarningTimeChanged(WorldBorder border, int newTime)
            {
                WorldServerMulti.this.getWorldBorder().setWarningTime(newTime);
            }
            public void onWarningDistanceChanged(WorldBorder border, int newDistance)
            {
                WorldServerMulti.this.getWorldBorder().setWarningDistance(newDistance);
            }
            public void onDamageAmountChanged(WorldBorder border, double newAmount)
            {
                WorldServerMulti.this.getWorldBorder().setDamagePerBlock(newAmount);
            }
            public void onDamageBufferChanged(WorldBorder border, double newSize)
            {
                WorldServerMulti.this.getWorldBorder().setDamageBuffer(newSize);
            }
        });
    }

    /**
     * Saves the chunks to disk.
     */
    protected void saveLevel() throws MinecraftException
    {
    }

    public World func_175643_b()
    {
        this.field_72988_C = this.field_175743_a.func_175693_T();
        this.field_96442_D = this.field_175743_a.getScoreboard();
        this.field_184151_B = this.field_175743_a.func_184146_ak();
        this.field_191951_C = this.field_175743_a.func_191952_z();
        String s = VillageCollection.func_176062_a(this.dimension);
        VillageCollection villagecollection = (VillageCollection)this.field_72988_C.func_75742_a(VillageCollection.class, s);

        if (villagecollection == null)
        {
            this.field_72982_D = new VillageCollection(this);
            this.field_72988_C.func_75745_a(s, this.field_72982_D);
        }
        else
        {
            this.field_72982_D = villagecollection;
            this.field_72982_D.func_82566_a(this);
        }

        return this;
    }

    public void func_184166_c()
    {
        this.dimension.onWorldSave();
    }
}

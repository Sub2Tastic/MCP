package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Biomes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.storage.WorldInfo;

public class BiomeProvider
{
    private ChunkGeneratorSettings field_190945_a;
    private GenLayer field_76944_d;
    private GenLayer field_76945_e;
    private final BiomeCache field_76942_f;
    private final List<Biome> field_76943_g;

    protected BiomeProvider()
    {
        this.field_76942_f = new BiomeCache(this);
        this.field_76943_g = Lists.newArrayList(Biomes.FOREST, Biomes.PLAINS, Biomes.TAIGA, Biomes.TAIGA_HILLS, Biomes.WOODED_HILLS, Biomes.JUNGLE, Biomes.JUNGLE_HILLS);
    }

    private BiomeProvider(long p_i45744_1_, WorldType p_i45744_3_, String p_i45744_4_)
    {
        this();

        if (p_i45744_3_ == WorldType.CUSTOMIZED && !p_i45744_4_.isEmpty())
        {
            this.field_190945_a = ChunkGeneratorSettings.Factory.func_177865_a(p_i45744_4_).func_177864_b();
        }

        GenLayer[] agenlayer = GenLayer.func_180781_a(p_i45744_1_, p_i45744_3_, this.field_190945_a);
        this.field_76944_d = agenlayer[0];
        this.field_76945_e = agenlayer[1];
    }

    public BiomeProvider(WorldInfo p_i46712_1_)
    {
        this(p_i46712_1_.getSeed(), p_i46712_1_.getGenerator(), p_i46712_1_.func_82571_y());
    }

    public List<Biome> getBiomesToSpawnIn()
    {
        return this.field_76943_g;
    }

    public Biome func_180631_a(BlockPos p_180631_1_)
    {
        return this.func_180300_a(p_180631_1_, (Biome)null);
    }

    public Biome func_180300_a(BlockPos p_180300_1_, Biome p_180300_2_)
    {
        return this.field_76942_f.func_180284_a(p_180300_1_.getX(), p_180300_1_.getZ(), p_180300_2_);
    }

    public float func_76939_a(float p_76939_1_, int p_76939_2_)
    {
        return p_76939_1_;
    }

    public Biome[] func_76937_a(Biome[] p_76937_1_, int p_76937_2_, int p_76937_3_, int p_76937_4_, int p_76937_5_)
    {
        IntCache.func_76446_a();

        if (p_76937_1_ == null || p_76937_1_.length < p_76937_4_ * p_76937_5_)
        {
            p_76937_1_ = new Biome[p_76937_4_ * p_76937_5_];
        }

        int[] aint = this.field_76944_d.func_75904_a(p_76937_2_, p_76937_3_, p_76937_4_, p_76937_5_);

        try
        {
            for (int i = 0; i < p_76937_4_ * p_76937_5_; ++i)
            {
                p_76937_1_[i] = Biome.func_180276_a(aint[i], Biomes.DEFAULT);
            }

            return p_76937_1_;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
            crashreportcategory.addDetail("biomes[] size", Integer.valueOf(p_76937_1_.length));
            crashreportcategory.addDetail("x", Integer.valueOf(p_76937_2_));
            crashreportcategory.addDetail("z", Integer.valueOf(p_76937_3_));
            crashreportcategory.addDetail("w", Integer.valueOf(p_76937_4_));
            crashreportcategory.addDetail("h", Integer.valueOf(p_76937_5_));
            throw new ReportedException(crashreport);
        }
    }

    public Biome[] func_76933_b(@Nullable Biome[] p_76933_1_, int p_76933_2_, int p_76933_3_, int p_76933_4_, int p_76933_5_)
    {
        return this.func_76931_a(p_76933_1_, p_76933_2_, p_76933_3_, p_76933_4_, p_76933_5_, true);
    }

    public Biome[] func_76931_a(@Nullable Biome[] p_76931_1_, int p_76931_2_, int p_76931_3_, int p_76931_4_, int p_76931_5_, boolean p_76931_6_)
    {
        IntCache.func_76446_a();

        if (p_76931_1_ == null || p_76931_1_.length < p_76931_4_ * p_76931_5_)
        {
            p_76931_1_ = new Biome[p_76931_4_ * p_76931_5_];
        }

        if (p_76931_6_ && p_76931_4_ == 16 && p_76931_5_ == 16 && (p_76931_2_ & 15) == 0 && (p_76931_3_ & 15) == 0)
        {
            Biome[] abiome = this.field_76942_f.func_76839_e(p_76931_2_, p_76931_3_);
            System.arraycopy(abiome, 0, p_76931_1_, 0, p_76931_4_ * p_76931_5_);
            return p_76931_1_;
        }
        else
        {
            int[] aint = this.field_76945_e.func_75904_a(p_76931_2_, p_76931_3_, p_76931_4_, p_76931_5_);

            for (int i = 0; i < p_76931_4_ * p_76931_5_; ++i)
            {
                p_76931_1_[i] = Biome.func_180276_a(aint[i], Biomes.DEFAULT);
            }

            return p_76931_1_;
        }
    }

    public boolean func_76940_a(int p_76940_1_, int p_76940_2_, int p_76940_3_, List<Biome> p_76940_4_)
    {
        IntCache.func_76446_a();
        int i = p_76940_1_ - p_76940_3_ >> 2;
        int j = p_76940_2_ - p_76940_3_ >> 2;
        int k = p_76940_1_ + p_76940_3_ >> 2;
        int l = p_76940_2_ + p_76940_3_ >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        int[] aint = this.field_76944_d.func_75904_a(i, j, i1, j1);

        try
        {
            for (int k1 = 0; k1 < i1 * j1; ++k1)
            {
                Biome biome = Biome.func_150568_d(aint[k1]);

                if (!p_76940_4_.contains(biome))
                {
                    return false;
                }
            }

            return true;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
            crashreportcategory.addDetail("Layer", this.field_76944_d.toString());
            crashreportcategory.addDetail("x", Integer.valueOf(p_76940_1_));
            crashreportcategory.addDetail("z", Integer.valueOf(p_76940_2_));
            crashreportcategory.addDetail("radius", Integer.valueOf(p_76940_3_));
            crashreportcategory.addDetail("allowed", p_76940_4_);
            throw new ReportedException(crashreport);
        }
    }

    @Nullable
    public BlockPos func_180630_a(int p_180630_1_, int p_180630_2_, int p_180630_3_, List<Biome> p_180630_4_, Random p_180630_5_)
    {
        IntCache.func_76446_a();
        int i = p_180630_1_ - p_180630_3_ >> 2;
        int j = p_180630_2_ - p_180630_3_ >> 2;
        int k = p_180630_1_ + p_180630_3_ >> 2;
        int l = p_180630_2_ + p_180630_3_ >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        int[] aint = this.field_76944_d.func_75904_a(i, j, i1, j1);
        BlockPos blockpos = null;
        int k1 = 0;

        for (int l1 = 0; l1 < i1 * j1; ++l1)
        {
            int i2 = i + l1 % i1 << 2;
            int j2 = j + l1 / i1 << 2;
            Biome biome = Biome.func_150568_d(aint[l1]);

            if (p_180630_4_.contains(biome) && (blockpos == null || p_180630_5_.nextInt(k1 + 1) == 0))
            {
                blockpos = new BlockPos(i2, 0, j2);
                ++k1;
            }
        }

        return blockpos;
    }

    public void func_76938_b()
    {
        this.field_76942_f.func_76838_a();
    }

    public boolean func_190944_c()
    {
        return this.field_190945_a != null && this.field_190945_a.field_177779_F >= 0;
    }

    public Biome func_190943_d()
    {
        return this.field_190945_a != null && this.field_190945_a.field_177779_F >= 0 ? Biome.func_185357_a(this.field_190945_a.field_177779_F) : null;
    }
}

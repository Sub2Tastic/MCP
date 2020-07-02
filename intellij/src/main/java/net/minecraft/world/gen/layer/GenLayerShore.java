package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.biome.BiomeMesa;

public class GenLayerShore extends GenLayer
{
    public GenLayerShore(long p_i2130_1_, GenLayer p_i2130_3_)
    {
        super(p_i2130_1_);
        this.field_75909_a = p_i2130_3_;
    }

    public int[] func_75904_a(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_)
    {
        int[] aint = this.field_75909_a.func_75904_a(p_75904_1_ - 1, p_75904_2_ - 1, p_75904_3_ + 2, p_75904_4_ + 2);
        int[] aint1 = IntCache.func_76445_a(p_75904_3_ * p_75904_4_);

        for (int i = 0; i < p_75904_4_; ++i)
        {
            for (int j = 0; j < p_75904_3_; ++j)
            {
                this.func_75903_a((long)(j + p_75904_1_), (long)(i + p_75904_2_));
                int k = aint[j + 1 + (i + 1) * (p_75904_3_ + 2)];
                Biome biome = Biome.func_150568_d(k);

                if (k == Biome.func_185362_a(Biomes.MUSHROOM_FIELDS))
                {
                    int j2 = aint[j + 1 + (i + 1 - 1) * (p_75904_3_ + 2)];
                    int i3 = aint[j + 1 + 1 + (i + 1) * (p_75904_3_ + 2)];
                    int l3 = aint[j + 1 - 1 + (i + 1) * (p_75904_3_ + 2)];
                    int k4 = aint[j + 1 + (i + 1 + 1) * (p_75904_3_ + 2)];

                    if (j2 != Biome.func_185362_a(Biomes.OCEAN) && i3 != Biome.func_185362_a(Biomes.OCEAN) && l3 != Biome.func_185362_a(Biomes.OCEAN) && k4 != Biome.func_185362_a(Biomes.OCEAN))
                    {
                        aint1[j + i * p_75904_3_] = k;
                    }
                    else
                    {
                        aint1[j + i * p_75904_3_] = Biome.func_185362_a(Biomes.MUSHROOM_FIELD_SHORE);
                    }
                }
                else if (biome != null && biome.func_150562_l() == BiomeJungle.class)
                {
                    int i2 = aint[j + 1 + (i + 1 - 1) * (p_75904_3_ + 2)];
                    int l2 = aint[j + 1 + 1 + (i + 1) * (p_75904_3_ + 2)];
                    int k3 = aint[j + 1 - 1 + (i + 1) * (p_75904_3_ + 2)];
                    int j4 = aint[j + 1 + (i + 1 + 1) * (p_75904_3_ + 2)];

                    if (this.isJungleCompatible(i2) && this.isJungleCompatible(l2) && this.isJungleCompatible(k3) && this.isJungleCompatible(j4))
                    {
                        if (!func_151618_b(i2) && !func_151618_b(l2) && !func_151618_b(k3) && !func_151618_b(j4))
                        {
                            aint1[j + i * p_75904_3_] = k;
                        }
                        else
                        {
                            aint1[j + i * p_75904_3_] = Biome.func_185362_a(Biomes.BEACH);
                        }
                    }
                    else
                    {
                        aint1[j + i * p_75904_3_] = Biome.func_185362_a(Biomes.JUNGLE_EDGE);
                    }
                }
                else if (k != Biome.func_185362_a(Biomes.MOUNTAINS) && k != Biome.func_185362_a(Biomes.WOODED_MOUNTAINS) && k != Biome.func_185362_a(Biomes.MOUNTAIN_EDGE))
                {
                    if (biome != null && biome.func_150559_j())
                    {
                        this.func_151632_a(aint, aint1, j, i, p_75904_3_, k, Biome.func_185362_a(Biomes.SNOWY_BEACH));
                    }
                    else if (k != Biome.func_185362_a(Biomes.BADLANDS) && k != Biome.func_185362_a(Biomes.WOODED_BADLANDS_PLATEAU))
                    {
                        if (k != Biome.func_185362_a(Biomes.OCEAN) && k != Biome.func_185362_a(Biomes.DEEP_OCEAN) && k != Biome.func_185362_a(Biomes.RIVER) && k != Biome.func_185362_a(Biomes.SWAMP))
                        {
                            int l1 = aint[j + 1 + (i + 1 - 1) * (p_75904_3_ + 2)];
                            int k2 = aint[j + 1 + 1 + (i + 1) * (p_75904_3_ + 2)];
                            int j3 = aint[j + 1 - 1 + (i + 1) * (p_75904_3_ + 2)];
                            int i4 = aint[j + 1 + (i + 1 + 1) * (p_75904_3_ + 2)];

                            if (!func_151618_b(l1) && !func_151618_b(k2) && !func_151618_b(j3) && !func_151618_b(i4))
                            {
                                aint1[j + i * p_75904_3_] = k;
                            }
                            else
                            {
                                aint1[j + i * p_75904_3_] = Biome.func_185362_a(Biomes.BEACH);
                            }
                        }
                        else
                        {
                            aint1[j + i * p_75904_3_] = k;
                        }
                    }
                    else
                    {
                        int l = aint[j + 1 + (i + 1 - 1) * (p_75904_3_ + 2)];
                        int i1 = aint[j + 1 + 1 + (i + 1) * (p_75904_3_ + 2)];
                        int j1 = aint[j + 1 - 1 + (i + 1) * (p_75904_3_ + 2)];
                        int k1 = aint[j + 1 + (i + 1 + 1) * (p_75904_3_ + 2)];

                        if (!func_151618_b(l) && !func_151618_b(i1) && !func_151618_b(j1) && !func_151618_b(k1))
                        {
                            if (this.isMesa(l) && this.isMesa(i1) && this.isMesa(j1) && this.isMesa(k1))
                            {
                                aint1[j + i * p_75904_3_] = k;
                            }
                            else
                            {
                                aint1[j + i * p_75904_3_] = Biome.func_185362_a(Biomes.DESERT);
                            }
                        }
                        else
                        {
                            aint1[j + i * p_75904_3_] = k;
                        }
                    }
                }
                else
                {
                    this.func_151632_a(aint, aint1, j, i, p_75904_3_, k, Biome.func_185362_a(Biomes.STONE_SHORE));
                }
            }
        }

        return aint1;
    }

    private void func_151632_a(int[] p_151632_1_, int[] p_151632_2_, int p_151632_3_, int p_151632_4_, int p_151632_5_, int p_151632_6_, int p_151632_7_)
    {
        if (func_151618_b(p_151632_6_))
        {
            p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
        }
        else
        {
            int i = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 - 1) * (p_151632_5_ + 2)];
            int j = p_151632_1_[p_151632_3_ + 1 + 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
            int k = p_151632_1_[p_151632_3_ + 1 - 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
            int l = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 + 1) * (p_151632_5_ + 2)];

            if (!func_151618_b(i) && !func_151618_b(j) && !func_151618_b(k) && !func_151618_b(l))
            {
                p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
            }
            else
            {
                p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_7_;
            }
        }
    }

    private boolean isJungleCompatible(int p_151631_1_)
    {
        if (Biome.func_150568_d(p_151631_1_) != null && Biome.func_150568_d(p_151631_1_).func_150562_l() == BiomeJungle.class)
        {
            return true;
        }
        else
        {
            return p_151631_1_ == Biome.func_185362_a(Biomes.JUNGLE_EDGE) || p_151631_1_ == Biome.func_185362_a(Biomes.JUNGLE) || p_151631_1_ == Biome.func_185362_a(Biomes.JUNGLE_HILLS) || p_151631_1_ == Biome.func_185362_a(Biomes.FOREST) || p_151631_1_ == Biome.func_185362_a(Biomes.TAIGA) || func_151618_b(p_151631_1_);
        }
    }

    private boolean isMesa(int p_151633_1_)
    {
        return Biome.func_150568_d(p_151633_1_) instanceof BiomeMesa;
    }
}

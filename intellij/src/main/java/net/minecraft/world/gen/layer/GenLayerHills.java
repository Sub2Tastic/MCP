package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenLayerHills extends GenLayer
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final GenLayer field_151628_d;

    public GenLayerHills(long p_i45479_1_, GenLayer p_i45479_3_, GenLayer p_i45479_4_)
    {
        super(p_i45479_1_);
        this.field_75909_a = p_i45479_3_;
        this.field_151628_d = p_i45479_4_;
    }

    public int[] func_75904_a(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_)
    {
        int[] aint = this.field_75909_a.func_75904_a(p_75904_1_ - 1, p_75904_2_ - 1, p_75904_3_ + 2, p_75904_4_ + 2);
        int[] aint1 = this.field_151628_d.func_75904_a(p_75904_1_ - 1, p_75904_2_ - 1, p_75904_3_ + 2, p_75904_4_ + 2);
        int[] aint2 = IntCache.func_76445_a(p_75904_3_ * p_75904_4_);

        for (int i = 0; i < p_75904_4_; ++i)
        {
            for (int j = 0; j < p_75904_3_; ++j)
            {
                this.func_75903_a((long)(j + p_75904_1_), (long)(i + p_75904_2_));
                int k = aint[j + 1 + (i + 1) * (p_75904_3_ + 2)];
                int l = aint1[j + 1 + (i + 1) * (p_75904_3_ + 2)];
                boolean flag = (l - 2) % 29 == 0;

                if (k > 255)
                {
                    LOGGER.debug("old! {}", (int)k);
                }

                Biome biome = Biome.func_185357_a(k);
                boolean flag1 = biome != null && biome.isMutation();

                if (k != 0 && l >= 2 && (l - 2) % 29 == 1 && !flag1)
                {
                    Biome biome3 = Biome.getMutationForBiome(biome);
                    aint2[j + i * p_75904_3_] = biome3 == null ? k : Biome.func_185362_a(biome3);
                }
                else if (this.func_75902_a(3) != 0 && !flag)
                {
                    aint2[j + i * p_75904_3_] = k;
                }
                else
                {
                    Biome biome1 = biome;

                    if (biome == Biomes.DESERT)
                    {
                        biome1 = Biomes.DESERT_HILLS;
                    }
                    else if (biome == Biomes.FOREST)
                    {
                        biome1 = Biomes.WOODED_HILLS;
                    }
                    else if (biome == Biomes.BIRCH_FOREST)
                    {
                        biome1 = Biomes.BIRCH_FOREST_HILLS;
                    }
                    else if (biome == Biomes.DARK_FOREST)
                    {
                        biome1 = Biomes.PLAINS;
                    }
                    else if (biome == Biomes.TAIGA)
                    {
                        biome1 = Biomes.TAIGA_HILLS;
                    }
                    else if (biome == Biomes.GIANT_TREE_TAIGA)
                    {
                        biome1 = Biomes.GIANT_TREE_TAIGA_HILLS;
                    }
                    else if (biome == Biomes.SNOWY_TAIGA)
                    {
                        biome1 = Biomes.SNOWY_TAIGA_HILLS;
                    }
                    else if (biome == Biomes.PLAINS)
                    {
                        if (this.func_75902_a(3) == 0)
                        {
                            biome1 = Biomes.WOODED_HILLS;
                        }
                        else
                        {
                            biome1 = Biomes.FOREST;
                        }
                    }
                    else if (biome == Biomes.SNOWY_TUNDRA)
                    {
                        biome1 = Biomes.SNOWY_MOUNTAINS;
                    }
                    else if (biome == Biomes.JUNGLE)
                    {
                        biome1 = Biomes.JUNGLE_HILLS;
                    }
                    else if (biome == Biomes.OCEAN)
                    {
                        biome1 = Biomes.DEEP_OCEAN;
                    }
                    else if (biome == Biomes.MOUNTAINS)
                    {
                        biome1 = Biomes.WOODED_MOUNTAINS;
                    }
                    else if (biome == Biomes.SAVANNA)
                    {
                        biome1 = Biomes.SAVANNA_PLATEAU;
                    }
                    else if (func_151616_a(k, Biome.func_185362_a(Biomes.WOODED_BADLANDS_PLATEAU)))
                    {
                        biome1 = Biomes.BADLANDS;
                    }
                    else if (biome == Biomes.DEEP_OCEAN && this.func_75902_a(3) == 0)
                    {
                        int i1 = this.func_75902_a(2);

                        if (i1 == 0)
                        {
                            biome1 = Biomes.PLAINS;
                        }
                        else
                        {
                            biome1 = Biomes.FOREST;
                        }
                    }

                    int j2 = Biome.func_185362_a(biome1);

                    if (flag && j2 != k)
                    {
                        Biome biome2 = Biome.getMutationForBiome(biome1);
                        j2 = biome2 == null ? k : Biome.func_185362_a(biome2);
                    }

                    if (j2 == k)
                    {
                        aint2[j + i * p_75904_3_] = k;
                    }
                    else
                    {
                        int k2 = aint[j + 1 + (i + 0) * (p_75904_3_ + 2)];
                        int j1 = aint[j + 2 + (i + 1) * (p_75904_3_ + 2)];
                        int k1 = aint[j + 0 + (i + 1) * (p_75904_3_ + 2)];
                        int l1 = aint[j + 1 + (i + 2) * (p_75904_3_ + 2)];
                        int i2 = 0;

                        if (func_151616_a(k2, k))
                        {
                            ++i2;
                        }

                        if (func_151616_a(j1, k))
                        {
                            ++i2;
                        }

                        if (func_151616_a(k1, k))
                        {
                            ++i2;
                        }

                        if (func_151616_a(l1, k))
                        {
                            ++i2;
                        }

                        if (i2 >= 3)
                        {
                            aint2[j + i * p_75904_3_] = j2;
                        }
                        else
                        {
                            aint2[j + i * p_75904_3_] = k;
                        }
                    }
                }
            }
        }

        return aint2;
    }
}

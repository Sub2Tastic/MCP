package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;

public class GenLayerBiome extends GenLayer
{
    private Biome[] warmBiomes = new Biome[] {Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.SAVANNA, Biomes.SAVANNA, Biomes.PLAINS};
    private final Biome[] field_151621_d = new Biome[] {Biomes.FOREST, Biomes.DARK_FOREST, Biomes.MOUNTAINS, Biomes.PLAINS, Biomes.BIRCH_FOREST, Biomes.SWAMP};
    private final Biome[] field_151622_e = new Biome[] {Biomes.FOREST, Biomes.MOUNTAINS, Biomes.TAIGA, Biomes.PLAINS};
    private final Biome[] field_151620_f = new Biome[] {Biomes.SNOWY_TUNDRA, Biomes.SNOWY_TUNDRA, Biomes.SNOWY_TUNDRA, Biomes.SNOWY_TAIGA};
    private final ChunkGeneratorSettings field_175973_g;

    public GenLayerBiome(long p_i45560_1_, GenLayer p_i45560_3_, WorldType p_i45560_4_, ChunkGeneratorSettings p_i45560_5_)
    {
        super(p_i45560_1_);
        this.field_75909_a = p_i45560_3_;

        if (p_i45560_4_ == WorldType.DEFAULT_1_1)
        {
            this.warmBiomes = new Biome[] {Biomes.DESERT, Biomes.FOREST, Biomes.MOUNTAINS, Biomes.SWAMP, Biomes.PLAINS, Biomes.TAIGA};
            this.field_175973_g = null;
        }
        else
        {
            this.field_175973_g = p_i45560_5_;
        }
    }

    public int[] func_75904_a(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_)
    {
        int[] aint = this.field_75909_a.func_75904_a(p_75904_1_, p_75904_2_, p_75904_3_, p_75904_4_);
        int[] aint1 = IntCache.func_76445_a(p_75904_3_ * p_75904_4_);

        for (int i = 0; i < p_75904_4_; ++i)
        {
            for (int j = 0; j < p_75904_3_; ++j)
            {
                this.func_75903_a((long)(j + p_75904_1_), (long)(i + p_75904_2_));
                int k = aint[j + i * p_75904_3_];
                int l = (k & 3840) >> 8;
                k = k & -3841;

                if (this.field_175973_g != null && this.field_175973_g.field_177779_F >= 0)
                {
                    aint1[j + i * p_75904_3_] = this.field_175973_g.field_177779_F;
                }
                else if (func_151618_b(k))
                {
                    aint1[j + i * p_75904_3_] = k;
                }
                else if (k == Biome.func_185362_a(Biomes.MUSHROOM_FIELDS))
                {
                    aint1[j + i * p_75904_3_] = k;
                }
                else if (k == 1)
                {
                    if (l > 0)
                    {
                        if (this.func_75902_a(3) == 0)
                        {
                            aint1[j + i * p_75904_3_] = Biome.func_185362_a(Biomes.BADLANDS_PLATEAU);
                        }
                        else
                        {
                            aint1[j + i * p_75904_3_] = Biome.func_185362_a(Biomes.WOODED_BADLANDS_PLATEAU);
                        }
                    }
                    else
                    {
                        aint1[j + i * p_75904_3_] = Biome.func_185362_a(this.warmBiomes[this.func_75902_a(this.warmBiomes.length)]);
                    }
                }
                else if (k == 2)
                {
                    if (l > 0)
                    {
                        aint1[j + i * p_75904_3_] = Biome.func_185362_a(Biomes.JUNGLE);
                    }
                    else
                    {
                        aint1[j + i * p_75904_3_] = Biome.func_185362_a(this.field_151621_d[this.func_75902_a(this.field_151621_d.length)]);
                    }
                }
                else if (k == 3)
                {
                    if (l > 0)
                    {
                        aint1[j + i * p_75904_3_] = Biome.func_185362_a(Biomes.GIANT_TREE_TAIGA);
                    }
                    else
                    {
                        aint1[j + i * p_75904_3_] = Biome.func_185362_a(this.field_151622_e[this.func_75902_a(this.field_151622_e.length)]);
                    }
                }
                else if (k == 4)
                {
                    aint1[j + i * p_75904_3_] = Biome.func_185362_a(this.field_151620_f[this.func_75902_a(this.field_151620_f.length)]);
                }
                else
                {
                    aint1[j + i * p_75904_3_] = Biome.func_185362_a(Biomes.MUSHROOM_FIELDS);
                }
            }
        }

        return aint1;
    }
}

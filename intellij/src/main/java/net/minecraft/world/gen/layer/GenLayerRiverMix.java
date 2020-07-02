package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class GenLayerRiverMix extends GenLayer
{
    private final GenLayer field_75910_b;
    private final GenLayer field_75911_c;

    public GenLayerRiverMix(long p_i2129_1_, GenLayer p_i2129_3_, GenLayer p_i2129_4_)
    {
        super(p_i2129_1_);
        this.field_75910_b = p_i2129_3_;
        this.field_75911_c = p_i2129_4_;
    }

    public void func_75905_a(long p_75905_1_)
    {
        this.field_75910_b.func_75905_a(p_75905_1_);
        this.field_75911_c.func_75905_a(p_75905_1_);
        super.func_75905_a(p_75905_1_);
    }

    public int[] func_75904_a(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_)
    {
        int[] aint = this.field_75910_b.func_75904_a(p_75904_1_, p_75904_2_, p_75904_3_, p_75904_4_);
        int[] aint1 = this.field_75911_c.func_75904_a(p_75904_1_, p_75904_2_, p_75904_3_, p_75904_4_);
        int[] aint2 = IntCache.func_76445_a(p_75904_3_ * p_75904_4_);

        for (int i = 0; i < p_75904_3_ * p_75904_4_; ++i)
        {
            if (aint[i] != Biome.func_185362_a(Biomes.OCEAN) && aint[i] != Biome.func_185362_a(Biomes.DEEP_OCEAN))
            {
                if (aint1[i] == Biome.func_185362_a(Biomes.RIVER))
                {
                    if (aint[i] == Biome.func_185362_a(Biomes.SNOWY_TUNDRA))
                    {
                        aint2[i] = Biome.func_185362_a(Biomes.FROZEN_RIVER);
                    }
                    else if (aint[i] != Biome.func_185362_a(Biomes.MUSHROOM_FIELDS) && aint[i] != Biome.func_185362_a(Biomes.MUSHROOM_FIELD_SHORE))
                    {
                        aint2[i] = aint1[i] & 255;
                    }
                    else
                    {
                        aint2[i] = Biome.func_185362_a(Biomes.MUSHROOM_FIELD_SHORE);
                    }
                }
                else
                {
                    aint2[i] = aint[i];
                }
            }
            else
            {
                aint2[i] = aint[i];
            }
        }

        return aint2;
    }
}

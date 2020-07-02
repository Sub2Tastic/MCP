package net.minecraft.world.biome;

public class BiomeOcean extends Biome
{
    public BiomeOcean(Biome.BiomeProperties p_i46700_1_)
    {
        super(p_i46700_1_);
        this.field_76762_K.clear();
    }

    public Biome.TempCategory getTempCategory()
    {
        return Biome.TempCategory.OCEAN;
    }
}

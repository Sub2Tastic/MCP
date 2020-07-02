package net.minecraft.world;

public class WorldProviderSurface extends WorldProvider
{
    public DimensionType getType()
    {
        return DimensionType.OVERWORLD;
    }

    public boolean func_186056_c(int p_186056_1_, int p_186056_2_)
    {
        return !this.world.func_72916_c(p_186056_1_, p_186056_2_);
    }
}

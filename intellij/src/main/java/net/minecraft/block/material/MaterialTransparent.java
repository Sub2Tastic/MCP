package net.minecraft.block.material;

public class MaterialTransparent extends Material
{
    public MaterialTransparent(MapColor p_i2113_1_)
    {
        super(p_i2113_1_);
        this.func_76231_i();
    }

    /**
     * Returns true if the block is a considered solid. This is true by default.
     */
    public boolean isSolid()
    {
        return false;
    }

    public boolean func_76228_b()
    {
        return false;
    }

    /**
     * Returns if this material is considered solid or not
     */
    public boolean blocksMovement()
    {
        return false;
    }
}

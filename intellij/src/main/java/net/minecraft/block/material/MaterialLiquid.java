package net.minecraft.block.material;

public class MaterialLiquid extends Material
{
    public MaterialLiquid(MapColor p_i2114_1_)
    {
        super(p_i2114_1_);
        this.func_76231_i();
        this.func_76219_n();
    }

    /**
     * Returns if blocks of these materials are liquids.
     */
    public boolean isLiquid()
    {
        return true;
    }

    /**
     * Returns if this material is considered solid or not
     */
    public boolean blocksMovement()
    {
        return false;
    }

    /**
     * Returns true if the block is a considered solid. This is true by default.
     */
    public boolean isSolid()
    {
        return false;
    }
}

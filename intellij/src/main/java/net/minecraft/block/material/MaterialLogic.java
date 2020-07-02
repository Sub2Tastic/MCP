package net.minecraft.block.material;

public class MaterialLogic extends Material
{
    public MaterialLogic(MapColor p_i2112_1_)
    {
        super(p_i2112_1_);
        this.func_85158_p();
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

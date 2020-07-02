package net.minecraft.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class FlatLayerInfo
{
    private final int field_175902_a;
    private IBlockState layerMaterial;
    private int layerCount;
    private int layerMinimumY;

    public FlatLayerInfo(int p_i45467_1_, Block layerMaterialIn)
    {
        this(3, p_i45467_1_, layerMaterialIn);
    }

    public FlatLayerInfo(int p_i45627_1_, int p_i45627_2_, Block p_i45627_3_)
    {
        this.layerCount = 1;
        this.field_175902_a = p_i45627_1_;
        this.layerCount = p_i45627_2_;
        this.layerMaterial = p_i45627_3_.getDefaultState();
    }

    public FlatLayerInfo(int p_i45628_1_, int p_i45628_2_, Block p_i45628_3_, int p_i45628_4_)
    {
        this(p_i45628_1_, p_i45628_2_, p_i45628_3_);
        this.layerMaterial = p_i45628_3_.func_176203_a(p_i45628_4_);
    }

    /**
     * Return the amount of layers for this set of layers.
     */
    public int getLayerCount()
    {
        return this.layerCount;
    }

    public IBlockState getLayerMaterial()
    {
        return this.layerMaterial;
    }

    private Block func_151536_b()
    {
        return this.layerMaterial.getBlock();
    }

    private int func_82658_c()
    {
        return this.layerMaterial.getBlock().func_176201_c(this.layerMaterial);
    }

    /**
     * Return the minimum Y coordinate for this layer, set during generation.
     */
    public int getMinY()
    {
        return this.layerMinimumY;
    }

    /**
     * Set the minimum Y coordinate for this layer.
     */
    public void setMinY(int minY)
    {
        this.layerMinimumY = minY;
    }

    public String toString()
    {
        String s;

        if (this.field_175902_a >= 3)
        {
            ResourceLocation resourcelocation = Block.field_149771_c.getKey(this.func_151536_b());
            s = resourcelocation == null ? "null" : resourcelocation.toString();

            if (this.layerCount > 1)
            {
                s = this.layerCount + "*" + s;
            }
        }
        else
        {
            s = Integer.toString(Block.func_149682_b(this.func_151536_b()));

            if (this.layerCount > 1)
            {
                s = this.layerCount + "x" + s;
            }
        }

        int i = this.func_82658_c();

        if (i > 0)
        {
            s = s + ":" + i;
        }

        return s;
    }
}

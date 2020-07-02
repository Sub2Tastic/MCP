package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenAbstractTree extends WorldGenerator
{
    public WorldGenAbstractTree(boolean p_i45448_1_)
    {
        super(p_i45448_1_);
    }

    protected boolean func_150523_a(Block p_150523_1_)
    {
        Material material = p_150523_1_.getDefaultState().getMaterial();
        return material == Material.AIR || material == Material.LEAVES || p_150523_1_ == Blocks.GRASS || p_150523_1_ == Blocks.DIRT || p_150523_1_ == Blocks.field_150364_r || p_150523_1_ == Blocks.field_150363_s || p_150523_1_ == Blocks.field_150345_g || p_150523_1_ == Blocks.VINE;
    }

    public void func_180711_a(World p_180711_1_, Random p_180711_2_, BlockPos p_180711_3_)
    {
    }

    protected void func_175921_a(World p_175921_1_, BlockPos p_175921_2_)
    {
        if (p_175921_1_.getBlockState(p_175921_2_).getBlock() != Blocks.DIRT)
        {
            this.func_175903_a(p_175921_1_, p_175921_2_, Blocks.DIRT.getDefaultState());
        }
    }
}

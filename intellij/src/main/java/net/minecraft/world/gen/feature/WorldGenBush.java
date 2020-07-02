package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBush extends WorldGenerator
{
    private final BlockBush field_175908_a;

    public WorldGenBush(BlockBush p_i45633_1_)
    {
        this.field_175908_a = p_i45633_1_;
    }

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (int i = 0; i < 64; ++i)
        {
            BlockPos blockpos = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));

            if (p_180709_1_.isAirBlock(blockpos) && (!p_180709_1_.dimension.isNether() || blockpos.getY() < 255) && this.field_175908_a.func_180671_f(p_180709_1_, blockpos, this.field_175908_a.getDefaultState()))
            {
                p_180709_1_.setBlockState(blockpos, this.field_175908_a.getDefaultState(), 2);
            }
        }

        return true;
    }
}

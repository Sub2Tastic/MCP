package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenHellLava extends WorldGenerator
{
    private final Block field_150553_a;
    private final boolean field_94524_b;

    public WorldGenHellLava(Block p_i45453_1_, boolean p_i45453_2_)
    {
        this.field_150553_a = p_i45453_1_;
        this.field_94524_b = p_i45453_2_;
    }

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        if (p_180709_1_.getBlockState(p_180709_3_.up()).getBlock() != Blocks.NETHERRACK)
        {
            return false;
        }
        else if (p_180709_1_.getBlockState(p_180709_3_).getMaterial() != Material.AIR && p_180709_1_.getBlockState(p_180709_3_).getBlock() != Blocks.NETHERRACK)
        {
            return false;
        }
        else
        {
            int i = 0;

            if (p_180709_1_.getBlockState(p_180709_3_.west()).getBlock() == Blocks.NETHERRACK)
            {
                ++i;
            }

            if (p_180709_1_.getBlockState(p_180709_3_.east()).getBlock() == Blocks.NETHERRACK)
            {
                ++i;
            }

            if (p_180709_1_.getBlockState(p_180709_3_.north()).getBlock() == Blocks.NETHERRACK)
            {
                ++i;
            }

            if (p_180709_1_.getBlockState(p_180709_3_.south()).getBlock() == Blocks.NETHERRACK)
            {
                ++i;
            }

            if (p_180709_1_.getBlockState(p_180709_3_.down()).getBlock() == Blocks.NETHERRACK)
            {
                ++i;
            }

            int j = 0;

            if (p_180709_1_.isAirBlock(p_180709_3_.west()))
            {
                ++j;
            }

            if (p_180709_1_.isAirBlock(p_180709_3_.east()))
            {
                ++j;
            }

            if (p_180709_1_.isAirBlock(p_180709_3_.north()))
            {
                ++j;
            }

            if (p_180709_1_.isAirBlock(p_180709_3_.south()))
            {
                ++j;
            }

            if (p_180709_1_.isAirBlock(p_180709_3_.down()))
            {
                ++j;
            }

            if (!this.field_94524_b && i == 4 && j == 1 || i == 5)
            {
                IBlockState iblockstate = this.field_150553_a.getDefaultState();
                p_180709_1_.setBlockState(p_180709_3_, iblockstate, 2);
                p_180709_1_.func_189507_a(p_180709_3_, iblockstate, p_180709_2_);
            }

            return true;
        }
    }
}

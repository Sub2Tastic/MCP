package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenLiquids extends WorldGenerator
{
    private final Block field_150521_a;

    public WorldGenLiquids(Block p_i45465_1_)
    {
        this.field_150521_a = p_i45465_1_;
    }

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        if (p_180709_1_.getBlockState(p_180709_3_.up()).getBlock() != Blocks.STONE)
        {
            return false;
        }
        else if (p_180709_1_.getBlockState(p_180709_3_.down()).getBlock() != Blocks.STONE)
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = p_180709_1_.getBlockState(p_180709_3_);

            if (iblockstate.getMaterial() != Material.AIR && iblockstate.getBlock() != Blocks.STONE)
            {
                return false;
            }
            else
            {
                int i = 0;

                if (p_180709_1_.getBlockState(p_180709_3_.west()).getBlock() == Blocks.STONE)
                {
                    ++i;
                }

                if (p_180709_1_.getBlockState(p_180709_3_.east()).getBlock() == Blocks.STONE)
                {
                    ++i;
                }

                if (p_180709_1_.getBlockState(p_180709_3_.north()).getBlock() == Blocks.STONE)
                {
                    ++i;
                }

                if (p_180709_1_.getBlockState(p_180709_3_.south()).getBlock() == Blocks.STONE)
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

                if (i == 3 && j == 1)
                {
                    IBlockState iblockstate1 = this.field_150521_a.getDefaultState();
                    p_180709_1_.setBlockState(p_180709_3_, iblockstate1, 2);
                    p_180709_1_.func_189507_a(p_180709_3_, iblockstate1, p_180709_2_);
                }

                return true;
            }
        }
    }
}

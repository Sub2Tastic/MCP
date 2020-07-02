package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BiomeVoidDecorator extends BiomeDecorator
{
    public void func_180292_a(World p_180292_1_, Random p_180292_2_, Biome p_180292_3_, BlockPos p_180292_4_)
    {
        BlockPos blockpos = p_180292_1_.getSpawnPoint();
        int i = 16;
        double d0 = blockpos.distanceSq(p_180292_4_.add(8, blockpos.getY(), 8));

        if (d0 <= 1024.0D)
        {
            BlockPos blockpos1 = new BlockPos(blockpos.getX() - 16, blockpos.getY() - 1, blockpos.getZ() - 16);
            BlockPos blockpos2 = new BlockPos(blockpos.getX() + 16, blockpos.getY() - 1, blockpos.getZ() + 16);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(blockpos1);

            for (int j = p_180292_4_.getZ(); j < p_180292_4_.getZ() + 16; ++j)
            {
                for (int k = p_180292_4_.getX(); k < p_180292_4_.getX() + 16; ++k)
                {
                    if (j >= blockpos1.getZ() && j <= blockpos2.getZ() && k >= blockpos1.getX() && k <= blockpos2.getX())
                    {
                        blockpos$mutableblockpos.setPos(k, blockpos$mutableblockpos.getY(), j);

                        if (blockpos.getX() == k && blockpos.getZ() == j)
                        {
                            p_180292_1_.setBlockState(blockpos$mutableblockpos, Blocks.COBBLESTONE.getDefaultState(), 2);
                        }
                        else
                        {
                            p_180292_1_.setBlockState(blockpos$mutableblockpos, Blocks.STONE.getDefaultState(), 2);
                        }
                    }
                }
            }
        }
    }
}

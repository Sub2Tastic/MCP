package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenEndGateway extends WorldGenerator
{
    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.func_177975_b(p_180709_3_.add(-1, -2, -1), p_180709_3_.add(1, 2, 1)))
        {
            boolean flag = blockpos$mutableblockpos.getX() == p_180709_3_.getX();
            boolean flag1 = blockpos$mutableblockpos.getY() == p_180709_3_.getY();
            boolean flag2 = blockpos$mutableblockpos.getZ() == p_180709_3_.getZ();
            boolean flag3 = Math.abs(blockpos$mutableblockpos.getY() - p_180709_3_.getY()) == 2;

            if (flag && flag1 && flag2)
            {
                this.func_175903_a(p_180709_1_, new BlockPos(blockpos$mutableblockpos), Blocks.END_GATEWAY.getDefaultState());
            }
            else if (flag1)
            {
                this.func_175903_a(p_180709_1_, blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
            }
            else if (flag3 && flag && flag2)
            {
                this.func_175903_a(p_180709_1_, blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
            }
            else if ((flag || flag2) && !flag3)
            {
                this.func_175903_a(p_180709_1_, blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
            }
            else
            {
                this.func_175903_a(p_180709_1_, blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
            }
        }

        return true;
    }
}

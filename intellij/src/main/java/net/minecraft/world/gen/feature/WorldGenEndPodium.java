package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenEndPodium extends WorldGenerator
{
    public static final BlockPos END_PODIUM_LOCATION = BlockPos.ZERO;
    public static final BlockPos field_186140_b = new BlockPos(END_PODIUM_LOCATION.getX() - 4 & -16, 0, END_PODIUM_LOCATION.getZ() - 4 & -16);
    private final boolean activePortal;

    public WorldGenEndPodium(boolean activePortalIn)
    {
        this.activePortal = activePortalIn;
    }

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.func_177975_b(new BlockPos(p_180709_3_.getX() - 4, p_180709_3_.getY() - 1, p_180709_3_.getZ() - 4), new BlockPos(p_180709_3_.getX() + 4, p_180709_3_.getY() + 32, p_180709_3_.getZ() + 4)))
        {
            double d0 = blockpos$mutableblockpos.func_185332_f(p_180709_3_.getX(), blockpos$mutableblockpos.getY(), p_180709_3_.getZ());

            if (d0 <= 3.5D)
            {
                if (blockpos$mutableblockpos.getY() < p_180709_3_.getY())
                {
                    if (d0 <= 2.5D)
                    {
                        this.func_175903_a(p_180709_1_, blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
                    }
                    else if (blockpos$mutableblockpos.getY() < p_180709_3_.getY())
                    {
                        this.func_175903_a(p_180709_1_, blockpos$mutableblockpos, Blocks.END_STONE.getDefaultState());
                    }
                }
                else if (blockpos$mutableblockpos.getY() > p_180709_3_.getY())
                {
                    this.func_175903_a(p_180709_1_, blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
                }
                else if (d0 > 2.5D)
                {
                    this.func_175903_a(p_180709_1_, blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
                }
                else if (this.activePortal)
                {
                    this.func_175903_a(p_180709_1_, new BlockPos(blockpos$mutableblockpos), Blocks.END_PORTAL.getDefaultState());
                }
                else
                {
                    this.func_175903_a(p_180709_1_, new BlockPos(blockpos$mutableblockpos), Blocks.AIR.getDefaultState());
                }
            }
        }

        for (int i = 0; i < 4; ++i)
        {
            this.func_175903_a(p_180709_1_, p_180709_3_.up(i), Blocks.BEDROCK.getDefaultState());
        }

        BlockPos blockpos = p_180709_3_.up(2);

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            this.func_175903_a(p_180709_1_, blockpos.offset(enumfacing), Blocks.TORCH.getDefaultState().func_177226_a(BlockTorch.field_176596_a, enumfacing));
        }

        return true;
    }
}

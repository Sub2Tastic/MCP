package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockBreakable extends Block
{
    private final boolean field_149996_a;

    protected BlockBreakable(Material p_i45712_1_, boolean p_i45712_2_)
    {
        this(p_i45712_1_, p_i45712_2_, p_i45712_1_.getColor());
    }

    protected BlockBreakable(Material p_i46393_1_, boolean p_i46393_2_, MapColor p_i46393_3_)
    {
        super(p_i46393_1_, p_i46393_3_);
        this.field_149996_a = p_i46393_2_;
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    /**
     * ""
     */
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing p_176225_4_)
    {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(p_176225_4_));
        Block block = iblockstate.getBlock();

        if (this == Blocks.GLASS || this == Blocks.field_150399_cn)
        {
            if (blockState != iblockstate)
            {
                return true;
            }

            if (block == this)
            {
                return false;
            }
        }

        return !this.field_149996_a && block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, p_176225_4_);
    }
}

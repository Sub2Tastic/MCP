package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHay extends BlockRotatedPillar
{
    public BlockHay()
    {
        super(Material.ORGANIC, MapColor.YELLOW);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(AXIS, EnumFacing.Axis.Y));
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    /**
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        entityIn.func_180430_e(fallDistance, 0.2F);
    }
}

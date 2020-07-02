package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSlime extends BlockBreakable
{
    public BlockSlime()
    {
        super(Material.CLAY, false, MapColor.GRASS);
        this.func_149647_a(CreativeTabs.DECORATIONS);
        this.slipperiness = 0.8F;
    }

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    /**
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        if (entityIn.func_70093_af())
        {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        }
        else
        {
            entityIn.func_180430_e(fallDistance, 0.0F);
        }
    }

    /**
     * Called when an Entity lands on this Block. This method *must* update motionY because the entity will not do that
     * on its own
     */
    public void onLanded(World worldIn, Entity entityIn)
    {
        if (entityIn.func_70093_af())
        {
            super.onLanded(worldIn, entityIn);
        }
        else if (entityIn.field_70181_x < 0.0D)
        {
            entityIn.field_70181_x = -entityIn.field_70181_x;

            if (!(entityIn instanceof EntityLivingBase))
            {
                entityIn.field_70181_x *= 0.8D;
            }
        }
    }

    /**
     * Called when the given entity walks on this Block
     */
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
        if (Math.abs(entityIn.field_70181_x) < 0.1D && !entityIn.func_70093_af())
        {
            double d0 = 0.4D + Math.abs(entityIn.field_70181_x) * 0.2D;
            entityIn.field_70159_w *= d0;
            entityIn.field_70179_y *= d0;
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }
}

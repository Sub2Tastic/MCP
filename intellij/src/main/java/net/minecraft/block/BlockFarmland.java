package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFarmland extends Block
{
    public static final PropertyInteger MOISTURE = PropertyInteger.create("moisture", 0, 7);
    protected static final AxisAlignedBB field_185665_b = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);
    protected static final AxisAlignedBB field_194405_c = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);

    protected BlockFarmland()
    {
        super(Material.EARTH);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(MOISTURE, Integer.valueOf(0)));
        this.func_149675_a(true);
        this.func_149713_g(255);
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185665_b;
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        int i = ((Integer)p_180650_3_.get(MOISTURE)).intValue();

        if (!this.hasWater(p_180650_1_, p_180650_2_) && !p_180650_1_.isRainingAt(p_180650_2_.up()))
        {
            if (i > 0)
            {
                p_180650_1_.setBlockState(p_180650_2_, p_180650_3_.func_177226_a(MOISTURE, Integer.valueOf(i - 1)), 2);
            }
            else if (!this.hasCrops(p_180650_1_, p_180650_2_))
            {
                func_190970_b(p_180650_1_, p_180650_2_);
            }
        }
        else if (i < 7)
        {
            p_180650_1_.setBlockState(p_180650_2_, p_180650_3_.func_177226_a(MOISTURE, Integer.valueOf(7)), 2);
        }
    }

    /**
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5F && entityIn instanceof EntityLivingBase && (entityIn instanceof EntityPlayer || worldIn.getGameRules().func_82766_b("mobGriefing")) && entityIn.field_70130_N * entityIn.field_70130_N * entityIn.field_70131_O > 0.512F)
        {
            func_190970_b(worldIn, pos);
        }

        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }

    protected static void func_190970_b(World p_190970_0_, BlockPos p_190970_1_)
    {
        p_190970_0_.setBlockState(p_190970_1_, Blocks.DIRT.getDefaultState());
        AxisAlignedBB axisalignedbb = field_194405_c.offset(p_190970_1_);

        for (Entity entity : p_190970_0_.getEntitiesWithinAABBExcludingEntity((Entity)null, axisalignedbb))
        {
            double d0 = Math.min(axisalignedbb.maxY - axisalignedbb.minY, axisalignedbb.maxY - entity.getBoundingBox().minY);
            entity.setPositionAndUpdate(entity.posX, entity.posY + d0 + 0.001D, entity.posZ);
        }
    }

    private boolean hasCrops(World pos, BlockPos p_176529_2_)
    {
        Block block = pos.getBlockState(p_176529_2_.up()).getBlock();
        return block instanceof BlockCrops || block instanceof BlockStem;
    }

    private boolean hasWater(World pos, BlockPos p_176530_2_)
    {
        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.func_177975_b(p_176530_2_.add(-4, 0, -4), p_176530_2_.add(4, 1, 4)))
        {
            if (pos.getBlockState(blockpos$mutableblockpos).getMaterial() == Material.WATER)
            {
                return true;
            }
        }

        return false;
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        super.func_189540_a(p_189540_1_, p_189540_2_, p_189540_3_, p_189540_4_, p_189540_5_);

        if (p_189540_2_.getBlockState(p_189540_3_.up()).getMaterial().isSolid())
        {
            func_190970_b(p_189540_2_, p_189540_3_);
        }
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        super.func_176213_c(p_176213_1_, p_176213_2_, p_176213_3_);

        if (p_176213_1_.getBlockState(p_176213_2_.up()).getMaterial().isSolid())
        {
            func_190970_b(p_176213_1_, p_176213_2_);
        }
    }

    /**
     * ""
     */
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing p_176225_4_)
    {
        switch (p_176225_4_)
        {
            case UP:
                return true;

            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                IBlockState iblockstate = blockAccess.getBlockState(pos.offset(p_176225_4_));
                Block block = iblockstate.getBlock();
                return !iblockstate.func_185914_p() && block != Blocks.FARMLAND && block != Blocks.GRASS_PATH;

            default:
                return super.shouldSideBeRendered(blockState, blockAccess, pos, p_176225_4_);
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Blocks.DIRT.func_180660_a(Blocks.DIRT.getDefaultState().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.DIRT), p_180660_2_, p_180660_3_);
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(MOISTURE, Integer.valueOf(p_176203_1_ & 7));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((Integer)p_176201_1_.get(MOISTURE)).intValue();
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {MOISTURE});
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return p_193383_4_ == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}

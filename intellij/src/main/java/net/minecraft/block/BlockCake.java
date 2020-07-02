package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCake extends Block
{
    public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 6);
    protected static final AxisAlignedBB[] field_185595_b = new AxisAlignedBB[] {new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.1875D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.3125D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.4375D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.5625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.6875D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.8125D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)};

    protected BlockCake()
    {
        super(Material.CAKE);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(BITES, Integer.valueOf(0)));
        this.func_149675_a(true);
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185595_b[((Integer)p_185496_1_.get(BITES)).intValue()];
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (!p_180639_1_.isRemote)
        {
            return this.func_180682_b(p_180639_1_, p_180639_2_, p_180639_3_, p_180639_4_);
        }
        else
        {
            ItemStack itemstack = p_180639_4_.getHeldItem(p_180639_5_);
            return this.func_180682_b(p_180639_1_, p_180639_2_, p_180639_3_, p_180639_4_) || itemstack.isEmpty();
        }
    }

    private boolean func_180682_b(World p_180682_1_, BlockPos p_180682_2_, IBlockState p_180682_3_, EntityPlayer p_180682_4_)
    {
        if (!p_180682_4_.canEat(false))
        {
            return false;
        }
        else
        {
            p_180682_4_.addStat(StatList.EAT_CAKE_SLICE);
            p_180682_4_.getFoodStats().addStats(2, 0.1F);
            int i = ((Integer)p_180682_3_.get(BITES)).intValue();

            if (i < 6)
            {
                p_180682_1_.setBlockState(p_180682_2_, p_180682_3_.func_177226_a(BITES, Integer.valueOf(i + 1)), 3);
            }
            else
            {
                p_180682_1_.func_175698_g(p_180682_2_);
            }

            return true;
        }
    }

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        return super.func_176196_c(p_176196_1_, p_176196_2_) ? this.func_176588_d(p_176196_1_, p_176196_2_) : false;
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (!this.func_176588_d(p_189540_2_, p_189540_3_))
        {
            p_189540_2_.func_175698_g(p_189540_3_);
        }
    }

    private boolean func_176588_d(World p_176588_1_, BlockPos p_176588_2_)
    {
        return p_176588_1_.getBlockState(p_176588_2_.down()).getMaterial().isSolid();
    }

    public int func_149745_a(Random p_149745_1_)
    {
        return 0;
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.AIR;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Items.field_151105_aU);
    }

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(BITES, Integer.valueOf(p_176203_1_));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((Integer)p_176201_1_.get(BITES)).intValue();
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {BITES});
    }

    /**
     * @deprecated call via {@link IBlockState#getComparatorInputOverride(World,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return (7 - ((Integer)blockState.get(BITES)).intValue()) * 2;
    }

    /**
     * @deprecated call via {@link IBlockState#hasComparatorInputOverride()} whenever possible. Implementing/overriding
     * is fine.
     */
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }
}

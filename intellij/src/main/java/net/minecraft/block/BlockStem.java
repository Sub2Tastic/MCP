package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStem extends BlockBush implements IGrowable
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
    public static final PropertyDirection field_176483_b = BlockTorch.field_176596_a;
    private final Block crop;
    protected static final AxisAlignedBB[] field_185521_d = new AxisAlignedBB[] {new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.125D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.25D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.5D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.625D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.75D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.875D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D)};

    protected BlockStem(Block p_i45430_1_)
    {
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(AGE, Integer.valueOf(0)).func_177226_a(field_176483_b, EnumFacing.UP));
        this.crop = p_i45430_1_;
        this.func_149675_a(true);
        this.func_149647_a((CreativeTabs)null);
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185521_d[((Integer)p_185496_1_.get(AGE)).intValue()];
    }

    public IBlockState func_176221_a(IBlockState p_176221_1_, IBlockAccess p_176221_2_, BlockPos p_176221_3_)
    {
        int i = ((Integer)p_176221_1_.get(AGE)).intValue();
        p_176221_1_ = p_176221_1_.func_177226_a(field_176483_b, EnumFacing.UP);

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            if (p_176221_2_.getBlockState(p_176221_3_.offset(enumfacing)).getBlock() == this.crop && i == 7)
            {
                p_176221_1_ = p_176221_1_.func_177226_a(field_176483_b, enumfacing);
                break;
            }
        }

        return p_176221_1_;
    }

    protected boolean func_185514_i(IBlockState p_185514_1_)
    {
        return p_185514_1_.getBlock() == Blocks.FARMLAND;
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        super.func_180650_b(p_180650_1_, p_180650_2_, p_180650_3_, p_180650_4_);

        if (p_180650_1_.func_175671_l(p_180650_2_.up()) >= 9)
        {
            float f = BlockCrops.getGrowthChance(this, p_180650_1_, p_180650_2_);

            if (p_180650_4_.nextInt((int)(25.0F / f) + 1) == 0)
            {
                int i = ((Integer)p_180650_3_.get(AGE)).intValue();

                if (i < 7)
                {
                    p_180650_3_ = p_180650_3_.func_177226_a(AGE, Integer.valueOf(i + 1));
                    p_180650_1_.setBlockState(p_180650_2_, p_180650_3_, 2);
                }
                else
                {
                    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                    {
                        if (p_180650_1_.getBlockState(p_180650_2_.offset(enumfacing)).getBlock() == this.crop)
                        {
                            return;
                        }
                    }

                    p_180650_2_ = p_180650_2_.offset(EnumFacing.Plane.HORIZONTAL.random(p_180650_4_));
                    Block block = p_180650_1_.getBlockState(p_180650_2_.down()).getBlock();

                    if (p_180650_1_.getBlockState(p_180650_2_).getBlock().material == Material.AIR && (block == Blocks.FARMLAND || block == Blocks.DIRT || block == Blocks.GRASS))
                    {
                        p_180650_1_.setBlockState(p_180650_2_, this.crop.getDefaultState());
                    }
                }
            }
        }
    }

    public void func_176482_g(World p_176482_1_, BlockPos p_176482_2_, IBlockState p_176482_3_)
    {
        int i = ((Integer)p_176482_3_.get(AGE)).intValue() + MathHelper.nextInt(p_176482_1_.rand, 2, 5);
        p_176482_1_.setBlockState(p_176482_2_, p_176482_3_.func_177226_a(AGE, Integer.valueOf(Math.min(7, i))), 2);
    }

    public void func_180653_a(World p_180653_1_, BlockPos p_180653_2_, IBlockState p_180653_3_, float p_180653_4_, int p_180653_5_)
    {
        super.func_180653_a(p_180653_1_, p_180653_2_, p_180653_3_, p_180653_4_, p_180653_5_);

        if (!p_180653_1_.isRemote)
        {
            Item item = this.getSeedItem();

            if (item != null)
            {
                int i = ((Integer)p_180653_3_.get(AGE)).intValue();

                for (int j = 0; j < 3; ++j)
                {
                    if (p_180653_1_.rand.nextInt(15) <= i)
                    {
                        spawnAsEntity(p_180653_1_, p_180653_2_, new ItemStack(item));
                    }
                }
            }
        }
    }

    @Nullable
    protected Item getSeedItem()
    {
        if (this.crop == Blocks.PUMPKIN)
        {
            return Items.PUMPKIN_SEEDS;
        }
        else
        {
            return this.crop == Blocks.MELON ? Items.MELON_SEEDS : null;
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.AIR;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        Item item = this.getSeedItem();
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    /**
     * Whether this IGrowable can grow
     */
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return ((Integer)state.get(AGE)).intValue() != 7;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    public void func_176474_b(World p_176474_1_, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_)
    {
        this.func_176482_g(p_176474_1_, p_176474_3_, p_176474_4_);
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(AGE, Integer.valueOf(p_176203_1_));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((Integer)p_176201_1_.get(AGE)).intValue();
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {AGE, field_176483_b});
    }
}

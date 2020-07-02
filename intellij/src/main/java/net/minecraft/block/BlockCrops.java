package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCrops extends BlockBush implements IGrowable
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
    private static final AxisAlignedBB[] field_185530_a = new AxisAlignedBB[] {new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

    protected BlockCrops()
    {
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(this.getAgeProperty(), Integer.valueOf(0)));
        this.func_149675_a(true);
        this.func_149647_a((CreativeTabs)null);
        this.func_149711_c(0.0F);
        this.func_149672_a(SoundType.PLANT);
        this.func_149649_H();
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185530_a[((Integer)p_185496_1_.get(this.getAgeProperty())).intValue()];
    }

    protected boolean func_185514_i(IBlockState p_185514_1_)
    {
        return p_185514_1_.getBlock() == Blocks.FARMLAND;
    }

    protected PropertyInteger getAgeProperty()
    {
        return AGE;
    }

    public int getMaxAge()
    {
        return 7;
    }

    protected int getAge(IBlockState state)
    {
        return ((Integer)state.get(this.getAgeProperty())).intValue();
    }

    public IBlockState withAge(int age)
    {
        return this.getDefaultState().func_177226_a(this.getAgeProperty(), Integer.valueOf(age));
    }

    public boolean isMaxAge(IBlockState state)
    {
        return ((Integer)state.get(this.getAgeProperty())).intValue() >= this.getMaxAge();
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        super.func_180650_b(p_180650_1_, p_180650_2_, p_180650_3_, p_180650_4_);

        if (p_180650_1_.func_175671_l(p_180650_2_.up()) >= 9)
        {
            int i = this.getAge(p_180650_3_);

            if (i < this.getMaxAge())
            {
                float f = getGrowthChance(this, p_180650_1_, p_180650_2_);

                if (p_180650_4_.nextInt((int)(25.0F / f) + 1) == 0)
                {
                    p_180650_1_.setBlockState(p_180650_2_, this.withAge(i + 1), 2);
                }
            }
        }
    }

    public void grow(World worldIn, BlockPos pos, IBlockState state)
    {
        int i = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
        int j = this.getMaxAge();

        if (i > j)
        {
            i = j;
        }

        worldIn.setBlockState(pos, this.withAge(i), 2);
    }

    protected int getBonemealAgeIncrease(World worldIn)
    {
        return MathHelper.nextInt(worldIn.rand, 2, 5);
    }

    protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos)
    {
        float f = 1.0F;
        BlockPos blockpos = pos.down();

        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                float f1 = 0.0F;
                IBlockState iblockstate = worldIn.getBlockState(blockpos.add(i, 0, j));

                if (iblockstate.getBlock() == Blocks.FARMLAND)
                {
                    f1 = 1.0F;

                    if (((Integer)iblockstate.get(BlockFarmland.MOISTURE)).intValue() > 0)
                    {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0)
                {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockpos1 = pos.north();
        BlockPos blockpos2 = pos.south();
        BlockPos blockpos3 = pos.west();
        BlockPos blockpos4 = pos.east();
        boolean flag = blockIn == worldIn.getBlockState(blockpos3).getBlock() || blockIn == worldIn.getBlockState(blockpos4).getBlock();
        boolean flag1 = blockIn == worldIn.getBlockState(blockpos1).getBlock() || blockIn == worldIn.getBlockState(blockpos2).getBlock();

        if (flag && flag1)
        {
            f /= 2.0F;
        }
        else
        {
            boolean flag2 = blockIn == worldIn.getBlockState(blockpos3.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.south()).getBlock() || blockIn == worldIn.getBlockState(blockpos3.south()).getBlock();

            if (flag2)
            {
                f /= 2.0F;
            }
        }

        return f;
    }

    public boolean func_180671_f(World p_180671_1_, BlockPos p_180671_2_, IBlockState p_180671_3_)
    {
        return (p_180671_1_.func_175699_k(p_180671_2_) >= 8 || p_180671_1_.func_175678_i(p_180671_2_)) && this.func_185514_i(p_180671_1_.getBlockState(p_180671_2_.down()));
    }

    protected Item func_149866_i()
    {
        return Items.WHEAT_SEEDS;
    }

    protected Item func_149865_P()
    {
        return Items.WHEAT;
    }

    public void func_180653_a(World p_180653_1_, BlockPos p_180653_2_, IBlockState p_180653_3_, float p_180653_4_, int p_180653_5_)
    {
        super.func_180653_a(p_180653_1_, p_180653_2_, p_180653_3_, p_180653_4_, 0);

        if (!p_180653_1_.isRemote)
        {
            int i = this.getAge(p_180653_3_);

            if (i >= this.getMaxAge())
            {
                int j = 3 + p_180653_5_;

                for (int k = 0; k < j; ++k)
                {
                    if (p_180653_1_.rand.nextInt(2 * this.getMaxAge()) <= i)
                    {
                        spawnAsEntity(p_180653_1_, p_180653_2_, new ItemStack(this.func_149866_i()));
                    }
                }
            }
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return this.isMaxAge(p_180660_1_) ? this.func_149865_P() : this.func_149866_i();
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this.func_149866_i());
    }

    /**
     * Whether this IGrowable can grow
     */
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return !this.isMaxAge(state);
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    public void func_176474_b(World p_176474_1_, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_)
    {
        this.grow(p_176474_1_, p_176474_3_, p_176474_4_);
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.withAge(p_176203_1_);
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return this.getAge(p_176201_1_);
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {AGE});
    }
}

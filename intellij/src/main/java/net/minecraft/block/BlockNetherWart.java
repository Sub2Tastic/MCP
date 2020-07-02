package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockNetherWart extends BlockBush
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);
    private static final AxisAlignedBB[] field_185519_c = new AxisAlignedBB[] {new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D)};

    protected BlockNetherWart()
    {
        super(Material.PLANTS, MapColor.RED);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(AGE, Integer.valueOf(0)));
        this.func_149675_a(true);
        this.func_149647_a((CreativeTabs)null);
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185519_c[((Integer)p_185496_1_.get(AGE)).intValue()];
    }

    protected boolean func_185514_i(IBlockState p_185514_1_)
    {
        return p_185514_1_.getBlock() == Blocks.SOUL_SAND;
    }

    public boolean func_180671_f(World p_180671_1_, BlockPos p_180671_2_, IBlockState p_180671_3_)
    {
        return this.func_185514_i(p_180671_1_.getBlockState(p_180671_2_.down()));
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        int i = ((Integer)p_180650_3_.get(AGE)).intValue();

        if (i < 3 && p_180650_4_.nextInt(10) == 0)
        {
            p_180650_3_ = p_180650_3_.func_177226_a(AGE, Integer.valueOf(i + 1));
            p_180650_1_.setBlockState(p_180650_2_, p_180650_3_, 2);
        }

        super.func_180650_b(p_180650_1_, p_180650_2_, p_180650_3_, p_180650_4_);
    }

    public void func_180653_a(World p_180653_1_, BlockPos p_180653_2_, IBlockState p_180653_3_, float p_180653_4_, int p_180653_5_)
    {
        if (!p_180653_1_.isRemote)
        {
            int i = 1;

            if (((Integer)p_180653_3_.get(AGE)).intValue() >= 3)
            {
                i = 2 + p_180653_1_.rand.nextInt(3);

                if (p_180653_5_ > 0)
                {
                    i += p_180653_1_.rand.nextInt(p_180653_5_ + 1);
                }
            }

            for (int j = 0; j < i; ++j)
            {
                spawnAsEntity(p_180653_1_, p_180653_2_, new ItemStack(Items.NETHER_WART));
            }
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.AIR;
    }

    public int func_149745_a(Random p_149745_1_)
    {
        return 0;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Items.NETHER_WART);
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
        return new BlockStateContainer(this, new IProperty[] {AGE});
    }
}

package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class BlockSponge extends Block
{
    public static final PropertyBool field_176313_a = PropertyBool.create("wet");

    protected BlockSponge()
    {
        super(Material.SPONGE);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176313_a, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public String func_149732_F()
    {
        return I18n.func_74838_a(this.getTranslationKey() + ".dry.name");
    }

    public int func_180651_a(IBlockState p_180651_1_)
    {
        return ((Boolean)p_180651_1_.get(field_176313_a)).booleanValue() ? 1 : 0;
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        this.func_176311_e(p_176213_1_, p_176213_2_, p_176213_3_);
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        this.func_176311_e(p_189540_2_, p_189540_3_, p_189540_1_);
        super.func_189540_a(p_189540_1_, p_189540_2_, p_189540_3_, p_189540_4_, p_189540_5_);
    }

    protected void func_176311_e(World p_176311_1_, BlockPos p_176311_2_, IBlockState p_176311_3_)
    {
        if (!((Boolean)p_176311_3_.get(field_176313_a)).booleanValue() && this.absorb(p_176311_1_, p_176311_2_))
        {
            p_176311_1_.setBlockState(p_176311_2_, p_176311_3_.func_177226_a(field_176313_a, Boolean.valueOf(true)), 2);
            p_176311_1_.func_175718_b(2001, p_176311_2_, Block.func_149682_b(Blocks.WATER));
        }
    }

    private boolean absorb(World worldIn, BlockPos pos)
    {
        Queue<Tuple<BlockPos, Integer>> queue = Lists.<Tuple<BlockPos, Integer>>newLinkedList();
        List<BlockPos> list = Lists.<BlockPos>newArrayList();
        queue.add(new Tuple(pos, Integer.valueOf(0)));
        int i = 0;

        while (!queue.isEmpty())
        {
            Tuple<BlockPos, Integer> tuple = (Tuple)queue.poll();
            BlockPos blockpos = tuple.getA();
            int j = ((Integer)tuple.getB()).intValue();

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                BlockPos blockpos1 = blockpos.offset(enumfacing);

                if (worldIn.getBlockState(blockpos1).getMaterial() == Material.WATER)
                {
                    worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 2);
                    list.add(blockpos1);
                    ++i;

                    if (j < 6)
                    {
                        queue.add(new Tuple(blockpos1, j + 1));
                    }
                }
            }

            if (i > 64)
            {
                break;
            }
        }

        for (BlockPos blockpos2 : list)
        {
            worldIn.func_175685_c(blockpos2, Blocks.AIR, false);
        }

        return i > 0;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_176313_a, Boolean.valueOf((p_176203_1_ & 1) == 1));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((Boolean)p_176201_1_.get(field_176313_a)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176313_a});
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
     * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
     * of whether the block can receive random update ticks
     */
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (((Boolean)stateIn.get(field_176313_a)).booleanValue())
        {
            EnumFacing enumfacing = EnumFacing.random(rand);

            if (enumfacing != EnumFacing.UP && !worldIn.getBlockState(pos.offset(enumfacing)).func_185896_q())
            {
                double d0 = (double)pos.getX();
                double d1 = (double)pos.getY();
                double d2 = (double)pos.getZ();

                if (enumfacing == EnumFacing.DOWN)
                {
                    d1 = d1 - 0.05D;
                    d0 += rand.nextDouble();
                    d2 += rand.nextDouble();
                }
                else
                {
                    d1 = d1 + rand.nextDouble() * 0.8D;

                    if (enumfacing.getAxis() == EnumFacing.Axis.X)
                    {
                        d2 += rand.nextDouble();

                        if (enumfacing == EnumFacing.EAST)
                        {
                            ++d0;
                        }
                        else
                        {
                            d0 += 0.05D;
                        }
                    }
                    else
                    {
                        d0 += rand.nextDouble();

                        if (enumfacing == EnumFacing.SOUTH)
                        {
                            ++d2;
                        }
                        else
                        {
                            d2 += 0.05D;
                        }
                    }
                }

                worldIn.func_175688_a(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}

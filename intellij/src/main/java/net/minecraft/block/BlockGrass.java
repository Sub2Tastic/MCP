package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGrass extends Block implements IGrowable
{
    public static final PropertyBool field_176498_a = PropertyBool.create("snowy");

    protected BlockGrass()
    {
        super(Material.ORGANIC);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176498_a, Boolean.valueOf(false)));
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public IBlockState func_176221_a(IBlockState p_176221_1_, IBlockAccess p_176221_2_, BlockPos p_176221_3_)
    {
        Block block = p_176221_2_.getBlockState(p_176221_3_.up()).getBlock();
        return p_176221_1_.func_177226_a(field_176498_a, Boolean.valueOf(block == Blocks.SNOW || block == Blocks.field_150431_aC));
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (!p_180650_1_.isRemote)
        {
            if (p_180650_1_.func_175671_l(p_180650_2_.up()) < 4 && p_180650_1_.getBlockState(p_180650_2_.up()).func_185891_c() > 2)
            {
                p_180650_1_.setBlockState(p_180650_2_, Blocks.DIRT.getDefaultState());
            }
            else
            {
                if (p_180650_1_.func_175671_l(p_180650_2_.up()) >= 9)
                {
                    for (int i = 0; i < 4; ++i)
                    {
                        BlockPos blockpos = p_180650_2_.add(p_180650_4_.nextInt(3) - 1, p_180650_4_.nextInt(5) - 3, p_180650_4_.nextInt(3) - 1);

                        if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !p_180650_1_.isBlockLoaded(blockpos))
                        {
                            return;
                        }

                        IBlockState iblockstate = p_180650_1_.getBlockState(blockpos.up());
                        IBlockState iblockstate1 = p_180650_1_.getBlockState(blockpos);

                        if (iblockstate1.getBlock() == Blocks.DIRT && iblockstate1.get(BlockDirt.field_176386_a) == BlockDirt.DirtType.DIRT && p_180650_1_.func_175671_l(blockpos.up()) >= 4 && iblockstate.func_185891_c() <= 2)
                        {
                            p_180650_1_.setBlockState(blockpos, Blocks.GRASS.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Blocks.DIRT.func_180660_a(Blocks.DIRT.getDefaultState().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.DIRT), p_180660_2_, p_180660_3_);
    }

    /**
     * Whether this IGrowable can grow
     */
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    public void func_176474_b(World p_176474_1_, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_)
    {
        BlockPos blockpos = p_176474_3_.up();

        for (int i = 0; i < 128; ++i)
        {
            BlockPos blockpos1 = blockpos;
            int j = 0;

            while (true)
            {
                if (j >= i / 16)
                {
                    if (p_176474_1_.getBlockState(blockpos1).getBlock().material == Material.AIR)
                    {
                        if (p_176474_2_.nextInt(8) == 0)
                        {
                            BlockFlower.EnumFlowerType blockflower$enumflowertype = p_176474_1_.func_180494_b(blockpos1).func_180623_a(p_176474_2_, blockpos1);
                            BlockFlower blockflower = blockflower$enumflowertype.func_176964_a().func_180346_a();
                            IBlockState iblockstate = blockflower.getDefaultState().func_177226_a(blockflower.func_176494_l(), blockflower$enumflowertype);

                            if (blockflower.func_180671_f(p_176474_1_, blockpos1, iblockstate))
                            {
                                p_176474_1_.setBlockState(blockpos1, iblockstate, 3);
                            }
                        }
                        else
                        {
                            IBlockState iblockstate1 = Blocks.field_150329_H.getDefaultState().func_177226_a(BlockTallGrass.field_176497_a, BlockTallGrass.EnumType.GRASS);

                            if (Blocks.field_150329_H.func_180671_f(p_176474_1_, blockpos1, iblockstate1))
                            {
                                p_176474_1_.setBlockState(blockpos1, iblockstate1, 3);
                            }
                        }
                    }

                    break;
                }

                blockpos1 = blockpos1.add(p_176474_2_.nextInt(3) - 1, (p_176474_2_.nextInt(3) - 1) * p_176474_2_.nextInt(3) / 2, p_176474_2_.nextInt(3) - 1);

                if (p_176474_1_.getBlockState(blockpos1.down()).getBlock() != Blocks.GRASS || p_176474_1_.getBlockState(blockpos1).func_185915_l())
                {
                    break;
                }

                ++j;
            }
        }
    }

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return 0;
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176498_a});
    }
}

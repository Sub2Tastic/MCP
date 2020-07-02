package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockSapling extends BlockBush implements IGrowable
{
    public static final PropertyEnum<BlockPlanks.EnumType> field_176480_a = PropertyEnum.<BlockPlanks.EnumType>create("type", BlockPlanks.EnumType.class);
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
    protected static final AxisAlignedBB field_185520_d = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    protected BlockSapling()
    {
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176480_a, BlockPlanks.EnumType.OAK).func_177226_a(STAGE, Integer.valueOf(0)));
        this.func_149647_a(CreativeTabs.DECORATIONS);
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185520_d;
    }

    public String func_149732_F()
    {
        return I18n.func_74838_a(this.getTranslationKey() + "." + BlockPlanks.EnumType.OAK.func_176840_c() + ".name");
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (!p_180650_1_.isRemote)
        {
            super.func_180650_b(p_180650_1_, p_180650_2_, p_180650_3_, p_180650_4_);

            if (p_180650_1_.func_175671_l(p_180650_2_.up()) >= 9 && p_180650_4_.nextInt(7) == 0)
            {
                this.func_176478_d(p_180650_1_, p_180650_2_, p_180650_3_, p_180650_4_);
            }
        }
    }

    public void func_176478_d(World p_176478_1_, BlockPos p_176478_2_, IBlockState p_176478_3_, Random p_176478_4_)
    {
        if (((Integer)p_176478_3_.get(STAGE)).intValue() == 0)
        {
            p_176478_1_.setBlockState(p_176478_2_, p_176478_3_.cycle(STAGE), 4);
        }
        else
        {
            this.func_176476_e(p_176478_1_, p_176478_2_, p_176478_3_, p_176478_4_);
        }
    }

    public void func_176476_e(World p_176476_1_, BlockPos p_176476_2_, IBlockState p_176476_3_, Random p_176476_4_)
    {
        WorldGenerator worldgenerator = (WorldGenerator)(p_176476_4_.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true));
        int i = 0;
        int j = 0;
        boolean flag = false;

        switch ((BlockPlanks.EnumType)p_176476_3_.get(field_176480_a))
        {
            case SPRUCE:
                label68:
                for (i = 0; i >= -1; --i)
                {
                    for (j = 0; j >= -1; --j)
                    {
                        if (this.func_181624_a(p_176476_1_, p_176476_2_, i, j, BlockPlanks.EnumType.SPRUCE))
                        {
                            worldgenerator = new WorldGenMegaPineTree(false, p_176476_4_.nextBoolean());
                            flag = true;
                            break label68;
                        }
                    }
                }

                if (!flag)
                {
                    i = 0;
                    j = 0;
                    worldgenerator = new WorldGenTaiga2(true);
                }

                break;

            case BIRCH:
                worldgenerator = new WorldGenBirchTree(true, false);
                break;

            case JUNGLE:
                IBlockState iblockstate = Blocks.field_150364_r.getDefaultState().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.JUNGLE);
                IBlockState iblockstate1 = Blocks.field_150362_t.getDefaultState().func_177226_a(BlockOldLeaf.field_176239_P, BlockPlanks.EnumType.JUNGLE).func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(false));
                label82:

                for (i = 0; i >= -1; --i)
                {
                    for (j = 0; j >= -1; --j)
                    {
                        if (this.func_181624_a(p_176476_1_, p_176476_2_, i, j, BlockPlanks.EnumType.JUNGLE))
                        {
                            worldgenerator = new WorldGenMegaJungle(true, 10, 20, iblockstate, iblockstate1);
                            flag = true;
                            break label82;
                        }
                    }
                }

                if (!flag)
                {
                    i = 0;
                    j = 0;
                    worldgenerator = new WorldGenTrees(true, 4 + p_176476_4_.nextInt(7), iblockstate, iblockstate1, false);
                }

                break;

            case ACACIA:
                worldgenerator = new WorldGenSavannaTree(true);
                break;

            case DARK_OAK:
                label96:
                for (i = 0; i >= -1; --i)
                {
                    for (j = 0; j >= -1; --j)
                    {
                        if (this.func_181624_a(p_176476_1_, p_176476_2_, i, j, BlockPlanks.EnumType.DARK_OAK))
                        {
                            worldgenerator = new WorldGenCanopyTree(true);
                            flag = true;
                            break label96;
                        }
                    }
                }

                if (!flag)
                {
                    return;
                }

            case OAK:
        }

        IBlockState iblockstate2 = Blocks.AIR.getDefaultState();

        if (flag)
        {
            p_176476_1_.setBlockState(p_176476_2_.add(i, 0, j), iblockstate2, 4);
            p_176476_1_.setBlockState(p_176476_2_.add(i + 1, 0, j), iblockstate2, 4);
            p_176476_1_.setBlockState(p_176476_2_.add(i, 0, j + 1), iblockstate2, 4);
            p_176476_1_.setBlockState(p_176476_2_.add(i + 1, 0, j + 1), iblockstate2, 4);
        }
        else
        {
            p_176476_1_.setBlockState(p_176476_2_, iblockstate2, 4);
        }

        if (!worldgenerator.func_180709_b(p_176476_1_, p_176476_4_, p_176476_2_.add(i, 0, j)))
        {
            if (flag)
            {
                p_176476_1_.setBlockState(p_176476_2_.add(i, 0, j), p_176476_3_, 4);
                p_176476_1_.setBlockState(p_176476_2_.add(i + 1, 0, j), p_176476_3_, 4);
                p_176476_1_.setBlockState(p_176476_2_.add(i, 0, j + 1), p_176476_3_, 4);
                p_176476_1_.setBlockState(p_176476_2_.add(i + 1, 0, j + 1), p_176476_3_, 4);
            }
            else
            {
                p_176476_1_.setBlockState(p_176476_2_, p_176476_3_, 4);
            }
        }
    }

    private boolean func_181624_a(World p_181624_1_, BlockPos p_181624_2_, int p_181624_3_, int p_181624_4_, BlockPlanks.EnumType p_181624_5_)
    {
        return this.func_176477_a(p_181624_1_, p_181624_2_.add(p_181624_3_, 0, p_181624_4_), p_181624_5_) && this.func_176477_a(p_181624_1_, p_181624_2_.add(p_181624_3_ + 1, 0, p_181624_4_), p_181624_5_) && this.func_176477_a(p_181624_1_, p_181624_2_.add(p_181624_3_, 0, p_181624_4_ + 1), p_181624_5_) && this.func_176477_a(p_181624_1_, p_181624_2_.add(p_181624_3_ + 1, 0, p_181624_4_ + 1), p_181624_5_);
    }

    public boolean func_176477_a(World p_176477_1_, BlockPos p_176477_2_, BlockPlanks.EnumType p_176477_3_)
    {
        IBlockState iblockstate = p_176477_1_.getBlockState(p_176477_2_);
        return iblockstate.getBlock() == this && iblockstate.get(field_176480_a) == p_176477_3_;
    }

    public int func_180651_a(IBlockState p_180651_1_)
    {
        return ((BlockPlanks.EnumType)p_180651_1_.get(field_176480_a)).func_176839_a();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        for (BlockPlanks.EnumType blockplanks$enumtype : BlockPlanks.EnumType.values())
        {
            items.add(new ItemStack(this, 1, blockplanks$enumtype.func_176839_a()));
        }
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
        return (double)worldIn.rand.nextFloat() < 0.45D;
    }

    public void func_176474_b(World p_176474_1_, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_)
    {
        this.func_176478_d(p_176474_1_, p_176474_3_, p_176474_4_, p_176474_2_);
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_176480_a, BlockPlanks.EnumType.func_176837_a(p_176203_1_ & 7)).func_177226_a(STAGE, Integer.valueOf((p_176203_1_ & 8) >> 3));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((BlockPlanks.EnumType)p_176201_1_.get(field_176480_a)).func_176839_a();
        i = i | ((Integer)p_176201_1_.get(STAGE)).intValue() << 3;
        return i;
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176480_a, STAGE});
    }
}
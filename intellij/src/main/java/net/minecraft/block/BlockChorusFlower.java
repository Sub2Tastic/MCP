package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChorusFlower extends Block
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 5);

    protected BlockChorusFlower()
    {
        super(Material.PLANTS, MapColor.PURPLE);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(AGE, Integer.valueOf(0)));
        this.func_149647_a(CreativeTabs.DECORATIONS);
        this.func_149675_a(true);
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.AIR;
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (!this.func_185606_b(p_180650_1_, p_180650_2_))
        {
            p_180650_1_.destroyBlock(p_180650_2_, true);
        }
        else
        {
            BlockPos blockpos = p_180650_2_.up();

            if (p_180650_1_.isAirBlock(blockpos) && blockpos.getY() < 256)
            {
                int i = ((Integer)p_180650_3_.get(AGE)).intValue();

                if (i < 5 && p_180650_4_.nextInt(1) == 0)
                {
                    boolean flag = false;
                    boolean flag1 = false;
                    IBlockState iblockstate = p_180650_1_.getBlockState(p_180650_2_.down());
                    Block block = iblockstate.getBlock();

                    if (block == Blocks.END_STONE)
                    {
                        flag = true;
                    }
                    else if (block == Blocks.CHORUS_PLANT)
                    {
                        int j = 1;

                        for (int k = 0; k < 4; ++k)
                        {
                            Block block1 = p_180650_1_.getBlockState(p_180650_2_.down(j + 1)).getBlock();

                            if (block1 != Blocks.CHORUS_PLANT)
                            {
                                if (block1 == Blocks.END_STONE)
                                {
                                    flag1 = true;
                                }

                                break;
                            }

                            ++j;
                        }

                        int i1 = 4;

                        if (flag1)
                        {
                            ++i1;
                        }

                        if (j < 2 || p_180650_4_.nextInt(i1) >= j)
                        {
                            flag = true;
                        }
                    }
                    else if (iblockstate.getMaterial() == Material.AIR)
                    {
                        flag = true;
                    }

                    if (flag && areAllNeighborsEmpty(p_180650_1_, blockpos, (EnumFacing)null) && p_180650_1_.isAirBlock(p_180650_2_.up(2)))
                    {
                        p_180650_1_.setBlockState(p_180650_2_, Blocks.CHORUS_PLANT.getDefaultState(), 2);
                        this.placeGrownFlower(p_180650_1_, blockpos, i);
                    }
                    else if (i < 4)
                    {
                        int l = p_180650_4_.nextInt(4);
                        boolean flag2 = false;

                        if (flag1)
                        {
                            ++l;
                        }

                        for (int j1 = 0; j1 < l; ++j1)
                        {
                            EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(p_180650_4_);
                            BlockPos blockpos1 = p_180650_2_.offset(enumfacing);

                            if (p_180650_1_.isAirBlock(blockpos1) && p_180650_1_.isAirBlock(blockpos1.down()) && areAllNeighborsEmpty(p_180650_1_, blockpos1, enumfacing.getOpposite()))
                            {
                                this.placeGrownFlower(p_180650_1_, blockpos1, i + 1);
                                flag2 = true;
                            }
                        }

                        if (flag2)
                        {
                            p_180650_1_.setBlockState(p_180650_2_, Blocks.CHORUS_PLANT.getDefaultState(), 2);
                        }
                        else
                        {
                            this.placeDeadFlower(p_180650_1_, p_180650_2_);
                        }
                    }
                    else if (i == 4)
                    {
                        this.placeDeadFlower(p_180650_1_, p_180650_2_);
                    }
                }
            }
        }
    }

    private void placeGrownFlower(World worldIn, BlockPos pos, int age)
    {
        worldIn.setBlockState(pos, this.getDefaultState().func_177226_a(AGE, Integer.valueOf(age)), 2);
        worldIn.func_175718_b(1033, pos, 0);
    }

    private void placeDeadFlower(World worldIn, BlockPos pos)
    {
        worldIn.setBlockState(pos, this.getDefaultState().func_177226_a(AGE, Integer.valueOf(5)), 2);
        worldIn.func_175718_b(1034, pos, 0);
    }

    private static boolean areAllNeighborsEmpty(World worldIn, BlockPos pos, EnumFacing excludingSide)
    {
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            if (enumfacing != excludingSide && !worldIn.isAirBlock(pos.offset(enumfacing)))
            {
                return false;
            }
        }

        return true;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        return super.func_176196_c(p_176196_1_, p_176196_2_) && this.func_185606_b(p_176196_1_, p_176196_2_);
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (!this.func_185606_b(p_189540_2_, p_189540_3_))
        {
            p_189540_2_.func_175684_a(p_189540_3_, this, 1);
        }
    }

    public boolean func_185606_b(World p_185606_1_, BlockPos p_185606_2_)
    {
        IBlockState iblockstate = p_185606_1_.getBlockState(p_185606_2_.down());
        Block block = iblockstate.getBlock();

        if (block != Blocks.CHORUS_PLANT && block != Blocks.END_STONE)
        {
            if (iblockstate.getMaterial() == Material.AIR)
            {
                int i = 0;

                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                {
                    IBlockState iblockstate1 = p_185606_1_.getBlockState(p_185606_2_.offset(enumfacing));
                    Block block1 = iblockstate1.getBlock();

                    if (block1 == Blocks.CHORUS_PLANT)
                    {
                        ++i;
                    }
                    else if (iblockstate1.getMaterial() != Material.AIR)
                    {
                        return false;
                    }
                }

                return i == 1;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this)));
    }

    protected ItemStack func_180643_i(IBlockState p_180643_1_)
    {
        return ItemStack.EMPTY;
    }

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.CUTOUT;
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

    public static void generatePlant(World worldIn, BlockPos pos, Random rand, int p_185603_3_)
    {
        worldIn.setBlockState(pos, Blocks.CHORUS_PLANT.getDefaultState(), 2);
        growTreeRecursive(worldIn, pos, rand, pos, p_185603_3_, 0);
    }

    private static void growTreeRecursive(World worldIn, BlockPos p_185601_1_, Random rand, BlockPos p_185601_3_, int p_185601_4_, int p_185601_5_)
    {
        int i = rand.nextInt(4) + 1;

        if (p_185601_5_ == 0)
        {
            ++i;
        }

        for (int j = 0; j < i; ++j)
        {
            BlockPos blockpos = p_185601_1_.up(j + 1);

            if (!areAllNeighborsEmpty(worldIn, blockpos, (EnumFacing)null))
            {
                return;
            }

            worldIn.setBlockState(blockpos, Blocks.CHORUS_PLANT.getDefaultState(), 2);
        }

        boolean flag = false;

        if (p_185601_5_ < 4)
        {
            int l = rand.nextInt(4);

            if (p_185601_5_ == 0)
            {
                ++l;
            }

            for (int k = 0; k < l; ++k)
            {
                EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
                BlockPos blockpos1 = p_185601_1_.up(i).offset(enumfacing);

                if (Math.abs(blockpos1.getX() - p_185601_3_.getX()) < p_185601_4_ && Math.abs(blockpos1.getZ() - p_185601_3_.getZ()) < p_185601_4_ && worldIn.isAirBlock(blockpos1) && worldIn.isAirBlock(blockpos1.down()) && areAllNeighborsEmpty(worldIn, blockpos1, enumfacing.getOpposite()))
                {
                    flag = true;
                    worldIn.setBlockState(blockpos1, Blocks.CHORUS_PLANT.getDefaultState(), 2);
                    growTreeRecursive(worldIn, blockpos1, rand, p_185601_3_, p_185601_4_, p_185601_5_ + 1);
                }
            }
        }

        if (!flag)
        {
            worldIn.setBlockState(p_185601_1_.up(i), Blocks.CHORUS_FLOWER.getDefaultState().func_177226_a(AGE, Integer.valueOf(5)), 2);
        }
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }
}

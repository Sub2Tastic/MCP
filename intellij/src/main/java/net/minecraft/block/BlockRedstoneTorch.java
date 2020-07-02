package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneTorch extends BlockTorch
{
    private static final Map<World, List<BlockRedstoneTorch.Toggle>> field_150112_b = Maps.<World, List<BlockRedstoneTorch.Toggle>>newHashMap();
    private final boolean field_150113_a;

    private boolean isBurnedOut(World worldIn, BlockPos pos, boolean p_176598_3_)
    {
        if (!field_150112_b.containsKey(worldIn))
        {
            field_150112_b.put(worldIn, Lists.newArrayList());
        }

        List<BlockRedstoneTorch.Toggle> list = (List)field_150112_b.get(worldIn);

        if (p_176598_3_)
        {
            list.add(new BlockRedstoneTorch.Toggle(pos, worldIn.getGameTime()));
        }

        int i = 0;

        for (int j = 0; j < list.size(); ++j)
        {
            BlockRedstoneTorch.Toggle blockredstonetorch$toggle = list.get(j);

            if (blockredstonetorch$toggle.pos.equals(pos))
            {
                ++i;

                if (i >= 8)
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected BlockRedstoneTorch(boolean p_i45423_1_)
    {
        this.field_150113_a = p_i45423_1_;
        this.func_149675_a(true);
        this.func_149647_a((CreativeTabs)null);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 2;
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        if (this.field_150113_a)
        {
            for (EnumFacing enumfacing : EnumFacing.values())
            {
                p_176213_1_.func_175685_c(p_176213_2_.offset(enumfacing), this, false);
            }
        }
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        if (this.field_150113_a)
        {
            for (EnumFacing enumfacing : EnumFacing.values())
            {
                p_180663_1_.func_175685_c(p_180663_2_.offset(enumfacing), this, false);
            }
        }
    }

    /**
     * @deprecated call via {@link IBlockState#getWeakPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return this.field_150113_a && blockState.get(field_176596_a) != side ? 15 : 0;
    }

    private boolean shouldBeOff(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing enumfacing = ((EnumFacing)state.get(field_176596_a)).getOpposite();
        return worldIn.isSidePowered(pos.offset(enumfacing), enumfacing);
    }

    public void func_180645_a(World p_180645_1_, BlockPos p_180645_2_, IBlockState p_180645_3_, Random p_180645_4_)
    {
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        boolean flag = this.shouldBeOff(p_180650_1_, p_180650_2_, p_180650_3_);
        List<BlockRedstoneTorch.Toggle> list = (List)field_150112_b.get(p_180650_1_);

        while (list != null && !list.isEmpty() && p_180650_1_.getGameTime() - (list.get(0)).time > 60L)
        {
            list.remove(0);
        }

        if (this.field_150113_a)
        {
            if (flag)
            {
                p_180650_1_.setBlockState(p_180650_2_, Blocks.field_150437_az.getDefaultState().func_177226_a(field_176596_a, p_180650_3_.get(field_176596_a)), 3);

                if (this.isBurnedOut(p_180650_1_, p_180650_2_, true))
                {
                    p_180650_1_.playSound((EntityPlayer)null, p_180650_2_, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 0.5F, 2.6F + (p_180650_1_.rand.nextFloat() - p_180650_1_.rand.nextFloat()) * 0.8F);

                    for (int i = 0; i < 5; ++i)
                    {
                        double d0 = (double)p_180650_2_.getX() + p_180650_4_.nextDouble() * 0.6D + 0.2D;
                        double d1 = (double)p_180650_2_.getY() + p_180650_4_.nextDouble() * 0.6D + 0.2D;
                        double d2 = (double)p_180650_2_.getZ() + p_180650_4_.nextDouble() * 0.6D + 0.2D;
                        p_180650_1_.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                    }

                    p_180650_1_.func_175684_a(p_180650_2_, p_180650_1_.getBlockState(p_180650_2_).getBlock(), 160);
                }
            }
        }
        else if (!flag && !this.isBurnedOut(p_180650_1_, p_180650_2_, false))
        {
            p_180650_1_.setBlockState(p_180650_2_, Blocks.REDSTONE_TORCH.getDefaultState().func_177226_a(field_176596_a, p_180650_3_.get(field_176596_a)), 3);
        }
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (!this.func_176592_e(p_189540_2_, p_189540_3_, p_189540_1_))
        {
            if (this.field_150113_a == this.shouldBeOff(p_189540_2_, p_189540_3_, p_189540_1_))
            {
                p_189540_2_.func_175684_a(p_189540_3_, this, this.tickRate(p_189540_2_));
            }
        }
    }

    /**
     * @deprecated call via {@link IBlockState#getStrongPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.DOWN ? blockState.getWeakPower(blockAccess, pos, side) : 0;
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Item.getItemFromBlock(Blocks.REDSTONE_TORCH);
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     * @deprecated call via {@link IBlockState#canProvidePower()} whenever possible. Implementing/overriding is fine.
     */
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
     * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
     * of whether the block can receive random update ticks
     */
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (this.field_150113_a)
        {
            double d0 = (double)pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            double d1 = (double)pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
            double d2 = (double)pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            EnumFacing enumfacing = (EnumFacing)stateIn.get(field_176596_a);

            if (enumfacing.getAxis().isHorizontal())
            {
                EnumFacing enumfacing1 = enumfacing.getOpposite();
                double d3 = 0.27D;
                d0 += 0.27D * (double)enumfacing1.getXOffset();
                d1 += 0.22D;
                d2 += 0.27D * (double)enumfacing1.getZOffset();
            }

            worldIn.func_175688_a(EnumParticleTypes.REDSTONE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Blocks.REDSTONE_TORCH);
    }

    public boolean func_149667_c(Block p_149667_1_)
    {
        return p_149667_1_ == Blocks.field_150437_az || p_149667_1_ == Blocks.REDSTONE_TORCH;
    }

    static class Toggle
    {
        BlockPos pos;
        long time;

        public Toggle(BlockPos pos, long time)
        {
            this.pos = pos;
            this.time = time;
        }
    }
}

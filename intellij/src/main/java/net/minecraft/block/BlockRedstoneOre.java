package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRedstoneOre extends Block
{
    private final boolean field_150187_a;

    public BlockRedstoneOre(boolean p_i45420_1_)
    {
        super(Material.ROCK);

        if (p_i45420_1_)
        {
            this.func_149675_a(true);
        }

        this.field_150187_a = p_i45420_1_;
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 30;
    }

    public void func_180649_a(World p_180649_1_, BlockPos p_180649_2_, EntityPlayer p_180649_3_)
    {
        this.func_176352_d(p_180649_1_, p_180649_2_);
        super.func_180649_a(p_180649_1_, p_180649_2_, p_180649_3_);
    }

    /**
     * Called when the given entity walks on this Block
     */
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
        this.func_176352_d(worldIn, pos);
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        this.func_176352_d(p_180639_1_, p_180639_2_);
        return super.func_180639_a(p_180639_1_, p_180639_2_, p_180639_3_, p_180639_4_, p_180639_5_, p_180639_6_, p_180639_7_, p_180639_8_, p_180639_9_);
    }

    private void func_176352_d(World p_176352_1_, BlockPos p_176352_2_)
    {
        this.spawnParticles(p_176352_1_, p_176352_2_);

        if (this == Blocks.REDSTONE_ORE)
        {
            p_176352_1_.setBlockState(p_176352_2_, Blocks.field_150439_ay.getDefaultState());
        }
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (this == Blocks.field_150439_ay)
        {
            p_180650_1_.setBlockState(p_180650_2_, Blocks.REDSTONE_ORE.getDefaultState());
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.REDSTONE;
    }

    public int func_149679_a(int p_149679_1_, Random p_149679_2_)
    {
        return this.func_149745_a(p_149679_2_) + p_149679_2_.nextInt(p_149679_1_ + 1);
    }

    public int func_149745_a(Random p_149745_1_)
    {
        return 4 + p_149745_1_.nextInt(2);
    }

    public void func_180653_a(World p_180653_1_, BlockPos p_180653_2_, IBlockState p_180653_3_, float p_180653_4_, int p_180653_5_)
    {
        super.func_180653_a(p_180653_1_, p_180653_2_, p_180653_3_, p_180653_4_, p_180653_5_);

        if (this.func_180660_a(p_180653_3_, p_180653_1_.rand, p_180653_5_) != Item.getItemFromBlock(this))
        {
            int i = 1 + p_180653_1_.rand.nextInt(5);
            this.dropXpOnBlockBreak(p_180653_1_, p_180653_2_, i);
        }
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
     * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
     * of whether the block can receive random update ticks
     */
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (this.field_150187_a)
        {
            this.spawnParticles(worldIn, pos);
        }
    }

    private void spawnParticles(World worldIn, BlockPos p_180691_2_)
    {
        Random random = worldIn.rand;
        double d0 = 0.0625D;

        for (int i = 0; i < 6; ++i)
        {
            double d1 = (double)((float)p_180691_2_.getX() + random.nextFloat());
            double d2 = (double)((float)p_180691_2_.getY() + random.nextFloat());
            double d3 = (double)((float)p_180691_2_.getZ() + random.nextFloat());

            if (i == 0 && !worldIn.getBlockState(p_180691_2_.up()).func_185914_p())
            {
                d2 = (double)p_180691_2_.getY() + 0.0625D + 1.0D;
            }

            if (i == 1 && !worldIn.getBlockState(p_180691_2_.down()).func_185914_p())
            {
                d2 = (double)p_180691_2_.getY() - 0.0625D;
            }

            if (i == 2 && !worldIn.getBlockState(p_180691_2_.south()).func_185914_p())
            {
                d3 = (double)p_180691_2_.getZ() + 0.0625D + 1.0D;
            }

            if (i == 3 && !worldIn.getBlockState(p_180691_2_.north()).func_185914_p())
            {
                d3 = (double)p_180691_2_.getZ() - 0.0625D;
            }

            if (i == 4 && !worldIn.getBlockState(p_180691_2_.east()).func_185914_p())
            {
                d1 = (double)p_180691_2_.getX() + 0.0625D + 1.0D;
            }

            if (i == 5 && !worldIn.getBlockState(p_180691_2_.west()).func_185914_p())
            {
                d1 = (double)p_180691_2_.getX() - 0.0625D;
            }

            if (d1 < (double)p_180691_2_.getX() || d1 > (double)(p_180691_2_.getX() + 1) || d2 < 0.0D || d2 > (double)(p_180691_2_.getY() + 1) || d3 < (double)p_180691_2_.getZ() || d3 > (double)(p_180691_2_.getZ() + 1))
            {
                worldIn.func_175688_a(EnumParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected ItemStack func_180643_i(IBlockState p_180643_1_)
    {
        return new ItemStack(Blocks.REDSTONE_ORE);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(Blocks.REDSTONE_ORE), 1, this.func_180651_a(state));
    }
}

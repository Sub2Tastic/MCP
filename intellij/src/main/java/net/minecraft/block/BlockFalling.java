package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFalling extends Block
{
    public static boolean field_149832_M;

    public BlockFalling()
    {
        super(Material.SAND);
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public BlockFalling(Material p_i45405_1_)
    {
        super(p_i45405_1_);
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        p_176213_1_.func_175684_a(p_176213_2_, this, this.tickRate(p_176213_1_));
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        p_189540_2_.func_175684_a(p_189540_3_, this, this.tickRate(p_189540_2_));
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (!p_180650_1_.isRemote)
        {
            this.func_176503_e(p_180650_1_, p_180650_2_);
        }
    }

    private void func_176503_e(World p_176503_1_, BlockPos p_176503_2_)
    {
        if (canFallThrough(p_176503_1_.getBlockState(p_176503_2_.down())) && p_176503_2_.getY() >= 0)
        {
            int i = 32;

            if (!field_149832_M && p_176503_1_.isAreaLoaded(p_176503_2_.add(-32, -32, -32), p_176503_2_.add(32, 32, 32)))
            {
                if (!p_176503_1_.isRemote)
                {
                    EntityFallingBlock entityfallingblock = new EntityFallingBlock(p_176503_1_, (double)p_176503_2_.getX() + 0.5D, (double)p_176503_2_.getY(), (double)p_176503_2_.getZ() + 0.5D, p_176503_1_.getBlockState(p_176503_2_));
                    this.onStartFalling(entityfallingblock);
                    p_176503_1_.addEntity0(entityfallingblock);
                }
            }
            else
            {
                p_176503_1_.func_175698_g(p_176503_2_);
                BlockPos blockpos;

                for (blockpos = p_176503_2_.down(); canFallThrough(p_176503_1_.getBlockState(blockpos)) && blockpos.getY() > 0; blockpos = blockpos.down())
                {
                    ;
                }

                if (blockpos.getY() > 0)
                {
                    p_176503_1_.setBlockState(blockpos.up(), this.getDefaultState());
                }
            }
        }
    }

    protected void onStartFalling(EntityFallingBlock fallingEntity)
    {
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 2;
    }

    public static boolean canFallThrough(IBlockState state)
    {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return block == Blocks.FIRE || material == Material.AIR || material == Material.WATER || material == Material.LAVA;
    }

    public void onEndFalling(World worldIn, BlockPos pos, IBlockState fallingState, IBlockState hitState)
    {
    }

    public void onBroken(World worldIn, BlockPos pos)
    {
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
     * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
     * of whether the block can receive random update ticks
     */
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (rand.nextInt(16) == 0)
        {
            BlockPos blockpos = pos.down();

            if (canFallThrough(worldIn.getBlockState(blockpos)))
            {
                double d0 = (double)((float)pos.getX() + rand.nextFloat());
                double d1 = (double)pos.getY() - 0.05D;
                double d2 = (double)((float)pos.getZ() + rand.nextFloat());
                worldIn.func_175688_a(EnumParticleTypes.FALLING_DUST, d0, d1, d2, 0.0D, 0.0D, 0.0D, Block.func_176210_f(stateIn));
            }
        }
    }

    public int getDustColor(IBlockState state)
    {
        return -16777216;
    }
}

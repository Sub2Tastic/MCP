package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaPineTree extends WorldGenHugeTrees
{
    private static final IBlockState field_181633_e = Blocks.field_150364_r.getDefaultState().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.SPRUCE);
    private static final IBlockState field_181634_f = Blocks.field_150362_t.getDefaultState().func_177226_a(BlockOldLeaf.field_176239_P, BlockPlanks.EnumType.SPRUCE).func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(false));
    private static final IBlockState field_181635_g = Blocks.DIRT.getDefaultState().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.PODZOL);
    private final boolean field_150542_e;

    public WorldGenMegaPineTree(boolean p_i45457_1_, boolean p_i45457_2_)
    {
        super(p_i45457_1_, 13, 15, field_181633_e, field_181634_f);
        this.field_150542_e = p_i45457_2_;
    }

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int i = this.func_150533_a(p_180709_2_);

        if (!this.func_175929_a(p_180709_1_, p_180709_2_, p_180709_3_, i))
        {
            return false;
        }
        else
        {
            this.func_150541_c(p_180709_1_, p_180709_3_.getX(), p_180709_3_.getZ(), p_180709_3_.getY() + i, 0, p_180709_2_);

            for (int j = 0; j < i; ++j)
            {
                IBlockState iblockstate = p_180709_1_.getBlockState(p_180709_3_.up(j));

                if (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES)
                {
                    this.func_175903_a(p_180709_1_, p_180709_3_.up(j), this.field_76520_b);
                }

                if (j < i - 1)
                {
                    iblockstate = p_180709_1_.getBlockState(p_180709_3_.add(1, j, 0));

                    if (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES)
                    {
                        this.func_175903_a(p_180709_1_, p_180709_3_.add(1, j, 0), this.field_76520_b);
                    }

                    iblockstate = p_180709_1_.getBlockState(p_180709_3_.add(1, j, 1));

                    if (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES)
                    {
                        this.func_175903_a(p_180709_1_, p_180709_3_.add(1, j, 1), this.field_76520_b);
                    }

                    iblockstate = p_180709_1_.getBlockState(p_180709_3_.add(0, j, 1));

                    if (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES)
                    {
                        this.func_175903_a(p_180709_1_, p_180709_3_.add(0, j, 1), this.field_76520_b);
                    }
                }
            }

            return true;
        }
    }

    private void func_150541_c(World p_150541_1_, int p_150541_2_, int p_150541_3_, int p_150541_4_, int p_150541_5_, Random p_150541_6_)
    {
        int i = p_150541_6_.nextInt(5) + (this.field_150542_e ? this.field_76522_a : 3);
        int j = 0;

        for (int k = p_150541_4_ - i; k <= p_150541_4_; ++k)
        {
            int l = p_150541_4_ - k;
            int i1 = p_150541_5_ + MathHelper.floor((float)l / (float)i * 3.5F);
            this.func_175925_a(p_150541_1_, new BlockPos(p_150541_2_, k, p_150541_3_), i1 + (l > 0 && i1 == j && (k & 1) == 0 ? 1 : 0));
            j = i1;
        }
    }

    public void func_180711_a(World p_180711_1_, Random p_180711_2_, BlockPos p_180711_3_)
    {
        this.func_175933_b(p_180711_1_, p_180711_3_.west().north());
        this.func_175933_b(p_180711_1_, p_180711_3_.east(2).north());
        this.func_175933_b(p_180711_1_, p_180711_3_.west().south(2));
        this.func_175933_b(p_180711_1_, p_180711_3_.east(2).south(2));

        for (int i = 0; i < 5; ++i)
        {
            int j = p_180711_2_.nextInt(64);
            int k = j % 8;
            int l = j / 8;

            if (k == 0 || k == 7 || l == 0 || l == 7)
            {
                this.func_175933_b(p_180711_1_, p_180711_3_.add(-3 + k, 0, -3 + l));
            }
        }
    }

    private void func_175933_b(World p_175933_1_, BlockPos p_175933_2_)
    {
        for (int i = -2; i <= 2; ++i)
        {
            for (int j = -2; j <= 2; ++j)
            {
                if (Math.abs(i) != 2 || Math.abs(j) != 2)
                {
                    this.func_175934_c(p_175933_1_, p_175933_2_.add(i, 0, j));
                }
            }
        }
    }

    private void func_175934_c(World p_175934_1_, BlockPos p_175934_2_)
    {
        for (int i = 2; i >= -3; --i)
        {
            BlockPos blockpos = p_175934_2_.up(i);
            IBlockState iblockstate = p_175934_1_.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            if (block == Blocks.GRASS || block == Blocks.DIRT)
            {
                this.func_175903_a(p_175934_1_, blockpos, field_181635_g);
                break;
            }

            if (iblockstate.getMaterial() != Material.AIR && i < 0)
            {
                break;
            }
        }
    }
}

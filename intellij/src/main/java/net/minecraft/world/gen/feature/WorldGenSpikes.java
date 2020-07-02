package net.minecraft.world.gen.feature;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenSpikes extends WorldGenerator
{
    private boolean field_186145_a;
    private WorldGenSpikes.EndSpike field_186146_b;
    private BlockPos field_186147_c;

    public void func_186143_a(WorldGenSpikes.EndSpike p_186143_1_)
    {
        this.field_186146_b = p_186143_1_;
    }

    public void func_186144_a(boolean p_186144_1_)
    {
        this.field_186145_a = p_186144_1_;
    }

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        if (this.field_186146_b == null)
        {
            throw new IllegalStateException("Decoration requires priming with a spike");
        }
        else
        {
            int i = this.field_186146_b.getRadius();

            for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.func_177975_b(new BlockPos(p_180709_3_.getX() - i, 0, p_180709_3_.getZ() - i), new BlockPos(p_180709_3_.getX() + i, this.field_186146_b.getHeight() + 10, p_180709_3_.getZ() + i)))
            {
                if (blockpos$mutableblockpos.func_177954_c((double)p_180709_3_.getX(), (double)blockpos$mutableblockpos.getY(), (double)p_180709_3_.getZ()) <= (double)(i * i + 1) && blockpos$mutableblockpos.getY() < this.field_186146_b.getHeight())
                {
                    this.func_175903_a(p_180709_1_, blockpos$mutableblockpos, Blocks.OBSIDIAN.getDefaultState());
                }
                else if (blockpos$mutableblockpos.getY() > 65)
                {
                    this.func_175903_a(p_180709_1_, blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
                }
            }

            if (this.field_186146_b.isGuarded())
            {
                for (int j = -2; j <= 2; ++j)
                {
                    for (int k = -2; k <= 2; ++k)
                    {
                        if (MathHelper.abs(j) == 2 || MathHelper.abs(k) == 2)
                        {
                            this.func_175903_a(p_180709_1_, new BlockPos(p_180709_3_.getX() + j, this.field_186146_b.getHeight(), p_180709_3_.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                            this.func_175903_a(p_180709_1_, new BlockPos(p_180709_3_.getX() + j, this.field_186146_b.getHeight() + 1, p_180709_3_.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                            this.func_175903_a(p_180709_1_, new BlockPos(p_180709_3_.getX() + j, this.field_186146_b.getHeight() + 2, p_180709_3_.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                        }

                        this.func_175903_a(p_180709_1_, new BlockPos(p_180709_3_.getX() + j, this.field_186146_b.getHeight() + 3, p_180709_3_.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                    }
                }
            }

            EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(p_180709_1_);
            entityendercrystal.setBeamTarget(this.field_186147_c);
            entityendercrystal.setInvulnerable(this.field_186145_a);
            entityendercrystal.setLocationAndAngles((double)((float)p_180709_3_.getX() + 0.5F), (double)(this.field_186146_b.getHeight() + 1), (double)((float)p_180709_3_.getZ() + 0.5F), p_180709_2_.nextFloat() * 360.0F, 0.0F);
            p_180709_1_.addEntity0(entityendercrystal);
            this.func_175903_a(p_180709_1_, new BlockPos(p_180709_3_.getX(), this.field_186146_b.getHeight(), p_180709_3_.getZ()), Blocks.BEDROCK.getDefaultState());
            return true;
        }
    }

    public void func_186142_a(@Nullable BlockPos p_186142_1_)
    {
        this.field_186147_c = p_186142_1_;
    }

    public static class EndSpike
    {
        private final int centerX;
        private final int centerZ;
        private final int radius;
        private final int height;
        private final boolean guarded;
        private final AxisAlignedBB topBoundingBox;

        public EndSpike(int centerXIn, int centerZIn, int radiusIn, int heightIn, boolean guardedIn)
        {
            this.centerX = centerXIn;
            this.centerZ = centerZIn;
            this.radius = radiusIn;
            this.height = heightIn;
            this.guarded = guardedIn;
            this.topBoundingBox = new AxisAlignedBB((double)(centerXIn - radiusIn), 0.0D, (double)(centerZIn - radiusIn), (double)(centerXIn + radiusIn), 256.0D, (double)(centerZIn + radiusIn));
        }

        public boolean doesStartInChunk(BlockPos pos)
        {
            int i = this.centerX - this.radius;
            int j = this.centerZ - this.radius;
            return pos.getX() == (i & -16) && pos.getZ() == (j & -16);
        }

        public int getCenterX()
        {
            return this.centerX;
        }

        public int getCenterZ()
        {
            return this.centerZ;
        }

        public int getRadius()
        {
            return this.radius;
        }

        public int getHeight()
        {
            return this.height;
        }

        public boolean isGuarded()
        {
            return this.guarded;
        }

        public AxisAlignedBB getTopBoundingBox()
        {
            return this.topBoundingBox;
        }
    }
}

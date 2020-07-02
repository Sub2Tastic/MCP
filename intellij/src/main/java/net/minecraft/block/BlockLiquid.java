package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLiquid extends Block
{
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);

    protected BlockLiquid(Material p_i45413_1_)
    {
        super(p_i45413_1_);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(LEVEL, Integer.valueOf(0)));
        this.func_149675_a(true);
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185505_j;
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState p_180646_1_, IBlockAccess p_180646_2_, BlockPos p_180646_3_)
    {
        return field_185506_k;
    }

    public boolean func_176205_b(IBlockAccess p_176205_1_, BlockPos p_176205_2_)
    {
        return this.material != Material.LAVA;
    }

    public static float func_149801_b(int p_149801_0_)
    {
        if (p_149801_0_ >= 8)
        {
            p_149801_0_ = 0;
        }

        return (float)(p_149801_0_ + 1) / 9.0F;
    }

    protected int func_189542_i(IBlockState p_189542_1_)
    {
        return p_189542_1_.getMaterial() == this.material ? ((Integer)p_189542_1_.get(LEVEL)).intValue() : -1;
    }

    protected int func_189545_x(IBlockState p_189545_1_)
    {
        int i = this.func_189542_i(p_189545_1_);
        return i >= 8 ? 0 : i;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_176209_a(IBlockState p_176209_1_, boolean p_176209_2_)
    {
        return p_176209_2_ && ((Integer)p_176209_1_.get(LEVEL)).intValue() == 0;
    }

    private boolean func_176212_b(IBlockAccess p_176212_1_, BlockPos p_176212_2_, EnumFacing p_176212_3_)
    {
        IBlockState iblockstate = p_176212_1_.getBlockState(p_176212_2_);
        Block block = iblockstate.getBlock();
        Material material = iblockstate.getMaterial();

        if (material == this.material)
        {
            return false;
        }
        else if (p_176212_3_ == EnumFacing.UP)
        {
            return true;
        }
        else if (material == Material.ICE)
        {
            return false;
        }
        else
        {
            boolean flag = func_193382_c(block) || block instanceof BlockStairs;
            return !flag && iblockstate.func_193401_d(p_176212_1_, p_176212_2_, p_176212_3_) == BlockFaceShape.SOLID;
        }
    }

    /**
     * ""
     */
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing p_176225_4_)
    {
        if (blockAccess.getBlockState(pos.offset(p_176225_4_)).getMaterial() == this.material)
        {
            return false;
        }
        else
        {
            return p_176225_4_ == EnumFacing.UP ? true : super.shouldSideBeRendered(blockState, blockAccess, pos, p_176225_4_);
        }
    }

    public boolean func_176364_g(IBlockAccess p_176364_1_, BlockPos p_176364_2_)
    {
        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                IBlockState iblockstate = p_176364_1_.getBlockState(p_176364_2_.add(i, 0, j));

                if (iblockstate.getMaterial() != this.material && !iblockstate.func_185913_b())
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.LIQUID;
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.AIR;
    }

    public int func_149745_a(Random p_149745_1_)
    {
        return 0;
    }

    protected Vec3d func_189543_a(IBlockAccess p_189543_1_, BlockPos p_189543_2_, IBlockState p_189543_3_)
    {
        double d0 = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        int i = this.func_189545_x(p_189543_3_);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            blockpos$pooledmutableblockpos.setPos(p_189543_2_).move(enumfacing);
            int j = this.func_189545_x(p_189543_1_.getBlockState(blockpos$pooledmutableblockpos));

            if (j < 0)
            {
                if (!p_189543_1_.getBlockState(blockpos$pooledmutableblockpos).getMaterial().blocksMovement())
                {
                    j = this.func_189545_x(p_189543_1_.getBlockState(blockpos$pooledmutableblockpos.down()));

                    if (j >= 0)
                    {
                        int k = j - (i - 8);
                        d0 += (double)(enumfacing.getXOffset() * k);
                        d1 += (double)(enumfacing.getYOffset() * k);
                        d2 += (double)(enumfacing.getZOffset() * k);
                    }
                }
            }
            else if (j >= 0)
            {
                int l = j - i;
                d0 += (double)(enumfacing.getXOffset() * l);
                d1 += (double)(enumfacing.getYOffset() * l);
                d2 += (double)(enumfacing.getZOffset() * l);
            }
        }

        Vec3d vec3d = new Vec3d(d0, d1, d2);

        if (((Integer)p_189543_3_.get(LEVEL)).intValue() >= 8)
        {
            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
            {
                blockpos$pooledmutableblockpos.setPos(p_189543_2_).move(enumfacing1);

                if (this.func_176212_b(p_189543_1_, blockpos$pooledmutableblockpos, enumfacing1) || this.func_176212_b(p_189543_1_, blockpos$pooledmutableblockpos.up(), enumfacing1))
                {
                    vec3d = vec3d.normalize().add(0.0D, -6.0D, 0.0D);
                    break;
                }
            }
        }

        blockpos$pooledmutableblockpos.func_185344_t();
        return vec3d.normalize();
    }

    public Vec3d func_176197_a(World p_176197_1_, BlockPos p_176197_2_, Entity p_176197_3_, Vec3d p_176197_4_)
    {
        return p_176197_4_.add(this.func_189543_a(p_176197_1_, p_176197_2_, p_176197_1_.getBlockState(p_176197_2_)));
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        if (this.material == Material.WATER)
        {
            return 5;
        }
        else if (this.material == Material.LAVA)
        {
            return worldIn.dimension.isNether() ? 10 : 30;
        }
        else
        {
            return 0;
        }
    }

    public int func_185484_c(IBlockState p_185484_1_, IBlockAccess p_185484_2_, BlockPos p_185484_3_)
    {
        int i = p_185484_2_.func_175626_b(p_185484_3_, 0);
        int j = p_185484_2_.func_175626_b(p_185484_3_.up(), 0);
        int k = i & 255;
        int l = j & 255;
        int i1 = i >> 16 & 255;
        int j1 = j >> 16 & 255;
        return (k > l ? k : l) | (i1 > j1 ? i1 : j1) << 16;
    }

    public BlockRenderLayer func_180664_k()
    {
        return this.material == Material.WATER ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.SOLID;
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
     * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
     * of whether the block can receive random update ticks
     */
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        double d0 = (double)pos.getX();
        double d1 = (double)pos.getY();
        double d2 = (double)pos.getZ();

        if (this.material == Material.WATER)
        {
            int i = ((Integer)stateIn.get(LEVEL)).intValue();

            if (i > 0 && i < 8)
            {
                if (rand.nextInt(64) == 0)
                {
                    worldIn.playSound(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, rand.nextFloat() * 0.25F + 0.75F, rand.nextFloat() + 0.5F, false);
                }
            }
            else if (rand.nextInt(10) == 0)
            {
                worldIn.func_175688_a(EnumParticleTypes.SUSPENDED, d0 + (double)rand.nextFloat(), d1 + (double)rand.nextFloat(), d2 + (double)rand.nextFloat(), 0.0D, 0.0D, 0.0D);
            }
        }

        if (this.material == Material.LAVA && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && !worldIn.getBlockState(pos.up()).func_185914_p())
        {
            if (rand.nextInt(100) == 0)
            {
                double d8 = d0 + (double)rand.nextFloat();
                double d4 = d1 + stateIn.func_185900_c(worldIn, pos).maxY;
                double d6 = d2 + (double)rand.nextFloat();
                worldIn.func_175688_a(EnumParticleTypes.LAVA, d8, d4, d6, 0.0D, 0.0D, 0.0D);
                worldIn.playSound(d8, d4, d6, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }

            if (rand.nextInt(200) == 0)
            {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }
        }

        if (rand.nextInt(10) == 0 && worldIn.getBlockState(pos.down()).func_185896_q())
        {
            Material material = worldIn.getBlockState(pos.down(2)).getMaterial();

            if (!material.blocksMovement() && !material.isLiquid())
            {
                double d3 = d0 + (double)rand.nextFloat();
                double d5 = d1 - 1.05D;
                double d7 = d2 + (double)rand.nextFloat();

                if (this.material == Material.WATER)
                {
                    worldIn.func_175688_a(EnumParticleTypes.DRIP_WATER, d3, d5, d7, 0.0D, 0.0D, 0.0D);
                }
                else
                {
                    worldIn.func_175688_a(EnumParticleTypes.DRIP_LAVA, d3, d5, d7, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    public static float func_189544_a(IBlockAccess p_189544_0_, BlockPos p_189544_1_, Material p_189544_2_, IBlockState p_189544_3_)
    {
        Vec3d vec3d = func_176361_a(p_189544_2_).func_189543_a(p_189544_0_, p_189544_1_, p_189544_3_);
        return vec3d.x == 0.0D && vec3d.z == 0.0D ? -1000.0F : (float)MathHelper.atan2(vec3d.z, vec3d.x) - ((float)Math.PI / 2F);
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        this.func_176365_e(p_176213_1_, p_176213_2_, p_176213_3_);
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        this.func_176365_e(p_189540_2_, p_189540_3_, p_189540_1_);
    }

    public boolean func_176365_e(World p_176365_1_, BlockPos p_176365_2_, IBlockState p_176365_3_)
    {
        if (this.material == Material.LAVA)
        {
            boolean flag = false;

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                if (enumfacing != EnumFacing.DOWN && p_176365_1_.getBlockState(p_176365_2_.offset(enumfacing)).getMaterial() == Material.WATER)
                {
                    flag = true;
                    break;
                }
            }

            if (flag)
            {
                Integer integer = (Integer)p_176365_3_.get(LEVEL);

                if (integer.intValue() == 0)
                {
                    p_176365_1_.setBlockState(p_176365_2_, Blocks.OBSIDIAN.getDefaultState());
                    this.triggerMixEffects(p_176365_1_, p_176365_2_);
                    return true;
                }

                if (integer.intValue() <= 4)
                {
                    p_176365_1_.setBlockState(p_176365_2_, Blocks.COBBLESTONE.getDefaultState());
                    this.triggerMixEffects(p_176365_1_, p_176365_2_);
                    return true;
                }
            }
        }

        return false;
    }

    protected void triggerMixEffects(World worldIn, BlockPos pos)
    {
        double d0 = (double)pos.getX();
        double d1 = (double)pos.getY();
        double d2 = (double)pos.getZ();
        worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

        for (int i = 0; i < 8; ++i)
        {
            worldIn.func_175688_a(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d1 + 1.2D, d2 + Math.random(), 0.0D, 0.0D, 0.0D);
        }
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(LEVEL, Integer.valueOf(p_176203_1_));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((Integer)p_176201_1_.get(LEVEL)).intValue();
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {LEVEL});
    }

    public static BlockDynamicLiquid func_176361_a(Material p_176361_0_)
    {
        if (p_176361_0_ == Material.WATER)
        {
            return Blocks.field_150358_i;
        }
        else if (p_176361_0_ == Material.LAVA)
        {
            return Blocks.field_150356_k;
        }
        else
        {
            throw new IllegalArgumentException("Invalid material");
        }
    }

    public static BlockStaticLiquid func_176363_b(Material p_176363_0_)
    {
        if (p_176363_0_ == Material.WATER)
        {
            return Blocks.WATER;
        }
        else if (p_176363_0_ == Material.LAVA)
        {
            return Blocks.LAVA;
        }
        else
        {
            throw new IllegalArgumentException("Invalid material");
        }
    }

    public static float func_190973_f(IBlockState p_190973_0_, IBlockAccess p_190973_1_, BlockPos p_190973_2_)
    {
        int i = ((Integer)p_190973_0_.get(LEVEL)).intValue();
        return (i & 7) == 0 && p_190973_1_.getBlockState(p_190973_2_.up()).getMaterial() == Material.WATER ? 1.0F : 1.0F - func_149801_b(i);
    }

    public static float func_190972_g(IBlockState p_190972_0_, IBlockAccess p_190972_1_, BlockPos p_190972_2_)
    {
        return (float)p_190972_2_.getY() + func_190973_f(p_190972_0_, p_190972_1_, p_190972_2_);
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }
}

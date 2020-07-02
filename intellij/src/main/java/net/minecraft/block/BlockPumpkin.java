package net.minecraft.block;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPumpkin extends BlockHorizontal
{
    private BlockPattern field_176394_a;
    private BlockPattern field_176393_b;
    private BlockPattern field_176395_M;
    private BlockPattern field_176396_O;
    private static final Predicate<IBlockState> field_181085_Q = new Predicate<IBlockState>()
    {
        public boolean apply(@Nullable IBlockState p_apply_1_)
        {
            return p_apply_1_ != null && (p_apply_1_.getBlock() == Blocks.PUMPKIN || p_apply_1_.getBlock() == Blocks.field_150428_aP);
        }
    };

    protected BlockPumpkin()
    {
        super(Material.GOURD, MapColor.ADOBE);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(HORIZONTAL_FACING, EnumFacing.NORTH));
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        super.func_176213_c(p_176213_1_, p_176213_2_, p_176213_3_);
        this.func_180673_e(p_176213_1_, p_176213_2_);
    }

    public boolean func_176390_d(World p_176390_1_, BlockPos p_176390_2_)
    {
        return this.func_176392_j().match(p_176390_1_, p_176390_2_) != null || this.func_176389_S().match(p_176390_1_, p_176390_2_) != null;
    }

    private void func_180673_e(World p_180673_1_, BlockPos p_180673_2_)
    {
        BlockPattern.PatternHelper blockpattern$patternhelper = this.func_176391_l().match(p_180673_1_, p_180673_2_);

        if (blockpattern$patternhelper != null)
        {
            for (int i = 0; i < this.func_176391_l().getThumbLength(); ++i)
            {
                BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(0, i, 0);
                p_180673_1_.setBlockState(blockworldstate.getPos(), Blocks.AIR.getDefaultState(), 2);
            }

            EntitySnowman entitysnowman = new EntitySnowman(p_180673_1_);
            BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(0, 2, 0).getPos();
            entitysnowman.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.05D, (double)blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
            p_180673_1_.addEntity0(entitysnowman);

            for (EntityPlayerMP entityplayermp : p_180673_1_.func_72872_a(EntityPlayerMP.class, entitysnowman.getBoundingBox().grow(5.0D)))
            {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitysnowman);
            }

            for (int l = 0; l < 120; ++l)
            {
                p_180673_1_.func_175688_a(EnumParticleTypes.SNOW_SHOVEL, (double)blockpos1.getX() + p_180673_1_.rand.nextDouble(), (double)blockpos1.getY() + p_180673_1_.rand.nextDouble() * 2.5D, (double)blockpos1.getZ() + p_180673_1_.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }

            for (int i1 = 0; i1 < this.func_176391_l().getThumbLength(); ++i1)
            {
                BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(0, i1, 0);
                p_180673_1_.func_175722_b(blockworldstate2.getPos(), Blocks.AIR, false);
            }
        }
        else
        {
            blockpattern$patternhelper = this.func_176388_T().match(p_180673_1_, p_180673_2_);

            if (blockpattern$patternhelper != null)
            {
                for (int j = 0; j < this.func_176388_T().getPalmLength(); ++j)
                {
                    for (int k = 0; k < this.func_176388_T().getThumbLength(); ++k)
                    {
                        p_180673_1_.setBlockState(blockpattern$patternhelper.translateOffset(j, k, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
                    }
                }

                BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
                EntityIronGolem entityirongolem = new EntityIronGolem(p_180673_1_);
                entityirongolem.setPlayerCreated(true);
                entityirongolem.setLocationAndAngles((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.05D, (double)blockpos.getZ() + 0.5D, 0.0F, 0.0F);
                p_180673_1_.addEntity0(entityirongolem);

                for (EntityPlayerMP entityplayermp1 : p_180673_1_.func_72872_a(EntityPlayerMP.class, entityirongolem.getBoundingBox().grow(5.0D)))
                {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp1, entityirongolem);
                }

                for (int j1 = 0; j1 < 120; ++j1)
                {
                    p_180673_1_.func_175688_a(EnumParticleTypes.SNOWBALL, (double)blockpos.getX() + p_180673_1_.rand.nextDouble(), (double)blockpos.getY() + p_180673_1_.rand.nextDouble() * 3.9D, (double)blockpos.getZ() + p_180673_1_.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
                }

                for (int k1 = 0; k1 < this.func_176388_T().getPalmLength(); ++k1)
                {
                    for (int l1 = 0; l1 < this.func_176388_T().getThumbLength(); ++l1)
                    {
                        BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(k1, l1, 0);
                        p_180673_1_.func_175722_b(blockworldstate1.getPos(), Blocks.AIR, false);
                    }
                }
            }
        }
    }

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        return p_176196_1_.getBlockState(p_176196_2_).getBlock().material.isReplaceable() && p_176196_1_.getBlockState(p_176196_2_.down()).func_185896_q();
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
        return state.func_177226_a(HORIZONTAL_FACING, rot.rotate((EnumFacing)state.get(HORIZONTAL_FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation((EnumFacing)state.get(HORIZONTAL_FACING)));
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        return this.getDefaultState().func_177226_a(HORIZONTAL_FACING, p_180642_8_.getHorizontalFacing().getOpposite());
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(HORIZONTAL_FACING, EnumFacing.byHorizontalIndex(p_176203_1_));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((EnumFacing)p_176201_1_.get(HORIZONTAL_FACING)).getHorizontalIndex();
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {HORIZONTAL_FACING});
    }

    protected BlockPattern func_176392_j()
    {
        if (this.field_176394_a == null)
        {
            this.field_176394_a = FactoryBlockPattern.start().aisle(" ", "#", "#").where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SNOW))).build();
        }

        return this.field_176394_a;
    }

    protected BlockPattern func_176391_l()
    {
        if (this.field_176393_b == null)
        {
            this.field_176393_b = FactoryBlockPattern.start().aisle("^", "#", "#").where('^', BlockWorldState.hasState(field_181085_Q)).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SNOW))).build();
        }

        return this.field_176393_b;
    }

    protected BlockPattern func_176389_S()
    {
        if (this.field_176395_M == null)
        {
            this.field_176395_M = FactoryBlockPattern.start().aisle("~ ~", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }

        return this.field_176395_M;
    }

    protected BlockPattern func_176388_T()
    {
        if (this.field_176396_O == null)
        {
            this.field_176396_O = FactoryBlockPattern.start().aisle("~^~", "###", "~#~").where('^', BlockWorldState.hasState(field_181085_Q)).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }

        return this.field_176396_O;
    }
}

package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStairs extends Block
{
    public static final PropertyDirection FACING = BlockHorizontal.HORIZONTAL_FACING;
    public static final PropertyEnum<BlockStairs.EnumHalf> HALF = PropertyEnum.<BlockStairs.EnumHalf>create("half", BlockStairs.EnumHalf.class);
    public static final PropertyEnum<BlockStairs.EnumShape> SHAPE = PropertyEnum.<BlockStairs.EnumShape>create("shape", BlockStairs.EnumShape.class);
    protected static final AxisAlignedBB AABB_SLAB_TOP = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185714_e = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185716_f = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185718_g = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB field_185710_B = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185711_C = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 0.5D);
    protected static final AxisAlignedBB field_185713_D = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB field_185715_E = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185717_F = new AxisAlignedBB(0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_SLAB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB field_185720_H = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
    protected static final AxisAlignedBB field_185721_I = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB field_185722_J = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
    protected static final AxisAlignedBB field_185723_K = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB field_185724_L = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
    protected static final AxisAlignedBB field_185725_M = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
    protected static final AxisAlignedBB field_185726_N = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
    protected static final AxisAlignedBB field_185727_O = new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
    private final Block modelBlock;
    private final IBlockState modelState;

    protected BlockStairs(IBlockState p_i45684_1_)
    {
        super(p_i45684_1_.getBlock().material);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(FACING, EnumFacing.NORTH).func_177226_a(HALF, BlockStairs.EnumHalf.BOTTOM).func_177226_a(SHAPE, BlockStairs.EnumShape.STRAIGHT));
        this.modelBlock = p_i45684_1_.getBlock();
        this.modelState = p_i45684_1_;
        this.func_149711_c(this.modelBlock.blockHardness);
        this.func_149752_b(this.modelBlock.blockResistance / 3.0F);
        this.func_149672_a(this.modelBlock.soundType);
        this.func_149713_g(255);
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public void func_185477_a(IBlockState p_185477_1_, World p_185477_2_, BlockPos p_185477_3_, AxisAlignedBB p_185477_4_, List<AxisAlignedBB> p_185477_5_, @Nullable Entity p_185477_6_, boolean p_185477_7_)
    {
        if (!p_185477_7_)
        {
            p_185477_1_ = this.func_176221_a(p_185477_1_, p_185477_2_, p_185477_3_);
        }

        for (AxisAlignedBB axisalignedbb : func_185708_x(p_185477_1_))
        {
            func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, axisalignedbb);
        }
    }

    private static List<AxisAlignedBB> func_185708_x(IBlockState p_185708_0_)
    {
        List<AxisAlignedBB> list = Lists.<AxisAlignedBB>newArrayList();
        boolean flag = p_185708_0_.get(HALF) == BlockStairs.EnumHalf.TOP;
        list.add(flag ? AABB_SLAB_TOP : AABB_SLAB_BOTTOM);
        BlockStairs.EnumShape blockstairs$enumshape = (BlockStairs.EnumShape)p_185708_0_.get(SHAPE);

        if (blockstairs$enumshape == BlockStairs.EnumShape.STRAIGHT || blockstairs$enumshape == BlockStairs.EnumShape.INNER_LEFT || blockstairs$enumshape == BlockStairs.EnumShape.INNER_RIGHT)
        {
            list.add(func_185707_y(p_185708_0_));
        }

        if (blockstairs$enumshape != BlockStairs.EnumShape.STRAIGHT)
        {
            list.add(func_185705_z(p_185708_0_));
        }

        return list;
    }

    private static AxisAlignedBB func_185707_y(IBlockState p_185707_0_)
    {
        boolean flag = p_185707_0_.get(HALF) == BlockStairs.EnumHalf.TOP;

        switch ((EnumFacing)p_185707_0_.get(FACING))
        {
            case NORTH:
            default:
                return flag ? field_185722_J : field_185718_g;

            case SOUTH:
                return flag ? field_185723_K : field_185710_B;

            case WEST:
                return flag ? field_185720_H : field_185714_e;

            case EAST:
                return flag ? field_185721_I : field_185716_f;
        }
    }

    private static AxisAlignedBB func_185705_z(IBlockState p_185705_0_)
    {
        EnumFacing enumfacing = (EnumFacing)p_185705_0_.get(FACING);
        EnumFacing enumfacing1;

        switch ((BlockStairs.EnumShape)p_185705_0_.get(SHAPE))
        {
            case OUTER_LEFT:
            default:
                enumfacing1 = enumfacing;
                break;

            case OUTER_RIGHT:
                enumfacing1 = enumfacing.rotateY();
                break;

            case INNER_RIGHT:
                enumfacing1 = enumfacing.getOpposite();
                break;

            case INNER_LEFT:
                enumfacing1 = enumfacing.rotateYCCW();
        }

        boolean flag = p_185705_0_.get(HALF) == BlockStairs.EnumHalf.TOP;

        switch (enumfacing1)
        {
            case NORTH:
            default:
                return flag ? field_185724_L : field_185711_C;

            case SOUTH:
                return flag ? field_185727_O : field_185717_F;

            case WEST:
                return flag ? field_185726_N : field_185715_E;

            case EAST:
                return flag ? field_185725_M : field_185713_D;
        }
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        p_193383_2_ = this.func_176221_a(p_193383_2_, p_193383_1_, p_193383_3_);

        if (p_193383_4_.getAxis() == EnumFacing.Axis.Y)
        {
            return p_193383_4_ == EnumFacing.UP == (p_193383_2_.get(HALF) == BlockStairs.EnumHalf.TOP) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
        }
        else
        {
            BlockStairs.EnumShape blockstairs$enumshape = (BlockStairs.EnumShape)p_193383_2_.get(SHAPE);

            if (blockstairs$enumshape != BlockStairs.EnumShape.OUTER_LEFT && blockstairs$enumshape != BlockStairs.EnumShape.OUTER_RIGHT)
            {
                EnumFacing enumfacing = (EnumFacing)p_193383_2_.get(FACING);

                switch (blockstairs$enumshape)
                {
                    case INNER_RIGHT:
                        return enumfacing != p_193383_4_ && enumfacing != p_193383_4_.rotateYCCW() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;

                    case INNER_LEFT:
                        return enumfacing != p_193383_4_ && enumfacing != p_193383_4_.rotateY() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;

                    case STRAIGHT:
                        return enumfacing == p_193383_4_ ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;

                    default:
                        return BlockFaceShape.UNDEFINED;
                }
            }
            else
            {
                return BlockFaceShape.UNDEFINED;
            }
        }
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
     * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
     * of whether the block can receive random update ticks
     */
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        this.modelBlock.animateTick(stateIn, worldIn, pos, rand);
    }

    public void func_180649_a(World p_180649_1_, BlockPos p_180649_2_, EntityPlayer p_180649_3_)
    {
        this.modelBlock.func_180649_a(p_180649_1_, p_180649_2_, p_180649_3_);
    }

    /**
     * Called after a player destroys this Block - the posiiton pos may no longer hold the state indicated.
     */
    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state)
    {
        this.modelBlock.onPlayerDestroy(worldIn, pos, state);
    }

    public int func_185484_c(IBlockState p_185484_1_, IBlockAccess p_185484_2_, BlockPos p_185484_3_)
    {
        return this.modelState.func_185889_a(p_185484_2_, p_185484_3_);
    }

    /**
     * Returns how much this block can resist explosions from the passed in entity.
     */
    public float getExplosionResistance(Entity p_149638_1_)
    {
        return this.modelBlock.getExplosionResistance(p_149638_1_);
    }

    public BlockRenderLayer func_180664_k()
    {
        return this.modelBlock.func_180664_k();
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return this.modelBlock.tickRate(worldIn);
    }

    public AxisAlignedBB func_180640_a(IBlockState p_180640_1_, World p_180640_2_, BlockPos p_180640_3_)
    {
        return this.modelState.func_185918_c(p_180640_2_, p_180640_3_);
    }

    public Vec3d func_176197_a(World p_176197_1_, BlockPos p_176197_2_, Entity p_176197_3_, Vec3d p_176197_4_)
    {
        return this.modelBlock.func_176197_a(p_176197_1_, p_176197_2_, p_176197_3_, p_176197_4_);
    }

    public boolean func_149703_v()
    {
        return this.modelBlock.func_149703_v();
    }

    public boolean func_176209_a(IBlockState p_176209_1_, boolean p_176209_2_)
    {
        return this.modelBlock.func_176209_a(p_176209_1_, p_176209_2_);
    }

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        return this.modelBlock.func_176196_c(p_176196_1_, p_176196_2_);
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        this.modelState.func_189546_a(p_176213_1_, p_176213_2_, Blocks.AIR, p_176213_2_);
        this.modelBlock.func_176213_c(p_176213_1_, p_176213_2_, this.modelState);
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        this.modelBlock.func_180663_b(p_180663_1_, p_180663_2_, this.modelState);
    }

    /**
     * Called when the given entity walks on this Block
     */
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
        this.modelBlock.onEntityWalk(worldIn, pos, entityIn);
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        this.modelBlock.func_180650_b(p_180650_1_, p_180650_2_, p_180650_3_, p_180650_4_);
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        return this.modelBlock.func_180639_a(p_180639_1_, p_180639_2_, this.modelState, p_180639_4_, p_180639_5_, EnumFacing.DOWN, 0.0F, 0.0F, 0.0F);
    }

    /**
     * Called when this Block is destroyed by an Explosion
     */
    public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn)
    {
        this.modelBlock.onExplosionDestroy(worldIn, pos, explosionIn);
    }

    public boolean func_185481_k(IBlockState p_185481_1_)
    {
        return p_185481_1_.get(HALF) == BlockStairs.EnumHalf.TOP;
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public MapColor getMaterialColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return this.modelBlock.getMaterialColor(this.modelState, worldIn, pos);
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        IBlockState iblockstate = super.func_180642_a(p_180642_1_, p_180642_2_, p_180642_3_, p_180642_4_, p_180642_5_, p_180642_6_, p_180642_7_, p_180642_8_);
        iblockstate = iblockstate.func_177226_a(FACING, p_180642_8_.getHorizontalFacing()).func_177226_a(SHAPE, BlockStairs.EnumShape.STRAIGHT);
        return p_180642_3_ != EnumFacing.DOWN && (p_180642_3_ == EnumFacing.UP || (double)p_180642_5_ <= 0.5D) ? iblockstate.func_177226_a(HALF, BlockStairs.EnumHalf.BOTTOM) : iblockstate.func_177226_a(HALF, BlockStairs.EnumHalf.TOP);
    }

    @Nullable
    public RayTraceResult func_180636_a(IBlockState p_180636_1_, World p_180636_2_, BlockPos p_180636_3_, Vec3d p_180636_4_, Vec3d p_180636_5_)
    {
        List<RayTraceResult> list = Lists.<RayTraceResult>newArrayList();

        for (AxisAlignedBB axisalignedbb : func_185708_x(this.func_176221_a(p_180636_1_, p_180636_2_, p_180636_3_)))
        {
            list.add(this.func_185503_a(p_180636_3_, p_180636_4_, p_180636_5_, axisalignedbb));
        }

        RayTraceResult raytraceresult1 = null;
        double d1 = 0.0D;

        for (RayTraceResult raytraceresult : list)
        {
            if (raytraceresult != null)
            {
                double d0 = raytraceresult.hitResult.squareDistanceTo(p_180636_5_);

                if (d0 > d1)
                {
                    raytraceresult1 = raytraceresult;
                    d1 = d0;
                }
            }
        }

        return raytraceresult1;
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        IBlockState iblockstate = this.getDefaultState().func_177226_a(HALF, (p_176203_1_ & 4) > 0 ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM);
        iblockstate = iblockstate.func_177226_a(FACING, EnumFacing.byIndex(5 - (p_176203_1_ & 3)));
        return iblockstate;
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;

        if (p_176201_1_.get(HALF) == BlockStairs.EnumHalf.TOP)
        {
            i |= 4;
        }

        i = i | 5 - ((EnumFacing)p_176201_1_.get(FACING)).getIndex();
        return i;
    }

    public IBlockState func_176221_a(IBlockState p_176221_1_, IBlockAccess p_176221_2_, BlockPos p_176221_3_)
    {
        return p_176221_1_.func_177226_a(SHAPE, func_185706_d(p_176221_1_, p_176221_2_, p_176221_3_));
    }

    private static BlockStairs.EnumShape func_185706_d(IBlockState p_185706_0_, IBlockAccess p_185706_1_, BlockPos p_185706_2_)
    {
        EnumFacing enumfacing = (EnumFacing)p_185706_0_.get(FACING);
        IBlockState iblockstate = p_185706_1_.getBlockState(p_185706_2_.offset(enumfacing));

        if (isBlockStairs(iblockstate) && p_185706_0_.get(HALF) == iblockstate.get(HALF))
        {
            EnumFacing enumfacing1 = (EnumFacing)iblockstate.get(FACING);

            if (enumfacing1.getAxis() != ((EnumFacing)p_185706_0_.get(FACING)).getAxis() && isDifferentStairs(p_185706_0_, p_185706_1_, p_185706_2_, enumfacing1.getOpposite()))
            {
                if (enumfacing1 == enumfacing.rotateYCCW())
                {
                    return BlockStairs.EnumShape.OUTER_LEFT;
                }

                return BlockStairs.EnumShape.OUTER_RIGHT;
            }
        }

        IBlockState iblockstate1 = p_185706_1_.getBlockState(p_185706_2_.offset(enumfacing.getOpposite()));

        if (isBlockStairs(iblockstate1) && p_185706_0_.get(HALF) == iblockstate1.get(HALF))
        {
            EnumFacing enumfacing2 = (EnumFacing)iblockstate1.get(FACING);

            if (enumfacing2.getAxis() != ((EnumFacing)p_185706_0_.get(FACING)).getAxis() && isDifferentStairs(p_185706_0_, p_185706_1_, p_185706_2_, enumfacing2))
            {
                if (enumfacing2 == enumfacing.rotateYCCW())
                {
                    return BlockStairs.EnumShape.INNER_LEFT;
                }

                return BlockStairs.EnumShape.INNER_RIGHT;
            }
        }

        return BlockStairs.EnumShape.STRAIGHT;
    }

    private static boolean isDifferentStairs(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing face)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos.offset(face));
        return !isBlockStairs(iblockstate) || iblockstate.get(FACING) != state.get(FACING) || iblockstate.get(HALF) != state.get(HALF);
    }

    public static boolean isBlockStairs(IBlockState state)
    {
        return state.getBlock() instanceof BlockStairs;
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
        return state.func_177226_a(FACING, rot.rotate((EnumFacing)state.get(FACING)));
    }

    @SuppressWarnings("incomplete-switch")

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        EnumFacing enumfacing = (EnumFacing)state.get(FACING);
        BlockStairs.EnumShape blockstairs$enumshape = (BlockStairs.EnumShape)state.get(SHAPE);

        switch (mirrorIn)
        {
            case LEFT_RIGHT:
                if (enumfacing.getAxis() == EnumFacing.Axis.Z)
                {
                    switch (blockstairs$enumshape)
                    {
                        case OUTER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).func_177226_a(SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);

                        case OUTER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).func_177226_a(SHAPE, BlockStairs.EnumShape.OUTER_LEFT);

                        case INNER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).func_177226_a(SHAPE, BlockStairs.EnumShape.INNER_LEFT);

                        case INNER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).func_177226_a(SHAPE, BlockStairs.EnumShape.INNER_RIGHT);

                        default:
                            return state.rotate(Rotation.CLOCKWISE_180);
                    }
                }

                break;

            case FRONT_BACK:
                if (enumfacing.getAxis() == EnumFacing.Axis.X)
                {
                    switch (blockstairs$enumshape)
                    {
                        case OUTER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).func_177226_a(SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);

                        case OUTER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).func_177226_a(SHAPE, BlockStairs.EnumShape.OUTER_LEFT);

                        case INNER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).func_177226_a(SHAPE, BlockStairs.EnumShape.INNER_RIGHT);

                        case INNER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).func_177226_a(SHAPE, BlockStairs.EnumShape.INNER_LEFT);

                        case STRAIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180);
                    }
                }
        }

        return super.mirror(state, mirrorIn);
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, HALF, SHAPE});
    }

    public static enum EnumHalf implements IStringSerializable
    {
        TOP("top"),
        BOTTOM("bottom");

        private final String field_176709_c;

        private EnumHalf(String p_i45683_3_)
        {
            this.field_176709_c = p_i45683_3_;
        }

        public String toString()
        {
            return this.field_176709_c;
        }

        public String getName()
        {
            return this.field_176709_c;
        }
    }

    public static enum EnumShape implements IStringSerializable
    {
        STRAIGHT("straight"),
        INNER_LEFT("inner_left"),
        INNER_RIGHT("inner_right"),
        OUTER_LEFT("outer_left"),
        OUTER_RIGHT("outer_right");

        private final String field_176699_f;

        private EnumShape(String p_i45682_3_)
        {
            this.field_176699_f = p_i45682_3_;
        }

        public String toString()
        {
            return this.field_176699_f;
        }

        public String getName()
        {
            return this.field_176699_f;
        }
    }
}

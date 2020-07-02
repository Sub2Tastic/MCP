package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSkull extends BlockContainer
{
    public static final PropertyDirection field_176418_a = BlockDirectional.FACING;
    public static final PropertyBool field_176417_b = PropertyBool.create("nodrop");
    private static final Predicate<BlockWorldState> field_176419_M = new Predicate<BlockWorldState>()
    {
        public boolean apply(@Nullable BlockWorldState p_apply_1_)
        {
            return p_apply_1_.getBlockState() != null && p_apply_1_.getBlockState().getBlock() == Blocks.field_150465_bP && p_apply_1_.getTileEntity() instanceof TileEntitySkull && ((TileEntitySkull)p_apply_1_.getTileEntity()).func_145904_a() == 1;
        }
    };
    protected static final AxisAlignedBB field_185582_c = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D);
    protected static final AxisAlignedBB field_185583_d = new AxisAlignedBB(0.25D, 0.25D, 0.5D, 0.75D, 0.75D, 1.0D);
    protected static final AxisAlignedBB field_185584_e = new AxisAlignedBB(0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 0.5D);
    protected static final AxisAlignedBB field_185585_f = new AxisAlignedBB(0.5D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D);
    protected static final AxisAlignedBB field_185586_g = new AxisAlignedBB(0.0D, 0.25D, 0.25D, 0.5D, 0.75D, 0.75D);
    private BlockPattern field_176420_N;
    private BlockPattern field_176421_O;

    protected BlockSkull()
    {
        super(Material.MISCELLANEOUS);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176418_a, EnumFacing.NORTH).func_177226_a(field_176417_b, Boolean.valueOf(false)));
    }

    public String func_149732_F()
    {
        return I18n.func_74838_a("tile.skull.skeleton.name");
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public boolean func_190946_v(IBlockState p_190946_1_)
    {
        return true;
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        switch ((EnumFacing)p_185496_1_.get(field_176418_a))
        {
            case UP:
            default:
                return field_185582_c;

            case NORTH:
                return field_185583_d;

            case SOUTH:
                return field_185584_e;

            case WEST:
                return field_185585_f;

            case EAST:
                return field_185586_g;
        }
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        return this.getDefaultState().func_177226_a(field_176418_a, p_180642_8_.getHorizontalFacing()).func_177226_a(field_176417_b, Boolean.valueOf(false));
    }

    public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntitySkull();
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        int i = 0;
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntitySkull)
        {
            i = ((TileEntitySkull)tileentity).func_145904_a();
        }

        return new ItemStack(Items.field_151144_bL, 1, i);
    }

    public void func_180653_a(World p_180653_1_, BlockPos p_180653_2_, IBlockState p_180653_3_, float p_180653_4_, int p_180653_5_)
    {
    }

    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually
     * collect this block
     */
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (player.abilities.isCreativeMode)
        {
            state = state.func_177226_a(field_176417_b, Boolean.valueOf(true));
            worldIn.setBlockState(pos, state, 4);
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        if (!p_180663_1_.isRemote)
        {
            if (!((Boolean)p_180663_3_.get(field_176417_b)).booleanValue())
            {
                TileEntity tileentity = p_180663_1_.getTileEntity(p_180663_2_);

                if (tileentity instanceof TileEntitySkull)
                {
                    TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;
                    ItemStack itemstack = this.getItem(p_180663_1_, p_180663_2_, p_180663_3_);

                    if (tileentityskull.func_145904_a() == 3 && tileentityskull.getPlayerProfile() != null)
                    {
                        itemstack.setTag(new NBTTagCompound());
                        NBTTagCompound nbttagcompound = new NBTTagCompound();
                        NBTUtil.writeGameProfile(nbttagcompound, tileentityskull.getPlayerProfile());
                        itemstack.getTag().func_74782_a("SkullOwner", nbttagcompound);
                    }

                    spawnAsEntity(p_180663_1_, p_180663_2_, itemstack);
                }
            }

            super.func_180663_b(p_180663_1_, p_180663_2_, p_180663_3_);
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.field_151144_bL;
    }

    public boolean func_176415_b(World p_176415_1_, BlockPos p_176415_2_, ItemStack p_176415_3_)
    {
        if (p_176415_3_.func_77960_j() == 1 && p_176415_2_.getY() >= 2 && p_176415_1_.getDifficulty() != EnumDifficulty.PEACEFUL && !p_176415_1_.isRemote)
        {
            return this.func_176414_j().match(p_176415_1_, p_176415_2_) != null;
        }
        else
        {
            return false;
        }
    }

    public void func_180679_a(World p_180679_1_, BlockPos p_180679_2_, TileEntitySkull p_180679_3_)
    {
        if (p_180679_3_.func_145904_a() == 1 && p_180679_2_.getY() >= 2 && p_180679_1_.getDifficulty() != EnumDifficulty.PEACEFUL && !p_180679_1_.isRemote)
        {
            BlockPattern blockpattern = this.func_176416_l();
            BlockPattern.PatternHelper blockpattern$patternhelper = blockpattern.match(p_180679_1_, p_180679_2_);

            if (blockpattern$patternhelper != null)
            {
                for (int i = 0; i < 3; ++i)
                {
                    BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, 0, 0);
                    p_180679_1_.setBlockState(blockworldstate.getPos(), blockworldstate.getBlockState().func_177226_a(field_176417_b, Boolean.valueOf(true)), 2);
                }

                for (int j = 0; j < blockpattern.getPalmLength(); ++j)
                {
                    for (int k = 0; k < blockpattern.getThumbLength(); ++k)
                    {
                        BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(j, k, 0);
                        p_180679_1_.setBlockState(blockworldstate1.getPos(), Blocks.AIR.getDefaultState(), 2);
                    }
                }

                BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 0, 0).getPos();
                EntityWither entitywither = new EntityWither(p_180679_1_);
                BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
                entitywither.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.55D, (double)blockpos1.getZ() + 0.5D, blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F, 0.0F);
                entitywither.renderYawOffset = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F;
                entitywither.ignite();

                for (EntityPlayerMP entityplayermp : p_180679_1_.func_72872_a(EntityPlayerMP.class, entitywither.getBoundingBox().grow(50.0D)))
                {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitywither);
                }

                p_180679_1_.addEntity0(entitywither);

                for (int l = 0; l < 120; ++l)
                {
                    p_180679_1_.func_175688_a(EnumParticleTypes.SNOWBALL, (double)blockpos.getX() + p_180679_1_.rand.nextDouble(), (double)(blockpos.getY() - 2) + p_180679_1_.rand.nextDouble() * 3.9D, (double)blockpos.getZ() + p_180679_1_.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
                }

                for (int i1 = 0; i1 < blockpattern.getPalmLength(); ++i1)
                {
                    for (int j1 = 0; j1 < blockpattern.getThumbLength(); ++j1)
                    {
                        BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(i1, j1, 0);
                        p_180679_1_.func_175722_b(blockworldstate2.getPos(), Blocks.AIR, false);
                    }
                }
            }
        }
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_176418_a, EnumFacing.byIndex(p_176203_1_ & 7)).func_177226_a(field_176417_b, Boolean.valueOf((p_176203_1_ & 8) > 0));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((EnumFacing)p_176201_1_.get(field_176418_a)).getIndex();

        if (((Boolean)p_176201_1_.get(field_176417_b)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
        return state.func_177226_a(field_176418_a, rot.rotate((EnumFacing)state.get(field_176418_a)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation((EnumFacing)state.get(field_176418_a)));
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176418_a, field_176417_b});
    }

    protected BlockPattern func_176414_j()
    {
        if (this.field_176420_N == null)
        {
            this.field_176420_N = FactoryBlockPattern.start().aisle("   ", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SOUL_SAND))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }

        return this.field_176420_N;
    }

    protected BlockPattern func_176416_l()
    {
        if (this.field_176421_O == null)
        {
            this.field_176421_O = FactoryBlockPattern.start().aisle("^^^", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SOUL_SAND))).where('^', field_176419_M).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }

        return this.field_176421_O;
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }
}

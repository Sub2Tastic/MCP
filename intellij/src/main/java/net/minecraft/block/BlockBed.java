package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBed extends BlockHorizontal implements ITileEntityProvider
{
    public static final PropertyEnum<BlockBed.EnumPartType> PART = PropertyEnum.<BlockBed.EnumPartType>create("part", BlockBed.EnumPartType.class);
    public static final PropertyBool OCCUPIED = PropertyBool.create("occupied");
    protected static final AxisAlignedBB field_185513_c = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D);

    public BlockBed()
    {
        super(Material.WOOL);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(PART, BlockBed.EnumPartType.FOOT).func_177226_a(OCCUPIED, Boolean.valueOf(false)));
        this.field_149758_A = true;
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public MapColor getMaterialColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if (state.get(PART) == BlockBed.EnumPartType.FOOT)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityBed)
            {
                EnumDyeColor enumdyecolor = ((TileEntityBed)tileentity).getColor();
                return MapColor.func_193558_a(enumdyecolor);
            }
        }

        return MapColor.WOOL;
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (p_180639_1_.isRemote)
        {
            return true;
        }
        else
        {
            if (p_180639_3_.get(PART) != BlockBed.EnumPartType.HEAD)
            {
                p_180639_2_ = p_180639_2_.offset((EnumFacing)p_180639_3_.get(HORIZONTAL_FACING));
                p_180639_3_ = p_180639_1_.getBlockState(p_180639_2_);

                if (p_180639_3_.getBlock() != this)
                {
                    return true;
                }
            }

            if (p_180639_1_.dimension.canRespawnHere() && p_180639_1_.func_180494_b(p_180639_2_) != Biomes.NETHER)
            {
                if (((Boolean)p_180639_3_.get(OCCUPIED)).booleanValue())
                {
                    EntityPlayer entityplayer = this.func_176470_e(p_180639_1_, p_180639_2_);

                    if (entityplayer != null)
                    {
                        p_180639_4_.sendStatusMessage(new TextComponentTranslation("tile.bed.occupied", new Object[0]), true);
                        return true;
                    }

                    p_180639_3_ = p_180639_3_.func_177226_a(OCCUPIED, Boolean.valueOf(false));
                    p_180639_1_.setBlockState(p_180639_2_, p_180639_3_, 4);
                }

                EntityPlayer.SleepResult entityplayer$sleepresult = p_180639_4_.func_180469_a(p_180639_2_);

                if (entityplayer$sleepresult == EntityPlayer.SleepResult.OK)
                {
                    p_180639_3_ = p_180639_3_.func_177226_a(OCCUPIED, Boolean.valueOf(true));
                    p_180639_1_.setBlockState(p_180639_2_, p_180639_3_, 4);
                    return true;
                }
                else
                {
                    if (entityplayer$sleepresult == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW)
                    {
                        p_180639_4_.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]), true);
                    }
                    else if (entityplayer$sleepresult == EntityPlayer.SleepResult.NOT_SAFE)
                    {
                        p_180639_4_.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe", new Object[0]), true);
                    }
                    else if (entityplayer$sleepresult == EntityPlayer.SleepResult.TOO_FAR_AWAY)
                    {
                        p_180639_4_.sendStatusMessage(new TextComponentTranslation("tile.bed.tooFarAway", new Object[0]), true);
                    }

                    return true;
                }
            }
            else
            {
                p_180639_1_.func_175698_g(p_180639_2_);
                BlockPos blockpos = p_180639_2_.offset(((EnumFacing)p_180639_3_.get(HORIZONTAL_FACING)).getOpposite());

                if (p_180639_1_.getBlockState(blockpos).getBlock() == this)
                {
                    p_180639_1_.func_175698_g(blockpos);
                }

                p_180639_1_.func_72885_a((Entity)null, (double)p_180639_2_.getX() + 0.5D, (double)p_180639_2_.getY() + 0.5D, (double)p_180639_2_.getZ() + 0.5D, 5.0F, true, true);
                return true;
            }
        }
    }

    @Nullable
    private EntityPlayer func_176470_e(World p_176470_1_, BlockPos p_176470_2_)
    {
        for (EntityPlayer entityplayer : p_176470_1_.field_73010_i)
        {
            if (entityplayer.isSleeping() && entityplayer.field_71081_bT.equals(p_176470_2_))
            {
                return entityplayer;
            }
        }

        return null;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    /**
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance * 0.5F);
    }

    /**
     * Called when an Entity lands on this Block. This method *must* update motionY because the entity will not do that
     * on its own
     */
    public void onLanded(World worldIn, Entity entityIn)
    {
        if (entityIn.func_70093_af())
        {
            super.onLanded(worldIn, entityIn);
        }
        else if (entityIn.field_70181_x < 0.0D)
        {
            entityIn.field_70181_x = -entityIn.field_70181_x * 0.6600000262260437D;

            if (!(entityIn instanceof EntityLivingBase))
            {
                entityIn.field_70181_x *= 0.8D;
            }
        }
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        EnumFacing enumfacing = (EnumFacing)p_189540_1_.get(HORIZONTAL_FACING);

        if (p_189540_1_.get(PART) == BlockBed.EnumPartType.FOOT)
        {
            if (p_189540_2_.getBlockState(p_189540_3_.offset(enumfacing)).getBlock() != this)
            {
                p_189540_2_.func_175698_g(p_189540_3_);
            }
        }
        else if (p_189540_2_.getBlockState(p_189540_3_.offset(enumfacing.getOpposite())).getBlock() != this)
        {
            if (!p_189540_2_.isRemote)
            {
                this.func_176226_b(p_189540_2_, p_189540_3_, p_189540_1_, 0);
            }

            p_189540_2_.func_175698_g(p_189540_3_);
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return p_180660_1_.get(PART) == BlockBed.EnumPartType.FOOT ? Items.AIR : Items.field_151104_aV;
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185513_c;
    }

    public boolean func_190946_v(IBlockState p_190946_1_)
    {
        return true;
    }

    @Nullable
    public static BlockPos func_176468_a(World p_176468_0_, BlockPos p_176468_1_, int p_176468_2_)
    {
        EnumFacing enumfacing = (EnumFacing)p_176468_0_.getBlockState(p_176468_1_).get(HORIZONTAL_FACING);
        int i = p_176468_1_.getX();
        int j = p_176468_1_.getY();
        int k = p_176468_1_.getZ();

        for (int l = 0; l <= 1; ++l)
        {
            int i1 = i - enumfacing.getXOffset() * l - 1;
            int j1 = k - enumfacing.getZOffset() * l - 1;
            int k1 = i1 + 2;
            int l1 = j1 + 2;

            for (int i2 = i1; i2 <= k1; ++i2)
            {
                for (int j2 = j1; j2 <= l1; ++j2)
                {
                    BlockPos blockpos = new BlockPos(i2, j, j2);

                    if (func_176469_d(p_176468_0_, blockpos))
                    {
                        if (p_176468_2_ <= 0)
                        {
                            return blockpos;
                        }

                        --p_176468_2_;
                    }
                }
            }
        }

        return null;
    }

    protected static boolean func_176469_d(World p_176469_0_, BlockPos p_176469_1_)
    {
        return p_176469_0_.getBlockState(p_176469_1_.down()).func_185896_q() && !p_176469_0_.getBlockState(p_176469_1_).getMaterial().isSolid() && !p_176469_0_.getBlockState(p_176469_1_.up()).getMaterial().isSolid();
    }

    public void func_180653_a(World p_180653_1_, BlockPos p_180653_2_, IBlockState p_180653_3_, float p_180653_4_, int p_180653_5_)
    {
        if (p_180653_3_.get(PART) == BlockBed.EnumPartType.HEAD)
        {
            TileEntity tileentity = p_180653_1_.getTileEntity(p_180653_2_);
            EnumDyeColor enumdyecolor = tileentity instanceof TileEntityBed ? ((TileEntityBed)tileentity).getColor() : EnumDyeColor.RED;
            spawnAsEntity(p_180653_1_, p_180653_2_, new ItemStack(Items.field_151104_aV, 1, enumdyecolor.func_176765_a()));
        }
    }

    /**
     * @deprecated call via {@link IBlockState#getMobilityFlag()} whenever possible. Implementing/overriding is fine.
     */
    public EnumPushReaction getPushReaction(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
    }

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        BlockPos blockpos = pos;

        if (state.get(PART) == BlockBed.EnumPartType.FOOT)
        {
            blockpos = pos.offset((EnumFacing)state.get(HORIZONTAL_FACING));
        }

        TileEntity tileentity = worldIn.getTileEntity(blockpos);
        EnumDyeColor enumdyecolor = tileentity instanceof TileEntityBed ? ((TileEntityBed)tileentity).getColor() : EnumDyeColor.RED;
        return new ItemStack(Items.field_151104_aV, 1, enumdyecolor.func_176765_a());
    }

    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually
     * collect this block
     */
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (player.abilities.isCreativeMode && state.get(PART) == BlockBed.EnumPartType.FOOT)
        {
            BlockPos blockpos = pos.offset((EnumFacing)state.get(HORIZONTAL_FACING));

            if (worldIn.getBlockState(blockpos).getBlock() == this)
            {
                worldIn.func_175698_g(blockpos);
            }
        }
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
    {
        if (state.get(PART) == BlockBed.EnumPartType.HEAD && te instanceof TileEntityBed)
        {
            TileEntityBed tileentitybed = (TileEntityBed)te;
            ItemStack itemstack = tileentitybed.func_193049_f();
            spawnAsEntity(worldIn, pos, itemstack);
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, (TileEntity)null, stack);
        }
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        super.func_180663_b(p_180663_1_, p_180663_2_, p_180663_3_);
        p_180663_1_.removeTileEntity(p_180663_2_);
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        EnumFacing enumfacing = EnumFacing.byHorizontalIndex(p_176203_1_);
        return (p_176203_1_ & 8) > 0 ? this.getDefaultState().func_177226_a(PART, BlockBed.EnumPartType.HEAD).func_177226_a(HORIZONTAL_FACING, enumfacing).func_177226_a(OCCUPIED, Boolean.valueOf((p_176203_1_ & 4) > 0)) : this.getDefaultState().func_177226_a(PART, BlockBed.EnumPartType.FOOT).func_177226_a(HORIZONTAL_FACING, enumfacing);
    }

    public IBlockState func_176221_a(IBlockState p_176221_1_, IBlockAccess p_176221_2_, BlockPos p_176221_3_)
    {
        if (p_176221_1_.get(PART) == BlockBed.EnumPartType.FOOT)
        {
            IBlockState iblockstate = p_176221_2_.getBlockState(p_176221_3_.offset((EnumFacing)p_176221_1_.get(HORIZONTAL_FACING)));

            if (iblockstate.getBlock() == this)
            {
                p_176221_1_ = p_176221_1_.func_177226_a(OCCUPIED, iblockstate.get(OCCUPIED));
            }
        }

        return p_176221_1_;
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

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((EnumFacing)p_176201_1_.get(HORIZONTAL_FACING)).getHorizontalIndex();

        if (p_176201_1_.get(PART) == BlockBed.EnumPartType.HEAD)
        {
            i |= 8;

            if (((Boolean)p_176201_1_.get(OCCUPIED)).booleanValue())
            {
                i |= 4;
            }
        }

        return i;
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {HORIZONTAL_FACING, PART, OCCUPIED});
    }

    public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityBed();
    }

    public static boolean func_193385_b(int p_193385_0_)
    {
        return (p_193385_0_ & 8) != 0;
    }

    public static enum EnumPartType implements IStringSerializable
    {
        HEAD("head"),
        FOOT("foot");

        private final String name;

        private EnumPartType(String p_i45735_3_)
        {
            this.name = p_i45735_3_;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
    }
}

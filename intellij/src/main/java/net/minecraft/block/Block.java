package net.minecraft.block;

import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Block
{
    private static final ResourceLocation field_176230_a = new ResourceLocation("air");
    public static final RegistryNamespacedDefaultedByKey<ResourceLocation, Block> field_149771_c = new RegistryNamespacedDefaultedByKey<ResourceLocation, Block>(field_176230_a);
    public static final ObjectIntIdentityMap<IBlockState> BLOCK_STATE_IDS = new ObjectIntIdentityMap<IBlockState>();
    public static final AxisAlignedBB field_185505_j = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    @Nullable
    public static final AxisAlignedBB field_185506_k = null;
    private CreativeTabs field_149772_a;
    protected boolean field_149787_q;
    protected int field_149786_r;
    protected boolean field_149785_s;

    /** Amount of light emitted */
    protected int lightValue;
    protected boolean field_149783_u;

    /** Indicates how many hits it takes to break a block. */
    protected float blockHardness;

    /** Indicates how much this block can resist explosions */
    protected float blockResistance;
    protected boolean field_149790_y;

    /**
     * Flags whether or not this block is of a type that needs random ticking. Ref-counted by ExtendedBlockStorage in
     * order to broadly cull a chunk from the random chunk update list for efficiency's sake.
     */
    protected boolean ticksRandomly;
    protected boolean field_149758_A;
    protected SoundType soundType;
    public float field_149763_I;
    protected final Material material;
    protected final MapColor materialColor;

    /**
     * Determines how much velocity is maintained while moving on top of this block
     */
    public float slipperiness;
    protected final BlockStateContainer stateContainer;
    private IBlockState field_176228_M;
    private String translationKey;

    public static int func_149682_b(Block p_149682_0_)
    {
        return field_149771_c.getId(p_149682_0_);
    }

    public static int func_176210_f(IBlockState p_176210_0_)
    {
        Block block = p_176210_0_.getBlock();
        return func_149682_b(block) + (block.func_176201_c(p_176210_0_) << 12);
    }

    public static Block func_149729_e(int p_149729_0_)
    {
        return field_149771_c.func_148754_a(p_149729_0_);
    }

    public static IBlockState func_176220_d(int p_176220_0_)
    {
        int i = p_176220_0_ & 4095;
        int j = p_176220_0_ >> 12 & 15;
        return func_149729_e(i).func_176203_a(j);
    }

    public static Block getBlockFromItem(@Nullable Item itemIn)
    {
        return itemIn instanceof ItemBlock ? ((ItemBlock)itemIn).getBlock() : Blocks.AIR;
    }

    @Nullable
    public static Block func_149684_b(String p_149684_0_)
    {
        ResourceLocation resourcelocation = new ResourceLocation(p_149684_0_);

        if (field_149771_c.func_148741_d(resourcelocation))
        {
            return field_149771_c.getOrDefault(resourcelocation);
        }
        else
        {
            try
            {
                return field_149771_c.func_148754_a(Integer.parseInt(p_149684_0_));
            }
            catch (NumberFormatException var3)
            {
                return null;
            }
        }
    }

    @Deprecated
    public boolean func_185481_k(IBlockState p_185481_1_)
    {
        return p_185481_1_.getMaterial().isOpaque() && p_185481_1_.func_185917_h();
    }

    @Deprecated
    public boolean func_149730_j(IBlockState p_149730_1_)
    {
        return this.field_149787_q;
    }

    @Deprecated
    public boolean func_189872_a(IBlockState p_189872_1_, Entity p_189872_2_)
    {
        return true;
    }

    @Deprecated
    public int func_149717_k(IBlockState p_149717_1_)
    {
        return this.field_149786_r;
    }

    @Deprecated
    public boolean func_149751_l(IBlockState p_149751_1_)
    {
        return this.field_149785_s;
    }

    @Deprecated

    /**
     * Amount of light emitted
     * @deprecated prefer calling {@link IBlockState#getLightValue()}
     */
    public int getLightValue(IBlockState state)
    {
        return this.lightValue;
    }

    @Deprecated
    public boolean func_149710_n(IBlockState p_149710_1_)
    {
        return this.field_149783_u;
    }

    @Deprecated

    /**
     * Get a material of block
     * @deprecated call via {@link IBlockState#getMaterial()} whenever possible. Implementing/overriding is fine.
     */
    public Material getMaterial(IBlockState state)
    {
        return this.material;
    }

    @Deprecated

    /**
     * Get the MapColor for this Block and the given BlockState
     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public MapColor getMaterialColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return this.materialColor;
    }

    @Deprecated
    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState();
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        if (p_176201_1_.func_177227_a().isEmpty())
        {
            return 0;
        }
        else
        {
            throw new IllegalArgumentException("Don't know how to convert " + p_176201_1_ + " back into data...");
        }
    }

    @Deprecated
    public IBlockState func_176221_a(IBlockState p_176221_1_, IBlockAccess p_176221_2_, BlockPos p_176221_3_)
    {
        return p_176221_1_;
    }

    @Deprecated

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
        return state;
    }

    @Deprecated

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        return state;
    }

    public Block(Material p_i46399_1_, MapColor p_i46399_2_)
    {
        this.field_149790_y = true;
        this.soundType = SoundType.STONE;
        this.field_149763_I = 1.0F;
        this.slipperiness = 0.6F;
        this.material = p_i46399_1_;
        this.materialColor = p_i46399_2_;
        this.stateContainer = this.func_180661_e();
        this.setDefaultState(this.stateContainer.getBaseState());
        this.field_149787_q = this.getDefaultState().func_185914_p();
        this.field_149786_r = this.field_149787_q ? 255 : 0;
        this.field_149785_s = !p_i46399_1_.func_76228_b();
    }

    protected Block(Material p_i45394_1_)
    {
        this(p_i45394_1_, p_i45394_1_.getColor());
    }

    protected Block func_149672_a(SoundType p_149672_1_)
    {
        this.soundType = p_149672_1_;
        return this;
    }

    protected Block func_149713_g(int p_149713_1_)
    {
        this.field_149786_r = p_149713_1_;
        return this;
    }

    protected Block func_149715_a(float p_149715_1_)
    {
        this.lightValue = (int)(15.0F * p_149715_1_);
        return this;
    }

    protected Block func_149752_b(float p_149752_1_)
    {
        this.blockResistance = p_149752_1_ * 3.0F;
        return this;
    }

    protected static boolean func_193384_b(Block p_193384_0_)
    {
        return p_193384_0_ instanceof BlockShulkerBox || p_193384_0_ instanceof BlockLeaves || p_193384_0_ instanceof BlockTrapDoor || p_193384_0_ == Blocks.BEACON || p_193384_0_ == Blocks.CAULDRON || p_193384_0_ == Blocks.GLASS || p_193384_0_ == Blocks.GLOWSTONE || p_193384_0_ == Blocks.ICE || p_193384_0_ == Blocks.SEA_LANTERN || p_193384_0_ == Blocks.field_150399_cn;
    }

    protected static boolean func_193382_c(Block p_193382_0_)
    {
        return func_193384_b(p_193382_0_) || p_193382_0_ == Blocks.PISTON || p_193382_0_ == Blocks.STICKY_PISTON || p_193382_0_ == Blocks.PISTON_HEAD;
    }

    @Deprecated
    public boolean func_149637_q(IBlockState p_149637_1_)
    {
        return p_149637_1_.getMaterial().blocksMovement() && p_149637_1_.func_185917_h();
    }

    @Deprecated
    public boolean func_149721_r(IBlockState p_149721_1_)
    {
        return p_149721_1_.getMaterial().isOpaque() && p_149721_1_.func_185917_h() && !p_149721_1_.canProvidePower();
    }

    @Deprecated
    public boolean func_176214_u(IBlockState p_176214_1_)
    {
        return this.material.blocksMovement() && this.getDefaultState().func_185917_h();
    }

    @Deprecated
    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return true;
    }

    @Deprecated
    public boolean func_190946_v(IBlockState p_190946_1_)
    {
        return false;
    }

    public boolean func_176205_b(IBlockAccess p_176205_1_, BlockPos p_176205_2_)
    {
        return !this.material.blocksMovement();
    }

    @Deprecated

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public boolean func_176200_f(IBlockAccess p_176200_1_, BlockPos p_176200_2_)
    {
        return false;
    }

    protected Block func_149711_c(float p_149711_1_)
    {
        this.blockHardness = p_149711_1_;

        if (this.blockResistance < p_149711_1_ * 5.0F)
        {
            this.blockResistance = p_149711_1_ * 5.0F;
        }

        return this;
    }

    protected Block func_149722_s()
    {
        this.func_149711_c(-1.0F);
        return this;
    }

    @Deprecated

    /**
     * @deprecated call via {@link IBlockState#getBlockHardness(World,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return this.blockHardness;
    }

    protected Block func_149675_a(boolean p_149675_1_)
    {
        this.ticksRandomly = p_149675_1_;
        return this;
    }

    /**
     * Returns whether or not this block is of a type that needs random ticking. Called for ref-counting purposes by
     * ExtendedBlockStorage in order to broadly cull a chunk from the random chunk update list for efficiency's sake.
     */
    public boolean ticksRandomly()
    {
        return this.ticksRandomly;
    }

    public boolean hasTileEntity()
    {
        return this.field_149758_A;
    }

    @Deprecated
    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185505_j;
    }

    @Deprecated
    public int func_185484_c(IBlockState p_185484_1_, IBlockAccess p_185484_2_, BlockPos p_185484_3_)
    {
        int i = p_185484_2_.func_175626_b(p_185484_3_, p_185484_1_.getLightValue());

        if (i == 0 && p_185484_1_.getBlock() instanceof BlockSlab)
        {
            p_185484_3_ = p_185484_3_.down();
            p_185484_1_ = p_185484_2_.getBlockState(p_185484_3_);
            return p_185484_2_.func_175626_b(p_185484_3_, p_185484_1_.getLightValue());
        }
        else
        {
            return i;
        }
    }

    @Deprecated

    /**
     * ""
     */
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing p_176225_4_)
    {
        AxisAlignedBB axisalignedbb = blockState.func_185900_c(blockAccess, pos);

        switch (p_176225_4_)
        {
            case DOWN:
                if (axisalignedbb.minY > 0.0D)
                {
                    return true;
                }

                break;

            case UP:
                if (axisalignedbb.maxY < 1.0D)
                {
                    return true;
                }

                break;

            case NORTH:
                if (axisalignedbb.minZ > 0.0D)
                {
                    return true;
                }

                break;

            case SOUTH:
                if (axisalignedbb.maxZ < 1.0D)
                {
                    return true;
                }

                break;

            case WEST:
                if (axisalignedbb.minX > 0.0D)
                {
                    return true;
                }

                break;

            case EAST:
                if (axisalignedbb.maxX < 1.0D)
                {
                    return true;
                }
        }

        return !blockAccess.getBlockState(pos.offset(p_176225_4_)).func_185914_p();
    }

    @Deprecated
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.SOLID;
    }

    @Deprecated
    public AxisAlignedBB func_180640_a(IBlockState p_180640_1_, World p_180640_2_, BlockPos p_180640_3_)
    {
        return p_180640_1_.func_185900_c(p_180640_2_, p_180640_3_).offset(p_180640_3_);
    }

    @Deprecated
    public void func_185477_a(IBlockState p_185477_1_, World p_185477_2_, BlockPos p_185477_3_, AxisAlignedBB p_185477_4_, List<AxisAlignedBB> p_185477_5_, @Nullable Entity p_185477_6_, boolean p_185477_7_)
    {
        func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, p_185477_1_.func_185890_d(p_185477_2_, p_185477_3_));
    }

    protected static void func_185492_a(BlockPos p_185492_0_, AxisAlignedBB p_185492_1_, List<AxisAlignedBB> p_185492_2_, @Nullable AxisAlignedBB p_185492_3_)
    {
        if (p_185492_3_ != field_185506_k)
        {
            AxisAlignedBB axisalignedbb = p_185492_3_.offset(p_185492_0_);

            if (p_185492_1_.intersects(axisalignedbb))
            {
                p_185492_2_.add(axisalignedbb);
            }
        }
    }

    @Deprecated
    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState p_180646_1_, IBlockAccess p_180646_2_, BlockPos p_180646_3_)
    {
        return p_180646_1_.func_185900_c(p_180646_2_, p_180646_3_);
    }

    @Deprecated
    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return true;
    }

    public boolean func_176209_a(IBlockState p_176209_1_, boolean p_176209_2_)
    {
        return this.func_149703_v();
    }

    public boolean func_149703_v()
    {
        return true;
    }

    public void func_180645_a(World p_180645_1_, BlockPos p_180645_2_, IBlockState p_180645_3_, Random p_180645_4_)
    {
        this.func_180650_b(p_180645_1_, p_180645_2_, p_180645_3_, p_180645_4_);
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
     * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
     * of whether the block can receive random update ticks
     */
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
    }

    /**
     * Called after a player destroys this Block - the posiiton pos may no longer hold the state indicated.
     */
    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state)
    {
    }

    @Deprecated
    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 10;
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
    }

    public int func_149745_a(Random p_149745_1_)
    {
        return 1;
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Item.getItemFromBlock(this);
    }

    @Deprecated

    /**
     * Get the hardness of this Block relative to the ability of the given player
     * @deprecated call via {@link IBlockState#getPlayerRelativeBlockHardness(EntityPlayer,World,BlockPos)} whenever
     * possible. Implementing/overriding is fine.
     */
    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
    {
        float f = state.getBlockHardness(worldIn, pos);

        if (f < 0.0F)
        {
            return 0.0F;
        }
        else
        {
            return !player.canHarvestBlock(state) ? player.getDigSpeed(state) / f / 100.0F : player.getDigSpeed(state) / f / 30.0F;
        }
    }

    public final void func_176226_b(World p_176226_1_, BlockPos p_176226_2_, IBlockState p_176226_3_, int p_176226_4_)
    {
        this.func_180653_a(p_176226_1_, p_176226_2_, p_176226_3_, 1.0F, p_176226_4_);
    }

    public void func_180653_a(World p_180653_1_, BlockPos p_180653_2_, IBlockState p_180653_3_, float p_180653_4_, int p_180653_5_)
    {
        if (!p_180653_1_.isRemote)
        {
            int i = this.func_149679_a(p_180653_5_, p_180653_1_.rand);

            for (int j = 0; j < i; ++j)
            {
                if (p_180653_1_.rand.nextFloat() <= p_180653_4_)
                {
                    Item item = this.func_180660_a(p_180653_3_, p_180653_1_.rand, p_180653_5_);

                    if (item != Items.AIR)
                    {
                        spawnAsEntity(p_180653_1_, p_180653_2_, new ItemStack(item, 1, this.func_180651_a(p_180653_3_)));
                    }
                }
            }
        }
    }

    /**
     * Spawns the given stack into the World at the given position, respecting the doTileDrops gamerule
     */
    public static void spawnAsEntity(World worldIn, BlockPos pos, ItemStack stack)
    {
        if (!worldIn.isRemote && !stack.isEmpty() && worldIn.getGameRules().func_82766_b("doTileDrops"))
        {
            float f = 0.5F;
            double d0 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
            double d1 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
            double d2 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
            EntityItem entityitem = new EntityItem(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, stack);
            entityitem.setDefaultPickupDelay();
            worldIn.addEntity0(entityitem);
        }
    }

    /**
     * Spawns the given amount of experience into the World as XP orb entities
     */
    protected void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount)
    {
        if (!worldIn.isRemote && worldIn.getGameRules().func_82766_b("doTileDrops"))
        {
            while (amount > 0)
            {
                int i = EntityXPOrb.getXPSplit(amount);
                amount -= i;
                worldIn.addEntity0(new EntityXPOrb(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, i));
            }
        }
    }

    public int func_180651_a(IBlockState p_180651_1_)
    {
        return 0;
    }

    /**
     * Returns how much this block can resist explosions from the passed in entity.
     */
    public float getExplosionResistance(Entity p_149638_1_)
    {
        return this.blockResistance / 5.0F;
    }

    @Deprecated
    @Nullable
    public RayTraceResult func_180636_a(IBlockState p_180636_1_, World p_180636_2_, BlockPos p_180636_3_, Vec3d p_180636_4_, Vec3d p_180636_5_)
    {
        return this.func_185503_a(p_180636_3_, p_180636_4_, p_180636_5_, p_180636_1_.func_185900_c(p_180636_2_, p_180636_3_));
    }

    @Nullable
    protected RayTraceResult func_185503_a(BlockPos p_185503_1_, Vec3d p_185503_2_, Vec3d p_185503_3_, AxisAlignedBB p_185503_4_)
    {
        Vec3d vec3d = p_185503_2_.subtract((double)p_185503_1_.getX(), (double)p_185503_1_.getY(), (double)p_185503_1_.getZ());
        Vec3d vec3d1 = p_185503_3_.subtract((double)p_185503_1_.getX(), (double)p_185503_1_.getY(), (double)p_185503_1_.getZ());
        RayTraceResult raytraceresult = p_185503_4_.func_72327_a(vec3d, vec3d1);
        return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitResult.add((double)p_185503_1_.getX(), (double)p_185503_1_.getY(), (double)p_185503_1_.getZ()), raytraceresult.field_178784_b, p_185503_1_);
    }

    /**
     * Called when this Block is destroyed by an Explosion
     */
    public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn)
    {
    }

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.SOLID;
    }

    public boolean func_176198_a(World p_176198_1_, BlockPos p_176198_2_, EnumFacing p_176198_3_)
    {
        return this.func_176196_c(p_176198_1_, p_176198_2_);
    }

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        return p_176196_1_.getBlockState(p_176196_2_).getBlock().material.isReplaceable();
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        return false;
    }

    /**
     * Called when the given entity walks on this Block
     */
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        return this.func_176203_a(p_180642_7_);
    }

    public void func_180649_a(World p_180649_1_, BlockPos p_180649_2_, EntityPlayer p_180649_3_)
    {
    }

    public Vec3d func_176197_a(World p_176197_1_, BlockPos p_176197_2_, Entity p_176197_3_, Vec3d p_176197_4_)
    {
        return p_176197_4_;
    }

    @Deprecated

    /**
     * @deprecated call via {@link IBlockState#getWeakPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return 0;
    }

    @Deprecated

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     * @deprecated call via {@link IBlockState#canProvidePower()} whenever possible. Implementing/overriding is fine.
     */
    public boolean canProvidePower(IBlockState state)
    {
        return false;
    }

    public void func_180634_a(World p_180634_1_, BlockPos p_180634_2_, IBlockState p_180634_3_, Entity p_180634_4_)
    {
    }

    @Deprecated

    /**
     * @deprecated call via {@link IBlockState#getStrongPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return 0;
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        player.addStat(StatList.func_188055_a(this));
        player.addExhaustion(0.005F);

        if (this.func_149700_E() && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0)
        {
            ItemStack itemstack = this.func_180643_i(state);
            spawnAsEntity(worldIn, pos, itemstack);
        }
        else
        {
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            this.func_176226_b(worldIn, pos, state, i);
        }
    }

    protected boolean func_149700_E()
    {
        return this.getDefaultState().func_185917_h() && !this.field_149758_A;
    }

    protected ItemStack func_180643_i(IBlockState p_180643_1_)
    {
        Item item = Item.getItemFromBlock(this);
        int i = 0;

        if (item.func_77614_k())
        {
            i = this.func_176201_c(p_180643_1_);
        }

        return new ItemStack(item, 1, i);
    }

    public int func_149679_a(int p_149679_1_, Random p_149679_2_)
    {
        return this.func_149745_a(p_149679_2_);
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
    }

    /**
     * Return true if an entity can be spawned inside the block (used to get the player's bed spawn location)
     */
    public boolean canSpawnInBlock()
    {
        return !this.material.isSolid() && !this.material.isLiquid();
    }

    public Block func_149663_c(String p_149663_1_)
    {
        this.translationKey = p_149663_1_;
        return this;
    }

    public String func_149732_F()
    {
        return I18n.func_74838_a(this.getTranslationKey() + ".name");
    }

    /**
     * Returns the unlocalized name of the block with "tile." appended to the front.
     */
    public String getTranslationKey()
    {
        return "tile." + this.translationKey;
    }

    @Deprecated

    /**
     * Called on server when World#addBlockEvent is called. If server returns true, then also called on the client. On
     * the Server, this may perform additional changes to the world, like pistons replacing the block with an extended
     * base. On the client, the update may involve replacing tile entities or effects such as sounds or particles
     * @deprecated call via {@link IBlockState#onBlockEventReceived(World,BlockPos,int,int)} whenever possible.
     * Implementing/overriding is fine.
     */
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        return false;
    }

    public boolean func_149652_G()
    {
        return this.field_149790_y;
    }

    protected Block func_149649_H()
    {
        this.field_149790_y = false;
        return this;
    }

    @Deprecated

    /**
     * @deprecated call via {@link IBlockState#getMobilityFlag()} whenever possible. Implementing/overriding is fine.
     */
    public EnumPushReaction getPushReaction(IBlockState state)
    {
        return this.material.getPushReaction();
    }

    @Deprecated
    public float func_185485_f(IBlockState p_185485_1_)
    {
        return p_185485_1_.func_185898_k() ? 0.2F : 1.0F;
    }

    /**
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        entityIn.func_180430_e(fallDistance, 1.0F);
    }

    /**
     * Called when an Entity lands on this Block. This method *must* update motionY because the entity will not do that
     * on its own
     */
    public void onLanded(World worldIn, Entity entityIn)
    {
        entityIn.field_70181_x = 0.0D;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.func_180651_a(state));
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this));
    }

    public CreativeTabs func_149708_J()
    {
        return this.field_149772_a;
    }

    public Block func_149647_a(CreativeTabs p_149647_1_)
    {
        this.field_149772_a = p_149647_1_;
        return this;
    }

    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually
     * collect this block
     */
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
    }

    /**
     * Called similar to random ticks, but only when it is raining.
     */
    public void fillWithRain(World worldIn, BlockPos pos)
    {
    }

    public boolean func_149698_L()
    {
        return true;
    }

    /**
     * Return whether this block can drop from an explosion.
     */
    public boolean canDropFromExplosion(Explosion explosionIn)
    {
        return true;
    }

    public boolean func_149667_c(Block p_149667_1_)
    {
        return this == p_149667_1_;
    }

    public static boolean func_149680_a(Block p_149680_0_, Block p_149680_1_)
    {
        if (p_149680_0_ != null && p_149680_1_ != null)
        {
            return p_149680_0_ == p_149680_1_ ? true : p_149680_0_.func_149667_c(p_149680_1_);
        }
        else
        {
            return false;
        }
    }

    @Deprecated

    /**
     * @deprecated call via {@link IBlockState#hasComparatorInputOverride()} whenever possible. Implementing/overriding
     * is fine.
     */
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return false;
    }

    @Deprecated

    /**
     * @deprecated call via {@link IBlockState#getComparatorInputOverride(World,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return 0;
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[0]);
    }

    public BlockStateContainer getStateContainer()
    {
        return this.stateContainer;
    }

    protected final void setDefaultState(IBlockState state)
    {
        this.field_176228_M = state;
    }

    /**
     * Gets the default state for this block
     */
    public final IBlockState getDefaultState()
    {
        return this.field_176228_M;
    }

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     */
    public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.NONE;
    }

    @Deprecated

    /**
     * @deprecated call via {@link IBlockState#getOffset(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public Vec3d getOffset(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        Block.EnumOffsetType block$enumoffsettype = this.getOffsetType();

        if (block$enumoffsettype == Block.EnumOffsetType.NONE)
        {
            return Vec3d.ZERO;
        }
        else
        {
            long i = MathHelper.getCoordinateRandom(pos.getX(), 0, pos.getZ());
            return new Vec3d(((double)((float)(i >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D, block$enumoffsettype == Block.EnumOffsetType.XYZ ? ((double)((float)(i >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D : 0.0D, ((double)((float)(i >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D);
        }
    }

    public SoundType func_185467_w()
    {
        return this.soundType;
    }

    public String toString()
    {
        return "Block{" + field_149771_c.getKey(this) + "}";
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
    }

    public static void func_149671_p()
    {
        func_176215_a(0, field_176230_a, (new BlockAir()).func_149663_c("air"));
        func_176219_a(1, "stone", (new BlockStone()).func_149711_c(1.5F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("stone"));
        func_176219_a(2, "grass", (new BlockGrass()).func_149711_c(0.6F).func_149672_a(SoundType.PLANT).func_149663_c("grass"));
        func_176219_a(3, "dirt", (new BlockDirt()).func_149711_c(0.5F).func_149672_a(SoundType.GROUND).func_149663_c("dirt"));
        Block block = (new Block(Material.ROCK)).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("stonebrick").func_149647_a(CreativeTabs.BUILDING_BLOCKS);
        func_176219_a(4, "cobblestone", block);
        Block block1 = (new BlockPlanks()).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("wood");
        func_176219_a(5, "planks", block1);
        func_176219_a(6, "sapling", (new BlockSapling()).func_149711_c(0.0F).func_149672_a(SoundType.PLANT).func_149663_c("sapling"));
        func_176219_a(7, "bedrock", (new BlockEmptyDrops(Material.ROCK)).func_149722_s().func_149752_b(6000000.0F).func_149672_a(SoundType.STONE).func_149663_c("bedrock").func_149649_H().func_149647_a(CreativeTabs.BUILDING_BLOCKS));
        func_176219_a(8, "flowing_water", (new BlockDynamicLiquid(Material.WATER)).func_149711_c(100.0F).func_149713_g(3).func_149663_c("water").func_149649_H());
        func_176219_a(9, "water", (new BlockStaticLiquid(Material.WATER)).func_149711_c(100.0F).func_149713_g(3).func_149663_c("water").func_149649_H());
        func_176219_a(10, "flowing_lava", (new BlockDynamicLiquid(Material.LAVA)).func_149711_c(100.0F).func_149715_a(1.0F).func_149663_c("lava").func_149649_H());
        func_176219_a(11, "lava", (new BlockStaticLiquid(Material.LAVA)).func_149711_c(100.0F).func_149715_a(1.0F).func_149663_c("lava").func_149649_H());
        func_176219_a(12, "sand", (new BlockSand()).func_149711_c(0.5F).func_149672_a(SoundType.SAND).func_149663_c("sand"));
        func_176219_a(13, "gravel", (new BlockGravel()).func_149711_c(0.6F).func_149672_a(SoundType.GROUND).func_149663_c("gravel"));
        func_176219_a(14, "gold_ore", (new BlockOre()).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.STONE).func_149663_c("oreGold"));
        func_176219_a(15, "iron_ore", (new BlockOre()).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.STONE).func_149663_c("oreIron"));
        func_176219_a(16, "coal_ore", (new BlockOre()).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.STONE).func_149663_c("oreCoal"));
        func_176219_a(17, "log", (new BlockOldLog()).func_149663_c("log"));
        func_176219_a(18, "leaves", (new BlockOldLeaf()).func_149663_c("leaves"));
        func_176219_a(19, "sponge", (new BlockSponge()).func_149711_c(0.6F).func_149672_a(SoundType.PLANT).func_149663_c("sponge"));
        func_176219_a(20, "glass", (new BlockGlass(Material.GLASS, false)).func_149711_c(0.3F).func_149672_a(SoundType.GLASS).func_149663_c("glass"));
        func_176219_a(21, "lapis_ore", (new BlockOre()).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.STONE).func_149663_c("oreLapis"));
        func_176219_a(22, "lapis_block", (new Block(Material.IRON, MapColor.LAPIS)).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.STONE).func_149663_c("blockLapis").func_149647_a(CreativeTabs.BUILDING_BLOCKS));
        func_176219_a(23, "dispenser", (new BlockDispenser()).func_149711_c(3.5F).func_149672_a(SoundType.STONE).func_149663_c("dispenser"));
        Block block2 = (new BlockSandStone()).func_149672_a(SoundType.STONE).func_149711_c(0.8F).func_149663_c("sandStone");
        func_176219_a(24, "sandstone", block2);
        func_176219_a(25, "noteblock", (new BlockNote()).func_149672_a(SoundType.WOOD).func_149711_c(0.8F).func_149663_c("musicBlock"));
        func_176219_a(26, "bed", (new BlockBed()).func_149672_a(SoundType.WOOD).func_149711_c(0.2F).func_149663_c("bed").func_149649_H());
        func_176219_a(27, "golden_rail", (new BlockRailPowered()).func_149711_c(0.7F).func_149672_a(SoundType.METAL).func_149663_c("goldenRail"));
        func_176219_a(28, "detector_rail", (new BlockRailDetector()).func_149711_c(0.7F).func_149672_a(SoundType.METAL).func_149663_c("detectorRail"));
        func_176219_a(29, "sticky_piston", (new BlockPistonBase(true)).func_149663_c("pistonStickyBase"));
        func_176219_a(30, "web", (new BlockWeb()).func_149713_g(1).func_149711_c(4.0F).func_149663_c("web"));
        func_176219_a(31, "tallgrass", (new BlockTallGrass()).func_149711_c(0.0F).func_149672_a(SoundType.PLANT).func_149663_c("tallgrass"));
        func_176219_a(32, "deadbush", (new BlockDeadBush()).func_149711_c(0.0F).func_149672_a(SoundType.PLANT).func_149663_c("deadbush"));
        func_176219_a(33, "piston", (new BlockPistonBase(false)).func_149663_c("pistonBase"));
        func_176219_a(34, "piston_head", (new BlockPistonExtension()).func_149663_c("pistonBase"));
        func_176219_a(35, "wool", (new BlockColored(Material.WOOL)).func_149711_c(0.8F).func_149672_a(SoundType.CLOTH).func_149663_c("cloth"));
        func_176219_a(36, "piston_extension", new BlockPistonMoving());
        func_176219_a(37, "yellow_flower", (new BlockYellowFlower()).func_149711_c(0.0F).func_149672_a(SoundType.PLANT).func_149663_c("flower1"));
        func_176219_a(38, "red_flower", (new BlockRedFlower()).func_149711_c(0.0F).func_149672_a(SoundType.PLANT).func_149663_c("flower2"));
        Block block3 = (new BlockMushroom()).func_149711_c(0.0F).func_149672_a(SoundType.PLANT).func_149715_a(0.125F).func_149663_c("mushroom");
        func_176219_a(39, "brown_mushroom", block3);
        Block block4 = (new BlockMushroom()).func_149711_c(0.0F).func_149672_a(SoundType.PLANT).func_149663_c("mushroom");
        func_176219_a(40, "red_mushroom", block4);
        func_176219_a(41, "gold_block", (new Block(Material.IRON, MapColor.GOLD)).func_149711_c(3.0F).func_149752_b(10.0F).func_149672_a(SoundType.METAL).func_149663_c("blockGold").func_149647_a(CreativeTabs.BUILDING_BLOCKS));
        func_176219_a(42, "iron_block", (new Block(Material.IRON, MapColor.IRON)).func_149711_c(5.0F).func_149752_b(10.0F).func_149672_a(SoundType.METAL).func_149663_c("blockIron").func_149647_a(CreativeTabs.BUILDING_BLOCKS));
        func_176219_a(43, "double_stone_slab", (new BlockDoubleStoneSlab()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("stoneSlab"));
        func_176219_a(44, "stone_slab", (new BlockHalfStoneSlab()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("stoneSlab"));
        Block block5 = (new Block(Material.ROCK, MapColor.RED)).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("brick").func_149647_a(CreativeTabs.BUILDING_BLOCKS);
        func_176219_a(45, "brick_block", block5);
        func_176219_a(46, "tnt", (new BlockTNT()).func_149711_c(0.0F).func_149672_a(SoundType.PLANT).func_149663_c("tnt"));
        func_176219_a(47, "bookshelf", (new BlockBookshelf()).func_149711_c(1.5F).func_149672_a(SoundType.WOOD).func_149663_c("bookshelf"));
        func_176219_a(48, "mossy_cobblestone", (new Block(Material.ROCK)).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("stoneMoss").func_149647_a(CreativeTabs.BUILDING_BLOCKS));
        func_176219_a(49, "obsidian", (new BlockObsidian()).func_149711_c(50.0F).func_149752_b(2000.0F).func_149672_a(SoundType.STONE).func_149663_c("obsidian"));
        func_176219_a(50, "torch", (new BlockTorch()).func_149711_c(0.0F).func_149715_a(0.9375F).func_149672_a(SoundType.WOOD).func_149663_c("torch"));
        func_176219_a(51, "fire", (new BlockFire()).func_149711_c(0.0F).func_149715_a(1.0F).func_149672_a(SoundType.CLOTH).func_149663_c("fire").func_149649_H());
        func_176219_a(52, "mob_spawner", (new BlockMobSpawner()).func_149711_c(5.0F).func_149672_a(SoundType.METAL).func_149663_c("mobSpawner").func_149649_H());
        func_176219_a(53, "oak_stairs", (new BlockStairs(block1.getDefaultState().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.OAK))).func_149663_c("stairsWood"));
        func_176219_a(54, "chest", (new BlockChest(BlockChest.Type.BASIC)).func_149711_c(2.5F).func_149672_a(SoundType.WOOD).func_149663_c("chest"));
        func_176219_a(55, "redstone_wire", (new BlockRedstoneWire()).func_149711_c(0.0F).func_149672_a(SoundType.STONE).func_149663_c("redstoneDust").func_149649_H());
        func_176219_a(56, "diamond_ore", (new BlockOre()).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.STONE).func_149663_c("oreDiamond"));
        func_176219_a(57, "diamond_block", (new Block(Material.IRON, MapColor.DIAMOND)).func_149711_c(5.0F).func_149752_b(10.0F).func_149672_a(SoundType.METAL).func_149663_c("blockDiamond").func_149647_a(CreativeTabs.BUILDING_BLOCKS));
        func_176219_a(58, "crafting_table", (new BlockWorkbench()).func_149711_c(2.5F).func_149672_a(SoundType.WOOD).func_149663_c("workbench"));
        func_176219_a(59, "wheat", (new BlockCrops()).func_149663_c("crops"));
        Block block6 = (new BlockFarmland()).func_149711_c(0.6F).func_149672_a(SoundType.GROUND).func_149663_c("farmland");
        func_176219_a(60, "farmland", block6);
        func_176219_a(61, "furnace", (new BlockFurnace(false)).func_149711_c(3.5F).func_149672_a(SoundType.STONE).func_149663_c("furnace").func_149647_a(CreativeTabs.DECORATIONS));
        func_176219_a(62, "lit_furnace", (new BlockFurnace(true)).func_149711_c(3.5F).func_149672_a(SoundType.STONE).func_149715_a(0.875F).func_149663_c("furnace"));
        func_176219_a(63, "standing_sign", (new BlockStandingSign()).func_149711_c(1.0F).func_149672_a(SoundType.WOOD).func_149663_c("sign").func_149649_H());
        func_176219_a(64, "wooden_door", (new BlockDoor(Material.WOOD)).func_149711_c(3.0F).func_149672_a(SoundType.WOOD).func_149663_c("doorOak").func_149649_H());
        func_176219_a(65, "ladder", (new BlockLadder()).func_149711_c(0.4F).func_149672_a(SoundType.LADDER).func_149663_c("ladder"));
        func_176219_a(66, "rail", (new BlockRail()).func_149711_c(0.7F).func_149672_a(SoundType.METAL).func_149663_c("rail"));
        func_176219_a(67, "stone_stairs", (new BlockStairs(block.getDefaultState())).func_149663_c("stairsStone"));
        func_176219_a(68, "wall_sign", (new BlockWallSign()).func_149711_c(1.0F).func_149672_a(SoundType.WOOD).func_149663_c("sign").func_149649_H());
        func_176219_a(69, "lever", (new BlockLever()).func_149711_c(0.5F).func_149672_a(SoundType.WOOD).func_149663_c("lever"));
        func_176219_a(70, "stone_pressure_plate", (new BlockPressurePlate(Material.ROCK, BlockPressurePlate.Sensitivity.MOBS)).func_149711_c(0.5F).func_149672_a(SoundType.STONE).func_149663_c("pressurePlateStone"));
        func_176219_a(71, "iron_door", (new BlockDoor(Material.IRON)).func_149711_c(5.0F).func_149672_a(SoundType.METAL).func_149663_c("doorIron").func_149649_H());
        func_176219_a(72, "wooden_pressure_plate", (new BlockPressurePlate(Material.WOOD, BlockPressurePlate.Sensitivity.EVERYTHING)).func_149711_c(0.5F).func_149672_a(SoundType.WOOD).func_149663_c("pressurePlateWood"));
        func_176219_a(73, "redstone_ore", (new BlockRedstoneOre(false)).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.STONE).func_149663_c("oreRedstone").func_149647_a(CreativeTabs.BUILDING_BLOCKS));
        func_176219_a(74, "lit_redstone_ore", (new BlockRedstoneOre(true)).func_149715_a(0.625F).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.STONE).func_149663_c("oreRedstone"));
        func_176219_a(75, "unlit_redstone_torch", (new BlockRedstoneTorch(false)).func_149711_c(0.0F).func_149672_a(SoundType.WOOD).func_149663_c("notGate"));
        func_176219_a(76, "redstone_torch", (new BlockRedstoneTorch(true)).func_149711_c(0.0F).func_149715_a(0.5F).func_149672_a(SoundType.WOOD).func_149663_c("notGate").func_149647_a(CreativeTabs.REDSTONE));
        func_176219_a(77, "stone_button", (new BlockButtonStone()).func_149711_c(0.5F).func_149672_a(SoundType.STONE).func_149663_c("button"));
        func_176219_a(78, "snow_layer", (new BlockSnow()).func_149711_c(0.1F).func_149672_a(SoundType.SNOW).func_149663_c("snow").func_149713_g(0));
        func_176219_a(79, "ice", (new BlockIce()).func_149711_c(0.5F).func_149713_g(3).func_149672_a(SoundType.GLASS).func_149663_c("ice"));
        func_176219_a(80, "snow", (new BlockSnowBlock()).func_149711_c(0.2F).func_149672_a(SoundType.SNOW).func_149663_c("snow"));
        func_176219_a(81, "cactus", (new BlockCactus()).func_149711_c(0.4F).func_149672_a(SoundType.CLOTH).func_149663_c("cactus"));
        func_176219_a(82, "clay", (new BlockClay()).func_149711_c(0.6F).func_149672_a(SoundType.GROUND).func_149663_c("clay"));
        func_176219_a(83, "reeds", (new BlockReed()).func_149711_c(0.0F).func_149672_a(SoundType.PLANT).func_149663_c("reeds").func_149649_H());
        func_176219_a(84, "jukebox", (new BlockJukebox()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("jukebox"));
        func_176219_a(85, "fence", (new BlockFence(Material.WOOD, BlockPlanks.EnumType.OAK.func_181070_c())).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("fence"));
        Block block7 = (new BlockPumpkin()).func_149711_c(1.0F).func_149672_a(SoundType.WOOD).func_149663_c("pumpkin");
        func_176219_a(86, "pumpkin", block7);
        func_176219_a(87, "netherrack", (new BlockNetherrack()).func_149711_c(0.4F).func_149672_a(SoundType.STONE).func_149663_c("hellrock"));
        func_176219_a(88, "soul_sand", (new BlockSoulSand()).func_149711_c(0.5F).func_149672_a(SoundType.SAND).func_149663_c("hellsand"));
        func_176219_a(89, "glowstone", (new BlockGlowstone(Material.GLASS)).func_149711_c(0.3F).func_149672_a(SoundType.GLASS).func_149715_a(1.0F).func_149663_c("lightgem"));
        func_176219_a(90, "portal", (new BlockPortal()).func_149711_c(-1.0F).func_149672_a(SoundType.GLASS).func_149715_a(0.75F).func_149663_c("portal"));
        func_176219_a(91, "lit_pumpkin", (new BlockPumpkin()).func_149711_c(1.0F).func_149672_a(SoundType.WOOD).func_149715_a(1.0F).func_149663_c("litpumpkin"));
        func_176219_a(92, "cake", (new BlockCake()).func_149711_c(0.5F).func_149672_a(SoundType.CLOTH).func_149663_c("cake").func_149649_H());
        func_176219_a(93, "unpowered_repeater", (new BlockRedstoneRepeater(false)).func_149711_c(0.0F).func_149672_a(SoundType.WOOD).func_149663_c("diode").func_149649_H());
        func_176219_a(94, "powered_repeater", (new BlockRedstoneRepeater(true)).func_149711_c(0.0F).func_149672_a(SoundType.WOOD).func_149663_c("diode").func_149649_H());
        func_176219_a(95, "stained_glass", (new BlockStainedGlass(Material.GLASS)).func_149711_c(0.3F).func_149672_a(SoundType.GLASS).func_149663_c("stainedGlass"));
        func_176219_a(96, "trapdoor", (new BlockTrapDoor(Material.WOOD)).func_149711_c(3.0F).func_149672_a(SoundType.WOOD).func_149663_c("trapdoor").func_149649_H());
        func_176219_a(97, "monster_egg", (new BlockSilverfish()).func_149711_c(0.75F).func_149663_c("monsterStoneEgg"));
        Block block8 = (new BlockStoneBrick()).func_149711_c(1.5F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("stonebricksmooth");
        func_176219_a(98, "stonebrick", block8);
        func_176219_a(99, "brown_mushroom_block", (new BlockHugeMushroom(Material.WOOD, MapColor.DIRT, block3)).func_149711_c(0.2F).func_149672_a(SoundType.WOOD).func_149663_c("mushroom"));
        func_176219_a(100, "red_mushroom_block", (new BlockHugeMushroom(Material.WOOD, MapColor.RED, block4)).func_149711_c(0.2F).func_149672_a(SoundType.WOOD).func_149663_c("mushroom"));
        func_176219_a(101, "iron_bars", (new BlockPane(Material.IRON, true)).func_149711_c(5.0F).func_149752_b(10.0F).func_149672_a(SoundType.METAL).func_149663_c("fenceIron"));
        func_176219_a(102, "glass_pane", (new BlockPane(Material.GLASS, false)).func_149711_c(0.3F).func_149672_a(SoundType.GLASS).func_149663_c("thinGlass"));
        Block block9 = (new BlockMelon()).func_149711_c(1.0F).func_149672_a(SoundType.WOOD).func_149663_c("melon");
        func_176219_a(103, "melon_block", block9);
        func_176219_a(104, "pumpkin_stem", (new BlockStem(block7)).func_149711_c(0.0F).func_149672_a(SoundType.WOOD).func_149663_c("pumpkinStem"));
        func_176219_a(105, "melon_stem", (new BlockStem(block9)).func_149711_c(0.0F).func_149672_a(SoundType.WOOD).func_149663_c("pumpkinStem"));
        func_176219_a(106, "vine", (new BlockVine()).func_149711_c(0.2F).func_149672_a(SoundType.PLANT).func_149663_c("vine"));
        func_176219_a(107, "fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.OAK)).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("fenceGate"));
        func_176219_a(108, "brick_stairs", (new BlockStairs(block5.getDefaultState())).func_149663_c("stairsBrick"));
        func_176219_a(109, "stone_brick_stairs", (new BlockStairs(block8.getDefaultState().func_177226_a(BlockStoneBrick.field_176249_a, BlockStoneBrick.EnumType.DEFAULT))).func_149663_c("stairsStoneBrickSmooth"));
        func_176219_a(110, "mycelium", (new BlockMycelium()).func_149711_c(0.6F).func_149672_a(SoundType.PLANT).func_149663_c("mycel"));
        func_176219_a(111, "waterlily", (new BlockLilyPad()).func_149711_c(0.0F).func_149672_a(SoundType.PLANT).func_149663_c("waterlily"));
        Block block10 = (new BlockNetherBrick()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("netherBrick").func_149647_a(CreativeTabs.BUILDING_BLOCKS);
        func_176219_a(112, "nether_brick", block10);
        func_176219_a(113, "nether_brick_fence", (new BlockFence(Material.ROCK, MapColor.NETHERRACK)).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("netherFence"));
        func_176219_a(114, "nether_brick_stairs", (new BlockStairs(block10.getDefaultState())).func_149663_c("stairsNetherBrick"));
        func_176219_a(115, "nether_wart", (new BlockNetherWart()).func_149663_c("netherStalk"));
        func_176219_a(116, "enchanting_table", (new BlockEnchantmentTable()).func_149711_c(5.0F).func_149752_b(2000.0F).func_149663_c("enchantmentTable"));
        func_176219_a(117, "brewing_stand", (new BlockBrewingStand()).func_149711_c(0.5F).func_149715_a(0.125F).func_149663_c("brewingStand"));
        func_176219_a(118, "cauldron", (new BlockCauldron()).func_149711_c(2.0F).func_149663_c("cauldron"));
        func_176219_a(119, "end_portal", (new BlockEndPortal(Material.PORTAL)).func_149711_c(-1.0F).func_149752_b(6000000.0F));
        func_176219_a(120, "end_portal_frame", (new BlockEndPortalFrame()).func_149672_a(SoundType.GLASS).func_149715_a(0.125F).func_149711_c(-1.0F).func_149663_c("endPortalFrame").func_149752_b(6000000.0F).func_149647_a(CreativeTabs.DECORATIONS));
        func_176219_a(121, "end_stone", (new Block(Material.ROCK, MapColor.SAND)).func_149711_c(3.0F).func_149752_b(15.0F).func_149672_a(SoundType.STONE).func_149663_c("whiteStone").func_149647_a(CreativeTabs.BUILDING_BLOCKS));
        func_176219_a(122, "dragon_egg", (new BlockDragonEgg()).func_149711_c(3.0F).func_149752_b(15.0F).func_149672_a(SoundType.STONE).func_149715_a(0.125F).func_149663_c("dragonEgg"));
        func_176219_a(123, "redstone_lamp", (new BlockRedstoneLight(false)).func_149711_c(0.3F).func_149672_a(SoundType.GLASS).func_149663_c("redstoneLight").func_149647_a(CreativeTabs.REDSTONE));
        func_176219_a(124, "lit_redstone_lamp", (new BlockRedstoneLight(true)).func_149711_c(0.3F).func_149672_a(SoundType.GLASS).func_149663_c("redstoneLight"));
        func_176219_a(125, "double_wooden_slab", (new BlockDoubleWoodSlab()).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("woodSlab"));
        func_176219_a(126, "wooden_slab", (new BlockHalfWoodSlab()).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("woodSlab"));
        func_176219_a(127, "cocoa", (new BlockCocoa()).func_149711_c(0.2F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("cocoa"));
        func_176219_a(128, "sandstone_stairs", (new BlockStairs(block2.getDefaultState().func_177226_a(BlockSandStone.field_176297_a, BlockSandStone.EnumType.SMOOTH))).func_149663_c("stairsSandStone"));
        func_176219_a(129, "emerald_ore", (new BlockOre()).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.STONE).func_149663_c("oreEmerald"));
        func_176219_a(130, "ender_chest", (new BlockEnderChest()).func_149711_c(22.5F).func_149752_b(1000.0F).func_149672_a(SoundType.STONE).func_149663_c("enderChest").func_149715_a(0.5F));
        func_176219_a(131, "tripwire_hook", (new BlockTripWireHook()).func_149663_c("tripWireSource"));
        func_176219_a(132, "tripwire", (new BlockTripWire()).func_149663_c("tripWire"));
        func_176219_a(133, "emerald_block", (new Block(Material.IRON, MapColor.EMERALD)).func_149711_c(5.0F).func_149752_b(10.0F).func_149672_a(SoundType.METAL).func_149663_c("blockEmerald").func_149647_a(CreativeTabs.BUILDING_BLOCKS));
        func_176219_a(134, "spruce_stairs", (new BlockStairs(block1.getDefaultState().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.SPRUCE))).func_149663_c("stairsWoodSpruce"));
        func_176219_a(135, "birch_stairs", (new BlockStairs(block1.getDefaultState().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.BIRCH))).func_149663_c("stairsWoodBirch"));
        func_176219_a(136, "jungle_stairs", (new BlockStairs(block1.getDefaultState().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.JUNGLE))).func_149663_c("stairsWoodJungle"));
        func_176219_a(137, "command_block", (new BlockCommandBlock(MapColor.BROWN)).func_149722_s().func_149752_b(6000000.0F).func_149663_c("commandBlock"));
        func_176219_a(138, "beacon", (new BlockBeacon()).func_149663_c("beacon").func_149715_a(1.0F));
        func_176219_a(139, "cobblestone_wall", (new BlockWall(block)).func_149663_c("cobbleWall"));
        func_176219_a(140, "flower_pot", (new BlockFlowerPot()).func_149711_c(0.0F).func_149672_a(SoundType.STONE).func_149663_c("flowerPot"));
        func_176219_a(141, "carrots", (new BlockCarrot()).func_149663_c("carrots"));
        func_176219_a(142, "potatoes", (new BlockPotato()).func_149663_c("potatoes"));
        func_176219_a(143, "wooden_button", (new BlockButtonWood()).func_149711_c(0.5F).func_149672_a(SoundType.WOOD).func_149663_c("button"));
        func_176219_a(144, "skull", (new BlockSkull()).func_149711_c(1.0F).func_149672_a(SoundType.STONE).func_149663_c("skull"));
        func_176219_a(145, "anvil", (new BlockAnvil()).func_149711_c(5.0F).func_149672_a(SoundType.ANVIL).func_149752_b(2000.0F).func_149663_c("anvil"));
        func_176219_a(146, "trapped_chest", (new BlockChest(BlockChest.Type.TRAP)).func_149711_c(2.5F).func_149672_a(SoundType.WOOD).func_149663_c("chestTrap"));
        func_176219_a(147, "light_weighted_pressure_plate", (new BlockPressurePlateWeighted(Material.IRON, 15, MapColor.GOLD)).func_149711_c(0.5F).func_149672_a(SoundType.WOOD).func_149663_c("weightedPlate_light"));
        func_176219_a(148, "heavy_weighted_pressure_plate", (new BlockPressurePlateWeighted(Material.IRON, 150)).func_149711_c(0.5F).func_149672_a(SoundType.WOOD).func_149663_c("weightedPlate_heavy"));
        func_176219_a(149, "unpowered_comparator", (new BlockRedstoneComparator(false)).func_149711_c(0.0F).func_149672_a(SoundType.WOOD).func_149663_c("comparator").func_149649_H());
        func_176219_a(150, "powered_comparator", (new BlockRedstoneComparator(true)).func_149711_c(0.0F).func_149715_a(0.625F).func_149672_a(SoundType.WOOD).func_149663_c("comparator").func_149649_H());
        func_176219_a(151, "daylight_detector", new BlockDaylightDetector(false));
        func_176219_a(152, "redstone_block", (new BlockCompressedPowered(Material.IRON, MapColor.TNT)).func_149711_c(5.0F).func_149752_b(10.0F).func_149672_a(SoundType.METAL).func_149663_c("blockRedstone").func_149647_a(CreativeTabs.REDSTONE));
        func_176219_a(153, "quartz_ore", (new BlockOre(MapColor.NETHERRACK)).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.STONE).func_149663_c("netherquartz"));
        func_176219_a(154, "hopper", (new BlockHopper()).func_149711_c(3.0F).func_149752_b(8.0F).func_149672_a(SoundType.METAL).func_149663_c("hopper"));
        Block block11 = (new BlockQuartz()).func_149672_a(SoundType.STONE).func_149711_c(0.8F).func_149663_c("quartzBlock");
        func_176219_a(155, "quartz_block", block11);
        func_176219_a(156, "quartz_stairs", (new BlockStairs(block11.getDefaultState().func_177226_a(BlockQuartz.field_176335_a, BlockQuartz.EnumType.DEFAULT))).func_149663_c("stairsQuartz"));
        func_176219_a(157, "activator_rail", (new BlockRailPowered()).func_149711_c(0.7F).func_149672_a(SoundType.METAL).func_149663_c("activatorRail"));
        func_176219_a(158, "dropper", (new BlockDropper()).func_149711_c(3.5F).func_149672_a(SoundType.STONE).func_149663_c("dropper"));
        func_176219_a(159, "stained_hardened_clay", (new BlockStainedHardenedClay()).func_149711_c(1.25F).func_149752_b(7.0F).func_149672_a(SoundType.STONE).func_149663_c("clayHardenedStained"));
        func_176219_a(160, "stained_glass_pane", (new BlockStainedGlassPane()).func_149711_c(0.3F).func_149672_a(SoundType.GLASS).func_149663_c("thinStainedGlass"));
        func_176219_a(161, "leaves2", (new BlockNewLeaf()).func_149663_c("leaves"));
        func_176219_a(162, "log2", (new BlockNewLog()).func_149663_c("log"));
        func_176219_a(163, "acacia_stairs", (new BlockStairs(block1.getDefaultState().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.ACACIA))).func_149663_c("stairsWoodAcacia"));
        func_176219_a(164, "dark_oak_stairs", (new BlockStairs(block1.getDefaultState().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.DARK_OAK))).func_149663_c("stairsWoodDarkOak"));
        func_176219_a(165, "slime", (new BlockSlime()).func_149663_c("slime").func_149672_a(SoundType.SLIME));
        func_176219_a(166, "barrier", (new BlockBarrier()).func_149663_c("barrier"));
        func_176219_a(167, "iron_trapdoor", (new BlockTrapDoor(Material.IRON)).func_149711_c(5.0F).func_149672_a(SoundType.METAL).func_149663_c("ironTrapdoor").func_149649_H());
        func_176219_a(168, "prismarine", (new BlockPrismarine()).func_149711_c(1.5F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("prismarine"));
        func_176219_a(169, "sea_lantern", (new BlockSeaLantern(Material.GLASS)).func_149711_c(0.3F).func_149672_a(SoundType.GLASS).func_149715_a(1.0F).func_149663_c("seaLantern"));
        func_176219_a(170, "hay_block", (new BlockHay()).func_149711_c(0.5F).func_149672_a(SoundType.PLANT).func_149663_c("hayBlock").func_149647_a(CreativeTabs.BUILDING_BLOCKS));
        func_176219_a(171, "carpet", (new BlockCarpet()).func_149711_c(0.1F).func_149672_a(SoundType.CLOTH).func_149663_c("woolCarpet").func_149713_g(0));
        func_176219_a(172, "hardened_clay", (new BlockHardenedClay()).func_149711_c(1.25F).func_149752_b(7.0F).func_149672_a(SoundType.STONE).func_149663_c("clayHardened"));
        func_176219_a(173, "coal_block", (new Block(Material.ROCK, MapColor.BLACK)).func_149711_c(5.0F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("blockCoal").func_149647_a(CreativeTabs.BUILDING_BLOCKS));
        func_176219_a(174, "packed_ice", (new BlockPackedIce()).func_149711_c(0.5F).func_149672_a(SoundType.GLASS).func_149663_c("icePacked"));
        func_176219_a(175, "double_plant", new BlockDoublePlant());
        func_176219_a(176, "standing_banner", (new BlockBanner.BlockBannerStanding()).func_149711_c(1.0F).func_149672_a(SoundType.WOOD).func_149663_c("banner").func_149649_H());
        func_176219_a(177, "wall_banner", (new BlockBanner.BlockBannerHanging()).func_149711_c(1.0F).func_149672_a(SoundType.WOOD).func_149663_c("banner").func_149649_H());
        func_176219_a(178, "daylight_detector_inverted", new BlockDaylightDetector(true));
        Block block12 = (new BlockRedSandstone()).func_149672_a(SoundType.STONE).func_149711_c(0.8F).func_149663_c("redSandStone");
        func_176219_a(179, "red_sandstone", block12);
        func_176219_a(180, "red_sandstone_stairs", (new BlockStairs(block12.getDefaultState().func_177226_a(BlockRedSandstone.field_176336_a, BlockRedSandstone.EnumType.SMOOTH))).func_149663_c("stairsRedSandStone"));
        func_176219_a(181, "double_stone_slab2", (new BlockDoubleStoneSlabNew()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("stoneSlab2"));
        func_176219_a(182, "stone_slab2", (new BlockHalfStoneSlabNew()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("stoneSlab2"));
        func_176219_a(183, "spruce_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.SPRUCE)).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("spruceFenceGate"));
        func_176219_a(184, "birch_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.BIRCH)).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("birchFenceGate"));
        func_176219_a(185, "jungle_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.JUNGLE)).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("jungleFenceGate"));
        func_176219_a(186, "dark_oak_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.DARK_OAK)).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("darkOakFenceGate"));
        func_176219_a(187, "acacia_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.ACACIA)).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("acaciaFenceGate"));
        func_176219_a(188, "spruce_fence", (new BlockFence(Material.WOOD, BlockPlanks.EnumType.SPRUCE.func_181070_c())).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("spruceFence"));
        func_176219_a(189, "birch_fence", (new BlockFence(Material.WOOD, BlockPlanks.EnumType.BIRCH.func_181070_c())).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("birchFence"));
        func_176219_a(190, "jungle_fence", (new BlockFence(Material.WOOD, BlockPlanks.EnumType.JUNGLE.func_181070_c())).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("jungleFence"));
        func_176219_a(191, "dark_oak_fence", (new BlockFence(Material.WOOD, BlockPlanks.EnumType.DARK_OAK.func_181070_c())).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("darkOakFence"));
        func_176219_a(192, "acacia_fence", (new BlockFence(Material.WOOD, BlockPlanks.EnumType.ACACIA.func_181070_c())).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.WOOD).func_149663_c("acaciaFence"));
        func_176219_a(193, "spruce_door", (new BlockDoor(Material.WOOD)).func_149711_c(3.0F).func_149672_a(SoundType.WOOD).func_149663_c("doorSpruce").func_149649_H());
        func_176219_a(194, "birch_door", (new BlockDoor(Material.WOOD)).func_149711_c(3.0F).func_149672_a(SoundType.WOOD).func_149663_c("doorBirch").func_149649_H());
        func_176219_a(195, "jungle_door", (new BlockDoor(Material.WOOD)).func_149711_c(3.0F).func_149672_a(SoundType.WOOD).func_149663_c("doorJungle").func_149649_H());
        func_176219_a(196, "acacia_door", (new BlockDoor(Material.WOOD)).func_149711_c(3.0F).func_149672_a(SoundType.WOOD).func_149663_c("doorAcacia").func_149649_H());
        func_176219_a(197, "dark_oak_door", (new BlockDoor(Material.WOOD)).func_149711_c(3.0F).func_149672_a(SoundType.WOOD).func_149663_c("doorDarkOak").func_149649_H());
        func_176219_a(198, "end_rod", (new BlockEndRod()).func_149711_c(0.0F).func_149715_a(0.9375F).func_149672_a(SoundType.WOOD).func_149663_c("endRod"));
        func_176219_a(199, "chorus_plant", (new BlockChorusPlant()).func_149711_c(0.4F).func_149672_a(SoundType.WOOD).func_149663_c("chorusPlant"));
        func_176219_a(200, "chorus_flower", (new BlockChorusFlower()).func_149711_c(0.4F).func_149672_a(SoundType.WOOD).func_149663_c("chorusFlower"));
        Block block13 = (new Block(Material.ROCK, MapColor.MAGENTA)).func_149711_c(1.5F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149647_a(CreativeTabs.BUILDING_BLOCKS).func_149663_c("purpurBlock");
        func_176219_a(201, "purpur_block", block13);
        func_176219_a(202, "purpur_pillar", (new BlockRotatedPillar(Material.ROCK, MapColor.MAGENTA)).func_149711_c(1.5F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149647_a(CreativeTabs.BUILDING_BLOCKS).func_149663_c("purpurPillar"));
        func_176219_a(203, "purpur_stairs", (new BlockStairs(block13.getDefaultState())).func_149663_c("stairsPurpur"));
        func_176219_a(204, "purpur_double_slab", (new BlockPurpurSlab.Double()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("purpurSlab"));
        func_176219_a(205, "purpur_slab", (new BlockPurpurSlab.Half()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("purpurSlab"));
        func_176219_a(206, "end_bricks", (new Block(Material.ROCK, MapColor.SAND)).func_149672_a(SoundType.STONE).func_149711_c(0.8F).func_149647_a(CreativeTabs.BUILDING_BLOCKS).func_149663_c("endBricks"));
        func_176219_a(207, "beetroots", (new BlockBeetroot()).func_149663_c("beetroots"));
        Block block14 = (new BlockGrassPath()).func_149711_c(0.65F).func_149672_a(SoundType.PLANT).func_149663_c("grassPath").func_149649_H();
        func_176219_a(208, "grass_path", block14);
        func_176219_a(209, "end_gateway", (new BlockEndGateway(Material.PORTAL)).func_149711_c(-1.0F).func_149752_b(6000000.0F));
        func_176219_a(210, "repeating_command_block", (new BlockCommandBlock(MapColor.PURPLE)).func_149722_s().func_149752_b(6000000.0F).func_149663_c("repeatingCommandBlock"));
        func_176219_a(211, "chain_command_block", (new BlockCommandBlock(MapColor.GREEN)).func_149722_s().func_149752_b(6000000.0F).func_149663_c("chainCommandBlock"));
        func_176219_a(212, "frosted_ice", (new BlockFrostedIce()).func_149711_c(0.5F).func_149713_g(3).func_149672_a(SoundType.GLASS).func_149663_c("frostedIce"));
        func_176219_a(213, "magma", (new BlockMagma()).func_149711_c(0.5F).func_149672_a(SoundType.STONE).func_149663_c("magma"));
        func_176219_a(214, "nether_wart_block", (new Block(Material.ORGANIC, MapColor.RED)).func_149647_a(CreativeTabs.BUILDING_BLOCKS).func_149711_c(1.0F).func_149672_a(SoundType.WOOD).func_149663_c("netherWartBlock"));
        func_176219_a(215, "red_nether_brick", (new BlockNetherBrick()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.STONE).func_149663_c("redNetherBrick").func_149647_a(CreativeTabs.BUILDING_BLOCKS));
        func_176219_a(216, "bone_block", (new BlockBone()).func_149663_c("boneBlock"));
        func_176219_a(217, "structure_void", (new BlockStructureVoid()).func_149663_c("structureVoid"));
        func_176219_a(218, "observer", (new BlockObserver()).func_149711_c(3.0F).func_149663_c("observer"));
        func_176219_a(219, "white_shulker_box", (new BlockShulkerBox(EnumDyeColor.WHITE)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxWhite"));
        func_176219_a(220, "orange_shulker_box", (new BlockShulkerBox(EnumDyeColor.ORANGE)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxOrange"));
        func_176219_a(221, "magenta_shulker_box", (new BlockShulkerBox(EnumDyeColor.MAGENTA)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxMagenta"));
        func_176219_a(222, "light_blue_shulker_box", (new BlockShulkerBox(EnumDyeColor.LIGHT_BLUE)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxLightBlue"));
        func_176219_a(223, "yellow_shulker_box", (new BlockShulkerBox(EnumDyeColor.YELLOW)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxYellow"));
        func_176219_a(224, "lime_shulker_box", (new BlockShulkerBox(EnumDyeColor.LIME)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxLime"));
        func_176219_a(225, "pink_shulker_box", (new BlockShulkerBox(EnumDyeColor.PINK)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxPink"));
        func_176219_a(226, "gray_shulker_box", (new BlockShulkerBox(EnumDyeColor.GRAY)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxGray"));
        func_176219_a(227, "silver_shulker_box", (new BlockShulkerBox(EnumDyeColor.SILVER)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxSilver"));
        func_176219_a(228, "cyan_shulker_box", (new BlockShulkerBox(EnumDyeColor.CYAN)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxCyan"));
        func_176219_a(229, "purple_shulker_box", (new BlockShulkerBox(EnumDyeColor.PURPLE)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxPurple"));
        func_176219_a(230, "blue_shulker_box", (new BlockShulkerBox(EnumDyeColor.BLUE)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxBlue"));
        func_176219_a(231, "brown_shulker_box", (new BlockShulkerBox(EnumDyeColor.BROWN)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxBrown"));
        func_176219_a(232, "green_shulker_box", (new BlockShulkerBox(EnumDyeColor.GREEN)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxGreen"));
        func_176219_a(233, "red_shulker_box", (new BlockShulkerBox(EnumDyeColor.RED)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxRed"));
        func_176219_a(234, "black_shulker_box", (new BlockShulkerBox(EnumDyeColor.BLACK)).func_149711_c(2.0F).func_149672_a(SoundType.STONE).func_149663_c("shulkerBoxBlack"));
        func_176219_a(235, "white_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.WHITE));
        func_176219_a(236, "orange_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.ORANGE));
        func_176219_a(237, "magenta_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.MAGENTA));
        func_176219_a(238, "light_blue_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.LIGHT_BLUE));
        func_176219_a(239, "yellow_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.YELLOW));
        func_176219_a(240, "lime_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.LIME));
        func_176219_a(241, "pink_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.PINK));
        func_176219_a(242, "gray_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.GRAY));
        func_176219_a(243, "silver_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.SILVER));
        func_176219_a(244, "cyan_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.CYAN));
        func_176219_a(245, "purple_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.PURPLE));
        func_176219_a(246, "blue_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BLUE));
        func_176219_a(247, "brown_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BROWN));
        func_176219_a(248, "green_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.GREEN));
        func_176219_a(249, "red_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.RED));
        func_176219_a(250, "black_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BLACK));
        func_176219_a(251, "concrete", (new BlockColored(Material.ROCK)).func_149711_c(1.8F).func_149672_a(SoundType.STONE).func_149663_c("concrete"));
        func_176219_a(252, "concrete_powder", (new BlockConcretePowder()).func_149711_c(0.5F).func_149672_a(SoundType.SAND).func_149663_c("concretePowder"));
        func_176219_a(255, "structure_block", (new BlockStructure()).func_149722_s().func_149752_b(6000000.0F).func_149663_c("structureBlock"));
        field_149771_c.func_177776_a();

        for (Block block15 : field_149771_c)
        {
            if (block15.material == Material.AIR)
            {
                block15.field_149783_u = false;
            }
            else
            {
                boolean flag = false;
                boolean flag1 = block15 instanceof BlockStairs;
                boolean flag2 = block15 instanceof BlockSlab;
                boolean flag3 = block15 == block6 || block15 == block14;
                boolean flag4 = block15.field_149785_s;
                boolean flag5 = block15.field_149786_r == 0;

                if (flag1 || flag2 || flag3 || flag4 || flag5)
                {
                    flag = true;
                }

                block15.field_149783_u = flag;
            }
        }

        Set<Block> set = Sets.newHashSet(field_149771_c.getOrDefault(new ResourceLocation("tripwire")));

        for (Block block16 : field_149771_c)
        {
            if (set.contains(block16))
            {
                for (int i = 0; i < 15; ++i)
                {
                    int j = field_149771_c.getId(block16) << 4 | i;
                    BLOCK_STATE_IDS.put(block16.func_176203_a(i), j);
                }
            }
            else
            {
                UnmodifiableIterator unmodifiableiterator = block16.getStateContainer().getValidStates().iterator();

                while (unmodifiableiterator.hasNext())
                {
                    IBlockState iblockstate = (IBlockState)unmodifiableiterator.next();
                    int k = field_149771_c.getId(block16) << 4 | block16.func_176201_c(iblockstate);
                    BLOCK_STATE_IDS.put(iblockstate, k);
                }
            }
        }
    }

    private static void func_176215_a(int p_176215_0_, ResourceLocation p_176215_1_, Block p_176215_2_)
    {
        field_149771_c.func_177775_a(p_176215_0_, p_176215_1_, p_176215_2_);
    }

    private static void func_176219_a(int p_176219_0_, String p_176219_1_, Block p_176219_2_)
    {
        func_176215_a(p_176219_0_, new ResourceLocation(p_176219_1_), p_176219_2_);
    }

    public static enum EnumOffsetType
    {
        NONE,
        XZ,
        XYZ;
    }
}

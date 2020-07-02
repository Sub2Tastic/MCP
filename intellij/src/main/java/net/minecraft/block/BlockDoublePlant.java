package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoublePlant extends BlockBush implements IGrowable
{
    public static final PropertyEnum<BlockDoublePlant.EnumPlantType> field_176493_a = PropertyEnum.<BlockDoublePlant.EnumPlantType>create("variant", BlockDoublePlant.EnumPlantType.class);
    public static final PropertyEnum<BlockDoublePlant.EnumBlockHalf> HALF = PropertyEnum.<BlockDoublePlant.EnumBlockHalf>create("half", BlockDoublePlant.EnumBlockHalf.class);
    public static final PropertyEnum<EnumFacing> field_181084_N = BlockHorizontal.HORIZONTAL_FACING;

    public BlockDoublePlant()
    {
        super(Material.TALL_PLANTS);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176493_a, BlockDoublePlant.EnumPlantType.SUNFLOWER).func_177226_a(HALF, BlockDoublePlant.EnumBlockHalf.LOWER).func_177226_a(field_181084_N, EnumFacing.NORTH));
        this.func_149711_c(0.0F);
        this.func_149672_a(SoundType.PLANT);
        this.func_149663_c("doublePlant");
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185505_j;
    }

    private BlockDoublePlant.EnumPlantType func_185517_a(IBlockAccess p_185517_1_, BlockPos p_185517_2_, IBlockState p_185517_3_)
    {
        if (p_185517_3_.getBlock() == this)
        {
            p_185517_3_ = p_185517_3_.func_185899_b(p_185517_1_, p_185517_2_);
            return (BlockDoublePlant.EnumPlantType)p_185517_3_.get(field_176493_a);
        }
        else
        {
            return BlockDoublePlant.EnumPlantType.FERN;
        }
    }

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        return super.func_176196_c(p_176196_1_, p_176196_2_) && p_176196_1_.isAirBlock(p_176196_2_.up());
    }

    public boolean func_176200_f(IBlockAccess p_176200_1_, BlockPos p_176200_2_)
    {
        IBlockState iblockstate = p_176200_1_.getBlockState(p_176200_2_);

        if (iblockstate.getBlock() != this)
        {
            return true;
        }
        else
        {
            BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = (BlockDoublePlant.EnumPlantType)iblockstate.func_185899_b(p_176200_1_, p_176200_2_).get(field_176493_a);
            return blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.FERN || blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.GRASS;
        }
    }

    protected void func_176475_e(World p_176475_1_, BlockPos p_176475_2_, IBlockState p_176475_3_)
    {
        if (!this.func_180671_f(p_176475_1_, p_176475_2_, p_176475_3_))
        {
            boolean flag = p_176475_3_.get(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER;
            BlockPos blockpos = flag ? p_176475_2_ : p_176475_2_.up();
            BlockPos blockpos1 = flag ? p_176475_2_.down() : p_176475_2_;
            Block block = (Block)(flag ? this : p_176475_1_.getBlockState(blockpos).getBlock());
            Block block1 = (Block)(flag ? p_176475_1_.getBlockState(blockpos1).getBlock() : this);

            if (block == this)
            {
                p_176475_1_.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
            }

            if (block1 == this)
            {
                p_176475_1_.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 3);

                if (!flag)
                {
                    this.func_176226_b(p_176475_1_, blockpos1, p_176475_3_, 0);
                }
            }
        }
    }

    public boolean func_180671_f(World p_180671_1_, BlockPos p_180671_2_, IBlockState p_180671_3_)
    {
        if (p_180671_3_.get(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER)
        {
            return p_180671_1_.getBlockState(p_180671_2_.down()).getBlock() == this;
        }
        else
        {
            IBlockState iblockstate = p_180671_1_.getBlockState(p_180671_2_.up());
            return iblockstate.getBlock() == this && super.func_180671_f(p_180671_1_, p_180671_2_, iblockstate);
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        if (p_180660_1_.get(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER)
        {
            return Items.AIR;
        }
        else
        {
            BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = (BlockDoublePlant.EnumPlantType)p_180660_1_.get(field_176493_a);

            if (blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.FERN)
            {
                return Items.AIR;
            }
            else if (blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.GRASS)
            {
                return p_180660_2_.nextInt(8) == 0 ? Items.WHEAT_SEEDS : Items.AIR;
            }
            else
            {
                return super.func_180660_a(p_180660_1_, p_180660_2_, p_180660_3_);
            }
        }
    }

    public int func_180651_a(IBlockState p_180651_1_)
    {
        return p_180651_1_.get(HALF) != BlockDoublePlant.EnumBlockHalf.UPPER && p_180651_1_.get(field_176493_a) != BlockDoublePlant.EnumPlantType.GRASS ? ((BlockDoublePlant.EnumPlantType)p_180651_1_.get(field_176493_a)).func_176936_a() : 0;
    }

    public void func_176491_a(World p_176491_1_, BlockPos p_176491_2_, BlockDoublePlant.EnumPlantType p_176491_3_, int p_176491_4_)
    {
        p_176491_1_.setBlockState(p_176491_2_, this.getDefaultState().func_177226_a(HALF, BlockDoublePlant.EnumBlockHalf.LOWER).func_177226_a(field_176493_a, p_176491_3_), p_176491_4_);
        p_176491_1_.setBlockState(p_176491_2_.up(), this.getDefaultState().func_177226_a(HALF, BlockDoublePlant.EnumBlockHalf.UPPER), p_176491_4_);
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos.up(), this.getDefaultState().func_177226_a(HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (worldIn.isRemote || stack.getItem() != Items.SHEARS || state.get(HALF) != BlockDoublePlant.EnumBlockHalf.LOWER || !this.func_176489_b(worldIn, pos, state, player))
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually
     * collect this block
     */
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (state.get(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER)
        {
            if (worldIn.getBlockState(pos.down()).getBlock() == this)
            {
                if (player.abilities.isCreativeMode)
                {
                    worldIn.func_175698_g(pos.down());
                }
                else
                {
                    IBlockState iblockstate = worldIn.getBlockState(pos.down());
                    BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = (BlockDoublePlant.EnumPlantType)iblockstate.get(field_176493_a);

                    if (blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN && blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS)
                    {
                        worldIn.destroyBlock(pos.down(), true);
                    }
                    else if (worldIn.isRemote)
                    {
                        worldIn.func_175698_g(pos.down());
                    }
                    else if (!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == Items.SHEARS)
                    {
                        this.func_176489_b(worldIn, pos, iblockstate, player);
                        worldIn.func_175698_g(pos.down());
                    }
                    else
                    {
                        worldIn.destroyBlock(pos.down(), true);
                    }
                }
            }
        }
        else if (worldIn.getBlockState(pos.up()).getBlock() == this)
        {
            worldIn.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 2);
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    private boolean func_176489_b(World p_176489_1_, BlockPos p_176489_2_, IBlockState p_176489_3_, EntityPlayer p_176489_4_)
    {
        BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = (BlockDoublePlant.EnumPlantType)p_176489_3_.get(field_176493_a);

        if (blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN && blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS)
        {
            return false;
        }
        else
        {
            p_176489_4_.addStat(StatList.func_188055_a(this));
            int i = (blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.GRASS ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).func_177044_a();
            spawnAsEntity(p_176489_1_, p_176489_2_, new ItemStack(Blocks.field_150329_H, 2, i));
            return true;
        }
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        for (BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype : BlockDoublePlant.EnumPlantType.values())
        {
            items.add(new ItemStack(this, 1, blockdoubleplant$enumplanttype.func_176936_a()));
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this, 1, this.func_185517_a(worldIn, pos, state).func_176936_a());
    }

    /**
     * Whether this IGrowable can grow
     */
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = this.func_185517_a(worldIn, pos, state);
        return blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS && blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    public void func_176474_b(World p_176474_1_, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_)
    {
        spawnAsEntity(p_176474_1_, p_176474_3_, new ItemStack(this, 1, this.func_185517_a(p_176474_1_, p_176474_3_, p_176474_4_).func_176936_a()));
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return (p_176203_1_ & 8) > 0 ? this.getDefaultState().func_177226_a(HALF, BlockDoublePlant.EnumBlockHalf.UPPER) : this.getDefaultState().func_177226_a(HALF, BlockDoublePlant.EnumBlockHalf.LOWER).func_177226_a(field_176493_a, BlockDoublePlant.EnumPlantType.func_176938_a(p_176203_1_ & 7));
    }

    public IBlockState func_176221_a(IBlockState p_176221_1_, IBlockAccess p_176221_2_, BlockPos p_176221_3_)
    {
        if (p_176221_1_.get(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER)
        {
            IBlockState iblockstate = p_176221_2_.getBlockState(p_176221_3_.down());

            if (iblockstate.getBlock() == this)
            {
                p_176221_1_ = p_176221_1_.func_177226_a(field_176493_a, iblockstate.get(field_176493_a));
            }
        }

        return p_176221_1_;
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return p_176201_1_.get(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER ? 8 | ((EnumFacing)p_176201_1_.get(field_181084_N)).getHorizontalIndex() : ((BlockDoublePlant.EnumPlantType)p_176201_1_.get(field_176493_a)).func_176936_a();
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {HALF, field_176493_a, field_181084_N});
    }

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     */
    public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XZ;
    }

    public static enum EnumBlockHalf implements IStringSerializable
    {
        UPPER,
        LOWER;

        public String toString()
        {
            return this.getName();
        }

        public String getName()
        {
            return this == UPPER ? "upper" : "lower";
        }
    }

    public static enum EnumPlantType implements IStringSerializable
    {
        SUNFLOWER(0, "sunflower"),
        SYRINGA(1, "syringa"),
        GRASS(2, "double_grass", "grass"),
        FERN(3, "double_fern", "fern"),
        ROSE(4, "double_rose", "rose"),
        PAEONIA(5, "paeonia");

        private static final BlockDoublePlant.EnumPlantType[] field_176941_g = new BlockDoublePlant.EnumPlantType[values().length];
        private final int field_176949_h;
        private final String field_176950_i;
        private final String field_176947_j;

        private EnumPlantType(int p_i45722_3_, String p_i45722_4_)
        {
            this(p_i45722_3_, p_i45722_4_, p_i45722_4_);
        }

        private EnumPlantType(int p_i45723_3_, String p_i45723_4_, String p_i45723_5_)
        {
            this.field_176949_h = p_i45723_3_;
            this.field_176950_i = p_i45723_4_;
            this.field_176947_j = p_i45723_5_;
        }

        public int func_176936_a()
        {
            return this.field_176949_h;
        }

        public String toString()
        {
            return this.field_176950_i;
        }

        public static BlockDoublePlant.EnumPlantType func_176938_a(int p_176938_0_)
        {
            if (p_176938_0_ < 0 || p_176938_0_ >= field_176941_g.length)
            {
                p_176938_0_ = 0;
            }

            return field_176941_g[p_176938_0_];
        }

        public String getName()
        {
            return this.field_176950_i;
        }

        public String func_176939_c()
        {
            return this.field_176947_j;
        }

        static {
            for (BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype : values())
            {
                field_176941_g[blockdoubleplant$enumplanttype.func_176936_a()] = blockdoubleplant$enumplanttype;
            }
        }
    }
}

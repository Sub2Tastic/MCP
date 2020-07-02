package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTallGrass extends BlockBush implements IGrowable
{
    public static final PropertyEnum<BlockTallGrass.EnumType> field_176497_a = PropertyEnum.<BlockTallGrass.EnumType>create("type", BlockTallGrass.EnumType.class);
    protected static final AxisAlignedBB field_185522_c = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    protected BlockTallGrass()
    {
        super(Material.TALL_PLANTS);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176497_a, BlockTallGrass.EnumType.DEAD_BUSH));
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185522_c;
    }

    public boolean func_180671_f(World p_180671_1_, BlockPos p_180671_2_, IBlockState p_180671_3_)
    {
        return this.func_185514_i(p_180671_1_.getBlockState(p_180671_2_.down()));
    }

    public boolean func_176200_f(IBlockAccess p_176200_1_, BlockPos p_176200_2_)
    {
        return true;
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return p_180660_2_.nextInt(8) == 0 ? Items.WHEAT_SEEDS : Items.AIR;
    }

    public int func_149679_a(int p_149679_1_, Random p_149679_2_)
    {
        return 1 + p_149679_2_.nextInt(p_149679_1_ * 2 + 1);
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
            player.addStat(StatList.func_188055_a(this));
            spawnAsEntity(worldIn, pos, new ItemStack(Blocks.field_150329_H, 1, ((BlockTallGrass.EnumType)state.get(field_176497_a)).func_177044_a()));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this, 1, state.getBlock().func_176201_c(state));
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        for (int i = 1; i < 3; ++i)
        {
            items.add(new ItemStack(this, 1, i));
        }
    }

    /**
     * Whether this IGrowable can grow
     */
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return state.get(field_176497_a) != BlockTallGrass.EnumType.DEAD_BUSH;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    public void func_176474_b(World p_176474_1_, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_)
    {
        BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.GRASS;

        if (p_176474_4_.get(field_176497_a) == BlockTallGrass.EnumType.FERN)
        {
            blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.FERN;
        }

        if (Blocks.field_150398_cm.func_176196_c(p_176474_1_, p_176474_3_))
        {
            Blocks.field_150398_cm.func_176491_a(p_176474_1_, p_176474_3_, blockdoubleplant$enumplanttype, 2);
        }
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_176497_a, BlockTallGrass.EnumType.func_177045_a(p_176203_1_));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((BlockTallGrass.EnumType)p_176201_1_.get(field_176497_a)).func_177044_a();
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176497_a});
    }

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     */
    public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XYZ;
    }

    public static enum EnumType implements IStringSerializable
    {
        DEAD_BUSH(0, "dead_bush"),
        GRASS(1, "tall_grass"),
        FERN(2, "fern");

        private static final BlockTallGrass.EnumType[] field_177048_d = new BlockTallGrass.EnumType[values().length];
        private final int field_177049_e;
        private final String field_177046_f;

        private EnumType(int p_i45676_3_, String p_i45676_4_)
        {
            this.field_177049_e = p_i45676_3_;
            this.field_177046_f = p_i45676_4_;
        }

        public int func_177044_a()
        {
            return this.field_177049_e;
        }

        public String toString()
        {
            return this.field_177046_f;
        }

        public static BlockTallGrass.EnumType func_177045_a(int p_177045_0_)
        {
            if (p_177045_0_ < 0 || p_177045_0_ >= field_177048_d.length)
            {
                p_177045_0_ = 0;
            }

            return field_177048_d[p_177045_0_];
        }

        public String getName()
        {
            return this.field_177046_f;
        }

        static {
            for (BlockTallGrass.EnumType blocktallgrass$enumtype : values())
            {
                field_177048_d[blocktallgrass$enumtype.func_177044_a()] = blocktallgrass$enumtype;
            }
        }
    }
}

package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDaylightDetector extends BlockContainer
{
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    protected static final AxisAlignedBB field_185566_b = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D);
    private final boolean field_176435_b;

    public BlockDaylightDetector(boolean p_i45729_1_)
    {
        super(Material.WOOD);
        this.field_176435_b = p_i45729_1_;
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(POWER, Integer.valueOf(0)));
        this.func_149647_a(CreativeTabs.REDSTONE);
        this.func_149711_c(0.2F);
        this.func_149672_a(SoundType.WOOD);
        this.func_149663_c("daylightDetector");
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185566_b;
    }

    /**
     * @deprecated call via {@link IBlockState#getWeakPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return ((Integer)blockState.get(POWER)).intValue();
    }

    public void func_180677_d(World p_180677_1_, BlockPos p_180677_2_)
    {
        if (p_180677_1_.dimension.hasSkyLight())
        {
            IBlockState iblockstate = p_180677_1_.getBlockState(p_180677_2_);
            int i = p_180677_1_.func_175642_b(EnumSkyBlock.SKY, p_180677_2_) - p_180677_1_.getSkylightSubtracted();
            float f = p_180677_1_.getCelestialAngleRadians(1.0F);

            if (this.field_176435_b)
            {
                i = 15 - i;
            }

            if (i > 0 && !this.field_176435_b)
            {
                float f1 = f < (float)Math.PI ? 0.0F : ((float)Math.PI * 2F);
                f = f + (f1 - f) * 0.2F;
                i = Math.round((float)i * MathHelper.cos(f));
            }

            i = MathHelper.clamp(i, 0, 15);

            if (((Integer)iblockstate.get(POWER)).intValue() != i)
            {
                p_180677_1_.setBlockState(p_180677_2_, iblockstate.func_177226_a(POWER, Integer.valueOf(i)), 3);
            }
        }
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (p_180639_4_.isAllowEdit())
        {
            if (p_180639_1_.isRemote)
            {
                return true;
            }
            else
            {
                if (this.field_176435_b)
                {
                    p_180639_1_.setBlockState(p_180639_2_, Blocks.DAYLIGHT_DETECTOR.getDefaultState().func_177226_a(POWER, p_180639_3_.get(POWER)), 4);
                    Blocks.DAYLIGHT_DETECTOR.func_180677_d(p_180639_1_, p_180639_2_);
                }
                else
                {
                    p_180639_1_.setBlockState(p_180639_2_, Blocks.field_180402_cm.getDefaultState().func_177226_a(POWER, p_180639_3_.get(POWER)), 4);
                    Blocks.field_180402_cm.func_180677_d(p_180639_1_, p_180639_2_);
                }

                return true;
            }
        }
        else
        {
            return super.func_180639_a(p_180639_1_, p_180639_2_, p_180639_3_, p_180639_4_, p_180639_5_, p_180639_6_, p_180639_7_, p_180639_8_, p_180639_9_);
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Item.getItemFromBlock(Blocks.DAYLIGHT_DETECTOR);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Blocks.DAYLIGHT_DETECTOR);
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
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     * @deprecated call via {@link IBlockState#canProvidePower()} whenever possible. Implementing/overriding is fine.
     */
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityDaylightDetector();
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(POWER, Integer.valueOf(p_176203_1_));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((Integer)p_176201_1_.get(POWER)).intValue();
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {POWER});
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        if (!this.field_176435_b)
        {
            super.fillItemGroup(group, items);
        }
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return p_193383_4_ == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}

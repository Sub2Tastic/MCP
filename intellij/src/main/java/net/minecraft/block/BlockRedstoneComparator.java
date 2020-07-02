package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneComparator extends BlockRedstoneDiode implements ITileEntityProvider
{
    public static final PropertyBool field_176464_a = PropertyBool.create("powered");
    public static final PropertyEnum<BlockRedstoneComparator.Mode> MODE = PropertyEnum.<BlockRedstoneComparator.Mode>create("mode", BlockRedstoneComparator.Mode.class);

    public BlockRedstoneComparator(boolean p_i45399_1_)
    {
        super(p_i45399_1_);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(HORIZONTAL_FACING, EnumFacing.NORTH).func_177226_a(field_176464_a, Boolean.valueOf(false)).func_177226_a(MODE, BlockRedstoneComparator.Mode.COMPARE));
        this.field_149758_A = true;
    }

    public String func_149732_F()
    {
        return I18n.func_74838_a("item.comparator.name");
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.field_151132_bS;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Items.field_151132_bS);
    }

    protected int func_176403_d(IBlockState p_176403_1_)
    {
        return 2;
    }

    protected IBlockState func_180674_e(IBlockState p_180674_1_)
    {
        Boolean obool = (Boolean)p_180674_1_.get(field_176464_a);
        BlockRedstoneComparator.Mode blockredstonecomparator$mode = (BlockRedstoneComparator.Mode)p_180674_1_.get(MODE);
        EnumFacing enumfacing = (EnumFacing)p_180674_1_.get(HORIZONTAL_FACING);
        return Blocks.field_150455_bV.getDefaultState().func_177226_a(HORIZONTAL_FACING, enumfacing).func_177226_a(field_176464_a, obool).func_177226_a(MODE, blockredstonecomparator$mode);
    }

    protected IBlockState func_180675_k(IBlockState p_180675_1_)
    {
        Boolean obool = (Boolean)p_180675_1_.get(field_176464_a);
        BlockRedstoneComparator.Mode blockredstonecomparator$mode = (BlockRedstoneComparator.Mode)p_180675_1_.get(MODE);
        EnumFacing enumfacing = (EnumFacing)p_180675_1_.get(HORIZONTAL_FACING);
        return Blocks.field_150441_bU.getDefaultState().func_177226_a(HORIZONTAL_FACING, enumfacing).func_177226_a(field_176464_a, obool).func_177226_a(MODE, blockredstonecomparator$mode);
    }

    protected boolean func_176406_l(IBlockState p_176406_1_)
    {
        return this.field_149914_a || ((Boolean)p_176406_1_.get(field_176464_a)).booleanValue();
    }

    protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileEntityComparator ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
    }

    private int calculateOutput(World worldIn, BlockPos pos, IBlockState state)
    {
        return state.get(MODE) == BlockRedstoneComparator.Mode.SUBTRACT ? Math.max(this.calculateInputStrength(worldIn, pos, state) - this.getPowerOnSides(worldIn, pos, state), 0) : this.calculateInputStrength(worldIn, pos, state);
    }

    protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state)
    {
        int i = this.calculateInputStrength(worldIn, pos, state);

        if (i >= 15)
        {
            return true;
        }
        else if (i == 0)
        {
            return false;
        }
        else
        {
            int j = this.getPowerOnSides(worldIn, pos, state);

            if (j == 0)
            {
                return true;
            }
            else
            {
                return i >= j;
            }
        }
    }

    protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state)
    {
        int i = super.calculateInputStrength(worldIn, pos, state);
        EnumFacing enumfacing = (EnumFacing)state.get(HORIZONTAL_FACING);
        BlockPos blockpos = pos.offset(enumfacing);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);

        if (iblockstate.hasComparatorInputOverride())
        {
            i = iblockstate.getComparatorInputOverride(worldIn, blockpos);
        }
        else if (i < 15 && iblockstate.func_185915_l())
        {
            blockpos = blockpos.offset(enumfacing);
            iblockstate = worldIn.getBlockState(blockpos);

            if (iblockstate.hasComparatorInputOverride())
            {
                i = iblockstate.getComparatorInputOverride(worldIn, blockpos);
            }
            else if (iblockstate.getMaterial() == Material.AIR)
            {
                EntityItemFrame entityitemframe = this.findItemFrame(worldIn, enumfacing, blockpos);

                if (entityitemframe != null)
                {
                    i = entityitemframe.getAnalogOutput();
                }
            }
        }

        return i;
    }

    @Nullable
    private EntityItemFrame findItemFrame(World worldIn, final EnumFacing facing, BlockPos pos)
    {
        List<EntityItemFrame> list = worldIn.<EntityItemFrame>getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 1), (double)(pos.getZ() + 1)), new Predicate<Entity>()
        {
            public boolean apply(@Nullable Entity p_apply_1_)
            {
                return p_apply_1_ != null && p_apply_1_.getHorizontalFacing() == facing;
            }
        });
        return list.size() == 1 ? (EntityItemFrame)list.get(0) : null;
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (!p_180639_4_.abilities.allowEdit)
        {
            return false;
        }
        else
        {
            p_180639_3_ = p_180639_3_.cycle(MODE);
            float f = p_180639_3_.get(MODE) == BlockRedstoneComparator.Mode.SUBTRACT ? 0.55F : 0.5F;
            p_180639_1_.playSound(p_180639_4_, p_180639_2_, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, f);
            p_180639_1_.setBlockState(p_180639_2_, p_180639_3_, 2);
            this.onStateChange(p_180639_1_, p_180639_2_, p_180639_3_);
            return true;
        }
    }

    protected void updateState(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.func_175691_a(pos, this))
        {
            int i = this.calculateOutput(worldIn, pos, state);
            TileEntity tileentity = worldIn.getTileEntity(pos);
            int j = tileentity instanceof TileEntityComparator ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;

            if (i != j || this.func_176406_l(state) != this.shouldBePowered(worldIn, pos, state))
            {
                if (this.isFacingTowardsRepeater(worldIn, pos, state))
                {
                    worldIn.func_175654_a(pos, this, 2, -1);
                }
                else
                {
                    worldIn.func_175654_a(pos, this, 2, 0);
                }
            }
        }
    }

    private void onStateChange(World worldIn, BlockPos pos, IBlockState state)
    {
        int i = this.calculateOutput(worldIn, pos, state);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        int j = 0;

        if (tileentity instanceof TileEntityComparator)
        {
            TileEntityComparator tileentitycomparator = (TileEntityComparator)tileentity;
            j = tileentitycomparator.getOutputSignal();
            tileentitycomparator.setOutputSignal(i);
        }

        if (j != i || state.get(MODE) == BlockRedstoneComparator.Mode.COMPARE)
        {
            boolean flag1 = this.shouldBePowered(worldIn, pos, state);
            boolean flag = this.func_176406_l(state);

            if (flag && !flag1)
            {
                worldIn.setBlockState(pos, state.func_177226_a(field_176464_a, Boolean.valueOf(false)), 2);
            }
            else if (!flag && flag1)
            {
                worldIn.setBlockState(pos, state.func_177226_a(field_176464_a, Boolean.valueOf(true)), 2);
            }

            this.notifyNeighbors(worldIn, pos, state);
        }
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (this.field_149914_a)
        {
            p_180650_1_.setBlockState(p_180650_2_, this.func_180675_k(p_180650_3_).func_177226_a(field_176464_a, Boolean.valueOf(true)), 4);
        }

        this.onStateChange(p_180650_1_, p_180650_2_, p_180650_3_);
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        super.func_176213_c(p_176213_1_, p_176213_2_, p_176213_3_);
        p_176213_1_.setTileEntity(p_176213_2_, this.func_149915_a(p_176213_1_, 0));
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        super.func_180663_b(p_180663_1_, p_180663_2_, p_180663_3_);
        p_180663_1_.removeTileEntity(p_180663_2_);
        this.notifyNeighbors(p_180663_1_, p_180663_2_, p_180663_3_);
    }

    /**
     * Called on server when World#addBlockEvent is called. If server returns true, then also called on the client. On
     * the Server, this may perform additional changes to the world, like pistons replacing the block with an extended
     * base. On the client, the update may involve replacing tile entities or effects such as sounds or particles
     * @deprecated call via {@link IBlockState#onBlockEventReceived(World,BlockPos,int,int)} whenever possible.
     * Implementing/overriding is fine.
     */
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
    }

    public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityComparator();
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(HORIZONTAL_FACING, EnumFacing.byHorizontalIndex(p_176203_1_)).func_177226_a(field_176464_a, Boolean.valueOf((p_176203_1_ & 8) > 0)).func_177226_a(MODE, (p_176203_1_ & 4) > 0 ? BlockRedstoneComparator.Mode.SUBTRACT : BlockRedstoneComparator.Mode.COMPARE);
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((EnumFacing)p_176201_1_.get(HORIZONTAL_FACING)).getHorizontalIndex();

        if (((Boolean)p_176201_1_.get(field_176464_a)).booleanValue())
        {
            i |= 8;
        }

        if (p_176201_1_.get(MODE) == BlockRedstoneComparator.Mode.SUBTRACT)
        {
            i |= 4;
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

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {HORIZONTAL_FACING, MODE, field_176464_a});
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        return this.getDefaultState().func_177226_a(HORIZONTAL_FACING, p_180642_8_.getHorizontalFacing().getOpposite()).func_177226_a(field_176464_a, Boolean.valueOf(false)).func_177226_a(MODE, BlockRedstoneComparator.Mode.COMPARE);
    }

    public static enum Mode implements IStringSerializable
    {
        COMPARE("compare"),
        SUBTRACT("subtract");

        private final String name;

        private Mode(String p_i45731_3_)
        {
            this.name = p_i45731_3_;
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

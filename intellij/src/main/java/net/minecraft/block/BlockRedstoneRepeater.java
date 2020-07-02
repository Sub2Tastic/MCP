package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneRepeater extends BlockRedstoneDiode
{
    public static final PropertyBool LOCKED = PropertyBool.create("locked");
    public static final PropertyInteger DELAY = PropertyInteger.create("delay", 1, 4);

    protected BlockRedstoneRepeater(boolean p_i45424_1_)
    {
        super(p_i45424_1_);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(HORIZONTAL_FACING, EnumFacing.NORTH).func_177226_a(DELAY, Integer.valueOf(1)).func_177226_a(LOCKED, Boolean.valueOf(false)));
    }

    public String func_149732_F()
    {
        return I18n.func_74838_a("item.diode.name");
    }

    public IBlockState func_176221_a(IBlockState p_176221_1_, IBlockAccess p_176221_2_, BlockPos p_176221_3_)
    {
        return p_176221_1_.func_177226_a(LOCKED, Boolean.valueOf(this.isLocked(p_176221_2_, p_176221_3_, p_176221_1_)));
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

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (!p_180639_4_.abilities.allowEdit)
        {
            return false;
        }
        else
        {
            p_180639_1_.setBlockState(p_180639_2_, p_180639_3_.cycle(DELAY), 3);
            return true;
        }
    }

    protected int func_176403_d(IBlockState p_176403_1_)
    {
        return ((Integer)p_176403_1_.get(DELAY)).intValue() * 2;
    }

    protected IBlockState func_180674_e(IBlockState p_180674_1_)
    {
        Integer integer = (Integer)p_180674_1_.get(DELAY);
        Boolean obool = (Boolean)p_180674_1_.get(LOCKED);
        EnumFacing enumfacing = (EnumFacing)p_180674_1_.get(HORIZONTAL_FACING);
        return Blocks.field_150416_aS.getDefaultState().func_177226_a(HORIZONTAL_FACING, enumfacing).func_177226_a(DELAY, integer).func_177226_a(LOCKED, obool);
    }

    protected IBlockState func_180675_k(IBlockState p_180675_1_)
    {
        Integer integer = (Integer)p_180675_1_.get(DELAY);
        Boolean obool = (Boolean)p_180675_1_.get(LOCKED);
        EnumFacing enumfacing = (EnumFacing)p_180675_1_.get(HORIZONTAL_FACING);
        return Blocks.field_150413_aR.getDefaultState().func_177226_a(HORIZONTAL_FACING, enumfacing).func_177226_a(DELAY, integer).func_177226_a(LOCKED, obool);
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.field_151107_aW;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Items.field_151107_aW);
    }

    public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        return this.getPowerOnSides(worldIn, pos, state) > 0;
    }

    protected boolean isAlternateInput(IBlockState state)
    {
        return isDiode(state);
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
     * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
     * of whether the block can receive random update ticks
     */
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (this.field_149914_a)
        {
            EnumFacing enumfacing = (EnumFacing)stateIn.get(HORIZONTAL_FACING);
            double d0 = (double)((float)pos.getX() + 0.5F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
            double d1 = (double)((float)pos.getY() + 0.4F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
            double d2 = (double)((float)pos.getZ() + 0.5F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
            float f = -5.0F;

            if (rand.nextBoolean())
            {
                f = (float)(((Integer)stateIn.get(DELAY)).intValue() * 2 - 1);
            }

            f = f / 16.0F;
            double d3 = (double)(f * (float)enumfacing.getXOffset());
            double d4 = (double)(f * (float)enumfacing.getZOffset());
            worldIn.func_175688_a(EnumParticleTypes.REDSTONE, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
        }
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        super.func_180663_b(p_180663_1_, p_180663_2_, p_180663_3_);
        this.notifyNeighbors(p_180663_1_, p_180663_2_, p_180663_3_);
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(HORIZONTAL_FACING, EnumFacing.byHorizontalIndex(p_176203_1_)).func_177226_a(LOCKED, Boolean.valueOf(false)).func_177226_a(DELAY, Integer.valueOf(1 + (p_176203_1_ >> 2)));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((EnumFacing)p_176201_1_.get(HORIZONTAL_FACING)).getHorizontalIndex();
        i = i | ((Integer)p_176201_1_.get(DELAY)).intValue() - 1 << 2;
        return i;
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {HORIZONTAL_FACING, DELAY, LOCKED});
    }
}

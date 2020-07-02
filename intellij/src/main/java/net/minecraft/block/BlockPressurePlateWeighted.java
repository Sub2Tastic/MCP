package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate
{
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    private final int maxWeight;

    protected BlockPressurePlateWeighted(Material p_i46379_1_, int p_i46379_2_)
    {
        this(p_i46379_1_, p_i46379_2_, p_i46379_1_.getColor());
    }

    protected BlockPressurePlateWeighted(Material p_i46380_1_, int p_i46380_2_, MapColor p_i46380_3_)
    {
        super(p_i46380_1_, p_i46380_3_);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(POWER, Integer.valueOf(0)));
        this.maxWeight = p_i46380_2_;
    }

    protected int computeRedstoneStrength(World worldIn, BlockPos pos)
    {
        int i = Math.min(worldIn.func_72872_a(Entity.class, PRESSURE_AABB.offset(pos)).size(), this.maxWeight);

        if (i > 0)
        {
            float f = (float)Math.min(this.maxWeight, i) / (float)this.maxWeight;
            return MathHelper.ceil(f * 15.0F);
        }
        else
        {
            return 0;
        }
    }

    protected void playClickOnSound(World worldIn, BlockPos pos)
    {
        worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.90000004F);
    }

    protected void playClickOffSound(World worldIn, BlockPos pos)
    {
        worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.75F);
    }

    protected int getRedstoneStrength(IBlockState state)
    {
        return ((Integer)state.get(POWER)).intValue();
    }

    protected IBlockState setRedstoneStrength(IBlockState state, int strength)
    {
        return state.func_177226_a(POWER, Integer.valueOf(strength));
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 10;
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
}

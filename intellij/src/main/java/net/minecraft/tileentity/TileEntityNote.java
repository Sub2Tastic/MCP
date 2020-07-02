package net.minecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileEntityNote extends TileEntity
{
    public byte field_145879_a;
    public boolean field_145880_i;

    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);
        compound.putByte("note", this.field_145879_a);
        compound.putBoolean("powered", this.field_145880_i);
        return compound;
    }

    public void read(NBTTagCompound compound)
    {
        super.read(compound);
        this.field_145879_a = compound.getByte("note");
        this.field_145879_a = (byte)MathHelper.clamp(this.field_145879_a, 0, 24);
        this.field_145880_i = compound.getBoolean("powered");
    }

    public void func_145877_a()
    {
        this.field_145879_a = (byte)((this.field_145879_a + 1) % 25);
        this.markDirty();
    }

    public void func_175108_a(World p_175108_1_, BlockPos p_175108_2_)
    {
        if (p_175108_1_.getBlockState(p_175108_2_.up()).getMaterial() == Material.AIR)
        {
            IBlockState iblockstate = p_175108_1_.getBlockState(p_175108_2_.down());
            Material material = iblockstate.getMaterial();
            int i = 0;

            if (material == Material.ROCK)
            {
                i = 1;
            }

            if (material == Material.SAND)
            {
                i = 2;
            }

            if (material == Material.GLASS)
            {
                i = 3;
            }

            if (material == Material.WOOD)
            {
                i = 4;
            }

            Block block = iblockstate.getBlock();

            if (block == Blocks.CLAY)
            {
                i = 5;
            }

            if (block == Blocks.GOLD_BLOCK)
            {
                i = 6;
            }

            if (block == Blocks.field_150325_L)
            {
                i = 7;
            }

            if (block == Blocks.PACKED_ICE)
            {
                i = 8;
            }

            if (block == Blocks.BONE_BLOCK)
            {
                i = 9;
            }

            p_175108_1_.addBlockEvent(p_175108_2_, Blocks.field_150323_B, i, this.field_145879_a);
        }
    }
}

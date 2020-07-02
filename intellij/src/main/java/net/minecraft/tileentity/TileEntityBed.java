package net.minecraft.tileentity;

import net.minecraft.block.BlockBed;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

public class TileEntityBed extends TileEntity
{
    private EnumDyeColor color = EnumDyeColor.RED;

    public void func_193051_a(ItemStack p_193051_1_)
    {
        this.setColor(EnumDyeColor.func_176764_b(p_193051_1_.func_77960_j()));
    }

    public void read(NBTTagCompound compound)
    {
        super.read(compound);

        if (compound.contains("color"))
        {
            this.color = EnumDyeColor.func_176764_b(compound.getInt("color"));
        }
    }

    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);
        compound.putInt("color", this.color.func_176765_a());
        return compound;
    }

    /**
     * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
     * many blocks change at once. This compound comes back to you clientside in {@link handleUpdateTag}
     */
    public NBTTagCompound getUpdateTag()
    {
        return this.write(new NBTTagCompound());
    }

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 11, this.getUpdateTag());
    }

    public EnumDyeColor getColor()
    {
        return this.color;
    }

    public void setColor(EnumDyeColor color)
    {
        this.color = color;
        this.markDirty();
    }

    public boolean func_193050_e()
    {
        return BlockBed.func_193385_b(this.func_145832_p());
    }

    public ItemStack func_193049_f()
    {
        return new ItemStack(Items.field_151104_aV, 1, this.color.func_176765_a());
    }
}

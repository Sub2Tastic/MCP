package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;

public class TileEntityFlowerPot extends TileEntity
{
    private Item field_145967_a;
    private int field_145968_i;

    public TileEntityFlowerPot()
    {
    }

    public TileEntityFlowerPot(Item p_i45442_1_, int p_i45442_2_)
    {
        this.field_145967_a = p_i45442_1_;
        this.field_145968_i = p_i45442_2_;
    }

    public static void func_189699_a(DataFixer p_189699_0_)
    {
    }

    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);
        ResourceLocation resourcelocation = Item.field_150901_e.getKey(this.field_145967_a);
        compound.putString("Item", resourcelocation == null ? "" : resourcelocation.toString());
        compound.putInt("Data", this.field_145968_i);
        return compound;
    }

    public void read(NBTTagCompound compound)
    {
        super.read(compound);

        if (compound.contains("Item", 8))
        {
            this.field_145967_a = Item.func_111206_d(compound.getString("Item"));
        }
        else
        {
            this.field_145967_a = Item.getItemById(compound.getInt("Item"));
        }

        this.field_145968_i = compound.getInt("Data");
    }

    @Nullable

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 5, this.getUpdateTag());
    }

    /**
     * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
     * many blocks change at once. This compound comes back to you clientside in {@link handleUpdateTag}
     */
    public NBTTagCompound getUpdateTag()
    {
        return this.write(new NBTTagCompound());
    }

    public void func_190614_a(ItemStack p_190614_1_)
    {
        this.field_145967_a = p_190614_1_.getItem();
        this.field_145968_i = p_190614_1_.func_77960_j();
    }

    public ItemStack func_184403_b()
    {
        return this.field_145967_a == null ? ItemStack.EMPTY : new ItemStack(this.field_145967_a, 1, this.field_145968_i);
    }

    @Nullable
    public Item func_145965_a()
    {
        return this.field_145967_a;
    }

    public int func_145966_b()
    {
        return this.field_145968_i;
    }
}

package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerFurnace extends Container
{
    private final IInventory field_75158_e;
    private int field_178152_f;
    private int field_178153_g;
    private int field_178154_h;
    private int field_178155_i;

    public ContainerFurnace(InventoryPlayer p_i45794_1_, IInventory p_i45794_2_)
    {
        this.field_75158_e = p_i45794_2_;
        this.addSlot(new Slot(p_i45794_2_, 0, 56, 17));
        this.addSlot(new SlotFurnaceFuel(p_i45794_2_, 1, 56, 53));
        this.addSlot(new SlotFurnaceOutput(p_i45794_1_.player, p_i45794_2_, 2, 116, 35));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlot(new Slot(p_i45794_1_, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlot(new Slot(p_i45794_1_, k, 8 + k * 18, 142));
        }
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.func_175173_a(this, this.field_75158_e);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.field_178152_f != this.field_75158_e.func_174887_a_(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.field_75158_e.func_174887_a_(2));
            }

            if (this.field_178154_h != this.field_75158_e.func_174887_a_(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.field_75158_e.func_174887_a_(0));
            }

            if (this.field_178155_i != this.field_75158_e.func_174887_a_(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.field_75158_e.func_174887_a_(1));
            }

            if (this.field_178153_g != this.field_75158_e.func_174887_a_(3))
            {
                icontainerlistener.sendWindowProperty(this, 3, this.field_75158_e.func_174887_a_(3));
            }
        }

        this.field_178152_f = this.field_75158_e.func_174887_a_(2);
        this.field_178154_h = this.field_75158_e.func_174887_a_(0);
        this.field_178155_i = this.field_75158_e.func_174887_a_(1);
        this.field_178153_g = this.field_75158_e.func_174887_a_(3);
    }

    public void updateProgressBar(int id, int data)
    {
        this.field_75158_e.func_174885_b(id, data);
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.field_75158_e.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 1 && index != 0)
            {
                if (!FurnaceRecipes.func_77602_a().func_151395_a(itemstack1).isEmpty())
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (TileEntityFurnace.func_145954_b(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}

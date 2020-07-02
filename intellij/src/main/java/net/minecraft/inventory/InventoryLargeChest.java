package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public class InventoryLargeChest implements ILockableContainer
{
    private final String field_70479_a;
    private final ILockableContainer upperChest;
    private final ILockableContainer lowerChest;

    public InventoryLargeChest(String p_i45905_1_, ILockableContainer p_i45905_2_, ILockableContainer p_i45905_3_)
    {
        this.field_70479_a = p_i45905_1_;

        if (p_i45905_2_ == null)
        {
            p_i45905_2_ = p_i45905_3_;
        }

        if (p_i45905_3_ == null)
        {
            p_i45905_3_ = p_i45905_2_;
        }

        this.upperChest = p_i45905_2_;
        this.lowerChest = p_i45905_3_;

        if (p_i45905_2_.func_174893_q_())
        {
            p_i45905_3_.func_174892_a(p_i45905_2_.func_174891_i());
        }
        else if (p_i45905_3_.func_174893_q_())
        {
            p_i45905_2_.func_174892_a(p_i45905_3_.func_174891_i());
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
    }

    public boolean isEmpty()
    {
        return this.upperChest.isEmpty() && this.lowerChest.isEmpty();
    }

    /**
     * Return whether the given inventory is part of this large chest.
     */
    public boolean isPartOfLargeChest(IInventory inventoryIn)
    {
        return this.upperChest == inventoryIn || this.lowerChest == inventoryIn;
    }

    public String func_70005_c_()
    {
        if (this.upperChest.hasCustomName())
        {
            return this.upperChest.func_70005_c_();
        }
        else
        {
            return this.lowerChest.hasCustomName() ? this.lowerChest.func_70005_c_() : this.field_70479_a;
        }
    }

    public boolean hasCustomName()
    {
        return this.upperChest.hasCustomName() || this.lowerChest.hasCustomName();
    }

    public ITextComponent getDisplayName()
    {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index)
    {
        return index >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlot(index - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        return index >= this.upperChest.getSizeInventory() ? this.lowerChest.decrStackSize(index - this.upperChest.getSizeInventory(), count) : this.upperChest.decrStackSize(index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index)
    {
        return index >= this.upperChest.getSizeInventory() ? this.lowerChest.removeStackFromSlot(index - this.upperChest.getSizeInventory()) : this.upperChest.removeStackFromSlot(index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (index >= this.upperChest.getSizeInventory())
        {
            this.lowerChest.setInventorySlotContents(index - this.upperChest.getSizeInventory(), stack);
        }
        else
        {
            this.upperChest.setInventorySlotContents(index, stack);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return this.upperChest.getInventoryStackLimit();
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty()
    {
        this.upperChest.markDirty();
        this.lowerChest.markDirty();
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.upperChest.isUsableByPlayer(player) && this.lowerChest.isUsableByPlayer(player);
    }

    public void openInventory(EntityPlayer player)
    {
        this.upperChest.openInventory(player);
        this.lowerChest.openInventory(player);
    }

    public void closeInventory(EntityPlayer player)
    {
        this.upperChest.closeInventory(player);
        this.lowerChest.closeInventory(player);
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    public int func_174887_a_(int p_174887_1_)
    {
        return 0;
    }

    public void func_174885_b(int p_174885_1_, int p_174885_2_)
    {
    }

    public int func_174890_g()
    {
        return 0;
    }

    public boolean func_174893_q_()
    {
        return this.upperChest.func_174893_q_() || this.lowerChest.func_174893_q_();
    }

    public void func_174892_a(LockCode p_174892_1_)
    {
        this.upperChest.func_174892_a(p_174892_1_);
        this.lowerChest.func_174892_a(p_174892_1_);
    }

    public LockCode func_174891_i()
    {
        return this.upperChest.func_174891_i();
    }

    public String func_174875_k()
    {
        return this.upperChest.func_174875_k();
    }

    public Container func_174876_a(InventoryPlayer p_174876_1_, EntityPlayer p_174876_2_)
    {
        return new ContainerChest(p_174876_1_, this, p_174876_2_);
    }

    public void clear()
    {
        this.upperChest.clear();
        this.lowerChest.clear();
    }
}

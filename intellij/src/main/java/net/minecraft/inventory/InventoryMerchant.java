package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchant implements IInventory
{
    private final IMerchant merchant;
    private final NonNullList<ItemStack> slots = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    private final EntityPlayer field_70475_c;
    private MerchantRecipe field_70472_d;
    private int currentRecipeIndex;

    public InventoryMerchant(EntityPlayer p_i1820_1_, IMerchant p_i1820_2_)
    {
        this.field_70475_c = p_i1820_1_;
        this.merchant = p_i1820_2_;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.slots.size();
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.slots)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index)
    {
        return this.slots.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = this.slots.get(index);

        if (index == 2 && !itemstack.isEmpty())
        {
            return ItemStackHelper.getAndSplit(this.slots, index, itemstack.getCount());
        }
        else
        {
            ItemStack itemstack1 = ItemStackHelper.getAndSplit(this.slots, index, count);

            if (!itemstack1.isEmpty() && this.inventoryResetNeededOnSlotChange(index))
            {
                this.resetRecipeAndSlots();
            }

            return itemstack1;
        }
    }

    /**
     * if par1 slot has changed, does resetRecipeAndSlots need to be called?
     */
    private boolean inventoryResetNeededOnSlotChange(int slotIn)
    {
        return slotIn == 0 || slotIn == 1;
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.slots, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.slots.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (this.inventoryResetNeededOnSlotChange(index))
        {
            this.resetRecipeAndSlots();
        }
    }

    public String func_70005_c_()
    {
        return "mob.villager";
    }

    public boolean hasCustomName()
    {
        return false;
    }

    public ITextComponent getDisplayName()
    {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.merchant.getCustomer() == player;
    }

    public void openInventory(EntityPlayer player)
    {
    }

    public void closeInventory(EntityPlayer player)
    {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty()
    {
        this.resetRecipeAndSlots();
    }

    public void resetRecipeAndSlots()
    {
        this.field_70472_d = null;
        ItemStack itemstack = this.slots.get(0);
        ItemStack itemstack1 = this.slots.get(1);

        if (itemstack.isEmpty())
        {
            itemstack = itemstack1;
            itemstack1 = ItemStack.EMPTY;
        }

        if (itemstack.isEmpty())
        {
            this.setInventorySlotContents(2, ItemStack.EMPTY);
        }
        else
        {
            MerchantRecipeList merchantrecipelist = this.merchant.func_70934_b(this.field_70475_c);

            if (merchantrecipelist != null)
            {
                MerchantRecipe merchantrecipe = merchantrecipelist.func_77203_a(itemstack, itemstack1, this.currentRecipeIndex);

                if (merchantrecipe != null && !merchantrecipe.func_82784_g())
                {
                    this.field_70472_d = merchantrecipe;
                    this.setInventorySlotContents(2, merchantrecipe.func_77397_d().copy());
                }
                else if (!itemstack1.isEmpty())
                {
                    merchantrecipe = merchantrecipelist.func_77203_a(itemstack1, itemstack, this.currentRecipeIndex);

                    if (merchantrecipe != null && !merchantrecipe.func_82784_g())
                    {
                        this.field_70472_d = merchantrecipe;
                        this.setInventorySlotContents(2, merchantrecipe.func_77397_d().copy());
                    }
                    else
                    {
                        this.setInventorySlotContents(2, ItemStack.EMPTY);
                    }
                }
                else
                {
                    this.setInventorySlotContents(2, ItemStack.EMPTY);
                }
            }

            this.merchant.verifySellingItem(this.getStackInSlot(2));
        }
    }

    public MerchantRecipe func_70468_h()
    {
        return this.field_70472_d;
    }

    public void setCurrentRecipeIndex(int currentRecipeIndexIn)
    {
        this.currentRecipeIndex = currentRecipeIndexIn;
        this.resetRecipeAndSlots();
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

    public void clear()
    {
        this.slots.clear();
    }
}

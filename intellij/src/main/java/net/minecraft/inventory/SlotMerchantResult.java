package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.village.MerchantRecipe;

public class SlotMerchantResult extends Slot
{
    private final InventoryMerchant merchantInventory;
    private final EntityPlayer player;
    private int removeCount;
    private final IMerchant merchant;

    public SlotMerchantResult(EntityPlayer player, IMerchant merchant, InventoryMerchant merchantInventory, int slotIndex, int xPosition, int yPosition)
    {
        super(merchantInventory, slotIndex, xPosition, yPosition);
        this.player = player;
        this.merchant = merchant;
        this.merchantInventory = merchantInventory;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int amount)
    {
        if (this.getHasStack())
        {
            this.removeCount += Math.min(amount, this.getStack().getCount());
        }

        return super.decrStackSize(amount);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    protected void onCrafting(ItemStack stack, int amount)
    {
        this.removeCount += amount;
        this.onCrafting(stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void onCrafting(ItemStack stack)
    {
        stack.onCrafting(this.player.world, this.player, this.removeCount);
        this.removeCount = 0;
    }

    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
    {
        this.onCrafting(stack);
        MerchantRecipe merchantrecipe = this.merchantInventory.func_70468_h();

        if (merchantrecipe != null)
        {
            ItemStack itemstack = this.merchantInventory.getStackInSlot(0);
            ItemStack itemstack1 = this.merchantInventory.getStackInSlot(1);

            if (this.func_75230_a(merchantrecipe, itemstack, itemstack1) || this.func_75230_a(merchantrecipe, itemstack1, itemstack))
            {
                this.merchant.func_70933_a(merchantrecipe);
                thePlayer.addStat(StatList.TRADED_WITH_VILLAGER);
                this.merchantInventory.setInventorySlotContents(0, itemstack);
                this.merchantInventory.setInventorySlotContents(1, itemstack1);
            }
        }

        return stack;
    }

    private boolean func_75230_a(MerchantRecipe p_75230_1_, ItemStack p_75230_2_, ItemStack p_75230_3_)
    {
        ItemStack itemstack = p_75230_1_.func_77394_a();
        ItemStack itemstack1 = p_75230_1_.func_77396_b();

        if (p_75230_2_.getItem() == itemstack.getItem() && p_75230_2_.getCount() >= itemstack.getCount())
        {
            if (!itemstack1.isEmpty() && !p_75230_3_.isEmpty() && itemstack1.getItem() == p_75230_3_.getItem() && p_75230_3_.getCount() >= itemstack1.getCount())
            {
                p_75230_2_.shrink(itemstack.getCount());
                p_75230_3_.shrink(itemstack1.getCount());
                return true;
            }

            if (itemstack1.isEmpty() && p_75230_3_.isEmpty())
            {
                p_75230_2_.shrink(itemstack.getCount());
                return true;
            }
        }

        return false;
    }
}

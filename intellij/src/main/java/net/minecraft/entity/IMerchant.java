package net.minecraft.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public interface IMerchant
{
    void setCustomer(EntityPlayer player);

    @Nullable
    EntityPlayer getCustomer();

    @Nullable
    MerchantRecipeList func_70934_b(EntityPlayer p_70934_1_);

    void func_70930_a(MerchantRecipeList p_70930_1_);

    void func_70933_a(MerchantRecipe p_70933_1_);

    /**
     * Notifies the merchant of a possible merchantrecipe being fulfilled or not. Usually, this is just a sound byte
     * being played depending if the suggested itemstack is not null.
     */
    void verifySellingItem(ItemStack stack);

    ITextComponent getDisplayName();

    World getWorld();

    BlockPos func_190671_u_();
}

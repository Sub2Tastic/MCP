package net.minecraft.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class NpcMerchant implements IMerchant
{
    private final InventoryMerchant merchantInventory;
    private final EntityPlayer customer;
    private MerchantRecipeList field_70936_c;
    private final ITextComponent field_175548_d;

    public NpcMerchant(EntityPlayer p_i45817_1_, ITextComponent p_i45817_2_)
    {
        this.customer = p_i45817_1_;
        this.field_175548_d = p_i45817_2_;
        this.merchantInventory = new InventoryMerchant(p_i45817_1_, this);
    }

    @Nullable
    public EntityPlayer getCustomer()
    {
        return this.customer;
    }

    public void setCustomer(@Nullable EntityPlayer player)
    {
    }

    @Nullable
    public MerchantRecipeList func_70934_b(EntityPlayer p_70934_1_)
    {
        return this.field_70936_c;
    }

    public void func_70930_a(@Nullable MerchantRecipeList p_70930_1_)
    {
        this.field_70936_c = p_70930_1_;
    }

    public void func_70933_a(MerchantRecipe p_70933_1_)
    {
        p_70933_1_.func_77399_f();
    }

    /**
     * Notifies the merchant of a possible merchantrecipe being fulfilled or not. Usually, this is just a sound byte
     * being played depending if the suggested itemstack is not null.
     */
    public void verifySellingItem(ItemStack stack)
    {
    }

    public ITextComponent getDisplayName()
    {
        return (ITextComponent)(this.field_175548_d != null ? this.field_175548_d : new TextComponentTranslation("entity.Villager.name", new Object[0]));
    }

    public World getWorld()
    {
        return this.customer.world;
    }

    public BlockPos func_190671_u_()
    {
        return new BlockPos(this.customer);
    }
}

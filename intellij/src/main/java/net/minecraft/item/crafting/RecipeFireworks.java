package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeFireworks implements IRecipe
{
    private ItemStack field_92102_a = ItemStack.EMPTY;

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        this.field_92102_a = ItemStack.EMPTY;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int i1 = 0;
        int j1 = 0;

        for (int k1 = 0; k1 < inv.getSizeInventory(); ++k1)
        {
            ItemStack itemstack = inv.getStackInSlot(k1);

            if (!itemstack.isEmpty())
            {
                if (itemstack.getItem() == Items.GUNPOWDER)
                {
                    ++j;
                }
                else if (itemstack.getItem() == Items.field_151154_bQ)
                {
                    ++l;
                }
                else if (itemstack.getItem() == Items.field_151100_aR)
                {
                    ++k;
                }
                else if (itemstack.getItem() == Items.PAPER)
                {
                    ++i;
                }
                else if (itemstack.getItem() == Items.GLOWSTONE_DUST)
                {
                    ++i1;
                }
                else if (itemstack.getItem() == Items.DIAMOND)
                {
                    ++i1;
                }
                else if (itemstack.getItem() == Items.FIRE_CHARGE)
                {
                    ++j1;
                }
                else if (itemstack.getItem() == Items.FEATHER)
                {
                    ++j1;
                }
                else if (itemstack.getItem() == Items.GOLD_NUGGET)
                {
                    ++j1;
                }
                else
                {
                    if (itemstack.getItem() != Items.field_151144_bL)
                    {
                        return false;
                    }

                    ++j1;
                }
            }
        }

        i1 = i1 + k + j1;

        if (j <= 3 && i <= 1)
        {
            if (j >= 1 && i == 1 && i1 == 0)
            {
                this.field_92102_a = new ItemStack(Items.field_151152_bP, 3);
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                if (l > 0)
                {
                    NBTTagList nbttaglist = new NBTTagList();

                    for (int k2 = 0; k2 < inv.getSizeInventory(); ++k2)
                    {
                        ItemStack itemstack3 = inv.getStackInSlot(k2);

                        if (itemstack3.getItem() == Items.field_151154_bQ && itemstack3.hasTag() && itemstack3.getTag().contains("Explosion", 10))
                        {
                            nbttaglist.func_74742_a(itemstack3.getTag().getCompound("Explosion"));
                        }
                    }

                    nbttagcompound1.func_74782_a("Explosions", nbttaglist);
                }

                nbttagcompound1.putByte("Flight", (byte)j);
                NBTTagCompound nbttagcompound3 = new NBTTagCompound();
                nbttagcompound3.func_74782_a("Fireworks", nbttagcompound1);
                this.field_92102_a.setTag(nbttagcompound3);
                return true;
            }
            else if (j == 1 && i == 0 && l == 0 && k > 0 && j1 <= 1)
            {
                this.field_92102_a = new ItemStack(Items.field_151154_bQ);
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                byte b0 = 0;
                List<Integer> list = Lists.<Integer>newArrayList();

                for (int l1 = 0; l1 < inv.getSizeInventory(); ++l1)
                {
                    ItemStack itemstack2 = inv.getStackInSlot(l1);

                    if (!itemstack2.isEmpty())
                    {
                        if (itemstack2.getItem() == Items.field_151100_aR)
                        {
                            list.add(Integer.valueOf(ItemDye.field_150922_c[itemstack2.func_77960_j() & 15]));
                        }
                        else if (itemstack2.getItem() == Items.GLOWSTONE_DUST)
                        {
                            nbttagcompound2.putBoolean("Flicker", true);
                        }
                        else if (itemstack2.getItem() == Items.DIAMOND)
                        {
                            nbttagcompound2.putBoolean("Trail", true);
                        }
                        else if (itemstack2.getItem() == Items.FIRE_CHARGE)
                        {
                            b0 = 1;
                        }
                        else if (itemstack2.getItem() == Items.FEATHER)
                        {
                            b0 = 4;
                        }
                        else if (itemstack2.getItem() == Items.GOLD_NUGGET)
                        {
                            b0 = 2;
                        }
                        else if (itemstack2.getItem() == Items.field_151144_bL)
                        {
                            b0 = 3;
                        }
                    }
                }

                int[] aint1 = new int[list.size()];

                for (int l2 = 0; l2 < aint1.length; ++l2)
                {
                    aint1[l2] = ((Integer)list.get(l2)).intValue();
                }

                nbttagcompound2.putIntArray("Colors", aint1);
                nbttagcompound2.putByte("Type", b0);
                nbttagcompound.func_74782_a("Explosion", nbttagcompound2);
                this.field_92102_a.setTag(nbttagcompound);
                return true;
            }
            else if (j == 0 && i == 0 && l == 1 && k > 0 && k == i1)
            {
                List<Integer> list1 = Lists.<Integer>newArrayList();

                for (int i2 = 0; i2 < inv.getSizeInventory(); ++i2)
                {
                    ItemStack itemstack1 = inv.getStackInSlot(i2);

                    if (!itemstack1.isEmpty())
                    {
                        if (itemstack1.getItem() == Items.field_151100_aR)
                        {
                            list1.add(Integer.valueOf(ItemDye.field_150922_c[itemstack1.func_77960_j() & 15]));
                        }
                        else if (itemstack1.getItem() == Items.field_151154_bQ)
                        {
                            this.field_92102_a = itemstack1.copy();
                            this.field_92102_a.setCount(1);
                        }
                    }
                }

                int[] aint = new int[list1.size()];

                for (int j2 = 0; j2 < aint.length; ++j2)
                {
                    aint[j2] = ((Integer)list1.get(j2)).intValue();
                }

                if (!this.field_92102_a.isEmpty() && this.field_92102_a.hasTag())
                {
                    NBTTagCompound nbttagcompound4 = this.field_92102_a.getTag().getCompound("Explosion");

                    if (nbttagcompound4 == null)
                    {
                        return false;
                    }
                    else
                    {
                        nbttagcompound4.putIntArray("FadeColors", aint);
                        return true;
                    }
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        return this.field_92102_a.copy();
    }

    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
     * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
     */
    public ItemStack getRecipeOutput()
    {
        return this.field_92102_a;
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);

            if (itemstack.getItem().hasContainerItem())
            {
                nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
            }
        }

        return nonnulllist;
    }

    /**
     * If true, this recipe does not appear in the recipe book and does not respect recipe unlocking (and the
     * doLimitedCrafting gamerule)
     */
    public boolean isDynamic()
    {
        return true;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canFit(int width, int height)
    {
        return width * height >= 1;
    }
}

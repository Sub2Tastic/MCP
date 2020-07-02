package net.minecraft.stats;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RecipeBookServer extends RecipeBook
{
    private static final Logger LOGGER = LogManager.getLogger();

    public void func_193835_a(List<IRecipe> p_193835_1_, EntityPlayerMP p_193835_2_)
    {
        List<IRecipe> list = Lists.<IRecipe>newArrayList();

        for (IRecipe irecipe : p_193835_1_)
        {
            if (!this.recipes.get(func_194075_d(irecipe)) && !irecipe.isDynamic())
            {
                this.unlock(irecipe);
                this.markNew(irecipe);
                list.add(irecipe);
                CriteriaTriggers.RECIPE_UNLOCKED.trigger(p_193835_2_, irecipe);
            }
        }

        this.sendPacket(SPacketRecipeBook.State.ADD, p_193835_2_, list);
    }

    public void func_193834_b(List<IRecipe> p_193834_1_, EntityPlayerMP p_193834_2_)
    {
        List<IRecipe> list = Lists.<IRecipe>newArrayList();

        for (IRecipe irecipe : p_193834_1_)
        {
            if (this.recipes.get(func_194075_d(irecipe)))
            {
                this.lock(irecipe);
                list.add(irecipe);
            }
        }

        this.sendPacket(SPacketRecipeBook.State.REMOVE, p_193834_2_, list);
    }

    private void sendPacket(SPacketRecipeBook.State state, EntityPlayerMP player, List<IRecipe> recipesIn)
    {
        player.connection.sendPacket(new SPacketRecipeBook(state, recipesIn, Collections.emptyList(), this.isGuiOpen, this.isFilteringCraftable));
    }

    public NBTTagCompound write()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.putBoolean("isGuiOpen", this.isGuiOpen);
        nbttagcompound.putBoolean("isFilteringCraftable", this.isFilteringCraftable);
        NBTTagList nbttaglist = new NBTTagList();

        for (IRecipe irecipe : this.func_194079_d())
        {
            nbttaglist.func_74742_a(new NBTTagString(((ResourceLocation)CraftingManager.field_193380_a.getKey(irecipe)).toString()));
        }

        nbttagcompound.func_74782_a("recipes", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        for (IRecipe irecipe1 : this.func_194080_e())
        {
            nbttaglist1.func_74742_a(new NBTTagString(((ResourceLocation)CraftingManager.field_193380_a.getKey(irecipe1)).toString()));
        }

        nbttagcompound.func_74782_a("toBeDisplayed", nbttaglist1);
        return nbttagcompound;
    }

    public void read(NBTTagCompound tag)
    {
        this.isGuiOpen = tag.getBoolean("isGuiOpen");
        this.isFilteringCraftable = tag.getBoolean("isFilteringCraftable");
        NBTTagList nbttaglist = tag.getList("recipes", 8);

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
        {
            ResourceLocation resourcelocation = new ResourceLocation(nbttaglist.getString(i));
            IRecipe irecipe = CraftingManager.func_193373_a(resourcelocation);

            if (irecipe == null)
            {
                LOGGER.info("Tried to load unrecognized recipe: {} removed now.", (Object)resourcelocation);
            }
            else
            {
                this.unlock(irecipe);
            }
        }

        NBTTagList nbttaglist1 = tag.getList("toBeDisplayed", 8);

        for (int j = 0; j < nbttaglist1.func_74745_c(); ++j)
        {
            ResourceLocation resourcelocation1 = new ResourceLocation(nbttaglist1.getString(j));
            IRecipe irecipe1 = CraftingManager.func_193373_a(resourcelocation1);

            if (irecipe1 == null)
            {
                LOGGER.info("Tried to load unrecognized recipe: {} removed now.", (Object)resourcelocation1);
            }
            else
            {
                this.markNew(irecipe1);
            }
        }
    }

    private List<IRecipe> func_194079_d()
    {
        List<IRecipe> list = Lists.<IRecipe>newArrayList();

        for (int i = this.recipes.nextSetBit(0); i >= 0; i = this.recipes.nextSetBit(i + 1))
        {
            list.add(CraftingManager.field_193380_a.func_148754_a(i));
        }

        return list;
    }

    private List<IRecipe> func_194080_e()
    {
        List<IRecipe> list = Lists.<IRecipe>newArrayList();

        for (int i = this.newRecipes.nextSetBit(0); i >= 0; i = this.newRecipes.nextSetBit(i + 1))
        {
            list.add(CraftingManager.field_193380_a.func_148754_a(i));
        }

        return list;
    }

    public void init(EntityPlayerMP player)
    {
        player.connection.sendPacket(new SPacketRecipeBook(SPacketRecipeBook.State.INIT, this.func_194079_d(), this.func_194080_e(), this.isGuiOpen, this.isFilteringCraftable));
    }
}

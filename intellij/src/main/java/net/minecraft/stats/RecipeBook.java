package net.minecraft.stats;

import java.util.BitSet;
import javax.annotation.Nullable;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class RecipeBook
{
    protected final BitSet recipes = new BitSet();
    protected final BitSet newRecipes = new BitSet();
    protected boolean isGuiOpen;
    protected boolean isFilteringCraftable;

    public void copyFrom(RecipeBook that)
    {
        this.recipes.clear();
        this.newRecipes.clear();
        this.recipes.or(that.recipes);
        this.newRecipes.or(that.newRecipes);
    }

    public void unlock(IRecipe recipe)
    {
        if (!recipe.isDynamic())
        {
            this.recipes.set(func_194075_d(recipe));
        }
    }

    public boolean isUnlocked(@Nullable IRecipe recipe)
    {
        return this.recipes.get(func_194075_d(recipe));
    }

    public void lock(IRecipe recipe)
    {
        int i = func_194075_d(recipe);
        this.recipes.clear(i);
        this.newRecipes.clear(i);
    }

    protected static int func_194075_d(@Nullable IRecipe p_194075_0_)
    {
        return CraftingManager.field_193380_a.getId(p_194075_0_);
    }

    public boolean isNew(IRecipe recipe)
    {
        return this.newRecipes.get(func_194075_d(recipe));
    }

    public void markSeen(IRecipe recipe)
    {
        this.newRecipes.clear(func_194075_d(recipe));
    }

    public void markNew(IRecipe recipe)
    {
        this.newRecipes.set(func_194075_d(recipe));
    }

    public boolean isGuiOpen()
    {
        return this.isGuiOpen;
    }

    public void setGuiOpen(boolean open)
    {
        this.isGuiOpen = open;
    }

    public boolean isFilteringCraftable()
    {
        return this.isFilteringCraftable;
    }

    public void setFilteringCraftable(boolean shouldFilter)
    {
        this.isFilteringCraftable = shouldFilter;
    }
}

package net.minecraft.client.util;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.ItemStack;

public class SearchTreeManager implements IResourceManagerReloadListener
{
    public static final SearchTreeManager.Key<ItemStack> field_194011_a = new SearchTreeManager.Key<ItemStack>();
    public static final SearchTreeManager.Key<RecipeList> RECIPES = new SearchTreeManager.Key<RecipeList>();
    private final Map < SearchTreeManager.Key<?>, SearchTree<? >> trees = Maps. < SearchTreeManager.Key<?>, SearchTree<? >> newHashMap();

    public void func_110549_a(IResourceManager p_110549_1_)
    {
        for (SearchTree<?> searchtree : this.trees.values())
        {
            searchtree.recalculate();
        }
    }

    public <T> void func_194009_a(SearchTreeManager.Key<T> p_194009_1_, SearchTree<T> p_194009_2_)
    {
        this.trees.put(p_194009_1_, p_194009_2_);
    }

    public <T> ISearchTree<T> func_194010_a(SearchTreeManager.Key<T> p_194010_1_)
    {
        return (ISearchTree)this.trees.get(p_194010_1_);
    }

    public static class Key<T>
    {
    }
}

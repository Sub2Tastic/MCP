package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class RecipeUnlockedTrigger implements ICriterionTrigger<RecipeUnlockedTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("recipe_unlocked");
    private final Map<PlayerAdvancements, RecipeUnlockedTrigger.Listeners> field_192228_b = Maps.<PlayerAdvancements, RecipeUnlockedTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance> listener)
    {
        RecipeUnlockedTrigger.Listeners recipeunlockedtrigger$listeners = this.field_192228_b.get(playerAdvancementsIn);

        if (recipeunlockedtrigger$listeners == null)
        {
            recipeunlockedtrigger$listeners = new RecipeUnlockedTrigger.Listeners(playerAdvancementsIn);
            this.field_192228_b.put(playerAdvancementsIn, recipeunlockedtrigger$listeners);
        }

        recipeunlockedtrigger$listeners.func_192528_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance> listener)
    {
        RecipeUnlockedTrigger.Listeners recipeunlockedtrigger$listeners = this.field_192228_b.get(playerAdvancementsIn);

        if (recipeunlockedtrigger$listeners != null)
        {
            recipeunlockedtrigger$listeners.func_192525_b(listener);

            if (recipeunlockedtrigger$listeners.func_192527_a())
            {
                this.field_192228_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192228_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public RecipeUnlockedTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "recipe"));
        IRecipe irecipe = CraftingManager.func_193373_a(resourcelocation);

        if (irecipe == null)
        {
            throw new JsonSyntaxException("Unknown recipe '" + resourcelocation + "'");
        }
        else
        {
            return new RecipeUnlockedTrigger.Instance(irecipe);
        }
    }

    public void trigger(EntityPlayerMP player, IRecipe recipe)
    {
        RecipeUnlockedTrigger.Listeners recipeunlockedtrigger$listeners = this.field_192228_b.get(player.getAdvancements());

        if (recipeunlockedtrigger$listeners != null)
        {
            recipeunlockedtrigger$listeners.func_193493_a(recipe);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final IRecipe field_192282_a;

        public Instance(IRecipe p_i47526_1_)
        {
            super(RecipeUnlockedTrigger.ID);
            this.field_192282_a = p_i47526_1_;
        }

        public boolean test(IRecipe recipe)
        {
            return this.field_192282_a == recipe;
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192529_a;
        private final Set<ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance>> field_192530_b = Sets.<ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47397_1_)
        {
            this.field_192529_a = p_i47397_1_;
        }

        public boolean func_192527_a()
        {
            return this.field_192530_b.isEmpty();
        }

        public void func_192528_a(ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance> p_192528_1_)
        {
            this.field_192530_b.add(p_192528_1_);
        }

        public void func_192525_b(ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance> p_192525_1_)
        {
            this.field_192530_b.remove(p_192525_1_);
        }

        public void func_193493_a(IRecipe p_193493_1_)
        {
            List<ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance> listener : this.field_192530_b)
            {
                if (((RecipeUnlockedTrigger.Instance)listener.getCriterionInstance()).test(p_193493_1_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<RecipeUnlockedTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192529_a);
                }
            }
        }
    }
}

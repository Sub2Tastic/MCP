package net.minecraft.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class ItemPredicate
{
    public static final ItemPredicate ANY = new ItemPredicate();
    private final Item item;
    private final Integer field_192497_c;
    private final MinMaxBounds count;
    private final MinMaxBounds durability;
    private final EnchantmentPredicate[] enchantments;
    private final PotionType potion;
    private final NBTPredicate nbt;

    public ItemPredicate()
    {
        this.item = null;
        this.field_192497_c = null;
        this.potion = null;
        this.count = MinMaxBounds.field_192516_a;
        this.durability = MinMaxBounds.field_192516_a;
        this.enchantments = new EnchantmentPredicate[0];
        this.nbt = NBTPredicate.ANY;
    }

    public ItemPredicate(@Nullable Item p_i47540_1_, @Nullable Integer p_i47540_2_, MinMaxBounds p_i47540_3_, MinMaxBounds p_i47540_4_, EnchantmentPredicate[] p_i47540_5_, @Nullable PotionType p_i47540_6_, NBTPredicate p_i47540_7_)
    {
        this.item = p_i47540_1_;
        this.field_192497_c = p_i47540_2_;
        this.count = p_i47540_3_;
        this.durability = p_i47540_4_;
        this.enchantments = p_i47540_5_;
        this.potion = p_i47540_6_;
        this.nbt = p_i47540_7_;
    }

    public boolean test(ItemStack item)
    {
        if (this.item != null && item.getItem() != this.item)
        {
            return false;
        }
        else if (this.field_192497_c != null && item.func_77960_j() != this.field_192497_c.intValue())
        {
            return false;
        }
        else if (!this.count.func_192514_a((float)item.getCount()))
        {
            return false;
        }
        else if (this.durability != MinMaxBounds.field_192516_a && !item.isDamageable())
        {
            return false;
        }
        else if (!this.durability.func_192514_a((float)(item.getMaxDamage() - item.getDamage())))
        {
            return false;
        }
        else if (!this.nbt.test(item))
        {
            return false;
        }
        else
        {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(item);

            for (int i = 0; i < this.enchantments.length; ++i)
            {
                if (!this.enchantments[i].test(map))
                {
                    return false;
                }
            }

            PotionType potiontype = PotionUtils.getPotionFromItem(item);

            if (this.potion != null && this.potion != potiontype)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }

    public static ItemPredicate deserialize(@Nullable JsonElement element)
    {
        if (element != null && !element.isJsonNull())
        {
            JsonObject jsonobject = JsonUtils.getJsonObject(element, "item");
            MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(jsonobject.get("count"));
            MinMaxBounds minmaxbounds1 = MinMaxBounds.func_192515_a(jsonobject.get("durability"));
            Integer integer = jsonobject.has("data") ? JsonUtils.getInt(jsonobject, "data") : null;
            NBTPredicate nbtpredicate = NBTPredicate.deserialize(jsonobject.get("nbt"));
            Item item = null;

            if (jsonobject.has("item"))
            {
                ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "item"));
                item = Item.field_150901_e.getOrDefault(resourcelocation);

                if (item == null)
                {
                    throw new JsonSyntaxException("Unknown item id '" + resourcelocation + "'");
                }
            }

            EnchantmentPredicate[] aenchantmentpredicate = EnchantmentPredicate.deserializeArray(jsonobject.get("enchantments"));
            PotionType potiontype = null;

            if (jsonobject.has("potion"))
            {
                ResourceLocation resourcelocation1 = new ResourceLocation(JsonUtils.getString(jsonobject, "potion"));

                if (!PotionType.field_185176_a.func_148741_d(resourcelocation1))
                {
                    throw new JsonSyntaxException("Unknown potion '" + resourcelocation1 + "'");
                }

                potiontype = PotionType.field_185176_a.getOrDefault(resourcelocation1);
            }

            return new ItemPredicate(item, integer, minmaxbounds, minmaxbounds1, aenchantmentpredicate, potiontype, nbtpredicate);
        }
        else
        {
            return ANY;
        }
    }

    public static ItemPredicate[] deserializeArray(@Nullable JsonElement element)
    {
        if (element != null && !element.isJsonNull())
        {
            JsonArray jsonarray = JsonUtils.getJsonArray(element, "items");
            ItemPredicate[] aitempredicate = new ItemPredicate[jsonarray.size()];

            for (int i = 0; i < aitempredicate.length; ++i)
            {
                aitempredicate[i] = deserialize(jsonarray.get(i));
            }

            return aitempredicate;
        }
        else
        {
            return new ItemPredicate[0];
        }
    }
}

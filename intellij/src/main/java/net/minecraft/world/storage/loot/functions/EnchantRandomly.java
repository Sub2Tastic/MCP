package net.minecraft.world.storage.loot.functions;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnchantRandomly extends LootFunction
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final List<Enchantment> enchantments;

    public EnchantRandomly(LootCondition[] p_i46628_1_, @Nullable List<Enchantment> p_i46628_2_)
    {
        super(p_i46628_1_);
        this.enchantments = p_i46628_2_ == null ? Collections.emptyList() : p_i46628_2_;
    }

    public ItemStack func_186553_a(ItemStack p_186553_1_, Random p_186553_2_, LootContext p_186553_3_)
    {
        Enchantment enchantment;

        if (this.enchantments.isEmpty())
        {
            List<Enchantment> list = Lists.<Enchantment>newArrayList();

            for (Enchantment enchantment1 : Enchantment.field_185264_b)
            {
                if (p_186553_1_.getItem() == Items.BOOK || enchantment1.canApply(p_186553_1_))
                {
                    list.add(enchantment1);
                }
            }

            if (list.isEmpty())
            {
                LOGGER.warn("Couldn't find a compatible enchantment for {}", (Object)p_186553_1_);
                return p_186553_1_;
            }

            enchantment = list.get(p_186553_2_.nextInt(list.size()));
        }
        else
        {
            enchantment = this.enchantments.get(p_186553_2_.nextInt(this.enchantments.size()));
        }

        int i = MathHelper.nextInt(p_186553_2_, enchantment.getMinLevel(), enchantment.getMaxLevel());

        if (p_186553_1_.getItem() == Items.BOOK)
        {
            p_186553_1_ = new ItemStack(Items.ENCHANTED_BOOK);
            ItemEnchantedBook.addEnchantment(p_186553_1_, new EnchantmentData(enchantment, i));
        }
        else
        {
            p_186553_1_.addEnchantment(enchantment, i);
        }

        return p_186553_1_;
    }

    public static class Serializer extends LootFunction.Serializer<EnchantRandomly>
    {
        public Serializer()
        {
            super(new ResourceLocation("enchant_randomly"), EnchantRandomly.class);
        }

        public void serialize(JsonObject object, EnchantRandomly functionClazz, JsonSerializationContext serializationContext)
        {
            if (!functionClazz.enchantments.isEmpty())
            {
                JsonArray jsonarray = new JsonArray();

                for (Enchantment enchantment : functionClazz.enchantments)
                {
                    ResourceLocation resourcelocation = Enchantment.field_185264_b.getKey(enchantment);

                    if (resourcelocation == null)
                    {
                        throw new IllegalArgumentException("Don't know how to serialize enchantment " + enchantment);
                    }

                    jsonarray.add(new JsonPrimitive(resourcelocation.toString()));
                }

                object.add("enchantments", jsonarray);
            }
        }

        public EnchantRandomly deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn)
        {
            List<Enchantment> list = Lists.<Enchantment>newArrayList();

            if (object.has("enchantments"))
            {
                for (JsonElement jsonelement : JsonUtils.getJsonArray(object, "enchantments"))
                {
                    String s = JsonUtils.getString(jsonelement, "enchantment");
                    Enchantment enchantment = Enchantment.field_185264_b.getOrDefault(new ResourceLocation(s));

                    if (enchantment == null)
                    {
                        throw new JsonSyntaxException("Unknown enchantment '" + s + "'");
                    }

                    list.add(enchantment);
                }
            }

            return new EnchantRandomly(conditionsIn, list);
        }
    }
}

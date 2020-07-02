package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootingEnchantBonus extends LootFunction
{
    private final RandomValueRange count;
    private final int limit;

    public LootingEnchantBonus(LootCondition[] conditions, RandomValueRange countIn, int limitIn)
    {
        super(conditions);
        this.count = countIn;
        this.limit = limitIn;
    }

    public ItemStack func_186553_a(ItemStack p_186553_1_, Random p_186553_2_, LootContext p_186553_3_)
    {
        Entity entity = p_186553_3_.func_186492_c();

        if (entity instanceof EntityLivingBase)
        {
            int i = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity);

            if (i == 0)
            {
                return p_186553_1_;
            }

            float f = (float)i * this.count.generateFloat(p_186553_2_);
            p_186553_1_.grow(Math.round(f));

            if (this.limit != 0 && p_186553_1_.getCount() > this.limit)
            {
                p_186553_1_.setCount(this.limit);
            }
        }

        return p_186553_1_;
    }

    public static class Serializer extends LootFunction.Serializer<LootingEnchantBonus>
    {
        protected Serializer()
        {
            super(new ResourceLocation("looting_enchant"), LootingEnchantBonus.class);
        }

        public void serialize(JsonObject object, LootingEnchantBonus functionClazz, JsonSerializationContext serializationContext)
        {
            object.add("count", serializationContext.serialize(functionClazz.count));

            if (functionClazz.limit > 0)
            {
                object.add("limit", serializationContext.serialize(Integer.valueOf(functionClazz.limit)));
            }
        }

        public LootingEnchantBonus deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn)
        {
            int i = JsonUtils.getInt(object, "limit", 0);
            return new LootingEnchantBonus(conditionsIn, (RandomValueRange)JsonUtils.deserializeClass(object, "count", deserializationContext, RandomValueRange.class), i);
        }
    }
}

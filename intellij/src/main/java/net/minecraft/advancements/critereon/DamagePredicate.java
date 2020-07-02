package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.JsonUtils;

public class DamagePredicate
{
    public static DamagePredicate ANY = new DamagePredicate();
    private final MinMaxBounds dealt;
    private final MinMaxBounds taken;
    private final EntityPredicate sourceEntity;
    private final Boolean blocked;
    private final DamageSourcePredicate type;

    public DamagePredicate()
    {
        this.dealt = MinMaxBounds.field_192516_a;
        this.taken = MinMaxBounds.field_192516_a;
        this.sourceEntity = EntityPredicate.ANY;
        this.blocked = null;
        this.type = DamageSourcePredicate.ANY;
    }

    public DamagePredicate(MinMaxBounds p_i47464_1_, MinMaxBounds p_i47464_2_, EntityPredicate p_i47464_3_, @Nullable Boolean p_i47464_4_, DamageSourcePredicate p_i47464_5_)
    {
        this.dealt = p_i47464_1_;
        this.taken = p_i47464_2_;
        this.sourceEntity = p_i47464_3_;
        this.blocked = p_i47464_4_;
        this.type = p_i47464_5_;
    }

    public boolean test(EntityPlayerMP player, DamageSource source, float dealt, float taken, boolean blocked)
    {
        if (this == ANY)
        {
            return true;
        }
        else if (!this.dealt.func_192514_a(dealt))
        {
            return false;
        }
        else if (!this.taken.func_192514_a(taken))
        {
            return false;
        }
        else if (!this.sourceEntity.test(player, source.getTrueSource()))
        {
            return false;
        }
        else if (this.blocked != null && this.blocked.booleanValue() != blocked)
        {
            return false;
        }
        else
        {
            return this.type.test(player, source);
        }
    }

    public static DamagePredicate deserialize(@Nullable JsonElement element)
    {
        if (element != null && !element.isJsonNull())
        {
            JsonObject jsonobject = JsonUtils.getJsonObject(element, "damage");
            MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(jsonobject.get("dealt"));
            MinMaxBounds minmaxbounds1 = MinMaxBounds.func_192515_a(jsonobject.get("taken"));
            Boolean obool = jsonobject.has("blocked") ? JsonUtils.getBoolean(jsonobject, "blocked") : null;
            EntityPredicate entitypredicate = EntityPredicate.deserialize(jsonobject.get("source_entity"));
            DamageSourcePredicate damagesourcepredicate = DamageSourcePredicate.deserialize(jsonobject.get("type"));
            return new DamagePredicate(minmaxbounds, minmaxbounds1, entitypredicate, obool, damagesourcepredicate);
        }
        else
        {
            return ANY;
        }
    }
}

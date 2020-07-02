package net.minecraft.world.storage.loot;

import com.google.common.collect.Sets;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;

public class LootContext
{
    private final float luck;
    private final WorldServer world;
    private final LootTableManager lootTableManager;
    @Nullable
    private final Entity field_186501_d;
    @Nullable
    private final EntityPlayer field_186502_e;
    @Nullable
    private final DamageSource field_186503_f;
    private final Set<LootTable> lootTables = Sets.<LootTable>newLinkedHashSet();

    public LootContext(float p_i46640_1_, WorldServer p_i46640_2_, LootTableManager p_i46640_3_, @Nullable Entity p_i46640_4_, @Nullable EntityPlayer p_i46640_5_, @Nullable DamageSource p_i46640_6_)
    {
        this.luck = p_i46640_1_;
        this.world = p_i46640_2_;
        this.lootTableManager = p_i46640_3_;
        this.field_186501_d = p_i46640_4_;
        this.field_186502_e = p_i46640_5_;
        this.field_186503_f = p_i46640_6_;
    }

    @Nullable
    public Entity func_186493_a()
    {
        return this.field_186501_d;
    }

    @Nullable
    public Entity func_186495_b()
    {
        return this.field_186502_e;
    }

    @Nullable
    public Entity func_186492_c()
    {
        return this.field_186503_f == null ? null : this.field_186503_f.getTrueSource();
    }

    public boolean addLootTable(LootTable lootTableIn)
    {
        return this.lootTables.add(lootTableIn);
    }

    public void removeLootTable(LootTable lootTableIn)
    {
        this.lootTables.remove(lootTableIn);
    }

    public LootTableManager func_186497_e()
    {
        return this.lootTableManager;
    }

    public float getLuck()
    {
        return this.luck;
    }

    @Nullable
    public Entity func_186494_a(LootContext.EntityTarget p_186494_1_)
    {
        switch (p_186494_1_)
        {
            case THIS:
                return this.func_186493_a();

            case KILLER:
                return this.func_186492_c();

            case KILLER_PLAYER:
                return this.func_186495_b();

            default:
                return null;
        }
    }

    public static class Builder
    {
        private final WorldServer world;
        private float luck;
        private Entity field_186476_c;
        private EntityPlayer field_186477_d;
        private DamageSource field_186478_e;

        public Builder(WorldServer worldIn)
        {
            this.world = worldIn;
        }

        public LootContext.Builder withLuck(float luckIn)
        {
            this.luck = luckIn;
            return this;
        }

        public LootContext.Builder func_186472_a(Entity p_186472_1_)
        {
            this.field_186476_c = p_186472_1_;
            return this;
        }

        public LootContext.Builder func_186470_a(EntityPlayer p_186470_1_)
        {
            this.field_186477_d = p_186470_1_;
            return this;
        }

        public LootContext.Builder func_186473_a(DamageSource p_186473_1_)
        {
            this.field_186478_e = p_186473_1_;
            return this;
        }

        public LootContext func_186471_a()
        {
            return new LootContext(this.luck, this.world, this.world.func_184146_ak(), this.field_186476_c, this.field_186477_d, this.field_186478_e);
        }
    }

    public static enum EntityTarget
    {
        THIS("this"),
        KILLER("killer"),
        KILLER_PLAYER("killer_player");

        private final String targetType;

        private EntityTarget(String p_i46992_3_)
        {
            this.targetType = p_i46992_3_;
        }

        public static LootContext.EntityTarget fromString(String type)
        {
            for (LootContext.EntityTarget lootcontext$entitytarget : values())
            {
                if (lootcontext$entitytarget.targetType.equals(type))
                {
                    return lootcontext$entitytarget;
                }
            }

            throw new IllegalArgumentException("Invalid entity target " + type);
        }

        public static class Serializer extends TypeAdapter<LootContext.EntityTarget> {
            public void write(JsonWriter p_write_1_, LootContext.EntityTarget p_write_2_) throws IOException {
                p_write_1_.value(p_write_2_.targetType);
            }

            public LootContext.EntityTarget read(JsonReader p_read_1_) throws IOException {
                return LootContext.EntityTarget.fromString(p_read_1_.nextString());
            }
        }
    }
}

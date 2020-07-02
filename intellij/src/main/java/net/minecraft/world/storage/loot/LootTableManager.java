package net.minecraft.world.storage.loot;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LootTableManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON_INSTANCE = (new GsonBuilder()).registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer()).registerTypeAdapter(LootPool.class, new LootPool.Serializer()).registerTypeAdapter(LootTable.class, new LootTable.Serializer()).registerTypeHierarchyAdapter(LootEntry.class, new LootEntry.Serializer()).registerTypeHierarchyAdapter(LootFunction.class, new LootFunctionManager.Serializer()).registerTypeHierarchyAdapter(LootCondition.class, new LootConditionManager.Serializer()).registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer()).create();
    private final LoadingCache<ResourceLocation, LootTable> registeredLootTables = CacheBuilder.newBuilder().<ResourceLocation, LootTable>build(new LootTableManager.Loader());
    private final File field_186528_d;

    public LootTableManager(@Nullable File p_i46632_1_)
    {
        this.field_186528_d = p_i46632_1_;
        this.func_186522_a();
    }

    public LootTable getLootTableFromLocation(ResourceLocation ressources)
    {
        return this.registeredLootTables.getUnchecked(ressources);
    }

    public void func_186522_a()
    {
        this.registeredLootTables.invalidateAll();

        for (ResourceLocation resourcelocation : LootTableList.func_186374_a())
        {
            this.getLootTableFromLocation(resourcelocation);
        }
    }

    class Loader extends CacheLoader<ResourceLocation, LootTable>
    {
        private Loader()
        {
        }

        public LootTable load(ResourceLocation p_load_1_) throws Exception
        {
            if (p_load_1_.getPath().contains("."))
            {
                LootTableManager.LOGGER.debug("Invalid loot table name '{}' (can't contain periods)", (Object)p_load_1_);
                return LootTable.EMPTY_LOOT_TABLE;
            }
            else
            {
                LootTable loottable = this.func_186517_b(p_load_1_);

                if (loottable == null)
                {
                    loottable = this.func_186518_c(p_load_1_);
                }

                if (loottable == null)
                {
                    loottable = LootTable.EMPTY_LOOT_TABLE;
                    LootTableManager.LOGGER.warn("Couldn't find resource table {}", (Object)p_load_1_);
                }

                return loottable;
            }
        }

        @Nullable
        private LootTable func_186517_b(ResourceLocation p_186517_1_)
        {
            if (LootTableManager.this.field_186528_d == null)
            {
                return null;
            }
            else
            {
                File file1 = new File(new File(LootTableManager.this.field_186528_d, p_186517_1_.getNamespace()), p_186517_1_.getPath() + ".json");

                if (file1.exists())
                {
                    if (file1.isFile())
                    {
                        String s;

                        try
                        {
                            s = Files.toString(file1, StandardCharsets.UTF_8);
                        }
                        catch (IOException ioexception)
                        {
                            LootTableManager.LOGGER.warn("Couldn't load loot table {} from {}", p_186517_1_, file1, ioexception);
                            return LootTable.EMPTY_LOOT_TABLE;
                        }

                        try
                        {
                            return (LootTable)JsonUtils.fromJson(LootTableManager.GSON_INSTANCE, s, LootTable.class);
                        }
                        catch (IllegalArgumentException | JsonParseException jsonparseexception)
                        {
                            LootTableManager.LOGGER.error("Couldn't load loot table {} from {}", p_186517_1_, file1, jsonparseexception);
                            return LootTable.EMPTY_LOOT_TABLE;
                        }
                    }
                    else
                    {
                        LootTableManager.LOGGER.warn("Expected to find loot table {} at {} but it was a folder.", p_186517_1_, file1);
                        return LootTable.EMPTY_LOOT_TABLE;
                    }
                }
                else
                {
                    return null;
                }
            }
        }

        @Nullable
        private LootTable func_186518_c(ResourceLocation p_186518_1_)
        {
            URL url = LootTableManager.class.getResource("/assets/" + p_186518_1_.getNamespace() + "/loot_tables/" + p_186518_1_.getPath() + ".json");

            if (url != null)
            {
                String s;

                try
                {
                    s = Resources.toString(url, StandardCharsets.UTF_8);
                }
                catch (IOException ioexception)
                {
                    LootTableManager.LOGGER.warn("Couldn't load loot table {} from {}", p_186518_1_, url, ioexception);
                    return LootTable.EMPTY_LOOT_TABLE;
                }

                try
                {
                    return (LootTable)JsonUtils.fromJson(LootTableManager.GSON_INSTANCE, s, LootTable.class);
                }
                catch (JsonParseException jsonparseexception)
                {
                    LootTableManager.LOGGER.error("Couldn't load loot table {} from {}", p_186518_1_, url, jsonparseexception);
                    return LootTable.EMPTY_LOOT_TABLE;
                }
            }
            else
            {
                return null;
            }
        }
    }
}

package net.minecraft.world.storage.loot;

import com.google.common.collect.Sets;
import java.io.File;
import java.util.Collections;
import java.util.Set;
import net.minecraft.util.ResourceLocation;

public class LootTableList
{
    private static final Set<ResourceLocation> LOOT_TABLES = Sets.<ResourceLocation>newHashSet();
    private static final Set<ResourceLocation> READ_ONLY_LOOT_TABLES = Collections.<ResourceLocation>unmodifiableSet(LOOT_TABLES);
    public static final ResourceLocation EMPTY = register("empty");
    public static final ResourceLocation CHESTS_SPAWN_BONUS_CHEST = register("chests/spawn_bonus_chest");
    public static final ResourceLocation CHESTS_END_CITY_TREASURE = register("chests/end_city_treasure");
    public static final ResourceLocation CHESTS_SIMPLE_DUNGEON = register("chests/simple_dungeon");
    public static final ResourceLocation field_186423_e = register("chests/village_blacksmith");
    public static final ResourceLocation CHESTS_ABANDONED_MINESHAFT = register("chests/abandoned_mineshaft");
    public static final ResourceLocation CHESTS_NETHER_BRIDGE = register("chests/nether_bridge");
    public static final ResourceLocation CHESTS_STRONGHOLD_LIBRARY = register("chests/stronghold_library");
    public static final ResourceLocation CHESTS_STRONGHOLD_CROSSING = register("chests/stronghold_crossing");
    public static final ResourceLocation CHESTS_STRONGHOLD_CORRIDOR = register("chests/stronghold_corridor");
    public static final ResourceLocation CHESTS_DESERT_PYRAMID = register("chests/desert_pyramid");
    public static final ResourceLocation CHESTS_JUNGLE_TEMPLE = register("chests/jungle_temple");
    public static final ResourceLocation CHESTS_JUNGLE_TEMPLE_DISPENSER = register("chests/jungle_temple_dispenser");
    public static final ResourceLocation CHESTS_IGLOO_CHEST = register("chests/igloo_chest");
    public static final ResourceLocation CHESTS_WOODLAND_MANSION = register("chests/woodland_mansion");
    public static final ResourceLocation field_186432_n = register("entities/witch");
    public static final ResourceLocation field_186433_o = register("entities/blaze");
    public static final ResourceLocation field_186434_p = register("entities/creeper");
    public static final ResourceLocation field_186435_q = register("entities/spider");
    public static final ResourceLocation field_186436_r = register("entities/cave_spider");
    public static final ResourceLocation field_186437_s = register("entities/giant");
    public static final ResourceLocation field_186438_t = register("entities/silverfish");
    public static final ResourceLocation field_186439_u = register("entities/enderman");
    public static final ResourceLocation field_186440_v = register("entities/guardian");
    public static final ResourceLocation field_186441_w = register("entities/elder_guardian");
    public static final ResourceLocation field_186442_x = register("entities/shulker");
    public static final ResourceLocation field_186443_y = register("entities/iron_golem");
    public static final ResourceLocation field_186444_z = register("entities/snowman");
    public static final ResourceLocation field_186393_A = register("entities/rabbit");
    public static final ResourceLocation field_186394_B = register("entities/chicken");
    public static final ResourceLocation field_186395_C = register("entities/pig");
    public static final ResourceLocation field_189969_E = register("entities/polar_bear");
    public static final ResourceLocation field_186396_D = register("entities/horse");
    public static final ResourceLocation field_191190_H = register("entities/donkey");
    public static final ResourceLocation field_191191_I = register("entities/mule");
    public static final ResourceLocation field_186397_E = register("entities/zombie_horse");
    public static final ResourceLocation field_186398_F = register("entities/skeleton_horse");
    public static final ResourceLocation field_186399_G = register("entities/cow");
    public static final ResourceLocation field_186400_H = register("entities/mushroom_cow");
    public static final ResourceLocation field_186401_I = register("entities/wolf");
    public static final ResourceLocation field_186402_J = register("entities/ocelot");
    public static final ResourceLocation field_186403_K = register("entities/sheep");
    public static final ResourceLocation ENTITIES_SHEEP_WHITE = register("entities/sheep/white");
    public static final ResourceLocation ENTITIES_SHEEP_ORANGE = register("entities/sheep/orange");
    public static final ResourceLocation ENTITIES_SHEEP_MAGENTA = register("entities/sheep/magenta");
    public static final ResourceLocation ENTITIES_SHEEP_LIGHT_BLUE = register("entities/sheep/light_blue");
    public static final ResourceLocation ENTITIES_SHEEP_YELLOW = register("entities/sheep/yellow");
    public static final ResourceLocation ENTITIES_SHEEP_LIME = register("entities/sheep/lime");
    public static final ResourceLocation ENTITIES_SHEEP_PINK = register("entities/sheep/pink");
    public static final ResourceLocation ENTITIES_SHEEP_GRAY = register("entities/sheep/gray");
    public static final ResourceLocation field_186412_T = register("entities/sheep/silver");
    public static final ResourceLocation ENTITIES_SHEEP_CYAN = register("entities/sheep/cyan");
    public static final ResourceLocation ENTITIES_SHEEP_PURPLE = register("entities/sheep/purple");
    public static final ResourceLocation ENTITIES_SHEEP_BLUE = register("entities/sheep/blue");
    public static final ResourceLocation ENTITIES_SHEEP_BROWN = register("entities/sheep/brown");
    public static final ResourceLocation ENTITIES_SHEEP_GREEN = register("entities/sheep/green");
    public static final ResourceLocation ENTITIES_SHEEP_RED = register("entities/sheep/red");
    public static final ResourceLocation ENTITIES_SHEEP_BLACK = register("entities/sheep/black");
    public static final ResourceLocation field_186377_ab = register("entities/bat");
    public static final ResourceLocation field_186378_ac = register("entities/slime");
    public static final ResourceLocation field_186379_ad = register("entities/magma_cube");
    public static final ResourceLocation field_186380_ae = register("entities/ghast");
    public static final ResourceLocation field_186381_af = register("entities/squid");
    public static final ResourceLocation field_186382_ag = register("entities/endermite");
    public static final ResourceLocation field_186383_ah = register("entities/zombie");
    public static final ResourceLocation field_186384_ai = register("entities/zombie_pigman");
    public static final ResourceLocation field_186385_aj = register("entities/skeleton");
    public static final ResourceLocation field_186386_ak = register("entities/wither_skeleton");
    public static final ResourceLocation field_189968_an = register("entities/stray");
    public static final ResourceLocation field_191182_ar = register("entities/husk");
    public static final ResourceLocation field_191183_as = register("entities/zombie_villager");
    public static final ResourceLocation field_191184_at = register("entities/villager");
    public static final ResourceLocation field_191185_au = register("entities/evocation_illager");
    public static final ResourceLocation field_191186_av = register("entities/vindication_illager");
    public static final ResourceLocation field_191187_aw = register("entities/llama");
    public static final ResourceLocation field_192561_ax = register("entities/parrot");
    public static final ResourceLocation field_191188_ax = register("entities/vex");
    public static final ResourceLocation field_191189_ay = register("entities/ender_dragon");
    public static final ResourceLocation GAMEPLAY_FISHING = register("gameplay/fishing");
    public static final ResourceLocation GAMEPLAY_FISHING_JUNK = register("gameplay/fishing/junk");
    public static final ResourceLocation GAMEPLAY_FISHING_TREASURE = register("gameplay/fishing/treasure");
    public static final ResourceLocation GAMEPLAY_FISHING_FISH = register("gameplay/fishing/fish");

    private static ResourceLocation register(String id)
    {
        return register(new ResourceLocation("minecraft", id));
    }

    public static ResourceLocation register(ResourceLocation id)
    {
        if (LOOT_TABLES.add(id))
        {
            return id;
        }
        else
        {
            throw new IllegalArgumentException(id + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceLocation> func_186374_a()
    {
        return READ_ONLY_LOOT_TABLES;
    }

    public static boolean func_193579_b()
    {
        LootTableManager loottablemanager = new LootTableManager((File)null);

        for (ResourceLocation resourcelocation : READ_ONLY_LOOT_TABLES)
        {
            if (loottablemanager.getLootTableFromLocation(resourcelocation) == LootTable.EMPTY_LOOT_TABLE)
            {
                return false;
            }
        }

        return true;
    }
}

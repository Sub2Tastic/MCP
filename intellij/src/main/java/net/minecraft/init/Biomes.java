package net.minecraft.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public abstract class Biomes
{
    public static final Biome OCEAN;
    public static final Biome DEFAULT;
    public static final Biome PLAINS;
    public static final Biome DESERT;
    public static final Biome MOUNTAINS;
    public static final Biome FOREST;
    public static final Biome TAIGA;
    public static final Biome SWAMP;
    public static final Biome RIVER;
    public static final Biome NETHER;
    public static final Biome THE_END;
    public static final Biome FROZEN_OCEAN;
    public static final Biome FROZEN_RIVER;
    public static final Biome SNOWY_TUNDRA;
    public static final Biome SNOWY_MOUNTAINS;
    public static final Biome MUSHROOM_FIELDS;
    public static final Biome MUSHROOM_FIELD_SHORE;
    public static final Biome BEACH;
    public static final Biome DESERT_HILLS;
    public static final Biome WOODED_HILLS;
    public static final Biome TAIGA_HILLS;
    public static final Biome MOUNTAIN_EDGE;
    public static final Biome JUNGLE;
    public static final Biome JUNGLE_HILLS;
    public static final Biome JUNGLE_EDGE;
    public static final Biome DEEP_OCEAN;
    public static final Biome STONE_SHORE;
    public static final Biome SNOWY_BEACH;
    public static final Biome BIRCH_FOREST;
    public static final Biome BIRCH_FOREST_HILLS;
    public static final Biome DARK_FOREST;
    public static final Biome SNOWY_TAIGA;
    public static final Biome SNOWY_TAIGA_HILLS;
    public static final Biome GIANT_TREE_TAIGA;
    public static final Biome GIANT_TREE_TAIGA_HILLS;
    public static final Biome WOODED_MOUNTAINS;
    public static final Biome SAVANNA;
    public static final Biome SAVANNA_PLATEAU;
    public static final Biome BADLANDS;
    public static final Biome WOODED_BADLANDS_PLATEAU;
    public static final Biome BADLANDS_PLATEAU;
    public static final Biome THE_VOID;
    public static final Biome SUNFLOWER_PLAINS;
    public static final Biome DESERT_LAKES;
    public static final Biome GRAVELLY_MOUNTAINS;
    public static final Biome FLOWER_FOREST;
    public static final Biome TAIGA_MOUNTAINS;
    public static final Biome SWAMP_HILLS;
    public static final Biome ICE_SPIKES;
    public static final Biome MODIFIED_JUNGLE;
    public static final Biome MODIFIED_JUNGLE_EDGE;
    public static final Biome TALL_BIRCH_FOREST;
    public static final Biome TALL_BIRCH_HILLS;
    public static final Biome DARK_FOREST_HILLS;
    public static final Biome SNOWY_TAIGA_MOUNTAINS;
    public static final Biome GIANT_SPRUCE_TAIGA;
    public static final Biome GIANT_SPRUCE_TAIGA_HILLS;
    public static final Biome MODIFIED_GRAVELLY_MOUNTAINS;
    public static final Biome SHATTERED_SAVANNA;
    public static final Biome SHATTERED_SAVANNA_PLATEAU;
    public static final Biome ERODED_BADLANDS;
    public static final Biome MODIFIED_WOODED_BADLANDS_PLATEAU;
    public static final Biome MODIFIED_BADLANDS_PLATEAU;

    private static Biome func_185428_a(String p_185428_0_)
    {
        Biome biome = Biome.field_185377_q.getOrDefault(new ResourceLocation(p_185428_0_));

        if (biome == null)
        {
            throw new IllegalStateException("Invalid Biome requested: " + p_185428_0_);
        }
        else
        {
            return biome;
        }
    }

    static
    {
        if (!Bootstrap.func_179869_a())
        {
            throw new RuntimeException("Accessed Biomes before Bootstrap!");
        }
        else
        {
            OCEAN = func_185428_a("ocean");
            DEFAULT = OCEAN;
            PLAINS = func_185428_a("plains");
            DESERT = func_185428_a("desert");
            MOUNTAINS = func_185428_a("extreme_hills");
            FOREST = func_185428_a("forest");
            TAIGA = func_185428_a("taiga");
            SWAMP = func_185428_a("swampland");
            RIVER = func_185428_a("river");
            NETHER = func_185428_a("hell");
            THE_END = func_185428_a("sky");
            FROZEN_OCEAN = func_185428_a("frozen_ocean");
            FROZEN_RIVER = func_185428_a("frozen_river");
            SNOWY_TUNDRA = func_185428_a("ice_flats");
            SNOWY_MOUNTAINS = func_185428_a("ice_mountains");
            MUSHROOM_FIELDS = func_185428_a("mushroom_island");
            MUSHROOM_FIELD_SHORE = func_185428_a("mushroom_island_shore");
            BEACH = func_185428_a("beaches");
            DESERT_HILLS = func_185428_a("desert_hills");
            WOODED_HILLS = func_185428_a("forest_hills");
            TAIGA_HILLS = func_185428_a("taiga_hills");
            MOUNTAIN_EDGE = func_185428_a("smaller_extreme_hills");
            JUNGLE = func_185428_a("jungle");
            JUNGLE_HILLS = func_185428_a("jungle_hills");
            JUNGLE_EDGE = func_185428_a("jungle_edge");
            DEEP_OCEAN = func_185428_a("deep_ocean");
            STONE_SHORE = func_185428_a("stone_beach");
            SNOWY_BEACH = func_185428_a("cold_beach");
            BIRCH_FOREST = func_185428_a("birch_forest");
            BIRCH_FOREST_HILLS = func_185428_a("birch_forest_hills");
            DARK_FOREST = func_185428_a("roofed_forest");
            SNOWY_TAIGA = func_185428_a("taiga_cold");
            SNOWY_TAIGA_HILLS = func_185428_a("taiga_cold_hills");
            GIANT_TREE_TAIGA = func_185428_a("redwood_taiga");
            GIANT_TREE_TAIGA_HILLS = func_185428_a("redwood_taiga_hills");
            WOODED_MOUNTAINS = func_185428_a("extreme_hills_with_trees");
            SAVANNA = func_185428_a("savanna");
            SAVANNA_PLATEAU = func_185428_a("savanna_rock");
            BADLANDS = func_185428_a("mesa");
            WOODED_BADLANDS_PLATEAU = func_185428_a("mesa_rock");
            BADLANDS_PLATEAU = func_185428_a("mesa_clear_rock");
            THE_VOID = func_185428_a("void");
            SUNFLOWER_PLAINS = func_185428_a("mutated_plains");
            DESERT_LAKES = func_185428_a("mutated_desert");
            GRAVELLY_MOUNTAINS = func_185428_a("mutated_extreme_hills");
            FLOWER_FOREST = func_185428_a("mutated_forest");
            TAIGA_MOUNTAINS = func_185428_a("mutated_taiga");
            SWAMP_HILLS = func_185428_a("mutated_swampland");
            ICE_SPIKES = func_185428_a("mutated_ice_flats");
            MODIFIED_JUNGLE = func_185428_a("mutated_jungle");
            MODIFIED_JUNGLE_EDGE = func_185428_a("mutated_jungle_edge");
            TALL_BIRCH_FOREST = func_185428_a("mutated_birch_forest");
            TALL_BIRCH_HILLS = func_185428_a("mutated_birch_forest_hills");
            DARK_FOREST_HILLS = func_185428_a("mutated_roofed_forest");
            SNOWY_TAIGA_MOUNTAINS = func_185428_a("mutated_taiga_cold");
            GIANT_SPRUCE_TAIGA = func_185428_a("mutated_redwood_taiga");
            GIANT_SPRUCE_TAIGA_HILLS = func_185428_a("mutated_redwood_taiga_hills");
            MODIFIED_GRAVELLY_MOUNTAINS = func_185428_a("mutated_extreme_hills_with_trees");
            SHATTERED_SAVANNA = func_185428_a("mutated_savanna");
            SHATTERED_SAVANNA_PLATEAU = func_185428_a("mutated_savanna_rock");
            ERODED_BADLANDS = func_185428_a("mutated_mesa");
            MODIFIED_WOODED_BADLANDS_PLATEAU = func_185428_a("mutated_mesa_rock");
            MODIFIED_BADLANDS_PLATEAU = func_185428_a("mutated_mesa_clear_rock");
        }
    }
}

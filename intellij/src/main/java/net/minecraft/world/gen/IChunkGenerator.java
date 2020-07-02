package net.minecraft.world.gen;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public interface IChunkGenerator
{
    Chunk func_185932_a(int p_185932_1_, int p_185932_2_);

    void func_185931_b(int p_185931_1_, int p_185931_2_);

    boolean func_185933_a(Chunk p_185933_1_, int p_185933_2_, int p_185933_3_);

    List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos);

    @Nullable
    BlockPos func_180513_a(World p_180513_1_, String p_180513_2_, BlockPos p_180513_3_, boolean p_180513_4_);

    void func_180514_a(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_);

    boolean func_193414_a(World p_193414_1_, String p_193414_2_, BlockPos p_193414_3_);
}

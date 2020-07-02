package net.minecraft.world.chunk;

import javax.annotation.Nullable;

public interface IChunkProvider
{
    @Nullable
    Chunk func_186026_b(int p_186026_1_, int p_186026_2_);

    Chunk func_186025_d(int p_186025_1_, int p_186025_2_);

    boolean func_73156_b();

    /**
     * Converts the instance data to a readable string.
     */
    String makeString();

    boolean func_191062_e(int p_191062_1_, int p_191062_2_);
}

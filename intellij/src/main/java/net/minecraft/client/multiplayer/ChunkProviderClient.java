package net.minecraft.client.multiplayer;

import com.google.common.base.MoreObjects;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import javax.annotation.Nullable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkProviderClient implements IChunkProvider
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Chunk empty;
    private final Long2ObjectMap<Chunk> field_73236_b = new Long2ObjectOpenHashMap<Chunk>(8192)
    {
        protected void rehash(int p_rehash_1_)
        {
            if (p_rehash_1_ > this.key.length)
            {
                super.rehash(p_rehash_1_);
            }
        }
    };
    private final World world;

    public ChunkProviderClient(World p_i1184_1_)
    {
        this.empty = new EmptyChunk(p_i1184_1_, 0, 0);
        this.world = p_i1184_1_;
    }

    /**
     * Unload chunk from ChunkProviderClient's hashmap. Called in response to a Packet50PreChunk with its mode field set
     * to false
     */
    public void unloadChunk(int x, int z)
    {
        Chunk chunk = this.func_186025_d(x, z);

        if (!chunk.isEmpty())
        {
            chunk.func_76623_d();
        }

        this.field_73236_b.remove(ChunkPos.asLong(x, z));
    }

    @Nullable
    public Chunk func_186026_b(int p_186026_1_, int p_186026_2_)
    {
        return (Chunk)this.field_73236_b.get(ChunkPos.asLong(p_186026_1_, p_186026_2_));
    }

    public Chunk func_73158_c(int p_73158_1_, int p_73158_2_)
    {
        Chunk chunk = new Chunk(this.world, p_73158_1_, p_73158_2_);
        this.field_73236_b.put(ChunkPos.asLong(p_73158_1_, p_73158_2_), chunk);
        chunk.setLoaded(true);
        return chunk;
    }

    public Chunk func_186025_d(int p_186025_1_, int p_186025_2_)
    {
        return (Chunk)MoreObjects.firstNonNull(this.func_186026_b(p_186025_1_, p_186025_2_), this.empty);
    }

    public boolean func_73156_b()
    {
        long i = System.currentTimeMillis();
        ObjectIterator objectiterator = this.field_73236_b.values().iterator();

        while (objectiterator.hasNext())
        {
            Chunk chunk = (Chunk)objectiterator.next();
            chunk.func_150804_b(System.currentTimeMillis() - i > 5L);
        }

        if (System.currentTimeMillis() - i > 100L)
        {
            LOGGER.info("Warning: Clientside chunk ticking took {} ms", (long)(System.currentTimeMillis() - i));
        }

        return false;
    }

    /**
     * Converts the instance data to a readable string.
     */
    public String makeString()
    {
        return "MultiplayerChunkCache: " + this.field_73236_b.size() + ", " + this.field_73236_b.size();
    }

    public boolean func_191062_e(int p_191062_1_, int p_191062_2_)
    {
        return this.field_73236_b.containsKey(ChunkPos.asLong(p_191062_1_, p_191062_2_));
    }
}

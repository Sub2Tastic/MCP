package net.minecraft.world.chunk.storage;

import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnvilChunkLoader implements IChunkLoader, IThreadedFileIO
{
    private static final Logger field_151505_a = LogManager.getLogger();
    private final Map<ChunkPos, NBTTagCompound> field_75828_a = Maps.<ChunkPos, NBTTagCompound>newConcurrentMap();
    private final Set<ChunkPos> field_193415_c = Collections.<ChunkPos>newSetFromMap(Maps.newConcurrentMap());
    private final File field_75825_d;
    private final DataFixer field_193416_e;
    private boolean field_183014_e;

    public AnvilChunkLoader(File p_i46673_1_, DataFixer p_i46673_2_)
    {
        this.field_75825_d = p_i46673_1_;
        this.field_193416_e = p_i46673_2_;
    }

    @Nullable
    public Chunk func_75815_a(World p_75815_1_, int p_75815_2_, int p_75815_3_) throws IOException
    {
        ChunkPos chunkpos = new ChunkPos(p_75815_2_, p_75815_3_);
        NBTTagCompound nbttagcompound = this.field_75828_a.get(chunkpos);

        if (nbttagcompound == null)
        {
            DataInputStream datainputstream = RegionFileCache.func_76549_c(this.field_75825_d, p_75815_2_, p_75815_3_);

            if (datainputstream == null)
            {
                return null;
            }

            nbttagcompound = this.field_193416_e.func_188257_a(FixTypes.CHUNK, CompressedStreamTools.read(datainputstream));
        }

        return this.func_75822_a(p_75815_1_, p_75815_2_, p_75815_3_, nbttagcompound);
    }

    public boolean func_191063_a(int p_191063_1_, int p_191063_2_)
    {
        ChunkPos chunkpos = new ChunkPos(p_191063_1_, p_191063_2_);
        NBTTagCompound nbttagcompound = this.field_75828_a.get(chunkpos);
        return nbttagcompound != null ? true : RegionFileCache.func_191064_f(this.field_75825_d, p_191063_1_, p_191063_2_);
    }

    @Nullable
    protected Chunk func_75822_a(World p_75822_1_, int p_75822_2_, int p_75822_3_, NBTTagCompound p_75822_4_)
    {
        if (!p_75822_4_.contains("Level", 10))
        {
            field_151505_a.error("Chunk file at {},{} is missing level data, skipping", Integer.valueOf(p_75822_2_), Integer.valueOf(p_75822_3_));
            return null;
        }
        else
        {
            NBTTagCompound nbttagcompound = p_75822_4_.getCompound("Level");

            if (!nbttagcompound.contains("Sections", 9))
            {
                field_151505_a.error("Chunk file at {},{} is missing block data, skipping", Integer.valueOf(p_75822_2_), Integer.valueOf(p_75822_3_));
                return null;
            }
            else
            {
                Chunk chunk = this.func_75823_a(p_75822_1_, nbttagcompound);

                if (!chunk.func_76600_a(p_75822_2_, p_75822_3_))
                {
                    field_151505_a.error("Chunk file at {},{} is in the wrong location; relocating. (Expected {}, {}, got {}, {})", Integer.valueOf(p_75822_2_), Integer.valueOf(p_75822_3_), Integer.valueOf(p_75822_2_), Integer.valueOf(p_75822_3_), Integer.valueOf(chunk.field_76635_g), Integer.valueOf(chunk.field_76647_h));
                    nbttagcompound.putInt("xPos", p_75822_2_);
                    nbttagcompound.putInt("zPos", p_75822_3_);
                    chunk = this.func_75823_a(p_75822_1_, nbttagcompound);
                }

                return chunk;
            }
        }
    }

    public void func_75816_a(World p_75816_1_, Chunk p_75816_2_) throws MinecraftException, IOException
    {
        p_75816_1_.func_72906_B();

        try
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound.func_74782_a("Level", nbttagcompound1);
            nbttagcompound.putInt("DataVersion", 1343);
            this.func_75820_a(p_75816_2_, p_75816_1_, nbttagcompound1);
            this.func_75824_a(p_75816_2_.getPos(), nbttagcompound);
        }
        catch (Exception exception)
        {
            field_151505_a.error("Failed to save chunk", (Throwable)exception);
        }
    }

    protected void func_75824_a(ChunkPos p_75824_1_, NBTTagCompound p_75824_2_)
    {
        if (!this.field_193415_c.contains(p_75824_1_))
        {
            this.field_75828_a.put(p_75824_1_, p_75824_2_);
        }

        ThreadedFileIOBase.func_178779_a().func_75735_a(this);
    }

    public boolean func_75814_c()
    {
        if (this.field_75828_a.isEmpty())
        {
            if (this.field_183014_e)
            {
                field_151505_a.info("ThreadedAnvilChunkStorage ({}): All chunks are saved", (Object)this.field_75825_d.getName());
            }

            return false;
        }
        else
        {
            ChunkPos chunkpos = this.field_75828_a.keySet().iterator().next();
            boolean lvt_3_1_;

            try
            {
                this.field_193415_c.add(chunkpos);
                NBTTagCompound nbttagcompound = this.field_75828_a.remove(chunkpos);

                if (nbttagcompound != null)
                {
                    try
                    {
                        this.func_183013_b(chunkpos, nbttagcompound);
                    }
                    catch (Exception exception)
                    {
                        field_151505_a.error("Failed to save chunk", (Throwable)exception);
                    }
                }

                lvt_3_1_ = true;
            }
            finally
            {
                this.field_193415_c.remove(chunkpos);
            }

            return lvt_3_1_;
        }
    }

    private void func_183013_b(ChunkPos p_183013_1_, NBTTagCompound p_183013_2_) throws IOException
    {
        DataOutputStream dataoutputstream = RegionFileCache.func_76552_d(this.field_75825_d, p_183013_1_.x, p_183013_1_.z);
        CompressedStreamTools.write(p_183013_2_, dataoutputstream);
        dataoutputstream.close();
    }

    public void func_75819_b(World p_75819_1_, Chunk p_75819_2_) throws IOException
    {
    }

    public void func_75817_a()
    {
    }

    public void func_75818_b()
    {
        try
        {
            this.field_183014_e = true;

            while (this.func_75814_c());
        }
        finally
        {
            this.field_183014_e = false;
        }
    }

    public static void func_189889_a(DataFixer p_189889_0_)
    {
        p_189889_0_.func_188258_a(FixTypes.CHUNK, new IDataWalker()
        {
            public NBTTagCompound func_188266_a(IDataFixer p_188266_1_, NBTTagCompound p_188266_2_, int p_188266_3_)
            {
                if (p_188266_2_.contains("Level", 10))
                {
                    NBTTagCompound nbttagcompound = p_188266_2_.getCompound("Level");

                    if (nbttagcompound.contains("Entities", 9))
                    {
                        NBTTagList nbttaglist = nbttagcompound.getList("Entities", 10);

                        for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                        {
                            nbttaglist.func_150304_a(i, p_188266_1_.func_188251_a(FixTypes.ENTITY, (NBTTagCompound)nbttaglist.func_179238_g(i), p_188266_3_));
                        }
                    }

                    if (nbttagcompound.contains("TileEntities", 9))
                    {
                        NBTTagList nbttaglist1 = nbttagcompound.getList("TileEntities", 10);

                        for (int j = 0; j < nbttaglist1.func_74745_c(); ++j)
                        {
                            nbttaglist1.func_150304_a(j, p_188266_1_.func_188251_a(FixTypes.BLOCK_ENTITY, (NBTTagCompound)nbttaglist1.func_179238_g(j), p_188266_3_));
                        }
                    }
                }

                return p_188266_2_;
            }
        });
    }

    private void func_75820_a(Chunk p_75820_1_, World p_75820_2_, NBTTagCompound p_75820_3_)
    {
        p_75820_3_.putInt("xPos", p_75820_1_.field_76635_g);
        p_75820_3_.putInt("zPos", p_75820_1_.field_76647_h);
        p_75820_3_.putLong("LastUpdate", p_75820_2_.getGameTime());
        p_75820_3_.putIntArray("HeightMap", p_75820_1_.func_177445_q());
        p_75820_3_.putBoolean("TerrainPopulated", p_75820_1_.func_177419_t());
        p_75820_3_.putBoolean("LightPopulated", p_75820_1_.func_177423_u());
        p_75820_3_.putLong("InhabitedTime", p_75820_1_.getInhabitedTime());
        ExtendedBlockStorage[] aextendedblockstorage = p_75820_1_.getSections();
        NBTTagList nbttaglist = new NBTTagList();
        boolean flag = p_75820_2_.dimension.hasSkyLight();

        for (ExtendedBlockStorage extendedblockstorage : aextendedblockstorage)
        {
            if (extendedblockstorage != Chunk.EMPTY_SECTION)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.putByte("Y", (byte)(extendedblockstorage.func_76662_d() >> 4 & 255));
                byte[] abyte = new byte[4096];
                NibbleArray nibblearray = new NibbleArray();
                NibbleArray nibblearray1 = extendedblockstorage.getData().func_186017_a(abyte, nibblearray);
                nbttagcompound.putByteArray("Blocks", abyte);
                nbttagcompound.putByteArray("Data", nibblearray.getData());

                if (nibblearray1 != null)
                {
                    nbttagcompound.putByteArray("Add", nibblearray1.getData());
                }

                nbttagcompound.putByteArray("BlockLight", extendedblockstorage.func_76661_k().getData());

                if (flag)
                {
                    nbttagcompound.putByteArray("SkyLight", extendedblockstorage.func_76671_l().getData());
                }
                else
                {
                    nbttagcompound.putByteArray("SkyLight", new byte[extendedblockstorage.func_76661_k().getData().length]);
                }

                nbttaglist.func_74742_a(nbttagcompound);
            }
        }

        p_75820_3_.func_74782_a("Sections", nbttaglist);
        p_75820_3_.putByteArray("Biomes", p_75820_1_.func_76605_m());
        p_75820_1_.setHasEntities(false);
        NBTTagList nbttaglist1 = new NBTTagList();

        for (int i = 0; i < p_75820_1_.getEntityLists().length; ++i)
        {
            for (Entity entity : p_75820_1_.getEntityLists()[i])
            {
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();

                if (entity.writeUnlessPassenger(nbttagcompound2))
                {
                    p_75820_1_.setHasEntities(true);
                    nbttaglist1.func_74742_a(nbttagcompound2);
                }
            }
        }

        p_75820_3_.func_74782_a("Entities", nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();

        for (TileEntity tileentity : p_75820_1_.getTileEntityMap().values())
        {
            NBTTagCompound nbttagcompound3 = tileentity.write(new NBTTagCompound());
            nbttaglist2.func_74742_a(nbttagcompound3);
        }

        p_75820_3_.func_74782_a("TileEntities", nbttaglist2);
        List<NextTickListEntry> list = p_75820_2_.func_72920_a(p_75820_1_, false);

        if (list != null)
        {
            long j = p_75820_2_.getGameTime();
            NBTTagList nbttaglist3 = new NBTTagList();

            for (NextTickListEntry nextticklistentry : list)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                ResourceLocation resourcelocation = Block.field_149771_c.getKey(nextticklistentry.getTarget());
                nbttagcompound1.putString("i", resourcelocation == null ? "" : resourcelocation.toString());
                nbttagcompound1.putInt("x", nextticklistentry.position.getX());
                nbttagcompound1.putInt("y", nextticklistentry.position.getY());
                nbttagcompound1.putInt("z", nextticklistentry.position.getZ());
                nbttagcompound1.putInt("t", (int)(nextticklistentry.scheduledTime - j));
                nbttagcompound1.putInt("p", nextticklistentry.priority);
                nbttaglist3.func_74742_a(nbttagcompound1);
            }

            p_75820_3_.func_74782_a("TileTicks", nbttaglist3);
        }
    }

    private Chunk func_75823_a(World p_75823_1_, NBTTagCompound p_75823_2_)
    {
        int i = p_75823_2_.getInt("xPos");
        int j = p_75823_2_.getInt("zPos");
        Chunk chunk = new Chunk(p_75823_1_, i, j);
        chunk.func_177420_a(p_75823_2_.getIntArray("HeightMap"));
        chunk.func_177446_d(p_75823_2_.getBoolean("TerrainPopulated"));
        chunk.func_177421_e(p_75823_2_.getBoolean("LightPopulated"));
        chunk.setInhabitedTime(p_75823_2_.getLong("InhabitedTime"));
        NBTTagList nbttaglist = p_75823_2_.getList("Sections", 10);
        int k = 16;
        ExtendedBlockStorage[] aextendedblockstorage = new ExtendedBlockStorage[16];
        boolean flag = p_75823_1_.dimension.hasSkyLight();

        for (int l = 0; l < nbttaglist.func_74745_c(); ++l)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompound(l);
            int i1 = nbttagcompound.getByte("Y");
            ExtendedBlockStorage extendedblockstorage = new ExtendedBlockStorage(i1 << 4, flag);
            byte[] abyte = nbttagcompound.getByteArray("Blocks");
            NibbleArray nibblearray = new NibbleArray(nbttagcompound.getByteArray("Data"));
            NibbleArray nibblearray1 = nbttagcompound.contains("Add", 7) ? new NibbleArray(nbttagcompound.getByteArray("Add")) : null;
            extendedblockstorage.getData().func_186019_a(abyte, nibblearray, nibblearray1);
            extendedblockstorage.func_76659_c(new NibbleArray(nbttagcompound.getByteArray("BlockLight")));

            if (flag)
            {
                extendedblockstorage.func_76666_d(new NibbleArray(nbttagcompound.getByteArray("SkyLight")));
            }

            extendedblockstorage.recalculateRefCounts();
            aextendedblockstorage[i1] = extendedblockstorage;
        }

        chunk.func_76602_a(aextendedblockstorage);

        if (p_75823_2_.contains("Biomes", 7))
        {
            chunk.func_76616_a(p_75823_2_.getByteArray("Biomes"));
        }

        NBTTagList nbttaglist1 = p_75823_2_.getList("Entities", 10);

        for (int j1 = 0; j1 < nbttaglist1.func_74745_c(); ++j1)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist1.getCompound(j1);
            func_186050_a(nbttagcompound1, p_75823_1_, chunk);
            chunk.setHasEntities(true);
        }

        NBTTagList nbttaglist2 = p_75823_2_.getList("TileEntities", 10);

        for (int k1 = 0; k1 < nbttaglist2.func_74745_c(); ++k1)
        {
            NBTTagCompound nbttagcompound2 = nbttaglist2.getCompound(k1);
            TileEntity tileentity = TileEntity.func_190200_a(p_75823_1_, nbttagcompound2);

            if (tileentity != null)
            {
                chunk.addTileEntity(tileentity);
            }
        }

        if (p_75823_2_.contains("TileTicks", 9))
        {
            NBTTagList nbttaglist3 = p_75823_2_.getList("TileTicks", 10);

            for (int l1 = 0; l1 < nbttaglist3.func_74745_c(); ++l1)
            {
                NBTTagCompound nbttagcompound3 = nbttaglist3.getCompound(l1);
                Block block;

                if (nbttagcompound3.contains("i", 8))
                {
                    block = Block.func_149684_b(nbttagcompound3.getString("i"));
                }
                else
                {
                    block = Block.func_149729_e(nbttagcompound3.getInt("i"));
                }

                p_75823_1_.func_180497_b(new BlockPos(nbttagcompound3.getInt("x"), nbttagcompound3.getInt("y"), nbttagcompound3.getInt("z")), block, nbttagcompound3.getInt("t"), nbttagcompound3.getInt("p"));
            }
        }

        return chunk;
    }

    @Nullable
    public static Entity func_186050_a(NBTTagCompound p_186050_0_, World p_186050_1_, Chunk p_186050_2_)
    {
        Entity entity = func_186053_a(p_186050_0_, p_186050_1_);

        if (entity == null)
        {
            return null;
        }
        else
        {
            p_186050_2_.addEntity(entity);

            if (p_186050_0_.contains("Passengers", 9))
            {
                NBTTagList nbttaglist = p_186050_0_.getList("Passengers", 10);

                for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                {
                    Entity entity1 = func_186050_a(nbttaglist.getCompound(i), p_186050_1_, p_186050_2_);

                    if (entity1 != null)
                    {
                        entity1.startRiding(entity, true);
                    }
                }
            }

            return entity;
        }
    }

    @Nullable
    public static Entity func_186054_a(NBTTagCompound p_186054_0_, World p_186054_1_, double p_186054_2_, double p_186054_4_, double p_186054_6_, boolean p_186054_8_)
    {
        Entity entity = func_186053_a(p_186054_0_, p_186054_1_);

        if (entity == null)
        {
            return null;
        }
        else
        {
            entity.setLocationAndAngles(p_186054_2_, p_186054_4_, p_186054_6_, entity.rotationYaw, entity.rotationPitch);

            if (p_186054_8_ && !p_186054_1_.addEntity0(entity))
            {
                return null;
            }
            else
            {
                if (p_186054_0_.contains("Passengers", 9))
                {
                    NBTTagList nbttaglist = p_186054_0_.getList("Passengers", 10);

                    for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                    {
                        Entity entity1 = func_186054_a(nbttaglist.getCompound(i), p_186054_1_, p_186054_2_, p_186054_4_, p_186054_6_, p_186054_8_);

                        if (entity1 != null)
                        {
                            entity1.startRiding(entity, true);
                        }
                    }
                }

                return entity;
            }
        }
    }

    @Nullable
    protected static Entity func_186053_a(NBTTagCompound p_186053_0_, World p_186053_1_)
    {
        try
        {
            return EntityList.func_75615_a(p_186053_0_, p_186053_1_);
        }
        catch (RuntimeException var3)
        {
            return null;
        }
    }

    public static void func_186052_a(Entity p_186052_0_, World p_186052_1_)
    {
        if (p_186052_1_.addEntity0(p_186052_0_) && p_186052_0_.isBeingRidden())
        {
            for (Entity entity : p_186052_0_.getPassengers())
            {
                func_186052_a(entity, p_186052_1_);
            }
        }
    }

    @Nullable
    public static Entity func_186051_a(NBTTagCompound p_186051_0_, World p_186051_1_, boolean p_186051_2_)
    {
        Entity entity = func_186053_a(p_186051_0_, p_186051_1_);

        if (entity == null)
        {
            return null;
        }
        else if (p_186051_2_ && !p_186051_1_.addEntity0(entity))
        {
            return null;
        }
        else
        {
            if (p_186051_0_.contains("Passengers", 9))
            {
                NBTTagList nbttaglist = p_186051_0_.getList("Passengers", 10);

                for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                {
                    Entity entity1 = func_186051_a(nbttaglist.getCompound(i), p_186051_1_, p_186051_2_);

                    if (entity1 != null)
                    {
                        entity1.startRiding(entity, true);
                    }
                }
            }

            return entity;
        }
    }
}

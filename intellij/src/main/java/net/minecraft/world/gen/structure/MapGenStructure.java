package net.minecraft.world.gen.structure;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;

public abstract class MapGenStructure extends MapGenBase
{
    private MapGenStructureData field_143029_e;
    protected Long2ObjectMap<StructureStart> field_75053_d = new Long2ObjectOpenHashMap<StructureStart>(1024);

    public abstract String getStructureName();

    protected final synchronized void func_180701_a(World p_180701_1_, final int p_180701_2_, final int p_180701_3_, int p_180701_4_, int p_180701_5_, ChunkPrimer p_180701_6_)
    {
        this.func_143027_a(p_180701_1_);

        if (!this.field_75053_d.containsKey(ChunkPos.asLong(p_180701_2_, p_180701_3_)))
        {
            this.field_75038_b.nextInt();

            try
            {
                if (this.func_75047_a(p_180701_2_, p_180701_3_))
                {
                    StructureStart structurestart = this.func_75049_b(p_180701_2_, p_180701_3_);
                    this.field_75053_d.put(ChunkPos.asLong(p_180701_2_, p_180701_3_), structurestart);

                    if (structurestart.isValid())
                    {
                        this.func_143026_a(p_180701_2_, p_180701_3_, structurestart);
                    }
                }
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception preparing structure feature");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Feature being prepared");
                crashreportcategory.addDetail("Is feature chunk", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return MapGenStructure.this.func_75047_a(p_180701_2_, p_180701_3_) ? "True" : "False";
                    }
                });
                crashreportcategory.addDetail("Chunk location", String.format("%d,%d", p_180701_2_, p_180701_3_));
                crashreportcategory.addDetail("Chunk pos hash", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return String.valueOf(ChunkPos.asLong(p_180701_2_, p_180701_3_));
                    }
                });
                crashreportcategory.addDetail("Structure type", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return MapGenStructure.this.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    public synchronized boolean func_175794_a(World p_175794_1_, Random p_175794_2_, ChunkPos p_175794_3_)
    {
        this.func_143027_a(p_175794_1_);
        int i = (p_175794_3_.x << 4) + 8;
        int j = (p_175794_3_.z << 4) + 8;
        boolean flag = false;
        ObjectIterator objectiterator = this.field_75053_d.values().iterator();

        while (objectiterator.hasNext())
        {
            StructureStart structurestart = (StructureStart)objectiterator.next();

            if (structurestart.isValid() && structurestart.func_175788_a(p_175794_3_) && structurestart.getBoundingBox().intersectsWith(i, j, i + 15, j + 15))
            {
                structurestart.func_75068_a(p_175794_1_, p_175794_2_, new StructureBoundingBox(i, j, i + 15, j + 15));
                structurestart.func_175787_b(p_175794_3_);
                flag = true;
                this.func_143026_a(structurestart.getChunkPosX(), structurestart.getChunkPosZ(), structurestart);
            }
        }

        return flag;
    }

    public boolean func_175795_b(BlockPos p_175795_1_)
    {
        if (this.field_75039_c == null)
        {
            return false;
        }
        else
        {
            this.func_143027_a(this.field_75039_c);
            return this.func_175797_c(p_175795_1_) != null;
        }
    }

    @Nullable
    protected StructureStart func_175797_c(BlockPos p_175797_1_)
    {
        ObjectIterator objectiterator = this.field_75053_d.values().iterator();
        label31:

        while (objectiterator.hasNext())
        {
            StructureStart structurestart = (StructureStart)objectiterator.next();

            if (structurestart.isValid() && structurestart.getBoundingBox().isVecInside(p_175797_1_))
            {
                Iterator<StructureComponent> iterator = structurestart.getComponents().iterator();

                while (true)
                {
                    if (!iterator.hasNext())
                    {
                        continue label31;
                    }

                    StructureComponent structurecomponent = iterator.next();

                    if (structurecomponent.getBoundingBox().isVecInside(p_175797_1_))
                    {
                        break;
                    }
                }

                return structurestart;
            }
        }

        return null;
    }

    public boolean isPositionInStructure(World worldIn, BlockPos pos)
    {
        this.func_143027_a(worldIn);
        ObjectIterator objectiterator = this.field_75053_d.values().iterator();

        while (objectiterator.hasNext())
        {
            StructureStart structurestart = (StructureStart)objectiterator.next();

            if (structurestart.isValid() && structurestart.getBoundingBox().isVecInside(pos))
            {
                return true;
            }
        }

        return false;
    }

    @Nullable
    public abstract BlockPos func_180706_b(World p_180706_1_, BlockPos p_180706_2_, boolean p_180706_3_);

    protected void func_143027_a(World p_143027_1_)
    {
        if (this.field_143029_e == null && p_143027_1_ != null)
        {
            this.field_143029_e = (MapGenStructureData)p_143027_1_.func_72943_a(MapGenStructureData.class, this.getStructureName());

            if (this.field_143029_e == null)
            {
                this.field_143029_e = new MapGenStructureData(this.getStructureName());
                p_143027_1_.func_72823_a(this.getStructureName(), this.field_143029_e);
            }
            else
            {
                NBTTagCompound nbttagcompound = this.field_143029_e.func_143041_a();

                for (String s : nbttagcompound.keySet())
                {
                    NBTBase nbtbase = nbttagcompound.get(s);

                    if (nbtbase.getId() == 10)
                    {
                        NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbtbase;

                        if (nbttagcompound1.contains("ChunkX") && nbttagcompound1.contains("ChunkZ"))
                        {
                            int i = nbttagcompound1.getInt("ChunkX");
                            int j = nbttagcompound1.getInt("ChunkZ");
                            StructureStart structurestart = MapGenStructureIO.func_143035_a(nbttagcompound1, p_143027_1_);

                            if (structurestart != null)
                            {
                                this.field_75053_d.put(ChunkPos.asLong(i, j), structurestart);
                            }
                        }
                    }
                }
            }
        }
    }

    private void func_143026_a(int p_143026_1_, int p_143026_2_, StructureStart p_143026_3_)
    {
        this.field_143029_e.func_143043_a(p_143026_3_.write(p_143026_1_, p_143026_2_), p_143026_1_, p_143026_2_);
        this.field_143029_e.markDirty();
    }

    protected abstract boolean func_75047_a(int p_75047_1_, int p_75047_2_);

    protected abstract StructureStart func_75049_b(int p_75049_1_, int p_75049_2_);

    protected static BlockPos func_191069_a(World p_191069_0_, MapGenStructure p_191069_1_, BlockPos p_191069_2_, int p_191069_3_, int p_191069_4_, int p_191069_5_, boolean p_191069_6_, int p_191069_7_, boolean p_191069_8_)
    {
        int i = p_191069_2_.getX() >> 4;
        int j = p_191069_2_.getZ() >> 4;
        int k = 0;

        for (Random random = new Random(); k <= p_191069_7_; ++k)
        {
            for (int l = -k; l <= k; ++l)
            {
                boolean flag = l == -k || l == k;

                for (int i1 = -k; i1 <= k; ++i1)
                {
                    boolean flag1 = i1 == -k || i1 == k;

                    if (flag || flag1)
                    {
                        int j1 = i + p_191069_3_ * l;
                        int k1 = j + p_191069_3_ * i1;

                        if (j1 < 0)
                        {
                            j1 -= p_191069_3_ - 1;
                        }

                        if (k1 < 0)
                        {
                            k1 -= p_191069_3_ - 1;
                        }

                        int l1 = j1 / p_191069_3_;
                        int i2 = k1 / p_191069_3_;
                        Random random1 = p_191069_0_.func_72843_D(l1, i2, p_191069_5_);
                        l1 = l1 * p_191069_3_;
                        i2 = i2 * p_191069_3_;

                        if (p_191069_6_)
                        {
                            l1 = l1 + (random1.nextInt(p_191069_3_ - p_191069_4_) + random1.nextInt(p_191069_3_ - p_191069_4_)) / 2;
                            i2 = i2 + (random1.nextInt(p_191069_3_ - p_191069_4_) + random1.nextInt(p_191069_3_ - p_191069_4_)) / 2;
                        }
                        else
                        {
                            l1 = l1 + random1.nextInt(p_191069_3_ - p_191069_4_);
                            i2 = i2 + random1.nextInt(p_191069_3_ - p_191069_4_);
                        }

                        MapGenBase.func_191068_a(p_191069_0_.getSeed(), random, l1, i2);
                        random.nextInt();

                        if (p_191069_1_.func_75047_a(l1, i2))
                        {
                            if (!p_191069_8_ || !p_191069_0_.func_190526_b(l1, i2))
                            {
                                return new BlockPos((l1 << 4) + 8, 64, (i2 << 4) + 8);
                            }
                        }
                        else if (k == 0)
                        {
                            break;
                        }
                    }
                }

                if (k == 0)
                {
                    break;
                }
            }
        }

        return null;
    }
}

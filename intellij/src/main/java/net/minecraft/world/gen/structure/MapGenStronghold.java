package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class MapGenStronghold extends MapGenStructure
{
    private final List<Biome> field_151546_e;
    private boolean ranBiomeCheck;
    private ChunkPos[] structureCoords;
    private double field_82671_h;
    private int field_82672_i;

    public MapGenStronghold()
    {
        this.structureCoords = new ChunkPos[128];
        this.field_82671_h = 32.0D;
        this.field_82672_i = 3;
        this.field_151546_e = Lists.<Biome>newArrayList();

        for (Biome biome : Biome.field_185377_q)
        {
            if (biome != null && biome.getDepth() > 0.0F)
            {
                this.field_151546_e.add(biome);
            }
        }
    }

    public MapGenStronghold(Map<String, String> p_i2068_1_)
    {
        this();

        for (Entry<String, String> entry : p_i2068_1_.entrySet())
        {
            if (((String)entry.getKey()).equals("distance"))
            {
                this.field_82671_h = MathHelper.func_82713_a(entry.getValue(), this.field_82671_h, 1.0D);
            }
            else if (((String)entry.getKey()).equals("count"))
            {
                this.structureCoords = new ChunkPos[MathHelper.getInt(entry.getValue(), this.structureCoords.length, 1)];
            }
            else if (((String)entry.getKey()).equals("spread"))
            {
                this.field_82672_i = MathHelper.getInt(entry.getValue(), this.field_82672_i, 1);
            }
        }
    }

    public String getStructureName()
    {
        return "Stronghold";
    }

    public BlockPos func_180706_b(World p_180706_1_, BlockPos p_180706_2_, boolean p_180706_3_)
    {
        if (!this.ranBiomeCheck)
        {
            this.func_189104_c();
            this.ranBiomeCheck = true;
        }

        BlockPos blockpos = null;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(0, 0, 0);
        double d0 = Double.MAX_VALUE;

        for (ChunkPos chunkpos : this.structureCoords)
        {
            blockpos$mutableblockpos.setPos((chunkpos.x << 4) + 8, 32, (chunkpos.z << 4) + 8);
            double d1 = blockpos$mutableblockpos.distanceSq(p_180706_2_);

            if (blockpos == null)
            {
                blockpos = new BlockPos(blockpos$mutableblockpos);
                d0 = d1;
            }
            else if (d1 < d0)
            {
                blockpos = new BlockPos(blockpos$mutableblockpos);
                d0 = d1;
            }
        }

        return blockpos;
    }

    protected boolean func_75047_a(int p_75047_1_, int p_75047_2_)
    {
        if (!this.ranBiomeCheck)
        {
            this.func_189104_c();
            this.ranBiomeCheck = true;
        }

        for (ChunkPos chunkpos : this.structureCoords)
        {
            if (p_75047_1_ == chunkpos.x && p_75047_2_ == chunkpos.z)
            {
                return true;
            }
        }

        return false;
    }

    private void func_189104_c()
    {
        this.func_143027_a(this.field_75039_c);
        int i = 0;
        ObjectIterator lvt_2_1_ = this.field_75053_d.values().iterator();

        while (lvt_2_1_.hasNext())
        {
            StructureStart structurestart = (StructureStart)lvt_2_1_.next();

            if (i < this.structureCoords.length)
            {
                this.structureCoords[i++] = new ChunkPos(structurestart.getChunkPosX(), structurestart.getChunkPosZ());
            }
        }

        Random random = new Random();
        random.setSeed(this.field_75039_c.getSeed());
        double d1 = random.nextDouble() * Math.PI * 2.0D;
        int j = 0;
        int k = 0;
        int l = this.field_75053_d.size();

        if (l < this.structureCoords.length)
        {
            for (int i1 = 0; i1 < this.structureCoords.length; ++i1)
            {
                double d0 = 4.0D * this.field_82671_h + this.field_82671_h * (double)j * 6.0D + (random.nextDouble() - 0.5D) * this.field_82671_h * 2.5D;
                int j1 = (int)Math.round(Math.cos(d1) * d0);
                int k1 = (int)Math.round(Math.sin(d1) * d0);
                BlockPos blockpos = this.field_75039_c.func_72959_q().func_180630_a((j1 << 4) + 8, (k1 << 4) + 8, 112, this.field_151546_e, random);

                if (blockpos != null)
                {
                    j1 = blockpos.getX() >> 4;
                    k1 = blockpos.getZ() >> 4;
                }

                if (i1 >= l)
                {
                    this.structureCoords[i1] = new ChunkPos(j1, k1);
                }

                d1 += (Math.PI * 2D) / (double)this.field_82672_i;
                ++k;

                if (k == this.field_82672_i)
                {
                    ++j;
                    k = 0;
                    this.field_82672_i += 2 * this.field_82672_i / (j + 1);
                    this.field_82672_i = Math.min(this.field_82672_i, this.structureCoords.length - i1);
                    d1 += random.nextDouble() * Math.PI * 2.0D;
                }
            }
        }
    }

    protected StructureStart func_75049_b(int p_75049_1_, int p_75049_2_)
    {
        MapGenStronghold.Start mapgenstronghold$start;

        for (mapgenstronghold$start = new MapGenStronghold.Start(this.field_75039_c, this.field_75038_b, p_75049_1_, p_75049_2_); mapgenstronghold$start.getComponents().isEmpty() || ((StructureStrongholdPieces.Stairs2)mapgenstronghold$start.getComponents().get(0)).strongholdPortalRoom == null; mapgenstronghold$start = new MapGenStronghold.Start(this.field_75039_c, this.field_75038_b, p_75049_1_, p_75049_2_))
        {
            ;
        }

        return mapgenstronghold$start;
    }

    public static class Start extends StructureStart
    {
        public Start()
        {
        }

        public Start(World p_i2067_1_, Random p_i2067_2_, int p_i2067_3_, int p_i2067_4_)
        {
            super(p_i2067_3_, p_i2067_4_);
            StructureStrongholdPieces.prepareStructurePieces();
            StructureStrongholdPieces.Stairs2 structurestrongholdpieces$stairs2 = new StructureStrongholdPieces.Stairs2(0, p_i2067_2_, (p_i2067_3_ << 4) + 2, (p_i2067_4_ << 4) + 2);
            this.components.add(structurestrongholdpieces$stairs2);
            structurestrongholdpieces$stairs2.buildComponent(structurestrongholdpieces$stairs2, this.components, p_i2067_2_);
            List<StructureComponent> list = structurestrongholdpieces$stairs2.pendingChildren;

            while (!list.isEmpty())
            {
                int i = p_i2067_2_.nextInt(list.size());
                StructureComponent structurecomponent = list.remove(i);
                structurecomponent.buildComponent(structurestrongholdpieces$stairs2, this.components, p_i2067_2_);
            }

            this.func_75072_c();
            this.func_75067_a(p_i2067_1_, p_i2067_2_, 10);
        }
    }
}

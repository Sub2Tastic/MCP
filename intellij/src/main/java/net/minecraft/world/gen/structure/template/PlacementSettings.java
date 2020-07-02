package net.minecraft.world.gen.structure.template;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class PlacementSettings
{
    private Mirror mirror = Mirror.NONE;
    private Rotation rotation = Rotation.NONE;
    private boolean ignoreEntities;
    @Nullable
    private Block field_186231_d;
    @Nullable
    private ChunkPos chunk;
    @Nullable
    private StructureBoundingBox boundingBox;
    private boolean field_186234_g = true;
    private float field_189951_h = 1.0F;
    @Nullable
    private Random random;
    @Nullable
    private Long field_189953_j;

    public PlacementSettings copy()
    {
        PlacementSettings placementsettings = new PlacementSettings();
        placementsettings.mirror = this.mirror;
        placementsettings.rotation = this.rotation;
        placementsettings.ignoreEntities = this.ignoreEntities;
        placementsettings.field_186231_d = this.field_186231_d;
        placementsettings.chunk = this.chunk;
        placementsettings.boundingBox = this.boundingBox;
        placementsettings.field_186234_g = this.field_186234_g;
        placementsettings.field_189951_h = this.field_189951_h;
        placementsettings.random = this.random;
        placementsettings.field_189953_j = this.field_189953_j;
        return placementsettings;
    }

    public PlacementSettings setMirror(Mirror mirrorIn)
    {
        this.mirror = mirrorIn;
        return this;
    }

    public PlacementSettings setRotation(Rotation rotationIn)
    {
        this.rotation = rotationIn;
        return this;
    }

    public PlacementSettings setIgnoreEntities(boolean ignoreEntitiesIn)
    {
        this.ignoreEntities = ignoreEntitiesIn;
        return this;
    }

    public PlacementSettings func_186225_a(Block p_186225_1_)
    {
        this.field_186231_d = p_186225_1_;
        return this;
    }

    public PlacementSettings setChunk(ChunkPos chunkPosIn)
    {
        this.chunk = chunkPosIn;
        return this;
    }

    public PlacementSettings setBoundingBox(StructureBoundingBox boundingBoxIn)
    {
        this.boundingBox = boundingBoxIn;
        return this;
    }

    public PlacementSettings func_189949_a(@Nullable Long p_189949_1_)
    {
        this.field_189953_j = p_189949_1_;
        return this;
    }

    public PlacementSettings setRandom(@Nullable Random randomIn)
    {
        this.random = randomIn;
        return this;
    }

    public PlacementSettings func_189946_a(float p_189946_1_)
    {
        this.field_189951_h = p_189946_1_;
        return this;
    }

    public Mirror getMirror()
    {
        return this.mirror;
    }

    public PlacementSettings func_186226_b(boolean p_186226_1_)
    {
        this.field_186234_g = p_186226_1_;
        return this;
    }

    public Rotation getRotation()
    {
        return this.rotation;
    }

    public Random getRandom(@Nullable BlockPos seed)
    {
        if (this.random != null)
        {
            return this.random;
        }
        else if (this.field_189953_j != null)
        {
            return this.field_189953_j.longValue() == 0L ? new Random(System.currentTimeMillis()) : new Random(this.field_189953_j.longValue());
        }
        else if (seed == null)
        {
            return new Random(System.currentTimeMillis());
        }
        else
        {
            int i = seed.getX();
            int j = seed.getZ();
            return new Random((long)(i * i * 4987142 + i * 5947611) + (long)(j * j) * 4392871L + (long)(j * 389711) ^ 987234911L);
        }
    }

    public float func_189948_f()
    {
        return this.field_189951_h;
    }

    public boolean getIgnoreEntities()
    {
        return this.ignoreEntities;
    }

    @Nullable
    public Block func_186219_f()
    {
        return this.field_186231_d;
    }

    @Nullable
    public StructureBoundingBox getBoundingBox()
    {
        if (this.boundingBox == null && this.chunk != null)
        {
            this.setBoundingBoxFromChunk();
        }

        return this.boundingBox;
    }

    public boolean func_186227_h()
    {
        return this.field_186234_g;
    }

    void setBoundingBoxFromChunk()
    {
        this.boundingBox = this.getBoundingBoxFromChunk(this.chunk);
    }

    @Nullable
    private StructureBoundingBox getBoundingBoxFromChunk(@Nullable ChunkPos pos)
    {
        if (pos == null)
        {
            return null;
        }
        else
        {
            int i = pos.x * 16;
            int j = pos.z * 16;
            return new StructureBoundingBox(i, 0, j, i + 16 - 1, 255, j + 16 - 1);
        }
    }
}

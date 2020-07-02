package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.IChunkGenerator;

public abstract class WorldProvider
{
    public static final float[] MOON_PHASE_FACTORS = new float[] {1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F};
    protected World world;
    private WorldType field_76577_b;
    private String field_82913_c;
    protected BiomeProvider field_76578_c;
    protected boolean doesWaterVaporize;
    protected boolean nether;
    protected boolean field_191067_f;
    protected final float[] lightBrightnessTable = new float[16];
    private final float[] colorsSunriseSunset = new float[4];

    public final void func_76558_a(World p_76558_1_)
    {
        this.world = p_76558_1_;
        this.field_76577_b = p_76558_1_.getWorldInfo().getGenerator();
        this.field_82913_c = p_76558_1_.getWorldInfo().func_82571_y();
        this.func_76572_b();
        this.func_76556_a();
    }

    protected void func_76556_a()
    {
        float f = 0.0F;

        for (int i = 0; i <= 15; ++i)
        {
            float f1 = 1.0F - (float)i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * 1.0F + 0.0F;
        }
    }

    protected void func_76572_b()
    {
        this.field_191067_f = true;
        WorldType worldtype = this.world.getWorldInfo().getGenerator();

        if (worldtype == WorldType.FLAT)
        {
            FlatGeneratorInfo flatgeneratorinfo = FlatGeneratorInfo.createFlatGeneratorFromString(this.world.getWorldInfo().func_82571_y());
            this.field_76578_c = new BiomeProviderSingle(Biome.func_180276_a(flatgeneratorinfo.getBiome(), Biomes.DEFAULT));
        }
        else if (worldtype == WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            this.field_76578_c = new BiomeProviderSingle(Biomes.PLAINS);
        }
        else
        {
            this.field_76578_c = new BiomeProvider(this.world.getWorldInfo());
        }
    }

    public IChunkGenerator createChunkGenerator()
    {
        if (this.field_76577_b == WorldType.FLAT)
        {
            return new ChunkGeneratorFlat(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.field_82913_c);
        }
        else if (this.field_76577_b == WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            return new ChunkGeneratorDebug(this.world);
        }
        else
        {
            return this.field_76577_b == WorldType.CUSTOMIZED ? new ChunkGeneratorOverworld(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.field_82913_c) : new ChunkGeneratorOverworld(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled(), this.field_82913_c);
        }
    }

    public boolean func_76566_a(int p_76566_1_, int p_76566_2_)
    {
        BlockPos blockpos = new BlockPos(p_76566_1_, 0, p_76566_2_);

        if (this.world.func_180494_b(blockpos).func_185352_i())
        {
            return true;
        }
        else
        {
            return this.world.getGroundAboveSeaLevel(blockpos).getBlock() == Blocks.GRASS;
        }
    }

    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     */
    public float calculateCelestialAngle(long worldTime, float partialTicks)
    {
        int i = (int)(worldTime % 24000L);
        float f = ((float)i + partialTicks) / 24000.0F - 0.25F;

        if (f < 0.0F)
        {
            ++f;
        }

        if (f > 1.0F)
        {
            --f;
        }

        float f1 = 1.0F - (float)((Math.cos((double)f * Math.PI) + 1.0D) / 2.0D);
        f = f + (f1 - f) / 3.0F;
        return f;
    }

    public int getMoonPhase(long worldTime)
    {
        return (int)(worldTime / 24000L % 8L + 8L) % 8;
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
    public boolean isSurfaceWorld()
    {
        return true;
    }

    @Nullable

    /**
     * Returns array with sunrise/sunset colors
     */
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
    {
        float f = 0.4F;
        float f1 = MathHelper.cos(celestialAngle * ((float)Math.PI * 2F)) - 0.0F;
        float f2 = -0.0F;

        if (f1 >= -0.4F && f1 <= 0.4F)
        {
            float f3 = (f1 - -0.0F) / 0.4F * 0.5F + 0.5F;
            float f4 = 1.0F - (1.0F - MathHelper.sin(f3 * (float)Math.PI)) * 0.99F;
            f4 = f4 * f4;
            this.colorsSunriseSunset[0] = f3 * 0.3F + 0.7F;
            this.colorsSunriseSunset[1] = f3 * f3 * 0.7F + 0.2F;
            this.colorsSunriseSunset[2] = f3 * f3 * 0.0F + 0.2F;
            this.colorsSunriseSunset[3] = f4;
            return this.colorsSunriseSunset;
        }
        else
        {
            return null;
        }
    }

    /**
     * Return Vec3D with biome specific fog color
     */
    public Vec3d getFogColor(float celestialAngle, float partialTicks)
    {
        float f = MathHelper.cos(celestialAngle * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float f1 = 0.7529412F;
        float f2 = 0.84705883F;
        float f3 = 1.0F;
        f1 = f1 * (f * 0.94F + 0.06F);
        f2 = f2 * (f * 0.94F + 0.06F);
        f3 = f3 * (f * 0.91F + 0.09F);
        return new Vec3d((double)f1, (double)f2, (double)f3);
    }

    /**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */
    public boolean canRespawnHere()
    {
        return true;
    }

    /**
     * the y level at which clouds are rendered.
     */
    public float getCloudHeight()
    {
        return 128.0F;
    }

    public boolean isSkyColored()
    {
        return true;
    }

    @Nullable
    public BlockPos getSpawnCoordinate()
    {
        return null;
    }

    public int func_76557_i()
    {
        return this.field_76577_b == WorldType.FLAT ? 4 : this.world.getSeaLevel() + 1;
    }

    /**
     * Returns a double value representing the Y value relative to the top of the map at which void fog is at its
     * maximum. The default factor of 0.03125 relative to 256, for example, means the void fog will be at its maximum at
     * (256*0.03125), or 8.
     */
    public double getVoidFogYFactor()
    {
        return this.field_76577_b == WorldType.FLAT ? 1.0D : 0.03125D;
    }

    /**
     * Returns true if the given X,Z coordinate should show environmental fog.
     */
    public boolean doesXZShowFog(int x, int z)
    {
        return false;
    }

    public BiomeProvider func_177499_m()
    {
        return this.field_76578_c;
    }

    public boolean doesWaterVaporize()
    {
        return this.doesWaterVaporize;
    }

    public boolean hasSkyLight()
    {
        return this.field_191067_f;
    }

    public boolean isNether()
    {
        return this.nether;
    }

    public float[] func_177497_p()
    {
        return this.lightBrightnessTable;
    }

    public WorldBorder createWorldBorder()
    {
        return new WorldBorder();
    }

    public void func_186061_a(EntityPlayerMP p_186061_1_)
    {
    }

    public void func_186062_b(EntityPlayerMP p_186062_1_)
    {
    }

    public abstract DimensionType getType();

    /**
     * Called when the world is performing a save. Only used to save the state of the Dragon Boss fight in
     * WorldProviderEnd in Vanilla.
     */
    public void onWorldSave()
    {
    }

    /**
     * Called when the world is updating entities. Only used in WorldProviderEnd to update the DragonFightManager in
     * Vanilla.
     */
    public void tick()
    {
    }

    public boolean func_186056_c(int p_186056_1_, int p_186056_2_)
    {
        return true;
    }
}

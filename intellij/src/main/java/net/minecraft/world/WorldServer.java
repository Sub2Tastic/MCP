package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEventData;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.INpc;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ReportedException;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillageCollection;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedDataCallableSave;
import net.minecraft.world.storage.loot.LootTableManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldServer extends World implements IThreadListener
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final MinecraftServer server;
    private final EntityTracker field_73062_L;
    private final PlayerChunkMap field_73063_M;
    private final Set<NextTickListEntry> field_73064_N = Sets.<NextTickListEntry>newHashSet();
    private final TreeSet<NextTickListEntry> field_73065_O = new TreeSet<NextTickListEntry>();
    private final Map<UUID, Entity> entitiesByUuid = Maps.<UUID, Entity>newHashMap();
    public boolean disableLevelSaving;
    private boolean allPlayersSleeping;
    private int updateEntityTick;
    private final Teleporter worldTeleporter;
    private final WorldEntitySpawner field_175742_R = new WorldEntitySpawner();
    protected final VillageSiege field_175740_d = new VillageSiege(this);
    private final WorldServer.ServerBlockEventList[] blockEventQueue = new WorldServer.ServerBlockEventList[] {new WorldServer.ServerBlockEventList(), new WorldServer.ServerBlockEventList()};
    private int field_147489_T;
    private final List<NextTickListEntry> pendingBlockTicks = Lists.<NextTickListEntry>newArrayList();

    public WorldServer(MinecraftServer p_i45921_1_, ISaveHandler p_i45921_2_, WorldInfo p_i45921_3_, int p_i45921_4_, Profiler p_i45921_5_)
    {
        super(p_i45921_2_, p_i45921_3_, DimensionType.getById(p_i45921_4_).func_186070_d(), p_i45921_5_, false);
        this.server = p_i45921_1_;
        this.field_73062_L = new EntityTracker(this);
        this.field_73063_M = new PlayerChunkMap(this);
        this.dimension.func_76558_a(this);
        this.chunkProvider = this.func_72970_h();
        this.worldTeleporter = new Teleporter(this);
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
        this.getWorldBorder().setSize(p_i45921_1_.getMaxWorldSize());
    }

    public World func_175643_b()
    {
        this.field_72988_C = new MapStorage(this.field_73019_z);
        String s = VillageCollection.func_176062_a(this.dimension);
        VillageCollection villagecollection = (VillageCollection)this.field_72988_C.func_75742_a(VillageCollection.class, s);

        if (villagecollection == null)
        {
            this.field_72982_D = new VillageCollection(this);
            this.field_72988_C.func_75745_a(s, this.field_72982_D);
        }
        else
        {
            this.field_72982_D = villagecollection;
            this.field_72982_D.func_82566_a(this);
        }

        this.field_96442_D = new ServerScoreboard(this.server);
        ScoreboardSaveData scoreboardsavedata = (ScoreboardSaveData)this.field_72988_C.func_75742_a(ScoreboardSaveData.class, "scoreboard");

        if (scoreboardsavedata == null)
        {
            scoreboardsavedata = new ScoreboardSaveData();
            this.field_72988_C.func_75745_a("scoreboard", scoreboardsavedata);
        }

        scoreboardsavedata.setScoreboard(this.field_96442_D);
        ((ServerScoreboard)this.field_96442_D).addDirtyRunnable(new WorldSavedDataCallableSave(scoreboardsavedata));
        this.field_184151_B = new LootTableManager(new File(new File(this.field_73019_z.getWorldDirectory(), "data"), "loot_tables"));
        this.field_191951_C = new AdvancementManager(new File(new File(this.field_73019_z.getWorldDirectory(), "data"), "advancements"));
        this.field_193036_D = new FunctionManager(new File(new File(this.field_73019_z.getWorldDirectory(), "data"), "functions"), this.server);
        this.getWorldBorder().setCenter(this.worldInfo.getBorderCenterX(), this.worldInfo.getBorderCenterZ());
        this.getWorldBorder().setDamagePerBlock(this.worldInfo.getBorderDamagePerBlock());
        this.getWorldBorder().setDamageBuffer(this.worldInfo.getBorderSafeZone());
        this.getWorldBorder().setWarningDistance(this.worldInfo.getBorderWarningBlocks());
        this.getWorldBorder().setWarningTime(this.worldInfo.getBorderWarningTime());

        if (this.worldInfo.getBorderSizeLerpTime() > 0L)
        {
            this.getWorldBorder().setTransition(this.worldInfo.getBorderSize(), this.worldInfo.getBorderSizeLerpTarget(), this.worldInfo.getBorderSizeLerpTime());
        }
        else
        {
            this.getWorldBorder().setTransition(this.worldInfo.getBorderSize());
        }

        return this;
    }

    /**
     * Runs a single tick for the world
     */
    public void tick()
    {
        super.tick();

        if (this.getWorldInfo().isHardcore() && this.getDifficulty() != EnumDifficulty.HARD)
        {
            this.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
        }

        this.dimension.func_177499_m().func_76938_b();

        if (this.func_73056_e())
        {
            if (this.getGameRules().func_82766_b("doDaylightCycle"))
            {
                long i = this.worldInfo.getDayTime() + 24000L;
                this.worldInfo.setDayTime(i - i % 24000L);
            }

            this.func_73053_d();
        }

        this.profiler.startSection("mobSpawner");

        if (this.getGameRules().func_82766_b("doMobSpawning") && this.worldInfo.getGenerator() != WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            this.field_175742_R.func_77192_a(this, this.field_72985_G, this.field_72992_H, this.worldInfo.getGameTime() % 400L == 0L);
        }

        this.profiler.func_76318_c("chunkSource");
        this.chunkProvider.func_73156_b();
        int j = this.func_72967_a(1.0F);

        if (j != this.getSkylightSubtracted())
        {
            this.func_175692_b(j);
        }

        this.worldInfo.setGameTime(this.worldInfo.getGameTime() + 1L);

        if (this.getGameRules().func_82766_b("doDaylightCycle"))
        {
            this.worldInfo.setDayTime(this.worldInfo.getDayTime() + 1L);
        }

        this.profiler.func_76318_c("tickPending");
        this.func_72955_a(false);
        this.profiler.func_76318_c("tickBlocks");
        this.func_147456_g();
        this.profiler.func_76318_c("chunkMap");
        this.field_73063_M.func_72693_b();
        this.profiler.func_76318_c("village");
        this.field_72982_D.func_75544_a();
        this.field_175740_d.func_75528_a();
        this.profiler.func_76318_c("portalForcer");
        this.worldTeleporter.func_85189_a(this.getGameTime());
        this.profiler.endSection();
        this.sendQueuedBlockEvents();
    }

    @Nullable
    public Biome.SpawnListEntry func_175734_a(EnumCreatureType p_175734_1_, BlockPos p_175734_2_)
    {
        List<Biome.SpawnListEntry> list = this.getChunkProvider().getPossibleCreatures(p_175734_1_, p_175734_2_);
        return list != null && !list.isEmpty() ? (Biome.SpawnListEntry)WeightedRandom.getRandomItem(this.rand, list) : null;
    }

    public boolean func_175732_a(EnumCreatureType p_175732_1_, Biome.SpawnListEntry p_175732_2_, BlockPos p_175732_3_)
    {
        List<Biome.SpawnListEntry> list = this.getChunkProvider().getPossibleCreatures(p_175732_1_, p_175732_3_);
        return list != null && !list.isEmpty() ? list.contains(p_175732_2_) : false;
    }

    /**
     * Updates the flag that indicates whether or not all players in the world are sleeping.
     */
    public void updateAllPlayersSleepingFlag()
    {
        this.allPlayersSleeping = false;

        if (!this.field_73010_i.isEmpty())
        {
            int i = 0;
            int j = 0;

            for (EntityPlayer entityplayer : this.field_73010_i)
            {
                if (entityplayer.isSpectator())
                {
                    ++i;
                }
                else if (entityplayer.isSleeping())
                {
                    ++j;
                }
            }

            this.allPlayersSleeping = j > 0 && j >= this.field_73010_i.size() - i;
        }
    }

    protected void func_73053_d()
    {
        this.allPlayersSleeping = false;

        for (EntityPlayer entityplayer : this.field_73010_i.stream().filter(EntityPlayer::isSleeping).collect(Collectors.toList()))
        {
            entityplayer.func_70999_a(false, false, true);
        }

        if (this.getGameRules().func_82766_b("doWeatherCycle"))
        {
            this.resetRainAndThunder();
        }
    }

    /**
     * Clears the current rain and thunder weather states.
     */
    private void resetRainAndThunder()
    {
        this.worldInfo.setRainTime(0);
        this.worldInfo.setRaining(false);
        this.worldInfo.setThunderTime(0);
        this.worldInfo.setThundering(false);
    }

    public boolean func_73056_e()
    {
        if (this.allPlayersSleeping && !this.isRemote)
        {
            for (EntityPlayer entityplayer : this.field_73010_i)
            {
                if (!entityplayer.isSpectator() && !entityplayer.isPlayerFullyAsleep())
                {
                    return false;
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Sets a new spawn location by finding an uncovered block at a random (x,z) location in the chunk.
     */
    public void setInitialSpawnLocation()
    {
        if (this.worldInfo.getSpawnY() <= 0)
        {
            this.worldInfo.setSpawnY(this.getSeaLevel() + 1);
        }

        int i = this.worldInfo.getSpawnX();
        int j = this.worldInfo.getSpawnZ();
        int k = 0;

        while (this.getGroundAboveSeaLevel(new BlockPos(i, 0, j)).getMaterial() == Material.AIR)
        {
            i += this.rand.nextInt(8) - this.rand.nextInt(8);
            j += this.rand.nextInt(8) - this.rand.nextInt(8);
            ++k;

            if (k == 10000)
            {
                break;
            }
        }

        this.worldInfo.setSpawnX(i);
        this.worldInfo.setSpawnZ(j);
    }

    protected boolean func_175680_a(int p_175680_1_, int p_175680_2_, boolean p_175680_3_)
    {
        return this.getChunkProvider().chunkExists(p_175680_1_, p_175680_2_);
    }

    protected void func_184162_i()
    {
        this.profiler.startSection("playerCheckLight");

        if (!this.field_73010_i.isEmpty())
        {
            int i = this.rand.nextInt(this.field_73010_i.size());
            EntityPlayer entityplayer = this.field_73010_i.get(i);
            int j = MathHelper.floor(entityplayer.posX) + this.rand.nextInt(11) - 5;
            int k = MathHelper.floor(entityplayer.posY) + this.rand.nextInt(11) - 5;
            int l = MathHelper.floor(entityplayer.posZ) + this.rand.nextInt(11) - 5;
            this.func_175664_x(new BlockPos(j, k, l));
        }

        this.profiler.endSection();
    }

    protected void func_147456_g()
    {
        this.func_184162_i();

        if (this.worldInfo.getGenerator() == WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            Iterator<Chunk> iterator1 = this.field_73063_M.func_187300_b();

            while (iterator1.hasNext())
            {
                ((Chunk)iterator1.next()).func_150804_b(false);
            }
        }
        else
        {
            int i = this.getGameRules().func_180263_c("randomTickSpeed");
            boolean flag = this.isRaining();
            boolean flag1 = this.isThundering();
            this.profiler.startSection("pollingChunks");

            for (Iterator<Chunk> iterator = this.field_73063_M.func_187300_b(); iterator.hasNext(); this.profiler.endSection())
            {
                this.profiler.startSection("getChunk");
                Chunk chunk = iterator.next();
                int j = chunk.field_76635_g * 16;
                int k = chunk.field_76647_h * 16;
                this.profiler.func_76318_c("checkNextLight");
                chunk.func_76594_o();
                this.profiler.func_76318_c("tickChunk");
                chunk.func_150804_b(false);
                this.profiler.func_76318_c("thunder");

                if (flag && flag1 && this.rand.nextInt(100000) == 0)
                {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    int l = this.updateLCG >> 2;
                    BlockPos blockpos = this.adjustPosToNearbyEntity(new BlockPos(j + (l & 15), 0, k + (l >> 8 & 15)));

                    if (this.isRainingAt(blockpos))
                    {
                        DifficultyInstance difficultyinstance = this.getDifficultyForLocation(blockpos);

                        if (this.getGameRules().func_82766_b("doMobSpawning") && this.rand.nextDouble() < (double)difficultyinstance.getAdditionalDifficulty() * 0.01D)
                        {
                            EntitySkeletonHorse entityskeletonhorse = new EntitySkeletonHorse(this);
                            entityskeletonhorse.setTrap(true);
                            entityskeletonhorse.setGrowingAge(0);
                            entityskeletonhorse.setPosition((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
                            this.addEntity0(entityskeletonhorse);
                            this.func_72942_c(new EntityLightningBolt(this, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), true));
                        }
                        else
                        {
                            this.func_72942_c(new EntityLightningBolt(this, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), false));
                        }
                    }
                }

                this.profiler.func_76318_c("iceandsnow");

                if (this.rand.nextInt(16) == 0)
                {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    int j2 = this.updateLCG >> 2;
                    BlockPos blockpos1 = this.func_175725_q(new BlockPos(j + (j2 & 15), 0, k + (j2 >> 8 & 15)));
                    BlockPos blockpos2 = blockpos1.down();

                    if (this.func_175662_w(blockpos2))
                    {
                        this.setBlockState(blockpos2, Blocks.ICE.getDefaultState());
                    }

                    if (flag && this.func_175708_f(blockpos1, true))
                    {
                        this.setBlockState(blockpos1, Blocks.field_150431_aC.getDefaultState());
                    }

                    if (flag && this.func_180494_b(blockpos2).func_76738_d())
                    {
                        this.getBlockState(blockpos2).getBlock().fillWithRain(this, blockpos2);
                    }
                }

                this.profiler.func_76318_c("tickBlocks");

                if (i > 0)
                {
                    for (ExtendedBlockStorage extendedblockstorage : chunk.getSections())
                    {
                        if (extendedblockstorage != Chunk.EMPTY_SECTION && extendedblockstorage.needsRandomTick())
                        {
                            for (int i1 = 0; i1 < i; ++i1)
                            {
                                this.updateLCG = this.updateLCG * 3 + 1013904223;
                                int j1 = this.updateLCG >> 2;
                                int k1 = j1 & 15;
                                int l1 = j1 >> 8 & 15;
                                int i2 = j1 >> 16 & 15;
                                IBlockState iblockstate = extendedblockstorage.getBlockState(k1, i2, l1);
                                Block block = iblockstate.getBlock();
                                this.profiler.startSection("randomTick");

                                if (block.ticksRandomly())
                                {
                                    block.func_180645_a(this, new BlockPos(k1 + j, i2 + extendedblockstorage.func_76662_d(), l1 + k), iblockstate, this.rand);
                                }

                                this.profiler.endSection();
                            }
                        }
                    }
                }
            }

            this.profiler.endSection();
        }
    }

    protected BlockPos adjustPosToNearbyEntity(BlockPos pos)
    {
        BlockPos blockpos = this.func_175725_q(pos);
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockpos, new BlockPos(blockpos.getX(), this.func_72800_K(), blockpos.getZ()))).grow(3.0D);
        List<EntityLivingBase> list = this.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, new com.google.common.base.Predicate<EntityLivingBase>()
        {
            public boolean apply(@Nullable EntityLivingBase p_apply_1_)
            {
                return p_apply_1_ != null && p_apply_1_.isAlive() && WorldServer.this.func_175678_i(p_apply_1_.getPosition());
            }
        });

        if (!list.isEmpty())
        {
            return ((EntityLivingBase)list.get(this.rand.nextInt(list.size()))).getPosition();
        }
        else
        {
            if (blockpos.getY() == -1)
            {
                blockpos = blockpos.up(2);
            }

            return blockpos;
        }
    }

    public boolean func_175691_a(BlockPos p_175691_1_, Block p_175691_2_)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(p_175691_1_, p_175691_2_);
        return this.pendingBlockTicks.contains(nextticklistentry);
    }

    public boolean func_184145_b(BlockPos p_184145_1_, Block p_184145_2_)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(p_184145_1_, p_184145_2_);
        return this.field_73064_N.contains(nextticklistentry);
    }

    public void func_175684_a(BlockPos p_175684_1_, Block p_175684_2_, int p_175684_3_)
    {
        this.func_175654_a(p_175684_1_, p_175684_2_, p_175684_3_, 0);
    }

    public void func_175654_a(BlockPos p_175654_1_, Block p_175654_2_, int p_175654_3_, int p_175654_4_)
    {
        Material material = p_175654_2_.getDefaultState().getMaterial();

        if (this.field_72999_e && material != Material.AIR)
        {
            if (p_175654_2_.func_149698_L())
            {
                if (this.isAreaLoaded(p_175654_1_.add(-8, -8, -8), p_175654_1_.add(8, 8, 8)))
                {
                    IBlockState iblockstate = this.getBlockState(p_175654_1_);

                    if (iblockstate.getMaterial() != Material.AIR && iblockstate.getBlock() == p_175654_2_)
                    {
                        iblockstate.getBlock().func_180650_b(this, p_175654_1_, iblockstate, this.rand);
                    }
                }

                return;
            }

            p_175654_3_ = 1;
        }

        NextTickListEntry nextticklistentry = new NextTickListEntry(p_175654_1_, p_175654_2_);

        if (this.isBlockLoaded(p_175654_1_))
        {
            if (material != Material.AIR)
            {
                nextticklistentry.func_77176_a((long)p_175654_3_ + this.worldInfo.getGameTime());
                nextticklistentry.func_82753_a(p_175654_4_);
            }

            if (!this.field_73064_N.contains(nextticklistentry))
            {
                this.field_73064_N.add(nextticklistentry);
                this.field_73065_O.add(nextticklistentry);
            }
        }
    }

    public void func_180497_b(BlockPos p_180497_1_, Block p_180497_2_, int p_180497_3_, int p_180497_4_)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(p_180497_1_, p_180497_2_);
        nextticklistentry.func_82753_a(p_180497_4_);
        Material material = p_180497_2_.getDefaultState().getMaterial();

        if (material != Material.AIR)
        {
            nextticklistentry.func_77176_a((long)p_180497_3_ + this.worldInfo.getGameTime());
        }

        if (!this.field_73064_N.contains(nextticklistentry))
        {
            this.field_73064_N.add(nextticklistentry);
            this.field_73065_O.add(nextticklistentry);
        }
    }

    public void func_72939_s()
    {
        if (this.field_73010_i.isEmpty())
        {
            if (this.updateEntityTick++ >= 300)
            {
                return;
            }
        }
        else
        {
            this.resetUpdateEntityTick();
        }

        this.dimension.tick();
        super.func_72939_s();
    }

    protected void func_184147_l()
    {
        super.func_184147_l();
        this.profiler.func_76318_c("players");

        for (int i = 0; i < this.field_73010_i.size(); ++i)
        {
            Entity entity = this.field_73010_i.get(i);
            Entity entity1 = entity.getRidingEntity();

            if (entity1 != null)
            {
                if (!entity1.removed && entity1.isPassenger(entity))
                {
                    continue;
                }

                entity.stopRiding();
            }

            this.profiler.startSection("tick");

            if (!entity.removed)
            {
                try
                {
                    this.func_72870_g(entity);
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
                    entity.fillCrashReport(crashreportcategory);
                    throw new ReportedException(crashreport);
                }
            }

            this.profiler.endSection();
            this.profiler.startSection("remove");

            if (entity.removed)
            {
                int j = entity.chunkCoordX;
                int k = entity.chunkCoordZ;

                if (entity.addedToChunk && this.func_175680_a(j, k, true))
                {
                    this.func_72964_e(j, k).removeEntity(entity);
                }

                this.field_72996_f.remove(entity);
                this.func_72847_b(entity);
            }

            this.profiler.endSection();
        }
    }

    /**
     * Resets the updateEntityTick field to 0
     */
    public void resetUpdateEntityTick()
    {
        this.updateEntityTick = 0;
    }

    public boolean func_72955_a(boolean p_72955_1_)
    {
        if (this.worldInfo.getGenerator() == WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            return false;
        }
        else
        {
            int i = this.field_73065_O.size();

            if (i != this.field_73064_N.size())
            {
                throw new IllegalStateException("TickNextTick list out of synch");
            }
            else
            {
                if (i > 65536)
                {
                    i = 65536;
                }

                this.profiler.startSection("cleaning");

                for (int j = 0; j < i; ++j)
                {
                    NextTickListEntry nextticklistentry = this.field_73065_O.first();

                    if (!p_72955_1_ && nextticklistentry.scheduledTime > this.worldInfo.getGameTime())
                    {
                        break;
                    }

                    this.field_73065_O.remove(nextticklistentry);
                    this.field_73064_N.remove(nextticklistentry);
                    this.pendingBlockTicks.add(nextticklistentry);
                }

                this.profiler.endSection();
                this.profiler.startSection("ticking");
                Iterator<NextTickListEntry> iterator = this.pendingBlockTicks.iterator();

                while (iterator.hasNext())
                {
                    NextTickListEntry nextticklistentry1 = iterator.next();
                    iterator.remove();
                    int k = 0;

                    if (this.isAreaLoaded(nextticklistentry1.position.add(0, 0, 0), nextticklistentry1.position.add(0, 0, 0)))
                    {
                        IBlockState iblockstate = this.getBlockState(nextticklistentry1.position);

                        if (iblockstate.getMaterial() != Material.AIR && Block.func_149680_a(iblockstate.getBlock(), nextticklistentry1.getTarget()))
                        {
                            try
                            {
                                iblockstate.getBlock().func_180650_b(this, nextticklistentry1.position, iblockstate, this.rand);
                            }
                            catch (Throwable throwable)
                            {
                                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while ticking a block");
                                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being ticked");
                                CrashReportCategory.addBlockInfo(crashreportcategory, nextticklistentry1.position, iblockstate);
                                throw new ReportedException(crashreport);
                            }
                        }
                    }
                    else
                    {
                        this.func_175684_a(nextticklistentry1.position, nextticklistentry1.getTarget(), 0);
                    }
                }

                this.profiler.endSection();
                this.pendingBlockTicks.clear();
                return !this.field_73065_O.isEmpty();
            }
        }
    }

    @Nullable
    public List<NextTickListEntry> func_72920_a(Chunk p_72920_1_, boolean p_72920_2_)
    {
        ChunkPos chunkpos = p_72920_1_.getPos();
        int i = (chunkpos.x << 4) - 2;
        int j = i + 16 + 2;
        int k = (chunkpos.z << 4) - 2;
        int l = k + 16 + 2;
        return this.func_175712_a(new StructureBoundingBox(i, 0, k, j, 256, l), p_72920_2_);
    }

    @Nullable
    public List<NextTickListEntry> func_175712_a(StructureBoundingBox p_175712_1_, boolean p_175712_2_)
    {
        List<NextTickListEntry> list = null;

        for (int i = 0; i < 2; ++i)
        {
            Iterator<NextTickListEntry> iterator;

            if (i == 0)
            {
                iterator = this.field_73065_O.iterator();
            }
            else
            {
                iterator = this.pendingBlockTicks.iterator();
            }

            while (iterator.hasNext())
            {
                NextTickListEntry nextticklistentry = iterator.next();
                BlockPos blockpos = nextticklistentry.position;

                if (blockpos.getX() >= p_175712_1_.minX && blockpos.getX() < p_175712_1_.maxX && blockpos.getZ() >= p_175712_1_.minZ && blockpos.getZ() < p_175712_1_.maxZ)
                {
                    if (p_175712_2_)
                    {
                        if (i == 0)
                        {
                            this.field_73064_N.remove(nextticklistentry);
                        }

                        iterator.remove();
                    }

                    if (list == null)
                    {
                        list = Lists.<NextTickListEntry>newArrayList();
                    }

                    list.add(nextticklistentry);
                }
            }
        }

        return list;
    }

    public void func_72866_a(Entity p_72866_1_, boolean p_72866_2_)
    {
        if (!this.func_175735_ai() && (p_72866_1_ instanceof EntityAnimal || p_72866_1_ instanceof EntityWaterMob))
        {
            p_72866_1_.remove();
        }

        if (!this.func_175738_ah() && p_72866_1_ instanceof INpc)
        {
            p_72866_1_.remove();
        }

        super.func_72866_a(p_72866_1_, p_72866_2_);
    }

    private boolean func_175738_ah()
    {
        return this.server.getCanSpawnNPCs();
    }

    private boolean func_175735_ai()
    {
        return this.server.getCanSpawnAnimals();
    }

    protected IChunkProvider func_72970_h()
    {
        IChunkLoader ichunkloader = this.field_73019_z.func_75763_a(this.dimension);
        return new ChunkProviderServer(this, ichunkloader, this.dimension.createChunkGenerator());
    }

    public boolean isBlockModifiable(EntityPlayer player, BlockPos pos)
    {
        return !this.server.isBlockProtected(this, pos, player) && this.getWorldBorder().contains(pos);
    }

    public void func_72963_a(WorldSettings p_72963_1_)
    {
        if (!this.worldInfo.isInitialized())
        {
            try
            {
                this.createSpawnPosition(p_72963_1_);

                if (this.worldInfo.getGenerator() == WorldType.DEBUG_ALL_BLOCK_STATES)
                {
                    this.func_175737_aj();
                }

                super.func_72963_a(p_72963_1_);
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception initializing level");

                try
                {
                    this.fillCrashReport(crashreport);
                }
                catch (Throwable var5)
                {
                    ;
                }

                throw new ReportedException(crashreport);
            }

            this.worldInfo.setInitialized(true);
        }
    }

    private void func_175737_aj()
    {
        this.worldInfo.setMapFeaturesEnabled(false);
        this.worldInfo.setAllowCommands(true);
        this.worldInfo.setRaining(false);
        this.worldInfo.setThundering(false);
        this.worldInfo.setClearWeatherTime(1000000000);
        this.worldInfo.setDayTime(6000L);
        this.worldInfo.setGameType(GameType.SPECTATOR);
        this.worldInfo.setHardcore(false);
        this.worldInfo.setDifficulty(EnumDifficulty.PEACEFUL);
        this.worldInfo.setDifficultyLocked(true);
        this.getGameRules().func_82764_b("doDaylightCycle", "false");
    }

    /**
     * creates a spawn position at random within 256 blocks of 0,0
     */
    private void createSpawnPosition(WorldSettings settings)
    {
        if (!this.dimension.canRespawnHere())
        {
            this.worldInfo.setSpawn(BlockPos.ZERO.up(this.dimension.func_76557_i()));
        }
        else if (this.worldInfo.getGenerator() == WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            this.worldInfo.setSpawn(BlockPos.ZERO.up());
        }
        else
        {
            this.field_72987_B = true;
            BiomeProvider biomeprovider = this.dimension.func_177499_m();
            List<Biome> list = biomeprovider.getBiomesToSpawnIn();
            Random random = new Random(this.getSeed());
            BlockPos blockpos = biomeprovider.func_180630_a(0, 0, 256, list, random);
            int i = 8;
            int j = this.dimension.func_76557_i();
            int k = 8;

            if (blockpos != null)
            {
                i = blockpos.getX();
                k = blockpos.getZ();
            }
            else
            {
                LOGGER.warn("Unable to find spawn biome");
            }

            int l = 0;

            while (!this.dimension.func_76566_a(i, k))
            {
                i += random.nextInt(64) - random.nextInt(64);
                k += random.nextInt(64) - random.nextInt(64);
                ++l;

                if (l == 1000)
                {
                    break;
                }
            }

            this.worldInfo.setSpawn(new BlockPos(i, j, k));
            this.field_72987_B = false;

            if (settings.isBonusChestEnabled())
            {
                this.createBonusChest();
            }
        }
    }

    /**
     * Creates the bonus chest in the world.
     */
    protected void createBonusChest()
    {
        WorldGeneratorBonusChest worldgeneratorbonuschest = new WorldGeneratorBonusChest();

        for (int i = 0; i < 10; ++i)
        {
            int j = this.worldInfo.getSpawnX() + this.rand.nextInt(6) - this.rand.nextInt(6);
            int k = this.worldInfo.getSpawnZ() + this.rand.nextInt(6) - this.rand.nextInt(6);
            BlockPos blockpos = this.func_175672_r(new BlockPos(j, 0, k)).up();

            if (worldgeneratorbonuschest.func_180709_b(this, this.rand, blockpos))
            {
                break;
            }
        }
    }

    @Nullable

    /**
     * Returns null for anything other than the End
     */
    public BlockPos getSpawnCoordinate()
    {
        return this.dimension.getSpawnCoordinate();
    }

    public void func_73044_a(boolean p_73044_1_, @Nullable IProgressUpdate p_73044_2_) throws MinecraftException
    {
        ChunkProviderServer chunkproviderserver = this.getChunkProvider();

        if (chunkproviderserver.func_73157_c())
        {
            if (p_73044_2_ != null)
            {
                p_73044_2_.func_73720_a("Saving level");
            }

            this.saveLevel();

            if (p_73044_2_ != null)
            {
                p_73044_2_.func_73719_c("Saving chunks");
            }

            chunkproviderserver.func_186027_a(p_73044_1_);

            for (Chunk chunk : Lists.newArrayList(chunkproviderserver.func_189548_a()))
            {
                if (chunk != null && !this.field_73063_M.func_152621_a(chunk.field_76635_g, chunk.field_76647_h))
                {
                    chunkproviderserver.func_189549_a(chunk);
                }
            }
        }
    }

    public void func_104140_m()
    {
        ChunkProviderServer chunkproviderserver = this.getChunkProvider();

        if (chunkproviderserver.func_73157_c())
        {
            chunkproviderserver.func_104112_b();
        }
    }

    /**
     * Saves the chunks to disk.
     */
    protected void saveLevel() throws MinecraftException
    {
        this.func_72906_B();

        for (WorldServer worldserver : this.server.worlds)
        {
            if (worldserver instanceof WorldServerMulti)
            {
                ((WorldServerMulti)worldserver).func_184166_c();
            }
        }

        this.worldInfo.setBorderSize(this.getWorldBorder().getDiameter());
        this.worldInfo.setBorderCenterX(this.getWorldBorder().getCenterX());
        this.worldInfo.setBorderCenterZ(this.getWorldBorder().getCenterZ());
        this.worldInfo.setBorderSafeZone(this.getWorldBorder().getDamageBuffer());
        this.worldInfo.setBorderDamagePerBlock(this.getWorldBorder().getDamagePerBlock());
        this.worldInfo.setBorderWarningBlocks(this.getWorldBorder().getWarningDistance());
        this.worldInfo.setBorderWarningTime(this.getWorldBorder().getWarningTime());
        this.worldInfo.setBorderSizeLerpTarget(this.getWorldBorder().getTargetSize());
        this.worldInfo.setBorderSizeLerpTime(this.getWorldBorder().getTimeUntilTarget());
        this.field_73019_z.saveWorldInfoWithPlayer(this.worldInfo, this.server.getPlayerList().getHostPlayerData());
        this.field_72988_C.func_75744_a();
    }

    /**
     * Called when an entity is spawned in the world. This includes players.
     */
    public boolean addEntity0(Entity entityIn)
    {
        return this.func_184165_i(entityIn) ? super.addEntity0(entityIn) : false;
    }

    public void func_175650_b(Collection<Entity> p_175650_1_)
    {
        for (Entity entity : Lists.newArrayList(p_175650_1_))
        {
            if (this.func_184165_i(entity))
            {
                this.field_72996_f.add(entity);
                this.func_72923_a(entity);
            }
        }
    }

    private boolean func_184165_i(Entity p_184165_1_)
    {
        if (p_184165_1_.removed)
        {
            LOGGER.warn("Tried to add entity {} but it was marked as removed already", (Object)EntityList.func_191301_a(p_184165_1_));
            return false;
        }
        else
        {
            UUID uuid = p_184165_1_.getUniqueID();

            if (this.entitiesByUuid.containsKey(uuid))
            {
                Entity entity = this.entitiesByUuid.get(uuid);

                if (this.field_72997_g.contains(entity))
                {
                    this.field_72997_g.remove(entity);
                }
                else
                {
                    if (!(p_184165_1_ instanceof EntityPlayer))
                    {
                        LOGGER.warn("Keeping entity {} that already exists with UUID {}", EntityList.func_191301_a(entity), uuid.toString());
                        return false;
                    }

                    LOGGER.warn("Force-added player with duplicate UUID {}", (Object)uuid.toString());
                }

                this.func_72973_f(entity);
            }

            return true;
        }
    }

    protected void func_72923_a(Entity p_72923_1_)
    {
        super.func_72923_a(p_72923_1_);
        this.field_175729_l.func_76038_a(p_72923_1_.getEntityId(), p_72923_1_);
        this.entitiesByUuid.put(p_72923_1_.getUniqueID(), p_72923_1_);
        Entity[] aentity = p_72923_1_.func_70021_al();

        if (aentity != null)
        {
            for (Entity entity : aentity)
            {
                this.field_175729_l.func_76038_a(entity.getEntityId(), entity);
            }
        }
    }

    protected void func_72847_b(Entity p_72847_1_)
    {
        super.func_72847_b(p_72847_1_);
        this.field_175729_l.func_76049_d(p_72847_1_.getEntityId());
        this.entitiesByUuid.remove(p_72847_1_.getUniqueID());
        Entity[] aentity = p_72847_1_.func_70021_al();

        if (aentity != null)
        {
            for (Entity entity : aentity)
            {
                this.field_175729_l.func_76049_d(entity.getEntityId());
            }
        }
    }

    public boolean func_72942_c(Entity p_72942_1_)
    {
        if (super.func_72942_c(p_72942_1_))
        {
            this.server.getPlayerList().sendToAllNearExcept((EntityPlayer)null, p_72942_1_.posX, p_72942_1_.posY, p_72942_1_.posZ, 512.0D, this.dimension.getType().getId(), new SPacketSpawnGlobalEntity(p_72942_1_));
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * sends a Packet 38 (Entity Status) to all tracked players of that entity
     */
    public void setEntityState(Entity entityIn, byte state)
    {
        this.func_73039_n().func_151248_b(entityIn, new SPacketEntityStatus(entityIn, state));
    }

    /**
     * Gets the world's chunk provider
     */
    public ChunkProviderServer getChunkProvider()
    {
        return (ChunkProviderServer)super.getChunkProvider();
    }

    public Explosion func_72885_a(@Nullable Entity p_72885_1_, double p_72885_2_, double p_72885_4_, double p_72885_6_, float p_72885_8_, boolean p_72885_9_, boolean p_72885_10_)
    {
        Explosion explosion = new Explosion(this, p_72885_1_, p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_, p_72885_9_, p_72885_10_);
        explosion.doExplosionA();
        explosion.doExplosionB(false);

        if (!p_72885_10_)
        {
            explosion.clearAffectedBlockPositions();
        }

        for (EntityPlayer entityplayer : this.field_73010_i)
        {
            if (entityplayer.getDistanceSq(p_72885_2_, p_72885_4_, p_72885_6_) < 4096.0D)
            {
                ((EntityPlayerMP)entityplayer).connection.sendPacket(new SPacketExplosion(p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_, explosion.getAffectedBlockPositions(), (Vec3d)explosion.getPlayerKnockbackMap().get(entityplayer)));
            }
        }

        return explosion;
    }

    public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam)
    {
        BlockEventData blockeventdata = new BlockEventData(pos, blockIn, eventID, eventParam);

        for (BlockEventData blockeventdata1 : this.blockEventQueue[this.field_147489_T])
        {
            if (blockeventdata1.equals(blockeventdata))
            {
                return;
            }
        }

        this.blockEventQueue[this.field_147489_T].add(blockeventdata);
    }

    private void sendQueuedBlockEvents()
    {
        while (!this.blockEventQueue[this.field_147489_T].isEmpty())
        {
            int i = this.field_147489_T;
            this.field_147489_T ^= 1;

            for (BlockEventData blockeventdata : this.blockEventQueue[i])
            {
                if (this.fireBlockEvent(blockeventdata))
                {
                    this.server.getPlayerList().sendToAllNearExcept((EntityPlayer)null, (double)blockeventdata.getPosition().getX(), (double)blockeventdata.getPosition().getY(), (double)blockeventdata.getPosition().getZ(), 64.0D, this.dimension.getType().getId(), new SPacketBlockAction(blockeventdata.getPosition(), blockeventdata.getBlock(), blockeventdata.getEventID(), blockeventdata.getEventParameter()));
                }
            }

            this.blockEventQueue[i].clear();
        }
    }

    private boolean fireBlockEvent(BlockEventData event)
    {
        IBlockState iblockstate = this.getBlockState(event.getPosition());
        return iblockstate.getBlock() == event.getBlock() ? iblockstate.onBlockEventReceived(this, event.getPosition(), event.getEventID(), event.getEventParameter()) : false;
    }

    public void func_73041_k()
    {
        this.field_73019_z.func_75759_a();
    }

    protected void func_72979_l()
    {
        boolean flag = this.isRaining();
        super.func_72979_l();

        if (this.prevRainingStrength != this.rainingStrength)
        {
            this.server.getPlayerList().sendPacketToAllPlayersInDimension(new SPacketChangeGameState(7, this.rainingStrength), this.dimension.getType().getId());
        }

        if (this.prevThunderingStrength != this.thunderingStrength)
        {
            this.server.getPlayerList().sendPacketToAllPlayersInDimension(new SPacketChangeGameState(8, this.thunderingStrength), this.dimension.getType().getId());
        }

        if (flag != this.isRaining())
        {
            if (flag)
            {
                this.server.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(2, 0.0F));
            }
            else
            {
                this.server.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(1, 0.0F));
            }

            this.server.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(7, this.rainingStrength));
            this.server.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(8, this.thunderingStrength));
        }
    }

    @Nullable
    public MinecraftServer getServer()
    {
        return this.server;
    }

    public EntityTracker func_73039_n()
    {
        return this.field_73062_L;
    }

    public PlayerChunkMap func_184164_w()
    {
        return this.field_73063_M;
    }

    public Teleporter getDefaultTeleporter()
    {
        return this.worldTeleporter;
    }

    public TemplateManager getStructureTemplateManager()
    {
        return this.field_73019_z.getStructureTemplateManager();
    }

    public void func_175739_a(EnumParticleTypes p_175739_1_, double p_175739_2_, double p_175739_4_, double p_175739_6_, int p_175739_8_, double p_175739_9_, double p_175739_11_, double p_175739_13_, double p_175739_15_, int... p_175739_17_)
    {
        this.func_180505_a(p_175739_1_, false, p_175739_2_, p_175739_4_, p_175739_6_, p_175739_8_, p_175739_9_, p_175739_11_, p_175739_13_, p_175739_15_, p_175739_17_);
    }

    public void func_180505_a(EnumParticleTypes p_180505_1_, boolean p_180505_2_, double p_180505_3_, double p_180505_5_, double p_180505_7_, int p_180505_9_, double p_180505_10_, double p_180505_12_, double p_180505_14_, double p_180505_16_, int... p_180505_18_)
    {
        SPacketParticles spacketparticles = new SPacketParticles(p_180505_1_, p_180505_2_, (float)p_180505_3_, (float)p_180505_5_, (float)p_180505_7_, (float)p_180505_10_, (float)p_180505_12_, (float)p_180505_14_, (float)p_180505_16_, p_180505_9_, p_180505_18_);

        for (int i = 0; i < this.field_73010_i.size(); ++i)
        {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)this.field_73010_i.get(i);
            this.func_184159_a(entityplayermp, p_180505_2_, p_180505_3_, p_180505_5_, p_180505_7_, spacketparticles);
        }
    }

    public void func_184161_a(EntityPlayerMP p_184161_1_, EnumParticleTypes p_184161_2_, boolean p_184161_3_, double p_184161_4_, double p_184161_6_, double p_184161_8_, int p_184161_10_, double p_184161_11_, double p_184161_13_, double p_184161_15_, double p_184161_17_, int... p_184161_19_)
    {
        Packet<?> packet = new SPacketParticles(p_184161_2_, p_184161_3_, (float)p_184161_4_, (float)p_184161_6_, (float)p_184161_8_, (float)p_184161_11_, (float)p_184161_13_, (float)p_184161_15_, (float)p_184161_17_, p_184161_10_, p_184161_19_);
        this.func_184159_a(p_184161_1_, p_184161_3_, p_184161_4_, p_184161_6_, p_184161_8_, packet);
    }

    private void func_184159_a(EntityPlayerMP p_184159_1_, boolean p_184159_2_, double p_184159_3_, double p_184159_5_, double p_184159_7_, Packet<?> p_184159_9_)
    {
        BlockPos blockpos = p_184159_1_.getPosition();
        double d0 = blockpos.func_177954_c(p_184159_3_, p_184159_5_, p_184159_7_);

        if (d0 <= 1024.0D || p_184159_2_ && d0 <= 262144.0D)
        {
            p_184159_1_.connection.sendPacket(p_184159_9_);
        }
    }

    @Nullable
    public Entity func_175733_a(UUID p_175733_1_)
    {
        return this.entitiesByUuid.get(p_175733_1_);
    }

    public ListenableFuture<Object> func_152344_a(Runnable p_152344_1_)
    {
        return this.server.func_152344_a(p_152344_1_);
    }

    public boolean func_152345_ab()
    {
        return this.server.func_152345_ab();
    }

    @Nullable
    public BlockPos func_190528_a(String p_190528_1_, BlockPos p_190528_2_, boolean p_190528_3_)
    {
        return this.getChunkProvider().func_180513_a(this, p_190528_1_, p_190528_2_, p_190528_3_);
    }

    public AdvancementManager func_191952_z()
    {
        return this.field_191951_C;
    }

    public FunctionManager func_193037_A()
    {
        return this.field_193036_D;
    }

    static class ServerBlockEventList extends ArrayList<BlockEventData>
    {
        private ServerBlockEventList()
        {
        }
    }
}

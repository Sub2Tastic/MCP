package net.minecraft.world;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObserver;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.pathfinding.PathWorldListener;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.loot.LootTableManager;

public abstract class World implements IBlockAccess
{
    private int field_181546_a = 63;
    protected boolean field_72999_e;
    public final List<Entity> field_72996_f = Lists.<Entity>newArrayList();
    protected final List<Entity> field_72997_g = Lists.<Entity>newArrayList();
    public final List<TileEntity> loadedTileEntityList = Lists.<TileEntity>newArrayList();
    public final List<TileEntity> tickableTileEntities = Lists.<TileEntity>newArrayList();
    private final List<TileEntity> addedTileEntityList = Lists.<TileEntity>newArrayList();
    private final List<TileEntity> tileEntitiesToBeRemoved = Lists.<TileEntity>newArrayList();
    public final List<EntityPlayer> field_73010_i = Lists.<EntityPlayer>newArrayList();
    public final List<Entity> field_73007_j = Lists.<Entity>newArrayList();
    protected final IntHashMap<Entity> field_175729_l = new IntHashMap<Entity>();
    private final long field_73001_c = 16777215L;
    private int skylightSubtracted;

    /**
     * Contains the current Linear Congruential Generator seed for block updates. Used with an A value of 3 and a C
     * value of 0x3c6ef35f, producing a highly planar series of values ill-suited for choosing random blocks in a
     * 16x128x16 field.
     */
    protected int updateLCG = (new Random()).nextInt();
    protected final int DIST_HASH_MAGIC = 1013904223;
    protected float prevRainingStrength;
    protected float rainingStrength;
    protected float prevThunderingStrength;
    protected float thunderingStrength;
    private int field_73016_r;
    public final Random rand = new Random();
    public final WorldProvider dimension;
    protected PathWorldListener field_184152_t = new PathWorldListener();
    protected List<IWorldEventListener> field_73021_x;
    protected IChunkProvider chunkProvider;
    protected final ISaveHandler field_73019_z;
    protected WorldInfo worldInfo;
    protected boolean field_72987_B;
    protected MapStorage field_72988_C;
    protected VillageCollection field_72982_D;
    protected LootTableManager field_184151_B;
    protected AdvancementManager field_191951_C;
    protected FunctionManager field_193036_D;
    public final Profiler profiler;
    private final Calendar field_83016_L;
    protected Scoreboard field_96442_D;
    public final boolean isRemote;
    protected boolean field_72985_G;
    protected boolean field_72992_H;

    /**
     * True while the World is ticking , to prevent CME's if any of those ticks create more tile entities.
     */
    private boolean processingLoadedTiles;
    private final WorldBorder worldBorder;
    int[] field_72994_J;

    protected World(ISaveHandler p_i45749_1_, WorldInfo p_i45749_2_, WorldProvider p_i45749_3_, Profiler p_i45749_4_, boolean p_i45749_5_)
    {
        this.field_73021_x = Lists.newArrayList(this.field_184152_t);
        this.field_83016_L = Calendar.getInstance();
        this.field_96442_D = new Scoreboard();
        this.field_72985_G = true;
        this.field_72992_H = true;
        this.field_72994_J = new int[32768];
        this.field_73019_z = p_i45749_1_;
        this.profiler = p_i45749_4_;
        this.worldInfo = p_i45749_2_;
        this.dimension = p_i45749_3_;
        this.isRemote = p_i45749_5_;
        this.worldBorder = p_i45749_3_.createWorldBorder();
    }

    public World func_175643_b()
    {
        return this;
    }

    public Biome func_180494_b(final BlockPos p_180494_1_)
    {
        if (this.isBlockLoaded(p_180494_1_))
        {
            Chunk chunk = this.getChunkAt(p_180494_1_);

            try
            {
                return chunk.func_177411_a(p_180494_1_, this.dimension.func_177499_m());
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting biome");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Coordinates of biome request");
                crashreportcategory.addDetail("Location", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return CrashReportCategory.getCoordinateInfo(p_180494_1_);
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        else
        {
            return this.dimension.func_177499_m().func_180300_a(p_180494_1_, Biomes.PLAINS);
        }
    }

    public BiomeProvider func_72959_q()
    {
        return this.dimension.func_177499_m();
    }

    protected abstract IChunkProvider func_72970_h();

    public void func_72963_a(WorldSettings p_72963_1_)
    {
        this.worldInfo.setInitialized(true);
    }

    @Nullable
    public MinecraftServer getServer()
    {
        return null;
    }

    /**
     * Sets a new spawn location by finding an uncovered block at a random (x,z) location in the chunk.
     */
    public void setInitialSpawnLocation()
    {
        this.setSpawnPoint(new BlockPos(8, 64, 8));
    }

    public IBlockState getGroundAboveSeaLevel(BlockPos pos)
    {
        BlockPos blockpos;

        for (blockpos = new BlockPos(pos.getX(), this.getSeaLevel(), pos.getZ()); !this.isAirBlock(blockpos.up()); blockpos = blockpos.up())
        {
            ;
        }

        return this.getBlockState(blockpos);
    }

    /**
     * Check if the given BlockPos has valid coordinates
     */
    private boolean isValid(BlockPos p_175701_1_)
    {
        return !this.isOutsideBuildHeight(p_175701_1_) && p_175701_1_.getX() >= -30000000 && p_175701_1_.getZ() >= -30000000 && p_175701_1_.getX() < 30000000 && p_175701_1_.getZ() < 30000000;
    }

    private boolean isOutsideBuildHeight(BlockPos p_189509_1_)
    {
        return p_189509_1_.getY() < 0 || p_189509_1_.getY() >= 256;
    }

    /**
     * Checks to see if an air block exists at the provided location. Note that this only checks to see if the blocks
     * material is set to air, meaning it is possible for non-vanilla blocks to still pass this check.
     */
    public boolean isAirBlock(BlockPos pos)
    {
        return this.getBlockState(pos).getMaterial() == Material.AIR;
    }

    public boolean isBlockLoaded(BlockPos pos)
    {
        return this.func_175668_a(pos, true);
    }

    public boolean func_175668_a(BlockPos p_175668_1_, boolean p_175668_2_)
    {
        return this.func_175680_a(p_175668_1_.getX() >> 4, p_175668_1_.getZ() >> 4, p_175668_2_);
    }

    public boolean func_175697_a(BlockPos p_175697_1_, int p_175697_2_)
    {
        return this.func_175648_a(p_175697_1_, p_175697_2_, true);
    }

    public boolean func_175648_a(BlockPos p_175648_1_, int p_175648_2_, boolean p_175648_3_)
    {
        return this.func_175663_a(p_175648_1_.getX() - p_175648_2_, p_175648_1_.getY() - p_175648_2_, p_175648_1_.getZ() - p_175648_2_, p_175648_1_.getX() + p_175648_2_, p_175648_1_.getY() + p_175648_2_, p_175648_1_.getZ() + p_175648_2_, p_175648_3_);
    }

    public boolean isAreaLoaded(BlockPos from, BlockPos to)
    {
        return this.func_175706_a(from, to, true);
    }

    public boolean func_175706_a(BlockPos p_175706_1_, BlockPos p_175706_2_, boolean p_175706_3_)
    {
        return this.func_175663_a(p_175706_1_.getX(), p_175706_1_.getY(), p_175706_1_.getZ(), p_175706_2_.getX(), p_175706_2_.getY(), p_175706_2_.getZ(), p_175706_3_);
    }

    public boolean func_175711_a(StructureBoundingBox p_175711_1_)
    {
        return this.func_175639_b(p_175711_1_, true);
    }

    public boolean func_175639_b(StructureBoundingBox p_175639_1_, boolean p_175639_2_)
    {
        return this.func_175663_a(p_175639_1_.minX, p_175639_1_.minY, p_175639_1_.minZ, p_175639_1_.maxX, p_175639_1_.maxY, p_175639_1_.maxZ, p_175639_2_);
    }

    private boolean func_175663_a(int p_175663_1_, int p_175663_2_, int p_175663_3_, int p_175663_4_, int p_175663_5_, int p_175663_6_, boolean p_175663_7_)
    {
        if (p_175663_5_ >= 0 && p_175663_2_ < 256)
        {
            p_175663_1_ = p_175663_1_ >> 4;
            p_175663_3_ = p_175663_3_ >> 4;
            p_175663_4_ = p_175663_4_ >> 4;
            p_175663_6_ = p_175663_6_ >> 4;

            for (int i = p_175663_1_; i <= p_175663_4_; ++i)
            {
                for (int j = p_175663_3_; j <= p_175663_6_; ++j)
                {
                    if (!this.func_175680_a(i, j, p_175663_7_))
                    {
                        return false;
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected abstract boolean func_175680_a(int p_175680_1_, int p_175680_2_, boolean p_175680_3_);

    public Chunk getChunkAt(BlockPos pos)
    {
        return this.func_72964_e(pos.getX() >> 4, pos.getZ() >> 4);
    }

    public Chunk func_72964_e(int p_72964_1_, int p_72964_2_)
    {
        return this.chunkProvider.func_186025_d(p_72964_1_, p_72964_2_);
    }

    public boolean func_190526_b(int p_190526_1_, int p_190526_2_)
    {
        return this.func_175680_a(p_190526_1_, p_190526_2_, false) ? true : this.chunkProvider.func_191062_e(p_190526_1_, p_190526_2_);
    }

    /**
     * Sets a block state into this world.Flags are as follows:
     * 1 will cause a block update.
     * 2 will send the change to clients.
     * 4 will prevent the block from being re-rendered.
     * 8 will force any re-renders to run on the main thread instead
     * 16 will prevent neighbor reactions (e.g. fences connecting, observers pulsing).
     * 32 will prevent neighbor reactions from spawning drops.
     * 64 will signify the block is being moved.
     * Flags can be OR-ed
     */
    public boolean setBlockState(BlockPos pos, IBlockState newState, int flags)
    {
        if (this.isOutsideBuildHeight(pos))
        {
            return false;
        }
        else if (!this.isRemote && this.worldInfo.getGenerator() == WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            return false;
        }
        else
        {
            Chunk chunk = this.getChunkAt(pos);
            Block block = newState.getBlock();
            IBlockState iblockstate = chunk.setBlockState(pos, newState);

            if (iblockstate == null)
            {
                return false;
            }
            else
            {
                if (newState.func_185891_c() != iblockstate.func_185891_c() || newState.getLightValue() != iblockstate.getLightValue())
                {
                    this.profiler.startSection("checkLight");
                    this.func_175664_x(pos);
                    this.profiler.endSection();
                }

                if ((flags & 2) != 0 && (!this.isRemote || (flags & 4) == 0) && chunk.func_150802_k())
                {
                    this.notifyBlockUpdate(pos, iblockstate, newState, flags);
                }

                if (!this.isRemote && (flags & 1) != 0)
                {
                    this.func_175722_b(pos, iblockstate.getBlock(), true);

                    if (newState.hasComparatorInputOverride())
                    {
                        this.updateComparatorOutputLevel(pos, block);
                    }
                }
                else if (!this.isRemote && (flags & 16) == 0)
                {
                    this.func_190522_c(pos, block);
                }

                return true;
            }
        }
    }

    public boolean func_175698_g(BlockPos p_175698_1_)
    {
        return this.setBlockState(p_175698_1_, Blocks.AIR.getDefaultState(), 3);
    }

    /**
     * Sets a block to air, but also plays the sound and particles and can spawn drops
     */
    public boolean destroyBlock(BlockPos pos, boolean dropBlock)
    {
        IBlockState iblockstate = this.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (iblockstate.getMaterial() == Material.AIR)
        {
            return false;
        }
        else
        {
            this.func_175718_b(2001, pos, Block.func_176210_f(iblockstate));

            if (dropBlock)
            {
                block.func_176226_b(this, pos, iblockstate, 0);
            }

            return this.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    /**
     * Convenience method to update the block on both the client and server
     */
    public boolean setBlockState(BlockPos pos, IBlockState state)
    {
        return this.setBlockState(pos, state, 3);
    }

    /**
     * Flags are as in setBlockState
     */
    public void notifyBlockUpdate(BlockPos pos, IBlockState oldState, IBlockState newState, int flags)
    {
        for (int i = 0; i < this.field_73021_x.size(); ++i)
        {
            ((IWorldEventListener)this.field_73021_x.get(i)).notifyBlockUpdate(this, pos, oldState, newState, flags);
        }
    }

    public void func_175722_b(BlockPos p_175722_1_, Block p_175722_2_, boolean p_175722_3_)
    {
        if (this.worldInfo.getGenerator() != WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            this.func_175685_c(p_175722_1_, p_175722_2_, p_175722_3_);
        }
    }

    public void func_72975_g(int p_72975_1_, int p_72975_2_, int p_72975_3_, int p_72975_4_)
    {
        if (p_72975_3_ > p_72975_4_)
        {
            int i = p_72975_4_;
            p_72975_4_ = p_72975_3_;
            p_72975_3_ = i;
        }

        if (this.dimension.hasSkyLight())
        {
            for (int j = p_72975_3_; j <= p_72975_4_; ++j)
            {
                this.func_180500_c(EnumSkyBlock.SKY, new BlockPos(p_72975_1_, j, p_72975_2_));
            }
        }

        this.func_147458_c(p_72975_1_, p_72975_3_, p_72975_2_, p_72975_1_, p_72975_4_, p_72975_2_);
    }

    public void func_175704_b(BlockPos p_175704_1_, BlockPos p_175704_2_)
    {
        this.func_147458_c(p_175704_1_.getX(), p_175704_1_.getY(), p_175704_1_.getZ(), p_175704_2_.getX(), p_175704_2_.getY(), p_175704_2_.getZ());
    }

    public void func_147458_c(int p_147458_1_, int p_147458_2_, int p_147458_3_, int p_147458_4_, int p_147458_5_, int p_147458_6_)
    {
        for (int i = 0; i < this.field_73021_x.size(); ++i)
        {
            ((IWorldEventListener)this.field_73021_x.get(i)).markBlockRangeForRenderUpdate(p_147458_1_, p_147458_2_, p_147458_3_, p_147458_4_, p_147458_5_, p_147458_6_);
        }
    }

    public void func_190522_c(BlockPos p_190522_1_, Block p_190522_2_)
    {
        this.func_190529_b(p_190522_1_.west(), p_190522_2_, p_190522_1_);
        this.func_190529_b(p_190522_1_.east(), p_190522_2_, p_190522_1_);
        this.func_190529_b(p_190522_1_.down(), p_190522_2_, p_190522_1_);
        this.func_190529_b(p_190522_1_.up(), p_190522_2_, p_190522_1_);
        this.func_190529_b(p_190522_1_.north(), p_190522_2_, p_190522_1_);
        this.func_190529_b(p_190522_1_.south(), p_190522_2_, p_190522_1_);
    }

    public void func_175685_c(BlockPos p_175685_1_, Block p_175685_2_, boolean p_175685_3_)
    {
        this.neighborChanged(p_175685_1_.west(), p_175685_2_, p_175685_1_);
        this.neighborChanged(p_175685_1_.east(), p_175685_2_, p_175685_1_);
        this.neighborChanged(p_175685_1_.down(), p_175685_2_, p_175685_1_);
        this.neighborChanged(p_175685_1_.up(), p_175685_2_, p_175685_1_);
        this.neighborChanged(p_175685_1_.north(), p_175685_2_, p_175685_1_);
        this.neighborChanged(p_175685_1_.south(), p_175685_2_, p_175685_1_);

        if (p_175685_3_)
        {
            this.func_190522_c(p_175685_1_, p_175685_2_);
        }
    }

    public void notifyNeighborsOfStateExcept(BlockPos pos, Block blockType, EnumFacing skipSide)
    {
        if (skipSide != EnumFacing.WEST)
        {
            this.neighborChanged(pos.west(), blockType, pos);
        }

        if (skipSide != EnumFacing.EAST)
        {
            this.neighborChanged(pos.east(), blockType, pos);
        }

        if (skipSide != EnumFacing.DOWN)
        {
            this.neighborChanged(pos.down(), blockType, pos);
        }

        if (skipSide != EnumFacing.UP)
        {
            this.neighborChanged(pos.up(), blockType, pos);
        }

        if (skipSide != EnumFacing.NORTH)
        {
            this.neighborChanged(pos.north(), blockType, pos);
        }

        if (skipSide != EnumFacing.SOUTH)
        {
            this.neighborChanged(pos.south(), blockType, pos);
        }
    }

    public void neighborChanged(BlockPos pos, final Block blockIn, BlockPos fromPos)
    {
        if (!this.isRemote)
        {
            IBlockState iblockstate = this.getBlockState(pos);

            try
            {
                iblockstate.func_189546_a(this, pos, blockIn, fromPos);
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
                crashreportcategory.addDetail("Source block type", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        try
                        {
                            return String.format("ID #%d (%s // %s)", Block.func_149682_b(blockIn), blockIn.getTranslationKey(), blockIn.getClass().getCanonicalName());
                        }
                        catch (Throwable var2)
                        {
                            return "ID #" + Block.func_149682_b(blockIn);
                        }
                    }
                });
                CrashReportCategory.addBlockInfo(crashreportcategory, pos, iblockstate);
                throw new ReportedException(crashreport);
            }
        }
    }

    public void func_190529_b(BlockPos p_190529_1_, final Block p_190529_2_, BlockPos p_190529_3_)
    {
        if (!this.isRemote)
        {
            IBlockState iblockstate = this.getBlockState(p_190529_1_);

            if (iblockstate.getBlock() == Blocks.OBSERVER)
            {
                try
                {
                    ((BlockObserver)iblockstate.getBlock()).func_190962_b(iblockstate, this, p_190529_1_, p_190529_2_, p_190529_3_);
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
                    crashreportcategory.addDetail("Source block type", new ICrashReportDetail<String>()
                    {
                        public String call() throws Exception
                        {
                            try
                            {
                                return String.format("ID #%d (%s // %s)", Block.func_149682_b(p_190529_2_), p_190529_2_.getTranslationKey(), p_190529_2_.getClass().getCanonicalName());
                            }
                            catch (Throwable var2)
                            {
                                return "ID #" + Block.func_149682_b(p_190529_2_);
                            }
                        }
                    });
                    CrashReportCategory.addBlockInfo(crashreportcategory, p_190529_1_, iblockstate);
                    throw new ReportedException(crashreport);
                }
            }
        }
    }

    public boolean func_175691_a(BlockPos p_175691_1_, Block p_175691_2_)
    {
        return false;
    }

    public boolean func_175678_i(BlockPos p_175678_1_)
    {
        return this.getChunkAt(p_175678_1_).func_177444_d(p_175678_1_);
    }

    public boolean canBlockSeeSky(BlockPos pos)
    {
        if (pos.getY() >= this.getSeaLevel())
        {
            return this.func_175678_i(pos);
        }
        else
        {
            BlockPos blockpos = new BlockPos(pos.getX(), this.getSeaLevel(), pos.getZ());

            if (!this.func_175678_i(blockpos))
            {
                return false;
            }
            else
            {
                for (BlockPos blockpos1 = blockpos.down(); blockpos1.getY() > pos.getY(); blockpos1 = blockpos1.down())
                {
                    IBlockState iblockstate = this.getBlockState(blockpos1);

                    if (iblockstate.func_185891_c() > 0 && !iblockstate.getMaterial().isLiquid())
                    {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public int func_175699_k(BlockPos p_175699_1_)
    {
        if (p_175699_1_.getY() < 0)
        {
            return 0;
        }
        else
        {
            if (p_175699_1_.getY() >= 256)
            {
                p_175699_1_ = new BlockPos(p_175699_1_.getX(), 255, p_175699_1_.getZ());
            }

            return this.getChunkAt(p_175699_1_).func_177443_a(p_175699_1_, 0);
        }
    }

    public int func_175671_l(BlockPos p_175671_1_)
    {
        return this.func_175721_c(p_175671_1_, true);
    }

    public int func_175721_c(BlockPos p_175721_1_, boolean p_175721_2_)
    {
        if (p_175721_1_.getX() >= -30000000 && p_175721_1_.getZ() >= -30000000 && p_175721_1_.getX() < 30000000 && p_175721_1_.getZ() < 30000000)
        {
            if (p_175721_2_ && this.getBlockState(p_175721_1_).func_185916_f())
            {
                int i1 = this.func_175721_c(p_175721_1_.up(), false);
                int i = this.func_175721_c(p_175721_1_.east(), false);
                int j = this.func_175721_c(p_175721_1_.west(), false);
                int k = this.func_175721_c(p_175721_1_.south(), false);
                int l = this.func_175721_c(p_175721_1_.north(), false);

                if (i > i1)
                {
                    i1 = i;
                }

                if (j > i1)
                {
                    i1 = j;
                }

                if (k > i1)
                {
                    i1 = k;
                }

                if (l > i1)
                {
                    i1 = l;
                }

                return i1;
            }
            else if (p_175721_1_.getY() < 0)
            {
                return 0;
            }
            else
            {
                if (p_175721_1_.getY() >= 256)
                {
                    p_175721_1_ = new BlockPos(p_175721_1_.getX(), 255, p_175721_1_.getZ());
                }

                Chunk chunk = this.getChunkAt(p_175721_1_);
                return chunk.func_177443_a(p_175721_1_, this.skylightSubtracted);
            }
        }
        else
        {
            return 15;
        }
    }

    public BlockPos func_175645_m(BlockPos p_175645_1_)
    {
        return new BlockPos(p_175645_1_.getX(), this.func_189649_b(p_175645_1_.getX(), p_175645_1_.getZ()), p_175645_1_.getZ());
    }

    public int func_189649_b(int p_189649_1_, int p_189649_2_)
    {
        int i;

        if (p_189649_1_ >= -30000000 && p_189649_2_ >= -30000000 && p_189649_1_ < 30000000 && p_189649_2_ < 30000000)
        {
            if (this.func_175680_a(p_189649_1_ >> 4, p_189649_2_ >> 4, true))
            {
                i = this.func_72964_e(p_189649_1_ >> 4, p_189649_2_ >> 4).func_76611_b(p_189649_1_ & 15, p_189649_2_ & 15);
            }
            else
            {
                i = 0;
            }
        }
        else
        {
            i = this.getSeaLevel() + 1;
        }

        return i;
    }

    @Deprecated
    public int func_82734_g(int p_82734_1_, int p_82734_2_)
    {
        if (p_82734_1_ >= -30000000 && p_82734_2_ >= -30000000 && p_82734_1_ < 30000000 && p_82734_2_ < 30000000)
        {
            if (!this.func_175680_a(p_82734_1_ >> 4, p_82734_2_ >> 4, true))
            {
                return 0;
            }
            else
            {
                Chunk chunk = this.func_72964_e(p_82734_1_ >> 4, p_82734_2_ >> 4);
                return chunk.func_177442_v();
            }
        }
        else
        {
            return this.getSeaLevel() + 1;
        }
    }

    public int func_175705_a(EnumSkyBlock p_175705_1_, BlockPos p_175705_2_)
    {
        if (!this.dimension.hasSkyLight() && p_175705_1_ == EnumSkyBlock.SKY)
        {
            return 0;
        }
        else
        {
            if (p_175705_2_.getY() < 0)
            {
                p_175705_2_ = new BlockPos(p_175705_2_.getX(), 0, p_175705_2_.getZ());
            }

            if (!this.isValid(p_175705_2_))
            {
                return p_175705_1_.defaultLightValue;
            }
            else if (!this.isBlockLoaded(p_175705_2_))
            {
                return p_175705_1_.defaultLightValue;
            }
            else if (this.getBlockState(p_175705_2_).func_185916_f())
            {
                int i1 = this.func_175642_b(p_175705_1_, p_175705_2_.up());
                int i = this.func_175642_b(p_175705_1_, p_175705_2_.east());
                int j = this.func_175642_b(p_175705_1_, p_175705_2_.west());
                int k = this.func_175642_b(p_175705_1_, p_175705_2_.south());
                int l = this.func_175642_b(p_175705_1_, p_175705_2_.north());

                if (i > i1)
                {
                    i1 = i;
                }

                if (j > i1)
                {
                    i1 = j;
                }

                if (k > i1)
                {
                    i1 = k;
                }

                if (l > i1)
                {
                    i1 = l;
                }

                return i1;
            }
            else
            {
                Chunk chunk = this.getChunkAt(p_175705_2_);
                return chunk.func_177413_a(p_175705_1_, p_175705_2_);
            }
        }
    }

    public int func_175642_b(EnumSkyBlock p_175642_1_, BlockPos p_175642_2_)
    {
        if (p_175642_2_.getY() < 0)
        {
            p_175642_2_ = new BlockPos(p_175642_2_.getX(), 0, p_175642_2_.getZ());
        }

        if (!this.isValid(p_175642_2_))
        {
            return p_175642_1_.defaultLightValue;
        }
        else if (!this.isBlockLoaded(p_175642_2_))
        {
            return p_175642_1_.defaultLightValue;
        }
        else
        {
            Chunk chunk = this.getChunkAt(p_175642_2_);
            return chunk.func_177413_a(p_175642_1_, p_175642_2_);
        }
    }

    public void func_175653_a(EnumSkyBlock p_175653_1_, BlockPos p_175653_2_, int p_175653_3_)
    {
        if (this.isValid(p_175653_2_))
        {
            if (this.isBlockLoaded(p_175653_2_))
            {
                Chunk chunk = this.getChunkAt(p_175653_2_);
                chunk.func_177431_a(p_175653_1_, p_175653_2_, p_175653_3_);
                this.func_175679_n(p_175653_2_);
            }
        }
    }

    public void func_175679_n(BlockPos p_175679_1_)
    {
        for (int i = 0; i < this.field_73021_x.size(); ++i)
        {
            ((IWorldEventListener)this.field_73021_x.get(i)).func_174959_b(p_175679_1_);
        }
    }

    public int func_175626_b(BlockPos p_175626_1_, int p_175626_2_)
    {
        int i = this.func_175705_a(EnumSkyBlock.SKY, p_175626_1_);
        int j = this.func_175705_a(EnumSkyBlock.BLOCK, p_175626_1_);

        if (j < p_175626_2_)
        {
            j = p_175626_2_;
        }

        return i << 20 | j << 4;
    }

    public float func_175724_o(BlockPos p_175724_1_)
    {
        return this.dimension.func_177497_p()[this.func_175671_l(p_175724_1_)];
    }

    public IBlockState getBlockState(BlockPos pos)
    {
        if (this.isOutsideBuildHeight(pos))
        {
            return Blocks.AIR.getDefaultState();
        }
        else
        {
            Chunk chunk = this.getChunkAt(pos);
            return chunk.func_177435_g(pos);
        }
    }

    /**
     * Checks whether its daytime by seeing if the light subtracted from the skylight is less than 4. Always returns
     * true on the client because vanilla has no need for it on the client, therefore it is not synced to the client
     */
    public boolean isDaytime()
    {
        return this.skylightSubtracted < 4;
    }

    @Nullable
    public RayTraceResult func_72933_a(Vec3d p_72933_1_, Vec3d p_72933_2_)
    {
        return this.func_147447_a(p_72933_1_, p_72933_2_, false, false, false);
    }

    @Nullable
    public RayTraceResult func_72901_a(Vec3d p_72901_1_, Vec3d p_72901_2_, boolean p_72901_3_)
    {
        return this.func_147447_a(p_72901_1_, p_72901_2_, p_72901_3_, false, false);
    }

    @Nullable
    public RayTraceResult func_147447_a(Vec3d p_147447_1_, Vec3d p_147447_2_, boolean p_147447_3_, boolean p_147447_4_, boolean p_147447_5_)
    {
        if (!Double.isNaN(p_147447_1_.x) && !Double.isNaN(p_147447_1_.y) && !Double.isNaN(p_147447_1_.z))
        {
            if (!Double.isNaN(p_147447_2_.x) && !Double.isNaN(p_147447_2_.y) && !Double.isNaN(p_147447_2_.z))
            {
                int i = MathHelper.floor(p_147447_2_.x);
                int j = MathHelper.floor(p_147447_2_.y);
                int k = MathHelper.floor(p_147447_2_.z);
                int l = MathHelper.floor(p_147447_1_.x);
                int i1 = MathHelper.floor(p_147447_1_.y);
                int j1 = MathHelper.floor(p_147447_1_.z);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = this.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if ((!p_147447_4_ || iblockstate.func_185890_d(this, blockpos) != Block.field_185506_k) && block.func_176209_a(iblockstate, p_147447_3_))
                {
                    RayTraceResult raytraceresult = iblockstate.func_185910_a(this, blockpos, p_147447_1_, p_147447_2_);

                    if (raytraceresult != null)
                    {
                        return raytraceresult;
                    }
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(p_147447_1_.x) || Double.isNaN(p_147447_1_.y) || Double.isNaN(p_147447_1_.z))
                    {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k)
                    {
                        return p_147447_5_ ? raytraceresult2 : null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l)
                    {
                        d0 = (double)l + 1.0D;
                    }
                    else if (i < l)
                    {
                        d0 = (double)l + 0.0D;
                    }
                    else
                    {
                        flag2 = false;
                    }

                    if (j > i1)
                    {
                        d1 = (double)i1 + 1.0D;
                    }
                    else if (j < i1)
                    {
                        d1 = (double)i1 + 0.0D;
                    }
                    else
                    {
                        flag = false;
                    }

                    if (k > j1)
                    {
                        d2 = (double)j1 + 1.0D;
                    }
                    else if (k < j1)
                    {
                        d2 = (double)j1 + 0.0D;
                    }
                    else
                    {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = p_147447_2_.x - p_147447_1_.x;
                    double d7 = p_147447_2_.y - p_147447_1_.y;
                    double d8 = p_147447_2_.z - p_147447_1_.z;

                    if (flag2)
                    {
                        d3 = (d0 - p_147447_1_.x) / d6;
                    }

                    if (flag)
                    {
                        d4 = (d1 - p_147447_1_.y) / d7;
                    }

                    if (flag1)
                    {
                        d5 = (d2 - p_147447_1_.z) / d8;
                    }

                    if (d3 == -0.0D)
                    {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D)
                    {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D)
                    {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        p_147447_1_ = new Vec3d(d0, p_147447_1_.y + d7 * d3, p_147447_1_.z + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        p_147447_1_ = new Vec3d(p_147447_1_.x + d6 * d4, d1, p_147447_1_.z + d8 * d4);
                    }
                    else
                    {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        p_147447_1_ = new Vec3d(p_147447_1_.x + d6 * d5, p_147447_1_.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(p_147447_1_.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(p_147447_1_.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(p_147447_1_.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = this.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if (!p_147447_4_ || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.func_185890_d(this, blockpos) != Block.field_185506_k)
                    {
                        if (block1.func_176209_a(iblockstate1, p_147447_3_))
                        {
                            RayTraceResult raytraceresult1 = iblockstate1.func_185910_a(this, blockpos, p_147447_1_, p_147447_2_);

                            if (raytraceresult1 != null)
                            {
                                return raytraceresult1;
                            }
                        }
                        else
                        {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, p_147447_1_, enumfacing, blockpos);
                        }
                    }
                }

                return p_147447_5_ ? raytraceresult2 : null;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Plays a sound. On the server, the sound is broadcast to all nearby <em>except</em> the given player. On the
     * client, the sound only plays if the given player is the client player. Thus, this method is intended to be called
     * from code running on both sides. The client plays it locally and the server plays it for everyone else.
     */
    public void playSound(@Nullable EntityPlayer player, BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch)
    {
        this.playSound(player, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, soundIn, category, volume, pitch);
    }

    public void playSound(@Nullable EntityPlayer player, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch)
    {
        for (int i = 0; i < this.field_73021_x.size(); ++i)
        {
            ((IWorldEventListener)this.field_73021_x.get(i)).func_184375_a(player, soundIn, category, x, y, z, volume, pitch);
        }
    }

    public void playSound(double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay)
    {
    }

    public void func_184149_a(BlockPos p_184149_1_, @Nullable SoundEvent p_184149_2_)
    {
        for (int i = 0; i < this.field_73021_x.size(); ++i)
        {
            ((IWorldEventListener)this.field_73021_x.get(i)).playRecord(p_184149_2_, p_184149_1_);
        }
    }

    public void func_175688_a(EnumParticleTypes p_175688_1_, double p_175688_2_, double p_175688_4_, double p_175688_6_, double p_175688_8_, double p_175688_10_, double p_175688_12_, int... p_175688_14_)
    {
        this.func_175720_a(p_175688_1_.func_179348_c(), p_175688_1_.func_179344_e(), p_175688_2_, p_175688_4_, p_175688_6_, p_175688_8_, p_175688_10_, p_175688_12_, p_175688_14_);
    }

    public void func_190523_a(int p_190523_1_, double p_190523_2_, double p_190523_4_, double p_190523_6_, double p_190523_8_, double p_190523_10_, double p_190523_12_, int... p_190523_14_)
    {
        for (int i = 0; i < this.field_73021_x.size(); ++i)
        {
            ((IWorldEventListener)this.field_73021_x.get(i)).func_190570_a(p_190523_1_, false, true, p_190523_2_, p_190523_4_, p_190523_6_, p_190523_8_, p_190523_10_, p_190523_12_, p_190523_14_);
        }
    }

    public void func_175682_a(EnumParticleTypes p_175682_1_, boolean p_175682_2_, double p_175682_3_, double p_175682_5_, double p_175682_7_, double p_175682_9_, double p_175682_11_, double p_175682_13_, int... p_175682_15_)
    {
        this.func_175720_a(p_175682_1_.func_179348_c(), p_175682_1_.func_179344_e() || p_175682_2_, p_175682_3_, p_175682_5_, p_175682_7_, p_175682_9_, p_175682_11_, p_175682_13_, p_175682_15_);
    }

    private void func_175720_a(int p_175720_1_, boolean p_175720_2_, double p_175720_3_, double p_175720_5_, double p_175720_7_, double p_175720_9_, double p_175720_11_, double p_175720_13_, int... p_175720_15_)
    {
        for (int i = 0; i < this.field_73021_x.size(); ++i)
        {
            ((IWorldEventListener)this.field_73021_x.get(i)).func_180442_a(p_175720_1_, p_175720_2_, p_175720_3_, p_175720_5_, p_175720_7_, p_175720_9_, p_175720_11_, p_175720_13_, p_175720_15_);
        }
    }

    public boolean func_72942_c(Entity p_72942_1_)
    {
        this.field_73007_j.add(p_72942_1_);
        return true;
    }

    /**
     * Called when an entity is spawned in the world. This includes players.
     */
    public boolean addEntity0(Entity entityIn)
    {
        int i = MathHelper.floor(entityIn.posX / 16.0D);
        int j = MathHelper.floor(entityIn.posZ / 16.0D);
        boolean flag = entityIn.forceSpawn;

        if (entityIn instanceof EntityPlayer)
        {
            flag = true;
        }

        if (!flag && !this.func_175680_a(i, j, false))
        {
            return false;
        }
        else
        {
            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                this.field_73010_i.add(entityplayer);
                this.updateAllPlayersSleepingFlag();
            }

            this.func_72964_e(i, j).addEntity(entityIn);
            this.field_72996_f.add(entityIn);
            this.func_72923_a(entityIn);
            return true;
        }
    }

    protected void func_72923_a(Entity p_72923_1_)
    {
        for (int i = 0; i < this.field_73021_x.size(); ++i)
        {
            ((IWorldEventListener)this.field_73021_x.get(i)).func_72703_a(p_72923_1_);
        }
    }

    protected void func_72847_b(Entity p_72847_1_)
    {
        for (int i = 0; i < this.field_73021_x.size(); ++i)
        {
            ((IWorldEventListener)this.field_73021_x.get(i)).func_72709_b(p_72847_1_);
        }
    }

    public void func_72900_e(Entity p_72900_1_)
    {
        if (p_72900_1_.isBeingRidden())
        {
            p_72900_1_.removePassengers();
        }

        if (p_72900_1_.isPassenger())
        {
            p_72900_1_.stopRiding();
        }

        p_72900_1_.remove();

        if (p_72900_1_ instanceof EntityPlayer)
        {
            this.field_73010_i.remove(p_72900_1_);
            this.updateAllPlayersSleepingFlag();
            this.func_72847_b(p_72900_1_);
        }
    }

    public void func_72973_f(Entity p_72973_1_)
    {
        p_72973_1_.func_184174_b(false);
        p_72973_1_.remove();

        if (p_72973_1_ instanceof EntityPlayer)
        {
            this.field_73010_i.remove(p_72973_1_);
            this.updateAllPlayersSleepingFlag();
        }

        int i = p_72973_1_.chunkCoordX;
        int j = p_72973_1_.chunkCoordZ;

        if (p_72973_1_.addedToChunk && this.func_175680_a(i, j, true))
        {
            this.func_72964_e(i, j).removeEntity(p_72973_1_);
        }

        this.field_72996_f.remove(p_72973_1_);
        this.func_72847_b(p_72973_1_);
    }

    public void func_72954_a(IWorldEventListener p_72954_1_)
    {
        this.field_73021_x.add(p_72954_1_);
    }

    public void func_72848_b(IWorldEventListener p_72848_1_)
    {
        this.field_73021_x.remove(p_72848_1_);
    }

    private boolean func_191504_a(@Nullable Entity p_191504_1_, AxisAlignedBB p_191504_2_, boolean p_191504_3_, @Nullable List<AxisAlignedBB> p_191504_4_)
    {
        int i = MathHelper.floor(p_191504_2_.minX) - 1;
        int j = MathHelper.ceil(p_191504_2_.maxX) + 1;
        int k = MathHelper.floor(p_191504_2_.minY) - 1;
        int l = MathHelper.ceil(p_191504_2_.maxY) + 1;
        int i1 = MathHelper.floor(p_191504_2_.minZ) - 1;
        int j1 = MathHelper.ceil(p_191504_2_.maxZ) + 1;
        WorldBorder worldborder = this.getWorldBorder();
        boolean flag = p_191504_1_ != null && p_191504_1_.func_174832_aS();
        boolean flag1 = p_191504_1_ != null && this.func_191503_g(p_191504_1_);
        IBlockState iblockstate = Blocks.STONE.getDefaultState();
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        try
        {
            for (int k1 = i; k1 < j; ++k1)
            {
                for (int l1 = i1; l1 < j1; ++l1)
                {
                    boolean flag2 = k1 == i || k1 == j - 1;
                    boolean flag3 = l1 == i1 || l1 == j1 - 1;

                    if ((!flag2 || !flag3) && this.isBlockLoaded(blockpos$pooledmutableblockpos.setPos(k1, 64, l1)))
                    {
                        for (int i2 = k; i2 < l; ++i2)
                        {
                            if (!flag2 && !flag3 || i2 != l - 1)
                            {
                                if (p_191504_3_)
                                {
                                    if (k1 < -30000000 || k1 >= 30000000 || l1 < -30000000 || l1 >= 30000000)
                                    {
                                        boolean lvt_21_2_ = true;
                                        return lvt_21_2_;
                                    }
                                }
                                else if (p_191504_1_ != null && flag == flag1)
                                {
                                    p_191504_1_.func_174821_h(!flag1);
                                }

                                blockpos$pooledmutableblockpos.setPos(k1, i2, l1);
                                IBlockState iblockstate1;

                                if (!p_191504_3_ && !worldborder.contains(blockpos$pooledmutableblockpos) && flag1)
                                {
                                    iblockstate1 = iblockstate;
                                }
                                else
                                {
                                    iblockstate1 = this.getBlockState(blockpos$pooledmutableblockpos);
                                }

                                iblockstate1.func_185908_a(this, blockpos$pooledmutableblockpos, p_191504_2_, p_191504_4_, p_191504_1_, false);

                                if (p_191504_3_ && !p_191504_4_.isEmpty())
                                {
                                    boolean flag5 = true;
                                    return flag5;
                                }
                            }
                        }
                    }
                }
            }
        }
        finally
        {
            blockpos$pooledmutableblockpos.func_185344_t();
        }

        return !p_191504_4_.isEmpty();
    }

    public List<AxisAlignedBB> func_184144_a(@Nullable Entity p_184144_1_, AxisAlignedBB p_184144_2_)
    {
        List<AxisAlignedBB> list = Lists.<AxisAlignedBB>newArrayList();
        this.func_191504_a(p_184144_1_, p_184144_2_, false, list);

        if (p_184144_1_ != null)
        {
            List<Entity> list1 = this.getEntitiesWithinAABBExcludingEntity(p_184144_1_, p_184144_2_.grow(0.25D));

            for (int i = 0; i < list1.size(); ++i)
            {
                Entity entity = list1.get(i);

                if (!p_184144_1_.isRidingSameEntity(entity))
                {
                    AxisAlignedBB axisalignedbb = entity.getCollisionBoundingBox();

                    if (axisalignedbb != null && axisalignedbb.intersects(p_184144_2_))
                    {
                        list.add(axisalignedbb);
                    }

                    axisalignedbb = p_184144_1_.getCollisionBox(entity);

                    if (axisalignedbb != null && axisalignedbb.intersects(p_184144_2_))
                    {
                        list.add(axisalignedbb);
                    }
                }
            }
        }

        return list;
    }

    public boolean func_191503_g(Entity p_191503_1_)
    {
        double d0 = this.worldBorder.minX();
        double d1 = this.worldBorder.minZ();
        double d2 = this.worldBorder.maxX();
        double d3 = this.worldBorder.maxZ();

        if (p_191503_1_.func_174832_aS())
        {
            ++d0;
            ++d1;
            --d2;
            --d3;
        }
        else
        {
            --d0;
            --d1;
            ++d2;
            ++d3;
        }

        return p_191503_1_.posX > d0 && p_191503_1_.posX < d2 && p_191503_1_.posZ > d1 && p_191503_1_.posZ < d3;
    }

    public boolean func_184143_b(AxisAlignedBB p_184143_1_)
    {
        return this.func_191504_a((Entity)null, p_184143_1_, true, Lists.newArrayList());
    }

    public int func_72967_a(float p_72967_1_)
    {
        float f = this.getCelestialAngle(p_72967_1_);
        float f1 = 1.0F - (MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F);
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        f1 = (float)((double)f1 * (1.0D - (double)(this.getRainStrength(p_72967_1_) * 5.0F) / 16.0D));
        f1 = (float)((double)f1 * (1.0D - (double)(this.getThunderStrength(p_72967_1_) * 5.0F) / 16.0D));
        f1 = 1.0F - f1;
        return (int)(f1 * 11.0F);
    }

    public float func_72971_b(float p_72971_1_)
    {
        float f = this.getCelestialAngle(p_72971_1_);
        float f1 = 1.0F - (MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.2F);
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        f1 = 1.0F - f1;
        f1 = (float)((double)f1 * (1.0D - (double)(this.getRainStrength(p_72971_1_) * 5.0F) / 16.0D));
        f1 = (float)((double)f1 * (1.0D - (double)(this.getThunderStrength(p_72971_1_) * 5.0F) / 16.0D));
        return f1 * 0.8F + 0.2F;
    }

    public Vec3d func_72833_a(Entity p_72833_1_, float p_72833_2_)
    {
        float f = this.getCelestialAngle(p_72833_2_);
        float f1 = MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        int i = MathHelper.floor(p_72833_1_.posX);
        int j = MathHelper.floor(p_72833_1_.posY);
        int k = MathHelper.floor(p_72833_1_.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        Biome biome = this.func_180494_b(blockpos);
        float f2 = biome.getTemperatureRaw(blockpos);
        int l = biome.func_76731_a(f2);
        float f3 = (float)(l >> 16 & 255) / 255.0F;
        float f4 = (float)(l >> 8 & 255) / 255.0F;
        float f5 = (float)(l & 255) / 255.0F;
        f3 = f3 * f1;
        f4 = f4 * f1;
        f5 = f5 * f1;
        float f6 = this.getRainStrength(p_72833_2_);

        if (f6 > 0.0F)
        {
            float f7 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.6F;
            float f8 = 1.0F - f6 * 0.75F;
            f3 = f3 * f8 + f7 * (1.0F - f8);
            f4 = f4 * f8 + f7 * (1.0F - f8);
            f5 = f5 * f8 + f7 * (1.0F - f8);
        }

        float f10 = this.getThunderStrength(p_72833_2_);

        if (f10 > 0.0F)
        {
            float f11 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.2F;
            float f9 = 1.0F - f10 * 0.75F;
            f3 = f3 * f9 + f11 * (1.0F - f9);
            f4 = f4 * f9 + f11 * (1.0F - f9);
            f5 = f5 * f9 + f11 * (1.0F - f9);
        }

        if (this.field_73016_r > 0)
        {
            float f12 = (float)this.field_73016_r - p_72833_2_;

            if (f12 > 1.0F)
            {
                f12 = 1.0F;
            }

            f12 = f12 * 0.45F;
            f3 = f3 * (1.0F - f12) + 0.8F * f12;
            f4 = f4 * (1.0F - f12) + 0.8F * f12;
            f5 = f5 * (1.0F - f12) + 1.0F * f12;
        }

        return new Vec3d((double)f3, (double)f4, (double)f5);
    }

    /**
     * calls calculateCelestialAngle
     */
    public float getCelestialAngle(float partialTicks)
    {
        return this.dimension.calculateCelestialAngle(this.worldInfo.getDayTime(), partialTicks);
    }

    public int getMoonPhase()
    {
        return this.dimension.getMoonPhase(this.worldInfo.getDayTime());
    }

    /**
     * gets the current fullness of the moon expressed as a float between 1.0 and 0.0, in steps of .25
     */
    public float getCurrentMoonPhaseFactor()
    {
        return WorldProvider.MOON_PHASE_FACTORS[this.dimension.getMoonPhase(this.worldInfo.getDayTime())];
    }

    /**
     * Return getCelestialAngle()*2*PI
     */
    public float getCelestialAngleRadians(float partialTicks)
    {
        float f = this.getCelestialAngle(partialTicks);
        return f * ((float)Math.PI * 2F);
    }

    public Vec3d func_72824_f(float p_72824_1_)
    {
        float f = this.getCelestialAngle(p_72824_1_);
        float f1 = MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        float f2 = 1.0F;
        float f3 = 1.0F;
        float f4 = 1.0F;
        float f5 = this.getRainStrength(p_72824_1_);

        if (f5 > 0.0F)
        {
            float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
            float f7 = 1.0F - f5 * 0.95F;
            f2 = f2 * f7 + f6 * (1.0F - f7);
            f3 = f3 * f7 + f6 * (1.0F - f7);
            f4 = f4 * f7 + f6 * (1.0F - f7);
        }

        f2 = f2 * (f1 * 0.9F + 0.1F);
        f3 = f3 * (f1 * 0.9F + 0.1F);
        f4 = f4 * (f1 * 0.85F + 0.15F);
        float f9 = this.getThunderStrength(p_72824_1_);

        if (f9 > 0.0F)
        {
            float f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
            float f8 = 1.0F - f9 * 0.95F;
            f2 = f2 * f8 + f10 * (1.0F - f8);
            f3 = f3 * f8 + f10 * (1.0F - f8);
            f4 = f4 * f8 + f10 * (1.0F - f8);
        }

        return new Vec3d((double)f2, (double)f3, (double)f4);
    }

    public Vec3d func_72948_g(float p_72948_1_)
    {
        float f = this.getCelestialAngle(p_72948_1_);
        return this.dimension.getFogColor(f, p_72948_1_);
    }

    public BlockPos func_175725_q(BlockPos p_175725_1_)
    {
        return this.getChunkAt(p_175725_1_).func_177440_h(p_175725_1_);
    }

    public BlockPos func_175672_r(BlockPos p_175672_1_)
    {
        Chunk chunk = this.getChunkAt(p_175672_1_);
        BlockPos blockpos;
        BlockPos blockpos1;

        for (blockpos = new BlockPos(p_175672_1_.getX(), chunk.getTopFilledSegment() + 16, p_175672_1_.getZ()); blockpos.getY() >= 0; blockpos = blockpos1)
        {
            blockpos1 = blockpos.down();
            Material material = chunk.func_177435_g(blockpos1).getMaterial();

            if (material.blocksMovement() && material != Material.LEAVES)
            {
                break;
            }
        }

        return blockpos;
    }

    public float func_72880_h(float p_72880_1_)
    {
        float f = this.getCelestialAngle(p_72880_1_);
        float f1 = 1.0F - (MathHelper.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.25F);
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        return f1 * f1 * 0.5F;
    }

    public boolean func_184145_b(BlockPos p_184145_1_, Block p_184145_2_)
    {
        return true;
    }

    public void func_175684_a(BlockPos p_175684_1_, Block p_175684_2_, int p_175684_3_)
    {
    }

    public void func_175654_a(BlockPos p_175654_1_, Block p_175654_2_, int p_175654_3_, int p_175654_4_)
    {
    }

    public void func_180497_b(BlockPos p_180497_1_, Block p_180497_2_, int p_180497_3_, int p_180497_4_)
    {
    }

    public void func_72939_s()
    {
        this.profiler.startSection("entities");
        this.profiler.startSection("global");

        for (int i = 0; i < this.field_73007_j.size(); ++i)
        {
            Entity entity = this.field_73007_j.get(i);

            try
            {
                ++entity.ticksExisted;
                entity.tick();
            }
            catch (Throwable throwable2)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable2, "Ticking entity");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being ticked");

                if (entity == null)
                {
                    crashreportcategory.addDetail("Entity", "~~NULL~~");
                }
                else
                {
                    entity.fillCrashReport(crashreportcategory);
                }

                throw new ReportedException(crashreport);
            }

            if (entity.removed)
            {
                this.field_73007_j.remove(i--);
            }
        }

        this.profiler.func_76318_c("remove");
        this.field_72996_f.removeAll(this.field_72997_g);

        for (int k = 0; k < this.field_72997_g.size(); ++k)
        {
            Entity entity1 = this.field_72997_g.get(k);
            int j = entity1.chunkCoordX;
            int k1 = entity1.chunkCoordZ;

            if (entity1.addedToChunk && this.func_175680_a(j, k1, true))
            {
                this.func_72964_e(j, k1).removeEntity(entity1);
            }
        }

        for (int l = 0; l < this.field_72997_g.size(); ++l)
        {
            this.func_72847_b(this.field_72997_g.get(l));
        }

        this.field_72997_g.clear();
        this.func_184147_l();
        this.profiler.func_76318_c("regular");

        for (int i1 = 0; i1 < this.field_72996_f.size(); ++i1)
        {
            Entity entity2 = this.field_72996_f.get(i1);
            Entity entity3 = entity2.getRidingEntity();

            if (entity3 != null)
            {
                if (!entity3.removed && entity3.isPassenger(entity2))
                {
                    continue;
                }

                entity2.stopRiding();
            }

            this.profiler.startSection("tick");

            if (!entity2.removed && !(entity2 instanceof EntityPlayerMP))
            {
                try
                {
                    this.func_72870_g(entity2);
                }
                catch (Throwable throwable1)
                {
                    CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Ticking entity");
                    CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Entity being ticked");
                    entity2.fillCrashReport(crashreportcategory1);
                    throw new ReportedException(crashreport1);
                }
            }

            this.profiler.endSection();
            this.profiler.startSection("remove");

            if (entity2.removed)
            {
                int l1 = entity2.chunkCoordX;
                int i2 = entity2.chunkCoordZ;

                if (entity2.addedToChunk && this.func_175680_a(l1, i2, true))
                {
                    this.func_72964_e(l1, i2).removeEntity(entity2);
                }

                this.field_72996_f.remove(i1--);
                this.func_72847_b(entity2);
            }

            this.profiler.endSection();
        }

        this.profiler.func_76318_c("blockEntities");

        if (!this.tileEntitiesToBeRemoved.isEmpty())
        {
            this.tickableTileEntities.removeAll(this.tileEntitiesToBeRemoved);
            this.loadedTileEntityList.removeAll(this.tileEntitiesToBeRemoved);
            this.tileEntitiesToBeRemoved.clear();
        }

        this.processingLoadedTiles = true;
        Iterator<TileEntity> iterator = this.tickableTileEntities.iterator();

        while (iterator.hasNext())
        {
            TileEntity tileentity = iterator.next();

            if (!tileentity.isRemoved() && tileentity.hasWorld())
            {
                BlockPos blockpos = tileentity.getPos();

                if (this.isBlockLoaded(blockpos) && this.worldBorder.contains(blockpos))
                {
                    try
                    {
                        this.profiler.startSection(() ->
                        {
                            return String.valueOf((Object)TileEntity.func_190559_a(tileentity.getClass()));
                        });
                        ((ITickable)tileentity).tick();
                        this.profiler.endSection();
                    }
                    catch (Throwable throwable)
                    {
                        CrashReport crashreport2 = CrashReport.makeCrashReport(throwable, "Ticking block entity");
                        CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Block entity being ticked");
                        tileentity.addInfoToCrashReport(crashreportcategory2);
                        throw new ReportedException(crashreport2);
                    }
                }
            }

            if (tileentity.isRemoved())
            {
                iterator.remove();
                this.loadedTileEntityList.remove(tileentity);

                if (this.isBlockLoaded(tileentity.getPos()))
                {
                    this.getChunkAt(tileentity.getPos()).removeTileEntity(tileentity.getPos());
                }
            }
        }

        this.processingLoadedTiles = false;
        this.profiler.func_76318_c("pendingBlockEntities");

        if (!this.addedTileEntityList.isEmpty())
        {
            for (int j1 = 0; j1 < this.addedTileEntityList.size(); ++j1)
            {
                TileEntity tileentity1 = this.addedTileEntityList.get(j1);

                if (!tileentity1.isRemoved())
                {
                    if (!this.loadedTileEntityList.contains(tileentity1))
                    {
                        this.addTileEntity(tileentity1);
                    }

                    if (this.isBlockLoaded(tileentity1.getPos()))
                    {
                        Chunk chunk = this.getChunkAt(tileentity1.getPos());
                        IBlockState iblockstate = chunk.func_177435_g(tileentity1.getPos());
                        chunk.addTileEntity(tileentity1.getPos(), tileentity1);
                        this.notifyBlockUpdate(tileentity1.getPos(), iblockstate, iblockstate, 3);
                    }
                }
            }

            this.addedTileEntityList.clear();
        }

        this.profiler.endSection();
        this.profiler.endSection();
    }

    protected void func_184147_l()
    {
    }

    public boolean addTileEntity(TileEntity tile)
    {
        boolean flag = this.loadedTileEntityList.add(tile);

        if (flag && tile instanceof ITickable)
        {
            this.tickableTileEntities.add(tile);
        }

        if (this.isRemote)
        {
            BlockPos blockpos1 = tile.getPos();
            IBlockState iblockstate1 = this.getBlockState(blockpos1);
            this.notifyBlockUpdate(blockpos1, iblockstate1, iblockstate1, 2);
        }

        return flag;
    }

    public void addTileEntities(Collection<TileEntity> tileEntityCollection)
    {
        if (this.processingLoadedTiles)
        {
            this.addedTileEntityList.addAll(tileEntityCollection);
        }
        else
        {
            for (TileEntity tileentity2 : tileEntityCollection)
            {
                this.addTileEntity(tileentity2);
            }
        }
    }

    public void func_72870_g(Entity p_72870_1_)
    {
        this.func_72866_a(p_72870_1_, true);
    }

    public void func_72866_a(Entity p_72866_1_, boolean p_72866_2_)
    {
        if (!(p_72866_1_ instanceof EntityPlayer))
        {
            int j2 = MathHelper.floor(p_72866_1_.posX);
            int k2 = MathHelper.floor(p_72866_1_.posZ);
            int l2 = 32;

            if (p_72866_2_ && !this.func_175663_a(j2 - 32, 0, k2 - 32, j2 + 32, 0, k2 + 32, true))
            {
                return;
            }
        }

        p_72866_1_.lastTickPosX = p_72866_1_.posX;
        p_72866_1_.lastTickPosY = p_72866_1_.posY;
        p_72866_1_.lastTickPosZ = p_72866_1_.posZ;
        p_72866_1_.prevRotationYaw = p_72866_1_.rotationYaw;
        p_72866_1_.prevRotationPitch = p_72866_1_.rotationPitch;

        if (p_72866_2_ && p_72866_1_.addedToChunk)
        {
            ++p_72866_1_.ticksExisted;

            if (p_72866_1_.isPassenger())
            {
                p_72866_1_.updateRidden();
            }
            else
            {
                p_72866_1_.tick();
            }
        }

        this.profiler.startSection("chunkCheck");

        if (Double.isNaN(p_72866_1_.posX) || Double.isInfinite(p_72866_1_.posX))
        {
            p_72866_1_.posX = p_72866_1_.lastTickPosX;
        }

        if (Double.isNaN(p_72866_1_.posY) || Double.isInfinite(p_72866_1_.posY))
        {
            p_72866_1_.posY = p_72866_1_.lastTickPosY;
        }

        if (Double.isNaN(p_72866_1_.posZ) || Double.isInfinite(p_72866_1_.posZ))
        {
            p_72866_1_.posZ = p_72866_1_.lastTickPosZ;
        }

        if (Double.isNaN((double)p_72866_1_.rotationPitch) || Double.isInfinite((double)p_72866_1_.rotationPitch))
        {
            p_72866_1_.rotationPitch = p_72866_1_.prevRotationPitch;
        }

        if (Double.isNaN((double)p_72866_1_.rotationYaw) || Double.isInfinite((double)p_72866_1_.rotationYaw))
        {
            p_72866_1_.rotationYaw = p_72866_1_.prevRotationYaw;
        }

        int i3 = MathHelper.floor(p_72866_1_.posX / 16.0D);
        int j3 = MathHelper.floor(p_72866_1_.posY / 16.0D);
        int k3 = MathHelper.floor(p_72866_1_.posZ / 16.0D);

        if (!p_72866_1_.addedToChunk || p_72866_1_.chunkCoordX != i3 || p_72866_1_.chunkCoordY != j3 || p_72866_1_.chunkCoordZ != k3)
        {
            if (p_72866_1_.addedToChunk && this.func_175680_a(p_72866_1_.chunkCoordX, p_72866_1_.chunkCoordZ, true))
            {
                this.func_72964_e(p_72866_1_.chunkCoordX, p_72866_1_.chunkCoordZ).removeEntityAtIndex(p_72866_1_, p_72866_1_.chunkCoordY);
            }

            if (!p_72866_1_.setPositionNonDirty() && !this.func_175680_a(i3, k3, true))
            {
                p_72866_1_.addedToChunk = false;
            }
            else
            {
                this.func_72964_e(i3, k3).addEntity(p_72866_1_);
            }
        }

        this.profiler.endSection();

        if (p_72866_2_ && p_72866_1_.addedToChunk)
        {
            for (Entity entity4 : p_72866_1_.getPassengers())
            {
                if (!entity4.removed && entity4.getRidingEntity() == p_72866_1_)
                {
                    this.func_72870_g(entity4);
                }
                else
                {
                    entity4.stopRiding();
                }
            }
        }
    }

    public boolean func_72855_b(AxisAlignedBB p_72855_1_)
    {
        return this.func_72917_a(p_72855_1_, (Entity)null);
    }

    public boolean func_72917_a(AxisAlignedBB p_72917_1_, @Nullable Entity p_72917_2_)
    {
        List<Entity> list = this.getEntitiesWithinAABBExcludingEntity((Entity)null, p_72917_1_);

        for (int j2 = 0; j2 < list.size(); ++j2)
        {
            Entity entity4 = list.get(j2);

            if (!entity4.removed && entity4.preventEntitySpawning && entity4 != p_72917_2_ && (p_72917_2_ == null || entity4.isRidingSameEntity(p_72917_2_)))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if there are any blocks in the region constrained by an AxisAlignedBB
     */
    public boolean checkBlockCollision(AxisAlignedBB bb)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int l3 = j2; l3 < k2; ++l3)
        {
            for (int i4 = l2; i4 < i3; ++i4)
            {
                for (int j4 = j3; j4 < k3; ++j4)
                {
                    IBlockState iblockstate1 = this.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4));

                    if (iblockstate1.getMaterial() != Material.AIR)
                    {
                        blockpos$pooledmutableblockpos.func_185344_t();
                        return true;
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.func_185344_t();
        return false;
    }

    /**
     * Checks if any of the blocks within the aabb are liquids.
     */
    public boolean containsAnyLiquid(AxisAlignedBB bb)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int l3 = j2; l3 < k2; ++l3)
        {
            for (int i4 = l2; i4 < i3; ++i4)
            {
                for (int j4 = j3; j4 < k3; ++j4)
                {
                    IBlockState iblockstate1 = this.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4));

                    if (iblockstate1.getMaterial().isLiquid())
                    {
                        blockpos$pooledmutableblockpos.func_185344_t();
                        return true;
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.func_185344_t();
        return false;
    }

    public boolean isFlammableWithin(AxisAlignedBB bb)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);

        if (this.func_175663_a(j2, l2, j3, k2, i3, k3, true))
        {
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

            for (int l3 = j2; l3 < k2; ++l3)
            {
                for (int i4 = l2; i4 < i3; ++i4)
                {
                    for (int j4 = j3; j4 < k3; ++j4)
                    {
                        Block block = this.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4)).getBlock();

                        if (block == Blocks.FIRE || block == Blocks.field_150356_k || block == Blocks.LAVA)
                        {
                            blockpos$pooledmutableblockpos.func_185344_t();
                            return true;
                        }
                    }
                }
            }

            blockpos$pooledmutableblockpos.func_185344_t();
        }

        return false;
    }

    public boolean func_72918_a(AxisAlignedBB p_72918_1_, Material p_72918_2_, Entity p_72918_3_)
    {
        int j2 = MathHelper.floor(p_72918_1_.minX);
        int k2 = MathHelper.ceil(p_72918_1_.maxX);
        int l2 = MathHelper.floor(p_72918_1_.minY);
        int i3 = MathHelper.ceil(p_72918_1_.maxY);
        int j3 = MathHelper.floor(p_72918_1_.minZ);
        int k3 = MathHelper.ceil(p_72918_1_.maxZ);

        if (!this.func_175663_a(j2, l2, j3, k2, i3, k3, true))
        {
            return false;
        }
        else
        {
            boolean flag = false;
            Vec3d vec3d = Vec3d.ZERO;
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

            for (int l3 = j2; l3 < k2; ++l3)
            {
                for (int i4 = l2; i4 < i3; ++i4)
                {
                    for (int j4 = j3; j4 < k3; ++j4)
                    {
                        blockpos$pooledmutableblockpos.setPos(l3, i4, j4);
                        IBlockState iblockstate1 = this.getBlockState(blockpos$pooledmutableblockpos);
                        Block block = iblockstate1.getBlock();

                        if (iblockstate1.getMaterial() == p_72918_2_)
                        {
                            double d0 = (double)((float)(i4 + 1) - BlockLiquid.func_149801_b(((Integer)iblockstate1.get(BlockLiquid.LEVEL)).intValue()));

                            if ((double)i3 >= d0)
                            {
                                flag = true;
                                vec3d = block.func_176197_a(this, blockpos$pooledmutableblockpos, p_72918_3_, vec3d);
                            }
                        }
                    }
                }
            }

            blockpos$pooledmutableblockpos.func_185344_t();

            if (vec3d.length() > 0.0D && p_72918_3_.isPushedByWater())
            {
                vec3d = vec3d.normalize();
                double d1 = 0.014D;
                p_72918_3_.field_70159_w += vec3d.x * 0.014D;
                p_72918_3_.field_70181_x += vec3d.y * 0.014D;
                p_72918_3_.field_70179_y += vec3d.z * 0.014D;
            }

            return flag;
        }
    }

    /**
     * Returns true if the given bounding box contains the given material
     */
    public boolean isMaterialInBB(AxisAlignedBB bb, Material materialIn)
    {
        int j2 = MathHelper.floor(bb.minX);
        int k2 = MathHelper.ceil(bb.maxX);
        int l2 = MathHelper.floor(bb.minY);
        int i3 = MathHelper.ceil(bb.maxY);
        int j3 = MathHelper.floor(bb.minZ);
        int k3 = MathHelper.ceil(bb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        for (int l3 = j2; l3 < k2; ++l3)
        {
            for (int i4 = l2; i4 < i3; ++i4)
            {
                for (int j4 = j3; j4 < k3; ++j4)
                {
                    if (this.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4)).getMaterial() == materialIn)
                    {
                        blockpos$pooledmutableblockpos.func_185344_t();
                        return true;
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.func_185344_t();
        return false;
    }

    public Explosion func_72876_a(@Nullable Entity p_72876_1_, double p_72876_2_, double p_72876_4_, double p_72876_6_, float p_72876_8_, boolean p_72876_9_)
    {
        return this.func_72885_a(p_72876_1_, p_72876_2_, p_72876_4_, p_72876_6_, p_72876_8_, false, p_72876_9_);
    }

    public Explosion func_72885_a(@Nullable Entity p_72885_1_, double p_72885_2_, double p_72885_4_, double p_72885_6_, float p_72885_8_, boolean p_72885_9_, boolean p_72885_10_)
    {
        Explosion explosion = new Explosion(this, p_72885_1_, p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_, p_72885_9_, p_72885_10_);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }

    public float func_72842_a(Vec3d p_72842_1_, AxisAlignedBB p_72842_2_)
    {
        double d0 = 1.0D / ((p_72842_2_.maxX - p_72842_2_.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((p_72842_2_.maxY - p_72842_2_.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((p_72842_2_.maxZ - p_72842_2_.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;

        if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D)
        {
            int j2 = 0;
            int k2 = 0;

            for (float f = 0.0F; f <= 1.0F; f = (float)((double)f + d0))
            {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1))
                {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2))
                    {
                        double d5 = p_72842_2_.minX + (p_72842_2_.maxX - p_72842_2_.minX) * (double)f;
                        double d6 = p_72842_2_.minY + (p_72842_2_.maxY - p_72842_2_.minY) * (double)f1;
                        double d7 = p_72842_2_.minZ + (p_72842_2_.maxZ - p_72842_2_.minZ) * (double)f2;

                        if (this.func_72933_a(new Vec3d(d5 + d3, d6, d7 + d4), p_72842_1_) == null)
                        {
                            ++j2;
                        }

                        ++k2;
                    }
                }
            }

            return (float)j2 / (float)k2;
        }
        else
        {
            return 0.0F;
        }
    }

    /**
     * Attempts to extinguish a fire
     */
    public boolean extinguishFire(@Nullable EntityPlayer player, BlockPos pos, EnumFacing side)
    {
        pos = pos.offset(side);

        if (this.getBlockState(pos).getBlock() == Blocks.FIRE)
        {
            this.func_180498_a(player, 1009, pos, 0);
            this.func_175698_g(pos);
            return true;
        }
        else
        {
            return false;
        }
    }

    public String func_72981_t()
    {
        return "All: " + this.field_72996_f.size();
    }

    /**
     * Returns the name of the current chunk provider, by calling chunkprovider.makeString()
     */
    public String getProviderName()
    {
        return this.chunkProvider.makeString();
    }

    @Nullable
    public TileEntity getTileEntity(BlockPos pos)
    {
        if (this.isOutsideBuildHeight(pos))
        {
            return null;
        }
        else
        {
            TileEntity tileentity2 = null;

            if (this.processingLoadedTiles)
            {
                tileentity2 = this.getPendingTileEntityAt(pos);
            }

            if (tileentity2 == null)
            {
                tileentity2 = this.getChunkAt(pos).getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
            }

            if (tileentity2 == null)
            {
                tileentity2 = this.getPendingTileEntityAt(pos);
            }

            return tileentity2;
        }
    }

    @Nullable
    private TileEntity getPendingTileEntityAt(BlockPos pos)
    {
        for (int j2 = 0; j2 < this.addedTileEntityList.size(); ++j2)
        {
            TileEntity tileentity2 = this.addedTileEntityList.get(j2);

            if (!tileentity2.isRemoved() && tileentity2.getPos().equals(pos))
            {
                return tileentity2;
            }
        }

        return null;
    }

    public void setTileEntity(BlockPos pos, @Nullable TileEntity tileEntityIn)
    {
        if (!this.isOutsideBuildHeight(pos))
        {
            if (tileEntityIn != null && !tileEntityIn.isRemoved())
            {
                if (this.processingLoadedTiles)
                {
                    tileEntityIn.setPos(pos);
                    Iterator<TileEntity> iterator1 = this.addedTileEntityList.iterator();

                    while (iterator1.hasNext())
                    {
                        TileEntity tileentity2 = iterator1.next();

                        if (tileentity2.getPos().equals(pos))
                        {
                            tileentity2.remove();
                            iterator1.remove();
                        }
                    }

                    this.addedTileEntityList.add(tileEntityIn);
                }
                else
                {
                    this.getChunkAt(pos).addTileEntity(pos, tileEntityIn);
                    this.addTileEntity(tileEntityIn);
                }
            }
        }
    }

    public void removeTileEntity(BlockPos pos)
    {
        TileEntity tileentity2 = this.getTileEntity(pos);

        if (tileentity2 != null && this.processingLoadedTiles)
        {
            tileentity2.remove();
            this.addedTileEntityList.remove(tileentity2);
        }
        else
        {
            if (tileentity2 != null)
            {
                this.addedTileEntityList.remove(tileentity2);
                this.loadedTileEntityList.remove(tileentity2);
                this.tickableTileEntities.remove(tileentity2);
            }

            this.getChunkAt(pos).removeTileEntity(pos);
        }
    }

    public void func_147457_a(TileEntity p_147457_1_)
    {
        this.tileEntitiesToBeRemoved.add(p_147457_1_);
    }

    public boolean func_175665_u(BlockPos p_175665_1_)
    {
        AxisAlignedBB axisalignedbb = this.getBlockState(p_175665_1_).func_185890_d(this, p_175665_1_);
        return axisalignedbb != Block.field_185506_k && axisalignedbb.getAverageEdgeLength() >= 1.0D;
    }

    public boolean func_175677_d(BlockPos p_175677_1_, boolean p_175677_2_)
    {
        if (this.isOutsideBuildHeight(p_175677_1_))
        {
            return false;
        }
        else
        {
            Chunk chunk1 = this.chunkProvider.func_186026_b(p_175677_1_.getX() >> 4, p_175677_1_.getZ() >> 4);

            if (chunk1 != null && !chunk1.isEmpty())
            {
                IBlockState iblockstate1 = this.getBlockState(p_175677_1_);
                return iblockstate1.getMaterial().isOpaque() && iblockstate1.func_185917_h();
            }
            else
            {
                return p_175677_2_;
            }
        }
    }

    /**
     * Called on construction of the World class to setup the initial skylight values
     */
    public void calculateInitialSkylight()
    {
        int j2 = this.func_72967_a(1.0F);

        if (j2 != this.skylightSubtracted)
        {
            this.skylightSubtracted = j2;
        }
    }

    /**
     * first boolean for hostile mobs and second for peaceful mobs
     */
    public void setAllowedSpawnTypes(boolean hostile, boolean peaceful)
    {
        this.field_72985_G = hostile;
        this.field_72992_H = peaceful;
    }

    /**
     * Runs a single tick for the world
     */
    public void tick()
    {
        this.func_72979_l();
    }

    /**
     * Called from World constructor to set rainingStrength and thunderingStrength
     */
    protected void calculateInitialWeather()
    {
        if (this.worldInfo.isRaining())
        {
            this.rainingStrength = 1.0F;

            if (this.worldInfo.isThundering())
            {
                this.thunderingStrength = 1.0F;
            }
        }
    }

    protected void func_72979_l()
    {
        if (this.dimension.hasSkyLight())
        {
            if (!this.isRemote)
            {
                boolean flag = this.getGameRules().func_82766_b("doWeatherCycle");

                if (flag)
                {
                    int j2 = this.worldInfo.getClearWeatherTime();

                    if (j2 > 0)
                    {
                        --j2;
                        this.worldInfo.setClearWeatherTime(j2);
                        this.worldInfo.setThunderTime(this.worldInfo.isThundering() ? 1 : 2);
                        this.worldInfo.setRainTime(this.worldInfo.isRaining() ? 1 : 2);
                    }

                    int k2 = this.worldInfo.getThunderTime();

                    if (k2 <= 0)
                    {
                        if (this.worldInfo.isThundering())
                        {
                            this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
                        }
                        else
                        {
                            this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
                        }
                    }
                    else
                    {
                        --k2;
                        this.worldInfo.setThunderTime(k2);

                        if (k2 <= 0)
                        {
                            this.worldInfo.setThundering(!this.worldInfo.isThundering());
                        }
                    }

                    int l2 = this.worldInfo.getRainTime();

                    if (l2 <= 0)
                    {
                        if (this.worldInfo.isRaining())
                        {
                            this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
                        }
                        else
                        {
                            this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
                        }
                    }
                    else
                    {
                        --l2;
                        this.worldInfo.setRainTime(l2);

                        if (l2 <= 0)
                        {
                            this.worldInfo.setRaining(!this.worldInfo.isRaining());
                        }
                    }
                }

                this.prevThunderingStrength = this.thunderingStrength;

                if (this.worldInfo.isThundering())
                {
                    this.thunderingStrength = (float)((double)this.thunderingStrength + 0.01D);
                }
                else
                {
                    this.thunderingStrength = (float)((double)this.thunderingStrength - 0.01D);
                }

                this.thunderingStrength = MathHelper.clamp(this.thunderingStrength, 0.0F, 1.0F);
                this.prevRainingStrength = this.rainingStrength;

                if (this.worldInfo.isRaining())
                {
                    this.rainingStrength = (float)((double)this.rainingStrength + 0.01D);
                }
                else
                {
                    this.rainingStrength = (float)((double)this.rainingStrength - 0.01D);
                }

                this.rainingStrength = MathHelper.clamp(this.rainingStrength, 0.0F, 1.0F);
            }
        }
    }

    protected void func_147467_a(int p_147467_1_, int p_147467_2_, Chunk p_147467_3_)
    {
        p_147467_3_.func_76594_o();
    }

    protected void func_147456_g()
    {
    }

    public void func_189507_a(BlockPos p_189507_1_, IBlockState p_189507_2_, Random p_189507_3_)
    {
        this.field_72999_e = true;
        p_189507_2_.getBlock().func_180650_b(this, p_189507_1_, p_189507_2_, p_189507_3_);
        this.field_72999_e = false;
    }

    public boolean func_175675_v(BlockPos p_175675_1_)
    {
        return this.func_175670_e(p_175675_1_, false);
    }

    public boolean func_175662_w(BlockPos p_175662_1_)
    {
        return this.func_175670_e(p_175662_1_, true);
    }

    public boolean func_175670_e(BlockPos p_175670_1_, boolean p_175670_2_)
    {
        Biome biome = this.func_180494_b(p_175670_1_);
        float f = biome.getTemperatureRaw(p_175670_1_);

        if (f >= 0.15F)
        {
            return false;
        }
        else
        {
            if (p_175670_1_.getY() >= 0 && p_175670_1_.getY() < 256 && this.func_175642_b(EnumSkyBlock.BLOCK, p_175670_1_) < 10)
            {
                IBlockState iblockstate1 = this.getBlockState(p_175670_1_);
                Block block = iblockstate1.getBlock();

                if ((block == Blocks.WATER || block == Blocks.field_150358_i) && ((Integer)iblockstate1.get(BlockLiquid.LEVEL)).intValue() == 0)
                {
                    if (!p_175670_2_)
                    {
                        return true;
                    }

                    boolean flag = this.func_175696_F(p_175670_1_.west()) && this.func_175696_F(p_175670_1_.east()) && this.func_175696_F(p_175670_1_.north()) && this.func_175696_F(p_175670_1_.south());

                    if (!flag)
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private boolean func_175696_F(BlockPos p_175696_1_)
    {
        return this.getBlockState(p_175696_1_).getMaterial() == Material.WATER;
    }

    public boolean func_175708_f(BlockPos p_175708_1_, boolean p_175708_2_)
    {
        Biome biome = this.func_180494_b(p_175708_1_);
        float f = biome.getTemperatureRaw(p_175708_1_);

        if (f >= 0.15F)
        {
            return false;
        }
        else if (!p_175708_2_)
        {
            return true;
        }
        else
        {
            if (p_175708_1_.getY() >= 0 && p_175708_1_.getY() < 256 && this.func_175642_b(EnumSkyBlock.BLOCK, p_175708_1_) < 10)
            {
                IBlockState iblockstate1 = this.getBlockState(p_175708_1_);

                if (iblockstate1.getMaterial() == Material.AIR && Blocks.field_150431_aC.func_176196_c(this, p_175708_1_))
                {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean func_175664_x(BlockPos p_175664_1_)
    {
        boolean flag = false;

        if (this.dimension.hasSkyLight())
        {
            flag |= this.func_180500_c(EnumSkyBlock.SKY, p_175664_1_);
        }

        flag = flag | this.func_180500_c(EnumSkyBlock.BLOCK, p_175664_1_);
        return flag;
    }

    private int func_175638_a(BlockPos p_175638_1_, EnumSkyBlock p_175638_2_)
    {
        if (p_175638_2_ == EnumSkyBlock.SKY && this.func_175678_i(p_175638_1_))
        {
            return 15;
        }
        else
        {
            IBlockState iblockstate1 = this.getBlockState(p_175638_1_);
            int j2 = p_175638_2_ == EnumSkyBlock.SKY ? 0 : iblockstate1.getLightValue();
            int k2 = iblockstate1.func_185891_c();

            if (k2 >= 15 && iblockstate1.getLightValue() > 0)
            {
                k2 = 1;
            }

            if (k2 < 1)
            {
                k2 = 1;
            }

            if (k2 >= 15)
            {
                return 0;
            }
            else if (j2 >= 14)
            {
                return j2;
            }
            else
            {
                BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

                try
                {
                    for (EnumFacing enumfacing : EnumFacing.values())
                    {
                        blockpos$pooledmutableblockpos.setPos(p_175638_1_).move(enumfacing);
                        int l2 = this.func_175642_b(p_175638_2_, blockpos$pooledmutableblockpos) - k2;

                        if (l2 > j2)
                        {
                            j2 = l2;
                        }

                        if (j2 >= 14)
                        {
                            int i3 = j2;
                            return i3;
                        }
                    }

                    return j2;
                }
                finally
                {
                    blockpos$pooledmutableblockpos.func_185344_t();
                }
            }
        }
    }

    public boolean func_180500_c(EnumSkyBlock p_180500_1_, BlockPos p_180500_2_)
    {
        if (!this.func_175648_a(p_180500_2_, 17, false))
        {
            return false;
        }
        else
        {
            int j2 = 0;
            int k2 = 0;
            this.profiler.startSection("getBrightness");
            int l2 = this.func_175642_b(p_180500_1_, p_180500_2_);
            int i3 = this.func_175638_a(p_180500_2_, p_180500_1_);
            int j3 = p_180500_2_.getX();
            int k3 = p_180500_2_.getY();
            int l3 = p_180500_2_.getZ();

            if (i3 > l2)
            {
                this.field_72994_J[k2++] = 133152;
            }
            else if (i3 < l2)
            {
                this.field_72994_J[k2++] = 133152 | l2 << 18;

                while (j2 < k2)
                {
                    int i4 = this.field_72994_J[j2++];
                    int j4 = (i4 & 63) - 32 + j3;
                    int k4 = (i4 >> 6 & 63) - 32 + k3;
                    int l4 = (i4 >> 12 & 63) - 32 + l3;
                    int i5 = i4 >> 18 & 15;
                    BlockPos blockpos1 = new BlockPos(j4, k4, l4);
                    int j5 = this.func_175642_b(p_180500_1_, blockpos1);

                    if (j5 == i5)
                    {
                        this.func_175653_a(p_180500_1_, blockpos1, 0);

                        if (i5 > 0)
                        {
                            int k5 = MathHelper.abs(j4 - j3);
                            int l5 = MathHelper.abs(k4 - k3);
                            int i6 = MathHelper.abs(l4 - l3);

                            if (k5 + l5 + i6 < 17)
                            {
                                BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

                                for (EnumFacing enumfacing : EnumFacing.values())
                                {
                                    int j6 = j4 + enumfacing.getXOffset();
                                    int k6 = k4 + enumfacing.getYOffset();
                                    int l6 = l4 + enumfacing.getZOffset();
                                    blockpos$pooledmutableblockpos.setPos(j6, k6, l6);
                                    int i7 = Math.max(1, this.getBlockState(blockpos$pooledmutableblockpos).func_185891_c());
                                    j5 = this.func_175642_b(p_180500_1_, blockpos$pooledmutableblockpos);

                                    if (j5 == i5 - i7 && k2 < this.field_72994_J.length)
                                    {
                                        this.field_72994_J[k2++] = j6 - j3 + 32 | k6 - k3 + 32 << 6 | l6 - l3 + 32 << 12 | i5 - i7 << 18;
                                    }
                                }

                                blockpos$pooledmutableblockpos.func_185344_t();
                            }
                        }
                    }
                }

                j2 = 0;
            }

            this.profiler.endSection();
            this.profiler.startSection("checkedPosition < toCheckCount");

            while (j2 < k2)
            {
                int j7 = this.field_72994_J[j2++];
                int k7 = (j7 & 63) - 32 + j3;
                int l7 = (j7 >> 6 & 63) - 32 + k3;
                int i8 = (j7 >> 12 & 63) - 32 + l3;
                BlockPos blockpos2 = new BlockPos(k7, l7, i8);
                int j8 = this.func_175642_b(p_180500_1_, blockpos2);
                int k8 = this.func_175638_a(blockpos2, p_180500_1_);

                if (k8 != j8)
                {
                    this.func_175653_a(p_180500_1_, blockpos2, k8);

                    if (k8 > j8)
                    {
                        int l8 = Math.abs(k7 - j3);
                        int i9 = Math.abs(l7 - k3);
                        int j9 = Math.abs(i8 - l3);
                        boolean flag = k2 < this.field_72994_J.length - 6;

                        if (l8 + i9 + j9 < 17 && flag)
                        {
                            if (this.func_175642_b(p_180500_1_, blockpos2.west()) < k8)
                            {
                                this.field_72994_J[k2++] = k7 - 1 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                            }

                            if (this.func_175642_b(p_180500_1_, blockpos2.east()) < k8)
                            {
                                this.field_72994_J[k2++] = k7 + 1 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                            }

                            if (this.func_175642_b(p_180500_1_, blockpos2.down()) < k8)
                            {
                                this.field_72994_J[k2++] = k7 - j3 + 32 + (l7 - 1 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                            }

                            if (this.func_175642_b(p_180500_1_, blockpos2.up()) < k8)
                            {
                                this.field_72994_J[k2++] = k7 - j3 + 32 + (l7 + 1 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                            }

                            if (this.func_175642_b(p_180500_1_, blockpos2.north()) < k8)
                            {
                                this.field_72994_J[k2++] = k7 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - 1 - l3 + 32 << 12);
                            }

                            if (this.func_175642_b(p_180500_1_, blockpos2.south()) < k8)
                            {
                                this.field_72994_J[k2++] = k7 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 + 1 - l3 + 32 << 12);
                            }
                        }
                    }
                }
            }

            this.profiler.endSection();
            return true;
        }
    }

    public boolean func_72955_a(boolean p_72955_1_)
    {
        return false;
    }

    @Nullable
    public List<NextTickListEntry> func_72920_a(Chunk p_72920_1_, boolean p_72920_2_)
    {
        return null;
    }

    @Nullable
    public List<NextTickListEntry> func_175712_a(StructureBoundingBox p_175712_1_, boolean p_175712_2_)
    {
        return null;
    }

    public List<Entity> getEntitiesWithinAABBExcludingEntity(@Nullable Entity entityIn, AxisAlignedBB bb)
    {
        return this.getEntitiesInAABBexcluding(entityIn, bb, EntitySelectors.NOT_SPECTATING);
    }

    public List<Entity> getEntitiesInAABBexcluding(@Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate <? super Entity > predicate)
    {
        List<Entity> list = Lists.<Entity>newArrayList();
        int j2 = MathHelper.floor((boundingBox.minX - 2.0D) / 16.0D);
        int k2 = MathHelper.floor((boundingBox.maxX + 2.0D) / 16.0D);
        int l2 = MathHelper.floor((boundingBox.minZ - 2.0D) / 16.0D);
        int i3 = MathHelper.floor((boundingBox.maxZ + 2.0D) / 16.0D);

        for (int j3 = j2; j3 <= k2; ++j3)
        {
            for (int k3 = l2; k3 <= i3; ++k3)
            {
                if (this.func_175680_a(j3, k3, true))
                {
                    this.func_72964_e(j3, k3).getEntitiesWithinAABBForEntity(entityIn, boundingBox, list, predicate);
                }
            }
        }

        return list;
    }

    public <T extends Entity> List<T> func_175644_a(Class <? extends T > p_175644_1_, Predicate <? super T > p_175644_2_)
    {
        List<T> list = Lists.<T>newArrayList();

        for (Entity entity4 : this.field_72996_f)
        {
            if (p_175644_1_.isAssignableFrom(entity4.getClass()) && p_175644_2_.apply((T)entity4))
            {
                list.add((T)entity4);
            }
        }

        return list;
    }

    public <T extends Entity> List<T> func_175661_b(Class <? extends T > p_175661_1_, Predicate <? super T > p_175661_2_)
    {
        List<T> list = Lists.<T>newArrayList();

        for (Entity entity4 : this.field_73010_i)
        {
            if (p_175661_1_.isAssignableFrom(entity4.getClass()) && p_175661_2_.apply((T)entity4))
            {
                list.add((T)entity4);
            }
        }

        return list;
    }

    public <T extends Entity> List<T> func_72872_a(Class <? extends T > p_72872_1_, AxisAlignedBB p_72872_2_)
    {
        return this.<T>getEntitiesWithinAABB(p_72872_1_, p_72872_2_, EntitySelectors.NOT_SPECTATING);
    }

    public <T extends Entity> List<T> getEntitiesWithinAABB(Class <? extends T > clazz, AxisAlignedBB aabb, @Nullable Predicate <? super T > filter)
    {
        int j2 = MathHelper.floor((aabb.minX - 2.0D) / 16.0D);
        int k2 = MathHelper.ceil((aabb.maxX + 2.0D) / 16.0D);
        int l2 = MathHelper.floor((aabb.minZ - 2.0D) / 16.0D);
        int i3 = MathHelper.ceil((aabb.maxZ + 2.0D) / 16.0D);
        List<T> list = Lists.<T>newArrayList();

        for (int j3 = j2; j3 < k2; ++j3)
        {
            for (int k3 = l2; k3 < i3; ++k3)
            {
                if (this.func_175680_a(j3, k3, true))
                {
                    this.func_72964_e(j3, k3).getEntitiesOfTypeWithinAABB(clazz, aabb, list, filter);
                }
            }
        }

        return list;
    }

    @Nullable
    public <T extends Entity> T func_72857_a(Class <? extends T > p_72857_1_, AxisAlignedBB p_72857_2_, T p_72857_3_)
    {
        List<T> list = this.<T>func_72872_a(p_72857_1_, p_72857_2_);
        T t = null;
        double d0 = Double.MAX_VALUE;

        for (int j2 = 0; j2 < list.size(); ++j2)
        {
            T t1 = list.get(j2);

            if (t1 != p_72857_3_ && EntitySelectors.NOT_SPECTATING.apply(t1))
            {
                double d1 = p_72857_3_.getDistanceSq(t1);

                if (d1 <= d0)
                {
                    t = t1;
                    d0 = d1;
                }
            }
        }

        return t;
    }

    @Nullable

    /**
     * Returns the Entity with the given ID, or null if it doesn't exist in this World.
     */
    public Entity getEntityByID(int id)
    {
        return this.field_175729_l.func_76041_a(id);
    }

    public List<Entity> func_72910_y()
    {
        return this.field_72996_f;
    }

    public void markChunkDirty(BlockPos pos, TileEntity unusedTileEntity)
    {
        if (this.isBlockLoaded(pos))
        {
            this.getChunkAt(pos).markDirty();
        }
    }

    public int func_72907_a(Class<?> p_72907_1_)
    {
        int j2 = 0;

        for (Entity entity4 : this.field_72996_f)
        {
            if ((!(entity4 instanceof EntityLiving) || !((EntityLiving)entity4).isNoDespawnRequired()) && p_72907_1_.isAssignableFrom(entity4.getClass()))
            {
                ++j2;
            }
        }

        return j2;
    }

    public void func_175650_b(Collection<Entity> p_175650_1_)
    {
        this.field_72996_f.addAll(p_175650_1_);

        for (Entity entity4 : p_175650_1_)
        {
            this.func_72923_a(entity4);
        }
    }

    public void func_175681_c(Collection<Entity> p_175681_1_)
    {
        this.field_72997_g.addAll(p_175681_1_);
    }

    public boolean func_190527_a(Block p_190527_1_, BlockPos p_190527_2_, boolean p_190527_3_, EnumFacing p_190527_4_, @Nullable Entity p_190527_5_)
    {
        IBlockState iblockstate1 = this.getBlockState(p_190527_2_);
        AxisAlignedBB axisalignedbb = p_190527_3_ ? null : p_190527_1_.getDefaultState().func_185890_d(this, p_190527_2_);

        if (axisalignedbb != Block.field_185506_k && !this.func_72917_a(axisalignedbb.offset(p_190527_2_), p_190527_5_))
        {
            return false;
        }
        else if (iblockstate1.getMaterial() == Material.MISCELLANEOUS && p_190527_1_ == Blocks.ANVIL)
        {
            return true;
        }
        else
        {
            return iblockstate1.getMaterial().isReplaceable() && p_190527_1_.func_176198_a(this, p_190527_2_, p_190527_4_);
        }
    }

    public int getSeaLevel()
    {
        return this.field_181546_a;
    }

    public void func_181544_b(int p_181544_1_)
    {
        this.field_181546_a = p_181544_1_;
    }

    public int getStrongPower(BlockPos pos, EnumFacing direction)
    {
        return this.getBlockState(pos).getStrongPower(this, pos, direction);
    }

    public WorldType getWorldType()
    {
        return this.worldInfo.getGenerator();
    }

    /**
     * Returns the single highest strong power out of all directions using getStrongPower(BlockPos, EnumFacing)
     */
    public int getStrongPower(BlockPos pos)
    {
        int j2 = 0;
        j2 = Math.max(j2, this.getStrongPower(pos.down(), EnumFacing.DOWN));

        if (j2 >= 15)
        {
            return j2;
        }
        else
        {
            j2 = Math.max(j2, this.getStrongPower(pos.up(), EnumFacing.UP));

            if (j2 >= 15)
            {
                return j2;
            }
            else
            {
                j2 = Math.max(j2, this.getStrongPower(pos.north(), EnumFacing.NORTH));

                if (j2 >= 15)
                {
                    return j2;
                }
                else
                {
                    j2 = Math.max(j2, this.getStrongPower(pos.south(), EnumFacing.SOUTH));

                    if (j2 >= 15)
                    {
                        return j2;
                    }
                    else
                    {
                        j2 = Math.max(j2, this.getStrongPower(pos.west(), EnumFacing.WEST));

                        if (j2 >= 15)
                        {
                            return j2;
                        }
                        else
                        {
                            j2 = Math.max(j2, this.getStrongPower(pos.east(), EnumFacing.EAST));
                            return j2 >= 15 ? j2 : j2;
                        }
                    }
                }
            }
        }
    }

    public boolean isSidePowered(BlockPos pos, EnumFacing side)
    {
        return this.getRedstonePower(pos, side) > 0;
    }

    public int getRedstonePower(BlockPos pos, EnumFacing facing)
    {
        IBlockState iblockstate1 = this.getBlockState(pos);
        return iblockstate1.func_185915_l() ? this.getStrongPower(pos) : iblockstate1.getWeakPower(this, pos, facing);
    }

    public boolean isBlockPowered(BlockPos pos)
    {
        if (this.getRedstonePower(pos.down(), EnumFacing.DOWN) > 0)
        {
            return true;
        }
        else if (this.getRedstonePower(pos.up(), EnumFacing.UP) > 0)
        {
            return true;
        }
        else if (this.getRedstonePower(pos.north(), EnumFacing.NORTH) > 0)
        {
            return true;
        }
        else if (this.getRedstonePower(pos.south(), EnumFacing.SOUTH) > 0)
        {
            return true;
        }
        else if (this.getRedstonePower(pos.west(), EnumFacing.WEST) > 0)
        {
            return true;
        }
        else
        {
            return this.getRedstonePower(pos.east(), EnumFacing.EAST) > 0;
        }
    }

    /**
     * Checks if the specified block or its neighbors are powered by a neighboring block. Used by blocks like TNT and
     * Doors.
     */
    public int getRedstonePowerFromNeighbors(BlockPos pos)
    {
        int j2 = 0;

        for (EnumFacing enumfacing : EnumFacing.values())
        {
            int k2 = this.getRedstonePower(pos.offset(enumfacing), enumfacing);

            if (k2 >= 15)
            {
                return 15;
            }

            if (k2 > j2)
            {
                j2 = k2;
            }
        }

        return j2;
    }

    @Nullable
    public EntityPlayer func_72890_a(Entity p_72890_1_, double p_72890_2_)
    {
        return this.func_184137_a(p_72890_1_.posX, p_72890_1_.posY, p_72890_1_.posZ, p_72890_2_, false);
    }

    @Nullable
    public EntityPlayer func_184136_b(Entity p_184136_1_, double p_184136_2_)
    {
        return this.func_184137_a(p_184136_1_.posX, p_184136_1_.posY, p_184136_1_.posZ, p_184136_2_, true);
    }

    @Nullable
    public EntityPlayer func_184137_a(double p_184137_1_, double p_184137_3_, double p_184137_5_, double p_184137_7_, boolean p_184137_9_)
    {
        Predicate<Entity> predicate = p_184137_9_ ? EntitySelectors.CAN_AI_TARGET : EntitySelectors.NOT_SPECTATING;
        return this.getClosestPlayer(p_184137_1_, p_184137_3_, p_184137_5_, p_184137_7_, predicate);
    }

    @Nullable
    public EntityPlayer getClosestPlayer(double x, double y, double z, double distance, Predicate<Entity> predicate)
    {
        double d0 = -1.0D;
        EntityPlayer entityplayer = null;

        for (int j2 = 0; j2 < this.field_73010_i.size(); ++j2)
        {
            EntityPlayer entityplayer1 = this.field_73010_i.get(j2);

            if (predicate.apply(entityplayer1))
            {
                double d1 = entityplayer1.getDistanceSq(x, y, z);

                if ((distance < 0.0D || d1 < distance * distance) && (d0 == -1.0D || d1 < d0))
                {
                    d0 = d1;
                    entityplayer = entityplayer1;
                }
            }
        }

        return entityplayer;
    }

    public boolean func_175636_b(double p_175636_1_, double p_175636_3_, double p_175636_5_, double p_175636_7_)
    {
        for (int j2 = 0; j2 < this.field_73010_i.size(); ++j2)
        {
            EntityPlayer entityplayer = this.field_73010_i.get(j2);

            if (EntitySelectors.NOT_SPECTATING.apply(entityplayer))
            {
                double d0 = entityplayer.getDistanceSq(p_175636_1_, p_175636_3_, p_175636_5_);

                if (p_175636_7_ < 0.0D || d0 < p_175636_7_ * p_175636_7_)
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Nullable
    public EntityPlayer func_184142_a(Entity p_184142_1_, double p_184142_2_, double p_184142_4_)
    {
        return this.func_184150_a(p_184142_1_.posX, p_184142_1_.posY, p_184142_1_.posZ, p_184142_2_, p_184142_4_, (Function)null, (Predicate)null);
    }

    @Nullable
    public EntityPlayer func_184139_a(BlockPos p_184139_1_, double p_184139_2_, double p_184139_4_)
    {
        return this.func_184150_a((double)((float)p_184139_1_.getX() + 0.5F), (double)((float)p_184139_1_.getY() + 0.5F), (double)((float)p_184139_1_.getZ() + 0.5F), p_184139_2_, p_184139_4_, (Function)null, (Predicate)null);
    }

    @Nullable
    public EntityPlayer func_184150_a(double p_184150_1_, double p_184150_3_, double p_184150_5_, double p_184150_7_, double p_184150_9_, @Nullable Function<EntityPlayer, Double> p_184150_11_, @Nullable Predicate<EntityPlayer> p_184150_12_)
    {
        double d0 = -1.0D;
        EntityPlayer entityplayer = null;

        for (int j2 = 0; j2 < this.field_73010_i.size(); ++j2)
        {
            EntityPlayer entityplayer1 = this.field_73010_i.get(j2);

            if (!entityplayer1.abilities.disableDamage && entityplayer1.isAlive() && !entityplayer1.isSpectator() && (p_184150_12_ == null || p_184150_12_.apply(entityplayer1)))
            {
                double d1 = entityplayer1.getDistanceSq(p_184150_1_, entityplayer1.posY, p_184150_5_);
                double d2 = p_184150_7_;

                if (entityplayer1.func_70093_af())
                {
                    d2 = p_184150_7_ * 0.800000011920929D;
                }

                if (entityplayer1.isInvisible())
                {
                    float f = entityplayer1.func_82243_bO();

                    if (f < 0.1F)
                    {
                        f = 0.1F;
                    }

                    d2 *= (double)(0.7F * f);
                }

                if (p_184150_11_ != null)
                {
                    d2 *= ((Double)MoreObjects.firstNonNull(p_184150_11_.apply(entityplayer1), Double.valueOf(1.0D))).doubleValue();
                }

                if ((p_184150_9_ < 0.0D || Math.abs(entityplayer1.posY - p_184150_3_) < p_184150_9_ * p_184150_9_) && (p_184150_7_ < 0.0D || d1 < d2 * d2) && (d0 == -1.0D || d1 < d0))
                {
                    d0 = d1;
                    entityplayer = entityplayer1;
                }
            }
        }

        return entityplayer;
    }

    @Nullable
    public EntityPlayer func_72924_a(String p_72924_1_)
    {
        for (int j2 = 0; j2 < this.field_73010_i.size(); ++j2)
        {
            EntityPlayer entityplayer = this.field_73010_i.get(j2);

            if (p_72924_1_.equals(entityplayer.func_70005_c_()))
            {
                return entityplayer;
            }
        }

        return null;
    }

    @Nullable
    public EntityPlayer func_152378_a(UUID p_152378_1_)
    {
        for (int j2 = 0; j2 < this.field_73010_i.size(); ++j2)
        {
            EntityPlayer entityplayer = this.field_73010_i.get(j2);

            if (p_152378_1_.equals(entityplayer.getUniqueID()))
            {
                return entityplayer;
            }
        }

        return null;
    }

    /**
     * If on MP, sends a quitting packet.
     */
    public void sendQuittingDisconnectingPacket()
    {
    }

    public void func_72906_B() throws MinecraftException
    {
        this.field_73019_z.checkSessionLock();
    }

    public void setGameTime(long worldTime)
    {
        this.worldInfo.setGameTime(worldTime);
    }

    /**
     * gets the random world seed
     */
    public long getSeed()
    {
        return this.worldInfo.getSeed();
    }

    public long getGameTime()
    {
        return this.worldInfo.getGameTime();
    }

    public long getDayTime()
    {
        return this.worldInfo.getDayTime();
    }

    /**
     * Sets the world time.
     */
    public void setDayTime(long time)
    {
        this.worldInfo.setDayTime(time);
    }

    /**
     * Gets the spawn point in the world
     */
    public BlockPos getSpawnPoint()
    {
        BlockPos blockpos1 = new BlockPos(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());

        if (!this.getWorldBorder().contains(blockpos1))
        {
            blockpos1 = this.func_175645_m(new BlockPos(this.getWorldBorder().getCenterX(), 0.0D, this.getWorldBorder().getCenterZ()));
        }

        return blockpos1;
    }

    public void setSpawnPoint(BlockPos pos)
    {
        this.worldInfo.setSpawn(pos);
    }

    public void func_72897_h(Entity p_72897_1_)
    {
        int j2 = MathHelper.floor(p_72897_1_.posX / 16.0D);
        int k2 = MathHelper.floor(p_72897_1_.posZ / 16.0D);
        int l2 = 2;

        for (int i3 = -2; i3 <= 2; ++i3)
        {
            for (int j3 = -2; j3 <= 2; ++j3)
            {
                this.func_72964_e(j2 + i3, k2 + j3);
            }
        }

        if (!this.field_72996_f.contains(p_72897_1_))
        {
            this.field_72996_f.add(p_72897_1_);
        }
    }

    public boolean isBlockModifiable(EntityPlayer player, BlockPos pos)
    {
        return true;
    }

    /**
     * sends a Packet 38 (Entity Status) to all tracked players of that entity
     */
    public void setEntityState(Entity entityIn, byte state)
    {
    }

    /**
     * Gets the world's chunk provider
     */
    public IChunkProvider getChunkProvider()
    {
        return this.chunkProvider;
    }

    public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam)
    {
        this.getBlockState(pos).onBlockEventReceived(this, pos, eventID, eventParam);
    }

    public ISaveHandler func_72860_G()
    {
        return this.field_73019_z;
    }

    /**
     * Returns the world's WorldInfo object
     */
    public WorldInfo getWorldInfo()
    {
        return this.worldInfo;
    }

    /**
     * Gets the GameRules instance.
     */
    public GameRules getGameRules()
    {
        return this.worldInfo.getGameRulesInstance();
    }

    /**
     * Updates the flag that indicates whether or not all players in the world are sleeping.
     */
    public void updateAllPlayersSleepingFlag()
    {
    }

    public float getThunderStrength(float delta)
    {
        return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * delta) * this.getRainStrength(delta);
    }

    /**
     * Sets the strength of the thunder.
     */
    public void setThunderStrength(float strength)
    {
        this.prevThunderingStrength = strength;
        this.thunderingStrength = strength;
    }

    /**
     * Returns rain strength.
     */
    public float getRainStrength(float delta)
    {
        return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * delta;
    }

    /**
     * Sets the strength of the rain.
     */
    public void setRainStrength(float strength)
    {
        this.prevRainingStrength = strength;
        this.rainingStrength = strength;
    }

    /**
     * Returns true if the current thunder strength (weighted with the rain strength) is greater than 0.9
     */
    public boolean isThundering()
    {
        return (double)this.getThunderStrength(1.0F) > 0.9D;
    }

    /**
     * Returns true if the current rain strength is greater than 0.2
     */
    public boolean isRaining()
    {
        return (double)this.getRainStrength(1.0F) > 0.2D;
    }

    /**
     * Check if precipitation is currently happening at a position
     */
    public boolean isRainingAt(BlockPos position)
    {
        if (!this.isRaining())
        {
            return false;
        }
        else if (!this.func_175678_i(position))
        {
            return false;
        }
        else if (this.func_175725_q(position).getY() > position.getY())
        {
            return false;
        }
        else
        {
            Biome biome = this.func_180494_b(position);

            if (biome.func_76746_c())
            {
                return false;
            }
            else
            {
                return this.func_175708_f(position, false) ? false : biome.func_76738_d();
            }
        }
    }

    public boolean isBlockinHighHumidity(BlockPos pos)
    {
        Biome biome = this.func_180494_b(pos);
        return biome.isHighHumidity();
    }

    @Nullable
    public MapStorage func_175693_T()
    {
        return this.field_72988_C;
    }

    public void func_72823_a(String p_72823_1_, WorldSavedData p_72823_2_)
    {
        this.field_72988_C.func_75745_a(p_72823_1_, p_72823_2_);
    }

    @Nullable
    public WorldSavedData func_72943_a(Class <? extends WorldSavedData > p_72943_1_, String p_72943_2_)
    {
        return this.field_72988_C.func_75742_a(p_72943_1_, p_72943_2_);
    }

    public int func_72841_b(String p_72841_1_)
    {
        return this.field_72988_C.func_75743_a(p_72841_1_);
    }

    public void playBroadcastSound(int id, BlockPos pos, int data)
    {
        for (int j2 = 0; j2 < this.field_73021_x.size(); ++j2)
        {
            ((IWorldEventListener)this.field_73021_x.get(j2)).broadcastSound(id, pos, data);
        }
    }

    public void func_175718_b(int p_175718_1_, BlockPos p_175718_2_, int p_175718_3_)
    {
        this.func_180498_a((EntityPlayer)null, p_175718_1_, p_175718_2_, p_175718_3_);
    }

    public void func_180498_a(@Nullable EntityPlayer p_180498_1_, int p_180498_2_, BlockPos p_180498_3_, int p_180498_4_)
    {
        try
        {
            for (int j2 = 0; j2 < this.field_73021_x.size(); ++j2)
            {
                ((IWorldEventListener)this.field_73021_x.get(j2)).playEvent(p_180498_1_, p_180498_2_, p_180498_3_, p_180498_4_);
            }
        }
        catch (Throwable throwable3)
        {
            CrashReport crashreport3 = CrashReport.makeCrashReport(throwable3, "Playing level event");
            CrashReportCategory crashreportcategory3 = crashreport3.makeCategory("Level event being played");
            crashreportcategory3.addDetail("Block coordinates", CrashReportCategory.getCoordinateInfo(p_180498_3_));
            crashreportcategory3.addDetail("Event source", p_180498_1_);
            crashreportcategory3.addDetail("Event type", Integer.valueOf(p_180498_2_));
            crashreportcategory3.addDetail("Event data", Integer.valueOf(p_180498_4_));
            throw new ReportedException(crashreport3);
        }
    }

    public int func_72800_K()
    {
        return 256;
    }

    /**
     * Returns current world height.
     */
    public int getActualHeight()
    {
        return this.dimension.isNether() ? 128 : 256;
    }

    public Random func_72843_D(int p_72843_1_, int p_72843_2_, int p_72843_3_)
    {
        long j2 = (long)p_72843_1_ * 341873128712L + (long)p_72843_2_ * 132897987541L + this.getWorldInfo().getSeed() + (long)p_72843_3_;
        this.rand.setSeed(j2);
        return this.rand;
    }

    public double func_72919_O()
    {
        return this.worldInfo.getGenerator() == WorldType.FLAT ? 0.0D : 63.0D;
    }

    /**
     * Adds some basic stats of the world to the given crash report.
     */
    public CrashReportCategory fillCrashReport(CrashReport report)
    {
        CrashReportCategory crashreportcategory3 = report.makeCategoryDepth("Affected level", 1);
        crashreportcategory3.addDetail("Level name", this.worldInfo == null ? "????" : this.worldInfo.getWorldName());
        crashreportcategory3.addDetail("All players", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return World.this.field_73010_i.size() + " total; " + World.this.field_73010_i;
            }
        });
        crashreportcategory3.addDetail("Chunk stats", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return World.this.chunkProvider.makeString();
            }
        });

        try
        {
            this.worldInfo.addToCrashReport(crashreportcategory3);
        }
        catch (Throwable throwable3)
        {
            crashreportcategory3.addCrashSectionThrowable("Level Data Unobtainable", throwable3);
        }

        return crashreportcategory3;
    }

    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress)
    {
        for (int j2 = 0; j2 < this.field_73021_x.size(); ++j2)
        {
            IWorldEventListener iworldeventlistener = this.field_73021_x.get(j2);
            iworldeventlistener.sendBlockBreakProgress(breakerId, pos, progress);
        }
    }

    public Calendar func_83015_S()
    {
        if (this.getGameTime() % 600L == 0L)
        {
            this.field_83016_L.setTimeInMillis(MinecraftServer.func_130071_aq());
        }

        return this.field_83016_L;
    }

    public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, @Nullable NBTTagCompound compound)
    {
    }

    public Scoreboard getScoreboard()
    {
        return this.field_96442_D;
    }

    public void updateComparatorOutputLevel(BlockPos pos, Block blockIn)
    {
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos blockpos1 = pos.offset(enumfacing);

            if (this.isBlockLoaded(blockpos1))
            {
                IBlockState iblockstate1 = this.getBlockState(blockpos1);

                if (Blocks.field_150441_bU.func_185547_C(iblockstate1))
                {
                    iblockstate1.func_189546_a(this, blockpos1, blockIn, pos);
                }
                else if (iblockstate1.func_185915_l())
                {
                    blockpos1 = blockpos1.offset(enumfacing);
                    iblockstate1 = this.getBlockState(blockpos1);

                    if (Blocks.field_150441_bU.func_185547_C(iblockstate1))
                    {
                        iblockstate1.func_189546_a(this, blockpos1, blockIn, pos);
                    }
                }
            }
        }
    }

    public DifficultyInstance getDifficultyForLocation(BlockPos pos)
    {
        long j2 = 0L;
        float f = 0.0F;

        if (this.isBlockLoaded(pos))
        {
            f = this.getCurrentMoonPhaseFactor();
            j2 = this.getChunkAt(pos).getInhabitedTime();
        }

        return new DifficultyInstance(this.getDifficulty(), this.getDayTime(), j2, f);
    }

    public EnumDifficulty getDifficulty()
    {
        return this.getWorldInfo().getDifficulty();
    }

    public int getSkylightSubtracted()
    {
        return this.skylightSubtracted;
    }

    public void func_175692_b(int p_175692_1_)
    {
        this.skylightSubtracted = p_175692_1_;
    }

    public int func_175658_ac()
    {
        return this.field_73016_r;
    }

    public void func_175702_c(int p_175702_1_)
    {
        this.field_73016_r = p_175702_1_;
    }

    public VillageCollection func_175714_ae()
    {
        return this.field_72982_D;
    }

    public WorldBorder getWorldBorder()
    {
        return this.worldBorder;
    }

    public boolean func_72916_c(int p_72916_1_, int p_72916_2_)
    {
        BlockPos blockpos1 = this.getSpawnPoint();
        int j2 = p_72916_1_ * 16 + 8 - blockpos1.getX();
        int k2 = p_72916_2_ * 16 + 8 - blockpos1.getZ();
        int l2 = 128;
        return j2 >= -128 && j2 <= 128 && k2 >= -128 && k2 <= 128;
    }

    public void sendPacketToServer(Packet<?> packetIn)
    {
        throw new UnsupportedOperationException("Can't send packets to server unless you're on the client.");
    }

    public LootTableManager func_184146_ak()
    {
        return this.field_184151_B;
    }

    @Nullable
    public BlockPos func_190528_a(String p_190528_1_, BlockPos p_190528_2_, boolean p_190528_3_)
    {
        return null;
    }
}

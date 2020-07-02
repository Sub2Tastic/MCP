package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.IChunkGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ExtendedBlockStorage EMPTY_SECTION = null;
    private final ExtendedBlockStorage[] sections;
    private final byte[] blockBiomeArray;
    private final int[] field_76638_b;
    private final boolean[] field_76639_c;
    private boolean loaded;
    private final World world;
    private final int[] heightMap;
    public final int field_76635_g;
    public final int field_76647_h;
    private boolean field_76650_s;
    private final Map<BlockPos, TileEntity> tileEntities;
    private final ClassInheritanceMultiMap<Entity>[] entityLists;
    private boolean field_76646_k;
    private boolean field_150814_l;
    private boolean field_150815_m;
    private boolean dirty;
    private boolean hasEntities;
    private long lastSaveTime;
    private int field_82912_p;

    /** the cumulative number of ticks players have been in this chunk */
    private long inhabitedTime;
    private int field_76649_t;
    private final ConcurrentLinkedQueue<BlockPos> field_177447_w;
    public boolean field_189550_d;

    public Chunk(World p_i1995_1_, int p_i1995_2_, int p_i1995_3_)
    {
        this.sections = new ExtendedBlockStorage[16];
        this.blockBiomeArray = new byte[256];
        this.field_76638_b = new int[256];
        this.field_76639_c = new boolean[256];
        this.tileEntities = Maps.<BlockPos, TileEntity>newHashMap();
        this.field_76649_t = 4096;
        this.field_177447_w = Queues.<BlockPos>newConcurrentLinkedQueue();
        this.entityLists = (ClassInheritanceMultiMap[])(new ClassInheritanceMultiMap[16]);
        this.world = p_i1995_1_;
        this.field_76635_g = p_i1995_2_;
        this.field_76647_h = p_i1995_3_;
        this.heightMap = new int[256];

        for (int i = 0; i < this.entityLists.length; ++i)
        {
            this.entityLists[i] = new ClassInheritanceMultiMap(Entity.class);
        }

        Arrays.fill(this.field_76638_b, -999);
        Arrays.fill(this.blockBiomeArray, (byte) - 1);
    }

    public Chunk(World p_i45645_1_, ChunkPrimer p_i45645_2_, int p_i45645_3_, int p_i45645_4_)
    {
        this(p_i45645_1_, p_i45645_3_, p_i45645_4_);
        int i = 256;
        boolean flag = p_i45645_1_.dimension.hasSkyLight();

        for (int j = 0; j < 16; ++j)
        {
            for (int k = 0; k < 16; ++k)
            {
                for (int l = 0; l < 256; ++l)
                {
                    IBlockState iblockstate = p_i45645_2_.func_177856_a(j, l, k);

                    if (iblockstate.getMaterial() != Material.AIR)
                    {
                        int i1 = l >> 4;

                        if (this.sections[i1] == EMPTY_SECTION)
                        {
                            this.sections[i1] = new ExtendedBlockStorage(i1 << 4, flag);
                        }

                        this.sections[i1].setBlockState(j, l & 15, k, iblockstate);
                    }
                }
            }
        }
    }

    public boolean func_76600_a(int p_76600_1_, int p_76600_2_)
    {
        return p_76600_1_ == this.field_76635_g && p_76600_2_ == this.field_76647_h;
    }

    public int func_177433_f(BlockPos p_177433_1_)
    {
        return this.func_76611_b(p_177433_1_.getX() & 15, p_177433_1_.getZ() & 15);
    }

    public int func_76611_b(int p_76611_1_, int p_76611_2_)
    {
        return this.heightMap[p_76611_2_ << 4 | p_76611_1_];
    }

    @Nullable
    private ExtendedBlockStorage getLastExtendedBlockStorage()
    {
        for (int i = this.sections.length - 1; i >= 0; --i)
        {
            if (this.sections[i] != EMPTY_SECTION)
            {
                return this.sections[i];
            }
        }

        return null;
    }

    /**
     * Returns the topmost ExtendedBlockStorage instance for this Chunk that actually contains a block.
     */
    public int getTopFilledSegment()
    {
        ExtendedBlockStorage extendedblockstorage = this.getLastExtendedBlockStorage();
        return extendedblockstorage == null ? 0 : extendedblockstorage.func_76662_d();
    }

    /**
     * Returns the sections array for this Chunk.
     */
    public ExtendedBlockStorage[] getSections()
    {
        return this.sections;
    }

    protected void func_76590_a()
    {
        int i = this.getTopFilledSegment();
        this.field_82912_p = Integer.MAX_VALUE;

        for (int j = 0; j < 16; ++j)
        {
            for (int k = 0; k < 16; ++k)
            {
                this.field_76638_b[j + (k << 4)] = -999;

                for (int l = i + 16; l > 0; --l)
                {
                    IBlockState iblockstate = this.func_186032_a(j, l - 1, k);

                    if (iblockstate.func_185891_c() != 0)
                    {
                        this.heightMap[k << 4 | j] = l;

                        if (l < this.field_82912_p)
                        {
                            this.field_82912_p = l;
                        }

                        break;
                    }
                }
            }
        }

        this.dirty = true;
    }

    public void func_76603_b()
    {
        int i = this.getTopFilledSegment();
        this.field_82912_p = Integer.MAX_VALUE;

        for (int j = 0; j < 16; ++j)
        {
            for (int k = 0; k < 16; ++k)
            {
                this.field_76638_b[j + (k << 4)] = -999;

                for (int l = i + 16; l > 0; --l)
                {
                    if (this.func_150808_b(j, l - 1, k) != 0)
                    {
                        this.heightMap[k << 4 | j] = l;

                        if (l < this.field_82912_p)
                        {
                            this.field_82912_p = l;
                        }

                        break;
                    }
                }

                if (this.world.dimension.hasSkyLight())
                {
                    int k1 = 15;
                    int i1 = i + 16 - 1;

                    while (true)
                    {
                        int j1 = this.func_150808_b(j, i1, k);

                        if (j1 == 0 && k1 != 15)
                        {
                            j1 = 1;
                        }

                        k1 -= j1;

                        if (k1 > 0)
                        {
                            ExtendedBlockStorage extendedblockstorage = this.sections[i1 >> 4];

                            if (extendedblockstorage != EMPTY_SECTION)
                            {
                                extendedblockstorage.func_76657_c(j, i1 & 15, k, k1);
                                this.world.func_175679_n(new BlockPos((this.field_76635_g << 4) + j, i1, (this.field_76647_h << 4) + k));
                            }
                        }

                        --i1;

                        if (i1 <= 0 || k1 <= 0)
                        {
                            break;
                        }
                    }
                }
            }
        }

        this.dirty = true;
    }

    private void func_76595_e(int p_76595_1_, int p_76595_2_)
    {
        this.field_76639_c[p_76595_1_ + p_76595_2_ * 16] = true;
        this.field_76650_s = true;
    }

    private void func_150803_c(boolean p_150803_1_)
    {
        this.world.profiler.startSection("recheckGaps");

        if (this.world.func_175697_a(new BlockPos(this.field_76635_g * 16 + 8, 0, this.field_76647_h * 16 + 8), 16))
        {
            for (int i = 0; i < 16; ++i)
            {
                for (int j = 0; j < 16; ++j)
                {
                    if (this.field_76639_c[i + j * 16])
                    {
                        this.field_76639_c[i + j * 16] = false;
                        int k = this.func_76611_b(i, j);
                        int l = this.field_76635_g * 16 + i;
                        int i1 = this.field_76647_h * 16 + j;
                        int j1 = Integer.MAX_VALUE;

                        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                        {
                            j1 = Math.min(j1, this.world.func_82734_g(l + enumfacing.getXOffset(), i1 + enumfacing.getZOffset()));
                        }

                        this.func_76599_g(l, i1, j1);

                        for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
                        {
                            this.func_76599_g(l + enumfacing1.getXOffset(), i1 + enumfacing1.getZOffset(), k);
                        }

                        if (p_150803_1_)
                        {
                            this.world.profiler.endSection();
                            return;
                        }
                    }
                }
            }

            this.field_76650_s = false;
        }

        this.world.profiler.endSection();
    }

    private void func_76599_g(int p_76599_1_, int p_76599_2_, int p_76599_3_)
    {
        int i = this.world.func_175645_m(new BlockPos(p_76599_1_, 0, p_76599_2_)).getY();

        if (i > p_76599_3_)
        {
            this.func_76609_d(p_76599_1_, p_76599_2_, p_76599_3_, i + 1);
        }
        else if (i < p_76599_3_)
        {
            this.func_76609_d(p_76599_1_, p_76599_2_, i, p_76599_3_ + 1);
        }
    }

    private void func_76609_d(int p_76609_1_, int p_76609_2_, int p_76609_3_, int p_76609_4_)
    {
        if (p_76609_4_ > p_76609_3_ && this.world.func_175697_a(new BlockPos(p_76609_1_, 0, p_76609_2_), 16))
        {
            for (int i = p_76609_3_; i < p_76609_4_; ++i)
            {
                this.world.func_180500_c(EnumSkyBlock.SKY, new BlockPos(p_76609_1_, i, p_76609_2_));
            }

            this.dirty = true;
        }
    }

    private void func_76615_h(int p_76615_1_, int p_76615_2_, int p_76615_3_)
    {
        int i = this.heightMap[p_76615_3_ << 4 | p_76615_1_] & 255;
        int j = i;

        if (p_76615_2_ > i)
        {
            j = p_76615_2_;
        }

        while (j > 0 && this.func_150808_b(p_76615_1_, j - 1, p_76615_3_) == 0)
        {
            --j;
        }

        if (j != i)
        {
            this.world.func_72975_g(p_76615_1_ + this.field_76635_g * 16, p_76615_3_ + this.field_76647_h * 16, j, i);
            this.heightMap[p_76615_3_ << 4 | p_76615_1_] = j;
            int k = this.field_76635_g * 16 + p_76615_1_;
            int l = this.field_76647_h * 16 + p_76615_3_;

            if (this.world.dimension.hasSkyLight())
            {
                if (j < i)
                {
                    for (int j1 = j; j1 < i; ++j1)
                    {
                        ExtendedBlockStorage extendedblockstorage2 = this.sections[j1 >> 4];

                        if (extendedblockstorage2 != EMPTY_SECTION)
                        {
                            extendedblockstorage2.func_76657_c(p_76615_1_, j1 & 15, p_76615_3_, 15);
                            this.world.func_175679_n(new BlockPos((this.field_76635_g << 4) + p_76615_1_, j1, (this.field_76647_h << 4) + p_76615_3_));
                        }
                    }
                }
                else
                {
                    for (int i1 = i; i1 < j; ++i1)
                    {
                        ExtendedBlockStorage extendedblockstorage = this.sections[i1 >> 4];

                        if (extendedblockstorage != EMPTY_SECTION)
                        {
                            extendedblockstorage.func_76657_c(p_76615_1_, i1 & 15, p_76615_3_, 0);
                            this.world.func_175679_n(new BlockPos((this.field_76635_g << 4) + p_76615_1_, i1, (this.field_76647_h << 4) + p_76615_3_));
                        }
                    }
                }

                int k1 = 15;

                while (j > 0 && k1 > 0)
                {
                    --j;
                    int i2 = this.func_150808_b(p_76615_1_, j, p_76615_3_);

                    if (i2 == 0)
                    {
                        i2 = 1;
                    }

                    k1 -= i2;

                    if (k1 < 0)
                    {
                        k1 = 0;
                    }

                    ExtendedBlockStorage extendedblockstorage1 = this.sections[j >> 4];

                    if (extendedblockstorage1 != EMPTY_SECTION)
                    {
                        extendedblockstorage1.func_76657_c(p_76615_1_, j & 15, p_76615_3_, k1);
                    }
                }
            }

            int l1 = this.heightMap[p_76615_3_ << 4 | p_76615_1_];
            int j2 = i;
            int k2 = l1;

            if (l1 < i)
            {
                j2 = l1;
                k2 = i;
            }

            if (l1 < this.field_82912_p)
            {
                this.field_82912_p = l1;
            }

            if (this.world.dimension.hasSkyLight())
            {
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                {
                    this.func_76609_d(k + enumfacing.getXOffset(), l + enumfacing.getZOffset(), j2, k2);
                }

                this.func_76609_d(k, l, j2, k2);
            }

            this.dirty = true;
        }
    }

    public int func_177437_b(BlockPos p_177437_1_)
    {
        return this.func_177435_g(p_177437_1_).func_185891_c();
    }

    private int func_150808_b(int p_150808_1_, int p_150808_2_, int p_150808_3_)
    {
        return this.func_186032_a(p_150808_1_, p_150808_2_, p_150808_3_).func_185891_c();
    }

    public IBlockState func_177435_g(BlockPos p_177435_1_)
    {
        return this.func_186032_a(p_177435_1_.getX(), p_177435_1_.getY(), p_177435_1_.getZ());
    }

    public IBlockState func_186032_a(final int p_186032_1_, final int p_186032_2_, final int p_186032_3_)
    {
        if (this.world.getWorldType() == WorldType.DEBUG_ALL_BLOCK_STATES)
        {
            IBlockState iblockstate = null;

            if (p_186032_2_ == 60)
            {
                iblockstate = Blocks.BARRIER.getDefaultState();
            }

            if (p_186032_2_ == 70)
            {
                iblockstate = ChunkGeneratorDebug.getBlockStateFor(p_186032_1_, p_186032_3_);
            }

            return iblockstate == null ? Blocks.AIR.getDefaultState() : iblockstate;
        }
        else
        {
            try
            {
                if (p_186032_2_ >= 0 && p_186032_2_ >> 4 < this.sections.length)
                {
                    ExtendedBlockStorage extendedblockstorage = this.sections[p_186032_2_ >> 4];

                    if (extendedblockstorage != EMPTY_SECTION)
                    {
                        return extendedblockstorage.getBlockState(p_186032_1_ & 15, p_186032_2_ & 15, p_186032_3_ & 15);
                    }
                }

                return Blocks.AIR.getDefaultState();
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block state");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being got");
                crashreportcategory.addDetail("Location", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return CrashReportCategory.getCoordinateInfo(p_186032_1_, p_186032_2_, p_186032_3_);
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    @Nullable
    public IBlockState setBlockState(BlockPos pos, IBlockState state)
    {
        int i = pos.getX() & 15;
        int j = pos.getY();
        int k = pos.getZ() & 15;
        int l = k << 4 | i;

        if (j >= this.field_76638_b[l] - 1)
        {
            this.field_76638_b[l] = -999;
        }

        int i1 = this.heightMap[l];
        IBlockState iblockstate = this.func_177435_g(pos);

        if (iblockstate == state)
        {
            return null;
        }
        else
        {
            Block block = state.getBlock();
            Block block1 = iblockstate.getBlock();
            ExtendedBlockStorage extendedblockstorage = this.sections[j >> 4];
            boolean flag = false;

            if (extendedblockstorage == EMPTY_SECTION)
            {
                if (block == Blocks.AIR)
                {
                    return null;
                }

                extendedblockstorage = new ExtendedBlockStorage(j >> 4 << 4, this.world.dimension.hasSkyLight());
                this.sections[j >> 4] = extendedblockstorage;
                flag = j >= i1;
            }

            extendedblockstorage.setBlockState(i, j & 15, k, state);

            if (block1 != block)
            {
                if (!this.world.isRemote)
                {
                    block1.func_180663_b(this.world, pos, iblockstate);
                }
                else if (block1 instanceof ITileEntityProvider)
                {
                    this.world.removeTileEntity(pos);
                }
            }

            if (extendedblockstorage.getBlockState(i, j & 15, k).getBlock() != block)
            {
                return null;
            }
            else
            {
                if (flag)
                {
                    this.func_76603_b();
                }
                else
                {
                    int j1 = state.func_185891_c();
                    int k1 = iblockstate.func_185891_c();

                    if (j1 > 0)
                    {
                        if (j >= i1)
                        {
                            this.func_76615_h(i, j + 1, k);
                        }
                    }
                    else if (j == i1 - 1)
                    {
                        this.func_76615_h(i, j, k);
                    }

                    if (j1 != k1 && (j1 < k1 || this.func_177413_a(EnumSkyBlock.SKY, pos) > 0 || this.func_177413_a(EnumSkyBlock.BLOCK, pos) > 0))
                    {
                        this.func_76595_e(i, k);
                    }
                }

                if (block1 instanceof ITileEntityProvider)
                {
                    TileEntity tileentity = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);

                    if (tileentity != null)
                    {
                        tileentity.updateContainingBlockInfo();
                    }
                }

                if (!this.world.isRemote && block1 != block)
                {
                    block.func_176213_c(this.world, pos, state);
                }

                if (block instanceof ITileEntityProvider)
                {
                    TileEntity tileentity1 = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);

                    if (tileentity1 == null)
                    {
                        tileentity1 = ((ITileEntityProvider)block).func_149915_a(this.world, block.func_176201_c(state));
                        this.world.setTileEntity(pos, tileentity1);
                    }

                    if (tileentity1 != null)
                    {
                        tileentity1.updateContainingBlockInfo();
                    }
                }

                this.dirty = true;
                return iblockstate;
            }
        }
    }

    public int func_177413_a(EnumSkyBlock p_177413_1_, BlockPos p_177413_2_)
    {
        int i = p_177413_2_.getX() & 15;
        int j = p_177413_2_.getY();
        int k = p_177413_2_.getZ() & 15;
        ExtendedBlockStorage extendedblockstorage = this.sections[j >> 4];

        if (extendedblockstorage == EMPTY_SECTION)
        {
            return this.func_177444_d(p_177413_2_) ? p_177413_1_.defaultLightValue : 0;
        }
        else if (p_177413_1_ == EnumSkyBlock.SKY)
        {
            return !this.world.dimension.hasSkyLight() ? 0 : extendedblockstorage.func_76670_c(i, j & 15, k);
        }
        else
        {
            return p_177413_1_ == EnumSkyBlock.BLOCK ? extendedblockstorage.func_76674_d(i, j & 15, k) : p_177413_1_.defaultLightValue;
        }
    }

    public void func_177431_a(EnumSkyBlock p_177431_1_, BlockPos p_177431_2_, int p_177431_3_)
    {
        int i = p_177431_2_.getX() & 15;
        int j = p_177431_2_.getY();
        int k = p_177431_2_.getZ() & 15;
        ExtendedBlockStorage extendedblockstorage = this.sections[j >> 4];

        if (extendedblockstorage == EMPTY_SECTION)
        {
            extendedblockstorage = new ExtendedBlockStorage(j >> 4 << 4, this.world.dimension.hasSkyLight());
            this.sections[j >> 4] = extendedblockstorage;
            this.func_76603_b();
        }

        this.dirty = true;

        if (p_177431_1_ == EnumSkyBlock.SKY)
        {
            if (this.world.dimension.hasSkyLight())
            {
                extendedblockstorage.func_76657_c(i, j & 15, k, p_177431_3_);
            }
        }
        else if (p_177431_1_ == EnumSkyBlock.BLOCK)
        {
            extendedblockstorage.func_76677_d(i, j & 15, k, p_177431_3_);
        }
    }

    public int func_177443_a(BlockPos p_177443_1_, int p_177443_2_)
    {
        int i = p_177443_1_.getX() & 15;
        int j = p_177443_1_.getY();
        int k = p_177443_1_.getZ() & 15;
        ExtendedBlockStorage extendedblockstorage = this.sections[j >> 4];

        if (extendedblockstorage == EMPTY_SECTION)
        {
            return this.world.dimension.hasSkyLight() && p_177443_2_ < EnumSkyBlock.SKY.defaultLightValue ? EnumSkyBlock.SKY.defaultLightValue - p_177443_2_ : 0;
        }
        else
        {
            int l = !this.world.dimension.hasSkyLight() ? 0 : extendedblockstorage.func_76670_c(i, j & 15, k);
            l = l - p_177443_2_;
            int i1 = extendedblockstorage.func_76674_d(i, j & 15, k);

            if (i1 > l)
            {
                l = i1;
            }

            return l;
        }
    }

    /**
     * Adds an entity to the chunk.
     */
    public void addEntity(Entity entityIn)
    {
        this.hasEntities = true;
        int i = MathHelper.floor(entityIn.posX / 16.0D);
        int j = MathHelper.floor(entityIn.posZ / 16.0D);

        if (i != this.field_76635_g || j != this.field_76647_h)
        {
            LOGGER.warn("Wrong location! ({}, {}) should be ({}, {}), {}", Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(this.field_76635_g), Integer.valueOf(this.field_76647_h), entityIn);
            entityIn.remove();
        }

        int k = MathHelper.floor(entityIn.posY / 16.0D);

        if (k < 0)
        {
            k = 0;
        }

        if (k >= this.entityLists.length)
        {
            k = this.entityLists.length - 1;
        }

        entityIn.addedToChunk = true;
        entityIn.chunkCoordX = this.field_76635_g;
        entityIn.chunkCoordY = k;
        entityIn.chunkCoordZ = this.field_76647_h;
        this.entityLists[k].add(entityIn);
    }

    /**
     * removes entity using its y chunk coordinate as its index
     */
    public void removeEntity(Entity entityIn)
    {
        this.removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
    }

    /**
     * Removes entity at the specified index from the entity array.
     */
    public void removeEntityAtIndex(Entity entityIn, int index)
    {
        if (index < 0)
        {
            index = 0;
        }

        if (index >= this.entityLists.length)
        {
            index = this.entityLists.length - 1;
        }

        this.entityLists[index].remove(entityIn);
    }

    public boolean func_177444_d(BlockPos p_177444_1_)
    {
        int i = p_177444_1_.getX() & 15;
        int j = p_177444_1_.getY();
        int k = p_177444_1_.getZ() & 15;
        return j >= this.heightMap[k << 4 | i];
    }

    @Nullable
    private TileEntity createNewTileEntity(BlockPos pos)
    {
        IBlockState iblockstate = this.func_177435_g(pos);
        Block block = iblockstate.getBlock();
        return !block.hasTileEntity() ? null : ((ITileEntityProvider)block).func_149915_a(this.world, iblockstate.getBlock().func_176201_c(iblockstate));
    }

    @Nullable
    public TileEntity getTileEntity(BlockPos pos, Chunk.EnumCreateEntityType creationMode)
    {
        TileEntity tileentity = this.tileEntities.get(pos);

        if (tileentity == null)
        {
            if (creationMode == Chunk.EnumCreateEntityType.IMMEDIATE)
            {
                tileentity = this.createNewTileEntity(pos);
                this.world.setTileEntity(pos, tileentity);
            }
            else if (creationMode == Chunk.EnumCreateEntityType.QUEUED)
            {
                this.field_177447_w.add(pos);
            }
        }
        else if (tileentity.isRemoved())
        {
            this.tileEntities.remove(pos);
            return null;
        }

        return tileentity;
    }

    public void addTileEntity(TileEntity tileEntityIn)
    {
        this.addTileEntity(tileEntityIn.getPos(), tileEntityIn);

        if (this.loaded)
        {
            this.world.addTileEntity(tileEntityIn);
        }
    }

    public void addTileEntity(BlockPos pos, TileEntity tileEntityIn)
    {
        tileEntityIn.func_145834_a(this.world);
        tileEntityIn.setPos(pos);

        if (this.func_177435_g(pos).getBlock() instanceof ITileEntityProvider)
        {
            if (this.tileEntities.containsKey(pos))
            {
                ((TileEntity)this.tileEntities.get(pos)).remove();
            }

            tileEntityIn.validate();
            this.tileEntities.put(pos, tileEntityIn);
        }
    }

    public void removeTileEntity(BlockPos pos)
    {
        if (this.loaded)
        {
            TileEntity tileentity = this.tileEntities.remove(pos);

            if (tileentity != null)
            {
                tileentity.remove();
            }
        }
    }

    public void func_76631_c()
    {
        this.loaded = true;
        this.world.addTileEntities(this.tileEntities.values());

        for (ClassInheritanceMultiMap<Entity> classinheritancemultimap : this.entityLists)
        {
            this.world.func_175650_b(classinheritancemultimap);
        }
    }

    public void func_76623_d()
    {
        this.loaded = false;

        for (TileEntity tileentity : this.tileEntities.values())
        {
            this.world.func_147457_a(tileentity);
        }

        for (ClassInheritanceMultiMap<Entity> classinheritancemultimap : this.entityLists)
        {
            this.world.func_175681_c(classinheritancemultimap);
        }
    }

    /**
     * Sets the isModified flag for this Chunk
     */
    public void markDirty()
    {
        this.dirty = true;
    }

    /**
     * Fills the given list of all entities that intersect within the given bounding box that aren't the passed entity.
     */
    public void getEntitiesWithinAABBForEntity(@Nullable Entity entityIn, AxisAlignedBB aabb, List<Entity> listToFill, Predicate <? super Entity > filter)
    {
        int i = MathHelper.floor((aabb.minY - 2.0D) / 16.0D);
        int j = MathHelper.floor((aabb.maxY + 2.0D) / 16.0D);
        i = MathHelper.clamp(i, 0, this.entityLists.length - 1);
        j = MathHelper.clamp(j, 0, this.entityLists.length - 1);

        for (int k = i; k <= j; ++k)
        {
            if (!this.entityLists[k].isEmpty())
            {
                for (Entity entity : this.entityLists[k])
                {
                    if (entity.getBoundingBox().intersects(aabb) && entity != entityIn)
                    {
                        if (filter == null || filter.apply(entity))
                        {
                            listToFill.add(entity);
                        }

                        Entity[] aentity = entity.func_70021_al();

                        if (aentity != null)
                        {
                            for (Entity entity1 : aentity)
                            {
                                if (entity1 != entityIn && entity1.getBoundingBox().intersects(aabb) && (filter == null || filter.apply(entity1)))
                                {
                                    listToFill.add(entity1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public <T extends Entity> void getEntitiesOfTypeWithinAABB(Class <? extends T > entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate <? super T > filter)
    {
        int i = MathHelper.floor((aabb.minY - 2.0D) / 16.0D);
        int j = MathHelper.floor((aabb.maxY + 2.0D) / 16.0D);
        i = MathHelper.clamp(i, 0, this.entityLists.length - 1);
        j = MathHelper.clamp(j, 0, this.entityLists.length - 1);

        for (int k = i; k <= j; ++k)
        {
            for (T t : this.entityLists[k].func_180215_b(entityClass))
            {
                if (t.getBoundingBox().intersects(aabb) && (filter == null || filter.apply(t)))
                {
                    listToFill.add(t);
                }
            }
        }
    }

    public boolean func_76601_a(boolean p_76601_1_)
    {
        if (p_76601_1_)
        {
            if (this.hasEntities && this.world.getGameTime() != this.lastSaveTime || this.dirty)
            {
                return true;
            }
        }
        else if (this.hasEntities && this.world.getGameTime() >= this.lastSaveTime + 600L)
        {
            return true;
        }

        return this.dirty;
    }

    public Random func_76617_a(long p_76617_1_)
    {
        return new Random(this.world.getSeed() + (long)(this.field_76635_g * this.field_76635_g * 4987142) + (long)(this.field_76635_g * 5947611) + (long)(this.field_76647_h * this.field_76647_h) * 4392871L + (long)(this.field_76647_h * 389711) ^ p_76617_1_);
    }

    public boolean isEmpty()
    {
        return false;
    }

    public void func_186030_a(IChunkProvider p_186030_1_, IChunkGenerator p_186030_2_)
    {
        Chunk chunk = p_186030_1_.func_186026_b(this.field_76635_g, this.field_76647_h - 1);
        Chunk chunk1 = p_186030_1_.func_186026_b(this.field_76635_g + 1, this.field_76647_h);
        Chunk chunk2 = p_186030_1_.func_186026_b(this.field_76635_g, this.field_76647_h + 1);
        Chunk chunk3 = p_186030_1_.func_186026_b(this.field_76635_g - 1, this.field_76647_h);

        if (chunk1 != null && chunk2 != null && p_186030_1_.func_186026_b(this.field_76635_g + 1, this.field_76647_h + 1) != null)
        {
            this.func_186034_a(p_186030_2_);
        }

        if (chunk3 != null && chunk2 != null && p_186030_1_.func_186026_b(this.field_76635_g - 1, this.field_76647_h + 1) != null)
        {
            chunk3.func_186034_a(p_186030_2_);
        }

        if (chunk != null && chunk1 != null && p_186030_1_.func_186026_b(this.field_76635_g + 1, this.field_76647_h - 1) != null)
        {
            chunk.func_186034_a(p_186030_2_);
        }

        if (chunk != null && chunk3 != null)
        {
            Chunk chunk4 = p_186030_1_.func_186026_b(this.field_76635_g - 1, this.field_76647_h - 1);

            if (chunk4 != null)
            {
                chunk4.func_186034_a(p_186030_2_);
            }
        }
    }

    protected void func_186034_a(IChunkGenerator p_186034_1_)
    {
        if (this.func_177419_t())
        {
            if (p_186034_1_.func_185933_a(this, this.field_76635_g, this.field_76647_h))
            {
                this.markDirty();
            }
        }
        else
        {
            this.func_150809_p();
            p_186034_1_.func_185931_b(this.field_76635_g, this.field_76647_h);
            this.markDirty();
        }
    }

    public BlockPos func_177440_h(BlockPos p_177440_1_)
    {
        int i = p_177440_1_.getX() & 15;
        int j = p_177440_1_.getZ() & 15;
        int k = i | j << 4;
        BlockPos blockpos = new BlockPos(p_177440_1_.getX(), this.field_76638_b[k], p_177440_1_.getZ());

        if (blockpos.getY() == -999)
        {
            int l = this.getTopFilledSegment() + 15;
            blockpos = new BlockPos(p_177440_1_.getX(), l, p_177440_1_.getZ());
            int i1 = -1;

            while (blockpos.getY() > 0 && i1 == -1)
            {
                IBlockState iblockstate = this.func_177435_g(blockpos);
                Material material = iblockstate.getMaterial();

                if (!material.blocksMovement() && !material.isLiquid())
                {
                    blockpos = blockpos.down();
                }
                else
                {
                    i1 = blockpos.getY() + 1;
                }
            }

            this.field_76638_b[k] = i1;
        }

        return new BlockPos(p_177440_1_.getX(), this.field_76638_b[k], p_177440_1_.getZ());
    }

    public void func_150804_b(boolean p_150804_1_)
    {
        if (this.field_76650_s && this.world.dimension.hasSkyLight() && !p_150804_1_)
        {
            this.func_150803_c(this.world.isRemote);
        }

        this.field_150815_m = true;

        if (!this.field_150814_l && this.field_76646_k)
        {
            this.func_150809_p();
        }

        while (!this.field_177447_w.isEmpty())
        {
            BlockPos blockpos = this.field_177447_w.poll();

            if (this.getTileEntity(blockpos, Chunk.EnumCreateEntityType.CHECK) == null && this.func_177435_g(blockpos).getBlock().hasTileEntity())
            {
                TileEntity tileentity = this.createNewTileEntity(blockpos);
                this.world.setTileEntity(blockpos, tileentity);
                this.world.func_175704_b(blockpos, blockpos);
            }
        }
    }

    public boolean func_150802_k()
    {
        return this.field_150815_m && this.field_76646_k && this.field_150814_l;
    }

    public boolean func_186035_j()
    {
        return this.field_150815_m;
    }

    /**
     * Gets a {@link ChunkPos} representing the x and z coordinates of this chunk.
     */
    public ChunkPos getPos()
    {
        return new ChunkPos(this.field_76635_g, this.field_76647_h);
    }

    /**
     * Returns whether the ExtendedBlockStorages containing levels (in blocks) from arg 1 to arg 2 are fully empty
     * (true) or not (false).
     */
    public boolean isEmptyBetween(int startY, int endY)
    {
        if (startY < 0)
        {
            startY = 0;
        }

        if (endY >= 256)
        {
            endY = 255;
        }

        for (int i = startY; i <= endY; i += 16)
        {
            ExtendedBlockStorage extendedblockstorage = this.sections[i >> 4];

            if (extendedblockstorage != EMPTY_SECTION && !extendedblockstorage.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public void func_76602_a(ExtendedBlockStorage[] p_76602_1_)
    {
        if (this.sections.length != p_76602_1_.length)
        {
            LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", Integer.valueOf(p_76602_1_.length), Integer.valueOf(this.sections.length));
        }
        else
        {
            System.arraycopy(p_76602_1_, 0, this.sections, 0, this.sections.length);
        }
    }

    public void func_186033_a(PacketBuffer p_186033_1_, int p_186033_2_, boolean p_186033_3_)
    {
        boolean flag = this.world.dimension.hasSkyLight();

        for (int i = 0; i < this.sections.length; ++i)
        {
            ExtendedBlockStorage extendedblockstorage = this.sections[i];

            if ((p_186033_2_ & 1 << i) == 0)
            {
                if (p_186033_3_ && extendedblockstorage != EMPTY_SECTION)
                {
                    this.sections[i] = EMPTY_SECTION;
                }
            }
            else
            {
                if (extendedblockstorage == EMPTY_SECTION)
                {
                    extendedblockstorage = new ExtendedBlockStorage(i << 4, flag);
                    this.sections[i] = extendedblockstorage;
                }

                extendedblockstorage.getData().read(p_186033_1_);
                p_186033_1_.readBytes(extendedblockstorage.func_76661_k().getData());

                if (flag)
                {
                    p_186033_1_.readBytes(extendedblockstorage.func_76671_l().getData());
                }
            }
        }

        if (p_186033_3_)
        {
            p_186033_1_.readBytes(this.blockBiomeArray);
        }

        for (int j = 0; j < this.sections.length; ++j)
        {
            if (this.sections[j] != EMPTY_SECTION && (p_186033_2_ & 1 << j) != 0)
            {
                this.sections[j].recalculateRefCounts();
            }
        }

        this.field_150814_l = true;
        this.field_76646_k = true;
        this.func_76590_a();

        for (TileEntity tileentity : this.tileEntities.values())
        {
            tileentity.updateContainingBlockInfo();
        }
    }

    public Biome func_177411_a(BlockPos p_177411_1_, BiomeProvider p_177411_2_)
    {
        int i = p_177411_1_.getX() & 15;
        int j = p_177411_1_.getZ() & 15;
        int k = this.blockBiomeArray[j << 4 | i] & 255;

        if (k == 255)
        {
            Biome biome = p_177411_2_.func_180300_a(p_177411_1_, Biomes.PLAINS);
            k = Biome.func_185362_a(biome);
            this.blockBiomeArray[j << 4 | i] = (byte)(k & 255);
        }

        Biome biome1 = Biome.func_150568_d(k);
        return biome1 == null ? Biomes.PLAINS : biome1;
    }

    public byte[] func_76605_m()
    {
        return this.blockBiomeArray;
    }

    public void func_76616_a(byte[] p_76616_1_)
    {
        if (this.blockBiomeArray.length != p_76616_1_.length)
        {
            LOGGER.warn("Could not set level chunk biomes, array length is {} instead of {}", Integer.valueOf(p_76616_1_.length), Integer.valueOf(this.blockBiomeArray.length));
        }
        else
        {
            System.arraycopy(p_76616_1_, 0, this.blockBiomeArray, 0, this.blockBiomeArray.length);
        }
    }

    public void func_76613_n()
    {
        this.field_76649_t = 0;
    }

    public void func_76594_o()
    {
        if (this.field_76649_t < 4096)
        {
            BlockPos blockpos = new BlockPos(this.field_76635_g << 4, 0, this.field_76647_h << 4);

            for (int i = 0; i < 8; ++i)
            {
                if (this.field_76649_t >= 4096)
                {
                    return;
                }

                int j = this.field_76649_t % 16;
                int k = this.field_76649_t / 16 % 16;
                int l = this.field_76649_t / 256;
                ++this.field_76649_t;

                for (int i1 = 0; i1 < 16; ++i1)
                {
                    BlockPos blockpos1 = blockpos.add(k, (j << 4) + i1, l);
                    boolean flag = i1 == 0 || i1 == 15 || k == 0 || k == 15 || l == 0 || l == 15;

                    if (this.sections[j] == EMPTY_SECTION && flag || this.sections[j] != EMPTY_SECTION && this.sections[j].getBlockState(k, i1, l).getMaterial() == Material.AIR)
                    {
                        for (EnumFacing enumfacing : EnumFacing.values())
                        {
                            BlockPos blockpos2 = blockpos1.offset(enumfacing);

                            if (this.world.getBlockState(blockpos2).getLightValue() > 0)
                            {
                                this.world.func_175664_x(blockpos2);
                            }
                        }

                        this.world.func_175664_x(blockpos1);
                    }
                }
            }
        }
    }

    public void func_150809_p()
    {
        this.field_76646_k = true;
        this.field_150814_l = true;
        BlockPos blockpos = new BlockPos(this.field_76635_g << 4, 0, this.field_76647_h << 4);

        if (this.world.dimension.hasSkyLight())
        {
            if (this.world.isAreaLoaded(blockpos.add(-1, 0, -1), blockpos.add(16, this.world.getSeaLevel(), 16)))
            {
                label44:

                for (int i = 0; i < 16; ++i)
                {
                    for (int j = 0; j < 16; ++j)
                    {
                        if (!this.func_150811_f(i, j))
                        {
                            this.field_150814_l = false;
                            break label44;
                        }
                    }
                }

                if (this.field_150814_l)
                {
                    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                    {
                        int k = enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 16 : 1;
                        this.world.getChunkAt(blockpos.offset(enumfacing, k)).func_180700_a(enumfacing.getOpposite());
                    }

                    this.func_177441_y();
                }
            }
            else
            {
                this.field_150814_l = false;
            }
        }
    }

    private void func_177441_y()
    {
        for (int i = 0; i < this.field_76639_c.length; ++i)
        {
            this.field_76639_c[i] = true;
        }

        this.func_150803_c(false);
    }

    private void func_180700_a(EnumFacing p_180700_1_)
    {
        if (this.field_76646_k)
        {
            if (p_180700_1_ == EnumFacing.EAST)
            {
                for (int i = 0; i < 16; ++i)
                {
                    this.func_150811_f(15, i);
                }
            }
            else if (p_180700_1_ == EnumFacing.WEST)
            {
                for (int j = 0; j < 16; ++j)
                {
                    this.func_150811_f(0, j);
                }
            }
            else if (p_180700_1_ == EnumFacing.SOUTH)
            {
                for (int k = 0; k < 16; ++k)
                {
                    this.func_150811_f(k, 15);
                }
            }
            else if (p_180700_1_ == EnumFacing.NORTH)
            {
                for (int l = 0; l < 16; ++l)
                {
                    this.func_150811_f(l, 0);
                }
            }
        }
    }

    private boolean func_150811_f(int p_150811_1_, int p_150811_2_)
    {
        int i = this.getTopFilledSegment();
        boolean flag = false;
        boolean flag1 = false;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos((this.field_76635_g << 4) + p_150811_1_, 0, (this.field_76647_h << 4) + p_150811_2_);

        for (int j = i + 16 - 1; j > this.world.getSeaLevel() || j > 0 && !flag1; --j)
        {
            blockpos$mutableblockpos.setPos(blockpos$mutableblockpos.getX(), j, blockpos$mutableblockpos.getZ());
            int k = this.func_177437_b(blockpos$mutableblockpos);

            if (k == 255 && blockpos$mutableblockpos.getY() < this.world.getSeaLevel())
            {
                flag1 = true;
            }

            if (!flag && k > 0)
            {
                flag = true;
            }
            else if (flag && k == 0 && !this.world.func_175664_x(blockpos$mutableblockpos))
            {
                return false;
            }
        }

        for (int l = blockpos$mutableblockpos.getY(); l > 0; --l)
        {
            blockpos$mutableblockpos.setPos(blockpos$mutableblockpos.getX(), l, blockpos$mutableblockpos.getZ());

            if (this.func_177435_g(blockpos$mutableblockpos).getLightValue() > 0)
            {
                this.world.func_175664_x(blockpos$mutableblockpos);
            }
        }

        return true;
    }

    public boolean func_177410_o()
    {
        return this.loaded;
    }

    public void setLoaded(boolean loaded)
    {
        this.loaded = loaded;
    }

    public World getWorld()
    {
        return this.world;
    }

    public int[] func_177445_q()
    {
        return this.heightMap;
    }

    public void func_177420_a(int[] p_177420_1_)
    {
        if (this.heightMap.length != p_177420_1_.length)
        {
            LOGGER.warn("Could not set level chunk heightmap, array length is {} instead of {}", Integer.valueOf(p_177420_1_.length), Integer.valueOf(this.heightMap.length));
        }
        else
        {
            System.arraycopy(p_177420_1_, 0, this.heightMap, 0, this.heightMap.length);
        }
    }

    public Map<BlockPos, TileEntity> getTileEntityMap()
    {
        return this.tileEntities;
    }

    public ClassInheritanceMultiMap<Entity>[] getEntityLists()
    {
        return this.entityLists;
    }

    public boolean func_177419_t()
    {
        return this.field_76646_k;
    }

    public void func_177446_d(boolean p_177446_1_)
    {
        this.field_76646_k = p_177446_1_;
    }

    public boolean func_177423_u()
    {
        return this.field_150814_l;
    }

    public void func_177421_e(boolean p_177421_1_)
    {
        this.field_150814_l = p_177421_1_;
    }

    public void setModified(boolean modified)
    {
        this.dirty = modified;
    }

    public void setHasEntities(boolean hasEntitiesIn)
    {
        this.hasEntities = hasEntitiesIn;
    }

    public void setLastSaveTime(long saveTime)
    {
        this.lastSaveTime = saveTime;
    }

    public int func_177442_v()
    {
        return this.field_82912_p;
    }

    public long getInhabitedTime()
    {
        return this.inhabitedTime;
    }

    public void setInhabitedTime(long newInhabitedTime)
    {
        this.inhabitedTime = newInhabitedTime;
    }

    public static enum EnumCreateEntityType
    {
        IMMEDIATE,
        QUEUED,
        CHECK;
    }
}

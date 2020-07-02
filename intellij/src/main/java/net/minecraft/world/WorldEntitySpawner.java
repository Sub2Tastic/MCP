package net.minecraft.world;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public final class WorldEntitySpawner
{
    private static final int field_180268_a = (int)Math.pow(17.0D, 2.0D);
    private final Set<ChunkPos> field_77193_b = Sets.<ChunkPos>newHashSet();

    public int func_77192_a(WorldServer p_77192_1_, boolean p_77192_2_, boolean p_77192_3_, boolean p_77192_4_)
    {
        if (!p_77192_2_ && !p_77192_3_)
        {
            return 0;
        }
        else
        {
            this.field_77193_b.clear();
            int i = 0;

            for (EntityPlayer entityplayer : p_77192_1_.field_73010_i)
            {
                if (!entityplayer.isSpectator())
                {
                    int j = MathHelper.floor(entityplayer.posX / 16.0D);
                    int k = MathHelper.floor(entityplayer.posZ / 16.0D);
                    int l = 8;

                    for (int i1 = -8; i1 <= 8; ++i1)
                    {
                        for (int j1 = -8; j1 <= 8; ++j1)
                        {
                            boolean flag = i1 == -8 || i1 == 8 || j1 == -8 || j1 == 8;
                            ChunkPos chunkpos = new ChunkPos(i1 + j, j1 + k);

                            if (!this.field_77193_b.contains(chunkpos))
                            {
                                ++i;

                                if (!flag && p_77192_1_.getWorldBorder().contains(chunkpos))
                                {
                                    PlayerChunkMapEntry playerchunkmapentry = p_77192_1_.func_184164_w().func_187301_b(chunkpos.x, chunkpos.z);

                                    if (playerchunkmapentry != null && playerchunkmapentry.func_187274_e())
                                    {
                                        this.field_77193_b.add(chunkpos);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            int j4 = 0;
            BlockPos blockpos1 = p_77192_1_.getSpawnPoint();

            for (EnumCreatureType enumcreaturetype : EnumCreatureType.values())
            {
                if ((!enumcreaturetype.getPeacefulCreature() || p_77192_3_) && (enumcreaturetype.getPeacefulCreature() || p_77192_2_) && (!enumcreaturetype.getAnimal() || p_77192_4_))
                {
                    int k4 = p_77192_1_.func_72907_a(enumcreaturetype.func_75598_a());
                    int l4 = enumcreaturetype.getMaxNumberOfCreature() * i / field_180268_a;

                    if (k4 <= l4)
                    {
                        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                        label134:

                        for (ChunkPos chunkpos1 : this.field_77193_b)
                        {
                            BlockPos blockpos = func_180621_a(p_77192_1_, chunkpos1.x, chunkpos1.z);
                            int k1 = blockpos.getX();
                            int l1 = blockpos.getY();
                            int i2 = blockpos.getZ();
                            IBlockState iblockstate = p_77192_1_.getBlockState(blockpos);

                            if (!iblockstate.func_185915_l())
                            {
                                int j2 = 0;

                                for (int k2 = 0; k2 < 3; ++k2)
                                {
                                    int l2 = k1;
                                    int i3 = l1;
                                    int j3 = i2;
                                    int k3 = 6;
                                    Biome.SpawnListEntry biome$spawnlistentry = null;
                                    IEntityLivingData ientitylivingdata = null;
                                    int l3 = MathHelper.ceil(Math.random() * 4.0D);

                                    for (int i4 = 0; i4 < l3; ++i4)
                                    {
                                        l2 += p_77192_1_.rand.nextInt(6) - p_77192_1_.rand.nextInt(6);
                                        i3 += p_77192_1_.rand.nextInt(1) - p_77192_1_.rand.nextInt(1);
                                        j3 += p_77192_1_.rand.nextInt(6) - p_77192_1_.rand.nextInt(6);
                                        blockpos$mutableblockpos.setPos(l2, i3, j3);
                                        float f = (float)l2 + 0.5F;
                                        float f1 = (float)j3 + 0.5F;

                                        if (!p_77192_1_.func_175636_b((double)f, (double)i3, (double)f1, 24.0D) && blockpos1.func_177954_c((double)f, (double)i3, (double)f1) >= 576.0D)
                                        {
                                            if (biome$spawnlistentry == null)
                                            {
                                                biome$spawnlistentry = p_77192_1_.func_175734_a(enumcreaturetype, blockpos$mutableblockpos);

                                                if (biome$spawnlistentry == null)
                                                {
                                                    break;
                                                }
                                            }

                                            if (p_77192_1_.func_175732_a(enumcreaturetype, biome$spawnlistentry, blockpos$mutableblockpos) && func_180267_a(EntitySpawnPlacementRegistry.func_180109_a(biome$spawnlistentry.field_76300_b), p_77192_1_, blockpos$mutableblockpos))
                                            {
                                                EntityLiving entityliving;

                                                try
                                                {
                                                    entityliving = biome$spawnlistentry.field_76300_b.getConstructor(World.class).newInstance(p_77192_1_);
                                                }
                                                catch (Exception exception)
                                                {
                                                    exception.printStackTrace();
                                                    return j4;
                                                }

                                                entityliving.setLocationAndAngles((double)f, (double)i3, (double)f1, p_77192_1_.rand.nextFloat() * 360.0F, 0.0F);

                                                if (entityliving.func_70601_bi() && entityliving.func_70058_J())
                                                {
                                                    ientitylivingdata = entityliving.func_180482_a(p_77192_1_.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);

                                                    if (entityliving.func_70058_J())
                                                    {
                                                        ++j2;
                                                        p_77192_1_.addEntity0(entityliving);
                                                    }
                                                    else
                                                    {
                                                        entityliving.remove();
                                                    }

                                                    if (j2 >= entityliving.getMaxSpawnedInChunk())
                                                    {
                                                        continue label134;
                                                    }
                                                }

                                                j4 += j2;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return j4;
        }
    }

    private static BlockPos func_180621_a(World p_180621_0_, int p_180621_1_, int p_180621_2_)
    {
        Chunk chunk = p_180621_0_.func_72964_e(p_180621_1_, p_180621_2_);
        int i = p_180621_1_ * 16 + p_180621_0_.rand.nextInt(16);
        int j = p_180621_2_ * 16 + p_180621_0_.rand.nextInt(16);
        int k = MathHelper.roundUp(chunk.func_177433_f(new BlockPos(i, 0, j)) + 1, 16);
        int l = p_180621_0_.rand.nextInt(k > 0 ? k : chunk.getTopFilledSegment() + 16 - 1);
        return new BlockPos(i, l, j);
    }

    public static boolean func_185331_a(IBlockState p_185331_0_)
    {
        if (p_185331_0_.func_185898_k())
        {
            return false;
        }
        else if (p_185331_0_.canProvidePower())
        {
            return false;
        }
        else if (p_185331_0_.getMaterial().isLiquid())
        {
            return false;
        }
        else
        {
            return !BlockRailBase.func_176563_d(p_185331_0_);
        }
    }

    public static boolean func_180267_a(EntityLiving.SpawnPlacementType p_180267_0_, World p_180267_1_, BlockPos p_180267_2_)
    {
        if (!p_180267_1_.getWorldBorder().contains(p_180267_2_))
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = p_180267_1_.getBlockState(p_180267_2_);

            if (p_180267_0_ == EntityLiving.SpawnPlacementType.IN_WATER)
            {
                return iblockstate.getMaterial() == Material.WATER && p_180267_1_.getBlockState(p_180267_2_.down()).getMaterial() == Material.WATER && !p_180267_1_.getBlockState(p_180267_2_.up()).func_185915_l();
            }
            else
            {
                BlockPos blockpos = p_180267_2_.down();

                if (!p_180267_1_.getBlockState(blockpos).func_185896_q())
                {
                    return false;
                }
                else
                {
                    Block block = p_180267_1_.getBlockState(blockpos).getBlock();
                    boolean flag = block != Blocks.BEDROCK && block != Blocks.BARRIER;
                    return flag && func_185331_a(iblockstate) && func_185331_a(p_180267_1_.getBlockState(p_180267_2_.up()));
                }
            }
        }
    }

    /**
     * Called during chunk generation to spawn initial creatures.
     */
    public static void performWorldGenSpawning(World worldIn, Biome biomeIn, int centerX, int centerZ, int diameterX, int p_77191_5_, Random p_77191_6_)
    {
        List<Biome.SpawnListEntry> list = biomeIn.getSpawns(EnumCreatureType.CREATURE);

        if (!list.isEmpty())
        {
            while (p_77191_6_.nextFloat() < biomeIn.getSpawningChance())
            {
                Biome.SpawnListEntry biome$spawnlistentry = (Biome.SpawnListEntry)WeightedRandom.getRandomItem(worldIn.rand, list);
                int i = biome$spawnlistentry.minGroupCount + p_77191_6_.nextInt(1 + biome$spawnlistentry.maxGroupCount - biome$spawnlistentry.minGroupCount);
                IEntityLivingData ientitylivingdata = null;
                int j = centerX + p_77191_6_.nextInt(diameterX);
                int k = centerZ + p_77191_6_.nextInt(p_77191_5_);
                int l = j;
                int i1 = k;

                for (int j1 = 0; j1 < i; ++j1)
                {
                    boolean flag = false;

                    for (int k1 = 0; !flag && k1 < 4; ++k1)
                    {
                        BlockPos blockpos = worldIn.func_175672_r(new BlockPos(j, 0, k));

                        if (func_180267_a(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos))
                        {
                            EntityLiving entityliving;

                            try
                            {
                                entityliving = biome$spawnlistentry.field_76300_b.getConstructor(World.class).newInstance(worldIn);
                            }
                            catch (Exception exception)
                            {
                                exception.printStackTrace();
                                continue;
                            }

                            entityliving.setLocationAndAngles((double)((float)j + 0.5F), (double)blockpos.getY(), (double)((float)k + 0.5F), p_77191_6_.nextFloat() * 360.0F, 0.0F);
                            worldIn.addEntity0(entityliving);
                            ientitylivingdata = entityliving.func_180482_a(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                            flag = true;
                        }

                        j += p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5);

                        for (k += p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5); j < centerX || j >= centerX + diameterX || k < centerZ || k >= centerZ + diameterX; k = i1 + p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5))
                        {
                            j = l + p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5);
                        }
                    }
                }
            }
        }
    }
}

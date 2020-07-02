package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldGenDungeons extends WorldGenerator
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation[] SPAWNERTYPES = new ResourceLocation[] {EntityList.func_191306_a(EntitySkeleton.class), EntityList.func_191306_a(EntityZombie.class), EntityList.func_191306_a(EntityZombie.class), EntityList.func_191306_a(EntitySpider.class)};

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int i = 3;
        int j = p_180709_2_.nextInt(2) + 2;
        int k = -j - 1;
        int l = j + 1;
        int i1 = -1;
        int j1 = 4;
        int k1 = p_180709_2_.nextInt(2) + 2;
        int l1 = -k1 - 1;
        int i2 = k1 + 1;
        int j2 = 0;

        for (int k2 = k; k2 <= l; ++k2)
        {
            for (int l2 = -1; l2 <= 4; ++l2)
            {
                for (int i3 = l1; i3 <= i2; ++i3)
                {
                    BlockPos blockpos = p_180709_3_.add(k2, l2, i3);
                    Material material = p_180709_1_.getBlockState(blockpos).getMaterial();
                    boolean flag = material.isSolid();

                    if (l2 == -1 && !flag)
                    {
                        return false;
                    }

                    if (l2 == 4 && !flag)
                    {
                        return false;
                    }

                    if ((k2 == k || k2 == l || i3 == l1 || i3 == i2) && l2 == 0 && p_180709_1_.isAirBlock(blockpos) && p_180709_1_.isAirBlock(blockpos.up()))
                    {
                        ++j2;
                    }
                }
            }
        }

        if (j2 >= 1 && j2 <= 5)
        {
            for (int k3 = k; k3 <= l; ++k3)
            {
                for (int i4 = 3; i4 >= -1; --i4)
                {
                    for (int k4 = l1; k4 <= i2; ++k4)
                    {
                        BlockPos blockpos1 = p_180709_3_.add(k3, i4, k4);

                        if (k3 != k && i4 != -1 && k4 != l1 && k3 != l && i4 != 4 && k4 != i2)
                        {
                            if (p_180709_1_.getBlockState(blockpos1).getBlock() != Blocks.CHEST)
                            {
                                p_180709_1_.func_175698_g(blockpos1);
                            }
                        }
                        else if (blockpos1.getY() >= 0 && !p_180709_1_.getBlockState(blockpos1.down()).getMaterial().isSolid())
                        {
                            p_180709_1_.func_175698_g(blockpos1);
                        }
                        else if (p_180709_1_.getBlockState(blockpos1).getMaterial().isSolid() && p_180709_1_.getBlockState(blockpos1).getBlock() != Blocks.CHEST)
                        {
                            if (i4 == -1 && p_180709_2_.nextInt(4) != 0)
                            {
                                p_180709_1_.setBlockState(blockpos1, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 2);
                            }
                            else
                            {
                                p_180709_1_.setBlockState(blockpos1, Blocks.COBBLESTONE.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            for (int l3 = 0; l3 < 2; ++l3)
            {
                for (int j4 = 0; j4 < 3; ++j4)
                {
                    int l4 = p_180709_3_.getX() + p_180709_2_.nextInt(j * 2 + 1) - j;
                    int i5 = p_180709_3_.getY();
                    int j5 = p_180709_3_.getZ() + p_180709_2_.nextInt(k1 * 2 + 1) - k1;
                    BlockPos blockpos2 = new BlockPos(l4, i5, j5);

                    if (p_180709_1_.isAirBlock(blockpos2))
                    {
                        int j3 = 0;

                        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                        {
                            if (p_180709_1_.getBlockState(blockpos2.offset(enumfacing)).getMaterial().isSolid())
                            {
                                ++j3;
                            }
                        }

                        if (j3 == 1)
                        {
                            p_180709_1_.setBlockState(blockpos2, Blocks.CHEST.func_176458_f(p_180709_1_, blockpos2, Blocks.CHEST.getDefaultState()), 2);
                            TileEntity tileentity1 = p_180709_1_.getTileEntity(blockpos2);

                            if (tileentity1 instanceof TileEntityChest)
                            {
                                ((TileEntityChest)tileentity1).setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON, p_180709_2_.nextLong());
                            }

                            break;
                        }
                    }
                }
            }

            p_180709_1_.setBlockState(p_180709_3_, Blocks.SPAWNER.getDefaultState(), 2);
            TileEntity tileentity = p_180709_1_.getTileEntity(p_180709_3_);

            if (tileentity instanceof TileEntityMobSpawner)
            {
                ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().func_190894_a(this.func_76543_b(p_180709_2_));
            }
            else
            {
                LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", Integer.valueOf(p_180709_3_.getX()), Integer.valueOf(p_180709_3_.getY()), Integer.valueOf(p_180709_3_.getZ()));
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    private ResourceLocation func_76543_b(Random p_76543_1_)
    {
        return SPAWNERTYPES[p_76543_1_.nextInt(SPAWNERTYPES.length)];
    }
}

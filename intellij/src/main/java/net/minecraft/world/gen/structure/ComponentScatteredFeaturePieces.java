package net.minecraft.world.gen.structure;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class ComponentScatteredFeaturePieces
{
    public static void func_143045_a()
    {
        MapGenStructureIO.func_143031_a(ComponentScatteredFeaturePieces.DesertPyramid.class, "TeDP");
        MapGenStructureIO.func_143031_a(ComponentScatteredFeaturePieces.JunglePyramid.class, "TeJP");
        MapGenStructureIO.func_143031_a(ComponentScatteredFeaturePieces.SwampHut.class, "TeSH");
        MapGenStructureIO.func_143031_a(ComponentScatteredFeaturePieces.Igloo.class, "Iglu");
    }

    public static class DesertPyramid extends ComponentScatteredFeaturePieces.Feature
    {
        private final boolean[] field_74940_h = new boolean[4];

        public DesertPyramid()
        {
        }

        public DesertPyramid(Random p_i2062_1_, int p_i2062_2_, int p_i2062_3_)
        {
            super(p_i2062_1_, p_i2062_2_, 64, p_i2062_3_, 21, 15, 21);
        }

        protected void func_143012_a(NBTTagCompound p_143012_1_)
        {
            super.func_143012_a(p_143012_1_);
            p_143012_1_.putBoolean("hasPlacedChest0", this.field_74940_h[0]);
            p_143012_1_.putBoolean("hasPlacedChest1", this.field_74940_h[1]);
            p_143012_1_.putBoolean("hasPlacedChest2", this.field_74940_h[2]);
            p_143012_1_.putBoolean("hasPlacedChest3", this.field_74940_h[3]);
        }

        protected void readAdditional(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            super.readAdditional(tagCompound, p_143011_2_);
            this.field_74940_h[0] = tagCompound.getBoolean("hasPlacedChest0");
            this.field_74940_h[1] = tagCompound.getBoolean("hasPlacedChest1");
            this.field_74940_h[2] = tagCompound.getBoolean("hasPlacedChest2");
            this.field_74940_h[3] = tagCompound.getBoolean("hasPlacedChest3");
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, -4, 0, this.field_74939_a - 1, 0, this.field_74938_c - 1, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);

            for (int i = 1; i <= 9; ++i)
            {
                this.fillWithBlocks(p_74875_1_, p_74875_3_, i, i, i, this.field_74939_a - 1 - i, i, this.field_74938_c - 1 - i, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, i + 1, i, i + 1, this.field_74939_a - 2 - i, i, this.field_74938_c - 2 - i, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            }

            for (int i2 = 0; i2 < this.field_74939_a; ++i2)
            {
                for (int j = 0; j < this.field_74938_c; ++j)
                {
                    int k = -5;
                    this.replaceAirAndLiquidDownwards(p_74875_1_, Blocks.SANDSTONE.getDefaultState(), i2, -5, j, p_74875_3_);
                }
            }

            IBlockState iblockstate1 = Blocks.SANDSTONE_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.NORTH);
            IBlockState iblockstate2 = Blocks.SANDSTONE_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.SOUTH);
            IBlockState iblockstate3 = Blocks.SANDSTONE_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.EAST);
            IBlockState iblockstate = Blocks.SANDSTONE_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.WEST);
            int l = ~EnumDyeColor.ORANGE.func_176767_b() & 15;
            int i1 = ~EnumDyeColor.BLUE.func_176767_b() & 15;
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 4, 9, 4, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 10, 1, 3, 10, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            this.setBlockState(p_74875_1_, iblockstate1, 2, 10, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate2, 2, 10, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, 0, 10, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 4, 10, 2, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, this.field_74939_a - 5, 0, 0, this.field_74939_a - 1, 9, 4, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, this.field_74939_a - 4, 10, 1, this.field_74939_a - 2, 10, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            this.setBlockState(p_74875_1_, iblockstate1, this.field_74939_a - 3, 10, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate2, this.field_74939_a - 3, 10, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, this.field_74939_a - 5, 10, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, this.field_74939_a - 1, 10, 2, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, 0, 0, 12, 4, 4, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 9, 1, 0, 11, 3, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 9, 1, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 9, 2, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 9, 3, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 10, 3, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 11, 3, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 11, 2, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 11, 1, 1, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 4, 1, 1, 8, 3, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 4, 1, 2, 8, 2, 2, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 12, 1, 1, 16, 3, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 12, 1, 2, 16, 2, 2, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 5, 4, 5, this.field_74939_a - 6, 4, this.field_74938_c - 6, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 9, 4, 9, 11, 4, 11, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, 1, 8, 8, 3, 8, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 12, 1, 8, 12, 3, 8, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, 1, 12, 8, 3, 12, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 12, 1, 12, 12, 3, 12, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 5, 4, 4, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, this.field_74939_a - 5, 1, 5, this.field_74939_a - 2, 4, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 6, 7, 9, 6, 7, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, this.field_74939_a - 7, 7, 9, this.field_74939_a - 7, 7, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 5, 5, 9, 5, 7, 11, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, this.field_74939_a - 6, 5, 9, this.field_74939_a - 6, 7, 11, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 5, 5, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 5, 6, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 6, 6, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), this.field_74939_a - 6, 5, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), this.field_74939_a - 6, 6, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), this.field_74939_a - 7, 6, 10, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 2, 4, 4, 2, 6, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, this.field_74939_a - 3, 4, 4, this.field_74939_a - 3, 6, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(p_74875_1_, iblockstate1, 2, 4, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 2, 3, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, this.field_74939_a - 3, 4, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, this.field_74939_a - 3, 3, 4, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 3, 2, 2, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, this.field_74939_a - 3, 1, 3, this.field_74939_a - 2, 2, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.getDefaultState(), 1, 1, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.getDefaultState(), this.field_74939_a - 2, 1, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.STONE_SLAB.func_176203_a(BlockStoneSlab.EnumType.SAND.func_176624_a()), 1, 2, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.STONE_SLAB.func_176203_a(BlockStoneSlab.EnumType.SAND.func_176624_a()), this.field_74939_a - 2, 2, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 2, 1, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, this.field_74939_a - 3, 1, 2, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 4, 3, 5, 4, 3, 18, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, this.field_74939_a - 5, 3, 5, this.field_74939_a - 5, 3, 17, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 3, 1, 5, 4, 2, 16, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, this.field_74939_a - 6, 1, 5, this.field_74939_a - 5, 2, 16, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

            for (int j1 = 5; j1 <= 17; j1 += 2)
            {
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 4, 1, j1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), 4, 2, j1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), this.field_74939_a - 5, 1, j1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), this.field_74939_a - 5, 2, j1, p_74875_3_);
            }

            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), 10, 0, 7, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), 10, 0, 8, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), 9, 0, 9, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), 11, 0, 9, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), 8, 0, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), 12, 0, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), 7, 0, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), 13, 0, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), 9, 0, 11, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), 11, 0, 11, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), 10, 0, 12, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), 10, 0, 13, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(i1), 10, 0, 10, p_74875_3_);

            for (int j2 = 0; j2 <= this.field_74939_a - 1; j2 += this.field_74939_a - 1)
            {
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), j2, 2, 1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), j2, 2, 2, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), j2, 2, 3, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), j2, 3, 1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), j2, 3, 2, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), j2, 3, 3, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), j2, 4, 1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), j2, 4, 2, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), j2, 4, 3, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), j2, 5, 1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), j2, 5, 2, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), j2, 5, 3, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), j2, 6, 1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), j2, 6, 2, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), j2, 6, 3, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), j2, 7, 1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), j2, 7, 2, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), j2, 7, 3, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), j2, 8, 1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), j2, 8, 2, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), j2, 8, 3, p_74875_3_);
            }

            for (int k2 = 2; k2 <= this.field_74939_a - 3; k2 += this.field_74939_a - 3 - 2)
            {
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), k2 - 1, 2, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), k2, 2, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), k2 + 1, 2, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), k2 - 1, 3, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), k2, 3, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), k2 + 1, 3, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), k2 - 1, 4, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), k2, 4, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), k2 + 1, 4, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), k2 - 1, 5, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), k2, 5, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), k2 + 1, 5, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), k2 - 1, 6, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), k2, 6, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), k2 + 1, 6, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), k2 - 1, 7, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), k2, 7, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), k2 + 1, 7, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), k2 - 1, 8, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), k2, 8, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), k2 + 1, 8, 0, p_74875_3_);
            }

            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, 4, 0, 12, 6, 0, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 8, 6, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 12, 6, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), 9, 5, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), 10, 5, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150406_ce.func_176203_a(l), 11, 5, 0, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, -14, 8, 12, -11, 12, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, -10, 8, 12, -10, 12, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, -9, 8, 12, -9, 12, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, -8, 8, 12, -1, 12, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 9, -11, 9, 11, -1, 11, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(p_74875_1_, Blocks.STONE_PRESSURE_PLATE.getDefaultState(), 10, -11, 10, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 9, -13, 9, 11, -13, 11, Blocks.TNT.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 8, -11, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 8, -10, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), 7, -10, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 7, -11, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 12, -11, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 12, -10, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), 13, -10, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 13, -11, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 10, -11, 8, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 10, -10, 8, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), 10, -10, 7, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 10, -11, 7, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 10, -11, 12, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 10, -10, 12, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), 10, -10, 13, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 10, -11, 13, p_74875_3_);

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                if (!this.field_74940_h[enumfacing.getHorizontalIndex()])
                {
                    int k1 = enumfacing.getXOffset() * 2;
                    int l1 = enumfacing.getZOffset() * 2;
                    this.field_74940_h[enumfacing.getHorizontalIndex()] = this.generateChest(p_74875_1_, p_74875_3_, p_74875_2_, 10 + k1, -11, 10 + l1, LootTableList.CHESTS_DESERT_PYRAMID);
                }
            }

            return true;
        }
    }

    abstract static class Feature extends StructureComponent
    {
        protected int field_74939_a;
        protected int field_74937_b;
        protected int field_74938_c;
        protected int field_74936_d = -1;

        public Feature()
        {
        }

        protected Feature(Random p_i2065_1_, int p_i2065_2_, int p_i2065_3_, int p_i2065_4_, int p_i2065_5_, int p_i2065_6_, int p_i2065_7_)
        {
            super(0);
            this.field_74939_a = p_i2065_5_;
            this.field_74937_b = p_i2065_6_;
            this.field_74938_c = p_i2065_7_;
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(p_i2065_1_));

            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z)
            {
                this.boundingBox = new StructureBoundingBox(p_i2065_2_, p_i2065_3_, p_i2065_4_, p_i2065_2_ + p_i2065_5_ - 1, p_i2065_3_ + p_i2065_6_ - 1, p_i2065_4_ + p_i2065_7_ - 1);
            }
            else
            {
                this.boundingBox = new StructureBoundingBox(p_i2065_2_, p_i2065_3_, p_i2065_4_, p_i2065_2_ + p_i2065_7_ - 1, p_i2065_3_ + p_i2065_6_ - 1, p_i2065_4_ + p_i2065_5_ - 1);
            }
        }

        protected void func_143012_a(NBTTagCompound p_143012_1_)
        {
            p_143012_1_.putInt("Width", this.field_74939_a);
            p_143012_1_.putInt("Height", this.field_74937_b);
            p_143012_1_.putInt("Depth", this.field_74938_c);
            p_143012_1_.putInt("HPos", this.field_74936_d);
        }

        protected void readAdditional(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            this.field_74939_a = tagCompound.getInt("Width");
            this.field_74937_b = tagCompound.getInt("Height");
            this.field_74938_c = tagCompound.getInt("Depth");
            this.field_74936_d = tagCompound.getInt("HPos");
        }

        protected boolean func_74935_a(World p_74935_1_, StructureBoundingBox p_74935_2_, int p_74935_3_)
        {
            if (this.field_74936_d >= 0)
            {
                return true;
            }
            else
            {
                int i = 0;
                int j = 0;
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k)
                {
                    for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l)
                    {
                        blockpos$mutableblockpos.setPos(l, 64, k);

                        if (p_74935_2_.isVecInside(blockpos$mutableblockpos))
                        {
                            i += Math.max(p_74935_1_.func_175672_r(blockpos$mutableblockpos).getY(), p_74935_1_.dimension.func_76557_i());
                            ++j;
                        }
                    }
                }

                if (j == 0)
                {
                    return false;
                }
                else
                {
                    this.field_74936_d = i / j;
                    this.boundingBox.offset(0, this.field_74936_d - this.boundingBox.minY + p_74935_3_, 0);
                    return true;
                }
            }
        }
    }

    public static class Igloo extends ComponentScatteredFeaturePieces.Feature
    {
        private static final ResourceLocation field_186170_e = new ResourceLocation("igloo/igloo_top");
        private static final ResourceLocation field_186171_f = new ResourceLocation("igloo/igloo_middle");
        private static final ResourceLocation field_186172_g = new ResourceLocation("igloo/igloo_bottom");

        public Igloo()
        {
        }

        public Igloo(Random p_i47036_1_, int p_i47036_2_, int p_i47036_3_)
        {
            super(p_i47036_1_, p_i47036_2_, 64, p_i47036_3_, 7, 5, 8);
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (!this.func_74935_a(p_74875_1_, p_74875_3_, -1))
            {
                return false;
            }
            else
            {
                StructureBoundingBox structureboundingbox = this.getBoundingBox();
                BlockPos blockpos = new BlockPos(structureboundingbox.minX, structureboundingbox.minY, structureboundingbox.minZ);
                Rotation[] arotation = Rotation.values();
                MinecraftServer minecraftserver = p_74875_1_.getServer();
                TemplateManager templatemanager = p_74875_1_.func_72860_G().getStructureTemplateManager();
                PlacementSettings placementsettings = (new PlacementSettings()).setRotation(arotation[p_74875_2_.nextInt(arotation.length)]).func_186225_a(Blocks.STRUCTURE_VOID).setBoundingBox(structureboundingbox);
                Template template = templatemanager.func_186237_a(minecraftserver, field_186170_e);
                template.addBlocksToWorldChunk(p_74875_1_, blockpos, placementsettings);

                if (p_74875_2_.nextDouble() < 0.5D)
                {
                    Template template1 = templatemanager.func_186237_a(minecraftserver, field_186171_f);
                    Template template2 = templatemanager.func_186237_a(minecraftserver, field_186172_g);
                    int i = p_74875_2_.nextInt(8) + 4;

                    for (int j = 0; j < i; ++j)
                    {
                        BlockPos blockpos1 = template.calculateConnectedPos(placementsettings, new BlockPos(3, -1 - j * 3, 5), placementsettings, new BlockPos(1, 2, 1));
                        template1.addBlocksToWorldChunk(p_74875_1_, blockpos.add(blockpos1), placementsettings);
                    }

                    BlockPos blockpos4 = blockpos.add(template.calculateConnectedPos(placementsettings, new BlockPos(3, -1 - i * 3, 5), placementsettings, new BlockPos(3, 5, 7)));
                    template2.addBlocksToWorldChunk(p_74875_1_, blockpos4, placementsettings);
                    Map<BlockPos, String> map = template2.func_186258_a(blockpos4, placementsettings);

                    for (Entry<BlockPos, String> entry : map.entrySet())
                    {
                        if ("chest".equals(entry.getValue()))
                        {
                            BlockPos blockpos2 = entry.getKey();
                            p_74875_1_.setBlockState(blockpos2, Blocks.AIR.getDefaultState(), 3);
                            TileEntity tileentity = p_74875_1_.getTileEntity(blockpos2.down());

                            if (tileentity instanceof TileEntityChest)
                            {
                                ((TileEntityChest)tileentity).setLootTable(LootTableList.CHESTS_IGLOO_CHEST, p_74875_2_.nextLong());
                            }
                        }
                    }
                }
                else
                {
                    BlockPos blockpos3 = Template.transformedBlockPos(placementsettings, new BlockPos(3, 0, 5));
                    p_74875_1_.setBlockState(blockpos.add(blockpos3), Blocks.SNOW.getDefaultState(), 3);
                }

                return true;
            }
        }
    }

    public static class JunglePyramid extends ComponentScatteredFeaturePieces.Feature
    {
        private boolean field_74947_h;
        private boolean field_74948_i;
        private boolean field_74945_j;
        private boolean field_74946_k;
        private static final ComponentScatteredFeaturePieces.JunglePyramid.Stones field_74942_n = new ComponentScatteredFeaturePieces.JunglePyramid.Stones();

        public JunglePyramid()
        {
        }

        public JunglePyramid(Random p_i2064_1_, int p_i2064_2_, int p_i2064_3_)
        {
            super(p_i2064_1_, p_i2064_2_, 64, p_i2064_3_, 12, 10, 15);
        }

        protected void func_143012_a(NBTTagCompound p_143012_1_)
        {
            super.func_143012_a(p_143012_1_);
            p_143012_1_.putBoolean("placedMainChest", this.field_74947_h);
            p_143012_1_.putBoolean("placedHiddenChest", this.field_74948_i);
            p_143012_1_.putBoolean("placedTrap1", this.field_74945_j);
            p_143012_1_.putBoolean("placedTrap2", this.field_74946_k);
        }

        protected void readAdditional(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            super.readAdditional(tagCompound, p_143011_2_);
            this.field_74947_h = tagCompound.getBoolean("placedMainChest");
            this.field_74948_i = tagCompound.getBoolean("placedHiddenChest");
            this.field_74945_j = tagCompound.getBoolean("placedTrap1");
            this.field_74946_k = tagCompound.getBoolean("placedTrap2");
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (!this.func_74935_a(p_74875_1_, p_74875_3_, 0))
            {
                return false;
            }
            else
            {
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 0, -4, 0, this.field_74939_a - 1, 0, this.field_74938_c - 1, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 1, 2, 9, 2, 2, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 1, 12, 9, 2, 12, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 1, 3, 2, 2, 11, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 9, 1, 3, 9, 2, 11, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, 3, 1, 10, 6, 1, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, 3, 13, 10, 6, 13, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, 3, 2, 1, 6, 12, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 10, 3, 2, 10, 6, 12, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 3, 2, 9, 3, 12, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 6, 2, 9, 6, 12, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 3, 7, 3, 8, 7, 11, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 8, 4, 7, 8, 10, false, p_74875_2_, field_74942_n);
                this.fillWithAir(p_74875_1_, p_74875_3_, 3, 1, 3, 8, 2, 11);
                this.fillWithAir(p_74875_1_, p_74875_3_, 4, 3, 6, 7, 3, 9);
                this.fillWithAir(p_74875_1_, p_74875_3_, 2, 4, 2, 9, 5, 12);
                this.fillWithAir(p_74875_1_, p_74875_3_, 4, 6, 5, 7, 6, 9);
                this.fillWithAir(p_74875_1_, p_74875_3_, 5, 7, 6, 6, 7, 8);
                this.fillWithAir(p_74875_1_, p_74875_3_, 5, 1, 2, 6, 2, 2);
                this.fillWithAir(p_74875_1_, p_74875_3_, 5, 2, 12, 6, 2, 12);
                this.fillWithAir(p_74875_1_, p_74875_3_, 5, 5, 1, 6, 5, 1);
                this.fillWithAir(p_74875_1_, p_74875_3_, 5, 5, 13, 6, 5, 13);
                this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 1, 5, 5, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 10, 5, 5, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 1, 5, 9, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 10, 5, 9, p_74875_3_);

                for (int i = 0; i <= 14; i += 14)
                {
                    this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 4, i, 2, 5, i, false, p_74875_2_, field_74942_n);
                    this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 4, i, 4, 5, i, false, p_74875_2_, field_74942_n);
                    this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 7, 4, i, 7, 5, i, false, p_74875_2_, field_74942_n);
                    this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 9, 4, i, 9, 5, i, false, p_74875_2_, field_74942_n);
                }

                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 5, 6, 0, 6, 6, 0, false, p_74875_2_, field_74942_n);

                for (int l = 0; l <= 11; l += 11)
                {
                    for (int j = 2; j <= 12; j += 2)
                    {
                        this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, l, 4, j, l, 5, j, false, p_74875_2_, field_74942_n);
                    }

                    this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, l, 6, 5, l, 6, 5, false, p_74875_2_, field_74942_n);
                    this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, l, 6, 9, l, 6, 9, false, p_74875_2_, field_74942_n);
                }

                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 7, 2, 2, 9, 2, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 9, 7, 2, 9, 9, 2, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, 7, 12, 2, 9, 12, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 9, 7, 12, 9, 9, 12, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 9, 4, 4, 9, 4, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 7, 9, 4, 7, 9, 4, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 9, 10, 4, 9, 10, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 7, 9, 10, 7, 9, 10, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 5, 9, 7, 6, 9, 7, false, p_74875_2_, field_74942_n);
                IBlockState iblockstate2 = Blocks.field_150446_ar.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.EAST);
                IBlockState iblockstate3 = Blocks.field_150446_ar.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.WEST);
                IBlockState iblockstate = Blocks.field_150446_ar.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.SOUTH);
                IBlockState iblockstate1 = Blocks.field_150446_ar.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.NORTH);
                this.setBlockState(p_74875_1_, iblockstate1, 5, 9, 6, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate1, 6, 9, 6, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate, 5, 9, 8, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate, 6, 9, 8, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate1, 4, 0, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate1, 5, 0, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate1, 6, 0, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate1, 7, 0, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate1, 4, 1, 8, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate1, 4, 2, 9, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate1, 4, 3, 10, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate1, 7, 1, 8, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate1, 7, 2, 9, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate1, 7, 3, 10, p_74875_3_);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 1, 9, 4, 1, 9, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 7, 1, 9, 7, 1, 9, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 4, 1, 10, 7, 2, 10, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 5, 4, 5, 6, 4, 5, false, p_74875_2_, field_74942_n);
                this.setBlockState(p_74875_1_, iblockstate2, 4, 4, 5, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate3, 7, 4, 5, p_74875_3_);

                for (int k = 0; k < 4; ++k)
                {
                    this.setBlockState(p_74875_1_, iblockstate, 5, 0 - k, 6 + k, p_74875_3_);
                    this.setBlockState(p_74875_1_, iblockstate, 6, 0 - k, 6 + k, p_74875_3_);
                    this.fillWithAir(p_74875_1_, p_74875_3_, 5, 0 - k, 7 + k, 6, 0 - k, 9 + k);
                }

                this.fillWithAir(p_74875_1_, p_74875_3_, 1, -3, 12, 10, -1, 13);
                this.fillWithAir(p_74875_1_, p_74875_3_, 1, -3, 1, 3, -1, 13);
                this.fillWithAir(p_74875_1_, p_74875_3_, 1, -3, 1, 9, -1, 5);

                for (int i1 = 1; i1 <= 13; i1 += 2)
                {
                    this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, -3, i1, 1, -2, i1, false, p_74875_2_, field_74942_n);
                }

                for (int j1 = 2; j1 <= 12; j1 += 2)
                {
                    this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 1, -1, j1, 3, -1, j1, false, p_74875_2_, field_74942_n);
                }

                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 2, -2, 1, 5, -2, 1, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 7, -2, 1, 9, -2, 1, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 6, -3, 1, 6, -3, 1, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 6, -1, 1, 6, -1, 1, false, p_74875_2_, field_74942_n);
                this.setBlockState(p_74875_1_, Blocks.TRIPWIRE_HOOK.getDefaultState().func_177226_a(BlockTripWireHook.FACING, EnumFacing.EAST).func_177226_a(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 1, -3, 8, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.TRIPWIRE_HOOK.getDefaultState().func_177226_a(BlockTripWireHook.FACING, EnumFacing.WEST).func_177226_a(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 4, -3, 8, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.TRIPWIRE.getDefaultState().func_177226_a(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 2, -3, 8, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.TRIPWIRE.getDefaultState().func_177226_a(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 3, -3, 8, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 7, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 6, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 5, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 4, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 3, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 2, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 5, -3, 1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 4, -3, 1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 3, -3, 1, p_74875_3_);

                if (!this.field_74945_j)
                {
                    this.field_74945_j = this.createDispenser(p_74875_1_, p_74875_3_, p_74875_2_, 3, -2, 1, EnumFacing.NORTH, LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER);
                }

                this.setBlockState(p_74875_1_, Blocks.VINE.getDefaultState().func_177226_a(BlockVine.SOUTH, Boolean.valueOf(true)), 3, -2, 2, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.TRIPWIRE_HOOK.getDefaultState().func_177226_a(BlockTripWireHook.FACING, EnumFacing.NORTH).func_177226_a(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 7, -3, 1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.TRIPWIRE_HOOK.getDefaultState().func_177226_a(BlockTripWireHook.FACING, EnumFacing.SOUTH).func_177226_a(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 7, -3, 5, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.TRIPWIRE.getDefaultState().func_177226_a(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 7, -3, 2, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.TRIPWIRE.getDefaultState().func_177226_a(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 7, -3, 3, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.TRIPWIRE.getDefaultState().func_177226_a(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 7, -3, 4, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 8, -3, 6, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 9, -3, 6, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 9, -3, 5, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 9, -3, 4, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 9, -2, 4, p_74875_3_);

                if (!this.field_74946_k)
                {
                    this.field_74946_k = this.createDispenser(p_74875_1_, p_74875_3_, p_74875_2_, 9, -2, 3, EnumFacing.WEST, LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER);
                }

                this.setBlockState(p_74875_1_, Blocks.VINE.getDefaultState().func_177226_a(BlockVine.EAST, Boolean.valueOf(true)), 8, -1, 3, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.VINE.getDefaultState().func_177226_a(BlockVine.EAST, Boolean.valueOf(true)), 8, -2, 3, p_74875_3_);

                if (!this.field_74947_h)
                {
                    this.field_74947_h = this.generateChest(p_74875_1_, p_74875_3_, p_74875_2_, 8, -3, 3, LootTableList.CHESTS_JUNGLE_TEMPLE);
                }

                this.setBlockState(p_74875_1_, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 9, -3, 2, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 8, -3, 1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 4, -3, 5, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 5, -2, 5, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 5, -1, 5, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 6, -3, 5, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 7, -2, 5, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 7, -1, 5, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 8, -3, 5, p_74875_3_);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 9, -1, 1, 9, -1, 5, false, p_74875_2_, field_74942_n);
                this.fillWithAir(p_74875_1_, p_74875_3_, 8, -3, 8, 10, -1, 10);
                this.setBlockState(p_74875_1_, Blocks.field_150417_aV.func_176203_a(BlockStoneBrick.field_176252_O), 8, -2, 11, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150417_aV.func_176203_a(BlockStoneBrick.field_176252_O), 9, -2, 11, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150417_aV.func_176203_a(BlockStoneBrick.field_176252_O), 10, -2, 11, p_74875_3_);
                IBlockState iblockstate4 = Blocks.LEVER.getDefaultState().func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.NORTH);
                this.setBlockState(p_74875_1_, iblockstate4, 8, -2, 12, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 9, -2, 12, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 10, -2, 12, p_74875_3_);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 8, -3, 8, 8, -3, 10, false, p_74875_2_, field_74942_n);
                this.fillWithRandomizedBlocks(p_74875_1_, p_74875_3_, 10, -3, 8, 10, -3, 10, false, p_74875_2_, field_74942_n);
                this.setBlockState(p_74875_1_, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 10, -2, 9, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 8, -2, 9, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 8, -2, 10, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.REDSTONE_WIRE.getDefaultState(), 10, -1, 9, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.STICKY_PISTON.getDefaultState().func_177226_a(BlockPistonBase.FACING, EnumFacing.UP), 9, -2, 8, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.STICKY_PISTON.getDefaultState().func_177226_a(BlockPistonBase.FACING, EnumFacing.WEST), 10, -2, 8, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.STICKY_PISTON.getDefaultState().func_177226_a(BlockPistonBase.FACING, EnumFacing.WEST), 10, -1, 8, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150413_aR.getDefaultState().func_177226_a(BlockRedstoneRepeater.HORIZONTAL_FACING, EnumFacing.NORTH), 10, -2, 10, p_74875_3_);

                if (!this.field_74948_i)
                {
                    this.field_74948_i = this.generateChest(p_74875_1_, p_74875_3_, p_74875_2_, 9, -3, 10, LootTableList.CHESTS_JUNGLE_TEMPLE);
                }

                return true;
            }
        }

        static class Stones extends StructureComponent.BlockSelector
        {
            private Stones()
            {
            }

            public void selectBlocks(Random rand, int x, int y, int z, boolean wall)
            {
                if (rand.nextFloat() < 0.4F)
                {
                    this.blockstate = Blocks.COBBLESTONE.getDefaultState();
                }
                else
                {
                    this.blockstate = Blocks.MOSSY_COBBLESTONE.getDefaultState();
                }
            }
        }
    }

    public static class SwampHut extends ComponentScatteredFeaturePieces.Feature
    {
        private boolean field_82682_h;

        public SwampHut()
        {
        }

        public SwampHut(Random p_i2066_1_, int p_i2066_2_, int p_i2066_3_)
        {
            super(p_i2066_1_, p_i2066_2_, 64, p_i2066_3_, 7, 7, 9);
        }

        protected void func_143012_a(NBTTagCompound p_143012_1_)
        {
            super.func_143012_a(p_143012_1_);
            p_143012_1_.putBoolean("Witch", this.field_82682_h);
        }

        protected void readAdditional(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            super.readAdditional(tagCompound, p_143011_2_);
            this.field_82682_h = tagCompound.getBoolean("Witch");
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (!this.func_74935_a(p_74875_1_, p_74875_3_, 0))
            {
                return false;
            }
            else
            {
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 1, 5, 1, 7, Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), false);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 4, 2, 5, 4, 7, Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), false);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 2, 1, 0, 4, 1, 0, Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), false);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 2, 2, 2, 3, 3, 2, Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), false);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 2, 3, 1, 3, 6, Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), false);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 5, 2, 3, 5, 3, 6, Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), false);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 2, 2, 7, 4, 3, 7, Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), false);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 2, 1, 3, 2, Blocks.field_150364_r.getDefaultState(), Blocks.field_150364_r.getDefaultState(), false);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 5, 0, 2, 5, 3, 2, Blocks.field_150364_r.getDefaultState(), Blocks.field_150364_r.getDefaultState(), false);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 7, 1, 3, 7, Blocks.field_150364_r.getDefaultState(), Blocks.field_150364_r.getDefaultState(), false);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 5, 0, 7, 5, 3, 7, Blocks.field_150364_r.getDefaultState(), Blocks.field_150364_r.getDefaultState(), false);
                this.setBlockState(p_74875_1_, Blocks.OAK_FENCE.getDefaultState(), 2, 3, 2, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.OAK_FENCE.getDefaultState(), 3, 3, 7, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 1, 3, 4, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 5, 3, 4, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 5, 3, 5, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.FLOWER_POT.getDefaultState().func_177226_a(BlockFlowerPot.field_176443_b, BlockFlowerPot.EnumFlowerType.MUSHROOM_RED), 1, 3, 5, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.CRAFTING_TABLE.getDefaultState(), 3, 2, 6, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.CAULDRON.getDefaultState(), 4, 2, 6, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.OAK_FENCE.getDefaultState(), 1, 2, 1, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.OAK_FENCE.getDefaultState(), 5, 2, 1, p_74875_3_);
                IBlockState iblockstate = Blocks.SPRUCE_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.NORTH);
                IBlockState iblockstate1 = Blocks.SPRUCE_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.EAST);
                IBlockState iblockstate2 = Blocks.SPRUCE_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.WEST);
                IBlockState iblockstate3 = Blocks.SPRUCE_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.SOUTH);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 4, 1, 6, 4, 1, iblockstate, iblockstate, false);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 4, 2, 0, 4, 7, iblockstate1, iblockstate1, false);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 6, 4, 2, 6, 4, 7, iblockstate2, iblockstate2, false);
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 4, 8, 6, 4, 8, iblockstate3, iblockstate3, false);

                for (int i = 2; i <= 7; i += 5)
                {
                    for (int j = 1; j <= 5; j += 4)
                    {
                        this.replaceAirAndLiquidDownwards(p_74875_1_, Blocks.field_150364_r.getDefaultState(), j, -1, i, p_74875_3_);
                    }
                }

                if (!this.field_82682_h)
                {
                    int l = this.getXWithOffset(2, 5);
                    int i1 = this.getYWithOffset(2);
                    int k = this.getZWithOffset(2, 5);

                    if (p_74875_3_.isVecInside(new BlockPos(l, i1, k)))
                    {
                        this.field_82682_h = true;
                        EntityWitch entitywitch = new EntityWitch(p_74875_1_);
                        entitywitch.enablePersistence();
                        entitywitch.setLocationAndAngles((double)l + 0.5D, (double)i1, (double)k + 0.5D, 0.0F, 0.0F);
                        entitywitch.func_180482_a(p_74875_1_.getDifficultyForLocation(new BlockPos(l, i1, k)), (IEntityLivingData)null);
                        p_74875_1_.addEntity0(entitywitch);
                    }
                }

                return true;
            }
        }
    }
}

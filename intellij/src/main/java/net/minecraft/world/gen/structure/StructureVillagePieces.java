package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeSavanna;
import net.minecraft.world.biome.BiomeTaiga;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureVillagePieces
{
    public static void func_143016_a()
    {
        MapGenStructureIO.func_143031_a(StructureVillagePieces.House1.class, "ViBH");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Field1.class, "ViDF");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Field2.class, "ViF");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Torch.class, "ViL");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Hall.class, "ViPH");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.House4Garden.class, "ViSH");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.WoodHut.class, "ViSmH");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Church.class, "ViST");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.House2.class, "ViS");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Start.class, "ViStart");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Path.class, "ViSR");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.House3.class, "ViTRH");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Well.class, "ViW");
    }

    public static List<StructureVillagePieces.PieceWeight> func_75084_a(Random p_75084_0_, int p_75084_1_)
    {
        List<StructureVillagePieces.PieceWeight> list = Lists.<StructureVillagePieces.PieceWeight>newArrayList();
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House4Garden.class, 4, MathHelper.nextInt(p_75084_0_, 2 + p_75084_1_, 4 + p_75084_1_ * 2)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Church.class, 20, MathHelper.nextInt(p_75084_0_, 0 + p_75084_1_, 1 + p_75084_1_)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House1.class, 20, MathHelper.nextInt(p_75084_0_, 0 + p_75084_1_, 2 + p_75084_1_)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.WoodHut.class, 3, MathHelper.nextInt(p_75084_0_, 2 + p_75084_1_, 5 + p_75084_1_ * 3)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Hall.class, 15, MathHelper.nextInt(p_75084_0_, 0 + p_75084_1_, 2 + p_75084_1_)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field1.class, 3, MathHelper.nextInt(p_75084_0_, 1 + p_75084_1_, 4 + p_75084_1_)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field2.class, 3, MathHelper.nextInt(p_75084_0_, 2 + p_75084_1_, 4 + p_75084_1_ * 2)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House2.class, 15, MathHelper.nextInt(p_75084_0_, 0, 1 + p_75084_1_)));
        list.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House3.class, 8, MathHelper.nextInt(p_75084_0_, 0 + p_75084_1_, 3 + p_75084_1_ * 2)));
        Iterator<StructureVillagePieces.PieceWeight> iterator = list.iterator();

        while (iterator.hasNext())
        {
            if ((iterator.next()).field_75087_d == 0)
            {
                iterator.remove();
            }
        }

        return list;
    }

    private static int func_75079_a(List<StructureVillagePieces.PieceWeight> p_75079_0_)
    {
        boolean flag = false;
        int i = 0;

        for (StructureVillagePieces.PieceWeight structurevillagepieces$pieceweight : p_75079_0_)
        {
            if (structurevillagepieces$pieceweight.field_75087_d > 0 && structurevillagepieces$pieceweight.field_75089_c < structurevillagepieces$pieceweight.field_75087_d)
            {
                flag = true;
            }

            i += structurevillagepieces$pieceweight.field_75088_b;
        }

        return flag ? i : -1;
    }

    private static StructureVillagePieces.Village func_176065_a(StructureVillagePieces.Start p_176065_0_, StructureVillagePieces.PieceWeight p_176065_1_, List<StructureComponent> p_176065_2_, Random p_176065_3_, int p_176065_4_, int p_176065_5_, int p_176065_6_, EnumFacing p_176065_7_, int p_176065_8_)
    {
        Class <? extends StructureVillagePieces.Village > oclass = p_176065_1_.field_75090_a;
        StructureVillagePieces.Village structurevillagepieces$village = null;

        if (oclass == StructureVillagePieces.House4Garden.class)
        {
            structurevillagepieces$village = StructureVillagePieces.House4Garden.func_175858_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (oclass == StructureVillagePieces.Church.class)
        {
            structurevillagepieces$village = StructureVillagePieces.Church.func_175854_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (oclass == StructureVillagePieces.House1.class)
        {
            structurevillagepieces$village = StructureVillagePieces.House1.func_175850_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (oclass == StructureVillagePieces.WoodHut.class)
        {
            structurevillagepieces$village = StructureVillagePieces.WoodHut.func_175853_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (oclass == StructureVillagePieces.Hall.class)
        {
            structurevillagepieces$village = StructureVillagePieces.Hall.func_175857_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (oclass == StructureVillagePieces.Field1.class)
        {
            structurevillagepieces$village = StructureVillagePieces.Field1.func_175851_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (oclass == StructureVillagePieces.Field2.class)
        {
            structurevillagepieces$village = StructureVillagePieces.Field2.func_175852_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (oclass == StructureVillagePieces.House2.class)
        {
            structurevillagepieces$village = StructureVillagePieces.House2.func_175855_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }
        else if (oclass == StructureVillagePieces.House3.class)
        {
            structurevillagepieces$village = StructureVillagePieces.House3.func_175849_a(p_176065_0_, p_176065_2_, p_176065_3_, p_176065_4_, p_176065_5_, p_176065_6_, p_176065_7_, p_176065_8_);
        }

        return structurevillagepieces$village;
    }

    private static StructureVillagePieces.Village func_176067_c(StructureVillagePieces.Start p_176067_0_, List<StructureComponent> p_176067_1_, Random p_176067_2_, int p_176067_3_, int p_176067_4_, int p_176067_5_, EnumFacing p_176067_6_, int p_176067_7_)
    {
        int i = func_75079_a(p_176067_0_.field_74931_h);

        if (i <= 0)
        {
            return null;
        }
        else
        {
            int j = 0;

            while (j < 5)
            {
                ++j;
                int k = p_176067_2_.nextInt(i);

                for (StructureVillagePieces.PieceWeight structurevillagepieces$pieceweight : p_176067_0_.field_74931_h)
                {
                    k -= structurevillagepieces$pieceweight.field_75088_b;

                    if (k < 0)
                    {
                        if (!structurevillagepieces$pieceweight.func_75085_a(p_176067_7_) || structurevillagepieces$pieceweight == p_176067_0_.field_74926_d && p_176067_0_.field_74931_h.size() > 1)
                        {
                            break;
                        }

                        StructureVillagePieces.Village structurevillagepieces$village = func_176065_a(p_176067_0_, structurevillagepieces$pieceweight, p_176067_1_, p_176067_2_, p_176067_3_, p_176067_4_, p_176067_5_, p_176067_6_, p_176067_7_);

                        if (structurevillagepieces$village != null)
                        {
                            ++structurevillagepieces$pieceweight.field_75089_c;
                            p_176067_0_.field_74926_d = structurevillagepieces$pieceweight;

                            if (!structurevillagepieces$pieceweight.func_75086_a())
                            {
                                p_176067_0_.field_74931_h.remove(structurevillagepieces$pieceweight);
                            }

                            return structurevillagepieces$village;
                        }
                    }
                }
            }

            StructureBoundingBox structureboundingbox = StructureVillagePieces.Torch.func_175856_a(p_176067_0_, p_176067_1_, p_176067_2_, p_176067_3_, p_176067_4_, p_176067_5_, p_176067_6_);

            if (structureboundingbox != null)
            {
                return new StructureVillagePieces.Torch(p_176067_0_, p_176067_7_, p_176067_2_, structureboundingbox, p_176067_6_);
            }
            else
            {
                return null;
            }
        }
    }

    private static StructureComponent func_176066_d(StructureVillagePieces.Start p_176066_0_, List<StructureComponent> p_176066_1_, Random p_176066_2_, int p_176066_3_, int p_176066_4_, int p_176066_5_, EnumFacing p_176066_6_, int p_176066_7_)
    {
        if (p_176066_7_ > 50)
        {
            return null;
        }
        else if (Math.abs(p_176066_3_ - p_176066_0_.getBoundingBox().minX) <= 112 && Math.abs(p_176066_5_ - p_176066_0_.getBoundingBox().minZ) <= 112)
        {
            StructureComponent structurecomponent = func_176067_c(p_176066_0_, p_176066_1_, p_176066_2_, p_176066_3_, p_176066_4_, p_176066_5_, p_176066_6_, p_176066_7_ + 1);

            if (structurecomponent != null)
            {
                p_176066_1_.add(structurecomponent);
                p_176066_0_.field_74932_i.add(structurecomponent);
                return structurecomponent;
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

    private static StructureComponent func_176069_e(StructureVillagePieces.Start p_176069_0_, List<StructureComponent> p_176069_1_, Random p_176069_2_, int p_176069_3_, int p_176069_4_, int p_176069_5_, EnumFacing p_176069_6_, int p_176069_7_)
    {
        if (p_176069_7_ > 3 + p_176069_0_.field_74928_c)
        {
            return null;
        }
        else if (Math.abs(p_176069_3_ - p_176069_0_.getBoundingBox().minX) <= 112 && Math.abs(p_176069_5_ - p_176069_0_.getBoundingBox().minZ) <= 112)
        {
            StructureBoundingBox structureboundingbox = StructureVillagePieces.Path.func_175848_a(p_176069_0_, p_176069_1_, p_176069_2_, p_176069_3_, p_176069_4_, p_176069_5_, p_176069_6_);

            if (structureboundingbox != null && structureboundingbox.minY > 10)
            {
                StructureComponent structurecomponent = new StructureVillagePieces.Path(p_176069_0_, p_176069_7_, p_176069_2_, structureboundingbox, p_176069_6_);
                p_176069_1_.add(structurecomponent);
                p_176069_0_.field_74930_j.add(structurecomponent);
                return structurecomponent;
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

    public static class Church extends StructureVillagePieces.Village
    {
        public Church()
        {
        }

        public Church(StructureVillagePieces.Start p_i45564_1_, int p_i45564_2_, Random p_i45564_3_, StructureBoundingBox p_i45564_4_, EnumFacing p_i45564_5_)
        {
            super(p_i45564_1_, p_i45564_2_);
            this.setCoordBaseMode(p_i45564_5_);
            this.boundingBox = p_i45564_4_;
        }

        public static StructureVillagePieces.Church func_175854_a(StructureVillagePieces.Start p_175854_0_, List<StructureComponent> p_175854_1_, Random p_175854_2_, int p_175854_3_, int p_175854_4_, int p_175854_5_, EnumFacing p_175854_6_, int p_175854_7_)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175854_3_, p_175854_4_, p_175854_5_, 0, 0, 0, 5, 12, 9, p_175854_6_);
            return func_74895_a(structureboundingbox) && StructureComponent.findIntersecting(p_175854_1_, structureboundingbox) == null ? new StructureVillagePieces.Church(p_175854_0_, p_175854_7_, p_175854_2_, structureboundingbox, p_175854_6_) : null;
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_143015_k < 0)
            {
                this.field_143015_k = this.func_74889_b(p_74875_1_, p_74875_3_);

                if (this.field_143015_k < 0)
                {
                    return true;
                }

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 12 - 1, 0);
            }

            IBlockState iblockstate = Blocks.COBBLESTONE.getDefaultState();
            IBlockState iblockstate1 = this.func_175847_a(Blocks.field_150446_ar.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate2 = this.func_175847_a(Blocks.field_150446_ar.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.WEST));
            IBlockState iblockstate3 = this.func_175847_a(Blocks.field_150446_ar.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.EAST));
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 1, 3, 3, 7, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 5, 1, 3, 9, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 0, 3, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 0, 3, 10, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 1, 1, 0, 10, 3, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 4, 1, 1, 4, 10, 3, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 4, 0, 4, 7, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 4, 0, 4, 4, 4, 7, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 8, 3, 4, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 5, 4, 3, 10, 4, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 5, 5, 3, 5, 7, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 9, 0, 4, 9, 4, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 4, 0, 4, 4, 4, iblockstate, iblockstate, false);
            this.setBlockState(p_74875_1_, iblockstate, 0, 11, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 4, 11, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 2, 11, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 2, 11, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 1, 1, 6, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 1, 1, 7, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 2, 1, 7, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 3, 1, 6, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 3, 1, 7, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 1, 1, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 2, 1, 6, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 3, 1, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate2, 1, 2, 7, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, 3, 2, 7, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 4, 3, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 6, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 7, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 4, 6, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 4, 7, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 2, 6, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 2, 7, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 2, 6, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 2, 7, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 6, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 4, 3, 6, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 2, 3, 8, p_74875_3_);
            this.func_189926_a(p_74875_1_, EnumFacing.SOUTH, 2, 4, 7, p_74875_3_);
            this.func_189926_a(p_74875_1_, EnumFacing.EAST, 1, 4, 6, p_74875_3_);
            this.func_189926_a(p_74875_1_, EnumFacing.WEST, 3, 4, 6, p_74875_3_);
            this.func_189926_a(p_74875_1_, EnumFacing.NORTH, 2, 4, 5, p_74875_3_);
            IBlockState iblockstate4 = Blocks.LADDER.getDefaultState().func_177226_a(BlockLadder.FACING, EnumFacing.WEST);

            for (int i = 1; i <= 9; ++i)
            {
                this.setBlockState(p_74875_1_, iblockstate4, 3, i, 3, p_74875_3_);
            }

            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 2, 1, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 2, 2, 0, p_74875_3_);
            this.func_189927_a(p_74875_1_, p_74875_3_, p_74875_2_, 2, 1, 0, EnumFacing.NORTH);

            if (this.getBlockStateFromPos(p_74875_1_, 2, 0, -1, p_74875_3_).getMaterial() == Material.AIR && this.getBlockStateFromPos(p_74875_1_, 2, -1, -1, p_74875_3_).getMaterial() != Material.AIR)
            {
                this.setBlockState(p_74875_1_, iblockstate1, 2, 0, -1, p_74875_3_);

                if (this.getBlockStateFromPos(p_74875_1_, 2, -1, -1, p_74875_3_).getBlock() == Blocks.GRASS_PATH)
                {
                    this.setBlockState(p_74875_1_, Blocks.GRASS.getDefaultState(), 2, -1, -1, p_74875_3_);
                }
            }

            for (int k = 0; k < 9; ++k)
            {
                for (int j = 0; j < 5; ++j)
                {
                    this.func_74871_b(p_74875_1_, j, 12, k, p_74875_3_);
                    this.replaceAirAndLiquidDownwards(p_74875_1_, iblockstate, j, -1, k, p_74875_3_);
                }
            }

            this.func_74893_a(p_74875_1_, p_74875_3_, 2, 1, 2, 1);
            return true;
        }

        protected int func_180779_c(int p_180779_1_, int p_180779_2_)
        {
            return 2;
        }
    }

    public static class Field1 extends StructureVillagePieces.Village
    {
        private Block field_82679_b;
        private Block field_82680_c;
        private Block field_82678_d;
        private Block field_82681_h;

        public Field1()
        {
        }

        public Field1(StructureVillagePieces.Start p_i45570_1_, int p_i45570_2_, Random p_i45570_3_, StructureBoundingBox p_i45570_4_, EnumFacing p_i45570_5_)
        {
            super(p_i45570_1_, p_i45570_2_);
            this.setCoordBaseMode(p_i45570_5_);
            this.boundingBox = p_i45570_4_;
            this.field_82679_b = this.func_151559_a(p_i45570_3_);
            this.field_82680_c = this.func_151559_a(p_i45570_3_);
            this.field_82678_d = this.func_151559_a(p_i45570_3_);
            this.field_82681_h = this.func_151559_a(p_i45570_3_);
        }

        protected void func_143012_a(NBTTagCompound p_143012_1_)
        {
            super.func_143012_a(p_143012_1_);
            p_143012_1_.putInt("CA", Block.field_149771_c.getId(this.field_82679_b));
            p_143012_1_.putInt("CB", Block.field_149771_c.getId(this.field_82680_c));
            p_143012_1_.putInt("CC", Block.field_149771_c.getId(this.field_82678_d));
            p_143012_1_.putInt("CD", Block.field_149771_c.getId(this.field_82681_h));
        }

        protected void readAdditional(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            super.readAdditional(tagCompound, p_143011_2_);
            this.field_82679_b = Block.func_149729_e(tagCompound.getInt("CA"));
            this.field_82680_c = Block.func_149729_e(tagCompound.getInt("CB"));
            this.field_82678_d = Block.func_149729_e(tagCompound.getInt("CC"));
            this.field_82681_h = Block.func_149729_e(tagCompound.getInt("CD"));

            if (!(this.field_82679_b instanceof BlockCrops))
            {
                this.field_82679_b = Blocks.WHEAT;
            }

            if (!(this.field_82680_c instanceof BlockCrops))
            {
                this.field_82680_c = Blocks.CARROTS;
            }

            if (!(this.field_82678_d instanceof BlockCrops))
            {
                this.field_82678_d = Blocks.POTATOES;
            }

            if (!(this.field_82681_h instanceof BlockCrops))
            {
                this.field_82681_h = Blocks.BEETROOTS;
            }
        }

        private Block func_151559_a(Random p_151559_1_)
        {
            switch (p_151559_1_.nextInt(10))
            {
                case 0:
                case 1:
                    return Blocks.CARROTS;

                case 2:
                case 3:
                    return Blocks.POTATOES;

                case 4:
                    return Blocks.BEETROOTS;

                default:
                    return Blocks.WHEAT;
            }
        }

        public static StructureVillagePieces.Field1 func_175851_a(StructureVillagePieces.Start p_175851_0_, List<StructureComponent> p_175851_1_, Random p_175851_2_, int p_175851_3_, int p_175851_4_, int p_175851_5_, EnumFacing p_175851_6_, int p_175851_7_)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175851_3_, p_175851_4_, p_175851_5_, 0, 0, 0, 13, 4, 9, p_175851_6_);
            return func_74895_a(structureboundingbox) && StructureComponent.findIntersecting(p_175851_1_, structureboundingbox) == null ? new StructureVillagePieces.Field1(p_175851_0_, p_175851_7_, p_175851_2_, structureboundingbox, p_175851_6_) : null;
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_143015_k < 0)
            {
                this.field_143015_k = this.func_74889_b(p_74875_1_, p_74875_3_);

                if (this.field_143015_k < 0)
                {
                    return true;
                }

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
            }

            IBlockState iblockstate = this.func_175847_a(Blocks.field_150364_r.getDefaultState());
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 1, 0, 12, 4, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 1, 2, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 4, 0, 1, 5, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 7, 0, 1, 8, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 10, 0, 1, 11, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 0, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 6, 0, 0, 6, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 12, 0, 0, 12, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 0, 11, 0, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 8, 11, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 3, 0, 1, 3, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 9, 0, 1, 9, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);

            for (int i = 1; i <= 7; ++i)
            {
                int j = ((BlockCrops)this.field_82679_b).getMaxAge();
                int k = j / 3;
                this.setBlockState(p_74875_1_, this.field_82679_b.func_176203_a(MathHelper.nextInt(p_74875_2_, k, j)), 1, 1, i, p_74875_3_);
                this.setBlockState(p_74875_1_, this.field_82679_b.func_176203_a(MathHelper.nextInt(p_74875_2_, k, j)), 2, 1, i, p_74875_3_);
                int l = ((BlockCrops)this.field_82680_c).getMaxAge();
                int i1 = l / 3;
                this.setBlockState(p_74875_1_, this.field_82680_c.func_176203_a(MathHelper.nextInt(p_74875_2_, i1, l)), 4, 1, i, p_74875_3_);
                this.setBlockState(p_74875_1_, this.field_82680_c.func_176203_a(MathHelper.nextInt(p_74875_2_, i1, l)), 5, 1, i, p_74875_3_);
                int j1 = ((BlockCrops)this.field_82678_d).getMaxAge();
                int k1 = j1 / 3;
                this.setBlockState(p_74875_1_, this.field_82678_d.func_176203_a(MathHelper.nextInt(p_74875_2_, k1, j1)), 7, 1, i, p_74875_3_);
                this.setBlockState(p_74875_1_, this.field_82678_d.func_176203_a(MathHelper.nextInt(p_74875_2_, k1, j1)), 8, 1, i, p_74875_3_);
                int l1 = ((BlockCrops)this.field_82681_h).getMaxAge();
                int i2 = l1 / 3;
                this.setBlockState(p_74875_1_, this.field_82681_h.func_176203_a(MathHelper.nextInt(p_74875_2_, i2, l1)), 10, 1, i, p_74875_3_);
                this.setBlockState(p_74875_1_, this.field_82681_h.func_176203_a(MathHelper.nextInt(p_74875_2_, i2, l1)), 11, 1, i, p_74875_3_);
            }

            for (int j2 = 0; j2 < 9; ++j2)
            {
                for (int k2 = 0; k2 < 13; ++k2)
                {
                    this.func_74871_b(p_74875_1_, k2, 4, j2, p_74875_3_);
                    this.replaceAirAndLiquidDownwards(p_74875_1_, Blocks.DIRT.getDefaultState(), k2, -1, j2, p_74875_3_);
                }
            }

            return true;
        }
    }

    public static class Field2 extends StructureVillagePieces.Village
    {
        private Block field_82675_b;
        private Block field_82676_c;

        public Field2()
        {
        }

        public Field2(StructureVillagePieces.Start p_i45569_1_, int p_i45569_2_, Random p_i45569_3_, StructureBoundingBox p_i45569_4_, EnumFacing p_i45569_5_)
        {
            super(p_i45569_1_, p_i45569_2_);
            this.setCoordBaseMode(p_i45569_5_);
            this.boundingBox = p_i45569_4_;
            this.field_82675_b = this.func_151560_a(p_i45569_3_);
            this.field_82676_c = this.func_151560_a(p_i45569_3_);
        }

        protected void func_143012_a(NBTTagCompound p_143012_1_)
        {
            super.func_143012_a(p_143012_1_);
            p_143012_1_.putInt("CA", Block.field_149771_c.getId(this.field_82675_b));
            p_143012_1_.putInt("CB", Block.field_149771_c.getId(this.field_82676_c));
        }

        protected void readAdditional(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            super.readAdditional(tagCompound, p_143011_2_);
            this.field_82675_b = Block.func_149729_e(tagCompound.getInt("CA"));
            this.field_82676_c = Block.func_149729_e(tagCompound.getInt("CB"));
        }

        private Block func_151560_a(Random p_151560_1_)
        {
            switch (p_151560_1_.nextInt(10))
            {
                case 0:
                case 1:
                    return Blocks.CARROTS;

                case 2:
                case 3:
                    return Blocks.POTATOES;

                case 4:
                    return Blocks.BEETROOTS;

                default:
                    return Blocks.WHEAT;
            }
        }

        public static StructureVillagePieces.Field2 func_175852_a(StructureVillagePieces.Start p_175852_0_, List<StructureComponent> p_175852_1_, Random p_175852_2_, int p_175852_3_, int p_175852_4_, int p_175852_5_, EnumFacing p_175852_6_, int p_175852_7_)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175852_3_, p_175852_4_, p_175852_5_, 0, 0, 0, 7, 4, 9, p_175852_6_);
            return func_74895_a(structureboundingbox) && StructureComponent.findIntersecting(p_175852_1_, structureboundingbox) == null ? new StructureVillagePieces.Field2(p_175852_0_, p_175852_7_, p_175852_2_, structureboundingbox, p_175852_6_) : null;
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_143015_k < 0)
            {
                this.field_143015_k = this.func_74889_b(p_74875_1_, p_74875_3_);

                if (this.field_143015_k < 0)
                {
                    return true;
                }

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
            }

            IBlockState iblockstate = this.func_175847_a(Blocks.field_150364_r.getDefaultState());
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 1, 0, 6, 4, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 1, 2, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 4, 0, 1, 5, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 0, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 6, 0, 0, 6, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 0, 5, 0, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 8, 5, 0, 8, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 3, 0, 1, 3, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);

            for (int i = 1; i <= 7; ++i)
            {
                int j = ((BlockCrops)this.field_82675_b).getMaxAge();
                int k = j / 3;
                this.setBlockState(p_74875_1_, this.field_82675_b.func_176203_a(MathHelper.nextInt(p_74875_2_, k, j)), 1, 1, i, p_74875_3_);
                this.setBlockState(p_74875_1_, this.field_82675_b.func_176203_a(MathHelper.nextInt(p_74875_2_, k, j)), 2, 1, i, p_74875_3_);
                int l = ((BlockCrops)this.field_82676_c).getMaxAge();
                int i1 = l / 3;
                this.setBlockState(p_74875_1_, this.field_82676_c.func_176203_a(MathHelper.nextInt(p_74875_2_, i1, l)), 4, 1, i, p_74875_3_);
                this.setBlockState(p_74875_1_, this.field_82676_c.func_176203_a(MathHelper.nextInt(p_74875_2_, i1, l)), 5, 1, i, p_74875_3_);
            }

            for (int j1 = 0; j1 < 9; ++j1)
            {
                for (int k1 = 0; k1 < 7; ++k1)
                {
                    this.func_74871_b(p_74875_1_, k1, 4, j1, p_74875_3_);
                    this.replaceAirAndLiquidDownwards(p_74875_1_, Blocks.DIRT.getDefaultState(), k1, -1, j1, p_74875_3_);
                }
            }

            return true;
        }
    }

    public static class Hall extends StructureVillagePieces.Village
    {
        public Hall()
        {
        }

        public Hall(StructureVillagePieces.Start p_i45567_1_, int p_i45567_2_, Random p_i45567_3_, StructureBoundingBox p_i45567_4_, EnumFacing p_i45567_5_)
        {
            super(p_i45567_1_, p_i45567_2_);
            this.setCoordBaseMode(p_i45567_5_);
            this.boundingBox = p_i45567_4_;
        }

        public static StructureVillagePieces.Hall func_175857_a(StructureVillagePieces.Start p_175857_0_, List<StructureComponent> p_175857_1_, Random p_175857_2_, int p_175857_3_, int p_175857_4_, int p_175857_5_, EnumFacing p_175857_6_, int p_175857_7_)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175857_3_, p_175857_4_, p_175857_5_, 0, 0, 0, 9, 7, 11, p_175857_6_);
            return func_74895_a(structureboundingbox) && StructureComponent.findIntersecting(p_175857_1_, structureboundingbox) == null ? new StructureVillagePieces.Hall(p_175857_0_, p_175857_7_, p_175857_2_, structureboundingbox, p_175857_6_) : null;
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_143015_k < 0)
            {
                this.field_143015_k = this.func_74889_b(p_74875_1_, p_74875_3_);

                if (this.field_143015_k < 0)
                {
                    return true;
                }

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 7 - 1, 0);
            }

            IBlockState iblockstate = this.func_175847_a(Blocks.COBBLESTONE.getDefaultState());
            IBlockState iblockstate1 = this.func_175847_a(Blocks.OAK_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate2 = this.func_175847_a(Blocks.OAK_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.SOUTH));
            IBlockState iblockstate3 = this.func_175847_a(Blocks.OAK_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.WEST));
            IBlockState iblockstate4 = this.func_175847_a(Blocks.field_150344_f.getDefaultState());
            IBlockState iblockstate5 = this.func_175847_a(Blocks.field_150364_r.getDefaultState());
            IBlockState iblockstate6 = this.func_175847_a(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 1, 7, 4, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 2, 1, 6, 8, 4, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 2, 0, 6, 8, 0, 10, Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState(), false);
            this.setBlockState(p_74875_1_, iblockstate, 6, 0, 6, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 2, 1, 6, 2, 1, 10, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, 1, 6, 8, 1, 10, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 3, 1, 10, 7, 1, 10, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 1, 7, 0, 4, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 0, 3, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, 0, 0, 8, 3, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 0, 7, 1, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 5, 7, 1, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 2, 0, 7, 3, 0, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 2, 5, 7, 3, 5, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 4, 1, 8, 4, 1, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 4, 4, 8, 4, 4, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 5, 2, 8, 5, 3, iblockstate4, iblockstate4, false);
            this.setBlockState(p_74875_1_, iblockstate4, 0, 4, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate4, 0, 4, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate4, 8, 4, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate4, 8, 4, 3, p_74875_3_);
            IBlockState iblockstate7 = iblockstate1;
            IBlockState iblockstate8 = iblockstate2;

            for (int i = -1; i <= 2; ++i)
            {
                for (int j = 0; j <= 8; ++j)
                {
                    this.setBlockState(p_74875_1_, iblockstate7, j, 4 + i, i, p_74875_3_);
                    this.setBlockState(p_74875_1_, iblockstate8, j, 4 + i, 5 - i, p_74875_3_);
                }
            }

            this.setBlockState(p_74875_1_, iblockstate5, 0, 2, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate5, 0, 2, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate5, 8, 2, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate5, 8, 2, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 2, 1, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150452_aw.getDefaultState(), 2, 2, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate4, 1, 1, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate7, 2, 1, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, 1, 1, 3, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 5, 0, 1, 7, 0, 3, Blocks.field_150334_T.getDefaultState(), Blocks.field_150334_T.getDefaultState(), false);
            this.setBlockState(p_74875_1_, Blocks.field_150334_T.getDefaultState(), 6, 1, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150334_T.getDefaultState(), 6, 1, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 2, 1, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 2, 2, 0, p_74875_3_);
            this.func_189926_a(p_74875_1_, EnumFacing.NORTH, 2, 3, 1, p_74875_3_);
            this.func_189927_a(p_74875_1_, p_74875_3_, p_74875_2_, 2, 1, 0, EnumFacing.NORTH);

            if (this.getBlockStateFromPos(p_74875_1_, 2, 0, -1, p_74875_3_).getMaterial() == Material.AIR && this.getBlockStateFromPos(p_74875_1_, 2, -1, -1, p_74875_3_).getMaterial() != Material.AIR)
            {
                this.setBlockState(p_74875_1_, iblockstate7, 2, 0, -1, p_74875_3_);

                if (this.getBlockStateFromPos(p_74875_1_, 2, -1, -1, p_74875_3_).getBlock() == Blocks.GRASS_PATH)
                {
                    this.setBlockState(p_74875_1_, Blocks.GRASS.getDefaultState(), 2, -1, -1, p_74875_3_);
                }
            }

            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 6, 1, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 6, 2, 5, p_74875_3_);
            this.func_189926_a(p_74875_1_, EnumFacing.SOUTH, 6, 3, 4, p_74875_3_);
            this.func_189927_a(p_74875_1_, p_74875_3_, p_74875_2_, 6, 1, 5, EnumFacing.SOUTH);

            for (int k = 0; k < 5; ++k)
            {
                for (int l = 0; l < 9; ++l)
                {
                    this.func_74871_b(p_74875_1_, l, 7, k, p_74875_3_);
                    this.replaceAirAndLiquidDownwards(p_74875_1_, iblockstate, l, -1, k, p_74875_3_);
                }
            }

            this.func_74893_a(p_74875_1_, p_74875_3_, 4, 1, 2, 2);
            return true;
        }

        protected int func_180779_c(int p_180779_1_, int p_180779_2_)
        {
            return p_180779_1_ == 0 ? 4 : super.func_180779_c(p_180779_1_, p_180779_2_);
        }
    }

    public static class House1 extends StructureVillagePieces.Village
    {
        public House1()
        {
        }

        public House1(StructureVillagePieces.Start p_i45571_1_, int p_i45571_2_, Random p_i45571_3_, StructureBoundingBox p_i45571_4_, EnumFacing p_i45571_5_)
        {
            super(p_i45571_1_, p_i45571_2_);
            this.setCoordBaseMode(p_i45571_5_);
            this.boundingBox = p_i45571_4_;
        }

        public static StructureVillagePieces.House1 func_175850_a(StructureVillagePieces.Start p_175850_0_, List<StructureComponent> p_175850_1_, Random p_175850_2_, int p_175850_3_, int p_175850_4_, int p_175850_5_, EnumFacing p_175850_6_, int p_175850_7_)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175850_3_, p_175850_4_, p_175850_5_, 0, 0, 0, 9, 9, 6, p_175850_6_);
            return func_74895_a(structureboundingbox) && StructureComponent.findIntersecting(p_175850_1_, structureboundingbox) == null ? new StructureVillagePieces.House1(p_175850_0_, p_175850_7_, p_175850_2_, structureboundingbox, p_175850_6_) : null;
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_143015_k < 0)
            {
                this.field_143015_k = this.func_74889_b(p_74875_1_, p_74875_3_);

                if (this.field_143015_k < 0)
                {
                    return true;
                }

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 9 - 1, 0);
            }

            IBlockState iblockstate = this.func_175847_a(Blocks.COBBLESTONE.getDefaultState());
            IBlockState iblockstate1 = this.func_175847_a(Blocks.OAK_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate2 = this.func_175847_a(Blocks.OAK_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.SOUTH));
            IBlockState iblockstate3 = this.func_175847_a(Blocks.OAK_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.EAST));
            IBlockState iblockstate4 = this.func_175847_a(Blocks.field_150344_f.getDefaultState());
            IBlockState iblockstate5 = this.func_175847_a(Blocks.field_150446_ar.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate6 = this.func_175847_a(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 1, 7, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 8, 0, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 5, 0, 8, 5, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 6, 1, 8, 6, 4, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 7, 2, 8, 7, 3, iblockstate, iblockstate, false);

            for (int i = -1; i <= 2; ++i)
            {
                for (int j = 0; j <= 8; ++j)
                {
                    this.setBlockState(p_74875_1_, iblockstate1, j, 6 + i, i, p_74875_3_);
                    this.setBlockState(p_74875_1_, iblockstate2, j, 6 + i, 5 - i, p_74875_3_);
                }
            }

            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 1, 0, 0, 1, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 5, 8, 1, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, 1, 0, 8, 1, 4, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 2, 1, 0, 7, 1, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 2, 0, 0, 4, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 2, 5, 0, 4, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, 2, 5, 8, 4, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, 2, 0, 8, 4, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 2, 1, 0, 4, 4, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 2, 5, 7, 4, 5, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, 2, 1, 8, 4, 4, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 2, 0, 7, 4, 0, iblockstate4, iblockstate4, false);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 4, 3, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 5, 3, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 6, 3, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 8, 3, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 8, 3, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 5, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 4, 1, 7, 4, 1, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 4, 4, 7, 4, 4, iblockstate4, iblockstate4, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 3, 4, 7, 3, 4, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            this.setBlockState(p_74875_1_, iblockstate4, 7, 1, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, 7, 1, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 6, 1, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 5, 1, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 4, 1, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 3, 1, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 6, 1, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150452_aw.getDefaultState(), 6, 2, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 4, 1, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150452_aw.getDefaultState(), 4, 2, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.CRAFTING_TABLE.getDefaultState(), 7, 1, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 1, 1, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 1, 2, 0, p_74875_3_);
            this.func_189927_a(p_74875_1_, p_74875_3_, p_74875_2_, 1, 1, 0, EnumFacing.NORTH);

            if (this.getBlockStateFromPos(p_74875_1_, 1, 0, -1, p_74875_3_).getMaterial() == Material.AIR && this.getBlockStateFromPos(p_74875_1_, 1, -1, -1, p_74875_3_).getMaterial() != Material.AIR)
            {
                this.setBlockState(p_74875_1_, iblockstate5, 1, 0, -1, p_74875_3_);

                if (this.getBlockStateFromPos(p_74875_1_, 1, -1, -1, p_74875_3_).getBlock() == Blocks.GRASS_PATH)
                {
                    this.setBlockState(p_74875_1_, Blocks.GRASS.getDefaultState(), 1, -1, -1, p_74875_3_);
                }
            }

            for (int l = 0; l < 6; ++l)
            {
                for (int k = 0; k < 9; ++k)
                {
                    this.func_74871_b(p_74875_1_, k, 9, l, p_74875_3_);
                    this.replaceAirAndLiquidDownwards(p_74875_1_, iblockstate, k, -1, l, p_74875_3_);
                }
            }

            this.func_74893_a(p_74875_1_, p_74875_3_, 2, 1, 2, 1);
            return true;
        }

        protected int func_180779_c(int p_180779_1_, int p_180779_2_)
        {
            return 1;
        }
    }

    public static class House2 extends StructureVillagePieces.Village
    {
        private boolean field_74917_c;

        public House2()
        {
        }

        public House2(StructureVillagePieces.Start p_i45563_1_, int p_i45563_2_, Random p_i45563_3_, StructureBoundingBox p_i45563_4_, EnumFacing p_i45563_5_)
        {
            super(p_i45563_1_, p_i45563_2_);
            this.setCoordBaseMode(p_i45563_5_);
            this.boundingBox = p_i45563_4_;
        }

        public static StructureVillagePieces.House2 func_175855_a(StructureVillagePieces.Start p_175855_0_, List<StructureComponent> p_175855_1_, Random p_175855_2_, int p_175855_3_, int p_175855_4_, int p_175855_5_, EnumFacing p_175855_6_, int p_175855_7_)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175855_3_, p_175855_4_, p_175855_5_, 0, 0, 0, 10, 6, 7, p_175855_6_);
            return func_74895_a(structureboundingbox) && StructureComponent.findIntersecting(p_175855_1_, structureboundingbox) == null ? new StructureVillagePieces.House2(p_175855_0_, p_175855_7_, p_175855_2_, structureboundingbox, p_175855_6_) : null;
        }

        protected void func_143012_a(NBTTagCompound p_143012_1_)
        {
            super.func_143012_a(p_143012_1_);
            p_143012_1_.putBoolean("Chest", this.field_74917_c);
        }

        protected void readAdditional(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            super.readAdditional(tagCompound, p_143011_2_);
            this.field_74917_c = tagCompound.getBoolean("Chest");
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_143015_k < 0)
            {
                this.field_143015_k = this.func_74889_b(p_74875_1_, p_74875_3_);

                if (this.field_143015_k < 0)
                {
                    return true;
                }

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
            }

            IBlockState iblockstate = Blocks.COBBLESTONE.getDefaultState();
            IBlockState iblockstate1 = this.func_175847_a(Blocks.OAK_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate2 = this.func_175847_a(Blocks.OAK_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.WEST));
            IBlockState iblockstate3 = this.func_175847_a(Blocks.field_150344_f.getDefaultState());
            IBlockState iblockstate4 = this.func_175847_a(Blocks.field_150446_ar.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate5 = this.func_175847_a(Blocks.field_150364_r.getDefaultState());
            IBlockState iblockstate6 = this.func_175847_a(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 1, 0, 9, 4, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 9, 0, 6, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 4, 0, 9, 4, 6, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 5, 0, 9, 5, 6, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 5, 1, 8, 5, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 0, 2, 3, 0, iblockstate3, iblockstate3, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 1, 0, 0, 4, 0, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 3, 1, 0, 3, 4, 0, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 1, 6, 0, 4, 6, iblockstate5, iblockstate5, false);
            this.setBlockState(p_74875_1_, iblockstate3, 3, 3, 1, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 3, 1, 2, 3, 3, 2, iblockstate3, iblockstate3, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 4, 1, 3, 5, 3, 3, iblockstate3, iblockstate3, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 1, 1, 0, 3, 5, iblockstate3, iblockstate3, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 6, 5, 3, 6, iblockstate3, iblockstate3, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 5, 1, 0, 5, 3, 0, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 9, 1, 0, 9, 3, 0, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 6, 1, 4, 9, 4, 6, iblockstate, iblockstate, false);
            this.setBlockState(p_74875_1_, Blocks.field_150356_k.getDefaultState(), 7, 1, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150356_k.getDefaultState(), 8, 1, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.IRON_BARS.getDefaultState(), 9, 2, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.IRON_BARS.getDefaultState(), 9, 2, 4, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 7, 2, 4, 8, 2, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(p_74875_1_, iblockstate, 6, 1, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.FURNACE.getDefaultState(), 6, 2, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.FURNACE.getDefaultState(), 6, 3, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150334_T.getDefaultState(), 8, 1, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 6, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 6, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 2, 1, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150452_aw.getDefaultState(), 2, 2, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, 1, 1, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 2, 1, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate2, 1, 1, 4, p_74875_3_);

            if (!this.field_74917_c && p_74875_3_.isVecInside(new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(1), this.getZWithOffset(5, 5))))
            {
                this.field_74917_c = true;
                this.generateChest(p_74875_1_, p_74875_3_, p_74875_2_, 5, 1, 5, LootTableList.field_186423_e);
            }

            for (int i = 6; i <= 8; ++i)
            {
                if (this.getBlockStateFromPos(p_74875_1_, i, 0, -1, p_74875_3_).getMaterial() == Material.AIR && this.getBlockStateFromPos(p_74875_1_, i, -1, -1, p_74875_3_).getMaterial() != Material.AIR)
                {
                    this.setBlockState(p_74875_1_, iblockstate4, i, 0, -1, p_74875_3_);

                    if (this.getBlockStateFromPos(p_74875_1_, i, -1, -1, p_74875_3_).getBlock() == Blocks.GRASS_PATH)
                    {
                        this.setBlockState(p_74875_1_, Blocks.GRASS.getDefaultState(), i, -1, -1, p_74875_3_);
                    }
                }
            }

            for (int k = 0; k < 7; ++k)
            {
                for (int j = 0; j < 10; ++j)
                {
                    this.func_74871_b(p_74875_1_, j, 6, k, p_74875_3_);
                    this.replaceAirAndLiquidDownwards(p_74875_1_, iblockstate, j, -1, k, p_74875_3_);
                }
            }

            this.func_74893_a(p_74875_1_, p_74875_3_, 7, 1, 1, 1);
            return true;
        }

        protected int func_180779_c(int p_180779_1_, int p_180779_2_)
        {
            return 3;
        }
    }

    public static class House3 extends StructureVillagePieces.Village
    {
        public House3()
        {
        }

        public House3(StructureVillagePieces.Start p_i45561_1_, int p_i45561_2_, Random p_i45561_3_, StructureBoundingBox p_i45561_4_, EnumFacing p_i45561_5_)
        {
            super(p_i45561_1_, p_i45561_2_);
            this.setCoordBaseMode(p_i45561_5_);
            this.boundingBox = p_i45561_4_;
        }

        public static StructureVillagePieces.House3 func_175849_a(StructureVillagePieces.Start p_175849_0_, List<StructureComponent> p_175849_1_, Random p_175849_2_, int p_175849_3_, int p_175849_4_, int p_175849_5_, EnumFacing p_175849_6_, int p_175849_7_)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175849_3_, p_175849_4_, p_175849_5_, 0, 0, 0, 9, 7, 12, p_175849_6_);
            return func_74895_a(structureboundingbox) && StructureComponent.findIntersecting(p_175849_1_, structureboundingbox) == null ? new StructureVillagePieces.House3(p_175849_0_, p_175849_7_, p_175849_2_, structureboundingbox, p_175849_6_) : null;
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_143015_k < 0)
            {
                this.field_143015_k = this.func_74889_b(p_74875_1_, p_74875_3_);

                if (this.field_143015_k < 0)
                {
                    return true;
                }

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 7 - 1, 0);
            }

            IBlockState iblockstate = this.func_175847_a(Blocks.COBBLESTONE.getDefaultState());
            IBlockState iblockstate1 = this.func_175847_a(Blocks.OAK_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate2 = this.func_175847_a(Blocks.OAK_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.SOUTH));
            IBlockState iblockstate3 = this.func_175847_a(Blocks.OAK_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.EAST));
            IBlockState iblockstate4 = this.func_175847_a(Blocks.OAK_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.WEST));
            IBlockState iblockstate5 = this.func_175847_a(Blocks.field_150344_f.getDefaultState());
            IBlockState iblockstate6 = this.func_175847_a(Blocks.field_150364_r.getDefaultState());
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 1, 7, 4, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 2, 1, 6, 8, 4, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 2, 0, 5, 8, 0, 10, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 1, 7, 0, 4, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 0, 3, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 8, 0, 0, 8, 3, 10, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 0, 7, 2, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 5, 2, 1, 5, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 2, 0, 6, 2, 3, 10, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 3, 0, 10, 7, 3, 10, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 2, 0, 7, 3, 0, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 2, 5, 2, 3, 5, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 4, 1, 8, 4, 1, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 4, 4, 3, 4, 4, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 5, 2, 8, 5, 3, iblockstate5, iblockstate5, false);
            this.setBlockState(p_74875_1_, iblockstate5, 0, 4, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate5, 0, 4, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate5, 8, 4, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate5, 8, 4, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate5, 8, 4, 4, p_74875_3_);
            IBlockState iblockstate7 = iblockstate1;
            IBlockState iblockstate8 = iblockstate2;
            IBlockState iblockstate9 = iblockstate4;
            IBlockState iblockstate10 = iblockstate3;

            for (int i = -1; i <= 2; ++i)
            {
                for (int j = 0; j <= 8; ++j)
                {
                    this.setBlockState(p_74875_1_, iblockstate7, j, 4 + i, i, p_74875_3_);

                    if ((i > -1 || j <= 1) && (i > 0 || j <= 3) && (i > 1 || j <= 4 || j >= 6))
                    {
                        this.setBlockState(p_74875_1_, iblockstate8, j, 4 + i, 5 - i, p_74875_3_);
                    }
                }
            }

            this.fillWithBlocks(p_74875_1_, p_74875_3_, 3, 4, 5, 3, 4, 10, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 7, 4, 2, 7, 4, 10, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 4, 5, 4, 4, 5, 10, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 6, 5, 4, 6, 5, 10, iblockstate5, iblockstate5, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 5, 6, 3, 5, 6, 10, iblockstate5, iblockstate5, false);

            for (int k = 4; k >= 1; --k)
            {
                this.setBlockState(p_74875_1_, iblockstate5, k, 2 + k, 7 - k, p_74875_3_);

                for (int k1 = 8 - k; k1 <= 10; ++k1)
                {
                    this.setBlockState(p_74875_1_, iblockstate10, k, 2 + k, k1, p_74875_3_);
                }
            }

            this.setBlockState(p_74875_1_, iblockstate5, 6, 6, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate5, 7, 5, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate4, 6, 6, 4, p_74875_3_);

            for (int l = 6; l <= 8; ++l)
            {
                for (int l1 = 5; l1 <= 10; ++l1)
                {
                    this.setBlockState(p_74875_1_, iblockstate9, l, 12 - l, l1, p_74875_3_);
                }
            }

            this.setBlockState(p_74875_1_, iblockstate6, 0, 2, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 0, 2, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 4, 2, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 6, 2, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 8, 2, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 8, 2, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate5, 8, 2, 5, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 8, 2, 6, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 7, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 8, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 8, 2, 9, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 2, 2, 6, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 7, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 8, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 2, 2, 9, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 4, 4, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 5, 4, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate6, 6, 4, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate5, 5, 5, 10, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 2, 1, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 2, 2, 0, p_74875_3_);
            this.func_189926_a(p_74875_1_, EnumFacing.NORTH, 2, 3, 1, p_74875_3_);
            this.func_189927_a(p_74875_1_, p_74875_3_, p_74875_2_, 2, 1, 0, EnumFacing.NORTH);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, -1, 3, 2, -1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

            if (this.getBlockStateFromPos(p_74875_1_, 2, 0, -1, p_74875_3_).getMaterial() == Material.AIR && this.getBlockStateFromPos(p_74875_1_, 2, -1, -1, p_74875_3_).getMaterial() != Material.AIR)
            {
                this.setBlockState(p_74875_1_, iblockstate7, 2, 0, -1, p_74875_3_);

                if (this.getBlockStateFromPos(p_74875_1_, 2, -1, -1, p_74875_3_).getBlock() == Blocks.GRASS_PATH)
                {
                    this.setBlockState(p_74875_1_, Blocks.GRASS.getDefaultState(), 2, -1, -1, p_74875_3_);
                }
            }

            for (int i1 = 0; i1 < 5; ++i1)
            {
                for (int i2 = 0; i2 < 9; ++i2)
                {
                    this.func_74871_b(p_74875_1_, i2, 7, i1, p_74875_3_);
                    this.replaceAirAndLiquidDownwards(p_74875_1_, iblockstate, i2, -1, i1, p_74875_3_);
                }
            }

            for (int j1 = 5; j1 < 11; ++j1)
            {
                for (int j2 = 2; j2 < 9; ++j2)
                {
                    this.func_74871_b(p_74875_1_, j2, 7, j1, p_74875_3_);
                    this.replaceAirAndLiquidDownwards(p_74875_1_, iblockstate, j2, -1, j1, p_74875_3_);
                }
            }

            this.func_74893_a(p_74875_1_, p_74875_3_, 4, 1, 2, 2);
            return true;
        }
    }

    public static class House4Garden extends StructureVillagePieces.Village
    {
        private boolean field_74913_b;

        public House4Garden()
        {
        }

        public House4Garden(StructureVillagePieces.Start p_i45566_1_, int p_i45566_2_, Random p_i45566_3_, StructureBoundingBox p_i45566_4_, EnumFacing p_i45566_5_)
        {
            super(p_i45566_1_, p_i45566_2_);
            this.setCoordBaseMode(p_i45566_5_);
            this.boundingBox = p_i45566_4_;
            this.field_74913_b = p_i45566_3_.nextBoolean();
        }

        protected void func_143012_a(NBTTagCompound p_143012_1_)
        {
            super.func_143012_a(p_143012_1_);
            p_143012_1_.putBoolean("Terrace", this.field_74913_b);
        }

        protected void readAdditional(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            super.readAdditional(tagCompound, p_143011_2_);
            this.field_74913_b = tagCompound.getBoolean("Terrace");
        }

        public static StructureVillagePieces.House4Garden func_175858_a(StructureVillagePieces.Start p_175858_0_, List<StructureComponent> p_175858_1_, Random p_175858_2_, int p_175858_3_, int p_175858_4_, int p_175858_5_, EnumFacing p_175858_6_, int p_175858_7_)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175858_3_, p_175858_4_, p_175858_5_, 0, 0, 0, 5, 6, 5, p_175858_6_);
            return StructureComponent.findIntersecting(p_175858_1_, structureboundingbox) != null ? null : new StructureVillagePieces.House4Garden(p_175858_0_, p_175858_7_, p_175858_2_, structureboundingbox, p_175858_6_);
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_143015_k < 0)
            {
                this.field_143015_k = this.func_74889_b(p_74875_1_, p_74875_3_);

                if (this.field_143015_k < 0)
                {
                    return true;
                }

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
            }

            IBlockState iblockstate = this.func_175847_a(Blocks.COBBLESTONE.getDefaultState());
            IBlockState iblockstate1 = this.func_175847_a(Blocks.field_150344_f.getDefaultState());
            IBlockState iblockstate2 = this.func_175847_a(Blocks.field_150446_ar.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate3 = this.func_175847_a(Blocks.field_150364_r.getDefaultState());
            IBlockState iblockstate4 = this.func_175847_a(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 4, 0, 4, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 4, 0, 4, 4, 4, iblockstate3, iblockstate3, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 4, 1, 3, 4, 3, iblockstate1, iblockstate1, false);
            this.setBlockState(p_74875_1_, iblockstate, 0, 1, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 0, 2, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 0, 3, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 4, 1, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 4, 2, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 4, 3, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 0, 1, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 0, 2, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 0, 3, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 4, 1, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 4, 2, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 4, 3, 4, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 1, 1, 0, 3, 3, iblockstate1, iblockstate1, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 4, 1, 1, 4, 3, 3, iblockstate1, iblockstate1, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 4, 3, 3, 4, iblockstate1, iblockstate1, false);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 1, 1, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 1, 2, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 1, 3, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 2, 3, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 3, 3, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 3, 2, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 3, 1, 0, p_74875_3_);

            if (this.getBlockStateFromPos(p_74875_1_, 2, 0, -1, p_74875_3_).getMaterial() == Material.AIR && this.getBlockStateFromPos(p_74875_1_, 2, -1, -1, p_74875_3_).getMaterial() != Material.AIR)
            {
                this.setBlockState(p_74875_1_, iblockstate2, 2, 0, -1, p_74875_3_);

                if (this.getBlockStateFromPos(p_74875_1_, 2, -1, -1, p_74875_3_).getBlock() == Blocks.GRASS_PATH)
                {
                    this.setBlockState(p_74875_1_, Blocks.GRASS.getDefaultState(), 2, -1, -1, p_74875_3_);
                }
            }

            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 1, 3, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

            if (this.field_74913_b)
            {
                this.setBlockState(p_74875_1_, iblockstate4, 0, 5, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 1, 5, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 2, 5, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 3, 5, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 4, 5, 0, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 0, 5, 4, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 1, 5, 4, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 2, 5, 4, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 3, 5, 4, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 4, 5, 4, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 4, 5, 1, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 4, 5, 2, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 4, 5, 3, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 0, 5, 1, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 0, 5, 2, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate4, 0, 5, 3, p_74875_3_);
            }

            if (this.field_74913_b)
            {
                IBlockState iblockstate5 = Blocks.LADDER.getDefaultState().func_177226_a(BlockLadder.FACING, EnumFacing.SOUTH);
                this.setBlockState(p_74875_1_, iblockstate5, 3, 1, 3, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate5, 3, 2, 3, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate5, 3, 3, 3, p_74875_3_);
                this.setBlockState(p_74875_1_, iblockstate5, 3, 4, 3, p_74875_3_);
            }

            this.func_189926_a(p_74875_1_, EnumFacing.NORTH, 2, 3, 1, p_74875_3_);

            for (int j = 0; j < 5; ++j)
            {
                for (int i = 0; i < 5; ++i)
                {
                    this.func_74871_b(p_74875_1_, i, 6, j, p_74875_3_);
                    this.replaceAirAndLiquidDownwards(p_74875_1_, iblockstate, i, -1, j, p_74875_3_);
                }
            }

            this.func_74893_a(p_74875_1_, p_74875_3_, 1, 1, 2, 1);
            return true;
        }
    }

    public static class Path extends StructureVillagePieces.Road
    {
        private int field_74934_a;

        public Path()
        {
        }

        public Path(StructureVillagePieces.Start p_i45562_1_, int p_i45562_2_, Random p_i45562_3_, StructureBoundingBox p_i45562_4_, EnumFacing p_i45562_5_)
        {
            super(p_i45562_1_, p_i45562_2_);
            this.setCoordBaseMode(p_i45562_5_);
            this.boundingBox = p_i45562_4_;
            this.field_74934_a = Math.max(p_i45562_4_.getXSize(), p_i45562_4_.getZSize());
        }

        protected void func_143012_a(NBTTagCompound p_143012_1_)
        {
            super.func_143012_a(p_143012_1_);
            p_143012_1_.putInt("Length", this.field_74934_a);
        }

        protected void readAdditional(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            super.readAdditional(tagCompound, p_143011_2_);
            this.field_74934_a = tagCompound.getInt("Length");
        }

        public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
        {
            boolean flag = false;

            for (int i = rand.nextInt(5); i < this.field_74934_a - 8; i += 2 + rand.nextInt(5))
            {
                StructureComponent structurecomponent = this.func_74891_a((StructureVillagePieces.Start)componentIn, listIn, rand, 0, i);

                if (structurecomponent != null)
                {
                    i += Math.max(structurecomponent.boundingBox.getXSize(), structurecomponent.boundingBox.getZSize());
                    flag = true;
                }
            }

            for (int j = rand.nextInt(5); j < this.field_74934_a - 8; j += 2 + rand.nextInt(5))
            {
                StructureComponent structurecomponent1 = this.func_74894_b((StructureVillagePieces.Start)componentIn, listIn, rand, 0, j);

                if (structurecomponent1 != null)
                {
                    j += Math.max(structurecomponent1.boundingBox.getXSize(), structurecomponent1.boundingBox.getZSize());
                    flag = true;
                }
            }

            EnumFacing enumfacing = this.getCoordBaseMode();

            if (flag && rand.nextInt(3) > 0 && enumfacing != null)
            {
                switch (enumfacing)
                {
                    case NORTH:
                    default:
                        StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, this.getComponentType());
                        break;

                    case SOUTH:
                        StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, this.getComponentType());
                        break;

                    case WEST:
                        StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                        break;

                    case EAST:
                        StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                }
            }

            if (flag && rand.nextInt(3) > 0 && enumfacing != null)
            {
                switch (enumfacing)
                {
                    case NORTH:
                    default:
                        StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
                        break;

                    case SOUTH:
                        StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, this.getComponentType());
                        break;

                    case WEST:
                        StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                        break;

                    case EAST:
                        StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                }
            }
        }

        public static StructureBoundingBox func_175848_a(StructureVillagePieces.Start p_175848_0_, List<StructureComponent> p_175848_1_, Random p_175848_2_, int p_175848_3_, int p_175848_4_, int p_175848_5_, EnumFacing p_175848_6_)
        {
            for (int i = 7 * MathHelper.nextInt(p_175848_2_, 3, 5); i >= 7; i -= 7)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175848_3_, p_175848_4_, p_175848_5_, 0, 0, 0, 3, 3, i, p_175848_6_);

                if (StructureComponent.findIntersecting(p_175848_1_, structureboundingbox) == null)
                {
                    return structureboundingbox;
                }
            }

            return null;
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            IBlockState iblockstate = this.func_175847_a(Blocks.GRASS_PATH.getDefaultState());
            IBlockState iblockstate1 = this.func_175847_a(Blocks.field_150344_f.getDefaultState());
            IBlockState iblockstate2 = this.func_175847_a(Blocks.GRAVEL.getDefaultState());
            IBlockState iblockstate3 = this.func_175847_a(Blocks.COBBLESTONE.getDefaultState());

            for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i)
            {
                for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j)
                {
                    BlockPos blockpos = new BlockPos(i, 64, j);

                    if (p_74875_3_.isVecInside(blockpos))
                    {
                        blockpos = p_74875_1_.func_175672_r(blockpos).down();

                        if (blockpos.getY() < p_74875_1_.getSeaLevel())
                        {
                            blockpos = new BlockPos(blockpos.getX(), p_74875_1_.getSeaLevel() - 1, blockpos.getZ());
                        }

                        while (blockpos.getY() >= p_74875_1_.getSeaLevel() - 1)
                        {
                            IBlockState iblockstate4 = p_74875_1_.getBlockState(blockpos);

                            if (iblockstate4.getBlock() == Blocks.GRASS && p_74875_1_.isAirBlock(blockpos.up()))
                            {
                                p_74875_1_.setBlockState(blockpos, iblockstate, 2);
                                break;
                            }

                            if (iblockstate4.getMaterial().isLiquid())
                            {
                                p_74875_1_.setBlockState(blockpos, iblockstate1, 2);
                                break;
                            }

                            if (iblockstate4.getBlock() == Blocks.SAND || iblockstate4.getBlock() == Blocks.SANDSTONE || iblockstate4.getBlock() == Blocks.RED_SANDSTONE)
                            {
                                p_74875_1_.setBlockState(blockpos, iblockstate2, 2);
                                p_74875_1_.setBlockState(blockpos.down(), iblockstate3, 2);
                                break;
                            }

                            blockpos = blockpos.down();
                        }
                    }
                }
            }

            return true;
        }
    }

    public static class PieceWeight
    {
        public Class <? extends StructureVillagePieces.Village > field_75090_a;
        public final int field_75088_b;
        public int field_75089_c;
        public int field_75087_d;

        public PieceWeight(Class <? extends StructureVillagePieces.Village > p_i2098_1_, int p_i2098_2_, int p_i2098_3_)
        {
            this.field_75090_a = p_i2098_1_;
            this.field_75088_b = p_i2098_2_;
            this.field_75087_d = p_i2098_3_;
        }

        public boolean func_75085_a(int p_75085_1_)
        {
            return this.field_75087_d == 0 || this.field_75089_c < this.field_75087_d;
        }

        public boolean func_75086_a()
        {
            return this.field_75087_d == 0 || this.field_75089_c < this.field_75087_d;
        }
    }

    public abstract static class Road extends StructureVillagePieces.Village
    {
        public Road()
        {
        }

        protected Road(StructureVillagePieces.Start p_i2108_1_, int p_i2108_2_)
        {
            super(p_i2108_1_, p_i2108_2_);
        }
    }

    public static class Start extends StructureVillagePieces.Well
    {
        public BiomeProvider field_74929_a;
        public int field_74928_c;
        public StructureVillagePieces.PieceWeight field_74926_d;
        public List<StructureVillagePieces.PieceWeight> field_74931_h;
        public List<StructureComponent> field_74932_i = Lists.<StructureComponent>newArrayList();
        public List<StructureComponent> field_74930_j = Lists.<StructureComponent>newArrayList();

        public Start()
        {
        }

        public Start(BiomeProvider p_i2104_1_, int p_i2104_2_, Random p_i2104_3_, int p_i2104_4_, int p_i2104_5_, List<StructureVillagePieces.PieceWeight> p_i2104_6_, int p_i2104_7_)
        {
            super((StructureVillagePieces.Start)null, 0, p_i2104_3_, p_i2104_4_, p_i2104_5_);
            this.field_74929_a = p_i2104_1_;
            this.field_74931_h = p_i2104_6_;
            this.field_74928_c = p_i2104_7_;
            Biome biome = p_i2104_1_.func_180300_a(new BlockPos(p_i2104_4_, 0, p_i2104_5_), Biomes.DEFAULT);

            if (biome instanceof BiomeDesert)
            {
                this.field_189928_h = 1;
            }
            else if (biome instanceof BiomeSavanna)
            {
                this.field_189928_h = 2;
            }
            else if (biome instanceof BiomeTaiga)
            {
                this.field_189928_h = 3;
            }

            this.func_189924_a(this.field_189928_h);
            this.field_189929_i = p_i2104_3_.nextInt(50) == 0;
        }
    }

    public static class Torch extends StructureVillagePieces.Village
    {
        public Torch()
        {
        }

        public Torch(StructureVillagePieces.Start p_i45568_1_, int p_i45568_2_, Random p_i45568_3_, StructureBoundingBox p_i45568_4_, EnumFacing p_i45568_5_)
        {
            super(p_i45568_1_, p_i45568_2_);
            this.setCoordBaseMode(p_i45568_5_);
            this.boundingBox = p_i45568_4_;
        }

        public static StructureBoundingBox func_175856_a(StructureVillagePieces.Start p_175856_0_, List<StructureComponent> p_175856_1_, Random p_175856_2_, int p_175856_3_, int p_175856_4_, int p_175856_5_, EnumFacing p_175856_6_)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175856_3_, p_175856_4_, p_175856_5_, 0, 0, 0, 3, 4, 2, p_175856_6_);
            return StructureComponent.findIntersecting(p_175856_1_, structureboundingbox) != null ? null : structureboundingbox;
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_143015_k < 0)
            {
                this.field_143015_k = this.func_74889_b(p_74875_1_, p_74875_3_);

                if (this.field_143015_k < 0)
                {
                    return true;
                }

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
            }

            IBlockState iblockstate = this.func_175847_a(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 2, 3, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(p_74875_1_, iblockstate, 1, 0, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 1, 1, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate, 1, 2, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.field_150325_L.func_176203_a(EnumDyeColor.WHITE.func_176767_b()), 1, 3, 0, p_74875_3_);
            this.func_189926_a(p_74875_1_, EnumFacing.EAST, 2, 3, 0, p_74875_3_);
            this.func_189926_a(p_74875_1_, EnumFacing.NORTH, 1, 3, 1, p_74875_3_);
            this.func_189926_a(p_74875_1_, EnumFacing.WEST, 0, 3, 0, p_74875_3_);
            this.func_189926_a(p_74875_1_, EnumFacing.SOUTH, 1, 3, -1, p_74875_3_);
            return true;
        }
    }

    abstract static class Village extends StructureComponent
    {
        protected int field_143015_k = -1;
        private int field_74896_a;
        protected int field_189928_h;
        protected boolean field_189929_i;

        public Village()
        {
        }

        protected Village(StructureVillagePieces.Start p_i2107_1_, int p_i2107_2_)
        {
            super(p_i2107_2_);

            if (p_i2107_1_ != null)
            {
                this.field_189928_h = p_i2107_1_.field_189928_h;
                this.field_189929_i = p_i2107_1_.field_189929_i;
            }
        }

        protected void func_143012_a(NBTTagCompound p_143012_1_)
        {
            p_143012_1_.putInt("HPos", this.field_143015_k);
            p_143012_1_.putInt("VCount", this.field_74896_a);
            p_143012_1_.putByte("Type", (byte)this.field_189928_h);
            p_143012_1_.putBoolean("Zombie", this.field_189929_i);
        }

        protected void readAdditional(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            this.field_143015_k = tagCompound.getInt("HPos");
            this.field_74896_a = tagCompound.getInt("VCount");
            this.field_189928_h = tagCompound.getByte("Type");

            if (tagCompound.getBoolean("Desert"))
            {
                this.field_189928_h = 1;
            }

            this.field_189929_i = tagCompound.getBoolean("Zombie");
        }

        @Nullable
        protected StructureComponent func_74891_a(StructureVillagePieces.Start p_74891_1_, List<StructureComponent> p_74891_2_, Random p_74891_3_, int p_74891_4_, int p_74891_5_)
        {
            EnumFacing enumfacing = this.getCoordBaseMode();

            if (enumfacing != null)
            {
                switch (enumfacing)
                {
                    case NORTH:
                    default:
                        return StructureVillagePieces.func_176066_d(p_74891_1_, p_74891_2_, p_74891_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ + p_74891_5_, EnumFacing.WEST, this.getComponentType());

                    case SOUTH:
                        return StructureVillagePieces.func_176066_d(p_74891_1_, p_74891_2_, p_74891_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ + p_74891_5_, EnumFacing.WEST, this.getComponentType());

                    case WEST:
                        return StructureVillagePieces.func_176066_d(p_74891_1_, p_74891_2_, p_74891_3_, this.boundingBox.minX + p_74891_5_, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());

                    case EAST:
                        return StructureVillagePieces.func_176066_d(p_74891_1_, p_74891_2_, p_74891_3_, this.boundingBox.minX + p_74891_5_, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                }
            }
            else
            {
                return null;
            }
        }

        @Nullable
        protected StructureComponent func_74894_b(StructureVillagePieces.Start p_74894_1_, List<StructureComponent> p_74894_2_, Random p_74894_3_, int p_74894_4_, int p_74894_5_)
        {
            EnumFacing enumfacing = this.getCoordBaseMode();

            if (enumfacing != null)
            {
                switch (enumfacing)
                {
                    case NORTH:
                    default:
                        return StructureVillagePieces.func_176066_d(p_74894_1_, p_74894_2_, p_74894_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74894_4_, this.boundingBox.minZ + p_74894_5_, EnumFacing.EAST, this.getComponentType());

                    case SOUTH:
                        return StructureVillagePieces.func_176066_d(p_74894_1_, p_74894_2_, p_74894_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74894_4_, this.boundingBox.minZ + p_74894_5_, EnumFacing.EAST, this.getComponentType());

                    case WEST:
                        return StructureVillagePieces.func_176066_d(p_74894_1_, p_74894_2_, p_74894_3_, this.boundingBox.minX + p_74894_5_, this.boundingBox.minY + p_74894_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());

                    case EAST:
                        return StructureVillagePieces.func_176066_d(p_74894_1_, p_74894_2_, p_74894_3_, this.boundingBox.minX + p_74894_5_, this.boundingBox.minY + p_74894_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                }
            }
            else
            {
                return null;
            }
        }

        protected int func_74889_b(World p_74889_1_, StructureBoundingBox p_74889_2_)
        {
            int i = 0;
            int j = 0;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k)
            {
                for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l)
                {
                    blockpos$mutableblockpos.setPos(l, 64, k);

                    if (p_74889_2_.isVecInside(blockpos$mutableblockpos))
                    {
                        i += Math.max(p_74889_1_.func_175672_r(blockpos$mutableblockpos).getY(), p_74889_1_.dimension.func_76557_i() - 1);
                        ++j;
                    }
                }
            }

            if (j == 0)
            {
                return -1;
            }
            else
            {
                return i / j;
            }
        }

        protected static boolean func_74895_a(StructureBoundingBox p_74895_0_)
        {
            return p_74895_0_ != null && p_74895_0_.minY > 10;
        }

        protected void func_74893_a(World p_74893_1_, StructureBoundingBox p_74893_2_, int p_74893_3_, int p_74893_4_, int p_74893_5_, int p_74893_6_)
        {
            if (this.field_74896_a < p_74893_6_)
            {
                for (int i = this.field_74896_a; i < p_74893_6_; ++i)
                {
                    int j = this.getXWithOffset(p_74893_3_ + i, p_74893_5_);
                    int k = this.getYWithOffset(p_74893_4_);
                    int l = this.getZWithOffset(p_74893_3_ + i, p_74893_5_);

                    if (!p_74893_2_.isVecInside(new BlockPos(j, k, l)))
                    {
                        break;
                    }

                    ++this.field_74896_a;

                    if (this.field_189929_i)
                    {
                        EntityZombieVillager entityzombievillager = new EntityZombieVillager(p_74893_1_);
                        entityzombievillager.setLocationAndAngles((double)j + 0.5D, (double)k, (double)l + 0.5D, 0.0F, 0.0F);
                        entityzombievillager.func_180482_a(p_74893_1_.getDifficultyForLocation(new BlockPos(entityzombievillager)), (IEntityLivingData)null);
                        entityzombievillager.func_190733_a(this.func_180779_c(i, 0));
                        entityzombievillager.enablePersistence();
                        p_74893_1_.addEntity0(entityzombievillager);
                    }
                    else
                    {
                        EntityVillager entityvillager = new EntityVillager(p_74893_1_);
                        entityvillager.setLocationAndAngles((double)j + 0.5D, (double)k, (double)l + 0.5D, 0.0F, 0.0F);
                        entityvillager.func_70938_b(this.func_180779_c(i, p_74893_1_.rand.nextInt(6)));
                        entityvillager.func_190672_a(p_74893_1_.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
                        p_74893_1_.addEntity0(entityvillager);
                    }
                }
            }
        }

        protected int func_180779_c(int p_180779_1_, int p_180779_2_)
        {
            return p_180779_2_;
        }

        protected IBlockState func_175847_a(IBlockState p_175847_1_)
        {
            if (this.field_189928_h == 1)
            {
                if (p_175847_1_.getBlock() == Blocks.field_150364_r || p_175847_1_.getBlock() == Blocks.field_150363_s)
                {
                    return Blocks.SANDSTONE.getDefaultState();
                }

                if (p_175847_1_.getBlock() == Blocks.COBBLESTONE)
                {
                    return Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.DEFAULT.func_176675_a());
                }

                if (p_175847_1_.getBlock() == Blocks.field_150344_f)
                {
                    return Blocks.SANDSTONE.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a());
                }

                if (p_175847_1_.getBlock() == Blocks.OAK_STAIRS)
                {
                    return Blocks.SANDSTONE_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, p_175847_1_.get(BlockStairs.FACING));
                }

                if (p_175847_1_.getBlock() == Blocks.field_150446_ar)
                {
                    return Blocks.SANDSTONE_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, p_175847_1_.get(BlockStairs.FACING));
                }

                if (p_175847_1_.getBlock() == Blocks.GRAVEL)
                {
                    return Blocks.SANDSTONE.getDefaultState();
                }
            }
            else if (this.field_189928_h == 3)
            {
                if (p_175847_1_.getBlock() == Blocks.field_150364_r || p_175847_1_.getBlock() == Blocks.field_150363_s)
                {
                    return Blocks.field_150364_r.getDefaultState().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.SPRUCE).func_177226_a(BlockLog.field_176299_a, p_175847_1_.get(BlockLog.field_176299_a));
                }

                if (p_175847_1_.getBlock() == Blocks.field_150344_f)
                {
                    return Blocks.field_150344_f.getDefaultState().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.SPRUCE);
                }

                if (p_175847_1_.getBlock() == Blocks.OAK_STAIRS)
                {
                    return Blocks.SPRUCE_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, p_175847_1_.get(BlockStairs.FACING));
                }

                if (p_175847_1_.getBlock() == Blocks.OAK_FENCE)
                {
                    return Blocks.SPRUCE_FENCE.getDefaultState();
                }
            }
            else if (this.field_189928_h == 2)
            {
                if (p_175847_1_.getBlock() == Blocks.field_150364_r || p_175847_1_.getBlock() == Blocks.field_150363_s)
                {
                    return Blocks.field_150363_s.getDefaultState().func_177226_a(BlockNewLog.field_176300_b, BlockPlanks.EnumType.ACACIA).func_177226_a(BlockLog.field_176299_a, p_175847_1_.get(BlockLog.field_176299_a));
                }

                if (p_175847_1_.getBlock() == Blocks.field_150344_f)
                {
                    return Blocks.field_150344_f.getDefaultState().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.ACACIA);
                }

                if (p_175847_1_.getBlock() == Blocks.OAK_STAIRS)
                {
                    return Blocks.ACACIA_STAIRS.getDefaultState().func_177226_a(BlockStairs.FACING, p_175847_1_.get(BlockStairs.FACING));
                }

                if (p_175847_1_.getBlock() == Blocks.COBBLESTONE)
                {
                    return Blocks.field_150363_s.getDefaultState().func_177226_a(BlockNewLog.field_176300_b, BlockPlanks.EnumType.ACACIA).func_177226_a(BlockLog.field_176299_a, BlockLog.EnumAxis.Y);
                }

                if (p_175847_1_.getBlock() == Blocks.OAK_FENCE)
                {
                    return Blocks.ACACIA_FENCE.getDefaultState();
                }
            }

            return p_175847_1_;
        }

        protected BlockDoor func_189925_i()
        {
            switch (this.field_189928_h)
            {
                case 2:
                    return Blocks.ACACIA_DOOR;

                case 3:
                    return Blocks.SPRUCE_DOOR;

                default:
                    return Blocks.OAK_DOOR;
            }
        }

        protected void func_189927_a(World p_189927_1_, StructureBoundingBox p_189927_2_, Random p_189927_3_, int p_189927_4_, int p_189927_5_, int p_189927_6_, EnumFacing p_189927_7_)
        {
            if (!this.field_189929_i)
            {
                this.func_189915_a(p_189927_1_, p_189927_2_, p_189927_3_, p_189927_4_, p_189927_5_, p_189927_6_, EnumFacing.NORTH, this.func_189925_i());
            }
        }

        protected void func_189926_a(World p_189926_1_, EnumFacing p_189926_2_, int p_189926_3_, int p_189926_4_, int p_189926_5_, StructureBoundingBox p_189926_6_)
        {
            if (!this.field_189929_i)
            {
                this.setBlockState(p_189926_1_, Blocks.TORCH.getDefaultState().func_177226_a(BlockTorch.field_176596_a, p_189926_2_), p_189926_3_, p_189926_4_, p_189926_5_, p_189926_6_);
            }
        }

        protected void replaceAirAndLiquidDownwards(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
        {
            IBlockState iblockstate = this.func_175847_a(blockstateIn);
            super.replaceAirAndLiquidDownwards(worldIn, iblockstate, x, y, z, boundingboxIn);
        }

        protected void func_189924_a(int p_189924_1_)
        {
            this.field_189928_h = p_189924_1_;
        }
    }

    public static class Well extends StructureVillagePieces.Village
    {
        public Well()
        {
        }

        public Well(StructureVillagePieces.Start p_i2109_1_, int p_i2109_2_, Random p_i2109_3_, int p_i2109_4_, int p_i2109_5_)
        {
            super(p_i2109_1_, p_i2109_2_);
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(p_i2109_3_));

            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z)
            {
                this.boundingBox = new StructureBoundingBox(p_i2109_4_, 64, p_i2109_5_, p_i2109_4_ + 6 - 1, 78, p_i2109_5_ + 6 - 1);
            }
            else
            {
                this.boundingBox = new StructureBoundingBox(p_i2109_4_, 64, p_i2109_5_, p_i2109_4_ + 6 - 1, 78, p_i2109_5_ + 6 - 1);
            }
        }

        public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
        {
            StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.WEST, this.getComponentType());
            StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.EAST, this.getComponentType());
            StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
            StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_143015_k < 0)
            {
                this.field_143015_k = this.func_74889_b(p_74875_1_, p_74875_3_);

                if (this.field_143015_k < 0)
                {
                    return true;
                }

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 3, 0);
            }

            IBlockState iblockstate = this.func_175847_a(Blocks.COBBLESTONE.getDefaultState());
            IBlockState iblockstate1 = this.func_175847_a(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 1, 4, 12, 4, iblockstate, Blocks.field_150358_i.getDefaultState(), false);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 2, 12, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 3, 12, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 2, 12, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 3, 12, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 1, 13, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 1, 14, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 4, 13, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 4, 14, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 1, 13, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 1, 14, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 4, 13, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate1, 4, 14, 4, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 15, 1, 4, 15, 4, iblockstate, iblockstate, false);

            for (int i = 0; i <= 5; ++i)
            {
                for (int j = 0; j <= 5; ++j)
                {
                    if (j == 0 || j == 5 || i == 0 || i == 5)
                    {
                        this.setBlockState(p_74875_1_, iblockstate, j, 11, i, p_74875_3_);
                        this.func_74871_b(p_74875_1_, j, 12, i, p_74875_3_);
                    }
                }
            }

            return true;
        }
    }

    public static class WoodHut extends StructureVillagePieces.Village
    {
        private boolean field_74909_b;
        private int field_74910_c;

        public WoodHut()
        {
        }

        public WoodHut(StructureVillagePieces.Start p_i45565_1_, int p_i45565_2_, Random p_i45565_3_, StructureBoundingBox p_i45565_4_, EnumFacing p_i45565_5_)
        {
            super(p_i45565_1_, p_i45565_2_);
            this.setCoordBaseMode(p_i45565_5_);
            this.boundingBox = p_i45565_4_;
            this.field_74909_b = p_i45565_3_.nextBoolean();
            this.field_74910_c = p_i45565_3_.nextInt(3);
        }

        protected void func_143012_a(NBTTagCompound p_143012_1_)
        {
            super.func_143012_a(p_143012_1_);
            p_143012_1_.putInt("T", this.field_74910_c);
            p_143012_1_.putBoolean("C", this.field_74909_b);
        }

        protected void readAdditional(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
        {
            super.readAdditional(tagCompound, p_143011_2_);
            this.field_74910_c = tagCompound.getInt("T");
            this.field_74909_b = tagCompound.getBoolean("C");
        }

        public static StructureVillagePieces.WoodHut func_175853_a(StructureVillagePieces.Start p_175853_0_, List<StructureComponent> p_175853_1_, Random p_175853_2_, int p_175853_3_, int p_175853_4_, int p_175853_5_, EnumFacing p_175853_6_, int p_175853_7_)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175853_3_, p_175853_4_, p_175853_5_, 0, 0, 0, 4, 6, 5, p_175853_6_);
            return func_74895_a(structureboundingbox) && StructureComponent.findIntersecting(p_175853_1_, structureboundingbox) == null ? new StructureVillagePieces.WoodHut(p_175853_0_, p_175853_7_, p_175853_2_, structureboundingbox, p_175853_6_) : null;
        }

        public boolean func_74875_a(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_)
        {
            if (this.field_143015_k < 0)
            {
                this.field_143015_k = this.func_74889_b(p_74875_1_, p_74875_3_);

                if (this.field_143015_k < 0)
                {
                    return true;
                }

                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
            }

            IBlockState iblockstate = this.func_175847_a(Blocks.COBBLESTONE.getDefaultState());
            IBlockState iblockstate1 = this.func_175847_a(Blocks.field_150344_f.getDefaultState());
            IBlockState iblockstate2 = this.func_175847_a(Blocks.field_150446_ar.getDefaultState().func_177226_a(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate3 = this.func_175847_a(Blocks.field_150364_r.getDefaultState());
            IBlockState iblockstate4 = this.func_175847_a(Blocks.OAK_FENCE.getDefaultState());
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 1, 3, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 3, 0, 4, iblockstate, iblockstate, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 0, 1, 2, 0, 3, Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState(), false);

            if (this.field_74909_b)
            {
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 4, 1, 2, 4, 3, iblockstate3, iblockstate3, false);
            }
            else
            {
                this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 5, 1, 2, 5, 3, iblockstate3, iblockstate3, false);
            }

            this.setBlockState(p_74875_1_, iblockstate3, 1, 4, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, 2, 4, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, 1, 4, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, 2, 4, 4, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, 0, 4, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, 0, 4, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, 0, 4, 3, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, 3, 4, 1, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, 3, 4, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, iblockstate3, 3, 4, 3, p_74875_3_);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 1, 0, 0, 3, 0, iblockstate3, iblockstate3, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 3, 1, 0, 3, 3, 0, iblockstate3, iblockstate3, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 1, 4, 0, 3, 4, iblockstate3, iblockstate3, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 3, 1, 4, 3, 3, 4, iblockstate3, iblockstate3, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 0, 1, 1, 0, 3, 3, iblockstate1, iblockstate1, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 3, 1, 1, 3, 3, 3, iblockstate1, iblockstate1, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 0, 2, 3, 0, iblockstate1, iblockstate1, false);
            this.fillWithBlocks(p_74875_1_, p_74875_3_, 1, 1, 4, 2, 3, 4, iblockstate1, iblockstate1, false);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 2, p_74875_3_);

            if (this.field_74910_c > 0)
            {
                this.setBlockState(p_74875_1_, iblockstate4, this.field_74910_c, 1, 3, p_74875_3_);
                this.setBlockState(p_74875_1_, Blocks.field_150452_aw.getDefaultState(), this.field_74910_c, 2, 3, p_74875_3_);
            }

            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 1, 1, 0, p_74875_3_);
            this.setBlockState(p_74875_1_, Blocks.AIR.getDefaultState(), 1, 2, 0, p_74875_3_);
            this.func_189927_a(p_74875_1_, p_74875_3_, p_74875_2_, 1, 1, 0, EnumFacing.NORTH);

            if (this.getBlockStateFromPos(p_74875_1_, 1, 0, -1, p_74875_3_).getMaterial() == Material.AIR && this.getBlockStateFromPos(p_74875_1_, 1, -1, -1, p_74875_3_).getMaterial() != Material.AIR)
            {
                this.setBlockState(p_74875_1_, iblockstate2, 1, 0, -1, p_74875_3_);

                if (this.getBlockStateFromPos(p_74875_1_, 1, -1, -1, p_74875_3_).getBlock() == Blocks.GRASS_PATH)
                {
                    this.setBlockState(p_74875_1_, Blocks.GRASS.getDefaultState(), 1, -1, -1, p_74875_3_);
                }
            }

            for (int i = 0; i < 5; ++i)
            {
                for (int j = 0; j < 4; ++j)
                {
                    this.func_74871_b(p_74875_1_, j, 6, i, p_74875_3_);
                    this.replaceAirAndLiquidDownwards(p_74875_1_, iblockstate, j, -1, i, p_74875_3_);
                }
            }

            this.func_74893_a(p_74875_1_, p_74875_3_, 1, 1, 2, 1);
            return true;
        }
    }
}

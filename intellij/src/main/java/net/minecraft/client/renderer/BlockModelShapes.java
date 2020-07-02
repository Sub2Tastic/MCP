package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class BlockModelShapes
{
    private final Map<IBlockState, IBakedModel> bakedModelStore = Maps.<IBlockState, IBakedModel>newIdentityHashMap();
    private final BlockStateMapper field_178127_b = new BlockStateMapper();
    private final ModelManager modelManager;

    public BlockModelShapes(ModelManager manager)
    {
        this.modelManager = manager;
        this.func_178119_d();
    }

    public BlockStateMapper func_178120_a()
    {
        return this.field_178127_b;
    }

    public TextureAtlasSprite getTexture(IBlockState state)
    {
        Block block = state.getBlock();
        IBakedModel ibakedmodel = this.getModel(state);

        if (ibakedmodel == null || ibakedmodel == this.modelManager.getMissingModel())
        {
            if (block == Blocks.field_150444_as || block == Blocks.field_150472_an || block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST || block == Blocks.field_180393_cK || block == Blocks.field_180394_cL || block == Blocks.field_150324_C)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/planks_oak");
            }

            if (block == Blocks.ENDER_CHEST)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/obsidian");
            }

            if (block == Blocks.field_150356_k || block == Blocks.LAVA)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/lava_still");
            }

            if (block == Blocks.field_150358_i || block == Blocks.WATER)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/water_still");
            }

            if (block == Blocks.field_150465_bP)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/soul_sand");
            }

            if (block == Blocks.BARRIER)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:items/barrier");
            }

            if (block == Blocks.STRUCTURE_VOID)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:items/structure_void");
            }

            if (block == Blocks.WHITE_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_white");
            }

            if (block == Blocks.ORANGE_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_orange");
            }

            if (block == Blocks.MAGENTA_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_magenta");
            }

            if (block == Blocks.LIGHT_BLUE_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_light_blue");
            }

            if (block == Blocks.YELLOW_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_yellow");
            }

            if (block == Blocks.LIME_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_lime");
            }

            if (block == Blocks.PINK_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_pink");
            }

            if (block == Blocks.GRAY_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_gray");
            }

            if (block == Blocks.field_190985_dt)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_silver");
            }

            if (block == Blocks.CYAN_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_cyan");
            }

            if (block == Blocks.PURPLE_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_purple");
            }

            if (block == Blocks.BLUE_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_blue");
            }

            if (block == Blocks.BROWN_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_brown");
            }

            if (block == Blocks.GREEN_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_green");
            }

            if (block == Blocks.RED_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_red");
            }

            if (block == Blocks.BLACK_SHULKER_BOX)
            {
                return this.modelManager.func_174952_b().func_110572_b("minecraft:blocks/shulker_top_black");
            }
        }

        if (ibakedmodel == null)
        {
            ibakedmodel = this.modelManager.getMissingModel();
        }

        return ibakedmodel.getParticleTexture();
    }

    public IBakedModel getModel(IBlockState state)
    {
        IBakedModel ibakedmodel = this.bakedModelStore.get(state);

        if (ibakedmodel == null)
        {
            ibakedmodel = this.modelManager.getMissingModel();
        }

        return ibakedmodel;
    }

    public ModelManager getModelManager()
    {
        return this.modelManager;
    }

    public void reloadModels()
    {
        this.bakedModelStore.clear();

        for (Entry<IBlockState, ModelResourceLocation> entry : this.field_178127_b.func_178446_a().entrySet())
        {
            this.bakedModelStore.put(entry.getKey(), this.modelManager.getModel(entry.getValue()));
        }
    }

    public void func_178121_a(Block p_178121_1_, IStateMapper p_178121_2_)
    {
        this.field_178127_b.func_178447_a(p_178121_1_, p_178121_2_);
    }

    public void func_178123_a(Block... p_178123_1_)
    {
        this.field_178127_b.func_178448_a(p_178123_1_);
    }

    private void func_178119_d()
    {
        this.func_178123_a(Blocks.AIR, Blocks.field_150358_i, Blocks.WATER, Blocks.field_150356_k, Blocks.LAVA, Blocks.field_180384_M, Blocks.CHEST, Blocks.ENDER_CHEST, Blocks.TRAPPED_CHEST, Blocks.field_150472_an, Blocks.field_150465_bP, Blocks.END_PORTAL, Blocks.BARRIER, Blocks.field_150444_as, Blocks.field_180394_cL, Blocks.field_180393_cK, Blocks.END_GATEWAY, Blocks.STRUCTURE_VOID, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.field_190985_dt, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.field_150324_C);
        this.func_178121_a(Blocks.STONE, (new StateMap.Builder()).func_178440_a(BlockStone.field_176247_a).func_178441_a());
        this.func_178121_a(Blocks.PRISMARINE, (new StateMap.Builder()).func_178440_a(BlockPrismarine.field_176332_a).func_178441_a());
        this.func_178121_a(Blocks.field_150362_t, (new StateMap.Builder()).func_178440_a(BlockOldLeaf.field_176239_P).func_178439_a("_leaves").func_178442_a(BlockLeaves.field_176236_b, BlockLeaves.field_176237_a).func_178441_a());
        this.func_178121_a(Blocks.field_150361_u, (new StateMap.Builder()).func_178440_a(BlockNewLeaf.field_176240_P).func_178439_a("_leaves").func_178442_a(BlockLeaves.field_176236_b, BlockLeaves.field_176237_a).func_178441_a());
        this.func_178121_a(Blocks.CACTUS, (new StateMap.Builder()).func_178442_a(BlockCactus.AGE).func_178441_a());
        this.func_178121_a(Blocks.field_150436_aH, (new StateMap.Builder()).func_178442_a(BlockReed.AGE).func_178441_a());
        this.func_178121_a(Blocks.JUKEBOX, (new StateMap.Builder()).func_178442_a(BlockJukebox.HAS_RECORD).func_178441_a());
        this.func_178121_a(Blocks.COBBLESTONE_WALL, (new StateMap.Builder()).func_178440_a(BlockWall.field_176255_P).func_178439_a("_wall").func_178441_a());
        this.func_178121_a(Blocks.field_150398_cm, (new StateMap.Builder()).func_178440_a(BlockDoublePlant.field_176493_a).func_178442_a(BlockDoublePlant.field_181084_N).func_178441_a());
        this.func_178121_a(Blocks.OAK_FENCE_GATE, (new StateMap.Builder()).func_178442_a(BlockFenceGate.POWERED).func_178441_a());
        this.func_178121_a(Blocks.SPRUCE_FENCE_GATE, (new StateMap.Builder()).func_178442_a(BlockFenceGate.POWERED).func_178441_a());
        this.func_178121_a(Blocks.BIRCH_FENCE_GATE, (new StateMap.Builder()).func_178442_a(BlockFenceGate.POWERED).func_178441_a());
        this.func_178121_a(Blocks.JUNGLE_FENCE_GATE, (new StateMap.Builder()).func_178442_a(BlockFenceGate.POWERED).func_178441_a());
        this.func_178121_a(Blocks.DARK_OAK_FENCE_GATE, (new StateMap.Builder()).func_178442_a(BlockFenceGate.POWERED).func_178441_a());
        this.func_178121_a(Blocks.ACACIA_FENCE_GATE, (new StateMap.Builder()).func_178442_a(BlockFenceGate.POWERED).func_178441_a());
        this.func_178121_a(Blocks.TRIPWIRE, (new StateMap.Builder()).func_178442_a(BlockTripWire.DISARMED, BlockTripWire.POWERED).func_178441_a());
        this.func_178121_a(Blocks.field_150373_bw, (new StateMap.Builder()).func_178440_a(BlockPlanks.field_176383_a).func_178439_a("_double_slab").func_178441_a());
        this.func_178121_a(Blocks.field_150376_bx, (new StateMap.Builder()).func_178440_a(BlockPlanks.field_176383_a).func_178439_a("_slab").func_178441_a());
        this.func_178121_a(Blocks.TNT, (new StateMap.Builder()).func_178442_a(BlockTNT.field_176246_a).func_178441_a());
        this.func_178121_a(Blocks.FIRE, (new StateMap.Builder()).func_178442_a(BlockFire.AGE).func_178441_a());
        this.func_178121_a(Blocks.REDSTONE_WIRE, (new StateMap.Builder()).func_178442_a(BlockRedstoneWire.POWER).func_178441_a());
        this.func_178121_a(Blocks.OAK_DOOR, (new StateMap.Builder()).func_178442_a(BlockDoor.POWERED).func_178441_a());
        this.func_178121_a(Blocks.SPRUCE_DOOR, (new StateMap.Builder()).func_178442_a(BlockDoor.POWERED).func_178441_a());
        this.func_178121_a(Blocks.BIRCH_DOOR, (new StateMap.Builder()).func_178442_a(BlockDoor.POWERED).func_178441_a());
        this.func_178121_a(Blocks.JUNGLE_DOOR, (new StateMap.Builder()).func_178442_a(BlockDoor.POWERED).func_178441_a());
        this.func_178121_a(Blocks.ACACIA_DOOR, (new StateMap.Builder()).func_178442_a(BlockDoor.POWERED).func_178441_a());
        this.func_178121_a(Blocks.DARK_OAK_DOOR, (new StateMap.Builder()).func_178442_a(BlockDoor.POWERED).func_178441_a());
        this.func_178121_a(Blocks.IRON_DOOR, (new StateMap.Builder()).func_178442_a(BlockDoor.POWERED).func_178441_a());
        this.func_178121_a(Blocks.field_150325_L, (new StateMap.Builder()).func_178440_a(BlockColored.field_176581_a).func_178439_a("_wool").func_178441_a());
        this.func_178121_a(Blocks.field_150404_cg, (new StateMap.Builder()).func_178440_a(BlockColored.field_176581_a).func_178439_a("_carpet").func_178441_a());
        this.func_178121_a(Blocks.field_150406_ce, (new StateMap.Builder()).func_178440_a(BlockColored.field_176581_a).func_178439_a("_stained_hardened_clay").func_178441_a());
        this.func_178121_a(Blocks.field_150397_co, (new StateMap.Builder()).func_178440_a(BlockColored.field_176581_a).func_178439_a("_stained_glass_pane").func_178441_a());
        this.func_178121_a(Blocks.field_150399_cn, (new StateMap.Builder()).func_178440_a(BlockColored.field_176581_a).func_178439_a("_stained_glass").func_178441_a());
        this.func_178121_a(Blocks.SANDSTONE, (new StateMap.Builder()).func_178440_a(BlockSandStone.field_176297_a).func_178441_a());
        this.func_178121_a(Blocks.RED_SANDSTONE, (new StateMap.Builder()).func_178440_a(BlockRedSandstone.field_176336_a).func_178441_a());
        this.func_178121_a(Blocks.field_150329_H, (new StateMap.Builder()).func_178440_a(BlockTallGrass.field_176497_a).func_178441_a());
        this.func_178121_a(Blocks.field_150327_N, (new StateMap.Builder()).func_178440_a(Blocks.field_150327_N.func_176494_l()).func_178441_a());
        this.func_178121_a(Blocks.field_150328_O, (new StateMap.Builder()).func_178440_a(Blocks.field_150328_O.func_176494_l()).func_178441_a());
        this.func_178121_a(Blocks.STONE_SLAB, (new StateMap.Builder()).func_178440_a(BlockStoneSlab.field_176556_M).func_178439_a("_slab").func_178441_a());
        this.func_178121_a(Blocks.field_180389_cP, (new StateMap.Builder()).func_178440_a(BlockStoneSlabNew.field_176559_M).func_178439_a("_slab").func_178441_a());
        this.func_178121_a(Blocks.field_150418_aU, (new StateMap.Builder()).func_178440_a(BlockSilverfish.field_176378_a).func_178439_a("_monster_egg").func_178441_a());
        this.func_178121_a(Blocks.field_150417_aV, (new StateMap.Builder()).func_178440_a(BlockStoneBrick.field_176249_a).func_178441_a());
        this.func_178121_a(Blocks.DISPENSER, (new StateMap.Builder()).func_178442_a(BlockDispenser.TRIGGERED).func_178441_a());
        this.func_178121_a(Blocks.DROPPER, (new StateMap.Builder()).func_178442_a(BlockDropper.TRIGGERED).func_178441_a());
        this.func_178121_a(Blocks.field_150364_r, (new StateMap.Builder()).func_178440_a(BlockOldLog.field_176301_b).func_178439_a("_log").func_178441_a());
        this.func_178121_a(Blocks.field_150363_s, (new StateMap.Builder()).func_178440_a(BlockNewLog.field_176300_b).func_178439_a("_log").func_178441_a());
        this.func_178121_a(Blocks.field_150344_f, (new StateMap.Builder()).func_178440_a(BlockPlanks.field_176383_a).func_178439_a("_planks").func_178441_a());
        this.func_178121_a(Blocks.field_150345_g, (new StateMap.Builder()).func_178440_a(BlockSapling.field_176480_a).func_178439_a("_sapling").func_178441_a());
        this.func_178121_a(Blocks.SAND, (new StateMap.Builder()).func_178440_a(BlockSand.field_176504_a).func_178441_a());
        this.func_178121_a(Blocks.HOPPER, (new StateMap.Builder()).func_178442_a(BlockHopper.ENABLED).func_178441_a());
        this.func_178121_a(Blocks.FLOWER_POT, (new StateMap.Builder()).func_178442_a(BlockFlowerPot.field_176444_a).func_178441_a());
        this.func_178121_a(Blocks.field_192443_dR, (new StateMap.Builder()).func_178440_a(BlockColored.field_176581_a).func_178439_a("_concrete").func_178441_a());
        this.func_178121_a(Blocks.field_192444_dS, (new StateMap.Builder()).func_178440_a(BlockColored.field_176581_a).func_178439_a("_concrete_powder").func_178441_a());
        this.func_178121_a(Blocks.QUARTZ_BLOCK, new StateMapperBase()
        {
            protected ModelResourceLocation func_178132_a(IBlockState p_178132_1_)
            {
                BlockQuartz.EnumType blockquartz$enumtype = (BlockQuartz.EnumType)p_178132_1_.get(BlockQuartz.field_176335_a);

                switch (blockquartz$enumtype)
                {
                    case DEFAULT:
                    default:
                        return new ModelResourceLocation("quartz_block", "normal");

                    case CHISELED:
                        return new ModelResourceLocation("chiseled_quartz_block", "normal");

                    case LINES_Y:
                        return new ModelResourceLocation("quartz_column", "axis=y");

                    case LINES_X:
                        return new ModelResourceLocation("quartz_column", "axis=x");

                    case LINES_Z:
                        return new ModelResourceLocation("quartz_column", "axis=z");
                }
            }
        });
        this.func_178121_a(Blocks.field_150330_I, new StateMapperBase()
        {
            protected ModelResourceLocation func_178132_a(IBlockState p_178132_1_)
            {
                return new ModelResourceLocation("dead_bush", "normal");
            }
        });
        this.func_178121_a(Blocks.PUMPKIN_STEM, new StateMapperBase()
        {
            protected ModelResourceLocation func_178132_a(IBlockState p_178132_1_)
            {
                Map < IProperty<?>, Comparable<? >> map = Maps. < IProperty<?>, Comparable<? >> newLinkedHashMap(p_178132_1_.func_177228_b());

                if (p_178132_1_.get(BlockStem.field_176483_b) != EnumFacing.UP)
                {
                    map.remove(BlockStem.AGE);
                }

                return new ModelResourceLocation(Block.field_149771_c.getKey(p_178132_1_.getBlock()), this.func_178131_a(map));
            }
        });
        this.func_178121_a(Blocks.MELON_STEM, new StateMapperBase()
        {
            protected ModelResourceLocation func_178132_a(IBlockState p_178132_1_)
            {
                Map < IProperty<?>, Comparable<? >> map = Maps. < IProperty<?>, Comparable<? >> newLinkedHashMap(p_178132_1_.func_177228_b());

                if (p_178132_1_.get(BlockStem.field_176483_b) != EnumFacing.UP)
                {
                    map.remove(BlockStem.AGE);
                }

                return new ModelResourceLocation(Block.field_149771_c.getKey(p_178132_1_.getBlock()), this.func_178131_a(map));
            }
        });
        this.func_178121_a(Blocks.DIRT, new StateMapperBase()
        {
            protected ModelResourceLocation func_178132_a(IBlockState p_178132_1_)
            {
                Map < IProperty<?>, Comparable<? >> map = Maps. < IProperty<?>, Comparable<? >> newLinkedHashMap(p_178132_1_.func_177228_b());
                String s = BlockDirt.field_176386_a.getName((BlockDirt.DirtType)map.remove(BlockDirt.field_176386_a));

                if (BlockDirt.DirtType.PODZOL != p_178132_1_.get(BlockDirt.field_176386_a))
                {
                    map.remove(BlockDirt.field_176385_b);
                }

                return new ModelResourceLocation(s, this.func_178131_a(map));
            }
        });
        this.func_178121_a(Blocks.field_150334_T, new StateMapperBase()
        {
            protected ModelResourceLocation func_178132_a(IBlockState p_178132_1_)
            {
                Map < IProperty<?>, Comparable<? >> map = Maps. < IProperty<?>, Comparable<? >> newLinkedHashMap(p_178132_1_.func_177228_b());
                String s = BlockStoneSlab.field_176556_M.getName((BlockStoneSlab.EnumType)map.remove(BlockStoneSlab.field_176556_M));
                map.remove(BlockStoneSlab.field_176555_b);
                String s1 = ((Boolean)p_178132_1_.get(BlockStoneSlab.field_176555_b)).booleanValue() ? "all" : "normal";
                return new ModelResourceLocation(s + "_double_slab", s1);
            }
        });
        this.func_178121_a(Blocks.field_180388_cO, new StateMapperBase()
        {
            protected ModelResourceLocation func_178132_a(IBlockState p_178132_1_)
            {
                Map < IProperty<?>, Comparable<? >> map = Maps. < IProperty<?>, Comparable<? >> newLinkedHashMap(p_178132_1_.func_177228_b());
                String s = BlockStoneSlabNew.field_176559_M.getName((BlockStoneSlabNew.EnumType)map.remove(BlockStoneSlabNew.field_176559_M));
                map.remove(BlockStoneSlab.field_176555_b);
                String s1 = ((Boolean)p_178132_1_.get(BlockStoneSlabNew.field_176558_b)).booleanValue() ? "all" : "normal";
                return new ModelResourceLocation(s + "_double_slab", s1);
            }
        });
    }
}

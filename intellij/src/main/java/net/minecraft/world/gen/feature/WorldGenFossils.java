package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class WorldGenFossils extends WorldGenerator
{
    private static final ResourceLocation STRUCTURE_SPINE_01 = new ResourceLocation("fossils/fossil_spine_01");
    private static final ResourceLocation STRUCTURE_SPINE_02 = new ResourceLocation("fossils/fossil_spine_02");
    private static final ResourceLocation STRUCTURE_SPINE_03 = new ResourceLocation("fossils/fossil_spine_03");
    private static final ResourceLocation STRUCTURE_SPINE_04 = new ResourceLocation("fossils/fossil_spine_04");
    private static final ResourceLocation STRUCTURE_SPINE_01_COAL = new ResourceLocation("fossils/fossil_spine_01_coal");
    private static final ResourceLocation STRUCTURE_SPINE_02_COAL = new ResourceLocation("fossils/fossil_spine_02_coal");
    private static final ResourceLocation STRUCTURE_SPINE_03_COAL = new ResourceLocation("fossils/fossil_spine_03_coal");
    private static final ResourceLocation STRUCTURE_SPINE_04_COAL = new ResourceLocation("fossils/fossil_spine_04_coal");
    private static final ResourceLocation STRUCTURE_SKULL_01 = new ResourceLocation("fossils/fossil_skull_01");
    private static final ResourceLocation STRUCTURE_SKULL_02 = new ResourceLocation("fossils/fossil_skull_02");
    private static final ResourceLocation STRUCTURE_SKULL_03 = new ResourceLocation("fossils/fossil_skull_03");
    private static final ResourceLocation STRUCTURE_SKULL_04 = new ResourceLocation("fossils/fossil_skull_04");
    private static final ResourceLocation STRUCTURE_SKULL_01_COAL = new ResourceLocation("fossils/fossil_skull_01_coal");
    private static final ResourceLocation STRUCTURE_SKULL_02_COAL = new ResourceLocation("fossils/fossil_skull_02_coal");
    private static final ResourceLocation STRUCTURE_SKULL_03_COAL = new ResourceLocation("fossils/fossil_skull_03_coal");
    private static final ResourceLocation STRUCTURE_SKULL_04_COAL = new ResourceLocation("fossils/fossil_skull_04_coal");
    private static final ResourceLocation[] FOSSILS = new ResourceLocation[] {STRUCTURE_SPINE_01, STRUCTURE_SPINE_02, STRUCTURE_SPINE_03, STRUCTURE_SPINE_04, STRUCTURE_SKULL_01, STRUCTURE_SKULL_02, STRUCTURE_SKULL_03, STRUCTURE_SKULL_04};
    private static final ResourceLocation[] FOSSILS_COAL = new ResourceLocation[] {STRUCTURE_SPINE_01_COAL, STRUCTURE_SPINE_02_COAL, STRUCTURE_SPINE_03_COAL, STRUCTURE_SPINE_04_COAL, STRUCTURE_SKULL_01_COAL, STRUCTURE_SKULL_02_COAL, STRUCTURE_SKULL_03_COAL, STRUCTURE_SKULL_04_COAL};

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        Random random = p_180709_1_.getChunkAt(p_180709_3_).func_76617_a(987234911L);
        MinecraftServer minecraftserver = p_180709_1_.getServer();
        Rotation[] arotation = Rotation.values();
        Rotation rotation = arotation[random.nextInt(arotation.length)];
        int i = random.nextInt(FOSSILS.length);
        TemplateManager templatemanager = p_180709_1_.func_72860_G().getStructureTemplateManager();
        Template template = templatemanager.func_186237_a(minecraftserver, FOSSILS[i]);
        Template template1 = templatemanager.func_186237_a(minecraftserver, FOSSILS_COAL[i]);
        ChunkPos chunkpos = new ChunkPos(p_180709_3_);
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(chunkpos.getXStart(), 0, chunkpos.getZStart(), chunkpos.getXEnd(), 256, chunkpos.getZEnd());
        PlacementSettings placementsettings = (new PlacementSettings()).setRotation(rotation).setBoundingBox(structureboundingbox).setRandom(random);
        BlockPos blockpos = template.transformedSize(rotation);
        int j = random.nextInt(16 - blockpos.getX());
        int k = random.nextInt(16 - blockpos.getZ());
        int l = 256;

        for (int i1 = 0; i1 < blockpos.getX(); ++i1)
        {
            for (int j1 = 0; j1 < blockpos.getX(); ++j1)
            {
                l = Math.min(l, p_180709_1_.func_189649_b(p_180709_3_.getX() + i1 + j, p_180709_3_.getZ() + j1 + k));
            }
        }

        int k1 = Math.max(l - 15 - random.nextInt(10), 10);
        BlockPos blockpos1 = template.getZeroPositionWithTransform(p_180709_3_.add(j, k1, k), Mirror.NONE, rotation);
        placementsettings.func_189946_a(0.9F);
        template.addBlocksToWorld(p_180709_1_, blockpos1, placementsettings, 20);
        placementsettings.func_189946_a(0.1F);
        template1.addBlocksToWorld(p_180709_1_, blockpos1, placementsettings, 20);
        return true;
    }
}

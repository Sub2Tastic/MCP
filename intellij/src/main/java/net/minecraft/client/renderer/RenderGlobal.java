package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemRecord;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class RenderGlobal implements IWorldEventListener, IResourceManagerReloadListener
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation CLOUDS_TEXTURES = new ResourceLocation("textures/environment/clouds.png");
    private static final ResourceLocation END_SKY_TEXTURES = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation FORCEFIELD_TEXTURES = new ResourceLocation("textures/misc/forcefield.png");
    private final Minecraft mc;
    private final TextureManager textureManager;
    private final RenderManager renderManager;
    private WorldClient world;
    private Set<RenderChunk> chunksToUpdate = Sets.<RenderChunk>newLinkedHashSet();
    private List<RenderGlobal.ContainerLocalRenderInformation> renderInfos = Lists.<RenderGlobal.ContainerLocalRenderInformation>newArrayListWithCapacity(69696);
    private final Set<TileEntity> setTileEntities = Sets.<TileEntity>newHashSet();
    private ViewFrustum viewFrustum;
    private int field_72772_v = -1;
    private int field_72771_w = -1;
    private int field_72781_x = -1;
    private final VertexFormat skyVertexFormat;
    private VertexBuffer starVBO;
    private VertexBuffer skyVBO;
    private VertexBuffer sky2VBO;
    private int ticks;
    private final Map<Integer, DestroyBlockProgress> damagedBlocks = Maps.<Integer, DestroyBlockProgress>newHashMap();
    private final Map<BlockPos, ISound> mapSoundPositions = Maps.<BlockPos, ISound>newHashMap();
    private final TextureAtlasSprite[] field_94141_F = new TextureAtlasSprite[10];
    private Framebuffer entityOutlineFramebuffer;

    /** Stores the shader group for the entity_outline shader */
    private ShaderGroup entityOutlineShader;
    private double frustumUpdatePosX = Double.MIN_VALUE;
    private double frustumUpdatePosY = Double.MIN_VALUE;
    private double frustumUpdatePosZ = Double.MIN_VALUE;
    private int frustumUpdatePosChunkX = Integer.MIN_VALUE;
    private int frustumUpdatePosChunkY = Integer.MIN_VALUE;
    private int frustumUpdatePosChunkZ = Integer.MIN_VALUE;
    private double lastViewEntityX = Double.MIN_VALUE;
    private double lastViewEntityY = Double.MIN_VALUE;
    private double lastViewEntityZ = Double.MIN_VALUE;
    private double lastViewEntityPitch = Double.MIN_VALUE;
    private double lastViewEntityYaw = Double.MIN_VALUE;
    private ChunkRenderDispatcher renderDispatcher;
    private ChunkRenderContainer field_174996_N;
    private int renderDistanceChunks = -1;
    private int field_72740_G = 2;
    private int field_72748_H;
    private int countEntitiesRendered;
    private int countEntitiesHidden;
    private boolean debugFixTerrainFrustum;
    private ClippingHelper debugFixedClippingHelper;
    private final Vector4f[] debugTerrainMatrix = new Vector4f[8];
    private final Vector3d debugTerrainFrustumPosition = new Vector3d();
    private boolean field_175005_X;
    IRenderChunkFactory field_175007_a;
    private double prevRenderSortX;
    private double prevRenderSortY;
    private double prevRenderSortZ;
    private boolean displayListEntitiesDirty = true;
    private boolean field_184386_ad;
    private final Set<BlockPos> field_184387_ae = Sets.<BlockPos>newHashSet();

    public RenderGlobal(Minecraft p_i1249_1_)
    {
        this.mc = p_i1249_1_;
        this.renderManager = p_i1249_1_.getRenderManager();
        this.textureManager = p_i1249_1_.getTextureManager();
        this.textureManager.bindTexture(FORCEFIELD_TEXTURES);
        GlStateManager.func_187421_b(3553, 10242, 10497);
        GlStateManager.func_187421_b(3553, 10243, 10497);
        GlStateManager.func_179144_i(0);
        this.func_174971_n();
        this.field_175005_X = OpenGlHelper.func_176075_f();

        if (this.field_175005_X)
        {
            this.field_174996_N = new VboRenderList();
            this.field_175007_a = new VboChunkFactory();
        }
        else
        {
            this.field_174996_N = new RenderList();
            this.field_175007_a = new ListChunkFactory();
        }

        this.skyVertexFormat = new VertexFormat();
        this.skyVertexFormat.func_181721_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
        this.generateStars();
        this.generateSky();
        this.generateSky2();
    }

    public void func_110549_a(IResourceManager p_110549_1_)
    {
        this.func_174971_n();
    }

    private void func_174971_n()
    {
        TextureMap texturemap = this.mc.func_147117_R();

        for (int i = 0; i < this.field_94141_F.length; ++i)
        {
            this.field_94141_F[i] = texturemap.func_110572_b("minecraft:blocks/destroy_stage_" + i);
        }
    }

    /**
     * Creates the entity outline shader to be stored in RenderGlobal.entityOutlineShader
     */
    public void makeEntityOutlineShader()
    {
        if (OpenGlHelper.field_148824_g)
        {
            if (ShaderLinkHelper.func_148074_b() == null)
            {
                ShaderLinkHelper.func_148076_a();
            }

            ResourceLocation resourcelocation = new ResourceLocation("shaders/post/entity_outline.json");

            try
            {
                this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.func_110442_L(), this.mc.getFramebuffer(), resourcelocation);
                this.entityOutlineShader.createBindFramebuffers(this.mc.field_71443_c, this.mc.field_71440_d);
                this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
            }
            catch (IOException ioexception)
            {
                LOGGER.warn("Failed to load shader: {}", resourcelocation, ioexception);
                this.entityOutlineShader = null;
                this.entityOutlineFramebuffer = null;
            }
            catch (JsonSyntaxException jsonsyntaxexception)
            {
                LOGGER.warn("Failed to load shader: {}", resourcelocation, jsonsyntaxexception);
                this.entityOutlineShader = null;
                this.entityOutlineFramebuffer = null;
            }
        }
        else
        {
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
        }
    }

    public void renderEntityOutlineFramebuffer()
    {
        if (this.isRenderEntityOutlines())
        {
            GlStateManager.func_179147_l();
            GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.field_71443_c, this.mc.field_71440_d, false);
            GlStateManager.func_179084_k();
        }
    }

    protected boolean isRenderEntityOutlines()
    {
        return this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && this.mc.player != null;
    }

    private void generateSky2()
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        if (this.sky2VBO != null)
        {
            this.sky2VBO.func_177362_c();
        }

        if (this.field_72781_x >= 0)
        {
            GLAllocation.func_74523_b(this.field_72781_x);
            this.field_72781_x = -1;
        }

        if (this.field_175005_X)
        {
            this.sky2VBO = new VertexBuffer(this.skyVertexFormat);
            this.renderSky(bufferbuilder, -16.0F, true);
            bufferbuilder.finishDrawing();
            bufferbuilder.reset();
            this.sky2VBO.func_181722_a(bufferbuilder.func_178966_f());
        }
        else
        {
            this.field_72781_x = GLAllocation.func_74526_a(1);
            GlStateManager.func_187423_f(this.field_72781_x, 4864);
            this.renderSky(bufferbuilder, -16.0F, true);
            tessellator.draw();
            GlStateManager.func_187415_K();
        }
    }

    private void generateSky()
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        if (this.skyVBO != null)
        {
            this.skyVBO.func_177362_c();
        }

        if (this.field_72771_w >= 0)
        {
            GLAllocation.func_74523_b(this.field_72771_w);
            this.field_72771_w = -1;
        }

        if (this.field_175005_X)
        {
            this.skyVBO = new VertexBuffer(this.skyVertexFormat);
            this.renderSky(bufferbuilder, 16.0F, false);
            bufferbuilder.finishDrawing();
            bufferbuilder.reset();
            this.skyVBO.func_181722_a(bufferbuilder.func_178966_f());
        }
        else
        {
            this.field_72771_w = GLAllocation.func_74526_a(1);
            GlStateManager.func_187423_f(this.field_72771_w, 4864);
            this.renderSky(bufferbuilder, 16.0F, false);
            tessellator.draw();
            GlStateManager.func_187415_K();
        }
    }

    private void renderSky(BufferBuilder bufferBuilderIn, float posY, boolean reverseX)
    {
        int i = 64;
        int j = 6;
        bufferBuilderIn.begin(7, DefaultVertexFormats.POSITION);

        for (int k = -384; k <= 384; k += 64)
        {
            for (int l = -384; l <= 384; l += 64)
            {
                float f = (float)k;
                float f1 = (float)(k + 64);

                if (reverseX)
                {
                    f1 = (float)k;
                    f = (float)(k + 64);
                }

                bufferBuilderIn.func_181662_b((double)f, (double)posY, (double)l).endVertex();
                bufferBuilderIn.func_181662_b((double)f1, (double)posY, (double)l).endVertex();
                bufferBuilderIn.func_181662_b((double)f1, (double)posY, (double)(l + 64)).endVertex();
                bufferBuilderIn.func_181662_b((double)f, (double)posY, (double)(l + 64)).endVertex();
            }
        }
    }

    private void generateStars()
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        if (this.starVBO != null)
        {
            this.starVBO.func_177362_c();
        }

        if (this.field_72772_v >= 0)
        {
            GLAllocation.func_74523_b(this.field_72772_v);
            this.field_72772_v = -1;
        }

        if (this.field_175005_X)
        {
            this.starVBO = new VertexBuffer(this.skyVertexFormat);
            this.renderStars(bufferbuilder);
            bufferbuilder.finishDrawing();
            bufferbuilder.reset();
            this.starVBO.func_181722_a(bufferbuilder.func_178966_f());
        }
        else
        {
            this.field_72772_v = GLAllocation.func_74526_a(1);
            GlStateManager.func_179094_E();
            GlStateManager.func_187423_f(this.field_72772_v, 4864);
            this.renderStars(bufferbuilder);
            tessellator.draw();
            GlStateManager.func_187415_K();
            GlStateManager.func_179121_F();
        }
    }

    private void renderStars(BufferBuilder bufferBuilderIn)
    {
        Random random = new Random(10842L);
        bufferBuilderIn.begin(7, DefaultVertexFormats.POSITION);

        for (int i = 0; i < 1500; ++i)
        {
            double d0 = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d1 = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d2 = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d3 = (double)(0.15F + random.nextFloat() * 0.1F);
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d4 < 1.0D && d4 > 0.01D)
            {
                d4 = 1.0D / Math.sqrt(d4);
                d0 = d0 * d4;
                d1 = d1 * d4;
                d2 = d2 * d4;
                double d5 = d0 * 100.0D;
                double d6 = d1 * 100.0D;
                double d7 = d2 * 100.0D;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * Math.PI * 2.0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for (int j = 0; j < 4; ++j)
                {
                    double d17 = 0.0D;
                    double d18 = (double)((j & 2) - 1) * d3;
                    double d19 = (double)((j + 1 & 2) - 1) * d3;
                    double d20 = 0.0D;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + 0.0D * d13;
                    double d24 = 0.0D * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    bufferBuilderIn.func_181662_b(d5 + d25, d6 + d23, d7 + d26).endVertex();
                }
            }
        }
    }

    /**
     * set null to clear
     */
    public void setWorldAndLoadRenderers(@Nullable WorldClient worldClientIn)
    {
        if (this.world != null)
        {
            this.world.func_72848_b(this);
        }

        this.frustumUpdatePosX = Double.MIN_VALUE;
        this.frustumUpdatePosY = Double.MIN_VALUE;
        this.frustumUpdatePosZ = Double.MIN_VALUE;
        this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
        this.renderManager.setWorld(worldClientIn);
        this.world = worldClientIn;

        if (worldClientIn != null)
        {
            worldClientIn.func_72954_a(this);
            this.loadRenderers();
        }
        else
        {
            this.chunksToUpdate.clear();
            this.renderInfos.clear();

            if (this.viewFrustum != null)
            {
                this.viewFrustum.deleteGlResources();
                this.viewFrustum = null;
            }

            if (this.renderDispatcher != null)
            {
                this.renderDispatcher.stopWorkerThreads();
            }

            this.renderDispatcher = null;
        }
    }

    /**
     * Loads all the renderers and sets up the basic settings usage
     */
    public void loadRenderers()
    {
        if (this.world != null)
        {
            if (this.renderDispatcher == null)
            {
                this.renderDispatcher = new ChunkRenderDispatcher();
            }

            this.displayListEntitiesDirty = true;
            Blocks.field_150362_t.func_150122_b(this.mc.gameSettings.fancyGraphics);
            Blocks.field_150361_u.func_150122_b(this.mc.gameSettings.fancyGraphics);
            this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
            boolean flag = this.field_175005_X;
            this.field_175005_X = OpenGlHelper.func_176075_f();

            if (flag && !this.field_175005_X)
            {
                this.field_174996_N = new RenderList();
                this.field_175007_a = new ListChunkFactory();
            }
            else if (!flag && this.field_175005_X)
            {
                this.field_174996_N = new VboRenderList();
                this.field_175007_a = new VboChunkFactory();
            }

            if (flag != this.field_175005_X)
            {
                this.generateStars();
                this.generateSky();
                this.generateSky2();
            }

            if (this.viewFrustum != null)
            {
                this.viewFrustum.deleteGlResources();
            }

            this.stopChunkUpdates();

            synchronized (this.setTileEntities)
            {
                this.setTileEntities.clear();
            }

            this.viewFrustum = new ViewFrustum(this.world, this.mc.gameSettings.renderDistanceChunks, this, this.field_175007_a);

            if (this.world != null)
            {
                Entity entity = this.mc.getRenderViewEntity();

                if (entity != null)
                {
                    this.viewFrustum.updateChunkPositions(entity.posX, entity.posZ);
                }
            }

            this.field_72740_G = 2;
        }
    }

    protected void stopChunkUpdates()
    {
        this.chunksToUpdate.clear();
        this.renderDispatcher.stopChunkUpdates();
    }

    public void createBindEntityOutlineFbs(int width, int height)
    {
        if (OpenGlHelper.field_148824_g)
        {
            if (this.entityOutlineShader != null)
            {
                this.entityOutlineShader.createBindFramebuffers(width, height);
            }
        }
    }

    public void func_180446_a(Entity p_180446_1_, ICamera p_180446_2_, float p_180446_3_)
    {
        if (this.field_72740_G > 0)
        {
            --this.field_72740_G;
        }
        else
        {
            double d0 = p_180446_1_.prevPosX + (p_180446_1_.posX - p_180446_1_.prevPosX) * (double)p_180446_3_;
            double d1 = p_180446_1_.prevPosY + (p_180446_1_.posY - p_180446_1_.prevPosY) * (double)p_180446_3_;
            double d2 = p_180446_1_.prevPosZ + (p_180446_1_.posZ - p_180446_1_.prevPosZ) * (double)p_180446_3_;
            this.world.profiler.startSection("prepare");
            TileEntityRendererDispatcher.instance.func_190056_a(this.world, this.mc.getTextureManager(), this.mc.fontRenderer, this.mc.getRenderViewEntity(), this.mc.objectMouseOver, p_180446_3_);
            this.renderManager.func_180597_a(this.world, this.mc.fontRenderer, this.mc.getRenderViewEntity(), this.mc.pointedEntity, this.mc.gameSettings, p_180446_3_);
            this.field_72748_H = 0;
            this.countEntitiesRendered = 0;
            this.countEntitiesHidden = 0;
            Entity entity = this.mc.getRenderViewEntity();
            double d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)p_180446_3_;
            double d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)p_180446_3_;
            double d5 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)p_180446_3_;
            TileEntityRendererDispatcher.field_147554_b = d3;
            TileEntityRendererDispatcher.field_147555_c = d4;
            TileEntityRendererDispatcher.field_147552_d = d5;
            this.renderManager.func_178628_a(d3, d4, d5);
            this.mc.gameRenderer.func_180436_i();
            this.world.profiler.func_76318_c("global");
            List<Entity> list = this.world.func_72910_y();
            this.field_72748_H = list.size();

            for (int i = 0; i < this.world.field_73007_j.size(); ++i)
            {
                Entity entity1 = this.world.field_73007_j.get(i);
                ++this.countEntitiesRendered;

                if (entity1.isInRangeToRender3d(d0, d1, d2))
                {
                    this.renderManager.func_188388_a(entity1, p_180446_3_, false);
                }
            }

            this.world.profiler.func_76318_c("entities");
            List<Entity> list1 = Lists.<Entity>newArrayList();
            List<Entity> list2 = Lists.<Entity>newArrayList();
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

            for (RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos)
            {
                Chunk chunk = this.world.getChunkAt(renderglobal$containerlocalrenderinformation.renderChunk.getPosition());
                ClassInheritanceMultiMap<Entity> classinheritancemultimap = chunk.getEntityLists()[renderglobal$containerlocalrenderinformation.renderChunk.getPosition().getY() / 16];

                if (!classinheritancemultimap.isEmpty())
                {
                    for (Entity entity2 : classinheritancemultimap)
                    {
                        boolean flag = this.renderManager.func_178635_a(entity2, p_180446_2_, d0, d1, d2) || entity2.isRidingOrBeingRiddenBy(this.mc.player);

                        if (flag)
                        {
                            boolean flag1 = this.mc.getRenderViewEntity() instanceof EntityLivingBase ? ((EntityLivingBase)this.mc.getRenderViewEntity()).isSleeping() : false;

                            if ((entity2 != this.mc.getRenderViewEntity() || this.mc.gameSettings.thirdPersonView != 0 || flag1) && (entity2.posY < 0.0D || entity2.posY >= 256.0D || this.world.isBlockLoaded(blockpos$pooledmutableblockpos.setPos(entity2))))
                            {
                                ++this.countEntitiesRendered;
                                this.renderManager.func_188388_a(entity2, p_180446_3_, false);

                                if (this.func_184383_a(entity2, entity, p_180446_2_))
                                {
                                    list1.add(entity2);
                                }

                                if (this.renderManager.func_188390_b(entity2))
                                {
                                    list2.add(entity2);
                                }
                            }
                        }
                    }
                }
            }

            blockpos$pooledmutableblockpos.func_185344_t();

            if (!list2.isEmpty())
            {
                for (Entity entity3 : list2)
                {
                    this.renderManager.func_188389_a(entity3, p_180446_3_);
                }
            }

            if (this.isRenderEntityOutlines() && (!list1.isEmpty() || this.field_184386_ad))
            {
                this.world.profiler.func_76318_c("entityOutlines");
                this.entityOutlineFramebuffer.func_147614_f();
                this.field_184386_ad = !list1.isEmpty();

                if (!list1.isEmpty())
                {
                    GlStateManager.func_179143_c(519);
                    GlStateManager.func_179106_n();
                    this.entityOutlineFramebuffer.bindFramebuffer(false);
                    RenderHelper.disableStandardItemLighting();
                    this.renderManager.func_178632_c(true);

                    for (int j = 0; j < list1.size(); ++j)
                    {
                        this.renderManager.func_188388_a(list1.get(j), p_180446_3_, false);
                    }

                    this.renderManager.func_178632_c(false);
                    RenderHelper.func_74519_b();
                    GlStateManager.func_179132_a(false);
                    this.entityOutlineShader.render(p_180446_3_);
                    GlStateManager.func_179145_e();
                    GlStateManager.func_179132_a(true);
                    GlStateManager.func_179127_m();
                    GlStateManager.func_179147_l();
                    GlStateManager.func_179142_g();
                    GlStateManager.func_179143_c(515);
                    GlStateManager.func_179126_j();
                    GlStateManager.func_179141_d();
                }

                this.mc.getFramebuffer().bindFramebuffer(false);
            }

            this.world.profiler.func_76318_c("blockentities");
            RenderHelper.func_74519_b();

            for (RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1 : this.renderInfos)
            {
                List<TileEntity> list3 = renderglobal$containerlocalrenderinformation1.renderChunk.getCompiledChunk().getTileEntities();

                if (!list3.isEmpty())
                {
                    for (TileEntity tileentity2 : list3)
                    {
                        TileEntityRendererDispatcher.instance.func_180546_a(tileentity2, p_180446_3_, -1);
                    }
                }
            }

            synchronized (this.setTileEntities)
            {
                for (TileEntity tileentity : this.setTileEntities)
                {
                    TileEntityRendererDispatcher.instance.func_180546_a(tileentity, p_180446_3_, -1);
                }
            }

            this.func_180443_s();

            for (DestroyBlockProgress destroyblockprogress : this.damagedBlocks.values())
            {
                BlockPos blockpos = destroyblockprogress.getPosition();

                if (this.world.getBlockState(blockpos).getBlock().hasTileEntity())
                {
                    TileEntity tileentity1 = this.world.getTileEntity(blockpos);

                    if (tileentity1 instanceof TileEntityChest)
                    {
                        TileEntityChest tileentitychest = (TileEntityChest)tileentity1;

                        if (tileentitychest.field_145991_k != null)
                        {
                            blockpos = blockpos.offset(EnumFacing.WEST);
                            tileentity1 = this.world.getTileEntity(blockpos);
                        }
                        else if (tileentitychest.field_145992_i != null)
                        {
                            blockpos = blockpos.offset(EnumFacing.NORTH);
                            tileentity1 = this.world.getTileEntity(blockpos);
                        }
                    }

                    IBlockState iblockstate = this.world.getBlockState(blockpos);

                    if (tileentity1 != null && iblockstate.func_191057_i())
                    {
                        TileEntityRendererDispatcher.instance.func_180546_a(tileentity1, p_180446_3_, destroyblockprogress.getPartialBlockDamage());
                    }
                }
            }

            this.func_174969_t();
            this.mc.gameRenderer.func_175072_h();
            this.mc.profiler.endSection();
        }
    }

    private boolean func_184383_a(Entity p_184383_1_, Entity p_184383_2_, ICamera p_184383_3_)
    {
        boolean flag = p_184383_2_ instanceof EntityLivingBase && ((EntityLivingBase)p_184383_2_).isSleeping();

        if (p_184383_1_ == p_184383_2_ && this.mc.gameSettings.thirdPersonView == 0 && !flag)
        {
            return false;
        }
        else if (p_184383_1_.func_184202_aL())
        {
            return true;
        }
        else if (this.mc.player.isSpectator() && this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown() && p_184383_1_ instanceof EntityPlayer)
        {
            return p_184383_1_.ignoreFrustumCheck || p_184383_3_.func_78546_a(p_184383_1_.getBoundingBox()) || p_184383_1_.isRidingOrBeingRiddenBy(this.mc.player);
        }
        else
        {
            return false;
        }
    }

    /**
     * Gets the render info for use on the Debug screen
     */
    public String getDebugInfoRenders()
    {
        int i = this.viewFrustum.renderChunks.length;
        int j = this.getRenderedChunks();
        return String.format("C: %d/%d %sD: %d, L: %d, %s", j, i, this.mc.renderChunksMany ? "(s) " : "", this.renderDistanceChunks, this.field_184387_ae.size(), this.renderDispatcher == null ? "null" : this.renderDispatcher.getDebugInfo());
    }

    protected int getRenderedChunks()
    {
        int i = 0;

        for (RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos)
        {
            CompiledChunk compiledchunk = renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk;

            if (compiledchunk != CompiledChunk.DUMMY && !compiledchunk.isEmpty())
            {
                ++i;
            }
        }

        return i;
    }

    /**
     * Gets the entities info for use on the Debug screen
     */
    public String getDebugInfoEntities()
    {
        return "E: " + this.countEntitiesRendered + "/" + this.field_72748_H + ", B: " + this.countEntitiesHidden;
    }

    public void func_174970_a(Entity p_174970_1_, double p_174970_2_, ICamera p_174970_4_, int p_174970_5_, boolean p_174970_6_)
    {
        if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks)
        {
            this.loadRenderers();
        }

        this.world.profiler.startSection("camera");
        double d0 = p_174970_1_.posX - this.frustumUpdatePosX;
        double d1 = p_174970_1_.posY - this.frustumUpdatePosY;
        double d2 = p_174970_1_.posZ - this.frustumUpdatePosZ;

        if (this.frustumUpdatePosChunkX != p_174970_1_.chunkCoordX || this.frustumUpdatePosChunkY != p_174970_1_.chunkCoordY || this.frustumUpdatePosChunkZ != p_174970_1_.chunkCoordZ || d0 * d0 + d1 * d1 + d2 * d2 > 16.0D)
        {
            this.frustumUpdatePosX = p_174970_1_.posX;
            this.frustumUpdatePosY = p_174970_1_.posY;
            this.frustumUpdatePosZ = p_174970_1_.posZ;
            this.frustumUpdatePosChunkX = p_174970_1_.chunkCoordX;
            this.frustumUpdatePosChunkY = p_174970_1_.chunkCoordY;
            this.frustumUpdatePosChunkZ = p_174970_1_.chunkCoordZ;
            this.viewFrustum.updateChunkPositions(p_174970_1_.posX, p_174970_1_.posZ);
        }

        this.world.profiler.func_76318_c("renderlistcamera");
        double d3 = p_174970_1_.lastTickPosX + (p_174970_1_.posX - p_174970_1_.lastTickPosX) * p_174970_2_;
        double d4 = p_174970_1_.lastTickPosY + (p_174970_1_.posY - p_174970_1_.lastTickPosY) * p_174970_2_;
        double d5 = p_174970_1_.lastTickPosZ + (p_174970_1_.posZ - p_174970_1_.lastTickPosZ) * p_174970_2_;
        this.field_174996_N.func_178004_a(d3, d4, d5);
        this.world.profiler.func_76318_c("cull");

        if (this.debugFixedClippingHelper != null)
        {
            Frustum frustum = new Frustum(this.debugFixedClippingHelper);
            frustum.func_78547_a(this.debugTerrainFrustumPosition.x, this.debugTerrainFrustumPosition.y, this.debugTerrainFrustumPosition.z);
            p_174970_4_ = frustum;
        }

        this.mc.profiler.func_76318_c("culling");
        BlockPos blockpos1 = new BlockPos(d3, d4 + (double)p_174970_1_.getEyeHeight(), d5);
        RenderChunk renderchunk = this.viewFrustum.getRenderChunk(blockpos1);
        BlockPos blockpos = new BlockPos(MathHelper.floor(d3 / 16.0D) * 16, MathHelper.floor(d4 / 16.0D) * 16, MathHelper.floor(d5 / 16.0D) * 16);
        this.displayListEntitiesDirty = this.displayListEntitiesDirty || !this.chunksToUpdate.isEmpty() || p_174970_1_.posX != this.lastViewEntityX || p_174970_1_.posY != this.lastViewEntityY || p_174970_1_.posZ != this.lastViewEntityZ || (double)p_174970_1_.rotationPitch != this.lastViewEntityPitch || (double)p_174970_1_.rotationYaw != this.lastViewEntityYaw;
        this.lastViewEntityX = p_174970_1_.posX;
        this.lastViewEntityY = p_174970_1_.posY;
        this.lastViewEntityZ = p_174970_1_.posZ;
        this.lastViewEntityPitch = (double)p_174970_1_.rotationPitch;
        this.lastViewEntityYaw = (double)p_174970_1_.rotationYaw;
        boolean flag = this.debugFixedClippingHelper != null;
        this.mc.profiler.func_76318_c("update");

        if (!flag && this.displayListEntitiesDirty)
        {
            this.displayListEntitiesDirty = false;
            this.renderInfos = Lists.<RenderGlobal.ContainerLocalRenderInformation>newArrayList();
            Queue<RenderGlobal.ContainerLocalRenderInformation> queue = Queues.<RenderGlobal.ContainerLocalRenderInformation>newArrayDeque();
            Entity.setRenderDistanceWeight(MathHelper.clamp((double)this.mc.gameSettings.renderDistanceChunks / 8.0D, 1.0D, 2.5D));
            boolean flag1 = this.mc.renderChunksMany;

            if (renderchunk != null)
            {
                boolean flag2 = false;
                RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation3 = new RenderGlobal.ContainerLocalRenderInformation(renderchunk, (EnumFacing)null, 0);
                Set<EnumFacing> set1 = this.getVisibleFacings(blockpos1);

                if (set1.size() == 1)
                {
                    Vector3f vector3f = this.func_174962_a(p_174970_1_, p_174970_2_);
                    EnumFacing enumfacing = EnumFacing.getFacingFromVector(vector3f.x, vector3f.y, vector3f.z).getOpposite();
                    set1.remove(enumfacing);
                }

                if (set1.isEmpty())
                {
                    flag2 = true;
                }

                if (flag2 && !p_174970_6_)
                {
                    this.renderInfos.add(renderglobal$containerlocalrenderinformation3);
                }
                else
                {
                    if (p_174970_6_ && this.world.getBlockState(blockpos1).func_185914_p())
                    {
                        flag1 = false;
                    }

                    renderchunk.setFrameIndex(p_174970_5_);
                    queue.add(renderglobal$containerlocalrenderinformation3);
                }
            }
            else
            {
                int i = blockpos1.getY() > 0 ? 248 : 8;

                for (int j = -this.renderDistanceChunks; j <= this.renderDistanceChunks; ++j)
                {
                    for (int k = -this.renderDistanceChunks; k <= this.renderDistanceChunks; ++k)
                    {
                        RenderChunk renderchunk1 = this.viewFrustum.getRenderChunk(new BlockPos((j << 4) + 8, i, (k << 4) + 8));

                        if (renderchunk1 != null && p_174970_4_.func_78546_a(renderchunk1.boundingBox))
                        {
                            renderchunk1.setFrameIndex(p_174970_5_);
                            queue.add(new RenderGlobal.ContainerLocalRenderInformation(renderchunk1, (EnumFacing)null, 0));
                        }
                    }
                }
            }

            this.mc.profiler.startSection("iteration");

            while (!queue.isEmpty())
            {
                RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1 = queue.poll();
                RenderChunk renderchunk3 = renderglobal$containerlocalrenderinformation1.renderChunk;
                EnumFacing enumfacing2 = renderglobal$containerlocalrenderinformation1.facing;
                this.renderInfos.add(renderglobal$containerlocalrenderinformation1);

                for (EnumFacing enumfacing1 : EnumFacing.values())
                {
                    RenderChunk renderchunk2 = this.getRenderChunkOffset(blockpos, renderchunk3, enumfacing1);

                    if ((!flag1 || !renderglobal$containerlocalrenderinformation1.hasDirection(enumfacing1.getOpposite())) && (!flag1 || enumfacing2 == null || renderchunk3.getCompiledChunk().isVisible(enumfacing2.getOpposite(), enumfacing1)) && renderchunk2 != null && renderchunk2.setFrameIndex(p_174970_5_) && p_174970_4_.func_78546_a(renderchunk2.boundingBox))
                    {
                        RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = new RenderGlobal.ContainerLocalRenderInformation(renderchunk2, enumfacing1, renderglobal$containerlocalrenderinformation1.counter + 1);
                        renderglobal$containerlocalrenderinformation.setDirection(renderglobal$containerlocalrenderinformation1.setFacing, enumfacing1);
                        queue.add(renderglobal$containerlocalrenderinformation);
                    }
                }
            }

            this.mc.profiler.endSection();
        }

        this.mc.profiler.func_76318_c("captureFrustum");

        if (this.debugFixTerrainFrustum)
        {
            this.func_174984_a(d3, d4, d5);
            this.debugFixTerrainFrustum = false;
        }

        this.mc.profiler.func_76318_c("rebuildNear");
        Set<RenderChunk> set = this.chunksToUpdate;
        this.chunksToUpdate = Sets.<RenderChunk>newLinkedHashSet();

        for (RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation2 : this.renderInfos)
        {
            RenderChunk renderchunk4 = renderglobal$containerlocalrenderinformation2.renderChunk;

            if (renderchunk4.needsUpdate() || set.contains(renderchunk4))
            {
                this.displayListEntitiesDirty = true;
                BlockPos blockpos2 = renderchunk4.getPosition().add(8, 8, 8);
                boolean flag3 = blockpos2.distanceSq(blockpos1) < 768.0D;

                if (!renderchunk4.needsImmediateUpdate() && !flag3)
                {
                    this.chunksToUpdate.add(renderchunk4);
                }
                else
                {
                    this.mc.profiler.startSection("build near");
                    this.renderDispatcher.func_178505_b(renderchunk4);
                    renderchunk4.clearNeedsUpdate();
                    this.mc.profiler.endSection();
                }
            }
        }

        this.chunksToUpdate.addAll(set);
        this.mc.profiler.endSection();
    }

    private Set<EnumFacing> getVisibleFacings(BlockPos pos)
    {
        VisGraph visgraph = new VisGraph();
        BlockPos blockpos = new BlockPos(pos.getX() >> 4 << 4, pos.getY() >> 4 << 4, pos.getZ() >> 4 << 4);
        Chunk chunk = this.world.getChunkAt(blockpos);

        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.func_177975_b(blockpos, blockpos.add(15, 15, 15)))
        {
            if (chunk.func_177435_g(blockpos$mutableblockpos).func_185914_p())
            {
                visgraph.setOpaqueCube(blockpos$mutableblockpos);
            }
        }
        return visgraph.getVisibleFacings(pos);
    }

    @Nullable

    /**
     * Returns RenderChunk offset from given RenderChunk in given direction, or null if it can't be seen by player at
     * given BlockPos.
     */
    private RenderChunk getRenderChunkOffset(BlockPos playerPos, RenderChunk renderChunkBase, EnumFacing facing)
    {
        BlockPos blockpos = renderChunkBase.getBlockPosOffset16(facing);

        if (MathHelper.abs(playerPos.getX() - blockpos.getX()) > this.renderDistanceChunks * 16)
        {
            return null;
        }
        else if (blockpos.getY() >= 0 && blockpos.getY() < 256)
        {
            return MathHelper.abs(playerPos.getZ() - blockpos.getZ()) > this.renderDistanceChunks * 16 ? null : this.viewFrustum.getRenderChunk(blockpos);
        }
        else
        {
            return null;
        }
    }

    private void func_174984_a(double p_174984_1_, double p_174984_3_, double p_174984_5_)
    {
        this.debugFixedClippingHelper = new ClippingHelperImpl();
        ((ClippingHelperImpl)this.debugFixedClippingHelper).func_78560_b();
        Matrix4f matrix4f = new Matrix4f(this.debugFixedClippingHelper.field_178626_c);
        matrix4f.transpose();
        Matrix4f matrix4f1 = new Matrix4f(this.debugFixedClippingHelper.field_178625_b);
        matrix4f1.transpose();
        Matrix4f matrix4f2 = new Matrix4f();
        Matrix4f.mul(matrix4f1, matrix4f, matrix4f2);
        matrix4f2.invert();
        this.debugTerrainFrustumPosition.x = p_174984_1_;
        this.debugTerrainFrustumPosition.y = p_174984_3_;
        this.debugTerrainFrustumPosition.z = p_174984_5_;
        this.debugTerrainMatrix[0] = new Vector4f(-1.0F, -1.0F, -1.0F, 1.0F);
        this.debugTerrainMatrix[1] = new Vector4f(1.0F, -1.0F, -1.0F, 1.0F);
        this.debugTerrainMatrix[2] = new Vector4f(1.0F, 1.0F, -1.0F, 1.0F);
        this.debugTerrainMatrix[3] = new Vector4f(-1.0F, 1.0F, -1.0F, 1.0F);
        this.debugTerrainMatrix[4] = new Vector4f(-1.0F, -1.0F, 1.0F, 1.0F);
        this.debugTerrainMatrix[5] = new Vector4f(1.0F, -1.0F, 1.0F, 1.0F);
        this.debugTerrainMatrix[6] = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.debugTerrainMatrix[7] = new Vector4f(-1.0F, 1.0F, 1.0F, 1.0F);

        for (int i = 0; i < 8; ++i)
        {
            Matrix4f.transform(matrix4f2, this.debugTerrainMatrix[i], this.debugTerrainMatrix[i]);
            this.debugTerrainMatrix[i].x /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].y /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].z /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].w = 1.0F;
        }
    }

    protected Vector3f func_174962_a(Entity p_174962_1_, double p_174962_2_)
    {
        float f = (float)((double)p_174962_1_.prevRotationPitch + (double)(p_174962_1_.rotationPitch - p_174962_1_.prevRotationPitch) * p_174962_2_);
        float f1 = (float)((double)p_174962_1_.prevRotationYaw + (double)(p_174962_1_.rotationYaw - p_174962_1_.prevRotationYaw) * p_174962_2_);

        if (Minecraft.getInstance().gameSettings.thirdPersonView == 2)
        {
            f += 180.0F;
        }

        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        return new Vector3f(f3 * f4, f5, f2 * f4);
    }

    public int func_174977_a(BlockRenderLayer p_174977_1_, double p_174977_2_, int p_174977_4_, Entity p_174977_5_)
    {
        RenderHelper.disableStandardItemLighting();

        if (p_174977_1_ == BlockRenderLayer.TRANSLUCENT)
        {
            this.mc.profiler.startSection("translucent_sort");
            double d0 = p_174977_5_.posX - this.prevRenderSortX;
            double d1 = p_174977_5_.posY - this.prevRenderSortY;
            double d2 = p_174977_5_.posZ - this.prevRenderSortZ;

            if (d0 * d0 + d1 * d1 + d2 * d2 > 1.0D)
            {
                this.prevRenderSortX = p_174977_5_.posX;
                this.prevRenderSortY = p_174977_5_.posY;
                this.prevRenderSortZ = p_174977_5_.posZ;
                int k = 0;

                for (RenderGlobal.ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos)
                {
                    if (renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk.func_178492_d(p_174977_1_) && k++ < 15)
                    {
                        this.renderDispatcher.func_178509_c(renderglobal$containerlocalrenderinformation.renderChunk);
                    }
                }
            }

            this.mc.profiler.endSection();
        }

        this.mc.profiler.startSection("filterempty");
        int l = 0;
        boolean flag = p_174977_1_ == BlockRenderLayer.TRANSLUCENT;
        int i1 = flag ? this.renderInfos.size() - 1 : 0;
        int i = flag ? -1 : this.renderInfos.size();
        int j1 = flag ? -1 : 1;

        for (int j = i1; j != i; j += j1)
        {
            RenderChunk renderchunk = (this.renderInfos.get(j)).renderChunk;

            if (!renderchunk.getCompiledChunk().func_178491_b(p_174977_1_))
            {
                ++l;
                this.field_174996_N.func_178002_a(renderchunk, p_174977_1_);
            }
        }

        this.mc.profiler.endStartSection(() ->
        {
            return "render_" + p_174977_1_;
        });
        this.func_174982_a(p_174977_1_);
        this.mc.profiler.endSection();
        return l;
    }

    @SuppressWarnings("incomplete-switch")
    private void func_174982_a(BlockRenderLayer p_174982_1_)
    {
        this.mc.gameRenderer.func_180436_i();

        if (OpenGlHelper.func_176075_f())
        {
            GlStateManager.func_187410_q(32884);
            OpenGlHelper.func_77472_b(OpenGlHelper.field_77478_a);
            GlStateManager.func_187410_q(32888);
            OpenGlHelper.func_77472_b(OpenGlHelper.field_77476_b);
            GlStateManager.func_187410_q(32888);
            OpenGlHelper.func_77472_b(OpenGlHelper.field_77478_a);
            GlStateManager.func_187410_q(32886);
        }

        this.field_174996_N.func_178001_a(p_174982_1_);

        if (OpenGlHelper.func_176075_f())
        {
            for (VertexFormatElement vertexformatelement : DefaultVertexFormats.BLOCK.func_177343_g())
            {
                VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                int k1 = vertexformatelement.getIndex();

                switch (vertexformatelement$enumusage)
                {
                    case POSITION:
                        GlStateManager.func_187429_p(32884);
                        break;

                    case UV:
                        OpenGlHelper.func_77472_b(OpenGlHelper.field_77478_a + k1);
                        GlStateManager.func_187429_p(32888);
                        OpenGlHelper.func_77472_b(OpenGlHelper.field_77478_a);
                        break;

                    case COLOR:
                        GlStateManager.func_187429_p(32886);
                        GlStateManager.func_179117_G();
                }
            }
        }

        this.mc.gameRenderer.func_175072_h();
    }

    private void func_174965_a(Iterator<DestroyBlockProgress> p_174965_1_)
    {
        while (p_174965_1_.hasNext())
        {
            DestroyBlockProgress destroyblockprogress = p_174965_1_.next();
            int k1 = destroyblockprogress.getCreationCloudUpdateTick();

            if (this.ticks - k1 > 400)
            {
                p_174965_1_.remove();
            }
        }
    }

    public void tick()
    {
        ++this.ticks;

        if (this.ticks % 20 == 0)
        {
            this.func_174965_a(this.damagedBlocks.values().iterator());
        }

        if (!this.field_184387_ae.isEmpty() && !this.renderDispatcher.func_188248_h() && this.chunksToUpdate.isEmpty())
        {
            Iterator<BlockPos> iterator = this.field_184387_ae.iterator();

            while (iterator.hasNext())
            {
                BlockPos blockpos = iterator.next();
                iterator.remove();
                int k1 = blockpos.getX();
                int l1 = blockpos.getY();
                int i2 = blockpos.getZ();
                this.func_184385_a(k1 - 1, l1 - 1, i2 - 1, k1 + 1, l1 + 1, i2 + 1, false);
            }
        }
    }

    private void func_180448_r()
    {
        GlStateManager.func_179106_n();
        GlStateManager.func_179118_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.func_179132_a(false);
        this.textureManager.bindTexture(END_SKY_TEXTURES);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        for (int k1 = 0; k1 < 6; ++k1)
        {
            GlStateManager.func_179094_E();

            if (k1 == 1)
            {
                GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (k1 == 2)
            {
                GlStateManager.func_179114_b(-90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (k1 == 3)
            {
                GlStateManager.func_179114_b(180.0F, 1.0F, 0.0F, 0.0F);
            }

            if (k1 == 4)
            {
                GlStateManager.func_179114_b(90.0F, 0.0F, 0.0F, 1.0F);
            }

            if (k1 == 5)
            {
                GlStateManager.func_179114_b(-90.0F, 0.0F, 0.0F, 1.0F);
            }

            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.func_181662_b(-100.0D, -100.0D, -100.0D).func_187315_a(0.0D, 0.0D).func_181669_b(40, 40, 40, 255).endVertex();
            bufferbuilder.func_181662_b(-100.0D, -100.0D, 100.0D).func_187315_a(0.0D, 16.0D).func_181669_b(40, 40, 40, 255).endVertex();
            bufferbuilder.func_181662_b(100.0D, -100.0D, 100.0D).func_187315_a(16.0D, 16.0D).func_181669_b(40, 40, 40, 255).endVertex();
            bufferbuilder.func_181662_b(100.0D, -100.0D, -100.0D).func_187315_a(16.0D, 0.0D).func_181669_b(40, 40, 40, 255).endVertex();
            tessellator.draw();
            GlStateManager.func_179121_F();
        }

        GlStateManager.func_179132_a(true);
        GlStateManager.func_179098_w();
        GlStateManager.func_179141_d();
    }

    public void func_174976_a(float p_174976_1_, int p_174976_2_)
    {
        if (this.mc.world.dimension.getType().getId() == 1)
        {
            this.func_180448_r();
        }
        else if (this.mc.world.dimension.isSurfaceWorld())
        {
            GlStateManager.func_179090_x();
            Vec3d vec3d = this.world.func_72833_a(this.mc.getRenderViewEntity(), p_174976_1_);
            float f = (float)vec3d.x;
            float f1 = (float)vec3d.y;
            float f2 = (float)vec3d.z;

            if (p_174976_2_ != 2)
            {
                float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
                float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
                float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
                f = f3;
                f1 = f4;
                f2 = f5;
            }

            GlStateManager.func_179124_c(f, f1, f2);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            GlStateManager.func_179132_a(false);
            GlStateManager.func_179127_m();
            GlStateManager.func_179124_c(f, f1, f2);

            if (this.field_175005_X)
            {
                this.skyVBO.bindBuffer();
                GlStateManager.func_187410_q(32884);
                GlStateManager.func_187420_d(3, 5126, 12, 0);
                this.skyVBO.func_177358_a(7);
                this.skyVBO.unbindBuffer();
                GlStateManager.func_187429_p(32884);
            }
            else
            {
                GlStateManager.func_179148_o(this.field_72771_w);
            }

            GlStateManager.func_179106_n();
            GlStateManager.func_179118_c();
            GlStateManager.func_179147_l();
            GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderHelper.disableStandardItemLighting();
            float[] afloat = this.world.dimension.calcSunriseSunsetColors(this.world.getCelestialAngle(p_174976_1_), p_174976_1_);

            if (afloat != null)
            {
                GlStateManager.func_179090_x();
                GlStateManager.func_179103_j(7425);
                GlStateManager.func_179094_E();
                GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.func_179114_b(MathHelper.sin(this.world.getCelestialAngleRadians(p_174976_1_)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.func_179114_b(90.0F, 0.0F, 0.0F, 1.0F);
                float f6 = afloat[0];
                float f7 = afloat[1];
                float f8 = afloat[2];

                if (p_174976_2_ != 2)
                {
                    float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
                    float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
                    float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
                    f6 = f9;
                    f7 = f10;
                    f8 = f11;
                }

                bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
                bufferbuilder.func_181662_b(0.0D, 100.0D, 0.0D).func_181666_a(f6, f7, f8, afloat[3]).endVertex();
                int l1 = 16;

                for (int j2 = 0; j2 <= 16; ++j2)
                {
                    float f21 = (float)j2 * ((float)Math.PI * 2F) / 16.0F;
                    float f12 = MathHelper.sin(f21);
                    float f13 = MathHelper.cos(f21);
                    bufferbuilder.func_181662_b((double)(f12 * 120.0F), (double)(f13 * 120.0F), (double)(-f13 * 40.0F * afloat[3])).func_181666_a(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
                }

                tessellator.draw();
                GlStateManager.func_179121_F();
                GlStateManager.func_179103_j(7424);
            }

            GlStateManager.func_179098_w();
            GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.func_179094_E();
            float f16 = 1.0F - this.world.getRainStrength(p_174976_1_);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, f16);
            GlStateManager.func_179114_b(-90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(this.world.getCelestialAngle(p_174976_1_) * 360.0F, 1.0F, 0.0F, 0.0F);
            float f17 = 30.0F;
            this.textureManager.bindTexture(SUN_TEXTURES);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.func_181662_b((double)(-f17), 100.0D, (double)(-f17)).func_187315_a(0.0D, 0.0D).endVertex();
            bufferbuilder.func_181662_b((double)f17, 100.0D, (double)(-f17)).func_187315_a(1.0D, 0.0D).endVertex();
            bufferbuilder.func_181662_b((double)f17, 100.0D, (double)f17).func_187315_a(1.0D, 1.0D).endVertex();
            bufferbuilder.func_181662_b((double)(-f17), 100.0D, (double)f17).func_187315_a(0.0D, 1.0D).endVertex();
            tessellator.draw();
            f17 = 20.0F;
            this.textureManager.bindTexture(MOON_PHASES_TEXTURES);
            int k1 = this.world.getMoonPhase();
            int i2 = k1 % 4;
            int k2 = k1 / 4 % 2;
            float f22 = (float)(i2 + 0) / 4.0F;
            float f23 = (float)(k2 + 0) / 2.0F;
            float f24 = (float)(i2 + 1) / 4.0F;
            float f14 = (float)(k2 + 1) / 2.0F;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.func_181662_b((double)(-f17), -100.0D, (double)f17).func_187315_a((double)f24, (double)f14).endVertex();
            bufferbuilder.func_181662_b((double)f17, -100.0D, (double)f17).func_187315_a((double)f22, (double)f14).endVertex();
            bufferbuilder.func_181662_b((double)f17, -100.0D, (double)(-f17)).func_187315_a((double)f22, (double)f23).endVertex();
            bufferbuilder.func_181662_b((double)(-f17), -100.0D, (double)(-f17)).func_187315_a((double)f24, (double)f23).endVertex();
            tessellator.draw();
            GlStateManager.func_179090_x();
            float f15 = this.world.func_72880_h(p_174976_1_) * f16;

            if (f15 > 0.0F)
            {
                GlStateManager.func_179131_c(f15, f15, f15, f15);

                if (this.field_175005_X)
                {
                    this.starVBO.bindBuffer();
                    GlStateManager.func_187410_q(32884);
                    GlStateManager.func_187420_d(3, 5126, 12, 0);
                    this.starVBO.func_177358_a(7);
                    this.starVBO.unbindBuffer();
                    GlStateManager.func_187429_p(32884);
                }
                else
                {
                    GlStateManager.func_179148_o(this.field_72772_v);
                }
            }

            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.func_179084_k();
            GlStateManager.func_179141_d();
            GlStateManager.func_179127_m();
            GlStateManager.func_179121_F();
            GlStateManager.func_179090_x();
            GlStateManager.func_179124_c(0.0F, 0.0F, 0.0F);
            double d3 = this.mc.player.getEyePosition(p_174976_1_).y - this.world.func_72919_O();

            if (d3 < 0.0D)
            {
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b(0.0F, 12.0F, 0.0F);

                if (this.field_175005_X)
                {
                    this.sky2VBO.bindBuffer();
                    GlStateManager.func_187410_q(32884);
                    GlStateManager.func_187420_d(3, 5126, 12, 0);
                    this.sky2VBO.func_177358_a(7);
                    this.sky2VBO.unbindBuffer();
                    GlStateManager.func_187429_p(32884);
                }
                else
                {
                    GlStateManager.func_179148_o(this.field_72781_x);
                }

                GlStateManager.func_179121_F();
                float f18 = 1.0F;
                float f19 = -((float)(d3 + 65.0D));
                float f20 = -1.0F;
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                bufferbuilder.func_181662_b(-1.0D, (double)f19, 1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(1.0D, (double)f19, 1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(1.0D, -1.0D, 1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(-1.0D, -1.0D, 1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(-1.0D, -1.0D, -1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(1.0D, -1.0D, -1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(1.0D, (double)f19, -1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(-1.0D, (double)f19, -1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(1.0D, -1.0D, -1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(1.0D, -1.0D, 1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(1.0D, (double)f19, 1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(1.0D, (double)f19, -1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(-1.0D, (double)f19, -1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(-1.0D, (double)f19, 1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(-1.0D, -1.0D, 1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(-1.0D, -1.0D, -1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(-1.0D, -1.0D, -1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(-1.0D, -1.0D, 1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(1.0D, -1.0D, 1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                bufferbuilder.func_181662_b(1.0D, -1.0D, -1.0D).func_181669_b(0, 0, 0, 255).endVertex();
                tessellator.draw();
            }

            if (this.world.dimension.isSkyColored())
            {
                GlStateManager.func_179124_c(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
            }
            else
            {
                GlStateManager.func_179124_c(f, f1, f2);
            }

            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(0.0F, -((float)(d3 - 16.0D)), 0.0F);
            GlStateManager.func_179148_o(this.field_72781_x);
            GlStateManager.func_179121_F();
            GlStateManager.func_179098_w();
            GlStateManager.func_179132_a(true);
        }
    }

    public void func_180447_b(float p_180447_1_, int p_180447_2_, double p_180447_3_, double p_180447_5_, double p_180447_7_)
    {
        if (this.mc.world.dimension.isSurfaceWorld())
        {
            if (this.mc.gameSettings.func_181147_e() == 2)
            {
                this.func_180445_c(p_180447_1_, p_180447_2_, p_180447_3_, p_180447_5_, p_180447_7_);
            }
            else
            {
                GlStateManager.func_179129_p();
                int k1 = 32;
                int l1 = 8;
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                this.textureManager.bindTexture(CLOUDS_TEXTURES);
                GlStateManager.func_179147_l();
                GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                Vec3d vec3d = this.world.func_72824_f(p_180447_1_);
                float f = (float)vec3d.x;
                float f1 = (float)vec3d.y;
                float f2 = (float)vec3d.z;

                if (p_180447_2_ != 2)
                {
                    float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
                    float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
                    float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
                    f = f3;
                    f1 = f4;
                    f2 = f5;
                }

                float f9 = 4.8828125E-4F;
                double d5 = (double)((float)this.ticks + p_180447_1_);
                double d3 = p_180447_3_ + d5 * 0.029999999329447746D;
                int i2 = MathHelper.floor(d3 / 2048.0D);
                int j2 = MathHelper.floor(p_180447_7_ / 2048.0D);
                d3 = d3 - (double)(i2 * 2048);
                double lvt_22_1_ = p_180447_7_ - (double)(j2 * 2048);
                float f6 = this.world.dimension.getCloudHeight() - (float)p_180447_5_ + 0.33F;
                float f7 = (float)(d3 * 4.8828125E-4D);
                float f8 = (float)(lvt_22_1_ * 4.8828125E-4D);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

                for (int k2 = -256; k2 < 256; k2 += 32)
                {
                    for (int l2 = -256; l2 < 256; l2 += 32)
                    {
                        bufferbuilder.func_181662_b((double)(k2 + 0), (double)f6, (double)(l2 + 32)).func_187315_a((double)((float)(k2 + 0) * 4.8828125E-4F + f7), (double)((float)(l2 + 32) * 4.8828125E-4F + f8)).func_181666_a(f, f1, f2, 0.8F).endVertex();
                        bufferbuilder.func_181662_b((double)(k2 + 32), (double)f6, (double)(l2 + 32)).func_187315_a((double)((float)(k2 + 32) * 4.8828125E-4F + f7), (double)((float)(l2 + 32) * 4.8828125E-4F + f8)).func_181666_a(f, f1, f2, 0.8F).endVertex();
                        bufferbuilder.func_181662_b((double)(k2 + 32), (double)f6, (double)(l2 + 0)).func_187315_a((double)((float)(k2 + 32) * 4.8828125E-4F + f7), (double)((float)(l2 + 0) * 4.8828125E-4F + f8)).func_181666_a(f, f1, f2, 0.8F).endVertex();
                        bufferbuilder.func_181662_b((double)(k2 + 0), (double)f6, (double)(l2 + 0)).func_187315_a((double)((float)(k2 + 0) * 4.8828125E-4F + f7), (double)((float)(l2 + 0) * 4.8828125E-4F + f8)).func_181666_a(f, f1, f2, 0.8F).endVertex();
                    }
                }

                tessellator.draw();
                GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.func_179084_k();
                GlStateManager.func_179089_o();
            }
        }
    }

    public boolean func_72721_a(double p_72721_1_, double p_72721_3_, double p_72721_5_, float p_72721_7_)
    {
        return false;
    }

    private void func_180445_c(float p_180445_1_, int p_180445_2_, double p_180445_3_, double p_180445_5_, double p_180445_7_)
    {
        GlStateManager.func_179129_p();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f = 12.0F;
        float f1 = 4.0F;
        double d3 = (double)((float)this.ticks + p_180445_1_);
        double d4 = (p_180445_3_ + d3 * 0.029999999329447746D) / 12.0D;
        double d5 = p_180445_7_ / 12.0D + 0.33000001311302185D;
        float f2 = this.world.dimension.getCloudHeight() - (float)p_180445_5_ + 0.33F;
        int k1 = MathHelper.floor(d4 / 2048.0D);
        int l1 = MathHelper.floor(d5 / 2048.0D);
        d4 = d4 - (double)(k1 * 2048);
        d5 = d5 - (double)(l1 * 2048);
        this.textureManager.bindTexture(CLOUDS_TEXTURES);
        GlStateManager.func_179147_l();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Vec3d vec3d = this.world.func_72824_f(p_180445_1_);
        float f3 = (float)vec3d.x;
        float f4 = (float)vec3d.y;
        float f5 = (float)vec3d.z;

        if (p_180445_2_ != 2)
        {
            float f6 = (f3 * 30.0F + f4 * 59.0F + f5 * 11.0F) / 100.0F;
            float f7 = (f3 * 30.0F + f4 * 70.0F) / 100.0F;
            float f8 = (f3 * 30.0F + f5 * 70.0F) / 100.0F;
            f3 = f6;
            f4 = f7;
            f5 = f8;
        }

        float f25 = f3 * 0.9F;
        float f26 = f4 * 0.9F;
        float f27 = f5 * 0.9F;
        float f9 = f3 * 0.7F;
        float f10 = f4 * 0.7F;
        float f11 = f5 * 0.7F;
        float f12 = f3 * 0.8F;
        float f13 = f4 * 0.8F;
        float f14 = f5 * 0.8F;
        float f15 = 0.00390625F;
        float f16 = (float)MathHelper.floor(d4) * 0.00390625F;
        float f17 = (float)MathHelper.floor(d5) * 0.00390625F;
        float f18 = (float)(d4 - (double)MathHelper.floor(d4));
        float f19 = (float)(d5 - (double)MathHelper.floor(d5));
        int i2 = 8;
        int j2 = 4;
        float f20 = 9.765625E-4F;
        GlStateManager.func_179152_a(12.0F, 1.0F, 12.0F);

        for (int k2 = 0; k2 < 2; ++k2)
        {
            if (k2 == 0)
            {
                GlStateManager.func_179135_a(false, false, false, false);
            }
            else
            {
                switch (p_180445_2_)
                {
                    case 0:
                        GlStateManager.func_179135_a(false, true, true, true);
                        break;

                    case 1:
                        GlStateManager.func_179135_a(true, false, false, true);
                        break;

                    case 2:
                        GlStateManager.func_179135_a(true, true, true, true);
                }
            }

            for (int l2 = -3; l2 <= 4; ++l2)
            {
                for (int i3 = -3; i3 <= 4; ++i3)
                {
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                    float f21 = (float)(l2 * 8);
                    float f22 = (float)(i3 * 8);
                    float f23 = f21 - f18;
                    float f24 = f22 - f19;

                    if (f2 > -5.0F)
                    {
                        bufferbuilder.func_181662_b((double)(f23 + 0.0F), (double)(f2 + 0.0F), (double)(f24 + 8.0F)).func_187315_a((double)((f21 + 0.0F) * 0.00390625F + f16), (double)((f22 + 8.0F) * 0.00390625F + f17)).func_181666_a(f9, f10, f11, 0.8F).func_181663_c(0.0F, -1.0F, 0.0F).endVertex();
                        bufferbuilder.func_181662_b((double)(f23 + 8.0F), (double)(f2 + 0.0F), (double)(f24 + 8.0F)).func_187315_a((double)((f21 + 8.0F) * 0.00390625F + f16), (double)((f22 + 8.0F) * 0.00390625F + f17)).func_181666_a(f9, f10, f11, 0.8F).func_181663_c(0.0F, -1.0F, 0.0F).endVertex();
                        bufferbuilder.func_181662_b((double)(f23 + 8.0F), (double)(f2 + 0.0F), (double)(f24 + 0.0F)).func_187315_a((double)((f21 + 8.0F) * 0.00390625F + f16), (double)((f22 + 0.0F) * 0.00390625F + f17)).func_181666_a(f9, f10, f11, 0.8F).func_181663_c(0.0F, -1.0F, 0.0F).endVertex();
                        bufferbuilder.func_181662_b((double)(f23 + 0.0F), (double)(f2 + 0.0F), (double)(f24 + 0.0F)).func_187315_a((double)((f21 + 0.0F) * 0.00390625F + f16), (double)((f22 + 0.0F) * 0.00390625F + f17)).func_181666_a(f9, f10, f11, 0.8F).func_181663_c(0.0F, -1.0F, 0.0F).endVertex();
                    }

                    if (f2 <= 5.0F)
                    {
                        bufferbuilder.func_181662_b((double)(f23 + 0.0F), (double)(f2 + 4.0F - 9.765625E-4F), (double)(f24 + 8.0F)).func_187315_a((double)((f21 + 0.0F) * 0.00390625F + f16), (double)((f22 + 8.0F) * 0.00390625F + f17)).func_181666_a(f3, f4, f5, 0.8F).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
                        bufferbuilder.func_181662_b((double)(f23 + 8.0F), (double)(f2 + 4.0F - 9.765625E-4F), (double)(f24 + 8.0F)).func_187315_a((double)((f21 + 8.0F) * 0.00390625F + f16), (double)((f22 + 8.0F) * 0.00390625F + f17)).func_181666_a(f3, f4, f5, 0.8F).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
                        bufferbuilder.func_181662_b((double)(f23 + 8.0F), (double)(f2 + 4.0F - 9.765625E-4F), (double)(f24 + 0.0F)).func_187315_a((double)((f21 + 8.0F) * 0.00390625F + f16), (double)((f22 + 0.0F) * 0.00390625F + f17)).func_181666_a(f3, f4, f5, 0.8F).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
                        bufferbuilder.func_181662_b((double)(f23 + 0.0F), (double)(f2 + 4.0F - 9.765625E-4F), (double)(f24 + 0.0F)).func_187315_a((double)((f21 + 0.0F) * 0.00390625F + f16), (double)((f22 + 0.0F) * 0.00390625F + f17)).func_181666_a(f3, f4, f5, 0.8F).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
                    }

                    if (l2 > -1)
                    {
                        for (int j3 = 0; j3 < 8; ++j3)
                        {
                            bufferbuilder.func_181662_b((double)(f23 + (float)j3 + 0.0F), (double)(f2 + 0.0F), (double)(f24 + 8.0F)).func_187315_a((double)((f21 + (float)j3 + 0.5F) * 0.00390625F + f16), (double)((f22 + 8.0F) * 0.00390625F + f17)).func_181666_a(f25, f26, f27, 0.8F).func_181663_c(-1.0F, 0.0F, 0.0F).endVertex();
                            bufferbuilder.func_181662_b((double)(f23 + (float)j3 + 0.0F), (double)(f2 + 4.0F), (double)(f24 + 8.0F)).func_187315_a((double)((f21 + (float)j3 + 0.5F) * 0.00390625F + f16), (double)((f22 + 8.0F) * 0.00390625F + f17)).func_181666_a(f25, f26, f27, 0.8F).func_181663_c(-1.0F, 0.0F, 0.0F).endVertex();
                            bufferbuilder.func_181662_b((double)(f23 + (float)j3 + 0.0F), (double)(f2 + 4.0F), (double)(f24 + 0.0F)).func_187315_a((double)((f21 + (float)j3 + 0.5F) * 0.00390625F + f16), (double)((f22 + 0.0F) * 0.00390625F + f17)).func_181666_a(f25, f26, f27, 0.8F).func_181663_c(-1.0F, 0.0F, 0.0F).endVertex();
                            bufferbuilder.func_181662_b((double)(f23 + (float)j3 + 0.0F), (double)(f2 + 0.0F), (double)(f24 + 0.0F)).func_187315_a((double)((f21 + (float)j3 + 0.5F) * 0.00390625F + f16), (double)((f22 + 0.0F) * 0.00390625F + f17)).func_181666_a(f25, f26, f27, 0.8F).func_181663_c(-1.0F, 0.0F, 0.0F).endVertex();
                        }
                    }

                    if (l2 <= 1)
                    {
                        for (int k3 = 0; k3 < 8; ++k3)
                        {
                            bufferbuilder.func_181662_b((double)(f23 + (float)k3 + 1.0F - 9.765625E-4F), (double)(f2 + 0.0F), (double)(f24 + 8.0F)).func_187315_a((double)((f21 + (float)k3 + 0.5F) * 0.00390625F + f16), (double)((f22 + 8.0F) * 0.00390625F + f17)).func_181666_a(f25, f26, f27, 0.8F).func_181663_c(1.0F, 0.0F, 0.0F).endVertex();
                            bufferbuilder.func_181662_b((double)(f23 + (float)k3 + 1.0F - 9.765625E-4F), (double)(f2 + 4.0F), (double)(f24 + 8.0F)).func_187315_a((double)((f21 + (float)k3 + 0.5F) * 0.00390625F + f16), (double)((f22 + 8.0F) * 0.00390625F + f17)).func_181666_a(f25, f26, f27, 0.8F).func_181663_c(1.0F, 0.0F, 0.0F).endVertex();
                            bufferbuilder.func_181662_b((double)(f23 + (float)k3 + 1.0F - 9.765625E-4F), (double)(f2 + 4.0F), (double)(f24 + 0.0F)).func_187315_a((double)((f21 + (float)k3 + 0.5F) * 0.00390625F + f16), (double)((f22 + 0.0F) * 0.00390625F + f17)).func_181666_a(f25, f26, f27, 0.8F).func_181663_c(1.0F, 0.0F, 0.0F).endVertex();
                            bufferbuilder.func_181662_b((double)(f23 + (float)k3 + 1.0F - 9.765625E-4F), (double)(f2 + 0.0F), (double)(f24 + 0.0F)).func_187315_a((double)((f21 + (float)k3 + 0.5F) * 0.00390625F + f16), (double)((f22 + 0.0F) * 0.00390625F + f17)).func_181666_a(f25, f26, f27, 0.8F).func_181663_c(1.0F, 0.0F, 0.0F).endVertex();
                        }
                    }

                    if (i3 > -1)
                    {
                        for (int l3 = 0; l3 < 8; ++l3)
                        {
                            bufferbuilder.func_181662_b((double)(f23 + 0.0F), (double)(f2 + 4.0F), (double)(f24 + (float)l3 + 0.0F)).func_187315_a((double)((f21 + 0.0F) * 0.00390625F + f16), (double)((f22 + (float)l3 + 0.5F) * 0.00390625F + f17)).func_181666_a(f12, f13, f14, 0.8F).func_181663_c(0.0F, 0.0F, -1.0F).endVertex();
                            bufferbuilder.func_181662_b((double)(f23 + 8.0F), (double)(f2 + 4.0F), (double)(f24 + (float)l3 + 0.0F)).func_187315_a((double)((f21 + 8.0F) * 0.00390625F + f16), (double)((f22 + (float)l3 + 0.5F) * 0.00390625F + f17)).func_181666_a(f12, f13, f14, 0.8F).func_181663_c(0.0F, 0.0F, -1.0F).endVertex();
                            bufferbuilder.func_181662_b((double)(f23 + 8.0F), (double)(f2 + 0.0F), (double)(f24 + (float)l3 + 0.0F)).func_187315_a((double)((f21 + 8.0F) * 0.00390625F + f16), (double)((f22 + (float)l3 + 0.5F) * 0.00390625F + f17)).func_181666_a(f12, f13, f14, 0.8F).func_181663_c(0.0F, 0.0F, -1.0F).endVertex();
                            bufferbuilder.func_181662_b((double)(f23 + 0.0F), (double)(f2 + 0.0F), (double)(f24 + (float)l3 + 0.0F)).func_187315_a((double)((f21 + 0.0F) * 0.00390625F + f16), (double)((f22 + (float)l3 + 0.5F) * 0.00390625F + f17)).func_181666_a(f12, f13, f14, 0.8F).func_181663_c(0.0F, 0.0F, -1.0F).endVertex();
                        }
                    }

                    if (i3 <= 1)
                    {
                        for (int i4 = 0; i4 < 8; ++i4)
                        {
                            bufferbuilder.func_181662_b((double)(f23 + 0.0F), (double)(f2 + 4.0F), (double)(f24 + (float)i4 + 1.0F - 9.765625E-4F)).func_187315_a((double)((f21 + 0.0F) * 0.00390625F + f16), (double)((f22 + (float)i4 + 0.5F) * 0.00390625F + f17)).func_181666_a(f12, f13, f14, 0.8F).func_181663_c(0.0F, 0.0F, 1.0F).endVertex();
                            bufferbuilder.func_181662_b((double)(f23 + 8.0F), (double)(f2 + 4.0F), (double)(f24 + (float)i4 + 1.0F - 9.765625E-4F)).func_187315_a((double)((f21 + 8.0F) * 0.00390625F + f16), (double)((f22 + (float)i4 + 0.5F) * 0.00390625F + f17)).func_181666_a(f12, f13, f14, 0.8F).func_181663_c(0.0F, 0.0F, 1.0F).endVertex();
                            bufferbuilder.func_181662_b((double)(f23 + 8.0F), (double)(f2 + 0.0F), (double)(f24 + (float)i4 + 1.0F - 9.765625E-4F)).func_187315_a((double)((f21 + 8.0F) * 0.00390625F + f16), (double)((f22 + (float)i4 + 0.5F) * 0.00390625F + f17)).func_181666_a(f12, f13, f14, 0.8F).func_181663_c(0.0F, 0.0F, 1.0F).endVertex();
                            bufferbuilder.func_181662_b((double)(f23 + 0.0F), (double)(f2 + 0.0F), (double)(f24 + (float)i4 + 1.0F - 9.765625E-4F)).func_187315_a((double)((f21 + 0.0F) * 0.00390625F + f16), (double)((f22 + (float)i4 + 0.5F) * 0.00390625F + f17)).func_181666_a(f12, f13, f14, 0.8F).func_181663_c(0.0F, 0.0F, 1.0F).endVertex();
                        }
                    }

                    tessellator.draw();
                }
            }
        }

        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179084_k();
        GlStateManager.func_179089_o();
    }

    public void updateChunks(long finishTimeNano)
    {
        this.displayListEntitiesDirty |= this.renderDispatcher.func_178516_a(finishTimeNano);

        if (!this.chunksToUpdate.isEmpty())
        {
            Iterator<RenderChunk> iterator = this.chunksToUpdate.iterator();

            while (iterator.hasNext())
            {
                RenderChunk renderchunk1 = iterator.next();
                boolean flag1;

                if (renderchunk1.needsImmediateUpdate())
                {
                    flag1 = this.renderDispatcher.func_178505_b(renderchunk1);
                }
                else
                {
                    flag1 = this.renderDispatcher.func_178507_a(renderchunk1);
                }

                if (!flag1)
                {
                    break;
                }

                renderchunk1.clearNeedsUpdate();
                iterator.remove();
                long k1 = finishTimeNano - System.nanoTime();

                if (k1 < 0L)
                {
                    break;
                }
            }
        }
    }

    public void func_180449_a(Entity p_180449_1_, float p_180449_2_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        WorldBorder worldborder = this.world.getWorldBorder();
        double d3 = (double)(this.mc.gameSettings.renderDistanceChunks * 16);

        if (p_180449_1_.posX >= worldborder.maxX() - d3 || p_180449_1_.posX <= worldborder.minX() + d3 || p_180449_1_.posZ >= worldborder.maxZ() - d3 || p_180449_1_.posZ <= worldborder.minZ() + d3)
        {
            double d4 = 1.0D - worldborder.getClosestDistance(p_180449_1_) / d3;
            d4 = Math.pow(d4, 4.0D);
            double d5 = p_180449_1_.lastTickPosX + (p_180449_1_.posX - p_180449_1_.lastTickPosX) * (double)p_180449_2_;
            double d6 = p_180449_1_.lastTickPosY + (p_180449_1_.posY - p_180449_1_.lastTickPosY) * (double)p_180449_2_;
            double d7 = p_180449_1_.lastTickPosZ + (p_180449_1_.posZ - p_180449_1_.lastTickPosZ) * (double)p_180449_2_;
            GlStateManager.func_179147_l();
            GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            this.textureManager.bindTexture(FORCEFIELD_TEXTURES);
            GlStateManager.func_179132_a(false);
            GlStateManager.func_179094_E();
            int k1 = worldborder.getStatus().getColor();
            float f = (float)(k1 >> 16 & 255) / 255.0F;
            float f1 = (float)(k1 >> 8 & 255) / 255.0F;
            float f2 = (float)(k1 & 255) / 255.0F;
            GlStateManager.func_179131_c(f, f1, f2, (float)d4);
            GlStateManager.func_179136_a(-3.0F, -3.0F);
            GlStateManager.func_179088_q();
            GlStateManager.func_179092_a(516, 0.1F);
            GlStateManager.func_179141_d();
            GlStateManager.func_179129_p();
            float f3 = (float)(Minecraft.func_71386_F() % 3000L) / 3000.0F;
            float f4 = 0.0F;
            float f5 = 0.0F;
            float f6 = 128.0F;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.func_178969_c(-d5, -d6, -d7);
            double d8 = Math.max((double)MathHelper.floor(d7 - d3), worldborder.minZ());
            double d9 = Math.min((double)MathHelper.ceil(d7 + d3), worldborder.maxZ());

            if (d5 > worldborder.maxX() - d3)
            {
                float f7 = 0.0F;

                for (double d10 = d8; d10 < d9; f7 += 0.5F)
                {
                    double d11 = Math.min(1.0D, d9 - d10);
                    float f8 = (float)d11 * 0.5F;
                    bufferbuilder.func_181662_b(worldborder.maxX(), 256.0D, d10).func_187315_a((double)(f3 + f7), (double)(f3 + 0.0F)).endVertex();
                    bufferbuilder.func_181662_b(worldborder.maxX(), 256.0D, d10 + d11).func_187315_a((double)(f3 + f8 + f7), (double)(f3 + 0.0F)).endVertex();
                    bufferbuilder.func_181662_b(worldborder.maxX(), 0.0D, d10 + d11).func_187315_a((double)(f3 + f8 + f7), (double)(f3 + 128.0F)).endVertex();
                    bufferbuilder.func_181662_b(worldborder.maxX(), 0.0D, d10).func_187315_a((double)(f3 + f7), (double)(f3 + 128.0F)).endVertex();
                    ++d10;
                }
            }

            if (d5 < worldborder.minX() + d3)
            {
                float f9 = 0.0F;

                for (double d12 = d8; d12 < d9; f9 += 0.5F)
                {
                    double d15 = Math.min(1.0D, d9 - d12);
                    float f12 = (float)d15 * 0.5F;
                    bufferbuilder.func_181662_b(worldborder.minX(), 256.0D, d12).func_187315_a((double)(f3 + f9), (double)(f3 + 0.0F)).endVertex();
                    bufferbuilder.func_181662_b(worldborder.minX(), 256.0D, d12 + d15).func_187315_a((double)(f3 + f12 + f9), (double)(f3 + 0.0F)).endVertex();
                    bufferbuilder.func_181662_b(worldborder.minX(), 0.0D, d12 + d15).func_187315_a((double)(f3 + f12 + f9), (double)(f3 + 128.0F)).endVertex();
                    bufferbuilder.func_181662_b(worldborder.minX(), 0.0D, d12).func_187315_a((double)(f3 + f9), (double)(f3 + 128.0F)).endVertex();
                    ++d12;
                }
            }

            d8 = Math.max((double)MathHelper.floor(d5 - d3), worldborder.minX());
            d9 = Math.min((double)MathHelper.ceil(d5 + d3), worldborder.maxX());

            if (d7 > worldborder.maxZ() - d3)
            {
                float f10 = 0.0F;

                for (double d13 = d8; d13 < d9; f10 += 0.5F)
                {
                    double d16 = Math.min(1.0D, d9 - d13);
                    float f13 = (float)d16 * 0.5F;
                    bufferbuilder.func_181662_b(d13, 256.0D, worldborder.maxZ()).func_187315_a((double)(f3 + f10), (double)(f3 + 0.0F)).endVertex();
                    bufferbuilder.func_181662_b(d13 + d16, 256.0D, worldborder.maxZ()).func_187315_a((double)(f3 + f13 + f10), (double)(f3 + 0.0F)).endVertex();
                    bufferbuilder.func_181662_b(d13 + d16, 0.0D, worldborder.maxZ()).func_187315_a((double)(f3 + f13 + f10), (double)(f3 + 128.0F)).endVertex();
                    bufferbuilder.func_181662_b(d13, 0.0D, worldborder.maxZ()).func_187315_a((double)(f3 + f10), (double)(f3 + 128.0F)).endVertex();
                    ++d13;
                }
            }

            if (d7 < worldborder.minZ() + d3)
            {
                float f11 = 0.0F;

                for (double d14 = d8; d14 < d9; f11 += 0.5F)
                {
                    double d17 = Math.min(1.0D, d9 - d14);
                    float f14 = (float)d17 * 0.5F;
                    bufferbuilder.func_181662_b(d14, 256.0D, worldborder.minZ()).func_187315_a((double)(f3 + f11), (double)(f3 + 0.0F)).endVertex();
                    bufferbuilder.func_181662_b(d14 + d17, 256.0D, worldborder.minZ()).func_187315_a((double)(f3 + f14 + f11), (double)(f3 + 0.0F)).endVertex();
                    bufferbuilder.func_181662_b(d14 + d17, 0.0D, worldborder.minZ()).func_187315_a((double)(f3 + f14 + f11), (double)(f3 + 128.0F)).endVertex();
                    bufferbuilder.func_181662_b(d14, 0.0D, worldborder.minZ()).func_187315_a((double)(f3 + f11), (double)(f3 + 128.0F)).endVertex();
                    ++d14;
                }
            }

            tessellator.draw();
            bufferbuilder.func_178969_c(0.0D, 0.0D, 0.0D);
            GlStateManager.func_179089_o();
            GlStateManager.func_179118_c();
            GlStateManager.func_179136_a(0.0F, 0.0F);
            GlStateManager.func_179113_r();
            GlStateManager.func_179141_d();
            GlStateManager.func_179084_k();
            GlStateManager.func_179121_F();
            GlStateManager.func_179132_a(true);
        }
    }

    private void func_180443_s()
    {
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179147_l();
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.5F);
        GlStateManager.func_179136_a(-3.0F, -3.0F);
        GlStateManager.func_179088_q();
        GlStateManager.func_179092_a(516, 0.1F);
        GlStateManager.func_179141_d();
        GlStateManager.func_179094_E();
    }

    private void func_174969_t()
    {
        GlStateManager.func_179118_c();
        GlStateManager.func_179136_a(0.0F, 0.0F);
        GlStateManager.func_179113_r();
        GlStateManager.func_179141_d();
        GlStateManager.func_179132_a(true);
        GlStateManager.func_179121_F();
    }

    public void func_174981_a(Tessellator p_174981_1_, BufferBuilder p_174981_2_, Entity p_174981_3_, float p_174981_4_)
    {
        double d3 = p_174981_3_.lastTickPosX + (p_174981_3_.posX - p_174981_3_.lastTickPosX) * (double)p_174981_4_;
        double d4 = p_174981_3_.lastTickPosY + (p_174981_3_.posY - p_174981_3_.lastTickPosY) * (double)p_174981_4_;
        double d5 = p_174981_3_.lastTickPosZ + (p_174981_3_.posZ - p_174981_3_.lastTickPosZ) * (double)p_174981_4_;

        if (!this.damagedBlocks.isEmpty())
        {
            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.func_180443_s();
            p_174981_2_.begin(7, DefaultVertexFormats.BLOCK);
            p_174981_2_.func_178969_c(-d3, -d4, -d5);
            p_174981_2_.func_78914_f();
            Iterator<DestroyBlockProgress> iterator = this.damagedBlocks.values().iterator();

            while (iterator.hasNext())
            {
                DestroyBlockProgress destroyblockprogress = iterator.next();
                BlockPos blockpos = destroyblockprogress.getPosition();
                double d6 = (double)blockpos.getX() - d3;
                double d7 = (double)blockpos.getY() - d4;
                double d8 = (double)blockpos.getZ() - d5;
                Block block = this.world.getBlockState(blockpos).getBlock();

                if (!(block instanceof BlockChest) && !(block instanceof BlockEnderChest) && !(block instanceof BlockSign) && !(block instanceof BlockSkull))
                {
                    if (d6 * d6 + d7 * d7 + d8 * d8 > 1024.0D)
                    {
                        iterator.remove();
                    }
                    else
                    {
                        IBlockState iblockstate = this.world.getBlockState(blockpos);

                        if (iblockstate.getMaterial() != Material.AIR)
                        {
                            int k1 = destroyblockprogress.getPartialBlockDamage();
                            TextureAtlasSprite textureatlassprite = this.field_94141_F[k1];
                            BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
                            blockrendererdispatcher.func_175020_a(iblockstate, blockpos, textureatlassprite, this.world);
                        }
                    }
                }
            }

            p_174981_1_.draw();
            p_174981_2_.func_178969_c(0.0D, 0.0D, 0.0D);
            this.func_174969_t();
        }
    }

    public void func_72731_b(EntityPlayer p_72731_1_, RayTraceResult p_72731_2_, int p_72731_3_, float p_72731_4_)
    {
        if (p_72731_3_ == 0 && p_72731_2_.field_72313_a == RayTraceResult.Type.BLOCK)
        {
            GlStateManager.func_179147_l();
            GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.func_187441_d(2.0F);
            GlStateManager.func_179090_x();
            GlStateManager.func_179132_a(false);
            BlockPos blockpos = p_72731_2_.func_178782_a();
            IBlockState iblockstate = this.world.getBlockState(blockpos);

            if (iblockstate.getMaterial() != Material.AIR && this.world.getWorldBorder().contains(blockpos))
            {
                double d3 = p_72731_1_.lastTickPosX + (p_72731_1_.posX - p_72731_1_.lastTickPosX) * (double)p_72731_4_;
                double d4 = p_72731_1_.lastTickPosY + (p_72731_1_.posY - p_72731_1_.lastTickPosY) * (double)p_72731_4_;
                double d5 = p_72731_1_.lastTickPosZ + (p_72731_1_.posZ - p_72731_1_.lastTickPosZ) * (double)p_72731_4_;
                func_189697_a(iblockstate.func_185918_c(this.world, blockpos).grow(0.0020000000949949026D).offset(-d3, -d4, -d5), 0.0F, 0.0F, 0.0F, 0.4F);
            }

            GlStateManager.func_179132_a(true);
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
        }
    }

    public static void func_189697_a(AxisAlignedBB p_189697_0_, float p_189697_1_, float p_189697_2_, float p_189697_3_, float p_189697_4_)
    {
        func_189694_a(p_189697_0_.minX, p_189697_0_.minY, p_189697_0_.minZ, p_189697_0_.maxX, p_189697_0_.maxY, p_189697_0_.maxZ, p_189697_1_, p_189697_2_, p_189697_3_, p_189697_4_);
    }

    public static void func_189694_a(double p_189694_0_, double p_189694_2_, double p_189694_4_, double p_189694_6_, double p_189694_8_, double p_189694_10_, float p_189694_12_, float p_189694_13_, float p_189694_14_, float p_189694_15_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        func_189698_a(bufferbuilder, p_189694_0_, p_189694_2_, p_189694_4_, p_189694_6_, p_189694_8_, p_189694_10_, p_189694_12_, p_189694_13_, p_189694_14_, p_189694_15_);
        tessellator.draw();
    }

    public static void func_189698_a(BufferBuilder p_189698_0_, double p_189698_1_, double p_189698_3_, double p_189698_5_, double p_189698_7_, double p_189698_9_, double p_189698_11_, float p_189698_13_, float p_189698_14_, float p_189698_15_, float p_189698_16_)
    {
        p_189698_0_.func_181662_b(p_189698_1_, p_189698_3_, p_189698_5_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, 0.0F).endVertex();
        p_189698_0_.func_181662_b(p_189698_1_, p_189698_3_, p_189698_5_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, p_189698_16_).endVertex();
        p_189698_0_.func_181662_b(p_189698_7_, p_189698_3_, p_189698_5_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, p_189698_16_).endVertex();
        p_189698_0_.func_181662_b(p_189698_7_, p_189698_3_, p_189698_11_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, p_189698_16_).endVertex();
        p_189698_0_.func_181662_b(p_189698_1_, p_189698_3_, p_189698_11_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, p_189698_16_).endVertex();
        p_189698_0_.func_181662_b(p_189698_1_, p_189698_3_, p_189698_5_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, p_189698_16_).endVertex();
        p_189698_0_.func_181662_b(p_189698_1_, p_189698_9_, p_189698_5_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, p_189698_16_).endVertex();
        p_189698_0_.func_181662_b(p_189698_7_, p_189698_9_, p_189698_5_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, p_189698_16_).endVertex();
        p_189698_0_.func_181662_b(p_189698_7_, p_189698_9_, p_189698_11_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, p_189698_16_).endVertex();
        p_189698_0_.func_181662_b(p_189698_1_, p_189698_9_, p_189698_11_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, p_189698_16_).endVertex();
        p_189698_0_.func_181662_b(p_189698_1_, p_189698_9_, p_189698_5_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, p_189698_16_).endVertex();
        p_189698_0_.func_181662_b(p_189698_1_, p_189698_9_, p_189698_11_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, 0.0F).endVertex();
        p_189698_0_.func_181662_b(p_189698_1_, p_189698_3_, p_189698_11_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, p_189698_16_).endVertex();
        p_189698_0_.func_181662_b(p_189698_7_, p_189698_9_, p_189698_11_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, 0.0F).endVertex();
        p_189698_0_.func_181662_b(p_189698_7_, p_189698_3_, p_189698_11_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, p_189698_16_).endVertex();
        p_189698_0_.func_181662_b(p_189698_7_, p_189698_9_, p_189698_5_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, 0.0F).endVertex();
        p_189698_0_.func_181662_b(p_189698_7_, p_189698_3_, p_189698_5_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, p_189698_16_).endVertex();
        p_189698_0_.func_181662_b(p_189698_7_, p_189698_3_, p_189698_5_).func_181666_a(p_189698_13_, p_189698_14_, p_189698_15_, 0.0F).endVertex();
    }

    public static void func_189696_b(AxisAlignedBB p_189696_0_, float p_189696_1_, float p_189696_2_, float p_189696_3_, float p_189696_4_)
    {
        func_189695_b(p_189696_0_.minX, p_189696_0_.minY, p_189696_0_.minZ, p_189696_0_.maxX, p_189696_0_.maxY, p_189696_0_.maxZ, p_189696_1_, p_189696_2_, p_189696_3_, p_189696_4_);
    }

    public static void func_189695_b(double p_189695_0_, double p_189695_2_, double p_189695_4_, double p_189695_6_, double p_189695_8_, double p_189695_10_, float p_189695_12_, float p_189695_13_, float p_189695_14_, float p_189695_15_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        addChainedFilledBoxVertices(bufferbuilder, p_189695_0_, p_189695_2_, p_189695_4_, p_189695_6_, p_189695_8_, p_189695_10_, p_189695_12_, p_189695_13_, p_189695_14_, p_189695_15_);
        tessellator.draw();
    }

    public static void addChainedFilledBoxVertices(BufferBuilder builder, double x1, double y1, double z1, double x2, double y2, double z2, float red, float green, float blue, float alpha)
    {
        builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).endVertex();
        builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).endVertex();
    }

    private void func_184385_a(int p_184385_1_, int p_184385_2_, int p_184385_3_, int p_184385_4_, int p_184385_5_, int p_184385_6_, boolean p_184385_7_)
    {
        this.viewFrustum.func_187474_a(p_184385_1_, p_184385_2_, p_184385_3_, p_184385_4_, p_184385_5_, p_184385_6_, p_184385_7_);
    }

    public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags)
    {
        int k1 = pos.getX();
        int l1 = pos.getY();
        int i2 = pos.getZ();
        this.func_184385_a(k1 - 1, l1 - 1, i2 - 1, k1 + 1, l1 + 1, i2 + 1, (flags & 8) != 0);
    }

    public void func_174959_b(BlockPos p_174959_1_)
    {
        this.field_184387_ae.add(p_174959_1_.toImmutable());
    }

    /**
     * On the client, re-renders all blocks in this range, inclusive. On the server, does nothing.
     */
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        this.func_184385_a(x1 - 1, y1 - 1, z1 - 1, x2 + 1, y2 + 1, z2 + 1, false);
    }

    public void playRecord(@Nullable SoundEvent soundIn, BlockPos pos)
    {
        ISound isound = this.mapSoundPositions.get(pos);

        if (isound != null)
        {
            this.mc.getSoundHandler().stop(isound);
            this.mapSoundPositions.remove(pos);
        }

        if (soundIn != null)
        {
            ItemRecord itemrecord = ItemRecord.getBySound(soundIn);

            if (itemrecord != null)
            {
                this.mc.ingameGUI.setRecordPlayingMessage(itemrecord.func_150927_i());
            }

            ISound positionedsoundrecord = PositionedSoundRecord.record(soundIn, (float)pos.getX(), (float)pos.getY(), (float)pos.getZ());
            this.mapSoundPositions.put(pos, positionedsoundrecord);
            this.mc.getSoundHandler().play(positionedsoundrecord);
        }

        this.setPartying(this.world, pos, soundIn != null);
    }

    /**
     * Called when a record starts or stops playing. Used to make parrots start or stop partying.
     */
    private void setPartying(World worldIn, BlockPos pos, boolean isPartying)
    {
        for (EntityLivingBase entitylivingbase : worldIn.func_72872_a(EntityLivingBase.class, (new AxisAlignedBB(pos)).grow(3.0D)))
        {
            entitylivingbase.setPartying(pos, isPartying);
        }
    }

    public void func_184375_a(@Nullable EntityPlayer p_184375_1_, SoundEvent p_184375_2_, SoundCategory p_184375_3_, double p_184375_4_, double p_184375_6_, double p_184375_8_, float p_184375_10_, float p_184375_11_)
    {
    }

    public void func_180442_a(int p_180442_1_, boolean p_180442_2_, double p_180442_3_, double p_180442_5_, double p_180442_7_, double p_180442_9_, double p_180442_11_, double p_180442_13_, int... p_180442_15_)
    {
        this.func_190570_a(p_180442_1_, p_180442_2_, false, p_180442_3_, p_180442_5_, p_180442_7_, p_180442_9_, p_180442_11_, p_180442_13_, p_180442_15_);
    }

    public void func_190570_a(int p_190570_1_, boolean p_190570_2_, boolean p_190570_3_, final double p_190570_4_, final double p_190570_6_, final double p_190570_8_, double p_190570_10_, double p_190570_12_, double p_190570_14_, int... p_190570_16_)
    {
        try
        {
            this.func_190571_b(p_190570_1_, p_190570_2_, p_190570_3_, p_190570_4_, p_190570_6_, p_190570_8_, p_190570_10_, p_190570_12_, p_190570_14_, p_190570_16_);
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while adding particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being added");
            crashreportcategory.addDetail("ID", Integer.valueOf(p_190570_1_));

            if (p_190570_16_ != null)
            {
                crashreportcategory.addDetail("Parameters", p_190570_16_);
            }

            crashreportcategory.addDetail("Position", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    return CrashReportCategory.getCoordinateInfo(p_190570_4_, p_190570_6_, p_190570_8_);
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    private void func_174972_a(EnumParticleTypes p_174972_1_, double p_174972_2_, double p_174972_4_, double p_174972_6_, double p_174972_8_, double p_174972_10_, double p_174972_12_, int... p_174972_14_)
    {
        this.func_180442_a(p_174972_1_.func_179348_c(), p_174972_1_.func_179344_e(), p_174972_2_, p_174972_4_, p_174972_6_, p_174972_8_, p_174972_10_, p_174972_12_, p_174972_14_);
    }

    @Nullable
    private Particle func_174974_b(int p_174974_1_, boolean p_174974_2_, double p_174974_3_, double p_174974_5_, double p_174974_7_, double p_174974_9_, double p_174974_11_, double p_174974_13_, int... p_174974_15_)
    {
        return this.func_190571_b(p_174974_1_, p_174974_2_, false, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
    }

    @Nullable
    private Particle func_190571_b(int p_190571_1_, boolean p_190571_2_, boolean p_190571_3_, double p_190571_4_, double p_190571_6_, double p_190571_8_, double p_190571_10_, double p_190571_12_, double p_190571_14_, int... p_190571_16_)
    {
        Entity entity = this.mc.getRenderViewEntity();

        if (this.mc != null && entity != null && this.mc.particles != null)
        {
            int k1 = this.func_190572_a(p_190571_3_);
            double d3 = entity.posX - p_190571_4_;
            double d4 = entity.posY - p_190571_6_;
            double d5 = entity.posZ - p_190571_8_;

            if (p_190571_2_)
            {
                return this.mc.particles.func_178927_a(p_190571_1_, p_190571_4_, p_190571_6_, p_190571_8_, p_190571_10_, p_190571_12_, p_190571_14_, p_190571_16_);
            }
            else if (d3 * d3 + d4 * d4 + d5 * d5 > 1024.0D)
            {
                return null;
            }
            else
            {
                return k1 > 1 ? null : this.mc.particles.func_178927_a(p_190571_1_, p_190571_4_, p_190571_6_, p_190571_8_, p_190571_10_, p_190571_12_, p_190571_14_, p_190571_16_);
            }
        }
        else
        {
            return null;
        }
    }

    private int func_190572_a(boolean p_190572_1_)
    {
        int k1 = this.mc.gameSettings.particles;

        if (p_190572_1_ && k1 == 2 && this.world.rand.nextInt(10) == 0)
        {
            k1 = 1;
        }

        if (k1 == 1 && this.world.rand.nextInt(3) == 0)
        {
            k1 = 2;
        }

        return k1;
    }

    public void func_72703_a(Entity p_72703_1_)
    {
    }

    public void func_72709_b(Entity p_72709_1_)
    {
    }

    /**
     * Deletes all display lists
     */
    public void deleteAllDisplayLists()
    {
    }

    public void broadcastSound(int soundID, BlockPos pos, int data)
    {
        switch (soundID)
        {
            case 1023:
            case 1028:
            case 1038:
                Entity entity = this.mc.getRenderViewEntity();

                if (entity != null)
                {
                    double d3 = (double)pos.getX() - entity.posX;
                    double d4 = (double)pos.getY() - entity.posY;
                    double d5 = (double)pos.getZ() - entity.posZ;
                    double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                    double d7 = entity.posX;
                    double d8 = entity.posY;
                    double d9 = entity.posZ;

                    if (d6 > 0.0D)
                    {
                        d7 += d3 / d6 * 2.0D;
                        d8 += d4 / d6 * 2.0D;
                        d9 += d5 / d6 * 2.0D;
                    }

                    if (soundID == 1023)
                    {
                        this.world.playSound(d7, d8, d9, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.HOSTILE, 1.0F, 1.0F, false);
                    }
                    else if (soundID == 1038)
                    {
                        this.world.playSound(d7, d8, d9, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.HOSTILE, 1.0F, 1.0F, false);
                    }
                    else
                    {
                        this.world.playSound(d7, d8, d9, SoundEvents.ENTITY_ENDER_DRAGON_DEATH, SoundCategory.HOSTILE, 5.0F, 1.0F, false);
                    }
                }

            default:
        }
    }

    public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data)
    {
        Random random = this.world.rand;

        switch (type)
        {
            case 1000:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                break;

            case 1001:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.BLOCKS, 1.0F, 1.2F, false);
                break;

            case 1002:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_DISPENSER_LAUNCH, SoundCategory.BLOCKS, 1.0F, 1.2F, false);
                break;

            case 1003:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL, 1.0F, 1.2F, false);
                break;

            case 1004:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_FIREWORK_ROCKET_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.2F, false);
                break;

            case 1005:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1006:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_WOODEN_DOOR_OPEN, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1007:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1008:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_FENCE_GATE_OPEN, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1009:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F, false);
                break;

            case 1010:
                if (Item.getItemById(data) instanceof ItemRecord)
                {
                    this.world.func_184149_a(blockPosIn, ((ItemRecord)Item.getItemById(data)).getSound());
                }
                else
                {
                    this.world.func_184149_a(blockPosIn, (SoundEvent)null);
                }

                break;

            case 1011:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_IRON_DOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1012:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_WOODEN_DOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1013:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1014:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_FENCE_GATE_CLOSE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1015:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_GHAST_WARN, SoundCategory.HOSTILE, 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1016:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.HOSTILE, 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1017:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ENDER_DRAGON_SHOOT, SoundCategory.HOSTILE, 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1018:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1019:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1020:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1021:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1022:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1024:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1025:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.NEUTRAL, 0.05F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1026:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_INFECT, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1027:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.NEUTRAL, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;

            case 1029:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_ANVIL_DESTROY, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1030:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1031:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.3F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1032:
                this.mc.getSoundHandler().play(PositionedSoundRecord.master(SoundEvents.BLOCK_PORTAL_TRAVEL, random.nextFloat() * 0.4F + 0.8F));
                break;

            case 1033:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_CHORUS_FLOWER_GROW, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                break;

            case 1034:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_CHORUS_FLOWER_DEATH, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                break;

            case 1035:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                break;

            case 1036:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 1037:
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 2000:
                int j2 = data % 3 - 1;
                int k1 = data / 3 % 3 - 1;
                double d11 = (double)blockPosIn.getX() + (double)j2 * 0.6D + 0.5D;
                double d13 = (double)blockPosIn.getY() + 0.5D;
                double d15 = (double)blockPosIn.getZ() + (double)k1 * 0.6D + 0.5D;

                for (int l2 = 0; l2 < 10; ++l2)
                {
                    double d16 = random.nextDouble() * 0.2D + 0.01D;
                    double d19 = d11 + (double)j2 * 0.01D + (random.nextDouble() - 0.5D) * (double)k1 * 0.5D;
                    double d22 = d13 + (random.nextDouble() - 0.5D) * 0.5D;
                    double d25 = d15 + (double)k1 * 0.01D + (random.nextDouble() - 0.5D) * (double)j2 * 0.5D;
                    double d27 = (double)j2 * d16 + random.nextGaussian() * 0.01D;
                    double d29 = -0.03D + random.nextGaussian() * 0.01D;
                    double d30 = (double)k1 * d16 + random.nextGaussian() * 0.01D;
                    this.func_174972_a(EnumParticleTypes.SMOKE_NORMAL, d19, d22, d25, d27, d29, d30);
                }

                return;

            case 2001:
                Block block = Block.func_149729_e(data & 4095);

                if (block.getDefaultState().getMaterial() != Material.AIR)
                {
                    SoundType soundtype = block.func_185467_w();
                    this.world.playSound(blockPosIn, soundtype.getBreakSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F, false);
                }

                this.mc.particles.addBlockDestroyEffects(blockPosIn, block.func_176203_a(data >> 12 & 255));
                break;

            case 2002:
            case 2007:
                double d9 = (double)blockPosIn.getX();
                double d10 = (double)blockPosIn.getY();
                double d12 = (double)blockPosIn.getZ();

                for (int k2 = 0; k2 < 8; ++k2)
                {
                    this.func_174972_a(EnumParticleTypes.ITEM_CRACK, d9, d10, d12, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D, Item.getIdFromItem(Items.SPLASH_POTION));
                }

                float f5 = (float)(data >> 16 & 255) / 255.0F;
                float f = (float)(data >> 8 & 255) / 255.0F;
                float f1 = (float)(data >> 0 & 255) / 255.0F;
                EnumParticleTypes enumparticletypes = type == 2007 ? EnumParticleTypes.SPELL_INSTANT : EnumParticleTypes.SPELL;

                for (int j3 = 0; j3 < 100; ++j3)
                {
                    double d18 = random.nextDouble() * 4.0D;
                    double d21 = random.nextDouble() * Math.PI * 2.0D;
                    double d24 = Math.cos(d21) * d18;
                    double d26 = 0.01D + random.nextDouble() * 0.5D;
                    double d28 = Math.sin(d21) * d18;
                    Particle particle1 = this.func_174974_b(enumparticletypes.func_179348_c(), enumparticletypes.func_179344_e(), d9 + d24 * 0.1D, d10 + 0.3D, d12 + d28 * 0.1D, d24, d26, d28);

                    if (particle1 != null)
                    {
                        float f4 = 0.75F + random.nextFloat() * 0.25F;
                        particle1.setColor(f5 * f4, f * f4, f1 * f4);
                        particle1.multiplyVelocity((float)d18);
                    }
                }

                this.world.playSound(blockPosIn, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.NEUTRAL, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 2003:
                double d3 = (double)blockPosIn.getX() + 0.5D;
                double d4 = (double)blockPosIn.getY();
                double d5 = (double)blockPosIn.getZ() + 0.5D;

                for (int l1 = 0; l1 < 8; ++l1)
                {
                    this.func_174972_a(EnumParticleTypes.ITEM_CRACK, d3, d4, d5, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D, Item.getIdFromItem(Items.ENDER_EYE));
                }

                for (double d14 = 0.0D; d14 < (Math.PI * 2D); d14 += 0.15707963267948966D)
                {
                    this.func_174972_a(EnumParticleTypes.PORTAL, d3 + Math.cos(d14) * 5.0D, d4 - 0.4D, d5 + Math.sin(d14) * 5.0D, Math.cos(d14) * -5.0D, 0.0D, Math.sin(d14) * -5.0D);
                    this.func_174972_a(EnumParticleTypes.PORTAL, d3 + Math.cos(d14) * 5.0D, d4 - 0.4D, d5 + Math.sin(d14) * 5.0D, Math.cos(d14) * -7.0D, 0.0D, Math.sin(d14) * -7.0D);
                }

                return;

            case 2004:
                for (int i3 = 0; i3 < 20; ++i3)
                {
                    double d17 = (double)blockPosIn.getX() + 0.5D + ((double)this.world.rand.nextFloat() - 0.5D) * 2.0D;
                    double d20 = (double)blockPosIn.getY() + 0.5D + ((double)this.world.rand.nextFloat() - 0.5D) * 2.0D;
                    double d23 = (double)blockPosIn.getZ() + 0.5D + ((double)this.world.rand.nextFloat() - 0.5D) * 2.0D;
                    this.world.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d17, d20, d23, 0.0D, 0.0D, 0.0D, new int[0]);
                    this.world.func_175688_a(EnumParticleTypes.FLAME, d17, d20, d23, 0.0D, 0.0D, 0.0D, new int[0]);
                }

                return;

            case 2005:
                ItemDye.func_180617_a(this.world, blockPosIn, data);
                break;

            case 2006:
                for (int i2 = 0; i2 < 200; ++i2)
                {
                    float f2 = random.nextFloat() * 4.0F;
                    float f3 = random.nextFloat() * ((float)Math.PI * 2F);
                    double d6 = (double)(MathHelper.cos(f3) * f2);
                    double d7 = 0.01D + random.nextDouble() * 0.5D;
                    double d8 = (double)(MathHelper.sin(f3) * f2);
                    Particle particle = this.func_174974_b(EnumParticleTypes.DRAGON_BREATH.func_179348_c(), false, (double)blockPosIn.getX() + d6 * 0.1D, (double)blockPosIn.getY() + 0.3D, (double)blockPosIn.getZ() + d8 * 0.1D, d6, d7, d8);

                    if (particle != null)
                    {
                        particle.multiplyVelocity(f2);
                    }
                }

                this.world.playSound(blockPosIn, SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.HOSTILE, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F, false);
                break;

            case 3000:
                this.world.func_175682_a(EnumParticleTypes.EXPLOSION_HUGE, true, (double)blockPosIn.getX() + 0.5D, (double)blockPosIn.getY() + 0.5D, (double)blockPosIn.getZ() + 0.5D, 0.0D, 0.0D, 0.0D, new int[0]);
                this.world.playSound(blockPosIn, SoundEvents.BLOCK_END_GATEWAY_SPAWN, SoundCategory.BLOCKS, 10.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F, false);
                break;

            case 3001:
                this.world.playSound(blockPosIn, SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.HOSTILE, 64.0F, 0.8F + this.world.rand.nextFloat() * 0.3F, false);
        }
    }

    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress)
    {
        if (progress >= 0 && progress < 10)
        {
            DestroyBlockProgress destroyblockprogress = this.damagedBlocks.get(Integer.valueOf(breakerId));

            if (destroyblockprogress == null || destroyblockprogress.getPosition().getX() != pos.getX() || destroyblockprogress.getPosition().getY() != pos.getY() || destroyblockprogress.getPosition().getZ() != pos.getZ())
            {
                destroyblockprogress = new DestroyBlockProgress(breakerId, pos);
                this.damagedBlocks.put(Integer.valueOf(breakerId), destroyblockprogress);
            }

            destroyblockprogress.setPartialBlockDamage(progress);
            destroyblockprogress.setCloudUpdateTick(this.ticks);
        }
        else
        {
            this.damagedBlocks.remove(Integer.valueOf(breakerId));
        }
    }

    public boolean hasNoChunkUpdates()
    {
        return this.chunksToUpdate.isEmpty() && this.renderDispatcher.hasNoChunkUpdates();
    }

    public void setDisplayListEntitiesDirty()
    {
        this.displayListEntitiesDirty = true;
    }

    public void updateTileEntities(Collection<TileEntity> tileEntitiesToRemove, Collection<TileEntity> tileEntitiesToAdd)
    {
        synchronized (this.setTileEntities)
        {
            this.setTileEntities.removeAll(tileEntitiesToRemove);
            this.setTileEntities.addAll(tileEntitiesToAdd);
        }
    }

    class ContainerLocalRenderInformation
    {
        final RenderChunk renderChunk;
        final EnumFacing facing;
        byte setFacing;
        final int counter;

        private ContainerLocalRenderInformation(RenderChunk renderChunkIn, EnumFacing facingIn, @Nullable int counterIn)
        {
            this.renderChunk = renderChunkIn;
            this.facing = facingIn;
            this.counter = counterIn;
        }

        public void setDirection(byte dir, EnumFacing facingIn)
        {
            this.setFacing = (byte)(this.setFacing | dir | 1 << facingIn.ordinal());
        }

        public boolean hasDirection(EnumFacing facingIn)
        {
            return (this.setFacing & 1 << facingIn.ordinal()) > 0;
        }
    }
}

package net.minecraft.client.renderer;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonSyntaxException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class EntityRenderer implements IResourceManagerReloadListener
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation field_110924_q = new ResourceLocation("textures/environment/rain.png");
    private static final ResourceLocation field_110923_r = new ResourceLocation("textures/environment/snow.png");
    public static boolean field_78517_a;
    public static int field_78515_b;
    private final Minecraft mc;
    private final IResourceManager resourceManager;
    private final Random random = new Random();
    private float farPlaneDistance;
    public final ItemRenderer itemRenderer;
    private final MapItemRenderer mapItemRenderer;
    private int rendererUpdateCount;
    private Entity field_78528_u;
    private final MouseFilter field_78527_v = new MouseFilter();
    private final MouseFilter field_78526_w = new MouseFilter();
    private final float field_78490_B = 4.0F;
    private float field_78491_C = 4.0F;
    private float field_78496_H;
    private float field_78497_I;
    private float field_78498_J;
    private float field_78499_K;
    private float field_78492_L;
    private float fovModifierHand;
    private float fovModifierHandPrev;
    private float bossColorModifier;
    private float bossColorModifierPrev;
    private boolean field_78500_U;
    private boolean renderHand = true;
    private boolean drawBlockOutline = true;
    private long timeWorldIcon;
    private long prevFrameTime = Minecraft.func_71386_F();
    private long field_78510_Z;
    private final DynamicTexture lightmapTexture;
    private final int[] field_78504_Q;
    private final ResourceLocation field_110922_T;
    private boolean field_78536_aa;
    private float field_78514_e;
    private float field_175075_L;
    private int field_78534_ac;
    private final float[] field_175076_N = new float[1024];
    private final float[] field_175077_O = new float[1024];
    private final FloatBuffer field_78521_m = GLAllocation.createDirectFloatBuffer(16);
    private float field_175080_Q;
    private float field_175082_R;
    private float field_175081_S;
    private float field_78535_ad;
    private float field_78539_ae;
    private int field_175079_V;
    private boolean debugView;
    private double cameraZoom = 1.0D;
    private double field_78502_W;
    private double field_78509_X;
    private ItemStack itemActivationItem;
    private int itemActivationTicks;
    private float itemActivationOffX;
    private float itemActivationOffY;
    private ShaderGroup shaderGroup;
    private static final ResourceLocation[] SHADERS_TEXTURES = new ResourceLocation[] {new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json")};
    public static final int SHADER_COUNT = SHADERS_TEXTURES.length;
    private int shaderIndex;
    private boolean useShader;
    private int field_175084_ae;

    public EntityRenderer(Minecraft p_i45076_1_, IResourceManager p_i45076_2_)
    {
        this.shaderIndex = SHADER_COUNT;
        this.mc = p_i45076_1_;
        this.resourceManager = p_i45076_2_;
        this.itemRenderer = p_i45076_1_.getFirstPersonRenderer();
        this.mapItemRenderer = new MapItemRenderer(p_i45076_1_.getTextureManager());
        this.lightmapTexture = new DynamicTexture(16, 16);
        this.field_110922_T = p_i45076_1_.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
        this.field_78504_Q = this.lightmapTexture.func_110565_c();
        this.shaderGroup = null;

        for (int i = 0; i < 32; ++i)
        {
            for (int j = 0; j < 32; ++j)
            {
                float f = (float)(j - 16);
                float f1 = (float)(i - 16);
                float f2 = MathHelper.sqrt(f * f + f1 * f1);
                this.field_175076_N[i << 5 | j] = -f1 / f2;
                this.field_175077_O[i << 5 | j] = f / f2;
            }
        }
    }

    public boolean func_147702_a()
    {
        return OpenGlHelper.field_148824_g && this.shaderGroup != null;
    }

    public void stopUseShader()
    {
        if (this.shaderGroup != null)
        {
            this.shaderGroup.func_148021_a();
        }

        this.shaderGroup = null;
        this.shaderIndex = SHADER_COUNT;
    }

    public void switchUseShader()
    {
        this.useShader = !this.useShader;
    }

    /**
     * What shader to use when spectating this entity
     */
    public void loadEntityShader(@Nullable Entity entityIn)
    {
        if (OpenGlHelper.field_148824_g)
        {
            if (this.shaderGroup != null)
            {
                this.shaderGroup.func_148021_a();
            }

            this.shaderGroup = null;

            if (entityIn instanceof EntityCreeper)
            {
                this.loadShader(new ResourceLocation("shaders/post/creeper.json"));
            }
            else if (entityIn instanceof EntitySpider)
            {
                this.loadShader(new ResourceLocation("shaders/post/spider.json"));
            }
            else if (entityIn instanceof EntityEnderman)
            {
                this.loadShader(new ResourceLocation("shaders/post/invert.json"));
            }
        }
    }

    private void loadShader(ResourceLocation resourceLocationIn)
    {
        try
        {
            this.shaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), resourceLocationIn);
            this.shaderGroup.createBindFramebuffers(this.mc.field_71443_c, this.mc.field_71440_d);
            this.useShader = true;
        }
        catch (IOException ioexception)
        {
            LOGGER.warn("Failed to load shader: {}", resourceLocationIn, ioexception);
            this.shaderIndex = SHADER_COUNT;
            this.useShader = false;
        }
        catch (JsonSyntaxException jsonsyntaxexception)
        {
            LOGGER.warn("Failed to load shader: {}", resourceLocationIn, jsonsyntaxexception);
            this.shaderIndex = SHADER_COUNT;
            this.useShader = false;
        }
    }

    public void func_110549_a(IResourceManager p_110549_1_)
    {
        if (this.shaderGroup != null)
        {
            this.shaderGroup.func_148021_a();
        }

        this.shaderGroup = null;

        if (this.shaderIndex == SHADER_COUNT)
        {
            this.loadEntityShader(this.mc.getRenderViewEntity());
        }
        else
        {
            this.loadShader(SHADERS_TEXTURES[this.shaderIndex]);
        }
    }

    /**
     * Updates the entity renderer
     */
    public void tick()
    {
        if (OpenGlHelper.field_148824_g && ShaderLinkHelper.func_148074_b() == null)
        {
            ShaderLinkHelper.func_148076_a();
        }

        this.updateFovModifierHand();
        this.func_78470_f();
        this.field_78535_ad = this.field_78539_ae;
        this.field_78491_C = 4.0F;

        if (this.mc.gameSettings.smoothCamera)
        {
            float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f1 = f * f * f * 8.0F;
            this.field_78498_J = this.field_78527_v.func_76333_a(this.field_78496_H, 0.05F * f1);
            this.field_78499_K = this.field_78526_w.func_76333_a(this.field_78497_I, 0.05F * f1);
            this.field_78492_L = 0.0F;
            this.field_78496_H = 0.0F;
            this.field_78497_I = 0.0F;
        }
        else
        {
            this.field_78498_J = 0.0F;
            this.field_78499_K = 0.0F;
            this.field_78527_v.func_180179_a();
            this.field_78526_w.func_180179_a();
        }

        if (this.mc.getRenderViewEntity() == null)
        {
            this.mc.setRenderViewEntity(this.mc.player);
        }

        float f3 = this.mc.world.func_175724_o(new BlockPos(this.mc.getRenderViewEntity()));
        float f4 = (float)this.mc.gameSettings.renderDistanceChunks / 32.0F;
        float f2 = f3 * (1.0F - f4) + f4;
        this.field_78539_ae += (f2 - this.field_78539_ae) * 0.1F;
        ++this.rendererUpdateCount;
        this.itemRenderer.tick();
        this.func_78484_h();
        this.bossColorModifierPrev = this.bossColorModifier;

        if (this.mc.ingameGUI.getBossOverlay().shouldDarkenSky())
        {
            this.bossColorModifier += 0.05F;

            if (this.bossColorModifier > 1.0F)
            {
                this.bossColorModifier = 1.0F;
            }
        }
        else if (this.bossColorModifier > 0.0F)
        {
            this.bossColorModifier -= 0.0125F;
        }

        if (this.itemActivationTicks > 0)
        {
            --this.itemActivationTicks;

            if (this.itemActivationTicks == 0)
            {
                this.itemActivationItem = null;
            }
        }
    }

    public ShaderGroup getShaderGroup()
    {
        return this.shaderGroup;
    }

    public void updateShaderGroupSize(int width, int height)
    {
        if (OpenGlHelper.field_148824_g)
        {
            if (this.shaderGroup != null)
            {
                this.shaderGroup.createBindFramebuffers(width, height);
            }

            this.mc.worldRenderer.createBindEntityOutlineFbs(width, height);
        }
    }

    /**
     * Gets the block or object that is being moused over.
     */
    public void getMouseOver(float partialTicks)
    {
        Entity entity = this.mc.getRenderViewEntity();

        if (entity != null)
        {
            if (this.mc.world != null)
            {
                this.mc.profiler.startSection("pick");
                this.mc.pointedEntity = null;
                double d0 = (double)this.mc.playerController.getBlockReachDistance();
                this.mc.objectMouseOver = entity.func_174822_a(d0, partialTicks);
                Vec3d vec3d = entity.getEyePosition(partialTicks);
                boolean flag = false;
                int i = 3;
                double d1 = d0;

                if (this.mc.playerController.extendedReach())
                {
                    d1 = 6.0D;
                    d0 = d1;
                }
                else
                {
                    if (d0 > 3.0D)
                    {
                        flag = true;
                    }
                }

                if (this.mc.objectMouseOver != null)
                {
                    d1 = this.mc.objectMouseOver.hitResult.distanceTo(vec3d);
                }

                Vec3d vec3d1 = entity.getLook(1.0F);
                Vec3d vec3d2 = vec3d.add(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
                this.field_78528_u = null;
                Vec3d vec3d3 = null;
                float f = 1.0F;
                List<Entity> list = this.mc.world.getEntitiesInAABBexcluding(entity, entity.getBoundingBox().expand(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
                {
                    public boolean apply(@Nullable Entity p_apply_1_)
                    {
                        return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
                    }
                }));
                double d2 = d1;

                for (int j = 0; j < list.size(); ++j)
                {
                    Entity entity1 = list.get(j);
                    AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow((double)entity1.getCollisionBorderSize());
                    RayTraceResult raytraceresult = axisalignedbb.func_72327_a(vec3d, vec3d2);

                    if (axisalignedbb.contains(vec3d))
                    {
                        if (d2 >= 0.0D)
                        {
                            this.field_78528_u = entity1;
                            vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitResult;
                            d2 = 0.0D;
                        }
                    }
                    else if (raytraceresult != null)
                    {
                        double d3 = vec3d.distanceTo(raytraceresult.hitResult);

                        if (d3 < d2 || d2 == 0.0D)
                        {
                            if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity())
                            {
                                if (d2 == 0.0D)
                                {
                                    this.field_78528_u = entity1;
                                    vec3d3 = raytraceresult.hitResult;
                                }
                            }
                            else
                            {
                                this.field_78528_u = entity1;
                                vec3d3 = raytraceresult.hitResult;
                                d2 = d3;
                            }
                        }
                    }
                }

                if (this.field_78528_u != null && flag && vec3d.distanceTo(vec3d3) > 3.0D)
                {
                    this.field_78528_u = null;
                    this.mc.objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, (EnumFacing)null, new BlockPos(vec3d3));
                }

                if (this.field_78528_u != null && (d2 < d1 || this.mc.objectMouseOver == null))
                {
                    this.mc.objectMouseOver = new RayTraceResult(this.field_78528_u, vec3d3);

                    if (this.field_78528_u instanceof EntityLivingBase || this.field_78528_u instanceof EntityItemFrame)
                    {
                        this.mc.pointedEntity = this.field_78528_u;
                    }
                }

                this.mc.profiler.endSection();
            }
        }
    }

    /**
     * Update FOV modifier hand
     */
    private void updateFovModifierHand()
    {
        float f = 1.0F;

        if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer)
        {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)this.mc.getRenderViewEntity();
            f = abstractclientplayer.getFovModifier();
        }

        this.fovModifierHandPrev = this.fovModifierHand;
        this.fovModifierHand += (f - this.fovModifierHand) * 0.5F;

        if (this.fovModifierHand > 1.5F)
        {
            this.fovModifierHand = 1.5F;
        }

        if (this.fovModifierHand < 0.1F)
        {
            this.fovModifierHand = 0.1F;
        }
    }

    private float func_78481_a(float p_78481_1_, boolean p_78481_2_)
    {
        if (this.debugView)
        {
            return 90.0F;
        }
        else
        {
            Entity entity = this.mc.getRenderViewEntity();
            float f = 70.0F;

            if (p_78481_2_)
            {
                f = this.mc.gameSettings.fov;
                f = f * (this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * p_78481_1_);
            }

            if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() <= 0.0F)
            {
                float f1 = (float)((EntityLivingBase)entity).deathTime + p_78481_1_;
                f /= (1.0F - 500.0F / (f1 + 500.0F)) * 2.0F + 1.0F;
            }

            IBlockState iblockstate = ActiveRenderInfo.func_186703_a(this.mc.world, entity, p_78481_1_);

            if (iblockstate.getMaterial() == Material.WATER)
            {
                f = f * 60.0F / 70.0F;
            }

            return f;
        }
    }

    private void func_78482_e(float p_78482_1_)
    {
        if (this.mc.getRenderViewEntity() instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)this.mc.getRenderViewEntity();
            float f = (float)entitylivingbase.hurtTime - p_78482_1_;

            if (entitylivingbase.getHealth() <= 0.0F)
            {
                float f1 = (float)entitylivingbase.deathTime + p_78482_1_;
                GlStateManager.func_179114_b(40.0F - 8000.0F / (f1 + 200.0F), 0.0F, 0.0F, 1.0F);
            }

            if (f < 0.0F)
            {
                return;
            }

            f = f / (float)entitylivingbase.maxHurtTime;
            f = MathHelper.sin(f * f * f * f * (float)Math.PI);
            float f2 = entitylivingbase.attackedAtYaw;
            GlStateManager.func_179114_b(-f2, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(-f * 14.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.func_179114_b(f2, 0.0F, 1.0F, 0.0F);
        }
    }

    private void func_78475_f(float p_78475_1_)
    {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
            float f1 = -(entityplayer.distanceWalkedModified + f * p_78475_1_);
            float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * p_78475_1_;
            float f3 = entityplayer.field_70727_aS + (entityplayer.field_70726_aT - entityplayer.field_70727_aS) * p_78475_1_;
            GlStateManager.func_179109_b(MathHelper.sin(f1 * (float)Math.PI) * f2 * 0.5F, -Math.abs(MathHelper.cos(f1 * (float)Math.PI) * f2), 0.0F);
            GlStateManager.func_179114_b(MathHelper.sin(f1 * (float)Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.func_179114_b(Math.abs(MathHelper.cos(f1 * (float)Math.PI - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.func_179114_b(f3, 1.0F, 0.0F, 0.0F);
        }
    }

    private void func_78467_g(float p_78467_1_)
    {
        Entity entity = this.mc.getRenderViewEntity();
        float f = entity.getEyeHeight();
        double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)p_78467_1_;
        double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)p_78467_1_ + (double)f;
        double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)p_78467_1_;

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isSleeping())
        {
            f = (float)((double)f + 1.0D);
            GlStateManager.func_179109_b(0.0F, 0.3F, 0.0F);

            if (!this.mc.gameSettings.field_74325_U)
            {
                BlockPos blockpos = new BlockPos(entity);
                IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (block == Blocks.field_150324_C)
                {
                    int j = ((EnumFacing)iblockstate.get(BlockBed.HORIZONTAL_FACING)).getHorizontalIndex();
                    GlStateManager.func_179114_b((float)(j * 90), 0.0F, 1.0F, 0.0F);
                }

                GlStateManager.func_179114_b(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F, -1.0F, 0.0F);
                GlStateManager.func_179114_b(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * p_78467_1_, -1.0F, 0.0F, 0.0F);
            }
        }
        else if (this.mc.gameSettings.thirdPersonView > 0)
        {
            double d3 = (double)(this.field_78491_C + (4.0F - this.field_78491_C) * p_78467_1_);

            if (this.mc.gameSettings.field_74325_U)
            {
                GlStateManager.func_179109_b(0.0F, 0.0F, (float)(-d3));
            }
            else
            {
                float f1 = entity.rotationYaw;
                float f2 = entity.rotationPitch;

                if (this.mc.gameSettings.thirdPersonView == 2)
                {
                    f2 += 180.0F;
                }

                double d4 = (double)(-MathHelper.sin(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F)) * d3;
                double d5 = (double)(MathHelper.cos(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F)) * d3;
                double d6 = (double)(-MathHelper.sin(f2 * 0.017453292F)) * d3;

                for (int i = 0; i < 8; ++i)
                {
                    float f3 = (float)((i & 1) * 2 - 1);
                    float f4 = (float)((i >> 1 & 1) * 2 - 1);
                    float f5 = (float)((i >> 2 & 1) * 2 - 1);
                    f3 = f3 * 0.1F;
                    f4 = f4 * 0.1F;
                    f5 = f5 * 0.1F;
                    RayTraceResult raytraceresult = this.mc.world.func_72933_a(new Vec3d(d0 + (double)f3, d1 + (double)f4, d2 + (double)f5), new Vec3d(d0 - d4 + (double)f3 + (double)f5, d1 - d6 + (double)f4, d2 - d5 + (double)f5));

                    if (raytraceresult != null)
                    {
                        double d7 = raytraceresult.hitResult.distanceTo(new Vec3d(d0, d1, d2));

                        if (d7 < d3)
                        {
                            d3 = d7;
                        }
                    }
                }

                if (this.mc.gameSettings.thirdPersonView == 2)
                {
                    GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
                }

                GlStateManager.func_179114_b(entity.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
                GlStateManager.func_179114_b(entity.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
                GlStateManager.func_179109_b(0.0F, 0.0F, (float)(-d3));
                GlStateManager.func_179114_b(f1 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
                GlStateManager.func_179114_b(f2 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        }
        else
        {
            GlStateManager.func_179109_b(0.0F, 0.0F, 0.05F);
        }

        if (!this.mc.gameSettings.field_74325_U)
        {
            GlStateManager.func_179114_b(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * p_78467_1_, 1.0F, 0.0F, 0.0F);

            if (entity instanceof EntityAnimal)
            {
                EntityAnimal entityanimal = (EntityAnimal)entity;
                GlStateManager.func_179114_b(entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * p_78467_1_ + 180.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                GlStateManager.func_179114_b(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F, 1.0F, 0.0F);
            }
        }

        GlStateManager.func_179109_b(0.0F, -f, 0.0F);
        d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)p_78467_1_;
        d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)p_78467_1_ + (double)f;
        d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)p_78467_1_;
        this.field_78500_U = this.mc.worldRenderer.func_72721_a(d0, d1, d2, p_78467_1_);
    }

    private void func_78479_a(float p_78479_1_, int p_78479_2_)
    {
        this.farPlaneDistance = (float)(this.mc.gameSettings.renderDistanceChunks * 16);
        GlStateManager.func_179128_n(5889);
        GlStateManager.func_179096_D();
        float f = 0.07F;

        if (this.mc.gameSettings.field_74337_g)
        {
            GlStateManager.func_179109_b((float)(-(p_78479_2_ * 2 - 1)) * 0.07F, 0.0F, 0.0F);
        }

        if (this.cameraZoom != 1.0D)
        {
            GlStateManager.func_179109_b((float)this.field_78502_W, (float)(-this.field_78509_X), 0.0F);
            GlStateManager.func_179139_a(this.cameraZoom, this.cameraZoom, 1.0D);
        }

        Project.gluPerspective(this.func_78481_a(p_78479_1_, true), (float)this.mc.field_71443_c / (float)this.mc.field_71440_d, 0.05F, this.farPlaneDistance * MathHelper.SQRT_2);
        GlStateManager.func_179128_n(5888);
        GlStateManager.func_179096_D();

        if (this.mc.gameSettings.field_74337_g)
        {
            GlStateManager.func_179109_b((float)(p_78479_2_ * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        this.func_78482_e(p_78479_1_);

        if (this.mc.gameSettings.viewBobbing)
        {
            this.func_78475_f(p_78479_1_);
        }

        float f1 = this.mc.player.prevTimeInPortal + (this.mc.player.timeInPortal - this.mc.player.prevTimeInPortal) * p_78479_1_;

        if (f1 > 0.0F)
        {
            int i = 20;

            if (this.mc.player.isPotionActive(MobEffects.NAUSEA))
            {
                i = 7;
            }

            float f2 = 5.0F / (f1 * f1 + 5.0F) - f1 * 0.04F;
            f2 = f2 * f2;
            GlStateManager.func_179114_b(((float)this.rendererUpdateCount + p_78479_1_) * (float)i, 0.0F, 1.0F, 1.0F);
            GlStateManager.func_179152_a(1.0F / f2, 1.0F, 1.0F);
            GlStateManager.func_179114_b(-((float)this.rendererUpdateCount + p_78479_1_) * (float)i, 0.0F, 1.0F, 1.0F);
        }

        this.func_78467_g(p_78479_1_);

        if (this.debugView)
        {
            switch (this.field_175079_V)
            {
                case 0:
                    GlStateManager.func_179114_b(90.0F, 0.0F, 1.0F, 0.0F);
                    break;

                case 1:
                    GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
                    break;

                case 2:
                    GlStateManager.func_179114_b(-90.0F, 0.0F, 1.0F, 0.0F);
                    break;

                case 3:
                    GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
                    break;

                case 4:
                    GlStateManager.func_179114_b(-90.0F, 1.0F, 0.0F, 0.0F);
            }
        }
    }

    private void func_78476_b(float p_78476_1_, int p_78476_2_)
    {
        if (!this.debugView)
        {
            GlStateManager.func_179128_n(5889);
            GlStateManager.func_179096_D();
            float f = 0.07F;

            if (this.mc.gameSettings.field_74337_g)
            {
                GlStateManager.func_179109_b((float)(-(p_78476_2_ * 2 - 1)) * 0.07F, 0.0F, 0.0F);
            }

            Project.gluPerspective(this.func_78481_a(p_78476_1_, false), (float)this.mc.field_71443_c / (float)this.mc.field_71440_d, 0.05F, this.farPlaneDistance * 2.0F);
            GlStateManager.func_179128_n(5888);
            GlStateManager.func_179096_D();

            if (this.mc.gameSettings.field_74337_g)
            {
                GlStateManager.func_179109_b((float)(p_78476_2_ * 2 - 1) * 0.1F, 0.0F, 0.0F);
            }

            GlStateManager.func_179094_E();
            this.func_78482_e(p_78476_1_);

            if (this.mc.gameSettings.viewBobbing)
            {
                this.func_78475_f(p_78476_1_);
            }

            boolean flag = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isSleeping();

            if (this.mc.gameSettings.thirdPersonView == 0 && !flag && !this.mc.gameSettings.hideGUI && !this.mc.playerController.func_78747_a())
            {
                this.func_180436_i();
                this.itemRenderer.func_78440_a(p_78476_1_);
                this.func_175072_h();
            }

            GlStateManager.func_179121_F();

            if (this.mc.gameSettings.thirdPersonView == 0 && !flag)
            {
                this.itemRenderer.func_78447_b(p_78476_1_);
                this.func_78482_e(p_78476_1_);
            }

            if (this.mc.gameSettings.viewBobbing)
            {
                this.func_78475_f(p_78476_1_);
            }
        }
    }

    public void func_175072_h()
    {
        GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
    }

    public void func_180436_i()
    {
        GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
        GlStateManager.func_179128_n(5890);
        GlStateManager.func_179096_D();
        float f = 0.00390625F;
        GlStateManager.func_179152_a(0.00390625F, 0.00390625F, 0.00390625F);
        GlStateManager.func_179109_b(8.0F, 8.0F, 8.0F);
        GlStateManager.func_179128_n(5888);
        this.mc.getTextureManager().bindTexture(this.field_110922_T);
        GlStateManager.func_187421_b(3553, 10241, 9729);
        GlStateManager.func_187421_b(3553, 10240, 9729);
        GlStateManager.func_187421_b(3553, 10242, 10496);
        GlStateManager.func_187421_b(3553, 10243, 10496);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179098_w();
        GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
    }

    private void func_78470_f()
    {
        this.field_175075_L = (float)((double)this.field_175075_L + (Math.random() - Math.random()) * Math.random() * Math.random());
        this.field_175075_L = (float)((double)this.field_175075_L * 0.9D);
        this.field_78514_e += this.field_175075_L - this.field_78514_e;
        this.field_78536_aa = true;
    }

    private void func_78472_g(float p_78472_1_)
    {
        if (this.field_78536_aa)
        {
            this.mc.profiler.startSection("lightTex");
            World world = this.mc.world;

            if (world != null)
            {
                float f = world.func_72971_b(1.0F);
                float f1 = f * 0.95F + 0.05F;

                for (int i = 0; i < 256; ++i)
                {
                    float f2 = world.dimension.func_177497_p()[i / 16] * f1;
                    float f3 = world.dimension.func_177497_p()[i % 16] * (this.field_78514_e * 0.1F + 1.5F);

                    if (world.func_175658_ac() > 0)
                    {
                        f2 = world.dimension.func_177497_p()[i / 16];
                    }

                    float f4 = f2 * (f * 0.65F + 0.35F);
                    float f5 = f2 * (f * 0.65F + 0.35F);
                    float f6 = f3 * ((f3 * 0.6F + 0.4F) * 0.6F + 0.4F);
                    float f7 = f3 * (f3 * f3 * 0.6F + 0.4F);
                    float f8 = f4 + f3;
                    float f9 = f5 + f6;
                    float f10 = f2 + f7;
                    f8 = f8 * 0.96F + 0.03F;
                    f9 = f9 * 0.96F + 0.03F;
                    f10 = f10 * 0.96F + 0.03F;

                    if (this.bossColorModifier > 0.0F)
                    {
                        float f11 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * p_78472_1_;
                        f8 = f8 * (1.0F - f11) + f8 * 0.7F * f11;
                        f9 = f9 * (1.0F - f11) + f9 * 0.6F * f11;
                        f10 = f10 * (1.0F - f11) + f10 * 0.6F * f11;
                    }

                    if (world.dimension.getType().getId() == 1)
                    {
                        f8 = 0.22F + f3 * 0.75F;
                        f9 = 0.28F + f6 * 0.75F;
                        f10 = 0.25F + f7 * 0.75F;
                    }

                    if (this.mc.player.isPotionActive(MobEffects.NIGHT_VISION))
                    {
                        float f15 = this.getNightVisionBrightness(this.mc.player, p_78472_1_);
                        float f12 = 1.0F / f8;

                        if (f12 > 1.0F / f9)
                        {
                            f12 = 1.0F / f9;
                        }

                        if (f12 > 1.0F / f10)
                        {
                            f12 = 1.0F / f10;
                        }

                        f8 = f8 * (1.0F - f15) + f8 * f12 * f15;
                        f9 = f9 * (1.0F - f15) + f9 * f12 * f15;
                        f10 = f10 * (1.0F - f15) + f10 * f12 * f15;
                    }

                    if (f8 > 1.0F)
                    {
                        f8 = 1.0F;
                    }

                    if (f9 > 1.0F)
                    {
                        f9 = 1.0F;
                    }

                    if (f10 > 1.0F)
                    {
                        f10 = 1.0F;
                    }

                    float f16 = this.mc.gameSettings.gamma;
                    float f17 = 1.0F - f8;
                    float f13 = 1.0F - f9;
                    float f14 = 1.0F - f10;
                    f17 = 1.0F - f17 * f17 * f17 * f17;
                    f13 = 1.0F - f13 * f13 * f13 * f13;
                    f14 = 1.0F - f14 * f14 * f14 * f14;
                    f8 = f8 * (1.0F - f16) + f17 * f16;
                    f9 = f9 * (1.0F - f16) + f13 * f16;
                    f10 = f10 * (1.0F - f16) + f14 * f16;
                    f8 = f8 * 0.96F + 0.03F;
                    f9 = f9 * 0.96F + 0.03F;
                    f10 = f10 * 0.96F + 0.03F;

                    if (f8 > 1.0F)
                    {
                        f8 = 1.0F;
                    }

                    if (f9 > 1.0F)
                    {
                        f9 = 1.0F;
                    }

                    if (f10 > 1.0F)
                    {
                        f10 = 1.0F;
                    }

                    if (f8 < 0.0F)
                    {
                        f8 = 0.0F;
                    }

                    if (f9 < 0.0F)
                    {
                        f9 = 0.0F;
                    }

                    if (f10 < 0.0F)
                    {
                        f10 = 0.0F;
                    }

                    int j = 255;
                    int k = (int)(f8 * 255.0F);
                    int l = (int)(f9 * 255.0F);
                    int i1 = (int)(f10 * 255.0F);
                    this.field_78504_Q[i] = -16777216 | k << 16 | l << 8 | i1;
                }

                this.lightmapTexture.updateDynamicTexture();
                this.field_78536_aa = false;
                this.mc.profiler.endSection();
            }
        }
    }

    private float getNightVisionBrightness(EntityLivingBase entitylivingbaseIn, float p_180438_2_)
    {
        int i = entitylivingbaseIn.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
        return i > 200 ? 1.0F : 0.7F + MathHelper.sin(((float)i - p_180438_2_) * (float)Math.PI * 0.2F) * 0.3F;
    }

    public void func_181560_a(float p_181560_1_, long p_181560_2_)
    {
        boolean flag = Display.isActive();

        if (!flag && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1)))
        {
            if (Minecraft.func_71386_F() - this.prevFrameTime > 500L)
            {
                this.mc.displayInGameMenu();
            }
        }
        else
        {
            this.prevFrameTime = Minecraft.func_71386_F();
        }

        this.mc.profiler.startSection("mouse");

        if (flag && Minecraft.IS_RUNNING_ON_MAC && this.mc.field_71415_G && !Mouse.isInsideWindow())
        {
            Mouse.setGrabbed(false);
            Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2 - 20);
            Mouse.setGrabbed(true);
        }

        if (this.mc.field_71415_G && flag)
        {
            this.mc.mouseHelper.func_74374_c();
            this.mc.getTutorial().func_193299_a(this.mc.mouseHelper);
            float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f1 = f * f * f * 8.0F;
            float f2 = (float)this.mc.mouseHelper.field_74377_a * f1;
            float f3 = (float)this.mc.mouseHelper.field_74375_b * f1;
            int i = 1;

            if (this.mc.gameSettings.invertMouse)
            {
                i = -1;
            }

            if (this.mc.gameSettings.smoothCamera)
            {
                this.field_78496_H += f2;
                this.field_78497_I += f3;
                float f4 = p_181560_1_ - this.field_78492_L;
                this.field_78492_L = p_181560_1_;
                f2 = this.field_78498_J * f4;
                f3 = this.field_78499_K * f4;
                this.mc.player.func_70082_c(f2, f3 * (float)i);
            }
            else
            {
                this.field_78496_H = 0.0F;
                this.field_78497_I = 0.0F;
                this.mc.player.func_70082_c(f2, f3 * (float)i);
            }
        }

        this.mc.profiler.endSection();

        if (!this.mc.skipRenderWorld)
        {
            field_78517_a = this.mc.gameSettings.field_74337_g;
            final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int i1 = scaledresolution.func_78326_a();
            int j1 = scaledresolution.func_78328_b();
            final int k1 = Mouse.getX() * i1 / this.mc.field_71443_c;
            final int l1 = j1 - Mouse.getY() * j1 / this.mc.field_71440_d - 1;
            int i2 = this.mc.gameSettings.framerateLimit;

            if (this.mc.world != null)
            {
                this.mc.profiler.startSection("level");
                int j = Math.min(Minecraft.func_175610_ah(), i2);
                j = Math.max(j, 60);
                long k = System.nanoTime() - p_181560_2_;
                long l = Math.max((long)(1000000000 / j / 4) - k, 0L);
                this.func_78471_a(p_181560_1_, System.nanoTime() + l);

                if (this.mc.isSingleplayer() && this.timeWorldIcon < Minecraft.func_71386_F() - 1000L)
                {
                    this.timeWorldIcon = Minecraft.func_71386_F();

                    if (!this.mc.getIntegratedServer().isWorldIconSet())
                    {
                        this.createWorldIcon();
                    }
                }

                if (OpenGlHelper.field_148824_g)
                {
                    this.mc.worldRenderer.renderEntityOutlineFramebuffer();

                    if (this.shaderGroup != null && this.useShader)
                    {
                        GlStateManager.func_179128_n(5890);
                        GlStateManager.func_179094_E();
                        GlStateManager.func_179096_D();
                        this.shaderGroup.render(p_181560_1_);
                        GlStateManager.func_179121_F();
                    }

                    this.mc.getFramebuffer().bindFramebuffer(true);
                }

                this.field_78510_Z = System.nanoTime();
                this.mc.profiler.func_76318_c("gui");

                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null)
                {
                    GlStateManager.func_179092_a(516, 0.1F);
                    this.func_78478_c();
                    this.renderItemActivation(i1, j1, p_181560_1_);
                    this.mc.ingameGUI.renderGameOverlay(p_181560_1_);
                }

                this.mc.profiler.endSection();
            }
            else
            {
                GlStateManager.func_179083_b(0, 0, this.mc.field_71443_c, this.mc.field_71440_d);
                GlStateManager.func_179128_n(5889);
                GlStateManager.func_179096_D();
                GlStateManager.func_179128_n(5888);
                GlStateManager.func_179096_D();
                this.func_78478_c();
                this.field_78510_Z = System.nanoTime();
            }

            if (this.mc.currentScreen != null)
            {
                GlStateManager.func_179086_m(256);

                try
                {
                    this.mc.currentScreen.func_73863_a(k1, l1, this.mc.getTickLength());
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
                    crashreportcategory.addDetail("Screen name", new ICrashReportDetail<String>()
                    {
                        public String call() throws Exception
                        {
                            return EntityRenderer.this.mc.currentScreen.getClass().getCanonicalName();
                        }
                    });
                    crashreportcategory.addDetail("Mouse location", new ICrashReportDetail<String>()
                    {
                        public String call() throws Exception
                        {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", k1, l1, Mouse.getX(), Mouse.getY());
                        }
                    });
                    crashreportcategory.addDetail("Screen size", new ICrashReportDetail<String>()
                    {
                        public String call() throws Exception
                        {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", scaledresolution.func_78326_a(), scaledresolution.func_78328_b(), EntityRenderer.this.mc.field_71443_c, EntityRenderer.this.mc.field_71440_d, scaledresolution.func_78325_e());
                        }
                    });
                    throw new ReportedException(crashreport);
                }
            }
        }
    }

    private void createWorldIcon()
    {
        if (this.mc.worldRenderer.getRenderedChunks() > 10 && this.mc.worldRenderer.hasNoChunkUpdates() && !this.mc.getIntegratedServer().isWorldIconSet())
        {
            BufferedImage bufferedimage = ScreenShotHelper.func_186719_a(this.mc.field_71443_c, this.mc.field_71440_d, this.mc.getFramebuffer());
            int i = bufferedimage.getWidth();
            int j = bufferedimage.getHeight();
            int k = 0;
            int l = 0;

            if (i > j)
            {
                k = (i - j) / 2;
                i = j;
            }
            else
            {
                l = (j - i) / 2;
            }

            try
            {
                BufferedImage bufferedimage1 = new BufferedImage(64, 64, 1);
                Graphics graphics = bufferedimage1.createGraphics();
                graphics.drawImage(bufferedimage, 0, 0, 64, 64, k, l, k + i, l + i, (ImageObserver)null);
                graphics.dispose();
                ImageIO.write(bufferedimage1, "png", this.mc.getIntegratedServer().getWorldIconFile());
            }
            catch (IOException ioexception)
            {
                LOGGER.warn("Couldn't save auto screenshot", (Throwable)ioexception);
            }
        }
    }

    public void func_152430_c(float p_152430_1_)
    {
        this.func_78478_c();
    }

    private boolean isDrawBlockOutline()
    {
        if (!this.drawBlockOutline)
        {
            return false;
        }
        else
        {
            Entity entity = this.mc.getRenderViewEntity();
            boolean flag = entity instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;

            if (flag && !((EntityPlayer)entity).abilities.allowEdit)
            {
                ItemStack itemstack = ((EntityPlayer)entity).getHeldItemMainhand();

                if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.field_72313_a == RayTraceResult.Type.BLOCK)
                {
                    BlockPos blockpos = this.mc.objectMouseOver.func_178782_a();
                    Block block = this.mc.world.getBlockState(blockpos).getBlock();

                    if (this.mc.playerController.getCurrentGameType() == GameType.SPECTATOR)
                    {
                        flag = block.hasTileEntity() && this.mc.world.getTileEntity(blockpos) instanceof IInventory;
                    }
                    else
                    {
                        flag = !itemstack.isEmpty() && (itemstack.func_179544_c(block) || itemstack.func_179547_d(block));
                    }
                }
            }

            return flag;
        }
    }

    public void func_78471_a(float p_78471_1_, long p_78471_2_)
    {
        this.func_78472_g(p_78471_1_);

        if (this.mc.getRenderViewEntity() == null)
        {
            this.mc.setRenderViewEntity(this.mc.player);
        }

        this.getMouseOver(p_78471_1_);
        GlStateManager.func_179126_j();
        GlStateManager.func_179141_d();
        GlStateManager.func_179092_a(516, 0.5F);
        this.mc.profiler.startSection("center");

        if (this.mc.gameSettings.field_74337_g)
        {
            field_78515_b = 0;
            GlStateManager.func_179135_a(false, true, true, false);
            this.func_175068_a(0, p_78471_1_, p_78471_2_);
            field_78515_b = 1;
            GlStateManager.func_179135_a(true, false, false, false);
            this.func_175068_a(1, p_78471_1_, p_78471_2_);
            GlStateManager.func_179135_a(true, true, true, false);
        }
        else
        {
            this.func_175068_a(2, p_78471_1_, p_78471_2_);
        }

        this.mc.profiler.endSection();
    }

    private void func_175068_a(int p_175068_1_, float p_175068_2_, long p_175068_3_)
    {
        RenderGlobal renderglobal = this.mc.worldRenderer;
        ParticleManager particlemanager = this.mc.particles;
        boolean flag = this.isDrawBlockOutline();
        GlStateManager.func_179089_o();
        this.mc.profiler.func_76318_c("clear");
        GlStateManager.func_179083_b(0, 0, this.mc.field_71443_c, this.mc.field_71440_d);
        this.func_78466_h(p_175068_2_);
        GlStateManager.func_179086_m(16640);
        this.mc.profiler.func_76318_c("camera");
        this.func_78479_a(p_175068_2_, p_175068_1_);
        ActiveRenderInfo.func_74583_a(this.mc.player, this.mc.gameSettings.thirdPersonView == 2);
        this.mc.profiler.func_76318_c("frustum");
        ClippingHelperImpl.func_78558_a();
        this.mc.profiler.func_76318_c("culling");
        ICamera icamera = new Frustum();
        Entity entity = this.mc.getRenderViewEntity();
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)p_175068_2_;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)p_175068_2_;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)p_175068_2_;
        icamera.func_78547_a(d0, d1, d2);

        if (this.mc.gameSettings.renderDistanceChunks >= 4)
        {
            this.func_78468_a(-1, p_175068_2_);
            this.mc.profiler.func_76318_c("sky");
            GlStateManager.func_179128_n(5889);
            GlStateManager.func_179096_D();
            Project.gluPerspective(this.func_78481_a(p_175068_2_, true), (float)this.mc.field_71443_c / (float)this.mc.field_71440_d, 0.05F, this.farPlaneDistance * 2.0F);
            GlStateManager.func_179128_n(5888);
            renderglobal.func_174976_a(p_175068_2_, p_175068_1_);
            GlStateManager.func_179128_n(5889);
            GlStateManager.func_179096_D();
            Project.gluPerspective(this.func_78481_a(p_175068_2_, true), (float)this.mc.field_71443_c / (float)this.mc.field_71440_d, 0.05F, this.farPlaneDistance * MathHelper.SQRT_2);
            GlStateManager.func_179128_n(5888);
        }

        this.func_78468_a(0, p_175068_2_);
        GlStateManager.func_179103_j(7425);

        if (entity.posY + (double)entity.getEyeHeight() < 128.0D)
        {
            this.func_180437_a(renderglobal, p_175068_2_, p_175068_1_, d0, d1, d2);
        }

        this.mc.profiler.func_76318_c("prepareterrain");
        this.func_78468_a(0, p_175068_2_);
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        this.mc.profiler.func_76318_c("terrain_setup");
        renderglobal.func_174970_a(entity, (double)p_175068_2_, icamera, this.field_175084_ae++, this.mc.player.isSpectator());

        if (p_175068_1_ == 0 || p_175068_1_ == 2)
        {
            this.mc.profiler.func_76318_c("updatechunks");
            this.mc.worldRenderer.updateChunks(p_175068_3_);
        }

        this.mc.profiler.func_76318_c("terrain");
        GlStateManager.func_179128_n(5888);
        GlStateManager.func_179094_E();
        GlStateManager.func_179118_c();
        renderglobal.func_174977_a(BlockRenderLayer.SOLID, (double)p_175068_2_, p_175068_1_, entity);
        GlStateManager.func_179141_d();
        renderglobal.func_174977_a(BlockRenderLayer.CUTOUT_MIPPED, (double)p_175068_2_, p_175068_1_, entity);
        this.mc.getTextureManager().func_110581_b(TextureMap.LOCATION_BLOCKS_TEXTURE).func_174936_b(false, false);
        renderglobal.func_174977_a(BlockRenderLayer.CUTOUT, (double)p_175068_2_, p_175068_1_, entity);
        this.mc.getTextureManager().func_110581_b(TextureMap.LOCATION_BLOCKS_TEXTURE).func_174935_a();
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179092_a(516, 0.1F);

        if (!this.debugView)
        {
            GlStateManager.func_179128_n(5888);
            GlStateManager.func_179121_F();
            GlStateManager.func_179094_E();
            RenderHelper.func_74519_b();
            this.mc.profiler.func_76318_c("entities");
            renderglobal.func_180446_a(entity, icamera, p_175068_2_);
            RenderHelper.disableStandardItemLighting();
            this.func_175072_h();
        }

        GlStateManager.func_179128_n(5888);
        GlStateManager.func_179121_F();

        if (flag && this.mc.objectMouseOver != null && !entity.func_70055_a(Material.WATER))
        {
            EntityPlayer entityplayer = (EntityPlayer)entity;
            GlStateManager.func_179118_c();
            this.mc.profiler.func_76318_c("outline");
            renderglobal.func_72731_b(entityplayer, this.mc.objectMouseOver, 0, p_175068_2_);
            GlStateManager.func_179141_d();
        }

        if (this.mc.debugRenderer.func_190074_a())
        {
            this.mc.debugRenderer.func_190073_a(p_175068_2_, p_175068_3_);
        }

        this.mc.profiler.func_76318_c("destroyProgress");
        GlStateManager.func_179147_l();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        this.mc.getTextureManager().func_110581_b(TextureMap.LOCATION_BLOCKS_TEXTURE).func_174936_b(false, false);
        renderglobal.func_174981_a(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), entity, p_175068_2_);
        this.mc.getTextureManager().func_110581_b(TextureMap.LOCATION_BLOCKS_TEXTURE).func_174935_a();
        GlStateManager.func_179084_k();

        if (!this.debugView)
        {
            this.func_180436_i();
            this.mc.profiler.func_76318_c("litParticles");
            particlemanager.func_78872_b(entity, p_175068_2_);
            RenderHelper.disableStandardItemLighting();
            this.func_78468_a(0, p_175068_2_);
            this.mc.profiler.func_76318_c("particles");
            particlemanager.func_78874_a(entity, p_175068_2_);
            this.func_175072_h();
        }

        GlStateManager.func_179132_a(false);
        GlStateManager.func_179089_o();
        this.mc.profiler.func_76318_c("weather");
        this.func_78474_d(p_175068_2_);
        GlStateManager.func_179132_a(true);
        renderglobal.func_180449_a(entity, p_175068_2_);
        GlStateManager.func_179084_k();
        GlStateManager.func_179089_o();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179092_a(516, 0.1F);
        this.func_78468_a(0, p_175068_2_);
        GlStateManager.func_179147_l();
        GlStateManager.func_179132_a(false);
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.func_179103_j(7425);
        this.mc.profiler.func_76318_c("translucent");
        renderglobal.func_174977_a(BlockRenderLayer.TRANSLUCENT, (double)p_175068_2_, p_175068_1_, entity);
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179132_a(true);
        GlStateManager.func_179089_o();
        GlStateManager.func_179084_k();
        GlStateManager.func_179106_n();

        if (entity.posY + (double)entity.getEyeHeight() >= 128.0D)
        {
            this.mc.profiler.func_76318_c("aboveClouds");
            this.func_180437_a(renderglobal, p_175068_2_, p_175068_1_, d0, d1, d2);
        }

        this.mc.profiler.func_76318_c("hand");

        if (this.renderHand)
        {
            GlStateManager.func_179086_m(256);
            this.func_78476_b(p_175068_2_, p_175068_1_);
        }
    }

    private void func_180437_a(RenderGlobal p_180437_1_, float p_180437_2_, int p_180437_3_, double p_180437_4_, double p_180437_6_, double p_180437_8_)
    {
        if (this.mc.gameSettings.func_181147_e() != 0)
        {
            this.mc.profiler.func_76318_c("clouds");
            GlStateManager.func_179128_n(5889);
            GlStateManager.func_179096_D();
            Project.gluPerspective(this.func_78481_a(p_180437_2_, true), (float)this.mc.field_71443_c / (float)this.mc.field_71440_d, 0.05F, this.farPlaneDistance * 4.0F);
            GlStateManager.func_179128_n(5888);
            GlStateManager.func_179094_E();
            this.func_78468_a(0, p_180437_2_);
            p_180437_1_.func_180447_b(p_180437_2_, p_180437_3_, p_180437_4_, p_180437_6_, p_180437_8_);
            GlStateManager.func_179106_n();
            GlStateManager.func_179121_F();
            GlStateManager.func_179128_n(5889);
            GlStateManager.func_179096_D();
            Project.gluPerspective(this.func_78481_a(p_180437_2_, true), (float)this.mc.field_71443_c / (float)this.mc.field_71440_d, 0.05F, this.farPlaneDistance * MathHelper.SQRT_2);
            GlStateManager.func_179128_n(5888);
        }
    }

    private void func_78484_h()
    {
        float f = this.mc.world.getRainStrength(1.0F);

        if (!this.mc.gameSettings.fancyGraphics)
        {
            f /= 2.0F;
        }

        if (f != 0.0F)
        {
            this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
            Entity entity = this.mc.getRenderViewEntity();
            World world = this.mc.world;
            BlockPos blockpos = new BlockPos(entity);
            int i = 10;
            double d0 = 0.0D;
            double d1 = 0.0D;
            double d2 = 0.0D;
            int j = 0;
            int k = (int)(100.0F * f * f);

            if (this.mc.gameSettings.particles == 1)
            {
                k >>= 1;
            }
            else if (this.mc.gameSettings.particles == 2)
            {
                k = 0;
            }

            for (int l = 0; l < k; ++l)
            {
                BlockPos blockpos1 = world.func_175725_q(blockpos.add(this.random.nextInt(10) - this.random.nextInt(10), 0, this.random.nextInt(10) - this.random.nextInt(10)));
                Biome biome = world.func_180494_b(blockpos1);
                BlockPos blockpos2 = blockpos1.down();
                IBlockState iblockstate = world.getBlockState(blockpos2);

                if (blockpos1.getY() <= blockpos.getY() + 10 && blockpos1.getY() >= blockpos.getY() - 10 && biome.func_76738_d() && biome.getTemperatureRaw(blockpos1) >= 0.15F)
                {
                    double d3 = this.random.nextDouble();
                    double d4 = this.random.nextDouble();
                    AxisAlignedBB axisalignedbb = iblockstate.func_185900_c(world, blockpos2);

                    if (iblockstate.getMaterial() != Material.LAVA && iblockstate.getBlock() != Blocks.field_189877_df)
                    {
                        if (iblockstate.getMaterial() != Material.AIR)
                        {
                            ++j;

                            if (this.random.nextInt(j) == 0)
                            {
                                d0 = (double)blockpos2.getX() + d3;
                                d1 = (double)((float)blockpos2.getY() + 0.1F) + axisalignedbb.maxY - 1.0D;
                                d2 = (double)blockpos2.getZ() + d4;
                            }

                            this.mc.world.func_175688_a(EnumParticleTypes.WATER_DROP, (double)blockpos2.getX() + d3, (double)((float)blockpos2.getY() + 0.1F) + axisalignedbb.maxY, (double)blockpos2.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                        }
                    }
                    else
                    {
                        this.mc.world.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, (double)blockpos1.getX() + d3, (double)((float)blockpos1.getY() + 0.1F) - axisalignedbb.minY, (double)blockpos1.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    }
                }
            }

            if (j > 0 && this.random.nextInt(3) < this.field_78534_ac++)
            {
                this.field_78534_ac = 0;

                if (d1 > (double)(blockpos.getY() + 1) && world.func_175725_q(blockpos).getY() > MathHelper.floor((float)blockpos.getY()))
                {
                    this.mc.world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1F, 0.5F, false);
                }
                else
                {
                    this.mc.world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2F, 1.0F, false);
                }
            }
        }
    }

    protected void func_78474_d(float p_78474_1_)
    {
        float f = this.mc.world.getRainStrength(p_78474_1_);

        if (f > 0.0F)
        {
            this.func_180436_i();
            Entity entity = this.mc.getRenderViewEntity();
            World world = this.mc.world;
            int i = MathHelper.floor(entity.posX);
            int j = MathHelper.floor(entity.posY);
            int k = MathHelper.floor(entity.posZ);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            GlStateManager.func_179129_p();
            GlStateManager.func_187432_a(0.0F, 1.0F, 0.0F);
            GlStateManager.func_179147_l();
            GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.func_179092_a(516, 0.1F);
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)p_78474_1_;
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)p_78474_1_;
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)p_78474_1_;
            int l = MathHelper.floor(d1);
            int i1 = 5;

            if (this.mc.gameSettings.fancyGraphics)
            {
                i1 = 10;
            }

            int j1 = -1;
            float f1 = (float)this.rendererUpdateCount + p_78474_1_;
            bufferbuilder.func_178969_c(-d0, -d1, -d2);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k1 = k - i1; k1 <= k + i1; ++k1)
            {
                for (int l1 = i - i1; l1 <= i + i1; ++l1)
                {
                    int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                    double d3 = (double)this.field_175076_N[i2] * 0.5D;
                    double d4 = (double)this.field_175077_O[i2] * 0.5D;
                    blockpos$mutableblockpos.setPos(l1, 0, k1);
                    Biome biome = world.func_180494_b(blockpos$mutableblockpos);

                    if (biome.func_76738_d() || biome.func_76746_c())
                    {
                        int j2 = world.func_175725_q(blockpos$mutableblockpos).getY();
                        int k2 = j - i1;
                        int l2 = j + i1;

                        if (k2 < j2)
                        {
                            k2 = j2;
                        }

                        if (l2 < j2)
                        {
                            l2 = j2;
                        }

                        int i3 = j2;

                        if (j2 < l)
                        {
                            i3 = l;
                        }

                        if (k2 != l2)
                        {
                            this.random.setSeed((long)(l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761));
                            blockpos$mutableblockpos.setPos(l1, k2, k1);
                            float f2 = biome.getTemperatureRaw(blockpos$mutableblockpos);

                            if (world.func_72959_q().func_76939_a(f2, j2) >= 0.15F)
                            {
                                if (j1 != 0)
                                {
                                    if (j1 >= 0)
                                    {
                                        tessellator.draw();
                                    }

                                    j1 = 0;
                                    this.mc.getTextureManager().bindTexture(field_110924_q);
                                    bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }

                                double d5 = -((double)(this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + (double)p_78474_1_) / 32.0D * (3.0D + this.random.nextDouble());
                                double d6 = (double)((float)l1 + 0.5F) - entity.posX;
                                double d7 = (double)((float)k1 + 0.5F) - entity.posZ;
                                float f3 = MathHelper.sqrt(d6 * d6 + d7 * d7) / (float)i1;
                                float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
                                blockpos$mutableblockpos.setPos(l1, i3, k1);
                                int j3 = world.func_175626_b(blockpos$mutableblockpos, 0);
                                int k3 = j3 >> 16 & 65535;
                                int l3 = j3 & 65535;
                                bufferbuilder.func_181662_b((double)l1 - d3 + 0.5D, (double)l2, (double)k1 - d4 + 0.5D).func_187315_a(0.0D, (double)k2 * 0.25D + d5).func_181666_a(1.0F, 1.0F, 1.0F, f4).func_187314_a(k3, l3).endVertex();
                                bufferbuilder.func_181662_b((double)l1 + d3 + 0.5D, (double)l2, (double)k1 + d4 + 0.5D).func_187315_a(1.0D, (double)k2 * 0.25D + d5).func_181666_a(1.0F, 1.0F, 1.0F, f4).func_187314_a(k3, l3).endVertex();
                                bufferbuilder.func_181662_b((double)l1 + d3 + 0.5D, (double)k2, (double)k1 + d4 + 0.5D).func_187315_a(1.0D, (double)l2 * 0.25D + d5).func_181666_a(1.0F, 1.0F, 1.0F, f4).func_187314_a(k3, l3).endVertex();
                                bufferbuilder.func_181662_b((double)l1 - d3 + 0.5D, (double)k2, (double)k1 - d4 + 0.5D).func_187315_a(0.0D, (double)l2 * 0.25D + d5).func_181666_a(1.0F, 1.0F, 1.0F, f4).func_187314_a(k3, l3).endVertex();
                            }
                            else
                            {
                                if (j1 != 1)
                                {
                                    if (j1 >= 0)
                                    {
                                        tessellator.draw();
                                    }

                                    j1 = 1;
                                    this.mc.getTextureManager().bindTexture(field_110923_r);
                                    bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }

                                double d8 = (double)(-((float)(this.rendererUpdateCount & 511) + p_78474_1_) / 512.0F);
                                double d9 = this.random.nextDouble() + (double)f1 * 0.01D * (double)((float)this.random.nextGaussian());
                                double d10 = this.random.nextDouble() + (double)(f1 * (float)this.random.nextGaussian()) * 0.001D;
                                double d11 = (double)((float)l1 + 0.5F) - entity.posX;
                                double d12 = (double)((float)k1 + 0.5F) - entity.posZ;
                                float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / (float)i1;
                                float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * f;
                                blockpos$mutableblockpos.setPos(l1, i3, k1);
                                int i4 = (world.func_175626_b(blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
                                int j4 = i4 >> 16 & 65535;
                                int k4 = i4 & 65535;
                                bufferbuilder.func_181662_b((double)l1 - d3 + 0.5D, (double)l2, (double)k1 - d4 + 0.5D).func_187315_a(0.0D + d9, (double)k2 * 0.25D + d8 + d10).func_181666_a(1.0F, 1.0F, 1.0F, f5).func_187314_a(j4, k4).endVertex();
                                bufferbuilder.func_181662_b((double)l1 + d3 + 0.5D, (double)l2, (double)k1 + d4 + 0.5D).func_187315_a(1.0D + d9, (double)k2 * 0.25D + d8 + d10).func_181666_a(1.0F, 1.0F, 1.0F, f5).func_187314_a(j4, k4).endVertex();
                                bufferbuilder.func_181662_b((double)l1 + d3 + 0.5D, (double)k2, (double)k1 + d4 + 0.5D).func_187315_a(1.0D + d9, (double)l2 * 0.25D + d8 + d10).func_181666_a(1.0F, 1.0F, 1.0F, f5).func_187314_a(j4, k4).endVertex();
                                bufferbuilder.func_181662_b((double)l1 - d3 + 0.5D, (double)k2, (double)k1 - d4 + 0.5D).func_187315_a(0.0D + d9, (double)l2 * 0.25D + d8 + d10).func_181666_a(1.0F, 1.0F, 1.0F, f5).func_187314_a(j4, k4).endVertex();
                            }
                        }
                    }
                }
            }

            if (j1 >= 0)
            {
                tessellator.draw();
            }

            bufferbuilder.func_178969_c(0.0D, 0.0D, 0.0D);
            GlStateManager.func_179089_o();
            GlStateManager.func_179084_k();
            GlStateManager.func_179092_a(516, 0.1F);
            this.func_175072_h();
        }
    }

    public void func_78478_c()
    {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        GlStateManager.func_179086_m(256);
        GlStateManager.func_179128_n(5889);
        GlStateManager.func_179096_D();
        GlStateManager.func_179130_a(0.0D, scaledresolution.func_78327_c(), scaledresolution.func_78324_d(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.func_179128_n(5888);
        GlStateManager.func_179096_D();
        GlStateManager.func_179109_b(0.0F, 0.0F, -2000.0F);
    }

    private void func_78466_h(float p_78466_1_)
    {
        World world = this.mc.world;
        Entity entity = this.mc.getRenderViewEntity();
        float f = 0.25F + 0.75F * (float)this.mc.gameSettings.renderDistanceChunks / 32.0F;
        f = 1.0F - (float)Math.pow((double)f, 0.25D);
        Vec3d vec3d = world.func_72833_a(this.mc.getRenderViewEntity(), p_78466_1_);
        float f1 = (float)vec3d.x;
        float f2 = (float)vec3d.y;
        float f3 = (float)vec3d.z;
        Vec3d vec3d1 = world.func_72948_g(p_78466_1_);
        this.field_175080_Q = (float)vec3d1.x;
        this.field_175082_R = (float)vec3d1.y;
        this.field_175081_S = (float)vec3d1.z;

        if (this.mc.gameSettings.renderDistanceChunks >= 4)
        {
            double d0 = MathHelper.sin(world.getCelestialAngleRadians(p_78466_1_)) > 0.0F ? -1.0D : 1.0D;
            Vec3d vec3d2 = new Vec3d(d0, 0.0D, 0.0D);
            float f5 = (float)entity.getLook(p_78466_1_).dotProduct(vec3d2);

            if (f5 < 0.0F)
            {
                f5 = 0.0F;
            }

            if (f5 > 0.0F)
            {
                float[] afloat = world.dimension.calcSunriseSunsetColors(world.getCelestialAngle(p_78466_1_), p_78466_1_);

                if (afloat != null)
                {
                    f5 = f5 * afloat[3];
                    this.field_175080_Q = this.field_175080_Q * (1.0F - f5) + afloat[0] * f5;
                    this.field_175082_R = this.field_175082_R * (1.0F - f5) + afloat[1] * f5;
                    this.field_175081_S = this.field_175081_S * (1.0F - f5) + afloat[2] * f5;
                }
            }
        }

        this.field_175080_Q += (f1 - this.field_175080_Q) * f;
        this.field_175082_R += (f2 - this.field_175082_R) * f;
        this.field_175081_S += (f3 - this.field_175081_S) * f;
        float f8 = world.getRainStrength(p_78466_1_);

        if (f8 > 0.0F)
        {
            float f4 = 1.0F - f8 * 0.5F;
            float f10 = 1.0F - f8 * 0.4F;
            this.field_175080_Q *= f4;
            this.field_175082_R *= f4;
            this.field_175081_S *= f10;
        }

        float f9 = world.getThunderStrength(p_78466_1_);

        if (f9 > 0.0F)
        {
            float f11 = 1.0F - f9 * 0.5F;
            this.field_175080_Q *= f11;
            this.field_175082_R *= f11;
            this.field_175081_S *= f11;
        }

        IBlockState iblockstate = ActiveRenderInfo.func_186703_a(this.mc.world, entity, p_78466_1_);

        if (this.field_78500_U)
        {
            Vec3d vec3d3 = world.func_72824_f(p_78466_1_);
            this.field_175080_Q = (float)vec3d3.x;
            this.field_175082_R = (float)vec3d3.y;
            this.field_175081_S = (float)vec3d3.z;
        }
        else if (iblockstate.getMaterial() == Material.WATER)
        {
            float f12 = 0.0F;

            if (entity instanceof EntityLivingBase)
            {
                f12 = (float)EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.2F;

                if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING))
                {
                    f12 = f12 * 0.3F + 0.6F;
                }
            }

            this.field_175080_Q = 0.02F + f12;
            this.field_175082_R = 0.02F + f12;
            this.field_175081_S = 0.2F + f12;
        }
        else if (iblockstate.getMaterial() == Material.LAVA)
        {
            this.field_175080_Q = 0.6F;
            this.field_175082_R = 0.1F;
            this.field_175081_S = 0.0F;
        }

        float f13 = this.field_78535_ad + (this.field_78539_ae - this.field_78535_ad) * p_78466_1_;
        this.field_175080_Q *= f13;
        this.field_175082_R *= f13;
        this.field_175081_S *= f13;
        double d1 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)p_78466_1_) * world.dimension.getVoidFogYFactor();

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS))
        {
            int i = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();

            if (i < 20)
            {
                d1 *= (double)(1.0F - (float)i / 20.0F);
            }
            else
            {
                d1 = 0.0D;
            }
        }

        if (d1 < 1.0D)
        {
            if (d1 < 0.0D)
            {
                d1 = 0.0D;
            }

            d1 = d1 * d1;
            this.field_175080_Q = (float)((double)this.field_175080_Q * d1);
            this.field_175082_R = (float)((double)this.field_175082_R * d1);
            this.field_175081_S = (float)((double)this.field_175081_S * d1);
        }

        if (this.bossColorModifier > 0.0F)
        {
            float f14 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * p_78466_1_;
            this.field_175080_Q = this.field_175080_Q * (1.0F - f14) + this.field_175080_Q * 0.7F * f14;
            this.field_175082_R = this.field_175082_R * (1.0F - f14) + this.field_175082_R * 0.6F * f14;
            this.field_175081_S = this.field_175081_S * (1.0F - f14) + this.field_175081_S * 0.6F * f14;
        }

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.NIGHT_VISION))
        {
            float f15 = this.getNightVisionBrightness((EntityLivingBase)entity, p_78466_1_);
            float f6 = 1.0F / this.field_175080_Q;

            if (f6 > 1.0F / this.field_175082_R)
            {
                f6 = 1.0F / this.field_175082_R;
            }

            if (f6 > 1.0F / this.field_175081_S)
            {
                f6 = 1.0F / this.field_175081_S;
            }

            this.field_175080_Q = this.field_175080_Q * (1.0F - f15) + this.field_175080_Q * f6 * f15;
            this.field_175082_R = this.field_175082_R * (1.0F - f15) + this.field_175082_R * f6 * f15;
            this.field_175081_S = this.field_175081_S * (1.0F - f15) + this.field_175081_S * f6 * f15;
        }

        if (this.mc.gameSettings.field_74337_g)
        {
            float f16 = (this.field_175080_Q * 30.0F + this.field_175082_R * 59.0F + this.field_175081_S * 11.0F) / 100.0F;
            float f17 = (this.field_175080_Q * 30.0F + this.field_175082_R * 70.0F) / 100.0F;
            float f7 = (this.field_175080_Q * 30.0F + this.field_175081_S * 70.0F) / 100.0F;
            this.field_175080_Q = f16;
            this.field_175082_R = f17;
            this.field_175081_S = f7;
        }

        GlStateManager.func_179082_a(this.field_175080_Q, this.field_175082_R, this.field_175081_S, 0.0F);
    }

    private void func_78468_a(int p_78468_1_, float p_78468_2_)
    {
        Entity entity = this.mc.getRenderViewEntity();
        this.func_191514_d(false);
        GlStateManager.func_187432_a(0.0F, -1.0F, 0.0F);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        IBlockState iblockstate = ActiveRenderInfo.func_186703_a(this.mc.world, entity, p_78468_2_);

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(MobEffects.BLINDNESS))
        {
            float f1 = 5.0F;
            int i = ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.BLINDNESS).getDuration();

            if (i < 20)
            {
                f1 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float)i / 20.0F);
            }

            GlStateManager.func_187430_a(GlStateManager.FogMode.LINEAR);

            if (p_78468_1_ == -1)
            {
                GlStateManager.func_179102_b(0.0F);
                GlStateManager.func_179153_c(f1 * 0.8F);
            }
            else
            {
                GlStateManager.func_179102_b(f1 * 0.25F);
                GlStateManager.func_179153_c(f1);
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance)
            {
                GlStateManager.func_187412_c(34138, 34139);
            }
        }
        else if (this.field_78500_U)
        {
            GlStateManager.func_187430_a(GlStateManager.FogMode.EXP);
            GlStateManager.func_179095_a(0.1F);
        }
        else if (iblockstate.getMaterial() == Material.WATER)
        {
            GlStateManager.func_187430_a(GlStateManager.FogMode.EXP);

            if (entity instanceof EntityLivingBase)
            {
                if (((EntityLivingBase)entity).isPotionActive(MobEffects.WATER_BREATHING))
                {
                    GlStateManager.func_179095_a(0.01F);
                }
                else
                {
                    GlStateManager.func_179095_a(0.1F - (float)EnchantmentHelper.getRespirationModifier((EntityLivingBase)entity) * 0.03F);
                }
            }
            else
            {
                GlStateManager.func_179095_a(0.1F);
            }
        }
        else if (iblockstate.getMaterial() == Material.LAVA)
        {
            GlStateManager.func_187430_a(GlStateManager.FogMode.EXP);
            GlStateManager.func_179095_a(2.0F);
        }
        else
        {
            float f = this.farPlaneDistance;
            GlStateManager.func_187430_a(GlStateManager.FogMode.LINEAR);

            if (p_78468_1_ == -1)
            {
                GlStateManager.func_179102_b(0.0F);
                GlStateManager.func_179153_c(f);
            }
            else
            {
                GlStateManager.func_179102_b(f * 0.75F);
                GlStateManager.func_179153_c(f);
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance)
            {
                GlStateManager.func_187412_c(34138, 34139);
            }

            if (this.mc.world.dimension.doesXZShowFog((int)entity.posX, (int)entity.posZ) || this.mc.ingameGUI.getBossOverlay().shouldCreateFog())
            {
                GlStateManager.func_179102_b(f * 0.05F);
                GlStateManager.func_179153_c(Math.min(f, 192.0F) * 0.5F);
            }
        }

        GlStateManager.func_179142_g();
        GlStateManager.func_179127_m();
        GlStateManager.func_179104_a(1028, 4608);
    }

    public void func_191514_d(boolean p_191514_1_)
    {
        if (p_191514_1_)
        {
            GlStateManager.func_187402_b(2918, this.func_78469_a(0.0F, 0.0F, 0.0F, 1.0F));
        }
        else
        {
            GlStateManager.func_187402_b(2918, this.func_78469_a(this.field_175080_Q, this.field_175082_R, this.field_175081_S, 1.0F));
        }
    }

    private FloatBuffer func_78469_a(float p_78469_1_, float p_78469_2_, float p_78469_3_, float p_78469_4_)
    {
        this.field_78521_m.clear();
        this.field_78521_m.put(p_78469_1_).put(p_78469_2_).put(p_78469_3_).put(p_78469_4_);
        this.field_78521_m.flip();
        return this.field_78521_m;
    }

    public void resetData()
    {
        this.itemActivationItem = null;
        this.mapItemRenderer.clearLoadedMaps();
    }

    public MapItemRenderer getMapItemRenderer()
    {
        return this.mapItemRenderer;
    }

    public static void func_189692_a(FontRenderer p_189692_0_, String p_189692_1_, float p_189692_2_, float p_189692_3_, float p_189692_4_, int p_189692_5_, float p_189692_6_, float p_189692_7_, boolean p_189692_8_, boolean p_189692_9_)
    {
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b(p_189692_2_, p_189692_3_, p_189692_4_);
        GlStateManager.func_187432_a(0.0F, 1.0F, 0.0F);
        GlStateManager.func_179114_b(-p_189692_6_, 0.0F, 1.0F, 0.0F);
        GlStateManager.func_179114_b((float)(p_189692_8_ ? -1 : 1) * p_189692_7_, 1.0F, 0.0F, 0.0F);
        GlStateManager.func_179152_a(-0.025F, -0.025F, 0.025F);
        GlStateManager.func_179140_f();
        GlStateManager.func_179132_a(false);

        if (!p_189692_9_)
        {
            GlStateManager.func_179097_i();
        }

        GlStateManager.func_179147_l();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int i = p_189692_0_.getStringWidth(p_189692_1_) / 2;
        GlStateManager.func_179090_x();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.func_181662_b((double)(-i - 1), (double)(-1 + p_189692_5_), 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.func_181662_b((double)(-i - 1), (double)(8 + p_189692_5_), 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.func_181662_b((double)(i + 1), (double)(8 + p_189692_5_), 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.func_181662_b((double)(i + 1), (double)(-1 + p_189692_5_), 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.func_179098_w();

        if (!p_189692_9_)
        {
            p_189692_0_.func_78276_b(p_189692_1_, -p_189692_0_.getStringWidth(p_189692_1_) / 2, p_189692_5_, 553648127);
            GlStateManager.func_179126_j();
        }

        GlStateManager.func_179132_a(true);
        p_189692_0_.func_78276_b(p_189692_1_, -p_189692_0_.getStringWidth(p_189692_1_) / 2, p_189692_5_, p_189692_9_ ? 553648127 : -1);
        GlStateManager.func_179145_e();
        GlStateManager.func_179084_k();
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179121_F();
    }

    public void displayItemActivation(ItemStack stack)
    {
        this.itemActivationItem = stack;
        this.itemActivationTicks = 40;
        this.itemActivationOffX = this.random.nextFloat() * 2.0F - 1.0F;
        this.itemActivationOffY = this.random.nextFloat() * 2.0F - 1.0F;
    }

    private void renderItemActivation(int widthsp, int heightScaled, float partialTicks)
    {
        if (this.itemActivationItem != null && this.itemActivationTicks > 0)
        {
            int i = 40 - this.itemActivationTicks;
            float f = ((float)i + partialTicks) / 40.0F;
            float f1 = f * f;
            float f2 = f * f1;
            float f3 = 10.25F * f2 * f1 + -24.95F * f1 * f1 + 25.5F * f2 + -13.8F * f1 + 4.0F * f;
            float f4 = f3 * (float)Math.PI;
            float f5 = this.itemActivationOffX * (float)(widthsp / 4);
            float f6 = this.itemActivationOffY * (float)(heightScaled / 4);
            GlStateManager.func_179141_d();
            GlStateManager.func_179094_E();
            GlStateManager.func_179123_a();
            GlStateManager.func_179126_j();
            GlStateManager.func_179129_p();
            RenderHelper.func_74519_b();
            GlStateManager.func_179109_b((float)(widthsp / 2) + f5 * MathHelper.abs(MathHelper.sin(f4 * 2.0F)), (float)(heightScaled / 2) + f6 * MathHelper.abs(MathHelper.sin(f4 * 2.0F)), -50.0F);
            float f7 = 50.0F + 175.0F * MathHelper.sin(f4);
            GlStateManager.func_179152_a(f7, -f7, f7);
            GlStateManager.func_179114_b(900.0F * MathHelper.abs(MathHelper.sin(f4)), 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(6.0F * MathHelper.cos(f * 8.0F), 1.0F, 0.0F, 0.0F);
            GlStateManager.func_179114_b(6.0F * MathHelper.cos(f * 8.0F), 0.0F, 0.0F, 1.0F);
            this.mc.getItemRenderer().func_181564_a(this.itemActivationItem, ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.func_179099_b();
            GlStateManager.func_179121_F();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.func_179089_o();
            GlStateManager.func_179097_i();
        }
    }
}

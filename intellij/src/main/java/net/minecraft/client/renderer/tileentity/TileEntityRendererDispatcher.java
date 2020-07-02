package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class TileEntityRendererDispatcher
{
    private final Map < Class <? extends TileEntity > , TileEntitySpecialRenderer <? extends TileEntity >> renderers = Maps. < Class <? extends TileEntity > , TileEntitySpecialRenderer <? extends TileEntity >> newHashMap();
    public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
    private FontRenderer fontRenderer;
    public static double field_147554_b;
    public static double field_147555_c;
    public static double field_147552_d;
    public TextureManager textureManager;
    public World world;
    public Entity field_147551_g;
    public float field_147562_h;
    public float field_147563_i;
    public RayTraceResult cameraHitResult;
    public double field_147560_j;
    public double field_147561_k;
    public double field_147558_l;

    private TileEntityRendererDispatcher()
    {
        this.renderers.put(TileEntitySign.class, new TileEntitySignRenderer());
        this.renderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        this.renderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
        this.renderers.put(TileEntityChest.class, new TileEntityChestRenderer());
        this.renderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
        this.renderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
        this.renderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
        this.renderers.put(TileEntityEndGateway.class, new TileEntityEndGatewayRenderer());
        this.renderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
        this.renderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
        this.renderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());
        this.renderers.put(TileEntityStructure.class, new TileEntityStructureRenderer());
        this.renderers.put(TileEntityShulkerBox.class, new TileEntityShulkerBoxRenderer(new ModelShulker()));
        this.renderers.put(TileEntityBed.class, new TileEntityBedRenderer());

        for (TileEntitySpecialRenderer<?> tileentityspecialrenderer : this.renderers.values())
        {
            tileentityspecialrenderer.func_147497_a(this);
        }
    }

    public <T extends TileEntity> TileEntitySpecialRenderer<T> func_147546_a(Class <? extends TileEntity > p_147546_1_)
    {
        TileEntitySpecialRenderer<T> tileentityspecialrenderer = (TileEntitySpecialRenderer)this.renderers.get(p_147546_1_);

        if (tileentityspecialrenderer == null && p_147546_1_ != TileEntity.class)
        {
            tileentityspecialrenderer = this.func_147546_a((Class <? extends TileEntity >)p_147546_1_.getSuperclass());
            this.renderers.put(p_147546_1_, tileentityspecialrenderer);
        }

        return tileentityspecialrenderer;
    }

    @Nullable
    public <T extends TileEntity> TileEntitySpecialRenderer<T> getRenderer(@Nullable TileEntity tileEntityIn)
    {
        return tileEntityIn == null ? null : this.func_147546_a(tileEntityIn.getClass());
    }

    public void func_190056_a(World p_190056_1_, TextureManager p_190056_2_, FontRenderer p_190056_3_, Entity p_190056_4_, RayTraceResult p_190056_5_, float p_190056_6_)
    {
        if (this.world != p_190056_1_)
        {
            this.setWorld(p_190056_1_);
        }

        this.textureManager = p_190056_2_;
        this.field_147551_g = p_190056_4_;
        this.fontRenderer = p_190056_3_;
        this.cameraHitResult = p_190056_5_;
        this.field_147562_h = p_190056_4_.prevRotationYaw + (p_190056_4_.rotationYaw - p_190056_4_.prevRotationYaw) * p_190056_6_;
        this.field_147563_i = p_190056_4_.prevRotationPitch + (p_190056_4_.rotationPitch - p_190056_4_.prevRotationPitch) * p_190056_6_;
        this.field_147560_j = p_190056_4_.lastTickPosX + (p_190056_4_.posX - p_190056_4_.lastTickPosX) * (double)p_190056_6_;
        this.field_147561_k = p_190056_4_.lastTickPosY + (p_190056_4_.posY - p_190056_4_.lastTickPosY) * (double)p_190056_6_;
        this.field_147558_l = p_190056_4_.lastTickPosZ + (p_190056_4_.posZ - p_190056_4_.lastTickPosZ) * (double)p_190056_6_;
    }

    public void func_180546_a(TileEntity p_180546_1_, float p_180546_2_, int p_180546_3_)
    {
        if (p_180546_1_.getDistanceSq(this.field_147560_j, this.field_147561_k, this.field_147558_l) < p_180546_1_.getMaxRenderDistanceSquared())
        {
            RenderHelper.func_74519_b();
            int i = this.world.func_175626_b(p_180546_1_.getPos(), 0);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)j, (float)k);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            BlockPos blockpos = p_180546_1_.getPos();
            this.func_192854_a(p_180546_1_, (double)blockpos.getX() - field_147554_b, (double)blockpos.getY() - field_147555_c, (double)blockpos.getZ() - field_147552_d, p_180546_2_, p_180546_3_, 1.0F);
        }
    }

    public void func_147549_a(TileEntity p_147549_1_, double p_147549_2_, double p_147549_4_, double p_147549_6_, float p_147549_8_)
    {
        this.func_192855_a(p_147549_1_, p_147549_2_, p_147549_4_, p_147549_6_, p_147549_8_, 1.0F);
    }

    public void func_192855_a(TileEntity p_192855_1_, double p_192855_2_, double p_192855_4_, double p_192855_6_, float p_192855_8_, float p_192855_9_)
    {
        this.func_192854_a(p_192855_1_, p_192855_2_, p_192855_4_, p_192855_6_, p_192855_8_, -1, p_192855_9_);
    }

    public void func_192854_a(TileEntity p_192854_1_, double p_192854_2_, double p_192854_4_, double p_192854_6_, float p_192854_8_, int p_192854_9_, float p_192854_10_)
    {
        TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = this.<TileEntity>getRenderer(p_192854_1_);

        if (tileentityspecialrenderer != null)
        {
            try
            {
                tileentityspecialrenderer.func_192841_a(p_192854_1_, p_192854_2_, p_192854_4_, p_192854_6_, p_192854_8_, p_192854_9_, p_192854_10_);
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block Entity Details");
                p_192854_1_.addInfoToCrashReport(crashreportcategory);
                throw new ReportedException(crashreport);
            }
        }
    }

    public void setWorld(@Nullable World worldIn)
    {
        this.world = worldIn;

        if (worldIn == null)
        {
            this.field_147551_g = null;
        }
    }

    public FontRenderer getFontRenderer()
    {
        return this.fontRenderer;
    }
}

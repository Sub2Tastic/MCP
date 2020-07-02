package net.minecraft.client.renderer.entity;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class Render<T extends Entity>
{
    private static final ResourceLocation field_110778_a = new ResourceLocation("textures/misc/shadow.png");
    protected final RenderManager renderManager;
    protected float shadowSize;
    protected float shadowOpaque = 1.0F;
    protected boolean field_188301_f;

    protected Render(RenderManager renderManager)
    {
        this.renderManager = renderManager;
    }

    public void func_188297_a(boolean p_188297_1_)
    {
        this.field_188301_f = p_188297_1_;
    }

    public boolean func_177071_a(T p_177071_1_, ICamera p_177071_2_, double p_177071_3_, double p_177071_5_, double p_177071_7_)
    {
        AxisAlignedBB axisalignedbb = p_177071_1_.getRenderBoundingBox().grow(0.5D);

        if (axisalignedbb.hasNaN() || axisalignedbb.getAverageEdgeLength() == 0.0D)
        {
            axisalignedbb = new AxisAlignedBB(p_177071_1_.posX - 2.0D, p_177071_1_.posY - 2.0D, p_177071_1_.posZ - 2.0D, p_177071_1_.posX + 2.0D, p_177071_1_.posY + 2.0D, p_177071_1_.posZ + 2.0D);
        }

        return p_177071_1_.isInRangeToRender3d(p_177071_3_, p_177071_5_, p_177071_7_) && (p_177071_1_.ignoreFrustumCheck || p_177071_2_.func_78546_a(axisalignedbb));
    }

    public void func_76986_a(T p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        if (!this.field_188301_f)
        {
            this.func_177067_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_);
        }
    }

    protected int func_188298_c(T p_188298_1_)
    {
        int i = 16777215;
        ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)p_188298_1_.getTeam();

        if (scoreplayerteam != null)
        {
            String s = FontRenderer.func_78282_e(scoreplayerteam.func_96668_e());

            if (s.length() >= 2)
            {
                i = this.getFontRendererFromRenderManager().func_175064_b(s.charAt(1));
            }
        }

        return i;
    }

    protected void func_177067_a(T p_177067_1_, double p_177067_2_, double p_177067_4_, double p_177067_6_)
    {
        if (this.canRenderName(p_177067_1_))
        {
            this.func_147906_a(p_177067_1_, p_177067_1_.getDisplayName().getFormattedText(), p_177067_2_, p_177067_4_, p_177067_6_, 64);
        }
    }

    protected boolean canRenderName(T entity)
    {
        return entity.getAlwaysRenderNameTagForRender() && entity.hasCustomName();
    }

    protected void func_188296_a(T p_188296_1_, double p_188296_2_, double p_188296_4_, double p_188296_6_, String p_188296_8_, double p_188296_9_)
    {
        this.func_147906_a(p_188296_1_, p_188296_8_, p_188296_2_, p_188296_4_, p_188296_6_, 64);
    }

    @Nullable

    /**
     * Returns the location of an entity's texture.
     */
    protected abstract ResourceLocation getEntityTexture(T entity);

    protected boolean func_180548_c(T p_180548_1_)
    {
        ResourceLocation resourcelocation = this.getEntityTexture(p_180548_1_);

        if (resourcelocation == null)
        {
            return false;
        }
        else
        {
            this.func_110776_a(resourcelocation);
            return true;
        }
    }

    public void func_110776_a(ResourceLocation p_110776_1_)
    {
        this.renderManager.textureManager.bindTexture(p_110776_1_);
    }

    private void func_76977_a(Entity p_76977_1_, double p_76977_2_, double p_76977_4_, double p_76977_6_, float p_76977_8_)
    {
        GlStateManager.func_179140_f();
        TextureMap texturemap = Minecraft.getInstance().func_147117_R();
        TextureAtlasSprite textureatlassprite = texturemap.func_110572_b("minecraft:blocks/fire_layer_0");
        TextureAtlasSprite textureatlassprite1 = texturemap.func_110572_b("minecraft:blocks/fire_layer_1");
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)p_76977_2_, (float)p_76977_4_, (float)p_76977_6_);
        float f = p_76977_1_.field_70130_N * 1.4F;
        GlStateManager.func_179152_a(f, f, f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = p_76977_1_.field_70131_O / f;
        float f4 = (float)(p_76977_1_.posY - p_76977_1_.getBoundingBox().minY);
        GlStateManager.func_179114_b(-this.renderManager.field_78735_i, 0.0F, 1.0F, 0.0F);
        GlStateManager.func_179109_b(0.0F, 0.0F, -0.3F + (float)((int)f3) * 0.02F);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        float f5 = 0.0F;
        int i = 0;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

        while (f3 > 0.0F)
        {
            TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
            this.func_110776_a(TextureMap.LOCATION_BLOCKS_TEXTURE);
            float f6 = textureatlassprite2.getMinU();
            float f7 = textureatlassprite2.getMinV();
            float f8 = textureatlassprite2.getMaxU();
            float f9 = textureatlassprite2.getMaxV();

            if (i / 2 % 2 == 0)
            {
                float f10 = f8;
                f8 = f6;
                f6 = f10;
            }

            bufferbuilder.func_181662_b((double)(f1 - 0.0F), (double)(0.0F - f4), (double)f5).func_187315_a((double)f8, (double)f9).endVertex();
            bufferbuilder.func_181662_b((double)(-f1 - 0.0F), (double)(0.0F - f4), (double)f5).func_187315_a((double)f6, (double)f9).endVertex();
            bufferbuilder.func_181662_b((double)(-f1 - 0.0F), (double)(1.4F - f4), (double)f5).func_187315_a((double)f6, (double)f7).endVertex();
            bufferbuilder.func_181662_b((double)(f1 - 0.0F), (double)(1.4F - f4), (double)f5).func_187315_a((double)f8, (double)f7).endVertex();
            f3 -= 0.45F;
            f4 -= 0.45F;
            f1 *= 0.9F;
            f5 += 0.03F;
            ++i;
        }

        tessellator.draw();
        GlStateManager.func_179121_F();
        GlStateManager.func_179145_e();
    }

    private void func_76975_c(Entity p_76975_1_, double p_76975_2_, double p_76975_4_, double p_76975_6_, float p_76975_8_, float p_76975_9_)
    {
        GlStateManager.func_179147_l();
        GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.renderManager.textureManager.bindTexture(field_110778_a);
        World world = this.func_76982_b();
        GlStateManager.func_179132_a(false);
        float f = this.shadowSize;

        if (p_76975_1_ instanceof EntityLiving)
        {
            EntityLiving entityliving = (EntityLiving)p_76975_1_;
            f *= entityliving.func_70603_bj();

            if (entityliving.isChild())
            {
                f *= 0.5F;
            }
        }

        double d5 = p_76975_1_.lastTickPosX + (p_76975_1_.posX - p_76975_1_.lastTickPosX) * (double)p_76975_9_;
        double d0 = p_76975_1_.lastTickPosY + (p_76975_1_.posY - p_76975_1_.lastTickPosY) * (double)p_76975_9_;
        double d1 = p_76975_1_.lastTickPosZ + (p_76975_1_.posZ - p_76975_1_.lastTickPosZ) * (double)p_76975_9_;
        int i = MathHelper.floor(d5 - (double)f);
        int j = MathHelper.floor(d5 + (double)f);
        int k = MathHelper.floor(d0 - (double)f);
        int l = MathHelper.floor(d0);
        int i1 = MathHelper.floor(d1 - (double)f);
        int j1 = MathHelper.floor(d1 + (double)f);
        double d2 = p_76975_2_ - d5;
        double d3 = p_76975_4_ - d0;
        double d4 = p_76975_6_ - d1;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

        for (BlockPos blockpos : BlockPos.func_177975_b(new BlockPos(i, k, i1), new BlockPos(j, l, j1)))
        {
            IBlockState iblockstate = world.getBlockState(blockpos.down());

            if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE && world.func_175671_l(blockpos) > 3)
            {
                this.func_188299_a(iblockstate, p_76975_2_, p_76975_4_, p_76975_6_, blockpos, p_76975_8_, f, d2, d3, d4);
            }
        }

        tessellator.draw();
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179084_k();
        GlStateManager.func_179132_a(true);
    }

    private World func_76982_b()
    {
        return this.renderManager.world;
    }

    private void func_188299_a(IBlockState p_188299_1_, double p_188299_2_, double p_188299_4_, double p_188299_6_, BlockPos p_188299_8_, float p_188299_9_, float p_188299_10_, double p_188299_11_, double p_188299_13_, double p_188299_15_)
    {
        if (p_188299_1_.func_185917_h())
        {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            double d0 = ((double)p_188299_9_ - (p_188299_4_ - ((double)p_188299_8_.getY() + p_188299_13_)) / 2.0D) * 0.5D * (double)this.func_76982_b().func_175724_o(p_188299_8_);

            if (d0 >= 0.0D)
            {
                if (d0 > 1.0D)
                {
                    d0 = 1.0D;
                }

                AxisAlignedBB axisalignedbb = p_188299_1_.func_185900_c(this.func_76982_b(), p_188299_8_);
                double d1 = (double)p_188299_8_.getX() + axisalignedbb.minX + p_188299_11_;
                double d2 = (double)p_188299_8_.getX() + axisalignedbb.maxX + p_188299_11_;
                double d3 = (double)p_188299_8_.getY() + axisalignedbb.minY + p_188299_13_ + 0.015625D;
                double d4 = (double)p_188299_8_.getZ() + axisalignedbb.minZ + p_188299_15_;
                double d5 = (double)p_188299_8_.getZ() + axisalignedbb.maxZ + p_188299_15_;
                float f = (float)((p_188299_2_ - d1) / 2.0D / (double)p_188299_10_ + 0.5D);
                float f1 = (float)((p_188299_2_ - d2) / 2.0D / (double)p_188299_10_ + 0.5D);
                float f2 = (float)((p_188299_6_ - d4) / 2.0D / (double)p_188299_10_ + 0.5D);
                float f3 = (float)((p_188299_6_ - d5) / 2.0D / (double)p_188299_10_ + 0.5D);
                bufferbuilder.func_181662_b(d1, d3, d4).func_187315_a((double)f, (double)f2).func_181666_a(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
                bufferbuilder.func_181662_b(d1, d3, d5).func_187315_a((double)f, (double)f3).func_181666_a(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
                bufferbuilder.func_181662_b(d2, d3, d5).func_187315_a((double)f1, (double)f3).func_181666_a(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
                bufferbuilder.func_181662_b(d2, d3, d4).func_187315_a((double)f1, (double)f2).func_181666_a(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
            }
        }
    }

    public static void func_76978_a(AxisAlignedBB p_76978_0_, double p_76978_1_, double p_76978_3_, double p_76978_5_)
    {
        GlStateManager.func_179090_x();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        bufferbuilder.func_178969_c(p_76978_1_, p_76978_3_, p_76978_5_);
        bufferbuilder.begin(7, DefaultVertexFormats.field_181708_h);
        bufferbuilder.func_181662_b(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ).func_181663_c(0.0F, 0.0F, -1.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ).func_181663_c(0.0F, 0.0F, -1.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ).func_181663_c(0.0F, 0.0F, -1.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ).func_181663_c(0.0F, 0.0F, -1.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ).func_181663_c(0.0F, 0.0F, 1.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ).func_181663_c(0.0F, 0.0F, 1.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ).func_181663_c(0.0F, 0.0F, 1.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ).func_181663_c(0.0F, 0.0F, 1.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ).func_181663_c(0.0F, -1.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ).func_181663_c(0.0F, -1.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ).func_181663_c(0.0F, -1.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ).func_181663_c(0.0F, -1.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ).func_181663_c(-1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ).func_181663_c(-1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ).func_181663_c(-1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ).func_181663_c(-1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ).func_181663_c(1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ).func_181663_c(1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ).func_181663_c(1.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.func_181662_b(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ).func_181663_c(1.0F, 0.0F, 0.0F).endVertex();
        tessellator.draw();
        bufferbuilder.func_178969_c(0.0D, 0.0D, 0.0D);
        GlStateManager.func_179098_w();
    }

    public void func_76979_b(Entity p_76979_1_, double p_76979_2_, double p_76979_4_, double p_76979_6_, float p_76979_8_, float p_76979_9_)
    {
        if (this.renderManager.options != null)
        {
            if (this.renderManager.options.entityShadows && this.shadowSize > 0.0F && !p_76979_1_.isInvisible() && this.renderManager.func_178627_a())
            {
                double d0 = this.renderManager.getDistanceToCamera(p_76979_1_.posX, p_76979_1_.posY, p_76979_1_.posZ);
                float f = (float)((1.0D - d0 / 256.0D) * (double)this.shadowOpaque);

                if (f > 0.0F)
                {
                    this.func_76975_c(p_76979_1_, p_76979_2_, p_76979_4_, p_76979_6_, f, p_76979_9_);
                }
            }

            if (p_76979_1_.canRenderOnFire() && (!(p_76979_1_ instanceof EntityPlayer) || !((EntityPlayer)p_76979_1_).isSpectator()))
            {
                this.func_76977_a(p_76979_1_, p_76979_2_, p_76979_4_, p_76979_6_, p_76979_9_);
            }
        }
    }

    /**
     * Returns the font renderer from the set render manager
     */
    public FontRenderer getFontRendererFromRenderManager()
    {
        return this.renderManager.getFontRenderer();
    }

    protected void func_147906_a(T p_147906_1_, String p_147906_2_, double p_147906_3_, double p_147906_5_, double p_147906_7_, int p_147906_9_)
    {
        double d0 = p_147906_1_.getDistanceSq(this.renderManager.field_78734_h);

        if (d0 <= (double)(p_147906_9_ * p_147906_9_))
        {
            boolean flag = p_147906_1_.func_70093_af();
            float f = this.renderManager.field_78735_i;
            float f1 = this.renderManager.field_78732_j;
            boolean flag1 = this.renderManager.options.thirdPersonView == 2;
            float f2 = p_147906_1_.field_70131_O + 0.5F - (flag ? 0.25F : 0.0F);
            int i = "deadmau5".equals(p_147906_2_) ? -10 : 0;
            EntityRenderer.func_189692_a(this.getFontRendererFromRenderManager(), p_147906_2_, (float)p_147906_3_, (float)p_147906_5_ + f2, (float)p_147906_7_, i, f, f1, flag1, flag);
        }
    }

    public RenderManager getRenderManager()
    {
        return this.renderManager;
    }

    public boolean func_188295_H_()
    {
        return false;
    }

    public void func_188300_b(T p_188300_1_, double p_188300_2_, double p_188300_4_, double p_188300_6_, float p_188300_8_, float p_188300_9_)
    {
    }
}

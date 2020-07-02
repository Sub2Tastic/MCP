package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelGuardian;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class RenderGuardian extends RenderLiving<EntityGuardian>
{
    private static final ResourceLocation GUARDIAN_TEXTURE = new ResourceLocation("textures/entity/guardian.png");
    private static final ResourceLocation GUARDIAN_BEAM_TEXTURE = new ResourceLocation("textures/entity/guardian_beam.png");

    public RenderGuardian(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelGuardian(), 0.5F);
    }

    public boolean func_177071_a(EntityGuardian p_177071_1_, ICamera p_177071_2_, double p_177071_3_, double p_177071_5_, double p_177071_7_)
    {
        if (super.func_177071_a(p_177071_1_, p_177071_2_, p_177071_3_, p_177071_5_, p_177071_7_))
        {
            return true;
        }
        else
        {
            if (p_177071_1_.hasTargetedEntity())
            {
                EntityLivingBase entitylivingbase = p_177071_1_.getTargetedEntity();

                if (entitylivingbase != null)
                {
                    Vec3d vec3d = this.getPosition(entitylivingbase, (double)entitylivingbase.field_70131_O * 0.5D, 1.0F);
                    Vec3d vec3d1 = this.getPosition(p_177071_1_, (double)p_177071_1_.getEyeHeight(), 1.0F);

                    if (p_177071_2_.func_78546_a(new AxisAlignedBB(vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y, vec3d.z)))
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private Vec3d getPosition(EntityLivingBase entityLivingBaseIn, double p_177110_2_, float p_177110_4_)
    {
        double d0 = entityLivingBaseIn.lastTickPosX + (entityLivingBaseIn.posX - entityLivingBaseIn.lastTickPosX) * (double)p_177110_4_;
        double d1 = p_177110_2_ + entityLivingBaseIn.lastTickPosY + (entityLivingBaseIn.posY - entityLivingBaseIn.lastTickPosY) * (double)p_177110_4_;
        double d2 = entityLivingBaseIn.lastTickPosZ + (entityLivingBaseIn.posZ - entityLivingBaseIn.lastTickPosZ) * (double)p_177110_4_;
        return new Vec3d(d0, d1, d2);
    }

    public void func_76986_a(EntityGuardian p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        EntityLivingBase entitylivingbase = p_76986_1_.getTargetedEntity();

        if (entitylivingbase != null)
        {
            float f = p_76986_1_.getAttackAnimationScale(p_76986_9_);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            this.func_110776_a(GUARDIAN_BEAM_TEXTURE);
            GlStateManager.func_187421_b(3553, 10242, 10497);
            GlStateManager.func_187421_b(3553, 10243, 10497);
            GlStateManager.func_179140_f();
            GlStateManager.func_179129_p();
            GlStateManager.func_179084_k();
            GlStateManager.func_179132_a(true);
            float f1 = 240.0F;
            OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0F, 240.0F);
            GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            float f2 = (float)p_76986_1_.world.getGameTime() + p_76986_9_;
            float f3 = f2 * 0.5F % 1.0F;
            float f4 = p_76986_1_.getEyeHeight();
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)p_76986_2_, (float)p_76986_4_ + f4, (float)p_76986_6_);
            Vec3d vec3d = this.getPosition(entitylivingbase, (double)entitylivingbase.field_70131_O * 0.5D, p_76986_9_);
            Vec3d vec3d1 = this.getPosition(p_76986_1_, (double)f4, p_76986_9_);
            Vec3d vec3d2 = vec3d.subtract(vec3d1);
            double d0 = vec3d2.length() + 1.0D;
            vec3d2 = vec3d2.normalize();
            float f5 = (float)Math.acos(vec3d2.y);
            float f6 = (float)Math.atan2(vec3d2.z, vec3d2.x);
            GlStateManager.func_179114_b((((float)Math.PI / 2F) + -f6) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(f5 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
            int i = 1;
            double d1 = (double)f2 * 0.05D * -1.5D;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            float f7 = f * f;
            int j = 64 + (int)(f7 * 191.0F);
            int k = 32 + (int)(f7 * 191.0F);
            int l = 128 - (int)(f7 * 64.0F);
            double d2 = 0.2D;
            double d3 = 0.282D;
            double d4 = 0.0D + Math.cos(d1 + 2.356194490192345D) * 0.282D;
            double d5 = 0.0D + Math.sin(d1 + 2.356194490192345D) * 0.282D;
            double d6 = 0.0D + Math.cos(d1 + (Math.PI / 4D)) * 0.282D;
            double d7 = 0.0D + Math.sin(d1 + (Math.PI / 4D)) * 0.282D;
            double d8 = 0.0D + Math.cos(d1 + 3.9269908169872414D) * 0.282D;
            double d9 = 0.0D + Math.sin(d1 + 3.9269908169872414D) * 0.282D;
            double d10 = 0.0D + Math.cos(d1 + 5.497787143782138D) * 0.282D;
            double d11 = 0.0D + Math.sin(d1 + 5.497787143782138D) * 0.282D;
            double d12 = 0.0D + Math.cos(d1 + Math.PI) * 0.2D;
            double d13 = 0.0D + Math.sin(d1 + Math.PI) * 0.2D;
            double d14 = 0.0D + Math.cos(d1 + 0.0D) * 0.2D;
            double d15 = 0.0D + Math.sin(d1 + 0.0D) * 0.2D;
            double d16 = 0.0D + Math.cos(d1 + (Math.PI / 2D)) * 0.2D;
            double d17 = 0.0D + Math.sin(d1 + (Math.PI / 2D)) * 0.2D;
            double d18 = 0.0D + Math.cos(d1 + (Math.PI * 3D / 2D)) * 0.2D;
            double d19 = 0.0D + Math.sin(d1 + (Math.PI * 3D / 2D)) * 0.2D;
            double d20 = 0.0D;
            double d21 = 0.4999D;
            double d22 = (double)(-1.0F + f3);
            double d23 = d0 * 2.5D + d22;
            bufferbuilder.func_181662_b(d12, d0, d13).func_187315_a(0.4999D, d23).func_181669_b(j, k, l, 255).endVertex();
            bufferbuilder.func_181662_b(d12, 0.0D, d13).func_187315_a(0.4999D, d22).func_181669_b(j, k, l, 255).endVertex();
            bufferbuilder.func_181662_b(d14, 0.0D, d15).func_187315_a(0.0D, d22).func_181669_b(j, k, l, 255).endVertex();
            bufferbuilder.func_181662_b(d14, d0, d15).func_187315_a(0.0D, d23).func_181669_b(j, k, l, 255).endVertex();
            bufferbuilder.func_181662_b(d16, d0, d17).func_187315_a(0.4999D, d23).func_181669_b(j, k, l, 255).endVertex();
            bufferbuilder.func_181662_b(d16, 0.0D, d17).func_187315_a(0.4999D, d22).func_181669_b(j, k, l, 255).endVertex();
            bufferbuilder.func_181662_b(d18, 0.0D, d19).func_187315_a(0.0D, d22).func_181669_b(j, k, l, 255).endVertex();
            bufferbuilder.func_181662_b(d18, d0, d19).func_187315_a(0.0D, d23).func_181669_b(j, k, l, 255).endVertex();
            double d24 = 0.0D;

            if (p_76986_1_.ticksExisted % 2 == 0)
            {
                d24 = 0.5D;
            }

            bufferbuilder.func_181662_b(d4, d0, d5).func_187315_a(0.5D, d24 + 0.5D).func_181669_b(j, k, l, 255).endVertex();
            bufferbuilder.func_181662_b(d6, d0, d7).func_187315_a(1.0D, d24 + 0.5D).func_181669_b(j, k, l, 255).endVertex();
            bufferbuilder.func_181662_b(d10, d0, d11).func_187315_a(1.0D, d24).func_181669_b(j, k, l, 255).endVertex();
            bufferbuilder.func_181662_b(d8, d0, d9).func_187315_a(0.5D, d24).func_181669_b(j, k, l, 255).endVertex();
            tessellator.draw();
            GlStateManager.func_179121_F();
        }
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityGuardian entity)
    {
        return GUARDIAN_TEXTURE;
    }
}

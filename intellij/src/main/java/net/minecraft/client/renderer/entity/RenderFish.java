package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderFish extends Render<EntityFishHook>
{
    private static final ResourceLocation field_110792_a = new ResourceLocation("textures/particle/particles.png");

    public RenderFish(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void func_76986_a(EntityFishHook p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        EntityPlayer entityplayer = p_76986_1_.getAngler();

        if (entityplayer != null && !this.field_188301_f)
        {
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
            GlStateManager.func_179091_B();
            GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
            this.func_180548_c(p_76986_1_);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            int i = 1;
            int j = 2;
            float f = 0.0625F;
            float f1 = 0.125F;
            float f2 = 0.125F;
            float f3 = 0.1875F;
            float f4 = 1.0F;
            float f5 = 0.5F;
            float f6 = 0.5F;
            GlStateManager.func_179114_b(180.0F - this.renderManager.field_78735_i, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.field_78732_j, 1.0F, 0.0F, 0.0F);

            if (this.field_188301_f)
            {
                GlStateManager.func_179142_g();
                GlStateManager.func_187431_e(this.func_188298_c(p_76986_1_));
            }

            bufferbuilder.begin(7, DefaultVertexFormats.field_181710_j);
            bufferbuilder.func_181662_b(-0.5D, -0.5D, 0.0D).func_187315_a(0.0625D, 0.1875D).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.func_181662_b(0.5D, -0.5D, 0.0D).func_187315_a(0.125D, 0.1875D).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.func_181662_b(0.5D, 0.5D, 0.0D).func_187315_a(0.125D, 0.125D).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.func_181662_b(-0.5D, 0.5D, 0.0D).func_187315_a(0.0625D, 0.125D).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
            tessellator.draw();

            if (this.field_188301_f)
            {
                GlStateManager.func_187417_n();
                GlStateManager.func_179119_h();
            }

            GlStateManager.func_179101_C();
            GlStateManager.func_179121_F();
            int k = entityplayer.getPrimaryHand() == EnumHandSide.RIGHT ? 1 : -1;
            ItemStack itemstack = entityplayer.getHeldItemMainhand();

            if (itemstack.getItem() != Items.FISHING_ROD)
            {
                k = -k;
            }

            float f7 = entityplayer.getSwingProgress(p_76986_9_);
            float f8 = MathHelper.sin(MathHelper.sqrt(f7) * (float)Math.PI);
            float f9 = (entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * p_76986_9_) * 0.017453292F;
            double d0 = (double)MathHelper.sin(f9);
            double d1 = (double)MathHelper.cos(f9);
            double d2 = (double)k * 0.35D;
            double d3 = 0.8D;
            double d4;
            double d5;
            double d6;
            double d7;

            if ((this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0) && entityplayer == Minecraft.getInstance().player)
            {
                float f10 = this.renderManager.options.fov;
                f10 = f10 / 100.0F;
                Vec3d vec3d = new Vec3d((double)k * -0.36D * (double)f10, -0.045D * (double)f10, 0.4D);
                vec3d = vec3d.rotatePitch(-(entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * p_76986_9_) * 0.017453292F);
                vec3d = vec3d.rotateYaw(-(entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * p_76986_9_) * 0.017453292F);
                vec3d = vec3d.rotateYaw(f8 * 0.5F);
                vec3d = vec3d.rotatePitch(-f8 * 0.7F);
                d4 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)p_76986_9_ + vec3d.x;
                d5 = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double)p_76986_9_ + vec3d.y;
                d6 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)p_76986_9_ + vec3d.z;
                d7 = (double)entityplayer.getEyeHeight();
            }
            else
            {
                d4 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)p_76986_9_ - d1 * d2 - d0 * 0.8D;
                d5 = entityplayer.prevPosY + (double)entityplayer.getEyeHeight() + (entityplayer.posY - entityplayer.prevPosY) * (double)p_76986_9_ - 0.45D;
                d6 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)p_76986_9_ - d0 * d2 + d1 * 0.8D;
                d7 = entityplayer.func_70093_af() ? -0.1875D : 0.0D;
            }

            double d13 = p_76986_1_.prevPosX + (p_76986_1_.posX - p_76986_1_.prevPosX) * (double)p_76986_9_;
            double d8 = p_76986_1_.prevPosY + (p_76986_1_.posY - p_76986_1_.prevPosY) * (double)p_76986_9_ + 0.25D;
            double d9 = p_76986_1_.prevPosZ + (p_76986_1_.posZ - p_76986_1_.prevPosZ) * (double)p_76986_9_;
            double d10 = (double)((float)(d4 - d13));
            double d11 = (double)((float)(d5 - d8)) + d7;
            double d12 = (double)((float)(d6 - d9));
            GlStateManager.func_179090_x();
            GlStateManager.func_179140_f();
            bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            int l = 16;

            for (int i1 = 0; i1 <= 16; ++i1)
            {
                float f11 = (float)i1 / 16.0F;
                bufferbuilder.func_181662_b(p_76986_2_ + d10 * (double)f11, p_76986_4_ + d11 * (double)(f11 * f11 + f11) * 0.5D + 0.25D, p_76986_6_ + d12 * (double)f11).func_181669_b(0, 0, 0, 255).endVertex();
            }

            tessellator.draw();
            GlStateManager.func_179145_e();
            GlStateManager.func_179098_w();
            super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        }
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityFishHook entity)
    {
        return field_110792_a;
    }
}

package net.minecraft.client.renderer.entity;

import com.google.common.collect.Lists;
import java.nio.FloatBuffer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class RenderLivingBase<T extends EntityLivingBase> extends Render<T>
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DynamicTexture field_177096_e = new DynamicTexture(16, 16);
    protected ModelBase entityModel;
    protected FloatBuffer field_177095_g = GLAllocation.createDirectFloatBuffer(4);
    protected List<LayerRenderer<T>> layerRenderers = Lists.<LayerRenderer<T>>newArrayList();
    protected boolean field_188323_j;

    public RenderLivingBase(RenderManager p_i46156_1_, ModelBase p_i46156_2_, float p_i46156_3_)
    {
        super(p_i46156_1_);
        this.entityModel = p_i46156_2_;
        this.shadowSize = p_i46156_3_;
    }

    protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U layer)
    {
        return this.layerRenderers.add((LayerRenderer<T>)layer);
    }

    public ModelBase func_177087_b()
    {
        return this.entityModel;
    }

    protected float func_77034_a(float p_77034_1_, float p_77034_2_, float p_77034_3_)
    {
        float f;

        for (f = p_77034_2_ - p_77034_1_; f < -180.0F; f += 360.0F)
        {
            ;
        }

        while (f >= 180.0F)
        {
            f -= 360.0F;
        }

        return p_77034_1_ + p_77034_3_ * f;
    }

    public void func_82422_c()
    {
    }

    public void func_76986_a(T p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        GlStateManager.func_179094_E();
        GlStateManager.func_179129_p();
        this.entityModel.field_78095_p = this.getSwingProgress(p_76986_1_, p_76986_9_);
        this.entityModel.field_78093_q = p_76986_1_.isPassenger();
        this.entityModel.field_78091_s = p_76986_1_.isChild();

        try
        {
            float f = this.func_77034_a(p_76986_1_.prevRenderYawOffset, p_76986_1_.renderYawOffset, p_76986_9_);
            float f1 = this.func_77034_a(p_76986_1_.prevRotationYawHead, p_76986_1_.rotationYawHead, p_76986_9_);
            float f2 = f1 - f;

            if (p_76986_1_.isPassenger() && p_76986_1_.getRidingEntity() instanceof EntityLivingBase)
            {
                EntityLivingBase entitylivingbase = (EntityLivingBase)p_76986_1_.getRidingEntity();
                f = this.func_77034_a(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, p_76986_9_);
                f2 = f1 - f;
                float f3 = MathHelper.wrapDegrees(f2);

                if (f3 < -85.0F)
                {
                    f3 = -85.0F;
                }

                if (f3 >= 85.0F)
                {
                    f3 = 85.0F;
                }

                f = f1 - f3;

                if (f3 * f3 > 2500.0F)
                {
                    f += f3 * 0.2F;
                }

                f2 = f1 - f;
            }

            float f7 = p_76986_1_.prevRotationPitch + (p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch) * p_76986_9_;
            this.func_77039_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_);
            float f8 = this.handleRotationFloat(p_76986_1_, p_76986_9_);
            this.func_77043_a(p_76986_1_, f8, f, p_76986_9_);
            float f4 = this.func_188322_c(p_76986_1_, p_76986_9_);
            float f5 = 0.0F;
            float f6 = 0.0F;

            if (!p_76986_1_.isPassenger())
            {
                f5 = p_76986_1_.prevLimbSwingAmount + (p_76986_1_.limbSwingAmount - p_76986_1_.prevLimbSwingAmount) * p_76986_9_;
                f6 = p_76986_1_.limbSwing - p_76986_1_.limbSwingAmount * (1.0F - p_76986_9_);

                if (p_76986_1_.isChild())
                {
                    f6 *= 3.0F;
                }

                if (f5 > 1.0F)
                {
                    f5 = 1.0F;
                }
            }

            GlStateManager.func_179141_d();
            this.entityModel.func_78086_a(p_76986_1_, f6, f5, p_76986_9_);
            this.entityModel.func_78087_a(f6, f5, f8, f2, f7, f4, p_76986_1_);

            if (this.field_188301_f)
            {
                boolean flag1 = this.func_177088_c(p_76986_1_);
                GlStateManager.func_179142_g();
                GlStateManager.func_187431_e(this.func_188298_c(p_76986_1_));

                if (!this.field_188323_j)
                {
                    this.func_77036_a(p_76986_1_, f6, f5, f8, f2, f7, f4);
                }

                if (!(p_76986_1_ instanceof EntityPlayer) || !((EntityPlayer)p_76986_1_).isSpectator())
                {
                    this.func_177093_a(p_76986_1_, f6, f5, p_76986_9_, f8, f2, f7, f4);
                }

                GlStateManager.func_187417_n();
                GlStateManager.func_179119_h();

                if (flag1)
                {
                    this.func_180565_e();
                }
            }
            else
            {
                boolean flag = this.func_177090_c(p_76986_1_, p_76986_9_);
                this.func_77036_a(p_76986_1_, f6, f5, f8, f2, f7, f4);

                if (flag)
                {
                    this.func_177091_f();
                }

                GlStateManager.func_179132_a(true);

                if (!(p_76986_1_ instanceof EntityPlayer) || !((EntityPlayer)p_76986_1_).isSpectator())
                {
                    this.func_177093_a(p_76986_1_, f6, f5, p_76986_9_, f8, f2, f7, f4);
                }
            }

            GlStateManager.func_179101_C();
        }
        catch (Exception exception)
        {
            LOGGER.error("Couldn't render entity", (Throwable)exception);
        }

        GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
        GlStateManager.func_179098_w();
        GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
        GlStateManager.func_179089_o();
        GlStateManager.func_179121_F();
        super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    public float func_188322_c(T p_188322_1_, float p_188322_2_)
    {
        GlStateManager.func_179091_B();
        GlStateManager.func_179152_a(-1.0F, -1.0F, 1.0F);
        this.func_77041_b(p_188322_1_, p_188322_2_);
        float f = 0.0625F;
        GlStateManager.func_179109_b(0.0F, -1.501F, 0.0F);
        return 0.0625F;
    }

    protected boolean func_177088_c(T p_177088_1_)
    {
        GlStateManager.func_179140_f();
        GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
        return true;
    }

    protected void func_180565_e()
    {
        GlStateManager.func_179145_e();
        GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
        GlStateManager.func_179098_w();
        GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
    }

    protected void func_77036_a(T p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_)
    {
        boolean flag = this.func_193115_c(p_77036_1_);
        boolean flag1 = !flag && !p_77036_1_.isInvisibleToPlayer(Minecraft.getInstance().player);

        if (flag || flag1)
        {
            if (!this.func_180548_c(p_77036_1_))
            {
                return;
            }

            if (flag1)
            {
                GlStateManager.func_187408_a(GlStateManager.Profile.TRANSPARENT_MODEL);
            }

            this.entityModel.func_78088_a(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);

            if (flag1)
            {
                GlStateManager.func_187440_b(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
    }

    protected boolean func_193115_c(T p_193115_1_)
    {
        return !p_193115_1_.isInvisible() || this.field_188301_f;
    }

    protected boolean func_177090_c(T p_177090_1_, float p_177090_2_)
    {
        return this.func_177092_a(p_177090_1_, p_177090_2_, true);
    }

    protected boolean func_177092_a(T p_177092_1_, float p_177092_2_, boolean p_177092_3_)
    {
        float f = p_177092_1_.getBrightness();
        int i = this.func_77030_a(p_177092_1_, f, p_177092_2_);
        boolean flag = (i >> 24 & 255) > 0;
        boolean flag1 = p_177092_1_.hurtTime > 0 || p_177092_1_.deathTime > 0;

        if (!flag && !flag1)
        {
            return false;
        }
        else if (!flag && !p_177092_3_)
        {
            return false;
        }
        else
        {
            GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
            GlStateManager.func_179098_w();
            GlStateManager.func_187399_a(8960, 8704, OpenGlHelper.field_176095_s);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176099_x, 8448);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176098_y, OpenGlHelper.field_77478_a);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176097_z, OpenGlHelper.field_176093_u);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176081_B, 768);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176082_C, 768);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176077_E, 7681);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176078_F, OpenGlHelper.field_77478_a);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176085_I, 770);
            GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
            GlStateManager.func_179098_w();
            GlStateManager.func_187399_a(8960, 8704, OpenGlHelper.field_176095_s);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176099_x, OpenGlHelper.field_176094_t);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176098_y, OpenGlHelper.field_176092_v);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176097_z, OpenGlHelper.field_176091_w);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176080_A, OpenGlHelper.field_176092_v);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176081_B, 768);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176082_C, 768);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176076_D, 770);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176077_E, 7681);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176078_F, OpenGlHelper.field_176091_w);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176085_I, 770);
            this.field_177095_g.position(0);

            if (flag1)
            {
                this.field_177095_g.put(1.0F);
                this.field_177095_g.put(0.0F);
                this.field_177095_g.put(0.0F);
                this.field_177095_g.put(0.3F);
            }
            else
            {
                float f1 = (float)(i >> 24 & 255) / 255.0F;
                float f2 = (float)(i >> 16 & 255) / 255.0F;
                float f3 = (float)(i >> 8 & 255) / 255.0F;
                float f4 = (float)(i & 255) / 255.0F;
                this.field_177095_g.put(f2);
                this.field_177095_g.put(f3);
                this.field_177095_g.put(f4);
                this.field_177095_g.put(1.0F - f1);
            }

            this.field_177095_g.flip();
            GlStateManager.func_187448_b(8960, 8705, this.field_177095_g);
            GlStateManager.func_179138_g(OpenGlHelper.field_176096_r);
            GlStateManager.func_179098_w();
            GlStateManager.func_179144_i(field_177096_e.getGlTextureId());
            GlStateManager.func_187399_a(8960, 8704, OpenGlHelper.field_176095_s);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176099_x, 8448);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176098_y, OpenGlHelper.field_176091_w);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176097_z, OpenGlHelper.field_77476_b);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176081_B, 768);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176082_C, 768);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176077_E, 7681);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176078_F, OpenGlHelper.field_176091_w);
            GlStateManager.func_187399_a(8960, OpenGlHelper.field_176085_I, 770);
            GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
            return true;
        }
    }

    protected void func_177091_f()
    {
        GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
        GlStateManager.func_179098_w();
        GlStateManager.func_187399_a(8960, 8704, OpenGlHelper.field_176095_s);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176099_x, 8448);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176098_y, OpenGlHelper.field_77478_a);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176097_z, OpenGlHelper.field_176093_u);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176081_B, 768);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176082_C, 768);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176077_E, 8448);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176078_F, OpenGlHelper.field_77478_a);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176079_G, OpenGlHelper.field_176093_u);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176085_I, 770);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176086_J, 770);
        GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
        GlStateManager.func_187399_a(8960, 8704, OpenGlHelper.field_176095_s);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176099_x, 8448);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176081_B, 768);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176082_C, 768);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176098_y, 5890);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176097_z, OpenGlHelper.field_176091_w);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176077_E, 8448);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176085_I, 770);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176078_F, 5890);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179138_g(OpenGlHelper.field_176096_r);
        GlStateManager.func_179090_x();
        GlStateManager.func_179144_i(0);
        GlStateManager.func_187399_a(8960, 8704, OpenGlHelper.field_176095_s);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176099_x, 8448);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176081_B, 768);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176082_C, 768);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176098_y, 5890);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176097_z, OpenGlHelper.field_176091_w);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176077_E, 8448);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176085_I, 770);
        GlStateManager.func_187399_a(8960, OpenGlHelper.field_176078_F, 5890);
        GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
    }

    protected void func_77039_a(T p_77039_1_, double p_77039_2_, double p_77039_4_, double p_77039_6_)
    {
        GlStateManager.func_179109_b((float)p_77039_2_, (float)p_77039_4_, (float)p_77039_6_);
    }

    protected void func_77043_a(T p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        GlStateManager.func_179114_b(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);

        if (p_77043_1_.deathTime > 0)
        {
            float f = ((float)p_77043_1_.deathTime + p_77043_4_ - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);

            if (f > 1.0F)
            {
                f = 1.0F;
            }

            GlStateManager.func_179114_b(f * this.getDeathMaxRotation(p_77043_1_), 0.0F, 0.0F, 1.0F);
        }
        else
        {
            String s = TextFormatting.getTextWithoutFormattingCodes(p_77043_1_.func_70005_c_());

            if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(p_77043_1_ instanceof EntityPlayer) || ((EntityPlayer)p_77043_1_).isWearing(EnumPlayerModelParts.CAPE)))
            {
                GlStateManager.func_179109_b(0.0F, p_77043_1_.field_70131_O + 0.1F, 0.0F);
                GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

    /**
     * Returns where in the swing animation the living entity is (from 0 to 1).  Args : entity, partialTickTime
     */
    protected float getSwingProgress(T livingBase, float partialTickTime)
    {
        return livingBase.getSwingProgress(partialTickTime);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(T livingBase, float partialTicks)
    {
        return (float)livingBase.ticksExisted + partialTicks;
    }

    protected void func_177093_a(T p_177093_1_, float p_177093_2_, float p_177093_3_, float p_177093_4_, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_)
    {
        for (LayerRenderer<T> layerrenderer : this.layerRenderers)
        {
            boolean flag = this.func_177092_a(p_177093_1_, p_177093_4_, layerrenderer.func_177142_b());
            layerrenderer.func_177141_a(p_177093_1_, p_177093_2_, p_177093_3_, p_177093_4_, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);

            if (flag)
            {
                this.func_177091_f();
            }
        }
    }

    protected float getDeathMaxRotation(T entityLivingBaseIn)
    {
        return 90.0F;
    }

    protected int func_77030_a(T p_77030_1_, float p_77030_2_, float p_77030_3_)
    {
        return 0;
    }

    protected void func_77041_b(T p_77041_1_, float p_77041_2_)
    {
    }

    public void func_177067_a(T p_177067_1_, double p_177067_2_, double p_177067_4_, double p_177067_6_)
    {
        if (this.canRenderName(p_177067_1_))
        {
            double d0 = p_177067_1_.getDistanceSq(this.renderManager.field_78734_h);
            float f = p_177067_1_.func_70093_af() ? 32.0F : 64.0F;

            if (d0 < (double)(f * f))
            {
                String s = p_177067_1_.getDisplayName().getFormattedText();
                GlStateManager.func_179092_a(516, 0.1F);
                this.func_188296_a(p_177067_1_, p_177067_2_, p_177067_4_, p_177067_6_, s, d0);
            }
        }
    }

    protected boolean canRenderName(T entity)
    {
        EntityPlayerSP entityplayersp = Minecraft.getInstance().player;
        boolean flag = !entity.isInvisibleToPlayer(entityplayersp);

        if (entity != entityplayersp)
        {
            Team team = entity.getTeam();
            Team team1 = entityplayersp.getTeam();

            if (team != null)
            {
                Team.EnumVisible team$enumvisible = team.getNameTagVisibility();

                switch (team$enumvisible)
                {
                    case ALWAYS:
                        return flag;

                    case NEVER:
                        return false;

                    case HIDE_FOR_OTHER_TEAMS:
                        return team1 == null ? flag : team.isSameTeam(team1) && (team.getSeeFriendlyInvisiblesEnabled() || flag);

                    case HIDE_FOR_OWN_TEAM:
                        return team1 == null ? flag : !team.isSameTeam(team1) && flag;

                    default:
                        return true;
                }
            }
        }

        return Minecraft.isGuiEnabled() && entity != this.renderManager.field_78734_h && flag && !entity.isBeingRidden();
    }

    static
    {
        int[] aint = field_177096_e.func_110565_c();

        for (int i = 0; i < 256; ++i)
        {
            aint[i] = -1;
        }

        field_177096_e.updateDynamicTexture();
    }
}

package net.minecraft.client.renderer.entity;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderPlayer extends RenderLivingBase<AbstractClientPlayer>
{
    private final boolean field_177140_a;

    public RenderPlayer(RenderManager renderManager)
    {
        this(renderManager, false);
    }

    public RenderPlayer(RenderManager renderManager, boolean useSmallArms)
    {
        super(renderManager, new ModelPlayer(0.0F, useSmallArms), 0.5F);
        this.field_177140_a = useSmallArms;
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerDeadmau5Head(this));
        this.addLayer(new LayerCape(this));
        this.addLayer(new LayerCustomHead(this.func_177087_b().bipedHead));
        this.addLayer(new LayerElytra(this));
        this.addLayer(new LayerEntityOnShoulder(renderManager));
    }

    public ModelPlayer func_177087_b()
    {
        return (ModelPlayer)super.func_177087_b();
    }

    public void func_76986_a(AbstractClientPlayer p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        if (!p_76986_1_.isUser() || this.renderManager.field_78734_h == p_76986_1_)
        {
            double d0 = p_76986_4_;

            if (p_76986_1_.func_70093_af())
            {
                d0 = p_76986_4_ - 0.125D;
            }

            this.setModelVisibilities(p_76986_1_);
            GlStateManager.func_187408_a(GlStateManager.Profile.PLAYER_SKIN);
            super.func_76986_a(p_76986_1_, p_76986_2_, d0, p_76986_6_, p_76986_8_, p_76986_9_);
            GlStateManager.func_187440_b(GlStateManager.Profile.PLAYER_SKIN);
        }
    }

    private void setModelVisibilities(AbstractClientPlayer clientPlayer)
    {
        ModelPlayer modelplayer = this.func_177087_b();

        if (clientPlayer.isSpectator())
        {
            modelplayer.setVisible(false);
            modelplayer.bipedHead.showModel = true;
            modelplayer.bipedHeadwear.showModel = true;
        }
        else
        {
            ItemStack itemstack = clientPlayer.getHeldItemMainhand();
            ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
            modelplayer.setVisible(true);
            modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
            modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
            modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
            modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
            modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
            modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
            modelplayer.field_78117_n = clientPlayer.func_70093_af();
            ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
            ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;

            if (!itemstack.isEmpty())
            {
                modelbiped$armpose = ModelBiped.ArmPose.ITEM;

                if (clientPlayer.getItemInUseCount() > 0)
                {
                    EnumAction enumaction = itemstack.getUseAction();

                    if (enumaction == EnumAction.BLOCK)
                    {
                        modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
                    }
                    else if (enumaction == EnumAction.BOW)
                    {
                        modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
                    }
                }
            }

            if (!itemstack1.isEmpty())
            {
                modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

                if (clientPlayer.getItemInUseCount() > 0)
                {
                    EnumAction enumaction1 = itemstack1.getUseAction();

                    if (enumaction1 == EnumAction.BLOCK)
                    {
                        modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
                    }
                }
            }

            if (clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT)
            {
                modelplayer.rightArmPose = modelbiped$armpose;
                modelplayer.leftArmPose = modelbiped$armpose1;
            }
            else
            {
                modelplayer.rightArmPose = modelbiped$armpose1;
                modelplayer.leftArmPose = modelbiped$armpose;
            }
        }
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(AbstractClientPlayer entity)
    {
        return entity.getLocationSkin();
    }

    public void func_82422_c()
    {
        GlStateManager.func_179109_b(0.0F, 0.1875F, 0.0F);
    }

    protected void func_77041_b(AbstractClientPlayer p_77041_1_, float p_77041_2_)
    {
        float f = 0.9375F;
        GlStateManager.func_179152_a(0.9375F, 0.9375F, 0.9375F);
    }

    protected void func_188296_a(AbstractClientPlayer p_188296_1_, double p_188296_2_, double p_188296_4_, double p_188296_6_, String p_188296_8_, double p_188296_9_)
    {
        if (p_188296_9_ < 100.0D)
        {
            Scoreboard scoreboard = p_188296_1_.getWorldScoreboard();
            ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);

            if (scoreobjective != null)
            {
                Score score = scoreboard.getOrCreateScore(p_188296_1_.func_70005_c_(), scoreobjective);
                this.func_147906_a(p_188296_1_, score.getScorePoints() + " " + scoreobjective.getDisplayName(), p_188296_2_, p_188296_4_, p_188296_6_, 64);
                p_188296_4_ += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * 0.025F);
            }
        }

        super.func_188296_a(p_188296_1_, p_188296_2_, p_188296_4_, p_188296_6_, p_188296_8_, p_188296_9_);
    }

    public void func_177138_b(AbstractClientPlayer p_177138_1_)
    {
        float f = 1.0F;
        GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
        float f1 = 0.0625F;
        ModelPlayer modelplayer = this.func_177087_b();
        this.setModelVisibilities(p_177138_1_);
        GlStateManager.func_179147_l();
        modelplayer.field_78095_p = 0.0F;
        modelplayer.field_78117_n = false;
        modelplayer.func_78087_a(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, p_177138_1_);
        modelplayer.bipedRightArm.rotateAngleX = 0.0F;
        modelplayer.bipedRightArm.func_78785_a(0.0625F);
        modelplayer.bipedRightArmwear.rotateAngleX = 0.0F;
        modelplayer.bipedRightArmwear.func_78785_a(0.0625F);
        GlStateManager.func_179084_k();
    }

    public void func_177139_c(AbstractClientPlayer p_177139_1_)
    {
        float f = 1.0F;
        GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
        float f1 = 0.0625F;
        ModelPlayer modelplayer = this.func_177087_b();
        this.setModelVisibilities(p_177139_1_);
        GlStateManager.func_179147_l();
        modelplayer.field_78117_n = false;
        modelplayer.field_78095_p = 0.0F;
        modelplayer.func_78087_a(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, p_177139_1_);
        modelplayer.bipedLeftArm.rotateAngleX = 0.0F;
        modelplayer.bipedLeftArm.func_78785_a(0.0625F);
        modelplayer.bipedLeftArmwear.rotateAngleX = 0.0F;
        modelplayer.bipedLeftArmwear.func_78785_a(0.0625F);
        GlStateManager.func_179084_k();
    }

    protected void func_77039_a(AbstractClientPlayer p_77039_1_, double p_77039_2_, double p_77039_4_, double p_77039_6_)
    {
        if (p_77039_1_.isAlive() && p_77039_1_.isSleeping())
        {
            super.func_77039_a(p_77039_1_, p_77039_2_ + (double)p_77039_1_.field_71079_bU, p_77039_4_ + (double)p_77039_1_.field_71082_cx, p_77039_6_ + (double)p_77039_1_.field_71089_bV);
        }
        else
        {
            super.func_77039_a(p_77039_1_, p_77039_2_, p_77039_4_, p_77039_6_);
        }
    }

    protected void func_77043_a(AbstractClientPlayer p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        if (p_77043_1_.isAlive() && p_77043_1_.isSleeping())
        {
            GlStateManager.func_179114_b(p_77043_1_.func_71051_bG(), 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(this.getDeathMaxRotation(p_77043_1_), 0.0F, 0.0F, 1.0F);
            GlStateManager.func_179114_b(270.0F, 0.0F, 1.0F, 0.0F);
        }
        else if (p_77043_1_.isElytraFlying())
        {
            super.func_77043_a(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
            float f = (float)p_77043_1_.getTicksElytraFlying() + p_77043_4_;
            float f1 = MathHelper.clamp(f * f / 100.0F, 0.0F, 1.0F);
            GlStateManager.func_179114_b(f1 * (-90.0F - p_77043_1_.rotationPitch), 1.0F, 0.0F, 0.0F);
            Vec3d vec3d = p_77043_1_.getLook(p_77043_4_);
            double d0 = p_77043_1_.field_70159_w * p_77043_1_.field_70159_w + p_77043_1_.field_70179_y * p_77043_1_.field_70179_y;
            double d1 = vec3d.x * vec3d.x + vec3d.z * vec3d.z;

            if (d0 > 0.0D && d1 > 0.0D)
            {
                double d2 = (p_77043_1_.field_70159_w * vec3d.x + p_77043_1_.field_70179_y * vec3d.z) / (Math.sqrt(d0) * Math.sqrt(d1));
                double d3 = p_77043_1_.field_70159_w * vec3d.z - p_77043_1_.field_70179_y * vec3d.x;
                GlStateManager.func_179114_b((float)(Math.signum(d3) * Math.acos(d2)) * 180.0F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
            }
        }
        else
        {
            super.func_77043_a(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
        }
    }
}

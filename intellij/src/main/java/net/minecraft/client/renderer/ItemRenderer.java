package net.minecraft.client.renderer;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.MapData;

public class ItemRenderer
{
    private static final ResourceLocation field_110931_c = new ResourceLocation("textures/map/map_background.png");
    private static final ResourceLocation field_110929_d = new ResourceLocation("textures/misc/underwater.png");
    private final Minecraft mc;
    private ItemStack itemStackMainHand = ItemStack.EMPTY;
    private ItemStack itemStackOffHand = ItemStack.EMPTY;
    private float equippedProgressMainHand;
    private float prevEquippedProgressMainHand;
    private float equippedProgressOffHand;
    private float prevEquippedProgressOffHand;
    private final RenderManager renderManager;
    private final RenderItem itemRenderer;

    public ItemRenderer(Minecraft mcIn)
    {
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getItemRenderer();
    }

    public void func_178099_a(EntityLivingBase p_178099_1_, ItemStack p_178099_2_, ItemCameraTransforms.TransformType p_178099_3_)
    {
        this.func_187462_a(p_178099_1_, p_178099_2_, p_178099_3_, false);
    }

    public void func_187462_a(EntityLivingBase p_187462_1_, ItemStack p_187462_2_, ItemCameraTransforms.TransformType p_187462_3_, boolean p_187462_4_)
    {
        if (!p_187462_2_.isEmpty())
        {
            Item item = p_187462_2_.getItem();
            Block block = Block.getBlockFromItem(item);
            GlStateManager.func_179094_E();
            boolean flag = this.itemRenderer.func_175050_a(p_187462_2_) && block.func_180664_k() == BlockRenderLayer.TRANSLUCENT;

            if (flag)
            {
                GlStateManager.func_179132_a(false);
            }

            this.itemRenderer.func_184392_a(p_187462_2_, p_187462_1_, p_187462_3_, p_187462_4_);

            if (flag)
            {
                GlStateManager.func_179132_a(true);
            }

            GlStateManager.func_179121_F();
        }
    }

    private void func_178101_a(float p_178101_1_, float p_178101_2_)
    {
        GlStateManager.func_179094_E();
        GlStateManager.func_179114_b(p_178101_1_, 1.0F, 0.0F, 0.0F);
        GlStateManager.func_179114_b(p_178101_2_, 0.0F, 1.0F, 0.0F);
        RenderHelper.func_74519_b();
        GlStateManager.func_179121_F();
    }

    private void func_187464_b()
    {
        AbstractClientPlayer abstractclientplayer = this.mc.player;
        int i = this.mc.world.func_175626_b(new BlockPos(abstractclientplayer.posX, abstractclientplayer.posY + (double)abstractclientplayer.getEyeHeight(), abstractclientplayer.posZ), 0);
        float f = (float)(i & 65535);
        float f1 = (float)(i >> 16);
        OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, f, f1);
    }

    private void func_187458_c(float p_187458_1_)
    {
        EntityPlayerSP entityplayersp = this.mc.player;
        float f = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * p_187458_1_;
        float f1 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * p_187458_1_;
        GlStateManager.func_179114_b((entityplayersp.rotationPitch - f) * 0.1F, 1.0F, 0.0F, 0.0F);
        GlStateManager.func_179114_b((entityplayersp.rotationYaw - f1) * 0.1F, 0.0F, 1.0F, 0.0F);
    }

    /**
     * Return the angle to render the Map
     */
    private float getMapAngleFromPitch(float pitch)
    {
        float f = 1.0F - pitch / 45.0F + 0.1F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = -MathHelper.cos(f * (float)Math.PI) * 0.5F + 0.5F;
        return f;
    }

    private void func_187466_c()
    {
        if (!this.mc.player.isInvisible())
        {
            GlStateManager.func_179129_p();
            GlStateManager.func_179094_E();
            GlStateManager.func_179114_b(90.0F, 0.0F, 1.0F, 0.0F);
            this.func_187455_a(EnumHandSide.RIGHT);
            this.func_187455_a(EnumHandSide.LEFT);
            GlStateManager.func_179121_F();
            GlStateManager.func_179089_o();
        }
    }

    private void func_187455_a(EnumHandSide p_187455_1_)
    {
        this.mc.getTextureManager().bindTexture(this.mc.player.getLocationSkin());
        Render<AbstractClientPlayer> render = this.renderManager.<AbstractClientPlayer>getRenderer(this.mc.player);
        RenderPlayer renderplayer = (RenderPlayer)render;
        GlStateManager.func_179094_E();
        float f = p_187455_1_ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        GlStateManager.func_179114_b(92.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.func_179114_b(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.func_179114_b(f * -41.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.func_179109_b(f * 0.3F, -1.1F, 0.45F);

        if (p_187455_1_ == EnumHandSide.RIGHT)
        {
            renderplayer.func_177138_b(this.mc.player);
        }
        else
        {
            renderplayer.func_177139_c(this.mc.player);
        }

        GlStateManager.func_179121_F();
    }

    private void func_187465_a(float p_187465_1_, EnumHandSide p_187465_2_, float p_187465_3_, ItemStack p_187465_4_)
    {
        float f = p_187465_2_ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        GlStateManager.func_179109_b(f * 0.125F, -0.125F, 0.0F);

        if (!this.mc.player.isInvisible())
        {
            GlStateManager.func_179094_E();
            GlStateManager.func_179114_b(f * 10.0F, 0.0F, 0.0F, 1.0F);
            this.func_187456_a(p_187465_1_, p_187465_3_, p_187465_2_);
            GlStateManager.func_179121_F();
        }

        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b(f * 0.51F, -0.08F + p_187465_1_ * -1.2F, -0.75F);
        float f1 = MathHelper.sqrt(p_187465_3_);
        float f2 = MathHelper.sin(f1 * (float)Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * MathHelper.sin(f1 * ((float)Math.PI * 2F));
        float f5 = -0.3F * MathHelper.sin(p_187465_3_ * (float)Math.PI);
        GlStateManager.func_179109_b(f * f3, f4 - 0.3F * f2, f5);
        GlStateManager.func_179114_b(f2 * -45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.func_179114_b(f * f2 * -30.0F, 0.0F, 1.0F, 0.0F);
        this.func_187461_a(p_187465_4_);
        GlStateManager.func_179121_F();
    }

    private void func_187463_a(float p_187463_1_, float p_187463_2_, float p_187463_3_)
    {
        float f = MathHelper.sqrt(p_187463_3_);
        float f1 = -0.2F * MathHelper.sin(p_187463_3_ * (float)Math.PI);
        float f2 = -0.4F * MathHelper.sin(f * (float)Math.PI);
        GlStateManager.func_179109_b(0.0F, -f1 / 2.0F, f2);
        float f3 = this.getMapAngleFromPitch(p_187463_1_);
        GlStateManager.func_179109_b(0.0F, 0.04F + p_187463_2_ * -1.2F + f3 * -0.5F, -0.72F);
        GlStateManager.func_179114_b(f3 * -85.0F, 1.0F, 0.0F, 0.0F);
        this.func_187466_c();
        float f4 = MathHelper.sin(f * (float)Math.PI);
        GlStateManager.func_179114_b(f4 * 20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.func_179152_a(2.0F, 2.0F, 2.0F);
        this.func_187461_a(this.itemStackMainHand);
    }

    private void func_187461_a(ItemStack p_187461_1_)
    {
        GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.func_179152_a(0.38F, 0.38F, 0.38F);
        GlStateManager.func_179140_f();
        this.mc.getTextureManager().bindTexture(field_110931_c);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.func_179109_b(-0.5F, -0.5F, 0.0F);
        GlStateManager.func_179152_a(0.0078125F, 0.0078125F, 0.0078125F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.func_181662_b(-7.0D, 135.0D, 0.0D).func_187315_a(0.0D, 1.0D).endVertex();
        bufferbuilder.func_181662_b(135.0D, 135.0D, 0.0D).func_187315_a(1.0D, 1.0D).endVertex();
        bufferbuilder.func_181662_b(135.0D, -7.0D, 0.0D).func_187315_a(1.0D, 0.0D).endVertex();
        bufferbuilder.func_181662_b(-7.0D, -7.0D, 0.0D).func_187315_a(0.0D, 0.0D).endVertex();
        tessellator.draw();
        MapData mapdata = Items.FILLED_MAP.func_77873_a(p_187461_1_, this.mc.world);

        if (mapdata != null)
        {
            this.mc.gameRenderer.getMapItemRenderer().func_148250_a(mapdata, false);
        }

        GlStateManager.func_179145_e();
    }

    private void func_187456_a(float p_187456_1_, float p_187456_2_, EnumHandSide p_187456_3_)
    {
        boolean flag = p_187456_3_ != EnumHandSide.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(p_187456_2_);
        float f2 = -0.3F * MathHelper.sin(f1 * (float)Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float)Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(p_187456_2_ * (float)Math.PI);
        GlStateManager.func_179109_b(f * (f2 + 0.64000005F), f3 + -0.6F + p_187456_1_ * -0.6F, f4 + -0.71999997F);
        GlStateManager.func_179114_b(f * 45.0F, 0.0F, 1.0F, 0.0F);
        float f5 = MathHelper.sin(p_187456_2_ * p_187456_2_ * (float)Math.PI);
        float f6 = MathHelper.sin(f1 * (float)Math.PI);
        GlStateManager.func_179114_b(f * f6 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.func_179114_b(f * f5 * -20.0F, 0.0F, 0.0F, 1.0F);
        AbstractClientPlayer abstractclientplayer = this.mc.player;
        this.mc.getTextureManager().bindTexture(abstractclientplayer.getLocationSkin());
        GlStateManager.func_179109_b(f * -1.0F, 3.6F, 3.5F);
        GlStateManager.func_179114_b(f * 120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.func_179114_b(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.func_179114_b(f * -135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.func_179109_b(f * 5.6F, 0.0F, 0.0F);
        RenderPlayer renderplayer = (RenderPlayer)this.renderManager.<AbstractClientPlayer>getRenderer(abstractclientplayer);
        GlStateManager.func_179129_p();

        if (flag)
        {
            renderplayer.func_177138_b(abstractclientplayer);
        }
        else
        {
            renderplayer.func_177139_c(abstractclientplayer);
        }

        GlStateManager.func_179089_o();
    }

    private void func_187454_a(float p_187454_1_, EnumHandSide p_187454_2_, ItemStack p_187454_3_)
    {
        float f = (float)this.mc.player.getItemInUseCount() - p_187454_1_ + 1.0F;
        float f1 = f / (float)p_187454_3_.getUseDuration();

        if (f1 < 0.8F)
        {
            float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * (float)Math.PI) * 0.1F);
            GlStateManager.func_179109_b(0.0F, f2, 0.0F);
        }

        float f3 = 1.0F - (float)Math.pow((double)f1, 27.0D);
        int i = p_187454_2_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179109_b(f3 * 0.6F * (float)i, f3 * -0.5F, f3 * 0.0F);
        GlStateManager.func_179114_b((float)i * f3 * 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.func_179114_b(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.func_179114_b((float)i * f3 * 30.0F, 0.0F, 0.0F, 1.0F);
    }

    private void func_187453_a(EnumHandSide p_187453_1_, float p_187453_2_)
    {
        int i = p_187453_1_ == EnumHandSide.RIGHT ? 1 : -1;
        float f = MathHelper.sin(p_187453_2_ * p_187453_2_ * (float)Math.PI);
        GlStateManager.func_179114_b((float)i * (45.0F + f * -20.0F), 0.0F, 1.0F, 0.0F);
        float f1 = MathHelper.sin(MathHelper.sqrt(p_187453_2_) * (float)Math.PI);
        GlStateManager.func_179114_b((float)i * f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.func_179114_b(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.func_179114_b((float)i * -45.0F, 0.0F, 1.0F, 0.0F);
    }

    private void func_187459_b(EnumHandSide p_187459_1_, float p_187459_2_)
    {
        int i = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179109_b((float)i * 0.56F, -0.52F + p_187459_2_ * -0.6F, -0.72F);
    }

    public void func_78440_a(float p_78440_1_)
    {
        AbstractClientPlayer abstractclientplayer = this.mc.player;
        float f = abstractclientplayer.getSwingProgress(p_78440_1_);
        EnumHand enumhand = (EnumHand)MoreObjects.firstNonNull(abstractclientplayer.swingingHand, EnumHand.MAIN_HAND);
        float f1 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * p_78440_1_;
        float f2 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * p_78440_1_;
        boolean flag = true;
        boolean flag1 = true;

        if (abstractclientplayer.isHandActive())
        {
            ItemStack itemstack = abstractclientplayer.getActiveItemStack();

            if (itemstack.getItem() == Items.BOW)
            {
                EnumHand enumhand1 = abstractclientplayer.getActiveHand();
                flag = enumhand1 == EnumHand.MAIN_HAND;
                flag1 = !flag;
            }
        }

        this.func_178101_a(f1, f2);
        this.func_187464_b();
        this.func_187458_c(p_78440_1_);
        GlStateManager.func_179091_B();

        if (flag)
        {
            float f3 = enumhand == EnumHand.MAIN_HAND ? f : 0.0F;
            float f5 = 1.0F - (this.prevEquippedProgressMainHand + (this.equippedProgressMainHand - this.prevEquippedProgressMainHand) * p_78440_1_);
            this.func_187457_a(abstractclientplayer, p_78440_1_, f1, EnumHand.MAIN_HAND, f3, this.itemStackMainHand, f5);
        }

        if (flag1)
        {
            float f4 = enumhand == EnumHand.OFF_HAND ? f : 0.0F;
            float f6 = 1.0F - (this.prevEquippedProgressOffHand + (this.equippedProgressOffHand - this.prevEquippedProgressOffHand) * p_78440_1_);
            this.func_187457_a(abstractclientplayer, p_78440_1_, f1, EnumHand.OFF_HAND, f4, this.itemStackOffHand, f6);
        }

        GlStateManager.func_179101_C();
        RenderHelper.disableStandardItemLighting();
    }

    public void func_187457_a(AbstractClientPlayer p_187457_1_, float p_187457_2_, float p_187457_3_, EnumHand p_187457_4_, float p_187457_5_, ItemStack p_187457_6_, float p_187457_7_)
    {
        boolean flag = p_187457_4_ == EnumHand.MAIN_HAND;
        EnumHandSide enumhandside = flag ? p_187457_1_.getPrimaryHand() : p_187457_1_.getPrimaryHand().opposite();
        GlStateManager.func_179094_E();

        if (p_187457_6_.isEmpty())
        {
            if (flag && !p_187457_1_.isInvisible())
            {
                this.func_187456_a(p_187457_7_, p_187457_5_, enumhandside);
            }
        }
        else if (p_187457_6_.getItem() == Items.FILLED_MAP)
        {
            if (flag && this.itemStackOffHand.isEmpty())
            {
                this.func_187463_a(p_187457_3_, p_187457_7_, p_187457_5_);
            }
            else
            {
                this.func_187465_a(p_187457_7_, enumhandside, p_187457_5_, p_187457_6_);
            }
        }
        else
        {
            boolean flag1 = enumhandside == EnumHandSide.RIGHT;

            if (p_187457_1_.isHandActive() && p_187457_1_.getItemInUseCount() > 0 && p_187457_1_.getActiveHand() == p_187457_4_)
            {
                int j = flag1 ? 1 : -1;

                switch (p_187457_6_.getUseAction())
                {
                    case NONE:
                        this.func_187459_b(enumhandside, p_187457_7_);
                        break;

                    case EAT:
                    case DRINK:
                        this.func_187454_a(p_187457_2_, enumhandside, p_187457_6_);
                        this.func_187459_b(enumhandside, p_187457_7_);
                        break;

                    case BLOCK:
                        this.func_187459_b(enumhandside, p_187457_7_);
                        break;

                    case BOW:
                        this.func_187459_b(enumhandside, p_187457_7_);
                        GlStateManager.func_179109_b((float)j * -0.2785682F, 0.18344387F, 0.15731531F);
                        GlStateManager.func_179114_b(-13.935F, 1.0F, 0.0F, 0.0F);
                        GlStateManager.func_179114_b((float)j * 35.3F, 0.0F, 1.0F, 0.0F);
                        GlStateManager.func_179114_b((float)j * -9.785F, 0.0F, 0.0F, 1.0F);
                        float f5 = (float)p_187457_6_.getUseDuration() - ((float)this.mc.player.getItemInUseCount() - p_187457_2_ + 1.0F);
                        float f6 = f5 / 20.0F;
                        f6 = (f6 * f6 + f6 * 2.0F) / 3.0F;

                        if (f6 > 1.0F)
                        {
                            f6 = 1.0F;
                        }

                        if (f6 > 0.1F)
                        {
                            float f7 = MathHelper.sin((f5 - 0.1F) * 1.3F);
                            float f3 = f6 - 0.1F;
                            float f4 = f7 * f3;
                            GlStateManager.func_179109_b(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
                        }

                        GlStateManager.func_179109_b(f6 * 0.0F, f6 * 0.0F, f6 * 0.04F);
                        GlStateManager.func_179152_a(1.0F, 1.0F, 1.0F + f6 * 0.2F);
                        GlStateManager.func_179114_b((float)j * 45.0F, 0.0F, -1.0F, 0.0F);
                }
            }
            else
            {
                float f = -0.4F * MathHelper.sin(MathHelper.sqrt(p_187457_5_) * (float)Math.PI);
                float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt(p_187457_5_) * ((float)Math.PI * 2F));
                float f2 = -0.2F * MathHelper.sin(p_187457_5_ * (float)Math.PI);
                int i = flag1 ? 1 : -1;
                GlStateManager.func_179109_b((float)i * f, f1, f2);
                this.func_187459_b(enumhandside, p_187457_7_);
                this.func_187453_a(enumhandside, p_187457_5_);
            }

            this.func_187462_a(p_187457_1_, p_187457_6_, flag1 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag1);
        }

        GlStateManager.func_179121_F();
    }

    public void func_78447_b(float p_78447_1_)
    {
        GlStateManager.func_179118_c();

        if (this.mc.player.isEntityInsideOpaqueBlock())
        {
            IBlockState iblockstate = this.mc.world.getBlockState(new BlockPos(this.mc.player));
            EntityPlayer entityplayer = this.mc.player;

            for (int i = 0; i < 8; ++i)
            {
                double d0 = entityplayer.posX + (double)(((float)((i >> 0) % 2) - 0.5F) * entityplayer.field_70130_N * 0.8F);
                double d1 = entityplayer.posY + (double)(((float)((i >> 1) % 2) - 0.5F) * 0.1F);
                double d2 = entityplayer.posZ + (double)(((float)((i >> 2) % 2) - 0.5F) * entityplayer.field_70130_N * 0.8F);
                BlockPos blockpos = new BlockPos(d0, d1 + (double)entityplayer.getEyeHeight(), d2);
                IBlockState iblockstate1 = this.mc.world.getBlockState(blockpos);

                if (iblockstate1.func_191058_s())
                {
                    iblockstate = iblockstate1;
                }
            }

            if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE)
            {
                this.func_178108_a(this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iblockstate));
            }
        }

        if (!this.mc.player.isSpectator())
        {
            if (this.mc.player.func_70055_a(Material.WATER))
            {
                this.func_78448_c(p_78447_1_);
            }

            if (this.mc.player.isBurning())
            {
                this.func_78442_d();
            }
        }

        GlStateManager.func_179141_d();
    }

    private void func_178108_a(TextureAtlasSprite p_178108_1_)
    {
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f = 0.1F;
        GlStateManager.func_179131_c(0.1F, 0.1F, 0.1F, 0.5F);
        GlStateManager.func_179094_E();
        float f1 = -1.0F;
        float f2 = 1.0F;
        float f3 = -1.0F;
        float f4 = 1.0F;
        float f5 = -0.5F;
        float f6 = p_178108_1_.getMinU();
        float f7 = p_178108_1_.getMaxU();
        float f8 = p_178108_1_.getMinV();
        float f9 = p_178108_1_.getMaxV();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.func_181662_b(-1.0D, -1.0D, -0.5D).func_187315_a((double)f7, (double)f9).endVertex();
        bufferbuilder.func_181662_b(1.0D, -1.0D, -0.5D).func_187315_a((double)f6, (double)f9).endVertex();
        bufferbuilder.func_181662_b(1.0D, 1.0D, -0.5D).func_187315_a((double)f6, (double)f8).endVertex();
        bufferbuilder.func_181662_b(-1.0D, 1.0D, -0.5D).func_187315_a((double)f7, (double)f8).endVertex();
        tessellator.draw();
        GlStateManager.func_179121_F();
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void func_78448_c(float p_78448_1_)
    {
        this.mc.getTextureManager().bindTexture(field_110929_d);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f = this.mc.player.getBrightness();
        GlStateManager.func_179131_c(f, f, f, 0.5F);
        GlStateManager.func_179147_l();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179094_E();
        float f1 = 4.0F;
        float f2 = -1.0F;
        float f3 = 1.0F;
        float f4 = -1.0F;
        float f5 = 1.0F;
        float f6 = -0.5F;
        float f7 = -this.mc.player.rotationYaw / 64.0F;
        float f8 = this.mc.player.rotationPitch / 64.0F;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.func_181662_b(-1.0D, -1.0D, -0.5D).func_187315_a((double)(4.0F + f7), (double)(4.0F + f8)).endVertex();
        bufferbuilder.func_181662_b(1.0D, -1.0D, -0.5D).func_187315_a((double)(0.0F + f7), (double)(4.0F + f8)).endVertex();
        bufferbuilder.func_181662_b(1.0D, 1.0D, -0.5D).func_187315_a((double)(0.0F + f7), (double)(0.0F + f8)).endVertex();
        bufferbuilder.func_181662_b(-1.0D, 1.0D, -0.5D).func_187315_a((double)(4.0F + f7), (double)(0.0F + f8)).endVertex();
        tessellator.draw();
        GlStateManager.func_179121_F();
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179084_k();
    }

    private void func_78442_d()
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.9F);
        GlStateManager.func_179143_c(519);
        GlStateManager.func_179132_a(false);
        GlStateManager.func_179147_l();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        float f = 1.0F;

        for (int i = 0; i < 2; ++i)
        {
            GlStateManager.func_179094_E();
            TextureAtlasSprite textureatlassprite = this.mc.func_147117_R().func_110572_b("minecraft:blocks/fire_layer_1");
            this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            float f1 = textureatlassprite.getMinU();
            float f2 = textureatlassprite.getMaxU();
            float f3 = textureatlassprite.getMinV();
            float f4 = textureatlassprite.getMaxV();
            float f5 = -0.5F;
            float f6 = 0.5F;
            float f7 = -0.5F;
            float f8 = 0.5F;
            float f9 = -0.5F;
            GlStateManager.func_179109_b((float)(-(i * 2 - 1)) * 0.24F, -0.3F, 0.0F);
            GlStateManager.func_179114_b((float)(i * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.func_181662_b(-0.5D, -0.5D, -0.5D).func_187315_a((double)f2, (double)f4).endVertex();
            bufferbuilder.func_181662_b(0.5D, -0.5D, -0.5D).func_187315_a((double)f1, (double)f4).endVertex();
            bufferbuilder.func_181662_b(0.5D, 0.5D, -0.5D).func_187315_a((double)f1, (double)f3).endVertex();
            bufferbuilder.func_181662_b(-0.5D, 0.5D, -0.5D).func_187315_a((double)f2, (double)f3).endVertex();
            tessellator.draw();
            GlStateManager.func_179121_F();
        }

        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179084_k();
        GlStateManager.func_179132_a(true);
        GlStateManager.func_179143_c(515);
    }

    public void tick()
    {
        this.prevEquippedProgressMainHand = this.equippedProgressMainHand;
        this.prevEquippedProgressOffHand = this.equippedProgressOffHand;
        EntityPlayerSP entityplayersp = this.mc.player;
        ItemStack itemstack = entityplayersp.getHeldItemMainhand();
        ItemStack itemstack1 = entityplayersp.getHeldItemOffhand();

        if (entityplayersp.isRowingBoat())
        {
            this.equippedProgressMainHand = MathHelper.clamp(this.equippedProgressMainHand - 0.4F, 0.0F, 1.0F);
            this.equippedProgressOffHand = MathHelper.clamp(this.equippedProgressOffHand - 0.4F, 0.0F, 1.0F);
        }
        else
        {
            float f = entityplayersp.getCooledAttackStrength(1.0F);
            this.equippedProgressMainHand += MathHelper.clamp((Objects.equals(this.itemStackMainHand, itemstack) ? f * f * f : 0.0F) - this.equippedProgressMainHand, -0.4F, 0.4F);
            this.equippedProgressOffHand += MathHelper.clamp((float)(Objects.equals(this.itemStackOffHand, itemstack1) ? 1 : 0) - this.equippedProgressOffHand, -0.4F, 0.4F);
        }

        if (this.equippedProgressMainHand < 0.1F)
        {
            this.itemStackMainHand = itemstack;
        }

        if (this.equippedProgressOffHand < 0.1F)
        {
            this.itemStackOffHand = itemstack1;
        }
    }

    public void resetEquippedProgress(EnumHand hand)
    {
        if (hand == EnumHand.MAIN_HAND)
        {
            this.equippedProgressMainHand = 0.0F;
        }
        else
        {
            this.equippedProgressOffHand = 0.0F;
        }
    }
}

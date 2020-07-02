package net.minecraft.client.renderer.entity;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;

public class RenderItemFrame extends Render<EntityItemFrame>
{
    private static final ResourceLocation field_110789_a = new ResourceLocation("textures/map/map_background.png");
    private final Minecraft mc = Minecraft.getInstance();
    private final ModelResourceLocation field_177072_f = new ModelResourceLocation("item_frame", "normal");
    private final ModelResourceLocation field_177073_g = new ModelResourceLocation("item_frame", "map");
    private final RenderItem itemRenderer;

    public RenderItemFrame(RenderManager renderManagerIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn);
        this.itemRenderer = itemRendererIn;
    }

    public void func_76986_a(EntityItemFrame p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        GlStateManager.func_179094_E();
        BlockPos blockpos = p_76986_1_.getHangingPosition();
        double d0 = (double)blockpos.getX() - p_76986_1_.posX + p_76986_2_;
        double d1 = (double)blockpos.getY() - p_76986_1_.posY + p_76986_4_;
        double d2 = (double)blockpos.getZ() - p_76986_1_.posZ + p_76986_6_;
        GlStateManager.func_179137_b(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
        GlStateManager.func_179114_b(180.0F - p_76986_1_.rotationYaw, 0.0F, 1.0F, 0.0F);
        this.renderManager.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
        ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
        IBakedModel ibakedmodel;

        if (p_76986_1_.getDisplayedItem().getItem() == Items.FILLED_MAP)
        {
            ibakedmodel = modelmanager.getModel(this.field_177073_g);
        }
        else
        {
            ibakedmodel = modelmanager.getModel(this.field_177072_f);
        }

        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b(-0.5F, -0.5F, -0.5F);

        if (this.field_188301_f)
        {
            GlStateManager.func_179142_g();
            GlStateManager.func_187431_e(this.func_188298_c(p_76986_1_));
        }

        blockrendererdispatcher.getBlockModelRenderer().func_178262_a(ibakedmodel, 1.0F, 1.0F, 1.0F, 1.0F);

        if (this.field_188301_f)
        {
            GlStateManager.func_187417_n();
            GlStateManager.func_179119_h();
        }

        GlStateManager.func_179121_F();
        GlStateManager.func_179109_b(0.0F, 0.0F, 0.4375F);
        this.func_82402_b(p_76986_1_);
        GlStateManager.func_179121_F();
        this.func_177067_a(p_76986_1_, p_76986_2_ + (double)((float)p_76986_1_.facingDirection.getXOffset() * 0.3F), p_76986_4_ - 0.25D, p_76986_6_ + (double)((float)p_76986_1_.facingDirection.getZOffset() * 0.3F));
    }

    @Nullable

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityItemFrame entity)
    {
        return null;
    }

    private void func_82402_b(EntityItemFrame p_82402_1_)
    {
        ItemStack itemstack = p_82402_1_.getDisplayedItem();

        if (!itemstack.isEmpty())
        {
            GlStateManager.func_179094_E();
            GlStateManager.func_179140_f();
            boolean flag = itemstack.getItem() == Items.FILLED_MAP;
            int i = flag ? p_82402_1_.getRotation() % 4 * 2 : p_82402_1_.getRotation();
            GlStateManager.func_179114_b((float)i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);

            if (flag)
            {
                this.renderManager.textureManager.bindTexture(field_110789_a);
                GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
                float f = 0.0078125F;
                GlStateManager.func_179152_a(0.0078125F, 0.0078125F, 0.0078125F);
                GlStateManager.func_179109_b(-64.0F, -64.0F, 0.0F);
                MapData mapdata = Items.FILLED_MAP.func_77873_a(itemstack, p_82402_1_.world);
                GlStateManager.func_179109_b(0.0F, 0.0F, -1.0F);

                if (mapdata != null)
                {
                    this.mc.gameRenderer.getMapItemRenderer().func_148250_a(mapdata, true);
                }
            }
            else
            {
                GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
                GlStateManager.func_179123_a();
                RenderHelper.func_74519_b();
                this.itemRenderer.func_181564_a(itemstack, ItemCameraTransforms.TransformType.FIXED);
                RenderHelper.disableStandardItemLighting();
                GlStateManager.func_179099_b();
            }

            GlStateManager.func_179145_e();
            GlStateManager.func_179121_F();
        }
    }

    protected void func_177067_a(EntityItemFrame p_177067_1_, double p_177067_2_, double p_177067_4_, double p_177067_6_)
    {
        if (Minecraft.isGuiEnabled() && !p_177067_1_.getDisplayedItem().isEmpty() && p_177067_1_.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == p_177067_1_)
        {
            double d0 = p_177067_1_.getDistanceSq(this.renderManager.field_78734_h);
            float f = p_177067_1_.func_70093_af() ? 32.0F : 64.0F;

            if (d0 < (double)(f * f))
            {
                String s = p_177067_1_.getDisplayedItem().func_82833_r();
                this.func_147906_a(p_177067_1_, s, p_177067_2_, p_177067_4_, p_177067_6_, 64);
            }
        }
    }
}

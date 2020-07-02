package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderEntityItem extends Render<EntityItem>
{
    private final RenderItem itemRenderer;
    private final Random random = new Random();

    public RenderEntityItem(RenderManager renderManagerIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn);
        this.itemRenderer = itemRendererIn;
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    private int func_177077_a(EntityItem p_177077_1_, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_)
    {
        ItemStack itemstack = p_177077_1_.getItem();
        Item item = itemstack.getItem();

        if (item == null)
        {
            return 0;
        }
        else
        {
            boolean flag = p_177077_9_.isGui3d();
            int i = this.getModelCount(itemstack);
            float f = 0.25F;
            float f1 = MathHelper.sin(((float)p_177077_1_.getAge() + p_177077_8_) / 10.0F + p_177077_1_.hoverStart) * 0.1F + 0.1F;
            float f2 = p_177077_9_.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
            GlStateManager.func_179109_b((float)p_177077_2_, (float)p_177077_4_ + f1 + 0.25F * f2, (float)p_177077_6_);

            if (flag || this.renderManager.options != null)
            {
                float f3 = (((float)p_177077_1_.getAge() + p_177077_8_) / 20.0F + p_177077_1_.hoverStart) * (180F / (float)Math.PI);
                GlStateManager.func_179114_b(f3, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            return i;
        }
    }

    private int getModelCount(ItemStack stack)
    {
        int i = 1;

        if (stack.getCount() > 48)
        {
            i = 5;
        }
        else if (stack.getCount() > 32)
        {
            i = 4;
        }
        else if (stack.getCount() > 16)
        {
            i = 3;
        }
        else if (stack.getCount() > 1)
        {
            i = 2;
        }

        return i;
    }

    public void func_76986_a(EntityItem p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        ItemStack itemstack = p_76986_1_.getItem();
        int i = itemstack.isEmpty() ? 187 : Item.getIdFromItem(itemstack.getItem()) + itemstack.func_77960_j();
        this.random.setSeed((long)i);
        boolean flag = false;

        if (this.func_180548_c(p_76986_1_))
        {
            this.renderManager.textureManager.func_110581_b(this.getEntityTexture(p_76986_1_)).func_174936_b(false, false);
            flag = true;
        }

        GlStateManager.func_179091_B();
        GlStateManager.func_179092_a(516, 0.1F);
        GlStateManager.func_179147_l();
        RenderHelper.func_74519_b();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179094_E();
        IBakedModel ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemstack, p_76986_1_.world, (EntityLivingBase)null);
        int j = this.func_177077_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_9_, ibakedmodel);
        float f = ibakedmodel.getItemCameraTransforms().ground.scale.x;
        float f1 = ibakedmodel.getItemCameraTransforms().ground.scale.y;
        float f2 = ibakedmodel.getItemCameraTransforms().ground.scale.z;
        boolean flag1 = ibakedmodel.isGui3d();

        if (!flag1)
        {
            float f3 = -0.0F * (float)(j - 1) * 0.5F * f;
            float f4 = -0.0F * (float)(j - 1) * 0.5F * f1;
            float f5 = -0.09375F * (float)(j - 1) * 0.5F * f2;
            GlStateManager.func_179109_b(f3, f4, f5);
        }

        if (this.field_188301_f)
        {
            GlStateManager.func_179142_g();
            GlStateManager.func_187431_e(this.func_188298_c(p_76986_1_));
        }

        for (int k = 0; k < j; ++k)
        {
            if (flag1)
            {
                GlStateManager.func_179094_E();

                if (k > 0)
                {
                    float f7 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f9 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    GlStateManager.func_179109_b(f7, f9, f6);
                }

                ibakedmodel.getItemCameraTransforms().func_181689_a(ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.func_180454_a(itemstack, ibakedmodel);
                GlStateManager.func_179121_F();
            }
            else
            {
                GlStateManager.func_179094_E();

                if (k > 0)
                {
                    float f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    GlStateManager.func_179109_b(f8, f10, 0.0F);
                }

                ibakedmodel.getItemCameraTransforms().func_181689_a(ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.func_180454_a(itemstack, ibakedmodel);
                GlStateManager.func_179121_F();
                GlStateManager.func_179109_b(0.0F * f, 0.0F * f1, 0.09375F * f2);
            }
        }

        if (this.field_188301_f)
        {
            GlStateManager.func_187417_n();
            GlStateManager.func_179119_h();
        }

        GlStateManager.func_179121_F();
        GlStateManager.func_179101_C();
        GlStateManager.func_179084_k();
        this.func_180548_c(p_76986_1_);

        if (flag)
        {
            this.renderManager.textureManager.func_110581_b(this.getEntityTexture(p_76986_1_)).func_174935_a();
        }

        super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityItem entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}

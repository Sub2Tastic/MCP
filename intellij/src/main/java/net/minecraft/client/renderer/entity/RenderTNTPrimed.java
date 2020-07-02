package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderTNTPrimed extends Render<EntityTNTPrimed>
{
    public RenderTNTPrimed(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    public void func_76986_a(EntityTNTPrimed p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)p_76986_2_, (float)p_76986_4_ + 0.5F, (float)p_76986_6_);

        if ((float)p_76986_1_.getFuse() - p_76986_9_ + 1.0F < 10.0F)
        {
            float f = 1.0F - ((float)p_76986_1_.getFuse() - p_76986_9_ + 1.0F) / 10.0F;
            f = MathHelper.clamp(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f1 = 1.0F + f * 0.3F;
            GlStateManager.func_179152_a(f1, f1, f1);
        }

        float f2 = (1.0F - ((float)p_76986_1_.getFuse() - p_76986_9_ + 1.0F) / 100.0F) * 0.8F;
        this.func_180548_c(p_76986_1_);
        GlStateManager.func_179114_b(-90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.func_179109_b(-0.5F, -0.5F, 0.5F);
        blockrendererdispatcher.func_175016_a(Blocks.TNT.getDefaultState(), p_76986_1_.getBrightness());
        GlStateManager.func_179109_b(0.0F, 0.0F, 1.0F);

        if (this.field_188301_f)
        {
            GlStateManager.func_179142_g();
            GlStateManager.func_187431_e(this.func_188298_c(p_76986_1_));
            blockrendererdispatcher.func_175016_a(Blocks.TNT.getDefaultState(), 1.0F);
            GlStateManager.func_187417_n();
            GlStateManager.func_179119_h();
        }
        else if (p_76986_1_.getFuse() / 5 % 2 == 0)
        {
            GlStateManager.func_179090_x();
            GlStateManager.func_179140_f();
            GlStateManager.func_179147_l();
            GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.DST_ALPHA);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, f2);
            GlStateManager.func_179136_a(-3.0F, -3.0F);
            GlStateManager.func_179088_q();
            blockrendererdispatcher.func_175016_a(Blocks.TNT.getDefaultState(), 1.0F);
            GlStateManager.func_179136_a(0.0F, 0.0F);
            GlStateManager.func_179113_r();
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.func_179084_k();
            GlStateManager.func_179145_e();
            GlStateManager.func_179098_w();
        }

        GlStateManager.func_179121_F();
        super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityTNTPrimed entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}

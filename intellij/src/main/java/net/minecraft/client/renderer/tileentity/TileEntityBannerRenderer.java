package net.minecraft.client.renderer.tileentity;

import javax.annotation.Nullable;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TileEntityBannerRenderer extends TileEntitySpecialRenderer<TileEntityBanner>
{
    private final ModelBanner field_178465_e = new ModelBanner();

    public void func_192841_a(TileEntityBanner p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_)
    {
        boolean flag = p_192841_1_.getWorld() != null;
        boolean flag1 = !flag || p_192841_1_.func_145838_q() == Blocks.field_180393_cK;
        int i = flag ? p_192841_1_.func_145832_p() : 0;
        long j = flag ? p_192841_1_.getWorld().getGameTime() : 0L;
        GlStateManager.func_179094_E();
        float f = 0.6666667F;

        if (flag1)
        {
            GlStateManager.func_179109_b((float)p_192841_2_ + 0.5F, (float)p_192841_4_ + 0.5F, (float)p_192841_6_ + 0.5F);
            float f1 = (float)(i * 360) / 16.0F;
            GlStateManager.func_179114_b(-f1, 0.0F, 1.0F, 0.0F);
            this.field_178465_e.field_178688_b.showModel = true;
        }
        else
        {
            float f2 = 0.0F;

            if (i == 2)
            {
                f2 = 180.0F;
            }

            if (i == 4)
            {
                f2 = 90.0F;
            }

            if (i == 5)
            {
                f2 = -90.0F;
            }

            GlStateManager.func_179109_b((float)p_192841_2_ + 0.5F, (float)p_192841_4_ - 0.16666667F, (float)p_192841_6_ + 0.5F);
            GlStateManager.func_179114_b(-f2, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179109_b(0.0F, -0.3125F, -0.4375F);
            this.field_178465_e.field_178688_b.showModel = false;
        }

        BlockPos blockpos = p_192841_1_.getPos();
        float f3 = (float)(blockpos.getX() * 7 + blockpos.getY() * 9 + blockpos.getZ() * 13) + (float)j + p_192841_8_;
        this.field_178465_e.field_178690_a.rotateAngleX = (-0.0125F + 0.01F * MathHelper.cos(f3 * (float)Math.PI * 0.02F)) * (float)Math.PI;
        GlStateManager.func_179091_B();
        ResourceLocation resourcelocation = this.func_178463_a(p_192841_1_);

        if (resourcelocation != null)
        {
            this.func_147499_a(resourcelocation);
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a(0.6666667F, -0.6666667F, -0.6666667F);
            this.field_178465_e.func_178687_a();
            GlStateManager.func_179121_F();
        }

        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, p_192841_10_);
        GlStateManager.func_179121_F();
    }

    @Nullable
    private ResourceLocation func_178463_a(TileEntityBanner p_178463_1_)
    {
        return BannerTextures.field_178466_c.func_187478_a(p_178463_1_.func_175116_e(), p_178463_1_.getPatternList(), p_178463_1_.getColorList());
    }
}

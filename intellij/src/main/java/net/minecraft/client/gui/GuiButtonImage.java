package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonImage extends GuiButton
{
    private final ResourceLocation resourceLocation;
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffText;

    public GuiButtonImage(int p_i47392_1_, int p_i47392_2_, int p_i47392_3_, int p_i47392_4_, int p_i47392_5_, int p_i47392_6_, int p_i47392_7_, int p_i47392_8_, ResourceLocation p_i47392_9_)
    {
        super(p_i47392_1_, p_i47392_2_, p_i47392_3_, p_i47392_4_, p_i47392_5_, "");
        this.xTexStart = p_i47392_6_;
        this.yTexStart = p_i47392_7_;
        this.yDiffText = p_i47392_8_;
        this.resourceLocation = p_i47392_9_;
    }

    public void setPosition(int xIn, int yIn)
    {
        this.field_146128_h = xIn;
        this.field_146129_i = yIn;
    }

    public void func_191745_a(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_)
    {
        if (this.field_146125_m)
        {
            this.field_146123_n = p_191745_2_ >= this.field_146128_h && p_191745_3_ >= this.field_146129_i && p_191745_2_ < this.field_146128_h + this.field_146120_f && p_191745_3_ < this.field_146129_i + this.field_146121_g;
            p_191745_1_.getTextureManager().bindTexture(this.resourceLocation);
            GlStateManager.func_179097_i();
            int i = this.xTexStart;
            int j = this.yTexStart;

            if (this.field_146123_n)
            {
                j += this.yDiffText;
            }

            this.func_73729_b(this.field_146128_h, this.field_146129_i, i, j, this.field_146120_f, this.field_146121_g);
            GlStateManager.func_179126_j();
        }
    }
}

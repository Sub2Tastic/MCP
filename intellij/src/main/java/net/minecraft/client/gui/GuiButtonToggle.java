package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonToggle extends GuiButton
{
    protected ResourceLocation resourceLocation;
    protected boolean stateTriggered;
    protected int xTexStart;
    protected int yTexStart;
    protected int xDiffTex;
    protected int yDiffTex;

    public GuiButtonToggle(int p_i47389_1_, int p_i47389_2_, int p_i47389_3_, int p_i47389_4_, int p_i47389_5_, boolean p_i47389_6_)
    {
        super(p_i47389_1_, p_i47389_2_, p_i47389_3_, p_i47389_4_, p_i47389_5_, "");
        this.stateTriggered = p_i47389_6_;
    }

    public void initTextureValues(int xTexStartIn, int yTexStartIn, int xDiffTexIn, int yDiffTexIn, ResourceLocation resourceLocationIn)
    {
        this.xTexStart = xTexStartIn;
        this.yTexStart = yTexStartIn;
        this.xDiffTex = xDiffTexIn;
        this.yDiffTex = yDiffTexIn;
        this.resourceLocation = resourceLocationIn;
    }

    public void setStateTriggered(boolean triggered)
    {
        this.stateTriggered = triggered;
    }

    public boolean isStateTriggered()
    {
        return this.stateTriggered;
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

            if (this.stateTriggered)
            {
                i += this.xDiffTex;
            }

            if (this.field_146123_n)
            {
                j += this.yDiffTex;
            }

            this.func_73729_b(this.field_146128_h, this.field_146129_i, i, j, this.field_146120_f, this.field_146121_g);
            GlStateManager.func_179126_j();
        }
    }
}

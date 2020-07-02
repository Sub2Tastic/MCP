package net.minecraft.client.gui;

import java.io.IOException;
import java.net.URI;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiScreenDemo extends GuiScreen
{
    private static final Logger field_146349_a = LogManager.getLogger();
    private static final ResourceLocation DEMO_BACKGROUND_LOCATION = new ResourceLocation("textures/gui/demo_background.png");

    public void func_73866_w_()
    {
        this.field_146292_n.clear();
        int i = -16;
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 116, this.field_146295_m / 2 + 62 + -16, 114, 20, I18n.format("demo.help.buy")));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 + 2, this.field_146295_m / 2 + 62 + -16, 114, 20, I18n.format("demo.help.later")));
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        switch (p_146284_1_.field_146127_k)
        {
            case 1:
                p_146284_1_.field_146124_l = false;

                try
                {
                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop").invoke((Object)null);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI("http://www.minecraft.net/store?source=demo"));
                }
                catch (Throwable throwable)
                {
                    field_146349_a.error("Couldn't open link", throwable);
                }

                break;

            case 2:
                this.field_146297_k.displayGuiScreen((GuiScreen)null);
                this.field_146297_k.func_71381_h();
        }
    }

    public void func_146276_q_()
    {
        super.func_146276_q_();
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_146297_k.getTextureManager().bindTexture(DEMO_BACKGROUND_LOCATION);
        int i = (this.field_146294_l - 248) / 2;
        int j = (this.field_146295_m - 166) / 2;
        this.func_73729_b(i, j, 0, 0, 248, 166);
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        int i = (this.field_146294_l - 248) / 2 + 10;
        int j = (this.field_146295_m - 166) / 2 + 8;
        this.field_146289_q.func_78276_b(I18n.format("demo.help.title"), i, j, 2039583);
        j = j + 12;
        GameSettings gamesettings = this.field_146297_k.gameSettings;
        this.field_146289_q.func_78276_b(I18n.format("demo.help.movementShort", GameSettings.func_74298_c(gamesettings.keyBindForward.func_151463_i()), GameSettings.func_74298_c(gamesettings.keyBindLeft.func_151463_i()), GameSettings.func_74298_c(gamesettings.keyBindBack.func_151463_i()), GameSettings.func_74298_c(gamesettings.keyBindRight.func_151463_i())), i, j, 5197647);
        this.field_146289_q.func_78276_b(I18n.format("demo.help.movementMouse"), i, j + 12, 5197647);
        this.field_146289_q.func_78276_b(I18n.format("demo.help.jump", GameSettings.func_74298_c(gamesettings.keyBindJump.func_151463_i())), i, j + 24, 5197647);
        this.field_146289_q.func_78276_b(I18n.format("demo.help.inventory", GameSettings.func_74298_c(gamesettings.keyBindInventory.func_151463_i())), i, j + 36, 5197647);
        this.field_146289_q.drawSplitString(I18n.format("demo.help.fullWrapped"), i, j + 68, 218, 2039583);
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}

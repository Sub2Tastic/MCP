package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuRecipient;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiSpectator extends Gui implements ISpectatorMenuRecipient
{
    private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");
    public static final ResourceLocation SPECTATOR_WIDGETS = new ResourceLocation("textures/gui/spectator_widgets.png");
    private final Minecraft mc;
    private long lastSelectionTime;
    private SpectatorMenu menu;

    public GuiSpectator(Minecraft mcIn)
    {
        this.mc = mcIn;
    }

    public void onHotbarSelected(int p_175260_1_)
    {
        this.lastSelectionTime = Minecraft.func_71386_F();

        if (this.menu != null)
        {
            this.menu.selectSlot(p_175260_1_);
        }
        else
        {
            this.menu = new SpectatorMenu(this);
        }
    }

    private float getHotbarAlpha()
    {
        long i = this.lastSelectionTime - Minecraft.func_71386_F() + 5000L;
        return MathHelper.clamp((float)i / 2000.0F, 0.0F, 1.0F);
    }

    public void func_175264_a(ScaledResolution p_175264_1_, float p_175264_2_)
    {
        if (this.menu != null)
        {
            float f = this.getHotbarAlpha();

            if (f <= 0.0F)
            {
                this.menu.exit();
            }
            else
            {
                int i = p_175264_1_.func_78326_a() / 2;
                float f1 = this.field_73735_i;
                this.field_73735_i = -90.0F;
                float f2 = (float)p_175264_1_.func_78328_b() - 22.0F * f;
                SpectatorDetails spectatordetails = this.menu.getCurrentPage();
                this.func_175258_a(p_175264_1_, f, i, f2, spectatordetails);
                this.field_73735_i = f1;
            }
        }
    }

    protected void func_175258_a(ScaledResolution p_175258_1_, float p_175258_2_, int p_175258_3_, float p_175258_4_, SpectatorDetails p_175258_5_)
    {
        GlStateManager.func_179091_B();
        GlStateManager.func_179147_l();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, p_175258_2_);
        this.mc.getTextureManager().bindTexture(WIDGETS);
        this.func_175174_a((float)(p_175258_3_ - 91), p_175258_4_, 0, 0, 182, 22);

        if (p_175258_5_.getSelectedSlot() >= 0)
        {
            this.func_175174_a((float)(p_175258_3_ - 91 - 1 + p_175258_5_.getSelectedSlot() * 20), p_175258_4_ - 1.0F, 0, 22, 24, 22);
        }

        RenderHelper.func_74520_c();

        for (int i = 0; i < 9; ++i)
        {
            this.renderSlot(i, p_175258_1_.func_78326_a() / 2 - 90 + i * 20 + 2, p_175258_4_ + 3.0F, p_175258_2_, p_175258_5_.getObject(i));
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.func_179101_C();
        GlStateManager.func_179084_k();
    }

    private void renderSlot(int p_175266_1_, int p_175266_2_, float p_175266_3_, float p_175266_4_, ISpectatorMenuObject p_175266_5_)
    {
        this.mc.getTextureManager().bindTexture(SPECTATOR_WIDGETS);

        if (p_175266_5_ != SpectatorMenu.EMPTY_SLOT)
        {
            int i = (int)(p_175266_4_ * 255.0F);
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)p_175266_2_, p_175266_3_, 0.0F);
            float f = p_175266_5_.isEnabled() ? 1.0F : 0.25F;
            GlStateManager.func_179131_c(f, f, f, p_175266_4_);
            p_175266_5_.renderIcon(f, i);
            GlStateManager.func_179121_F();
            String s = String.valueOf((Object)GameSettings.func_74298_c(this.mc.gameSettings.keyBindsHotbar[p_175266_1_].func_151463_i()));

            if (i > 3 && p_175266_5_.isEnabled())
            {
                this.mc.fontRenderer.drawStringWithShadow(s, (float)(p_175266_2_ + 19 - 2 - this.mc.fontRenderer.getStringWidth(s)), p_175266_3_ + 6.0F + 3.0F, 16777215 + (i << 24));
            }
        }
    }

    public void func_175263_a(ScaledResolution p_175263_1_)
    {
        int i = (int)(this.getHotbarAlpha() * 255.0F);

        if (i > 3 && this.menu != null)
        {
            ISpectatorMenuObject ispectatormenuobject = this.menu.getSelectedItem();
            String s = ispectatormenuobject == SpectatorMenu.EMPTY_SLOT ? this.menu.getSelectedCategory().getPrompt().getFormattedText() : ispectatormenuobject.getSpectatorName().getFormattedText();

            if (s != null)
            {
                int j = (p_175263_1_.func_78326_a() - this.mc.fontRenderer.getStringWidth(s)) / 2;
                int k = p_175263_1_.func_78328_b() - 35;
                GlStateManager.func_179094_E();
                GlStateManager.func_179147_l();
                GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                this.mc.fontRenderer.drawStringWithShadow(s, (float)j, (float)k, 16777215 + (i << 24));
                GlStateManager.func_179084_k();
                GlStateManager.func_179121_F();
            }
        }
    }

    public void onSpectatorMenuClosed(SpectatorMenu menu)
    {
        this.menu = null;
        this.lastSelectionTime = 0L;
    }

    public boolean isMenuActive()
    {
        return this.menu != null;
    }

    public void func_175259_b(int p_175259_1_)
    {
        int i;

        for (i = this.menu.getSelectedSlot() + p_175259_1_; i >= 0 && i <= 8 && (this.menu.getItem(i) == SpectatorMenu.EMPTY_SLOT || !this.menu.getItem(i).isEnabled()); i += p_175259_1_)
        {
            ;
        }

        if (i >= 0 && i <= 8)
        {
            this.menu.selectSlot(i);
            this.lastSelectionTime = Minecraft.func_71386_F();
        }
    }

    public void onMiddleClick()
    {
        this.lastSelectionTime = Minecraft.func_71386_F();

        if (this.isMenuActive())
        {
            int i = this.menu.getSelectedSlot();

            if (i != -1)
            {
                this.menu.selectSlot(i);
            }
        }
        else
        {
            this.menu = new SpectatorMenu(this);
        }
    }
}

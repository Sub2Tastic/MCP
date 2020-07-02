package net.minecraft.client.gui.advancements;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class GuiScreenAdvancements extends GuiScreen implements ClientAdvancementManager.IListener
{
    private static final ResourceLocation WINDOW = new ResourceLocation("textures/gui/advancements/window.png");
    private static final ResourceLocation TABS = new ResourceLocation("textures/gui/advancements/tabs.png");
    private final ClientAdvancementManager clientAdvancementManager;
    private final Map<Advancement, GuiAdvancementTab> tabs = Maps.<Advancement, GuiAdvancementTab>newLinkedHashMap();
    private GuiAdvancementTab selectedTab;
    private int field_191941_t;
    private int field_191942_u;
    private boolean isScrolling;

    public GuiScreenAdvancements(ClientAdvancementManager p_i47383_1_)
    {
        this.clientAdvancementManager = p_i47383_1_;
    }

    public void func_73866_w_()
    {
        this.tabs.clear();
        this.selectedTab = null;
        this.clientAdvancementManager.setListener(this);

        if (this.selectedTab == null && !this.tabs.isEmpty())
        {
            this.clientAdvancementManager.setSelectedTab(((GuiAdvancementTab)this.tabs.values().iterator().next()).getAdvancement(), true);
        }
        else
        {
            this.clientAdvancementManager.setSelectedTab(this.selectedTab == null ? null : this.selectedTab.getAdvancement(), true);
        }
    }

    public void func_146281_b()
    {
        this.clientAdvancementManager.setListener((ClientAdvancementManager.IListener)null);
        NetHandlerPlayClient nethandlerplayclient = this.field_146297_k.getConnection();

        if (nethandlerplayclient != null)
        {
            nethandlerplayclient.sendPacket(CPacketSeenAdvancements.closedScreen());
        }
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        if (p_73864_3_ == 0)
        {
            int i = (this.field_146294_l - 252) / 2;
            int j = (this.field_146295_m - 140) / 2;

            for (GuiAdvancementTab guiadvancementtab : this.tabs.values())
            {
                if (guiadvancementtab.func_191793_c(i, j, p_73864_1_, p_73864_2_))
                {
                    this.clientAdvancementManager.setSelectedTab(guiadvancementtab.getAdvancement(), true);
                    break;
                }
            }
        }

        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (p_73869_2_ == this.field_146297_k.gameSettings.keyBindAdvancements.func_151463_i())
        {
            this.field_146297_k.displayGuiScreen((GuiScreen)null);
            this.field_146297_k.func_71381_h();
        }
        else
        {
            super.func_73869_a(p_73869_1_, p_73869_2_);
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        int i = (this.field_146294_l - 252) / 2;
        int j = (this.field_146295_m - 140) / 2;

        if (Mouse.isButtonDown(0))
        {
            if (!this.isScrolling)
            {
                this.isScrolling = true;
            }
            else if (this.selectedTab != null)
            {
                this.selectedTab.func_191797_b(p_73863_1_ - this.field_191941_t, p_73863_2_ - this.field_191942_u);
            }

            this.field_191941_t = p_73863_1_;
            this.field_191942_u = p_73863_2_;
        }
        else
        {
            this.isScrolling = false;
        }

        this.func_146276_q_();
        this.renderInside(p_73863_1_, p_73863_2_, i, j);
        this.renderWindow(i, j);
        this.renderToolTips(p_73863_1_, p_73863_2_, i, j);
    }

    private void renderInside(int p_191936_1_, int p_191936_2_, int p_191936_3_, int p_191936_4_)
    {
        GuiAdvancementTab guiadvancementtab = this.selectedTab;

        if (guiadvancementtab == null)
        {
            func_73734_a(p_191936_3_ + 9, p_191936_4_ + 18, p_191936_3_ + 9 + 234, p_191936_4_ + 18 + 113, -16777216);
            String s = I18n.format("advancements.empty");
            int i = this.field_146289_q.getStringWidth(s);
            this.field_146289_q.func_78276_b(s, p_191936_3_ + 9 + 117 - i / 2, p_191936_4_ + 18 + 56 - this.field_146289_q.FONT_HEIGHT / 2, -1);
            this.field_146289_q.func_78276_b(":(", p_191936_3_ + 9 + 117 - this.field_146289_q.getStringWidth(":(") / 2, p_191936_4_ + 18 + 113 - this.field_146289_q.FONT_HEIGHT, -1);
        }
        else
        {
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)(p_191936_3_ + 9), (float)(p_191936_4_ + 18), -400.0F);
            GlStateManager.func_179126_j();
            guiadvancementtab.drawContents();
            GlStateManager.func_179121_F();
            GlStateManager.func_179143_c(515);
            GlStateManager.func_179097_i();
        }
    }

    public void renderWindow(int p_191934_1_, int p_191934_2_)
    {
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179147_l();
        RenderHelper.disableStandardItemLighting();
        this.field_146297_k.getTextureManager().bindTexture(WINDOW);
        this.func_73729_b(p_191934_1_, p_191934_2_, 0, 0, 252, 140);

        if (this.tabs.size() > 1)
        {
            this.field_146297_k.getTextureManager().bindTexture(TABS);

            for (GuiAdvancementTab guiadvancementtab : this.tabs.values())
            {
                guiadvancementtab.drawTab(p_191934_1_, p_191934_2_, guiadvancementtab == this.selectedTab);
            }

            GlStateManager.func_179091_B();
            GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderHelper.func_74520_c();

            for (GuiAdvancementTab guiadvancementtab1 : this.tabs.values())
            {
                guiadvancementtab1.drawIcon(p_191934_1_, p_191934_2_, this.field_146296_j);
            }

            GlStateManager.func_179084_k();
        }

        this.field_146289_q.func_78276_b(I18n.format("gui.advancements"), p_191934_1_ + 8, p_191934_2_ + 6, 4210752);
    }

    private void renderToolTips(int p_191937_1_, int p_191937_2_, int p_191937_3_, int p_191937_4_)
    {
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.selectedTab != null)
        {
            GlStateManager.func_179094_E();
            GlStateManager.func_179126_j();
            GlStateManager.func_179109_b((float)(p_191937_3_ + 9), (float)(p_191937_4_ + 18), 400.0F);
            this.selectedTab.drawToolTips(p_191937_1_ - p_191937_3_ - 9, p_191937_2_ - p_191937_4_ - 18, p_191937_3_, p_191937_4_);
            GlStateManager.func_179097_i();
            GlStateManager.func_179121_F();
        }

        if (this.tabs.size() > 1)
        {
            for (GuiAdvancementTab guiadvancementtab : this.tabs.values())
            {
                if (guiadvancementtab.func_191793_c(p_191937_3_, p_191937_4_, p_191937_1_, p_191937_2_))
                {
                    this.func_146279_a(guiadvancementtab.getTitle(), p_191937_1_, p_191937_2_);
                }
            }
        }
    }

    public void rootAdvancementAdded(Advancement advancementIn)
    {
        GuiAdvancementTab guiadvancementtab = GuiAdvancementTab.create(this.field_146297_k, this, this.tabs.size(), advancementIn);

        if (guiadvancementtab != null)
        {
            this.tabs.put(advancementIn, guiadvancementtab);
        }
    }

    public void rootAdvancementRemoved(Advancement advancementIn)
    {
    }

    public void nonRootAdvancementAdded(Advancement advancementIn)
    {
        GuiAdvancementTab guiadvancementtab = this.getTab(advancementIn);

        if (guiadvancementtab != null)
        {
            guiadvancementtab.addAdvancement(advancementIn);
        }
    }

    public void nonRootAdvancementRemoved(Advancement advancementIn)
    {
    }

    public void onUpdateAdvancementProgress(Advancement advancementIn, AdvancementProgress progress)
    {
        GuiAdvancement guiadvancement = this.getAdvancementGui(advancementIn);

        if (guiadvancement != null)
        {
            guiadvancement.setAdvancementProgress(progress);
        }
    }

    public void setSelectedTab(@Nullable Advancement advancementIn)
    {
        this.selectedTab = this.tabs.get(advancementIn);
    }

    public void advancementsCleared()
    {
        this.tabs.clear();
        this.selectedTab = null;
    }

    @Nullable
    public GuiAdvancement getAdvancementGui(Advancement p_191938_1_)
    {
        GuiAdvancementTab guiadvancementtab = this.getTab(p_191938_1_);
        return guiadvancementtab == null ? null : guiadvancementtab.getAdvancementGui(p_191938_1_);
    }

    @Nullable
    private GuiAdvancementTab getTab(Advancement p_191935_1_)
    {
        while (p_191935_1_.getParent() != null)
        {
            p_191935_1_ = p_191935_1_.getParent();
        }

        return this.tabs.get(p_191935_1_);
    }
}

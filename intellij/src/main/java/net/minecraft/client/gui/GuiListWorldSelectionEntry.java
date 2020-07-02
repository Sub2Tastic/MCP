package net.minecraft.client.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiListWorldSelectionEntry implements GuiListExtended.IGuiListEntry
{
    private static final Logger field_186780_a = LogManager.getLogger();
    private static final DateFormat field_186781_b = new SimpleDateFormat();
    private static final ResourceLocation field_186782_c = new ResourceLocation("textures/misc/unknown_server.png");
    private static final ResourceLocation field_186783_d = new ResourceLocation("textures/gui/world_selection.png");
    private final Minecraft field_186784_e;
    private final GuiWorldSelection field_186785_f;
    private final WorldSummary field_186786_g;
    private final ResourceLocation field_186787_h;
    private final GuiListWorldSelection field_186788_i;
    private File field_186789_j;
    private DynamicTexture field_186790_k;
    private long field_186791_l;

    public GuiListWorldSelectionEntry(GuiListWorldSelection p_i46591_1_, WorldSummary p_i46591_2_, ISaveFormat p_i46591_3_)
    {
        this.field_186788_i = p_i46591_1_;
        this.field_186785_f = p_i46591_1_.getGuiWorldSelection();
        this.field_186786_g = p_i46591_2_;
        this.field_186784_e = Minecraft.getInstance();
        this.field_186787_h = new ResourceLocation("worlds/" + p_i46591_2_.getFileName() + "/icon");
        this.field_186789_j = p_i46591_3_.getFile(p_i46591_2_.getFileName(), "icon.png");

        if (!this.field_186789_j.isFile())
        {
            this.field_186789_j = null;
        }

        this.func_186769_f();
    }

    public void func_192634_a(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_)
    {
        String s = this.field_186786_g.getDisplayName();
        String s1 = this.field_186786_g.getFileName() + " (" + field_186781_b.format(new Date(this.field_186786_g.getLastTimePlayed())) + ")";
        String s2 = "";

        if (StringUtils.isEmpty(s))
        {
            s = I18n.format("selectWorld.world") + " " + (p_192634_1_ + 1);
        }

        if (this.field_186786_g.requiresConversion())
        {
            s2 = I18n.format("selectWorld.conversion") + " " + s2;
        }
        else
        {
            s2 = I18n.format("gameMode." + this.field_186786_g.getEnumGameType().getName());

            if (this.field_186786_g.isHardcoreModeEnabled())
            {
                s2 = TextFormatting.DARK_RED + I18n.format("gameMode.hardcore") + TextFormatting.RESET;
            }

            if (this.field_186786_g.getCheatsEnabled())
            {
                s2 = s2 + ", " + I18n.format("selectWorld.cheats");
            }

            String s3 = this.field_186786_g.func_186357_i();

            if (this.field_186786_g.markVersionInList())
            {
                if (this.field_186786_g.askToOpenWorld())
                {
                    s2 = s2 + ", " + I18n.format("selectWorld.version") + " " + TextFormatting.RED + s3 + TextFormatting.RESET;
                }
                else
                {
                    s2 = s2 + ", " + I18n.format("selectWorld.version") + " " + TextFormatting.ITALIC + s3 + TextFormatting.RESET;
                }
            }
            else
            {
                s2 = s2 + ", " + I18n.format("selectWorld.version") + " " + s3;
            }
        }

        this.field_186784_e.fontRenderer.func_78276_b(s, p_192634_2_ + 32 + 3, p_192634_3_ + 1, 16777215);
        this.field_186784_e.fontRenderer.func_78276_b(s1, p_192634_2_ + 32 + 3, p_192634_3_ + this.field_186784_e.fontRenderer.FONT_HEIGHT + 3, 8421504);
        this.field_186784_e.fontRenderer.func_78276_b(s2, p_192634_2_ + 32 + 3, p_192634_3_ + this.field_186784_e.fontRenderer.FONT_HEIGHT + this.field_186784_e.fontRenderer.FONT_HEIGHT + 3, 8421504);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_186784_e.getTextureManager().bindTexture(this.field_186790_k != null ? this.field_186787_h : field_186782_c);
        GlStateManager.func_179147_l();
        Gui.func_146110_a(p_192634_2_, p_192634_3_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        GlStateManager.func_179084_k();

        if (this.field_186784_e.gameSettings.touchscreen || p_192634_8_)
        {
            this.field_186784_e.getTextureManager().bindTexture(field_186783_d);
            Gui.func_73734_a(p_192634_2_, p_192634_3_, p_192634_2_ + 32, p_192634_3_ + 32, -1601138544);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            int j = p_192634_6_ - p_192634_2_;
            int i = j < 32 ? 32 : 0;

            if (this.field_186786_g.markVersionInList())
            {
                Gui.func_146110_a(p_192634_2_, p_192634_3_, 32.0F, (float)i, 32, 32, 256.0F, 256.0F);

                if (this.field_186786_g.askToOpenWorld())
                {
                    Gui.func_146110_a(p_192634_2_, p_192634_3_, 96.0F, (float)i, 32, 32, 256.0F, 256.0F);

                    if (j < 32)
                    {
                        this.field_186785_f.setVersionTooltip(TextFormatting.RED + I18n.format("selectWorld.tooltip.fromNewerVersion1") + "\n" + TextFormatting.RED + I18n.format("selectWorld.tooltip.fromNewerVersion2"));
                    }
                }
                else
                {
                    Gui.func_146110_a(p_192634_2_, p_192634_3_, 64.0F, (float)i, 32, 32, 256.0F, 256.0F);

                    if (j < 32)
                    {
                        this.field_186785_f.setVersionTooltip(TextFormatting.GOLD + I18n.format("selectWorld.tooltip.snapshot1") + "\n" + TextFormatting.GOLD + I18n.format("selectWorld.tooltip.snapshot2"));
                    }
                }
            }
            else
            {
                Gui.func_146110_a(p_192634_2_, p_192634_3_, 0.0F, (float)i, 32, 32, 256.0F, 256.0F);
            }
        }
    }

    public boolean func_148278_a(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        this.field_186788_i.func_186792_d(p_148278_1_);

        if (p_148278_5_ <= 32 && p_148278_5_ < 32)
        {
            this.func_186774_a();
            return true;
        }
        else if (Minecraft.func_71386_F() - this.field_186791_l < 250L)
        {
            this.func_186774_a();
            return true;
        }
        else
        {
            this.field_186791_l = Minecraft.func_71386_F();
            return false;
        }
    }

    public void func_186774_a()
    {
        if (this.field_186786_g.askToOpenWorld())
        {
            this.field_186784_e.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback()
            {
                public void func_73878_a(boolean p_73878_1_, int p_73878_2_)
                {
                    if (p_73878_1_)
                    {
                        GuiListWorldSelectionEntry.this.func_186777_e();
                    }
                    else
                    {
                        GuiListWorldSelectionEntry.this.field_186784_e.displayGuiScreen(GuiListWorldSelectionEntry.this.field_186785_f);
                    }
                }
            }, I18n.format("selectWorld.versionQuestion"), I18n.format("selectWorld.versionWarning", this.field_186786_g.func_186357_i()), I18n.format("selectWorld.versionJoinButton"), I18n.format("gui.cancel"), 0));
        }
        else
        {
            this.func_186777_e();
        }
    }

    public void func_186776_b()
    {
        this.field_186784_e.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback()
        {
            public void func_73878_a(boolean p_73878_1_, int p_73878_2_)
            {
                if (p_73878_1_)
                {
                    GuiListWorldSelectionEntry.this.field_186784_e.displayGuiScreen(new GuiScreenWorking());
                    ISaveFormat isaveformat = GuiListWorldSelectionEntry.this.field_186784_e.getSaveLoader();
                    isaveformat.func_75800_d();
                    isaveformat.deleteWorldDirectory(GuiListWorldSelectionEntry.this.field_186786_g.getFileName());
                    GuiListWorldSelectionEntry.this.field_186788_i.func_186795_e();
                }

                GuiListWorldSelectionEntry.this.field_186784_e.displayGuiScreen(GuiListWorldSelectionEntry.this.field_186785_f);
            }
        }, I18n.format("selectWorld.deleteQuestion"), "'" + this.field_186786_g.getDisplayName() + "' " + I18n.format("selectWorld.deleteWarning"), I18n.format("selectWorld.deleteButton"), I18n.format("gui.cancel"), 0));
    }

    public void func_186778_c()
    {
        this.field_186784_e.displayGuiScreen(new GuiWorldEdit(this.field_186785_f, this.field_186786_g.getFileName()));
    }

    public void func_186779_d()
    {
        this.field_186784_e.displayGuiScreen(new GuiScreenWorking());
        GuiCreateWorld guicreateworld = new GuiCreateWorld(this.field_186785_f);
        ISaveHandler isavehandler = this.field_186784_e.getSaveLoader().func_75804_a(this.field_186786_g.getFileName(), false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        isavehandler.func_75759_a();

        if (worldinfo != null)
        {
            guicreateworld.recreateFromExistingWorld(worldinfo);
            this.field_186784_e.displayGuiScreen(guicreateworld);
        }
    }

    private void func_186777_e()
    {
        this.field_186784_e.getSoundHandler().play(PositionedSoundRecord.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

        if (this.field_186784_e.getSaveLoader().canLoadWorld(this.field_186786_g.getFileName()))
        {
            this.field_186784_e.launchIntegratedServer(this.field_186786_g.getFileName(), this.field_186786_g.getDisplayName(), (WorldSettings)null);
        }
    }

    private void func_186769_f()
    {
        boolean flag = this.field_186789_j != null && this.field_186789_j.isFile();

        if (flag)
        {
            BufferedImage bufferedimage;

            try
            {
                bufferedimage = ImageIO.read(this.field_186789_j);
                Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide");
                Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high");
            }
            catch (Throwable throwable)
            {
                field_186780_a.error("Invalid icon for world {}", this.field_186786_g.getFileName(), throwable);
                this.field_186789_j = null;
                return;
            }

            if (this.field_186790_k == null)
            {
                this.field_186790_k = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                this.field_186784_e.getTextureManager().func_110579_a(this.field_186787_h, this.field_186790_k);
            }

            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.field_186790_k.func_110565_c(), 0, bufferedimage.getWidth());
            this.field_186790_k.updateDynamicTexture();
        }
        else if (!flag)
        {
            this.field_186784_e.getTextureManager().deleteTexture(this.field_186787_h);
            this.field_186790_k = null;
        }
    }

    public void func_148277_b(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_)
    {
    }

    public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_)
    {
    }
}

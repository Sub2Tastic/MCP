package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;

public class GuiOptions extends GuiScreen
{
    private static final GameSettings.Options[] SCREEN_OPTIONS = new GameSettings.Options[] {GameSettings.Options.FOV};
    private final GuiScreen lastScreen;

    /** Reference to the GameSettings object. */
    private final GameSettings settings;
    private GuiButton difficultyButton;
    private GuiLockIconButton lockButton;
    protected String field_146442_a = "Options";

    public GuiOptions(GuiScreen parentScreen, GameSettings gameSettingsObj)
    {
        this.lastScreen = parentScreen;
        this.settings = gameSettingsObj;
    }

    public void func_73866_w_()
    {
        this.field_146442_a = I18n.format("options.title");
        int i = 0;

        for (GameSettings.Options gamesettings$options : SCREEN_OPTIONS)
        {
            if (gamesettings$options.func_74380_a())
            {
                this.field_146292_n.add(new GuiOptionSlider(gamesettings$options.func_74381_c(), this.field_146294_l / 2 - 155 + i % 2 * 160, this.field_146295_m / 6 - 12 + 24 * (i >> 1), gamesettings$options));
            }
            else
            {
                GuiOptionButton guioptionbutton = new GuiOptionButton(gamesettings$options.func_74381_c(), this.field_146294_l / 2 - 155 + i % 2 * 160, this.field_146295_m / 6 - 12 + 24 * (i >> 1), gamesettings$options, this.settings.func_74297_c(gamesettings$options));
                this.field_146292_n.add(guioptionbutton);
            }

            ++i;
        }

        if (this.field_146297_k.world != null)
        {
            EnumDifficulty enumdifficulty = this.field_146297_k.world.getDifficulty();
            this.difficultyButton = new GuiButton(108, this.field_146294_l / 2 - 155 + i % 2 * 160, this.field_146295_m / 6 - 12 + 24 * (i >> 1), 150, 20, this.getDifficultyText(enumdifficulty));
            this.field_146292_n.add(this.difficultyButton);

            if (this.field_146297_k.isSingleplayer() && !this.field_146297_k.world.getWorldInfo().isHardcore())
            {
                this.difficultyButton.func_175211_a(this.difficultyButton.func_146117_b() - 20);
                this.lockButton = new GuiLockIconButton(109, this.difficultyButton.field_146128_h + this.difficultyButton.func_146117_b(), this.difficultyButton.field_146129_i);
                this.field_146292_n.add(this.lockButton);
                this.lockButton.setLocked(this.field_146297_k.world.getWorldInfo().isDifficultyLocked());
                this.lockButton.field_146124_l = !this.lockButton.isLocked();
                this.difficultyButton.field_146124_l = !this.lockButton.isLocked();
            }
            else
            {
                this.difficultyButton.field_146124_l = false;
            }
        }
        else
        {
            this.field_146292_n.add(new GuiOptionButton(GameSettings.Options.REALMS_NOTIFICATIONS.func_74381_c(), this.field_146294_l / 2 - 155 + i % 2 * 160, this.field_146295_m / 6 - 12 + 24 * (i >> 1), GameSettings.Options.REALMS_NOTIFICATIONS, this.settings.func_74297_c(GameSettings.Options.REALMS_NOTIFICATIONS)));
        }

        this.field_146292_n.add(new GuiButton(110, this.field_146294_l / 2 - 155, this.field_146295_m / 6 + 48 - 6, 150, 20, I18n.format("options.skinCustomisation")));
        this.field_146292_n.add(new GuiButton(106, this.field_146294_l / 2 + 5, this.field_146295_m / 6 + 48 - 6, 150, 20, I18n.format("options.sounds")));
        this.field_146292_n.add(new GuiButton(101, this.field_146294_l / 2 - 155, this.field_146295_m / 6 + 72 - 6, 150, 20, I18n.format("options.video")));
        this.field_146292_n.add(new GuiButton(100, this.field_146294_l / 2 + 5, this.field_146295_m / 6 + 72 - 6, 150, 20, I18n.format("options.controls")));
        this.field_146292_n.add(new GuiButton(102, this.field_146294_l / 2 - 155, this.field_146295_m / 6 + 96 - 6, 150, 20, I18n.format("options.language")));
        this.field_146292_n.add(new GuiButton(103, this.field_146294_l / 2 + 5, this.field_146295_m / 6 + 96 - 6, 150, 20, I18n.format("options.chat.title")));
        this.field_146292_n.add(new GuiButton(105, this.field_146294_l / 2 - 155, this.field_146295_m / 6 + 120 - 6, 150, 20, I18n.format("options.resourcepack")));
        this.field_146292_n.add(new GuiButton(104, this.field_146294_l / 2 + 5, this.field_146295_m / 6 + 120 - 6, 150, 20, I18n.format("options.snooper.view")));
        this.field_146292_n.add(new GuiButton(200, this.field_146294_l / 2 - 100, this.field_146295_m / 6 + 168, I18n.format("gui.done")));
    }

    public String getDifficultyText(EnumDifficulty p_175355_1_)
    {
        ITextComponent itextcomponent = new TextComponentString("");
        itextcomponent.appendSibling(new TextComponentTranslation("options.difficulty", new Object[0]));
        itextcomponent.appendText(": ");
        itextcomponent.appendSibling(new TextComponentTranslation(p_175355_1_.getTranslationKey(), new Object[0]));
        return itextcomponent.getFormattedText();
    }

    public void func_73878_a(boolean p_73878_1_, int p_73878_2_)
    {
        this.field_146297_k.displayGuiScreen(this);

        if (p_73878_2_ == 109 && p_73878_1_ && this.field_146297_k.world != null)
        {
            this.field_146297_k.world.getWorldInfo().setDifficultyLocked(true);
            this.lockButton.setLocked(true);
            this.lockButton.field_146124_l = false;
            this.difficultyButton.field_146124_l = false;
        }
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (p_73869_2_ == 1)
        {
            this.field_146297_k.gameSettings.saveOptions();
        }

        super.func_73869_a(p_73869_1_, p_73869_2_);
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146124_l)
        {
            if (p_146284_1_.field_146127_k < 100 && p_146284_1_ instanceof GuiOptionButton)
            {
                GameSettings.Options gamesettings$options = ((GuiOptionButton)p_146284_1_).func_146136_c();
                this.settings.func_74306_a(gamesettings$options, 1);
                p_146284_1_.field_146126_j = this.settings.func_74297_c(GameSettings.Options.func_74379_a(p_146284_1_.field_146127_k));
            }

            if (p_146284_1_.field_146127_k == 108)
            {
                this.field_146297_k.world.getWorldInfo().setDifficulty(EnumDifficulty.byId(this.field_146297_k.world.getDifficulty().getId() + 1));
                this.difficultyButton.field_146126_j = this.getDifficultyText(this.field_146297_k.world.getDifficulty());
            }

            if (p_146284_1_.field_146127_k == 109)
            {
                this.field_146297_k.displayGuiScreen(new GuiYesNo(this, (new TextComponentTranslation("difficulty.lock.title", new Object[0])).getFormattedText(), (new TextComponentTranslation("difficulty.lock.question", new Object[] {new TextComponentTranslation(this.field_146297_k.world.getWorldInfo().getDifficulty().getTranslationKey(), new Object[0])})).getFormattedText(), 109));
            }

            if (p_146284_1_.field_146127_k == 110)
            {
                this.field_146297_k.gameSettings.saveOptions();
                this.field_146297_k.displayGuiScreen(new GuiCustomizeSkin(this));
            }

            if (p_146284_1_.field_146127_k == 101)
            {
                this.field_146297_k.gameSettings.saveOptions();
                this.field_146297_k.displayGuiScreen(new GuiVideoSettings(this, this.settings));
            }

            if (p_146284_1_.field_146127_k == 100)
            {
                this.field_146297_k.gameSettings.saveOptions();
                this.field_146297_k.displayGuiScreen(new GuiControls(this, this.settings));
            }

            if (p_146284_1_.field_146127_k == 102)
            {
                this.field_146297_k.gameSettings.saveOptions();
                this.field_146297_k.displayGuiScreen(new GuiLanguage(this, this.settings, this.field_146297_k.getLanguageManager()));
            }

            if (p_146284_1_.field_146127_k == 103)
            {
                this.field_146297_k.gameSettings.saveOptions();
                this.field_146297_k.displayGuiScreen(new ScreenChatOptions(this, this.settings));
            }

            if (p_146284_1_.field_146127_k == 104)
            {
                this.field_146297_k.gameSettings.saveOptions();
                this.field_146297_k.displayGuiScreen(new GuiSnooper(this, this.settings));
            }

            if (p_146284_1_.field_146127_k == 200)
            {
                this.field_146297_k.gameSettings.saveOptions();
                this.field_146297_k.displayGuiScreen(this.lastScreen);
            }

            if (p_146284_1_.field_146127_k == 105)
            {
                this.field_146297_k.gameSettings.saveOptions();
                this.field_146297_k.displayGuiScreen(new GuiScreenResourcePacks(this));
            }

            if (p_146284_1_.field_146127_k == 106)
            {
                this.field_146297_k.gameSettings.saveOptions();
                this.field_146297_k.displayGuiScreen(new GuiScreenOptionsSounds(this, this.settings));
            }
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, this.field_146442_a, this.field_146294_l / 2, 15, 16777215);
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}

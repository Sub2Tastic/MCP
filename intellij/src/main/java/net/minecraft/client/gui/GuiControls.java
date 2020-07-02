package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class GuiControls extends GuiScreen
{
    private static final GameSettings.Options[] field_146492_g = new GameSettings.Options[] {GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN, GameSettings.Options.AUTO_JUMP};
    private final GuiScreen field_146496_h;
    protected String field_146495_a = "Controls";
    private final GameSettings field_146497_i;

    /** The ID of the button that has been pressed. */
    public KeyBinding buttonId;
    public long time;
    private GuiKeyBindingList keyBindingList;
    private GuiButton buttonReset;

    public GuiControls(GuiScreen screen, GameSettings settings)
    {
        this.field_146496_h = screen;
        this.field_146497_i = settings;
    }

    public void func_73866_w_()
    {
        this.keyBindingList = new GuiKeyBindingList(this, this.field_146297_k);
        this.field_146292_n.add(new GuiButton(200, this.field_146294_l / 2 - 155 + 160, this.field_146295_m - 29, 150, 20, I18n.format("gui.done")));
        this.buttonReset = this.func_189646_b(new GuiButton(201, this.field_146294_l / 2 - 155, this.field_146295_m - 29, 150, 20, I18n.format("controls.resetAll")));
        this.field_146495_a = I18n.format("controls.title");
        int i = 0;

        for (GameSettings.Options gamesettings$options : field_146492_g)
        {
            if (gamesettings$options.func_74380_a())
            {
                this.field_146292_n.add(new GuiOptionSlider(gamesettings$options.func_74381_c(), this.field_146294_l / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options));
            }
            else
            {
                this.field_146292_n.add(new GuiOptionButton(gamesettings$options.func_74381_c(), this.field_146294_l / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options, this.field_146497_i.func_74297_c(gamesettings$options)));
            }

            ++i;
        }
    }

    public void func_146274_d() throws IOException
    {
        super.func_146274_d();
        this.keyBindingList.func_178039_p();
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146127_k == 200)
        {
            this.field_146297_k.displayGuiScreen(this.field_146496_h);
        }
        else if (p_146284_1_.field_146127_k == 201)
        {
            for (KeyBinding keybinding : this.field_146297_k.gameSettings.keyBindings)
            {
                keybinding.func_151462_b(keybinding.func_151469_h());
            }

            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else if (p_146284_1_.field_146127_k < 100 && p_146284_1_ instanceof GuiOptionButton)
        {
            this.field_146497_i.func_74306_a(((GuiOptionButton)p_146284_1_).func_146136_c(), 1);
            p_146284_1_.field_146126_j = this.field_146497_i.func_74297_c(GameSettings.Options.func_74379_a(p_146284_1_.field_146127_k));
        }
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        if (this.buttonId != null)
        {
            this.field_146497_i.func_151440_a(this.buttonId, -100 + p_73864_3_);
            this.buttonId = null;
            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else if (p_73864_3_ != 0 || !this.keyBindingList.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_))
        {
            super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
        }
    }

    protected void func_146286_b(int p_146286_1_, int p_146286_2_, int p_146286_3_)
    {
        if (p_146286_3_ != 0 || !this.keyBindingList.func_148181_b(p_146286_1_, p_146286_2_, p_146286_3_))
        {
            super.func_146286_b(p_146286_1_, p_146286_2_, p_146286_3_);
        }
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (this.buttonId != null)
        {
            if (p_73869_2_ == 1)
            {
                this.field_146497_i.func_151440_a(this.buttonId, 0);
            }
            else if (p_73869_2_ != 0)
            {
                this.field_146497_i.func_151440_a(this.buttonId, p_73869_2_);
            }
            else if (p_73869_1_ > 0)
            {
                this.field_146497_i.func_151440_a(this.buttonId, p_73869_1_ + 256);
            }

            this.buttonId = null;
            this.time = Minecraft.func_71386_F();
            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else
        {
            super.func_73869_a(p_73869_1_, p_73869_2_);
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.keyBindingList.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.func_73732_a(this.field_146289_q, this.field_146495_a, this.field_146294_l / 2, 8, 16777215);
        boolean flag = false;

        for (KeyBinding keybinding : this.field_146497_i.keyBindings)
        {
            if (keybinding.func_151463_i() != keybinding.func_151469_h())
            {
                flag = true;
                break;
            }
        }

        this.buttonReset.field_146124_l = flag;
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}

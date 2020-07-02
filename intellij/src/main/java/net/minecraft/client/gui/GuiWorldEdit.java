package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

public class GuiWorldEdit extends GuiScreen
{
    private final GuiScreen field_184858_a;
    private GuiTextField nameEdit;
    private final String worldId;

    public GuiWorldEdit(GuiScreen p_i46593_1_, String p_i46593_2_)
    {
        this.field_184858_a = p_i46593_1_;
        this.worldId = p_i46593_2_;
    }

    public void func_73876_c()
    {
        this.nameEdit.tick();
    }

    public void func_73866_w_()
    {
        Keyboard.enableRepeatEvents(true);
        this.field_146292_n.clear();
        GuiButton guibutton = this.func_189646_b(new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 24 + 12, I18n.format("selectWorld.edit.resetIcon")));
        this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 12, I18n.format("selectWorld.edit.openFolder")));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96 + 12, I18n.format("selectWorld.edit.save")));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120 + 12, I18n.format("gui.cancel")));
        guibutton.field_146124_l = this.field_146297_k.getSaveLoader().getFile(this.worldId, "icon.png").isFile();
        ISaveFormat isaveformat = this.field_146297_k.getSaveLoader();
        WorldInfo worldinfo = isaveformat.getWorldInfo(this.worldId);
        String s = worldinfo == null ? "" : worldinfo.getWorldName();
        this.nameEdit = new GuiTextField(2, this.field_146289_q, this.field_146294_l / 2 - 100, 60, 200, 20);
        this.nameEdit.setFocused2(true);
        this.nameEdit.setText(s);
    }

    public void func_146281_b()
    {
        Keyboard.enableRepeatEvents(false);
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146124_l)
        {
            if (p_146284_1_.field_146127_k == 1)
            {
                this.field_146297_k.displayGuiScreen(this.field_184858_a);
            }
            else if (p_146284_1_.field_146127_k == 0)
            {
                ISaveFormat isaveformat = this.field_146297_k.getSaveLoader();
                isaveformat.renameWorld(this.worldId, this.nameEdit.getText().trim());
                this.field_146297_k.displayGuiScreen(this.field_184858_a);
            }
            else if (p_146284_1_.field_146127_k == 3)
            {
                ISaveFormat isaveformat1 = this.field_146297_k.getSaveLoader();
                FileUtils.deleteQuietly(isaveformat1.getFile(this.worldId, "icon.png"));
                p_146284_1_.field_146124_l = false;
            }
            else if (p_146284_1_.field_146127_k == 4)
            {
                ISaveFormat isaveformat2 = this.field_146297_k.getSaveLoader();
                OpenGlHelper.func_188786_a(isaveformat2.getFile(this.worldId, "icon.png").getParentFile());
            }
        }
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        this.nameEdit.func_146201_a(p_73869_1_, p_73869_2_);
        (this.field_146292_n.get(2)).field_146124_l = !this.nameEdit.getText().trim().isEmpty();

        if (p_73869_2_ == 28 || p_73869_2_ == 156)
        {
            this.func_146284_a(this.field_146292_n.get(2));
        }
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
        this.nameEdit.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, I18n.format("selectWorld.edit.title"), this.field_146294_l / 2, 20, 16777215);
        this.func_73731_b(this.field_146289_q, I18n.format("selectWorld.enterName"), this.field_146294_l / 2 - 100, 47, 10526880);
        this.nameEdit.func_146194_f();
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}

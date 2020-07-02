package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry
{
    private final Minecraft mc = Minecraft.getInstance();

    public void func_192634_a(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_)
    {
        int i = p_192634_3_ + p_192634_5_ / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2;
        this.mc.fontRenderer.func_78276_b(I18n.format("lanServer.scanning"), this.mc.currentScreen.field_146294_l / 2 - this.mc.fontRenderer.getStringWidth(I18n.format("lanServer.scanning")) / 2, i, 16777215);
        String s;

        switch ((int)(Minecraft.func_71386_F() / 300L % 4L))
        {
            case 0:
            default:
                s = "O o o";
                break;

            case 1:
            case 3:
                s = "o O o";
                break;

            case 2:
                s = "o o O";
        }

        this.mc.fontRenderer.func_78276_b(s, this.mc.currentScreen.field_146294_l / 2 - this.mc.fontRenderer.getStringWidth(s) / 2, i + this.mc.fontRenderer.FONT_HEIGHT, 8421504);
    }

    public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_)
    {
    }

    public boolean func_148278_a(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        return false;
    }

    public void func_148277_b(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_)
    {
    }
}

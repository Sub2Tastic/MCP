package net.minecraft.client.gui.inventory;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;

public class GuiEditSign extends GuiScreen
{
    /** Reference to the sign object. */
    private final TileEntitySign tileSign;

    /** Counts the number of screen updates. */
    private int updateCounter;

    /** The index of the line that is being edited. */
    private int editLine;
    private GuiButton field_146852_i;

    public GuiEditSign(TileEntitySign teSign)
    {
        this.tileSign = teSign;
    }

    public void func_73866_w_()
    {
        this.field_146292_n.clear();
        Keyboard.enableRepeatEvents(true);
        this.field_146852_i = this.func_189646_b(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120, I18n.format("gui.done")));
        this.tileSign.setEditable(false);
    }

    public void func_146281_b()
    {
        Keyboard.enableRepeatEvents(false);
        NetHandlerPlayClient nethandlerplayclient = this.field_146297_k.getConnection();

        if (nethandlerplayclient != null)
        {
            nethandlerplayclient.sendPacket(new CPacketUpdateSign(this.tileSign.getPos(), this.tileSign.signText));
        }

        this.tileSign.setEditable(true);
    }

    public void func_73876_c()
    {
        ++this.updateCounter;
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146124_l)
        {
            if (p_146284_1_.field_146127_k == 0)
            {
                this.tileSign.markDirty();
                this.field_146297_k.displayGuiScreen((GuiScreen)null);
            }
        }
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (p_73869_2_ == 200)
        {
            this.editLine = this.editLine - 1 & 3;
        }

        if (p_73869_2_ == 208 || p_73869_2_ == 28 || p_73869_2_ == 156)
        {
            this.editLine = this.editLine + 1 & 3;
        }

        String s = this.tileSign.signText[this.editLine].func_150260_c();

        if (p_73869_2_ == 14 && !s.isEmpty())
        {
            s = s.substring(0, s.length() - 1);
        }

        if (ChatAllowedCharacters.isAllowedCharacter(p_73869_1_) && this.field_146289_q.getStringWidth(s + p_73869_1_) <= 90)
        {
            s = s + p_73869_1_;
        }

        this.tileSign.signText[this.editLine] = new TextComponentString(s);

        if (p_73869_2_ == 1)
        {
            this.func_146284_a(this.field_146852_i);
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, I18n.format("sign.edit"), this.field_146294_l / 2, 40, 16777215);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)(this.field_146294_l / 2), 0.0F, 50.0F);
        float f = 93.75F;
        GlStateManager.func_179152_a(-93.75F, -93.75F, -93.75F);
        GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
        Block block = this.tileSign.func_145838_q();

        if (block == Blocks.field_150472_an)
        {
            float f1 = (float)(this.tileSign.func_145832_p() * 360) / 16.0F;
            GlStateManager.func_179114_b(f1, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179109_b(0.0F, -1.0625F, 0.0F);
        }
        else
        {
            int i = this.tileSign.func_145832_p();
            float f2 = 0.0F;

            if (i == 2)
            {
                f2 = 180.0F;
            }

            if (i == 4)
            {
                f2 = 90.0F;
            }

            if (i == 5)
            {
                f2 = -90.0F;
            }

            GlStateManager.func_179114_b(f2, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179109_b(0.0F, -1.0625F, 0.0F);
        }

        if (this.updateCounter / 6 % 2 == 0)
        {
            this.tileSign.field_145918_i = this.editLine;
        }

        TileEntityRendererDispatcher.instance.func_147549_a(this.tileSign, -0.5D, -0.75D, -0.5D, 0.0F);
        this.tileSign.field_145918_i = -1;
        GlStateManager.func_179121_F();
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}

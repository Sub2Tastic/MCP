package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.input.Keyboard;

public class GuiCommandBlock extends GuiScreen implements ITabCompleter
{
    private GuiTextField field_146485_f;
    private GuiTextField field_146486_g;
    private final TileEntityCommandBlock commandBlock;
    private GuiButton field_146490_i;
    private GuiButton field_146487_r;
    private GuiButton field_175390_s;
    private GuiButton modeBtn;
    private GuiButton conditionalBtn;
    private GuiButton autoExecBtn;
    private boolean field_175389_t;
    private TileEntityCommandBlock.Mode commandBlockMode = TileEntityCommandBlock.Mode.REDSTONE;
    private TabCompleter field_184083_x;
    private boolean conditional;
    private boolean automatic;

    public GuiCommandBlock(TileEntityCommandBlock commandBlockIn)
    {
        this.commandBlock = commandBlockIn;
    }

    public void func_73876_c()
    {
        this.field_146485_f.tick();
    }

    public void func_73866_w_()
    {
        final CommandBlockBaseLogic commandblockbaselogic = this.commandBlock.getCommandBlockLogic();
        Keyboard.enableRepeatEvents(true);
        this.field_146292_n.clear();
        this.field_146490_i = this.func_189646_b(new GuiButton(0, this.field_146294_l / 2 - 4 - 150, this.field_146295_m / 4 + 120 + 12, 150, 20, I18n.format("gui.done")));
        this.field_146487_r = this.func_189646_b(new GuiButton(1, this.field_146294_l / 2 + 4, this.field_146295_m / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel")));
        this.field_175390_s = this.func_189646_b(new GuiButton(4, this.field_146294_l / 2 + 150 - 20, 135, 20, 20, "O"));
        this.modeBtn = this.func_189646_b(new GuiButton(5, this.field_146294_l / 2 - 50 - 100 - 4, 165, 100, 20, I18n.format("advMode.mode.sequence")));
        this.conditionalBtn = this.func_189646_b(new GuiButton(6, this.field_146294_l / 2 - 50, 165, 100, 20, I18n.format("advMode.mode.unconditional")));
        this.autoExecBtn = this.func_189646_b(new GuiButton(7, this.field_146294_l / 2 + 50 + 4, 165, 100, 20, I18n.format("advMode.mode.redstoneTriggered")));
        this.field_146485_f = new GuiTextField(2, this.field_146289_q, this.field_146294_l / 2 - 150, 50, 300, 20);
        this.field_146485_f.setMaxStringLength(32500);
        this.field_146485_f.setFocused2(true);
        this.field_146486_g = new GuiTextField(3, this.field_146289_q, this.field_146294_l / 2 - 150, 135, 276, 20);
        this.field_146486_g.setMaxStringLength(32500);
        this.field_146486_g.setEnabled(false);
        this.field_146486_g.setText("-");
        this.field_146490_i.field_146124_l = false;
        this.field_175390_s.field_146124_l = false;
        this.modeBtn.field_146124_l = false;
        this.conditionalBtn.field_146124_l = false;
        this.autoExecBtn.field_146124_l = false;
        this.field_184083_x = new TabCompleter(this.field_146485_f, true)
        {
            @Nullable
            public BlockPos func_186839_b()
            {
                return commandblockbaselogic.getPosition();
            }
        };
    }

    public void updateGui()
    {
        CommandBlockBaseLogic commandblockbaselogic = this.commandBlock.getCommandBlockLogic();
        this.field_146485_f.setText(commandblockbaselogic.getCommand());
        this.field_175389_t = commandblockbaselogic.shouldTrackOutput();
        this.commandBlockMode = this.commandBlock.getMode();
        this.conditional = this.commandBlock.isConditional();
        this.automatic = this.commandBlock.isAuto();
        this.func_175388_a();
        this.updateMode();
        this.updateConditional();
        this.updateAutoExec();
        this.field_146490_i.field_146124_l = true;
        this.field_175390_s.field_146124_l = true;
        this.modeBtn.field_146124_l = true;
        this.conditionalBtn.field_146124_l = true;
        this.autoExecBtn.field_146124_l = true;
    }

    public void func_146281_b()
    {
        Keyboard.enableRepeatEvents(false);
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146124_l)
        {
            CommandBlockBaseLogic commandblockbaselogic = this.commandBlock.getCommandBlockLogic();

            if (p_146284_1_.field_146127_k == 1)
            {
                commandblockbaselogic.setTrackOutput(this.field_175389_t);
                this.field_146297_k.displayGuiScreen((GuiScreen)null);
            }
            else if (p_146284_1_.field_146127_k == 0)
            {
                PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
                commandblockbaselogic.func_145757_a(packetbuffer);
                packetbuffer.writeString(this.field_146485_f.getText());
                packetbuffer.writeBoolean(commandblockbaselogic.shouldTrackOutput());
                packetbuffer.writeString(this.commandBlockMode.name());
                packetbuffer.writeBoolean(this.conditional);
                packetbuffer.writeBoolean(this.automatic);
                this.field_146297_k.getConnection().sendPacket(new CPacketCustomPayload("MC|AutoCmd", packetbuffer));

                if (!commandblockbaselogic.shouldTrackOutput())
                {
                    commandblockbaselogic.setLastOutput((ITextComponent)null);
                }

                this.field_146297_k.displayGuiScreen((GuiScreen)null);
            }
            else if (p_146284_1_.field_146127_k == 4)
            {
                commandblockbaselogic.setTrackOutput(!commandblockbaselogic.shouldTrackOutput());
                this.func_175388_a();
            }
            else if (p_146284_1_.field_146127_k == 5)
            {
                this.nextMode();
                this.updateMode();
            }
            else if (p_146284_1_.field_146127_k == 6)
            {
                this.conditional = !this.conditional;
                this.updateConditional();
            }
            else if (p_146284_1_.field_146127_k == 7)
            {
                this.automatic = !this.automatic;
                this.updateAutoExec();
            }
        }
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        this.field_184083_x.func_186843_d();

        if (p_73869_2_ == 15)
        {
            this.field_184083_x.func_186841_a();
        }
        else
        {
            this.field_184083_x.func_186842_c();
        }

        this.field_146485_f.func_146201_a(p_73869_1_, p_73869_2_);
        this.field_146486_g.func_146201_a(p_73869_1_, p_73869_2_);

        if (p_73869_2_ != 28 && p_73869_2_ != 156)
        {
            if (p_73869_2_ == 1)
            {
                this.func_146284_a(this.field_146487_r);
            }
        }
        else
        {
            this.func_146284_a(this.field_146490_i);
        }
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
        this.field_146485_f.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
        this.field_146486_g.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, I18n.format("advMode.setCommand"), this.field_146294_l / 2, 20, 16777215);
        this.func_73731_b(this.field_146289_q, I18n.format("advMode.command"), this.field_146294_l / 2 - 150, 40, 10526880);
        this.field_146485_f.func_146194_f();
        int i = 75;
        int j = 0;
        this.func_73731_b(this.field_146289_q, I18n.format("advMode.nearestPlayer"), this.field_146294_l / 2 - 140, i + j++ * this.field_146289_q.FONT_HEIGHT, 10526880);
        this.func_73731_b(this.field_146289_q, I18n.format("advMode.randomPlayer"), this.field_146294_l / 2 - 140, i + j++ * this.field_146289_q.FONT_HEIGHT, 10526880);
        this.func_73731_b(this.field_146289_q, I18n.format("advMode.allPlayers"), this.field_146294_l / 2 - 140, i + j++ * this.field_146289_q.FONT_HEIGHT, 10526880);
        this.func_73731_b(this.field_146289_q, I18n.format("advMode.allEntities"), this.field_146294_l / 2 - 140, i + j++ * this.field_146289_q.FONT_HEIGHT, 10526880);
        this.func_73731_b(this.field_146289_q, I18n.format("advMode.self"), this.field_146294_l / 2 - 140, i + j++ * this.field_146289_q.FONT_HEIGHT, 10526880);

        if (!this.field_146486_g.getText().isEmpty())
        {
            i = i + j * this.field_146289_q.FONT_HEIGHT + 1;
            this.func_73731_b(this.field_146289_q, I18n.format("advMode.previousOutput"), this.field_146294_l / 2 - 150, i + 4, 10526880);
            this.field_146486_g.func_146194_f();
        }

        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    private void func_175388_a()
    {
        CommandBlockBaseLogic commandblockbaselogic = this.commandBlock.getCommandBlockLogic();

        if (commandblockbaselogic.shouldTrackOutput())
        {
            this.field_175390_s.field_146126_j = "O";

            if (commandblockbaselogic.getLastOutput() != null)
            {
                this.field_146486_g.setText(commandblockbaselogic.getLastOutput().func_150260_c());
            }
        }
        else
        {
            this.field_175390_s.field_146126_j = "X";
            this.field_146486_g.setText("-");
        }
    }

    private void updateMode()
    {
        switch (this.commandBlockMode)
        {
            case SEQUENCE:
                this.modeBtn.field_146126_j = I18n.format("advMode.mode.sequence");
                break;

            case AUTO:
                this.modeBtn.field_146126_j = I18n.format("advMode.mode.auto");
                break;

            case REDSTONE:
                this.modeBtn.field_146126_j = I18n.format("advMode.mode.redstone");
        }
    }

    private void nextMode()
    {
        switch (this.commandBlockMode)
        {
            case SEQUENCE:
                this.commandBlockMode = TileEntityCommandBlock.Mode.AUTO;
                break;

            case AUTO:
                this.commandBlockMode = TileEntityCommandBlock.Mode.REDSTONE;
                break;

            case REDSTONE:
                this.commandBlockMode = TileEntityCommandBlock.Mode.SEQUENCE;
        }
    }

    private void updateConditional()
    {
        if (this.conditional)
        {
            this.conditionalBtn.field_146126_j = I18n.format("advMode.mode.conditional");
        }
        else
        {
            this.conditionalBtn.field_146126_j = I18n.format("advMode.mode.unconditional");
        }
    }

    private void updateAutoExec()
    {
        if (this.automatic)
        {
            this.autoExecBtn.field_146126_j = I18n.format("advMode.mode.autoexec.bat");
        }
        else
        {
            this.autoExecBtn.field_146126_j = I18n.format("advMode.mode.redstoneTriggered");
        }
    }

    public void func_184072_a(String... p_184072_1_)
    {
        this.field_184083_x.func_186840_a(p_184072_1_);
    }
}

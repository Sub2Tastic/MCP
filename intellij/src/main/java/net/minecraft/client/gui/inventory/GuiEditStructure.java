package net.minecraft.client.gui.inventory;

import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiEditStructure extends GuiScreen
{
    private static final Logger field_189845_a = LogManager.getLogger();
    public static final int[] field_190302_a = new int[] {203, 205, 14, 211, 199, 207};
    private final TileEntityStructure tileStructure;
    private Mirror mirror = Mirror.NONE;
    private Rotation rotation = Rotation.NONE;
    private TileEntityStructure.Mode mode = TileEntityStructure.Mode.DATA;
    private boolean ignoreEntities;
    private boolean showAir;
    private boolean showBoundingBox;
    private GuiTextField nameEdit;
    private GuiTextField posXEdit;
    private GuiTextField posYEdit;
    private GuiTextField posZEdit;
    private GuiTextField sizeXEdit;
    private GuiTextField sizeYEdit;
    private GuiTextField sizeZEdit;
    private GuiTextField integrityEdit;
    private GuiTextField seedEdit;
    private GuiTextField dataEdit;
    private GuiButton doneButton;
    private GuiButton cancelButton;
    private GuiButton saveButton;
    private GuiButton loadButton;
    private GuiButton rotateZeroDegreesButton;
    private GuiButton rotateNinetyDegreesButton;
    private GuiButton rotate180DegreesButton;
    private GuiButton rotate270DegressButton;
    private GuiButton modeButton;
    private GuiButton detectSizeButton;
    private GuiButton showEntitiesButton;
    private GuiButton mirrorButton;
    private GuiButton showAirButton;
    private GuiButton showBoundingBoxButton;
    private final List<GuiTextField> field_189843_S = Lists.<GuiTextField>newArrayList();
    private final DecimalFormat decimalFormat = new DecimalFormat("0.0###");

    public GuiEditStructure(TileEntityStructure p_i47142_1_)
    {
        this.tileStructure = p_i47142_1_;
        this.decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
    }

    public void func_73876_c()
    {
        this.nameEdit.tick();
        this.posXEdit.tick();
        this.posYEdit.tick();
        this.posZEdit.tick();
        this.sizeXEdit.tick();
        this.sizeYEdit.tick();
        this.sizeZEdit.tick();
        this.integrityEdit.tick();
        this.seedEdit.tick();
        this.dataEdit.tick();
    }

    public void func_73866_w_()
    {
        Keyboard.enableRepeatEvents(true);
        this.field_146292_n.clear();
        this.doneButton = this.func_189646_b(new GuiButton(0, this.field_146294_l / 2 - 4 - 150, 210, 150, 20, I18n.format("gui.done")));
        this.cancelButton = this.func_189646_b(new GuiButton(1, this.field_146294_l / 2 + 4, 210, 150, 20, I18n.format("gui.cancel")));
        this.saveButton = this.func_189646_b(new GuiButton(9, this.field_146294_l / 2 + 4 + 100, 185, 50, 20, I18n.format("structure_block.button.save")));
        this.loadButton = this.func_189646_b(new GuiButton(10, this.field_146294_l / 2 + 4 + 100, 185, 50, 20, I18n.format("structure_block.button.load")));
        this.modeButton = this.func_189646_b(new GuiButton(18, this.field_146294_l / 2 - 4 - 150, 185, 50, 20, "MODE"));
        this.detectSizeButton = this.func_189646_b(new GuiButton(19, this.field_146294_l / 2 + 4 + 100, 120, 50, 20, I18n.format("structure_block.button.detect_size")));
        this.showEntitiesButton = this.func_189646_b(new GuiButton(20, this.field_146294_l / 2 + 4 + 100, 160, 50, 20, "ENTITIES"));
        this.mirrorButton = this.func_189646_b(new GuiButton(21, this.field_146294_l / 2 - 20, 185, 40, 20, "MIRROR"));
        this.showAirButton = this.func_189646_b(new GuiButton(22, this.field_146294_l / 2 + 4 + 100, 80, 50, 20, "SHOWAIR"));
        this.showBoundingBoxButton = this.func_189646_b(new GuiButton(23, this.field_146294_l / 2 + 4 + 100, 80, 50, 20, "SHOWBB"));
        this.rotateZeroDegreesButton = this.func_189646_b(new GuiButton(11, this.field_146294_l / 2 - 1 - 40 - 1 - 40 - 20, 185, 40, 20, "0"));
        this.rotateNinetyDegreesButton = this.func_189646_b(new GuiButton(12, this.field_146294_l / 2 - 1 - 40 - 20, 185, 40, 20, "90"));
        this.rotate180DegreesButton = this.func_189646_b(new GuiButton(13, this.field_146294_l / 2 + 1 + 20, 185, 40, 20, "180"));
        this.rotate270DegressButton = this.func_189646_b(new GuiButton(14, this.field_146294_l / 2 + 1 + 40 + 1 + 20, 185, 40, 20, "270"));
        this.nameEdit = new GuiTextField(2, this.field_146289_q, this.field_146294_l / 2 - 152, 40, 300, 20);
        this.nameEdit.setMaxStringLength(64);
        this.nameEdit.setText(this.tileStructure.getName());
        this.field_189843_S.add(this.nameEdit);
        BlockPos blockpos = this.tileStructure.getPosition();
        this.posXEdit = new GuiTextField(3, this.field_146289_q, this.field_146294_l / 2 - 152, 80, 80, 20);
        this.posXEdit.setMaxStringLength(15);
        this.posXEdit.setText(Integer.toString(blockpos.getX()));
        this.field_189843_S.add(this.posXEdit);
        this.posYEdit = new GuiTextField(4, this.field_146289_q, this.field_146294_l / 2 - 72, 80, 80, 20);
        this.posYEdit.setMaxStringLength(15);
        this.posYEdit.setText(Integer.toString(blockpos.getY()));
        this.field_189843_S.add(this.posYEdit);
        this.posZEdit = new GuiTextField(5, this.field_146289_q, this.field_146294_l / 2 + 8, 80, 80, 20);
        this.posZEdit.setMaxStringLength(15);
        this.posZEdit.setText(Integer.toString(blockpos.getZ()));
        this.field_189843_S.add(this.posZEdit);
        BlockPos blockpos1 = this.tileStructure.getStructureSize();
        this.sizeXEdit = new GuiTextField(6, this.field_146289_q, this.field_146294_l / 2 - 152, 120, 80, 20);
        this.sizeXEdit.setMaxStringLength(15);
        this.sizeXEdit.setText(Integer.toString(blockpos1.getX()));
        this.field_189843_S.add(this.sizeXEdit);
        this.sizeYEdit = new GuiTextField(7, this.field_146289_q, this.field_146294_l / 2 - 72, 120, 80, 20);
        this.sizeYEdit.setMaxStringLength(15);
        this.sizeYEdit.setText(Integer.toString(blockpos1.getY()));
        this.field_189843_S.add(this.sizeYEdit);
        this.sizeZEdit = new GuiTextField(8, this.field_146289_q, this.field_146294_l / 2 + 8, 120, 80, 20);
        this.sizeZEdit.setMaxStringLength(15);
        this.sizeZEdit.setText(Integer.toString(blockpos1.getZ()));
        this.field_189843_S.add(this.sizeZEdit);
        this.integrityEdit = new GuiTextField(15, this.field_146289_q, this.field_146294_l / 2 - 152, 120, 80, 20);
        this.integrityEdit.setMaxStringLength(15);
        this.integrityEdit.setText(this.decimalFormat.format((double)this.tileStructure.getIntegrity()));
        this.field_189843_S.add(this.integrityEdit);
        this.seedEdit = new GuiTextField(16, this.field_146289_q, this.field_146294_l / 2 - 72, 120, 80, 20);
        this.seedEdit.setMaxStringLength(31);
        this.seedEdit.setText(Long.toString(this.tileStructure.getSeed()));
        this.field_189843_S.add(this.seedEdit);
        this.dataEdit = new GuiTextField(17, this.field_146289_q, this.field_146294_l / 2 - 152, 120, 240, 20);
        this.dataEdit.setMaxStringLength(128);
        this.dataEdit.setText(this.tileStructure.getMetadata());
        this.field_189843_S.add(this.dataEdit);
        this.mirror = this.tileStructure.getMirror();
        this.updateMirrorButton();
        this.rotation = this.tileStructure.getRotation();
        this.updateDirectionButtons();
        this.mode = this.tileStructure.getMode();
        this.updateMode();
        this.ignoreEntities = this.tileStructure.ignoresEntities();
        this.updateEntitiesButton();
        this.showAir = this.tileStructure.showsAir();
        this.updateToggleAirButton();
        this.showBoundingBox = this.tileStructure.showsBoundingBox();
        this.updateToggleBoundingBox();
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
                this.tileStructure.setMirror(this.mirror);
                this.tileStructure.setRotation(this.rotation);
                this.tileStructure.setMode(this.mode);
                this.tileStructure.setIgnoresEntities(this.ignoreEntities);
                this.tileStructure.setShowAir(this.showAir);
                this.tileStructure.setShowBoundingBox(this.showBoundingBox);
                this.field_146297_k.displayGuiScreen((GuiScreen)null);
            }
            else if (p_146284_1_.field_146127_k == 0)
            {
                if (this.func_189820_b(1))
                {
                    this.field_146297_k.displayGuiScreen((GuiScreen)null);
                }
            }
            else if (p_146284_1_.field_146127_k == 9)
            {
                if (this.tileStructure.getMode() == TileEntityStructure.Mode.SAVE)
                {
                    this.func_189820_b(2);
                    this.field_146297_k.displayGuiScreen((GuiScreen)null);
                }
            }
            else if (p_146284_1_.field_146127_k == 10)
            {
                if (this.tileStructure.getMode() == TileEntityStructure.Mode.LOAD)
                {
                    this.func_189820_b(3);
                    this.field_146297_k.displayGuiScreen((GuiScreen)null);
                }
            }
            else if (p_146284_1_.field_146127_k == 11)
            {
                this.tileStructure.setRotation(Rotation.NONE);
                this.updateDirectionButtons();
            }
            else if (p_146284_1_.field_146127_k == 12)
            {
                this.tileStructure.setRotation(Rotation.CLOCKWISE_90);
                this.updateDirectionButtons();
            }
            else if (p_146284_1_.field_146127_k == 13)
            {
                this.tileStructure.setRotation(Rotation.CLOCKWISE_180);
                this.updateDirectionButtons();
            }
            else if (p_146284_1_.field_146127_k == 14)
            {
                this.tileStructure.setRotation(Rotation.COUNTERCLOCKWISE_90);
                this.updateDirectionButtons();
            }
            else if (p_146284_1_.field_146127_k == 18)
            {
                this.tileStructure.nextMode();
                this.updateMode();
            }
            else if (p_146284_1_.field_146127_k == 19)
            {
                if (this.tileStructure.getMode() == TileEntityStructure.Mode.SAVE)
                {
                    this.func_189820_b(4);
                    this.field_146297_k.displayGuiScreen((GuiScreen)null);
                }
            }
            else if (p_146284_1_.field_146127_k == 20)
            {
                this.tileStructure.setIgnoresEntities(!this.tileStructure.ignoresEntities());
                this.updateEntitiesButton();
            }
            else if (p_146284_1_.field_146127_k == 22)
            {
                this.tileStructure.setShowAir(!this.tileStructure.showsAir());
                this.updateToggleAirButton();
            }
            else if (p_146284_1_.field_146127_k == 23)
            {
                this.tileStructure.setShowBoundingBox(!this.tileStructure.showsBoundingBox());
                this.updateToggleBoundingBox();
            }
            else if (p_146284_1_.field_146127_k == 21)
            {
                switch (this.tileStructure.getMirror())
                {
                    case NONE:
                        this.tileStructure.setMirror(Mirror.LEFT_RIGHT);
                        break;

                    case LEFT_RIGHT:
                        this.tileStructure.setMirror(Mirror.FRONT_BACK);
                        break;

                    case FRONT_BACK:
                        this.tileStructure.setMirror(Mirror.NONE);
                }

                this.updateMirrorButton();
            }
        }
    }

    private void updateEntitiesButton()
    {
        boolean flag = !this.tileStructure.ignoresEntities();

        if (flag)
        {
            this.showEntitiesButton.field_146126_j = I18n.format("options.on");
        }
        else
        {
            this.showEntitiesButton.field_146126_j = I18n.format("options.off");
        }
    }

    private void updateToggleAirButton()
    {
        boolean flag = this.tileStructure.showsAir();

        if (flag)
        {
            this.showAirButton.field_146126_j = I18n.format("options.on");
        }
        else
        {
            this.showAirButton.field_146126_j = I18n.format("options.off");
        }
    }

    private void updateToggleBoundingBox()
    {
        boolean flag = this.tileStructure.showsBoundingBox();

        if (flag)
        {
            this.showBoundingBoxButton.field_146126_j = I18n.format("options.on");
        }
        else
        {
            this.showBoundingBoxButton.field_146126_j = I18n.format("options.off");
        }
    }

    private void updateMirrorButton()
    {
        Mirror mirror = this.tileStructure.getMirror();

        switch (mirror)
        {
            case NONE:
                this.mirrorButton.field_146126_j = "|";
                break;

            case LEFT_RIGHT:
                this.mirrorButton.field_146126_j = "< >";
                break;

            case FRONT_BACK:
                this.mirrorButton.field_146126_j = "^ v";
        }
    }

    private void updateDirectionButtons()
    {
        this.rotateZeroDegreesButton.field_146124_l = true;
        this.rotateNinetyDegreesButton.field_146124_l = true;
        this.rotate180DegreesButton.field_146124_l = true;
        this.rotate270DegressButton.field_146124_l = true;

        switch (this.tileStructure.getRotation())
        {
            case NONE:
                this.rotateZeroDegreesButton.field_146124_l = false;
                break;

            case CLOCKWISE_180:
                this.rotate180DegreesButton.field_146124_l = false;
                break;

            case COUNTERCLOCKWISE_90:
                this.rotate270DegressButton.field_146124_l = false;
                break;

            case CLOCKWISE_90:
                this.rotateNinetyDegreesButton.field_146124_l = false;
        }
    }

    private void updateMode()
    {
        this.nameEdit.setFocused2(false);
        this.posXEdit.setFocused2(false);
        this.posYEdit.setFocused2(false);
        this.posZEdit.setFocused2(false);
        this.sizeXEdit.setFocused2(false);
        this.sizeYEdit.setFocused2(false);
        this.sizeZEdit.setFocused2(false);
        this.integrityEdit.setFocused2(false);
        this.seedEdit.setFocused2(false);
        this.dataEdit.setFocused2(false);
        this.nameEdit.setVisible(false);
        this.nameEdit.setFocused2(false);
        this.posXEdit.setVisible(false);
        this.posYEdit.setVisible(false);
        this.posZEdit.setVisible(false);
        this.sizeXEdit.setVisible(false);
        this.sizeYEdit.setVisible(false);
        this.sizeZEdit.setVisible(false);
        this.integrityEdit.setVisible(false);
        this.seedEdit.setVisible(false);
        this.dataEdit.setVisible(false);
        this.saveButton.field_146125_m = false;
        this.loadButton.field_146125_m = false;
        this.detectSizeButton.field_146125_m = false;
        this.showEntitiesButton.field_146125_m = false;
        this.mirrorButton.field_146125_m = false;
        this.rotateZeroDegreesButton.field_146125_m = false;
        this.rotateNinetyDegreesButton.field_146125_m = false;
        this.rotate180DegreesButton.field_146125_m = false;
        this.rotate270DegressButton.field_146125_m = false;
        this.showAirButton.field_146125_m = false;
        this.showBoundingBoxButton.field_146125_m = false;

        switch (this.tileStructure.getMode())
        {
            case SAVE:
                this.nameEdit.setVisible(true);
                this.nameEdit.setFocused2(true);
                this.posXEdit.setVisible(true);
                this.posYEdit.setVisible(true);
                this.posZEdit.setVisible(true);
                this.sizeXEdit.setVisible(true);
                this.sizeYEdit.setVisible(true);
                this.sizeZEdit.setVisible(true);
                this.saveButton.field_146125_m = true;
                this.detectSizeButton.field_146125_m = true;
                this.showEntitiesButton.field_146125_m = true;
                this.showAirButton.field_146125_m = true;
                break;

            case LOAD:
                this.nameEdit.setVisible(true);
                this.nameEdit.setFocused2(true);
                this.posXEdit.setVisible(true);
                this.posYEdit.setVisible(true);
                this.posZEdit.setVisible(true);
                this.integrityEdit.setVisible(true);
                this.seedEdit.setVisible(true);
                this.loadButton.field_146125_m = true;
                this.showEntitiesButton.field_146125_m = true;
                this.mirrorButton.field_146125_m = true;
                this.rotateZeroDegreesButton.field_146125_m = true;
                this.rotateNinetyDegreesButton.field_146125_m = true;
                this.rotate180DegreesButton.field_146125_m = true;
                this.rotate270DegressButton.field_146125_m = true;
                this.showBoundingBoxButton.field_146125_m = true;
                this.updateDirectionButtons();
                break;

            case CORNER:
                this.nameEdit.setVisible(true);
                this.nameEdit.setFocused2(true);
                break;

            case DATA:
                this.dataEdit.setVisible(true);
                this.dataEdit.setFocused2(true);
        }

        this.modeButton.field_146126_j = I18n.format("structure_block.mode." + this.tileStructure.getMode().getName());
    }

    private boolean func_189820_b(int p_189820_1_)
    {
        try
        {
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            this.tileStructure.func_189705_a(packetbuffer);
            packetbuffer.writeByte(p_189820_1_);
            packetbuffer.writeString(this.tileStructure.getMode().toString());
            packetbuffer.writeString(this.nameEdit.getText());
            packetbuffer.writeInt(this.parseCoordinate(this.posXEdit.getText()));
            packetbuffer.writeInt(this.parseCoordinate(this.posYEdit.getText()));
            packetbuffer.writeInt(this.parseCoordinate(this.posZEdit.getText()));
            packetbuffer.writeInt(this.parseCoordinate(this.sizeXEdit.getText()));
            packetbuffer.writeInt(this.parseCoordinate(this.sizeYEdit.getText()));
            packetbuffer.writeInt(this.parseCoordinate(this.sizeZEdit.getText()));
            packetbuffer.writeString(this.tileStructure.getMirror().toString());
            packetbuffer.writeString(this.tileStructure.getRotation().toString());
            packetbuffer.writeString(this.dataEdit.getText());
            packetbuffer.writeBoolean(this.tileStructure.ignoresEntities());
            packetbuffer.writeBoolean(this.tileStructure.showsAir());
            packetbuffer.writeBoolean(this.tileStructure.showsBoundingBox());
            packetbuffer.writeFloat(this.parseIntegrity(this.integrityEdit.getText()));
            packetbuffer.writeVarLong(this.parseSeed(this.seedEdit.getText()));
            this.field_146297_k.getConnection().sendPacket(new CPacketCustomPayload("MC|Struct", packetbuffer));
            return true;
        }
        catch (Exception exception)
        {
            field_189845_a.warn("Could not send structure block info", (Throwable)exception);
            return false;
        }
    }

    private long parseSeed(String p_189821_1_)
    {
        try
        {
            return Long.valueOf(p_189821_1_).longValue();
        }
        catch (NumberFormatException var3)
        {
            return 0L;
        }
    }

    private float parseIntegrity(String p_189819_1_)
    {
        try
        {
            return Float.valueOf(p_189819_1_).floatValue();
        }
        catch (NumberFormatException var3)
        {
            return 1.0F;
        }
    }

    private int parseCoordinate(String p_189817_1_)
    {
        try
        {
            return Integer.parseInt(p_189817_1_);
        }
        catch (NumberFormatException var3)
        {
            return 0;
        }
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (this.nameEdit.getVisible() && func_190301_b(p_73869_1_, p_73869_2_))
        {
            this.nameEdit.func_146201_a(p_73869_1_, p_73869_2_);
        }

        if (this.posXEdit.getVisible())
        {
            this.posXEdit.func_146201_a(p_73869_1_, p_73869_2_);
        }

        if (this.posYEdit.getVisible())
        {
            this.posYEdit.func_146201_a(p_73869_1_, p_73869_2_);
        }

        if (this.posZEdit.getVisible())
        {
            this.posZEdit.func_146201_a(p_73869_1_, p_73869_2_);
        }

        if (this.sizeXEdit.getVisible())
        {
            this.sizeXEdit.func_146201_a(p_73869_1_, p_73869_2_);
        }

        if (this.sizeYEdit.getVisible())
        {
            this.sizeYEdit.func_146201_a(p_73869_1_, p_73869_2_);
        }

        if (this.sizeZEdit.getVisible())
        {
            this.sizeZEdit.func_146201_a(p_73869_1_, p_73869_2_);
        }

        if (this.integrityEdit.getVisible())
        {
            this.integrityEdit.func_146201_a(p_73869_1_, p_73869_2_);
        }

        if (this.seedEdit.getVisible())
        {
            this.seedEdit.func_146201_a(p_73869_1_, p_73869_2_);
        }

        if (this.dataEdit.getVisible())
        {
            this.dataEdit.func_146201_a(p_73869_1_, p_73869_2_);
        }

        if (p_73869_2_ == 15)
        {
            GuiTextField guitextfield = null;
            GuiTextField guitextfield1 = null;

            for (GuiTextField guitextfield2 : this.field_189843_S)
            {
                if (guitextfield != null && guitextfield2.getVisible())
                {
                    guitextfield1 = guitextfield2;
                    break;
                }

                if (guitextfield2.func_146206_l() && guitextfield2.getVisible())
                {
                    guitextfield = guitextfield2;
                }
            }

            if (guitextfield != null && guitextfield1 == null)
            {
                for (GuiTextField guitextfield3 : this.field_189843_S)
                {
                    if (guitextfield3.getVisible() && guitextfield3 != guitextfield)
                    {
                        guitextfield1 = guitextfield3;
                        break;
                    }
                }
            }

            if (guitextfield1 != null && guitextfield1 != guitextfield)
            {
                guitextfield.setFocused2(false);
                guitextfield1.setFocused2(true);
            }
        }

        if (p_73869_2_ != 28 && p_73869_2_ != 156)
        {
            if (p_73869_2_ == 1)
            {
                this.func_146284_a(this.cancelButton);
            }
        }
        else
        {
            this.func_146284_a(this.doneButton);
        }
    }

    private static boolean func_190301_b(char p_190301_0_, int p_190301_1_)
    {
        boolean flag = true;

        for (int i : field_190302_a)
        {
            if (i == p_190301_1_)
            {
                return true;
            }
        }

        for (char c0 : ChatAllowedCharacters.field_189861_b)
        {
            if (c0 == p_190301_0_)
            {
                flag = false;
                break;
            }
        }

        return flag;
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);

        if (this.nameEdit.getVisible())
        {
            this.nameEdit.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
        }

        if (this.posXEdit.getVisible())
        {
            this.posXEdit.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
        }

        if (this.posYEdit.getVisible())
        {
            this.posYEdit.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
        }

        if (this.posZEdit.getVisible())
        {
            this.posZEdit.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
        }

        if (this.sizeXEdit.getVisible())
        {
            this.sizeXEdit.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
        }

        if (this.sizeYEdit.getVisible())
        {
            this.sizeYEdit.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
        }

        if (this.sizeZEdit.getVisible())
        {
            this.sizeZEdit.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
        }

        if (this.integrityEdit.getVisible())
        {
            this.integrityEdit.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
        }

        if (this.seedEdit.getVisible())
        {
            this.seedEdit.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
        }

        if (this.dataEdit.getVisible())
        {
            this.dataEdit.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        TileEntityStructure.Mode tileentitystructure$mode = this.tileStructure.getMode();
        this.func_73732_a(this.field_146289_q, I18n.format("tile.structureBlock.name"), this.field_146294_l / 2, 10, 16777215);

        if (tileentitystructure$mode != TileEntityStructure.Mode.DATA)
        {
            this.func_73731_b(this.field_146289_q, I18n.format("structure_block.structure_name"), this.field_146294_l / 2 - 153, 30, 10526880);
            this.nameEdit.func_146194_f();
        }

        if (tileentitystructure$mode == TileEntityStructure.Mode.LOAD || tileentitystructure$mode == TileEntityStructure.Mode.SAVE)
        {
            this.func_73731_b(this.field_146289_q, I18n.format("structure_block.position"), this.field_146294_l / 2 - 153, 70, 10526880);
            this.posXEdit.func_146194_f();
            this.posYEdit.func_146194_f();
            this.posZEdit.func_146194_f();
            String s = I18n.format("structure_block.include_entities");
            int i = this.field_146289_q.getStringWidth(s);
            this.func_73731_b(this.field_146289_q, s, this.field_146294_l / 2 + 154 - i, 150, 10526880);
        }

        if (tileentitystructure$mode == TileEntityStructure.Mode.SAVE)
        {
            this.func_73731_b(this.field_146289_q, I18n.format("structure_block.size"), this.field_146294_l / 2 - 153, 110, 10526880);
            this.sizeXEdit.func_146194_f();
            this.sizeYEdit.func_146194_f();
            this.sizeZEdit.func_146194_f();
            String s2 = I18n.format("structure_block.detect_size");
            int k = this.field_146289_q.getStringWidth(s2);
            this.func_73731_b(this.field_146289_q, s2, this.field_146294_l / 2 + 154 - k, 110, 10526880);
            String s1 = I18n.format("structure_block.show_air");
            int j = this.field_146289_q.getStringWidth(s1);
            this.func_73731_b(this.field_146289_q, s1, this.field_146294_l / 2 + 154 - j, 70, 10526880);
        }

        if (tileentitystructure$mode == TileEntityStructure.Mode.LOAD)
        {
            this.func_73731_b(this.field_146289_q, I18n.format("structure_block.integrity"), this.field_146294_l / 2 - 153, 110, 10526880);
            this.integrityEdit.func_146194_f();
            this.seedEdit.func_146194_f();
            String s3 = I18n.format("structure_block.show_boundingbox");
            int l = this.field_146289_q.getStringWidth(s3);
            this.func_73731_b(this.field_146289_q, s3, this.field_146294_l / 2 + 154 - l, 70, 10526880);
        }

        if (tileentitystructure$mode == TileEntityStructure.Mode.DATA)
        {
            this.func_73731_b(this.field_146289_q, I18n.format("structure_block.custom_data"), this.field_146294_l / 2 - 153, 110, 10526880);
            this.dataEdit.func_146194_f();
        }

        String s4 = "structure_block.mode_info." + tileentitystructure$mode.getName();
        this.func_73731_b(this.field_146289_q, I18n.format(s4), this.field_146294_l / 2 - 153, 174, 10526880);
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    public boolean func_73868_f()
    {
        return false;
    }
}

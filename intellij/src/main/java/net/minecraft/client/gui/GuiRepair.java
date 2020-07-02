package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class GuiRepair extends GuiContainer implements IContainerListener
{
    private static final ResourceLocation ANVIL_RESOURCE = new ResourceLocation("textures/gui/container/anvil.png");
    private final ContainerRepair field_147092_v;
    private GuiTextField nameField;
    private final InventoryPlayer field_147094_x;

    public GuiRepair(InventoryPlayer p_i45508_1_, World p_i45508_2_)
    {
        super(new ContainerRepair(p_i45508_1_, p_i45508_2_, Minecraft.getInstance().player));
        this.field_147094_x = p_i45508_1_;
        this.field_147092_v = (ContainerRepair)this.container;
    }

    public void func_73866_w_()
    {
        super.func_73866_w_();
        Keyboard.enableRepeatEvents(true);
        int i = (this.field_146294_l - this.xSize) / 2;
        int j = (this.field_146295_m - this.ySize) / 2;
        this.nameField = new GuiTextField(0, this.field_146289_q, i + 62, j + 24, 103, 12);
        this.nameField.setTextColor(-1);
        this.nameField.setDisabledTextColour(-1);
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(35);
        this.container.removeListener(this);
        this.container.addListener(this);
    }

    public void func_146281_b()
    {
        super.func_146281_b();
        Keyboard.enableRepeatEvents(false);
        this.container.removeListener(this);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GlStateManager.func_179140_f();
        GlStateManager.func_179084_k();
        this.field_146289_q.func_78276_b(I18n.format("container.repair"), 60, 6, 4210752);

        if (this.field_147092_v.maximumCost > 0)
        {
            int i = 8453920;
            boolean flag = true;
            String s = I18n.format("container.repair.cost", this.field_147092_v.maximumCost);

            if (this.field_147092_v.maximumCost >= 40 && !this.field_146297_k.player.abilities.isCreativeMode)
            {
                s = I18n.format("container.repair.expensive");
                i = 16736352;
            }
            else if (!this.field_147092_v.getSlot(2).getHasStack())
            {
                flag = false;
            }
            else if (!this.field_147092_v.getSlot(2).canTakeStack(this.field_147094_x.player))
            {
                i = 16736352;
            }

            if (flag)
            {
                int j = -16777216 | (i & 16579836) >> 2 | i & -16777216;
                int k = this.xSize - 8 - this.field_146289_q.getStringWidth(s);
                int l = 67;

                if (this.field_146289_q.func_82883_a())
                {
                    func_73734_a(k - 3, 65, this.xSize - 7, 77, -16777216);
                    func_73734_a(k - 2, 66, this.xSize - 8, 76, -12895429);
                }
                else
                {
                    this.field_146289_q.func_78276_b(s, k, 68, j);
                    this.field_146289_q.func_78276_b(s, k + 1, 67, j);
                    this.field_146289_q.func_78276_b(s, k + 1, 68, j);
                }

                this.field_146289_q.func_78276_b(s, k, 67, i);
            }
        }

        GlStateManager.func_179145_e();
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (this.nameField.func_146201_a(p_73869_1_, p_73869_2_))
        {
            this.func_147090_g();
        }
        else
        {
            super.func_73869_a(p_73869_1_, p_73869_2_);
        }
    }

    private void func_147090_g()
    {
        String s = this.nameField.getText();
        Slot slot = this.field_147092_v.getSlot(0);

        if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && s.equals(slot.getStack().func_82833_r()))
        {
            s = "";
        }

        this.field_147092_v.updateItemName(s);
        this.field_146297_k.player.connection.sendPacket(new CPacketCustomPayload("MC|ItemName", (new PacketBuffer(Unpooled.buffer())).writeString(s)));
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
        this.nameField.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.renderHoveredToolTip(p_73863_1_, p_73863_2_);
        GlStateManager.func_179140_f();
        GlStateManager.func_179084_k();
        this.nameField.func_146194_f();
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_146297_k.getTextureManager().bindTexture(ANVIL_RESOURCE);
        int i = (this.field_146294_l - this.xSize) / 2;
        int j = (this.field_146295_m - this.ySize) / 2;
        this.func_73729_b(i, j, 0, 0, this.xSize, this.ySize);
        this.func_73729_b(i + 59, j + 20, 0, this.ySize + (this.field_147092_v.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

        if ((this.field_147092_v.getSlot(0).getHasStack() || this.field_147092_v.getSlot(1).getHasStack()) && !this.field_147092_v.getSlot(2).getHasStack())
        {
            this.func_73729_b(i + 99, j + 45, this.xSize, 0, 28, 21);
        }
    }

    /**
     * update the crafting window inventory with the items in the list
     */
    public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList)
    {
        this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
    }

    /**
     * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
     * contents of that slot.
     */
    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
    {
        if (slotInd == 0)
        {
            this.nameField.setText(stack.isEmpty() ? "" : stack.func_82833_r());
            this.nameField.setEnabled(!stack.isEmpty());

            if (!stack.isEmpty())
            {
                this.func_147090_g();
            }
        }
    }

    /**
     * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
     * and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
     * value. Both are truncated to shorts in non-local SMP.
     */
    public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue)
    {
    }

    public void func_175173_a(Container p_175173_1_, IInventory p_175173_2_)
    {
    }
}

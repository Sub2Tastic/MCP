package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiChest extends GuiContainer
{
    /** The ResourceLocation containing the chest GUI texture. */
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private final IInventory field_147016_v;
    private final IInventory field_147015_w;

    /**
     * Window height is calculated with these values; the more rows, the higher
     */
    private final int inventoryRows;

    public GuiChest(IInventory p_i46315_1_, IInventory p_i46315_2_)
    {
        super(new ContainerChest(p_i46315_1_, p_i46315_2_, Minecraft.getInstance().player));
        this.field_147016_v = p_i46315_1_;
        this.field_147015_w = p_i46315_2_;
        this.field_146291_p = false;
        int i = 222;
        int j = 114;
        this.inventoryRows = p_i46315_2_.getSizeInventory() / 9;
        this.ySize = 114 + this.inventoryRows * 18;
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.renderHoveredToolTip(p_73863_1_, p_73863_2_);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.field_146289_q.func_78276_b(this.field_147015_w.getDisplayName().func_150260_c(), 8, 6, 4210752);
        this.field_146289_q.func_78276_b(this.field_147016_v.getDisplayName().func_150260_c(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_146297_k.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.field_146294_l - this.xSize) / 2;
        int j = (this.field_146295_m - this.ySize) / 2;
        this.func_73729_b(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.func_73729_b(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}

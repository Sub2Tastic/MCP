package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;

public class GuiFurnace extends GuiContainer
{
    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/furnace.png");
    private final InventoryPlayer field_175383_v;
    private final IInventory field_147086_v;

    public GuiFurnace(InventoryPlayer p_i45501_1_, IInventory p_i45501_2_)
    {
        super(new ContainerFurnace(p_i45501_1_, p_i45501_2_));
        this.field_175383_v = p_i45501_1_;
        this.field_147086_v = p_i45501_2_;
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
        String s = this.field_147086_v.getDisplayName().func_150260_c();
        this.field_146289_q.func_78276_b(s, this.xSize / 2 - this.field_146289_q.getStringWidth(s) / 2, 6, 4210752);
        this.field_146289_q.func_78276_b(this.field_175383_v.getDisplayName().func_150260_c(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_146297_k.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
        int i = (this.field_146294_l - this.xSize) / 2;
        int j = (this.field_146295_m - this.ySize) / 2;
        this.func_73729_b(i, j, 0, 0, this.xSize, this.ySize);

        if (TileEntityFurnace.func_174903_a(this.field_147086_v))
        {
            int k = this.func_175382_i(13);
            this.func_73729_b(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = this.func_175381_h(24);
        this.func_73729_b(i + 79, j + 34, 176, 14, l + 1, 16);
    }

    private int func_175381_h(int p_175381_1_)
    {
        int i = this.field_147086_v.func_174887_a_(2);
        int j = this.field_147086_v.func_174887_a_(3);
        return j != 0 && i != 0 ? i * p_175381_1_ / j : 0;
    }

    private int func_175382_i(int p_175382_1_)
    {
        int i = this.field_147086_v.func_174887_a_(1);

        if (i == 0)
        {
            i = 200;
        }

        return this.field_147086_v.func_174887_a_(0) * p_175382_1_ / i;
    }
}

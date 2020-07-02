package net.minecraft.client.gui.inventory;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiCrafting extends GuiContainer implements IRecipeShownListener
{
    private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/crafting_table.png");
    private GuiButtonImage field_192049_w;
    private final GuiRecipeBook recipeBookGui;
    private boolean widthTooNarrow;

    public GuiCrafting(InventoryPlayer p_i45504_1_, World p_i45504_2_)
    {
        this(p_i45504_1_, p_i45504_2_, BlockPos.ZERO);
    }

    public GuiCrafting(InventoryPlayer p_i45505_1_, World p_i45505_2_, BlockPos p_i45505_3_)
    {
        super(new ContainerWorkbench(p_i45505_1_, p_i45505_2_, p_i45505_3_));
        this.recipeBookGui = new GuiRecipeBook();
    }

    public void func_73866_w_()
    {
        super.func_73866_w_();
        this.widthTooNarrow = this.field_146294_l < 379;
        this.recipeBookGui.func_194303_a(this.field_146294_l, this.field_146295_m, this.field_146297_k, this.widthTooNarrow, ((ContainerWorkbench)this.container).craftMatrix);
        this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.field_146294_l, this.xSize);
        this.field_192049_w = new GuiButtonImage(10, this.guiLeft + 5, this.field_146295_m / 2 - 49, 20, 18, 0, 168, 19, CRAFTING_TABLE_GUI_TEXTURES);
        this.field_146292_n.add(this.field_192049_w);
    }

    public void func_73876_c()
    {
        super.func_73876_c();
        this.recipeBookGui.tick();
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();

        if (this.recipeBookGui.isVisible() && this.widthTooNarrow)
        {
            this.drawGuiContainerBackgroundLayer(p_73863_3_, p_73863_1_, p_73863_2_);
            this.recipeBookGui.func_191861_a(p_73863_1_, p_73863_2_, p_73863_3_);
        }
        else
        {
            this.recipeBookGui.func_191861_a(p_73863_1_, p_73863_2_, p_73863_3_);
            super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
            this.recipeBookGui.renderGhostRecipe(this.guiLeft, this.guiTop, true, p_73863_3_);
        }

        this.renderHoveredToolTip(p_73863_1_, p_73863_2_);
        this.recipeBookGui.renderTooltip(this.guiLeft, this.guiTop, p_73863_1_, p_73863_2_);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.field_146289_q.func_78276_b(I18n.format("container.crafting"), 28, 6, 4210752);
        this.field_146289_q.func_78276_b(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_146297_k.getTextureManager().bindTexture(CRAFTING_TABLE_GUI_TEXTURES);
        int i = this.guiLeft;
        int j = (this.field_146295_m - this.ySize) / 2;
        this.func_73729_b(i, j, 0, 0, this.xSize, this.ySize);
    }

    protected boolean func_146978_c(int p_146978_1_, int p_146978_2_, int p_146978_3_, int p_146978_4_, int p_146978_5_, int p_146978_6_)
    {
        return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.func_146978_c(p_146978_1_, p_146978_2_, p_146978_3_, p_146978_4_, p_146978_5_, p_146978_6_);
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        if (!this.recipeBookGui.func_191862_a(p_73864_1_, p_73864_2_, p_73864_3_))
        {
            if (!this.widthTooNarrow || !this.recipeBookGui.isVisible())
            {
                super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
            }
        }
    }

    protected boolean func_193983_c(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_)
    {
        boolean flag = p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
        return this.recipeBookGui.func_193955_c(p_193983_1_, p_193983_2_, this.guiLeft, this.guiTop, this.xSize, this.ySize) && flag;
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146127_k == 10)
        {
            this.recipeBookGui.func_193014_a(this.widthTooNarrow, ((ContainerWorkbench)this.container).craftMatrix);
            this.recipeBookGui.toggleVisibility();
            this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.field_146294_l, this.xSize);
            this.field_192049_w.setPosition(this.guiLeft + 5, this.field_146295_m / 2 - 49);
        }
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (!this.recipeBookGui.func_191859_a(p_73869_1_, p_73869_2_))
        {
            super.func_73869_a(p_73869_1_, p_73869_2_);
        }
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type)
    {
        super.handleMouseClick(slotIn, slotId, mouseButton, type);
        this.recipeBookGui.slotClicked(slotIn);
    }

    public void recipesUpdated()
    {
        this.recipeBookGui.recipesUpdated();
    }

    public void func_146281_b()
    {
        this.recipeBookGui.removed();
        super.func_146281_b();
    }

    public GuiRecipeBook getRecipeGui()
    {
        return this.recipeBookGui;
    }
}

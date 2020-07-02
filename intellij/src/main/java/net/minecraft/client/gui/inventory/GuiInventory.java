package net.minecraft.client.gui.inventory;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;

public class GuiInventory extends InventoryEffectRenderer implements IRecipeShownListener
{
    /** The old x position of the mouse pointer */
    private float oldMouseX;

    /** The old y position of the mouse pointer */
    private float oldMouseY;
    private GuiButtonImage field_192048_z;
    private final GuiRecipeBook recipeBookGui = new GuiRecipeBook();
    private boolean widthTooNarrow;
    private boolean buttonClicked;

    public GuiInventory(EntityPlayer player)
    {
        super(player.container);
        this.field_146291_p = true;
    }

    public void func_73876_c()
    {
        if (this.field_146297_k.playerController.isInCreativeMode())
        {
            this.field_146297_k.displayGuiScreen(new GuiContainerCreative(this.field_146297_k.player));
        }

        this.recipeBookGui.tick();
    }

    public void func_73866_w_()
    {
        this.field_146292_n.clear();

        if (this.field_146297_k.playerController.isInCreativeMode())
        {
            this.field_146297_k.displayGuiScreen(new GuiContainerCreative(this.field_146297_k.player));
        }
        else
        {
            super.func_73866_w_();
        }

        this.widthTooNarrow = this.field_146294_l < 379;
        this.recipeBookGui.func_194303_a(this.field_146294_l, this.field_146295_m, this.field_146297_k, this.widthTooNarrow, ((ContainerPlayer)this.container).craftMatrix);
        this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.field_146294_l, this.xSize);
        this.field_192048_z = new GuiButtonImage(10, this.guiLeft + 104, this.field_146295_m / 2 - 22, 20, 18, 178, 0, 19, INVENTORY_BACKGROUND);
        this.field_146292_n.add(this.field_192048_z);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.field_146289_q.func_78276_b(I18n.format("container.crafting"), 97, 8, 4210752);
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.hasActivePotionEffects = !this.recipeBookGui.isVisible();

        if (this.recipeBookGui.isVisible() && this.widthTooNarrow)
        {
            this.drawGuiContainerBackgroundLayer(p_73863_3_, p_73863_1_, p_73863_2_);
            this.recipeBookGui.func_191861_a(p_73863_1_, p_73863_2_, p_73863_3_);
        }
        else
        {
            this.recipeBookGui.func_191861_a(p_73863_1_, p_73863_2_, p_73863_3_);
            super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
            this.recipeBookGui.renderGhostRecipe(this.guiLeft, this.guiTop, false, p_73863_3_);
        }

        this.renderHoveredToolTip(p_73863_1_, p_73863_2_);
        this.recipeBookGui.renderTooltip(this.guiLeft, this.guiTop, p_73863_1_, p_73863_2_);
        this.oldMouseX = (float)p_73863_1_;
        this.oldMouseY = (float)p_73863_2_;
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_146297_k.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.func_73729_b(i, j, 0, 0, this.xSize, this.ySize);
        func_147046_a(i + 51, j + 75, 30, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, this.field_146297_k.player);
    }

    public static void func_147046_a(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_, float p_147046_4_, EntityLivingBase p_147046_5_)
    {
        GlStateManager.func_179142_g();
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)p_147046_0_, (float)p_147046_1_, 50.0F);
        GlStateManager.func_179152_a((float)(-p_147046_2_), (float)p_147046_2_, (float)p_147046_2_);
        GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
        float f = p_147046_5_.renderYawOffset;
        float f1 = p_147046_5_.rotationYaw;
        float f2 = p_147046_5_.rotationPitch;
        float f3 = p_147046_5_.prevRotationYawHead;
        float f4 = p_147046_5_.rotationYawHead;
        GlStateManager.func_179114_b(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.func_74519_b();
        GlStateManager.func_179114_b(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.func_179114_b(-((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        p_147046_5_.renderYawOffset = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 20.0F;
        p_147046_5_.rotationYaw = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 40.0F;
        p_147046_5_.rotationPitch = -((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F;
        p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
        p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
        GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getInstance().getRenderManager();
        rendermanager.func_178631_a(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.func_188391_a(p_147046_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        p_147046_5_.renderYawOffset = f;
        p_147046_5_.rotationYaw = f1;
        p_147046_5_.rotationPitch = f2;
        p_147046_5_.prevRotationYawHead = f3;
        p_147046_5_.rotationYawHead = f4;
        GlStateManager.func_179121_F();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
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

    protected void func_146286_b(int p_146286_1_, int p_146286_2_, int p_146286_3_)
    {
        if (this.buttonClicked)
        {
            this.buttonClicked = false;
        }
        else
        {
            super.func_146286_b(p_146286_1_, p_146286_2_, p_146286_3_);
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
            this.recipeBookGui.func_193014_a(this.widthTooNarrow, ((ContainerPlayer)this.container).craftMatrix);
            this.recipeBookGui.toggleVisibility();
            this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.field_146294_l, this.xSize);
            this.field_192048_z.setPosition(this.guiLeft + 104, this.field_146295_m / 2 - 22);
            this.buttonClicked = true;
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

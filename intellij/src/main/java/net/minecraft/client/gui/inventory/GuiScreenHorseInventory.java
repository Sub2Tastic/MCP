package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiScreenHorseInventory extends GuiContainer
{
    private static final ResourceLocation HORSE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/horse.png");
    private final IInventory field_147030_v;
    private final IInventory field_147029_w;

    /** The EntityHorse whose inventory is currently being accessed. */
    private final AbstractHorse horseEntity;

    /** The mouse x-position recorded during the last rendered frame. */
    private float mousePosx;

    /** The mouse y-position recorded during the last renderered frame. */
    private float mousePosY;

    public GuiScreenHorseInventory(IInventory p_i1093_1_, IInventory p_i1093_2_, AbstractHorse p_i1093_3_)
    {
        super(new ContainerHorseInventory(p_i1093_1_, p_i1093_2_, p_i1093_3_, Minecraft.getInstance().player));
        this.field_147030_v = p_i1093_1_;
        this.field_147029_w = p_i1093_2_;
        this.horseEntity = p_i1093_3_;
        this.field_146291_p = false;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.field_146289_q.func_78276_b(this.field_147029_w.getDisplayName().func_150260_c(), 8, 6, 4210752);
        this.field_146289_q.func_78276_b(this.field_147030_v.getDisplayName().func_150260_c(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_146297_k.getTextureManager().bindTexture(HORSE_GUI_TEXTURES);
        int i = (this.field_146294_l - this.xSize) / 2;
        int j = (this.field_146295_m - this.ySize) / 2;
        this.func_73729_b(i, j, 0, 0, this.xSize, this.ySize);

        if (this.horseEntity instanceof AbstractChestHorse)
        {
            AbstractChestHorse abstractchesthorse = (AbstractChestHorse)this.horseEntity;

            if (abstractchesthorse.hasChest())
            {
                this.func_73729_b(i + 79, j + 17, 0, this.ySize, abstractchesthorse.getInventoryColumns() * 18, 54);
            }
        }

        if (this.horseEntity.canBeSaddled())
        {
            this.func_73729_b(i + 7, j + 35 - 18, 18, this.ySize + 54, 18, 18);
        }

        if (this.horseEntity.wearsArmor())
        {
            if (this.horseEntity instanceof EntityLlama)
            {
                this.func_73729_b(i + 7, j + 35, 36, this.ySize + 54, 18, 18);
            }
            else
            {
                this.func_73729_b(i + 7, j + 35, 0, this.ySize + 54, 18, 18);
            }
        }

        GuiInventory.func_147046_a(i + 51, j + 60, 17, (float)(i + 51) - this.mousePosx, (float)(j + 75 - 50) - this.mousePosY, this.horseEntity);
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.mousePosx = (float)p_73863_1_;
        this.mousePosY = (float)p_73863_2_;
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.renderHoveredToolTip(p_73863_1_, p_73863_2_);
    }
}

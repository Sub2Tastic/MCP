package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import org.lwjgl.util.glu.Project;

public class GuiEnchantment extends GuiContainer
{
    /** The ResourceLocation containing the Enchantment GUI texture location */
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");

    /**
     * The ResourceLocation containing the texture for the Book rendered above the enchantment table
     */
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation("textures/entity/enchanting_table_book.png");

    /**
     * The ModelBook instance used for rendering the book on the Enchantment table
     */
    private static final ModelBook MODEL_BOOK = new ModelBook();
    private final InventoryPlayer field_175379_F;

    /** A Random instance for use with the enchantment gui */
    private final Random random = new Random();
    private final ContainerEnchantment field_147075_G;
    public int ticks;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last = ItemStack.EMPTY;
    private final IWorldNameable field_175380_I;

    public GuiEnchantment(InventoryPlayer p_i45502_1_, World p_i45502_2_, IWorldNameable p_i45502_3_)
    {
        super(new ContainerEnchantment(p_i45502_1_, p_i45502_2_));
        this.field_175379_F = p_i45502_1_;
        this.field_147075_G = (ContainerEnchantment)this.container;
        this.field_175380_I = p_i45502_3_;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.field_146289_q.func_78276_b(this.field_175380_I.getDisplayName().func_150260_c(), 12, 5, 4210752);
        this.field_146289_q.func_78276_b(this.field_175379_F.getDisplayName().func_150260_c(), 8, this.ySize - 96 + 2, 4210752);
    }

    public void func_73876_c()
    {
        super.func_73876_c();
        this.tickBook();
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
        int i = (this.field_146294_l - this.xSize) / 2;
        int j = (this.field_146295_m - this.ySize) / 2;

        for (int k = 0; k < 3; ++k)
        {
            int l = p_73864_1_ - (i + 60);
            int i1 = p_73864_2_ - (j + 14 + 19 * k);

            if (l >= 0 && i1 >= 0 && l < 108 && i1 < 19 && this.field_147075_G.enchantItem(this.field_146297_k.player, k))
            {
                this.field_146297_k.playerController.sendEnchantPacket(this.field_147075_G.windowId, k);
            }
        }
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_146297_k.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
        int i = (this.field_146294_l - this.xSize) / 2;
        int j = (this.field_146295_m - this.ySize) / 2;
        this.func_73729_b(i, j, 0, 0, this.xSize, this.ySize);
        GlStateManager.func_179094_E();
        GlStateManager.func_179128_n(5889);
        GlStateManager.func_179094_E();
        GlStateManager.func_179096_D();
        ScaledResolution scaledresolution = new ScaledResolution(this.field_146297_k);
        GlStateManager.func_179083_b((scaledresolution.func_78326_a() - 320) / 2 * scaledresolution.func_78325_e(), (scaledresolution.func_78328_b() - 240) / 2 * scaledresolution.func_78325_e(), 320 * scaledresolution.func_78325_e(), 240 * scaledresolution.func_78325_e());
        GlStateManager.func_179109_b(-0.34F, 0.23F, 0.0F);
        Project.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
        float f = 1.0F;
        GlStateManager.func_179128_n(5888);
        GlStateManager.func_179096_D();
        RenderHelper.func_74519_b();
        GlStateManager.func_179109_b(0.0F, 3.3F, -16.0F);
        GlStateManager.func_179152_a(1.0F, 1.0F, 1.0F);
        float f1 = 5.0F;
        GlStateManager.func_179152_a(5.0F, 5.0F, 5.0F);
        GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
        this.field_146297_k.getTextureManager().bindTexture(ENCHANTMENT_TABLE_BOOK_TEXTURE);
        GlStateManager.func_179114_b(20.0F, 1.0F, 0.0F, 0.0F);
        float f2 = this.oOpen + (this.open - this.oOpen) * partialTicks;
        GlStateManager.func_179109_b((1.0F - f2) * 0.2F, (1.0F - f2) * 0.1F, (1.0F - f2) * 0.25F);
        GlStateManager.func_179114_b(-(1.0F - f2) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.func_179114_b(180.0F, 1.0F, 0.0F, 0.0F);
        float f3 = this.oFlip + (this.flip - this.oFlip) * partialTicks + 0.25F;
        float f4 = this.oFlip + (this.flip - this.oFlip) * partialTicks + 0.75F;
        f3 = (f3 - (float)MathHelper.fastFloor((double)f3)) * 1.6F - 0.3F;
        f4 = (f4 - (float)MathHelper.fastFloor((double)f4)) * 1.6F - 0.3F;

        if (f3 < 0.0F)
        {
            f3 = 0.0F;
        }

        if (f4 < 0.0F)
        {
            f4 = 0.0F;
        }

        if (f3 > 1.0F)
        {
            f3 = 1.0F;
        }

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        GlStateManager.func_179091_B();
        MODEL_BOOK.func_78088_a((Entity)null, 0.0F, f3, f4, f2, 0.0F, 0.0625F);
        GlStateManager.func_179101_C();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.func_179128_n(5889);
        GlStateManager.func_179083_b(0, 0, this.field_146297_k.field_71443_c, this.field_146297_k.field_71440_d);
        GlStateManager.func_179121_F();
        GlStateManager.func_179128_n(5888);
        GlStateManager.func_179121_F();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        EnchantmentNameParts.getInstance().reseedRandomGenerator((long)this.field_147075_G.xpSeed);
        int k = this.field_147075_G.getLapisAmount();

        for (int l = 0; l < 3; ++l)
        {
            int i1 = i + 60;
            int j1 = i1 + 20;
            this.field_73735_i = 0.0F;
            this.field_146297_k.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
            int k1 = this.field_147075_G.enchantLevels[l];
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);

            if (k1 == 0)
            {
                this.func_73729_b(i1, j + 14 + 19 * l, 0, 185, 108, 19);
            }
            else
            {
                String s = "" + k1;
                int l1 = 86 - this.field_146289_q.getStringWidth(s);
                String s1 = EnchantmentNameParts.getInstance().generateNewRandomName(this.field_146289_q, l1);
                FontRenderer fontrenderer = this.field_146297_k.standardGalacticFontRenderer;
                int i2 = 6839882;

                if ((k < l + 1 || this.field_146297_k.player.experienceLevel < k1) && !this.field_146297_k.player.abilities.isCreativeMode)
                {
                    this.func_73729_b(i1, j + 14 + 19 * l, 0, 185, 108, 19);
                    this.func_73729_b(i1 + 1, j + 15 + 19 * l, 16 * l, 239, 16, 16);
                    fontrenderer.drawSplitString(s1, j1, j + 16 + 19 * l, l1, (i2 & 16711422) >> 1);
                    i2 = 4226832;
                }
                else
                {
                    int j2 = mouseX - (i + 60);
                    int k2 = mouseY - (j + 14 + 19 * l);

                    if (j2 >= 0 && k2 >= 0 && j2 < 108 && k2 < 19)
                    {
                        this.func_73729_b(i1, j + 14 + 19 * l, 0, 204, 108, 19);
                        i2 = 16777088;
                    }
                    else
                    {
                        this.func_73729_b(i1, j + 14 + 19 * l, 0, 166, 108, 19);
                    }

                    this.func_73729_b(i1 + 1, j + 15 + 19 * l, 16 * l, 223, 16, 16);
                    fontrenderer.drawSplitString(s1, j1, j + 16 + 19 * l, l1, i2);
                    i2 = 8453920;
                }

                fontrenderer = this.field_146297_k.fontRenderer;
                fontrenderer.drawStringWithShadow(s, (float)(j1 + 86 - fontrenderer.getStringWidth(s)), (float)(j + 16 + 19 * l + 7), i2);
            }
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        p_73863_3_ = this.field_146297_k.getTickLength();
        this.func_146276_q_();
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.renderHoveredToolTip(p_73863_1_, p_73863_2_);
        boolean flag = this.field_146297_k.player.abilities.isCreativeMode;
        int i = this.field_147075_G.getLapisAmount();

        for (int j = 0; j < 3; ++j)
        {
            int k = this.field_147075_G.enchantLevels[j];
            Enchantment enchantment = Enchantment.getEnchantmentByID(this.field_147075_G.enchantClue[j]);
            int l = this.field_147075_G.worldClue[j];
            int i1 = j + 1;

            if (this.func_146978_c(60, 14 + 19 * j, 108, 17, p_73863_1_, p_73863_2_) && k > 0 && l >= 0 && enchantment != null)
            {
                List<String> list = Lists.<String>newArrayList();
                list.add("" + TextFormatting.WHITE + TextFormatting.ITALIC + I18n.format("container.enchant.clue", enchantment.func_77316_c(l)));

                if (!flag)
                {
                    list.add("");

                    if (this.field_146297_k.player.experienceLevel < k)
                    {
                        list.add(TextFormatting.RED + I18n.format("container.enchant.level.requirement", this.field_147075_G.enchantLevels[j]));
                    }
                    else
                    {
                        String s;

                        if (i1 == 1)
                        {
                            s = I18n.format("container.enchant.lapis.one");
                        }
                        else
                        {
                            s = I18n.format("container.enchant.lapis.many", i1);
                        }

                        TextFormatting textformatting = i >= i1 ? TextFormatting.GRAY : TextFormatting.RED;
                        list.add(textformatting + "" + s);

                        if (i1 == 1)
                        {
                            s = I18n.format("container.enchant.level.one");
                        }
                        else
                        {
                            s = I18n.format("container.enchant.level.many", i1);
                        }

                        list.add(TextFormatting.GRAY + "" + s);
                    }
                }

                this.func_146283_a(list, p_73863_1_, p_73863_2_);
                break;
            }
        }
    }

    public void tickBook()
    {
        ItemStack itemstack = this.container.getSlot(0).getStack();

        if (!ItemStack.areItemStacksEqual(itemstack, this.last))
        {
            this.last = itemstack;

            while (true)
            {
                this.flipT += (float)(this.random.nextInt(4) - this.random.nextInt(4));

                if (this.flip > this.flipT + 1.0F || this.flip < this.flipT - 1.0F)
                {
                    break;
                }
            }
        }

        ++this.ticks;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean flag = false;

        for (int i = 0; i < 3; ++i)
        {
            if (this.field_147075_G.enchantLevels[i] != 0)
            {
                flag = true;
            }
        }

        if (flag)
        {
            this.open += 0.2F;
        }
        else
        {
            this.open -= 0.2F;
        }

        this.open = MathHelper.clamp(this.open, 0.0F, 1.0F);
        float f1 = (this.flipT - this.flip) * 0.4F;
        float f = 0.2F;
        f1 = MathHelper.clamp(f1, -0.2F, 0.2F);
        this.flipA += (f1 - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }
}

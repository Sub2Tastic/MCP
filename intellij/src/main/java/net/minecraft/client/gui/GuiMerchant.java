package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiMerchant extends GuiContainer
{
    private static final Logger field_147039_u = LogManager.getLogger();

    /** The GUI texture for the villager merchant GUI. */
    private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");
    private final IMerchant field_147037_w;
    private GuiMerchant.MerchantButton field_147043_x;
    private GuiMerchant.MerchantButton field_147042_y;

    /**
     * The integer value corresponding to the currently selected merchant recipe.
     */
    private int selectedMerchantRecipe;
    private final ITextComponent field_147040_A;

    public GuiMerchant(InventoryPlayer p_i45500_1_, IMerchant p_i45500_2_, World p_i45500_3_)
    {
        super(new ContainerMerchant(p_i45500_1_, p_i45500_2_, p_i45500_3_));
        this.field_147037_w = p_i45500_2_;
        this.field_147040_A = p_i45500_2_.getDisplayName();
    }

    public void func_73866_w_()
    {
        super.func_73866_w_();
        int i = (this.field_146294_l - this.xSize) / 2;
        int j = (this.field_146295_m - this.ySize) / 2;
        this.field_147043_x = (GuiMerchant.MerchantButton)this.func_189646_b(new GuiMerchant.MerchantButton(1, i + 120 + 27, j + 24 - 1, true));
        this.field_147042_y = (GuiMerchant.MerchantButton)this.func_189646_b(new GuiMerchant.MerchantButton(2, i + 36 - 19, j + 24 - 1, false));
        this.field_147043_x.field_146124_l = false;
        this.field_147042_y.field_146124_l = false;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.field_147040_A.func_150260_c();
        this.field_146289_q.func_78276_b(s, this.xSize / 2 - this.field_146289_q.getStringWidth(s) / 2, 6, 4210752);
        this.field_146289_q.func_78276_b(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    public void func_73876_c()
    {
        super.func_73876_c();
        MerchantRecipeList merchantrecipelist = this.field_147037_w.func_70934_b(this.field_146297_k.player);

        if (merchantrecipelist != null)
        {
            this.field_147043_x.field_146124_l = this.selectedMerchantRecipe < merchantrecipelist.size() - 1;
            this.field_147042_y.field_146124_l = this.selectedMerchantRecipe > 0;
        }
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        boolean flag = false;

        if (p_146284_1_ == this.field_147043_x)
        {
            ++this.selectedMerchantRecipe;
            MerchantRecipeList merchantrecipelist = this.field_147037_w.func_70934_b(this.field_146297_k.player);

            if (merchantrecipelist != null && this.selectedMerchantRecipe >= merchantrecipelist.size())
            {
                this.selectedMerchantRecipe = merchantrecipelist.size() - 1;
            }

            flag = true;
        }
        else if (p_146284_1_ == this.field_147042_y)
        {
            --this.selectedMerchantRecipe;

            if (this.selectedMerchantRecipe < 0)
            {
                this.selectedMerchantRecipe = 0;
            }

            flag = true;
        }

        if (flag)
        {
            ((ContainerMerchant)this.container).setCurrentRecipeIndex(this.selectedMerchantRecipe);
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.selectedMerchantRecipe);
            this.field_146297_k.getConnection().sendPacket(new CPacketCustomPayload("MC|TrSel", packetbuffer));
        }
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_146297_k.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
        int i = (this.field_146294_l - this.xSize) / 2;
        int j = (this.field_146295_m - this.ySize) / 2;
        this.func_73729_b(i, j, 0, 0, this.xSize, this.ySize);
        MerchantRecipeList merchantrecipelist = this.field_147037_w.func_70934_b(this.field_146297_k.player);

        if (merchantrecipelist != null && !merchantrecipelist.isEmpty())
        {
            int k = this.selectedMerchantRecipe;

            if (k < 0 || k >= merchantrecipelist.size())
            {
                return;
            }

            MerchantRecipe merchantrecipe = (MerchantRecipe)merchantrecipelist.get(k);

            if (merchantrecipe.func_82784_g())
            {
                this.field_146297_k.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
                GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.func_179140_f();
                this.func_73729_b(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
                this.func_73729_b(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
            }
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
        MerchantRecipeList merchantrecipelist = this.field_147037_w.func_70934_b(this.field_146297_k.player);

        if (merchantrecipelist != null && !merchantrecipelist.isEmpty())
        {
            int i = (this.field_146294_l - this.xSize) / 2;
            int j = (this.field_146295_m - this.ySize) / 2;
            int k = this.selectedMerchantRecipe;
            MerchantRecipe merchantrecipe = (MerchantRecipe)merchantrecipelist.get(k);
            ItemStack itemstack = merchantrecipe.func_77394_a();
            ItemStack itemstack1 = merchantrecipe.func_77396_b();
            ItemStack itemstack2 = merchantrecipe.func_77397_d();
            GlStateManager.func_179094_E();
            RenderHelper.func_74520_c();
            GlStateManager.func_179140_f();
            GlStateManager.func_179091_B();
            GlStateManager.func_179142_g();
            GlStateManager.func_179145_e();
            this.field_146296_j.zLevel = 100.0F;
            this.field_146296_j.renderItemAndEffectIntoGUI(itemstack, i + 36, j + 24);
            this.field_146296_j.renderItemOverlays(this.field_146289_q, itemstack, i + 36, j + 24);

            if (!itemstack1.isEmpty())
            {
                this.field_146296_j.renderItemAndEffectIntoGUI(itemstack1, i + 62, j + 24);
                this.field_146296_j.renderItemOverlays(this.field_146289_q, itemstack1, i + 62, j + 24);
            }

            this.field_146296_j.renderItemAndEffectIntoGUI(itemstack2, i + 120, j + 24);
            this.field_146296_j.renderItemOverlays(this.field_146289_q, itemstack2, i + 120, j + 24);
            this.field_146296_j.zLevel = 0.0F;
            GlStateManager.func_179140_f();

            if (this.func_146978_c(36, 24, 16, 16, p_73863_1_, p_73863_2_) && !itemstack.isEmpty())
            {
                this.func_146285_a(itemstack, p_73863_1_, p_73863_2_);
            }
            else if (!itemstack1.isEmpty() && this.func_146978_c(62, 24, 16, 16, p_73863_1_, p_73863_2_) && !itemstack1.isEmpty())
            {
                this.func_146285_a(itemstack1, p_73863_1_, p_73863_2_);
            }
            else if (!itemstack2.isEmpty() && this.func_146978_c(120, 24, 16, 16, p_73863_1_, p_73863_2_) && !itemstack2.isEmpty())
            {
                this.func_146285_a(itemstack2, p_73863_1_, p_73863_2_);
            }
            else if (merchantrecipe.func_82784_g() && (this.func_146978_c(83, 21, 28, 21, p_73863_1_, p_73863_2_) || this.func_146978_c(83, 51, 28, 21, p_73863_1_, p_73863_2_)))
            {
                this.func_146279_a(I18n.format("merchant.deprecated"), p_73863_1_, p_73863_2_);
            }

            GlStateManager.func_179121_F();
            GlStateManager.func_179145_e();
            GlStateManager.func_179126_j();
            RenderHelper.func_74519_b();
        }

        this.renderHoveredToolTip(p_73863_1_, p_73863_2_);
    }

    public IMerchant func_147035_g()
    {
        return this.field_147037_w;
    }

    static class MerchantButton extends GuiButton
    {
        private final boolean field_146157_o;

        public MerchantButton(int p_i1095_1_, int p_i1095_2_, int p_i1095_3_, boolean p_i1095_4_)
        {
            super(p_i1095_1_, p_i1095_2_, p_i1095_3_, 12, 19, "");
            this.field_146157_o = p_i1095_4_;
        }

        public void func_191745_a(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_)
        {
            if (this.field_146125_m)
            {
                p_191745_1_.getTextureManager().bindTexture(GuiMerchant.MERCHANT_GUI_TEXTURE);
                GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                boolean flag = p_191745_2_ >= this.field_146128_h && p_191745_3_ >= this.field_146129_i && p_191745_2_ < this.field_146128_h + this.field_146120_f && p_191745_3_ < this.field_146129_i + this.field_146121_g;
                int i = 0;
                int j = 176;

                if (!this.field_146124_l)
                {
                    j += this.field_146120_f * 2;
                }
                else if (flag)
                {
                    j += this.field_146120_f;
                }

                if (!this.field_146157_o)
                {
                    i += this.field_146121_g;
                }

                this.func_73729_b(this.field_146128_h, this.field_146129_i, j, i, this.field_146120_f, this.field_146121_g);
            }
        }
    }
}

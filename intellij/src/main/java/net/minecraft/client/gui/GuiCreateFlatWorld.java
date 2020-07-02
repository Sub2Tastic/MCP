package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;

public class GuiCreateFlatWorld extends GuiScreen
{
    private final GuiCreateWorld createWorldGui;
    private FlatGeneratorInfo generatorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();
    private String field_146393_h;

    /** The text used to identify the material for a layer */
    private String materialText;

    /** The text used to identify the height of a layer */
    private String heightText;
    private GuiCreateFlatWorld.Details createFlatWorldListSlotGui;
    private GuiButton field_146389_t;
    private GuiButton field_146388_u;

    /** The remove layer button */
    private GuiButton removeLayerButton;

    public GuiCreateFlatWorld(GuiCreateWorld p_i1029_1_, String p_i1029_2_)
    {
        this.createWorldGui = p_i1029_1_;
        this.func_146383_a(p_i1029_2_);
    }

    public String func_146384_e()
    {
        return this.generatorInfo.toString();
    }

    public void func_146383_a(String p_146383_1_)
    {
        this.generatorInfo = FlatGeneratorInfo.createFlatGeneratorFromString(p_146383_1_);
    }

    public void func_73866_w_()
    {
        this.field_146292_n.clear();
        this.field_146393_h = I18n.format("createWorld.customize.flat.title");
        this.materialText = I18n.format("createWorld.customize.flat.tile");
        this.heightText = I18n.format("createWorld.customize.flat.height");
        this.createFlatWorldListSlotGui = new GuiCreateFlatWorld.Details();
        this.field_146389_t = this.func_189646_b(new GuiButton(2, this.field_146294_l / 2 - 154, this.field_146295_m - 52, 100, 20, I18n.format("createWorld.customize.flat.addLayer") + " (NYI)"));
        this.field_146388_u = this.func_189646_b(new GuiButton(3, this.field_146294_l / 2 - 50, this.field_146295_m - 52, 100, 20, I18n.format("createWorld.customize.flat.editLayer") + " (NYI)"));
        this.removeLayerButton = this.func_189646_b(new GuiButton(4, this.field_146294_l / 2 - 155, this.field_146295_m - 52, 150, 20, I18n.format("createWorld.customize.flat.removeLayer")));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 155, this.field_146295_m - 28, 150, 20, I18n.format("gui.done")));
        this.field_146292_n.add(new GuiButton(5, this.field_146294_l / 2 + 5, this.field_146295_m - 52, 150, 20, I18n.format("createWorld.customize.presets")));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 5, this.field_146295_m - 28, 150, 20, I18n.format("gui.cancel")));
        this.field_146389_t.field_146125_m = false;
        this.field_146388_u.field_146125_m = false;
        this.generatorInfo.updateLayers();
        this.onLayersChanged();
    }

    public void func_146274_d() throws IOException
    {
        super.func_146274_d();
        this.createFlatWorldListSlotGui.func_178039_p();
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        int i = this.generatorInfo.getFlatLayers().size() - this.createFlatWorldListSlotGui.field_148228_k - 1;

        if (p_146284_1_.field_146127_k == 1)
        {
            this.field_146297_k.displayGuiScreen(this.createWorldGui);
        }
        else if (p_146284_1_.field_146127_k == 0)
        {
            this.createWorldGui.chunkProviderSettingsJson = this.func_146384_e();
            this.field_146297_k.displayGuiScreen(this.createWorldGui);
        }
        else if (p_146284_1_.field_146127_k == 5)
        {
            this.field_146297_k.displayGuiScreen(new GuiFlatPresets(this));
        }
        else if (p_146284_1_.field_146127_k == 4 && this.hasSelectedLayer())
        {
            this.generatorInfo.getFlatLayers().remove(i);
            this.createFlatWorldListSlotGui.field_148228_k = Math.min(this.createFlatWorldListSlotGui.field_148228_k, this.generatorInfo.getFlatLayers().size() - 1);
        }

        this.generatorInfo.updateLayers();
        this.onLayersChanged();
    }

    /**
     * Would update whether or not the edit and remove buttons are enabled, but is currently disabled and always
     * disables the buttons (which are invisible anyways)
     */
    public void onLayersChanged()
    {
        boolean flag = this.hasSelectedLayer();
        this.removeLayerButton.field_146124_l = flag;
        this.field_146388_u.field_146124_l = flag;
        this.field_146388_u.field_146124_l = false;
        this.field_146389_t.field_146124_l = false;
    }

    /**
     * Returns whether there is a valid layer selection
     */
    private boolean hasSelectedLayer()
    {
        return this.createFlatWorldListSlotGui.field_148228_k > -1 && this.createFlatWorldListSlotGui.field_148228_k < this.generatorInfo.getFlatLayers().size();
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.createFlatWorldListSlotGui.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.func_73732_a(this.field_146289_q, this.field_146393_h, this.field_146294_l / 2, 8, 16777215);
        int i = this.field_146294_l / 2 - 92 - 16;
        this.func_73731_b(this.field_146289_q, this.materialText, i, 32, 16777215);
        this.func_73731_b(this.field_146289_q, this.heightText, i + 2 + 213 - this.field_146289_q.getStringWidth(this.heightText), 32, 16777215);
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    class Details extends GuiSlot
    {
        public int field_148228_k = -1;

        public Details()
        {
            super(GuiCreateFlatWorld.this.field_146297_k, GuiCreateFlatWorld.this.field_146294_l, GuiCreateFlatWorld.this.field_146295_m, 43, GuiCreateFlatWorld.this.field_146295_m - 60, 24);
        }

        private void func_148225_a(int p_148225_1_, int p_148225_2_, ItemStack p_148225_3_)
        {
            this.func_148226_e(p_148225_1_ + 1, p_148225_2_ + 1);
            GlStateManager.func_179091_B();

            if (!p_148225_3_.isEmpty())
            {
                RenderHelper.func_74520_c();
                GuiCreateFlatWorld.this.field_146296_j.renderItemIntoGUI(p_148225_3_, p_148225_1_ + 2, p_148225_2_ + 2);
                RenderHelper.disableStandardItemLighting();
            }

            GlStateManager.func_179101_C();
        }

        private void func_148226_e(int p_148226_1_, int p_148226_2_)
        {
            this.func_148224_c(p_148226_1_, p_148226_2_, 0, 0);
        }

        private void func_148224_c(int p_148224_1_, int p_148224_2_, int p_148224_3_, int p_148224_4_)
        {
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_148161_k.getTextureManager().bindTexture(Gui.field_110323_l);
            float f = 0.0078125F;
            float f1 = 0.0078125F;
            int i = 18;
            int j = 18;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.func_181662_b((double)(p_148224_1_ + 0), (double)(p_148224_2_ + 18), (double)GuiCreateFlatWorld.this.field_73735_i).func_187315_a((double)((float)(p_148224_3_ + 0) * 0.0078125F), (double)((float)(p_148224_4_ + 18) * 0.0078125F)).endVertex();
            bufferbuilder.func_181662_b((double)(p_148224_1_ + 18), (double)(p_148224_2_ + 18), (double)GuiCreateFlatWorld.this.field_73735_i).func_187315_a((double)((float)(p_148224_3_ + 18) * 0.0078125F), (double)((float)(p_148224_4_ + 18) * 0.0078125F)).endVertex();
            bufferbuilder.func_181662_b((double)(p_148224_1_ + 18), (double)(p_148224_2_ + 0), (double)GuiCreateFlatWorld.this.field_73735_i).func_187315_a((double)((float)(p_148224_3_ + 18) * 0.0078125F), (double)((float)(p_148224_4_ + 0) * 0.0078125F)).endVertex();
            bufferbuilder.func_181662_b((double)(p_148224_1_ + 0), (double)(p_148224_2_ + 0), (double)GuiCreateFlatWorld.this.field_73735_i).func_187315_a((double)((float)(p_148224_3_ + 0) * 0.0078125F), (double)((float)(p_148224_4_ + 0) * 0.0078125F)).endVertex();
            tessellator.draw();
        }

        protected int func_148127_b()
        {
            return GuiCreateFlatWorld.this.generatorInfo.getFlatLayers().size();
        }

        protected void func_148144_a(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_)
        {
            this.field_148228_k = p_148144_1_;
            GuiCreateFlatWorld.this.onLayersChanged();
        }

        protected boolean func_148131_a(int p_148131_1_)
        {
            return p_148131_1_ == this.field_148228_k;
        }

        protected void func_148123_a()
        {
        }

        protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_)
        {
            FlatLayerInfo flatlayerinfo = (FlatLayerInfo)GuiCreateFlatWorld.this.generatorInfo.getFlatLayers().get(GuiCreateFlatWorld.this.generatorInfo.getFlatLayers().size() - p_192637_1_ - 1);
            IBlockState iblockstate = flatlayerinfo.getLayerMaterial();
            Block block = iblockstate.getBlock();
            Item item = Item.getItemFromBlock(block);

            if (item == Items.AIR)
            {
                if (block != Blocks.WATER && block != Blocks.field_150358_i)
                {
                    if (block == Blocks.LAVA || block == Blocks.field_150356_k)
                    {
                        item = Items.LAVA_BUCKET;
                    }
                }
                else
                {
                    item = Items.WATER_BUCKET;
                }
            }

            ItemStack itemstack = new ItemStack(item, 1, item.func_77614_k() ? block.func_176201_c(iblockstate) : 0);
            String s = item.func_77653_i(itemstack);
            this.func_148225_a(p_192637_2_, p_192637_3_, itemstack);
            GuiCreateFlatWorld.this.field_146289_q.func_78276_b(s, p_192637_2_ + 18 + 5, p_192637_3_ + 3, 16777215);
            String s1;

            if (p_192637_1_ == 0)
            {
                s1 = I18n.format("createWorld.customize.flat.layer.top", flatlayerinfo.getLayerCount());
            }
            else if (p_192637_1_ == GuiCreateFlatWorld.this.generatorInfo.getFlatLayers().size() - 1)
            {
                s1 = I18n.format("createWorld.customize.flat.layer.bottom", flatlayerinfo.getLayerCount());
            }
            else
            {
                s1 = I18n.format("createWorld.customize.flat.layer", flatlayerinfo.getLayerCount());
            }

            GuiCreateFlatWorld.this.field_146289_q.func_78276_b(s1, p_192637_2_ + 2 + 213 - GuiCreateFlatWorld.this.field_146289_q.getStringWidth(s1), p_192637_3_ + 3, 16777215);
        }

        protected int func_148137_d()
        {
            return this.field_148155_a - 70;
        }
    }
}

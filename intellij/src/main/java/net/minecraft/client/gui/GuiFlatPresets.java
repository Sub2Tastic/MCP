package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import org.lwjgl.input.Keyboard;

public class GuiFlatPresets extends GuiScreen
{
    private static final List<GuiFlatPresets.LayerItem> FLAT_WORLD_PRESETS = Lists.<GuiFlatPresets.LayerItem>newArrayList();

    /** The parent GUI */
    private final GuiCreateFlatWorld parentScreen;
    private String field_146438_h;
    private String presetsShare;
    private String listText;
    private GuiFlatPresets.ListSlot list;
    private GuiButton btnSelect;
    private GuiTextField export;

    public GuiFlatPresets(GuiCreateFlatWorld parent)
    {
        this.parentScreen = parent;
    }

    public void func_73866_w_()
    {
        this.field_146292_n.clear();
        Keyboard.enableRepeatEvents(true);
        this.field_146438_h = I18n.format("createWorld.customize.presets.title");
        this.presetsShare = I18n.format("createWorld.customize.presets.share");
        this.listText = I18n.format("createWorld.customize.presets.list");
        this.export = new GuiTextField(2, this.field_146289_q, 50, 40, this.field_146294_l - 100, 20);
        this.list = new GuiFlatPresets.ListSlot();
        this.export.setMaxStringLength(1230);
        this.export.setText(this.parentScreen.func_146384_e());
        this.btnSelect = this.func_189646_b(new GuiButton(0, this.field_146294_l / 2 - 155, this.field_146295_m - 28, 150, 20, I18n.format("createWorld.customize.presets.select")));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 5, this.field_146295_m - 28, 150, 20, I18n.format("gui.cancel")));
        this.func_146426_g();
    }

    public void func_146274_d() throws IOException
    {
        super.func_146274_d();
        this.list.func_178039_p();
    }

    public void func_146281_b()
    {
        Keyboard.enableRepeatEvents(false);
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        this.export.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (!this.export.func_146201_a(p_73869_1_, p_73869_2_))
        {
            super.func_73869_a(p_73869_1_, p_73869_2_);
        }
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146127_k == 0 && this.func_146430_p())
        {
            this.parentScreen.func_146383_a(this.export.getText());
            this.field_146297_k.displayGuiScreen(this.parentScreen);
        }
        else if (p_146284_1_.field_146127_k == 1)
        {
            this.field_146297_k.displayGuiScreen(this.parentScreen);
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.list.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.func_73732_a(this.field_146289_q, this.field_146438_h, this.field_146294_l / 2, 8, 16777215);
        this.func_73731_b(this.field_146289_q, this.presetsShare, 50, 30, 10526880);
        this.func_73731_b(this.field_146289_q, this.listText, 50, 70, 10526880);
        this.export.func_146194_f();
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    public void func_73876_c()
    {
        this.export.tick();
        super.func_73876_c();
    }

    public void func_146426_g()
    {
        this.btnSelect.field_146124_l = this.func_146430_p();
    }

    private boolean func_146430_p()
    {
        return this.list.field_148175_k > -1 && this.list.field_148175_k < FLAT_WORLD_PRESETS.size() || this.export.getText().length() > 1;
    }

    private static void func_146421_a(String p_146421_0_, Item p_146421_1_, Biome p_146421_2_, List<String> p_146421_3_, FlatLayerInfo... p_146421_4_)
    {
        func_175354_a(p_146421_0_, p_146421_1_, 0, p_146421_2_, p_146421_3_, p_146421_4_);
    }

    private static void func_175354_a(String p_175354_0_, Item p_175354_1_, int p_175354_2_, Biome p_175354_3_, List<String> p_175354_4_, FlatLayerInfo... p_175354_5_)
    {
        FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();

        for (int i = p_175354_5_.length - 1; i >= 0; --i)
        {
            flatgeneratorinfo.getFlatLayers().add(p_175354_5_[i]);
        }

        flatgeneratorinfo.setBiome(Biome.func_185362_a(p_175354_3_));
        flatgeneratorinfo.updateLayers();

        for (String s : p_175354_4_)
        {
            flatgeneratorinfo.getWorldFeatures().put(s, Maps.newHashMap());
        }

        FLAT_WORLD_PRESETS.add(new GuiFlatPresets.LayerItem(p_175354_1_, p_175354_2_, p_175354_0_, flatgeneratorinfo.toString()));
    }

    static
    {
        func_146421_a(I18n.format("createWorld.customize.preset.classic_flat"), Item.getItemFromBlock(Blocks.GRASS), Biomes.PLAINS, Arrays.asList("village"), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(2, Blocks.DIRT), new FlatLayerInfo(1, Blocks.BEDROCK));
        func_146421_a(I18n.format("createWorld.customize.preset.tunnelers_dream"), Item.getItemFromBlock(Blocks.STONE), Biomes.MOUNTAINS, Arrays.asList("biome_1", "dungeon", "decoration", "stronghold", "mineshaft"), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(230, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        func_146421_a(I18n.format("createWorld.customize.preset.water_world"), Items.WATER_BUCKET, Biomes.DEEP_OCEAN, Arrays.asList("biome_1", "oceanmonument"), new FlatLayerInfo(90, Blocks.WATER), new FlatLayerInfo(5, Blocks.SAND), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(5, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        func_175354_a(I18n.format("createWorld.customize.preset.overworld"), Item.getItemFromBlock(Blocks.field_150329_H), BlockTallGrass.EnumType.GRASS.func_177044_a(), Biomes.PLAINS, Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon", "lake", "lava_lake"), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        func_146421_a(I18n.format("createWorld.customize.preset.snowy_kingdom"), Item.getItemFromBlock(Blocks.field_150431_aC), Biomes.SNOWY_TUNDRA, Arrays.asList("village", "biome_1"), new FlatLayerInfo(1, Blocks.field_150431_aC), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        func_146421_a(I18n.format("createWorld.customize.preset.bottomless_pit"), Items.FEATHER, Biomes.PLAINS, Arrays.asList("village", "biome_1"), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(2, Blocks.COBBLESTONE));
        func_146421_a(I18n.format("createWorld.customize.preset.desert"), Item.getItemFromBlock(Blocks.SAND), Biomes.DESERT, Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon"), new FlatLayerInfo(8, Blocks.SAND), new FlatLayerInfo(52, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        func_146421_a(I18n.format("createWorld.customize.preset.redstone_ready"), Items.REDSTONE, Biomes.DESERT, Collections.emptyList(), new FlatLayerInfo(52, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        func_146421_a(I18n.format("createWorld.customize.preset.the_void"), Item.getItemFromBlock(Blocks.BARRIER), Biomes.THE_VOID, Arrays.asList("decoration"), new FlatLayerInfo(1, Blocks.AIR));
    }

    static class LayerItem
    {
        public Item icon;
        public int field_179037_b;
        public String name;
        public String generatorInfo;

        public LayerItem(Item p_i45518_1_, int p_i45518_2_, String p_i45518_3_, String p_i45518_4_)
        {
            this.icon = p_i45518_1_;
            this.field_179037_b = p_i45518_2_;
            this.name = p_i45518_3_;
            this.generatorInfo = p_i45518_4_;
        }
    }

    class ListSlot extends GuiSlot
    {
        public int field_148175_k = -1;

        public ListSlot()
        {
            super(GuiFlatPresets.this.field_146297_k, GuiFlatPresets.this.field_146294_l, GuiFlatPresets.this.field_146295_m, 80, GuiFlatPresets.this.field_146295_m - 37, 24);
        }

        private void func_178054_a(int p_178054_1_, int p_178054_2_, Item p_178054_3_, int p_178054_4_)
        {
            this.func_148173_e(p_178054_1_ + 1, p_178054_2_ + 1);
            GlStateManager.func_179091_B();
            RenderHelper.func_74520_c();
            GuiFlatPresets.this.field_146296_j.renderItemIntoGUI(new ItemStack(p_178054_3_, 1, p_178054_3_.func_77614_k() ? p_178054_4_ : 0), p_178054_1_ + 2, p_178054_2_ + 2);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.func_179101_C();
        }

        private void func_148173_e(int p_148173_1_, int p_148173_2_)
        {
            this.func_148171_c(p_148173_1_, p_148173_2_, 0, 0);
        }

        private void func_148171_c(int p_148171_1_, int p_148171_2_, int p_148171_3_, int p_148171_4_)
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
            bufferbuilder.func_181662_b((double)(p_148171_1_ + 0), (double)(p_148171_2_ + 18), (double)GuiFlatPresets.this.field_73735_i).func_187315_a((double)((float)(p_148171_3_ + 0) * 0.0078125F), (double)((float)(p_148171_4_ + 18) * 0.0078125F)).endVertex();
            bufferbuilder.func_181662_b((double)(p_148171_1_ + 18), (double)(p_148171_2_ + 18), (double)GuiFlatPresets.this.field_73735_i).func_187315_a((double)((float)(p_148171_3_ + 18) * 0.0078125F), (double)((float)(p_148171_4_ + 18) * 0.0078125F)).endVertex();
            bufferbuilder.func_181662_b((double)(p_148171_1_ + 18), (double)(p_148171_2_ + 0), (double)GuiFlatPresets.this.field_73735_i).func_187315_a((double)((float)(p_148171_3_ + 18) * 0.0078125F), (double)((float)(p_148171_4_ + 0) * 0.0078125F)).endVertex();
            bufferbuilder.func_181662_b((double)(p_148171_1_ + 0), (double)(p_148171_2_ + 0), (double)GuiFlatPresets.this.field_73735_i).func_187315_a((double)((float)(p_148171_3_ + 0) * 0.0078125F), (double)((float)(p_148171_4_ + 0) * 0.0078125F)).endVertex();
            tessellator.draw();
        }

        protected int func_148127_b()
        {
            return GuiFlatPresets.FLAT_WORLD_PRESETS.size();
        }

        protected void func_148144_a(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_)
        {
            this.field_148175_k = p_148144_1_;
            GuiFlatPresets.this.func_146426_g();
            GuiFlatPresets.this.export.setText((GuiFlatPresets.FLAT_WORLD_PRESETS.get(GuiFlatPresets.this.list.field_148175_k)).generatorInfo);
        }

        protected boolean func_148131_a(int p_148131_1_)
        {
            return p_148131_1_ == this.field_148175_k;
        }

        protected void func_148123_a()
        {
        }

        protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_)
        {
            GuiFlatPresets.LayerItem guiflatpresets$layeritem = GuiFlatPresets.FLAT_WORLD_PRESETS.get(p_192637_1_);
            this.func_178054_a(p_192637_2_, p_192637_3_, guiflatpresets$layeritem.icon, guiflatpresets$layeritem.field_179037_b);
            GuiFlatPresets.this.field_146289_q.func_78276_b(guiflatpresets$layeritem.name, p_192637_2_ + 18 + 5, p_192637_3_ + 6, 16777215);
        }
    }
}

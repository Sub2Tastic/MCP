package net.minecraft.client.gui;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class MapItemRenderer
{
    private static final ResourceLocation TEXTURE_MAP_ICONS = new ResourceLocation("textures/map/map_icons.png");
    private final TextureManager textureManager;
    private final Map<String, MapItemRenderer.Instance> loadedMaps = Maps.<String, MapItemRenderer.Instance>newHashMap();

    public MapItemRenderer(TextureManager textureManagerIn)
    {
        this.textureManager = textureManagerIn;
    }

    /**
     * Updates a map texture
     */
    public void updateMapTexture(MapData mapdataIn)
    {
        this.getMapRendererInstance(mapdataIn).updateMapTexture();
    }

    public void func_148250_a(MapData p_148250_1_, boolean p_148250_2_)
    {
        this.getMapRendererInstance(p_148250_1_).func_148237_a(p_148250_2_);
    }

    /**
     * Returns {@link net.minecraft.client.gui.MapItemRenderer.Instance MapItemRenderer.Instance} with given map data
     */
    private MapItemRenderer.Instance getMapRendererInstance(MapData mapdataIn)
    {
        MapItemRenderer.Instance mapitemrenderer$instance = this.loadedMaps.get(mapdataIn.name);

        if (mapitemrenderer$instance == null)
        {
            mapitemrenderer$instance = new MapItemRenderer.Instance(mapdataIn);
            this.loadedMaps.put(mapdataIn.name, mapitemrenderer$instance);
        }

        return mapitemrenderer$instance;
    }

    @Nullable
    public MapItemRenderer.Instance getMapInstanceIfExists(String p_191205_1_)
    {
        return this.loadedMaps.get(p_191205_1_);
    }

    /**
     * Clears the currently loaded maps and removes their corresponding textures
     */
    public void clearLoadedMaps()
    {
        for (MapItemRenderer.Instance mapitemrenderer$instance : this.loadedMaps.values())
        {
            this.textureManager.deleteTexture(mapitemrenderer$instance.field_148240_d);
        }

        this.loadedMaps.clear();
    }

    @Nullable
    public MapData getData(@Nullable MapItemRenderer.Instance p_191207_1_)
    {
        return p_191207_1_ != null ? p_191207_1_.mapData : null;
    }

    class Instance
    {
        private final MapData mapData;
        private final DynamicTexture mapTexture;
        private final ResourceLocation field_148240_d;
        private final int[] field_148241_e;

        private Instance(MapData mapdataIn)
        {
            this.mapData = mapdataIn;
            this.mapTexture = new DynamicTexture(128, 128);
            this.field_148241_e = this.mapTexture.func_110565_c();
            this.field_148240_d = MapItemRenderer.this.textureManager.getDynamicTextureLocation("map/" + mapdataIn.name, this.mapTexture);

            for (int i = 0; i < this.field_148241_e.length; ++i)
            {
                this.field_148241_e[i] = 0;
            }
        }

        private void updateMapTexture()
        {
            for (int i = 0; i < 16384; ++i)
            {
                int j = this.mapData.colors[i] & 255;

                if (j / 4 == 0)
                {
                    this.field_148241_e[i] = (i + i / 128 & 1) * 8 + 16 << 24;
                }
                else
                {
                    this.field_148241_e[i] = MapColor.COLORS[j / 4].getMapColor(j & 3);
                }
            }

            this.mapTexture.updateDynamicTexture();
        }

        private void func_148237_a(boolean p_148237_1_)
        {
            int i = 0;
            int j = 0;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            float f = 0.0F;
            MapItemRenderer.this.textureManager.bindTexture(this.field_148240_d);
            GlStateManager.func_179147_l();
            GlStateManager.func_187428_a(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            GlStateManager.func_179118_c();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.func_181662_b(0.0D, 128.0D, -0.009999999776482582D).func_187315_a(0.0D, 1.0D).endVertex();
            bufferbuilder.func_181662_b(128.0D, 128.0D, -0.009999999776482582D).func_187315_a(1.0D, 1.0D).endVertex();
            bufferbuilder.func_181662_b(128.0D, 0.0D, -0.009999999776482582D).func_187315_a(1.0D, 0.0D).endVertex();
            bufferbuilder.func_181662_b(0.0D, 0.0D, -0.009999999776482582D).func_187315_a(0.0D, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.func_179141_d();
            GlStateManager.func_179084_k();
            MapItemRenderer.this.textureManager.bindTexture(MapItemRenderer.TEXTURE_MAP_ICONS);
            int k = 0;

            for (MapDecoration mapdecoration : this.mapData.mapDecorations.values())
            {
                if (!p_148237_1_ || mapdecoration.renderOnFrame())
                {
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179109_b(0.0F + (float)mapdecoration.getX() / 2.0F + 64.0F, 0.0F + (float)mapdecoration.getY() / 2.0F + 64.0F, -0.02F);
                    GlStateManager.func_179114_b((float)(mapdecoration.getRotation() * 360) / 16.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.func_179152_a(4.0F, 4.0F, 3.0F);
                    GlStateManager.func_179109_b(-0.125F, 0.125F, 0.0F);
                    byte b0 = mapdecoration.getImage();
                    float f1 = (float)(b0 % 4 + 0) / 4.0F;
                    float f2 = (float)(b0 / 4 + 0) / 4.0F;
                    float f3 = (float)(b0 % 4 + 1) / 4.0F;
                    float f4 = (float)(b0 / 4 + 1) / 4.0F;
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                    float f5 = -0.001F;
                    bufferbuilder.func_181662_b(-1.0D, 1.0D, (double)((float)k * -0.001F)).func_187315_a((double)f1, (double)f2).endVertex();
                    bufferbuilder.func_181662_b(1.0D, 1.0D, (double)((float)k * -0.001F)).func_187315_a((double)f3, (double)f2).endVertex();
                    bufferbuilder.func_181662_b(1.0D, -1.0D, (double)((float)k * -0.001F)).func_187315_a((double)f3, (double)f4).endVertex();
                    bufferbuilder.func_181662_b(-1.0D, -1.0D, (double)((float)k * -0.001F)).func_187315_a((double)f1, (double)f4).endVertex();
                    tessellator.draw();
                    GlStateManager.func_179121_F();
                    ++k;
                }
            }

            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(0.0F, 0.0F, -0.04F);
            GlStateManager.func_179152_a(1.0F, 1.0F, 1.0F);
            GlStateManager.func_179121_F();
        }
    }
}

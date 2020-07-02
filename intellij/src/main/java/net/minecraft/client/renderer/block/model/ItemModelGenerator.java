package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector3f;

public class ItemModelGenerator
{
    public static final List<String> LAYERS = Lists.newArrayList("layer0", "layer1", "layer2", "layer3", "layer4");

    @Nullable
    public ModelBlock func_178392_a(TextureMap p_178392_1_, ModelBlock p_178392_2_)
    {
        Map<String, String> map = Maps.<String, String>newHashMap();
        List<BlockPart> list = Lists.<BlockPart>newArrayList();

        for (int i = 0; i < LAYERS.size(); ++i)
        {
            String s = LAYERS.get(i);

            if (!p_178392_2_.isTexturePresent(s))
            {
                break;
            }

            String s1 = p_178392_2_.func_178308_c(s);
            map.put(s, s1);
            TextureAtlasSprite textureatlassprite = p_178392_1_.func_110572_b((new ResourceLocation(s1)).toString());
            list.addAll(this.getBlockParts(i, s, textureatlassprite));
        }

        if (list.isEmpty())
        {
            return null;
        }
        else
        {
            map.put("particle", p_178392_2_.isTexturePresent("particle") ? p_178392_2_.func_178308_c("particle") : (String)map.get("layer0"));
            return new ModelBlock((ResourceLocation)null, list, map, false, false, p_178392_2_.getAllTransforms(), p_178392_2_.getOverrides());
        }
    }

    private List<BlockPart> getBlockParts(int tintIndex, String textureIn, TextureAtlasSprite spriteIn)
    {
        Map<EnumFacing, BlockPartFace> map = Maps.<EnumFacing, BlockPartFace>newHashMap();
        map.put(EnumFacing.SOUTH, new BlockPartFace((EnumFacing)null, tintIndex, textureIn, new BlockFaceUV(new float[] {0.0F, 0.0F, 16.0F, 16.0F}, 0)));
        map.put(EnumFacing.NORTH, new BlockPartFace((EnumFacing)null, tintIndex, textureIn, new BlockFaceUV(new float[] {16.0F, 0.0F, 0.0F, 16.0F}, 0)));
        List<BlockPart> list = Lists.<BlockPart>newArrayList();
        list.add(new BlockPart(new Vector3f(0.0F, 0.0F, 7.5F), new Vector3f(16.0F, 16.0F, 8.5F), map, (BlockPartRotation)null, true));
        list.addAll(this.getBlockParts(spriteIn, textureIn, tintIndex));
        return list;
    }

    private List<BlockPart> getBlockParts(TextureAtlasSprite spriteIn, String textureIn, int tintIndexIn)
    {
        float f = (float)spriteIn.getWidth();
        float f1 = (float)spriteIn.getHeight();
        List<BlockPart> list = Lists.<BlockPart>newArrayList();

        for (ItemModelGenerator.Span itemmodelgenerator$span : this.getSpans(spriteIn))
        {
            float f2 = 0.0F;
            float f3 = 0.0F;
            float f4 = 0.0F;
            float f5 = 0.0F;
            float f6 = 0.0F;
            float f7 = 0.0F;
            float f8 = 0.0F;
            float f9 = 0.0F;
            float f10 = 0.0F;
            float f11 = 0.0F;
            float f12 = (float)itemmodelgenerator$span.getMin();
            float f13 = (float)itemmodelgenerator$span.getMax();
            float f14 = (float)itemmodelgenerator$span.getAnchor();
            ItemModelGenerator.SpanFacing itemmodelgenerator$spanfacing = itemmodelgenerator$span.getFacing();

            switch (itemmodelgenerator$spanfacing)
            {
                case UP:
                    f6 = f12;
                    f2 = f12;
                    f4 = f7 = f13 + 1.0F;
                    f8 = f14;
                    f3 = f14;
                    f9 = f14;
                    f5 = f14;
                    f10 = 16.0F / f;
                    f11 = 16.0F / (f1 - 1.0F);
                    break;

                case DOWN:
                    f9 = f14;
                    f8 = f14;
                    f6 = f12;
                    f2 = f12;
                    f4 = f7 = f13 + 1.0F;
                    f3 = f14 + 1.0F;
                    f5 = f14 + 1.0F;
                    f10 = 16.0F / f;
                    f11 = 16.0F / (f1 - 1.0F);
                    break;

                case LEFT:
                    f6 = f14;
                    f2 = f14;
                    f7 = f14;
                    f4 = f14;
                    f9 = f12;
                    f3 = f12;
                    f5 = f8 = f13 + 1.0F;
                    f10 = 16.0F / (f - 1.0F);
                    f11 = 16.0F / f1;
                    break;

                case RIGHT:
                    f7 = f14;
                    f6 = f14;
                    f2 = f14 + 1.0F;
                    f4 = f14 + 1.0F;
                    f9 = f12;
                    f3 = f12;
                    f5 = f8 = f13 + 1.0F;
                    f10 = 16.0F / (f - 1.0F);
                    f11 = 16.0F / f1;
            }

            float f15 = 16.0F / f;
            float f16 = 16.0F / f1;
            f2 = f2 * f15;
            f4 = f4 * f15;
            f3 = f3 * f16;
            f5 = f5 * f16;
            f3 = 16.0F - f3;
            f5 = 16.0F - f5;
            f6 = f6 * f10;
            f7 = f7 * f10;
            f8 = f8 * f11;
            f9 = f9 * f11;
            Map<EnumFacing, BlockPartFace> map = Maps.<EnumFacing, BlockPartFace>newHashMap();
            map.put(itemmodelgenerator$spanfacing.getFacing(), new BlockPartFace((EnumFacing)null, tintIndexIn, textureIn, new BlockFaceUV(new float[] {f6, f8, f7, f9}, 0)));

            switch (itemmodelgenerator$spanfacing)
            {
                case UP:
                    list.add(new BlockPart(new Vector3f(f2, f3, 7.5F), new Vector3f(f4, f3, 8.5F), map, (BlockPartRotation)null, true));
                    break;

                case DOWN:
                    list.add(new BlockPart(new Vector3f(f2, f5, 7.5F), new Vector3f(f4, f5, 8.5F), map, (BlockPartRotation)null, true));
                    break;

                case LEFT:
                    list.add(new BlockPart(new Vector3f(f2, f3, 7.5F), new Vector3f(f2, f5, 8.5F), map, (BlockPartRotation)null, true));
                    break;

                case RIGHT:
                    list.add(new BlockPart(new Vector3f(f4, f3, 7.5F), new Vector3f(f4, f5, 8.5F), map, (BlockPartRotation)null, true));
            }
        }

        return list;
    }

    private List<ItemModelGenerator.Span> getSpans(TextureAtlasSprite spriteIn)
    {
        int i = spriteIn.getWidth();
        int j = spriteIn.getHeight();
        List<ItemModelGenerator.Span> list = Lists.<ItemModelGenerator.Span>newArrayList();

        for (int k = 0; k < spriteIn.getFrameCount(); ++k)
        {
            int[] aint = spriteIn.func_147965_a(k)[0];

            for (int l = 0; l < j; ++l)
            {
                for (int i1 = 0; i1 < i; ++i1)
                {
                    boolean flag = !this.func_178391_a(aint, i1, l, i, j);
                    this.func_178396_a(ItemModelGenerator.SpanFacing.UP, list, aint, i1, l, i, j, flag);
                    this.func_178396_a(ItemModelGenerator.SpanFacing.DOWN, list, aint, i1, l, i, j, flag);
                    this.func_178396_a(ItemModelGenerator.SpanFacing.LEFT, list, aint, i1, l, i, j, flag);
                    this.func_178396_a(ItemModelGenerator.SpanFacing.RIGHT, list, aint, i1, l, i, j, flag);
                }
            }
        }

        return list;
    }

    private void func_178396_a(ItemModelGenerator.SpanFacing p_178396_1_, List<ItemModelGenerator.Span> p_178396_2_, int[] p_178396_3_, int p_178396_4_, int p_178396_5_, int p_178396_6_, int p_178396_7_, boolean p_178396_8_)
    {
        boolean flag = this.func_178391_a(p_178396_3_, p_178396_4_ + p_178396_1_.getXOffset(), p_178396_5_ + p_178396_1_.getYOffset(), p_178396_6_, p_178396_7_) && p_178396_8_;

        if (flag)
        {
            this.createOrExpandSpan(p_178396_2_, p_178396_1_, p_178396_4_, p_178396_5_);
        }
    }

    private void createOrExpandSpan(List<ItemModelGenerator.Span> listSpansIn, ItemModelGenerator.SpanFacing spanFacingIn, int pixelX, int pixelY)
    {
        ItemModelGenerator.Span itemmodelgenerator$span = null;

        for (ItemModelGenerator.Span itemmodelgenerator$span1 : listSpansIn)
        {
            if (itemmodelgenerator$span1.getFacing() == spanFacingIn)
            {
                int i = spanFacingIn.isHorizontal() ? pixelY : pixelX;

                if (itemmodelgenerator$span1.getAnchor() == i)
                {
                    itemmodelgenerator$span = itemmodelgenerator$span1;
                    break;
                }
            }
        }

        int j = spanFacingIn.isHorizontal() ? pixelY : pixelX;
        int k = spanFacingIn.isHorizontal() ? pixelX : pixelY;

        if (itemmodelgenerator$span == null)
        {
            listSpansIn.add(new ItemModelGenerator.Span(spanFacingIn, k, j));
        }
        else
        {
            itemmodelgenerator$span.expand(k);
        }
    }

    private boolean func_178391_a(int[] p_178391_1_, int p_178391_2_, int p_178391_3_, int p_178391_4_, int p_178391_5_)
    {
        if (p_178391_2_ >= 0 && p_178391_3_ >= 0 && p_178391_2_ < p_178391_4_ && p_178391_3_ < p_178391_5_)
        {
            return (p_178391_1_[p_178391_3_ * p_178391_4_ + p_178391_2_] >> 24 & 255) == 0;
        }
        else
        {
            return true;
        }
    }

    static class Span
    {
        private final ItemModelGenerator.SpanFacing spanFacing;
        private int min;
        private int max;
        private final int anchor;

        public Span(ItemModelGenerator.SpanFacing spanFacingIn, int minIn, int maxIn)
        {
            this.spanFacing = spanFacingIn;
            this.min = minIn;
            this.max = minIn;
            this.anchor = maxIn;
        }

        public void expand(int posIn)
        {
            if (posIn < this.min)
            {
                this.min = posIn;
            }
            else if (posIn > this.max)
            {
                this.max = posIn;
            }
        }

        public ItemModelGenerator.SpanFacing getFacing()
        {
            return this.spanFacing;
        }

        public int getMin()
        {
            return this.min;
        }

        public int getMax()
        {
            return this.max;
        }

        public int getAnchor()
        {
            return this.anchor;
        }
    }

    static enum SpanFacing
    {
        UP(EnumFacing.UP, 0, -1),
        DOWN(EnumFacing.DOWN, 0, 1),
        LEFT(EnumFacing.EAST, -1, 0),
        RIGHT(EnumFacing.WEST, 1, 0);

        private final EnumFacing facing;
        private final int xOffset;
        private final int yOffset;

        private SpanFacing(EnumFacing facing, int xOffsetIn, int yOffsetIn)
        {
            this.facing = facing;
            this.xOffset = xOffsetIn;
            this.yOffset = yOffsetIn;
        }

        public EnumFacing getFacing()
        {
            return this.facing;
        }

        public int getXOffset()
        {
            return this.xOffset;
        }

        public int getYOffset()
        {
            return this.yOffset;
        }

        private boolean isHorizontal()
        {
            return this == DOWN || this == UP;
        }
    }
}

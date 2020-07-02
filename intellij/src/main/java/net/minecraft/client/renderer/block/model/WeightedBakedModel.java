package net.minecraft.client.renderer.block.model;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;

public class WeightedBakedModel implements IBakedModel
{
    private final int totalWeight;
    private final List<WeightedBakedModel.WeightedModel> models;
    private final IBakedModel baseModel;

    public WeightedBakedModel(List<WeightedBakedModel.WeightedModel> modelsIn)
    {
        this.models = modelsIn;
        this.totalWeight = WeightedRandom.getTotalWeight(modelsIn);
        this.baseModel = (modelsIn.get(0)).model;
    }

    private IBakedModel func_188627_a(long p_188627_1_)
    {
        return ((WeightedBakedModel.WeightedModel)WeightedRandom.getRandomItem(this.models, Math.abs((int)p_188627_1_ >> 16) % this.totalWeight)).model;
    }

    public List<BakedQuad> func_188616_a(@Nullable IBlockState p_188616_1_, @Nullable EnumFacing p_188616_2_, long p_188616_3_)
    {
        return this.func_188627_a(p_188616_3_).func_188616_a(p_188616_1_, p_188616_2_, p_188616_3_);
    }

    public boolean isAmbientOcclusion()
    {
        return this.baseModel.isAmbientOcclusion();
    }

    public boolean isGui3d()
    {
        return this.baseModel.isGui3d();
    }

    public boolean isBuiltInRenderer()
    {
        return this.baseModel.isBuiltInRenderer();
    }

    public TextureAtlasSprite getParticleTexture()
    {
        return this.baseModel.getParticleTexture();
    }

    public ItemCameraTransforms getItemCameraTransforms()
    {
        return this.baseModel.getItemCameraTransforms();
    }

    public ItemOverrideList getOverrides()
    {
        return this.baseModel.getOverrides();
    }

    public static class Builder
    {
        private final List<WeightedBakedModel.WeightedModel> listItems = Lists.<WeightedBakedModel.WeightedModel>newArrayList();

        public WeightedBakedModel.Builder add(IBakedModel model, int weight)
        {
            this.listItems.add(new WeightedBakedModel.WeightedModel(model, weight));
            return this;
        }

        public WeightedBakedModel func_177676_a()
        {
            Collections.sort(this.listItems);
            return new WeightedBakedModel(this.listItems);
        }

        public IBakedModel func_177675_b()
        {
            return (this.listItems.get(0)).model;
        }
    }

    static class WeightedModel extends WeightedRandom.Item implements Comparable<WeightedBakedModel.WeightedModel>
    {
        protected final IBakedModel model;

        public WeightedModel(IBakedModel modelIn, int itemWeightIn)
        {
            super(itemWeightIn);
            this.model = modelIn;
        }

        public int compareTo(WeightedBakedModel.WeightedModel p_compareTo_1_)
        {
            return ComparisonChain.start().compare(p_compareTo_1_.itemWeight, this.itemWeight).result();
        }

        public String toString()
        {
            return "MyWeighedRandomItem{weight=" + this.itemWeight + ", model=" + this.model + '}';
        }
    }
}

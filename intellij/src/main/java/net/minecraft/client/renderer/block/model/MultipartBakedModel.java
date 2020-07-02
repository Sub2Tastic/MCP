package net.minecraft.client.renderer.block.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class MultipartBakedModel implements IBakedModel
{
    private final Map<Predicate<IBlockState>, IBakedModel> selectors;
    protected final boolean ambientOcclusion;
    protected final boolean gui3D;
    protected final TextureAtlasSprite particleTexture;
    protected final ItemCameraTransforms cameraTransforms;
    protected final ItemOverrideList overrides;

    public MultipartBakedModel(Map<Predicate<IBlockState>, IBakedModel> p_i46536_1_)
    {
        this.selectors = p_i46536_1_;
        IBakedModel ibakedmodel = p_i46536_1_.values().iterator().next();
        this.ambientOcclusion = ibakedmodel.isAmbientOcclusion();
        this.gui3D = ibakedmodel.isGui3d();
        this.particleTexture = ibakedmodel.getParticleTexture();
        this.cameraTransforms = ibakedmodel.getItemCameraTransforms();
        this.overrides = ibakedmodel.getOverrides();
    }

    public List<BakedQuad> func_188616_a(@Nullable IBlockState p_188616_1_, @Nullable EnumFacing p_188616_2_, long p_188616_3_)
    {
        List<BakedQuad> list = Lists.<BakedQuad>newArrayList();

        if (p_188616_1_ != null)
        {
            for (Entry<Predicate<IBlockState>, IBakedModel> entry : this.selectors.entrySet())
            {
                if (((Predicate)entry.getKey()).apply(p_188616_1_))
                {
                    list.addAll((entry.getValue()).func_188616_a(p_188616_1_, p_188616_2_, p_188616_3_++));
                }
            }
        }

        return list;
    }

    public boolean isAmbientOcclusion()
    {
        return this.ambientOcclusion;
    }

    public boolean isGui3d()
    {
        return this.gui3D;
    }

    public boolean isBuiltInRenderer()
    {
        return false;
    }

    public TextureAtlasSprite getParticleTexture()
    {
        return this.particleTexture;
    }

    public ItemCameraTransforms getItemCameraTransforms()
    {
        return this.cameraTransforms;
    }

    public ItemOverrideList getOverrides()
    {
        return this.overrides;
    }

    public static class Builder
    {
        private final Map<Predicate<IBlockState>, IBakedModel> selectors = Maps.<Predicate<IBlockState>, IBakedModel>newLinkedHashMap();

        public void putModel(Predicate<IBlockState> predicate, IBakedModel model)
        {
            this.selectors.put(predicate, model);
        }

        public IBakedModel build()
        {
            return new MultipartBakedModel(this.selectors);
        }
    }
}

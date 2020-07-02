package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SimpleBakedModel implements IBakedModel
{
    protected final List<BakedQuad> generalQuads;
    protected final Map<EnumFacing, List<BakedQuad>> faceQuads;
    protected final boolean ambientOcclusion;
    protected final boolean gui3d;
    protected final TextureAtlasSprite texture;
    protected final ItemCameraTransforms cameraTransforms;
    protected final ItemOverrideList itemOverrideList;

    public SimpleBakedModel(List<BakedQuad> generalQuadsIn, Map<EnumFacing, List<BakedQuad>> faceQuadsIn, boolean ambientOcclusionIn, boolean gui3dIn, TextureAtlasSprite textureIn, ItemCameraTransforms cameraTransformsIn, ItemOverrideList itemOverrideListIn)
    {
        this.generalQuads = generalQuadsIn;
        this.faceQuads = faceQuadsIn;
        this.ambientOcclusion = ambientOcclusionIn;
        this.gui3d = gui3dIn;
        this.texture = textureIn;
        this.cameraTransforms = cameraTransformsIn;
        this.itemOverrideList = itemOverrideListIn;
    }

    public List<BakedQuad> func_188616_a(@Nullable IBlockState p_188616_1_, @Nullable EnumFacing p_188616_2_, long p_188616_3_)
    {
        return p_188616_2_ == null ? this.generalQuads : (List)this.faceQuads.get(p_188616_2_);
    }

    public boolean isAmbientOcclusion()
    {
        return this.ambientOcclusion;
    }

    public boolean isGui3d()
    {
        return this.gui3d;
    }

    public boolean isBuiltInRenderer()
    {
        return false;
    }

    public TextureAtlasSprite getParticleTexture()
    {
        return this.texture;
    }

    public ItemCameraTransforms getItemCameraTransforms()
    {
        return this.cameraTransforms;
    }

    public ItemOverrideList getOverrides()
    {
        return this.itemOverrideList;
    }

    public static class Builder
    {
        private final List<BakedQuad> builderGeneralQuads;
        private final Map<EnumFacing, List<BakedQuad>> builderFaceQuads;
        private final ItemOverrideList builderItemOverrideList;
        private final boolean builderAmbientOcclusion;
        private TextureAtlasSprite builderTexture;
        private final boolean builderGui3d;
        private final ItemCameraTransforms builderCameraTransforms;

        public Builder(ModelBlock model, ItemOverrideList overrides)
        {
            this(model.isAmbientOcclusion(), model.isGui3d(), model.getAllTransforms(), overrides);
        }

        public Builder(IBlockState p_i46989_1_, IBakedModel p_i46989_2_, TextureAtlasSprite p_i46989_3_, BlockPos p_i46989_4_)
        {
            this(p_i46989_2_.isAmbientOcclusion(), p_i46989_2_.isGui3d(), p_i46989_2_.getItemCameraTransforms(), p_i46989_2_.getOverrides());
            this.builderTexture = p_i46989_2_.getParticleTexture();
            long i = MathHelper.getPositionRandom(p_i46989_4_);

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                this.func_188644_a(p_i46989_1_, p_i46989_2_, p_i46989_3_, enumfacing, i);
            }

            this.func_188645_a(p_i46989_1_, p_i46989_2_, p_i46989_3_, i);
        }

        private Builder(boolean ambientOcclusion, boolean gui3d, ItemCameraTransforms transforms, ItemOverrideList overrides)
        {
            this.builderGeneralQuads = Lists.<BakedQuad>newArrayList();
            this.builderFaceQuads = Maps.newEnumMap(EnumFacing.class);

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                this.builderFaceQuads.put(enumfacing, Lists.newArrayList());
            }

            this.builderItemOverrideList = overrides;
            this.builderAmbientOcclusion = ambientOcclusion;
            this.builderGui3d = gui3d;
            this.builderCameraTransforms = transforms;
        }

        private void func_188644_a(IBlockState p_188644_1_, IBakedModel p_188644_2_, TextureAtlasSprite p_188644_3_, EnumFacing p_188644_4_, long p_188644_5_)
        {
            for (BakedQuad bakedquad : p_188644_2_.func_188616_a(p_188644_1_, p_188644_4_, p_188644_5_))
            {
                this.addFaceQuad(p_188644_4_, new BakedQuadRetextured(bakedquad, p_188644_3_));
            }
        }

        private void func_188645_a(IBlockState p_188645_1_, IBakedModel p_188645_2_, TextureAtlasSprite p_188645_3_, long p_188645_4_)
        {
            for (BakedQuad bakedquad : p_188645_2_.func_188616_a(p_188645_1_, (EnumFacing)null, p_188645_4_))
            {
                this.addGeneralQuad(new BakedQuadRetextured(bakedquad, p_188645_3_));
            }
        }

        public SimpleBakedModel.Builder addFaceQuad(EnumFacing facing, BakedQuad quad)
        {
            (this.builderFaceQuads.get(facing)).add(quad);
            return this;
        }

        public SimpleBakedModel.Builder addGeneralQuad(BakedQuad quad)
        {
            this.builderGeneralQuads.add(quad);
            return this;
        }

        public SimpleBakedModel.Builder setTexture(TextureAtlasSprite texture)
        {
            this.builderTexture = texture;
            return this;
        }

        public IBakedModel build()
        {
            if (this.builderTexture == null)
            {
                throw new RuntimeException("Missing particle!");
            }
            else
            {
                return new SimpleBakedModel(this.builderGeneralQuads, this.builderFaceQuads, this.builderAmbientOcclusion, this.builderGui3d, this.builderTexture, this.builderCameraTransforms, this.builderItemOverrideList);
            }
        }
    }
}

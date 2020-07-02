package net.minecraft.client.renderer.block.model;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public interface IBakedModel
{
    List<BakedQuad> func_188616_a(IBlockState p_188616_1_, EnumFacing p_188616_2_, long p_188616_3_);

    boolean isAmbientOcclusion();

    boolean isGui3d();

    boolean isBuiltInRenderer();

    TextureAtlasSprite getParticleTexture();

    ItemCameraTransforms getItemCameraTransforms();

    ItemOverrideList getOverrides();
}

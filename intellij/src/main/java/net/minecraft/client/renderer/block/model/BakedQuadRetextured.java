package net.minecraft.client.renderer.block.model;

import java.util.Arrays;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BakedQuadRetextured extends BakedQuad
{
    private final TextureAtlasSprite field_178218_d;

    public BakedQuadRetextured(BakedQuad p_i46217_1_, TextureAtlasSprite p_i46217_2_)
    {
        super(Arrays.copyOf(p_i46217_1_.getVertexData(), p_i46217_1_.getVertexData().length), p_i46217_1_.tintIndex, FaceBakery.getFacingFromVertexData(p_i46217_1_.getVertexData()), p_i46217_1_.func_187508_a());
        this.field_178218_d = p_i46217_2_;
        this.func_178217_e();
    }

    private void func_178217_e()
    {
        for (int i = 0; i < 4; ++i)
        {
            int j = 7 * i;
            this.vertexData[j + 4] = Float.floatToRawIntBits(this.field_178218_d.getInterpolatedU((double)this.sprite.func_188537_a(Float.intBitsToFloat(this.vertexData[j + 4]))));
            this.vertexData[j + 4 + 1] = Float.floatToRawIntBits(this.field_178218_d.getInterpolatedV((double)this.sprite.func_188536_b(Float.intBitsToFloat(this.vertexData[j + 4 + 1]))));
        }
    }
}

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;

public class LayerSaddle implements LayerRenderer<EntityPig>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/pig/pig_saddle.png");
    private final RenderPig field_177156_b;
    private final ModelPig pigModel = new ModelPig(0.5F);

    public LayerSaddle(RenderPig p_i46113_1_)
    {
        this.field_177156_b = p_i46113_1_;
    }

    public void func_177141_a(EntityPig p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
    {
        if (p_177141_1_.getSaddled())
        {
            this.field_177156_b.func_110776_a(TEXTURE);
            this.pigModel.func_178686_a(this.field_177156_b.func_177087_b());
            this.pigModel.func_78088_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
        }
    }

    public boolean func_177142_b()
    {
        return false;
    }
}

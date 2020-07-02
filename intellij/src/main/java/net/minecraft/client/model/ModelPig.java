package net.minecraft.client.model;

public class ModelPig extends ModelQuadruped
{
    public ModelPig()
    {
        this(0.0F);
    }

    public ModelPig(float scale)
    {
        super(6, scale);
        this.headModel.setTextureOffset(16, 16).func_78790_a(-2.0F, 0.0F, -9.0F, 4, 3, 1, scale);
        this.field_78145_g = 4.0F;
    }
}

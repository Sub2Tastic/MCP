package net.minecraft.client.model;

public class ModelCow extends ModelQuadruped
{
    public ModelCow()
    {
        super(12, 0.0F);
        this.headModel = new ModelRenderer(this, 0, 0);
        this.headModel.func_78790_a(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
        this.headModel.setRotationPoint(0.0F, 4.0F, -8.0F);
        this.headModel.setTextureOffset(22, 0).func_78790_a(-5.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
        this.headModel.setTextureOffset(22, 0).func_78790_a(4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
        this.body = new ModelRenderer(this, 18, 4);
        this.body.func_78790_a(-6.0F, -10.0F, -7.0F, 12, 18, 10, 0.0F);
        this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
        this.body.setTextureOffset(52, 0).func_78789_a(-2.0F, 2.0F, -8.0F, 4, 6, 1);
        --this.legBackRight.rotationPointX;
        ++this.legBackLeft.rotationPointX;
        this.legBackRight.rotationPointZ += 0.0F;
        this.legBackLeft.rotationPointZ += 0.0F;
        --this.legFrontRight.rotationPointX;
        ++this.legFrontLeft.rotationPointX;
        --this.legFrontRight.rotationPointZ;
        --this.legFrontLeft.rotationPointZ;
        this.field_78151_h += 2.0F;
    }
}

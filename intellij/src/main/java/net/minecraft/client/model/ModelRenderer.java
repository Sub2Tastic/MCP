package net.minecraft.client.model;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

public class ModelRenderer
{
    public float textureWidth;
    public float textureHeight;
    private int textureOffsetX;
    private int textureOffsetY;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    private boolean field_78812_q;
    private int field_78811_r;
    public boolean mirror;
    public boolean showModel;
    public boolean field_78807_k;
    public List<ModelBox> cubeList;
    public List<ModelRenderer> childModels;
    public final String field_78802_n;
    private final ModelBase field_78810_s;
    public float field_82906_o;
    public float field_82908_p;
    public float field_82907_q;

    public ModelRenderer(ModelBase p_i1172_1_, String p_i1172_2_)
    {
        this.textureWidth = 64.0F;
        this.textureHeight = 32.0F;
        this.showModel = true;
        this.cubeList = Lists.<ModelBox>newArrayList();
        this.field_78810_s = p_i1172_1_;
        p_i1172_1_.field_78092_r.add(this);
        this.field_78802_n = p_i1172_2_;
        this.setTextureSize(p_i1172_1_.textureWidth, p_i1172_1_.textureHeight);
    }

    public ModelRenderer(ModelBase model)
    {
        this(model, (String)null);
    }

    public ModelRenderer(ModelBase model, int texOffX, int texOffY)
    {
        this(model);
        this.setTextureOffset(texOffX, texOffY);
    }

    /**
     * Sets the current box's rotation points and rotation angles to another box.
     */
    public void addChild(ModelRenderer renderer)
    {
        if (this.childModels == null)
        {
            this.childModels = Lists.<ModelRenderer>newArrayList();
        }

        this.childModels.add(renderer);
    }

    public ModelRenderer setTextureOffset(int x, int y)
    {
        this.textureOffsetX = x;
        this.textureOffsetY = y;
        return this;
    }

    public ModelRenderer func_78786_a(String p_78786_1_, float p_78786_2_, float p_78786_3_, float p_78786_4_, int p_78786_5_, int p_78786_6_, int p_78786_7_)
    {
        p_78786_1_ = this.field_78802_n + "." + p_78786_1_;
        TextureOffset textureoffset = this.field_78810_s.func_78084_a(p_78786_1_);
        this.setTextureOffset(textureoffset.field_78783_a, textureoffset.field_78782_b);
        this.cubeList.add((new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_78786_2_, p_78786_3_, p_78786_4_, p_78786_5_, p_78786_6_, p_78786_7_, 0.0F)).func_78244_a(p_78786_1_));
        return this;
    }

    public ModelRenderer func_78789_a(float p_78789_1_, float p_78789_2_, float p_78789_3_, int p_78789_4_, int p_78789_5_, int p_78789_6_)
    {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_78789_1_, p_78789_2_, p_78789_3_, p_78789_4_, p_78789_5_, p_78789_6_, 0.0F));
        return this;
    }

    public ModelRenderer func_178769_a(float p_178769_1_, float p_178769_2_, float p_178769_3_, int p_178769_4_, int p_178769_5_, int p_178769_6_, boolean p_178769_7_)
    {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_178769_1_, p_178769_2_, p_178769_3_, p_178769_4_, p_178769_5_, p_178769_6_, 0.0F, p_178769_7_));
        return this;
    }

    public void func_78790_a(float p_78790_1_, float p_78790_2_, float p_78790_3_, int p_78790_4_, int p_78790_5_, int p_78790_6_, float p_78790_7_)
    {
        this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_78790_1_, p_78790_2_, p_78790_3_, p_78790_4_, p_78790_5_, p_78790_6_, p_78790_7_));
    }

    public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn)
    {
        this.rotationPointX = rotationPointXIn;
        this.rotationPointY = rotationPointYIn;
        this.rotationPointZ = rotationPointZIn;
    }

    public void func_78785_a(float p_78785_1_)
    {
        if (!this.field_78807_k)
        {
            if (this.showModel)
            {
                if (!this.field_78812_q)
                {
                    this.func_78788_d(p_78785_1_);
                }

                GlStateManager.func_179109_b(this.field_82906_o, this.field_82908_p, this.field_82907_q);

                if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F)
                {
                    if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F)
                    {
                        GlStateManager.func_179148_o(this.field_78811_r);

                        if (this.childModels != null)
                        {
                            for (int k = 0; k < this.childModels.size(); ++k)
                            {
                                ((ModelRenderer)this.childModels.get(k)).func_78785_a(p_78785_1_);
                            }
                        }
                    }
                    else
                    {
                        GlStateManager.func_179109_b(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
                        GlStateManager.func_179148_o(this.field_78811_r);

                        if (this.childModels != null)
                        {
                            for (int j = 0; j < this.childModels.size(); ++j)
                            {
                                ((ModelRenderer)this.childModels.get(j)).func_78785_a(p_78785_1_);
                            }
                        }

                        GlStateManager.func_179109_b(-this.rotationPointX * p_78785_1_, -this.rotationPointY * p_78785_1_, -this.rotationPointZ * p_78785_1_);
                    }
                }
                else
                {
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179109_b(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);

                    if (this.rotateAngleZ != 0.0F)
                    {
                        GlStateManager.func_179114_b(this.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
                    }

                    if (this.rotateAngleY != 0.0F)
                    {
                        GlStateManager.func_179114_b(this.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
                    }

                    if (this.rotateAngleX != 0.0F)
                    {
                        GlStateManager.func_179114_b(this.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
                    }

                    GlStateManager.func_179148_o(this.field_78811_r);

                    if (this.childModels != null)
                    {
                        for (int i = 0; i < this.childModels.size(); ++i)
                        {
                            ((ModelRenderer)this.childModels.get(i)).func_78785_a(p_78785_1_);
                        }
                    }

                    GlStateManager.func_179121_F();
                }

                GlStateManager.func_179109_b(-this.field_82906_o, -this.field_82908_p, -this.field_82907_q);
            }
        }
    }

    public void func_78791_b(float p_78791_1_)
    {
        if (!this.field_78807_k)
        {
            if (this.showModel)
            {
                if (!this.field_78812_q)
                {
                    this.func_78788_d(p_78791_1_);
                }

                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b(this.rotationPointX * p_78791_1_, this.rotationPointY * p_78791_1_, this.rotationPointZ * p_78791_1_);

                if (this.rotateAngleY != 0.0F)
                {
                    GlStateManager.func_179114_b(this.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
                }

                if (this.rotateAngleX != 0.0F)
                {
                    GlStateManager.func_179114_b(this.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
                }

                if (this.rotateAngleZ != 0.0F)
                {
                    GlStateManager.func_179114_b(this.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
                }

                GlStateManager.func_179148_o(this.field_78811_r);
                GlStateManager.func_179121_F();
            }
        }
    }

    public void func_78794_c(float p_78794_1_)
    {
        if (!this.field_78807_k)
        {
            if (this.showModel)
            {
                if (!this.field_78812_q)
                {
                    this.func_78788_d(p_78794_1_);
                }

                if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F)
                {
                    if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F)
                    {
                        GlStateManager.func_179109_b(this.rotationPointX * p_78794_1_, this.rotationPointY * p_78794_1_, this.rotationPointZ * p_78794_1_);
                    }
                }
                else
                {
                    GlStateManager.func_179109_b(this.rotationPointX * p_78794_1_, this.rotationPointY * p_78794_1_, this.rotationPointZ * p_78794_1_);

                    if (this.rotateAngleZ != 0.0F)
                    {
                        GlStateManager.func_179114_b(this.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
                    }

                    if (this.rotateAngleY != 0.0F)
                    {
                        GlStateManager.func_179114_b(this.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
                    }

                    if (this.rotateAngleX != 0.0F)
                    {
                        GlStateManager.func_179114_b(this.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
                    }
                }
            }
        }
    }

    private void func_78788_d(float p_78788_1_)
    {
        this.field_78811_r = GLAllocation.func_74526_a(1);
        GlStateManager.func_187423_f(this.field_78811_r, 4864);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();

        for (int i = 0; i < this.cubeList.size(); ++i)
        {
            ((ModelBox)this.cubeList.get(i)).func_178780_a(bufferbuilder, p_78788_1_);
        }

        GlStateManager.func_187415_K();
        this.field_78812_q = true;
    }

    /**
     * Returns the model renderer with the new texture parameters.
     */
    public ModelRenderer setTextureSize(int textureWidthIn, int textureHeightIn)
    {
        this.textureWidth = (float)textureWidthIn;
        this.textureHeight = (float)textureHeightIn;
        return this;
    }
}

package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;

public class TextureAtlasSprite
{
    private final String field_110984_i;
    protected List<int[][]> field_110976_a = Lists.<int[][]>newArrayList();
    protected int[][] field_176605_b;
    private AnimationMetadataSection animationMetadata;
    protected boolean field_130222_e;
    protected int x;
    protected int y;
    protected int field_130223_c;
    protected int field_130224_d;
    private float minU;
    private float maxU;
    private float minV;
    private float maxV;
    protected int frameCounter;
    protected int tickCounter;

    protected TextureAtlasSprite(String p_i1282_1_)
    {
        this.field_110984_i = p_i1282_1_;
    }

    protected static TextureAtlasSprite func_176604_a(ResourceLocation p_176604_0_)
    {
        return new TextureAtlasSprite(p_176604_0_.toString());
    }

    public void func_110971_a(int p_110971_1_, int p_110971_2_, int p_110971_3_, int p_110971_4_, boolean p_110971_5_)
    {
        this.x = p_110971_3_;
        this.y = p_110971_4_;
        this.field_130222_e = p_110971_5_;
        float f = (float)(0.009999999776482582D / (double)p_110971_1_);
        float f1 = (float)(0.009999999776482582D / (double)p_110971_2_);
        this.minU = (float)p_110971_3_ / (float)((double)p_110971_1_) + f;
        this.maxU = (float)(p_110971_3_ + this.field_130223_c) / (float)((double)p_110971_1_) - f;
        this.minV = (float)p_110971_4_ / (float)p_110971_2_ + f1;
        this.maxV = (float)(p_110971_4_ + this.field_130224_d) / (float)p_110971_2_ - f1;
    }

    public void func_94217_a(TextureAtlasSprite p_94217_1_)
    {
        this.x = p_94217_1_.x;
        this.y = p_94217_1_.y;
        this.field_130223_c = p_94217_1_.field_130223_c;
        this.field_130224_d = p_94217_1_.field_130224_d;
        this.field_130222_e = p_94217_1_.field_130222_e;
        this.minU = p_94217_1_.minU;
        this.maxU = p_94217_1_.maxU;
        this.minV = p_94217_1_.minV;
        this.maxV = p_94217_1_.maxV;
    }

    public int func_130010_a()
    {
        return this.x;
    }

    public int func_110967_i()
    {
        return this.y;
    }

    /**
     * Returns the width of the icon, in pixels.
     */
    public int getWidth()
    {
        return this.field_130223_c;
    }

    /**
     * Returns the height of the icon, in pixels.
     */
    public int getHeight()
    {
        return this.field_130224_d;
    }

    /**
     * Returns the minimum U coordinate to use when rendering with this icon.
     */
    public float getMinU()
    {
        return this.minU;
    }

    /**
     * Returns the maximum U coordinate to use when rendering with this icon.
     */
    public float getMaxU()
    {
        return this.maxU;
    }

    /**
     * Gets a U coordinate on the icon. 0 returns uMin and 16 returns uMax. Other arguments return in-between values.
     */
    public float getInterpolatedU(double u)
    {
        float f = this.maxU - this.minU;
        return this.minU + f * (float)u / 16.0F;
    }

    public float func_188537_a(float p_188537_1_)
    {
        float f = this.maxU - this.minU;
        return (p_188537_1_ - this.minU) / f * 16.0F;
    }

    /**
     * Returns the minimum V coordinate to use when rendering with this icon.
     */
    public float getMinV()
    {
        return this.minV;
    }

    /**
     * Returns the maximum V coordinate to use when rendering with this icon.
     */
    public float getMaxV()
    {
        return this.maxV;
    }

    /**
     * Gets a V coordinate on the icon. 0 returns vMin and 16 returns vMax. Other arguments return in-between values.
     */
    public float getInterpolatedV(double v)
    {
        float f = this.maxV - this.minV;
        return this.minV + f * (float)v / 16.0F;
    }

    public float func_188536_b(float p_188536_1_)
    {
        float f = this.maxV - this.minV;
        return (p_188536_1_ - this.minV) / f * 16.0F;
    }

    public String func_94215_i()
    {
        return this.field_110984_i;
    }

    public void updateAnimation()
    {
        ++this.tickCounter;

        if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter))
        {
            int i = this.animationMetadata.getFrameIndex(this.frameCounter);
            int j = this.animationMetadata.getFrameCount() == 0 ? this.field_110976_a.size() : this.animationMetadata.getFrameCount();
            this.frameCounter = (this.frameCounter + 1) % j;
            this.tickCounter = 0;
            int k = this.animationMetadata.getFrameIndex(this.frameCounter);

            if (i != k && k >= 0 && k < this.field_110976_a.size())
            {
                TextureUtil.func_147955_a(this.field_110976_a.get(k), this.field_130223_c, this.field_130224_d, this.x, this.y, false, false);
            }
        }
        else if (this.animationMetadata.isInterpolate())
        {
            this.func_180599_n();
        }
    }

    private void func_180599_n()
    {
        double d0 = 1.0D - (double)this.tickCounter / (double)this.animationMetadata.getFrameTimeSingle(this.frameCounter);
        int i = this.animationMetadata.getFrameIndex(this.frameCounter);
        int j = this.animationMetadata.getFrameCount() == 0 ? this.field_110976_a.size() : this.animationMetadata.getFrameCount();
        int k = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % j);

        if (i != k && k >= 0 && k < this.field_110976_a.size())
        {
            int[][] aint = this.field_110976_a.get(i);
            int[][] aint1 = this.field_110976_a.get(k);

            if (this.field_176605_b == null || this.field_176605_b.length != aint.length)
            {
                this.field_176605_b = new int[aint.length][];
            }

            for (int l = 0; l < aint.length; ++l)
            {
                if (this.field_176605_b[l] == null)
                {
                    this.field_176605_b[l] = new int[aint[l].length];
                }

                if (l < aint1.length && aint1[l].length == aint[l].length)
                {
                    for (int i1 = 0; i1 < aint[l].length; ++i1)
                    {
                        int j1 = aint[l][i1];
                        int k1 = aint1[l][i1];
                        int l1 = this.func_188535_a(d0, j1 >> 16 & 255, k1 >> 16 & 255);
                        int i2 = this.func_188535_a(d0, j1 >> 8 & 255, k1 >> 8 & 255);
                        int j2 = this.func_188535_a(d0, j1 & 255, k1 & 255);
                        this.field_176605_b[l][i1] = j1 & -16777216 | l1 << 16 | i2 << 8 | j2;
                    }
                }
            }

            TextureUtil.func_147955_a(this.field_176605_b, this.field_130223_c, this.field_130224_d, this.x, this.y, false, false);
        }
    }

    private int func_188535_a(double p_188535_1_, int p_188535_3_, int p_188535_4_)
    {
        return (int)(p_188535_1_ * (double)p_188535_3_ + (1.0D - p_188535_1_) * (double)p_188535_4_);
    }

    public int[][] func_147965_a(int p_147965_1_)
    {
        return this.field_110976_a.get(p_147965_1_);
    }

    public int getFrameCount()
    {
        return this.field_110976_a.size();
    }

    public void func_110966_b(int p_110966_1_)
    {
        this.field_130223_c = p_110966_1_;
    }

    public void func_110969_c(int p_110969_1_)
    {
        this.field_130224_d = p_110969_1_;
    }

    public void func_188538_a(PngSizeInfo p_188538_1_, boolean p_188538_2_) throws IOException
    {
        this.func_130102_n();
        this.field_130223_c = p_188538_1_.width;
        this.field_130224_d = p_188538_1_.height;

        if (p_188538_2_)
        {
            this.field_130224_d = this.field_130223_c;
        }
        else if (p_188538_1_.height != p_188538_1_.width)
        {
            throw new RuntimeException("broken aspect ratio and not an animation");
        }
    }

    public void func_188539_a(IResource p_188539_1_, int p_188539_2_) throws IOException
    {
        BufferedImage bufferedimage = TextureUtil.func_177053_a(p_188539_1_.func_110527_b());
        AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)p_188539_1_.func_110526_a("animation");
        int[][] aint = new int[p_188539_2_][];
        aint[0] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
        bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[0], 0, bufferedimage.getWidth());

        if (animationmetadatasection == null)
        {
            this.field_110976_a.add(aint);
        }
        else
        {
            int i = bufferedimage.getHeight() / this.field_130223_c;

            if (animationmetadatasection.getFrameCount() > 0)
            {
                Iterator lvt_7_1_ = animationmetadatasection.getFrameIndexSet().iterator();

                while (lvt_7_1_.hasNext())
                {
                    int j = ((Integer)lvt_7_1_.next()).intValue();

                    if (j >= i)
                    {
                        throw new RuntimeException("invalid frameindex " + j);
                    }

                    this.func_130099_d(j);
                    this.field_110976_a.set(j, func_147962_a(aint, this.field_130223_c, this.field_130223_c, j));
                }

                this.animationMetadata = animationmetadatasection;
            }
            else
            {
                List<AnimationFrame> list = Lists.<AnimationFrame>newArrayList();

                for (int k = 0; k < i; ++k)
                {
                    this.field_110976_a.add(func_147962_a(aint, this.field_130223_c, this.field_130223_c, k));
                    list.add(new AnimationFrame(k, -1));
                }

                this.animationMetadata = new AnimationMetadataSection(list, this.field_130223_c, this.field_130224_d, animationmetadatasection.getFrameTime(), animationmetadatasection.isInterpolate());
            }
        }
    }

    public void func_147963_d(int p_147963_1_)
    {
        List<int[][]> list = Lists.<int[][]>newArrayList();

        for (int i = 0; i < this.field_110976_a.size(); ++i)
        {
            final int[][] aint = this.field_110976_a.get(i);

            if (aint != null)
            {
                try
                {
                    list.add(TextureUtil.func_147949_a(p_147963_1_, this.field_130223_c, aint));
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Generating mipmaps for frame");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Frame being iterated");
                    crashreportcategory.addDetail("Frame index", Integer.valueOf(i));
                    crashreportcategory.addDetail("Frame sizes", new ICrashReportDetail<String>()
                    {
                        public String call() throws Exception
                        {
                            StringBuilder stringbuilder = new StringBuilder();

                            for (int[] aint1 : aint)
                            {
                                if (stringbuilder.length() > 0)
                                {
                                    stringbuilder.append(", ");
                                }

                                stringbuilder.append(aint1 == null ? "null" : aint1.length);
                            }

                            return stringbuilder.toString();
                        }
                    });
                    throw new ReportedException(crashreport);
                }
            }
        }

        this.func_110968_a(list);
    }

    private void func_130099_d(int p_130099_1_)
    {
        if (this.field_110976_a.size() <= p_130099_1_)
        {
            for (int i = this.field_110976_a.size(); i <= p_130099_1_; ++i)
            {
                this.field_110976_a.add(null);
            }
        }
    }

    private static int[][] func_147962_a(int[][] p_147962_0_, int p_147962_1_, int p_147962_2_, int p_147962_3_)
    {
        int[][] aint = new int[p_147962_0_.length][];

        for (int i = 0; i < p_147962_0_.length; ++i)
        {
            int[] aint1 = p_147962_0_[i];

            if (aint1 != null)
            {
                aint[i] = new int[(p_147962_1_ >> i) * (p_147962_2_ >> i)];
                System.arraycopy(aint1, p_147962_3_ * aint[i].length, aint[i], 0, aint[i].length);
            }
        }

        return aint;
    }

    public void func_130103_l()
    {
        this.field_110976_a.clear();
    }

    public boolean hasAnimationMetadata()
    {
        return this.animationMetadata != null;
    }

    public void func_110968_a(List<int[][]> p_110968_1_)
    {
        this.field_110976_a = p_110968_1_;
    }

    private void func_130102_n()
    {
        this.animationMetadata = null;
        this.func_110968_a(Lists.newArrayList());
        this.frameCounter = 0;
        this.tickCounter = 0;
    }

    public String toString()
    {
        return "TextureAtlasSprite{name='" + this.field_110984_i + '\'' + ", frameCount=" + this.field_110976_a.size() + ", rotated=" + this.field_130222_e + ", x=" + this.x + ", y=" + this.y + ", height=" + this.field_130224_d + ", width=" + this.field_130223_c + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
    }
}

package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.util.math.MathHelper;

public class Stitcher
{
    private final int mipmapLevelStitcher;
    private final Set<Stitcher.Holder> setStitchHolders = Sets.<Stitcher.Holder>newHashSetWithExpectedSize(256);
    private final List<Stitcher.Slot> stitchSlots = Lists.<Stitcher.Slot>newArrayListWithCapacity(256);
    private int currentWidth;
    private int currentHeight;
    private final int maxWidth;
    private final int maxHeight;
    private final int field_94323_h;

    public Stitcher(int p_i46542_1_, int p_i46542_2_, int p_i46542_3_, int p_i46542_4_)
    {
        this.mipmapLevelStitcher = p_i46542_4_;
        this.maxWidth = p_i46542_1_;
        this.maxHeight = p_i46542_2_;
        this.field_94323_h = p_i46542_3_;
    }

    public int getCurrentWidth()
    {
        return this.currentWidth;
    }

    public int getCurrentHeight()
    {
        return this.currentHeight;
    }

    public void func_110934_a(TextureAtlasSprite p_110934_1_)
    {
        Stitcher.Holder stitcher$holder = new Stitcher.Holder(p_110934_1_, this.mipmapLevelStitcher);

        if (this.field_94323_h > 0)
        {
            stitcher$holder.func_94196_a(this.field_94323_h);
        }

        this.setStitchHolders.add(stitcher$holder);
    }

    public void doStitch()
    {
        Stitcher.Holder[] astitcher$holder = (Stitcher.Holder[])this.setStitchHolders.toArray(new Stitcher.Holder[this.setStitchHolders.size()]);
        Arrays.sort((Object[])astitcher$holder);

        for (Stitcher.Holder stitcher$holder : astitcher$holder)
        {
            if (!this.allocateSlot(stitcher$holder))
            {
                String s = String.format("Unable to fit: %s - size: %dx%d - Maybe try a lowerresolution resourcepack?", stitcher$holder.func_98150_a().func_94215_i(), stitcher$holder.func_98150_a().getWidth(), stitcher$holder.func_98150_a().getHeight());
                throw new StitcherException(stitcher$holder, s);
            }
        }

        this.currentWidth = MathHelper.smallestEncompassingPowerOfTwo(this.currentWidth);
        this.currentHeight = MathHelper.smallestEncompassingPowerOfTwo(this.currentHeight);
    }

    public List<TextureAtlasSprite> func_94309_g()
    {
        List<Stitcher.Slot> list = Lists.<Stitcher.Slot>newArrayList();

        for (Stitcher.Slot stitcher$slot : this.stitchSlots)
        {
            stitcher$slot.func_94184_a(list);
        }

        List<TextureAtlasSprite> list1 = Lists.<TextureAtlasSprite>newArrayList();

        for (Stitcher.Slot stitcher$slot1 : list)
        {
            Stitcher.Holder stitcher$holder = stitcher$slot1.getStitchHolder();
            TextureAtlasSprite textureatlassprite = stitcher$holder.func_98150_a();
            textureatlassprite.func_110971_a(this.currentWidth, this.currentHeight, stitcher$slot1.getOriginX(), stitcher$slot1.getOriginY(), stitcher$holder.func_94195_e());
            list1.add(textureatlassprite);
        }

        return list1;
    }

    private static int getMipmapDimension(int dimensionIn, int mipmapLevelIn)
    {
        return (dimensionIn >> mipmapLevelIn) + ((dimensionIn & (1 << mipmapLevelIn) - 1) == 0 ? 0 : 1) << mipmapLevelIn;
    }

    /**
     * Attempts to find space for specified tile
     */
    private boolean allocateSlot(Stitcher.Holder holderIn)
    {
        TextureAtlasSprite textureatlassprite = holderIn.func_98150_a();
        boolean flag = textureatlassprite.getWidth() != textureatlassprite.getHeight();

        for (int i = 0; i < this.stitchSlots.size(); ++i)
        {
            if (((Stitcher.Slot)this.stitchSlots.get(i)).addSlot(holderIn))
            {
                return true;
            }

            if (flag)
            {
                holderIn.func_94194_d();

                if (((Stitcher.Slot)this.stitchSlots.get(i)).addSlot(holderIn))
                {
                    return true;
                }

                holderIn.func_94194_d();
            }
        }

        return this.expandAndAllocateSlot(holderIn);
    }

    /**
     * Expand stitched texture in order to make space for specified tile
     */
    private boolean expandAndAllocateSlot(Stitcher.Holder holderIn)
    {
        int i = Math.min(holderIn.func_94197_a(), holderIn.func_94199_b());
        int j = Math.max(holderIn.func_94197_a(), holderIn.func_94199_b());
        int k = MathHelper.smallestEncompassingPowerOfTwo(this.currentWidth);
        int l = MathHelper.smallestEncompassingPowerOfTwo(this.currentHeight);
        int i1 = MathHelper.smallestEncompassingPowerOfTwo(this.currentWidth + i);
        int j1 = MathHelper.smallestEncompassingPowerOfTwo(this.currentHeight + i);
        boolean flag1 = i1 <= this.maxWidth;
        boolean flag2 = j1 <= this.maxHeight;

        if (!flag1 && !flag2)
        {
            return false;
        }
        else
        {
            boolean flag3 = flag1 && k != i1;
            boolean flag4 = flag2 && l != j1;
            boolean flag;

            if (flag3 ^ flag4)
            {
                flag = flag3;
            }
            else
            {
                flag = flag1 && k <= l;
            }

            Stitcher.Slot stitcher$slot;

            if (flag)
            {
                if (holderIn.func_94197_a() > holderIn.func_94199_b())
                {
                    holderIn.func_94194_d();
                }

                if (this.currentHeight == 0)
                {
                    this.currentHeight = holderIn.func_94199_b();
                }

                stitcher$slot = new Stitcher.Slot(this.currentWidth, 0, holderIn.func_94197_a(), this.currentHeight);
                this.currentWidth += holderIn.func_94197_a();
            }
            else
            {
                stitcher$slot = new Stitcher.Slot(0, this.currentHeight, this.currentWidth, holderIn.func_94199_b());
                this.currentHeight += holderIn.func_94199_b();
            }

            stitcher$slot.addSlot(holderIn);
            this.stitchSlots.add(stitcher$slot);
            return true;
        }
    }

    public static class Holder implements Comparable<Stitcher.Holder>
    {
        private final TextureAtlasSprite field_98151_a;
        private final int width;
        private final int height;
        private final int field_147968_d;
        private boolean field_94202_e;
        private float field_94205_a = 1.0F;

        public Holder(TextureAtlasSprite p_i45094_1_, int p_i45094_2_)
        {
            this.field_98151_a = p_i45094_1_;
            this.width = p_i45094_1_.getWidth();
            this.height = p_i45094_1_.getHeight();
            this.field_147968_d = p_i45094_2_;
            this.field_94202_e = Stitcher.getMipmapDimension(this.height, p_i45094_2_) > Stitcher.getMipmapDimension(this.width, p_i45094_2_);
        }

        public TextureAtlasSprite func_98150_a()
        {
            return this.field_98151_a;
        }

        public int func_94197_a()
        {
            int i = this.field_94202_e ? this.height : this.width;
            return Stitcher.getMipmapDimension((int)((float)i * this.field_94205_a), this.field_147968_d);
        }

        public int func_94199_b()
        {
            int i = this.field_94202_e ? this.width : this.height;
            return Stitcher.getMipmapDimension((int)((float)i * this.field_94205_a), this.field_147968_d);
        }

        public void func_94194_d()
        {
            this.field_94202_e = !this.field_94202_e;
        }

        public boolean func_94195_e()
        {
            return this.field_94202_e;
        }

        public void func_94196_a(int p_94196_1_)
        {
            if (this.width > p_94196_1_ && this.height > p_94196_1_)
            {
                this.field_94205_a = (float)p_94196_1_ / (float)Math.min(this.width, this.height);
            }
        }

        public String toString()
        {
            return "Holder{width=" + this.width + ", height=" + this.height + '}';
        }

        public int compareTo(Stitcher.Holder p_compareTo_1_)
        {
            int i;

            if (this.func_94199_b() == p_compareTo_1_.func_94199_b())
            {
                if (this.func_94197_a() == p_compareTo_1_.func_94197_a())
                {
                    if (this.field_98151_a.func_94215_i() == null)
                    {
                        return p_compareTo_1_.field_98151_a.func_94215_i() == null ? 0 : -1;
                    }

                    return this.field_98151_a.func_94215_i().compareTo(p_compareTo_1_.field_98151_a.func_94215_i());
                }

                i = this.func_94197_a() < p_compareTo_1_.func_94197_a() ? 1 : -1;
            }
            else
            {
                i = this.func_94199_b() < p_compareTo_1_.func_94199_b() ? 1 : -1;
            }

            return i;
        }
    }

    public static class Slot
    {
        private final int originX;
        private final int originY;
        private final int width;
        private final int height;
        private List<Stitcher.Slot> subSlots;
        private Stitcher.Holder holder;

        public Slot(int originXIn, int originYIn, int widthIn, int heightIn)
        {
            this.originX = originXIn;
            this.originY = originYIn;
            this.width = widthIn;
            this.height = heightIn;
        }

        public Stitcher.Holder getStitchHolder()
        {
            return this.holder;
        }

        public int getOriginX()
        {
            return this.originX;
        }

        public int getOriginY()
        {
            return this.originY;
        }

        public boolean addSlot(Stitcher.Holder holderIn)
        {
            if (this.holder != null)
            {
                return false;
            }
            else
            {
                int i = holderIn.func_94197_a();
                int j = holderIn.func_94199_b();

                if (i <= this.width && j <= this.height)
                {
                    if (i == this.width && j == this.height)
                    {
                        this.holder = holderIn;
                        return true;
                    }
                    else
                    {
                        if (this.subSlots == null)
                        {
                            this.subSlots = Lists.<Stitcher.Slot>newArrayListWithCapacity(1);
                            this.subSlots.add(new Stitcher.Slot(this.originX, this.originY, i, j));
                            int k = this.width - i;
                            int l = this.height - j;

                            if (l > 0 && k > 0)
                            {
                                int i1 = Math.max(this.height, k);
                                int j1 = Math.max(this.width, l);

                                if (i1 >= j1)
                                {
                                    this.subSlots.add(new Stitcher.Slot(this.originX, this.originY + j, i, l));
                                    this.subSlots.add(new Stitcher.Slot(this.originX + i, this.originY, k, this.height));
                                }
                                else
                                {
                                    this.subSlots.add(new Stitcher.Slot(this.originX + i, this.originY, k, j));
                                    this.subSlots.add(new Stitcher.Slot(this.originX, this.originY + j, this.width, l));
                                }
                            }
                            else if (k == 0)
                            {
                                this.subSlots.add(new Stitcher.Slot(this.originX, this.originY + j, i, l));
                            }
                            else if (l == 0)
                            {
                                this.subSlots.add(new Stitcher.Slot(this.originX + i, this.originY, k, j));
                            }
                        }

                        for (Stitcher.Slot stitcher$slot : this.subSlots)
                        {
                            if (stitcher$slot.addSlot(holderIn))
                            {
                                return true;
                            }
                        }

                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
        }

        public void func_94184_a(List<Stitcher.Slot> p_94184_1_)
        {
            if (this.holder != null)
            {
                p_94184_1_.add(this);
            }
            else if (this.subSlots != null)
            {
                for (Stitcher.Slot stitcher$slot : this.subSlots)
                {
                    stitcher$slot.func_94184_a(p_94184_1_);
                }
            }
        }

        public String toString()
        {
            return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + this.holder + ", subSlots=" + this.subSlots + '}';
        }
    }
}

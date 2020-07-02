package net.minecraft.client.renderer.vertex;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VertexFormat
{
    private static final Logger field_177357_a = LogManager.getLogger();
    private final List<VertexFormatElement> elements;
    private final List<Integer> offsets;

    /** The total size of this vertex format. */
    private int vertexSize;
    private int field_177354_e;
    private final List<Integer> field_177351_f;
    private int field_177352_g;

    public VertexFormat(VertexFormat p_i46097_1_)
    {
        this();

        for (int i = 0; i < p_i46097_1_.func_177345_h(); ++i)
        {
            this.func_181721_a(p_i46097_1_.func_177348_c(i));
        }

        this.vertexSize = p_i46097_1_.getSize();
    }

    public VertexFormat()
    {
        this.elements = Lists.<VertexFormatElement>newArrayList();
        this.offsets = Lists.<Integer>newArrayList();
        this.field_177354_e = -1;
        this.field_177351_f = Lists.<Integer>newArrayList();
        this.field_177352_g = -1;
    }

    public void func_177339_a()
    {
        this.elements.clear();
        this.offsets.clear();
        this.field_177354_e = -1;
        this.field_177351_f.clear();
        this.field_177352_g = -1;
        this.vertexSize = 0;
    }

    @SuppressWarnings("incomplete-switch")
    public VertexFormat func_181721_a(VertexFormatElement p_181721_1_)
    {
        if (p_181721_1_.isPositionElement() && this.func_177341_i())
        {
            field_177357_a.warn("VertexFormat error: Trying to add a position VertexFormatElement when one already exists, ignoring.");
            return this;
        }
        else
        {
            this.elements.add(p_181721_1_);
            this.offsets.add(Integer.valueOf(this.vertexSize));

            switch (p_181721_1_.getUsage())
            {
                case NORMAL:
                    this.field_177352_g = this.vertexSize;
                    break;

                case COLOR:
                    this.field_177354_e = this.vertexSize;
                    break;

                case UV:
                    this.field_177351_f.add(p_181721_1_.getIndex(), Integer.valueOf(this.vertexSize));
            }

            this.vertexSize += p_181721_1_.getSize();
            return this;
        }
    }

    public boolean func_177350_b()
    {
        return this.field_177352_g >= 0;
    }

    public int func_177342_c()
    {
        return this.field_177352_g;
    }

    public boolean func_177346_d()
    {
        return this.field_177354_e >= 0;
    }

    public int func_177340_e()
    {
        return this.field_177354_e;
    }

    public boolean func_177347_a(int p_177347_1_)
    {
        return this.field_177351_f.size() - 1 >= p_177347_1_;
    }

    public int func_177344_b(int p_177344_1_)
    {
        return ((Integer)this.field_177351_f.get(p_177344_1_)).intValue();
    }

    public String toString()
    {
        String s = "format: " + this.elements.size() + " elements: ";

        for (int i = 0; i < this.elements.size(); ++i)
        {
            s = s + ((VertexFormatElement)this.elements.get(i)).toString();

            if (i != this.elements.size() - 1)
            {
                s = s + " ";
            }
        }

        return s;
    }

    private boolean func_177341_i()
    {
        int i = 0;

        for (int j = this.elements.size(); i < j; ++i)
        {
            VertexFormatElement vertexformatelement = this.elements.get(i);

            if (vertexformatelement.isPositionElement())
            {
                return true;
            }
        }

        return false;
    }

    public int getIntegerSize()
    {
        return this.getSize() / 4;
    }

    public int getSize()
    {
        return this.vertexSize;
    }

    public List<VertexFormatElement> func_177343_g()
    {
        return this.elements;
    }

    public int func_177345_h()
    {
        return this.elements.size();
    }

    public VertexFormatElement func_177348_c(int p_177348_1_)
    {
        return this.elements.get(p_177348_1_);
    }

    public int func_181720_d(int p_181720_1_)
    {
        return ((Integer)this.offsets.get(p_181720_1_)).intValue();
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass())
        {
            VertexFormat vertexformat = (VertexFormat)p_equals_1_;

            if (this.vertexSize != vertexformat.vertexSize)
            {
                return false;
            }
            else if (!this.elements.equals(vertexformat.elements))
            {
                return false;
            }
            else
            {
                return this.offsets.equals(vertexformat.offsets);
            }
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        int i = this.elements.hashCode();
        i = 31 * i + this.offsets.hashCode();
        i = 31 * i + this.vertexSize;
        return i;
    }
}

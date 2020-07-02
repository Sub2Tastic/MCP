package net.minecraft.item;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

public enum EnumDyeColor implements IStringSerializable
{
    WHITE(0, 15, "white", "white", 16383998, TextFormatting.WHITE),
    ORANGE(1, 14, "orange", "orange", 16351261, TextFormatting.GOLD),
    MAGENTA(2, 13, "magenta", "magenta", 13061821, TextFormatting.AQUA),
    LIGHT_BLUE(3, 12, "light_blue", "lightBlue", 3847130, TextFormatting.BLUE),
    YELLOW(4, 11, "yellow", "yellow", 16701501, TextFormatting.YELLOW),
    LIME(5, 10, "lime", "lime", 8439583, TextFormatting.GREEN),
    PINK(6, 9, "pink", "pink", 15961002, TextFormatting.LIGHT_PURPLE),
    GRAY(7, 8, "gray", "gray", 4673362, TextFormatting.DARK_GRAY),
    SILVER(8, 7, "silver", "silver", 10329495, TextFormatting.GRAY),
    CYAN(9, 6, "cyan", "cyan", 1481884, TextFormatting.DARK_AQUA),
    PURPLE(10, 5, "purple", "purple", 8991416, TextFormatting.DARK_PURPLE),
    BLUE(11, 4, "blue", "blue", 3949738, TextFormatting.DARK_BLUE),
    BROWN(12, 3, "brown", "brown", 8606770, TextFormatting.GOLD),
    GREEN(13, 2, "green", "green", 6192150, TextFormatting.DARK_GREEN),
    RED(14, 1, "red", "red", 11546150, TextFormatting.DARK_RED),
    BLACK(15, 0, "black", "black", 1908001, TextFormatting.BLACK);

    private static final EnumDyeColor[] field_176790_q = new EnumDyeColor[values().length];
    private static final EnumDyeColor[] field_176789_r = new EnumDyeColor[values().length];
    private final int field_176788_s;
    private final int field_176787_t;
    private final String field_176786_u;
    private final String translationKey;
    private final int colorValue;
    private final float[] colorComponentValues;
    private final TextFormatting field_176793_x;

    private EnumDyeColor(int p_i47505_3_, int p_i47505_4_, String p_i47505_5_, String p_i47505_6_, int p_i47505_7_, TextFormatting p_i47505_8_)
    {
        this.field_176788_s = p_i47505_3_;
        this.field_176787_t = p_i47505_4_;
        this.field_176786_u = p_i47505_5_;
        this.translationKey = p_i47505_6_;
        this.colorValue = p_i47505_7_;
        this.field_176793_x = p_i47505_8_;
        int i = (p_i47505_7_ & 16711680) >> 16;
        int j = (p_i47505_7_ & 65280) >> 8;
        int k = (p_i47505_7_ & 255) >> 0;
        this.colorComponentValues = new float[] {(float)i / 255.0F, (float)j / 255.0F, (float)k / 255.0F};
    }

    public int func_176765_a()
    {
        return this.field_176788_s;
    }

    public int func_176767_b()
    {
        return this.field_176787_t;
    }

    public String func_192396_c()
    {
        return this.field_176786_u;
    }

    public String getTranslationKey()
    {
        return this.translationKey;
    }

    public int func_193350_e()
    {
        return this.colorValue;
    }

    /**
     * Gets an array containing 3 floats ranging from 0.0 to 1.0: the red, green, and blue components of the
     * corresponding color.
     */
    public float[] getColorComponentValues()
    {
        return this.colorComponentValues;
    }

    public static EnumDyeColor func_176766_a(int p_176766_0_)
    {
        if (p_176766_0_ < 0 || p_176766_0_ >= field_176789_r.length)
        {
            p_176766_0_ = 0;
        }

        return field_176789_r[p_176766_0_];
    }

    public static EnumDyeColor func_176764_b(int p_176764_0_)
    {
        if (p_176764_0_ < 0 || p_176764_0_ >= field_176790_q.length)
        {
            p_176764_0_ = 0;
        }

        return field_176790_q[p_176764_0_];
    }

    public String toString()
    {
        return this.translationKey;
    }

    public String getName()
    {
        return this.field_176786_u;
    }

    static {
        for (EnumDyeColor enumdyecolor : values())
        {
            field_176790_q[enumdyecolor.func_176765_a()] = enumdyecolor;
            field_176789_r[enumdyecolor.func_176767_b()] = enumdyecolor;
        }
    }
}

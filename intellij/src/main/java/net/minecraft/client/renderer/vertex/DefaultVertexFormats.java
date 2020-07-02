package net.minecraft.client.renderer.vertex;

public class DefaultVertexFormats
{
    public static final VertexFormat BLOCK = new VertexFormat();
    public static final VertexFormat field_176599_b = new VertexFormat();
    public static final VertexFormat field_181703_c = new VertexFormat();
    public static final VertexFormat PARTICLE_POSITION_TEX_COLOR_LMAP = new VertexFormat();
    public static final VertexFormat POSITION = new VertexFormat();
    public static final VertexFormat POSITION_COLOR = new VertexFormat();
    public static final VertexFormat POSITION_TEX = new VertexFormat();
    public static final VertexFormat field_181708_h = new VertexFormat();
    public static final VertexFormat POSITION_TEX_COLOR = new VertexFormat();
    public static final VertexFormat field_181710_j = new VertexFormat();
    public static final VertexFormat field_181711_k = new VertexFormat();
    public static final VertexFormat POSITION_TEX_COLOR_NORMAL = new VertexFormat();
    public static final VertexFormatElement POSITION_3F = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3);
    public static final VertexFormatElement COLOR_4UB = new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4);
    public static final VertexFormatElement TEX_2F = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2);

    /** Lightmap texture coords */
    public static final VertexFormatElement TEX_2S = new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.UV, 2);
    public static final VertexFormatElement NORMAL_3B = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3);
    public static final VertexFormatElement PADDING_1B = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.PADDING, 1);

    static
    {
        BLOCK.func_181721_a(POSITION_3F);
        BLOCK.func_181721_a(COLOR_4UB);
        BLOCK.func_181721_a(TEX_2F);
        BLOCK.func_181721_a(TEX_2S);
        field_176599_b.func_181721_a(POSITION_3F);
        field_176599_b.func_181721_a(COLOR_4UB);
        field_176599_b.func_181721_a(TEX_2F);
        field_176599_b.func_181721_a(NORMAL_3B);
        field_176599_b.func_181721_a(PADDING_1B);
        field_181703_c.func_181721_a(POSITION_3F);
        field_181703_c.func_181721_a(TEX_2F);
        field_181703_c.func_181721_a(NORMAL_3B);
        field_181703_c.func_181721_a(PADDING_1B);
        PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(POSITION_3F);
        PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(TEX_2F);
        PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(COLOR_4UB);
        PARTICLE_POSITION_TEX_COLOR_LMAP.func_181721_a(TEX_2S);
        POSITION.func_181721_a(POSITION_3F);
        POSITION_COLOR.func_181721_a(POSITION_3F);
        POSITION_COLOR.func_181721_a(COLOR_4UB);
        POSITION_TEX.func_181721_a(POSITION_3F);
        POSITION_TEX.func_181721_a(TEX_2F);
        field_181708_h.func_181721_a(POSITION_3F);
        field_181708_h.func_181721_a(NORMAL_3B);
        field_181708_h.func_181721_a(PADDING_1B);
        POSITION_TEX_COLOR.func_181721_a(POSITION_3F);
        POSITION_TEX_COLOR.func_181721_a(TEX_2F);
        POSITION_TEX_COLOR.func_181721_a(COLOR_4UB);
        field_181710_j.func_181721_a(POSITION_3F);
        field_181710_j.func_181721_a(TEX_2F);
        field_181710_j.func_181721_a(NORMAL_3B);
        field_181710_j.func_181721_a(PADDING_1B);
        field_181711_k.func_181721_a(POSITION_3F);
        field_181711_k.func_181721_a(TEX_2F);
        field_181711_k.func_181721_a(TEX_2S);
        field_181711_k.func_181721_a(COLOR_4UB);
        POSITION_TEX_COLOR_NORMAL.func_181721_a(POSITION_3F);
        POSITION_TEX_COLOR_NORMAL.func_181721_a(TEX_2F);
        POSITION_TEX_COLOR_NORMAL.func_181721_a(COLOR_4UB);
        POSITION_TEX_COLOR_NORMAL.func_181721_a(NORMAL_3B);
        POSITION_TEX_COLOR_NORMAL.func_181721_a(PADDING_1B);
    }
}

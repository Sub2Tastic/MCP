package net.minecraft.block.material;

import net.minecraft.item.EnumDyeColor;

public class MapColor
{
    /**
     * Holds all the 16 colors used on maps, very similar of a pallete system.
     */
    public static final MapColor[] COLORS = new MapColor[64];
    public static final MapColor[] field_193575_b = new MapColor[16];
    public static final MapColor AIR = new MapColor(0, 0);
    public static final MapColor GRASS = new MapColor(1, 8368696);
    public static final MapColor SAND = new MapColor(2, 16247203);
    public static final MapColor WOOL = new MapColor(3, 13092807);
    public static final MapColor TNT = new MapColor(4, 16711680);
    public static final MapColor ICE = new MapColor(5, 10526975);
    public static final MapColor IRON = new MapColor(6, 10987431);
    public static final MapColor FOLIAGE = new MapColor(7, 31744);
    public static final MapColor SNOW = new MapColor(8, 16777215);
    public static final MapColor CLAY = new MapColor(9, 10791096);
    public static final MapColor DIRT = new MapColor(10, 9923917);
    public static final MapColor STONE = new MapColor(11, 7368816);
    public static final MapColor WATER = new MapColor(12, 4210943);
    public static final MapColor WOOD = new MapColor(13, 9402184);
    public static final MapColor QUARTZ = new MapColor(14, 16776437);
    public static final MapColor ADOBE = new MapColor(15, 14188339);
    public static final MapColor MAGENTA = new MapColor(16, 11685080);
    public static final MapColor LIGHT_BLUE = new MapColor(17, 6724056);
    public static final MapColor YELLOW = new MapColor(18, 15066419);
    public static final MapColor LIME = new MapColor(19, 8375321);
    public static final MapColor PINK = new MapColor(20, 15892389);
    public static final MapColor GRAY = new MapColor(21, 5000268);
    public static final MapColor field_151680_x = new MapColor(22, 10066329);
    public static final MapColor CYAN = new MapColor(23, 5013401);
    public static final MapColor PURPLE = new MapColor(24, 8339378);
    public static final MapColor BLUE = new MapColor(25, 3361970);
    public static final MapColor BROWN = new MapColor(26, 6704179);
    public static final MapColor GREEN = new MapColor(27, 6717235);
    public static final MapColor RED = new MapColor(28, 10040115);
    public static final MapColor BLACK = new MapColor(29, 1644825);
    public static final MapColor GOLD = new MapColor(30, 16445005);
    public static final MapColor DIAMOND = new MapColor(31, 6085589);
    public static final MapColor LAPIS = new MapColor(32, 4882687);
    public static final MapColor EMERALD = new MapColor(33, 55610);
    public static final MapColor OBSIDIAN = new MapColor(34, 8476209);
    public static final MapColor NETHERRACK = new MapColor(35, 7340544);
    public static final MapColor WHITE_TERRACOTTA = new MapColor(36, 13742497);
    public static final MapColor ORANGE_TERRACOTTA = new MapColor(37, 10441252);
    public static final MapColor MAGENTA_TERRACOTTA = new MapColor(38, 9787244);
    public static final MapColor LIGHT_BLUE_TERRACOTTA = new MapColor(39, 7367818);
    public static final MapColor YELLOW_TERRACOTTA = new MapColor(40, 12223780);
    public static final MapColor LIME_TERRACOTTA = new MapColor(41, 6780213);
    public static final MapColor PINK_TERRACOTTA = new MapColor(42, 10505550);
    public static final MapColor GRAY_TERRACOTTA = new MapColor(43, 3746083);
    public static final MapColor field_193569_U = new MapColor(44, 8874850);
    public static final MapColor CYAN_TERRACOTTA = new MapColor(45, 5725276);
    public static final MapColor PURPLE_TERRACOTTA = new MapColor(46, 8014168);
    public static final MapColor BLUE_TERRACOTTA = new MapColor(47, 4996700);
    public static final MapColor BROWN_TERRACOTTA = new MapColor(48, 4993571);
    public static final MapColor GREEN_TERRACOTTA = new MapColor(49, 5001770);
    public static final MapColor RED_TERRACOTTA = new MapColor(50, 9321518);
    public static final MapColor BLACK_TERRACOTTA = new MapColor(51, 2430480);
    public final int colorValue;
    public final int colorIndex;

    private MapColor(int index, int color)
    {
        if (index >= 0 && index <= 63)
        {
            this.colorIndex = index;
            this.colorValue = color;
            COLORS[index] = this;
        }
        else
        {
            throw new IndexOutOfBoundsException("Map colour ID must be between 0 and 63 (inclusive)");
        }
    }

    public int getMapColor(int index)
    {
        int i = 220;

        if (index == 3)
        {
            i = 135;
        }

        if (index == 2)
        {
            i = 255;
        }

        if (index == 1)
        {
            i = 220;
        }

        if (index == 0)
        {
            i = 180;
        }

        int j = (this.colorValue >> 16 & 255) * i / 255;
        int k = (this.colorValue >> 8 & 255) * i / 255;
        int l = (this.colorValue & 255) * i / 255;
        return -16777216 | j << 16 | k << 8 | l;
    }

    public static MapColor func_193558_a(EnumDyeColor p_193558_0_)
    {
        return field_193575_b[p_193558_0_.func_176765_a()];
    }

    static
    {
        field_193575_b[EnumDyeColor.WHITE.func_176765_a()] = SNOW;
        field_193575_b[EnumDyeColor.ORANGE.func_176765_a()] = ADOBE;
        field_193575_b[EnumDyeColor.MAGENTA.func_176765_a()] = MAGENTA;
        field_193575_b[EnumDyeColor.LIGHT_BLUE.func_176765_a()] = LIGHT_BLUE;
        field_193575_b[EnumDyeColor.YELLOW.func_176765_a()] = YELLOW;
        field_193575_b[EnumDyeColor.LIME.func_176765_a()] = LIME;
        field_193575_b[EnumDyeColor.PINK.func_176765_a()] = PINK;
        field_193575_b[EnumDyeColor.GRAY.func_176765_a()] = GRAY;
        field_193575_b[EnumDyeColor.SILVER.func_176765_a()] = field_151680_x;
        field_193575_b[EnumDyeColor.CYAN.func_176765_a()] = CYAN;
        field_193575_b[EnumDyeColor.PURPLE.func_176765_a()] = PURPLE;
        field_193575_b[EnumDyeColor.BLUE.func_176765_a()] = BLUE;
        field_193575_b[EnumDyeColor.BROWN.func_176765_a()] = BROWN;
        field_193575_b[EnumDyeColor.GREEN.func_176765_a()] = GREEN;
        field_193575_b[EnumDyeColor.RED.func_176765_a()] = RED;
        field_193575_b[EnumDyeColor.BLACK.func_176765_a()] = BLACK;
    }
}

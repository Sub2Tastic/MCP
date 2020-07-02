package net.minecraft.item;

import net.minecraft.util.text.TextFormatting;

public enum EnumRarity
{
    COMMON(TextFormatting.WHITE, "Common"),
    UNCOMMON(TextFormatting.YELLOW, "Uncommon"),
    RARE(TextFormatting.AQUA, "Rare"),
    EPIC(TextFormatting.LIGHT_PURPLE, "Epic");

    public final TextFormatting color;
    public final String field_77934_f;

    private EnumRarity(TextFormatting p_i45349_3_, String p_i45349_4_)
    {
        this.color = p_i45349_3_;
        this.field_77934_f = p_i45349_4_;
    }
}

package net.minecraft.scoreboard;

import net.minecraft.util.text.TextFormatting;

public class ScoreCriteriaColored implements IScoreCriteria
{
    private final String field_178794_j;

    public ScoreCriteriaColored(String p_i45549_1_, TextFormatting p_i45549_2_)
    {
        this.field_178794_j = p_i45549_1_ + p_i45549_2_.getFriendlyName();
        IScoreCriteria.INSTANCES.put(this.field_178794_j, this);
    }

    public String getName()
    {
        return this.field_178794_j;
    }

    public boolean isReadOnly()
    {
        return false;
    }

    public IScoreCriteria.EnumRenderType getRenderType()
    {
        return IScoreCriteria.EnumRenderType.INTEGER;
    }
}

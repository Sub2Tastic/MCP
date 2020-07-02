package net.minecraft.scoreboard;

public class ScoreCriteriaHealth extends ScoreCriteria
{
    public ScoreCriteriaHealth(String p_i2312_1_)
    {
        super(p_i2312_1_);
    }

    public boolean isReadOnly()
    {
        return true;
    }

    public IScoreCriteria.EnumRenderType getRenderType()
    {
        return IScoreCriteria.EnumRenderType.HEARTS;
    }
}

package net.minecraft.scoreboard;

public class ScoreCriteria implements IScoreCriteria
{
    private final String field_96644_g;

    public ScoreCriteria(String p_i2311_1_)
    {
        this.field_96644_g = p_i2311_1_;
        IScoreCriteria.INSTANCES.put(p_i2311_1_, this);
    }

    public String getName()
    {
        return this.field_96644_g;
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

package net.minecraft.scoreboard;

public class ScoreObjective
{
    private final Scoreboard scoreboard;
    private final String name;
    private final IScoreCriteria objectiveCriteria;
    private IScoreCriteria.EnumRenderType field_178768_d;
    private String displayName;

    public ScoreObjective(Scoreboard p_i2307_1_, String p_i2307_2_, IScoreCriteria p_i2307_3_)
    {
        this.scoreboard = p_i2307_1_;
        this.name = p_i2307_2_;
        this.objectiveCriteria = p_i2307_3_;
        this.displayName = p_i2307_2_;
        this.field_178768_d = p_i2307_3_.getRenderType();
    }

    public Scoreboard getScoreboard()
    {
        return this.scoreboard;
    }

    public String getName()
    {
        return this.name;
    }

    public IScoreCriteria getCriteria()
    {
        return this.objectiveCriteria;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public void func_96681_a(String p_96681_1_)
    {
        this.displayName = p_96681_1_;
        this.scoreboard.func_96532_b(this);
    }

    public IScoreCriteria.EnumRenderType func_178766_e()
    {
        return this.field_178768_d;
    }

    public void func_178767_a(IScoreCriteria.EnumRenderType p_178767_1_)
    {
        this.field_178768_d = p_178767_1_;
        this.scoreboard.func_96532_b(this);
    }
}

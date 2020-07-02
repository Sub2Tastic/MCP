package net.minecraft.advancements;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraft.network.PacketBuffer;

public class CriterionProgress
{
    private static final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private final AdvancementProgress field_192156_b;
    private Date obtained;

    public CriterionProgress(AdvancementProgress p_i47469_1_)
    {
        this.field_192156_b = p_i47469_1_;
    }

    public boolean isObtained()
    {
        return this.obtained != null;
    }

    public void obtain()
    {
        this.obtained = new Date();
    }

    public void reset()
    {
        this.obtained = null;
    }

    public Date getObtained()
    {
        return this.obtained;
    }

    public String toString()
    {
        return "CriterionProgress{obtained=" + (this.obtained == null ? "false" : this.obtained) + '}';
    }

    public void write(PacketBuffer buf)
    {
        buf.writeBoolean(this.obtained != null);

        if (this.obtained != null)
        {
            buf.writeTime(this.obtained);
        }
    }

    public JsonElement serialize()
    {
        return (JsonElement)(this.obtained != null ? new JsonPrimitive(DATE_TIME_FORMATTER.format(this.obtained)) : JsonNull.INSTANCE);
    }

    public static CriterionProgress read(PacketBuffer buf, AdvancementProgress p_192149_1_)
    {
        CriterionProgress criterionprogress = new CriterionProgress(p_192149_1_);

        if (buf.readBoolean())
        {
            criterionprogress.obtained = buf.readTime();
        }

        return criterionprogress;
    }

    public static CriterionProgress func_192152_a(AdvancementProgress p_192152_0_, String p_192152_1_)
    {
        CriterionProgress criterionprogress = new CriterionProgress(p_192152_0_);

        try
        {
            criterionprogress.obtained = DATE_TIME_FORMATTER.parse(p_192152_1_);
            return criterionprogress;
        }
        catch (ParseException parseexception)
        {
            throw new JsonSyntaxException("Invalid datetime: " + p_192152_1_, parseexception);
        }
    }
}

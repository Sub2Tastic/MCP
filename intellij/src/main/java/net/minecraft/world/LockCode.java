package net.minecraft.world;

import javax.annotation.concurrent.Immutable;
import net.minecraft.nbt.NBTTagCompound;

@Immutable
public class LockCode
{
    public static final LockCode EMPTY_CODE = new LockCode("");
    private final String lock;

    public LockCode(String code)
    {
        this.lock = code;
    }

    public boolean func_180160_a()
    {
        return this.lock == null || this.lock.isEmpty();
    }

    public String func_180159_b()
    {
        return this.lock;
    }

    public void write(NBTTagCompound nbt)
    {
        nbt.putString("Lock", this.lock);
    }

    public static LockCode read(NBTTagCompound nbt)
    {
        if (nbt.contains("Lock", 8))
        {
            String s = nbt.getString("Lock");
            return new LockCode(s);
        }
        else
        {
            return EMPTY_CODE;
        }
    }
}

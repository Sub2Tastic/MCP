package net.minecraft.util;

public abstract class LazyLoadBase<T>
{
    private T value;
    private boolean field_179282_b;

    public T getValue()
    {
        if (!this.field_179282_b)
        {
            this.field_179282_b = true;
            this.value = (T)this.func_179280_b();
        }

        return this.value;
    }

    protected abstract T func_179280_b();
}

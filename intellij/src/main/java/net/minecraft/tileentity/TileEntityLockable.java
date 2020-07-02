package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public abstract class TileEntityLockable extends TileEntity implements ILockableContainer
{
    private LockCode code = LockCode.EMPTY_CODE;

    public void read(NBTTagCompound compound)
    {
        super.read(compound);
        this.code = LockCode.read(compound);
    }

    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);

        if (this.code != null)
        {
            this.code.write(compound);
        }

        return compound;
    }

    public boolean func_174893_q_()
    {
        return this.code != null && !this.code.func_180160_a();
    }

    public LockCode func_174891_i()
    {
        return this.code;
    }

    public void func_174892_a(LockCode p_174892_1_)
    {
        this.code = p_174892_1_;
    }

    public ITextComponent getDisplayName()
    {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
    }
}

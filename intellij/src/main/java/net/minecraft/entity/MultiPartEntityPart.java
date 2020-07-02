package net.minecraft.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class MultiPartEntityPart extends Entity
{
    public final IEntityMultiPart field_70259_a;
    public final String field_146032_b;

    public MultiPartEntityPart(IEntityMultiPart p_i1697_1_, String p_i1697_2_, float p_i1697_3_, float p_i1697_4_)
    {
        super(p_i1697_1_.func_82194_d());
        this.func_70105_a(p_i1697_3_, p_i1697_4_);
        this.field_70259_a = p_i1697_1_;
        this.field_146032_b = p_i1697_2_;
    }

    protected void registerData()
    {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(NBTTagCompound compound)
    {
    }

    protected void func_70014_b(NBTTagCompound p_70014_1_)
    {
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return this.isInvulnerableTo(source) ? false : this.field_70259_a.func_70965_a(this, source, amount);
    }

    /**
     * Returns true if Entity argument is equal to this Entity
     */
    public boolean isEntityEqual(Entity entityIn)
    {
        return this == entityIn || this.field_70259_a == entityIn;
    }
}

package net.minecraft.entity.passive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class EntityShoulderRiding extends EntityTameable
{
    private int rideCooldownCounter;

    public EntityShoulderRiding(World p_i47410_1_)
    {
        super(p_i47410_1_);
    }

    public boolean func_191994_f(EntityPlayer p_191994_1_)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.putString("id", this.getEntityString());
        this.writeWithoutTypeId(nbttagcompound);

        if (p_191994_1_.addShoulderEntity(nbttagcompound))
        {
            this.world.func_72900_e(this);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        ++this.rideCooldownCounter;
        super.tick();
    }

    public boolean canSitOnShoulder()
    {
        return this.rideCooldownCounter > 100;
    }
}

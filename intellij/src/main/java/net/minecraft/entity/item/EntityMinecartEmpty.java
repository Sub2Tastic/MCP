package net.minecraft.entity.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityMinecartEmpty extends EntityMinecart
{
    public EntityMinecartEmpty(World p_i1722_1_)
    {
        super(p_i1722_1_);
    }

    public EntityMinecartEmpty(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static void func_189673_a(DataFixer p_189673_0_)
    {
        EntityMinecart.func_189669_a(p_189673_0_, EntityMinecartEmpty.class);
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
    {
        if (player.func_70093_af())
        {
            return false;
        }
        else if (this.isBeingRidden())
        {
            return true;
        }
        else
        {
            if (!this.world.isRemote)
            {
                player.startRiding(this);
            }

            return true;
        }
    }

    /**
     * Called every tick the minecart is on an activator rail.
     */
    public void onActivatorRailPass(int x, int y, int z, boolean receivingPower)
    {
        if (receivingPower)
        {
            if (this.isBeingRidden())
            {
                this.removePassengers();
            }

            if (this.getRollingAmplitude() == 0)
            {
                this.setRollingDirection(-this.getRollingDirection());
                this.setRollingAmplitude(10);
                this.setDamage(50.0F);
                this.markVelocityChanged();
            }
        }
    }

    public EntityMinecart.Type getMinecartType()
    {
        return EntityMinecart.Type.RIDEABLE;
    }
}
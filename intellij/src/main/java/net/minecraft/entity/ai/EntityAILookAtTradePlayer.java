package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtTradePlayer extends EntityAIWatchClosest
{
    private final EntityVillager villager;

    public EntityAILookAtTradePlayer(EntityVillager p_i1633_1_)
    {
        super(p_i1633_1_, EntityPlayer.class, 8.0F);
        this.villager = p_i1633_1_;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        if (this.villager.func_70940_q())
        {
            this.closestEntity = this.villager.getCustomer();
            return true;
        }
        else
        {
            return false;
        }
    }
}

package net.minecraft.entity.passive;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class EntityAmbientCreature extends EntityLiving implements IAnimals
{
    public EntityAmbientCreature(World p_i1679_1_)
    {
        super(p_i1679_1_);
    }

    public boolean canBeLeashedTo(EntityPlayer player)
    {
        return false;
    }
}

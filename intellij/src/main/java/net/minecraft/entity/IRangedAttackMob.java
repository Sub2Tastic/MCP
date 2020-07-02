package net.minecraft.entity;

public interface IRangedAttackMob
{
    /**
     * Attack the specified entity using a ranged attack.
     */
    void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor);

    void func_184724_a(boolean p_184724_1_);
}

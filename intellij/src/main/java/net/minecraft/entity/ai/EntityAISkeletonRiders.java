package net.minecraft.entity.ai;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;

public class EntityAISkeletonRiders extends EntityAIBase
{
    private final EntitySkeletonHorse horse;

    public EntityAISkeletonRiders(EntitySkeletonHorse horseIn)
    {
        this.horse = horseIn;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        return this.horse.world.func_175636_b(this.horse.posX, this.horse.posY, this.horse.posZ, 10.0D);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick()
    {
        DifficultyInstance difficultyinstance = this.horse.world.getDifficultyForLocation(new BlockPos(this.horse));
        this.horse.setTrap(false);
        this.horse.setHorseTamed(true);
        this.horse.setGrowingAge(0);
        this.horse.world.func_72942_c(new EntityLightningBolt(this.horse.world, this.horse.posX, this.horse.posY, this.horse.posZ, true));
        EntitySkeleton entityskeleton = this.createSkeleton(difficultyinstance, this.horse);
        entityskeleton.startRiding(this.horse);

        for (int i = 0; i < 3; ++i)
        {
            AbstractHorse abstracthorse = this.createHorse(difficultyinstance);
            EntitySkeleton entityskeleton1 = this.createSkeleton(difficultyinstance, abstracthorse);
            entityskeleton1.startRiding(abstracthorse);
            abstracthorse.addVelocity(this.horse.getRNG().nextGaussian() * 0.5D, 0.0D, this.horse.getRNG().nextGaussian() * 0.5D);
        }
    }

    private AbstractHorse createHorse(DifficultyInstance p_188515_1_)
    {
        EntitySkeletonHorse entityskeletonhorse = new EntitySkeletonHorse(this.horse.world);
        entityskeletonhorse.func_180482_a(p_188515_1_, (IEntityLivingData)null);
        entityskeletonhorse.setPosition(this.horse.posX, this.horse.posY, this.horse.posZ);
        entityskeletonhorse.hurtResistantTime = 60;
        entityskeletonhorse.enablePersistence();
        entityskeletonhorse.setHorseTamed(true);
        entityskeletonhorse.setGrowingAge(0);
        entityskeletonhorse.world.addEntity0(entityskeletonhorse);
        return entityskeletonhorse;
    }

    private EntitySkeleton createSkeleton(DifficultyInstance p_188514_1_, AbstractHorse p_188514_2_)
    {
        EntitySkeleton entityskeleton = new EntitySkeleton(p_188514_2_.world);
        entityskeleton.func_180482_a(p_188514_1_, (IEntityLivingData)null);
        entityskeleton.setPosition(p_188514_2_.posX, p_188514_2_.posY, p_188514_2_.posZ);
        entityskeleton.hurtResistantTime = 60;
        entityskeleton.enablePersistence();

        if (entityskeleton.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty())
        {
            entityskeleton.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        }

        entityskeleton.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, EnchantmentHelper.addRandomEnchantment(entityskeleton.getRNG(), entityskeleton.getHeldItemMainhand(), (int)(5.0F + p_188514_1_.getClampedAdditionalDifficulty() * (float)entityskeleton.getRNG().nextInt(18)), false));
        entityskeleton.setItemStackToSlot(EntityEquipmentSlot.HEAD, EnchantmentHelper.addRandomEnchantment(entityskeleton.getRNG(), entityskeleton.getItemStackFromSlot(EntityEquipmentSlot.HEAD), (int)(5.0F + p_188514_1_.getClampedAdditionalDifficulty() * (float)entityskeleton.getRNG().nextInt(18)), false));
        entityskeleton.world.addEntity0(entityskeleton);
        return entityskeleton;
    }
}

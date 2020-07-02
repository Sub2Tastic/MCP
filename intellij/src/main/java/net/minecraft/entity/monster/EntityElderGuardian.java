package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityElderGuardian extends EntityGuardian
{
    public EntityElderGuardian(World p_i47288_1_)
    {
        super(p_i47288_1_);
        this.func_70105_a(this.field_70130_N * 2.35F, this.field_70131_O * 2.35F);
        this.enablePersistence();

        if (this.wander != null)
        {
            this.wander.setExecutionChance(400);
        }
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0D);
    }

    public static void func_190768_b(DataFixer p_190768_0_)
    {
        EntityLiving.func_189752_a(p_190768_0_, EntityElderGuardian.class);
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_186441_w;
    }

    public int getAttackDuration()
    {
        return 60;
    }

    public void func_190767_di()
    {
        this.clientSideSpikesAnimation = 1.0F;
        this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;
    }

    protected SoundEvent getAmbientSound()
    {
        return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT : SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT_LAND;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_HURT : SoundEvents.ENTITY_ELDER_GUARDIAN_HURT_LAND;
    }

    protected SoundEvent getDeathSound()
    {
        return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH : SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH_LAND;
    }

    protected SoundEvent getFlopSound()
    {
        return SoundEvents.ENTITY_ELDER_GUARDIAN_FLOP;
    }

    protected void updateAITasks()
    {
        super.updateAITasks();
        int i = 1200;

        if ((this.ticksExisted + this.getEntityId()) % 1200 == 0)
        {
            Potion potion = MobEffects.MINING_FATIGUE;
            List<EntityPlayerMP> list = this.world.<EntityPlayerMP>func_175661_b(EntityPlayerMP.class, new Predicate<EntityPlayerMP>()
            {
                public boolean apply(@Nullable EntityPlayerMP p_apply_1_)
                {
                    return EntityElderGuardian.this.getDistanceSq(p_apply_1_) < 2500.0D && p_apply_1_.interactionManager.survivalOrAdventure();
                }
            });
            int j = 2;
            int k = 6000;
            int l = 1200;

            for (EntityPlayerMP entityplayermp : list)
            {
                if (!entityplayermp.isPotionActive(potion) || entityplayermp.getActivePotionEffect(potion).getAmplifier() < 2 || entityplayermp.getActivePotionEffect(potion).getDuration() < 1200)
                {
                    entityplayermp.connection.sendPacket(new SPacketChangeGameState(10, 0.0F));
                    entityplayermp.func_70690_d(new PotionEffect(potion, 6000, 2));
                }
            }
        }

        if (!this.func_110175_bO())
        {
            this.func_175449_a(new BlockPos(this), 16);
        }
    }
}

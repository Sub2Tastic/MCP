package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityStray extends AbstractSkeleton
{
    public EntityStray(World p_i47281_1_)
    {
        super(p_i47281_1_);
    }

    public static void func_190728_b(DataFixer p_190728_0_)
    {
        EntityLiving.func_189752_a(p_190728_0_, EntityStray.class);
    }

    public boolean func_70601_bi()
    {
        return super.func_70601_bi() && this.world.func_175678_i(new BlockPos(this));
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_189968_an;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_STRAY_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_STRAY_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_STRAY_DEATH;
    }

    SoundEvent getStepSound()
    {
        return SoundEvents.ENTITY_STRAY_STEP;
    }

    protected EntityArrow func_190726_a(float p_190726_1_)
    {
        EntityArrow entityarrow = super.func_190726_a(p_190726_1_);

        if (entityarrow instanceof EntityTippedArrow)
        {
            ((EntityTippedArrow)entityarrow).addEffect(new PotionEffect(MobEffects.SLOWNESS, 600));
        }

        return entityarrow;
    }
}

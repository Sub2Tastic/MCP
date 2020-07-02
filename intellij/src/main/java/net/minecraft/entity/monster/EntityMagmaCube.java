package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityMagmaCube extends EntitySlime
{
    public EntityMagmaCube(World p_i1737_1_)
    {
        super(p_i1737_1_);
        this.field_70178_ae = true;
    }

    public static void func_189759_b(DataFixer p_189759_0_)
    {
        EntityLiving.func_189752_a(p_189759_0_, EntityMagmaCube.class);
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
    }

    public boolean func_70601_bi()
    {
        return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    public boolean func_70058_J()
    {
        return this.world.func_72917_a(this.getBoundingBox(), this) && this.world.func_184144_a(this, this.getBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getBoundingBox());
    }

    protected void setSlimeSize(int size, boolean resetHealth)
    {
        super.setSlimeSize(size, resetHealth);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue((double)(size * 3));
    }

    public int func_70070_b()
    {
        return 15728880;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness()
    {
        return 1.0F;
    }

    protected EnumParticleTypes func_180487_n()
    {
        return EnumParticleTypes.FLAME;
    }

    protected EntitySlime func_70802_j()
    {
        return new EntityMagmaCube(this.world);
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return this.isSmallSlime() ? LootTableList.EMPTY : LootTableList.field_186379_ad;
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning()
    {
        return false;
    }

    /**
     * Gets the amount of time the slime needs to wait between jumps.
     */
    protected int getJumpDelay()
    {
        return super.getJumpDelay() * 4;
    }

    protected void alterSquishAmount()
    {
        this.squishAmount *= 0.9F;
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {
        this.field_70181_x = (double)(0.42F + (float)this.getSlimeSize() * 0.1F);
        this.isAirBorne = true;
    }

    protected void handleFluidJump()
    {
        this.field_70181_x = (double)(0.22F + (float)this.getSlimeSize() * 0.05F);
        this.isAirBorne = true;
    }

    public void func_180430_e(float p_180430_1_, float p_180430_2_)
    {
    }

    /**
     * Indicates weather the slime is able to damage the player (based upon the slime's size)
     */
    protected boolean canDamagePlayer()
    {
        return true;
    }

    protected int func_70805_n()
    {
        return super.func_70805_n() + 2;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return this.isSmallSlime() ? SoundEvents.ENTITY_MAGMA_CUBE_HURT_SMALL : SoundEvents.ENTITY_MAGMA_CUBE_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return this.isSmallSlime() ? SoundEvents.ENTITY_MAGMA_CUBE_DEATH_SMALL : SoundEvents.ENTITY_MAGMA_CUBE_DEATH;
    }

    protected SoundEvent getSquishSound()
    {
        return this.isSmallSlime() ? SoundEvents.ENTITY_MAGMA_CUBE_SQUISH_SMALL : SoundEvents.ENTITY_MAGMA_CUBE_SQUISH;
    }

    protected SoundEvent getJumpSound()
    {
        return SoundEvents.ENTITY_MAGMA_CUBE_JUMP;
    }
}

package net.minecraft.entity.monster;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityZombieVillager extends EntityZombie
{
    private static final DataParameter<Boolean> CONVERTING = EntityDataManager.<Boolean>createKey(EntityZombieVillager.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> field_190739_c = EntityDataManager.<Integer>createKey(EntityZombieVillager.class, DataSerializers.VARINT);
    private int conversionTime;
    private UUID converstionStarter;

    public EntityZombieVillager(World p_i47277_1_)
    {
        super(p_i47277_1_);
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(CONVERTING, Boolean.valueOf(false));
        this.dataManager.register(field_190739_c, Integer.valueOf(0));
    }

    public void func_190733_a(int p_190733_1_)
    {
        this.dataManager.set(field_190739_c, Integer.valueOf(p_190733_1_));
    }

    public int func_190736_dl()
    {
        return Math.max(((Integer)this.dataManager.get(field_190739_c)).intValue() % 6, 0);
    }

    public static void func_190737_b(DataFixer p_190737_0_)
    {
        EntityLiving.func_189752_a(p_190737_0_, EntityZombieVillager.class);
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.putInt("Profession", this.func_190736_dl());
        p_70014_1_.putInt("ConversionTime", this.isConverting() ? this.conversionTime : -1);

        if (this.converstionStarter != null)
        {
            p_70014_1_.putUniqueId("ConversionPlayer", this.converstionStarter);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        this.func_190733_a(compound.getInt("Profession"));

        if (compound.contains("ConversionTime", 99) && compound.getInt("ConversionTime") > -1)
        {
            this.startConverting(compound.hasUniqueId("ConversionPlayer") ? compound.getUniqueId("ConversionPlayer") : null, compound.getInt("ConversionTime"));
        }
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, @Nullable IEntityLivingData p_180482_2_)
    {
        this.func_190733_a(this.world.rand.nextInt(6));
        return super.func_180482_a(p_180482_1_, p_180482_2_);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        if (!this.world.isRemote && this.isConverting())
        {
            int i = this.getConversionProgress();
            this.conversionTime -= i;

            if (this.conversionTime <= 0)
            {
                this.func_190738_dp();
            }
        }

        super.tick();
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() == Items.GOLDEN_APPLE && itemstack.func_77960_j() == 0 && this.isPotionActive(MobEffects.WEAKNESS))
        {
            if (!player.abilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }

            if (!this.world.isRemote)
            {
                this.startConverting(player.getUniqueID(), this.rand.nextInt(2401) + 3600);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean func_70692_ba()
    {
        return !this.isConverting();
    }

    /**
     * Returns whether this zombie is in the process of converting to a villager
     */
    public boolean isConverting()
    {
        return ((Boolean)this.getDataManager().get(CONVERTING)).booleanValue();
    }

    /**
     * Starts conversion of this zombie villager to a villager
     */
    protected void startConverting(@Nullable UUID conversionStarterIn, int conversionTimeIn)
    {
        this.converstionStarter = conversionStarterIn;
        this.conversionTime = conversionTimeIn;
        this.getDataManager().set(CONVERTING, Boolean.valueOf(true));
        this.func_184589_d(MobEffects.WEAKNESS);
        this.func_70690_d(new PotionEffect(MobEffects.STRENGTH, conversionTimeIn, Math.min(this.world.getDifficulty().getId() - 1, 0)));
        this.world.setEntityState(this, (byte)16);
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    public void handleStatusUpdate(byte id)
    {
        if (id == 16)
        {
            if (!this.isSilent())
            {
                this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, this.getSoundCategory(), 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
            }
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    protected void func_190738_dp()
    {
        EntityVillager entityvillager = new EntityVillager(this.world);
        entityvillager.copyLocationAndAnglesFrom(this);
        entityvillager.func_70938_b(this.func_190736_dl());
        entityvillager.func_190672_a(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
        entityvillager.func_82187_q();

        if (this.isChild())
        {
            entityvillager.setGrowingAge(-24000);
        }

        this.world.func_72900_e(this);
        entityvillager.setNoAI(this.isAIDisabled());

        if (this.hasCustomName())
        {
            entityvillager.func_96094_a(this.func_95999_t());
            entityvillager.setCustomNameVisible(this.isCustomNameVisible());
        }

        this.world.addEntity0(entityvillager);

        if (this.converstionStarter != null)
        {
            EntityPlayer entityplayer = this.world.func_152378_a(this.converstionStarter);

            if (entityplayer instanceof EntityPlayerMP)
            {
                CriteriaTriggers.CURED_ZOMBIE_VILLAGER.trigger((EntityPlayerMP)entityplayer, this, entityvillager);
            }
        }

        entityvillager.func_70690_d(new PotionEffect(MobEffects.NAUSEA, 200, 0));
        this.world.func_180498_a((EntityPlayer)null, 1027, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
    }

    protected int getConversionProgress()
    {
        int i = 1;

        if (this.rand.nextFloat() < 0.01F)
        {
            int j = 0;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k = (int)this.posX - 4; k < (int)this.posX + 4 && j < 14; ++k)
            {
                for (int l = (int)this.posY - 4; l < (int)this.posY + 4 && j < 14; ++l)
                {
                    for (int i1 = (int)this.posZ - 4; i1 < (int)this.posZ + 4 && j < 14; ++i1)
                    {
                        Block block = this.world.getBlockState(blockpos$mutableblockpos.setPos(k, l, i1)).getBlock();

                        if (block == Blocks.IRON_BARS || block == Blocks.field_150324_C)
                        {
                            if (this.rand.nextFloat() < 0.3F)
                            {
                                ++i;
                            }

                            ++j;
                        }
                    }
                }
            }
        }

        return i;
    }

    /**
     * Gets the pitch of living sounds in living entities.
     */
    protected float getSoundPitch()
    {
        return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 2.0F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
    }

    public SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT;
    }

    public SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_HURT;
    }

    public SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_DEATH;
    }

    public SoundEvent getStepSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_STEP;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_191183_as;
    }

    protected ItemStack getSkullDrop()
    {
        return ItemStack.EMPTY;
    }
}

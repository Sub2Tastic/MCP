package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.ai.EntityAIFollowOwnerFlying;
import net.minecraft.entity.ai.EntityAILandOnOwnersShoulder;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityParrot extends EntityShoulderRiding implements EntityFlying
{
    private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntityParrot.class, DataSerializers.VARINT);
    private static final Predicate<EntityLiving> CAN_MIMIC = new Predicate<EntityLiving>()
    {
        public boolean apply(@Nullable EntityLiving p_apply_1_)
        {
            return p_apply_1_ != null && EntityParrot.IMITATION_SOUND_EVENTS.containsKey(EntityList.field_191308_b.getId(p_apply_1_.getClass()));
        }
    };
    private static final Item DEADLY_ITEM = Items.COOKIE;
    private static final Set<Item> TAME_ITEMS = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    private static final Int2ObjectMap<SoundEvent> IMITATION_SOUND_EVENTS = new Int2ObjectOpenHashMap<SoundEvent>(32);
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0F;
    private boolean partyParrot;
    private BlockPos jukeboxPosition;

    public EntityParrot(World p_i47411_1_)
    {
        super(p_i47411_1_);
        this.func_70105_a(0.5F, 0.9F);
        this.moveController = new EntityFlyHelper(this);
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, @Nullable IEntityLivingData p_180482_2_)
    {
        this.setVariant(this.rand.nextInt(5));
        return super.func_180482_a(p_180482_1_, p_180482_2_);
    }

    protected void registerGoals()
    {
        this.sitGoal = new EntityAISit(this);
        this.goalSelector.addGoal(0, new EntityAIPanic(this, 1.25D));
        this.goalSelector.addGoal(0, new EntityAISwimming(this));
        this.goalSelector.addGoal(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.goalSelector.addGoal(2, this.sitGoal);
        this.goalSelector.addGoal(2, new EntityAIFollowOwnerFlying(this, 1.0D, 5.0F, 1.0F));
        this.goalSelector.addGoal(2, new EntityAIWanderAvoidWaterFlying(this, 1.0D));
        this.goalSelector.addGoal(3, new EntityAILandOnOwnersShoulder(this));
        this.goalSelector.addGoal(3, new EntityAIFollow(this, 1.0D, 3.0F, 7.0F));
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
        this.getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.4000000059604645D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
    }

    /**
     * Returns new PathNavigateGround instance
     */
    protected PathNavigate createNavigator(World worldIn)
    {
        PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
        pathnavigateflying.setCanOpenDoors(false);
        pathnavigateflying.func_192877_c(true);
        pathnavigateflying.setCanEnterDoors(true);
        return pathnavigateflying;
    }

    public float getEyeHeight()
    {
        return this.field_70131_O * 0.6F;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick()
    {
        playMimicSound(this.world, this);

        if (this.jukeboxPosition == null || this.jukeboxPosition.func_177954_c(this.posX, this.posY, this.posZ) > 12.0D || this.world.getBlockState(this.jukeboxPosition).getBlock() != Blocks.JUKEBOX)
        {
            this.partyParrot = false;
            this.jukeboxPosition = null;
        }

        super.livingTick();
        this.calculateFlapping();
    }

    /**
     * Called when a record starts or stops playing. Used to make parrots start or stop partying.
     */
    public void setPartying(BlockPos pos, boolean isPartying)
    {
        this.jukeboxPosition = pos;
        this.partyParrot = isPartying;
    }

    public boolean isPartying()
    {
        return this.partyParrot;
    }

    private void calculateFlapping()
    {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed = (float)((double)this.flapSpeed + (double)(this.onGround ? -1 : 4) * 0.3D);
        this.flapSpeed = MathHelper.clamp(this.flapSpeed, 0.0F, 1.0F);

        if (!this.onGround && this.flapping < 1.0F)
        {
            this.flapping = 1.0F;
        }

        this.flapping = (float)((double)this.flapping * 0.9D);

        if (!this.onGround && this.field_70181_x < 0.0D)
        {
            this.field_70181_x *= 0.6D;
        }

        this.flap += this.flapping * 2.0F;
    }

    private static boolean playMimicSound(World worldIn, Entity parrotIn)
    {
        if (!parrotIn.isSilent() && worldIn.rand.nextInt(50) == 0)
        {
            List<EntityLiving> list = worldIn.<EntityLiving>getEntitiesWithinAABB(EntityLiving.class, parrotIn.getBoundingBox().grow(20.0D), CAN_MIMIC);

            if (!list.isEmpty())
            {
                EntityLiving entityliving = list.get(worldIn.rand.nextInt(list.size()));

                if (!entityliving.isSilent())
                {
                    SoundEvent soundevent = func_191999_g(EntityList.field_191308_b.getId(entityliving.getClass()));
                    worldIn.playSound((EntityPlayer)null, parrotIn.posX, parrotIn.posY, parrotIn.posZ, soundevent, parrotIn.getSoundCategory(), 0.7F, getPitch(worldIn.rand));
                    return true;
                }
            }

            return false;
        }
        else
        {
            return false;
        }
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (!this.isTamed() && TAME_ITEMS.contains(itemstack.getItem()))
        {
            if (!player.abilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }

            if (!this.isSilent())
            {
                this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PARROT_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            }

            if (!this.world.isRemote)
            {
                if (this.rand.nextInt(10) == 0)
                {
                    this.setTamedBy(player);
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte)7);
                }
                else
                {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte)6);
                }
            }

            return true;
        }
        else if (itemstack.getItem() == DEADLY_ITEM)
        {
            if (!player.abilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }

            this.func_70690_d(new PotionEffect(MobEffects.POISON, 900));

            if (player.isCreative() || !this.isInvulnerable())
            {
                this.attackEntityFrom(DamageSource.causePlayerDamage(player), Float.MAX_VALUE);
            }

            return true;
        }
        else
        {
            if (!this.world.isRemote && !this.isFlying() && this.isTamed() && this.isOwner(player))
            {
                this.sitGoal.setSitting(!this.isSitting());
            }

            return super.processInteract(player, hand);
        }
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack stack)
    {
        return false;
    }

    public boolean func_70601_bi()
    {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        Block block = this.world.getBlockState(blockpos.down()).getBlock();
        return block instanceof BlockLeaves || block == Blocks.GRASS || block instanceof BlockLog || block == Blocks.AIR && this.world.func_175699_k(blockpos) > 8 && super.func_70601_bi();
    }

    public void func_180430_e(float p_180430_1_, float p_180430_2_)
    {
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
        return false;
    }

    @Nullable
    public EntityAgeable createChild(EntityAgeable ageable)
    {
        return null;
    }

    public static void playAmbientSound(World worldIn, Entity parrotIn)
    {
        if (!parrotIn.isSilent() && !playMimicSound(worldIn, parrotIn) && worldIn.rand.nextInt(200) == 0)
        {
            worldIn.playSound((EntityPlayer)null, parrotIn.posX, parrotIn.posY, parrotIn.posZ, getAmbientSound(worldIn.rand), parrotIn.getSoundCategory(), 1.0F, getPitch(worldIn.rand));
        }
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
    }

    @Nullable
    public SoundEvent getAmbientSound()
    {
        return getAmbientSound(this.rand);
    }

    private static SoundEvent getAmbientSound(Random random)
    {
        if (random.nextInt(1000) == 0)
        {
            List<Integer> list = new ArrayList<Integer>(IMITATION_SOUND_EVENTS.keySet());
            return func_191999_g(((Integer)list.get(random.nextInt(list.size()))).intValue());
        }
        else
        {
            return SoundEvents.ENTITY_PARROT_AMBIENT;
        }
    }

    public static SoundEvent func_191999_g(int p_191999_0_)
    {
        return IMITATION_SOUND_EVENTS.containsKey(p_191999_0_) ? (SoundEvent)IMITATION_SOUND_EVENTS.get(p_191999_0_) : SoundEvents.ENTITY_PARROT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_PARROT_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_PARROT_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_PARROT_STEP, 0.15F, 1.0F);
    }

    protected float playFlySound(float volume)
    {
        this.playSound(SoundEvents.ENTITY_PARROT_FLY, 0.15F, 1.0F);
        return volume + this.flapSpeed / 2.0F;
    }

    protected boolean makeFlySound()
    {
        return true;
    }

    /**
     * Gets the pitch of living sounds in living entities.
     */
    protected float getSoundPitch()
    {
        return getPitch(this.rand);
    }

    private static float getPitch(Random random)
    {
        return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
    }

    public SoundCategory getSoundCategory()
    {
        return SoundCategory.NEUTRAL;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return true;
    }

    protected void collideWithEntity(Entity entityIn)
    {
        if (!(entityIn instanceof EntityPlayer))
        {
            super.collideWithEntity(entityIn);
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isInvulnerableTo(source))
        {
            return false;
        }
        else
        {
            if (this.sitGoal != null)
            {
                this.sitGoal.setSitting(false);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    public int getVariant()
    {
        return MathHelper.clamp(((Integer)this.dataManager.get(VARIANT)).intValue(), 0, 4);
    }

    public void setVariant(int variantIn)
    {
        this.dataManager.set(VARIANT, Integer.valueOf(variantIn));
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.putInt("Variant", this.getVariant());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_192561_ax;
    }

    public boolean isFlying()
    {
        return !this.onGround;
    }

    static
    {
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityBlaze.class), SoundEvents.ENTITY_PARROT_IMITATE_BLAZE);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityCaveSpider.class), SoundEvents.ENTITY_PARROT_IMITATE_SPIDER);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityCreeper.class), SoundEvents.ENTITY_PARROT_IMITATE_CREEPER);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityElderGuardian.class), SoundEvents.ENTITY_PARROT_IMITATE_ELDER_GUARDIAN);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityDragon.class), SoundEvents.ENTITY_PARROT_IMITATE_ENDER_DRAGON);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityEnderman.class), SoundEvents.field_193795_eQ);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityEndermite.class), SoundEvents.ENTITY_PARROT_IMITATE_ENDERMITE);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityEvoker.class), SoundEvents.ENTITY_PARROT_IMITATE_EVOKER);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityGhast.class), SoundEvents.ENTITY_PARROT_IMITATE_GHAST);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityHusk.class), SoundEvents.ENTITY_PARROT_IMITATE_HUSK);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityIllusionIllager.class), SoundEvents.ENTITY_PARROT_IMITATE_ILLUSIONER);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityMagmaCube.class), SoundEvents.ENTITY_PARROT_IMITATE_MAGMA_CUBE);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityPigZombie.class), SoundEvents.field_193822_fl);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityPolarBear.class), SoundEvents.field_193802_eX);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityShulker.class), SoundEvents.ENTITY_PARROT_IMITATE_SHULKER);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntitySilverfish.class), SoundEvents.ENTITY_PARROT_IMITATE_SILVERFISH);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntitySkeleton.class), SoundEvents.ENTITY_PARROT_IMITATE_SKELETON);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntitySlime.class), SoundEvents.ENTITY_PARROT_IMITATE_SLIME);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntitySpider.class), SoundEvents.ENTITY_PARROT_IMITATE_SPIDER);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityStray.class), SoundEvents.ENTITY_PARROT_IMITATE_STRAY);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityVex.class), SoundEvents.ENTITY_PARROT_IMITATE_VEX);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityVindicator.class), SoundEvents.ENTITY_PARROT_IMITATE_VINDICATOR);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityWitch.class), SoundEvents.ENTITY_PARROT_IMITATE_WITCH);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityWither.class), SoundEvents.ENTITY_PARROT_IMITATE_WITHER);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityWitherSkeleton.class), SoundEvents.ENTITY_PARROT_IMITATE_WITHER_SKELETON);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityWolf.class), SoundEvents.field_193820_fj);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityZombie.class), SoundEvents.ENTITY_PARROT_IMITATE_ZOMBIE);
        IMITATION_SOUND_EVENTS.put(EntityList.field_191308_b.getId(EntityZombieVillager.class), SoundEvents.ENTITY_PARROT_IMITATE_ZOMBIE_VILLAGER);
    }
}

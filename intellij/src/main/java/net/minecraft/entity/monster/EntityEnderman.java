package net.minecraft.entity.monster;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityEnderman extends EntityMob
{
    private static final UUID ATTACKING_SPEED_BOOST_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier ATTACKING_SPEED_BOOST = (new AttributeModifier(ATTACKING_SPEED_BOOST_ID, "Attacking speed boost", 0.15000000596046448D, 0)).setSaved(false);
    private static final Set<Block> field_70827_d = Sets.<Block>newIdentityHashSet();
    private static final DataParameter<Optional<IBlockState>> CARRIED_BLOCK = EntityDataManager.<Optional<IBlockState>>createKey(EntityEnderman.class, DataSerializers.OPTIONAL_BLOCK_STATE);
    private static final DataParameter<Boolean> SCREAMING = EntityDataManager.<Boolean>createKey(EntityEnderman.class, DataSerializers.BOOLEAN);
    private int field_184720_bx;
    private int targetChangeTime;

    public EntityEnderman(World p_i1734_1_)
    {
        super(p_i1734_1_);
        this.func_70105_a(0.6F, 2.9F);
        this.stepHeight = 1.0F;
        this.setPathPriority(PathNodeType.WATER, -1.0F);
    }

    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new EntityAISwimming(this));
        this.goalSelector.addGoal(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.goalSelector.addGoal(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.goalSelector.addGoal(8, new EntityAILookIdle(this));
        this.goalSelector.addGoal(10, new EntityEnderman.AIPlaceBlock(this));
        this.goalSelector.addGoal(11, new EntityEnderman.AITakeBlock(this));
        this.targetSelector.addGoal(1, new EntityEnderman.AIFindPlayer(this));
        this.targetSelector.addGoal(2, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetSelector.addGoal(3, new EntityAINearestAttackableTarget(this, EntityEndermite.class, 10, true, false, new Predicate<EntityEndermite>()
        {
            public boolean apply(@Nullable EntityEndermite p_apply_1_)
            {
                return p_apply_1_.isSpawnedByPlayer();
            }
        }));
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
    {
        super.setAttackTarget(entitylivingbaseIn);
        IAttributeInstance iattributeinstance = this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (entitylivingbaseIn == null)
        {
            this.targetChangeTime = 0;
            this.dataManager.set(SCREAMING, Boolean.valueOf(false));
            iattributeinstance.removeModifier(ATTACKING_SPEED_BOOST);
        }
        else
        {
            this.targetChangeTime = this.ticksExisted;
            this.dataManager.set(SCREAMING, Boolean.valueOf(true));

            if (!iattributeinstance.hasModifier(ATTACKING_SPEED_BOOST))
            {
                iattributeinstance.applyModifier(ATTACKING_SPEED_BOOST);
            }
        }
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(CARRIED_BLOCK, Optional.absent());
        this.dataManager.register(SCREAMING, Boolean.valueOf(false));
    }

    public void func_184716_o()
    {
        if (this.ticksExisted >= this.field_184720_bx + 400)
        {
            this.field_184720_bx = this.ticksExisted;

            if (!this.isSilent())
            {
                this.world.playSound(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, SoundEvents.ENTITY_ENDERMAN_STARE, this.getSoundCategory(), 2.5F, 1.0F, false);
            }
        }
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (SCREAMING.equals(key) && this.isScreaming() && this.world.isRemote)
        {
            this.func_184716_o();
        }

        super.notifyDataManagerChange(key);
    }

    public static void func_189763_b(DataFixer p_189763_0_)
    {
        EntityLiving.func_189752_a(p_189763_0_, EntityEnderman.class);
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        IBlockState iblockstate = this.func_175489_ck();

        if (iblockstate != null)
        {
            p_70014_1_.putShort("carried", (short)Block.func_149682_b(iblockstate.getBlock()));
            p_70014_1_.putShort("carriedData", (short)iblockstate.getBlock().func_176201_c(iblockstate));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        IBlockState iblockstate;

        if (compound.contains("carried", 8))
        {
            iblockstate = Block.func_149684_b(compound.getString("carried")).func_176203_a(compound.getShort("carriedData") & 65535);
        }
        else
        {
            iblockstate = Block.func_149729_e(compound.getShort("carried")).func_176203_a(compound.getShort("carriedData") & 65535);
        }

        if (iblockstate == null || iblockstate.getBlock() == null || iblockstate.getMaterial() == Material.AIR)
        {
            iblockstate = null;
        }

        this.func_175490_a(iblockstate);
    }

    /**
     * Checks to see if this enderman should be attacking this player
     */
    private boolean shouldAttackPlayer(EntityPlayer player)
    {
        ItemStack itemstack = player.inventory.armorInventory.get(3);

        if (itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN))
        {
            return false;
        }
        else
        {
            Vec3d vec3d = player.getLook(1.0F).normalize();
            Vec3d vec3d1 = new Vec3d(this.posX - player.posX, this.getBoundingBox().minY + (double)this.getEyeHeight() - (player.posY + (double)player.getEyeHeight()), this.posZ - player.posZ);
            double d0 = vec3d1.length();
            vec3d1 = vec3d1.normalize();
            double d1 = vec3d.dotProduct(vec3d1);
            return d1 > 1.0D - 0.025D / d0 ? player.canEntityBeSeen(this) : false;
        }
    }

    public float getEyeHeight()
    {
        return 2.55F;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick()
    {
        if (this.world.isRemote)
        {
            for (int i = 0; i < 2; ++i)
            {
                this.world.func_175688_a(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.field_70130_N, this.posY + this.rand.nextDouble() * (double)this.field_70131_O - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.field_70130_N, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }

        this.isJumping = false;
        super.livingTick();
    }

    protected void updateAITasks()
    {
        if (this.isWet())
        {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }

        if (this.world.isDaytime() && this.ticksExisted >= this.targetChangeTime + 600)
        {
            float f = this.getBrightness();

            if (f > 0.5F && this.world.func_175678_i(new BlockPos(this)) && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F)
            {
                this.setAttackTarget((EntityLivingBase)null);
                this.teleportRandomly();
            }
        }

        super.updateAITasks();
    }

    /**
     * Teleport the enderman to a random nearby position
     */
    protected boolean teleportRandomly()
    {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.posY + (double)(this.rand.nextInt(64) - 32);
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(d0, d1, d2);
    }

    /**
     * Teleport the enderman to another entity
     */
    protected boolean teleportToEntity(Entity p_70816_1_)
    {
        Vec3d vec3d = new Vec3d(this.posX - p_70816_1_.posX, this.getBoundingBox().minY + (double)(this.field_70131_O / 2.0F) - p_70816_1_.posY + (double)p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
        vec3d = vec3d.normalize();
        double d0 = 16.0D;
        double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.x * 16.0D;
        double d2 = this.posY + (double)(this.rand.nextInt(16) - 8) - vec3d.y * 16.0D;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.z * 16.0D;
        return this.teleportTo(d1, d2, d3);
    }

    /**
     * Teleport the enderman
     */
    private boolean teleportTo(double x, double y, double z)
    {
        boolean flag = this.func_184595_k(x, y, z);

        if (flag)
        {
            this.world.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }

        return flag;
    }

    protected SoundEvent getAmbientSound()
    {
        return this.isScreaming() ? SoundEvents.ENTITY_ENDERMAN_SCREAM : SoundEvents.ENTITY_ENDERMAN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_ENDERMAN_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_ENDERMAN_DEATH;
    }

    protected void func_82160_b(boolean p_82160_1_, int p_82160_2_)
    {
        super.func_82160_b(p_82160_1_, p_82160_2_);
        IBlockState iblockstate = this.func_175489_ck();

        if (iblockstate != null)
        {
            Item item = Item.getItemFromBlock(iblockstate.getBlock());
            int i = item.func_77614_k() ? iblockstate.getBlock().func_176201_c(iblockstate) : 0;
            this.entityDropItem(new ItemStack(item, 1, i), 0.0F);
        }
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_186439_u;
    }

    public void func_175490_a(@Nullable IBlockState p_175490_1_)
    {
        this.dataManager.set(CARRIED_BLOCK, Optional.fromNullable(p_175490_1_));
    }

    @Nullable
    public IBlockState func_175489_ck()
    {
        return (IBlockState)((Optional)this.dataManager.get(CARRIED_BLOCK)).orNull();
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
        else if (source instanceof EntityDamageSourceIndirect)
        {
            for (int i = 0; i < 64; ++i)
            {
                if (this.teleportRandomly())
                {
                    return true;
                }
            }

            return false;
        }
        else
        {
            boolean flag = super.attackEntityFrom(source, amount);

            if (source.isUnblockable() && this.rand.nextInt(10) != 0)
            {
                this.teleportRandomly();
            }

            return flag;
        }
    }

    public boolean isScreaming()
    {
        return ((Boolean)this.dataManager.get(SCREAMING)).booleanValue();
    }

    static
    {
        field_70827_d.add(Blocks.GRASS);
        field_70827_d.add(Blocks.DIRT);
        field_70827_d.add(Blocks.SAND);
        field_70827_d.add(Blocks.GRAVEL);
        field_70827_d.add(Blocks.field_150327_N);
        field_70827_d.add(Blocks.field_150328_O);
        field_70827_d.add(Blocks.BROWN_MUSHROOM);
        field_70827_d.add(Blocks.RED_MUSHROOM);
        field_70827_d.add(Blocks.TNT);
        field_70827_d.add(Blocks.CACTUS);
        field_70827_d.add(Blocks.CLAY);
        field_70827_d.add(Blocks.PUMPKIN);
        field_70827_d.add(Blocks.MELON);
        field_70827_d.add(Blocks.MYCELIUM);
        field_70827_d.add(Blocks.NETHERRACK);
    }

    static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer>
    {
        private final EntityEnderman enderman;
        private EntityPlayer player;
        private int aggroTime;
        private int teleportTime;

        public AIFindPlayer(EntityEnderman endermanIn)
        {
            super(endermanIn, EntityPlayer.class, false);
            this.enderman = endermanIn;
        }

        public boolean shouldExecute()
        {
            double d0 = this.getTargetDistance();
            this.player = this.enderman.world.func_184150_a(this.enderman.posX, this.enderman.posY, this.enderman.posZ, d0, d0, (Function)null, new Predicate<EntityPlayer>()
            {
                public boolean apply(@Nullable EntityPlayer p_apply_1_)
                {
                    return p_apply_1_ != null && AIFindPlayer.this.enderman.shouldAttackPlayer(p_apply_1_);
                }
            });
            return this.player != null;
        }

        public void startExecuting()
        {
            this.aggroTime = 5;
            this.teleportTime = 0;
        }

        public void resetTask()
        {
            this.player = null;
            super.resetTask();
        }

        public boolean shouldContinueExecuting()
        {
            if (this.player != null)
            {
                if (!this.enderman.shouldAttackPlayer(this.player))
                {
                    return false;
                }
                else
                {
                    this.enderman.faceEntity(this.player, 10.0F, 10.0F);
                    return true;
                }
            }
            else
            {
                return this.nearestTarget != null && ((EntityPlayer)this.nearestTarget).isAlive() ? true : super.shouldContinueExecuting();
            }
        }

        public void tick()
        {
            if (this.player != null)
            {
                if (--this.aggroTime <= 0)
                {
                    this.nearestTarget = this.player;
                    this.player = null;
                    super.startExecuting();
                }
            }
            else
            {
                if (this.nearestTarget != null)
                {
                    if (this.enderman.shouldAttackPlayer((EntityPlayer)this.nearestTarget))
                    {
                        if (((EntityPlayer)this.nearestTarget).getDistanceSq(this.enderman) < 16.0D)
                        {
                            this.enderman.teleportRandomly();
                        }

                        this.teleportTime = 0;
                    }
                    else if (((EntityPlayer)this.nearestTarget).getDistanceSq(this.enderman) > 256.0D && this.teleportTime++ >= 30 && this.enderman.teleportToEntity(this.nearestTarget))
                    {
                        this.teleportTime = 0;
                    }
                }

                super.tick();
            }
        }
    }

    static class AIPlaceBlock extends EntityAIBase
    {
        private final EntityEnderman enderman;

        public AIPlaceBlock(EntityEnderman p_i45843_1_)
        {
            this.enderman = p_i45843_1_;
        }

        public boolean shouldExecute()
        {
            if (this.enderman.func_175489_ck() == null)
            {
                return false;
            }
            else if (!this.enderman.world.getGameRules().func_82766_b("mobGriefing"))
            {
                return false;
            }
            else
            {
                return this.enderman.getRNG().nextInt(2000) == 0;
            }
        }

        public void tick()
        {
            Random random = this.enderman.getRNG();
            World world = this.enderman.world;
            int i = MathHelper.floor(this.enderman.posX - 1.0D + random.nextDouble() * 2.0D);
            int j = MathHelper.floor(this.enderman.posY + random.nextDouble() * 2.0D);
            int k = MathHelper.floor(this.enderman.posZ - 1.0D + random.nextDouble() * 2.0D);
            BlockPos blockpos = new BlockPos(i, j, k);
            IBlockState iblockstate = world.getBlockState(blockpos);
            IBlockState iblockstate1 = world.getBlockState(blockpos.down());
            IBlockState iblockstate2 = this.enderman.func_175489_ck();

            if (iblockstate2 != null && this.func_188518_a(world, blockpos, iblockstate2.getBlock(), iblockstate, iblockstate1))
            {
                world.setBlockState(blockpos, iblockstate2, 3);
                this.enderman.func_175490_a((IBlockState)null);
            }
        }

        private boolean func_188518_a(World p_188518_1_, BlockPos p_188518_2_, Block p_188518_3_, IBlockState p_188518_4_, IBlockState p_188518_5_)
        {
            if (!p_188518_3_.func_176196_c(p_188518_1_, p_188518_2_))
            {
                return false;
            }
            else if (p_188518_4_.getMaterial() != Material.AIR)
            {
                return false;
            }
            else if (p_188518_5_.getMaterial() == Material.AIR)
            {
                return false;
            }
            else
            {
                return p_188518_5_.func_185917_h();
            }
        }
    }

    static class AITakeBlock extends EntityAIBase
    {
        private final EntityEnderman enderman;

        public AITakeBlock(EntityEnderman endermanIn)
        {
            this.enderman = endermanIn;
        }

        public boolean shouldExecute()
        {
            if (this.enderman.func_175489_ck() != null)
            {
                return false;
            }
            else if (!this.enderman.world.getGameRules().func_82766_b("mobGriefing"))
            {
                return false;
            }
            else
            {
                return this.enderman.getRNG().nextInt(20) == 0;
            }
        }

        public void tick()
        {
            Random random = this.enderman.getRNG();
            World world = this.enderman.world;
            int i = MathHelper.floor(this.enderman.posX - 2.0D + random.nextDouble() * 4.0D);
            int j = MathHelper.floor(this.enderman.posY + random.nextDouble() * 3.0D);
            int k = MathHelper.floor(this.enderman.posZ - 2.0D + random.nextDouble() * 4.0D);
            BlockPos blockpos = new BlockPos(i, j, k);
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            RayTraceResult raytraceresult = world.func_147447_a(new Vec3d((double)((float)MathHelper.floor(this.enderman.posX) + 0.5F), (double)((float)j + 0.5F), (double)((float)MathHelper.floor(this.enderman.posZ) + 0.5F)), new Vec3d((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F)), false, true, false);
            boolean flag = raytraceresult != null && raytraceresult.func_178782_a().equals(blockpos);

            if (EntityEnderman.field_70827_d.contains(block) && flag)
            {
                this.enderman.func_175490_a(iblockstate);
                world.func_175698_g(blockpos);
            }
        }
    }
}

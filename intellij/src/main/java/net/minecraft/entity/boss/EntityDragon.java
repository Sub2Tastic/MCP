package net.minecraft.entity.boss;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.dragon.phase.IPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathHeap;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityDragon extends EntityLiving implements IEntityMultiPart, IMob
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final DataParameter<Integer> PHASE = EntityDataManager.<Integer>createKey(EntityDragon.class, DataSerializers.VARINT);
    public double[][] ringBuffer = new double[64][3];
    public int ringBufferIndex = -1;
    public MultiPartEntityPart[] dragonParts;
    public MultiPartEntityPart dragonPartHead = new MultiPartEntityPart(this, "head", 6.0F, 6.0F);
    public MultiPartEntityPart dragonPartNeck = new MultiPartEntityPart(this, "neck", 6.0F, 6.0F);
    public MultiPartEntityPart dragonPartBody = new MultiPartEntityPart(this, "body", 8.0F, 8.0F);
    public MultiPartEntityPart dragonPartTail1 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
    public MultiPartEntityPart dragonPartTail2 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
    public MultiPartEntityPart dragonPartTail3 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
    public MultiPartEntityPart dragonPartRightWing = new MultiPartEntityPart(this, "wing", 4.0F, 4.0F);
    public MultiPartEntityPart dragonPartLeftWing = new MultiPartEntityPart(this, "wing", 4.0F, 4.0F);
    public float prevAnimTime;
    public float animTime;
    public boolean slowed;
    public int deathTicks;
    public EntityEnderCrystal closestEnderCrystal;
    private final DragonFightManager fightManager;
    private final PhaseManager phaseManager;
    private int growlTime = 200;
    private int sittingDamageReceived;
    private final PathPoint[] pathPoints = new PathPoint[24];
    private final int[] neighbors = new int[24];
    private final PathHeap pathFindQueue = new PathHeap();

    public EntityDragon(World p_i1700_1_)
    {
        super(p_i1700_1_);
        this.dragonParts = new MultiPartEntityPart[] {this.dragonPartHead, this.dragonPartNeck, this.dragonPartBody, this.dragonPartTail1, this.dragonPartTail2, this.dragonPartTail3, this.dragonPartRightWing, this.dragonPartLeftWing};
        this.setHealth(this.getMaxHealth());
        this.func_70105_a(16.0F, 8.0F);
        this.noClip = true;
        this.field_70178_ae = true;
        this.growlTime = 100;
        this.ignoreFrustumCheck = true;

        if (!p_i1700_1_.isRemote && p_i1700_1_.dimension instanceof WorldProviderEnd)
        {
            this.fightManager = ((WorldProviderEnd)p_i1700_1_.dimension).getDragonFightManager();
        }
        else
        {
            this.fightManager = null;
        }

        this.phaseManager = new PhaseManager(this);
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
    }

    protected void registerData()
    {
        super.registerData();
        this.getDataManager().register(PHASE, Integer.valueOf(PhaseList.HOVER.getId()));
    }

    /**
     * Returns a double[3] array with movement offsets, used to calculate trailing tail/neck positions. [0] = yaw
     * offset, [1] = y offset, [2] = unused, always 0. Parameters: buffer index offset, partial ticks.
     */
    public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_)
    {
        if (this.getHealth() <= 0.0F)
        {
            p_70974_2_ = 0.0F;
        }

        p_70974_2_ = 1.0F - p_70974_2_;
        int i = this.ringBufferIndex - p_70974_1_ & 63;
        int j = this.ringBufferIndex - p_70974_1_ - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.ringBuffer[i][0];
        double d1 = MathHelper.wrapDegrees(this.ringBuffer[j][0] - d0);
        adouble[0] = d0 + d1 * (double)p_70974_2_;
        d0 = this.ringBuffer[i][1];
        d1 = this.ringBuffer[j][1] - d0;
        adouble[1] = d0 + d1 * (double)p_70974_2_;
        adouble[2] = this.ringBuffer[i][2] + (this.ringBuffer[j][2] - this.ringBuffer[i][2]) * (double)p_70974_2_;
        return adouble;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick()
    {
        if (this.world.isRemote)
        {
            this.setHealth(this.getHealth());

            if (!this.isSilent())
            {
                float f = MathHelper.cos(this.animTime * ((float)Math.PI * 2F));
                float f1 = MathHelper.cos(this.prevAnimTime * ((float)Math.PI * 2F));

                if (f1 <= -0.3F && f >= -0.3F)
                {
                    this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDER_DRAGON_FLAP, this.getSoundCategory(), 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false);
                }

                if (!this.phaseManager.getCurrentPhase().getIsStationary() && --this.growlTime < 0)
                {
                    this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDER_DRAGON_GROWL, this.getSoundCategory(), 2.5F, 0.8F + this.rand.nextFloat() * 0.3F, false);
                    this.growlTime = 200 + this.rand.nextInt(200);
                }
            }
        }

        this.prevAnimTime = this.animTime;

        if (this.getHealth() <= 0.0F)
        {
            float f12 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            float f13 = (this.rand.nextFloat() - 0.5F) * 4.0F;
            float f15 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            this.world.func_175688_a(EnumParticleTypes.EXPLOSION_LARGE, this.posX + (double)f12, this.posY + 2.0D + (double)f13, this.posZ + (double)f15, 0.0D, 0.0D, 0.0D);
        }
        else
        {
            this.updateDragonEnderCrystal();
            float f11 = 0.2F / (MathHelper.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y) * 10.0F + 1.0F);
            f11 = f11 * (float)Math.pow(2.0D, this.field_70181_x);

            if (this.phaseManager.getCurrentPhase().getIsStationary())
            {
                this.animTime += 0.1F;
            }
            else if (this.slowed)
            {
                this.animTime += f11 * 0.5F;
            }
            else
            {
                this.animTime += f11;
            }

            this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);

            if (this.isAIDisabled())
            {
                this.animTime = 0.5F;
            }
            else
            {
                if (this.ringBufferIndex < 0)
                {
                    for (int i = 0; i < this.ringBuffer.length; ++i)
                    {
                        this.ringBuffer[i][0] = (double)this.rotationYaw;
                        this.ringBuffer[i][1] = this.posY;
                    }
                }

                if (++this.ringBufferIndex == this.ringBuffer.length)
                {
                    this.ringBufferIndex = 0;
                }

                this.ringBuffer[this.ringBufferIndex][0] = (double)this.rotationYaw;
                this.ringBuffer[this.ringBufferIndex][1] = this.posY;

                if (this.world.isRemote)
                {
                    if (this.newPosRotationIncrements > 0)
                    {
                        double d5 = this.posX + (this.interpTargetX - this.posX) / (double)this.newPosRotationIncrements;
                        double d0 = this.posY + (this.interpTargetY - this.posY) / (double)this.newPosRotationIncrements;
                        double d1 = this.posZ + (this.interpTargetZ - this.posZ) / (double)this.newPosRotationIncrements;
                        double d2 = MathHelper.wrapDegrees(this.interpTargetYaw - (double)this.rotationYaw);
                        this.rotationYaw = (float)((double)this.rotationYaw + d2 / (double)this.newPosRotationIncrements);
                        this.rotationPitch = (float)((double)this.rotationPitch + (this.interpTargetPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
                        --this.newPosRotationIncrements;
                        this.setPosition(d5, d0, d1);
                        this.setRotation(this.rotationYaw, this.rotationPitch);
                    }

                    this.phaseManager.getCurrentPhase().clientTick();
                }
                else
                {
                    IPhase iphase = this.phaseManager.getCurrentPhase();
                    iphase.serverTick();

                    if (this.phaseManager.getCurrentPhase() != iphase)
                    {
                        iphase = this.phaseManager.getCurrentPhase();
                        iphase.serverTick();
                    }

                    Vec3d vec3d = iphase.getTargetLocation();

                    if (vec3d != null)
                    {
                        double d6 = vec3d.x - this.posX;
                        double d7 = vec3d.y - this.posY;
                        double d8 = vec3d.z - this.posZ;
                        double d3 = d6 * d6 + d7 * d7 + d8 * d8;
                        float f5 = iphase.getMaxRiseOrFall();
                        d7 = MathHelper.clamp(d7 / (double)MathHelper.sqrt(d6 * d6 + d8 * d8), (double)(-f5), (double)f5);
                        this.field_70181_x += d7 * 0.10000000149011612D;
                        this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
                        double d4 = MathHelper.clamp(MathHelper.wrapDegrees(180.0D - MathHelper.atan2(d6, d8) * (180D / Math.PI) - (double)this.rotationYaw), -50.0D, 50.0D);
                        Vec3d vec3d1 = (new Vec3d(vec3d.x - this.posX, vec3d.y - this.posY, vec3d.z - this.posZ)).normalize();
                        Vec3d vec3d2 = (new Vec3d((double)MathHelper.sin(this.rotationYaw * 0.017453292F), this.field_70181_x, (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)))).normalize();
                        float f7 = Math.max(((float)vec3d2.dotProduct(vec3d1) + 0.5F) / 1.5F, 0.0F);
                        this.field_70704_bt *= 0.8F;
                        this.field_70704_bt = (float)((double)this.field_70704_bt + d4 * (double)iphase.getYawFactor());
                        this.rotationYaw += this.field_70704_bt * 0.1F;
                        float f8 = (float)(2.0D / (d3 + 1.0D));
                        float f9 = 0.06F;
                        this.func_191958_b(0.0F, 0.0F, -1.0F, 0.06F * (f7 * f8 + (1.0F - f8)));

                        if (this.slowed)
                        {
                            this.func_70091_d(MoverType.SELF, this.field_70159_w * 0.800000011920929D, this.field_70181_x * 0.800000011920929D, this.field_70179_y * 0.800000011920929D);
                        }
                        else
                        {
                            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
                        }

                        Vec3d vec3d3 = (new Vec3d(this.field_70159_w, this.field_70181_x, this.field_70179_y)).normalize();
                        float f10 = ((float)vec3d3.dotProduct(vec3d2) + 1.0F) / 2.0F;
                        f10 = 0.8F + 0.15F * f10;
                        this.field_70159_w *= (double)f10;
                        this.field_70179_y *= (double)f10;
                        this.field_70181_x *= 0.9100000262260437D;
                    }
                }

                this.renderYawOffset = this.rotationYaw;
                this.dragonPartHead.field_70130_N = 1.0F;
                this.dragonPartHead.field_70131_O = 1.0F;
                this.dragonPartNeck.field_70130_N = 3.0F;
                this.dragonPartNeck.field_70131_O = 3.0F;
                this.dragonPartTail1.field_70130_N = 2.0F;
                this.dragonPartTail1.field_70131_O = 2.0F;
                this.dragonPartTail2.field_70130_N = 2.0F;
                this.dragonPartTail2.field_70131_O = 2.0F;
                this.dragonPartTail3.field_70130_N = 2.0F;
                this.dragonPartTail3.field_70131_O = 2.0F;
                this.dragonPartBody.field_70131_O = 3.0F;
                this.dragonPartBody.field_70130_N = 5.0F;
                this.dragonPartRightWing.field_70131_O = 2.0F;
                this.dragonPartRightWing.field_70130_N = 4.0F;
                this.dragonPartLeftWing.field_70131_O = 3.0F;
                this.dragonPartLeftWing.field_70130_N = 4.0F;
                Vec3d[] avec3d = new Vec3d[this.dragonParts.length];

                for (int j = 0; j < this.dragonParts.length; ++j)
                {
                    avec3d[j] = new Vec3d(this.dragonParts[j].posX, this.dragonParts[j].posY, this.dragonParts[j].posZ);
                }

                float f14 = (float)(this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F * 0.017453292F;
                float f16 = MathHelper.cos(f14);
                float f2 = MathHelper.sin(f14);
                float f17 = this.rotationYaw * 0.017453292F;
                float f3 = MathHelper.sin(f17);
                float f18 = MathHelper.cos(f17);
                this.dragonPartBody.tick();
                this.dragonPartBody.setLocationAndAngles(this.posX + (double)(f3 * 0.5F), this.posY, this.posZ - (double)(f18 * 0.5F), 0.0F, 0.0F);
                this.dragonPartRightWing.tick();
                this.dragonPartRightWing.setLocationAndAngles(this.posX + (double)(f18 * 4.5F), this.posY + 2.0D, this.posZ + (double)(f3 * 4.5F), 0.0F, 0.0F);
                this.dragonPartLeftWing.tick();
                this.dragonPartLeftWing.setLocationAndAngles(this.posX - (double)(f18 * 4.5F), this.posY + 2.0D, this.posZ - (double)(f3 * 4.5F), 0.0F, 0.0F);

                if (!this.world.isRemote && this.hurtTime == 0)
                {
                    this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartRightWing.getBoundingBox().grow(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
                    this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartLeftWing.getBoundingBox().grow(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
                    this.attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getBoundingBox().grow(1.0D)));
                    this.attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartNeck.getBoundingBox().grow(1.0D)));
                }

                double[] adouble = this.getMovementOffsets(5, 1.0F);
                float f19 = MathHelper.sin(this.rotationYaw * 0.017453292F - this.field_70704_bt * 0.01F);
                float f4 = MathHelper.cos(this.rotationYaw * 0.017453292F - this.field_70704_bt * 0.01F);
                this.dragonPartHead.tick();
                this.dragonPartNeck.tick();
                float f20 = this.func_184662_q(1.0F);
                this.dragonPartHead.setLocationAndAngles(this.posX + (double)(f19 * 6.5F * f16), this.posY + (double)f20 + (double)(f2 * 6.5F), this.posZ - (double)(f4 * 6.5F * f16), 0.0F, 0.0F);
                this.dragonPartNeck.setLocationAndAngles(this.posX + (double)(f19 * 5.5F * f16), this.posY + (double)f20 + (double)(f2 * 5.5F), this.posZ - (double)(f4 * 5.5F * f16), 0.0F, 0.0F);

                for (int k = 0; k < 3; ++k)
                {
                    MultiPartEntityPart multipartentitypart = null;

                    if (k == 0)
                    {
                        multipartentitypart = this.dragonPartTail1;
                    }

                    if (k == 1)
                    {
                        multipartentitypart = this.dragonPartTail2;
                    }

                    if (k == 2)
                    {
                        multipartentitypart = this.dragonPartTail3;
                    }

                    double[] adouble1 = this.getMovementOffsets(12 + k * 2, 1.0F);
                    float f21 = this.rotationYaw * 0.017453292F + this.simplifyAngle(adouble1[0] - adouble[0]) * 0.017453292F;
                    float f6 = MathHelper.sin(f21);
                    float f22 = MathHelper.cos(f21);
                    float f23 = 1.5F;
                    float f24 = (float)(k + 1) * 2.0F;
                    multipartentitypart.tick();
                    multipartentitypart.setLocationAndAngles(this.posX - (double)((f3 * 1.5F + f6 * f24) * f16), this.posY + (adouble1[1] - adouble[1]) - (double)((f24 + 1.5F) * f2) + 1.5D, this.posZ + (double)((f18 * 1.5F + f22 * f24) * f16), 0.0F, 0.0F);
                }

                if (!this.world.isRemote)
                {
                    this.slowed = this.destroyBlocksInAABB(this.dragonPartHead.getBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartNeck.getBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartBody.getBoundingBox());

                    if (this.fightManager != null)
                    {
                        this.fightManager.dragonUpdate(this);
                    }
                }

                for (int l = 0; l < this.dragonParts.length; ++l)
                {
                    this.dragonParts[l].prevPosX = avec3d[l].x;
                    this.dragonParts[l].prevPosY = avec3d[l].y;
                    this.dragonParts[l].prevPosZ = avec3d[l].z;
                }
            }
        }
    }

    private float func_184662_q(float p_184662_1_)
    {
        double d0;

        if (this.phaseManager.getCurrentPhase().getIsStationary())
        {
            d0 = -1.0D;
        }
        else
        {
            double[] adouble = this.getMovementOffsets(5, 1.0F);
            double[] adouble1 = this.getMovementOffsets(0, 1.0F);
            d0 = adouble[1] - adouble1[1];
        }

        return (float)d0;
    }

    /**
     * Updates the state of the enderdragon's current endercrystal.
     */
    private void updateDragonEnderCrystal()
    {
        if (this.closestEnderCrystal != null)
        {
            if (this.closestEnderCrystal.removed)
            {
                this.closestEnderCrystal = null;
            }
            else if (this.ticksExisted % 10 == 0 && this.getHealth() < this.getMaxHealth())
            {
                this.setHealth(this.getHealth() + 1.0F);
            }
        }

        if (this.rand.nextInt(10) == 0)
        {
            List<EntityEnderCrystal> list = this.world.<EntityEnderCrystal>func_72872_a(EntityEnderCrystal.class, this.getBoundingBox().grow(32.0D));
            EntityEnderCrystal entityendercrystal = null;
            double d0 = Double.MAX_VALUE;

            for (EntityEnderCrystal entityendercrystal1 : list)
            {
                double d1 = entityendercrystal1.getDistanceSq(this);

                if (d1 < d0)
                {
                    d0 = d1;
                    entityendercrystal = entityendercrystal1;
                }
            }

            this.closestEnderCrystal = entityendercrystal;
        }
    }

    /**
     * Pushes all entities inside the list away from the enderdragon.
     */
    private void collideWithEntities(List<Entity> p_70970_1_)
    {
        double d0 = (this.dragonPartBody.getBoundingBox().minX + this.dragonPartBody.getBoundingBox().maxX) / 2.0D;
        double d1 = (this.dragonPartBody.getBoundingBox().minZ + this.dragonPartBody.getBoundingBox().maxZ) / 2.0D;

        for (Entity entity : p_70970_1_)
        {
            if (entity instanceof EntityLivingBase)
            {
                double d2 = entity.posX - d0;
                double d3 = entity.posZ - d1;
                double d4 = d2 * d2 + d3 * d3;
                entity.addVelocity(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);

                if (!this.phaseManager.getCurrentPhase().getIsStationary() && ((EntityLivingBase)entity).getRevengeTimer() < entity.ticksExisted - 2)
                {
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5.0F);
                    this.applyEnchantments(this, entity);
                }
            }
        }
    }

    /**
     * Attacks all entities inside this list, dealing 5 hearts of damage.
     */
    private void attackEntitiesInList(List<Entity> p_70971_1_)
    {
        for (int i = 0; i < p_70971_1_.size(); ++i)
        {
            Entity entity = p_70971_1_.get(i);

            if (entity instanceof EntityLivingBase)
            {
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
                this.applyEnchantments(this, entity);
            }
        }
    }

    /**
     * Simplifies the value of a number by adding/subtracting 180 to the point that the number is between -180 and 180.
     */
    private float simplifyAngle(double p_70973_1_)
    {
        return (float)MathHelper.wrapDegrees(p_70973_1_);
    }

    /**
     * Destroys all blocks that aren't associated with 'The End' inside the given bounding box.
     */
    private boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_)
    {
        int i = MathHelper.floor(p_70972_1_.minX);
        int j = MathHelper.floor(p_70972_1_.minY);
        int k = MathHelper.floor(p_70972_1_.minZ);
        int l = MathHelper.floor(p_70972_1_.maxX);
        int i1 = MathHelper.floor(p_70972_1_.maxY);
        int j1 = MathHelper.floor(p_70972_1_.maxZ);
        boolean flag = false;
        boolean flag1 = false;

        for (int k1 = i; k1 <= l; ++k1)
        {
            for (int l1 = j; l1 <= i1; ++l1)
            {
                for (int i2 = k; i2 <= j1; ++i2)
                {
                    BlockPos blockpos = new BlockPos(k1, l1, i2);
                    IBlockState iblockstate = this.world.getBlockState(blockpos);
                    Block block = iblockstate.getBlock();

                    if (iblockstate.getMaterial() != Material.AIR && iblockstate.getMaterial() != Material.FIRE)
                    {
                        if (!this.world.getGameRules().func_82766_b("mobGriefing"))
                        {
                            flag = true;
                        }
                        else if (block != Blocks.BARRIER && block != Blocks.OBSIDIAN && block != Blocks.END_STONE && block != Blocks.BEDROCK && block != Blocks.END_PORTAL && block != Blocks.END_PORTAL_FRAME)
                        {
                            if (block != Blocks.COMMAND_BLOCK && block != Blocks.REPEATING_COMMAND_BLOCK && block != Blocks.CHAIN_COMMAND_BLOCK && block != Blocks.IRON_BARS && block != Blocks.END_GATEWAY)
                            {
                                flag1 = this.world.func_175698_g(blockpos) || flag1;
                            }
                            else
                            {
                                flag = true;
                            }
                        }
                        else
                        {
                            flag = true;
                        }
                    }
                }
            }
        }

        if (flag1)
        {
            double d0 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * (double)this.rand.nextFloat();
            double d1 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * (double)this.rand.nextFloat();
            double d2 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * (double)this.rand.nextFloat();
            this.world.func_175688_a(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }

        return flag;
    }

    public boolean func_70965_a(MultiPartEntityPart p_70965_1_, DamageSource p_70965_2_, float p_70965_3_)
    {
        p_70965_3_ = this.phaseManager.getCurrentPhase().func_188656_a(p_70965_1_, p_70965_2_, p_70965_3_);

        if (p_70965_1_ != this.dragonPartHead)
        {
            p_70965_3_ = p_70965_3_ / 4.0F + Math.min(p_70965_3_, 1.0F);
        }

        if (p_70965_3_ < 0.01F)
        {
            return false;
        }
        else
        {
            if (p_70965_2_.getTrueSource() instanceof EntityPlayer || p_70965_2_.isExplosion())
            {
                float f = this.getHealth();
                this.attackDragonFrom(p_70965_2_, p_70965_3_);

                if (this.getHealth() <= 0.0F && !this.phaseManager.getCurrentPhase().getIsStationary())
                {
                    this.setHealth(1.0F);
                    this.phaseManager.setPhase(PhaseList.DYING);
                }

                if (this.phaseManager.getCurrentPhase().getIsStationary())
                {
                    this.sittingDamageReceived = (int)((float)this.sittingDamageReceived + (f - this.getHealth()));

                    if ((float)this.sittingDamageReceived > 0.25F * this.getMaxHealth())
                    {
                        this.sittingDamageReceived = 0;
                        this.phaseManager.setPhase(PhaseList.TAKEOFF);
                    }
                }
            }

            return true;
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage())
        {
            this.func_70965_a(this.dragonPartBody, source, amount);
        }

        return false;
    }

    /**
     * Provides a way to cause damage to an ender dragon.
     */
    protected boolean attackDragonFrom(DamageSource source, float amount)
    {
        return super.attackEntityFrom(source, amount);
    }

    /**
     * Called by the /kill command.
     */
    public void onKillCommand()
    {
        this.remove();

        if (this.fightManager != null)
        {
            this.fightManager.dragonUpdate(this);
            this.fightManager.processDragonDeath(this);
        }
    }

    /**
     * handles entity death timer, experience orb and particle creation
     */
    protected void onDeathUpdate()
    {
        if (this.fightManager != null)
        {
            this.fightManager.dragonUpdate(this);
        }

        ++this.deathTicks;

        if (this.deathTicks >= 180 && this.deathTicks <= 200)
        {
            float f = (this.rand.nextFloat() - 0.5F) * 8.0F;
            float f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
            float f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            this.world.func_175688_a(EnumParticleTypes.EXPLOSION_HUGE, this.posX + (double)f, this.posY + 2.0D + (double)f1, this.posZ + (double)f2, 0.0D, 0.0D, 0.0D);
        }

        boolean flag = this.world.getGameRules().func_82766_b("doMobLoot");
        int i = 500;

        if (this.fightManager != null && !this.fightManager.hasPreviouslyKilledDragon())
        {
            i = 12000;
        }

        if (!this.world.isRemote)
        {
            if (this.deathTicks > 150 && this.deathTicks % 5 == 0 && flag)
            {
                this.dropExperience(MathHelper.floor((float)i * 0.08F));
            }

            if (this.deathTicks == 1)
            {
                this.world.playBroadcastSound(1028, new BlockPos(this), 0);
            }
        }

        this.func_70091_d(MoverType.SELF, 0.0D, 0.10000000149011612D, 0.0D);
        this.rotationYaw += 20.0F;
        this.renderYawOffset = this.rotationYaw;

        if (this.deathTicks == 200 && !this.world.isRemote)
        {
            if (flag)
            {
                this.dropExperience(MathHelper.floor((float)i * 0.2F));
            }

            if (this.fightManager != null)
            {
                this.fightManager.processDragonDeath(this);
            }

            this.remove();
        }
    }

    private void dropExperience(int p_184668_1_)
    {
        while (p_184668_1_ > 0)
        {
            int i = EntityXPOrb.getXPSplit(p_184668_1_);
            p_184668_1_ -= i;
            this.world.addEntity0(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, i));
        }
    }

    /**
     * Generates values for the fields pathPoints, and neighbors, and then returns the nearest pathPoint to the
     * specified position.
     */
    public int initPathPoints()
    {
        if (this.pathPoints[0] == null)
        {
            for (int i = 0; i < 24; ++i)
            {
                int j = 5;
                int l;
                int i1;

                if (i < 12)
                {
                    l = (int)(60.0F * MathHelper.cos(2.0F * (-(float)Math.PI + 0.2617994F * (float)i)));
                    i1 = (int)(60.0F * MathHelper.sin(2.0F * (-(float)Math.PI + 0.2617994F * (float)i)));
                }
                else if (i < 20)
                {
                    int lvt_3_1_ = i - 12;
                    l = (int)(40.0F * MathHelper.cos(2.0F * (-(float)Math.PI + 0.3926991F * (float)lvt_3_1_)));
                    i1 = (int)(40.0F * MathHelper.sin(2.0F * (-(float)Math.PI + 0.3926991F * (float)lvt_3_1_)));
                    j += 10;
                }
                else
                {
                    int k1 = i - 20;
                    l = (int)(20.0F * MathHelper.cos(2.0F * (-(float)Math.PI + ((float)Math.PI / 4F) * (float)k1)));
                    i1 = (int)(20.0F * MathHelper.sin(2.0F * (-(float)Math.PI + ((float)Math.PI / 4F) * (float)k1)));
                }

                int j1 = Math.max(this.world.getSeaLevel() + 10, this.world.func_175672_r(new BlockPos(l, 0, i1)).getY() + j);
                this.pathPoints[i] = new PathPoint(l, j1, i1);
            }

            this.neighbors[0] = 6146;
            this.neighbors[1] = 8197;
            this.neighbors[2] = 8202;
            this.neighbors[3] = 16404;
            this.neighbors[4] = 32808;
            this.neighbors[5] = 32848;
            this.neighbors[6] = 65696;
            this.neighbors[7] = 131392;
            this.neighbors[8] = 131712;
            this.neighbors[9] = 263424;
            this.neighbors[10] = 526848;
            this.neighbors[11] = 525313;
            this.neighbors[12] = 1581057;
            this.neighbors[13] = 3166214;
            this.neighbors[14] = 2138120;
            this.neighbors[15] = 6373424;
            this.neighbors[16] = 4358208;
            this.neighbors[17] = 12910976;
            this.neighbors[18] = 9044480;
            this.neighbors[19] = 9706496;
            this.neighbors[20] = 15216640;
            this.neighbors[21] = 13688832;
            this.neighbors[22] = 11763712;
            this.neighbors[23] = 8257536;
        }

        return this.getNearestPpIdx(this.posX, this.posY, this.posZ);
    }

    /**
     * Returns the index into pathPoints of the nearest PathPoint.
     */
    public int getNearestPpIdx(double x, double y, double z)
    {
        float f = 10000.0F;
        int i = 0;
        PathPoint pathpoint = new PathPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
        int j = 0;

        if (this.fightManager == null || this.fightManager.getNumAliveCrystals() == 0)
        {
            j = 12;
        }

        for (int k = j; k < 24; ++k)
        {
            if (this.pathPoints[k] != null)
            {
                float f1 = this.pathPoints[k].distanceToSquared(pathpoint);

                if (f1 < f)
                {
                    f = f1;
                    i = k;
                }
            }
        }

        return i;
    }

    @Nullable

    /**
     * Find and return a path among the circles described by pathPoints, or null if the shortest path would just be
     * directly between the start and finish with no intermediate points.
     *  
     * Starting with pathPoint[startIdx], it searches the neighboring points (and their neighboring points, and so on)
     * until it reaches pathPoint[finishIdx], at which point it calls makePath to seal the deal.
     */
    public Path findPath(int startIdx, int finishIdx, @Nullable PathPoint andThen)
    {
        for (int i = 0; i < 24; ++i)
        {
            PathPoint pathpoint = this.pathPoints[i];
            pathpoint.visited = false;
            pathpoint.distanceToTarget = 0.0F;
            pathpoint.totalPathDistance = 0.0F;
            pathpoint.distanceToNext = 0.0F;
            pathpoint.previous = null;
            pathpoint.index = -1;
        }

        PathPoint pathpoint4 = this.pathPoints[startIdx];
        PathPoint pathpoint5 = this.pathPoints[finishIdx];
        pathpoint4.totalPathDistance = 0.0F;
        pathpoint4.distanceToNext = pathpoint4.distanceTo(pathpoint5);
        pathpoint4.distanceToTarget = pathpoint4.distanceToNext;
        this.pathFindQueue.clearPath();
        this.pathFindQueue.addPoint(pathpoint4);
        PathPoint pathpoint1 = pathpoint4;
        int j = 0;

        if (this.fightManager == null || this.fightManager.getNumAliveCrystals() == 0)
        {
            j = 12;
        }

        while (!this.pathFindQueue.isPathEmpty())
        {
            PathPoint pathpoint2 = this.pathFindQueue.dequeue();

            if (pathpoint2.equals(pathpoint5))
            {
                if (andThen != null)
                {
                    andThen.previous = pathpoint5;
                    pathpoint5 = andThen;
                }

                return this.makePath(pathpoint4, pathpoint5);
            }

            if (pathpoint2.distanceTo(pathpoint5) < pathpoint1.distanceTo(pathpoint5))
            {
                pathpoint1 = pathpoint2;
            }

            pathpoint2.visited = true;
            int k = 0;

            for (int l = 0; l < 24; ++l)
            {
                if (this.pathPoints[l] == pathpoint2)
                {
                    k = l;
                    break;
                }
            }

            for (int i1 = j; i1 < 24; ++i1)
            {
                if ((this.neighbors[k] & 1 << i1) > 0)
                {
                    PathPoint pathpoint3 = this.pathPoints[i1];

                    if (!pathpoint3.visited)
                    {
                        float f = pathpoint2.totalPathDistance + pathpoint2.distanceTo(pathpoint3);

                        if (!pathpoint3.isAssigned() || f < pathpoint3.totalPathDistance)
                        {
                            pathpoint3.previous = pathpoint2;
                            pathpoint3.totalPathDistance = f;
                            pathpoint3.distanceToNext = pathpoint3.distanceTo(pathpoint5);

                            if (pathpoint3.isAssigned())
                            {
                                this.pathFindQueue.changeDistance(pathpoint3, pathpoint3.totalPathDistance + pathpoint3.distanceToNext);
                            }
                            else
                            {
                                pathpoint3.distanceToTarget = pathpoint3.totalPathDistance + pathpoint3.distanceToNext;
                                this.pathFindQueue.addPoint(pathpoint3);
                            }
                        }
                    }
                }
            }
        }

        if (pathpoint1 == pathpoint4)
        {
            return null;
        }
        else
        {
            LOGGER.debug("Failed to find path from {} to {}", Integer.valueOf(startIdx), Integer.valueOf(finishIdx));

            if (andThen != null)
            {
                andThen.previous = pathpoint1;
                pathpoint1 = andThen;
            }

            return this.makePath(pathpoint4, pathpoint1);
        }
    }

    /**
     * Create and return a new PathEntity defining a path from the start to the finish, using the connections already
     * made by the caller, findPath.
     */
    private Path makePath(PathPoint start, PathPoint finish)
    {
        int i = 1;

        for (PathPoint pathpoint = finish; pathpoint.previous != null; pathpoint = pathpoint.previous)
        {
            ++i;
        }

        PathPoint[] apathpoint = new PathPoint[i];
        PathPoint pathpoint1 = finish;
        --i;

        for (apathpoint[i] = finish; pathpoint1.previous != null; apathpoint[i] = pathpoint1)
        {
            pathpoint1 = pathpoint1.previous;
            --i;
        }

        return new Path(apathpoint);
    }

    public static void func_189755_b(DataFixer p_189755_0_)
    {
        EntityLiving.func_189752_a(p_189755_0_, EntityDragon.class);
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.putInt("DragonPhase", this.phaseManager.getCurrentPhase().getType().getId());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);

        if (compound.contains("DragonPhase"))
        {
            this.phaseManager.setPhase(PhaseList.getById(compound.getInt("DragonPhase")));
        }
    }

    /**
     * Makes the entity despawn if requirements are reached
     */
    protected void checkDespawn()
    {
    }

    public Entity[] func_70021_al()
    {
        return this.dragonParts;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }

    public World func_82194_d()
    {
        return this.world;
    }

    public SoundCategory getSoundCategory()
    {
        return SoundCategory.HOSTILE;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_ENDER_DRAGON_HURT;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 5.0F;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_191189_ay;
    }

    public float getHeadPartYOffset(int p_184667_1_, double[] p_184667_2_, double[] p_184667_3_)
    {
        IPhase iphase = this.phaseManager.getCurrentPhase();
        PhaseList <? extends IPhase > phaselist = iphase.getType();
        double d0;

        if (phaselist != PhaseList.LANDING && phaselist != PhaseList.TAKEOFF)
        {
            if (iphase.getIsStationary())
            {
                d0 = (double)p_184667_1_;
            }
            else if (p_184667_1_ == 6)
            {
                d0 = 0.0D;
            }
            else
            {
                d0 = p_184667_3_[1] - p_184667_2_[1];
            }
        }
        else
        {
            BlockPos blockpos = this.world.func_175672_r(WorldGenEndPodium.END_PODIUM_LOCATION);
            float f = Math.max(MathHelper.sqrt(this.func_174831_c(blockpos)) / 4.0F, 1.0F);
            d0 = (double)((float)p_184667_1_ / f);
        }

        return (float)d0;
    }

    public Vec3d getHeadLookVec(float p_184665_1_)
    {
        IPhase iphase = this.phaseManager.getCurrentPhase();
        PhaseList <? extends IPhase > phaselist = iphase.getType();
        Vec3d vec3d;

        if (phaselist != PhaseList.LANDING && phaselist != PhaseList.TAKEOFF)
        {
            if (iphase.getIsStationary())
            {
                float f4 = this.rotationPitch;
                float f5 = 1.5F;
                this.rotationPitch = -45.0F;
                vec3d = this.getLook(p_184665_1_);
                this.rotationPitch = f4;
            }
            else
            {
                vec3d = this.getLook(p_184665_1_);
            }
        }
        else
        {
            BlockPos blockpos = this.world.func_175672_r(WorldGenEndPodium.END_PODIUM_LOCATION);
            float f = Math.max(MathHelper.sqrt(this.func_174831_c(blockpos)) / 4.0F, 1.0F);
            float f1 = 6.0F / f;
            float f2 = this.rotationPitch;
            float f3 = 1.5F;
            this.rotationPitch = -f1 * 1.5F * 5.0F;
            vec3d = this.getLook(p_184665_1_);
            this.rotationPitch = f2;
        }

        return vec3d;
    }

    public void onCrystalDestroyed(EntityEnderCrystal crystal, BlockPos pos, DamageSource dmgSrc)
    {
        EntityPlayer entityplayer;

        if (dmgSrc.getTrueSource() instanceof EntityPlayer)
        {
            entityplayer = (EntityPlayer)dmgSrc.getTrueSource();
        }
        else
        {
            entityplayer = this.world.func_184139_a(pos, 64.0D, 64.0D);
        }

        if (crystal == this.closestEnderCrystal)
        {
            this.func_70965_a(this.dragonPartHead, DamageSource.causeExplosionDamage(entityplayer), 10.0F);
        }

        this.phaseManager.getCurrentPhase().onCrystalDestroyed(crystal, pos, dmgSrc, entityplayer);
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (PHASE.equals(key) && this.world.isRemote)
        {
            this.phaseManager.setPhase(PhaseList.getById(((Integer)this.getDataManager().get(PHASE)).intValue()));
        }

        super.notifyDataManagerChange(key);
    }

    public PhaseManager getPhaseManager()
    {
        return this.phaseManager;
    }

    @Nullable
    public DragonFightManager getFightManager()
    {
        return this.fightManager;
    }

    public void func_70690_d(PotionEffect p_70690_1_)
    {
    }

    protected boolean canBeRidden(Entity entityIn)
    {
        return false;
    }

    /**
     * Returns false if this Entity is a boss, true otherwise.
     */
    public boolean isNonBoss()
    {
        return false;
    }
}

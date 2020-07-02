package net.minecraft.entity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWall;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Entity implements ICommandSender
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<ItemStack> EMPTY_EQUIPMENT = Collections.<ItemStack>emptyList();
    private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    private static double renderDistanceWeight = 1.0D;
    private static int field_70152_a;
    private int entityId;
    public boolean preventEntitySpawning;
    private final List<Entity> passengers;
    protected int rideCooldown;
    private Entity ridingEntity;
    public boolean forceSpawn;
    public World world;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;
    public double posX;
    public double posY;
    public double posZ;
    public double field_70159_w;
    public double field_70181_x;
    public double field_70179_y;
    public float rotationYaw;
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    private AxisAlignedBB boundingBox;
    public boolean onGround;
    public boolean collidedHorizontally;
    public boolean collidedVertically;
    public boolean collided;
    public boolean velocityChanged;
    protected boolean field_70134_J;
    private boolean field_174835_g;
    public boolean removed;
    public float field_70130_N;
    public float field_70131_O;
    public float prevDistanceWalkedModified;
    public float distanceWalkedModified;
    public float distanceWalkedOnStepModified;
    public float fallDistance;
    private int nextStepDistance;
    private float nextFlap;
    public double lastTickPosX;
    public double lastTickPosY;
    public double lastTickPosZ;
    public float stepHeight;
    public boolean noClip;
    public float entityCollisionReduction;
    protected Random rand;
    public int ticksExisted;
    private int fire;
    protected boolean inWater;
    public int hurtResistantTime;
    protected boolean firstUpdate;
    protected boolean field_70178_ae;
    protected EntityDataManager dataManager;
    protected static final DataParameter<Byte> FLAGS = EntityDataManager.<Byte>createKey(Entity.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> AIR = EntityDataManager.<Integer>createKey(Entity.class, DataSerializers.VARINT);
    private static final DataParameter<String> CUSTOM_NAME = EntityDataManager.<String>createKey(Entity.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> CUSTOM_NAME_VISIBLE = EntityDataManager.<Boolean>createKey(Entity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SILENT = EntityDataManager.<Boolean>createKey(Entity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> NO_GRAVITY = EntityDataManager.<Boolean>createKey(Entity.class, DataSerializers.BOOLEAN);
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;
    public long serverPosX;
    public long serverPosY;
    public long serverPosZ;
    public boolean ignoreFrustumCheck;
    public boolean isAirBorne;
    public int timeUntilPortal;
    protected boolean inPortal;
    protected int portalCounter;
    public int dimension;

    /** The position of the last portal the entity was in */
    protected BlockPos lastPortalPos;

    /**
     * A horizontal vector related to the position of the last portal the entity was in
     */
    protected Vec3d lastPortalVec;

    /**
     * A direction related to the position of the last portal the entity was in
     */
    protected EnumFacing teleportDirection;
    private boolean invulnerable;
    protected UUID entityUniqueID;
    protected String cachedUniqueIdString;
    private final CommandResultStats field_174837_as;
    protected boolean glowing;
    private final Set<String> tags;
    private boolean isPositionDirty;
    private final double[] pistonDeltas;
    private long pistonDeltasGameTime;

    public Entity(World p_i1582_1_)
    {
        this.entityId = field_70152_a++;
        this.passengers = Lists.<Entity>newArrayList();
        this.boundingBox = ZERO_AABB;
        this.field_70130_N = 0.6F;
        this.field_70131_O = 1.8F;
        this.nextStepDistance = 1;
        this.nextFlap = 1.0F;
        this.rand = new Random();
        this.fire = -this.getFireImmuneTicks();
        this.firstUpdate = true;
        this.entityUniqueID = MathHelper.getRandomUUID(this.rand);
        this.cachedUniqueIdString = this.entityUniqueID.toString();
        this.field_174837_as = new CommandResultStats();
        this.tags = Sets.<String>newHashSet();
        this.pistonDeltas = new double[] {0.0D, 0.0D, 0.0D};
        this.world = p_i1582_1_;
        this.setPosition(0.0D, 0.0D, 0.0D);

        if (p_i1582_1_ != null)
        {
            this.dimension = p_i1582_1_.dimension.getType().getId();
        }

        this.dataManager = new EntityDataManager(this);
        this.dataManager.register(FLAGS, Byte.valueOf((byte)0));
        this.dataManager.register(AIR, Integer.valueOf(300));
        this.dataManager.register(CUSTOM_NAME_VISIBLE, Boolean.valueOf(false));
        this.dataManager.register(CUSTOM_NAME, "");
        this.dataManager.register(SILENT, Boolean.valueOf(false));
        this.dataManager.register(NO_GRAVITY, Boolean.valueOf(false));
        this.registerData();
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public void setEntityId(int id)
    {
        this.entityId = id;
    }

    public Set<String> getTags()
    {
        return this.tags;
    }

    public boolean addTag(String tag)
    {
        if (this.tags.size() >= 1024)
        {
            return false;
        }
        else
        {
            this.tags.add(tag);
            return true;
        }
    }

    public boolean removeTag(String tag)
    {
        return this.tags.remove(tag);
    }

    /**
     * Called by the /kill command.
     */
    public void onKillCommand()
    {
        this.remove();
    }

    protected abstract void registerData();

    public EntityDataManager getDataManager()
    {
        return this.dataManager;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (p_equals_1_ instanceof Entity)
        {
            return ((Entity)p_equals_1_).entityId == this.entityId;
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return this.entityId;
    }

    /**
     * Keeps moving the entity up so it isn't colliding with blocks and other requirements for this entity to be spawned
     * (only actually used on players though its also on Entity)
     */
    protected void preparePlayerToSpawn()
    {
        if (this.world != null)
        {
            while (this.posY > 0.0D && this.posY < 256.0D)
            {
                this.setPosition(this.posX, this.posY, this.posZ);

                if (this.world.func_184144_a(this, this.getBoundingBox()).isEmpty())
                {
                    break;
                }

                ++this.posY;
            }

            this.field_70159_w = 0.0D;
            this.field_70181_x = 0.0D;
            this.field_70179_y = 0.0D;
            this.rotationPitch = 0.0F;
        }
    }

    /**
     * Queues the entity for removal from the world on the next tick.
     */
    public void remove()
    {
        this.removed = true;
    }

    public void func_184174_b(boolean p_184174_1_)
    {
    }

    protected void func_70105_a(float p_70105_1_, float p_70105_2_)
    {
        if (p_70105_1_ != this.field_70130_N || p_70105_2_ != this.field_70131_O)
        {
            float f = this.field_70130_N;
            this.field_70130_N = p_70105_1_;
            this.field_70131_O = p_70105_2_;

            if (this.field_70130_N < f)
            {
                double d0 = (double)p_70105_1_ / 2.0D;
                this.setBoundingBox(new AxisAlignedBB(this.posX - d0, this.posY, this.posZ - d0, this.posX + d0, this.posY + (double)this.field_70131_O, this.posZ + d0));
                return;
            }

            AxisAlignedBB axisalignedbb = this.getBoundingBox();
            this.setBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)this.field_70130_N, axisalignedbb.minY + (double)this.field_70131_O, axisalignedbb.minZ + (double)this.field_70130_N));

            if (this.field_70130_N > f && !this.firstUpdate && !this.world.isRemote)
            {
                this.func_70091_d(MoverType.SELF, (double)(f - this.field_70130_N), 0.0D, (double)(f - this.field_70130_N));
            }
        }
    }

    /**
     * Sets the rotation of the entity.
     */
    protected void setRotation(float yaw, float pitch)
    {
        this.rotationYaw = yaw % 360.0F;
        this.rotationPitch = pitch % 360.0F;
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    public void setPosition(double x, double y, double z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float f = this.field_70130_N / 2.0F;
        float f1 = this.field_70131_O;
        this.setBoundingBox(new AxisAlignedBB(x - (double)f, y, z - (double)f, x + (double)f, y + (double)f1, z + (double)f));
    }

    public void func_70082_c(float p_70082_1_, float p_70082_2_)
    {
        float f = this.rotationPitch;
        float f1 = this.rotationYaw;
        this.rotationYaw = (float)((double)this.rotationYaw + (double)p_70082_1_ * 0.15D);
        this.rotationPitch = (float)((double)this.rotationPitch - (double)p_70082_2_ * 0.15D);
        this.rotationPitch = MathHelper.clamp(this.rotationPitch, -90.0F, 90.0F);
        this.prevRotationPitch += this.rotationPitch - f;
        this.prevRotationYaw += this.rotationYaw - f1;

        if (this.ridingEntity != null)
        {
            this.ridingEntity.applyOrientationToEntity(this);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        if (!this.world.isRemote)
        {
            this.setFlag(6, this.func_184202_aL());
        }

        this.baseTick();
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void baseTick()
    {
        this.world.profiler.startSection("entityBaseTick");

        if (this.isPassenger() && this.getRidingEntity().removed)
        {
            this.stopRiding();
        }

        if (this.rideCooldown > 0)
        {
            --this.rideCooldown;
        }

        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;

        if (!this.world.isRemote && this.world instanceof WorldServer)
        {
            this.world.profiler.startSection("portal");

            if (this.inPortal)
            {
                MinecraftServer minecraftserver = this.world.getServer();

                if (minecraftserver.getAllowNether())
                {
                    if (!this.isPassenger())
                    {
                        int i = this.getMaxInPortalTime();

                        if (this.portalCounter++ >= i)
                        {
                            this.portalCounter = i;
                            this.timeUntilPortal = this.getPortalCooldown();
                            int j;

                            if (this.world.dimension.getType().getId() == -1)
                            {
                                j = 0;
                            }
                            else
                            {
                                j = -1;
                            }

                            this.func_184204_a(j);
                        }
                    }

                    this.inPortal = false;
                }
            }
            else
            {
                if (this.portalCounter > 0)
                {
                    this.portalCounter -= 4;
                }

                if (this.portalCounter < 0)
                {
                    this.portalCounter = 0;
                }
            }

            this.decrementTimeUntilPortal();
            this.world.profiler.endSection();
        }

        this.spawnRunningParticles();
        this.handleWaterMovement();

        if (this.world.isRemote)
        {
            this.extinguish();
        }
        else if (this.fire > 0)
        {
            if (this.field_70178_ae)
            {
                this.fire -= 4;

                if (this.fire < 0)
                {
                    this.extinguish();
                }
            }
            else
            {
                if (this.fire % 20 == 0)
                {
                    this.attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
                }

                --this.fire;
            }
        }

        if (this.isInLava())
        {
            this.setOnFireFromLava();
            this.fallDistance *= 0.5F;
        }

        if (this.posY < -64.0D)
        {
            this.outOfWorld();
        }

        if (!this.world.isRemote)
        {
            this.setFlag(0, this.fire > 0);
        }

        this.firstUpdate = false;
        this.world.profiler.endSection();
    }

    /**
     * Decrements the counter for the remaining time until the entity may use a portal again.
     */
    protected void decrementTimeUntilPortal()
    {
        if (this.timeUntilPortal > 0)
        {
            --this.timeUntilPortal;
        }
    }

    /**
     * Return the amount of time this entity should stay in a portal before being transported.
     */
    public int getMaxInPortalTime()
    {
        return 1;
    }

    /**
     * Called whenever the entity is walking inside of lava.
     */
    protected void setOnFireFromLava()
    {
        if (!this.field_70178_ae)
        {
            this.attackEntityFrom(DamageSource.LAVA, 4.0F);
            this.setFire(15);
        }
    }

    /**
     * Sets entity to burn for x amount of seconds, cannot lower amount of existing fire.
     */
    public void setFire(int seconds)
    {
        int i = seconds * 20;

        if (this instanceof EntityLivingBase)
        {
            i = EnchantmentProtection.getFireTimeForEntity((EntityLivingBase)this, i);
        }

        if (this.fire < i)
        {
            this.fire = i;
        }
    }

    /**
     * Removes fire from entity.
     */
    public void extinguish()
    {
        this.fire = 0;
    }

    /**
     * sets the dead flag. Used when you fall off the bottom of the world.
     */
    protected void outOfWorld()
    {
        this.remove();
    }

    /**
     * Checks if the offset position from the entity's current position is inside of a liquid.
     */
    public boolean isOffsetPositionInLiquid(double x, double y, double z)
    {
        AxisAlignedBB axisalignedbb = this.getBoundingBox().offset(x, y, z);
        return this.isLiquidPresentInAABB(axisalignedbb);
    }

    /**
     * Determines if a liquid is present within the specified AxisAlignedBB.
     */
    private boolean isLiquidPresentInAABB(AxisAlignedBB bb)
    {
        return this.world.func_184144_a(this, bb).isEmpty() && !this.world.containsAnyLiquid(bb);
    }

    public void func_70091_d(MoverType p_70091_1_, double p_70091_2_, double p_70091_4_, double p_70091_6_)
    {
        if (this.noClip)
        {
            this.setBoundingBox(this.getBoundingBox().offset(p_70091_2_, p_70091_4_, p_70091_6_));
            this.resetPositionToBB();
        }
        else
        {
            if (p_70091_1_ == MoverType.PISTON)
            {
                long i = this.world.getGameTime();

                if (i != this.pistonDeltasGameTime)
                {
                    Arrays.fill(this.pistonDeltas, 0.0D);
                    this.pistonDeltasGameTime = i;
                }

                if (p_70091_2_ != 0.0D)
                {
                    int j = EnumFacing.Axis.X.ordinal();
                    double d0 = MathHelper.clamp(p_70091_2_ + this.pistonDeltas[j], -0.51D, 0.51D);
                    p_70091_2_ = d0 - this.pistonDeltas[j];
                    this.pistonDeltas[j] = d0;

                    if (Math.abs(p_70091_2_) <= 9.999999747378752E-6D)
                    {
                        return;
                    }
                }
                else if (p_70091_4_ != 0.0D)
                {
                    int l4 = EnumFacing.Axis.Y.ordinal();
                    double d12 = MathHelper.clamp(p_70091_4_ + this.pistonDeltas[l4], -0.51D, 0.51D);
                    p_70091_4_ = d12 - this.pistonDeltas[l4];
                    this.pistonDeltas[l4] = d12;

                    if (Math.abs(p_70091_4_) <= 9.999999747378752E-6D)
                    {
                        return;
                    }
                }
                else
                {
                    if (p_70091_6_ == 0.0D)
                    {
                        return;
                    }

                    int i5 = EnumFacing.Axis.Z.ordinal();
                    double d13 = MathHelper.clamp(p_70091_6_ + this.pistonDeltas[i5], -0.51D, 0.51D);
                    p_70091_6_ = d13 - this.pistonDeltas[i5];
                    this.pistonDeltas[i5] = d13;

                    if (Math.abs(p_70091_6_) <= 9.999999747378752E-6D)
                    {
                        return;
                    }
                }
            }

            this.world.profiler.startSection("move");
            double d10 = this.posX;
            double d11 = this.posY;
            double d1 = this.posZ;

            if (this.field_70134_J)
            {
                this.field_70134_J = false;
                p_70091_2_ *= 0.25D;
                p_70091_4_ *= 0.05000000074505806D;
                p_70091_6_ *= 0.25D;
                this.field_70159_w = 0.0D;
                this.field_70181_x = 0.0D;
                this.field_70179_y = 0.0D;
            }

            double d2 = p_70091_2_;
            double d3 = p_70091_4_;
            double d4 = p_70091_6_;

            if ((p_70091_1_ == MoverType.SELF || p_70091_1_ == MoverType.PLAYER) && this.onGround && this.func_70093_af() && this instanceof EntityPlayer)
            {
                for (double d5 = 0.05D; p_70091_2_ != 0.0D && this.world.func_184144_a(this, this.getBoundingBox().offset(p_70091_2_, (double)(-this.stepHeight), 0.0D)).isEmpty(); d2 = p_70091_2_)
                {
                    if (p_70091_2_ < 0.05D && p_70091_2_ >= -0.05D)
                    {
                        p_70091_2_ = 0.0D;
                    }
                    else if (p_70091_2_ > 0.0D)
                    {
                        p_70091_2_ -= 0.05D;
                    }
                    else
                    {
                        p_70091_2_ += 0.05D;
                    }
                }

                for (; p_70091_6_ != 0.0D && this.world.func_184144_a(this, this.getBoundingBox().offset(0.0D, (double)(-this.stepHeight), p_70091_6_)).isEmpty(); d4 = p_70091_6_)
                {
                    if (p_70091_6_ < 0.05D && p_70091_6_ >= -0.05D)
                    {
                        p_70091_6_ = 0.0D;
                    }
                    else if (p_70091_6_ > 0.0D)
                    {
                        p_70091_6_ -= 0.05D;
                    }
                    else
                    {
                        p_70091_6_ += 0.05D;
                    }
                }

                for (; p_70091_2_ != 0.0D && p_70091_6_ != 0.0D && this.world.func_184144_a(this, this.getBoundingBox().offset(p_70091_2_, (double)(-this.stepHeight), p_70091_6_)).isEmpty(); d4 = p_70091_6_)
                {
                    if (p_70091_2_ < 0.05D && p_70091_2_ >= -0.05D)
                    {
                        p_70091_2_ = 0.0D;
                    }
                    else if (p_70091_2_ > 0.0D)
                    {
                        p_70091_2_ -= 0.05D;
                    }
                    else
                    {
                        p_70091_2_ += 0.05D;
                    }

                    d2 = p_70091_2_;

                    if (p_70091_6_ < 0.05D && p_70091_6_ >= -0.05D)
                    {
                        p_70091_6_ = 0.0D;
                    }
                    else if (p_70091_6_ > 0.0D)
                    {
                        p_70091_6_ -= 0.05D;
                    }
                    else
                    {
                        p_70091_6_ += 0.05D;
                    }
                }
            }

            List<AxisAlignedBB> list1 = this.world.func_184144_a(this, this.getBoundingBox().expand(p_70091_2_, p_70091_4_, p_70091_6_));
            AxisAlignedBB axisalignedbb = this.getBoundingBox();

            if (p_70091_4_ != 0.0D)
            {
                int k = 0;

                for (int l = list1.size(); k < l; ++k)
                {
                    p_70091_4_ = ((AxisAlignedBB)list1.get(k)).func_72323_b(this.getBoundingBox(), p_70091_4_);
                }

                this.setBoundingBox(this.getBoundingBox().offset(0.0D, p_70091_4_, 0.0D));
            }

            if (p_70091_2_ != 0.0D)
            {
                int j5 = 0;

                for (int l5 = list1.size(); j5 < l5; ++j5)
                {
                    p_70091_2_ = ((AxisAlignedBB)list1.get(j5)).func_72316_a(this.getBoundingBox(), p_70091_2_);
                }

                if (p_70091_2_ != 0.0D)
                {
                    this.setBoundingBox(this.getBoundingBox().offset(p_70091_2_, 0.0D, 0.0D));
                }
            }

            if (p_70091_6_ != 0.0D)
            {
                int k5 = 0;

                for (int i6 = list1.size(); k5 < i6; ++k5)
                {
                    p_70091_6_ = ((AxisAlignedBB)list1.get(k5)).func_72322_c(this.getBoundingBox(), p_70091_6_);
                }

                if (p_70091_6_ != 0.0D)
                {
                    this.setBoundingBox(this.getBoundingBox().offset(0.0D, 0.0D, p_70091_6_));
                }
            }

            boolean flag = this.onGround || d3 != p_70091_4_ && d3 < 0.0D;

            if (this.stepHeight > 0.0F && flag && (d2 != p_70091_2_ || d4 != p_70091_6_))
            {
                double d14 = p_70091_2_;
                double d6 = p_70091_4_;
                double d7 = p_70091_6_;
                AxisAlignedBB axisalignedbb1 = this.getBoundingBox();
                this.setBoundingBox(axisalignedbb);
                p_70091_4_ = (double)this.stepHeight;
                List<AxisAlignedBB> list = this.world.func_184144_a(this, this.getBoundingBox().expand(d2, p_70091_4_, d4));
                AxisAlignedBB axisalignedbb2 = this.getBoundingBox();
                AxisAlignedBB axisalignedbb3 = axisalignedbb2.expand(d2, 0.0D, d4);
                double d8 = p_70091_4_;
                int j1 = 0;

                for (int k1 = list.size(); j1 < k1; ++j1)
                {
                    d8 = ((AxisAlignedBB)list.get(j1)).func_72323_b(axisalignedbb3, d8);
                }

                axisalignedbb2 = axisalignedbb2.offset(0.0D, d8, 0.0D);
                double d18 = d2;
                int l1 = 0;

                for (int i2 = list.size(); l1 < i2; ++l1)
                {
                    d18 = ((AxisAlignedBB)list.get(l1)).func_72316_a(axisalignedbb2, d18);
                }

                axisalignedbb2 = axisalignedbb2.offset(d18, 0.0D, 0.0D);
                double d19 = d4;
                int j2 = 0;

                for (int k2 = list.size(); j2 < k2; ++j2)
                {
                    d19 = ((AxisAlignedBB)list.get(j2)).func_72322_c(axisalignedbb2, d19);
                }

                axisalignedbb2 = axisalignedbb2.offset(0.0D, 0.0D, d19);
                AxisAlignedBB axisalignedbb4 = this.getBoundingBox();
                double d20 = p_70091_4_;
                int l2 = 0;

                for (int i3 = list.size(); l2 < i3; ++l2)
                {
                    d20 = ((AxisAlignedBB)list.get(l2)).func_72323_b(axisalignedbb4, d20);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, d20, 0.0D);
                double d21 = d2;
                int j3 = 0;

                for (int k3 = list.size(); j3 < k3; ++j3)
                {
                    d21 = ((AxisAlignedBB)list.get(j3)).func_72316_a(axisalignedbb4, d21);
                }

                axisalignedbb4 = axisalignedbb4.offset(d21, 0.0D, 0.0D);
                double d22 = d4;
                int l3 = 0;

                for (int i4 = list.size(); l3 < i4; ++l3)
                {
                    d22 = ((AxisAlignedBB)list.get(l3)).func_72322_c(axisalignedbb4, d22);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d22);
                double d23 = d18 * d18 + d19 * d19;
                double d9 = d21 * d21 + d22 * d22;

                if (d23 > d9)
                {
                    p_70091_2_ = d18;
                    p_70091_6_ = d19;
                    p_70091_4_ = -d8;
                    this.setBoundingBox(axisalignedbb2);
                }
                else
                {
                    p_70091_2_ = d21;
                    p_70091_6_ = d22;
                    p_70091_4_ = -d20;
                    this.setBoundingBox(axisalignedbb4);
                }

                int j4 = 0;

                for (int k4 = list.size(); j4 < k4; ++j4)
                {
                    p_70091_4_ = ((AxisAlignedBB)list.get(j4)).func_72323_b(this.getBoundingBox(), p_70091_4_);
                }

                this.setBoundingBox(this.getBoundingBox().offset(0.0D, p_70091_4_, 0.0D));

                if (d14 * d14 + d7 * d7 >= p_70091_2_ * p_70091_2_ + p_70091_6_ * p_70091_6_)
                {
                    p_70091_2_ = d14;
                    p_70091_4_ = d6;
                    p_70091_6_ = d7;
                    this.setBoundingBox(axisalignedbb1);
                }
            }

            this.world.profiler.endSection();
            this.world.profiler.startSection("rest");
            this.resetPositionToBB();
            this.collidedHorizontally = d2 != p_70091_2_ || d4 != p_70091_6_;
            this.collidedVertically = d3 != p_70091_4_;
            this.onGround = this.collidedVertically && d3 < 0.0D;
            this.collided = this.collidedHorizontally || this.collidedVertically;
            int j6 = MathHelper.floor(this.posX);
            int i1 = MathHelper.floor(this.posY - 0.20000000298023224D);
            int k6 = MathHelper.floor(this.posZ);
            BlockPos blockpos = new BlockPos(j6, i1, k6);
            IBlockState iblockstate = this.world.getBlockState(blockpos);

            if (iblockstate.getMaterial() == Material.AIR)
            {
                BlockPos blockpos1 = blockpos.down();
                IBlockState iblockstate1 = this.world.getBlockState(blockpos1);
                Block block1 = iblockstate1.getBlock();

                if (block1 instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate)
                {
                    iblockstate = iblockstate1;
                    blockpos = blockpos1;
                }
            }

            this.updateFallState(p_70091_4_, this.onGround, iblockstate, blockpos);

            if (d2 != p_70091_2_)
            {
                this.field_70159_w = 0.0D;
            }

            if (d4 != p_70091_6_)
            {
                this.field_70179_y = 0.0D;
            }

            Block block = iblockstate.getBlock();

            if (d3 != p_70091_4_)
            {
                block.onLanded(this.world, this);
            }

            if (this.func_70041_e_() && (!this.onGround || !this.func_70093_af() || !(this instanceof EntityPlayer)) && !this.isPassenger())
            {
                double d15 = this.posX - d10;
                double d16 = this.posY - d11;
                double d17 = this.posZ - d1;

                if (block != Blocks.LADDER)
                {
                    d16 = 0.0D;
                }

                if (block != null && this.onGround)
                {
                    block.onEntityWalk(this.world, blockpos, this);
                }

                this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt(d15 * d15 + d17 * d17) * 0.6D);
                this.distanceWalkedOnStepModified = (float)((double)this.distanceWalkedOnStepModified + (double)MathHelper.sqrt(d15 * d15 + d16 * d16 + d17 * d17) * 0.6D);

                if (this.distanceWalkedOnStepModified > (float)this.nextStepDistance && iblockstate.getMaterial() != Material.AIR)
                {
                    this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;

                    if (this.isInWater())
                    {
                        Entity entity = this.isBeingRidden() && this.getControllingPassenger() != null ? this.getControllingPassenger() : this;
                        float f = entity == this ? 0.35F : 0.4F;
                        float f1 = MathHelper.sqrt(entity.field_70159_w * entity.field_70159_w * 0.20000000298023224D + entity.field_70181_x * entity.field_70181_x + entity.field_70179_y * entity.field_70179_y * 0.20000000298023224D) * f;

                        if (f1 > 1.0F)
                        {
                            f1 = 1.0F;
                        }

                        this.playSound(this.getSwimSound(), f1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                    }
                    else
                    {
                        this.playStepSound(blockpos, block);
                    }
                }
                else if (this.distanceWalkedOnStepModified > this.nextFlap && this.makeFlySound() && iblockstate.getMaterial() == Material.AIR)
                {
                    this.nextFlap = this.playFlySound(this.distanceWalkedOnStepModified);
                }
            }

            try
            {
                this.doBlockCollisions();
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.fillCrashReport(crashreportcategory);
                throw new ReportedException(crashreport);
            }

            boolean flag1 = this.isWet();

            if (this.world.isFlammableWithin(this.getBoundingBox().shrink(0.001D)))
            {
                this.dealFireDamage(1);

                if (!flag1)
                {
                    ++this.fire;

                    if (this.fire == 0)
                    {
                        this.setFire(8);
                    }
                }
            }
            else if (this.fire <= 0)
            {
                this.fire = -this.getFireImmuneTicks();
            }

            if (flag1 && this.isBurning())
            {
                this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                this.fire = -this.getFireImmuneTicks();
            }

            this.world.profiler.endSection();
        }
    }

    /**
     * Resets the entity's position to the center (planar) and bottom (vertical) points of its bounding box.
     */
    public void resetPositionToBB()
    {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        this.posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0D;
        this.posY = axisalignedbb.minY;
        this.posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D;
    }

    protected SoundEvent getSwimSound()
    {
        return SoundEvents.ENTITY_GENERIC_SWIM;
    }

    protected SoundEvent getSplashSound()
    {
        return SoundEvents.ENTITY_GENERIC_SPLASH;
    }

    protected void doBlockCollisions()
    {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos1 = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos2 = BlockPos.PooledMutableBlockPos.retain();

        if (this.world.isAreaLoaded(blockpos$pooledmutableblockpos, blockpos$pooledmutableblockpos1))
        {
            for (int i = blockpos$pooledmutableblockpos.getX(); i <= blockpos$pooledmutableblockpos1.getX(); ++i)
            {
                for (int j = blockpos$pooledmutableblockpos.getY(); j <= blockpos$pooledmutableblockpos1.getY(); ++j)
                {
                    for (int k = blockpos$pooledmutableblockpos.getZ(); k <= blockpos$pooledmutableblockpos1.getZ(); ++k)
                    {
                        blockpos$pooledmutableblockpos2.setPos(i, j, k);
                        IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos2);

                        try
                        {
                            iblockstate.getBlock().func_180634_a(this.world, blockpos$pooledmutableblockpos2, iblockstate, this);
                            this.onInsideBlock(iblockstate);
                        }
                        catch (Throwable throwable)
                        {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
                            CrashReportCategory.addBlockInfo(crashreportcategory, blockpos$pooledmutableblockpos2, iblockstate);
                            throw new ReportedException(crashreport);
                        }
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.func_185344_t();
        blockpos$pooledmutableblockpos1.func_185344_t();
        blockpos$pooledmutableblockpos2.func_185344_t();
    }

    protected void onInsideBlock(IBlockState p_191955_1_)
    {
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        SoundType soundtype = blockIn.func_185467_w();

        if (this.world.getBlockState(pos.up()).getBlock() == Blocks.field_150431_aC)
        {
            soundtype = Blocks.field_150431_aC.func_185467_w();
            this.playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.15F, soundtype.getPitch());
        }
        else if (!blockIn.getDefaultState().getMaterial().isLiquid())
        {
            this.playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.15F, soundtype.getPitch());
        }
    }

    protected float playFlySound(float volume)
    {
        return 0.0F;
    }

    protected boolean makeFlySound()
    {
        return false;
    }

    public void playSound(SoundEvent soundIn, float volume, float pitch)
    {
        if (!this.isSilent())
        {
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch);
        }
    }

    /**
     * @return True if this entity will not play sounds
     */
    public boolean isSilent()
    {
        return ((Boolean)this.dataManager.get(SILENT)).booleanValue();
    }

    /**
     * When set to true the entity will not play sounds.
     */
    public void setSilent(boolean isSilent)
    {
        this.dataManager.set(SILENT, Boolean.valueOf(isSilent));
    }

    public boolean hasNoGravity()
    {
        return ((Boolean)this.dataManager.get(NO_GRAVITY)).booleanValue();
    }

    public void setNoGravity(boolean noGravity)
    {
        this.dataManager.set(NO_GRAVITY, Boolean.valueOf(noGravity));
    }

    protected boolean func_70041_e_()
    {
        return true;
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
        if (onGroundIn)
        {
            if (this.fallDistance > 0.0F)
            {
                state.getBlock().onFallenUpon(this.world, pos, this, this.fallDistance);
            }

            this.fallDistance = 0.0F;
        }
        else if (y < 0.0D)
        {
            this.fallDistance = (float)((double)this.fallDistance - y);
        }
    }

    @Nullable

    /**
     * Returns the <b>solid</b> collision bounding box for this entity. Used to make (e.g.) boats solid. Return null if
     * this entity is not solid.
     *  
     * For general purposes, use {@link #width} and {@link #height}.
     *  
     * @see getEntityBoundingBox
     */
    public AxisAlignedBB getCollisionBoundingBox()
    {
        return null;
    }

    /**
     * Will deal the specified amount of fire damage to the entity if the entity isn't immune to fire damage.
     */
    protected void dealFireDamage(int amount)
    {
        if (!this.field_70178_ae)
        {
            this.attackEntityFrom(DamageSource.IN_FIRE, (float)amount);
        }
    }

    public final boolean isImmuneToFire()
    {
        return this.field_70178_ae;
    }

    public void func_180430_e(float p_180430_1_, float p_180430_2_)
    {
        if (this.isBeingRidden())
        {
            for (Entity entity : this.getPassengers())
            {
                entity.func_180430_e(p_180430_1_, p_180430_2_);
            }
        }
    }

    /**
     * Checks if this entity is either in water or on an open air block in rain (used in wolves).
     */
    public boolean isWet()
    {
        if (this.inWater)
        {
            return true;
        }
        else
        {
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(this.posX, this.posY, this.posZ);

            if (!this.world.isRainingAt(blockpos$pooledmutableblockpos) && !this.world.isRainingAt(blockpos$pooledmutableblockpos.setPos(this.posX, this.posY + (double)this.field_70131_O, this.posZ)))
            {
                blockpos$pooledmutableblockpos.func_185344_t();
                return false;
            }
            else
            {
                blockpos$pooledmutableblockpos.func_185344_t();
                return true;
            }
        }
    }

    /**
     * Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning
     * true)
     */
    public boolean isInWater()
    {
        return this.inWater;
    }

    public boolean func_191953_am()
    {
        return this.world.func_72918_a(this.getBoundingBox().grow(0.0D, -20.0D, 0.0D).shrink(0.001D), Material.WATER, this);
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        if (this.getRidingEntity() instanceof EntityBoat)
        {
            this.inWater = false;
        }
        else if (this.world.func_72918_a(this.getBoundingBox().grow(0.0D, -0.4000000059604645D, 0.0D).shrink(0.001D), Material.WATER, this))
        {
            if (!this.inWater && !this.firstUpdate)
            {
                this.doWaterSplashEffect();
            }

            this.fallDistance = 0.0F;
            this.inWater = true;
            this.extinguish();
        }
        else
        {
            this.inWater = false;
        }

        return this.inWater;
    }

    /**
     * Plays the {@link #getSplashSound() splash sound}, and the {@link ParticleType#WATER_BUBBLE} and {@link
     * ParticleType#WATER_SPLASH} particles.
     */
    protected void doWaterSplashEffect()
    {
        Entity entity = this.isBeingRidden() && this.getControllingPassenger() != null ? this.getControllingPassenger() : this;
        float f = entity == this ? 0.2F : 0.9F;
        float f1 = MathHelper.sqrt(entity.field_70159_w * entity.field_70159_w * 0.20000000298023224D + entity.field_70181_x * entity.field_70181_x + entity.field_70179_y * entity.field_70179_y * 0.20000000298023224D) * f;

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        this.playSound(this.getSplashSound(), f1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
        float f2 = (float)MathHelper.floor(this.getBoundingBox().minY);

        for (int i = 0; (float)i < 1.0F + this.field_70130_N * 20.0F; ++i)
        {
            float f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.field_70130_N;
            float f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.field_70130_N;
            this.world.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.posX + (double)f3, (double)(f2 + 1.0F), this.posZ + (double)f4, this.field_70159_w, this.field_70181_x - (double)(this.rand.nextFloat() * 0.2F), this.field_70179_y);
        }

        for (int j = 0; (float)j < 1.0F + this.field_70130_N * 20.0F; ++j)
        {
            float f5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.field_70130_N;
            float f6 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.field_70130_N;
            this.world.func_175688_a(EnumParticleTypes.WATER_SPLASH, this.posX + (double)f5, (double)(f2 + 1.0F), this.posZ + (double)f6, this.field_70159_w, this.field_70181_x, this.field_70179_y);
        }
    }

    /**
     * Attempts to create sprinting particles if the entity is sprinting and not in water.
     */
    public void spawnRunningParticles()
    {
        if (this.isSprinting() && !this.isInWater())
        {
            this.createRunningParticles();
        }
    }

    protected void createRunningParticles()
    {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY - 0.20000000298023224D);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        IBlockState iblockstate = this.world.getBlockState(blockpos);

        if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE)
        {
            this.world.func_175688_a(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.field_70130_N, this.getBoundingBox().minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.field_70130_N, -this.field_70159_w * 4.0D, 1.5D, -this.field_70179_y * 4.0D, Block.func_176210_f(iblockstate));
        }
    }

    public boolean func_70055_a(Material p_70055_1_)
    {
        if (this.getRidingEntity() instanceof EntityBoat)
        {
            return false;
        }
        else
        {
            double d0 = this.posY + (double)this.getEyeHeight();
            BlockPos blockpos = new BlockPos(this.posX, d0, this.posZ);
            IBlockState iblockstate = this.world.getBlockState(blockpos);

            if (iblockstate.getMaterial() == p_70055_1_)
            {
                float f = BlockLiquid.func_149801_b(iblockstate.getBlock().func_176201_c(iblockstate)) - 0.11111111F;
                float f1 = (float)(blockpos.getY() + 1) - f;
                boolean flag = d0 < (double)f1;
                return !flag && this instanceof EntityPlayer ? false : flag;
            }
            else
            {
                return false;
            }
        }
    }

    public boolean isInLava()
    {
        return this.world.isMaterialInBB(this.getBoundingBox().grow(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.LAVA);
    }

    public void func_191958_b(float p_191958_1_, float p_191958_2_, float p_191958_3_, float p_191958_4_)
    {
        float f = p_191958_1_ * p_191958_1_ + p_191958_2_ * p_191958_2_ + p_191958_3_ * p_191958_3_;

        if (f >= 1.0E-4F)
        {
            f = MathHelper.sqrt(f);

            if (f < 1.0F)
            {
                f = 1.0F;
            }

            f = p_191958_4_ / f;
            p_191958_1_ = p_191958_1_ * f;
            p_191958_2_ = p_191958_2_ * f;
            p_191958_3_ = p_191958_3_ * f;
            float f1 = MathHelper.sin(this.rotationYaw * 0.017453292F);
            float f2 = MathHelper.cos(this.rotationYaw * 0.017453292F);
            this.field_70159_w += (double)(p_191958_1_ * f2 - p_191958_3_ * f1);
            this.field_70181_x += (double)p_191958_2_;
            this.field_70179_y += (double)(p_191958_3_ * f2 + p_191958_1_ * f1);
        }
    }

    public int func_70070_b()
    {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));

        if (this.world.isBlockLoaded(blockpos$mutableblockpos))
        {
            blockpos$mutableblockpos.setY(MathHelper.floor(this.posY + (double)this.getEyeHeight()));
            return this.world.func_175626_b(blockpos$mutableblockpos, 0);
        }
        else
        {
            return 0;
        }
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness()
    {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));

        if (this.world.isBlockLoaded(blockpos$mutableblockpos))
        {
            blockpos$mutableblockpos.setY(MathHelper.floor(this.posY + (double)this.getEyeHeight()));
            return this.world.func_175724_o(blockpos$mutableblockpos);
        }
        else
        {
            return 0.0F;
        }
    }

    /**
     * Sets the reference to the World object.
     */
    public void setWorld(World worldIn)
    {
        this.world = worldIn;
    }

    /**
     * Sets position and rotation, clamping and wrapping params to valid values. Used by network code.
     */
    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch)
    {
        this.posX = MathHelper.clamp(x, -3.0E7D, 3.0E7D);
        this.posY = y;
        this.posZ = MathHelper.clamp(z, -3.0E7D, 3.0E7D);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        double d0 = (double)(this.prevRotationYaw - yaw);

        if (d0 < -180.0D)
        {
            this.prevRotationYaw += 360.0F;
        }

        if (d0 >= 180.0D)
        {
            this.prevRotationYaw -= 360.0F;
        }

        this.setPosition(this.posX, this.posY, this.posZ);
        this.setRotation(yaw, pitch);
    }

    public void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn, float rotationPitchIn)
    {
        this.setLocationAndAngles((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, rotationYawIn, rotationPitchIn);
    }

    /**
     * Sets the location and Yaw/Pitch of an entity in the world
     */
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    /**
     * Returns the distance to the entity.
     */
    public float getDistance(Entity entityIn)
    {
        float f = (float)(this.posX - entityIn.posX);
        float f1 = (float)(this.posY - entityIn.posY);
        float f2 = (float)(this.posZ - entityIn.posZ);
        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    /**
     * Gets the squared distance to the position.
     */
    public double getDistanceSq(double x, double y, double z)
    {
        double d0 = this.posX - x;
        double d1 = this.posY - y;
        double d2 = this.posZ - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double func_174818_b(BlockPos p_174818_1_)
    {
        return p_174818_1_.func_177954_c(this.posX, this.posY, this.posZ);
    }

    public double func_174831_c(BlockPos p_174831_1_)
    {
        return p_174831_1_.func_177957_d(this.posX, this.posY, this.posZ);
    }

    public double func_70011_f(double p_70011_1_, double p_70011_3_, double p_70011_5_)
    {
        double d0 = this.posX - p_70011_1_;
        double d1 = this.posY - p_70011_3_;
        double d2 = this.posZ - p_70011_5_;
        return (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    /**
     * Returns the squared distance to the entity.
     */
    public double getDistanceSq(Entity entityIn)
    {
        double d0 = this.posX - entityIn.posX;
        double d1 = this.posY - entityIn.posY;
        double d2 = this.posZ - entityIn.posZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    public void applyEntityCollision(Entity entityIn)
    {
        if (!this.isRidingSameEntity(entityIn))
        {
            if (!entityIn.noClip && !this.noClip)
            {
                double d0 = entityIn.posX - this.posX;
                double d1 = entityIn.posZ - this.posZ;
                double d2 = MathHelper.absMax(d0, d1);

                if (d2 >= 0.009999999776482582D)
                {
                    d2 = (double)MathHelper.sqrt(d2);
                    d0 = d0 / d2;
                    d1 = d1 / d2;
                    double d3 = 1.0D / d2;

                    if (d3 > 1.0D)
                    {
                        d3 = 1.0D;
                    }

                    d0 = d0 * d3;
                    d1 = d1 * d3;
                    d0 = d0 * 0.05000000074505806D;
                    d1 = d1 * 0.05000000074505806D;
                    d0 = d0 * (double)(1.0F - this.entityCollisionReduction);
                    d1 = d1 * (double)(1.0F - this.entityCollisionReduction);

                    if (!this.isBeingRidden())
                    {
                        this.addVelocity(-d0, 0.0D, -d1);
                    }

                    if (!entityIn.isBeingRidden())
                    {
                        entityIn.addVelocity(d0, 0.0D, d1);
                    }
                }
            }
        }
    }

    /**
     * Adds to the current velocity of the entity, and sets {@link #isAirBorne} to true.
     */
    public void addVelocity(double x, double y, double z)
    {
        this.field_70159_w += x;
        this.field_70181_x += y;
        this.field_70179_y += z;
        this.isAirBorne = true;
    }

    /**
     * Marks this entity's velocity as changed, so that it can be re-synced with the client later
     */
    protected void markVelocityChanged()
    {
        this.velocityChanged = true;
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
            this.markVelocityChanged();
            return false;
        }
    }

    /**
     * interpolated look vector
     */
    public Vec3d getLook(float partialTicks)
    {
        if (partialTicks == 1.0F)
        {
            return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
        }
        else
        {
            float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
            float f1 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * partialTicks;
            return this.getVectorForRotation(f, f1);
        }
    }

    /**
     * Creates a Vec3 using the pitch and yaw of the entities rotation.
     */
    protected final Vec3d getVectorForRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d((double)(f1 * f2), (double)f3, (double)(f * f2));
    }

    public Vec3d getEyePosition(float partialTicks)
    {
        if (partialTicks == 1.0F)
        {
            return new Vec3d(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
        }
        else
        {
            double d0 = this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks;
            double d1 = this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks + (double)this.getEyeHeight();
            double d2 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks;
            return new Vec3d(d0, d1, d2);
        }
    }

    @Nullable
    public RayTraceResult func_174822_a(double p_174822_1_, float p_174822_3_)
    {
        Vec3d vec3d = this.getEyePosition(p_174822_3_);
        Vec3d vec3d1 = this.getLook(p_174822_3_);
        Vec3d vec3d2 = vec3d.add(vec3d1.x * p_174822_1_, vec3d1.y * p_174822_1_, vec3d1.z * p_174822_1_);
        return this.world.func_147447_a(vec3d, vec3d2, false, false, true);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return false;
    }

    public void awardKillScore(Entity p_191956_1_, int p_191956_2_, DamageSource p_191956_3_)
    {
        if (p_191956_1_ instanceof EntityPlayerMP)
        {
            CriteriaTriggers.ENTITY_KILLED_PLAYER.trigger((EntityPlayerMP)p_191956_1_, this, p_191956_3_);
        }
    }

    public boolean isInRangeToRender3d(double x, double y, double z)
    {
        double d0 = this.posX - x;
        double d1 = this.posY - y;
        double d2 = this.posZ - z;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        return this.isInRangeToRenderDist(d3);
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getBoundingBox().getAverageEdgeLength();

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * renderDistanceWeight;
        return distance < d0 * d0;
    }

    /**
     * Writes this entity to NBT, unless it has been removed. Also writes this entity's passengers, and the entity type
     * ID (so the produced NBT is sufficient to recreate the entity).
     *  
     * Generally, {@link #writeUnlessPassenger} or {@link #writeWithoutTypeId} should be used instead of this method.
     *  
     * @return True if the entity was written (and the passed compound should be saved); false if the entity was not
     * written.
     */
    public boolean writeUnlessRemoved(NBTTagCompound compound)
    {
        String s = this.getEntityString();

        if (!this.removed && s != null)
        {
            compound.putString("id", s);
            this.writeWithoutTypeId(compound);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Writes this entity to NBT, unless it has been removed or it is a passenger. Also writes this entity's passengers,
     * and the entity type ID (so the produced NBT is sufficient to recreate the entity).
     * To always write the entity, use {@link #writeWithoutTypeId}.
     *  
     * @return True if the entity was written (and the passed compound should be saved); false if the entity was not
     * written.
     */
    public boolean writeUnlessPassenger(NBTTagCompound compound)
    {
        String s = this.getEntityString();

        if (!this.removed && s != null && !this.isPassenger())
        {
            compound.putString("id", s);
            this.writeWithoutTypeId(compound);
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void func_190533_a(DataFixer p_190533_0_)
    {
        p_190533_0_.func_188258_a(FixTypes.ENTITY, new IDataWalker()
        {
            public NBTTagCompound func_188266_a(IDataFixer p_188266_1_, NBTTagCompound p_188266_2_, int p_188266_3_)
            {
                if (p_188266_2_.contains("Passengers", 9))
                {
                    NBTTagList nbttaglist = p_188266_2_.getList("Passengers", 10);

                    for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                    {
                        nbttaglist.func_150304_a(i, p_188266_1_.func_188251_a(FixTypes.ENTITY, nbttaglist.getCompound(i), p_188266_3_));
                    }
                }

                return p_188266_2_;
            }
        });
    }

    /**
     * Writes this entity, including passengers, to NBT, regardless as to whether or not it is removed or a passenger.
     * Does <b>not</b> include the entity's type ID, so the NBT is insufficient to recreate the entity using {@link
     * AnvilChunkLoader#readWorldEntity}. Use {@link #writeUnlessPassenger} for that purpose.
     */
    public NBTTagCompound writeWithoutTypeId(NBTTagCompound compound)
    {
        try
        {
            compound.func_74782_a("Pos", this.newDoubleNBTList(this.posX, this.posY, this.posZ));
            compound.func_74782_a("Motion", this.newDoubleNBTList(this.field_70159_w, this.field_70181_x, this.field_70179_y));
            compound.func_74782_a("Rotation", this.newFloatNBTList(this.rotationYaw, this.rotationPitch));
            compound.putFloat("FallDistance", this.fallDistance);
            compound.putShort("Fire", (short)this.fire);
            compound.putShort("Air", (short)this.getAir());
            compound.putBoolean("OnGround", this.onGround);
            compound.putInt("Dimension", this.dimension);
            compound.putBoolean("Invulnerable", this.invulnerable);
            compound.putInt("PortalCooldown", this.timeUntilPortal);
            compound.putUniqueId("UUID", this.getUniqueID());

            if (this.hasCustomName())
            {
                compound.putString("CustomName", this.func_95999_t());
            }

            if (this.isCustomNameVisible())
            {
                compound.putBoolean("CustomNameVisible", this.isCustomNameVisible());
            }

            this.field_174837_as.func_179670_b(compound);

            if (this.isSilent())
            {
                compound.putBoolean("Silent", this.isSilent());
            }

            if (this.hasNoGravity())
            {
                compound.putBoolean("NoGravity", this.hasNoGravity());
            }

            if (this.glowing)
            {
                compound.putBoolean("Glowing", this.glowing);
            }

            if (!this.tags.isEmpty())
            {
                NBTTagList nbttaglist = new NBTTagList();

                for (String s : this.tags)
                {
                    nbttaglist.func_74742_a(new NBTTagString(s));
                }

                compound.func_74782_a("Tags", nbttaglist);
            }

            this.func_70014_b(compound);

            if (this.isBeingRidden())
            {
                NBTTagList nbttaglist1 = new NBTTagList();

                for (Entity entity : this.getPassengers())
                {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();

                    if (entity.writeUnlessRemoved(nbttagcompound))
                    {
                        nbttaglist1.func_74742_a(nbttagcompound);
                    }
                }

                if (!nbttaglist1.func_82582_d())
                {
                    compound.func_74782_a("Passengers", nbttaglist1);
                }
            }

            return compound;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being saved");
            this.fillCrashReport(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Reads the entity from NBT (calls an abstract helper method to read specialized data)
     */
    public void read(NBTTagCompound compound)
    {
        try
        {
            NBTTagList nbttaglist = compound.getList("Pos", 6);
            NBTTagList nbttaglist2 = compound.getList("Motion", 6);
            NBTTagList nbttaglist3 = compound.getList("Rotation", 5);
            this.field_70159_w = nbttaglist2.getDouble(0);
            this.field_70181_x = nbttaglist2.getDouble(1);
            this.field_70179_y = nbttaglist2.getDouble(2);

            if (Math.abs(this.field_70159_w) > 10.0D)
            {
                this.field_70159_w = 0.0D;
            }

            if (Math.abs(this.field_70181_x) > 10.0D)
            {
                this.field_70181_x = 0.0D;
            }

            if (Math.abs(this.field_70179_y) > 10.0D)
            {
                this.field_70179_y = 0.0D;
            }

            this.posX = nbttaglist.getDouble(0);
            this.posY = nbttaglist.getDouble(1);
            this.posZ = nbttaglist.getDouble(2);
            this.lastTickPosX = this.posX;
            this.lastTickPosY = this.posY;
            this.lastTickPosZ = this.posZ;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.rotationYaw = nbttaglist3.getFloat(0);
            this.rotationPitch = nbttaglist3.getFloat(1);
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
            this.setRotationYawHead(this.rotationYaw);
            this.setRenderYawOffset(this.rotationYaw);
            this.fallDistance = compound.getFloat("FallDistance");
            this.fire = compound.getShort("Fire");
            this.setAir(compound.getShort("Air"));
            this.onGround = compound.getBoolean("OnGround");

            if (compound.contains("Dimension"))
            {
                this.dimension = compound.getInt("Dimension");
            }

            this.invulnerable = compound.getBoolean("Invulnerable");
            this.timeUntilPortal = compound.getInt("PortalCooldown");

            if (compound.hasUniqueId("UUID"))
            {
                this.entityUniqueID = compound.getUniqueId("UUID");
                this.cachedUniqueIdString = this.entityUniqueID.toString();
            }

            this.setPosition(this.posX, this.posY, this.posZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);

            if (compound.contains("CustomName", 8))
            {
                this.func_96094_a(compound.getString("CustomName"));
            }

            this.setCustomNameVisible(compound.getBoolean("CustomNameVisible"));
            this.field_174837_as.func_179668_a(compound);
            this.setSilent(compound.getBoolean("Silent"));
            this.setNoGravity(compound.getBoolean("NoGravity"));
            this.setGlowing(compound.getBoolean("Glowing"));

            if (compound.contains("Tags", 9))
            {
                this.tags.clear();
                NBTTagList nbttaglist1 = compound.getList("Tags", 8);
                int i = Math.min(nbttaglist1.func_74745_c(), 1024);

                for (int j = 0; j < i; ++j)
                {
                    this.tags.add(nbttaglist1.getString(j));
                }
            }

            this.readAdditional(compound);

            if (this.shouldSetPosAfterLoading())
            {
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being loaded");
            this.fillCrashReport(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    protected boolean shouldSetPosAfterLoading()
    {
        return true;
    }

    @Nullable

    /**
     * Returns the string that identifies this Entity's class
     */
    protected final String getEntityString()
    {
        ResourceLocation resourcelocation = EntityList.func_191301_a(this);
        return resourcelocation == null ? null : resourcelocation.toString();
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected abstract void readAdditional(NBTTagCompound compound);

    protected abstract void func_70014_b(NBTTagCompound p_70014_1_);

    /**
     * creates a NBT list from the array of doubles passed to this function
     */
    protected NBTTagList newDoubleNBTList(double... numbers)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (double d0 : numbers)
        {
            nbttaglist.func_74742_a(new NBTTagDouble(d0));
        }

        return nbttaglist;
    }

    /**
     * Returns a new NBTTagList filled with the specified floats
     */
    protected NBTTagList newFloatNBTList(float... numbers)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (float f : numbers)
        {
            nbttaglist.func_74742_a(new NBTTagFloat(f));
        }

        return nbttaglist;
    }

    @Nullable
    public EntityItem func_145779_a(Item p_145779_1_, int p_145779_2_)
    {
        return this.func_145778_a(p_145779_1_, p_145779_2_, 0.0F);
    }

    @Nullable
    public EntityItem func_145778_a(Item p_145778_1_, int p_145778_2_, float p_145778_3_)
    {
        return this.entityDropItem(new ItemStack(p_145778_1_, p_145778_2_, 0), p_145778_3_);
    }

    @Nullable

    /**
     * Drops an item at the position of the entity.
     */
    public EntityItem entityDropItem(ItemStack stack, float offsetY)
    {
        if (stack.isEmpty())
        {
            return null;
        }
        else
        {
            EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY + (double)offsetY, this.posZ, stack);
            entityitem.setDefaultPickupDelay();
            this.world.addEntity0(entityitem);
            return entityitem;
        }
    }

    /**
     * Returns true if the entity has not been {@link #removed}.
     */
    public boolean isAlive()
    {
        return !this.removed;
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        if (this.noClip)
        {
            return false;
        }
        else
        {
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

            for (int i = 0; i < 8; ++i)
            {
                int j = MathHelper.floor(this.posY + (double)(((float)((i >> 0) % 2) - 0.5F) * 0.1F) + (double)this.getEyeHeight());
                int k = MathHelper.floor(this.posX + (double)(((float)((i >> 1) % 2) - 0.5F) * this.field_70130_N * 0.8F));
                int l = MathHelper.floor(this.posZ + (double)(((float)((i >> 2) % 2) - 0.5F) * this.field_70130_N * 0.8F));

                if (blockpos$pooledmutableblockpos.getX() != k || blockpos$pooledmutableblockpos.getY() != j || blockpos$pooledmutableblockpos.getZ() != l)
                {
                    blockpos$pooledmutableblockpos.setPos(k, j, l);

                    if (this.world.getBlockState(blockpos$pooledmutableblockpos).func_191058_s())
                    {
                        blockpos$pooledmutableblockpos.func_185344_t();
                        return true;
                    }
                }
            }

            blockpos$pooledmutableblockpos.func_185344_t();
            return false;
        }
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
    {
        return false;
    }

    @Nullable

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        return null;
    }

    /**
     * Handles updating while riding another entity
     */
    public void updateRidden()
    {
        Entity entity = this.getRidingEntity();

        if (this.isPassenger() && entity.removed)
        {
            this.stopRiding();
        }
        else
        {
            this.field_70159_w = 0.0D;
            this.field_70181_x = 0.0D;
            this.field_70179_y = 0.0D;
            this.tick();

            if (this.isPassenger())
            {
                entity.updatePassenger(this);
            }
        }
    }

    public void updatePassenger(Entity passenger)
    {
        if (this.isPassenger(passenger))
        {
            passenger.setPosition(this.posX, this.posY + this.getMountedYOffset() + passenger.getYOffset(), this.posZ);
        }
    }

    /**
     * Applies this entity's orientation (pitch/yaw) to another entity. Used to update passenger orientation.
     */
    public void applyOrientationToEntity(Entity entityToUpdate)
    {
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        return 0.0D;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return (double)this.field_70131_O * 0.75D;
    }

    public boolean startRiding(Entity entityIn)
    {
        return this.startRiding(entityIn, false);
    }

    public boolean startRiding(Entity entityIn, boolean force)
    {
        for (Entity entity = entityIn; entity.ridingEntity != null; entity = entity.ridingEntity)
        {
            if (entity.ridingEntity == this)
            {
                return false;
            }
        }

        if (force || this.canBeRidden(entityIn) && entityIn.canFitPassenger(this))
        {
            if (this.isPassenger())
            {
                this.stopRiding();
            }

            this.ridingEntity = entityIn;
            this.ridingEntity.addPassenger(this);
            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean canBeRidden(Entity entityIn)
    {
        return this.rideCooldown <= 0;
    }

    /**
     * Dismounts all entities riding this entity from this entity.
     */
    public void removePassengers()
    {
        for (int i = this.passengers.size() - 1; i >= 0; --i)
        {
            ((Entity)this.passengers.get(i)).stopRiding();
        }
    }

    /**
     * Dismounts this entity from the entity it is riding.
     */
    public void stopRiding()
    {
        if (this.ridingEntity != null)
        {
            Entity entity = this.ridingEntity;
            this.ridingEntity = null;
            entity.removePassenger(this);
        }
    }

    protected void addPassenger(Entity passenger)
    {
        if (passenger.getRidingEntity() != this)
        {
            throw new IllegalStateException("Use x.startRiding(y), not y.addPassenger(x)");
        }
        else
        {
            if (!this.world.isRemote && passenger instanceof EntityPlayer && !(this.getControllingPassenger() instanceof EntityPlayer))
            {
                this.passengers.add(0, passenger);
            }
            else
            {
                this.passengers.add(passenger);
            }
        }
    }

    protected void removePassenger(Entity passenger)
    {
        if (passenger.getRidingEntity() == this)
        {
            throw new IllegalStateException("Use x.stopRiding(y), not y.removePassenger(x)");
        }
        else
        {
            this.passengers.remove(passenger);
            passenger.rideCooldown = 60;
        }
    }

    protected boolean canFitPassenger(Entity passenger)
    {
        return this.getPassengers().size() < 1;
    }

    /**
     * Sets a target for the client to interpolate towards over the next few ticks
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    public float getCollisionBorderSize()
    {
        return 0.0F;
    }

    /**
     * returns a (normalized) vector of where this entity is looking
     */
    public Vec3d getLookVec()
    {
        return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
    }

    /**
     * returns the Entity's pitch and yaw as a Vec2f
     */
    public Vec2f getPitchYaw()
    {
        return new Vec2f(this.rotationPitch, this.rotationYaw);
    }

    public Vec3d getForward()
    {
        return Vec3d.fromPitchYaw(this.getPitchYaw());
    }

    /**
     * Marks the entity as being inside a portal, activating teleportation logic in onEntityUpdate() in the following
     * tick(s).
     */
    public void setPortal(BlockPos pos)
    {
        if (this.timeUntilPortal > 0)
        {
            this.timeUntilPortal = this.getPortalCooldown();
        }
        else
        {
            if (!this.world.isRemote && !pos.equals(this.lastPortalPos))
            {
                this.lastPortalPos = new BlockPos(pos);
                BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.NETHER_PORTAL.createPatternHelper(this.world, this.lastPortalPos);
                double d0 = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? (double)blockpattern$patternhelper.getFrontTopLeft().getZ() : (double)blockpattern$patternhelper.getFrontTopLeft().getX();
                double d1 = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? this.posZ : this.posX;
                d1 = Math.abs(MathHelper.pct(d1 - (double)(blockpattern$patternhelper.getForwards().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE ? 1 : 0), d0, d0 - (double)blockpattern$patternhelper.getWidth()));
                double d2 = MathHelper.pct(this.posY - 1.0D, (double)blockpattern$patternhelper.getFrontTopLeft().getY(), (double)(blockpattern$patternhelper.getFrontTopLeft().getY() - blockpattern$patternhelper.getHeight()));
                this.lastPortalVec = new Vec3d(d1, d2, 0.0D);
                this.teleportDirection = blockpattern$patternhelper.getForwards();
            }

            this.inPortal = true;
        }
    }

    /**
     * Return the amount of cooldown before this entity can use a portal again.
     */
    public int getPortalCooldown()
    {
        return 300;
    }

    /**
     * Updates the entity motion clientside, called by packets from the server
     */
    public void setVelocity(double x, double y, double z)
    {
        this.field_70159_w = x;
        this.field_70181_x = y;
        this.field_70179_y = z;
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    public void handleStatusUpdate(byte id)
    {
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation()
    {
    }

    public Iterable<ItemStack> getHeldEquipment()
    {
        return EMPTY_EQUIPMENT;
    }

    public Iterable<ItemStack> getArmorInventoryList()
    {
        return EMPTY_EQUIPMENT;
    }

    public Iterable<ItemStack> getEquipmentAndArmor()
    {
        return Iterables.<ItemStack>concat(this.getHeldEquipment(), this.getArmorInventoryList());
    }

    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack)
    {
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning()
    {
        boolean flag = this.world != null && this.world.isRemote;
        return !this.field_70178_ae && (this.fire > 0 || flag && this.getFlag(0));
    }

    public boolean isPassenger()
    {
        return this.getRidingEntity() != null;
    }

    /**
     * If at least 1 entity is riding this one
     */
    public boolean isBeingRidden()
    {
        return !this.getPassengers().isEmpty();
    }

    public boolean func_70093_af()
    {
        return this.getFlag(1);
    }

    public void func_70095_a(boolean p_70095_1_)
    {
        this.setFlag(1, p_70095_1_);
    }

    /**
     * Get if the Entity is sprinting.
     */
    public boolean isSprinting()
    {
        return this.getFlag(3);
    }

    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean sprinting)
    {
        this.setFlag(3, sprinting);
    }

    public boolean func_184202_aL()
    {
        return this.glowing || this.world.isRemote && this.getFlag(6);
    }

    public void setGlowing(boolean glowingIn)
    {
        this.glowing = glowingIn;

        if (!this.world.isRemote)
        {
            this.setFlag(6, this.glowing);
        }
    }

    public boolean isInvisible()
    {
        return this.getFlag(5);
    }

    /**
     * Only used by renderer in EntityLivingBase subclasses.
     * Determines if an entity is visible or not to a specific player, if the entity is normally invisible.
     * For EntityLivingBase subclasses, returning false when invisible will render the entity semi-transparent.
     */
    public boolean isInvisibleToPlayer(EntityPlayer player)
    {
        if (player.isSpectator())
        {
            return false;
        }
        else
        {
            Team team = this.getTeam();
            return team != null && player != null && player.getTeam() == team && team.getSeeFriendlyInvisiblesEnabled() ? false : this.isInvisible();
        }
    }

    @Nullable
    public Team getTeam()
    {
        return this.world.getScoreboard().getPlayersTeam(this.getCachedUniqueIdString());
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isOnSameTeam(Entity entityIn)
    {
        return this.isOnScoreboardTeam(entityIn.getTeam());
    }

    /**
     * Returns whether this Entity is on the given scoreboard team.
     */
    public boolean isOnScoreboardTeam(Team teamIn)
    {
        return this.getTeam() != null ? this.getTeam().isSameTeam(teamIn) : false;
    }

    public void setInvisible(boolean invisible)
    {
        this.setFlag(5, invisible);
    }

    /**
     * Returns true if the flag is active for the entity. Known flags: 0: burning; 1: sneaking; 2: unused; 3: sprinting;
     * 4: swimming; 5: invisible; 6: glowing; 7: elytra flying
     */
    protected boolean getFlag(int flag)
    {
        return (((Byte)this.dataManager.get(FLAGS)).byteValue() & 1 << flag) != 0;
    }

    /**
     * Enable or disable a entity flag, see getEntityFlag to read the know flags.
     */
    protected void setFlag(int flag, boolean set)
    {
        byte b0 = ((Byte)this.dataManager.get(FLAGS)).byteValue();

        if (set)
        {
            this.dataManager.set(FLAGS, Byte.valueOf((byte)(b0 | 1 << flag)));
        }
        else
        {
            this.dataManager.set(FLAGS, Byte.valueOf((byte)(b0 & ~(1 << flag))));
        }
    }

    public int getAir()
    {
        return ((Integer)this.dataManager.get(AIR)).intValue();
    }

    public void setAir(int air)
    {
        this.dataManager.set(AIR, Integer.valueOf(air));
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {
        this.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 5.0F);
        ++this.fire;

        if (this.fire == 0)
        {
            this.setFire(8);
        }
    }

    /**
     * This method gets called when the entity kills another one.
     */
    public void onKillEntity(EntityLivingBase entityLivingIn)
    {
    }

    protected boolean func_145771_j(double p_145771_1_, double p_145771_3_, double p_145771_5_)
    {
        BlockPos blockpos = new BlockPos(p_145771_1_, p_145771_3_, p_145771_5_);
        double d0 = p_145771_1_ - (double)blockpos.getX();
        double d1 = p_145771_3_ - (double)blockpos.getY();
        double d2 = p_145771_5_ - (double)blockpos.getZ();

        if (!this.world.func_184143_b(this.getBoundingBox()))
        {
            return false;
        }
        else
        {
            EnumFacing enumfacing = EnumFacing.UP;
            double d3 = Double.MAX_VALUE;

            if (!this.world.func_175665_u(blockpos.west()) && d0 < d3)
            {
                d3 = d0;
                enumfacing = EnumFacing.WEST;
            }

            if (!this.world.func_175665_u(blockpos.east()) && 1.0D - d0 < d3)
            {
                d3 = 1.0D - d0;
                enumfacing = EnumFacing.EAST;
            }

            if (!this.world.func_175665_u(blockpos.north()) && d2 < d3)
            {
                d3 = d2;
                enumfacing = EnumFacing.NORTH;
            }

            if (!this.world.func_175665_u(blockpos.south()) && 1.0D - d2 < d3)
            {
                d3 = 1.0D - d2;
                enumfacing = EnumFacing.SOUTH;
            }

            if (!this.world.func_175665_u(blockpos.up()) && 1.0D - d1 < d3)
            {
                d3 = 1.0D - d1;
                enumfacing = EnumFacing.UP;
            }

            float f = this.rand.nextFloat() * 0.2F + 0.1F;
            float f1 = (float)enumfacing.getAxisDirection().getOffset();

            if (enumfacing.getAxis() == EnumFacing.Axis.X)
            {
                this.field_70159_w = (double)(f1 * f);
                this.field_70181_x *= 0.75D;
                this.field_70179_y *= 0.75D;
            }
            else if (enumfacing.getAxis() == EnumFacing.Axis.Y)
            {
                this.field_70159_w *= 0.75D;
                this.field_70181_x = (double)(f1 * f);
                this.field_70179_y *= 0.75D;
            }
            else if (enumfacing.getAxis() == EnumFacing.Axis.Z)
            {
                this.field_70159_w *= 0.75D;
                this.field_70181_x *= 0.75D;
                this.field_70179_y = (double)(f1 * f);
            }

            return true;
        }
    }

    public void func_70110_aj()
    {
        this.field_70134_J = true;
        this.fallDistance = 0.0F;
    }

    public String func_70005_c_()
    {
        if (this.hasCustomName())
        {
            return this.func_95999_t();
        }
        else
        {
            String s = EntityList.func_75621_b(this);

            if (s == null)
            {
                s = "generic";
            }

            return I18n.func_74838_a("entity." + s + ".name");
        }
    }

    @Nullable
    public Entity[] func_70021_al()
    {
        return null;
    }

    /**
     * Returns true if Entity argument is equal to this Entity
     */
    public boolean isEntityEqual(Entity entityIn)
    {
        return this == entityIn;
    }

    public float getRotationYawHead()
    {
        return 0.0F;
    }

    /**
     * Sets the head's yaw rotation of the entity.
     */
    public void setRotationYawHead(float rotation)
    {
    }

    /**
     * Set the render yaw offset
     */
    public void setRenderYawOffset(float offset)
    {
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return true;
    }

    /**
     * Called when a player attacks an entity. If this returns true the attack will not happen.
     */
    public boolean hitByEntity(Entity entityIn)
    {
        return false;
    }

    public String toString()
    {
        return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", this.getClass().getSimpleName(), this.func_70005_c_(), this.entityId, this.world == null ? "~NULL~" : this.world.getWorldInfo().getWorldName(), this.posX, this.posY, this.posZ);
    }

    /**
     * Returns whether this Entity is invulnerable to the given DamageSource.
     */
    public boolean isInvulnerableTo(DamageSource source)
    {
        return this.invulnerable && source != DamageSource.OUT_OF_WORLD && !source.isCreativePlayer();
    }

    public boolean isInvulnerable()
    {
        return this.invulnerable;
    }

    /**
     * Sets whether this Entity is invulnerable.
     */
    public void setInvulnerable(boolean isInvulnerable)
    {
        this.invulnerable = isInvulnerable;
    }

    /**
     * Sets this entity's location and angles to the location and angles of the passed in entity.
     */
    public void copyLocationAndAnglesFrom(Entity entityIn)
    {
        this.setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
    }

    /**
     * Prepares this entity in new dimension by copying NBT data from entity in old dimension
     */
    private void copyDataFromOld(Entity entityIn)
    {
        NBTTagCompound nbttagcompound = entityIn.writeWithoutTypeId(new NBTTagCompound());
        nbttagcompound.remove("Dimension");
        this.read(nbttagcompound);
        this.timeUntilPortal = entityIn.timeUntilPortal;
        this.lastPortalPos = entityIn.lastPortalPos;
        this.lastPortalVec = entityIn.lastPortalVec;
        this.teleportDirection = entityIn.teleportDirection;
    }

    @Nullable
    public Entity func_184204_a(int p_184204_1_)
    {
        if (!this.world.isRemote && !this.removed)
        {
            this.world.profiler.startSection("changeDimension");
            MinecraftServer minecraftserver = this.getServer();
            int i = this.dimension;
            WorldServer worldserver = minecraftserver.getWorld(i);
            WorldServer worldserver1 = minecraftserver.getWorld(p_184204_1_);
            this.dimension = p_184204_1_;

            if (i == 1 && p_184204_1_ == 1)
            {
                worldserver1 = minecraftserver.getWorld(0);
                this.dimension = 0;
            }

            this.world.func_72900_e(this);
            this.removed = false;
            this.world.profiler.startSection("reposition");
            BlockPos blockpos;

            if (p_184204_1_ == 1)
            {
                blockpos = worldserver1.getSpawnCoordinate();
            }
            else
            {
                double d0 = this.posX;
                double d1 = this.posZ;
                double d2 = 8.0D;

                if (p_184204_1_ == -1)
                {
                    d0 = MathHelper.clamp(d0 / 8.0D, worldserver1.getWorldBorder().minX() + 16.0D, worldserver1.getWorldBorder().maxX() - 16.0D);
                    d1 = MathHelper.clamp(d1 / 8.0D, worldserver1.getWorldBorder().minZ() + 16.0D, worldserver1.getWorldBorder().maxZ() - 16.0D);
                }
                else if (p_184204_1_ == 0)
                {
                    d0 = MathHelper.clamp(d0 * 8.0D, worldserver1.getWorldBorder().minX() + 16.0D, worldserver1.getWorldBorder().maxX() - 16.0D);
                    d1 = MathHelper.clamp(d1 * 8.0D, worldserver1.getWorldBorder().minZ() + 16.0D, worldserver1.getWorldBorder().maxZ() - 16.0D);
                }

                d0 = (double)MathHelper.clamp((int)d0, -29999872, 29999872);
                d1 = (double)MathHelper.clamp((int)d1, -29999872, 29999872);
                float f = this.rotationYaw;
                this.setLocationAndAngles(d0, this.posY, d1, 90.0F, 0.0F);
                Teleporter teleporter = worldserver1.getDefaultTeleporter();
                teleporter.func_180620_b(this, f);
                blockpos = new BlockPos(this);
            }

            worldserver.func_72866_a(this, false);
            this.world.profiler.func_76318_c("reloading");
            Entity entity = EntityList.func_191304_a(this.getClass(), worldserver1);

            if (entity != null)
            {
                entity.copyDataFromOld(this);

                if (i == 1 && p_184204_1_ == 1)
                {
                    BlockPos blockpos1 = worldserver1.func_175672_r(worldserver1.getSpawnPoint());
                    entity.moveToBlockPosAndAngles(blockpos1, entity.rotationYaw, entity.rotationPitch);
                }
                else
                {
                    entity.moveToBlockPosAndAngles(blockpos, entity.rotationYaw, entity.rotationPitch);
                }

                boolean flag = entity.forceSpawn;
                entity.forceSpawn = true;
                worldserver1.addEntity0(entity);
                entity.forceSpawn = flag;
                worldserver1.func_72866_a(entity, false);
            }

            this.removed = true;
            this.world.profiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            this.world.profiler.endSection();
            return entity;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns false if this Entity is a boss, true otherwise.
     */
    public boolean isNonBoss()
    {
        return true;
    }

    /**
     * Explosion resistance of a block relative to this entity
     */
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn)
    {
        return blockStateIn.getBlock().getExplosionResistance(this);
    }

    public boolean canExplosionDestroyBlock(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float p_174816_5_)
    {
        return true;
    }

    /**
     * The maximum height from where the entity is alowed to jump (used in pathfinder)
     */
    public int getMaxFallHeight()
    {
        return 3;
    }

    public Vec3d getLastPortalVec()
    {
        return this.lastPortalVec;
    }

    public EnumFacing getTeleportDirection()
    {
        return this.teleportDirection;
    }

    /**
     * Return whether this entity should NOT trigger a pressure plate or a tripwire.
     */
    public boolean doesEntityNotTriggerPressurePlate()
    {
        return false;
    }

    public void fillCrashReport(CrashReportCategory category)
    {
        category.addDetail("Entity Type", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return EntityList.func_191301_a(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
            }
        });
        category.addDetail("Entity ID", Integer.valueOf(this.entityId));
        category.addDetail("Entity Name", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return Entity.this.func_70005_c_();
            }
        });
        category.addDetail("Entity's Exact location", String.format("%.2f, %.2f, %.2f", this.posX, this.posY, this.posZ));
        category.addDetail("Entity's Block location", CrashReportCategory.getCoordinateInfo(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)));
        category.addDetail("Entity's Momentum", String.format("%.2f, %.2f, %.2f", this.field_70159_w, this.field_70181_x, this.field_70179_y));
        category.addDetail("Entity's Passengers", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return Entity.this.getPassengers().toString();
            }
        });
        category.addDetail("Entity's Vehicle", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return Entity.this.getRidingEntity().toString();
            }
        });
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    public boolean canRenderOnFire()
    {
        return this.isBurning();
    }

    public void setUniqueId(UUID uniqueIdIn)
    {
        this.entityUniqueID = uniqueIdIn;
        this.cachedUniqueIdString = this.entityUniqueID.toString();
    }

    /**
     * Returns the UUID of this entity.
     */
    public UUID getUniqueID()
    {
        return this.entityUniqueID;
    }

    public String getCachedUniqueIdString()
    {
        return this.cachedUniqueIdString;
    }

    public boolean isPushedByWater()
    {
        return true;
    }

    public static double getRenderDistanceWeight()
    {
        return renderDistanceWeight;
    }

    public static void setRenderDistanceWeight(double renderDistWeight)
    {
        renderDistanceWeight = renderDistWeight;
    }

    public ITextComponent getDisplayName()
    {
        TextComponentString textcomponentstring = new TextComponentString(ScorePlayerTeam.func_96667_a(this.getTeam(), this.func_70005_c_()));
        textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
        textcomponentstring.getStyle().setInsertion(this.getCachedUniqueIdString());
        return textcomponentstring;
    }

    public void func_96094_a(String p_96094_1_)
    {
        this.dataManager.set(CUSTOM_NAME, p_96094_1_);
    }

    public String func_95999_t()
    {
        return (String)this.dataManager.get(CUSTOM_NAME);
    }

    public boolean hasCustomName()
    {
        return !((String)this.dataManager.get(CUSTOM_NAME)).isEmpty();
    }

    public void setCustomNameVisible(boolean alwaysRenderNameTag)
    {
        this.dataManager.set(CUSTOM_NAME_VISIBLE, Boolean.valueOf(alwaysRenderNameTag));
    }

    public boolean isCustomNameVisible()
    {
        return ((Boolean)this.dataManager.get(CUSTOM_NAME_VISIBLE)).booleanValue();
    }

    /**
     * Sets the position of the entity and updates the 'last' variables
     */
    public void setPositionAndUpdate(double x, double y, double z)
    {
        this.isPositionDirty = true;
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.world.func_72866_a(this, false);
    }

    public boolean getAlwaysRenderNameTagForRender()
    {
        return this.isCustomNameVisible();
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
    }

    /**
     * Gets the horizontal facing direction of this Entity.
     */
    public EnumFacing getHorizontalFacing()
    {
        return EnumFacing.byHorizontalIndex(MathHelper.floor((double)(this.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
    }

    /**
     * Gets the horizontal facing direction of this Entity, adjusted to take specially-treated entity types into
     * account.
     */
    public EnumFacing getAdjustedHorizontalFacing()
    {
        return this.getHorizontalFacing();
    }

    protected HoverEvent getHoverEvent()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        ResourceLocation resourcelocation = EntityList.func_191301_a(this);
        nbttagcompound.putString("id", this.getCachedUniqueIdString());

        if (resourcelocation != null)
        {
            nbttagcompound.putString("type", resourcelocation.toString());
        }

        nbttagcompound.putString("name", this.func_70005_c_());
        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new TextComponentString(nbttagcompound.toString()));
    }

    public boolean isSpectatedByPlayer(EntityPlayerMP player)
    {
        return true;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    /**
     * Gets the bounding box of this Entity, adjusted to take auxiliary entities into account (e.g. the tile contained
     * by a minecart, such as a command block).
     */
    public AxisAlignedBB getRenderBoundingBox()
    {
        return this.getBoundingBox();
    }

    public void setBoundingBox(AxisAlignedBB bb)
    {
        this.boundingBox = bb;
    }

    public float getEyeHeight()
    {
        return this.field_70131_O * 0.85F;
    }

    public boolean func_174832_aS()
    {
        return this.field_174835_g;
    }

    public void func_174821_h(boolean p_174821_1_)
    {
        this.field_174835_g = p_174821_1_;
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
    {
        return false;
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void sendMessage(ITextComponent component)
    {
    }

    public boolean func_70003_b(int p_70003_1_, String p_70003_2_)
    {
        return true;
    }

    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    public BlockPos getPosition()
    {
        return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
    }

    /**
     * Get the position vector. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return 0.0D,
     * 0.0D, 0.0D
     */
    public Vec3d getPositionVector()
    {
        return new Vec3d(this.posX, this.posY, this.posZ);
    }

    /**
     * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the overworld
     */
    public World getEntityWorld()
    {
        return this.world;
    }

    public Entity func_174793_f()
    {
        return this;
    }

    public boolean func_174792_t_()
    {
        return false;
    }

    public void func_174794_a(CommandResultStats.Type p_174794_1_, int p_174794_2_)
    {
        if (this.world != null && !this.world.isRemote)
        {
            this.field_174837_as.func_184932_a(this.world.getServer(), this, p_174794_1_, p_174794_2_);
        }
    }

    @Nullable

    /**
     * Get the Minecraft server instance
     */
    public MinecraftServer getServer()
    {
        return this.world.getServer();
    }

    public CommandResultStats func_174807_aT()
    {
        return this.field_174837_as;
    }

    public void func_174817_o(Entity p_174817_1_)
    {
        this.field_174837_as.func_179671_a(p_174817_1_.func_174807_aT());
    }

    /**
     * Applies the given player interaction to this Entity.
     */
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand)
    {
        return EnumActionResult.PASS;
    }

    public boolean isImmuneToExplosions()
    {
        return false;
    }

    protected void applyEnchantments(EntityLivingBase entityLivingBaseIn, Entity entityIn)
    {
        if (entityIn instanceof EntityLivingBase)
        {
            EnchantmentHelper.applyThornEnchantments((EntityLivingBase)entityIn, entityLivingBaseIn);
        }

        EnchantmentHelper.applyArthropodEnchantments(entityLivingBaseIn, entityIn);
    }

    /**
     * Add the given player to the list of players tracking this entity. For instance, a player may track a boss in
     * order to view its associated boss bar.
     */
    public void addTrackingPlayer(EntityPlayerMP player)
    {
    }

    /**
     * Removes the given player from the list of players tracking this entity. See {@link Entity#addTrackingPlayer} for
     * more information on tracking.
     */
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
    }

    /**
     * Transforms the entity's current yaw with the given Rotation and returns it. This does not have a side-effect.
     */
    public float getRotatedYaw(Rotation transformRotation)
    {
        float f = MathHelper.wrapDegrees(this.rotationYaw);

        switch (transformRotation)
        {
            case CLOCKWISE_180:
                return f + 180.0F;

            case COUNTERCLOCKWISE_90:
                return f + 270.0F;

            case CLOCKWISE_90:
                return f + 90.0F;

            default:
                return f;
        }
    }

    /**
     * Transforms the entity's current yaw with the given Mirror and returns it. This does not have a side-effect.
     */
    public float getMirroredYaw(Mirror transformMirror)
    {
        float f = MathHelper.wrapDegrees(this.rotationYaw);

        switch (transformMirror)
        {
            case LEFT_RIGHT:
                return -f;

            case FRONT_BACK:
                return 180.0F - f;

            default:
                return f;
        }
    }

    /**
     * Checks if players can use this entity to access operator (permission level 2) commands either directly or
     * indirectly, such as give or setblock. A similar method exists for entities at {@link
     * net.minecraft.tileentity.TileEntity#onlyOpsCanSetNbt()}.<p>For example, {@link
     * net.minecraft.entity.item.EntityMinecartCommandBlock#ignoreItemEntityData() command block minecarts} and {@link
     * net.minecraft.entity.item.EntityMinecartMobSpawner#ignoreItemEntityData() mob spawner minecarts} (spawning
     * command block minecarts or drops) are considered accessible.</p>@return true if this entity offers ways for
     * unauthorized players to use restricted commands
     */
    public boolean ignoreItemEntityData()
    {
        return false;
    }

    public boolean setPositionNonDirty()
    {
        boolean flag = this.isPositionDirty;
        this.isPositionDirty = false;
        return flag;
    }

    @Nullable

    /**
     * For vehicles, the first passenger is generally considered the controller and "drives" the vehicle. For example,
     * Pigs, Horses, and Boats are generally "steered" by the controlling passenger.
     */
    public Entity getControllingPassenger()
    {
        return null;
    }

    public List<Entity> getPassengers()
    {
        return (List<Entity>)(this.passengers.isEmpty() ? Collections.emptyList() : Lists.newArrayList(this.passengers));
    }

    public boolean isPassenger(Entity entityIn)
    {
        for (Entity entity : this.getPassengers())
        {
            if (entity.equals(entityIn))
            {
                return true;
            }
        }

        return false;
    }

    public Collection<Entity> getRecursivePassengers()
    {
        Set<Entity> set = Sets.<Entity>newHashSet();
        this.func_184175_a(Entity.class, set);
        return set;
    }

    public <T extends Entity> Collection<T> func_184180_b(Class<T> p_184180_1_)
    {
        Set<T> set = Sets.<T>newHashSet();
        this.func_184175_a(p_184180_1_, set);
        return set;
    }

    private <T extends Entity> void func_184175_a(Class<T> p_184175_1_, Set<T> p_184175_2_)
    {
        for (Entity entity : this.getPassengers())
        {
            if (p_184175_1_.isAssignableFrom(entity.getClass()))
            {
                p_184175_2_.add((T)entity);
            }

            entity.func_184175_a(p_184175_1_, p_184175_2_);
        }
    }

    public Entity getLowestRidingEntity()
    {
        Entity entity;

        for (entity = this; entity.isPassenger(); entity = entity.getRidingEntity())
        {
            ;
        }

        return entity;
    }

    public boolean isRidingSameEntity(Entity entityIn)
    {
        return this.getLowestRidingEntity() == entityIn.getLowestRidingEntity();
    }

    public boolean isRidingOrBeingRiddenBy(Entity entityIn)
    {
        for (Entity entity : this.getPassengers())
        {
            if (entity.equals(entityIn))
            {
                return true;
            }

            if (entity.isRidingOrBeingRiddenBy(entityIn))
            {
                return true;
            }
        }

        return false;
    }

    public boolean canPassengerSteer()
    {
        Entity entity = this.getControllingPassenger();

        if (entity instanceof EntityPlayer)
        {
            return ((EntityPlayer)entity).isUser();
        }
        else
        {
            return !this.world.isRemote;
        }
    }

    @Nullable

    /**
     * Get entity this is riding
     */
    public Entity getRidingEntity()
    {
        return this.ridingEntity;
    }

    public EnumPushReaction getPushReaction()
    {
        return EnumPushReaction.NORMAL;
    }

    public SoundCategory getSoundCategory()
    {
        return SoundCategory.NEUTRAL;
    }

    protected int getFireImmuneTicks()
    {
        return 1;
    }
}

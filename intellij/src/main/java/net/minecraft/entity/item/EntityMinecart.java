package net.minecraft.entity.item;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityMinecart extends Entity implements IWorldNameable
{
    private static final DataParameter<Integer> ROLLING_AMPLITUDE = EntityDataManager.<Integer>createKey(EntityMinecart.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> ROLLING_DIRECTION = EntityDataManager.<Integer>createKey(EntityMinecart.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DAMAGE = EntityDataManager.<Float>createKey(EntityMinecart.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> DISPLAY_TILE = EntityDataManager.<Integer>createKey(EntityMinecart.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DISPLAY_TILE_OFFSET = EntityDataManager.<Integer>createKey(EntityMinecart.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SHOW_BLOCK = EntityDataManager.<Boolean>createKey(EntityMinecart.class, DataSerializers.BOOLEAN);
    private boolean isInReverse;
    private static final int[][][] MATRIX = new int[][][] {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
    private int turnProgress;
    private double minecartX;
    private double minecartY;
    private double minecartZ;
    private double minecartYaw;
    private double minecartPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;

    public EntityMinecart(World p_i1712_1_)
    {
        super(p_i1712_1_);
        this.preventEntitySpawning = true;
        this.func_70105_a(0.98F, 0.7F);
    }

    public static EntityMinecart create(World worldIn, double x, double y, double z, EntityMinecart.Type typeIn)
    {
        switch (typeIn)
        {
            case CHEST:
                return new EntityMinecartChest(worldIn, x, y, z);

            case FURNACE:
                return new EntityMinecartFurnace(worldIn, x, y, z);

            case TNT:
                return new EntityMinecartTNT(worldIn, x, y, z);

            case SPAWNER:
                return new EntityMinecartMobSpawner(worldIn, x, y, z);

            case HOPPER:
                return new EntityMinecartHopper(worldIn, x, y, z);

            case COMMAND_BLOCK:
                return new EntityMinecartCommandBlock(worldIn, x, y, z);

            default:
                return new EntityMinecartEmpty(worldIn, x, y, z);
        }
    }

    protected boolean func_70041_e_()
    {
        return false;
    }

    protected void registerData()
    {
        this.dataManager.register(ROLLING_AMPLITUDE, Integer.valueOf(0));
        this.dataManager.register(ROLLING_DIRECTION, Integer.valueOf(1));
        this.dataManager.register(DAMAGE, Float.valueOf(0.0F));
        this.dataManager.register(DISPLAY_TILE, Integer.valueOf(0));
        this.dataManager.register(DISPLAY_TILE_OFFSET, Integer.valueOf(6));
        this.dataManager.register(SHOW_BLOCK, Boolean.valueOf(false));
    }

    @Nullable

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        return entityIn.canBePushed() ? entityIn.getBoundingBox() : null;
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
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return true;
    }

    public EntityMinecart(World p_i1713_1_, double p_i1713_2_, double p_i1713_4_, double p_i1713_6_)
    {
        this(p_i1713_1_);
        this.setPosition(p_i1713_2_, p_i1713_4_, p_i1713_6_);
        this.field_70159_w = 0.0D;
        this.field_70181_x = 0.0D;
        this.field_70179_y = 0.0D;
        this.prevPosX = p_i1713_2_;
        this.prevPosY = p_i1713_4_;
        this.prevPosZ = p_i1713_6_;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return 0.0D;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!this.world.isRemote && !this.removed)
        {
            if (this.isInvulnerableTo(source))
            {
                return false;
            }
            else
            {
                this.setRollingDirection(-this.getRollingDirection());
                this.setRollingAmplitude(10);
                this.markVelocityChanged();
                this.setDamage(this.getDamage() + amount * 10.0F);
                boolean flag = source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer)source.getTrueSource()).abilities.isCreativeMode;

                if (flag || this.getDamage() > 40.0F)
                {
                    this.removePassengers();

                    if (flag && !this.hasCustomName())
                    {
                        this.remove();
                    }
                    else
                    {
                        this.killMinecart(source);
                    }
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    public void killMinecart(DamageSource source)
    {
        this.remove();

        if (this.world.getGameRules().func_82766_b("doEntityDrops"))
        {
            ItemStack itemstack = new ItemStack(Items.MINECART, 1);

            if (this.hasCustomName())
            {
                itemstack.func_151001_c(this.func_95999_t());
            }

            this.entityDropItem(itemstack, 0.0F);
        }
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation()
    {
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(10);
        this.setDamage(this.getDamage() + this.getDamage() * 10.0F);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.removed;
    }

    /**
     * Gets the horizontal facing direction of this Entity, adjusted to take specially-treated entity types into
     * account.
     */
    public EnumFacing getAdjustedHorizontalFacing()
    {
        return this.isInReverse ? this.getHorizontalFacing().getOpposite().rotateY() : this.getHorizontalFacing().rotateY();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        if (this.getRollingAmplitude() > 0)
        {
            this.setRollingAmplitude(this.getRollingAmplitude() - 1);
        }

        if (this.getDamage() > 0.0F)
        {
            this.setDamage(this.getDamage() - 1.0F);
        }

        if (this.posY < -64.0D)
        {
            this.outOfWorld();
        }

        if (!this.world.isRemote && this.world instanceof WorldServer)
        {
            this.world.profiler.startSection("portal");
            MinecraftServer minecraftserver = this.world.getServer();
            int i = this.getMaxInPortalTime();

            if (this.inPortal)
            {
                if (minecraftserver.getAllowNether())
                {
                    if (!this.isPassenger() && this.portalCounter++ >= i)
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

            if (this.timeUntilPortal > 0)
            {
                --this.timeUntilPortal;
            }

            this.world.profiler.endSection();
        }

        if (this.world.isRemote)
        {
            if (this.turnProgress > 0)
            {
                double d4 = this.posX + (this.minecartX - this.posX) / (double)this.turnProgress;
                double d5 = this.posY + (this.minecartY - this.posY) / (double)this.turnProgress;
                double d6 = this.posZ + (this.minecartZ - this.posZ) / (double)this.turnProgress;
                double d1 = MathHelper.wrapDegrees(this.minecartYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + d1 / (double)this.turnProgress);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.minecartPitch - (double)this.rotationPitch) / (double)this.turnProgress);
                --this.turnProgress;
                this.setPosition(d4, d5, d6);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            if (!this.hasNoGravity())
            {
                this.field_70181_x -= 0.03999999910593033D;
            }

            int k = MathHelper.floor(this.posX);
            int l = MathHelper.floor(this.posY);
            int i1 = MathHelper.floor(this.posZ);

            if (BlockRailBase.func_176562_d(this.world, new BlockPos(k, l - 1, i1)))
            {
                --l;
            }

            BlockPos blockpos = new BlockPos(k, l, i1);
            IBlockState iblockstate = this.world.getBlockState(blockpos);

            if (BlockRailBase.func_176563_d(iblockstate))
            {
                this.moveAlongTrack(blockpos, iblockstate);

                if (iblockstate.getBlock() == Blocks.ACTIVATOR_RAIL)
                {
                    this.onActivatorRailPass(k, l, i1, ((Boolean)iblockstate.get(BlockRailPowered.POWERED)).booleanValue());
                }
            }
            else
            {
                this.moveDerailedMinecart();
            }

            this.doBlockCollisions();
            this.rotationPitch = 0.0F;
            double d0 = this.prevPosX - this.posX;
            double d2 = this.prevPosZ - this.posZ;

            if (d0 * d0 + d2 * d2 > 0.001D)
            {
                this.rotationYaw = (float)(MathHelper.atan2(d2, d0) * 180.0D / Math.PI);

                if (this.isInReverse)
                {
                    this.rotationYaw += 180.0F;
                }
            }

            double d3 = (double)MathHelper.wrapDegrees(this.rotationYaw - this.prevRotationYaw);

            if (d3 < -170.0D || d3 >= 170.0D)
            {
                this.rotationYaw += 180.0F;
                this.isInReverse = !this.isInReverse;
            }

            this.setRotation(this.rotationYaw, this.rotationPitch);

            if (this.getMinecartType() == EntityMinecart.Type.RIDEABLE && this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y > 0.01D)
            {
                List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().grow(0.20000000298023224D, 0.0D, 0.20000000298023224D), EntitySelectors.func_188442_a(this));

                if (!list.isEmpty())
                {
                    for (int j1 = 0; j1 < list.size(); ++j1)
                    {
                        Entity entity1 = list.get(j1);

                        if (!(entity1 instanceof EntityPlayer) && !(entity1 instanceof EntityIronGolem) && !(entity1 instanceof EntityMinecart) && !this.isBeingRidden() && !entity1.isPassenger())
                        {
                            entity1.startRiding(this);
                        }
                        else
                        {
                            entity1.applyEntityCollision(this);
                        }
                    }
                }
            }
            else
            {
                for (Entity entity : this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().grow(0.20000000298023224D, 0.0D, 0.20000000298023224D)))
                {
                    if (!this.isPassenger(entity) && entity.canBePushed() && entity instanceof EntityMinecart)
                    {
                        entity.applyEntityCollision(this);
                    }
                }
            }

            this.handleWaterMovement();
        }
    }

    /**
     * Get's the maximum speed for a minecart
     */
    protected double getMaximumSpeed()
    {
        return 0.4D;
    }

    /**
     * Called every tick the minecart is on an activator rail.
     */
    public void onActivatorRailPass(int x, int y, int z, boolean receivingPower)
    {
    }

    /**
     * Moves a minecart that is not attached to a rail
     */
    protected void moveDerailedMinecart()
    {
        double d0 = this.getMaximumSpeed();
        this.field_70159_w = MathHelper.clamp(this.field_70159_w, -d0, d0);
        this.field_70179_y = MathHelper.clamp(this.field_70179_y, -d0, d0);

        if (this.onGround)
        {
            this.field_70159_w *= 0.5D;
            this.field_70181_x *= 0.5D;
            this.field_70179_y *= 0.5D;
        }

        this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);

        if (!this.onGround)
        {
            this.field_70159_w *= 0.949999988079071D;
            this.field_70181_x *= 0.949999988079071D;
            this.field_70179_y *= 0.949999988079071D;
        }
    }

    @SuppressWarnings("incomplete-switch")
    protected void moveAlongTrack(BlockPos pos, IBlockState state)
    {
        this.fallDistance = 0.0F;
        Vec3d vec3d = this.getPos(this.posX, this.posY, this.posZ);
        this.posY = (double)pos.getY();
        boolean flag = false;
        boolean flag1 = false;
        BlockRailBase blockrailbase = (BlockRailBase)state.getBlock();

        if (blockrailbase == Blocks.field_150318_D)
        {
            flag = ((Boolean)state.get(BlockRailPowered.POWERED)).booleanValue();
            flag1 = !flag;
        }

        double d0 = 0.0078125D;
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)state.get(blockrailbase.getShapeProperty());

        switch (blockrailbase$enumraildirection)
        {
            case ASCENDING_EAST:
                this.field_70159_w -= 0.0078125D;
                ++this.posY;
                break;

            case ASCENDING_WEST:
                this.field_70159_w += 0.0078125D;
                ++this.posY;
                break;

            case ASCENDING_NORTH:
                this.field_70179_y += 0.0078125D;
                ++this.posY;
                break;

            case ASCENDING_SOUTH:
                this.field_70179_y -= 0.0078125D;
                ++this.posY;
        }

        int[][] aint = MATRIX[blockrailbase$enumraildirection.func_177015_a()];
        double d1 = (double)(aint[1][0] - aint[0][0]);
        double d2 = (double)(aint[1][2] - aint[0][2]);
        double d3 = Math.sqrt(d1 * d1 + d2 * d2);
        double d4 = this.field_70159_w * d1 + this.field_70179_y * d2;

        if (d4 < 0.0D)
        {
            d1 = -d1;
            d2 = -d2;
        }

        double d5 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);

        if (d5 > 2.0D)
        {
            d5 = 2.0D;
        }

        this.field_70159_w = d5 * d1 / d3;
        this.field_70179_y = d5 * d2 / d3;
        Entity entity = this.getPassengers().isEmpty() ? null : (Entity)this.getPassengers().get(0);

        if (entity instanceof EntityLivingBase)
        {
            double d6 = (double)((EntityLivingBase)entity).moveForward;

            if (d6 > 0.0D)
            {
                double d7 = -Math.sin((double)(entity.rotationYaw * 0.017453292F));
                double d8 = Math.cos((double)(entity.rotationYaw * 0.017453292F));
                double d9 = this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y;

                if (d9 < 0.01D)
                {
                    this.field_70159_w += d7 * 0.1D;
                    this.field_70179_y += d8 * 0.1D;
                    flag1 = false;
                }
            }
        }

        if (flag1)
        {
            double d17 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);

            if (d17 < 0.03D)
            {
                this.field_70159_w *= 0.0D;
                this.field_70181_x *= 0.0D;
                this.field_70179_y *= 0.0D;
            }
            else
            {
                this.field_70159_w *= 0.5D;
                this.field_70181_x *= 0.0D;
                this.field_70179_y *= 0.5D;
            }
        }

        double d18 = (double)pos.getX() + 0.5D + (double)aint[0][0] * 0.5D;
        double d19 = (double)pos.getZ() + 0.5D + (double)aint[0][2] * 0.5D;
        double d20 = (double)pos.getX() + 0.5D + (double)aint[1][0] * 0.5D;
        double d21 = (double)pos.getZ() + 0.5D + (double)aint[1][2] * 0.5D;
        d1 = d20 - d18;
        d2 = d21 - d19;
        double d10;

        if (d1 == 0.0D)
        {
            this.posX = (double)pos.getX() + 0.5D;
            d10 = this.posZ - (double)pos.getZ();
        }
        else if (d2 == 0.0D)
        {
            this.posZ = (double)pos.getZ() + 0.5D;
            d10 = this.posX - (double)pos.getX();
        }
        else
        {
            double d11 = this.posX - d18;
            double d12 = this.posZ - d19;
            d10 = (d11 * d1 + d12 * d2) * 2.0D;
        }

        this.posX = d18 + d1 * d10;
        this.posZ = d19 + d2 * d10;
        this.setPosition(this.posX, this.posY, this.posZ);
        double d22 = this.field_70159_w;
        double d23 = this.field_70179_y;

        if (this.isBeingRidden())
        {
            d22 *= 0.75D;
            d23 *= 0.75D;
        }

        double d13 = this.getMaximumSpeed();
        d22 = MathHelper.clamp(d22, -d13, d13);
        d23 = MathHelper.clamp(d23, -d13, d13);
        this.func_70091_d(MoverType.SELF, d22, 0.0D, d23);

        if (aint[0][1] != 0 && MathHelper.floor(this.posX) - pos.getX() == aint[0][0] && MathHelper.floor(this.posZ) - pos.getZ() == aint[0][2])
        {
            this.setPosition(this.posX, this.posY + (double)aint[0][1], this.posZ);
        }
        else if (aint[1][1] != 0 && MathHelper.floor(this.posX) - pos.getX() == aint[1][0] && MathHelper.floor(this.posZ) - pos.getZ() == aint[1][2])
        {
            this.setPosition(this.posX, this.posY + (double)aint[1][1], this.posZ);
        }

        this.applyDrag();
        Vec3d vec3d1 = this.getPos(this.posX, this.posY, this.posZ);

        if (vec3d1 != null && vec3d != null)
        {
            double d14 = (vec3d.y - vec3d1.y) * 0.05D;
            d5 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);

            if (d5 > 0.0D)
            {
                this.field_70159_w = this.field_70159_w / d5 * (d5 + d14);
                this.field_70179_y = this.field_70179_y / d5 * (d5 + d14);
            }

            this.setPosition(this.posX, vec3d1.y, this.posZ);
        }

        int j = MathHelper.floor(this.posX);
        int i = MathHelper.floor(this.posZ);

        if (j != pos.getX() || i != pos.getZ())
        {
            d5 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
            this.field_70159_w = d5 * (double)(j - pos.getX());
            this.field_70179_y = d5 * (double)(i - pos.getZ());
        }

        if (flag)
        {
            double d15 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);

            if (d15 > 0.01D)
            {
                double d16 = 0.06D;
                this.field_70159_w += this.field_70159_w / d15 * 0.06D;
                this.field_70179_y += this.field_70179_y / d15 * 0.06D;
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST)
            {
                if (this.world.getBlockState(pos.west()).func_185915_l())
                {
                    this.field_70159_w = 0.02D;
                }
                else if (this.world.getBlockState(pos.east()).func_185915_l())
                {
                    this.field_70159_w = -0.02D;
                }
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH)
            {
                if (this.world.getBlockState(pos.north()).func_185915_l())
                {
                    this.field_70179_y = 0.02D;
                }
                else if (this.world.getBlockState(pos.south()).func_185915_l())
                {
                    this.field_70179_y = -0.02D;
                }
            }
        }
    }

    protected void applyDrag()
    {
        if (this.isBeingRidden())
        {
            this.field_70159_w *= 0.996999979019165D;
            this.field_70181_x *= 0.0D;
            this.field_70179_y *= 0.996999979019165D;
        }
        else
        {
            this.field_70159_w *= 0.9599999785423279D;
            this.field_70181_x *= 0.0D;
            this.field_70179_y *= 0.9599999785423279D;
        }
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

    @Nullable
    public Vec3d getPosOffset(double x, double y, double z, double offset)
    {
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);

        if (BlockRailBase.func_176562_d(this.world, new BlockPos(i, j - 1, k)))
        {
            --j;
        }

        IBlockState iblockstate = this.world.getBlockState(new BlockPos(i, j, k));

        if (BlockRailBase.func_176563_d(iblockstate))
        {
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)iblockstate.get(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
            y = (double)j;

            if (blockrailbase$enumraildirection.func_177018_c())
            {
                y = (double)(j + 1);
            }

            int[][] aint = MATRIX[blockrailbase$enumraildirection.func_177015_a()];
            double d0 = (double)(aint[1][0] - aint[0][0]);
            double d1 = (double)(aint[1][2] - aint[0][2]);
            double d2 = Math.sqrt(d0 * d0 + d1 * d1);
            d0 = d0 / d2;
            d1 = d1 / d2;
            x = x + d0 * offset;
            z = z + d1 * offset;

            if (aint[0][1] != 0 && MathHelper.floor(x) - i == aint[0][0] && MathHelper.floor(z) - k == aint[0][2])
            {
                y += (double)aint[0][1];
            }
            else if (aint[1][1] != 0 && MathHelper.floor(x) - i == aint[1][0] && MathHelper.floor(z) - k == aint[1][2])
            {
                y += (double)aint[1][1];
            }

            return this.getPos(x, y, z);
        }
        else
        {
            return null;
        }
    }

    @Nullable
    public Vec3d getPos(double p_70489_1_, double p_70489_3_, double p_70489_5_)
    {
        int i = MathHelper.floor(p_70489_1_);
        int j = MathHelper.floor(p_70489_3_);
        int k = MathHelper.floor(p_70489_5_);

        if (BlockRailBase.func_176562_d(this.world, new BlockPos(i, j - 1, k)))
        {
            --j;
        }

        IBlockState iblockstate = this.world.getBlockState(new BlockPos(i, j, k));

        if (BlockRailBase.func_176563_d(iblockstate))
        {
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)iblockstate.get(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
            int[][] aint = MATRIX[blockrailbase$enumraildirection.func_177015_a()];
            double d0 = (double)i + 0.5D + (double)aint[0][0] * 0.5D;
            double d1 = (double)j + 0.0625D + (double)aint[0][1] * 0.5D;
            double d2 = (double)k + 0.5D + (double)aint[0][2] * 0.5D;
            double d3 = (double)i + 0.5D + (double)aint[1][0] * 0.5D;
            double d4 = (double)j + 0.0625D + (double)aint[1][1] * 0.5D;
            double d5 = (double)k + 0.5D + (double)aint[1][2] * 0.5D;
            double d6 = d3 - d0;
            double d7 = (d4 - d1) * 2.0D;
            double d8 = d5 - d2;
            double d9;

            if (d6 == 0.0D)
            {
                d9 = p_70489_5_ - (double)k;
            }
            else if (d8 == 0.0D)
            {
                d9 = p_70489_1_ - (double)i;
            }
            else
            {
                double d10 = p_70489_1_ - d0;
                double d11 = p_70489_5_ - d2;
                d9 = (d10 * d6 + d11 * d8) * 2.0D;
            }

            p_70489_1_ = d0 + d6 * d9;
            p_70489_3_ = d1 + d7 * d9;
            p_70489_5_ = d2 + d8 * d9;

            if (d7 < 0.0D)
            {
                ++p_70489_3_;
            }

            if (d7 > 0.0D)
            {
                p_70489_3_ += 0.5D;
            }

            return new Vec3d(p_70489_1_, p_70489_3_, p_70489_5_);
        }
        else
        {
            return null;
        }
    }

    /**
     * Gets the bounding box of this Entity, adjusted to take auxiliary entities into account (e.g. the tile contained
     * by a minecart, such as a command block).
     */
    public AxisAlignedBB getRenderBoundingBox()
    {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        return this.hasDisplayTile() ? axisalignedbb.grow((double)Math.abs(this.getDisplayTileOffset()) / 16.0D) : axisalignedbb;
    }

    public static void func_189669_a(DataFixer p_189669_0_, Class<?> p_189669_1_)
    {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(NBTTagCompound compound)
    {
        if (compound.getBoolean("CustomDisplayTile"))
        {
            Block block;

            if (compound.contains("DisplayTile", 8))
            {
                block = Block.func_149684_b(compound.getString("DisplayTile"));
            }
            else
            {
                block = Block.func_149729_e(compound.getInt("DisplayTile"));
            }

            int i = compound.getInt("DisplayData");
            this.setDisplayTile(block == null ? Blocks.AIR.getDefaultState() : block.func_176203_a(i));
            this.setDisplayTileOffset(compound.getInt("DisplayOffset"));
        }
    }

    protected void func_70014_b(NBTTagCompound p_70014_1_)
    {
        if (this.hasDisplayTile())
        {
            p_70014_1_.putBoolean("CustomDisplayTile", true);
            IBlockState iblockstate = this.getDisplayTile();
            ResourceLocation resourcelocation = Block.field_149771_c.getKey(iblockstate.getBlock());
            p_70014_1_.putString("DisplayTile", resourcelocation == null ? "" : resourcelocation.toString());
            p_70014_1_.putInt("DisplayData", iblockstate.getBlock().func_176201_c(iblockstate));
            p_70014_1_.putInt("DisplayOffset", this.getDisplayTileOffset());
        }
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    public void applyEntityCollision(Entity entityIn)
    {
        if (!this.world.isRemote)
        {
            if (!entityIn.noClip && !this.noClip)
            {
                if (!this.isPassenger(entityIn))
                {
                    double d0 = entityIn.posX - this.posX;
                    double d1 = entityIn.posZ - this.posZ;
                    double d2 = d0 * d0 + d1 * d1;

                    if (d2 >= 9.999999747378752E-5D)
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
                        d0 = d0 * 0.10000000149011612D;
                        d1 = d1 * 0.10000000149011612D;
                        d0 = d0 * (double)(1.0F - this.entityCollisionReduction);
                        d1 = d1 * (double)(1.0F - this.entityCollisionReduction);
                        d0 = d0 * 0.5D;
                        d1 = d1 * 0.5D;

                        if (entityIn instanceof EntityMinecart)
                        {
                            double d4 = entityIn.posX - this.posX;
                            double d5 = entityIn.posZ - this.posZ;
                            Vec3d vec3d = (new Vec3d(d4, 0.0D, d5)).normalize();
                            Vec3d vec3d1 = (new Vec3d((double)MathHelper.cos(this.rotationYaw * 0.017453292F), 0.0D, (double)MathHelper.sin(this.rotationYaw * 0.017453292F))).normalize();
                            double d6 = Math.abs(vec3d.dotProduct(vec3d1));

                            if (d6 < 0.800000011920929D)
                            {
                                return;
                            }

                            double d7 = entityIn.field_70159_w + this.field_70159_w;
                            double d8 = entityIn.field_70179_y + this.field_70179_y;

                            if (((EntityMinecart)entityIn).getMinecartType() == EntityMinecart.Type.FURNACE && this.getMinecartType() != EntityMinecart.Type.FURNACE)
                            {
                                this.field_70159_w *= 0.20000000298023224D;
                                this.field_70179_y *= 0.20000000298023224D;
                                this.addVelocity(entityIn.field_70159_w - d0, 0.0D, entityIn.field_70179_y - d1);
                                entityIn.field_70159_w *= 0.949999988079071D;
                                entityIn.field_70179_y *= 0.949999988079071D;
                            }
                            else if (((EntityMinecart)entityIn).getMinecartType() != EntityMinecart.Type.FURNACE && this.getMinecartType() == EntityMinecart.Type.FURNACE)
                            {
                                entityIn.field_70159_w *= 0.20000000298023224D;
                                entityIn.field_70179_y *= 0.20000000298023224D;
                                entityIn.addVelocity(this.field_70159_w + d0, 0.0D, this.field_70179_y + d1);
                                this.field_70159_w *= 0.949999988079071D;
                                this.field_70179_y *= 0.949999988079071D;
                            }
                            else
                            {
                                d7 = d7 / 2.0D;
                                d8 = d8 / 2.0D;
                                this.field_70159_w *= 0.20000000298023224D;
                                this.field_70179_y *= 0.20000000298023224D;
                                this.addVelocity(d7 - d0, 0.0D, d8 - d1);
                                entityIn.field_70159_w *= 0.20000000298023224D;
                                entityIn.field_70179_y *= 0.20000000298023224D;
                                entityIn.addVelocity(d7 + d0, 0.0D, d8 + d1);
                            }
                        }
                        else
                        {
                            this.addVelocity(-d0, 0.0D, -d1);
                            entityIn.addVelocity(d0 / 4.0D, 0.0D, d1 / 4.0D);
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets a target for the client to interpolate towards over the next few ticks
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        this.minecartX = x;
        this.minecartY = y;
        this.minecartZ = z;
        this.minecartYaw = (double)yaw;
        this.minecartPitch = (double)pitch;
        this.turnProgress = posRotationIncrements + 2;
        this.field_70159_w = this.velocityX;
        this.field_70181_x = this.velocityY;
        this.field_70179_y = this.velocityZ;
    }

    /**
     * Updates the entity motion clientside, called by packets from the server
     */
    public void setVelocity(double x, double y, double z)
    {
        this.field_70159_w = x;
        this.field_70181_x = y;
        this.field_70179_y = z;
        this.velocityX = this.field_70159_w;
        this.velocityY = this.field_70181_x;
        this.velocityZ = this.field_70179_y;
    }

    /**
     * Sets the current amount of damage the minecart has taken. Decreases over time. The cart breaks when this is over
     * 40.
     */
    public void setDamage(float damage)
    {
        this.dataManager.set(DAMAGE, Float.valueOf(damage));
    }

    /**
     * Gets the current amount of damage the minecart has taken. Decreases over time. The cart breaks when this is over
     * 40.
     */
    public float getDamage()
    {
        return ((Float)this.dataManager.get(DAMAGE)).floatValue();
    }

    /**
     * Sets the rolling amplitude the cart rolls while being attacked.
     */
    public void setRollingAmplitude(int rollingAmplitude)
    {
        this.dataManager.set(ROLLING_AMPLITUDE, Integer.valueOf(rollingAmplitude));
    }

    /**
     * Gets the rolling amplitude the cart rolls while being attacked.
     */
    public int getRollingAmplitude()
    {
        return ((Integer)this.dataManager.get(ROLLING_AMPLITUDE)).intValue();
    }

    /**
     * Sets the rolling direction the cart rolls while being attacked. Can be 1 or -1.
     */
    public void setRollingDirection(int rollingDirection)
    {
        this.dataManager.set(ROLLING_DIRECTION, Integer.valueOf(rollingDirection));
    }

    /**
     * Gets the rolling direction the cart rolls while being attacked. Can be 1 or -1.
     */
    public int getRollingDirection()
    {
        return ((Integer)this.dataManager.get(ROLLING_DIRECTION)).intValue();
    }

    public abstract EntityMinecart.Type getMinecartType();

    public IBlockState getDisplayTile()
    {
        return !this.hasDisplayTile() ? this.getDefaultDisplayTile() : Block.func_176220_d(((Integer)this.getDataManager().get(DISPLAY_TILE)).intValue());
    }

    public IBlockState getDefaultDisplayTile()
    {
        return Blocks.AIR.getDefaultState();
    }

    public int getDisplayTileOffset()
    {
        return !this.hasDisplayTile() ? this.getDefaultDisplayTileOffset() : ((Integer)this.getDataManager().get(DISPLAY_TILE_OFFSET)).intValue();
    }

    public int getDefaultDisplayTileOffset()
    {
        return 6;
    }

    public void setDisplayTile(IBlockState displayTile)
    {
        this.getDataManager().set(DISPLAY_TILE, Integer.valueOf(Block.func_176210_f(displayTile)));
        this.setHasDisplayTile(true);
    }

    public void setDisplayTileOffset(int displayTileOffset)
    {
        this.getDataManager().set(DISPLAY_TILE_OFFSET, Integer.valueOf(displayTileOffset));
        this.setHasDisplayTile(true);
    }

    public boolean hasDisplayTile()
    {
        return ((Boolean)this.getDataManager().get(SHOW_BLOCK)).booleanValue();
    }

    public void setHasDisplayTile(boolean showBlock)
    {
        this.getDataManager().set(SHOW_BLOCK, Boolean.valueOf(showBlock));
    }

    public static enum Type
    {
        RIDEABLE(0, "MinecartRideable"),
        CHEST(1, "MinecartChest"),
        FURNACE(2, "MinecartFurnace"),
        TNT(3, "MinecartTNT"),
        SPAWNER(4, "MinecartSpawner"),
        HOPPER(5, "MinecartHopper"),
        COMMAND_BLOCK(6, "MinecartCommandBlock");

        private static final Map<Integer, EntityMinecart.Type> field_184965_h = Maps.<Integer, EntityMinecart.Type>newHashMap();
        private final int field_184966_i;
        private final String field_184967_j;

        private Type(int p_i47005_3_, String p_i47005_4_)
        {
            this.field_184966_i = p_i47005_3_;
            this.field_184967_j = p_i47005_4_;
        }

        public int func_184956_a()
        {
            return this.field_184966_i;
        }

        public String func_184954_b()
        {
            return this.field_184967_j;
        }

        public static EntityMinecart.Type func_184955_a(int p_184955_0_)
        {
            EntityMinecart.Type entityminecart$type = field_184965_h.get(Integer.valueOf(p_184955_0_));
            return entityminecart$type == null ? RIDEABLE : entityminecart$type;
        }

        static {
            for (EntityMinecart.Type entityminecart$type : values())
            {
                field_184965_h.put(Integer.valueOf(entityminecart$type.func_184956_a()), entityminecart$type);
            }
        }
    }
}

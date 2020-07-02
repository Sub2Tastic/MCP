package net.minecraft.entity.projectile;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityShulkerBullet extends Entity
{
    private EntityLivingBase owner;
    private Entity target;
    @Nullable
    private EnumFacing direction;
    private int steps;
    private double targetDeltaX;
    private double targetDeltaY;
    private double targetDeltaZ;
    @Nullable
    private UUID ownerUniqueId;
    private BlockPos ownerBlockPos;
    @Nullable
    private UUID targetUniqueId;
    private BlockPos targetBlockPos;

    public EntityShulkerBullet(World p_i46770_1_)
    {
        super(p_i46770_1_);
        this.func_70105_a(0.3125F, 0.3125F);
        this.noClip = true;
    }

    public SoundCategory getSoundCategory()
    {
        return SoundCategory.HOSTILE;
    }

    public EntityShulkerBullet(World worldIn, double x, double y, double z, double motionXIn, double motionYIn, double motionZIn)
    {
        this(worldIn);
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.field_70159_w = motionXIn;
        this.field_70181_x = motionYIn;
        this.field_70179_y = motionZIn;
    }

    public EntityShulkerBullet(World worldIn, EntityLivingBase ownerIn, Entity targetIn, EnumFacing.Axis p_i46772_4_)
    {
        this(worldIn);
        this.owner = ownerIn;
        BlockPos blockpos = new BlockPos(ownerIn);
        double d0 = (double)blockpos.getX() + 0.5D;
        double d1 = (double)blockpos.getY() + 0.5D;
        double d2 = (double)blockpos.getZ() + 0.5D;
        this.setLocationAndAngles(d0, d1, d2, this.rotationYaw, this.rotationPitch);
        this.target = targetIn;
        this.direction = EnumFacing.UP;
        this.selectNextMoveDirection(p_i46772_4_);
    }

    protected void func_70014_b(NBTTagCompound p_70014_1_)
    {
        if (this.owner != null)
        {
            BlockPos blockpos = new BlockPos(this.owner);
            NBTTagCompound nbttagcompound = NBTUtil.writeUniqueId(this.owner.getUniqueID());
            nbttagcompound.putInt("X", blockpos.getX());
            nbttagcompound.putInt("Y", blockpos.getY());
            nbttagcompound.putInt("Z", blockpos.getZ());
            p_70014_1_.func_74782_a("Owner", nbttagcompound);
        }

        if (this.target != null)
        {
            BlockPos blockpos1 = new BlockPos(this.target);
            NBTTagCompound nbttagcompound1 = NBTUtil.writeUniqueId(this.target.getUniqueID());
            nbttagcompound1.putInt("X", blockpos1.getX());
            nbttagcompound1.putInt("Y", blockpos1.getY());
            nbttagcompound1.putInt("Z", blockpos1.getZ());
            p_70014_1_.func_74782_a("Target", nbttagcompound1);
        }

        if (this.direction != null)
        {
            p_70014_1_.putInt("Dir", this.direction.getIndex());
        }

        p_70014_1_.putInt("Steps", this.steps);
        p_70014_1_.putDouble("TXD", this.targetDeltaX);
        p_70014_1_.putDouble("TYD", this.targetDeltaY);
        p_70014_1_.putDouble("TZD", this.targetDeltaZ);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(NBTTagCompound compound)
    {
        this.steps = compound.getInt("Steps");
        this.targetDeltaX = compound.getDouble("TXD");
        this.targetDeltaY = compound.getDouble("TYD");
        this.targetDeltaZ = compound.getDouble("TZD");

        if (compound.contains("Dir", 99))
        {
            this.direction = EnumFacing.byIndex(compound.getInt("Dir"));
        }

        if (compound.contains("Owner", 10))
        {
            NBTTagCompound nbttagcompound = compound.getCompound("Owner");
            this.ownerUniqueId = NBTUtil.readUniqueId(nbttagcompound);
            this.ownerBlockPos = new BlockPos(nbttagcompound.getInt("X"), nbttagcompound.getInt("Y"), nbttagcompound.getInt("Z"));
        }

        if (compound.contains("Target", 10))
        {
            NBTTagCompound nbttagcompound1 = compound.getCompound("Target");
            this.targetUniqueId = NBTUtil.readUniqueId(nbttagcompound1);
            this.targetBlockPos = new BlockPos(nbttagcompound1.getInt("X"), nbttagcompound1.getInt("Y"), nbttagcompound1.getInt("Z"));
        }
    }

    protected void registerData()
    {
    }

    private void setDirection(@Nullable EnumFacing directionIn)
    {
        this.direction = directionIn;
    }

    private void selectNextMoveDirection(@Nullable EnumFacing.Axis p_184569_1_)
    {
        double d0 = 0.5D;
        BlockPos blockpos;

        if (this.target == null)
        {
            blockpos = (new BlockPos(this)).down();
        }
        else
        {
            d0 = (double)this.target.field_70131_O * 0.5D;
            blockpos = new BlockPos(this.target.posX, this.target.posY + d0, this.target.posZ);
        }

        double d1 = (double)blockpos.getX() + 0.5D;
        double d2 = (double)blockpos.getY() + d0;
        double d3 = (double)blockpos.getZ() + 0.5D;
        EnumFacing enumfacing = null;

        if (blockpos.func_177957_d(this.posX, this.posY, this.posZ) >= 4.0D)
        {
            BlockPos blockpos1 = new BlockPos(this);
            List<EnumFacing> list = Lists.<EnumFacing>newArrayList();

            if (p_184569_1_ != EnumFacing.Axis.X)
            {
                if (blockpos1.getX() < blockpos.getX() && this.world.isAirBlock(blockpos1.east()))
                {
                    list.add(EnumFacing.EAST);
                }
                else if (blockpos1.getX() > blockpos.getX() && this.world.isAirBlock(blockpos1.west()))
                {
                    list.add(EnumFacing.WEST);
                }
            }

            if (p_184569_1_ != EnumFacing.Axis.Y)
            {
                if (blockpos1.getY() < blockpos.getY() && this.world.isAirBlock(blockpos1.up()))
                {
                    list.add(EnumFacing.UP);
                }
                else if (blockpos1.getY() > blockpos.getY() && this.world.isAirBlock(blockpos1.down()))
                {
                    list.add(EnumFacing.DOWN);
                }
            }

            if (p_184569_1_ != EnumFacing.Axis.Z)
            {
                if (blockpos1.getZ() < blockpos.getZ() && this.world.isAirBlock(blockpos1.south()))
                {
                    list.add(EnumFacing.SOUTH);
                }
                else if (blockpos1.getZ() > blockpos.getZ() && this.world.isAirBlock(blockpos1.north()))
                {
                    list.add(EnumFacing.NORTH);
                }
            }

            enumfacing = EnumFacing.random(this.rand);

            if (list.isEmpty())
            {
                for (int i = 5; !this.world.isAirBlock(blockpos1.offset(enumfacing)) && i > 0; --i)
                {
                    enumfacing = EnumFacing.random(this.rand);
                }
            }
            else
            {
                enumfacing = list.get(this.rand.nextInt(list.size()));
            }

            d1 = this.posX + (double)enumfacing.getXOffset();
            d2 = this.posY + (double)enumfacing.getYOffset();
            d3 = this.posZ + (double)enumfacing.getZOffset();
        }

        this.setDirection(enumfacing);
        double d6 = d1 - this.posX;
        double d7 = d2 - this.posY;
        double d4 = d3 - this.posZ;
        double d5 = (double)MathHelper.sqrt(d6 * d6 + d7 * d7 + d4 * d4);

        if (d5 == 0.0D)
        {
            this.targetDeltaX = 0.0D;
            this.targetDeltaY = 0.0D;
            this.targetDeltaZ = 0.0D;
        }
        else
        {
            this.targetDeltaX = d6 / d5 * 0.15D;
            this.targetDeltaY = d7 / d5 * 0.15D;
            this.targetDeltaZ = d4 / d5 * 0.15D;
        }

        this.isAirBorne = true;
        this.steps = 10 + this.rand.nextInt(5) * 10;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
        {
            this.remove();
        }
        else
        {
            super.tick();

            if (!this.world.isRemote)
            {
                if (this.target == null && this.targetUniqueId != null)
                {
                    for (EntityLivingBase entitylivingbase : this.world.func_72872_a(EntityLivingBase.class, new AxisAlignedBB(this.targetBlockPos.add(-2, -2, -2), this.targetBlockPos.add(2, 2, 2))))
                    {
                        if (entitylivingbase.getUniqueID().equals(this.targetUniqueId))
                        {
                            this.target = entitylivingbase;
                            break;
                        }
                    }

                    this.targetUniqueId = null;
                }

                if (this.owner == null && this.ownerUniqueId != null)
                {
                    for (EntityLivingBase entitylivingbase1 : this.world.func_72872_a(EntityLivingBase.class, new AxisAlignedBB(this.ownerBlockPos.add(-2, -2, -2), this.ownerBlockPos.add(2, 2, 2))))
                    {
                        if (entitylivingbase1.getUniqueID().equals(this.ownerUniqueId))
                        {
                            this.owner = entitylivingbase1;
                            break;
                        }
                    }

                    this.ownerUniqueId = null;
                }

                if (this.target == null || !this.target.isAlive() || this.target instanceof EntityPlayer && ((EntityPlayer)this.target).isSpectator())
                {
                    if (!this.hasNoGravity())
                    {
                        this.field_70181_x -= 0.04D;
                    }
                }
                else
                {
                    this.targetDeltaX = MathHelper.clamp(this.targetDeltaX * 1.025D, -1.0D, 1.0D);
                    this.targetDeltaY = MathHelper.clamp(this.targetDeltaY * 1.025D, -1.0D, 1.0D);
                    this.targetDeltaZ = MathHelper.clamp(this.targetDeltaZ * 1.025D, -1.0D, 1.0D);
                    this.field_70159_w += (this.targetDeltaX - this.field_70159_w) * 0.2D;
                    this.field_70181_x += (this.targetDeltaY - this.field_70181_x) * 0.2D;
                    this.field_70179_y += (this.targetDeltaZ - this.field_70179_y) * 0.2D;
                }

                RayTraceResult raytraceresult = ProjectileHelper.func_188802_a(this, true, false, this.owner);

                if (raytraceresult != null)
                {
                    this.bulletHit(raytraceresult);
                }
            }

            this.setPosition(this.posX + this.field_70159_w, this.posY + this.field_70181_x, this.posZ + this.field_70179_y);
            ProjectileHelper.rotateTowardsMovement(this, 0.5F);

            if (this.world.isRemote)
            {
                this.world.func_175688_a(EnumParticleTypes.END_ROD, this.posX - this.field_70159_w, this.posY - this.field_70181_x + 0.15D, this.posZ - this.field_70179_y, 0.0D, 0.0D, 0.0D);
            }
            else if (this.target != null && !this.target.removed)
            {
                if (this.steps > 0)
                {
                    --this.steps;

                    if (this.steps == 0)
                    {
                        this.selectNextMoveDirection(this.direction == null ? null : this.direction.getAxis());
                    }
                }

                if (this.direction != null)
                {
                    BlockPos blockpos = new BlockPos(this);
                    EnumFacing.Axis enumfacing$axis = this.direction.getAxis();

                    if (this.world.func_175677_d(blockpos.offset(this.direction), false))
                    {
                        this.selectNextMoveDirection(enumfacing$axis);
                    }
                    else
                    {
                        BlockPos blockpos1 = new BlockPos(this.target);

                        if (enumfacing$axis == EnumFacing.Axis.X && blockpos.getX() == blockpos1.getX() || enumfacing$axis == EnumFacing.Axis.Z && blockpos.getZ() == blockpos1.getZ() || enumfacing$axis == EnumFacing.Axis.Y && blockpos.getY() == blockpos1.getY())
                        {
                            this.selectNextMoveDirection(enumfacing$axis);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning()
    {
        return false;
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        return distance < 16384.0D;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness()
    {
        return 1.0F;
    }

    public int func_70070_b()
    {
        return 15728880;
    }

    protected void bulletHit(RayTraceResult result)
    {
        if (result.field_72308_g == null)
        {
            ((WorldServer)this.world).func_175739_a(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 2, 0.2D, 0.2D, 0.2D, 0.0D);
            this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.0F);
        }
        else
        {
            boolean flag = result.field_72308_g.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.owner).setProjectile(), 4.0F);

            if (flag)
            {
                this.applyEnchantments(this.owner, result.field_72308_g);

                if (result.field_72308_g instanceof EntityLivingBase)
                {
                    ((EntityLivingBase)result.field_72308_g).func_70690_d(new PotionEffect(MobEffects.LEVITATION, 200));
                }
            }
        }

        this.remove();
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!this.world.isRemote)
        {
            this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HURT, 1.0F, 1.0F);
            ((WorldServer)this.world).func_175739_a(EnumParticleTypes.CRIT, this.posX, this.posY, this.posZ, 15, 0.2D, 0.2D, 0.2D, 0.0D);
            this.remove();
        }

        return true;
    }
}

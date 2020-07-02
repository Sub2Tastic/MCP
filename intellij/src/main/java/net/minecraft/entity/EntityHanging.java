package net.minecraft.entity;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

public abstract class EntityHanging extends Entity
{
    private static final Predicate<Entity> IS_HANGING_ENTITY = new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_ instanceof EntityHanging;
        }
    };
    private int tickCounter1;
    protected BlockPos hangingPosition;
    @Nullable

    /** The direction the entity is facing */
    public EnumFacing facingDirection;

    public EntityHanging(World p_i1588_1_)
    {
        super(p_i1588_1_);
        this.func_70105_a(0.5F, 0.5F);
    }

    public EntityHanging(World p_i45853_1_, BlockPos p_i45853_2_)
    {
        this(p_i45853_1_);
        this.hangingPosition = p_i45853_2_;
    }

    protected void registerData()
    {
    }

    /**
     * Updates facing and bounding box based on it
     */
    protected void updateFacingWithBoundingBox(EnumFacing facingDirectionIn)
    {
        Validate.notNull(facingDirectionIn);
        Validate.isTrue(facingDirectionIn.getAxis().isHorizontal());
        this.facingDirection = facingDirectionIn;
        this.rotationYaw = (float)(this.facingDirection.getHorizontalIndex() * 90);
        this.prevRotationYaw = this.rotationYaw;
        this.updateBoundingBox();
    }

    /**
     * Updates the entity bounding box based on current facing
     */
    protected void updateBoundingBox()
    {
        if (this.facingDirection != null)
        {
            double d0 = (double)this.hangingPosition.getX() + 0.5D;
            double d1 = (double)this.hangingPosition.getY() + 0.5D;
            double d2 = (double)this.hangingPosition.getZ() + 0.5D;
            double d3 = 0.46875D;
            double d4 = this.offs(this.getWidthPixels());
            double d5 = this.offs(this.getHeightPixels());
            d0 = d0 - (double)this.facingDirection.getXOffset() * 0.46875D;
            d2 = d2 - (double)this.facingDirection.getZOffset() * 0.46875D;
            d1 = d1 + d5;
            EnumFacing enumfacing = this.facingDirection.rotateYCCW();
            d0 = d0 + d4 * (double)enumfacing.getXOffset();
            d2 = d2 + d4 * (double)enumfacing.getZOffset();
            this.posX = d0;
            this.posY = d1;
            this.posZ = d2;
            double d6 = (double)this.getWidthPixels();
            double d7 = (double)this.getHeightPixels();
            double d8 = (double)this.getWidthPixels();

            if (this.facingDirection.getAxis() == EnumFacing.Axis.Z)
            {
                d8 = 1.0D;
            }
            else
            {
                d6 = 1.0D;
            }

            d6 = d6 / 32.0D;
            d7 = d7 / 32.0D;
            d8 = d8 / 32.0D;
            this.setBoundingBox(new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8));
        }
    }

    private double offs(int p_190202_1_)
    {
        return p_190202_1_ % 32 == 0 ? 0.5D : 0.0D;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.tickCounter1++ == 100 && !this.world.isRemote)
        {
            this.tickCounter1 = 0;

            if (!this.removed && !this.onValidSurface())
            {
                this.remove();
                this.onBroken((Entity)null);
            }
        }
    }

    /**
     * checks to make sure painting can be placed there
     */
    public boolean onValidSurface()
    {
        if (!this.world.func_184144_a(this, this.getBoundingBox()).isEmpty())
        {
            return false;
        }
        else
        {
            int i = Math.max(1, this.getWidthPixels() / 16);
            int j = Math.max(1, this.getHeightPixels() / 16);
            BlockPos blockpos = this.hangingPosition.offset(this.facingDirection.getOpposite());
            EnumFacing enumfacing = this.facingDirection.rotateYCCW();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k = 0; k < i; ++k)
            {
                for (int l = 0; l < j; ++l)
                {
                    int i1 = (i - 1) / -2;
                    int j1 = (j - 1) / -2;
                    blockpos$mutableblockpos.setPos(blockpos).move(enumfacing, k + i1).move(EnumFacing.UP, l + j1);
                    IBlockState iblockstate = this.world.getBlockState(blockpos$mutableblockpos);

                    if (!iblockstate.getMaterial().isSolid() && !BlockRedstoneDiode.isDiode(iblockstate))
                    {
                        return false;
                    }
                }
            }

            return this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox(), IS_HANGING_ENTITY).isEmpty();
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * Called when a player attacks an entity. If this returns true the attack will not happen.
     */
    public boolean hitByEntity(Entity entityIn)
    {
        return entityIn instanceof EntityPlayer ? this.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entityIn), 0.0F) : false;
    }

    /**
     * Gets the horizontal facing direction of this Entity.
     */
    public EnumFacing getHorizontalFacing()
    {
        return this.facingDirection;
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
            if (!this.removed && !this.world.isRemote)
            {
                this.remove();
                this.markVelocityChanged();
                this.onBroken(source.getTrueSource());
            }

            return true;
        }
    }

    public void func_70091_d(MoverType p_70091_1_, double p_70091_2_, double p_70091_4_, double p_70091_6_)
    {
        if (!this.world.isRemote && !this.removed && p_70091_2_ * p_70091_2_ + p_70091_4_ * p_70091_4_ + p_70091_6_ * p_70091_6_ > 0.0D)
        {
            this.remove();
            this.onBroken((Entity)null);
        }
    }

    /**
     * Adds to the current velocity of the entity, and sets {@link #isAirBorne} to true.
     */
    public void addVelocity(double x, double y, double z)
    {
        if (!this.world.isRemote && !this.removed && x * x + y * y + z * z > 0.0D)
        {
            this.remove();
            this.onBroken((Entity)null);
        }
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.putByte("Facing", (byte)this.facingDirection.getHorizontalIndex());
        BlockPos blockpos = this.getHangingPosition();
        p_70014_1_.putInt("TileX", blockpos.getX());
        p_70014_1_.putInt("TileY", blockpos.getY());
        p_70014_1_.putInt("TileZ", blockpos.getZ());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        this.hangingPosition = new BlockPos(compound.getInt("TileX"), compound.getInt("TileY"), compound.getInt("TileZ"));
        this.updateFacingWithBoundingBox(EnumFacing.byHorizontalIndex(compound.getByte("Facing")));
    }

    public abstract int getWidthPixels();

    public abstract int getHeightPixels();

    /**
     * Called when this entity is broken. Entity parameter may be null.
     */
    public abstract void onBroken(Entity brokenEntity);

    public abstract void playPlaceSound();

    /**
     * Drops an item at the position of the entity.
     */
    public EntityItem entityDropItem(ItemStack stack, float offsetY)
    {
        EntityItem entityitem = new EntityItem(this.world, this.posX + (double)((float)this.facingDirection.getXOffset() * 0.15F), this.posY + (double)offsetY, this.posZ + (double)((float)this.facingDirection.getZOffset() * 0.15F), stack);
        entityitem.setDefaultPickupDelay();
        this.world.addEntity0(entityitem);
        return entityitem;
    }

    protected boolean shouldSetPosAfterLoading()
    {
        return false;
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    public void setPosition(double x, double y, double z)
    {
        this.hangingPosition = new BlockPos(x, y, z);
        this.updateBoundingBox();
        this.isAirBorne = true;
    }

    public BlockPos getHangingPosition()
    {
        return this.hangingPosition;
    }

    @SuppressWarnings("incomplete-switch")

    /**
     * Transforms the entity's current yaw with the given Rotation and returns it. This does not have a side-effect.
     */
    public float getRotatedYaw(Rotation transformRotation)
    {
        if (this.facingDirection != null && this.facingDirection.getAxis() != EnumFacing.Axis.Y)
        {
            switch (transformRotation)
            {
                case CLOCKWISE_180:
                    this.facingDirection = this.facingDirection.getOpposite();
                    break;

                case COUNTERCLOCKWISE_90:
                    this.facingDirection = this.facingDirection.rotateYCCW();
                    break;

                case CLOCKWISE_90:
                    this.facingDirection = this.facingDirection.rotateY();
            }
        }

        float f = MathHelper.wrapDegrees(this.rotationYaw);

        switch (transformRotation)
        {
            case CLOCKWISE_180:
                return f + 180.0F;

            case COUNTERCLOCKWISE_90:
                return f + 90.0F;

            case CLOCKWISE_90:
                return f + 270.0F;

            default:
                return f;
        }
    }

    /**
     * Transforms the entity's current yaw with the given Mirror and returns it. This does not have a side-effect.
     */
    public float getMirroredYaw(Mirror transformMirror)
    {
        return this.getRotatedYaw(transformMirror.toRotation(this.facingDirection));
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {
    }
}
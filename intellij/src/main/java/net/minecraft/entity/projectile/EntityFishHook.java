package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityFishHook extends Entity
{
    private static final DataParameter<Integer> DATA_HOOKED_ENTITY = EntityDataManager.<Integer>createKey(EntityFishHook.class, DataSerializers.VARINT);
    private boolean inGround;
    private int ticksInGround;
    private EntityPlayer angler;
    private int ticksInAir;
    private int ticksCatchable;
    private int ticksCaughtDelay;
    private int ticksCatchableDelay;
    private float fishApproachAngle;
    public Entity caughtEntity;
    private EntityFishHook.State currentState = EntityFishHook.State.FLYING;
    private int luck;
    private int lureSpeed;

    public EntityFishHook(World worldIn, EntityPlayer p_i47290_2_, double x, double y, double z)
    {
        super(worldIn);
        this.func_190626_a(p_i47290_2_);
        this.setPosition(x, y, z);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }

    public EntityFishHook(World p_i1766_1_, EntityPlayer p_i1766_2_)
    {
        super(p_i1766_1_);
        this.func_190626_a(p_i1766_2_);
        this.func_190620_n();
    }

    private void func_190626_a(EntityPlayer p_190626_1_)
    {
        this.func_70105_a(0.25F, 0.25F);
        this.ignoreFrustumCheck = true;
        this.angler = p_190626_1_;
        this.angler.fishingBobber = this;
    }

    public void func_191516_a(int p_191516_1_)
    {
        this.lureSpeed = p_191516_1_;
    }

    public void func_191517_b(int p_191517_1_)
    {
        this.luck = p_191517_1_;
    }

    private void func_190620_n()
    {
        float f = this.angler.prevRotationPitch + (this.angler.rotationPitch - this.angler.prevRotationPitch);
        float f1 = this.angler.prevRotationYaw + (this.angler.rotationYaw - this.angler.prevRotationYaw);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        double d0 = this.angler.prevPosX + (this.angler.posX - this.angler.prevPosX) - (double)f3 * 0.3D;
        double d1 = this.angler.prevPosY + (this.angler.posY - this.angler.prevPosY) + (double)this.angler.getEyeHeight();
        double d2 = this.angler.prevPosZ + (this.angler.posZ - this.angler.prevPosZ) - (double)f2 * 0.3D;
        this.setLocationAndAngles(d0, d1, d2, f1, f);
        this.field_70159_w = (double)(-f3);
        this.field_70181_x = (double)MathHelper.clamp(-(f5 / f4), -5.0F, 5.0F);
        this.field_70179_y = (double)(-f2);
        float f6 = MathHelper.sqrt(this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y);
        this.field_70159_w *= 0.6D / (double)f6 + 0.5D + this.rand.nextGaussian() * 0.0045D;
        this.field_70181_x *= 0.6D / (double)f6 + 0.5D + this.rand.nextGaussian() * 0.0045D;
        this.field_70179_y *= 0.6D / (double)f6 + 0.5D + this.rand.nextGaussian() * 0.0045D;
        float f7 = MathHelper.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
        this.rotationYaw = (float)(MathHelper.atan2(this.field_70159_w, this.field_70179_y) * (180D / Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(this.field_70181_x, (double)f7) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    protected void registerData()
    {
        this.getDataManager().register(DATA_HOOKED_ENTITY, Integer.valueOf(0));
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (DATA_HOOKED_ENTITY.equals(key))
        {
            int i = ((Integer)this.getDataManager().get(DATA_HOOKED_ENTITY)).intValue();
            this.caughtEntity = i > 0 ? this.world.getEntityByID(i - 1) : null;
        }

        super.notifyDataManagerChange(key);
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = 64.0D;
        return distance < 4096.0D;
    }

    /**
     * Sets a target for the client to interpolate towards over the next few ticks
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        super.tick();

        if (this.angler == null)
        {
            this.remove();
        }
        else if (this.world.isRemote || !this.shouldStopFishing())
        {
            if (this.inGround)
            {
                ++this.ticksInGround;

                if (this.ticksInGround >= 1200)
                {
                    this.remove();
                    return;
                }
            }

            float f = 0.0F;
            BlockPos blockpos = new BlockPos(this);
            IBlockState iblockstate = this.world.getBlockState(blockpos);

            if (iblockstate.getMaterial() == Material.WATER)
            {
                f = BlockLiquid.func_190973_f(iblockstate, this.world, blockpos);
            }

            if (this.currentState == EntityFishHook.State.FLYING)
            {
                if (this.caughtEntity != null)
                {
                    this.field_70159_w = 0.0D;
                    this.field_70181_x = 0.0D;
                    this.field_70179_y = 0.0D;
                    this.currentState = EntityFishHook.State.HOOKED_IN_ENTITY;
                    return;
                }

                if (f > 0.0F)
                {
                    this.field_70159_w *= 0.3D;
                    this.field_70181_x *= 0.2D;
                    this.field_70179_y *= 0.3D;
                    this.currentState = EntityFishHook.State.BOBBING;
                    return;
                }

                if (!this.world.isRemote)
                {
                    this.checkCollision();
                }

                if (!this.inGround && !this.onGround && !this.collidedHorizontally)
                {
                    ++this.ticksInAir;
                }
                else
                {
                    this.ticksInAir = 0;
                    this.field_70159_w = 0.0D;
                    this.field_70181_x = 0.0D;
                    this.field_70179_y = 0.0D;
                }
            }
            else
            {
                if (this.currentState == EntityFishHook.State.HOOKED_IN_ENTITY)
                {
                    if (this.caughtEntity != null)
                    {
                        if (this.caughtEntity.removed)
                        {
                            this.caughtEntity = null;
                            this.currentState = EntityFishHook.State.FLYING;
                        }
                        else
                        {
                            this.posX = this.caughtEntity.posX;
                            double d2 = (double)this.caughtEntity.field_70131_O;
                            this.posY = this.caughtEntity.getBoundingBox().minY + d2 * 0.8D;
                            this.posZ = this.caughtEntity.posZ;
                            this.setPosition(this.posX, this.posY, this.posZ);
                        }
                    }

                    return;
                }

                if (this.currentState == EntityFishHook.State.BOBBING)
                {
                    this.field_70159_w *= 0.9D;
                    this.field_70179_y *= 0.9D;
                    double d0 = this.posY + this.field_70181_x - (double)blockpos.getY() - (double)f;

                    if (Math.abs(d0) < 0.01D)
                    {
                        d0 += Math.signum(d0) * 0.1D;
                    }

                    this.field_70181_x -= d0 * (double)this.rand.nextFloat() * 0.2D;

                    if (!this.world.isRemote && f > 0.0F)
                    {
                        this.catchingFish(blockpos);
                    }
                }
            }

            if (iblockstate.getMaterial() != Material.WATER)
            {
                this.field_70181_x -= 0.03D;
            }

            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.updateRotation();
            double d1 = 0.92D;
            this.field_70159_w *= 0.92D;
            this.field_70181_x *= 0.92D;
            this.field_70179_y *= 0.92D;
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    private boolean shouldStopFishing()
    {
        ItemStack itemstack = this.angler.getHeldItemMainhand();
        ItemStack itemstack1 = this.angler.getHeldItemOffhand();
        boolean flag = itemstack.getItem() == Items.FISHING_ROD;
        boolean flag1 = itemstack1.getItem() == Items.FISHING_ROD;

        if (!this.angler.removed && this.angler.isAlive() && (flag || flag1) && this.getDistanceSq(this.angler) <= 1024.0D)
        {
            return false;
        }
        else
        {
            this.remove();
            return true;
        }
    }

    private void updateRotation()
    {
        float f = MathHelper.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
        this.rotationYaw = (float)(MathHelper.atan2(this.field_70159_w, this.field_70179_y) * (180D / Math.PI));

        for (this.rotationPitch = (float)(MathHelper.atan2(this.field_70181_x, (double)f) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
    }

    private void checkCollision()
    {
        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d1 = new Vec3d(this.posX + this.field_70159_w, this.posY + this.field_70181_x, this.posZ + this.field_70179_y);
        RayTraceResult raytraceresult = this.world.func_147447_a(vec3d, vec3d1, false, true, false);
        vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d1 = new Vec3d(this.posX + this.field_70159_w, this.posY + this.field_70181_x, this.posZ + this.field_70179_y);

        if (raytraceresult != null)
        {
            vec3d1 = new Vec3d(raytraceresult.hitResult.x, raytraceresult.hitResult.y, raytraceresult.hitResult.z);
        }

        Entity entity = null;
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(this.field_70159_w, this.field_70181_x, this.field_70179_y).grow(1.0D));
        double d0 = 0.0D;

        for (Entity entity1 : list)
        {
            if (this.func_189739_a(entity1) && (entity1 != this.angler || this.ticksInAir >= 5))
            {
                AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(0.30000001192092896D);
                RayTraceResult raytraceresult1 = axisalignedbb.func_72327_a(vec3d, vec3d1);

                if (raytraceresult1 != null)
                {
                    double d1 = vec3d.squareDistanceTo(raytraceresult1.hitResult);

                    if (d1 < d0 || d0 == 0.0D)
                    {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        if (entity != null)
        {
            raytraceresult = new RayTraceResult(entity);
        }

        if (raytraceresult != null && raytraceresult.field_72313_a != RayTraceResult.Type.MISS)
        {
            if (raytraceresult.field_72313_a == RayTraceResult.Type.ENTITY)
            {
                this.caughtEntity = raytraceresult.field_72308_g;
                this.setHookedEntity();
            }
            else
            {
                this.inGround = true;
            }
        }
    }

    private void setHookedEntity()
    {
        this.getDataManager().set(DATA_HOOKED_ENTITY, Integer.valueOf(this.caughtEntity.getEntityId() + 1));
    }

    private void catchingFish(BlockPos p_190621_1_)
    {
        WorldServer worldserver = (WorldServer)this.world;
        int i = 1;
        BlockPos blockpos = p_190621_1_.up();

        if (this.rand.nextFloat() < 0.25F && this.world.isRainingAt(blockpos))
        {
            ++i;
        }

        if (this.rand.nextFloat() < 0.5F && !this.world.func_175678_i(blockpos))
        {
            --i;
        }

        if (this.ticksCatchable > 0)
        {
            --this.ticksCatchable;

            if (this.ticksCatchable <= 0)
            {
                this.ticksCaughtDelay = 0;
                this.ticksCatchableDelay = 0;
            }
            else
            {
                this.field_70181_x -= 0.2D * (double)this.rand.nextFloat() * (double)this.rand.nextFloat();
            }
        }
        else if (this.ticksCatchableDelay > 0)
        {
            this.ticksCatchableDelay -= i;

            if (this.ticksCatchableDelay > 0)
            {
                this.fishApproachAngle = (float)((double)this.fishApproachAngle + this.rand.nextGaussian() * 4.0D);
                float f = this.fishApproachAngle * 0.017453292F;
                float f1 = MathHelper.sin(f);
                float f2 = MathHelper.cos(f);
                double d0 = this.posX + (double)(f1 * (float)this.ticksCatchableDelay * 0.1F);
                double d1 = (double)((float)MathHelper.floor(this.getBoundingBox().minY) + 1.0F);
                double d2 = this.posZ + (double)(f2 * (float)this.ticksCatchableDelay * 0.1F);
                Block block = worldserver.getBlockState(new BlockPos(d0, d1 - 1.0D, d2)).getBlock();

                if (block == Blocks.WATER || block == Blocks.field_150358_i)
                {
                    if (this.rand.nextFloat() < 0.15F)
                    {
                        worldserver.func_175739_a(EnumParticleTypes.WATER_BUBBLE, d0, d1 - 0.10000000149011612D, d2, 1, (double)f1, 0.1D, (double)f2, 0.0D);
                    }

                    float f3 = f1 * 0.04F;
                    float f4 = f2 * 0.04F;
                    worldserver.func_175739_a(EnumParticleTypes.WATER_WAKE, d0, d1, d2, 0, (double)f4, 0.01D, (double)(-f3), 1.0D);
                    worldserver.func_175739_a(EnumParticleTypes.WATER_WAKE, d0, d1, d2, 0, (double)(-f4), 0.01D, (double)f3, 1.0D);
                }
            }
            else
            {
                this.field_70181_x = (double)(-0.4F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F));
                this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                double d3 = this.getBoundingBox().minY + 0.5D;
                worldserver.func_175739_a(EnumParticleTypes.WATER_BUBBLE, this.posX, d3, this.posZ, (int)(1.0F + this.field_70130_N * 20.0F), (double)this.field_70130_N, 0.0D, (double)this.field_70130_N, 0.20000000298023224D);
                worldserver.func_175739_a(EnumParticleTypes.WATER_WAKE, this.posX, d3, this.posZ, (int)(1.0F + this.field_70130_N * 20.0F), (double)this.field_70130_N, 0.0D, (double)this.field_70130_N, 0.20000000298023224D);
                this.ticksCatchable = MathHelper.nextInt(this.rand, 20, 40);
            }
        }
        else if (this.ticksCaughtDelay > 0)
        {
            this.ticksCaughtDelay -= i;
            float f5 = 0.15F;

            if (this.ticksCaughtDelay < 20)
            {
                f5 = (float)((double)f5 + (double)(20 - this.ticksCaughtDelay) * 0.05D);
            }
            else if (this.ticksCaughtDelay < 40)
            {
                f5 = (float)((double)f5 + (double)(40 - this.ticksCaughtDelay) * 0.02D);
            }
            else if (this.ticksCaughtDelay < 60)
            {
                f5 = (float)((double)f5 + (double)(60 - this.ticksCaughtDelay) * 0.01D);
            }

            if (this.rand.nextFloat() < f5)
            {
                float f6 = MathHelper.nextFloat(this.rand, 0.0F, 360.0F) * 0.017453292F;
                float f7 = MathHelper.nextFloat(this.rand, 25.0F, 60.0F);
                double d4 = this.posX + (double)(MathHelper.sin(f6) * f7 * 0.1F);
                double d5 = (double)((float)MathHelper.floor(this.getBoundingBox().minY) + 1.0F);
                double d6 = this.posZ + (double)(MathHelper.cos(f6) * f7 * 0.1F);
                Block block1 = worldserver.getBlockState(new BlockPos((int)d4, (int)d5 - 1, (int)d6)).getBlock();

                if (block1 == Blocks.WATER || block1 == Blocks.field_150358_i)
                {
                    worldserver.func_175739_a(EnumParticleTypes.WATER_SPLASH, d4, d5, d6, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                }
            }

            if (this.ticksCaughtDelay <= 0)
            {
                this.fishApproachAngle = MathHelper.nextFloat(this.rand, 0.0F, 360.0F);
                this.ticksCatchableDelay = MathHelper.nextInt(this.rand, 20, 80);
            }
        }
        else
        {
            this.ticksCaughtDelay = MathHelper.nextInt(this.rand, 100, 600);
            this.ticksCaughtDelay -= this.lureSpeed * 20 * 5;
        }
    }

    protected boolean func_189739_a(Entity p_189739_1_)
    {
        return p_189739_1_.canBeCollidedWith() || p_189739_1_ instanceof EntityItem;
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
    }

    public int handleHookRetraction()
    {
        if (!this.world.isRemote && this.angler != null)
        {
            int i = 0;

            if (this.caughtEntity != null)
            {
                this.bringInHookedEntity();
                this.world.setEntityState(this, (byte)31);
                i = this.caughtEntity instanceof EntityItem ? 3 : 5;
            }
            else if (this.ticksCatchable > 0)
            {
                LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.world);
                lootcontext$builder.withLuck((float)this.luck + this.angler.getLuck());

                for (ItemStack itemstack : this.world.func_184146_ak().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING).func_186462_a(this.rand, lootcontext$builder.func_186471_a()))
                {
                    EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY, this.posZ, itemstack);
                    double d0 = this.angler.posX - this.posX;
                    double d1 = this.angler.posY - this.posY;
                    double d2 = this.angler.posZ - this.posZ;
                    double d3 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    double d4 = 0.1D;
                    entityitem.field_70159_w = d0 * 0.1D;
                    entityitem.field_70181_x = d1 * 0.1D + (double)MathHelper.sqrt(d3) * 0.08D;
                    entityitem.field_70179_y = d2 * 0.1D;
                    this.world.addEntity0(entityitem);
                    this.angler.world.addEntity0(new EntityXPOrb(this.angler.world, this.angler.posX, this.angler.posY + 0.5D, this.angler.posZ + 0.5D, this.rand.nextInt(6) + 1));
                    Item item = itemstack.getItem();

                    if (item == Items.field_151115_aP || item == Items.field_179566_aV)
                    {
                        this.angler.addStat(StatList.FISH_CAUGHT, 1);
                    }
                }

                i = 1;
            }

            if (this.inGround)
            {
                i = 2;
            }

            this.remove();
            return i;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    public void handleStatusUpdate(byte id)
    {
        if (id == 31 && this.world.isRemote && this.caughtEntity instanceof EntityPlayer && ((EntityPlayer)this.caughtEntity).isUser())
        {
            this.bringInHookedEntity();
        }

        super.handleStatusUpdate(id);
    }

    protected void bringInHookedEntity()
    {
        if (this.angler != null)
        {
            double d0 = this.angler.posX - this.posX;
            double d1 = this.angler.posY - this.posY;
            double d2 = this.angler.posZ - this.posZ;
            double d3 = 0.1D;
            this.caughtEntity.field_70159_w += d0 * 0.1D;
            this.caughtEntity.field_70181_x += d1 * 0.1D;
            this.caughtEntity.field_70179_y += d2 * 0.1D;
        }
    }

    protected boolean func_70041_e_()
    {
        return false;
    }

    /**
     * Queues the entity for removal from the world on the next tick.
     */
    public void remove()
    {
        super.remove();

        if (this.angler != null)
        {
            this.angler.fishingBobber = null;
        }
    }

    public EntityPlayer getAngler()
    {
        return this.angler;
    }

    static enum State
    {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING;
    }
}

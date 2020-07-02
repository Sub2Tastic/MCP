package net.minecraft.entity.monster;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityShulker extends EntityGolem implements IMob
{
    private static final UUID COVERED_ARMOR_BONUS_ID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
    private static final AttributeModifier COVERED_ARMOR_BONUS_MODIFIER = (new AttributeModifier(COVERED_ARMOR_BONUS_ID, "Covered armor bonus", 20.0D, 0)).setSaved(false);
    protected static final DataParameter<EnumFacing> ATTACHED_FACE = EntityDataManager.<EnumFacing>createKey(EntityShulker.class, DataSerializers.DIRECTION);
    protected static final DataParameter<Optional<BlockPos>> ATTACHED_BLOCK_POS = EntityDataManager.<Optional<BlockPos>>createKey(EntityShulker.class, DataSerializers.OPTIONAL_BLOCK_POS);
    protected static final DataParameter<Byte> PEEK_TICK = EntityDataManager.<Byte>createKey(EntityShulker.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> COLOR = EntityDataManager.<Byte>createKey(EntityShulker.class, DataSerializers.BYTE);
    public static final EnumDyeColor field_190771_bx = EnumDyeColor.PURPLE;
    private float prevPeekAmount;
    private float peekAmount;
    private BlockPos currentAttachmentPosition;
    private int clientSideTeleportInterpolation;

    public EntityShulker(World p_i46779_1_)
    {
        super(p_i46779_1_);
        this.func_70105_a(1.0F, 1.0F);
        this.prevRenderYawOffset = 180.0F;
        this.renderYawOffset = 180.0F;
        this.field_70178_ae = true;
        this.currentAttachmentPosition = null;
        this.experienceValue = 5;
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, @Nullable IEntityLivingData p_180482_2_)
    {
        this.renderYawOffset = 180.0F;
        this.prevRenderYawOffset = 180.0F;
        this.rotationYaw = 180.0F;
        this.prevRotationYaw = 180.0F;
        this.rotationYawHead = 180.0F;
        this.prevRotationYawHead = 180.0F;
        return super.func_180482_a(p_180482_1_, p_180482_2_);
    }

    protected void registerGoals()
    {
        this.goalSelector.addGoal(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.goalSelector.addGoal(4, new EntityShulker.AIAttack());
        this.goalSelector.addGoal(7, new EntityShulker.AIPeek());
        this.goalSelector.addGoal(8, new EntityAILookIdle(this));
        this.targetSelector.addGoal(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetSelector.addGoal(2, new EntityShulker.AIAttackNearest(this));
        this.targetSelector.addGoal(3, new EntityShulker.AIDefenseAttack(this));
    }

    protected boolean func_70041_e_()
    {
        return false;
    }

    public SoundCategory getSoundCategory()
    {
        return SoundCategory.HOSTILE;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SHULKER_AMBIENT;
    }

    /**
     * Plays living's sound at its position
     */
    public void playAmbientSound()
    {
        if (!this.isClosed())
        {
            super.playAmbientSound();
        }
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SHULKER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return this.isClosed() ? SoundEvents.ENTITY_SHULKER_HURT_CLOSED : SoundEvents.ENTITY_SHULKER_HURT;
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(ATTACHED_FACE, EnumFacing.DOWN);
        this.dataManager.register(ATTACHED_BLOCK_POS, Optional.absent());
        this.dataManager.register(PEEK_TICK, Byte.valueOf((byte)0));
        this.dataManager.register(COLOR, Byte.valueOf((byte)field_190771_bx.func_176765_a()));
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
    }

    protected EntityBodyHelper createBodyController()
    {
        return new EntityShulker.BodyHelper(this);
    }

    public static void func_189757_b(DataFixer p_189757_0_)
    {
        EntityLiving.func_189752_a(p_189757_0_, EntityShulker.class);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        this.dataManager.set(ATTACHED_FACE, EnumFacing.byIndex(compound.getByte("AttachFace")));
        this.dataManager.set(PEEK_TICK, Byte.valueOf(compound.getByte("Peek")));
        this.dataManager.set(COLOR, Byte.valueOf(compound.getByte("Color")));

        if (compound.contains("APX"))
        {
            int i = compound.getInt("APX");
            int j = compound.getInt("APY");
            int k = compound.getInt("APZ");
            this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(new BlockPos(i, j, k)));
        }
        else
        {
            this.dataManager.set(ATTACHED_BLOCK_POS, Optional.absent());
        }
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.putByte("AttachFace", (byte)((EnumFacing)this.dataManager.get(ATTACHED_FACE)).getIndex());
        p_70014_1_.putByte("Peek", ((Byte)this.dataManager.get(PEEK_TICK)).byteValue());
        p_70014_1_.putByte("Color", ((Byte)this.dataManager.get(COLOR)).byteValue());
        BlockPos blockpos = this.getAttachmentPos();

        if (blockpos != null)
        {
            p_70014_1_.putInt("APX", blockpos.getX());
            p_70014_1_.putInt("APY", blockpos.getY());
            p_70014_1_.putInt("APZ", blockpos.getZ());
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        super.tick();
        BlockPos blockpos = (BlockPos)((Optional)this.dataManager.get(ATTACHED_BLOCK_POS)).orNull();

        if (blockpos == null && !this.world.isRemote)
        {
            blockpos = new BlockPos(this);
            this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
        }

        if (this.isPassenger())
        {
            blockpos = null;
            float f = this.getRidingEntity().rotationYaw;
            this.rotationYaw = f;
            this.renderYawOffset = f;
            this.prevRenderYawOffset = f;
            this.clientSideTeleportInterpolation = 0;
        }
        else if (!this.world.isRemote)
        {
            IBlockState iblockstate = this.world.getBlockState(blockpos);

            if (iblockstate.getMaterial() != Material.AIR)
            {
                if (iblockstate.getBlock() == Blocks.field_180384_M)
                {
                    EnumFacing enumfacing = (EnumFacing)iblockstate.get(BlockPistonBase.FACING);

                    if (this.world.isAirBlock(blockpos.offset(enumfacing)))
                    {
                        blockpos = blockpos.offset(enumfacing);
                        this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
                    }
                    else
                    {
                        this.tryTeleportToNewPosition();
                    }
                }
                else if (iblockstate.getBlock() == Blocks.PISTON_HEAD)
                {
                    EnumFacing enumfacing3 = (EnumFacing)iblockstate.get(BlockPistonExtension.FACING);

                    if (this.world.isAirBlock(blockpos.offset(enumfacing3)))
                    {
                        blockpos = blockpos.offset(enumfacing3);
                        this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
                    }
                    else
                    {
                        this.tryTeleportToNewPosition();
                    }
                }
                else
                {
                    this.tryTeleportToNewPosition();
                }
            }

            BlockPos blockpos1 = blockpos.offset(this.getAttachmentFacing());

            if (!this.world.func_175677_d(blockpos1, false))
            {
                boolean flag = false;

                for (EnumFacing enumfacing1 : EnumFacing.values())
                {
                    blockpos1 = blockpos.offset(enumfacing1);

                    if (this.world.func_175677_d(blockpos1, false))
                    {
                        this.dataManager.set(ATTACHED_FACE, enumfacing1);
                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
                    this.tryTeleportToNewPosition();
                }
            }

            BlockPos blockpos2 = blockpos.offset(this.getAttachmentFacing().getOpposite());

            if (this.world.func_175677_d(blockpos2, false))
            {
                this.tryTeleportToNewPosition();
            }
        }

        float f1 = (float)this.getPeekTick() * 0.01F;
        this.prevPeekAmount = this.peekAmount;

        if (this.peekAmount > f1)
        {
            this.peekAmount = MathHelper.clamp(this.peekAmount - 0.05F, f1, 1.0F);
        }
        else if (this.peekAmount < f1)
        {
            this.peekAmount = MathHelper.clamp(this.peekAmount + 0.05F, 0.0F, f1);
        }

        if (blockpos != null)
        {
            if (this.world.isRemote)
            {
                if (this.clientSideTeleportInterpolation > 0 && this.currentAttachmentPosition != null)
                {
                    --this.clientSideTeleportInterpolation;
                }
                else
                {
                    this.currentAttachmentPosition = blockpos;
                }
            }

            this.posX = (double)blockpos.getX() + 0.5D;
            this.posY = (double)blockpos.getY();
            this.posZ = (double)blockpos.getZ() + 0.5D;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.lastTickPosX = this.posX;
            this.lastTickPosY = this.posY;
            this.lastTickPosZ = this.posZ;
            double d3 = 0.5D - (double)MathHelper.sin((0.5F + this.peekAmount) * (float)Math.PI) * 0.5D;
            double d4 = 0.5D - (double)MathHelper.sin((0.5F + this.prevPeekAmount) * (float)Math.PI) * 0.5D;
            double d5 = d3 - d4;
            double d0 = 0.0D;
            double d1 = 0.0D;
            double d2 = 0.0D;
            EnumFacing enumfacing2 = this.getAttachmentFacing();

            switch (enumfacing2)
            {
                case DOWN:
                    this.setBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D + d3, this.posZ + 0.5D));
                    d1 = d5;
                    break;

                case UP:
                    this.setBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY - d3, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D));
                    d1 = -d5;
                    break;

                case NORTH:
                    this.setBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D + d3));
                    d2 = d5;
                    break;

                case SOUTH:
                    this.setBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D - d3, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D));
                    d2 = -d5;
                    break;

                case WEST:
                    this.setBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D + d3, this.posY + 1.0D, this.posZ + 0.5D));
                    d0 = d5;
                    break;

                case EAST:
                    this.setBoundingBox(new AxisAlignedBB(this.posX - 0.5D - d3, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D));
                    d0 = -d5;
            }

            if (d5 > 0.0D)
            {
                List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox());

                if (!list.isEmpty())
                {
                    for (Entity entity : list)
                    {
                        if (!(entity instanceof EntityShulker) && !entity.noClip)
                        {
                            entity.func_70091_d(MoverType.SHULKER, d0, d1, d2);
                        }
                    }
                }
            }
        }
    }

    public void func_70091_d(MoverType p_70091_1_, double p_70091_2_, double p_70091_4_, double p_70091_6_)
    {
        if (p_70091_1_ == MoverType.SHULKER_BOX)
        {
            this.tryTeleportToNewPosition();
        }
        else
        {
            super.func_70091_d(p_70091_1_, p_70091_2_, p_70091_4_, p_70091_6_);
        }
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    public void setPosition(double x, double y, double z)
    {
        super.setPosition(x, y, z);

        if (this.dataManager != null && this.ticksExisted != 0)
        {
            Optional<BlockPos> optional = (Optional)this.dataManager.get(ATTACHED_BLOCK_POS);
            Optional<BlockPos> optional1 = Optional.<BlockPos>of(new BlockPos(x, y, z));

            if (!optional1.equals(optional))
            {
                this.dataManager.set(ATTACHED_BLOCK_POS, optional1);
                this.dataManager.set(PEEK_TICK, Byte.valueOf((byte)0));
                this.isAirBorne = true;
            }
        }
    }

    protected boolean tryTeleportToNewPosition()
    {
        if (!this.isAIDisabled() && this.isAlive())
        {
            BlockPos blockpos = new BlockPos(this);

            for (int i = 0; i < 5; ++i)
            {
                BlockPos blockpos1 = blockpos.add(8 - this.rand.nextInt(17), 8 - this.rand.nextInt(17), 8 - this.rand.nextInt(17));

                if (blockpos1.getY() > 0 && this.world.isAirBlock(blockpos1) && this.world.func_191503_g(this) && this.world.func_184144_a(this, new AxisAlignedBB(blockpos1)).isEmpty())
                {
                    boolean flag = false;

                    for (EnumFacing enumfacing : EnumFacing.values())
                    {
                        if (this.world.func_175677_d(blockpos1.offset(enumfacing), false))
                        {
                            this.dataManager.set(ATTACHED_FACE, enumfacing);
                            flag = true;
                            break;
                        }
                    }

                    if (flag)
                    {
                        this.playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1.0F, 1.0F);
                        this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos1));
                        this.dataManager.set(PEEK_TICK, Byte.valueOf((byte)0));
                        this.setAttackTarget((EntityLivingBase)null);
                        return true;
                    }
                }
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick()
    {
        super.livingTick();
        this.field_70159_w = 0.0D;
        this.field_70181_x = 0.0D;
        this.field_70179_y = 0.0D;
        this.prevRenderYawOffset = 180.0F;
        this.renderYawOffset = 180.0F;
        this.rotationYaw = 180.0F;
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (ATTACHED_BLOCK_POS.equals(key) && this.world.isRemote && !this.isPassenger())
        {
            BlockPos blockpos = this.getAttachmentPos();

            if (blockpos != null)
            {
                if (this.currentAttachmentPosition == null)
                {
                    this.currentAttachmentPosition = blockpos;
                }
                else
                {
                    this.clientSideTeleportInterpolation = 6;
                }

                this.posX = (double)blockpos.getX() + 0.5D;
                this.posY = (double)blockpos.getY();
                this.posZ = (double)blockpos.getZ() + 0.5D;
                this.prevPosX = this.posX;
                this.prevPosY = this.posY;
                this.prevPosZ = this.posZ;
                this.lastTickPosX = this.posX;
                this.lastTickPosY = this.posY;
                this.lastTickPosZ = this.posZ;
            }
        }

        super.notifyDataManagerChange(key);
    }

    /**
     * Sets a target for the client to interpolate towards over the next few ticks
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        this.newPosRotationIncrements = 0;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isClosed())
        {
            Entity entity = source.getImmediateSource();

            if (entity instanceof EntityArrow)
            {
                return false;
            }
        }

        if (super.attackEntityFrom(source, amount))
        {
            if ((double)this.getHealth() < (double)this.getMaxHealth() * 0.5D && this.rand.nextInt(4) == 0)
            {
                this.tryTeleportToNewPosition();
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isClosed()
    {
        return this.getPeekTick() == 0;
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
        return this.isAlive() ? this.getBoundingBox() : null;
    }

    public EnumFacing getAttachmentFacing()
    {
        return (EnumFacing)this.dataManager.get(ATTACHED_FACE);
    }

    @Nullable
    public BlockPos getAttachmentPos()
    {
        return (BlockPos)((Optional)this.dataManager.get(ATTACHED_BLOCK_POS)).orNull();
    }

    public void setAttachmentPos(@Nullable BlockPos pos)
    {
        this.dataManager.set(ATTACHED_BLOCK_POS, Optional.fromNullable(pos));
    }

    public int getPeekTick()
    {
        return ((Byte)this.dataManager.get(PEEK_TICK)).byteValue();
    }

    /**
     * Applies or removes armor modifier
     */
    public void updateArmorModifier(int p_184691_1_)
    {
        if (!this.world.isRemote)
        {
            this.getAttribute(SharedMonsterAttributes.ARMOR).removeModifier(COVERED_ARMOR_BONUS_MODIFIER);

            if (p_184691_1_ == 0)
            {
                this.getAttribute(SharedMonsterAttributes.ARMOR).applyModifier(COVERED_ARMOR_BONUS_MODIFIER);
                this.playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0F, 1.0F);
            }
            else
            {
                this.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0F, 1.0F);
            }
        }

        this.dataManager.set(PEEK_TICK, Byte.valueOf((byte)p_184691_1_));
    }

    public float getClientPeekAmount(float p_184688_1_)
    {
        return this.prevPeekAmount + (this.peekAmount - this.prevPeekAmount) * p_184688_1_;
    }

    public int getClientTeleportInterp()
    {
        return this.clientSideTeleportInterpolation;
    }

    public BlockPos getOldAttachPos()
    {
        return this.currentAttachmentPosition;
    }

    public float getEyeHeight()
    {
        return 0.5F;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return 180;
    }

    public int getHorizontalFaceSpeed()
    {
        return 180;
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    public void applyEntityCollision(Entity entityIn)
    {
    }

    public float getCollisionBorderSize()
    {
        return 0.0F;
    }

    public boolean isAttachedToBlock()
    {
        return this.currentAttachmentPosition != null && this.getAttachmentPos() != null;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_186442_x;
    }

    public EnumDyeColor getColor()
    {
        return EnumDyeColor.func_176764_b(((Byte)this.dataManager.get(COLOR)).byteValue());
    }

    class AIAttack extends EntityAIBase
    {
        private int attackTime;

        public AIAttack()
        {
            this.func_75248_a(3);
        }

        public boolean shouldExecute()
        {
            EntityLivingBase entitylivingbase = EntityShulker.this.getAttackTarget();

            if (entitylivingbase != null && entitylivingbase.isAlive())
            {
                return EntityShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
            }
            else
            {
                return false;
            }
        }

        public void startExecuting()
        {
            this.attackTime = 20;
            EntityShulker.this.updateArmorModifier(100);
        }

        public void resetTask()
        {
            EntityShulker.this.updateArmorModifier(0);
        }

        public void tick()
        {
            if (EntityShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL)
            {
                --this.attackTime;
                EntityLivingBase entitylivingbase = EntityShulker.this.getAttackTarget();
                EntityShulker.this.getLookController().setLookPositionWithEntity(entitylivingbase, 180.0F, 180.0F);
                double d0 = EntityShulker.this.getDistanceSq(entitylivingbase);

                if (d0 < 400.0D)
                {
                    if (this.attackTime <= 0)
                    {
                        this.attackTime = 20 + EntityShulker.this.rand.nextInt(10) * 20 / 2;
                        EntityShulkerBullet entityshulkerbullet = new EntityShulkerBullet(EntityShulker.this.world, EntityShulker.this, entitylivingbase, EntityShulker.this.getAttachmentFacing().getAxis());
                        EntityShulker.this.world.addEntity0(entityshulkerbullet);
                        EntityShulker.this.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 2.0F, (EntityShulker.this.rand.nextFloat() - EntityShulker.this.rand.nextFloat()) * 0.2F + 1.0F);
                    }
                }
                else
                {
                    EntityShulker.this.setAttackTarget((EntityLivingBase)null);
                }

                super.tick();
            }
        }
    }

    class AIAttackNearest extends EntityAINearestAttackableTarget<EntityPlayer>
    {
        public AIAttackNearest(EntityShulker shulker)
        {
            super(shulker, EntityPlayer.class, true);
        }

        public boolean shouldExecute()
        {
            return EntityShulker.this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? false : super.shouldExecute();
        }

        protected AxisAlignedBB getTargetableArea(double targetDistance)
        {
            EnumFacing enumfacing = ((EntityShulker)this.goalOwner).getAttachmentFacing();

            if (enumfacing.getAxis() == EnumFacing.Axis.X)
            {
                return this.goalOwner.getBoundingBox().grow(4.0D, targetDistance, targetDistance);
            }
            else
            {
                return enumfacing.getAxis() == EnumFacing.Axis.Z ? this.goalOwner.getBoundingBox().grow(targetDistance, targetDistance, 4.0D) : this.goalOwner.getBoundingBox().grow(targetDistance, 4.0D, targetDistance);
            }
        }
    }

    static class AIDefenseAttack extends EntityAINearestAttackableTarget<EntityLivingBase>
    {
        public AIDefenseAttack(EntityShulker shulker)
        {
            super(shulker, EntityLivingBase.class, 10, true, false, new Predicate<EntityLivingBase>()
            {
                public boolean apply(@Nullable EntityLivingBase p_apply_1_)
                {
                    return p_apply_1_ instanceof IMob;
                }
            });
        }

        public boolean shouldExecute()
        {
            return this.goalOwner.getTeam() == null ? false : super.shouldExecute();
        }

        protected AxisAlignedBB getTargetableArea(double targetDistance)
        {
            EnumFacing enumfacing = ((EntityShulker)this.goalOwner).getAttachmentFacing();

            if (enumfacing.getAxis() == EnumFacing.Axis.X)
            {
                return this.goalOwner.getBoundingBox().grow(4.0D, targetDistance, targetDistance);
            }
            else
            {
                return enumfacing.getAxis() == EnumFacing.Axis.Z ? this.goalOwner.getBoundingBox().grow(targetDistance, targetDistance, 4.0D) : this.goalOwner.getBoundingBox().grow(targetDistance, 4.0D, targetDistance);
            }
        }
    }

    class AIPeek extends EntityAIBase
    {
        private int peekTime;

        private AIPeek()
        {
        }

        public boolean shouldExecute()
        {
            return EntityShulker.this.getAttackTarget() == null && EntityShulker.this.rand.nextInt(40) == 0;
        }

        public boolean shouldContinueExecuting()
        {
            return EntityShulker.this.getAttackTarget() == null && this.peekTime > 0;
        }

        public void startExecuting()
        {
            this.peekTime = 20 * (1 + EntityShulker.this.rand.nextInt(3));
            EntityShulker.this.updateArmorModifier(30);
        }

        public void resetTask()
        {
            if (EntityShulker.this.getAttackTarget() == null)
            {
                EntityShulker.this.updateArmorModifier(0);
            }
        }

        public void tick()
        {
            --this.peekTime;
        }
    }

    class BodyHelper extends EntityBodyHelper
    {
        public BodyHelper(EntityLivingBase p_i47062_2_)
        {
            super(p_i47062_2_);
        }

        public void updateRenderAngles()
        {
        }
    }
}
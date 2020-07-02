package net.minecraft.entity.projectile;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityPotion extends EntityThrowable
{
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.<ItemStack>createKey(EntityPotion.class, DataSerializers.ITEMSTACK);
    private static final Logger LOGGER = LogManager.getLogger();
    public static final Predicate<EntityLivingBase> WATER_SENSITIVE = new Predicate<EntityLivingBase>()
    {
        public boolean apply(@Nullable EntityLivingBase p_apply_1_)
        {
            return EntityPotion.isWaterSensitiveEntity(p_apply_1_);
        }
    };

    public EntityPotion(World p_i1788_1_)
    {
        super(p_i1788_1_);
    }

    public EntityPotion(World p_i1790_1_, EntityLivingBase p_i1790_2_, ItemStack p_i1790_3_)
    {
        super(p_i1790_1_, p_i1790_2_);
        this.setItem(p_i1790_3_);
    }

    public EntityPotion(World p_i1792_1_, double p_i1792_2_, double p_i1792_4_, double p_i1792_6_, ItemStack p_i1792_8_)
    {
        super(p_i1792_1_, p_i1792_2_, p_i1792_4_, p_i1792_6_);

        if (!p_i1792_8_.isEmpty())
        {
            this.setItem(p_i1792_8_);
        }
    }

    protected void registerData()
    {
        this.getDataManager().register(ITEM, ItemStack.EMPTY);
    }

    public ItemStack getItem()
    {
        ItemStack itemstack = (ItemStack)this.getDataManager().get(ITEM);

        if (itemstack.getItem() != Items.SPLASH_POTION && itemstack.getItem() != Items.LINGERING_POTION)
        {
            if (this.world != null)
            {
                LOGGER.error("ThrownPotion entity {} has no item?!", (int)this.getEntityId());
            }

            return new ItemStack(Items.SPLASH_POTION);
        }
        else
        {
            return itemstack;
        }
    }

    public void setItem(ItemStack stack)
    {
        this.getDataManager().set(ITEM, stack);
        this.getDataManager().func_187217_b(ITEM);
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    protected float getGravityVelocity()
    {
        return 0.05F;
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
            ItemStack itemstack = this.getItem();
            PotionType potiontype = PotionUtils.getPotionFromItem(itemstack);
            List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);
            boolean flag = potiontype == PotionTypes.WATER && list.isEmpty();

            if (result.field_72313_a == RayTraceResult.Type.BLOCK && flag)
            {
                BlockPos blockpos = result.func_178782_a().offset(result.field_178784_b);
                this.extinguishFires(blockpos, result.field_178784_b);

                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                {
                    this.extinguishFires(blockpos.offset(enumfacing), enumfacing);
                }
            }

            if (flag)
            {
                this.applyWater();
            }
            else if (!list.isEmpty())
            {
                if (this.isLingering())
                {
                    this.makeAreaOfEffectCloud(itemstack, potiontype);
                }
                else
                {
                    this.func_190543_a(result, list);
                }
            }

            int i = potiontype.hasInstantEffect() ? 2007 : 2002;
            this.world.func_175718_b(i, new BlockPos(this), PotionUtils.getColor(itemstack));
            this.remove();
        }
    }

    private void applyWater()
    {
        AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
        List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, WATER_SENSITIVE);

        if (!list.isEmpty())
        {
            for (EntityLivingBase entitylivingbase : list)
            {
                double d0 = this.getDistanceSq(entitylivingbase);

                if (d0 < 16.0D && isWaterSensitiveEntity(entitylivingbase))
                {
                    entitylivingbase.attackEntityFrom(DamageSource.DROWN, 1.0F);
                }
            }
        }
    }

    private void func_190543_a(RayTraceResult p_190543_1_, List<PotionEffect> p_190543_2_)
    {
        AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
        List<EntityLivingBase> list = this.world.<EntityLivingBase>func_72872_a(EntityLivingBase.class, axisalignedbb);

        if (!list.isEmpty())
        {
            for (EntityLivingBase entitylivingbase : list)
            {
                if (entitylivingbase.canBeHitWithPotion())
                {
                    double d0 = this.getDistanceSq(entitylivingbase);

                    if (d0 < 16.0D)
                    {
                        double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                        if (entitylivingbase == p_190543_1_.field_72308_g)
                        {
                            d1 = 1.0D;
                        }

                        for (PotionEffect potioneffect : p_190543_2_)
                        {
                            Potion potion = potioneffect.getPotion();

                            if (potion.isInstant())
                            {
                                potion.affectEntity(this, this.getThrower(), entitylivingbase, potioneffect.getAmplifier(), d1);
                            }
                            else
                            {
                                int i = (int)(d1 * (double)potioneffect.getDuration() + 0.5D);

                                if (i > 20)
                                {
                                    entitylivingbase.func_70690_d(new PotionEffect(potion, i, potioneffect.getAmplifier(), potioneffect.isAmbient(), potioneffect.doesShowParticles()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void makeAreaOfEffectCloud(ItemStack p_190542_1_, PotionType p_190542_2_)
    {
        EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
        entityareaeffectcloud.setOwner(this.getThrower());
        entityareaeffectcloud.setRadius(3.0F);
        entityareaeffectcloud.setRadiusOnUse(-0.5F);
        entityareaeffectcloud.setWaitTime(10);
        entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float)entityareaeffectcloud.getDuration());
        entityareaeffectcloud.setPotion(p_190542_2_);

        for (PotionEffect potioneffect : PotionUtils.getFullEffectsFromItem(p_190542_1_))
        {
            entityareaeffectcloud.addEffect(new PotionEffect(potioneffect));
        }

        NBTTagCompound nbttagcompound = p_190542_1_.getTag();

        if (nbttagcompound != null && nbttagcompound.contains("CustomPotionColor", 99))
        {
            entityareaeffectcloud.setColor(nbttagcompound.getInt("CustomPotionColor"));
        }

        this.world.addEntity0(entityareaeffectcloud);
    }

    private boolean isLingering()
    {
        return this.getItem().getItem() == Items.LINGERING_POTION;
    }

    private void extinguishFires(BlockPos pos, EnumFacing p_184542_2_)
    {
        if (this.world.getBlockState(pos).getBlock() == Blocks.FIRE)
        {
            this.world.extinguishFire((EntityPlayer)null, pos.offset(p_184542_2_), p_184542_2_.getOpposite());
        }
    }

    public static void func_189665_a(DataFixer p_189665_0_)
    {
        EntityThrowable.func_189661_a(p_189665_0_, "ThrownPotion");
        p_189665_0_.func_188258_a(FixTypes.ENTITY, new ItemStackData(EntityPotion.class, new String[] {"Potion"}));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        ItemStack itemstack = new ItemStack(compound.getCompound("Potion"));

        if (itemstack.isEmpty())
        {
            this.remove();
        }
        else
        {
            this.setItem(itemstack);
        }
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        ItemStack itemstack = this.getItem();

        if (!itemstack.isEmpty())
        {
            p_70014_1_.func_74782_a("Potion", itemstack.write(new NBTTagCompound()));
        }
    }

    private static boolean isWaterSensitiveEntity(EntityLivingBase p_190544_0_)
    {
        return p_190544_0_ instanceof EntityEnderman || p_190544_0_ instanceof EntityBlaze;
    }
}

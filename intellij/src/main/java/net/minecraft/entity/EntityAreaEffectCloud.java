package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.init.PotionTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityAreaEffectCloud extends Entity
{
    private static final DataParameter<Float> RADIUS = EntityDataManager.<Float>createKey(EntityAreaEffectCloud.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IGNORE_RADIUS = EntityDataManager.<Boolean>createKey(EntityAreaEffectCloud.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> PARTICLE = EntityDataManager.<Integer>createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> field_189736_e = EntityDataManager.<Integer>createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> field_189737_f = EntityDataManager.<Integer>createKey(EntityAreaEffectCloud.class, DataSerializers.VARINT);
    private PotionType potion;
    private final List<PotionEffect> effects;
    private final Map<Entity, Integer> reapplicationDelayMap;
    private int duration;
    private int waitTime;
    private int reapplicationDelay;
    private boolean colorSet;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    private EntityLivingBase owner;
    private UUID ownerUniqueId;

    public EntityAreaEffectCloud(World p_i46809_1_)
    {
        super(p_i46809_1_);
        this.potion = PotionTypes.EMPTY;
        this.effects = Lists.<PotionEffect>newArrayList();
        this.reapplicationDelayMap = Maps.<Entity, Integer>newHashMap();
        this.duration = 600;
        this.waitTime = 20;
        this.reapplicationDelay = 20;
        this.noClip = true;
        this.field_70178_ae = true;
        this.setRadius(3.0F);
    }

    public EntityAreaEffectCloud(World worldIn, double x, double y, double z)
    {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    protected void registerData()
    {
        this.getDataManager().register(COLOR, Integer.valueOf(0));
        this.getDataManager().register(RADIUS, Float.valueOf(0.5F));
        this.getDataManager().register(IGNORE_RADIUS, Boolean.valueOf(false));
        this.getDataManager().register(PARTICLE, Integer.valueOf(EnumParticleTypes.SPELL_MOB.func_179348_c()));
        this.getDataManager().register(field_189736_e, Integer.valueOf(0));
        this.getDataManager().register(field_189737_f, Integer.valueOf(0));
    }

    public void setRadius(float radiusIn)
    {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        this.func_70105_a(radiusIn * 2.0F, 0.5F);
        this.setPosition(d0, d1, d2);

        if (!this.world.isRemote)
        {
            this.getDataManager().set(RADIUS, Float.valueOf(radiusIn));
        }
    }

    public float getRadius()
    {
        return ((Float)this.getDataManager().get(RADIUS)).floatValue();
    }

    public void setPotion(PotionType potionIn)
    {
        this.potion = potionIn;

        if (!this.colorSet)
        {
            this.updateFixedColor();
        }
    }

    private void updateFixedColor()
    {
        if (this.potion == PotionTypes.EMPTY && this.effects.isEmpty())
        {
            this.getDataManager().set(COLOR, Integer.valueOf(0));
        }
        else
        {
            this.getDataManager().set(COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.effects))));
        }
    }

    public void addEffect(PotionEffect effect)
    {
        this.effects.add(effect);

        if (!this.colorSet)
        {
            this.updateFixedColor();
        }
    }

    public int getColor()
    {
        return ((Integer)this.getDataManager().get(COLOR)).intValue();
    }

    public void setColor(int colorIn)
    {
        this.colorSet = true;
        this.getDataManager().set(COLOR, Integer.valueOf(colorIn));
    }

    public EnumParticleTypes func_184493_l()
    {
        return EnumParticleTypes.func_179342_a(((Integer)this.getDataManager().get(PARTICLE)).intValue());
    }

    public void func_184491_a(EnumParticleTypes p_184491_1_)
    {
        this.getDataManager().set(PARTICLE, Integer.valueOf(p_184491_1_.func_179348_c()));
    }

    public int func_189733_n()
    {
        return ((Integer)this.getDataManager().get(field_189736_e)).intValue();
    }

    public void func_189734_b(int p_189734_1_)
    {
        this.getDataManager().set(field_189736_e, Integer.valueOf(p_189734_1_));
    }

    public int func_189735_o()
    {
        return ((Integer)this.getDataManager().get(field_189737_f)).intValue();
    }

    public void func_189732_d(int p_189732_1_)
    {
        this.getDataManager().set(field_189737_f, Integer.valueOf(p_189732_1_));
    }

    /**
     * Sets if the radius should be ignored, and the effect should be shown in a single point instead of an area
     */
    protected void setIgnoreRadius(boolean ignoreRadius)
    {
        this.getDataManager().set(IGNORE_RADIUS, Boolean.valueOf(ignoreRadius));
    }

    /**
     * Returns true if the radius should be ignored, and the effect should be shown in a single point instead of an area
     */
    public boolean shouldIgnoreRadius()
    {
        return ((Boolean)this.getDataManager().get(IGNORE_RADIUS)).booleanValue();
    }

    public int getDuration()
    {
        return this.duration;
    }

    public void setDuration(int durationIn)
    {
        this.duration = durationIn;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        super.tick();
        boolean flag = this.shouldIgnoreRadius();
        float f = this.getRadius();

        if (this.world.isRemote)
        {
            EnumParticleTypes enumparticletypes = this.func_184493_l();
            int[] aint = new int[enumparticletypes.func_179345_d()];

            if (aint.length > 0)
            {
                aint[0] = this.func_189733_n();
            }

            if (aint.length > 1)
            {
                aint[1] = this.func_189735_o();
            }

            if (flag)
            {
                if (this.rand.nextBoolean())
                {
                    for (int i = 0; i < 2; ++i)
                    {
                        float f1 = this.rand.nextFloat() * ((float)Math.PI * 2F);
                        float f2 = MathHelper.sqrt(this.rand.nextFloat()) * 0.2F;
                        float f3 = MathHelper.cos(f1) * f2;
                        float f4 = MathHelper.sin(f1) * f2;

                        if (enumparticletypes == EnumParticleTypes.SPELL_MOB)
                        {
                            int j = this.rand.nextBoolean() ? 16777215 : this.getColor();
                            int k = j >> 16 & 255;
                            int l = j >> 8 & 255;
                            int i1 = j & 255;
                            this.world.func_190523_a(EnumParticleTypes.SPELL_MOB.func_179348_c(), this.posX + (double)f3, this.posY, this.posZ + (double)f4, (double)((float)k / 255.0F), (double)((float)l / 255.0F), (double)((float)i1 / 255.0F));
                        }
                        else
                        {
                            this.world.func_190523_a(enumparticletypes.func_179348_c(), this.posX + (double)f3, this.posY, this.posZ + (double)f4, 0.0D, 0.0D, 0.0D, aint);
                        }
                    }
                }
            }
            else
            {
                float f5 = (float)Math.PI * f * f;

                for (int k1 = 0; (float)k1 < f5; ++k1)
                {
                    float f6 = this.rand.nextFloat() * ((float)Math.PI * 2F);
                    float f7 = MathHelper.sqrt(this.rand.nextFloat()) * f;
                    float f8 = MathHelper.cos(f6) * f7;
                    float f9 = MathHelper.sin(f6) * f7;

                    if (enumparticletypes == EnumParticleTypes.SPELL_MOB)
                    {
                        int l1 = this.getColor();
                        int i2 = l1 >> 16 & 255;
                        int j2 = l1 >> 8 & 255;
                        int j1 = l1 & 255;
                        this.world.func_190523_a(EnumParticleTypes.SPELL_MOB.func_179348_c(), this.posX + (double)f8, this.posY, this.posZ + (double)f9, (double)((float)i2 / 255.0F), (double)((float)j2 / 255.0F), (double)((float)j1 / 255.0F));
                    }
                    else
                    {
                        this.world.func_190523_a(enumparticletypes.func_179348_c(), this.posX + (double)f8, this.posY, this.posZ + (double)f9, (0.5D - this.rand.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - this.rand.nextDouble()) * 0.15D, aint);
                    }
                }
            }
        }
        else
        {
            if (this.ticksExisted >= this.waitTime + this.duration)
            {
                this.remove();
                return;
            }

            boolean flag1 = this.ticksExisted < this.waitTime;

            if (flag != flag1)
            {
                this.setIgnoreRadius(flag1);
            }

            if (flag1)
            {
                return;
            }

            if (this.radiusPerTick != 0.0F)
            {
                f += this.radiusPerTick;

                if (f < 0.5F)
                {
                    this.remove();
                    return;
                }

                this.setRadius(f);
            }

            if (this.ticksExisted % 5 == 0)
            {
                Iterator<Entry<Entity, Integer>> iterator = this.reapplicationDelayMap.entrySet().iterator();

                while (iterator.hasNext())
                {
                    Entry<Entity, Integer> entry = (Entry)iterator.next();

                    if (this.ticksExisted >= ((Integer)entry.getValue()).intValue())
                    {
                        iterator.remove();
                    }
                }

                List<PotionEffect> lstPotions = Lists.newArrayList();

                for (PotionEffect potioneffect1 : this.potion.getEffects())
                {
                    lstPotions.add(new PotionEffect(potioneffect1.getPotion(), potioneffect1.getDuration() / 4, potioneffect1.getAmplifier(), potioneffect1.isAmbient(), potioneffect1.doesShowParticles()));
                }

                lstPotions.addAll(this.effects);

                if (lstPotions.isEmpty())
                {
                    this.reapplicationDelayMap.clear();
                }
                else
                {
                    List<EntityLivingBase> list = this.world.<EntityLivingBase>func_72872_a(EntityLivingBase.class, this.getBoundingBox());

                    if (!list.isEmpty())
                    {
                        for (EntityLivingBase entitylivingbase : list)
                        {
                            if (!this.reapplicationDelayMap.containsKey(entitylivingbase) && entitylivingbase.canBeHitWithPotion())
                            {
                                double d0 = entitylivingbase.posX - this.posX;
                                double d1 = entitylivingbase.posZ - this.posZ;
                                double d2 = d0 * d0 + d1 * d1;

                                if (d2 <= (double)(f * f))
                                {
                                    this.reapplicationDelayMap.put(entitylivingbase, Integer.valueOf(this.ticksExisted + this.reapplicationDelay));

                                    for (PotionEffect potioneffect : lstPotions)
                                    {
                                        if (potioneffect.getPotion().isInstant())
                                        {
                                            potioneffect.getPotion().affectEntity(this, this.getOwner(), entitylivingbase, potioneffect.getAmplifier(), 0.5D);
                                        }
                                        else
                                        {
                                            entitylivingbase.func_70690_d(new PotionEffect(potioneffect));
                                        }
                                    }

                                    if (this.radiusOnUse != 0.0F)
                                    {
                                        f += this.radiusOnUse;

                                        if (f < 0.5F)
                                        {
                                            this.remove();
                                            return;
                                        }

                                        this.setRadius(f);
                                    }

                                    if (this.durationOnUse != 0)
                                    {
                                        this.duration += this.durationOnUse;

                                        if (this.duration <= 0)
                                        {
                                            this.remove();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void setRadiusOnUse(float radiusOnUseIn)
    {
        this.radiusOnUse = radiusOnUseIn;
    }

    public void setRadiusPerTick(float radiusPerTickIn)
    {
        this.radiusPerTick = radiusPerTickIn;
    }

    public void setWaitTime(int waitTimeIn)
    {
        this.waitTime = waitTimeIn;
    }

    public void setOwner(@Nullable EntityLivingBase ownerIn)
    {
        this.owner = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUniqueID();
    }

    @Nullable
    public EntityLivingBase getOwner()
    {
        if (this.owner == null && this.ownerUniqueId != null && this.world instanceof WorldServer)
        {
            Entity entity = ((WorldServer)this.world).func_175733_a(this.ownerUniqueId);

            if (entity instanceof EntityLivingBase)
            {
                this.owner = (EntityLivingBase)entity;
            }
        }

        return this.owner;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(NBTTagCompound compound)
    {
        this.ticksExisted = compound.getInt("Age");
        this.duration = compound.getInt("Duration");
        this.waitTime = compound.getInt("WaitTime");
        this.reapplicationDelay = compound.getInt("ReapplicationDelay");
        this.durationOnUse = compound.getInt("DurationOnUse");
        this.radiusOnUse = compound.getFloat("RadiusOnUse");
        this.radiusPerTick = compound.getFloat("RadiusPerTick");
        this.setRadius(compound.getFloat("Radius"));
        this.ownerUniqueId = compound.getUniqueId("OwnerUUID");

        if (compound.contains("Particle", 8))
        {
            EnumParticleTypes enumparticletypes = EnumParticleTypes.func_186831_a(compound.getString("Particle"));

            if (enumparticletypes != null)
            {
                this.func_184491_a(enumparticletypes);
                this.func_189734_b(compound.getInt("ParticleParam1"));
                this.func_189732_d(compound.getInt("ParticleParam2"));
            }
        }

        if (compound.contains("Color", 99))
        {
            this.setColor(compound.getInt("Color"));
        }

        if (compound.contains("Potion", 8))
        {
            this.setPotion(PotionUtils.getPotionTypeFromNBT(compound));
        }

        if (compound.contains("Effects", 9))
        {
            NBTTagList nbttaglist = compound.getList("Effects", 10);
            this.effects.clear();

            for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
            {
                PotionEffect potioneffect = PotionEffect.read(nbttaglist.getCompound(i));

                if (potioneffect != null)
                {
                    this.addEffect(potioneffect);
                }
            }
        }
    }

    protected void func_70014_b(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.putInt("Age", this.ticksExisted);
        p_70014_1_.putInt("Duration", this.duration);
        p_70014_1_.putInt("WaitTime", this.waitTime);
        p_70014_1_.putInt("ReapplicationDelay", this.reapplicationDelay);
        p_70014_1_.putInt("DurationOnUse", this.durationOnUse);
        p_70014_1_.putFloat("RadiusOnUse", this.radiusOnUse);
        p_70014_1_.putFloat("RadiusPerTick", this.radiusPerTick);
        p_70014_1_.putFloat("Radius", this.getRadius());
        p_70014_1_.putString("Particle", this.func_184493_l().func_179346_b());
        p_70014_1_.putInt("ParticleParam1", this.func_189733_n());
        p_70014_1_.putInt("ParticleParam2", this.func_189735_o());

        if (this.ownerUniqueId != null)
        {
            p_70014_1_.putUniqueId("OwnerUUID", this.ownerUniqueId);
        }

        if (this.colorSet)
        {
            p_70014_1_.putInt("Color", this.getColor());
        }

        if (this.potion != PotionTypes.EMPTY && this.potion != null)
        {
            p_70014_1_.putString("Potion", ((ResourceLocation)PotionType.field_185176_a.getKey(this.potion)).toString());
        }

        if (!this.effects.isEmpty())
        {
            NBTTagList nbttaglist = new NBTTagList();

            for (PotionEffect potioneffect : this.effects)
            {
                nbttaglist.func_74742_a(potioneffect.write(new NBTTagCompound()));
            }

            p_70014_1_.func_74782_a("Effects", nbttaglist);
        }
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (RADIUS.equals(key))
        {
            this.setRadius(this.getRadius());
        }

        super.notifyDataManagerChange(key);
    }

    public EnumPushReaction getPushReaction()
    {
        return EnumPushReaction.IGNORE;
    }
}

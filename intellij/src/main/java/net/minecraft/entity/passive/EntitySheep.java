package net.minecraft.entity.passive;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySheep extends EntityAnimal
{
    private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.<Byte>createKey(EntitySheep.class, DataSerializers.BYTE);
    private final InventoryCrafting field_90016_e = new InventoryCrafting(new Container()
    {
        public boolean canInteractWith(EntityPlayer playerIn)
        {
            return false;
        }
    }, 2, 1);
    private static final Map<EnumDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);
    private int sheepTimer;
    private EntityAIEatGrass eatGrassGoal;

    private static float[] createSheepColor(EnumDyeColor dyeColorIn)
    {
        float[] afloat = dyeColorIn.getColorComponentValues();
        float f = 0.75F;
        return new float[] {afloat[0] * 0.75F, afloat[1] * 0.75F, afloat[2] * 0.75F};
    }

    public static float[] getDyeRgb(EnumDyeColor dyeColor)
    {
        return DYE_TO_RGB.get(dyeColor);
    }

    public EntitySheep(World p_i1691_1_)
    {
        super(p_i1691_1_);
        this.func_70105_a(0.9F, 1.3F);
        this.field_90016_e.setInventorySlotContents(0, new ItemStack(Items.field_151100_aR));
        this.field_90016_e.setInventorySlotContents(1, new ItemStack(Items.field_151100_aR));
    }

    protected void registerGoals()
    {
        this.eatGrassGoal = new EntityAIEatGrass(this);
        this.goalSelector.addGoal(0, new EntityAISwimming(this));
        this.goalSelector.addGoal(1, new EntityAIPanic(this, 1.25D));
        this.goalSelector.addGoal(2, new EntityAIMate(this, 1.0D));
        this.goalSelector.addGoal(3, new EntityAITempt(this, 1.1D, Items.WHEAT, false));
        this.goalSelector.addGoal(4, new EntityAIFollowParent(this, 1.1D));
        this.goalSelector.addGoal(5, this.eatGrassGoal);
        this.goalSelector.addGoal(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.goalSelector.addGoal(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.goalSelector.addGoal(8, new EntityAILookIdle(this));
    }

    protected void updateAITasks()
    {
        this.sheepTimer = this.eatGrassGoal.getEatingGrassTimer();
        super.updateAITasks();
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick()
    {
        if (this.world.isRemote)
        {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }

        super.livingTick();
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(DYE_COLOR, Byte.valueOf((byte)0));
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        if (this.getSheared())
        {
            return LootTableList.field_186403_K;
        }
        else
        {
            switch (this.getFleeceColor())
            {
                case WHITE:
                default:
                    return LootTableList.ENTITIES_SHEEP_WHITE;

                case ORANGE:
                    return LootTableList.ENTITIES_SHEEP_ORANGE;

                case MAGENTA:
                    return LootTableList.ENTITIES_SHEEP_MAGENTA;

                case LIGHT_BLUE:
                    return LootTableList.ENTITIES_SHEEP_LIGHT_BLUE;

                case YELLOW:
                    return LootTableList.ENTITIES_SHEEP_YELLOW;

                case LIME:
                    return LootTableList.ENTITIES_SHEEP_LIME;

                case PINK:
                    return LootTableList.ENTITIES_SHEEP_PINK;

                case GRAY:
                    return LootTableList.ENTITIES_SHEEP_GRAY;

                case SILVER:
                    return LootTableList.field_186412_T;

                case CYAN:
                    return LootTableList.ENTITIES_SHEEP_CYAN;

                case PURPLE:
                    return LootTableList.ENTITIES_SHEEP_PURPLE;

                case BLUE:
                    return LootTableList.ENTITIES_SHEEP_BLUE;

                case BROWN:
                    return LootTableList.ENTITIES_SHEEP_BROWN;

                case GREEN:
                    return LootTableList.ENTITIES_SHEEP_GREEN;

                case RED:
                    return LootTableList.ENTITIES_SHEEP_RED;

                case BLACK:
                    return LootTableList.ENTITIES_SHEEP_BLACK;
            }
        }
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    public void handleStatusUpdate(byte id)
    {
        if (id == 10)
        {
            this.sheepTimer = 40;
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    public float getHeadRotationPointY(float p_70894_1_)
    {
        if (this.sheepTimer <= 0)
        {
            return 0.0F;
        }
        else if (this.sheepTimer >= 4 && this.sheepTimer <= 36)
        {
            return 1.0F;
        }
        else
        {
            return this.sheepTimer < 4 ? ((float)this.sheepTimer - p_70894_1_) / 4.0F : -((float)(this.sheepTimer - 40) - p_70894_1_) / 4.0F;
        }
    }

    public float getHeadRotationAngleX(float p_70890_1_)
    {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36)
        {
            float f = ((float)(this.sheepTimer - 4) - p_70890_1_) / 32.0F;
            return ((float)Math.PI / 5F) + ((float)Math.PI * 7F / 100F) * MathHelper.sin(f * 28.7F);
        }
        else
        {
            return this.sheepTimer > 0 ? ((float)Math.PI / 5F) : this.rotationPitch * 0.017453292F;
        }
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() == Items.SHEARS && !this.getSheared() && !this.isChild())
        {
            if (!this.world.isRemote)
            {
                this.setSheared(true);
                int i = 1 + this.rand.nextInt(3);

                for (int j = 0; j < i; ++j)
                {
                    EntityItem entityitem = this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, this.getFleeceColor().func_176765_a()), 1.0F);
                    entityitem.field_70181_x += (double)(this.rand.nextFloat() * 0.05F);
                    entityitem.field_70159_w += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                    entityitem.field_70179_y += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                }
            }

            itemstack.func_77972_a(1, player);
            this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        }

        return super.processInteract(player, hand);
    }

    public static void func_189802_b(DataFixer p_189802_0_)
    {
        EntityLiving.func_189752_a(p_189802_0_, EntitySheep.class);
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.putBoolean("Sheared", this.getSheared());
        p_70014_1_.putByte("Color", (byte)this.getFleeceColor().func_176765_a());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        this.setSheared(compound.getBoolean("Sheared"));
        this.setFleeceColor(EnumDyeColor.func_176764_b(compound.getByte("Color")));
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
    }

    /**
     * Gets the wool color of this sheep.
     */
    public EnumDyeColor getFleeceColor()
    {
        return EnumDyeColor.func_176764_b(((Byte)this.dataManager.get(DYE_COLOR)).byteValue() & 15);
    }

    /**
     * Sets the wool color of this sheep
     */
    public void setFleeceColor(EnumDyeColor color)
    {
        byte b0 = ((Byte)this.dataManager.get(DYE_COLOR)).byteValue();
        this.dataManager.set(DYE_COLOR, Byte.valueOf((byte)(b0 & 240 | color.func_176765_a() & 15)));
    }

    /**
     * returns true if a sheeps wool has been sheared
     */
    public boolean getSheared()
    {
        return (((Byte)this.dataManager.get(DYE_COLOR)).byteValue() & 16) != 0;
    }

    /**
     * make a sheep sheared if set to true
     */
    public void setSheared(boolean sheared)
    {
        byte b0 = ((Byte)this.dataManager.get(DYE_COLOR)).byteValue();

        if (sheared)
        {
            this.dataManager.set(DYE_COLOR, Byte.valueOf((byte)(b0 | 16)));
        }
        else
        {
            this.dataManager.set(DYE_COLOR, Byte.valueOf((byte)(b0 & -17)));
        }
    }

    /**
     * Chooses a "vanilla" sheep color based on the provided random.
     */
    public static EnumDyeColor getRandomSheepColor(Random random)
    {
        int i = random.nextInt(100);

        if (i < 5)
        {
            return EnumDyeColor.BLACK;
        }
        else if (i < 10)
        {
            return EnumDyeColor.GRAY;
        }
        else if (i < 15)
        {
            return EnumDyeColor.SILVER;
        }
        else if (i < 18)
        {
            return EnumDyeColor.BROWN;
        }
        else
        {
            return random.nextInt(500) == 0 ? EnumDyeColor.PINK : EnumDyeColor.WHITE;
        }
    }

    public EntitySheep createChild(EntityAgeable ageable)
    {
        EntitySheep entitysheep = (EntitySheep)ageable;
        EntitySheep entitysheep1 = new EntitySheep(this.world);
        entitysheep1.setFleeceColor(this.getDyeColorMixFromParents(this, entitysheep));
        return entitysheep1;
    }

    /**
     * This function applies the benefits of growing back wool and faster growing up to the acting entity. (This
     * function is used in the AIEatGrass)
     */
    public void eatGrassBonus()
    {
        this.setSheared(false);

        if (this.isChild())
        {
            this.addGrowth(60);
        }
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, @Nullable IEntityLivingData p_180482_2_)
    {
        p_180482_2_ = super.func_180482_a(p_180482_1_, p_180482_2_);
        this.setFleeceColor(getRandomSheepColor(this.world.rand));
        return p_180482_2_;
    }

    /**
     * Attempts to mix both parent sheep to come up with a mixed dye color.
     */
    private EnumDyeColor getDyeColorMixFromParents(EntityAnimal father, EntityAnimal mother)
    {
        int i = ((EntitySheep)father).getFleeceColor().func_176767_b();
        int j = ((EntitySheep)mother).getFleeceColor().func_176767_b();
        this.field_90016_e.getStackInSlot(0).func_77964_b(i);
        this.field_90016_e.getStackInSlot(1).func_77964_b(j);
        ItemStack itemstack = CraftingManager.func_82787_a(this.field_90016_e, ((EntitySheep)father).world);
        int k;

        if (itemstack.getItem() == Items.field_151100_aR)
        {
            k = itemstack.func_77960_j();
        }
        else
        {
            k = this.world.rand.nextBoolean() ? i : j;
        }

        return EnumDyeColor.func_176766_a(k);
    }

    public float getEyeHeight()
    {
        return 0.95F * this.field_70131_O;
    }

    static
    {
        for (EnumDyeColor enumdyecolor : EnumDyeColor.values())
        {
            DYE_TO_RGB.put(enumdyecolor, createSheepColor(enumdyecolor));
        }

        DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[] {0.9019608F, 0.9019608F, 0.9019608F});
    }
}

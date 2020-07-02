package net.minecraft.entity.passive;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.SoundType;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityHorse extends AbstractHorse
{
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
    private static final DataParameter<Integer> HORSE_VARIANT = EntityDataManager.<Integer>createKey(EntityHorse.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> field_184791_bI = EntityDataManager.<Integer>createKey(EntityHorse.class, DataSerializers.VARINT);
    private static final String[] HORSE_TEXTURES = new String[] {"textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png"};
    private static final String[] HORSE_TEXTURES_ABBR = new String[] {"hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb"};
    private static final String[] HORSE_MARKING_TEXTURES = new String[] {null, "textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_whitefield.png", "textures/entity/horse/horse_markings_whitedots.png", "textures/entity/horse/horse_markings_blackdots.png"};
    private static final String[] HORSE_MARKING_TEXTURES_ABBR = new String[] {"", "wo_", "wmo", "wdo", "bdo"};
    private String texturePrefix;
    private final String[] horseTexturesArray = new String[3];

    public EntityHorse(World p_i1685_1_)
    {
        super(p_i1685_1_);
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(HORSE_VARIANT, Integer.valueOf(0));
        this.dataManager.register(field_184791_bI, Integer.valueOf(HorseArmorType.NONE.func_188579_a()));
    }

    public static void func_189803_b(DataFixer p_189803_0_)
    {
        AbstractHorse.func_190683_c(p_189803_0_, EntityHorse.class);
        p_189803_0_.func_188258_a(FixTypes.ENTITY, new ItemStackData(EntityHorse.class, new String[] {"ArmorItem"}));
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.putInt("Variant", this.getHorseVariant());

        if (!this.horseChest.getStackInSlot(1).isEmpty())
        {
            p_70014_1_.func_74782_a("ArmorItem", this.horseChest.getStackInSlot(1).write(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        this.setHorseVariant(compound.getInt("Variant"));

        if (compound.contains("ArmorItem", 10))
        {
            ItemStack itemstack = new ItemStack(compound.getCompound("ArmorItem"));

            if (!itemstack.isEmpty() && HorseArmorType.func_188577_b(itemstack.getItem()))
            {
                this.horseChest.setInventorySlotContents(1, itemstack);
            }
        }

        this.updateHorseSlots();
    }

    public void setHorseVariant(int variant)
    {
        this.dataManager.set(HORSE_VARIANT, Integer.valueOf(variant));
        this.resetTexturePrefix();
    }

    public int getHorseVariant()
    {
        return ((Integer)this.dataManager.get(HORSE_VARIANT)).intValue();
    }

    private void resetTexturePrefix()
    {
        this.texturePrefix = null;
    }

    private void setHorseTexturePaths()
    {
        int i = this.getHorseVariant();
        int j = (i & 255) % 7;
        int k = ((i & 65280) >> 8) % 5;
        HorseArmorType horsearmortype = this.func_184783_dl();
        this.horseTexturesArray[0] = HORSE_TEXTURES[j];
        this.horseTexturesArray[1] = HORSE_MARKING_TEXTURES[k];
        this.horseTexturesArray[2] = horsearmortype.func_188574_d();
        this.texturePrefix = "horse/" + HORSE_TEXTURES_ABBR[j] + HORSE_MARKING_TEXTURES_ABBR[k] + horsearmortype.func_188573_b();
    }

    public String getHorseTexture()
    {
        if (this.texturePrefix == null)
        {
            this.setHorseTexturePaths();
        }

        return this.texturePrefix;
    }

    public String[] getVariantTexturePaths()
    {
        if (this.texturePrefix == null)
        {
            this.setHorseTexturePaths();
        }

        return this.horseTexturesArray;
    }

    /**
     * Updates the items in the saddle and armor slots of the horse's inventory.
     */
    protected void updateHorseSlots()
    {
        super.updateHorseSlots();
        this.func_146086_d(this.horseChest.getStackInSlot(1));
    }

    public void func_146086_d(ItemStack p_146086_1_)
    {
        HorseArmorType horsearmortype = HorseArmorType.func_188580_a(p_146086_1_);
        this.dataManager.set(field_184791_bI, Integer.valueOf(horsearmortype.func_188579_a()));
        this.resetTexturePrefix();

        if (!this.world.isRemote)
        {
            this.getAttribute(SharedMonsterAttributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            int i = horsearmortype.func_188578_c();

            if (i != 0)
            {
                this.getAttribute(SharedMonsterAttributes.ARMOR).applyModifier((new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", (double)i, 0)).setSaved(false));
            }
        }
    }

    public HorseArmorType func_184783_dl()
    {
        return HorseArmorType.func_188575_a(((Integer)this.dataManager.get(field_184791_bI)).intValue());
    }

    /**
     * Called by InventoryBasic.onInventoryChanged() on a array that is never filled.
     */
    public void onInventoryChanged(IInventory invBasic)
    {
        HorseArmorType horsearmortype = this.func_184783_dl();
        super.onInventoryChanged(invBasic);
        HorseArmorType horsearmortype1 = this.func_184783_dl();

        if (this.ticksExisted > 20 && horsearmortype != horsearmortype1 && horsearmortype1 != HorseArmorType.NONE)
        {
            this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
        }
    }

    protected void playGallopSound(SoundType p_190680_1_)
    {
        super.playGallopSound(p_190680_1_);

        if (this.rand.nextInt(10) == 0)
        {
            this.playSound(SoundEvents.ENTITY_HORSE_BREATHE, p_190680_1_.getVolume() * 0.6F, p_190680_1_.getPitch());
        }
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)this.getModifiedMaxHealth());
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.getModifiedMovementSpeed());
        this.getAttribute(JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        super.tick();

        if (this.world.isRemote && this.dataManager.isDirty())
        {
            this.dataManager.setClean();
            this.resetTexturePrefix();
        }
    }

    protected SoundEvent getAmbientSound()
    {
        super.getAmbientSound();
        return SoundEvents.ENTITY_HORSE_AMBIENT;
    }

    protected SoundEvent getDeathSound()
    {
        super.getDeathSound();
        return SoundEvents.ENTITY_HORSE_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        super.getHurtSound(damageSourceIn);
        return SoundEvents.ENTITY_HORSE_HURT;
    }

    protected SoundEvent getAngrySound()
    {
        super.getAngrySound();
        return SoundEvents.ENTITY_HORSE_ANGRY;
    }

    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_186396_D;
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        boolean flag = !itemstack.isEmpty();

        if (flag && itemstack.getItem() == Items.field_151063_bx)
        {
            return super.processInteract(player, hand);
        }
        else
        {
            if (!this.isChild())
            {
                if (this.isTame() && player.func_70093_af())
                {
                    this.openGUI(player);
                    return true;
                }

                if (this.isBeingRidden())
                {
                    return super.processInteract(player, hand);
                }
            }

            if (flag)
            {
                if (this.handleEating(player, itemstack))
                {
                    if (!player.abilities.isCreativeMode)
                    {
                        itemstack.shrink(1);
                    }

                    return true;
                }

                if (itemstack.interactWithEntity(player, this, hand))
                {
                    return true;
                }

                if (!this.isTame())
                {
                    this.makeMad();
                    return true;
                }

                boolean flag1 = HorseArmorType.func_188580_a(itemstack) != HorseArmorType.NONE;
                boolean flag2 = !this.isChild() && !this.isHorseSaddled() && itemstack.getItem() == Items.SADDLE;

                if (flag1 || flag2)
                {
                    this.openGUI(player);
                    return true;
                }
            }

            if (this.isChild())
            {
                return super.processInteract(player, hand);
            }
            else
            {
                this.mountTo(player);
                return true;
            }
        }
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
        if (otherAnimal == this)
        {
            return false;
        }
        else if (!(otherAnimal instanceof EntityDonkey) && !(otherAnimal instanceof EntityHorse))
        {
            return false;
        }
        else
        {
            return this.canMate() && ((AbstractHorse)otherAnimal).canMate();
        }
    }

    public EntityAgeable createChild(EntityAgeable ageable)
    {
        AbstractHorse abstracthorse;

        if (ageable instanceof EntityDonkey)
        {
            abstracthorse = new EntityMule(this.world);
        }
        else
        {
            EntityHorse entityhorse = (EntityHorse)ageable;
            abstracthorse = new EntityHorse(this.world);
            int j = this.rand.nextInt(9);
            int i;

            if (j < 4)
            {
                i = this.getHorseVariant() & 255;
            }
            else if (j < 8)
            {
                i = entityhorse.getHorseVariant() & 255;
            }
            else
            {
                i = this.rand.nextInt(7);
            }

            int k = this.rand.nextInt(5);

            if (k < 2)
            {
                i = i | this.getHorseVariant() & 65280;
            }
            else if (k < 4)
            {
                i = i | entityhorse.getHorseVariant() & 65280;
            }
            else
            {
                i = i | this.rand.nextInt(5) << 8 & 65280;
            }

            ((EntityHorse)abstracthorse).setHorseVariant(i);
        }

        this.setOffspringAttributes(ageable, abstracthorse);
        return abstracthorse;
    }

    public boolean wearsArmor()
    {
        return true;
    }

    public boolean isArmor(ItemStack stack)
    {
        return HorseArmorType.func_188577_b(stack.getItem());
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, @Nullable IEntityLivingData p_180482_2_)
    {
        p_180482_2_ = super.func_180482_a(p_180482_1_, p_180482_2_);
        int i;

        if (p_180482_2_ instanceof EntityHorse.GroupData)
        {
            i = ((EntityHorse.GroupData)p_180482_2_).variant;
        }
        else
        {
            i = this.rand.nextInt(7);
            p_180482_2_ = new EntityHorse.GroupData(i);
        }

        this.setHorseVariant(i | this.rand.nextInt(5) << 8);
        return p_180482_2_;
    }

    public static class GroupData implements IEntityLivingData
    {
        public int variant;

        public GroupData(int variantIn)
        {
            this.variant = variantIn;
        }
    }
}

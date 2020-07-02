package net.minecraft.item;

import com.google.common.base.Predicates;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemArmor extends Item
{
    private static final int[] MAX_DAMAGE_ARRAY = new int[] {13, 15, 16, 11};
    private static final UUID[] ARMOR_MODIFIERS = new UUID[] {UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    public static final String[] field_94603_a = new String[] {"minecraft:items/empty_armor_slot_boots", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_helmet"};
    public static final IBehaviorDispenseItem DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem()
    {
        protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
        {
            ItemStack itemstack = ItemArmor.func_185082_a(source, stack);
            return itemstack.isEmpty() ? super.dispenseStack(source, stack) : itemstack;
        }
    };
    public final EntityEquipmentSlot slot;
    public final int damageReduceAmount;
    public final float toughness;
    public final int field_77880_c;
    private final ItemArmor.ArmorMaterial field_77878_bZ;

    public static ItemStack func_185082_a(IBlockSource p_185082_0_, ItemStack p_185082_1_)
    {
        BlockPos blockpos = p_185082_0_.getBlockPos().offset((EnumFacing)p_185082_0_.getBlockState().get(BlockDispenser.FACING));
        List<EntityLivingBase> list = p_185082_0_.func_82618_k().<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(blockpos), Predicates.and(EntitySelectors.NOT_SPECTATING, new EntitySelectors.ArmoredMob(p_185082_1_)));

        if (list.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else
        {
            EntityLivingBase entitylivingbase = list.get(0);
            EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(p_185082_1_);
            ItemStack itemstack = p_185082_1_.split(1);
            entitylivingbase.setItemStackToSlot(entityequipmentslot, itemstack);

            if (entitylivingbase instanceof EntityLiving)
            {
                ((EntityLiving)entitylivingbase).setDropChance(entityequipmentslot, 2.0F);
            }

            return p_185082_1_;
        }
    }

    public ItemArmor(ItemArmor.ArmorMaterial p_i46750_1_, int p_i46750_2_, EntityEquipmentSlot p_i46750_3_)
    {
        this.field_77878_bZ = p_i46750_1_;
        this.slot = p_i46750_3_;
        this.field_77880_c = p_i46750_2_;
        this.damageReduceAmount = p_i46750_1_.func_78044_b(p_i46750_3_);
        this.func_77656_e(p_i46750_1_.func_78046_a(p_i46750_3_));
        this.toughness = p_i46750_1_.func_189416_e();
        this.maxStackSize = 1;
        this.func_77637_a(CreativeTabs.COMBAT);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.func_82595_a(this, DISPENSER_BEHAVIOR);
    }

    /**
     * Gets the equipment slot of this armor piece (formerly known as armor type)
     */
    public EntityEquipmentSlot getEquipmentSlot()
    {
        return this.slot;
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return this.field_77878_bZ.func_78045_a();
    }

    public ItemArmor.ArmorMaterial func_82812_d()
    {
        return this.field_77878_bZ;
    }

    public boolean func_82816_b_(ItemStack p_82816_1_)
    {
        if (this.field_77878_bZ != ItemArmor.ArmorMaterial.LEATHER)
        {
            return false;
        }
        else
        {
            NBTTagCompound nbttagcompound = p_82816_1_.getTag();
            return nbttagcompound != null && nbttagcompound.contains("display", 10) ? nbttagcompound.getCompound("display").contains("color", 3) : false;
        }
    }

    public int func_82814_b(ItemStack p_82814_1_)
    {
        if (this.field_77878_bZ != ItemArmor.ArmorMaterial.LEATHER)
        {
            return 16777215;
        }
        else
        {
            NBTTagCompound nbttagcompound = p_82814_1_.getTag();

            if (nbttagcompound != null)
            {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("display");

                if (nbttagcompound1 != null && nbttagcompound1.contains("color", 3))
                {
                    return nbttagcompound1.getInt("color");
                }
            }

            return 10511680;
        }
    }

    public void func_82815_c(ItemStack p_82815_1_)
    {
        if (this.field_77878_bZ == ItemArmor.ArmorMaterial.LEATHER)
        {
            NBTTagCompound nbttagcompound = p_82815_1_.getTag();

            if (nbttagcompound != null)
            {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("display");

                if (nbttagcompound1.contains("color"))
                {
                    nbttagcompound1.remove("color");
                }
            }
        }
    }

    public void func_82813_b(ItemStack p_82813_1_, int p_82813_2_)
    {
        if (this.field_77878_bZ != ItemArmor.ArmorMaterial.LEATHER)
        {
            throw new UnsupportedOperationException("Can't dye non-leather!");
        }
        else
        {
            NBTTagCompound nbttagcompound = p_82813_1_.getTag();

            if (nbttagcompound == null)
            {
                nbttagcompound = new NBTTagCompound();
                p_82813_1_.setTag(nbttagcompound);
            }

            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("display");

            if (!nbttagcompound.contains("display", 10))
            {
                nbttagcompound.func_74782_a("display", nbttagcompound1);
            }

            nbttagcompound1.putInt("color", p_82813_2_);
        }
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return this.field_77878_bZ.func_151685_b() == repair.getItem() ? true : super.getIsRepairable(toRepair, repair);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
        ItemStack itemstack1 = playerIn.getItemStackFromSlot(entityequipmentslot);

        if (itemstack1.isEmpty())
        {
            playerIn.setItemStackToSlot(entityequipmentslot, itemstack.copy());
            itemstack.setCount(0);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        else
        {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);

        if (equipmentSlot == this.slot)
        {
            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", (double)this.damageReduceAmount, 0));
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", (double)this.toughness, 0));
        }

        return multimap;
    }

    public static enum ArmorMaterial
    {
        LEATHER("leather", 5, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F),
        CHAIN("chainmail", 15, new int[]{1, 4, 5, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0.0F),
        IRON("iron", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F),
        GOLD("gold", 7, new int[]{1, 3, 5, 2}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F),
        DIAMOND("diamond", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F);

        private final String name;
        private final int maxDamageFactor;
        private final int[] damageReductionAmountArray;
        private final int enchantability;
        private final SoundEvent soundEvent;
        private final float toughness;

        private ArmorMaterial(String p_i47117_3_, int p_i47117_4_, int[] p_i47117_5_, int p_i47117_6_, SoundEvent p_i47117_7_, float p_i47117_8_)
        {
            this.name = p_i47117_3_;
            this.maxDamageFactor = p_i47117_4_;
            this.damageReductionAmountArray = p_i47117_5_;
            this.enchantability = p_i47117_6_;
            this.soundEvent = p_i47117_7_;
            this.toughness = p_i47117_8_;
        }

        public int func_78046_a(EntityEquipmentSlot p_78046_1_)
        {
            return ItemArmor.MAX_DAMAGE_ARRAY[p_78046_1_.getIndex()] * this.maxDamageFactor;
        }

        public int func_78044_b(EntityEquipmentSlot p_78044_1_)
        {
            return this.damageReductionAmountArray[p_78044_1_.getIndex()];
        }

        public int func_78045_a()
        {
            return this.enchantability;
        }

        public SoundEvent func_185017_b()
        {
            return this.soundEvent;
        }

        public Item func_151685_b()
        {
            if (this == LEATHER)
            {
                return Items.LEATHER;
            }
            else if (this == CHAIN)
            {
                return Items.IRON_INGOT;
            }
            else if (this == GOLD)
            {
                return Items.GOLD_INGOT;
            }
            else if (this == IRON)
            {
                return Items.IRON_INGOT;
            }
            else
            {
                return this == DIAMOND ? Items.DIAMOND : null;
            }
        }

        public String func_179242_c()
        {
            return this.name;
        }

        public float func_189416_e()
        {
            return this.toughness;
        }
    }
}

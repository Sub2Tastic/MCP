package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.BlockEntityTag;
import net.minecraft.util.datafix.walkers.EntityTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public final class ItemStack
{
    public static final ItemStack EMPTY = new ItemStack((Item)null);
    public static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
    private int count;
    private int animationsToGo;
    private final Item item;
    private NBTTagCompound tag;
    private boolean isEmpty;
    private int field_77991_e;
    private EntityItemFrame itemFrame;
    private Block canDestroyCacheBlock;
    private boolean canDestroyCacheResult;
    private Block canPlaceOnCacheBlock;
    private boolean canPlaceOnCacheResult;

    public ItemStack(Block p_i1876_1_)
    {
        this(p_i1876_1_, 1);
    }

    public ItemStack(Block p_i1877_1_, int p_i1877_2_)
    {
        this(p_i1877_1_, p_i1877_2_, 0);
    }

    public ItemStack(Block p_i1878_1_, int p_i1878_2_, int p_i1878_3_)
    {
        this(Item.getItemFromBlock(p_i1878_1_), p_i1878_2_, p_i1878_3_);
    }

    public ItemStack(Item p_i1879_1_)
    {
        this(p_i1879_1_, 1);
    }

    public ItemStack(Item p_i1880_1_, int p_i1880_2_)
    {
        this(p_i1880_1_, p_i1880_2_, 0);
    }

    public ItemStack(Item p_i1881_1_, int p_i1881_2_, int p_i1881_3_)
    {
        this.item = p_i1881_1_;
        this.field_77991_e = p_i1881_3_;
        this.count = p_i1881_2_;

        if (this.field_77991_e < 0)
        {
            this.field_77991_e = 0;
        }

        this.updateEmptyState();
    }

    private void updateEmptyState()
    {
        this.isEmpty = this.isEmpty();
    }

    public ItemStack(NBTTagCompound compound)
    {
        this.item = Item.func_111206_d(compound.getString("id"));
        this.count = compound.getByte("Count");
        this.field_77991_e = Math.max(0, compound.getShort("Damage"));

        if (compound.contains("tag", 10))
        {
            this.tag = compound.getCompound("tag");

            if (this.item != null)
            {
                this.item.updateItemStackNBT(compound);
            }
        }

        this.updateEmptyState();
    }

    public boolean isEmpty()
    {
        if (this == EMPTY)
        {
            return true;
        }
        else if (this.item != null && this.item != Item.getItemFromBlock(Blocks.AIR))
        {
            if (this.count <= 0)
            {
                return true;
            }
            else
            {
                return this.field_77991_e < -32768 || this.field_77991_e > 65535;
            }
        }
        else
        {
            return true;
        }
    }

    public static void func_189868_a(DataFixer p_189868_0_)
    {
        p_189868_0_.func_188258_a(FixTypes.ITEM_INSTANCE, new BlockEntityTag());
        p_189868_0_.func_188258_a(FixTypes.ITEM_INSTANCE, new EntityTag());
    }

    /**
     * Splits off a stack of the given amount of this stack and reduces this stack by the amount.
     */
    public ItemStack split(int amount)
    {
        int i = Math.min(amount, this.count);
        ItemStack itemstack = this.copy();
        itemstack.setCount(i);
        this.shrink(i);
        return itemstack;
    }

    /**
     * Returns the object corresponding to the stack.
     */
    public Item getItem()
    {
        return this.isEmpty ? Item.getItemFromBlock(Blocks.AIR) : this.item;
    }

    public EnumActionResult func_179546_a(EntityPlayer p_179546_1_, World p_179546_2_, BlockPos p_179546_3_, EnumHand p_179546_4_, EnumFacing p_179546_5_, float p_179546_6_, float p_179546_7_, float p_179546_8_)
    {
        EnumActionResult enumactionresult = this.getItem().func_180614_a(p_179546_1_, p_179546_2_, p_179546_3_, p_179546_4_, p_179546_5_, p_179546_6_, p_179546_7_, p_179546_8_);

        if (enumactionresult == EnumActionResult.SUCCESS)
        {
            p_179546_1_.addStat(StatList.func_188057_b(this.item));
        }

        return enumactionresult;
    }

    public float getDestroySpeed(IBlockState blockIn)
    {
        return this.getItem().getDestroySpeed(this, blockIn);
    }

    public ActionResult<ItemStack> useItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        return this.getItem().onItemRightClick(worldIn, playerIn, hand);
    }

    /**
     * Called when the item in use count reach 0, e.g. item food eaten. Return the new ItemStack. Args : world, entity
     */
    public ItemStack onItemUseFinish(World worldIn, EntityLivingBase entityLiving)
    {
        return this.getItem().onItemUseFinish(this, worldIn, entityLiving);
    }

    /**
     * Write the stack fields to a NBT object. Return the new NBT object.
     */
    public NBTTagCompound write(NBTTagCompound nbt)
    {
        ResourceLocation resourcelocation = Item.field_150901_e.getKey(this.item);
        nbt.putString("id", resourcelocation == null ? "minecraft:air" : resourcelocation.toString());
        nbt.putByte("Count", (byte)this.count);
        nbt.putShort("Damage", (short)this.field_77991_e);

        if (this.tag != null)
        {
            nbt.func_74782_a("tag", this.tag);
        }

        return nbt;
    }

    /**
     * Returns maximum size of the stack.
     */
    public int getMaxStackSize()
    {
        return this.getItem().getMaxStackSize();
    }

    /**
     * Returns true if the ItemStack can hold 2 or more units of the item.
     */
    public boolean isStackable()
    {
        return this.getMaxStackSize() > 1 && (!this.isDamageable() || !this.isDamaged());
    }

    /**
     * true if this itemStack is damageable
     */
    public boolean isDamageable()
    {
        if (this.isEmpty)
        {
            return false;
        }
        else if (this.item.getMaxDamage() <= 0)
        {
            return false;
        }
        else
        {
            return !this.hasTag() || !this.getTag().getBoolean("Unbreakable");
        }
    }

    public boolean func_77981_g()
    {
        return this.getItem().func_77614_k();
    }

    /**
     * returns true when a damageable item is damaged
     */
    public boolean isDamaged()
    {
        return this.isDamageable() && this.field_77991_e > 0;
    }

    public int getDamage()
    {
        return this.field_77991_e;
    }

    public int func_77960_j()
    {
        return this.field_77991_e;
    }

    public void func_77964_b(int p_77964_1_)
    {
        this.field_77991_e = p_77964_1_;

        if (this.field_77991_e < 0)
        {
            this.field_77991_e = 0;
        }
    }

    /**
     * Returns the max damage an item in the stack can take.
     */
    public int getMaxDamage()
    {
        return this.getItem().getMaxDamage();
    }

    /**
     * Attempts to damage the ItemStack with par1 amount of damage, If the ItemStack has the Unbreaking enchantment
     * there is a chance for each point of damage to be negated. Returns true if it takes more damage than
     * getMaxDamage(). Returns false otherwise or if the ItemStack can't be damaged or if all points of damage are
     * negated.
     */
    public boolean attemptDamageItem(int amount, Random rand, @Nullable EntityPlayerMP damager)
    {
        if (!this.isDamageable())
        {
            return false;
        }
        else
        {
            if (amount > 0)
            {
                int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, this);
                int j = 0;

                for (int k = 0; i > 0 && k < amount; ++k)
                {
                    if (EnchantmentDurability.negateDamage(this, i, rand))
                    {
                        ++j;
                    }
                }

                amount -= j;

                if (amount <= 0)
                {
                    return false;
                }
            }

            if (damager != null && amount != 0)
            {
                CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(damager, this, this.field_77991_e + amount);
            }

            this.field_77991_e += amount;
            return this.field_77991_e > this.getMaxDamage();
        }
    }

    public void func_77972_a(int p_77972_1_, EntityLivingBase p_77972_2_)
    {
        if (!(p_77972_2_ instanceof EntityPlayer) || !((EntityPlayer)p_77972_2_).abilities.isCreativeMode)
        {
            if (this.isDamageable())
            {
                if (this.attemptDamageItem(p_77972_1_, p_77972_2_.getRNG(), p_77972_2_ instanceof EntityPlayerMP ? (EntityPlayerMP)p_77972_2_ : null))
                {
                    p_77972_2_.renderBrokenItemStack(this);
                    this.shrink(1);

                    if (p_77972_2_ instanceof EntityPlayer)
                    {
                        EntityPlayer entityplayer = (EntityPlayer)p_77972_2_;
                        entityplayer.addStat(StatList.func_188059_c(this.item));
                    }

                    this.field_77991_e = 0;
                }
            }
        }
    }

    /**
     * Calls the delegated method to the Item to damage the incoming Entity, and if necessary, triggers a stats
     * increase.
     */
    public void hitEntity(EntityLivingBase entityIn, EntityPlayer playerIn)
    {
        boolean flag = this.item.hitEntity(this, entityIn, playerIn);

        if (flag)
        {
            playerIn.addStat(StatList.func_188057_b(this.item));
        }
    }

    /**
     * Called when a Block is destroyed using this ItemStack
     */
    public void onBlockDestroyed(World worldIn, IBlockState blockIn, BlockPos pos, EntityPlayer playerIn)
    {
        boolean flag = this.getItem().onBlockDestroyed(this, worldIn, blockIn, pos, playerIn);

        if (flag)
        {
            playerIn.addStat(StatList.func_188057_b(this.item));
        }
    }

    /**
     * Check whether the given Block can be harvested using this ItemStack.
     */
    public boolean canHarvestBlock(IBlockState blockIn)
    {
        return this.getItem().canHarvestBlock(blockIn);
    }

    public boolean interactWithEntity(EntityPlayer playerIn, EntityLivingBase entityIn, EnumHand hand)
    {
        return this.getItem().itemInteractionForEntity(this, playerIn, entityIn, hand);
    }

    /**
     * Returns a new stack with the same properties.
     */
    public ItemStack copy()
    {
        ItemStack itemstack = new ItemStack(this.item, this.count, this.field_77991_e);
        itemstack.setAnimationsToGo(this.getAnimationsToGo());

        if (this.tag != null)
        {
            itemstack.tag = this.tag.copy();
        }

        return itemstack;
    }

    public static boolean areItemStackTagsEqual(ItemStack stackA, ItemStack stackB)
    {
        if (stackA.isEmpty() && stackB.isEmpty())
        {
            return true;
        }
        else if (!stackA.isEmpty() && !stackB.isEmpty())
        {
            if (stackA.tag == null && stackB.tag != null)
            {
                return false;
            }
            else
            {
                return stackA.tag == null || stackA.tag.equals(stackB.tag);
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * compares ItemStack argument1 with ItemStack argument2; returns true if both ItemStacks are equal
     */
    public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB)
    {
        if (stackA.isEmpty() && stackB.isEmpty())
        {
            return true;
        }
        else
        {
            return !stackA.isEmpty() && !stackB.isEmpty() ? stackA.isItemStackEqual(stackB) : false;
        }
    }

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if both ItemStacks are equal
     */
    private boolean isItemStackEqual(ItemStack other)
    {
        if (this.count != other.count)
        {
            return false;
        }
        else if (this.getItem() != other.getItem())
        {
            return false;
        }
        else if (this.field_77991_e != other.field_77991_e)
        {
            return false;
        }
        else if (this.tag == null && other.tag != null)
        {
            return false;
        }
        else
        {
            return this.tag == null || this.tag.equals(other.tag);
        }
    }

    /**
     * Compares Item and damage value of the two stacks
     */
    public static boolean areItemsEqual(ItemStack stackA, ItemStack stackB)
    {
        if (stackA == stackB)
        {
            return true;
        }
        else
        {
            return !stackA.isEmpty() && !stackB.isEmpty() ? stackA.isItemEqual(stackB) : false;
        }
    }

    public static boolean areItemsEqualIgnoreDurability(ItemStack stackA, ItemStack stackB)
    {
        if (stackA == stackB)
        {
            return true;
        }
        else
        {
            return !stackA.isEmpty() && !stackB.isEmpty() ? stackA.isItemEqualIgnoreDurability(stackB) : false;
        }
    }

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if the Items contained in both ItemStacks are
     * equal
     */
    public boolean isItemEqual(ItemStack other)
    {
        return !other.isEmpty() && this.item == other.item && this.field_77991_e == other.field_77991_e;
    }

    public boolean isItemEqualIgnoreDurability(ItemStack stack)
    {
        if (!this.isDamageable())
        {
            return this.isItemEqual(stack);
        }
        else
        {
            return !stack.isEmpty() && this.item == stack.item;
        }
    }

    public String getTranslationKey()
    {
        return this.getItem().getTranslationKey(this);
    }

    public String toString()
    {
        return this.count + "x" + this.getItem().getTranslationKey() + "@" + this.field_77991_e;
    }

    /**
     * Called each tick as long the ItemStack in on player inventory. Used to progress the pickup animation and update
     * maps.
     */
    public void inventoryTick(World worldIn, Entity entityIn, int inventorySlot, boolean isCurrentItem)
    {
        if (this.animationsToGo > 0)
        {
            --this.animationsToGo;
        }

        if (this.item != null)
        {
            this.item.inventoryTick(this, worldIn, entityIn, inventorySlot, isCurrentItem);
        }
    }

    public void onCrafting(World worldIn, EntityPlayer playerIn, int amount)
    {
        playerIn.addStat(StatList.func_188060_a(this.item), amount);
        this.getItem().onCreated(this, worldIn, playerIn);
    }

    public int getUseDuration()
    {
        return this.getItem().getUseDuration(this);
    }

    public EnumAction getUseAction()
    {
        return this.getItem().getUseAction(this);
    }

    /**
     * Called when the player releases the use item button.
     */
    public void onPlayerStoppedUsing(World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        this.getItem().onPlayerStoppedUsing(this, worldIn, entityLiving, timeLeft);
    }

    /**
     * Returns true if the ItemStack has an NBTTagCompound. Currently used to store enchantments.
     */
    public boolean hasTag()
    {
        return !this.isEmpty && this.tag != null;
    }

    @Nullable
    public NBTTagCompound getTag()
    {
        return this.tag;
    }

    public NBTTagCompound getOrCreateChildTag(String key)
    {
        if (this.tag != null && this.tag.contains(key, 10))
        {
            return this.tag.getCompound(key);
        }
        else
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            this.setTagInfo(key, nbttagcompound);
            return nbttagcompound;
        }
    }

    @Nullable

    /**
     * Get an NBTTagCompound from this stack's NBT data.
     */
    public NBTTagCompound getChildTag(String key)
    {
        return this.tag != null && this.tag.contains(key, 10) ? this.tag.getCompound(key) : null;
    }

    public void func_190919_e(String p_190919_1_)
    {
        if (this.tag != null && this.tag.contains(p_190919_1_, 10))
        {
            this.tag.remove(p_190919_1_);
        }
    }

    public NBTTagList getEnchantmentTagList()
    {
        return this.tag != null ? this.tag.getList("ench", 10) : new NBTTagList();
    }

    /**
     * Assigns a NBTTagCompound to the ItemStack, minecraft validates that only non-stackable items can have it.
     */
    public void setTag(@Nullable NBTTagCompound nbt)
    {
        this.tag = nbt;
    }

    public String func_82833_r()
    {
        NBTTagCompound nbttagcompound = this.getChildTag("display");

        if (nbttagcompound != null)
        {
            if (nbttagcompound.contains("Name", 8))
            {
                return nbttagcompound.getString("Name");
            }

            if (nbttagcompound.contains("LocName", 8))
            {
                return I18n.func_74838_a(nbttagcompound.getString("LocName"));
            }
        }

        return this.getItem().func_77653_i(this);
    }

    public ItemStack func_190924_f(String p_190924_1_)
    {
        this.getOrCreateChildTag("display").putString("LocName", p_190924_1_);
        return this;
    }

    public ItemStack func_151001_c(String p_151001_1_)
    {
        this.getOrCreateChildTag("display").putString("Name", p_151001_1_);
        return this;
    }

    /**
     * Clear any custom name set for this ItemStack
     */
    public void clearCustomName()
    {
        NBTTagCompound nbttagcompound = this.getChildTag("display");

        if (nbttagcompound != null)
        {
            nbttagcompound.remove("Name");

            if (nbttagcompound.func_82582_d())
            {
                this.func_190919_e("display");
            }
        }

        if (this.tag != null && this.tag.func_82582_d())
        {
            this.tag = null;
        }
    }

    /**
     * Returns true if the itemstack has a display name
     */
    public boolean hasDisplayName()
    {
        NBTTagCompound nbttagcompound = this.getChildTag("display");
        return nbttagcompound != null && nbttagcompound.contains("Name", 8);
    }

    public List<String> getTooltip(@Nullable EntityPlayer playerIn, ITooltipFlag advanced)
    {
        List<String> list = Lists.<String>newArrayList();
        String s = this.func_82833_r();

        if (this.hasDisplayName())
        {
            s = TextFormatting.ITALIC + s;
        }

        s = s + TextFormatting.RESET;

        if (advanced.isAdvanced())
        {
            String s1 = "";

            if (!s.isEmpty())
            {
                s = s + " (";
                s1 = ")";
            }

            int i = Item.getIdFromItem(this.item);

            if (this.func_77981_g())
            {
                s = s + String.format("#%04d/%d%s", i, this.field_77991_e, s1);
            }
            else
            {
                s = s + String.format("#%04d%s", i, s1);
            }
        }
        else if (!this.hasDisplayName() && this.item == Items.FILLED_MAP)
        {
            s = s + " #" + this.field_77991_e;
        }

        list.add(s);
        int i1 = 0;

        if (this.hasTag() && this.tag.contains("HideFlags", 99))
        {
            i1 = this.tag.getInt("HideFlags");
        }

        if ((i1 & 32) == 0)
        {
            this.getItem().addInformation(this, playerIn == null ? null : playerIn.world, list, advanced);
        }

        if (this.hasTag())
        {
            if ((i1 & 1) == 0)
            {
                NBTTagList nbttaglist = this.getEnchantmentTagList();

                for (int j = 0; j < nbttaglist.func_74745_c(); ++j)
                {
                    NBTTagCompound nbttagcompound = nbttaglist.getCompound(j);
                    int k = nbttagcompound.getShort("id");
                    int l = nbttagcompound.getShort("lvl");
                    Enchantment enchantment = Enchantment.getEnchantmentByID(k);

                    if (enchantment != null)
                    {
                        list.add(enchantment.func_77316_c(l));
                    }
                }
            }

            if (this.tag.contains("display", 10))
            {
                NBTTagCompound nbttagcompound1 = this.tag.getCompound("display");

                if (nbttagcompound1.contains("color", 3))
                {
                    if (advanced.isAdvanced())
                    {
                        list.add(I18n.func_74837_a("item.color", String.format("#%06X", nbttagcompound1.getInt("color"))));
                    }
                    else
                    {
                        list.add(TextFormatting.ITALIC + I18n.func_74838_a("item.dyed"));
                    }
                }

                if (nbttagcompound1.getTagId("Lore") == 9)
                {
                    NBTTagList nbttaglist3 = nbttagcompound1.getList("Lore", 8);

                    if (!nbttaglist3.func_82582_d())
                    {
                        for (int l1 = 0; l1 < nbttaglist3.func_74745_c(); ++l1)
                        {
                            list.add(TextFormatting.DARK_PURPLE + "" + TextFormatting.ITALIC + nbttaglist3.getString(l1));
                        }
                    }
                }
            }
        }

        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
        {
            Multimap<String, AttributeModifier> multimap = this.getAttributeModifiers(entityequipmentslot);

            if (!multimap.isEmpty() && (i1 & 2) == 0)
            {
                list.add("");
                list.add(I18n.func_74838_a("item.modifiers." + entityequipmentslot.getName()));

                for (Entry<String, AttributeModifier> entry : multimap.entries())
                {
                    AttributeModifier attributemodifier = entry.getValue();
                    double d0 = attributemodifier.getAmount();
                    boolean flag = false;

                    if (playerIn != null)
                    {
                        if (attributemodifier.getID() == Item.ATTACK_DAMAGE_MODIFIER)
                        {
                            d0 = d0 + playerIn.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
                            d0 = d0 + (double)EnchantmentHelper.getModifierForCreature(this, EnumCreatureAttribute.UNDEFINED);
                            flag = true;
                        }
                        else if (attributemodifier.getID() == Item.ATTACK_SPEED_MODIFIER)
                        {
                            d0 += playerIn.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue();
                            flag = true;
                        }
                    }

                    double d1;

                    if (attributemodifier.func_111169_c() != 1 && attributemodifier.func_111169_c() != 2)
                    {
                        d1 = d0;
                    }
                    else
                    {
                        d1 = d0 * 100.0D;
                    }

                    if (flag)
                    {
                        list.add(" " + I18n.func_74837_a("attribute.modifier.equals." + attributemodifier.func_111169_c(), DECIMALFORMAT.format(d1), I18n.func_74838_a("attribute.name." + (String)entry.getKey())));
                    }
                    else if (d0 > 0.0D)
                    {
                        list.add(TextFormatting.BLUE + " " + I18n.func_74837_a("attribute.modifier.plus." + attributemodifier.func_111169_c(), DECIMALFORMAT.format(d1), I18n.func_74838_a("attribute.name." + (String)entry.getKey())));
                    }
                    else if (d0 < 0.0D)
                    {
                        d1 = d1 * -1.0D;
                        list.add(TextFormatting.RED + " " + I18n.func_74837_a("attribute.modifier.take." + attributemodifier.func_111169_c(), DECIMALFORMAT.format(d1), I18n.func_74838_a("attribute.name." + (String)entry.getKey())));
                    }
                }
            }
        }

        if (this.hasTag() && this.getTag().getBoolean("Unbreakable") && (i1 & 4) == 0)
        {
            list.add(TextFormatting.BLUE + I18n.func_74838_a("item.unbreakable"));
        }

        if (this.hasTag() && this.tag.contains("CanDestroy", 9) && (i1 & 8) == 0)
        {
            NBTTagList nbttaglist1 = this.tag.getList("CanDestroy", 8);

            if (!nbttaglist1.func_82582_d())
            {
                list.add("");
                list.add(TextFormatting.GRAY + I18n.func_74838_a("item.canBreak"));

                for (int j1 = 0; j1 < nbttaglist1.func_74745_c(); ++j1)
                {
                    Block block = Block.func_149684_b(nbttaglist1.getString(j1));

                    if (block != null)
                    {
                        list.add(TextFormatting.DARK_GRAY + block.func_149732_F());
                    }
                    else
                    {
                        list.add(TextFormatting.DARK_GRAY + "missingno");
                    }
                }
            }
        }

        if (this.hasTag() && this.tag.contains("CanPlaceOn", 9) && (i1 & 16) == 0)
        {
            NBTTagList nbttaglist2 = this.tag.getList("CanPlaceOn", 8);

            if (!nbttaglist2.func_82582_d())
            {
                list.add("");
                list.add(TextFormatting.GRAY + I18n.func_74838_a("item.canPlace"));

                for (int k1 = 0; k1 < nbttaglist2.func_74745_c(); ++k1)
                {
                    Block block1 = Block.func_149684_b(nbttaglist2.getString(k1));

                    if (block1 != null)
                    {
                        list.add(TextFormatting.DARK_GRAY + block1.func_149732_F());
                    }
                    else
                    {
                        list.add(TextFormatting.DARK_GRAY + "missingno");
                    }
                }
            }
        }

        if (advanced.isAdvanced())
        {
            if (this.isDamaged())
            {
                list.add(I18n.func_74837_a("item.durability", this.getMaxDamage() - this.getDamage(), this.getMaxDamage()));
            }

            list.add(TextFormatting.DARK_GRAY + ((ResourceLocation)Item.field_150901_e.getKey(this.item)).toString());

            if (this.hasTag())
            {
                list.add(TextFormatting.DARK_GRAY + I18n.func_74837_a("item.nbt_tags", this.getTag().keySet().size()));
            }
        }

        return list;
    }

    public boolean hasEffect()
    {
        return this.getItem().hasEffect(this);
    }

    public EnumRarity getRarity()
    {
        return this.getItem().getRarity(this);
    }

    /**
     * True if it is a tool and has no enchantments to begin with
     */
    public boolean isEnchantable()
    {
        if (!this.getItem().isEnchantable(this))
        {
            return false;
        }
        else
        {
            return !this.isEnchanted();
        }
    }

    /**
     * Adds an enchantment with a desired level on the ItemStack.
     */
    public void addEnchantment(Enchantment ench, int level)
    {
        if (this.tag == null)
        {
            this.setTag(new NBTTagCompound());
        }

        if (!this.tag.contains("ench", 9))
        {
            this.tag.func_74782_a("ench", new NBTTagList());
        }

        NBTTagList nbttaglist = this.tag.getList("ench", 10);
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.putShort("id", (short)Enchantment.func_185258_b(ench));
        nbttagcompound.putShort("lvl", (short)((byte)level));
        nbttaglist.func_74742_a(nbttagcompound);
    }

    /**
     * True if the item has enchantment data
     */
    public boolean isEnchanted()
    {
        if (this.tag != null && this.tag.contains("ench", 9))
        {
            return !this.tag.getList("ench", 10).func_82582_d();
        }
        else
        {
            return false;
        }
    }

    public void setTagInfo(String key, NBTBase value)
    {
        if (this.tag == null)
        {
            this.setTag(new NBTTagCompound());
        }

        this.tag.func_74782_a(key, value);
    }

    public boolean func_82835_x()
    {
        return this.getItem().func_82788_x();
    }

    /**
     * Return whether this stack is on an item frame.
     */
    public boolean isOnItemFrame()
    {
        return this.itemFrame != null;
    }

    /**
     * Set the item frame this stack is on.
     */
    public void setItemFrame(EntityItemFrame frame)
    {
        this.itemFrame = frame;
    }

    @Nullable

    /**
     * Return the item frame this stack is on. Returns null if not on an item frame.
     */
    public EntityItemFrame getItemFrame()
    {
        return this.isEmpty ? null : this.itemFrame;
    }

    /**
     * Get this stack's repair cost, or 0 if no repair cost is defined.
     */
    public int getRepairCost()
    {
        return this.hasTag() && this.tag.contains("RepairCost", 3) ? this.tag.getInt("RepairCost") : 0;
    }

    /**
     * Set this stack's repair cost.
     */
    public void setRepairCost(int cost)
    {
        if (!this.hasTag())
        {
            this.tag = new NBTTagCompound();
        }

        this.tag.putInt("RepairCost", cost);
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap;

        if (this.hasTag() && this.tag.contains("AttributeModifiers", 9))
        {
            multimap = HashMultimap.<String, AttributeModifier>create();
            NBTTagList nbttaglist = this.tag.getList("AttributeModifiers", 10);

            for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);
                AttributeModifier attributemodifier = SharedMonsterAttributes.readAttributeModifier(nbttagcompound);

                if (attributemodifier != null && (!nbttagcompound.contains("Slot", 8) || nbttagcompound.getString("Slot").equals(equipmentSlot.getName())) && attributemodifier.getID().getLeastSignificantBits() != 0L && attributemodifier.getID().getMostSignificantBits() != 0L)
                {
                    multimap.put(nbttagcompound.getString("AttributeName"), attributemodifier);
                }
            }
        }
        else
        {
            multimap = this.getItem().getAttributeModifiers(equipmentSlot);
        }

        return multimap;
    }

    public void addAttributeModifier(String attributeName, AttributeModifier modifier, @Nullable EntityEquipmentSlot equipmentSlot)
    {
        if (this.tag == null)
        {
            this.tag = new NBTTagCompound();
        }

        if (!this.tag.contains("AttributeModifiers", 9))
        {
            this.tag.func_74782_a("AttributeModifiers", new NBTTagList());
        }

        NBTTagList nbttaglist = this.tag.getList("AttributeModifiers", 10);
        NBTTagCompound nbttagcompound = SharedMonsterAttributes.writeAttributeModifier(modifier);
        nbttagcompound.putString("AttributeName", attributeName);

        if (equipmentSlot != null)
        {
            nbttagcompound.putString("Slot", equipmentSlot.getName());
        }

        nbttaglist.func_74742_a(nbttagcompound);
    }

    /**
     * Get a ChatComponent for this Item's display name that shows this Item on hover
     */
    public ITextComponent getTextComponent()
    {
        TextComponentString textcomponentstring = new TextComponentString(this.func_82833_r());

        if (this.hasDisplayName())
        {
            textcomponentstring.getStyle().setItalic(Boolean.valueOf(true));
        }

        ITextComponent itextcomponent = (new TextComponentString("[")).appendSibling(textcomponentstring).appendText("]");

        if (!this.isEmpty)
        {
            NBTTagCompound nbttagcompound = this.write(new NBTTagCompound());
            itextcomponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new TextComponentString(nbttagcompound.toString())));
            itextcomponent.getStyle().setColor(this.getRarity().color);
        }

        return itextcomponent;
    }

    public boolean func_179544_c(Block p_179544_1_)
    {
        if (p_179544_1_ == this.canDestroyCacheBlock)
        {
            return this.canDestroyCacheResult;
        }
        else
        {
            this.canDestroyCacheBlock = p_179544_1_;

            if (this.hasTag() && this.tag.contains("CanDestroy", 9))
            {
                NBTTagList nbttaglist = this.tag.getList("CanDestroy", 8);

                for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                {
                    Block block = Block.func_149684_b(nbttaglist.getString(i));

                    if (block == p_179544_1_)
                    {
                        this.canDestroyCacheResult = true;
                        return true;
                    }
                }
            }

            this.canDestroyCacheResult = false;
            return false;
        }
    }

    public boolean func_179547_d(Block p_179547_1_)
    {
        if (p_179547_1_ == this.canPlaceOnCacheBlock)
        {
            return this.canPlaceOnCacheResult;
        }
        else
        {
            this.canPlaceOnCacheBlock = p_179547_1_;

            if (this.hasTag() && this.tag.contains("CanPlaceOn", 9))
            {
                NBTTagList nbttaglist = this.tag.getList("CanPlaceOn", 8);

                for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                {
                    Block block = Block.func_149684_b(nbttaglist.getString(i));

                    if (block == p_179547_1_)
                    {
                        this.canPlaceOnCacheResult = true;
                        return true;
                    }
                }
            }

            this.canPlaceOnCacheResult = false;
            return false;
        }
    }

    public int getAnimationsToGo()
    {
        return this.animationsToGo;
    }

    public void setAnimationsToGo(int animations)
    {
        this.animationsToGo = animations;
    }

    public int getCount()
    {
        return this.isEmpty ? 0 : this.count;
    }

    public void setCount(int count)
    {
        this.count = count;
        this.updateEmptyState();
    }

    public void grow(int count)
    {
        this.setCount(this.count + count);
    }

    public void shrink(int count)
    {
        this.grow(-count);
    }
}

package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemBow extends Item
{
    public ItemBow()
    {
        this.maxStackSize = 1;
        this.func_77656_e(384);
        this.func_77637_a(CreativeTabs.COMBAT);
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
        {
            public float func_185085_a(ItemStack p_185085_1_, @Nullable World p_185085_2_, @Nullable EntityLivingBase p_185085_3_)
            {
                if (p_185085_3_ == null)
                {
                    return 0.0F;
                }
                else
                {
                    return p_185085_3_.getActiveItemStack().getItem() != Items.BOW ? 0.0F : (float)(p_185085_1_.getUseDuration() - p_185085_3_.getItemInUseCount()) / 20.0F;
                }
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter()
        {
            public float func_185085_a(ItemStack p_185085_1_, @Nullable World p_185085_2_, @Nullable EntityLivingBase p_185085_3_)
            {
                return p_185085_3_ != null && p_185085_3_.isHandActive() && p_185085_3_.getActiveItemStack() == p_185085_1_ ? 1.0F : 0.0F;
            }
        });
    }

    private ItemStack func_185060_a(EntityPlayer p_185060_1_)
    {
        if (this.func_185058_h_(p_185060_1_.getHeldItem(EnumHand.OFF_HAND)))
        {
            return p_185060_1_.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (this.func_185058_h_(p_185060_1_.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return p_185060_1_.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < p_185060_1_.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = p_185060_1_.inventory.getStackInSlot(i);

                if (this.func_185058_h_(itemstack))
                {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    protected boolean func_185058_h_(ItemStack p_185058_1_)
    {
        return p_185058_1_.getItem() instanceof ItemArrow;
    }

    /**
     * Called when the player stops using an Item (stops holding the right mouse button).
     */
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            boolean flag = entityplayer.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = this.func_185060_a(entityplayer);

            if (!itemstack.isEmpty() || flag)
            {
                if (itemstack.isEmpty())
                {
                    itemstack = new ItemStack(Items.ARROW);
                }

                int i = this.getUseDuration(stack) - timeLeft;
                float f = getArrowVelocity(i);

                if ((double)f >= 0.1D)
                {
                    boolean flag1 = flag && itemstack.getItem() == Items.ARROW;

                    if (!worldIn.isRemote)
                    {
                        ItemArrow itemarrow = (ItemArrow)(itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Items.ARROW);
                        EntityArrow entityarrow = itemarrow.func_185052_a(worldIn, itemstack, entityplayer);
                        entityarrow.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

                        if (f == 1.0F)
                        {
                            entityarrow.setIsCritical(true);
                        }

                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                        if (j > 0)
                        {
                            entityarrow.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

                        if (k > 0)
                        {
                            entityarrow.setKnockbackStrength(k);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
                        {
                            entityarrow.setFire(100);
                        }

                        stack.func_77972_a(1, entityplayer);

                        if (flag1 || entityplayer.abilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW))
                        {
                            entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }

                        worldIn.addEntity0(entityarrow);
                    }

                    worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    if (!flag1 && !entityplayer.abilities.isCreativeMode)
                    {
                        itemstack.shrink(1);

                        if (itemstack.isEmpty())
                        {
                            entityplayer.inventory.deleteStack(itemstack);
                        }
                    }

                    entityplayer.addStat(StatList.func_188057_b(this));
                }
            }
        }
    }

    /**
     * Gets the velocity of the arrow entity from the bow's charge
     */
    public static float getArrowVelocity(int charge)
    {
        float f = (float)charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        return f;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack)
    {
        return 72000;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        boolean flag = !this.func_185060_a(playerIn).isEmpty();

        if (!playerIn.abilities.isCreativeMode && !flag)
        {
            return flag ? new ActionResult(EnumActionResult.PASS, itemstack) : new ActionResult(EnumActionResult.FAIL, itemstack);
        }
        else
        {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return 1;
    }
}
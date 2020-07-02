package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemFood extends Item
{
    public final int field_77855_a;
    private final int field_77853_b;
    private final float field_77854_c;
    private final boolean field_77856_bY;
    private boolean field_77852_bZ;
    private PotionEffect field_77851_ca;
    private float field_77858_cd;

    public ItemFood(int p_i45339_1_, float p_i45339_2_, boolean p_i45339_3_)
    {
        this.field_77855_a = 32;
        this.field_77853_b = p_i45339_1_;
        this.field_77856_bY = p_i45339_3_;
        this.field_77854_c = p_i45339_2_;
        this.func_77637_a(CreativeTabs.FOOD);
    }

    public ItemFood(int p_i45340_1_, boolean p_i45340_2_)
    {
        this(p_i45340_1_, 0.6F, p_i45340_2_);
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            entityplayer.getFoodStats().func_151686_a(this, stack);
            worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            this.func_77849_c(stack, worldIn, entityplayer);
            entityplayer.addStat(StatList.func_188057_b(this));

            if (entityplayer instanceof EntityPlayerMP)
            {
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
            }
        }

        stack.shrink(1);
        return stack;
    }

    protected void func_77849_c(ItemStack p_77849_1_, World p_77849_2_, EntityPlayer p_77849_3_)
    {
        if (!p_77849_2_.isRemote && this.field_77851_ca != null && p_77849_2_.rand.nextFloat() < this.field_77858_cd)
        {
            p_77849_3_.func_70690_d(new PotionEffect(this.field_77851_ca));
        }
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack)
    {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getUseAction(ItemStack stack)
    {
        return EnumAction.EAT;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (playerIn.canEat(this.field_77852_bZ))
        {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        else
        {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
    }

    public int func_150905_g(ItemStack p_150905_1_)
    {
        return this.field_77853_b;
    }

    public float func_150906_h(ItemStack p_150906_1_)
    {
        return this.field_77854_c;
    }

    public boolean func_77845_h()
    {
        return this.field_77856_bY;
    }

    public ItemFood func_185070_a(PotionEffect p_185070_1_, float p_185070_2_)
    {
        this.field_77851_ca = p_185070_1_;
        this.field_77858_cd = p_185070_2_;
        return this;
    }

    public ItemFood func_77848_i()
    {
        this.field_77852_bZ = true;
        return this;
    }
}

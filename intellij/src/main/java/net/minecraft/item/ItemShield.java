package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemShield extends Item
{
    public ItemShield()
    {
        this.maxStackSize = 1;
        this.func_77637_a(CreativeTabs.COMBAT);
        this.func_77656_e(336);
        this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter()
        {
            public float func_185085_a(ItemStack p_185085_1_, @Nullable World p_185085_2_, @Nullable EntityLivingBase p_185085_3_)
            {
                return p_185085_3_ != null && p_185085_3_.isHandActive() && p_185085_3_.getActiveItemStack() == p_185085_1_ ? 1.0F : 0.0F;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.func_82595_a(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

    public String func_77653_i(ItemStack p_77653_1_)
    {
        if (p_77653_1_.getChildTag("BlockEntityTag") != null)
        {
            EnumDyeColor enumdyecolor = TileEntityBanner.func_190616_d(p_77653_1_);
            return I18n.func_74838_a("item.shield." + enumdyecolor.getTranslationKey() + ".name");
        }
        else
        {
            return I18n.func_74838_a("item.shield.name");
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        ItemBanner.appendHoverTextFromTileEntityTag(stack, tooltip);
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getUseAction(ItemStack stack)
    {
        return EnumAction.BLOCK;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack)
    {
        return 72000;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem() == Item.getItemFromBlock(Blocks.field_150344_f) ? true : super.getIsRepairable(toRepair, repair);
    }
}

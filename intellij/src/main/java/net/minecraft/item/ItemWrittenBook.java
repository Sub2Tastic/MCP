package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemWrittenBook extends Item
{
    public ItemWrittenBook()
    {
        this.func_77625_d(1);
    }

    public static boolean validBookTagContents(NBTTagCompound nbt)
    {
        if (!ItemWritableBook.isNBTValid(nbt))
        {
            return false;
        }
        else if (!nbt.contains("title", 8))
        {
            return false;
        }
        else
        {
            String s = nbt.getString("title");
            return s != null && s.length() <= 32 ? nbt.contains("author", 8) : false;
        }
    }

    /**
     * Gets the generation of the book (how many times it has been cloned)
     */
    public static int getGeneration(ItemStack book)
    {
        return book.getTag().getInt("generation");
    }

    public String func_77653_i(ItemStack p_77653_1_)
    {
        if (p_77653_1_.hasTag())
        {
            NBTTagCompound nbttagcompound = p_77653_1_.getTag();
            String s = nbttagcompound.getString("title");

            if (!StringUtils.isNullOrEmpty(s))
            {
                return s;
            }
        }

        return super.func_77653_i(p_77653_1_);
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (stack.hasTag())
        {
            NBTTagCompound nbttagcompound = stack.getTag();
            String s = nbttagcompound.getString("author");

            if (!StringUtils.isNullOrEmpty(s))
            {
                tooltip.add(TextFormatting.GRAY + I18n.func_74837_a("book.byAuthor", s));
            }

            tooltip.add(TextFormatting.GRAY + I18n.func_74838_a("book.generation." + nbttagcompound.getInt("generation")));
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!worldIn.isRemote)
        {
            this.func_179229_a(itemstack, playerIn);
        }

        playerIn.openBook(itemstack, handIn);
        playerIn.addStat(StatList.func_188057_b(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

    private void func_179229_a(ItemStack p_179229_1_, EntityPlayer p_179229_2_)
    {
        if (p_179229_1_.getTag() != null)
        {
            NBTTagCompound nbttagcompound = p_179229_1_.getTag();

            if (!nbttagcompound.getBoolean("resolved"))
            {
                nbttagcompound.putBoolean("resolved", true);

                if (validBookTagContents(nbttagcompound))
                {
                    NBTTagList nbttaglist = nbttagcompound.getList("pages", 8);

                    for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                    {
                        String s = nbttaglist.getString(i);
                        ITextComponent itextcomponent;

                        try
                        {
                            itextcomponent = ITextComponent.Serializer.fromJsonLenient(s);
                            itextcomponent = TextComponentUtils.func_179985_a(p_179229_2_, itextcomponent, p_179229_2_);
                        }
                        catch (Exception var9)
                        {
                            itextcomponent = new TextComponentString(s);
                        }

                        nbttaglist.func_150304_a(i, new NBTTagString(ITextComponent.Serializer.toJson(itextcomponent)));
                    }

                    nbttagcompound.func_74782_a("pages", nbttaglist);

                    if (p_179229_2_ instanceof EntityPlayerMP && p_179229_2_.getHeldItemMainhand() == p_179229_1_)
                    {
                        Slot slot = p_179229_2_.openContainer.func_75147_a(p_179229_2_.inventory, p_179229_2_.inventory.currentItem);
                        ((EntityPlayerMP)p_179229_2_).connection.sendPacket(new SPacketSetSlot(0, slot.slotNumber, p_179229_1_));
                    }
                }
            }
        }
    }

    /**
     * Returns true if this item has an enchantment glint. By default, this returns
     * <code>stack.isItemEnchanted()</code>, but other items can override it (for instance, written books always return
     * true).
     *  
     * Note that if you override this method, you generally want to also call the super version (on {@link Item}) to get
     * the glint for enchanted items. Of course, that is unnecessary if the overwritten version always returns true.
     */
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
}

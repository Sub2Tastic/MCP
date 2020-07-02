package net.minecraft.item;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemFirework extends Item
{
    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        if (!p_180614_2_.isRemote)
        {
            ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);
            EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(p_180614_2_, (double)((float)p_180614_3_.getX() + p_180614_6_), (double)((float)p_180614_3_.getY() + p_180614_7_), (double)((float)p_180614_3_.getZ() + p_180614_8_), itemstack);
            p_180614_2_.addEntity0(entityfireworkrocket);

            if (!p_180614_1_.abilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }
        }

        return EnumActionResult.SUCCESS;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (playerIn.isElytraFlying())
        {
            ItemStack itemstack = playerIn.getHeldItem(handIn);

            if (!worldIn.isRemote)
            {
                EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(worldIn, itemstack, playerIn);
                worldIn.addEntity0(entityfireworkrocket);

                if (!playerIn.abilities.isCreativeMode)
                {
                    itemstack.shrink(1);
                }
            }

            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        else
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        NBTTagCompound nbttagcompound = stack.getChildTag("Fireworks");

        if (nbttagcompound != null)
        {
            if (nbttagcompound.contains("Flight", 99))
            {
                tooltip.add(I18n.func_74838_a("item.fireworks.flight") + " " + nbttagcompound.getByte("Flight"));
            }

            NBTTagList nbttaglist = nbttagcompound.getList("Explosions", 10);

            if (!nbttaglist.func_82582_d())
            {
                for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                {
                    NBTTagCompound nbttagcompound1 = nbttaglist.getCompound(i);
                    List<String> list = Lists.<String>newArrayList();
                    ItemFireworkCharge.func_150902_a(nbttagcompound1, list);

                    if (!list.isEmpty())
                    {
                        for (int j = 1; j < list.size(); ++j)
                        {
                            list.set(j, "  " + (String)list.get(j));
                        }

                        tooltip.addAll(list);
                    }
                }
            }
        }
    }
}

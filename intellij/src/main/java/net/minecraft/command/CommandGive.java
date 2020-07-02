package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class CommandGive extends CommandBase
{
    public String func_71517_b()
    {
        return "give";
    }

    public int func_82362_a()
    {
        return 2;
    }

    public String func_71518_a(ICommandSender p_71518_1_)
    {
        return "commands.give.usage";
    }

    public void func_184881_a(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException
    {
        if (p_184881_3_.length < 2)
        {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        }
        else
        {
            EntityPlayer entityplayer = func_184888_a(p_184881_1_, p_184881_2_, p_184881_3_[0]);
            Item item = func_147179_f(p_184881_2_, p_184881_3_[1]);
            int i = p_184881_3_.length >= 3 ? func_175764_a(p_184881_3_[2], 1, item.getMaxStackSize()) : 1;
            int j = p_184881_3_.length >= 4 ? func_175755_a(p_184881_3_[3]) : 0;
            ItemStack itemstack = new ItemStack(item, i, j);

            if (p_184881_3_.length >= 5)
            {
                String s = func_180529_a(p_184881_3_, 4);

                try
                {
                    itemstack.setTag(JsonToNBT.getTagFromJson(s));
                }
                catch (NBTException nbtexception)
                {
                    throw new CommandException("commands.give.tagError", new Object[] {nbtexception.getMessage()});
                }
            }

            boolean flag = entityplayer.inventory.addItemStackToInventory(itemstack);

            if (flag)
            {
                entityplayer.world.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityplayer.container.detectAndSendChanges();
            }

            if (flag && itemstack.isEmpty())
            {
                itemstack.setCount(1);
                p_184881_2_.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, i);
                EntityItem entityitem1 = entityplayer.dropItem(itemstack, false);

                if (entityitem1 != null)
                {
                    entityitem1.makeFakeItem();
                }
            }
            else
            {
                p_184881_2_.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, i - itemstack.getCount());
                EntityItem entityitem = entityplayer.dropItem(itemstack, false);

                if (entityitem != null)
                {
                    entityitem.setNoPickupDelay();
                    entityitem.func_145797_a(entityplayer.func_70005_c_());
                }
            }

            func_152373_a(p_184881_2_, this, "commands.give.success", new Object[] {itemstack.getTextComponent(), i, entityplayer.func_70005_c_()});
        }
    }

    public List<String> func_184883_a(MinecraftServer p_184883_1_, ICommandSender p_184883_2_, String[] p_184883_3_, @Nullable BlockPos p_184883_4_)
    {
        if (p_184883_3_.length == 1)
        {
            return func_71530_a(p_184883_3_, p_184883_1_.getOnlinePlayerNames());
        }
        else
        {
            return p_184883_3_.length == 2 ? func_175762_a(p_184883_3_, Item.field_150901_e.keySet()) : Collections.emptyList();
        }
    }

    public boolean func_82358_a(String[] p_82358_1_, int p_82358_2_)
    {
        return p_82358_2_ == 0;
    }
}

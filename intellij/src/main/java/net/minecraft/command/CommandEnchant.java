package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandEnchant extends CommandBase
{
    public String func_71517_b()
    {
        return "enchant";
    }

    public int func_82362_a()
    {
        return 2;
    }

    public String func_71518_a(ICommandSender p_71518_1_)
    {
        return "commands.enchant.usage";
    }

    public void func_184881_a(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException
    {
        if (p_184881_3_.length < 2)
        {
            throw new WrongUsageException("commands.enchant.usage", new Object[0]);
        }
        else
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)func_184884_a(p_184881_1_, p_184881_2_, p_184881_3_[0], EntityLivingBase.class);
            p_184881_2_.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 0);
            Enchantment enchantment;

            try
            {
                enchantment = Enchantment.getEnchantmentByID(func_180528_a(p_184881_3_[1], 0));
            }
            catch (NumberInvalidException var12)
            {
                enchantment = Enchantment.func_180305_b(p_184881_3_[1]);
            }

            if (enchantment == null)
            {
                throw new NumberInvalidException("commands.enchant.notFound", new Object[] {p_184881_3_[1]});
            }
            else
            {
                int i = 1;
                ItemStack itemstack = entitylivingbase.getHeldItemMainhand();

                if (itemstack.isEmpty())
                {
                    throw new CommandException("commands.enchant.noItem", new Object[0]);
                }
                else if (!enchantment.canApply(itemstack))
                {
                    throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
                }
                else
                {
                    if (p_184881_3_.length >= 3)
                    {
                        i = func_175764_a(p_184881_3_[2], enchantment.getMinLevel(), enchantment.getMaxLevel());
                    }

                    if (itemstack.hasTag())
                    {
                        NBTTagList nbttaglist = itemstack.getEnchantmentTagList();

                        for (int j = 0; j < nbttaglist.func_74745_c(); ++j)
                        {
                            int k = nbttaglist.getCompound(j).getShort("id");

                            if (Enchantment.getEnchantmentByID(k) != null)
                            {
                                Enchantment enchantment1 = Enchantment.getEnchantmentByID(k);

                                if (!enchantment.isCompatibleWith(enchantment1))
                                {
                                    throw new CommandException("commands.enchant.cantCombine", new Object[] {enchantment.func_77316_c(i), enchantment1.func_77316_c(nbttaglist.getCompound(j).getShort("lvl"))});
                                }
                            }
                        }
                    }

                    itemstack.addEnchantment(enchantment, i);
                    func_152373_a(p_184881_2_, this, "commands.enchant.success", new Object[0]);
                    p_184881_2_.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 1);
                }
            }
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
            return p_184883_3_.length == 2 ? func_175762_a(p_184883_3_, Enchantment.field_185264_b.keySet()) : Collections.emptyList();
        }
    }

    public boolean func_82358_a(String[] p_82358_1_, int p_82358_2_)
    {
        return p_82358_2_ == 0;
    }
}

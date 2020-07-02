package net.minecraft.village;

import java.io.IOException;
import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;

public class MerchantRecipeList extends ArrayList<MerchantRecipe>
{
    public MerchantRecipeList()
    {
    }

    public MerchantRecipeList(NBTTagCompound p_i1944_1_)
    {
        this.func_77201_a(p_i1944_1_);
    }

    @Nullable
    public MerchantRecipe func_77203_a(ItemStack p_77203_1_, ItemStack p_77203_2_, int p_77203_3_)
    {
        if (p_77203_3_ > 0 && p_77203_3_ < this.size())
        {
            MerchantRecipe merchantrecipe1 = (MerchantRecipe)this.get(p_77203_3_);
            return !this.func_181078_a(p_77203_1_, merchantrecipe1.func_77394_a()) || (!p_77203_2_.isEmpty() || merchantrecipe1.func_77398_c()) && (!merchantrecipe1.func_77398_c() || !this.func_181078_a(p_77203_2_, merchantrecipe1.func_77396_b())) || p_77203_1_.getCount() < merchantrecipe1.func_77394_a().getCount() || merchantrecipe1.func_77398_c() && p_77203_2_.getCount() < merchantrecipe1.func_77396_b().getCount() ? null : merchantrecipe1;
        }
        else
        {
            for (int i = 0; i < this.size(); ++i)
            {
                MerchantRecipe merchantrecipe = (MerchantRecipe)this.get(i);

                if (this.func_181078_a(p_77203_1_, merchantrecipe.func_77394_a()) && p_77203_1_.getCount() >= merchantrecipe.func_77394_a().getCount() && (!merchantrecipe.func_77398_c() && p_77203_2_.isEmpty() || merchantrecipe.func_77398_c() && this.func_181078_a(p_77203_2_, merchantrecipe.func_77396_b()) && p_77203_2_.getCount() >= merchantrecipe.func_77396_b().getCount()))
                {
                    return merchantrecipe;
                }
            }

            return null;
        }
    }

    private boolean func_181078_a(ItemStack p_181078_1_, ItemStack p_181078_2_)
    {
        return ItemStack.areItemsEqual(p_181078_1_, p_181078_2_) && (!p_181078_2_.hasTag() || p_181078_1_.hasTag() && NBTUtil.areNBTEquals(p_181078_2_.getTag(), p_181078_1_.getTag(), false));
    }

    public void func_151391_a(PacketBuffer p_151391_1_)
    {
        p_151391_1_.writeByte((byte)(this.size() & 255));

        for (int i = 0; i < this.size(); ++i)
        {
            MerchantRecipe merchantrecipe = (MerchantRecipe)this.get(i);
            p_151391_1_.writeItemStack(merchantrecipe.func_77394_a());
            p_151391_1_.writeItemStack(merchantrecipe.func_77397_d());
            ItemStack itemstack = merchantrecipe.func_77396_b();
            p_151391_1_.writeBoolean(!itemstack.isEmpty());

            if (!itemstack.isEmpty())
            {
                p_151391_1_.writeItemStack(itemstack);
            }

            p_151391_1_.writeBoolean(merchantrecipe.func_82784_g());
            p_151391_1_.writeInt(merchantrecipe.func_180321_e());
            p_151391_1_.writeInt(merchantrecipe.func_180320_f());
        }
    }

    public static MerchantRecipeList func_151390_b(PacketBuffer p_151390_0_) throws IOException
    {
        MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
        int i = p_151390_0_.readByte() & 255;

        for (int j = 0; j < i; ++j)
        {
            ItemStack itemstack = p_151390_0_.readItemStack();
            ItemStack itemstack1 = p_151390_0_.readItemStack();
            ItemStack itemstack2 = ItemStack.EMPTY;

            if (p_151390_0_.readBoolean())
            {
                itemstack2 = p_151390_0_.readItemStack();
            }

            boolean flag = p_151390_0_.readBoolean();
            int k = p_151390_0_.readInt();
            int l = p_151390_0_.readInt();
            MerchantRecipe merchantrecipe = new MerchantRecipe(itemstack, itemstack2, itemstack1, k, l);

            if (flag)
            {
                merchantrecipe.func_82785_h();
            }

            merchantrecipelist.add(merchantrecipe);
        }

        return merchantrecipelist;
    }

    public void func_77201_a(NBTTagCompound p_77201_1_)
    {
        NBTTagList nbttaglist = p_77201_1_.getList("Recipes", 10);

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);
            this.add(new MerchantRecipe(nbttagcompound));
        }
    }

    public NBTTagCompound func_77202_a()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.size(); ++i)
        {
            MerchantRecipe merchantrecipe = (MerchantRecipe)this.get(i);
            nbttaglist.func_74742_a(merchantrecipe.func_77395_g());
        }

        nbttagcompound.func_74782_a("Recipes", nbttaglist);
        return nbttagcompound;
    }
}
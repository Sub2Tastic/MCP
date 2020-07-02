package net.minecraft.item;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

public class ItemSkull extends Item
{
    private static final String[] field_82807_a = new String[] {"skeleton", "wither", "zombie", "char", "creeper", "dragon"};

    public ItemSkull()
    {
        this.func_77637_a(CreativeTabs.DECORATIONS);
        this.func_77656_e(0);
        this.func_77627_a(true);
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        if (p_180614_5_ == EnumFacing.DOWN)
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = p_180614_2_.getBlockState(p_180614_3_);
            Block block = iblockstate.getBlock();
            boolean flag = block.func_176200_f(p_180614_2_, p_180614_3_);

            if (!flag)
            {
                if (!p_180614_2_.getBlockState(p_180614_3_).getMaterial().isSolid())
                {
                    return EnumActionResult.FAIL;
                }

                p_180614_3_ = p_180614_3_.offset(p_180614_5_);
            }

            ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

            if (p_180614_1_.canPlayerEdit(p_180614_3_, p_180614_5_, itemstack) && Blocks.field_150465_bP.func_176196_c(p_180614_2_, p_180614_3_))
            {
                if (p_180614_2_.isRemote)
                {
                    return EnumActionResult.SUCCESS;
                }
                else
                {
                    p_180614_2_.setBlockState(p_180614_3_, Blocks.field_150465_bP.getDefaultState().func_177226_a(BlockSkull.field_176418_a, p_180614_5_), 11);
                    int i = 0;

                    if (p_180614_5_ == EnumFacing.UP)
                    {
                        i = MathHelper.floor((double)(p_180614_1_.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
                    }

                    TileEntity tileentity = p_180614_2_.getTileEntity(p_180614_3_);

                    if (tileentity instanceof TileEntitySkull)
                    {
                        TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;

                        if (itemstack.func_77960_j() == 3)
                        {
                            GameProfile gameprofile = null;

                            if (itemstack.hasTag())
                            {
                                NBTTagCompound nbttagcompound = itemstack.getTag();

                                if (nbttagcompound.contains("SkullOwner", 10))
                                {
                                    gameprofile = NBTUtil.readGameProfile(nbttagcompound.getCompound("SkullOwner"));
                                }
                                else if (nbttagcompound.contains("SkullOwner", 8) && !StringUtils.isBlank(nbttagcompound.getString("SkullOwner")))
                                {
                                    gameprofile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                                }
                            }

                            tileentityskull.func_152106_a(gameprofile);
                        }
                        else
                        {
                            tileentityskull.func_152107_a(itemstack.func_77960_j());
                        }

                        tileentityskull.func_145903_a(i);
                        Blocks.field_150465_bP.func_180679_a(p_180614_2_, p_180614_3_, tileentityskull);
                    }

                    if (p_180614_1_ instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)p_180614_1_, p_180614_3_, itemstack);
                    }

                    itemstack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
            }
            else
            {
                return EnumActionResult.FAIL;
            }
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        if (this.isInGroup(group))
        {
            for (int i = 0; i < field_82807_a.length; ++i)
            {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    public int func_77647_b(int p_77647_1_)
    {
        return p_77647_1_;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getTranslationKey(ItemStack stack)
    {
        int i = stack.func_77960_j();

        if (i < 0 || i >= field_82807_a.length)
        {
            i = 0;
        }

        return super.getTranslationKey() + "." + field_82807_a[i];
    }

    public String func_77653_i(ItemStack p_77653_1_)
    {
        if (p_77653_1_.func_77960_j() == 3 && p_77653_1_.hasTag())
        {
            if (p_77653_1_.getTag().contains("SkullOwner", 8))
            {
                return I18n.func_74837_a("item.skull.player.name", p_77653_1_.getTag().getString("SkullOwner"));
            }

            if (p_77653_1_.getTag().contains("SkullOwner", 10))
            {
                NBTTagCompound nbttagcompound = p_77653_1_.getTag().getCompound("SkullOwner");

                if (nbttagcompound.contains("Name", 8))
                {
                    return I18n.func_74837_a("item.skull.player.name", nbttagcompound.getString("Name"));
                }
            }
        }

        return super.func_77653_i(p_77653_1_);
    }

    /**
     * Called when an ItemStack with NBT data is read to potentially that ItemStack's NBT data
     */
    public boolean updateItemStackNBT(NBTTagCompound nbt)
    {
        super.updateItemStackNBT(nbt);

        if (nbt.contains("SkullOwner", 8) && !StringUtils.isBlank(nbt.getString("SkullOwner")))
        {
            GameProfile gameprofile = new GameProfile((UUID)null, nbt.getString("SkullOwner"));
            gameprofile = TileEntitySkull.updateGameProfile(gameprofile);
            nbt.func_74782_a("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
            return true;
        }
        else
        {
            return false;
        }
    }
}

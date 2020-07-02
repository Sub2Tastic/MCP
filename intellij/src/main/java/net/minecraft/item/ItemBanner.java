package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemBanner extends ItemBlock
{
    public ItemBanner()
    {
        super(Blocks.field_180393_cK);
        this.maxStackSize = 16;
        this.func_77637_a(CreativeTabs.DECORATIONS);
        this.func_77627_a(true);
        this.func_77656_e(0);
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        IBlockState iblockstate = p_180614_2_.getBlockState(p_180614_3_);
        boolean flag = iblockstate.getBlock().func_176200_f(p_180614_2_, p_180614_3_);

        if (p_180614_5_ != EnumFacing.DOWN && (iblockstate.getMaterial().isSolid() || flag) && (!flag || p_180614_5_ == EnumFacing.UP))
        {
            p_180614_3_ = p_180614_3_.offset(p_180614_5_);
            ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

            if (p_180614_1_.canPlayerEdit(p_180614_3_, p_180614_5_, itemstack) && Blocks.field_180393_cK.func_176196_c(p_180614_2_, p_180614_3_))
            {
                if (p_180614_2_.isRemote)
                {
                    return EnumActionResult.SUCCESS;
                }
                else
                {
                    p_180614_3_ = flag ? p_180614_3_.down() : p_180614_3_;

                    if (p_180614_5_ == EnumFacing.UP)
                    {
                        int i = MathHelper.floor((double)((p_180614_1_.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
                        p_180614_2_.setBlockState(p_180614_3_, Blocks.field_180393_cK.getDefaultState().func_177226_a(BlockStandingSign.ROTATION, Integer.valueOf(i)), 3);
                    }
                    else
                    {
                        p_180614_2_.setBlockState(p_180614_3_, Blocks.field_180394_cL.getDefaultState().func_177226_a(BlockWallSign.FACING, p_180614_5_), 3);
                    }

                    TileEntity tileentity = p_180614_2_.getTileEntity(p_180614_3_);

                    if (tileentity instanceof TileEntityBanner)
                    {
                        ((TileEntityBanner)tileentity).func_175112_a(itemstack, false);
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
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    public String func_77653_i(ItemStack p_77653_1_)
    {
        String s = "item.banner.";
        EnumDyeColor enumdyecolor = func_179225_h(p_77653_1_);
        s = s + enumdyecolor.getTranslationKey() + ".name";
        return I18n.func_74838_a(s);
    }

    public static void appendHoverTextFromTileEntityTag(ItemStack stack, List<String> p_185054_1_)
    {
        NBTTagCompound nbttagcompound = stack.getChildTag("BlockEntityTag");

        if (nbttagcompound != null && nbttagcompound.contains("Patterns"))
        {
            NBTTagList nbttaglist = nbttagcompound.getList("Patterns", 10);

            for (int i = 0; i < nbttaglist.func_74745_c() && i < 6; ++i)
            {
                NBTTagCompound nbttagcompound1 = nbttaglist.getCompound(i);
                EnumDyeColor enumdyecolor = EnumDyeColor.func_176766_a(nbttagcompound1.getInt("Color"));
                BannerPattern bannerpattern = BannerPattern.byHash(nbttagcompound1.getString("Pattern"));

                if (bannerpattern != null)
                {
                    p_185054_1_.add(I18n.func_74838_a("item.banner." + bannerpattern.getFileName() + "." + enumdyecolor.getTranslationKey()));
                }
            }
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        appendHoverTextFromTileEntityTag(stack, tooltip);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        if (this.isInGroup(group))
        {
            for (EnumDyeColor enumdyecolor : EnumDyeColor.values())
            {
                items.add(func_190910_a(enumdyecolor, (NBTTagList)null));
            }
        }
    }

    public static ItemStack func_190910_a(EnumDyeColor p_190910_0_, @Nullable NBTTagList p_190910_1_)
    {
        ItemStack itemstack = new ItemStack(Items.field_179564_cE, 1, p_190910_0_.func_176767_b());

        if (p_190910_1_ != null && !p_190910_1_.func_82582_d())
        {
            itemstack.getOrCreateChildTag("BlockEntityTag").func_74782_a("Patterns", p_190910_1_.copy());
        }

        return itemstack;
    }

    /**
     * gets the CreativeTab this item is displayed on
     */
    public CreativeTabs getGroup()
    {
        return CreativeTabs.DECORATIONS;
    }

    public static EnumDyeColor func_179225_h(ItemStack p_179225_0_)
    {
        return EnumDyeColor.func_176766_a(p_179225_0_.func_77960_j() & 15);
    }
}

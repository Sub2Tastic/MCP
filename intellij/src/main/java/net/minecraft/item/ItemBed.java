package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemBed extends Item
{
    public ItemBed()
    {
        this.func_77637_a(CreativeTabs.DECORATIONS);
        this.func_77656_e(0);
        this.func_77627_a(true);
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        if (p_180614_2_.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        else if (p_180614_5_ != EnumFacing.UP)
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
                p_180614_3_ = p_180614_3_.up();
            }

            int i = MathHelper.floor((double)(p_180614_1_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            EnumFacing enumfacing = EnumFacing.byHorizontalIndex(i);
            BlockPos blockpos = p_180614_3_.offset(enumfacing);
            ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

            if (p_180614_1_.canPlayerEdit(p_180614_3_, p_180614_5_, itemstack) && p_180614_1_.canPlayerEdit(blockpos, p_180614_5_, itemstack))
            {
                IBlockState iblockstate1 = p_180614_2_.getBlockState(blockpos);
                boolean flag1 = iblockstate1.getBlock().func_176200_f(p_180614_2_, blockpos);
                boolean flag2 = flag || p_180614_2_.isAirBlock(p_180614_3_);
                boolean flag3 = flag1 || p_180614_2_.isAirBlock(blockpos);

                if (flag2 && flag3 && p_180614_2_.getBlockState(p_180614_3_.down()).func_185896_q() && p_180614_2_.getBlockState(blockpos.down()).func_185896_q())
                {
                    IBlockState iblockstate2 = Blocks.field_150324_C.getDefaultState().func_177226_a(BlockBed.OCCUPIED, Boolean.valueOf(false)).func_177226_a(BlockBed.HORIZONTAL_FACING, enumfacing).func_177226_a(BlockBed.PART, BlockBed.EnumPartType.FOOT);
                    p_180614_2_.setBlockState(p_180614_3_, iblockstate2, 10);
                    p_180614_2_.setBlockState(blockpos, iblockstate2.func_177226_a(BlockBed.PART, BlockBed.EnumPartType.HEAD), 10);
                    SoundType soundtype = iblockstate2.getBlock().func_185467_w();
                    p_180614_2_.playSound((EntityPlayer)null, p_180614_3_, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    TileEntity tileentity = p_180614_2_.getTileEntity(blockpos);

                    if (tileentity instanceof TileEntityBed)
                    {
                        ((TileEntityBed)tileentity).func_193051_a(itemstack);
                    }

                    TileEntity tileentity1 = p_180614_2_.getTileEntity(p_180614_3_);

                    if (tileentity1 instanceof TileEntityBed)
                    {
                        ((TileEntityBed)tileentity1).func_193051_a(itemstack);
                    }

                    p_180614_2_.func_175722_b(p_180614_3_, block, false);
                    p_180614_2_.func_175722_b(blockpos, iblockstate1.getBlock(), false);

                    if (p_180614_1_ instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)p_180614_1_, p_180614_3_, itemstack);
                    }

                    itemstack.shrink(1);
                    return EnumActionResult.SUCCESS;
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
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getTranslationKey(ItemStack stack)
    {
        return super.getTranslationKey() + "." + EnumDyeColor.func_176764_b(stack.func_77960_j()).getTranslationKey();
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        if (this.isInGroup(group))
        {
            for (int i = 0; i < 16; ++i)
            {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }
}

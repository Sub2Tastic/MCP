package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemSign extends Item
{
    public ItemSign()
    {
        this.maxStackSize = 16;
        this.func_77637_a(CreativeTabs.DECORATIONS);
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        IBlockState iblockstate = p_180614_2_.getBlockState(p_180614_3_);
        boolean flag = iblockstate.getBlock().func_176200_f(p_180614_2_, p_180614_3_);

        if (p_180614_5_ != EnumFacing.DOWN && (iblockstate.getMaterial().isSolid() || flag) && (!flag || p_180614_5_ == EnumFacing.UP))
        {
            p_180614_3_ = p_180614_3_.offset(p_180614_5_);
            ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

            if (p_180614_1_.canPlayerEdit(p_180614_3_, p_180614_5_, itemstack) && Blocks.field_150472_an.func_176196_c(p_180614_2_, p_180614_3_))
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
                        p_180614_2_.setBlockState(p_180614_3_, Blocks.field_150472_an.getDefaultState().func_177226_a(BlockStandingSign.ROTATION, Integer.valueOf(i)), 11);
                    }
                    else
                    {
                        p_180614_2_.setBlockState(p_180614_3_, Blocks.field_150444_as.getDefaultState().func_177226_a(BlockWallSign.FACING, p_180614_5_), 11);
                    }

                    TileEntity tileentity = p_180614_2_.getTileEntity(p_180614_3_);

                    if (tileentity instanceof TileEntitySign && !ItemBlock.setTileEntityNBT(p_180614_2_, p_180614_1_, p_180614_3_, itemstack))
                    {
                        p_180614_1_.openSignEditor((TileEntitySign)tileentity);
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
}

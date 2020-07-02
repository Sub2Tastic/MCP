package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockSpecial extends Item
{
    private final Block field_150935_a;

    public ItemBlockSpecial(Block p_i45329_1_)
    {
        this.field_150935_a = p_i45329_1_;
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        IBlockState iblockstate = p_180614_2_.getBlockState(p_180614_3_);
        Block block = iblockstate.getBlock();

        if (block == Blocks.field_150431_aC && ((Integer)iblockstate.get(BlockSnow.LAYERS)).intValue() < 1)
        {
            p_180614_5_ = EnumFacing.UP;
        }
        else if (!block.func_176200_f(p_180614_2_, p_180614_3_))
        {
            p_180614_3_ = p_180614_3_.offset(p_180614_5_);
        }

        ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

        if (!itemstack.isEmpty() && p_180614_1_.canPlayerEdit(p_180614_3_, p_180614_5_, itemstack) && p_180614_2_.func_190527_a(this.field_150935_a, p_180614_3_, false, p_180614_5_, (Entity)null))
        {
            IBlockState iblockstate1 = this.field_150935_a.func_180642_a(p_180614_2_, p_180614_3_, p_180614_5_, p_180614_6_, p_180614_7_, p_180614_8_, 0, p_180614_1_);

            if (!p_180614_2_.setBlockState(p_180614_3_, iblockstate1, 11))
            {
                return EnumActionResult.FAIL;
            }
            else
            {
                iblockstate1 = p_180614_2_.getBlockState(p_180614_3_);

                if (iblockstate1.getBlock() == this.field_150935_a)
                {
                    ItemBlock.setTileEntityNBT(p_180614_2_, p_180614_1_, p_180614_3_, itemstack);
                    iblockstate1.getBlock().onBlockPlacedBy(p_180614_2_, p_180614_3_, iblockstate1, p_180614_1_, itemstack);

                    if (p_180614_1_ instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)p_180614_1_, p_180614_3_, itemstack);
                    }
                }

                SoundType soundtype = this.field_150935_a.func_185467_w();
                p_180614_2_.playSound(p_180614_1_, p_180614_3_, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
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

package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDoor extends Item
{
    private final Block field_179236_a;

    public ItemDoor(Block p_i45788_1_)
    {
        this.field_179236_a = p_i45788_1_;
        this.func_77637_a(CreativeTabs.REDSTONE);
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        if (p_180614_5_ != EnumFacing.UP)
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = p_180614_2_.getBlockState(p_180614_3_);
            Block block = iblockstate.getBlock();

            if (!block.func_176200_f(p_180614_2_, p_180614_3_))
            {
                p_180614_3_ = p_180614_3_.offset(p_180614_5_);
            }

            ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

            if (p_180614_1_.canPlayerEdit(p_180614_3_, p_180614_5_, itemstack) && this.field_179236_a.func_176196_c(p_180614_2_, p_180614_3_))
            {
                EnumFacing enumfacing = EnumFacing.fromAngle((double)p_180614_1_.rotationYaw);
                int i = enumfacing.getXOffset();
                int j = enumfacing.getZOffset();
                boolean flag = i < 0 && p_180614_8_ < 0.5F || i > 0 && p_180614_8_ > 0.5F || j < 0 && p_180614_6_ > 0.5F || j > 0 && p_180614_6_ < 0.5F;
                func_179235_a(p_180614_2_, p_180614_3_, enumfacing, this.field_179236_a, flag);
                SoundType soundtype = this.field_179236_a.func_185467_w();
                p_180614_2_.playSound(p_180614_1_, p_180614_3_, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
            else
            {
                return EnumActionResult.FAIL;
            }
        }
    }

    public static void func_179235_a(World p_179235_0_, BlockPos p_179235_1_, EnumFacing p_179235_2_, Block p_179235_3_, boolean p_179235_4_)
    {
        BlockPos blockpos = p_179235_1_.offset(p_179235_2_.rotateY());
        BlockPos blockpos1 = p_179235_1_.offset(p_179235_2_.rotateYCCW());
        int i = (p_179235_0_.getBlockState(blockpos1).func_185915_l() ? 1 : 0) + (p_179235_0_.getBlockState(blockpos1.up()).func_185915_l() ? 1 : 0);
        int j = (p_179235_0_.getBlockState(blockpos).func_185915_l() ? 1 : 0) + (p_179235_0_.getBlockState(blockpos.up()).func_185915_l() ? 1 : 0);
        boolean flag = p_179235_0_.getBlockState(blockpos1).getBlock() == p_179235_3_ || p_179235_0_.getBlockState(blockpos1.up()).getBlock() == p_179235_3_;
        boolean flag1 = p_179235_0_.getBlockState(blockpos).getBlock() == p_179235_3_ || p_179235_0_.getBlockState(blockpos.up()).getBlock() == p_179235_3_;

        if ((!flag || flag1) && j <= i)
        {
            if (flag1 && !flag || j < i)
            {
                p_179235_4_ = false;
            }
        }
        else
        {
            p_179235_4_ = true;
        }

        BlockPos blockpos2 = p_179235_1_.up();
        boolean flag2 = p_179235_0_.isBlockPowered(p_179235_1_) || p_179235_0_.isBlockPowered(blockpos2);
        IBlockState iblockstate = p_179235_3_.getDefaultState().func_177226_a(BlockDoor.FACING, p_179235_2_).func_177226_a(BlockDoor.HINGE, p_179235_4_ ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT).func_177226_a(BlockDoor.POWERED, Boolean.valueOf(flag2)).func_177226_a(BlockDoor.OPEN, Boolean.valueOf(flag2));
        p_179235_0_.setBlockState(p_179235_1_, iblockstate.func_177226_a(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 2);
        p_179235_0_.setBlockState(blockpos2, iblockstate.func_177226_a(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);
        p_179235_0_.func_175685_c(p_179235_1_, p_179235_3_, false);
        p_179235_0_.func_175685_c(blockpos2, p_179235_3_, false);
    }
}

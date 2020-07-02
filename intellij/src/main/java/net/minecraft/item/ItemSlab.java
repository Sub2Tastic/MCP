package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSlab extends ItemBlock
{
    private final BlockSlab field_150949_c;
    private final BlockSlab field_179226_c;

    public ItemSlab(Block p_i45782_1_, BlockSlab p_i45782_2_, BlockSlab p_i45782_3_)
    {
        super(p_i45782_1_);
        this.field_150949_c = p_i45782_2_;
        this.field_179226_c = p_i45782_3_;
        this.func_77656_e(0);
        this.func_77627_a(true);
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
        return this.field_150949_c.func_150002_b(stack.func_77960_j());
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

        if (!itemstack.isEmpty() && p_180614_1_.canPlayerEdit(p_180614_3_.offset(p_180614_5_), p_180614_5_, itemstack))
        {
            Comparable<?> comparable = this.field_150949_c.func_185674_a(itemstack);
            IBlockState iblockstate = p_180614_2_.getBlockState(p_180614_3_);

            if (iblockstate.getBlock() == this.field_150949_c)
            {
                IProperty<?> iproperty = this.field_150949_c.func_176551_l();
                Comparable<?> comparable1 = iblockstate.get(iproperty);
                BlockSlab.EnumBlockHalf blockslab$enumblockhalf = (BlockSlab.EnumBlockHalf)iblockstate.get(BlockSlab.field_176554_a);

                if ((p_180614_5_ == EnumFacing.UP && blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.BOTTOM || p_180614_5_ == EnumFacing.DOWN && blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.TOP) && comparable1 == comparable)
                {
                    IBlockState iblockstate1 = this.func_185055_a(iproperty, comparable1);
                    AxisAlignedBB axisalignedbb = iblockstate1.func_185890_d(p_180614_2_, p_180614_3_);

                    if (axisalignedbb != Block.field_185506_k && p_180614_2_.func_72855_b(axisalignedbb.offset(p_180614_3_)) && p_180614_2_.setBlockState(p_180614_3_, iblockstate1, 11))
                    {
                        SoundType soundtype = this.field_179226_c.func_185467_w();
                        p_180614_2_.playSound(p_180614_1_, p_180614_3_, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                        itemstack.shrink(1);

                        if (p_180614_1_ instanceof EntityPlayerMP)
                        {
                            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)p_180614_1_, p_180614_3_, itemstack);
                        }
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            return this.func_180615_a(p_180614_1_, itemstack, p_180614_2_, p_180614_3_.offset(p_180614_5_), comparable) ? EnumActionResult.SUCCESS : super.func_180614_a(p_180614_1_, p_180614_2_, p_180614_3_, p_180614_4_, p_180614_5_, p_180614_6_, p_180614_7_, p_180614_8_);
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    public boolean func_179222_a(World p_179222_1_, BlockPos p_179222_2_, EnumFacing p_179222_3_, EntityPlayer p_179222_4_, ItemStack p_179222_5_)
    {
        BlockPos blockpos = p_179222_2_;
        IProperty<?> iproperty = this.field_150949_c.func_176551_l();
        Comparable<?> comparable = this.field_150949_c.func_185674_a(p_179222_5_);
        IBlockState iblockstate = p_179222_1_.getBlockState(p_179222_2_);

        if (iblockstate.getBlock() == this.field_150949_c)
        {
            boolean flag = iblockstate.get(BlockSlab.field_176554_a) == BlockSlab.EnumBlockHalf.TOP;

            if ((p_179222_3_ == EnumFacing.UP && !flag || p_179222_3_ == EnumFacing.DOWN && flag) && comparable == iblockstate.get(iproperty))
            {
                return true;
            }
        }

        p_179222_2_ = p_179222_2_.offset(p_179222_3_);
        IBlockState iblockstate1 = p_179222_1_.getBlockState(p_179222_2_);
        return iblockstate1.getBlock() == this.field_150949_c && comparable == iblockstate1.get(iproperty) ? true : super.func_179222_a(p_179222_1_, blockpos, p_179222_3_, p_179222_4_, p_179222_5_);
    }

    private boolean func_180615_a(EntityPlayer p_180615_1_, ItemStack p_180615_2_, World p_180615_3_, BlockPos p_180615_4_, Object p_180615_5_)
    {
        IBlockState iblockstate = p_180615_3_.getBlockState(p_180615_4_);

        if (iblockstate.getBlock() == this.field_150949_c)
        {
            Comparable<?> comparable = iblockstate.get(this.field_150949_c.func_176551_l());

            if (comparable == p_180615_5_)
            {
                IBlockState iblockstate1 = this.func_185055_a(this.field_150949_c.func_176551_l(), comparable);
                AxisAlignedBB axisalignedbb = iblockstate1.func_185890_d(p_180615_3_, p_180615_4_);

                if (axisalignedbb != Block.field_185506_k && p_180615_3_.func_72855_b(axisalignedbb.offset(p_180615_4_)) && p_180615_3_.setBlockState(p_180615_4_, iblockstate1, 11))
                {
                    SoundType soundtype = this.field_179226_c.func_185467_w();
                    p_180615_3_.playSound(p_180615_1_, p_180615_4_, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    p_180615_2_.shrink(1);
                }

                return true;
            }
        }

        return false;
    }

    protected <T extends Comparable<T>> IBlockState func_185055_a(IProperty<T> p_185055_1_, Comparable<?> p_185055_2_)
    {
        return this.field_179226_c.getDefaultState().func_177226_a(p_185055_1_, (T)p_185055_2_);
    }
}

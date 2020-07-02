package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDye extends Item
{
    public static final int[] field_150922_c = new int[] {1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

    public ItemDye()
    {
        this.func_77627_a(true);
        this.func_77656_e(0);
        this.func_77637_a(CreativeTabs.MATERIALS);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getTranslationKey(ItemStack stack)
    {
        int i = stack.func_77960_j();
        return super.getTranslationKey() + "." + EnumDyeColor.func_176766_a(i).getTranslationKey();
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

        if (!p_180614_1_.canPlayerEdit(p_180614_3_.offset(p_180614_5_), p_180614_5_, itemstack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            EnumDyeColor enumdyecolor = EnumDyeColor.func_176766_a(itemstack.func_77960_j());

            if (enumdyecolor == EnumDyeColor.WHITE)
            {
                if (func_179234_a(itemstack, p_180614_2_, p_180614_3_))
                {
                    if (!p_180614_2_.isRemote)
                    {
                        p_180614_2_.func_175718_b(2005, p_180614_3_, 0);
                    }

                    return EnumActionResult.SUCCESS;
                }
            }
            else if (enumdyecolor == EnumDyeColor.BROWN)
            {
                IBlockState iblockstate = p_180614_2_.getBlockState(p_180614_3_);
                Block block = iblockstate.getBlock();

                if (block == Blocks.field_150364_r && iblockstate.get(BlockOldLog.field_176301_b) == BlockPlanks.EnumType.JUNGLE)
                {
                    if (p_180614_5_ == EnumFacing.DOWN || p_180614_5_ == EnumFacing.UP)
                    {
                        return EnumActionResult.FAIL;
                    }

                    p_180614_3_ = p_180614_3_.offset(p_180614_5_);

                    if (p_180614_2_.isAirBlock(p_180614_3_))
                    {
                        IBlockState iblockstate1 = Blocks.COCOA.func_180642_a(p_180614_2_, p_180614_3_, p_180614_5_, p_180614_6_, p_180614_7_, p_180614_8_, 0, p_180614_1_);
                        p_180614_2_.setBlockState(p_180614_3_, iblockstate1, 10);

                        if (!p_180614_1_.abilities.isCreativeMode)
                        {
                            itemstack.shrink(1);
                        }

                        return EnumActionResult.SUCCESS;
                    }
                }

                return EnumActionResult.FAIL;
            }

            return EnumActionResult.PASS;
        }
    }

    public static boolean func_179234_a(ItemStack p_179234_0_, World p_179234_1_, BlockPos p_179234_2_)
    {
        IBlockState iblockstate = p_179234_1_.getBlockState(p_179234_2_);

        if (iblockstate.getBlock() instanceof IGrowable)
        {
            IGrowable igrowable = (IGrowable)iblockstate.getBlock();

            if (igrowable.canGrow(p_179234_1_, p_179234_2_, iblockstate, p_179234_1_.isRemote))
            {
                if (!p_179234_1_.isRemote)
                {
                    if (igrowable.canUseBonemeal(p_179234_1_, p_179234_1_.rand, p_179234_2_, iblockstate))
                    {
                        igrowable.func_176474_b(p_179234_1_, p_179234_1_.rand, p_179234_2_, iblockstate);
                    }

                    p_179234_0_.shrink(1);
                }

                return true;
            }
        }

        return false;
    }

    public static void func_180617_a(World p_180617_0_, BlockPos p_180617_1_, int p_180617_2_)
    {
        if (p_180617_2_ == 0)
        {
            p_180617_2_ = 15;
        }

        IBlockState iblockstate = p_180617_0_.getBlockState(p_180617_1_);

        if (iblockstate.getMaterial() != Material.AIR)
        {
            for (int i = 0; i < p_180617_2_; ++i)
            {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                p_180617_0_.func_175688_a(EnumParticleTypes.VILLAGER_HAPPY, (double)((float)p_180617_1_.getX() + random.nextFloat()), (double)p_180617_1_.getY() + (double)random.nextFloat() * iblockstate.func_185900_c(p_180617_0_, p_180617_1_).maxY, (double)((float)p_180617_1_.getZ() + random.nextFloat()), d0, d1, d2);
            }
        }
    }

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
        if (target instanceof EntitySheep)
        {
            EntitySheep entitysheep = (EntitySheep)target;
            EnumDyeColor enumdyecolor = EnumDyeColor.func_176766_a(stack.func_77960_j());

            if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != enumdyecolor)
            {
                entitysheep.setFleeceColor(enumdyecolor);
                stack.shrink(1);
            }

            return true;
        }
        else
        {
            return false;
        }
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

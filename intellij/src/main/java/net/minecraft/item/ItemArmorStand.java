package net.minecraft.item;

import java.util.List;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Rotations;
import net.minecraft.world.World;

public class ItemArmorStand extends Item
{
    public ItemArmorStand()
    {
        this.func_77637_a(CreativeTabs.DECORATIONS);
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        if (p_180614_5_ == EnumFacing.DOWN)
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            boolean flag = p_180614_2_.getBlockState(p_180614_3_).getBlock().func_176200_f(p_180614_2_, p_180614_3_);
            BlockPos blockpos = flag ? p_180614_3_ : p_180614_3_.offset(p_180614_5_);
            ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

            if (!p_180614_1_.canPlayerEdit(blockpos, p_180614_5_, itemstack))
            {
                return EnumActionResult.FAIL;
            }
            else
            {
                BlockPos blockpos1 = blockpos.up();
                boolean flag1 = !p_180614_2_.isAirBlock(blockpos) && !p_180614_2_.getBlockState(blockpos).getBlock().func_176200_f(p_180614_2_, blockpos);
                flag1 = flag1 | (!p_180614_2_.isAirBlock(blockpos1) && !p_180614_2_.getBlockState(blockpos1).getBlock().func_176200_f(p_180614_2_, blockpos1));

                if (flag1)
                {
                    return EnumActionResult.FAIL;
                }
                else
                {
                    double d0 = (double)blockpos.getX();
                    double d1 = (double)blockpos.getY();
                    double d2 = (double)blockpos.getZ();
                    List<Entity> list = p_180614_2_.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));

                    if (!list.isEmpty())
                    {
                        return EnumActionResult.FAIL;
                    }
                    else
                    {
                        if (!p_180614_2_.isRemote)
                        {
                            p_180614_2_.func_175698_g(blockpos);
                            p_180614_2_.func_175698_g(blockpos1);
                            EntityArmorStand entityarmorstand = new EntityArmorStand(p_180614_2_, d0 + 0.5D, d1, d2 + 0.5D);
                            float f = (float)MathHelper.floor((MathHelper.wrapDegrees(p_180614_1_.rotationYaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                            entityarmorstand.setLocationAndAngles(d0 + 0.5D, d1, d2 + 0.5D, f, 0.0F);
                            this.applyRandomRotations(entityarmorstand, p_180614_2_.rand);
                            ItemMonsterPlacer.func_185079_a(p_180614_2_, p_180614_1_, itemstack, entityarmorstand);
                            p_180614_2_.addEntity0(entityarmorstand);
                            p_180614_2_.playSound((EntityPlayer)null, entityarmorstand.posX, entityarmorstand.posY, entityarmorstand.posZ, SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
                        }

                        itemstack.shrink(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
    }

    private void applyRandomRotations(EntityArmorStand armorStand, Random rand)
    {
        Rotations rotations = armorStand.getHeadRotation();
        float f = rand.nextFloat() * 5.0F;
        float f1 = rand.nextFloat() * 20.0F - 10.0F;
        Rotations rotations1 = new Rotations(rotations.getX() + f, rotations.getY() + f1, rotations.getZ());
        armorStand.setHeadRotation(rotations1);
        rotations = armorStand.getBodyRotation();
        f = rand.nextFloat() * 10.0F - 5.0F;
        rotations1 = new Rotations(rotations.getX(), rotations.getY() + f, rotations.getZ());
        armorStand.setBodyRotation(rotations1);
    }
}

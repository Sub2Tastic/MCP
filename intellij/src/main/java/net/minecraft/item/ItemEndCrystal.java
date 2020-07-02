package net.minecraft.item;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;

public class ItemEndCrystal extends Item
{
    public ItemEndCrystal()
    {
        this.func_77655_b("end_crystal");
        this.func_77637_a(CreativeTabs.DECORATIONS);
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        IBlockState iblockstate = p_180614_2_.getBlockState(p_180614_3_);

        if (iblockstate.getBlock() != Blocks.OBSIDIAN && iblockstate.getBlock() != Blocks.BEDROCK)
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            BlockPos blockpos = p_180614_3_.up();
            ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

            if (!p_180614_1_.canPlayerEdit(blockpos, p_180614_5_, itemstack))
            {
                return EnumActionResult.FAIL;
            }
            else
            {
                BlockPos blockpos1 = blockpos.up();
                boolean flag = !p_180614_2_.isAirBlock(blockpos) && !p_180614_2_.getBlockState(blockpos).getBlock().func_176200_f(p_180614_2_, blockpos);
                flag = flag | (!p_180614_2_.isAirBlock(blockpos1) && !p_180614_2_.getBlockState(blockpos1).getBlock().func_176200_f(p_180614_2_, blockpos1));

                if (flag)
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
                            EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(p_180614_2_, (double)((float)p_180614_3_.getX() + 0.5F), (double)(p_180614_3_.getY() + 1), (double)((float)p_180614_3_.getZ() + 0.5F));
                            entityendercrystal.setShowBottom(false);
                            p_180614_2_.addEntity0(entityendercrystal);

                            if (p_180614_2_.dimension instanceof WorldProviderEnd)
                            {
                                DragonFightManager dragonfightmanager = ((WorldProviderEnd)p_180614_2_.dimension).getDragonFightManager();
                                dragonfightmanager.tryRespawnDragon();
                            }
                        }

                        itemstack.shrink(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
    }

    /**
     * Returns true if this item has an enchantment glint. By default, this returns
     * <code>stack.isItemEnchanted()</code>, but other items can override it (for instance, written books always return
     * true).
     *  
     * Note that if you override this method, you generally want to also call the super version (on {@link Item}) to get
     * the glint for enchanted items. Of course, that is unnecessary if the overwritten version always returns true.
     */
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
}

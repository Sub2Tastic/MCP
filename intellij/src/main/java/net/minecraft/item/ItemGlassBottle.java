package net.minecraft.item;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemGlassBottle extends Item
{
    public ItemGlassBottle()
    {
        this.func_77637_a(CreativeTabs.BREWING);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        List<EntityAreaEffectCloud> list = worldIn.<EntityAreaEffectCloud>getEntitiesWithinAABB(EntityAreaEffectCloud.class, playerIn.getBoundingBox().grow(2.0D), new Predicate<EntityAreaEffectCloud>()
        {
            public boolean apply(@Nullable EntityAreaEffectCloud p_apply_1_)
            {
                return p_apply_1_ != null && p_apply_1_.isAlive() && p_apply_1_.getOwner() instanceof EntityDragon;
            }
        });
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!list.isEmpty())
        {
            EntityAreaEffectCloud entityareaeffectcloud = list.get(0);
            entityareaeffectcloud.setRadius(entityareaeffectcloud.getRadius() - 0.5F);
            worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            return new ActionResult(EnumActionResult.SUCCESS, this.turnBottleIntoItem(itemstack, playerIn, new ItemStack(Items.DRAGON_BREATH)));
        }
        else
        {
            RayTraceResult raytraceresult = this.func_77621_a(worldIn, playerIn, true);

            if (raytraceresult == null)
            {
                return new ActionResult(EnumActionResult.PASS, itemstack);
            }
            else
            {
                if (raytraceresult.field_72313_a == RayTraceResult.Type.BLOCK)
                {
                    BlockPos blockpos = raytraceresult.func_178782_a();

                    if (!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.field_178784_b), raytraceresult.field_178784_b, itemstack))
                    {
                        return new ActionResult(EnumActionResult.PASS, itemstack);
                    }

                    if (worldIn.getBlockState(blockpos).getMaterial() == Material.WATER)
                    {
                        worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                        return new ActionResult(EnumActionResult.SUCCESS, this.turnBottleIntoItem(itemstack, playerIn, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionTypes.WATER)));
                    }
                }

                return new ActionResult(EnumActionResult.PASS, itemstack);
            }
        }
    }

    protected ItemStack turnBottleIntoItem(ItemStack bottleStack, EntityPlayer player, ItemStack stack)
    {
        bottleStack.shrink(1);
        player.addStat(StatList.func_188057_b(this));

        if (bottleStack.isEmpty())
        {
            return stack;
        }
        else
        {
            if (!player.inventory.addItemStackToInventory(stack))
            {
                player.dropItem(stack, false);
            }

            return bottleStack;
        }
    }
}

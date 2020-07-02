package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemEnderEye extends Item
{
    public ItemEnderEye()
    {
        this.func_77637_a(CreativeTabs.MISC);
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        IBlockState iblockstate = p_180614_2_.getBlockState(p_180614_3_);
        ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

        if (p_180614_1_.canPlayerEdit(p_180614_3_.offset(p_180614_5_), p_180614_5_, itemstack) && iblockstate.getBlock() == Blocks.END_PORTAL_FRAME && !((Boolean)iblockstate.get(BlockEndPortalFrame.EYE)).booleanValue())
        {
            if (p_180614_2_.isRemote)
            {
                return EnumActionResult.SUCCESS;
            }
            else
            {
                p_180614_2_.setBlockState(p_180614_3_, iblockstate.func_177226_a(BlockEndPortalFrame.EYE, Boolean.valueOf(true)), 2);
                p_180614_2_.updateComparatorOutputLevel(p_180614_3_, Blocks.END_PORTAL_FRAME);
                itemstack.shrink(1);

                for (int i = 0; i < 16; ++i)
                {
                    double d0 = (double)((float)p_180614_3_.getX() + (5.0F + random.nextFloat() * 6.0F) / 16.0F);
                    double d1 = (double)((float)p_180614_3_.getY() + 0.8125F);
                    double d2 = (double)((float)p_180614_3_.getZ() + (5.0F + random.nextFloat() * 6.0F) / 16.0F);
                    double d3 = 0.0D;
                    double d4 = 0.0D;
                    double d5 = 0.0D;
                    p_180614_2_.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }

                p_180614_2_.playSound((EntityPlayer)null, p_180614_3_, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                BlockPattern.PatternHelper blockpattern$patternhelper = BlockEndPortalFrame.getOrCreatePortalShape().match(p_180614_2_, p_180614_3_);

                if (blockpattern$patternhelper != null)
                {
                    BlockPos blockpos = blockpattern$patternhelper.getFrontTopLeft().add(-3, 0, -3);

                    for (int j = 0; j < 3; ++j)
                    {
                        for (int k = 0; k < 3; ++k)
                        {
                            p_180614_2_.setBlockState(blockpos.add(j, 0, k), Blocks.END_PORTAL.getDefaultState(), 2);
                        }
                    }

                    p_180614_2_.playBroadcastSound(1038, blockpos.add(1, 0, 1), 0);
                }

                return EnumActionResult.SUCCESS;
            }
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = this.func_77621_a(worldIn, playerIn, false);

        if (raytraceresult != null && raytraceresult.field_72313_a == RayTraceResult.Type.BLOCK && worldIn.getBlockState(raytraceresult.func_178782_a()).getBlock() == Blocks.END_PORTAL_FRAME)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        else
        {
            playerIn.setActiveHand(handIn);

            if (!worldIn.isRemote)
            {
                BlockPos blockpos = ((WorldServer)worldIn).getChunkProvider().func_180513_a(worldIn, "Stronghold", new BlockPos(playerIn), false);

                if (blockpos != null)
                {
                    EntityEnderEye entityendereye = new EntityEnderEye(worldIn, playerIn.posX, playerIn.posY + (double)(playerIn.field_70131_O / 2.0F), playerIn.posZ);
                    entityendereye.moveTowards(blockpos);
                    worldIn.addEntity0(entityendereye);

                    if (playerIn instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.USED_ENDER_EYE.trigger((EntityPlayerMP)playerIn, blockpos);
                    }

                    worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                    worldIn.func_180498_a((EntityPlayer)null, 1003, new BlockPos(playerIn), 0);

                    if (!playerIn.abilities.isCreativeMode)
                    {
                        itemstack.shrink(1);
                    }

                    playerIn.addStat(StatList.func_188057_b(this));
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
                }
            }

            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
    }
}

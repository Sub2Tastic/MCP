package net.minecraft.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMinecart extends Item
{
    private static final IBehaviorDispenseItem MINECART_DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem()
    {
        private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();
        public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
        {
            EnumFacing enumfacing = (EnumFacing)source.getBlockState().get(BlockDispenser.FACING);
            World world = source.func_82618_k();
            double d0 = source.getX() + (double)enumfacing.getXOffset() * 1.125D;
            double d1 = Math.floor(source.getY()) + (double)enumfacing.getYOffset();
            double d2 = source.getZ() + (double)enumfacing.getZOffset() * 1.125D;
            BlockPos blockpos = source.getBlockPos().offset(enumfacing);
            IBlockState iblockstate = world.getBlockState(blockpos);
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection)iblockstate.get(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            double d3;

            if (BlockRailBase.func_176563_d(iblockstate))
            {
                if (blockrailbase$enumraildirection.func_177018_c())
                {
                    d3 = 0.6D;
                }
                else
                {
                    d3 = 0.1D;
                }
            }
            else
            {
                if (iblockstate.getMaterial() != Material.AIR || !BlockRailBase.func_176563_d(world.getBlockState(blockpos.down())))
                {
                    return this.behaviourDefaultDispenseItem.func_82482_a(source, stack);
                }

                IBlockState iblockstate1 = world.getBlockState(blockpos.down());
                BlockRailBase.EnumRailDirection blockrailbase$enumraildirection1 = iblockstate1.getBlock() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection)iblockstate1.get(((BlockRailBase)iblockstate1.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;

                if (enumfacing != EnumFacing.DOWN && blockrailbase$enumraildirection1.func_177018_c())
                {
                    d3 = -0.4D;
                }
                else
                {
                    d3 = -0.9D;
                }
            }

            EntityMinecart entityminecart = EntityMinecart.create(world, d0, d1 + d3, d2, ((ItemMinecart)stack.getItem()).minecartType);

            if (stack.hasDisplayName())
            {
                entityminecart.func_96094_a(stack.func_82833_r());
            }

            world.addEntity0(entityminecart);
            stack.shrink(1);
            return stack;
        }
        protected void playDispenseSound(IBlockSource source)
        {
            source.func_82618_k().func_175718_b(1000, source.getBlockPos(), 0);
        }
    };
    private final EntityMinecart.Type minecartType;

    public ItemMinecart(EntityMinecart.Type p_i46743_1_)
    {
        this.maxStackSize = 1;
        this.minecartType = p_i46743_1_;
        this.func_77637_a(CreativeTabs.TRANSPORTATION);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.func_82595_a(this, MINECART_DISPENSER_BEHAVIOR);
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        IBlockState iblockstate = p_180614_2_.getBlockState(p_180614_3_);

        if (!BlockRailBase.func_176563_d(iblockstate))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

            if (!p_180614_2_.isRemote)
            {
                BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection)iblockstate.get(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                double d0 = 0.0D;

                if (blockrailbase$enumraildirection.func_177018_c())
                {
                    d0 = 0.5D;
                }

                EntityMinecart entityminecart = EntityMinecart.create(p_180614_2_, (double)p_180614_3_.getX() + 0.5D, (double)p_180614_3_.getY() + 0.0625D + d0, (double)p_180614_3_.getZ() + 0.5D, this.minecartType);

                if (itemstack.hasDisplayName())
                {
                    entityminecart.func_96094_a(itemstack.func_82833_r());
                }

                p_180614_2_.addEntity0(entityminecart);
            }

            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
    }
}

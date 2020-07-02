package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class BlockIce extends BlockBreakable
{
    public BlockIce()
    {
        super(Material.ICE, false);
        this.slipperiness = 0.98F;
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        player.addStat(StatList.func_188055_a(this));
        player.addExhaustion(0.005F);

        if (this.func_149700_E() && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0)
        {
            spawnAsEntity(worldIn, pos, this.func_180643_i(state));
        }
        else
        {
            if (worldIn.dimension.doesWaterVaporize())
            {
                worldIn.func_175698_g(pos);
                return;
            }

            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            this.func_176226_b(worldIn, pos, state, i);
            Material material = worldIn.getBlockState(pos.down()).getMaterial();

            if (material.blocksMovement() || material.isLiquid())
            {
                worldIn.setBlockState(pos, Blocks.field_150358_i.getDefaultState());
            }
        }
    }

    public int func_149745_a(Random p_149745_1_)
    {
        return 0;
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (p_180650_1_.func_175642_b(EnumSkyBlock.BLOCK, p_180650_2_) > 11 - this.getDefaultState().func_185891_c())
        {
            this.func_185679_b(p_180650_1_, p_180650_2_);
        }
    }

    protected void func_185679_b(World p_185679_1_, BlockPos p_185679_2_)
    {
        if (p_185679_1_.dimension.doesWaterVaporize())
        {
            p_185679_1_.func_175698_g(p_185679_2_);
        }
        else
        {
            this.func_176226_b(p_185679_1_, p_185679_2_, p_185679_1_.getBlockState(p_185679_2_), 0);
            p_185679_1_.setBlockState(p_185679_2_, Blocks.WATER.getDefaultState());
            p_185679_1_.neighborChanged(p_185679_2_, Blocks.WATER, p_185679_2_);
        }
    }

    /**
     * @deprecated call via {@link IBlockState#getMobilityFlag()} whenever possible. Implementing/overriding is fine.
     */
    public EnumPushReaction getPushReaction(IBlockState state)
    {
        return EnumPushReaction.NORMAL;
    }
}

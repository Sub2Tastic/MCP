package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class WorldGeneratorBonusChest extends WorldGenerator
{
    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (IBlockState iblockstate = p_180709_1_.getBlockState(p_180709_3_); (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES) && p_180709_3_.getY() > 1; iblockstate = p_180709_1_.getBlockState(p_180709_3_))
        {
            p_180709_3_ = p_180709_3_.down();
        }

        if (p_180709_3_.getY() < 1)
        {
            return false;
        }
        else
        {
            p_180709_3_ = p_180709_3_.up();

            for (int i = 0; i < 4; ++i)
            {
                BlockPos blockpos = p_180709_3_.add(p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(3) - p_180709_2_.nextInt(3), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4));

                if (p_180709_1_.isAirBlock(blockpos) && p_180709_1_.getBlockState(blockpos.down()).func_185896_q())
                {
                    p_180709_1_.setBlockState(blockpos, Blocks.CHEST.getDefaultState(), 2);
                    TileEntity tileentity = p_180709_1_.getTileEntity(blockpos);

                    if (tileentity instanceof TileEntityChest)
                    {
                        ((TileEntityChest)tileentity).setLootTable(LootTableList.CHESTS_SPAWN_BONUS_CHEST, p_180709_2_.nextLong());
                    }

                    BlockPos blockpos1 = blockpos.east();
                    BlockPos blockpos2 = blockpos.west();
                    BlockPos blockpos3 = blockpos.north();
                    BlockPos blockpos4 = blockpos.south();

                    if (p_180709_1_.isAirBlock(blockpos2) && p_180709_1_.getBlockState(blockpos2.down()).func_185896_q())
                    {
                        p_180709_1_.setBlockState(blockpos2, Blocks.TORCH.getDefaultState(), 2);
                    }

                    if (p_180709_1_.isAirBlock(blockpos1) && p_180709_1_.getBlockState(blockpos1.down()).func_185896_q())
                    {
                        p_180709_1_.setBlockState(blockpos1, Blocks.TORCH.getDefaultState(), 2);
                    }

                    if (p_180709_1_.isAirBlock(blockpos3) && p_180709_1_.getBlockState(blockpos3.down()).func_185896_q())
                    {
                        p_180709_1_.setBlockState(blockpos3, Blocks.TORCH.getDefaultState(), 2);
                    }

                    if (p_180709_1_.isAirBlock(blockpos4) && p_180709_1_.getBlockState(blockpos4.down()).func_185896_q())
                    {
                        p_180709_1_.setBlockState(blockpos4, Blocks.TORCH.getDefaultState(), 2);
                    }

                    return true;
                }
            }

            return false;
        }
    }
}

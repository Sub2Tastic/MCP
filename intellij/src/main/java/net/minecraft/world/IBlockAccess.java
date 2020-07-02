package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public interface IBlockAccess
{
    @Nullable
    TileEntity getTileEntity(BlockPos pos);

    int func_175626_b(BlockPos p_175626_1_, int p_175626_2_);

    IBlockState getBlockState(BlockPos pos);

    /**
     * Checks to see if an air block exists at the provided location. Note that this only checks to see if the blocks
     * material is set to air, meaning it is possible for non-vanilla blocks to still pass this check.
     */
    boolean isAirBlock(BlockPos pos);

    Biome func_180494_b(BlockPos p_180494_1_);

    int getStrongPower(BlockPos pos, EnumFacing direction);

    WorldType getWorldType();
}

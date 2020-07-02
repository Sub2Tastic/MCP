package net.minecraft.block.state;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockBehaviors
{
    /**
     * Called on both Client and Server when World#addBlockEvent is called. On the Server, this may perform additional
     * changes to the world, like pistons replacing the block with an extended base. On the client, the update may
     * involve replacing tile entities, playing sounds, or performing other visual actions to reflect the server side
     * changes.
     */
    boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param);

    void func_189546_a(World p_189546_1_, BlockPos p_189546_2_, Block p_189546_3_, BlockPos p_189546_4_);
}

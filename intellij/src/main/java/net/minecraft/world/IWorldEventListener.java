package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public interface IWorldEventListener
{
    void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags);

    void func_174959_b(BlockPos p_174959_1_);

    /**
     * On the client, re-renders all blocks in this range, inclusive. On the server, does nothing.
     */
    void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2);

    void func_184375_a(EntityPlayer p_184375_1_, SoundEvent p_184375_2_, SoundCategory p_184375_3_, double p_184375_4_, double p_184375_6_, double p_184375_8_, float p_184375_10_, float p_184375_11_);

    void playRecord(SoundEvent soundIn, BlockPos pos);

    void func_180442_a(int p_180442_1_, boolean p_180442_2_, double p_180442_3_, double p_180442_5_, double p_180442_7_, double p_180442_9_, double p_180442_11_, double p_180442_13_, int... p_180442_15_);

    void func_190570_a(int p_190570_1_, boolean p_190570_2_, boolean p_190570_3_, double p_190570_4_, double p_190570_6_, double p_190570_8_, double p_190570_10_, double p_190570_12_, double p_190570_14_, int... p_190570_16_);

    void func_72703_a(Entity p_72703_1_);

    void func_72709_b(Entity p_72709_1_);

    void broadcastSound(int soundID, BlockPos pos, int data);

    void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data);

    void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress);
}

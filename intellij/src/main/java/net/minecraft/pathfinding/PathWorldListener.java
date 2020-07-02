package net.minecraft.pathfinding;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;

public class PathWorldListener implements IWorldEventListener
{
    private final List<PathNavigate> field_189519_a = Lists.<PathNavigate>newArrayList();

    public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags)
    {
        if (this.func_184378_a(worldIn, pos, oldState, newState))
        {
            int i = 0;

            for (int j = this.field_189519_a.size(); i < j; ++i)
            {
                PathNavigate pathnavigate = this.field_189519_a.get(i);

                if (pathnavigate != null && !pathnavigate.canUpdatePathOnTimeout())
                {
                    Path path = pathnavigate.getPath();

                    if (path != null && !path.isFinished() && path.getCurrentPathLength() != 0)
                    {
                        PathPoint pathpoint = pathnavigate.currentPath.getFinalPathPoint();
                        double d0 = pos.func_177954_c(((double)pathpoint.x + pathnavigate.entity.posX) / 2.0D, ((double)pathpoint.y + pathnavigate.entity.posY) / 2.0D, ((double)pathpoint.z + pathnavigate.entity.posZ) / 2.0D);
                        int k = (path.getCurrentPathLength() - path.getCurrentPathIndex()) * (path.getCurrentPathLength() - path.getCurrentPathIndex());

                        if (d0 < (double)k)
                        {
                            pathnavigate.updatePath();
                        }
                    }
                }
            }
        }
    }

    protected boolean func_184378_a(World p_184378_1_, BlockPos p_184378_2_, IBlockState p_184378_3_, IBlockState p_184378_4_)
    {
        AxisAlignedBB axisalignedbb = p_184378_3_.func_185890_d(p_184378_1_, p_184378_2_);
        AxisAlignedBB axisalignedbb1 = p_184378_4_.func_185890_d(p_184378_1_, p_184378_2_);
        return axisalignedbb != axisalignedbb1 && (axisalignedbb == null || !axisalignedbb.equals(axisalignedbb1));
    }

    public void func_174959_b(BlockPos p_174959_1_)
    {
    }

    /**
     * On the client, re-renders all blocks in this range, inclusive. On the server, does nothing.
     */
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
    {
    }

    public void func_184375_a(@Nullable EntityPlayer p_184375_1_, SoundEvent p_184375_2_, SoundCategory p_184375_3_, double p_184375_4_, double p_184375_6_, double p_184375_8_, float p_184375_10_, float p_184375_11_)
    {
    }

    public void func_180442_a(int p_180442_1_, boolean p_180442_2_, double p_180442_3_, double p_180442_5_, double p_180442_7_, double p_180442_9_, double p_180442_11_, double p_180442_13_, int... p_180442_15_)
    {
    }

    public void func_190570_a(int p_190570_1_, boolean p_190570_2_, boolean p_190570_3_, double p_190570_4_, double p_190570_6_, double p_190570_8_, double p_190570_10_, double p_190570_12_, double p_190570_14_, int... p_190570_16_)
    {
    }

    public void func_72703_a(Entity p_72703_1_)
    {
        if (p_72703_1_ instanceof EntityLiving)
        {
            this.field_189519_a.add(((EntityLiving)p_72703_1_).getNavigator());
        }
    }

    public void func_72709_b(Entity p_72709_1_)
    {
        if (p_72709_1_ instanceof EntityLiving)
        {
            this.field_189519_a.remove(((EntityLiving)p_72709_1_).getNavigator());
        }
    }

    public void playRecord(SoundEvent soundIn, BlockPos pos)
    {
    }

    public void broadcastSound(int soundID, BlockPos pos, int data)
    {
    }

    public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data)
    {
    }

    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress)
    {
    }
}

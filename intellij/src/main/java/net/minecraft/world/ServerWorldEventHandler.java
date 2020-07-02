package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class ServerWorldEventHandler implements IWorldEventListener
{
    private final MinecraftServer field_72783_a;
    private final WorldServer field_72782_b;

    public ServerWorldEventHandler(MinecraftServer p_i1517_1_, WorldServer p_i1517_2_)
    {
        this.field_72783_a = p_i1517_1_;
        this.field_72782_b = p_i1517_2_;
    }

    public void func_180442_a(int p_180442_1_, boolean p_180442_2_, double p_180442_3_, double p_180442_5_, double p_180442_7_, double p_180442_9_, double p_180442_11_, double p_180442_13_, int... p_180442_15_)
    {
    }

    public void func_190570_a(int p_190570_1_, boolean p_190570_2_, boolean p_190570_3_, double p_190570_4_, double p_190570_6_, double p_190570_8_, double p_190570_10_, double p_190570_12_, double p_190570_14_, int... p_190570_16_)
    {
    }

    public void func_72703_a(Entity p_72703_1_)
    {
        this.field_72782_b.func_73039_n().func_72786_a(p_72703_1_);

        if (p_72703_1_ instanceof EntityPlayerMP)
        {
            this.field_72782_b.dimension.func_186061_a((EntityPlayerMP)p_72703_1_);
        }
    }

    public void func_72709_b(Entity p_72709_1_)
    {
        this.field_72782_b.func_73039_n().func_72790_b(p_72709_1_);
        this.field_72782_b.getScoreboard().removeEntity(p_72709_1_);

        if (p_72709_1_ instanceof EntityPlayerMP)
        {
            this.field_72782_b.dimension.func_186062_b((EntityPlayerMP)p_72709_1_);
        }
    }

    public void func_184375_a(@Nullable EntityPlayer p_184375_1_, SoundEvent p_184375_2_, SoundCategory p_184375_3_, double p_184375_4_, double p_184375_6_, double p_184375_8_, float p_184375_10_, float p_184375_11_)
    {
        this.field_72783_a.getPlayerList().sendToAllNearExcept(p_184375_1_, p_184375_4_, p_184375_6_, p_184375_8_, p_184375_10_ > 1.0F ? (double)(16.0F * p_184375_10_) : 16.0D, this.field_72782_b.dimension.getType().getId(), new SPacketSoundEffect(p_184375_2_, p_184375_3_, p_184375_4_, p_184375_6_, p_184375_8_, p_184375_10_, p_184375_11_));
    }

    /**
     * On the client, re-renders all blocks in this range, inclusive. On the server, does nothing.
     */
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
    {
    }

    public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags)
    {
        this.field_72782_b.func_184164_w().func_180244_a(pos);
    }

    public void func_174959_b(BlockPos p_174959_1_)
    {
    }

    public void playRecord(SoundEvent soundIn, BlockPos pos)
    {
    }

    public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data)
    {
        this.field_72783_a.getPlayerList().sendToAllNearExcept(player, (double)blockPosIn.getX(), (double)blockPosIn.getY(), (double)blockPosIn.getZ(), 64.0D, this.field_72782_b.dimension.getType().getId(), new SPacketEffect(type, blockPosIn, data, false));
    }

    public void broadcastSound(int soundID, BlockPos pos, int data)
    {
        this.field_72783_a.getPlayerList().sendPacketToAllPlayers(new SPacketEffect(soundID, pos, data, true));
    }

    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress)
    {
        for (EntityPlayerMP entityplayermp : this.field_72783_a.getPlayerList().getPlayers())
        {
            if (entityplayermp != null && entityplayermp.world == this.field_72782_b && entityplayermp.getEntityId() != breakerId)
            {
                double d0 = (double)pos.getX() - entityplayermp.posX;
                double d1 = (double)pos.getY() - entityplayermp.posY;
                double d2 = (double)pos.getZ() - entityplayermp.posZ;

                if (d0 * d0 + d1 * d1 + d2 * d2 < 1024.0D)
                {
                    entityplayermp.connection.sendPacket(new SPacketBlockBreakAnim(breakerId, pos, progress));
                }
            }
        }
    }
}

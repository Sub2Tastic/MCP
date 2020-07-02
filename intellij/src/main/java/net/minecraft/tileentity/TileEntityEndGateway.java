package net.minecraft.tileentity;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenEndGateway;
import net.minecraft.world.gen.feature.WorldGenEndIsland;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TileEntityEndGateway extends TileEntityEndPortal implements ITickable
{
    private static final Logger field_184314_a = LogManager.getLogger();
    private long field_184315_f;
    private int field_184316_g;
    private BlockPos field_184317_h;
    private boolean field_184318_i;

    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);
        compound.putLong("Age", this.field_184315_f);

        if (this.field_184317_h != null)
        {
            compound.func_74782_a("ExitPortal", NBTUtil.writeBlockPos(this.field_184317_h));
        }

        if (this.field_184318_i)
        {
            compound.putBoolean("ExactTeleport", this.field_184318_i);
        }

        return compound;
    }

    public void read(NBTTagCompound compound)
    {
        super.read(compound);
        this.field_184315_f = compound.getLong("Age");

        if (compound.contains("ExitPortal", 10))
        {
            this.field_184317_h = NBTUtil.readBlockPos(compound.getCompound("ExitPortal"));
        }

        this.field_184318_i = compound.getBoolean("ExactTeleport");
    }

    public double getMaxRenderDistanceSquared()
    {
        return 65536.0D;
    }

    public void tick()
    {
        boolean flag = this.func_184309_b();
        boolean flag1 = this.func_184310_d();
        ++this.field_184315_f;

        if (flag1)
        {
            --this.field_184316_g;
        }
        else if (!this.world.isRemote)
        {
            List<Entity> list = this.world.<Entity>func_72872_a(Entity.class, new AxisAlignedBB(this.getPos()));

            if (!list.isEmpty())
            {
                this.func_184306_a(list.get(0));
            }

            if (this.field_184315_f % 2400L == 0L)
            {
                this.func_184300_h();
            }
        }

        if (flag != this.func_184309_b() || flag1 != this.func_184310_d())
        {
            this.markDirty();
        }
    }

    public boolean func_184309_b()
    {
        return this.field_184315_f < 200L;
    }

    public boolean func_184310_d()
    {
        return this.field_184316_g > 0;
    }

    public float func_184302_e(float p_184302_1_)
    {
        return MathHelper.clamp(((float)this.field_184315_f + p_184302_1_) / 200.0F, 0.0F, 1.0F);
    }

    public float func_184305_g(float p_184305_1_)
    {
        return 1.0F - MathHelper.clamp(((float)this.field_184316_g - p_184305_1_) / 40.0F, 0.0F, 1.0F);
    }

    @Nullable

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 8, this.getUpdateTag());
    }

    /**
     * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
     * many blocks change at once. This compound comes back to you clientside in {@link handleUpdateTag}
     */
    public NBTTagCompound getUpdateTag()
    {
        return this.write(new NBTTagCompound());
    }

    public void func_184300_h()
    {
        if (!this.world.isRemote)
        {
            this.field_184316_g = 40;
            this.world.addBlockEvent(this.getPos(), this.func_145838_q(), 1, 0);
            this.markDirty();
        }
    }

    /**
     * See {@link Block#eventReceived} for more information. This must return true serverside before it is called
     * clientside.
     */
    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.field_184316_g = 40;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    public void func_184306_a(Entity p_184306_1_)
    {
        if (!this.world.isRemote && !this.func_184310_d())
        {
            this.field_184316_g = 100;

            if (this.field_184317_h == null && this.world.dimension instanceof WorldProviderEnd)
            {
                this.func_184311_k();
            }

            if (this.field_184317_h != null)
            {
                BlockPos blockpos = this.field_184318_i ? this.field_184317_h : this.func_184303_j();
                p_184306_1_.setPositionAndUpdate((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D);
            }

            this.func_184300_h();
        }
    }

    private BlockPos func_184303_j()
    {
        BlockPos blockpos = func_184308_a(this.world, this.field_184317_h, 5, false);
        field_184314_a.debug("Best exit position for portal at {} is {}", this.field_184317_h, blockpos);
        return blockpos.up();
    }

    private void func_184311_k()
    {
        Vec3d vec3d = (new Vec3d((double)this.getPos().getX(), 0.0D, (double)this.getPos().getZ())).normalize();
        Vec3d vec3d1 = vec3d.scale(1024.0D);

        for (int i = 16; func_184301_a(this.world, vec3d1).getTopFilledSegment() > 0 && i-- > 0; vec3d1 = vec3d1.add(vec3d.scale(-16.0D)))
        {
            field_184314_a.debug("Skipping backwards past nonempty chunk at {}", (Object)vec3d1);
        }

        for (int j = 16; func_184301_a(this.world, vec3d1).getTopFilledSegment() == 0 && j-- > 0; vec3d1 = vec3d1.add(vec3d.scale(16.0D)))
        {
            field_184314_a.debug("Skipping forward past empty chunk at {}", (Object)vec3d1);
        }

        field_184314_a.debug("Found chunk at {}", (Object)vec3d1);
        Chunk chunk = func_184301_a(this.world, vec3d1);
        this.field_184317_h = func_184307_a(chunk);

        if (this.field_184317_h == null)
        {
            this.field_184317_h = new BlockPos(vec3d1.x + 0.5D, 75.0D, vec3d1.z + 0.5D);
            field_184314_a.debug("Failed to find suitable block, settling on {}", (Object)this.field_184317_h);
            (new WorldGenEndIsland()).func_180709_b(this.world, new Random(this.field_184317_h.func_177986_g()), this.field_184317_h);
        }
        else
        {
            field_184314_a.debug("Found block at {}", (Object)this.field_184317_h);
        }

        this.field_184317_h = func_184308_a(this.world, this.field_184317_h, 16, true);
        field_184314_a.debug("Creating portal at {}", (Object)this.field_184317_h);
        this.field_184317_h = this.field_184317_h.up(10);
        this.func_184312_b(this.field_184317_h);
        this.markDirty();
    }

    private static BlockPos func_184308_a(World p_184308_0_, BlockPos p_184308_1_, int p_184308_2_, boolean p_184308_3_)
    {
        BlockPos blockpos = null;

        for (int i = -p_184308_2_; i <= p_184308_2_; ++i)
        {
            for (int j = -p_184308_2_; j <= p_184308_2_; ++j)
            {
                if (i != 0 || j != 0 || p_184308_3_)
                {
                    for (int k = 255; k > (blockpos == null ? 0 : blockpos.getY()); --k)
                    {
                        BlockPos blockpos1 = new BlockPos(p_184308_1_.getX() + i, k, p_184308_1_.getZ() + j);
                        IBlockState iblockstate = p_184308_0_.getBlockState(blockpos1);

                        if (iblockstate.func_185898_k() && (p_184308_3_ || iblockstate.getBlock() != Blocks.BEDROCK))
                        {
                            blockpos = blockpos1;
                            break;
                        }
                    }
                }
            }
        }

        return blockpos == null ? p_184308_1_ : blockpos;
    }

    private static Chunk func_184301_a(World p_184301_0_, Vec3d p_184301_1_)
    {
        return p_184301_0_.func_72964_e(MathHelper.floor(p_184301_1_.x / 16.0D), MathHelper.floor(p_184301_1_.z / 16.0D));
    }

    @Nullable
    private static BlockPos func_184307_a(Chunk p_184307_0_)
    {
        BlockPos blockpos = new BlockPos(p_184307_0_.field_76635_g * 16, 30, p_184307_0_.field_76647_h * 16);
        int i = p_184307_0_.getTopFilledSegment() + 16 - 1;
        BlockPos blockpos1 = new BlockPos(p_184307_0_.field_76635_g * 16 + 16 - 1, i, p_184307_0_.field_76647_h * 16 + 16 - 1);
        BlockPos blockpos2 = null;
        double d0 = 0.0D;

        for (BlockPos blockpos3 : BlockPos.func_177980_a(blockpos, blockpos1))
        {
            IBlockState iblockstate = p_184307_0_.func_177435_g(blockpos3);

            if (iblockstate.getBlock() == Blocks.END_STONE && !p_184307_0_.func_177435_g(blockpos3.up(1)).func_185898_k() && !p_184307_0_.func_177435_g(blockpos3.up(2)).func_185898_k())
            {
                double d1 = blockpos3.func_177957_d(0.0D, 0.0D, 0.0D);

                if (blockpos2 == null || d1 < d0)
                {
                    blockpos2 = blockpos3;
                    d0 = d1;
                }
            }
        }

        return blockpos2;
    }

    private void func_184312_b(BlockPos p_184312_1_)
    {
        (new WorldGenEndGateway()).func_180709_b(this.world, new Random(), p_184312_1_);
        TileEntity tileentity = this.world.getTileEntity(p_184312_1_);

        if (tileentity instanceof TileEntityEndGateway)
        {
            TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway)tileentity;
            tileentityendgateway.field_184317_h = new BlockPos(this.getPos());
            tileentityendgateway.markDirty();
        }
        else
        {
            field_184314_a.warn("Couldn't save exit portal at {}", (Object)p_184312_1_);
        }
    }

    public boolean shouldRenderFace(EnumFacing face)
    {
        return this.func_145838_q().getDefaultState().func_185894_c(this.world, this.getPos(), face);
    }

    public int func_184304_i()
    {
        int i = 0;

        for (EnumFacing enumfacing : EnumFacing.values())
        {
            i += this.shouldRenderFace(enumfacing) ? 1 : 0;
        }

        return i;
    }

    public void func_190603_b(BlockPos p_190603_1_)
    {
        this.field_184318_i = true;
        this.field_184317_h = p_190603_1_;
    }
}

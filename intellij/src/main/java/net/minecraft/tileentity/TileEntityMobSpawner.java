package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityMobSpawner extends TileEntity implements ITickable
{
    private final MobSpawnerBaseLogic spawnerLogic = new MobSpawnerBaseLogic()
    {
        public void broadcastEvent(int id)
        {
            TileEntityMobSpawner.this.world.addBlockEvent(TileEntityMobSpawner.this.pos, Blocks.SPAWNER, id, 0);
        }
        public World getWorld()
        {
            return TileEntityMobSpawner.this.world;
        }
        public BlockPos getSpawnerPosition()
        {
            return TileEntityMobSpawner.this.pos;
        }
        public void setNextSpawnData(WeightedSpawnerEntity nextSpawnData)
        {
            super.setNextSpawnData(nextSpawnData);

            if (this.getWorld() != null)
            {
                IBlockState iblockstate = this.getWorld().getBlockState(this.getSpawnerPosition());
                this.getWorld().notifyBlockUpdate(TileEntityMobSpawner.this.pos, iblockstate, iblockstate, 4);
            }
        }
    };

    public static void func_189684_a(DataFixer p_189684_0_)
    {
        p_189684_0_.func_188258_a(FixTypes.BLOCK_ENTITY, new IDataWalker()
        {
            public NBTTagCompound func_188266_a(IDataFixer p_188266_1_, NBTTagCompound p_188266_2_, int p_188266_3_)
            {
                if (TileEntity.func_190559_a(TileEntityMobSpawner.class).equals(new ResourceLocation(p_188266_2_.getString("id"))))
                {
                    if (p_188266_2_.contains("SpawnPotentials", 9))
                    {
                        NBTTagList nbttaglist = p_188266_2_.getList("SpawnPotentials", 10);

                        for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                        {
                            NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);
                            nbttagcompound.func_74782_a("Entity", p_188266_1_.func_188251_a(FixTypes.ENTITY, nbttagcompound.getCompound("Entity"), p_188266_3_));
                        }
                    }

                    p_188266_2_.func_74782_a("SpawnData", p_188266_1_.func_188251_a(FixTypes.ENTITY, p_188266_2_.getCompound("SpawnData"), p_188266_3_));
                }

                return p_188266_2_;
            }
        });
    }

    public void read(NBTTagCompound compound)
    {
        super.read(compound);
        this.spawnerLogic.read(compound);
    }

    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);
        this.spawnerLogic.write(compound);
        return compound;
    }

    public void tick()
    {
        this.spawnerLogic.tick();
    }

    @Nullable

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    /**
     * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
     * many blocks change at once. This compound comes back to you clientside in {@link handleUpdateTag}
     */
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbttagcompound = this.write(new NBTTagCompound());
        nbttagcompound.remove("SpawnPotentials");
        return nbttagcompound;
    }

    /**
     * See {@link Block#eventReceived} for more information. This must return true serverside before it is called
     * clientside.
     */
    public boolean receiveClientEvent(int id, int type)
    {
        return this.spawnerLogic.setDelayToMin(id) ? true : super.receiveClientEvent(id, type);
    }

    /**
     * Checks if players can use this tile entity to access operator (permission level 2) commands either directly or
     * indirectly, such as give or setblock. A similar method exists for entities at {@link
     * net.minecraft.entity.Entity#ignoreItemEntityData()}.<p>For example, {@link
     * net.minecraft.tileentity.TileEntitySign#onlyOpsCanSetNbt() signs} (player right-clicking) and {@link
     * net.minecraft.tileentity.TileEntityCommandBlock#onlyOpsCanSetNbt() command blocks} are considered
     * accessible.</p>@return true if this block entity offers ways for unauthorized players to use restricted commands
     */
    public boolean onlyOpsCanSetNbt()
    {
        return true;
    }

    public MobSpawnerBaseLogic getSpawnerBaseLogic()
    {
        return this.spawnerLogic;
    }
}

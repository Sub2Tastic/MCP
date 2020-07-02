package net.minecraft.tileentity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.BlockSkull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;

public class TileEntitySkull extends TileEntity implements ITickable
{
    private int field_145908_a;
    private int field_145910_i;
    private GameProfile playerProfile;
    private int dragonAnimatedTicks;
    private boolean dragonAnimated;
    private static PlayerProfileCache profileCache;
    private static MinecraftSessionService sessionService;

    public static void setProfileCache(PlayerProfileCache profileCacheIn)
    {
        profileCache = profileCacheIn;
    }

    public static void setSessionService(MinecraftSessionService sessionServiceIn)
    {
        sessionService = sessionServiceIn;
    }

    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);
        compound.putByte("SkullType", (byte)(this.field_145908_a & 255));
        compound.putByte("Rot", (byte)(this.field_145910_i & 255));

        if (this.playerProfile != null)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTUtil.writeGameProfile(nbttagcompound, this.playerProfile);
            compound.func_74782_a("Owner", nbttagcompound);
        }

        return compound;
    }

    public void read(NBTTagCompound compound)
    {
        super.read(compound);
        this.field_145908_a = compound.getByte("SkullType");
        this.field_145910_i = compound.getByte("Rot");

        if (this.field_145908_a == 3)
        {
            if (compound.contains("Owner", 10))
            {
                this.playerProfile = NBTUtil.readGameProfile(compound.getCompound("Owner"));
            }
            else if (compound.contains("ExtraType", 8))
            {
                String s = compound.getString("ExtraType");

                if (!StringUtils.isNullOrEmpty(s))
                {
                    this.playerProfile = new GameProfile((UUID)null, s);
                    this.updatePlayerProfile();
                }
            }
        }
    }

    public void tick()
    {
        if (this.field_145908_a == 5)
        {
            if (this.world.isBlockPowered(this.pos))
            {
                this.dragonAnimated = true;
                ++this.dragonAnimatedTicks;
            }
            else
            {
                this.dragonAnimated = false;
            }
        }
    }

    public float getAnimationProgress(float p_184295_1_)
    {
        return this.dragonAnimated ? (float)this.dragonAnimatedTicks + p_184295_1_ : (float)this.dragonAnimatedTicks;
    }

    @Nullable
    public GameProfile getPlayerProfile()
    {
        return this.playerProfile;
    }

    @Nullable

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 4, this.getUpdateTag());
    }

    /**
     * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
     * many blocks change at once. This compound comes back to you clientside in {@link handleUpdateTag}
     */
    public NBTTagCompound getUpdateTag()
    {
        return this.write(new NBTTagCompound());
    }

    public void func_152107_a(int p_152107_1_)
    {
        this.field_145908_a = p_152107_1_;
        this.playerProfile = null;
    }

    public void func_152106_a(@Nullable GameProfile p_152106_1_)
    {
        this.field_145908_a = 3;
        this.playerProfile = p_152106_1_;
        this.updatePlayerProfile();
    }

    private void updatePlayerProfile()
    {
        this.playerProfile = updateGameProfile(this.playerProfile);
        this.markDirty();
    }

    public static GameProfile updateGameProfile(GameProfile input)
    {
        if (input != null && !StringUtils.isNullOrEmpty(input.getName()))
        {
            if (input.isComplete() && input.getProperties().containsKey("textures"))
            {
                return input;
            }
            else if (profileCache != null && sessionService != null)
            {
                GameProfile gameprofile = profileCache.getGameProfileForUsername(input.getName());

                if (gameprofile == null)
                {
                    return input;
                }
                else
                {
                    Property property = (Property)Iterables.getFirst(gameprofile.getProperties().get("textures"), (Object)null);

                    if (property == null)
                    {
                        gameprofile = sessionService.fillProfileProperties(gameprofile, true);
                    }

                    return gameprofile;
                }
            }
            else
            {
                return input;
            }
        }
        else
        {
            return input;
        }
    }

    public int func_145904_a()
    {
        return this.field_145908_a;
    }

    public int func_145906_b()
    {
        return this.field_145910_i;
    }

    public void func_145903_a(int p_145903_1_)
    {
        this.field_145910_i = p_145903_1_;
    }

    public void mirror(Mirror mirrorIn)
    {
        if (this.world != null && this.world.getBlockState(this.getPos()).get(BlockSkull.field_176418_a) == EnumFacing.UP)
        {
            this.field_145910_i = mirrorIn.mirrorRotation(this.field_145910_i, 16);
        }
    }

    public void rotate(Rotation rotationIn)
    {
        if (this.world != null && this.world.getBlockState(this.getPos()).get(BlockSkull.field_176418_a) == EnumFacing.UP)
        {
            this.field_145910_i = rotationIn.rotate(this.field_145910_i, 16);
        }
    }
}

package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;

public class TileEntitySign extends TileEntity
{
    public final ITextComponent[] signText = new ITextComponent[] {new TextComponentString(""), new TextComponentString(""), new TextComponentString(""), new TextComponentString("")};
    public int field_145918_i = -1;
    private boolean isEditable = true;
    private EntityPlayer player;
    private final CommandResultStats field_174883_i = new CommandResultStats();

    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);

        for (int i = 0; i < 4; ++i)
        {
            String s = ITextComponent.Serializer.toJson(this.signText[i]);
            compound.putString("Text" + (i + 1), s);
        }

        this.field_174883_i.func_179670_b(compound);
        return compound;
    }

    protected void func_190201_b(World p_190201_1_)
    {
        this.func_145834_a(p_190201_1_);
    }

    public void read(NBTTagCompound compound)
    {
        this.isEditable = false;
        super.read(compound);
        ICommandSender icommandsender = new ICommandSender()
        {
            public String func_70005_c_()
            {
                return "Sign";
            }
            public boolean func_70003_b(int p_70003_1_, String p_70003_2_)
            {
                return true;
            }
            public BlockPos getPosition()
            {
                return TileEntitySign.this.pos;
            }
            public Vec3d getPositionVector()
            {
                return new Vec3d((double)TileEntitySign.this.pos.getX() + 0.5D, (double)TileEntitySign.this.pos.getY() + 0.5D, (double)TileEntitySign.this.pos.getZ() + 0.5D);
            }
            public World getEntityWorld()
            {
                return TileEntitySign.this.world;
            }
            public MinecraftServer getServer()
            {
                return TileEntitySign.this.world.getServer();
            }
        };

        for (int i = 0; i < 4; ++i)
        {
            String s = compound.getString("Text" + (i + 1));
            ITextComponent itextcomponent = ITextComponent.Serializer.fromJson(s);

            try
            {
                this.signText[i] = TextComponentUtils.func_179985_a(icommandsender, itextcomponent, (Entity)null);
            }
            catch (CommandException var7)
            {
                this.signText[i] = itextcomponent;
            }
        }

        this.field_174883_i.func_179668_a(compound);
    }

    @Nullable

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 9, this.getUpdateTag());
    }

    /**
     * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
     * many blocks change at once. This compound comes back to you clientside in {@link handleUpdateTag}
     */
    public NBTTagCompound getUpdateTag()
    {
        return this.write(new NBTTagCompound());
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

    public boolean getIsEditable()
    {
        return this.isEditable;
    }

    /**
     * Sets the sign's isEditable flag to the specified parameter.
     */
    public void setEditable(boolean isEditableIn)
    {
        this.isEditable = isEditableIn;

        if (!isEditableIn)
        {
            this.player = null;
        }
    }

    public void setPlayer(EntityPlayer playerIn)
    {
        this.player = playerIn;
    }

    public EntityPlayer getPlayer()
    {
        return this.player;
    }

    public boolean executeCommand(final EntityPlayer playerIn)
    {
        ICommandSender icommandsender = new ICommandSender()
        {
            public String func_70005_c_()
            {
                return playerIn.func_70005_c_();
            }
            public ITextComponent getDisplayName()
            {
                return playerIn.getDisplayName();
            }
            public void sendMessage(ITextComponent component)
            {
            }
            public boolean func_70003_b(int p_70003_1_, String p_70003_2_)
            {
                return p_70003_1_ <= 2;
            }
            public BlockPos getPosition()
            {
                return TileEntitySign.this.pos;
            }
            public Vec3d getPositionVector()
            {
                return new Vec3d((double)TileEntitySign.this.pos.getX() + 0.5D, (double)TileEntitySign.this.pos.getY() + 0.5D, (double)TileEntitySign.this.pos.getZ() + 0.5D);
            }
            public World getEntityWorld()
            {
                return playerIn.getEntityWorld();
            }
            public Entity func_174793_f()
            {
                return playerIn;
            }
            public boolean func_174792_t_()
            {
                return false;
            }
            public void func_174794_a(CommandResultStats.Type p_174794_1_, int p_174794_2_)
            {
                if (TileEntitySign.this.world != null && !TileEntitySign.this.world.isRemote)
                {
                    TileEntitySign.this.field_174883_i.func_184932_a(TileEntitySign.this.world.getServer(), this, p_174794_1_, p_174794_2_);
                }
            }
            public MinecraftServer getServer()
            {
                return playerIn.getServer();
            }
        };

        for (ITextComponent itextcomponent : this.signText)
        {
            Style style = itextcomponent == null ? null : itextcomponent.getStyle();

            if (style != null && style.getClickEvent() != null)
            {
                ClickEvent clickevent = style.getClickEvent();

                if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND)
                {
                    playerIn.getServer().func_71187_D().func_71556_a(icommandsender, clickevent.getValue());
                }
            }
        }

        return true;
    }

    public CommandResultStats func_174880_d()
    {
        return this.field_174883_i;
    }
}

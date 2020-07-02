package net.minecraft.command;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public interface ICommandSender
{
    String func_70005_c_();

default ITextComponent getDisplayName()
    {
        return new TextComponentString(this.func_70005_c_());
    }

default void sendMessage(ITextComponent component)
    {
    }

    boolean func_70003_b(int p_70003_1_, String p_70003_2_);

default BlockPos getPosition()
    {
        return BlockPos.ZERO;
    }

default Vec3d getPositionVector()
    {
        return Vec3d.ZERO;
    }

    /**
     * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the overworld
     */
    World getEntityWorld();

    @Nullable

default Entity func_174793_f()
    {
        return null;
    }

default boolean func_174792_t_()
    {
        return false;
    }

default void func_174794_a(CommandResultStats.Type p_174794_1_, int p_174794_2_)
    {
    }

    @Nullable

    /**
     * Get the Minecraft server instance
     */
    MinecraftServer getServer();
}

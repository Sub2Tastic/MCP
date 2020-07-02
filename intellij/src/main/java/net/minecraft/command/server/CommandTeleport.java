package net.minecraft.command.server;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CommandTeleport extends CommandBase
{
    public String func_71517_b()
    {
        return "teleport";
    }

    public int func_82362_a()
    {
        return 2;
    }

    public String func_71518_a(ICommandSender p_71518_1_)
    {
        return "commands.teleport.usage";
    }

    public void func_184881_a(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException
    {
        if (p_184881_3_.length < 4)
        {
            throw new WrongUsageException("commands.teleport.usage", new Object[0]);
        }
        else
        {
            Entity entity = func_184885_b(p_184881_1_, p_184881_2_, p_184881_3_[0]);

            if (entity.world != null)
            {
                int i = 4096;
                Vec3d vec3d = p_184881_2_.getPositionVector();
                int j = 1;
                CommandBase.CoordinateArg commandbase$coordinatearg = func_175770_a(vec3d.x, p_184881_3_[j++], true);
                CommandBase.CoordinateArg commandbase$coordinatearg1 = func_175767_a(vec3d.y, p_184881_3_[j++], -4096, 4096, false);
                CommandBase.CoordinateArg commandbase$coordinatearg2 = func_175770_a(vec3d.z, p_184881_3_[j++], true);
                Entity entity1 = p_184881_2_.func_174793_f() == null ? entity : p_184881_2_.func_174793_f();
                CommandBase.CoordinateArg commandbase$coordinatearg3 = func_175770_a(p_184881_3_.length > j ? (double)entity1.rotationYaw : (double)entity.rotationYaw, p_184881_3_.length > j ? p_184881_3_[j] : "~", false);
                ++j;
                CommandBase.CoordinateArg commandbase$coordinatearg4 = func_175770_a(p_184881_3_.length > j ? (double)entity1.rotationPitch : (double)entity.rotationPitch, p_184881_3_.length > j ? p_184881_3_[j] : "~", false);
                func_189862_a(entity, commandbase$coordinatearg, commandbase$coordinatearg1, commandbase$coordinatearg2, commandbase$coordinatearg3, commandbase$coordinatearg4);
                func_152373_a(p_184881_2_, this, "commands.teleport.success.coordinates", new Object[] {entity.func_70005_c_(), commandbase$coordinatearg.func_179628_a(), commandbase$coordinatearg1.func_179628_a(), commandbase$coordinatearg2.func_179628_a()});
            }
        }
    }

    private static void func_189862_a(Entity p_189862_0_, CommandBase.CoordinateArg p_189862_1_, CommandBase.CoordinateArg p_189862_2_, CommandBase.CoordinateArg p_189862_3_, CommandBase.CoordinateArg p_189862_4_, CommandBase.CoordinateArg p_189862_5_)
    {
        if (p_189862_0_ instanceof EntityPlayerMP)
        {
            Set<SPacketPlayerPosLook.EnumFlags> set = EnumSet.<SPacketPlayerPosLook.EnumFlags>noneOf(SPacketPlayerPosLook.EnumFlags.class);
            float f = (float)p_189862_4_.func_179629_b();

            if (p_189862_4_.func_179630_c())
            {
                set.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
            }
            else
            {
                f = MathHelper.wrapDegrees(f);
            }

            float f1 = (float)p_189862_5_.func_179629_b();

            if (p_189862_5_.func_179630_c())
            {
                set.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
            }
            else
            {
                f1 = MathHelper.wrapDegrees(f1);
            }

            p_189862_0_.stopRiding();
            ((EntityPlayerMP)p_189862_0_).connection.setPlayerLocation(p_189862_1_.func_179628_a(), p_189862_2_.func_179628_a(), p_189862_3_.func_179628_a(), f, f1, set);
            p_189862_0_.setRotationYawHead(f);
        }
        else
        {
            float f2 = (float)MathHelper.wrapDegrees(p_189862_4_.func_179628_a());
            float f3 = (float)MathHelper.wrapDegrees(p_189862_5_.func_179628_a());
            f3 = MathHelper.clamp(f3, -90.0F, 90.0F);
            p_189862_0_.setLocationAndAngles(p_189862_1_.func_179628_a(), p_189862_2_.func_179628_a(), p_189862_3_.func_179628_a(), f2, f3);
            p_189862_0_.setRotationYawHead(f2);
        }

        if (!(p_189862_0_ instanceof EntityLivingBase) || !((EntityLivingBase)p_189862_0_).isElytraFlying())
        {
            p_189862_0_.field_70181_x = 0.0D;
            p_189862_0_.onGround = true;
        }
    }

    public List<String> func_184883_a(MinecraftServer p_184883_1_, ICommandSender p_184883_2_, String[] p_184883_3_, @Nullable BlockPos p_184883_4_)
    {
        if (p_184883_3_.length == 1)
        {
            return func_71530_a(p_184883_3_, p_184883_1_.getOnlinePlayerNames());
        }
        else
        {
            return p_184883_3_.length > 1 && p_184883_3_.length <= 4 ? func_175771_a(p_184883_3_, 1, p_184883_4_) : Collections.emptyList();
        }
    }

    public boolean func_82358_a(String[] p_82358_1_, int p_82358_2_)
    {
        return p_82358_2_ == 0;
    }
}

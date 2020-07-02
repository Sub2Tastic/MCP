package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class EntityOtherPlayerMP extends AbstractClientPlayer
{
    private int field_71184_b;
    private double field_71185_c;
    private double field_71182_d;
    private double field_71183_e;
    private double field_71180_f;
    private double field_71181_g;

    public EntityOtherPlayerMP(World p_i45075_1_, GameProfile p_i45075_2_)
    {
        super(p_i45075_1_, p_i45075_2_);
        this.stepHeight = 1.0F;
        this.noClip = true;
        this.field_71082_cx = 0.25F;
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getBoundingBox().getAverageEdgeLength() * 10.0D;

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * getRenderDistanceWeight();
        return distance < d0 * d0;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return true;
    }

    /**
     * Sets a target for the client to interpolate towards over the next few ticks
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        this.field_71185_c = x;
        this.field_71182_d = y;
        this.field_71183_e = z;
        this.field_71180_f = (double)yaw;
        this.field_71181_g = (double)pitch;
        this.field_71184_b = posRotationIncrements;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        this.field_71082_cx = 0.0F;
        super.tick();
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d0 = this.posX - this.prevPosX;
        double d1 = this.posZ - this.prevPosZ;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        this.limbSwingAmount += (f - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick()
    {
        if (this.field_71184_b > 0)
        {
            double d0 = this.posX + (this.field_71185_c - this.posX) / (double)this.field_71184_b;
            double d1 = this.posY + (this.field_71182_d - this.posY) / (double)this.field_71184_b;
            double d2 = this.posZ + (this.field_71183_e - this.posZ) / (double)this.field_71184_b;
            double d3;

            for (d3 = this.field_71180_f - (double)this.rotationYaw; d3 < -180.0D; d3 += 360.0D)
            {
                ;
            }

            while (d3 >= 180.0D)
            {
                d3 -= 360.0D;
            }

            this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.field_71184_b);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.field_71181_g - (double)this.rotationPitch) / (double)this.field_71184_b);
            --this.field_71184_b;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }

        this.prevCameraYaw = this.cameraYaw;
        this.updateArmSwingProgress();
        float f1 = MathHelper.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
        float f = (float)Math.atan(-this.field_70181_x * 0.20000000298023224D) * 15.0F;

        if (f1 > 0.1F)
        {
            f1 = 0.1F;
        }

        if (!this.onGround || this.getHealth() <= 0.0F)
        {
            f1 = 0.0F;
        }

        if (this.onGround || this.getHealth() <= 0.0F)
        {
            f = 0.0F;
        }

        this.cameraYaw += (f1 - this.cameraYaw) * 0.4F;
        this.field_70726_aT += (f - this.field_70726_aT) * 0.8F;
        this.world.profiler.startSection("push");
        this.collideWithNearbyEntities();
        this.world.profiler.endSection();
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void sendMessage(ITextComponent component)
    {
        Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(component);
    }

    public boolean func_70003_b(int p_70003_1_, String p_70003_2_)
    {
        return false;
    }

    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    public BlockPos getPosition()
    {
        return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
    }
}

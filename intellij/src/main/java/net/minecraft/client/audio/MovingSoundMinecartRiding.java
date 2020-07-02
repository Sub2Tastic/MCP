package net.minecraft.client.audio;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class MovingSoundMinecartRiding extends MovingSound
{
    private final EntityPlayer player;
    private final EntityMinecart minecart;

    public MovingSoundMinecartRiding(EntityPlayer p_i45106_1_, EntityMinecart p_i45106_2_)
    {
        super(SoundEvents.ENTITY_MINECART_INSIDE, SoundCategory.NEUTRAL);
        this.player = p_i45106_1_;
        this.minecart = p_i45106_2_;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    public void tick()
    {
        if (!this.minecart.removed && this.player.isPassenger() && this.player.getRidingEntity() == this.minecart)
        {
            float f = MathHelper.sqrt(this.minecart.field_70159_w * this.minecart.field_70159_w + this.minecart.field_70179_y * this.minecart.field_70179_y);

            if ((double)f >= 0.01D)
            {
                this.volume = 0.0F + MathHelper.clamp(f, 0.0F, 1.0F) * 0.75F;
            }
            else
            {
                this.volume = 0.0F;
            }
        }
        else
        {
            this.donePlaying = true;
        }
    }
}

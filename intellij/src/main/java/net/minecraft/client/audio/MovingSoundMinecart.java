package net.minecraft.client.audio;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class MovingSoundMinecart extends MovingSound
{
    private final EntityMinecart minecart;
    private float distance = 0.0F;

    public MovingSoundMinecart(EntityMinecart p_i45105_1_)
    {
        super(SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.NEUTRAL);
        this.minecart = p_i45105_1_;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    public void tick()
    {
        if (this.minecart.removed)
        {
            this.donePlaying = true;
        }
        else
        {
            this.x = (float)this.minecart.posX;
            this.y = (float)this.minecart.posY;
            this.z = (float)this.minecart.posZ;
            float f = MathHelper.sqrt(this.minecart.field_70159_w * this.minecart.field_70159_w + this.minecart.field_70179_y * this.minecart.field_70179_y);

            if ((double)f >= 0.01D)
            {
                this.distance = MathHelper.clamp(this.distance + 0.0025F, 0.0F, 1.0F);
                this.volume = 0.0F + MathHelper.clamp(f, 0.0F, 0.5F) * 0.7F;
            }
            else
            {
                this.distance = 0.0F;
                this.volume = 0.0F;
            }
        }
    }
}

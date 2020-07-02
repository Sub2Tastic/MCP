package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class PositionedSoundRecord extends PositionedSound
{
    public PositionedSoundRecord(SoundEvent soundIn, SoundCategory categoryIn, float volumeIn, float pitchIn, BlockPos pos)
    {
        this(soundIn, categoryIn, volumeIn, pitchIn, (float)pos.getX() + 0.5F, (float)pos.getY() + 0.5F, (float)pos.getZ() + 0.5F);
    }

    public static PositionedSoundRecord master(SoundEvent soundIn, float pitchIn)
    {
        return master(soundIn, pitchIn, 0.25F);
    }

    public static PositionedSoundRecord master(SoundEvent soundIn, float pitchIn, float volumeIn)
    {
        return new PositionedSoundRecord(soundIn, SoundCategory.MASTER, volumeIn, pitchIn, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
    }

    public static PositionedSoundRecord music(SoundEvent soundIn)
    {
        return new PositionedSoundRecord(soundIn, SoundCategory.MUSIC, 1.0F, 1.0F, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
    }

    public static PositionedSoundRecord record(SoundEvent soundIn, float xIn, float yIn, float zIn)
    {
        return new PositionedSoundRecord(soundIn, SoundCategory.RECORDS, 4.0F, 1.0F, false, 0, ISound.AttenuationType.LINEAR, xIn, yIn, zIn);
    }

    public PositionedSoundRecord(SoundEvent soundIn, SoundCategory categoryIn, float volumeIn, float pitchIn, float xIn, float yIn, float zIn)
    {
        this(soundIn, categoryIn, volumeIn, pitchIn, false, 0, ISound.AttenuationType.LINEAR, xIn, yIn, zIn);
    }

    private PositionedSoundRecord(SoundEvent soundIn, SoundCategory categoryIn, float volumeIn, float pitchIn, boolean repeatIn, int repeatDelayIn, ISound.AttenuationType attenuationTypeIn, float xIn, float yIn, float zIn)
    {
        this(soundIn.getName(), categoryIn, volumeIn, pitchIn, repeatIn, repeatDelayIn, attenuationTypeIn, xIn, yIn, zIn);
    }

    public PositionedSoundRecord(ResourceLocation p_i46530_1_, SoundCategory p_i46530_2_, float p_i46530_3_, float p_i46530_4_, boolean p_i46530_5_, int p_i46530_6_, ISound.AttenuationType p_i46530_7_, float p_i46530_8_, float p_i46530_9_, float p_i46530_10_)
    {
        super(p_i46530_1_, p_i46530_2_);
        this.volume = p_i46530_3_;
        this.pitch = p_i46530_4_;
        this.x = p_i46530_8_;
        this.y = p_i46530_9_;
        this.z = p_i46530_10_;
        this.repeat = p_i46530_5_;
        this.repeatDelay = p_i46530_6_;
        this.attenuationType = p_i46530_7_;
    }
}

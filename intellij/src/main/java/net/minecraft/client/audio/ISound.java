package net.minecraft.client.audio;

import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public interface ISound
{
    ResourceLocation getSoundLocation();

    @Nullable
    SoundEventAccessor createAccessor(SoundHandler handler);

    Sound getSound();

    SoundCategory getCategory();

    boolean canRepeat();

    int getRepeatDelay();

    float getVolume();

    float getPitch();

    float getX();

    float getY();

    float getZ();

    ISound.AttenuationType getAttenuationType();

    public static enum AttenuationType
    {
        NONE(0),
        LINEAR(2);

        private final int field_148589_c;

        private AttenuationType(int p_i45110_3_)
        {
            this.field_148589_c = p_i45110_3_;
        }

        public int func_148586_a()
        {
            return this.field_148589_c;
        }
    }
}

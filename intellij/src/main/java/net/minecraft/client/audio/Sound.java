package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public class Sound implements ISoundEventAccessor<Sound>
{
    private final ResourceLocation name;
    private final float volume;
    private final float pitch;
    private final int weight;
    private final Sound.Type type;
    private final boolean streaming;

    public Sound(String p_i46526_1_, float p_i46526_2_, float p_i46526_3_, int p_i46526_4_, Sound.Type p_i46526_5_, boolean p_i46526_6_)
    {
        this.name = new ResourceLocation(p_i46526_1_);
        this.volume = p_i46526_2_;
        this.pitch = p_i46526_3_;
        this.weight = p_i46526_4_;
        this.type = p_i46526_5_;
        this.streaming = p_i46526_6_;
    }

    public ResourceLocation getSoundLocation()
    {
        return this.name;
    }

    public ResourceLocation getSoundAsOggLocation()
    {
        return new ResourceLocation(this.name.getNamespace(), "sounds/" + this.name.getPath() + ".ogg");
    }

    public float getVolume()
    {
        return this.volume;
    }

    public float getPitch()
    {
        return this.pitch;
    }

    public int getWeight()
    {
        return this.weight;
    }

    public Sound cloneEntry()
    {
        return this;
    }

    public Sound.Type getType()
    {
        return this.type;
    }

    public boolean isStreaming()
    {
        return this.streaming;
    }

    public static enum Type
    {
        FILE("file"),
        SOUND_EVENT("event");

        private final String name;

        private Type(String nameIn)
        {
            this.name = nameIn;
        }

        public static Sound.Type getByName(String nameIn)
        {
            for (Sound.Type sound$type : values())
            {
                if (sound$type.name.equals(nameIn))
                {
                    return sound$type;
                }
            }

            return null;
        }
    }
}

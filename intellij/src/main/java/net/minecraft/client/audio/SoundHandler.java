package net.minecraft.client.audio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoundHandler implements IResourceManagerReloadListener, ITickable
{
    public static final Sound MISSING_SOUND = new Sound("meta:missing_sound", 1.0F, 1.0F, 1, Sound.Type.FILE, false);
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer()).registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
    private static final ParameterizedType TYPE = new ParameterizedType()
    {
        public Type[] getActualTypeArguments()
        {
            return new Type[] {String.class, SoundList.class};
        }
        public Type getRawType()
        {
            return Map.class;
        }
        public Type getOwnerType()
        {
            return null;
        }
    };
    private final SoundRegistry soundRegistry = new SoundRegistry();
    private final SoundManager sndManager;
    private final IResourceManager field_147695_g;

    public SoundHandler(IResourceManager manager, GameSettings gameSettingsIn)
    {
        this.field_147695_g = manager;
        this.sndManager = new SoundManager(this, gameSettingsIn);
    }

    public void func_110549_a(IResourceManager p_110549_1_)
    {
        this.soundRegistry.func_148763_c();

        for (String s : p_110549_1_.func_135055_a())
        {
            try
            {
                for (IResource iresource : p_110549_1_.func_135056_b(new ResourceLocation(s, "sounds.json")))
                {
                    try
                    {
                        Map<String, SoundList> map = this.getSoundMap(iresource.func_110527_b());

                        for (Entry<String, SoundList> entry : map.entrySet())
                        {
                            this.func_147693_a(new ResourceLocation(s, entry.getKey()), entry.getValue());
                        }
                    }
                    catch (RuntimeException runtimeexception)
                    {
                        LOGGER.warn("Invalid sounds.json", (Throwable)runtimeexception);
                    }
                }
            }
            catch (IOException var11)
            {
                ;
            }
        }

        for (ResourceLocation resourcelocation : this.soundRegistry.keySet())
        {
            SoundEventAccessor soundeventaccessor = (SoundEventAccessor)this.soundRegistry.getOrDefault(resourcelocation);

            if (soundeventaccessor.getSubtitle() instanceof TextComponentTranslation)
            {
                String s1 = ((TextComponentTranslation)soundeventaccessor.getSubtitle()).getKey();

                if (!I18n.hasKey(s1))
                {
                    LOGGER.debug("Missing subtitle {} for event: {}", s1, resourcelocation);
                }
            }
        }

        for (ResourceLocation resourcelocation1 : this.soundRegistry.keySet())
        {
            if (SoundEvent.field_187505_a.getOrDefault(resourcelocation1) == null)
            {
                LOGGER.debug("Not having sound event for: {}", (Object)resourcelocation1);
            }
        }

        this.sndManager.reload();
    }

    @Nullable
    protected Map<String, SoundList> getSoundMap(InputStream p_175085_1_)
    {
        Map map;

        try
        {
            map = (Map)JsonUtils.fromJson(GSON, new InputStreamReader(p_175085_1_, StandardCharsets.UTF_8), TYPE);
        }
        finally
        {
            IOUtils.closeQuietly(p_175085_1_);
        }

        return map;
    }

    private void func_147693_a(ResourceLocation p_147693_1_, SoundList p_147693_2_)
    {
        SoundEventAccessor soundeventaccessor = (SoundEventAccessor)this.soundRegistry.getOrDefault(p_147693_1_);
        boolean flag = soundeventaccessor == null;

        if (flag || p_147693_2_.canReplaceExisting())
        {
            if (!flag)
            {
                LOGGER.debug("Replaced sound event location {}", (Object)p_147693_1_);
            }

            soundeventaccessor = new SoundEventAccessor(p_147693_1_, p_147693_2_.getSubtitle());
            this.soundRegistry.func_186803_a(soundeventaccessor);
        }

        for (final Sound sound : p_147693_2_.getSounds())
        {
            final ResourceLocation resourcelocation = sound.getSoundLocation();
            ISoundEventAccessor<Sound> isoundeventaccessor;

            switch (sound.getType())
            {
                case FILE:
                    if (!this.func_184401_a(sound, p_147693_1_))
                    {
                        continue;
                    }

                    isoundeventaccessor = sound;
                    break;

                case SOUND_EVENT:
                    isoundeventaccessor = new ISoundEventAccessor<Sound>()
                    {
                        public int getWeight()
                        {
                            SoundEventAccessor soundeventaccessor1 = (SoundEventAccessor)SoundHandler.this.soundRegistry.getOrDefault(resourcelocation);
                            return soundeventaccessor1 == null ? 0 : soundeventaccessor1.getWeight();
                        }
                        public Sound cloneEntry()
                        {
                            SoundEventAccessor soundeventaccessor1 = (SoundEventAccessor)SoundHandler.this.soundRegistry.getOrDefault(resourcelocation);

                            if (soundeventaccessor1 == null)
                            {
                                return SoundHandler.MISSING_SOUND;
                            }
                            else
                            {
                                Sound sound1 = soundeventaccessor1.cloneEntry();
                                return new Sound(sound1.getSoundLocation().toString(), sound1.getVolume() * sound.getVolume(), sound1.getPitch() * sound.getPitch(), sound.getWeight(), Sound.Type.FILE, sound1.isStreaming() || sound.isStreaming());
                            }
                        }
                    };

                    break;
                default:
                    throw new IllegalStateException("Unknown SoundEventRegistration type: " + sound.getType());
            }

            soundeventaccessor.addSound(isoundeventaccessor);
        }
    }

    private boolean func_184401_a(Sound p_184401_1_, ResourceLocation p_184401_2_)
    {
        ResourceLocation resourcelocation = p_184401_1_.getSoundAsOggLocation();
        IResource iresource = null;
        boolean flag;

        try
        {
            iresource = this.field_147695_g.func_110536_a(resourcelocation);
            iresource.func_110527_b();
            return true;
        }
        catch (FileNotFoundException var11)
        {
            LOGGER.warn("File {} does not exist, cannot add it to event {}", resourcelocation, p_184401_2_);
            flag = false;
        }
        catch (IOException ioexception)
        {
            LOGGER.warn("Could not load sound file {}, cannot add it to event {}", resourcelocation, p_184401_2_, ioexception);
            flag = false;
            return flag;
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)iresource);
        }

        return flag;
    }

    @Nullable
    public SoundEventAccessor getAccessor(ResourceLocation location)
    {
        return (SoundEventAccessor)this.soundRegistry.getOrDefault(location);
    }

    /**
     * Play a sound
     */
    public void play(ISound sound)
    {
        this.sndManager.play(sound);
    }

    /**
     * Plays the sound in n ticks
     */
    public void playDelayed(ISound sound, int delay)
    {
        this.sndManager.playDelayed(sound, delay);
    }

    public void func_147691_a(EntityPlayer p_147691_1_, float p_147691_2_)
    {
        this.sndManager.func_148615_a(p_147691_1_, p_147691_2_);
    }

    public void pause()
    {
        this.sndManager.pause();
    }

    public void stop()
    {
        this.sndManager.stopAllSounds();
    }

    public void unloadSounds()
    {
        this.sndManager.unload();
    }

    public void tick()
    {
        this.sndManager.func_148605_d();
    }

    public void resume()
    {
        this.sndManager.resume();
    }

    public void setSoundLevel(SoundCategory category, float volume)
    {
        if (category == SoundCategory.MASTER && volume <= 0.0F)
        {
            this.stop();
        }

        this.sndManager.setVolume(category, volume);
    }

    public void stop(ISound soundIn)
    {
        this.sndManager.stop(soundIn);
    }

    public boolean func_147692_c(ISound p_147692_1_)
    {
        return this.sndManager.func_148597_a(p_147692_1_);
    }

    public void addListener(ISoundEventListener listener)
    {
        this.sndManager.addListener(listener);
    }

    public void removeListener(ISoundEventListener listener)
    {
        this.sndManager.removeListener(listener);
    }

    public void func_189520_a(String p_189520_1_, SoundCategory p_189520_2_)
    {
        this.sndManager.func_189567_a(p_189520_1_, p_189520_2_);
    }
}

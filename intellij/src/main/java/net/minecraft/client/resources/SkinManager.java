package net.minecraft.client.resources;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.InsecureTextureException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class SkinManager
{
    private static final ExecutorService field_152794_b = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue());
    private final TextureManager textureManager;
    private final File skinCacheDir;
    private final MinecraftSessionService sessionService;
    private final LoadingCache<GameProfile, Map<Type, MinecraftProfileTexture>> skinCacheLoader;

    public SkinManager(TextureManager textureManagerInstance, File skinCacheDirectory, MinecraftSessionService sessionService)
    {
        this.textureManager = textureManagerInstance;
        this.skinCacheDir = skinCacheDirectory;
        this.sessionService = sessionService;
        this.skinCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(15L, TimeUnit.SECONDS).<GameProfile, Map<Type, MinecraftProfileTexture>>build(new CacheLoader<GameProfile, Map<Type, MinecraftProfileTexture>>()
        {
            public Map<Type, MinecraftProfileTexture> load(GameProfile p_load_1_) throws Exception
            {
                try
                {
                    return Minecraft.getInstance().getSessionService().getTextures(p_load_1_, false);
                }
                catch (Throwable var3)
                {
                    return Maps.<Type, MinecraftProfileTexture>newHashMap();
                }
            }
        });
    }

    /**
     * Used in the Skull renderer to fetch a skin. May download the skin if it's not in the cache
     */
    public ResourceLocation loadSkin(MinecraftProfileTexture profileTexture, Type textureType)
    {
        return this.loadSkin(profileTexture, textureType, (SkinManager.SkinAvailableCallback)null);
    }

    /**
     * May download the skin if its not in the cache, can be passed a SkinManager#SkinAvailableCallback for handling
     */
    public ResourceLocation loadSkin(final MinecraftProfileTexture profileTexture, final Type textureType, @Nullable final SkinManager.SkinAvailableCallback skinAvailableCallback)
    {
        final ResourceLocation resourcelocation = new ResourceLocation("skins/" + profileTexture.getHash());
        ITextureObject itextureobject = this.textureManager.func_110581_b(resourcelocation);

        if (itextureobject != null)
        {
            if (skinAvailableCallback != null)
            {
                skinAvailableCallback.func_180521_a(textureType, resourcelocation, profileTexture);
            }
        }
        else
        {
            File file1 = new File(this.skinCacheDir, profileTexture.getHash().length() > 2 ? profileTexture.getHash().substring(0, 2) : "xx");
            File file2 = new File(file1, profileTexture.getHash());
            final IImageBuffer iimagebuffer = textureType == Type.SKIN ? new ImageBufferDownload() : null;
            ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(file2, profileTexture.getUrl(), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer()
            {
                public BufferedImage func_78432_a(BufferedImage p_78432_1_)
                {
                    if (iimagebuffer != null)
                    {
                        p_78432_1_ = iimagebuffer.func_78432_a(p_78432_1_);
                    }

                    return p_78432_1_;
                }
                public void func_152634_a()
                {
                    if (iimagebuffer != null)
                    {
                        iimagebuffer.func_152634_a();
                    }

                    if (skinAvailableCallback != null)
                    {
                        skinAvailableCallback.func_180521_a(textureType, resourcelocation, profileTexture);
                    }
                }
            });
            this.textureManager.func_110579_a(resourcelocation, threaddownloadimagedata);
        }

        return resourcelocation;
    }

    public void loadProfileTextures(final GameProfile profile, final SkinManager.SkinAvailableCallback skinAvailableCallback, final boolean requireSecure)
    {
        field_152794_b.submit(new Runnable()
        {
            public void run()
            {
                final Map<Type, MinecraftProfileTexture> map = Maps.<Type, MinecraftProfileTexture>newHashMap();

                try
                {
                    map.putAll(SkinManager.this.sessionService.getTextures(profile, requireSecure));
                }
                catch (InsecureTextureException var3)
                {
                    ;
                }

                if (map.isEmpty() && profile.getId().equals(Minecraft.getInstance().getSession().getProfile().getId()))
                {
                    profile.getProperties().clear();
                    profile.getProperties().putAll(Minecraft.getInstance().getProfileProperties());
                    map.putAll(SkinManager.this.sessionService.getTextures(profile, false));
                }

                Minecraft.getInstance().func_152344_a(new Runnable()
                {
                    public void run()
                    {
                        if (map.containsKey(Type.SKIN))
                        {
                            SkinManager.this.loadSkin(map.get(Type.SKIN), Type.SKIN, skinAvailableCallback);
                        }

                        if (map.containsKey(Type.CAPE))
                        {
                            SkinManager.this.loadSkin(map.get(Type.CAPE), Type.CAPE, skinAvailableCallback);
                        }
                    }
                });
            }
        });
    }

    public Map<Type, MinecraftProfileTexture> loadSkinFromCache(GameProfile profile)
    {
        return (Map)this.skinCacheLoader.getUnchecked(profile);
    }

    public interface SkinAvailableCallback
    {
        void func_180521_a(Type p_180521_1_, ResourceLocation p_180521_2_, MinecraftProfileTexture p_180521_3_);
    }
}

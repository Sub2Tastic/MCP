package net.minecraft.client.main;

import com.mojang.authlib.properties.PropertyMap;
import java.io.File;
import java.net.Proxy;
import javax.annotation.Nullable;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.client.resources.ResourceIndexFolder;
import net.minecraft.util.Session;

public class GameConfiguration
{
    public final GameConfiguration.UserInformation userInfo;
    public final GameConfiguration.DisplayInformation displayInfo;
    public final GameConfiguration.FolderInformation folderInfo;
    public final GameConfiguration.GameInformation gameInfo;
    public final GameConfiguration.ServerInformation serverInfo;

    public GameConfiguration(GameConfiguration.UserInformation p_i45491_1_, GameConfiguration.DisplayInformation p_i45491_2_, GameConfiguration.FolderInformation p_i45491_3_, GameConfiguration.GameInformation p_i45491_4_, GameConfiguration.ServerInformation p_i45491_5_)
    {
        this.userInfo = p_i45491_1_;
        this.displayInfo = p_i45491_2_;
        this.folderInfo = p_i45491_3_;
        this.gameInfo = p_i45491_4_;
        this.serverInfo = p_i45491_5_;
    }

    public static class DisplayInformation
    {
        public final int field_178764_a;
        public final int field_178762_b;
        public final boolean field_178763_c;
        public final boolean field_178761_d;

        public DisplayInformation(int p_i45490_1_, int p_i45490_2_, boolean p_i45490_3_, boolean p_i45490_4_)
        {
            this.field_178764_a = p_i45490_1_;
            this.field_178762_b = p_i45490_2_;
            this.field_178763_c = p_i45490_3_;
            this.field_178761_d = p_i45490_4_;
        }
    }

    public static class FolderInformation
    {
        public final File gameDir;
        public final File resourcePacksDir;
        public final File assetsDir;
        public final String assetIndex;

        public FolderInformation(File mcDataDirIn, File resourcePacksDirIn, File assetsDirIn, @Nullable String assetIndexIn)
        {
            this.gameDir = mcDataDirIn;
            this.resourcePacksDir = resourcePacksDirIn;
            this.assetsDir = assetsDirIn;
            this.assetIndex = assetIndexIn;
        }

        public ResourceIndex getAssetsIndex()
        {
            return (ResourceIndex)(this.assetIndex == null ? new ResourceIndexFolder(this.assetsDir) : new ResourceIndex(this.assetsDir, this.assetIndex));
        }
    }

    public static class GameInformation
    {
        public final boolean isDemo;
        public final String version;
        public final String versionType;

        public GameInformation(boolean demo, String versionIn, String versionTypeIn)
        {
            this.isDemo = demo;
            this.version = versionIn;
            this.versionType = versionTypeIn;
        }
    }

    public static class ServerInformation
    {
        public final String serverName;
        public final int serverPort;

        public ServerInformation(String serverNameIn, int serverPortIn)
        {
            this.serverName = serverNameIn;
            this.serverPort = serverPortIn;
        }
    }

    public static class UserInformation
    {
        public final Session session;
        public final PropertyMap userProperties;
        public final PropertyMap profileProperties;
        public final Proxy proxy;

        public UserInformation(Session sessionIn, PropertyMap userPropertiesIn, PropertyMap profilePropertiesIn, Proxy proxyIn)
        {
            this.session = sessionIn;
            this.userProperties = userPropertiesIn;
            this.profileProperties = profilePropertiesIn;
            this.proxy = proxyIn;
        }
    }
}

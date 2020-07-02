package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

public interface IResourcePack
{
    InputStream func_110590_a(ResourceLocation p_110590_1_) throws IOException;

    boolean func_110589_b(ResourceLocation p_110589_1_);

    Set<String> func_110587_b();

    @Nullable
    <T extends IMetadataSection> T func_135058_a(MetadataSerializer p_135058_1_, String p_135058_2_) throws IOException;

    BufferedImage func_110586_a() throws IOException;

    String func_130077_b();
}

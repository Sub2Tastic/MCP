package net.minecraft.client.resources;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.minecraft.util.ResourceLocation;

public interface IResourceManager
{
    Set<String> func_135055_a();

    IResource func_110536_a(ResourceLocation p_110536_1_) throws IOException;

    List<IResource> func_135056_b(ResourceLocation p_135056_1_) throws IOException;
}

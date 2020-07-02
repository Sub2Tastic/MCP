package net.minecraft.client.renderer.texture;

import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;

public interface ITextureObject
{
    void func_174936_b(boolean p_174936_1_, boolean p_174936_2_);

    void func_174935_a();

    void func_110551_a(IResourceManager p_110551_1_) throws IOException;

    int getGlTextureId();
}

package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.world.World;

public interface IParticleFactory
{
    @Nullable
    Particle func_178902_a(int p_178902_1_, World p_178902_2_, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_);
}

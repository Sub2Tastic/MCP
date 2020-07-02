package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemClock extends Item
{
    public ItemClock()
    {
        this.addPropertyOverride(new ResourceLocation("time"), new IItemPropertyGetter()
        {
            double rotation;
            double rota;
            long lastUpdateTick;
            public float func_185085_a(ItemStack p_185085_1_, @Nullable World p_185085_2_, @Nullable EntityLivingBase p_185085_3_)
            {
                boolean flag = p_185085_3_ != null;
                Entity entity = (Entity)(flag ? p_185085_3_ : p_185085_1_.getItemFrame());

                if (p_185085_2_ == null && entity != null)
                {
                    p_185085_2_ = entity.world;
                }

                if (p_185085_2_ == null)
                {
                    return 0.0F;
                }
                else
                {
                    double d0;

                    if (p_185085_2_.dimension.isSurfaceWorld())
                    {
                        d0 = (double)p_185085_2_.getCelestialAngle(1.0F);
                    }
                    else
                    {
                        d0 = Math.random();
                    }

                    d0 = this.wobble(p_185085_2_, d0);
                    return (float)d0;
                }
            }
            private double wobble(World p_185087_1_, double p_185087_2_)
            {
                if (p_185087_1_.getGameTime() != this.lastUpdateTick)
                {
                    this.lastUpdateTick = p_185087_1_.getGameTime();
                    double d0 = p_185087_2_ - this.rotation;
                    d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
                    this.rota += d0 * 0.1D;
                    this.rota *= 0.9D;
                    this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
                }

                return this.rotation;
            }
        });
    }
}

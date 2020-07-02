package net.minecraft.client.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleDrip extends Particle
{
    private final Material field_70563_a;
    private int field_70564_aq;

    protected ParticleDrip(World p_i1203_1_, double p_i1203_2_, double p_i1203_4_, double p_i1203_6_, Material p_i1203_8_)
    {
        super(p_i1203_1_, p_i1203_2_, p_i1203_4_, p_i1203_6_, 0.0D, 0.0D, 0.0D);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;

        if (p_i1203_8_ == Material.WATER)
        {
            this.particleRed = 0.0F;
            this.particleGreen = 0.0F;
            this.particleBlue = 1.0F;
        }
        else
        {
            this.particleRed = 1.0F;
            this.particleGreen = 0.0F;
            this.particleBlue = 0.0F;
        }

        this.func_70536_a(113);
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.field_70563_a = p_i1203_8_;
        this.field_70564_aq = 40;
        this.maxAge = (int)(64.0D / (Math.random() * 0.8D + 0.2D));
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
    }

    public int getBrightnessForRender(float partialTick)
    {
        return this.field_70563_a == Material.WATER ? super.getBrightnessForRender(partialTick) : 257;
    }

    public void tick()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.field_70563_a == Material.WATER)
        {
            this.particleRed = 0.2F;
            this.particleGreen = 0.3F;
            this.particleBlue = 1.0F;
        }
        else
        {
            this.particleRed = 1.0F;
            this.particleGreen = 16.0F / (float)(40 - this.field_70564_aq + 16);
            this.particleBlue = 4.0F / (float)(40 - this.field_70564_aq + 8);
        }

        this.motionY -= (double)this.particleGravity;

        if (this.field_70564_aq-- > 0)
        {
            this.motionX *= 0.02D;
            this.motionY *= 0.02D;
            this.motionZ *= 0.02D;
            this.func_70536_a(113);
        }
        else
        {
            this.func_70536_a(112);
        }

        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.maxAge-- <= 0)
        {
            this.setExpired();
        }

        if (this.onGround)
        {
            if (this.field_70563_a == Material.WATER)
            {
                this.setExpired();
                this.world.func_175688_a(EnumParticleTypes.WATER_SPLASH, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            }
            else
            {
                this.func_70536_a(114);
            }

            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        Material material = iblockstate.getMaterial();

        if (material.isLiquid() || material.isSolid())
        {
            double d0 = 0.0D;

            if (iblockstate.getBlock() instanceof BlockLiquid)
            {
                d0 = (double)BlockLiquid.func_149801_b(((Integer)iblockstate.get(BlockLiquid.LEVEL)).intValue());
            }

            double d1 = (double)(MathHelper.floor(this.posY) + 1) - d0;

            if (this.posY < d1)
            {
                this.setExpired();
            }
        }
    }

    public static class LavaFactory implements IParticleFactory
    {
        public Particle func_178902_a(int p_178902_1_, World p_178902_2_, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_)
        {
            return new ParticleDrip(p_178902_2_, p_178902_3_, p_178902_5_, p_178902_7_, Material.LAVA);
        }
    }

    public static class WaterFactory implements IParticleFactory
    {
        public Particle func_178902_a(int p_178902_1_, World p_178902_2_, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_)
        {
            return new ParticleDrip(p_178902_2_, p_178902_3_, p_178902_5_, p_178902_7_, Material.WATER);
        }
    }
}

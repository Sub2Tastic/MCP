package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleFallingDust extends Particle
{
    float field_190018_a;
    final float rotSpeed;

    protected ParticleFallingDust(World p_i47135_1_, double p_i47135_2_, double p_i47135_4_, double p_i47135_6_, float p_i47135_8_, float p_i47135_9_, float p_i47135_10_)
    {
        super(p_i47135_1_, p_i47135_2_, p_i47135_4_, p_i47135_6_, 0.0D, 0.0D, 0.0D);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.particleRed = p_i47135_8_;
        this.particleGreen = p_i47135_9_;
        this.particleBlue = p_i47135_10_;
        float f = 0.9F;
        this.particleScale *= 0.75F;
        this.particleScale *= 0.9F;
        this.field_190018_a = this.particleScale;
        this.maxAge = (int)(32.0D / (Math.random() * 0.8D + 0.2D));
        this.maxAge = (int)((float)this.maxAge * 0.9F);
        this.rotSpeed = ((float)Math.random() - 0.5F) * 0.1F;
        this.particleAngle = (float)Math.random() * ((float)Math.PI * 2F);
    }

    public void func_180434_a(BufferBuilder p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        float f = ((float)this.age + p_180434_3_) / (float)this.maxAge * 32.0F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        this.particleScale = this.field_190018_a * f;
        super.func_180434_a(p_180434_1_, p_180434_2_, p_180434_3_, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
    }

    public void tick()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.age++ >= this.maxAge)
        {
            this.setExpired();
        }

        this.prevParticleAngle = this.particleAngle;
        this.particleAngle += (float)Math.PI * this.rotSpeed * 2.0F;

        if (this.onGround)
        {
            this.prevParticleAngle = this.particleAngle = 0.0F;
        }

        this.func_70536_a(7 - this.age * 8 / this.maxAge);
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionY -= 0.003000000026077032D;
        this.motionY = Math.max(this.motionY, -0.14000000059604645D);
    }

    public static class Factory implements IParticleFactory
    {
        @Nullable
        public Particle func_178902_a(int p_178902_1_, World p_178902_2_, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_)
        {
            IBlockState iblockstate = Block.func_176220_d(p_178902_15_[0]);

            if (iblockstate.getBlock() != Blocks.AIR && iblockstate.getRenderType() == EnumBlockRenderType.INVISIBLE)
            {
                return null;
            }
            else
            {
                int i = Minecraft.getInstance().getBlockColors().getColorOrMaterialColor(iblockstate, p_178902_2_, new BlockPos(p_178902_3_, p_178902_5_, p_178902_7_));

                if (iblockstate.getBlock() instanceof BlockFalling)
                {
                    i = ((BlockFalling)iblockstate.getBlock()).getDustColor(iblockstate);
                }

                float f = (float)(i >> 16 & 255) / 255.0F;
                float f1 = (float)(i >> 8 & 255) / 255.0F;
                float f2 = (float)(i & 255) / 255.0F;
                return new ParticleFallingDust(p_178902_2_, p_178902_3_, p_178902_5_, p_178902_7_, f, f1, f2);
            }
        }
    }
}

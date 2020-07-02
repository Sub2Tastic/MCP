package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class BlockFluidRenderer
{
    private final BlockColors field_187500_a;
    private final TextureAtlasSprite[] atlasSpritesLava = new TextureAtlasSprite[2];
    private final TextureAtlasSprite[] atlasSpritesWater = new TextureAtlasSprite[2];
    private TextureAtlasSprite atlasSpriteWaterOverlay;

    public BlockFluidRenderer(BlockColors p_i46576_1_)
    {
        this.field_187500_a = p_i46576_1_;
        this.initAtlasSprites();
    }

    protected void initAtlasSprites()
    {
        TextureMap texturemap = Minecraft.getInstance().func_147117_R();
        this.atlasSpritesLava[0] = texturemap.func_110572_b("minecraft:blocks/lava_still");
        this.atlasSpritesLava[1] = texturemap.func_110572_b("minecraft:blocks/lava_flow");
        this.atlasSpritesWater[0] = texturemap.func_110572_b("minecraft:blocks/water_still");
        this.atlasSpritesWater[1] = texturemap.func_110572_b("minecraft:blocks/water_flow");
        this.atlasSpriteWaterOverlay = texturemap.func_110572_b("minecraft:blocks/water_overlay");
    }

    public boolean func_178270_a(IBlockAccess p_178270_1_, IBlockState p_178270_2_, BlockPos p_178270_3_, BufferBuilder p_178270_4_)
    {
        BlockLiquid blockliquid = (BlockLiquid)p_178270_2_.getBlock();
        boolean flag = p_178270_2_.getMaterial() == Material.LAVA;
        TextureAtlasSprite[] atextureatlassprite = flag ? this.atlasSpritesLava : this.atlasSpritesWater;
        int i = this.field_187500_a.func_186724_a(p_178270_2_, p_178270_1_, p_178270_3_, 0);
        float f = (float)(i >> 16 & 255) / 255.0F;
        float f1 = (float)(i >> 8 & 255) / 255.0F;
        float f2 = (float)(i & 255) / 255.0F;
        boolean flag1 = p_178270_2_.func_185894_c(p_178270_1_, p_178270_3_, EnumFacing.UP);
        boolean flag2 = p_178270_2_.func_185894_c(p_178270_1_, p_178270_3_, EnumFacing.DOWN);
        boolean[] aboolean = new boolean[] {p_178270_2_.func_185894_c(p_178270_1_, p_178270_3_, EnumFacing.NORTH), p_178270_2_.func_185894_c(p_178270_1_, p_178270_3_, EnumFacing.SOUTH), p_178270_2_.func_185894_c(p_178270_1_, p_178270_3_, EnumFacing.WEST), p_178270_2_.func_185894_c(p_178270_1_, p_178270_3_, EnumFacing.EAST)};

        if (!flag1 && !flag2 && !aboolean[0] && !aboolean[1] && !aboolean[2] && !aboolean[3])
        {
            return false;
        }
        else
        {
            boolean flag3 = false;
            float f3 = 0.5F;
            float f4 = 1.0F;
            float f5 = 0.8F;
            float f6 = 0.6F;
            Material material = p_178270_2_.getMaterial();
            float f7 = this.func_178269_a(p_178270_1_, p_178270_3_, material);
            float f8 = this.func_178269_a(p_178270_1_, p_178270_3_.south(), material);
            float f9 = this.func_178269_a(p_178270_1_, p_178270_3_.east().south(), material);
            float f10 = this.func_178269_a(p_178270_1_, p_178270_3_.east(), material);
            double d0 = (double)p_178270_3_.getX();
            double d1 = (double)p_178270_3_.getY();
            double d2 = (double)p_178270_3_.getZ();
            float f11 = 0.001F;

            if (flag1)
            {
                flag3 = true;
                float f12 = BlockLiquid.func_189544_a(p_178270_1_, p_178270_3_, material, p_178270_2_);
                TextureAtlasSprite textureatlassprite = f12 > -999.0F ? atextureatlassprite[1] : atextureatlassprite[0];
                f7 -= 0.001F;
                f8 -= 0.001F;
                f9 -= 0.001F;
                f10 -= 0.001F;
                float f13;
                float f14;
                float f15;
                float f16;
                float f17;
                float f18;
                float f19;
                float f20;

                if (f12 < -999.0F)
                {
                    f13 = textureatlassprite.getInterpolatedU(0.0D);
                    f17 = textureatlassprite.getInterpolatedV(0.0D);
                    f14 = f13;
                    f18 = textureatlassprite.getInterpolatedV(16.0D);
                    f15 = textureatlassprite.getInterpolatedU(16.0D);
                    f19 = f18;
                    f16 = f15;
                    f20 = f17;
                }
                else
                {
                    float f21 = MathHelper.sin(f12) * 0.25F;
                    float f22 = MathHelper.cos(f12) * 0.25F;
                    float f23 = 8.0F;
                    f13 = textureatlassprite.getInterpolatedU((double)(8.0F + (-f22 - f21) * 16.0F));
                    f17 = textureatlassprite.getInterpolatedV((double)(8.0F + (-f22 + f21) * 16.0F));
                    f14 = textureatlassprite.getInterpolatedU((double)(8.0F + (-f22 + f21) * 16.0F));
                    f18 = textureatlassprite.getInterpolatedV((double)(8.0F + (f22 + f21) * 16.0F));
                    f15 = textureatlassprite.getInterpolatedU((double)(8.0F + (f22 + f21) * 16.0F));
                    f19 = textureatlassprite.getInterpolatedV((double)(8.0F + (f22 - f21) * 16.0F));
                    f16 = textureatlassprite.getInterpolatedU((double)(8.0F + (f22 - f21) * 16.0F));
                    f20 = textureatlassprite.getInterpolatedV((double)(8.0F + (-f22 - f21) * 16.0F));
                }

                int k2 = p_178270_2_.func_185889_a(p_178270_1_, p_178270_3_);
                int l2 = k2 >> 16 & 65535;
                int i3 = k2 & 65535;
                float f24 = 1.0F * f;
                float f25 = 1.0F * f1;
                float f26 = 1.0F * f2;
                p_178270_4_.func_181662_b(d0 + 0.0D, d1 + (double)f7, d2 + 0.0D).func_181666_a(f24, f25, f26, 1.0F).func_187315_a((double)f13, (double)f17).func_187314_a(l2, i3).endVertex();
                p_178270_4_.func_181662_b(d0 + 0.0D, d1 + (double)f8, d2 + 1.0D).func_181666_a(f24, f25, f26, 1.0F).func_187315_a((double)f14, (double)f18).func_187314_a(l2, i3).endVertex();
                p_178270_4_.func_181662_b(d0 + 1.0D, d1 + (double)f9, d2 + 1.0D).func_181666_a(f24, f25, f26, 1.0F).func_187315_a((double)f15, (double)f19).func_187314_a(l2, i3).endVertex();
                p_178270_4_.func_181662_b(d0 + 1.0D, d1 + (double)f10, d2 + 0.0D).func_181666_a(f24, f25, f26, 1.0F).func_187315_a((double)f16, (double)f20).func_187314_a(l2, i3).endVertex();

                if (blockliquid.func_176364_g(p_178270_1_, p_178270_3_.up()))
                {
                    p_178270_4_.func_181662_b(d0 + 0.0D, d1 + (double)f7, d2 + 0.0D).func_181666_a(f24, f25, f26, 1.0F).func_187315_a((double)f13, (double)f17).func_187314_a(l2, i3).endVertex();
                    p_178270_4_.func_181662_b(d0 + 1.0D, d1 + (double)f10, d2 + 0.0D).func_181666_a(f24, f25, f26, 1.0F).func_187315_a((double)f16, (double)f20).func_187314_a(l2, i3).endVertex();
                    p_178270_4_.func_181662_b(d0 + 1.0D, d1 + (double)f9, d2 + 1.0D).func_181666_a(f24, f25, f26, 1.0F).func_187315_a((double)f15, (double)f19).func_187314_a(l2, i3).endVertex();
                    p_178270_4_.func_181662_b(d0 + 0.0D, d1 + (double)f8, d2 + 1.0D).func_181666_a(f24, f25, f26, 1.0F).func_187315_a((double)f14, (double)f18).func_187314_a(l2, i3).endVertex();
                }
            }

            if (flag2)
            {
                float f35 = atextureatlassprite[0].getMinU();
                float f36 = atextureatlassprite[0].getMaxU();
                float f37 = atextureatlassprite[0].getMinV();
                float f38 = atextureatlassprite[0].getMaxV();
                int l1 = p_178270_2_.func_185889_a(p_178270_1_, p_178270_3_.down());
                int i2 = l1 >> 16 & 65535;
                int j2 = l1 & 65535;
                p_178270_4_.func_181662_b(d0, d1, d2 + 1.0D).func_181666_a(0.5F, 0.5F, 0.5F, 1.0F).func_187315_a((double)f35, (double)f38).func_187314_a(i2, j2).endVertex();
                p_178270_4_.func_181662_b(d0, d1, d2).func_181666_a(0.5F, 0.5F, 0.5F, 1.0F).func_187315_a((double)f35, (double)f37).func_187314_a(i2, j2).endVertex();
                p_178270_4_.func_181662_b(d0 + 1.0D, d1, d2).func_181666_a(0.5F, 0.5F, 0.5F, 1.0F).func_187315_a((double)f36, (double)f37).func_187314_a(i2, j2).endVertex();
                p_178270_4_.func_181662_b(d0 + 1.0D, d1, d2 + 1.0D).func_181666_a(0.5F, 0.5F, 0.5F, 1.0F).func_187315_a((double)f36, (double)f38).func_187314_a(i2, j2).endVertex();
                flag3 = true;
            }

            for (int i1 = 0; i1 < 4; ++i1)
            {
                int j1 = 0;
                int k1 = 0;

                if (i1 == 0)
                {
                    --k1;
                }

                if (i1 == 1)
                {
                    ++k1;
                }

                if (i1 == 2)
                {
                    --j1;
                }

                if (i1 == 3)
                {
                    ++j1;
                }

                BlockPos blockpos = p_178270_3_.add(j1, 0, k1);
                TextureAtlasSprite textureatlassprite1 = atextureatlassprite[1];

                if (!flag)
                {
                    Block block = p_178270_1_.getBlockState(blockpos).getBlock();

                    if (block == Blocks.GLASS || block == Blocks.field_150399_cn)
                    {
                        textureatlassprite1 = this.atlasSpriteWaterOverlay;
                    }
                }

                if (aboolean[i1])
                {
                    float f39;
                    float f40;
                    double d3;
                    double d4;
                    double d5;
                    double d6;

                    if (i1 == 0)
                    {
                        f39 = f7;
                        f40 = f10;
                        d3 = d0;
                        d5 = d0 + 1.0D;
                        d4 = d2 + 0.0010000000474974513D;
                        d6 = d2 + 0.0010000000474974513D;
                    }
                    else if (i1 == 1)
                    {
                        f39 = f9;
                        f40 = f8;
                        d3 = d0 + 1.0D;
                        d5 = d0;
                        d4 = d2 + 1.0D - 0.0010000000474974513D;
                        d6 = d2 + 1.0D - 0.0010000000474974513D;
                    }
                    else if (i1 == 2)
                    {
                        f39 = f8;
                        f40 = f7;
                        d3 = d0 + 0.0010000000474974513D;
                        d5 = d0 + 0.0010000000474974513D;
                        d4 = d2 + 1.0D;
                        d6 = d2;
                    }
                    else
                    {
                        f39 = f10;
                        f40 = f9;
                        d3 = d0 + 1.0D - 0.0010000000474974513D;
                        d5 = d0 + 1.0D - 0.0010000000474974513D;
                        d4 = d2;
                        d6 = d2 + 1.0D;
                    }

                    flag3 = true;
                    float f41 = textureatlassprite1.getInterpolatedU(0.0D);
                    float f27 = textureatlassprite1.getInterpolatedU(8.0D);
                    float f28 = textureatlassprite1.getInterpolatedV((double)((1.0F - f39) * 16.0F * 0.5F));
                    float f29 = textureatlassprite1.getInterpolatedV((double)((1.0F - f40) * 16.0F * 0.5F));
                    float f30 = textureatlassprite1.getInterpolatedV(8.0D);
                    int j = p_178270_2_.func_185889_a(p_178270_1_, blockpos);
                    int k = j >> 16 & 65535;
                    int l = j & 65535;
                    float f31 = i1 < 2 ? 0.8F : 0.6F;
                    float f32 = 1.0F * f31 * f;
                    float f33 = 1.0F * f31 * f1;
                    float f34 = 1.0F * f31 * f2;
                    p_178270_4_.func_181662_b(d3, d1 + (double)f39, d4).func_181666_a(f32, f33, f34, 1.0F).func_187315_a((double)f41, (double)f28).func_187314_a(k, l).endVertex();
                    p_178270_4_.func_181662_b(d5, d1 + (double)f40, d6).func_181666_a(f32, f33, f34, 1.0F).func_187315_a((double)f27, (double)f29).func_187314_a(k, l).endVertex();
                    p_178270_4_.func_181662_b(d5, d1 + 0.0D, d6).func_181666_a(f32, f33, f34, 1.0F).func_187315_a((double)f27, (double)f30).func_187314_a(k, l).endVertex();
                    p_178270_4_.func_181662_b(d3, d1 + 0.0D, d4).func_181666_a(f32, f33, f34, 1.0F).func_187315_a((double)f41, (double)f30).func_187314_a(k, l).endVertex();

                    if (textureatlassprite1 != this.atlasSpriteWaterOverlay)
                    {
                        p_178270_4_.func_181662_b(d3, d1 + 0.0D, d4).func_181666_a(f32, f33, f34, 1.0F).func_187315_a((double)f41, (double)f30).func_187314_a(k, l).endVertex();
                        p_178270_4_.func_181662_b(d5, d1 + 0.0D, d6).func_181666_a(f32, f33, f34, 1.0F).func_187315_a((double)f27, (double)f30).func_187314_a(k, l).endVertex();
                        p_178270_4_.func_181662_b(d5, d1 + (double)f40, d6).func_181666_a(f32, f33, f34, 1.0F).func_187315_a((double)f27, (double)f29).func_187314_a(k, l).endVertex();
                        p_178270_4_.func_181662_b(d3, d1 + (double)f39, d4).func_181666_a(f32, f33, f34, 1.0F).func_187315_a((double)f41, (double)f28).func_187314_a(k, l).endVertex();
                    }
                }
            }

            return flag3;
        }
    }

    private float func_178269_a(IBlockAccess p_178269_1_, BlockPos p_178269_2_, Material p_178269_3_)
    {
        int i = 0;
        float f = 0.0F;

        for (int j = 0; j < 4; ++j)
        {
            BlockPos blockpos = p_178269_2_.add(-(j & 1), 0, -(j >> 1 & 1));

            if (p_178269_1_.getBlockState(blockpos.up()).getMaterial() == p_178269_3_)
            {
                return 1.0F;
            }

            IBlockState iblockstate = p_178269_1_.getBlockState(blockpos);
            Material material = iblockstate.getMaterial();

            if (material != p_178269_3_)
            {
                if (!material.isSolid())
                {
                    ++f;
                    ++i;
                }
            }
            else
            {
                int k = ((Integer)iblockstate.get(BlockLiquid.LEVEL)).intValue();

                if (k >= 8 || k == 0)
                {
                    f += BlockLiquid.func_149801_b(k) * 10.0F;
                    i += 10;
                }

                f += BlockLiquid.func_149801_b(k);
                ++i;
            }
        }

        return 1.0F - f / (float)i;
    }
}

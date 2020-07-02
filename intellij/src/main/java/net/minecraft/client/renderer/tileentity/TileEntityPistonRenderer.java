package net.minecraft.client.renderer.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPistonRenderer extends TileEntitySpecialRenderer<TileEntityPiston>
{
    private final BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();

    public void func_192841_a(TileEntityPiston p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_)
    {
        BlockPos blockpos = p_192841_1_.getPos();
        IBlockState iblockstate = p_192841_1_.func_174927_b();
        Block block = iblockstate.getBlock();

        if (iblockstate.getMaterial() != Material.AIR && p_192841_1_.getProgress(p_192841_8_) < 1.0F)
        {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            this.func_147499_a(TextureMap.LOCATION_BLOCKS_TEXTURE);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.func_179147_l();
            GlStateManager.func_179129_p();

            if (Minecraft.isAmbientOcclusionEnabled())
            {
                GlStateManager.func_179103_j(7425);
            }
            else
            {
                GlStateManager.func_179103_j(7424);
            }

            bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
            bufferbuilder.func_178969_c(p_192841_2_ - (double)blockpos.getX() + (double)p_192841_1_.getOffsetX(p_192841_8_), p_192841_4_ - (double)blockpos.getY() + (double)p_192841_1_.getOffsetY(p_192841_8_), p_192841_6_ - (double)blockpos.getZ() + (double)p_192841_1_.getOffsetZ(p_192841_8_));
            World world = this.func_178459_a();

            if (block == Blocks.PISTON_HEAD && p_192841_1_.getProgress(p_192841_8_) <= 0.25F)
            {
                iblockstate = iblockstate.func_177226_a(BlockPistonExtension.SHORT, Boolean.valueOf(true));
                this.func_188186_a(blockpos, iblockstate, bufferbuilder, world, true);
            }
            else if (p_192841_1_.shouldPistonHeadBeRendered() && !p_192841_1_.isExtending())
            {
                BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = block == Blocks.STICKY_PISTON ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                IBlockState iblockstate1 = Blocks.PISTON_HEAD.getDefaultState().func_177226_a(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype).func_177226_a(BlockPistonExtension.FACING, iblockstate.get(BlockPistonBase.FACING));
                iblockstate1 = iblockstate1.func_177226_a(BlockPistonExtension.SHORT, Boolean.valueOf(p_192841_1_.getProgress(p_192841_8_) >= 0.5F));
                this.func_188186_a(blockpos, iblockstate1, bufferbuilder, world, true);
                bufferbuilder.func_178969_c(p_192841_2_ - (double)blockpos.getX(), p_192841_4_ - (double)blockpos.getY(), p_192841_6_ - (double)blockpos.getZ());
                iblockstate = iblockstate.func_177226_a(BlockPistonBase.EXTENDED, Boolean.valueOf(true));
                this.func_188186_a(blockpos, iblockstate, bufferbuilder, world, true);
            }
            else
            {
                this.func_188186_a(blockpos, iblockstate, bufferbuilder, world, false);
            }

            bufferbuilder.func_178969_c(0.0D, 0.0D, 0.0D);
            tessellator.draw();
            RenderHelper.func_74519_b();
        }
    }

    private boolean func_188186_a(BlockPos p_188186_1_, IBlockState p_188186_2_, BufferBuilder p_188186_3_, World p_188186_4_, boolean p_188186_5_)
    {
        return this.blockRenderer.getBlockModelRenderer().func_178267_a(p_188186_4_, this.blockRenderer.getModelForState(p_188186_2_), p_188186_2_, p_188186_1_, p_188186_3_, p_188186_5_);
    }
}

package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RenderFallingBlock extends Render<EntityFallingBlock>
{
    public RenderFallingBlock(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    public void func_76986_a(EntityFallingBlock p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        if (p_76986_1_.func_175131_l() != null)
        {
            IBlockState iblockstate = p_76986_1_.func_175131_l();

            if (iblockstate.getRenderType() == EnumBlockRenderType.MODEL)
            {
                World world = p_76986_1_.getWorldObj();

                if (iblockstate != world.getBlockState(new BlockPos(p_76986_1_)) && iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE)
                {
                    this.func_110776_a(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179140_f();
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder bufferbuilder = tessellator.getBuffer();

                    if (this.field_188301_f)
                    {
                        GlStateManager.func_179142_g();
                        GlStateManager.func_187431_e(this.func_188298_c(p_76986_1_));
                    }

                    bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
                    BlockPos blockpos = new BlockPos(p_76986_1_.posX, p_76986_1_.getBoundingBox().maxY, p_76986_1_.posZ);
                    GlStateManager.func_179109_b((float)(p_76986_2_ - (double)blockpos.getX() - 0.5D), (float)(p_76986_4_ - (double)blockpos.getY()), (float)(p_76986_6_ - (double)blockpos.getZ() - 0.5D));
                    BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
                    blockrendererdispatcher.getBlockModelRenderer().func_187493_a(world, blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos, bufferbuilder, false, MathHelper.getPositionRandom(p_76986_1_.getOrigin()));
                    tessellator.draw();

                    if (this.field_188301_f)
                    {
                        GlStateManager.func_187417_n();
                        GlStateManager.func_179119_h();
                    }

                    GlStateManager.func_179145_e();
                    GlStateManager.func_179121_F();
                    super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
                }
            }
        }
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityFallingBlock entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}

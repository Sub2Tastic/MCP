package net.minecraft.client.renderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;

public class BlockRendererDispatcher implements IResourceManagerReloadListener
{
    private final BlockModelShapes blockModelShapes;
    private final BlockModelRenderer blockModelRenderer;
    private final ChestRenderer field_175024_d = new ChestRenderer();
    private final BlockFluidRenderer fluidRenderer;

    public BlockRendererDispatcher(BlockModelShapes shapes, BlockColors colors)
    {
        this.blockModelShapes = shapes;
        this.blockModelRenderer = new BlockModelRenderer(colors);
        this.fluidRenderer = new BlockFluidRenderer(colors);
    }

    public BlockModelShapes getBlockModelShapes()
    {
        return this.blockModelShapes;
    }

    public void func_175020_a(IBlockState p_175020_1_, BlockPos p_175020_2_, TextureAtlasSprite p_175020_3_, IBlockAccess p_175020_4_)
    {
        if (p_175020_1_.getRenderType() == EnumBlockRenderType.MODEL)
        {
            p_175020_1_ = p_175020_1_.func_185899_b(p_175020_4_, p_175020_2_);
            IBakedModel ibakedmodel = this.blockModelShapes.getModel(p_175020_1_);
            IBakedModel ibakedmodel1 = (new SimpleBakedModel.Builder(p_175020_1_, ibakedmodel, p_175020_3_, p_175020_2_)).build();
            this.blockModelRenderer.func_178267_a(p_175020_4_, ibakedmodel1, p_175020_1_, p_175020_2_, Tessellator.getInstance().getBuffer(), true);
        }
    }

    public boolean func_175018_a(IBlockState p_175018_1_, BlockPos p_175018_2_, IBlockAccess p_175018_3_, BufferBuilder p_175018_4_)
    {
        try
        {
            EnumBlockRenderType enumblockrendertype = p_175018_1_.getRenderType();

            if (enumblockrendertype == EnumBlockRenderType.INVISIBLE)
            {
                return false;
            }
            else
            {
                if (p_175018_3_.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES)
                {
                    try
                    {
                        p_175018_1_ = p_175018_1_.func_185899_b(p_175018_3_, p_175018_2_);
                    }
                    catch (Exception var8)
                    {
                        ;
                    }
                }

                switch (enumblockrendertype)
                {
                    case MODEL:
                        return this.blockModelRenderer.func_178267_a(p_175018_3_, this.getModelForState(p_175018_1_), p_175018_1_, p_175018_2_, p_175018_4_, true);

                    case ENTITYBLOCK_ANIMATED:
                        return false;

                    case LIQUID:
                        return this.fluidRenderer.func_178270_a(p_175018_3_, p_175018_1_, p_175018_2_, p_175018_4_);

                    default:
                        return false;
                }
            }
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being tesselated");
            CrashReportCategory.func_180523_a(crashreportcategory, p_175018_2_, p_175018_1_.getBlock(), p_175018_1_.getBlock().func_176201_c(p_175018_1_));
            throw new ReportedException(crashreport);
        }
    }

    public BlockModelRenderer getBlockModelRenderer()
    {
        return this.blockModelRenderer;
    }

    public IBakedModel getModelForState(IBlockState state)
    {
        return this.blockModelShapes.getModel(state);
    }

    @SuppressWarnings("incomplete-switch")
    public void func_175016_a(IBlockState p_175016_1_, float p_175016_2_)
    {
        EnumBlockRenderType enumblockrendertype = p_175016_1_.getRenderType();

        if (enumblockrendertype != EnumBlockRenderType.INVISIBLE)
        {
            switch (enumblockrendertype)
            {
                case MODEL:
                    IBakedModel ibakedmodel = this.getModelForState(p_175016_1_);
                    this.blockModelRenderer.func_178266_a(ibakedmodel, p_175016_1_, p_175016_2_, true);
                    break;

                case ENTITYBLOCK_ANIMATED:
                    this.field_175024_d.func_178175_a(p_175016_1_.getBlock(), p_175016_2_);

                case LIQUID:
            }
        }
    }

    public void func_110549_a(IResourceManager p_110549_1_)
    {
        this.fluidRenderer.initAtlasSprites();
    }
}

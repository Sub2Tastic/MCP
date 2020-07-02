package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Sets;
import java.nio.FloatBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RenderChunk
{
    private World field_178588_d;
    private final RenderGlobal field_178589_e;
    public static int field_178592_a;
    public CompiledChunk compiledChunk = CompiledChunk.DUMMY;
    private final ReentrantLock field_178587_g = new ReentrantLock();
    private final ReentrantLock field_178598_h = new ReentrantLock();
    private ChunkCompileTaskGenerator field_178599_i;
    private final Set<TileEntity> globalTileEntities = Sets.<TileEntity>newHashSet();
    private final int field_178596_j;
    private final FloatBuffer field_178597_k = GLAllocation.createDirectFloatBuffer(16);
    private final VertexBuffer[] vertexBuffers = new VertexBuffer[BlockRenderLayer.values().length];
    public AxisAlignedBB boundingBox;
    private int frameIndex = -1;
    private boolean needsUpdate = true;
    private final BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos(-1, -1, -1);
    private final BlockPos.MutableBlockPos[] mapEnumFacing = new BlockPos.MutableBlockPos[6];
    private boolean needsImmediateUpdate;
    private ChunkCache field_189564_r;

    public RenderChunk(World p_i47120_1_, RenderGlobal p_i47120_2_, int p_i47120_3_)
    {
        for (int i = 0; i < this.mapEnumFacing.length; ++i)
        {
            this.mapEnumFacing[i] = new BlockPos.MutableBlockPos();
        }

        this.field_178588_d = p_i47120_1_;
        this.field_178589_e = p_i47120_2_;
        this.field_178596_j = p_i47120_3_;

        if (OpenGlHelper.func_176075_f())
        {
            for (int j = 0; j < BlockRenderLayer.values().length; ++j)
            {
                this.vertexBuffers[j] = new VertexBuffer(DefaultVertexFormats.BLOCK);
            }
        }
    }

    public boolean setFrameIndex(int frameIndexIn)
    {
        if (this.frameIndex == frameIndexIn)
        {
            return false;
        }
        else
        {
            this.frameIndex = frameIndexIn;
            return true;
        }
    }

    public VertexBuffer func_178565_b(int p_178565_1_)
    {
        return this.vertexBuffers[p_178565_1_];
    }

    /**
     * Sets the RenderChunk base position
     */
    public void setPosition(int x, int y, int z)
    {
        if (x != this.position.getX() || y != this.position.getY() || z != this.position.getZ())
        {
            this.stopCompileTask();
            this.position.setPos(x, y, z);
            this.boundingBox = new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 16), (double)(y + 16), (double)(z + 16));

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                this.mapEnumFacing[enumfacing.ordinal()].setPos(this.position).move(enumfacing, 16);
            }

            this.func_178567_n();
        }
    }

    public void func_178570_a(float p_178570_1_, float p_178570_2_, float p_178570_3_, ChunkCompileTaskGenerator p_178570_4_)
    {
        CompiledChunk compiledchunk = p_178570_4_.func_178544_c();

        if (compiledchunk.func_178487_c() != null && !compiledchunk.func_178491_b(BlockRenderLayer.TRANSLUCENT))
        {
            this.func_178573_a(p_178570_4_.func_178545_d().func_179038_a(BlockRenderLayer.TRANSLUCENT), this.position);
            p_178570_4_.func_178545_d().func_179038_a(BlockRenderLayer.TRANSLUCENT).setVertexState(compiledchunk.func_178487_c());
            this.func_178584_a(BlockRenderLayer.TRANSLUCENT, p_178570_1_, p_178570_2_, p_178570_3_, p_178570_4_.func_178545_d().func_179038_a(BlockRenderLayer.TRANSLUCENT), compiledchunk);
        }
    }

    public void func_178581_b(float p_178581_1_, float p_178581_2_, float p_178581_3_, ChunkCompileTaskGenerator p_178581_4_)
    {
        CompiledChunk compiledchunk = new CompiledChunk();
        int i = 1;
        BlockPos blockpos = this.position;
        BlockPos blockpos1 = blockpos.add(15, 15, 15);
        p_178581_4_.func_178540_f().lock();

        try
        {
            if (p_178581_4_.func_178546_a() != ChunkCompileTaskGenerator.Status.COMPILING)
            {
                return;
            }

            p_178581_4_.func_178543_a(compiledchunk);
        }
        finally
        {
            p_178581_4_.func_178540_f().unlock();
        }

        VisGraph lvt_9_1_ = new VisGraph();
        HashSet lvt_10_1_ = Sets.newHashSet();

        if (!this.field_189564_r.func_72806_N())
        {
            ++field_178592_a;
            boolean[] aboolean = new boolean[BlockRenderLayer.values().length];
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

            for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.func_177975_b(blockpos, blockpos1))
            {
                IBlockState iblockstate = this.field_189564_r.getBlockState(blockpos$mutableblockpos);
                Block block = iblockstate.getBlock();

                if (iblockstate.func_185914_p())
                {
                    lvt_9_1_.setOpaqueCube(blockpos$mutableblockpos);
                }

                if (block.hasTileEntity())
                {
                    TileEntity tileentity = this.field_189564_r.func_190300_a(blockpos$mutableblockpos, Chunk.EnumCreateEntityType.CHECK);

                    if (tileentity != null)
                    {
                        TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = TileEntityRendererDispatcher.instance.<TileEntity>getRenderer(tileentity);

                        if (tileentityspecialrenderer != null)
                        {
                            compiledchunk.func_178490_a(tileentity);

                            if (tileentityspecialrenderer.isGlobalRenderer(tileentity))
                            {
                                lvt_10_1_.add(tileentity);
                            }
                        }
                    }
                }

                BlockRenderLayer blockrenderlayer1 = block.func_180664_k();
                int j = blockrenderlayer1.ordinal();

                if (block.getDefaultState().getRenderType() != EnumBlockRenderType.INVISIBLE)
                {
                    BufferBuilder bufferbuilder = p_178581_4_.func_178545_d().func_179039_a(j);

                    if (!compiledchunk.func_178492_d(blockrenderlayer1))
                    {
                        compiledchunk.func_178493_c(blockrenderlayer1);
                        this.func_178573_a(bufferbuilder, blockpos);
                    }

                    aboolean[j] |= blockrendererdispatcher.func_175018_a(iblockstate, blockpos$mutableblockpos, this.field_189564_r, bufferbuilder);
                }
            }

            for (BlockRenderLayer blockrenderlayer : BlockRenderLayer.values())
            {
                if (aboolean[blockrenderlayer.ordinal()])
                {
                    compiledchunk.func_178486_a(blockrenderlayer);
                }

                if (compiledchunk.func_178492_d(blockrenderlayer))
                {
                    this.func_178584_a(blockrenderlayer, p_178581_1_, p_178581_2_, p_178581_3_, p_178581_4_.func_178545_d().func_179038_a(blockrenderlayer), compiledchunk);
                }
            }
        }

        compiledchunk.func_178488_a(lvt_9_1_.computeVisibility());
        this.field_178587_g.lock();

        try
        {
            Set<TileEntity> set = Sets.newHashSet(lvt_10_1_);
            Set<TileEntity> set1 = Sets.newHashSet(this.globalTileEntities);
            set.removeAll(this.globalTileEntities);
            set1.removeAll(lvt_10_1_);
            this.globalTileEntities.clear();
            this.globalTileEntities.addAll(lvt_10_1_);
            this.field_178589_e.updateTileEntities(set1, set);
        }
        finally
        {
            this.field_178587_g.unlock();
        }
    }

    protected void func_178578_b()
    {
        this.field_178587_g.lock();

        try
        {
            if (this.field_178599_i != null && this.field_178599_i.func_178546_a() != ChunkCompileTaskGenerator.Status.DONE)
            {
                this.field_178599_i.func_178542_e();
                this.field_178599_i = null;
            }
        }
        finally
        {
            this.field_178587_g.unlock();
        }
    }

    public ReentrantLock func_178579_c()
    {
        return this.field_178587_g;
    }

    public ChunkCompileTaskGenerator makeCompileTaskChunk()
    {
        this.field_178587_g.lock();
        ChunkCompileTaskGenerator chunkcompiletaskgenerator;

        try
        {
            this.func_178578_b();
            this.field_178599_i = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK, this.getDistanceSq());
            this.func_189563_q();
            chunkcompiletaskgenerator = this.field_178599_i;
        }
        finally
        {
            this.field_178587_g.unlock();
        }

        return chunkcompiletaskgenerator;
    }

    private void func_189563_q()
    {
        int i = 1;
        this.field_189564_r = new ChunkCache(this.field_178588_d, this.position.add(-1, -1, -1), this.position.add(16, 16, 16), 1);
    }

    @Nullable
    public ChunkCompileTaskGenerator func_178582_e()
    {
        this.field_178587_g.lock();
        ChunkCompileTaskGenerator chunkcompiletaskgenerator;

        try
        {
            if (this.field_178599_i == null || this.field_178599_i.func_178546_a() != ChunkCompileTaskGenerator.Status.PENDING)
            {
                if (this.field_178599_i != null && this.field_178599_i.func_178546_a() != ChunkCompileTaskGenerator.Status.DONE)
                {
                    this.field_178599_i.func_178542_e();
                    this.field_178599_i = null;
                }

                this.field_178599_i = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY, this.getDistanceSq());
                this.field_178599_i.func_178543_a(this.compiledChunk);
                chunkcompiletaskgenerator = this.field_178599_i;
                return chunkcompiletaskgenerator;
            }

            chunkcompiletaskgenerator = null;
        }
        finally
        {
            this.field_178587_g.unlock();
        }

        return chunkcompiletaskgenerator;
    }

    protected double getDistanceSq()
    {
        EntityPlayerSP entityplayersp = Minecraft.getInstance().player;
        double d0 = this.boundingBox.minX + 8.0D - entityplayersp.posX;
        double d1 = this.boundingBox.minY + 8.0D - entityplayersp.posY;
        double d2 = this.boundingBox.minZ + 8.0D - entityplayersp.posZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    private void func_178573_a(BufferBuilder p_178573_1_, BlockPos p_178573_2_)
    {
        p_178573_1_.begin(7, DefaultVertexFormats.BLOCK);
        p_178573_1_.func_178969_c((double)(-p_178573_2_.getX()), (double)(-p_178573_2_.getY()), (double)(-p_178573_2_.getZ()));
    }

    private void func_178584_a(BlockRenderLayer p_178584_1_, float p_178584_2_, float p_178584_3_, float p_178584_4_, BufferBuilder p_178584_5_, CompiledChunk p_178584_6_)
    {
        if (p_178584_1_ == BlockRenderLayer.TRANSLUCENT && !p_178584_6_.func_178491_b(p_178584_1_))
        {
            p_178584_5_.sortVertexData(p_178584_2_, p_178584_3_, p_178584_4_);
            p_178584_6_.func_178494_a(p_178584_5_.getVertexState());
        }

        p_178584_5_.finishDrawing();
    }

    private void func_178567_n()
    {
        GlStateManager.func_179094_E();
        GlStateManager.func_179096_D();
        float f = 1.000001F;
        GlStateManager.func_179109_b(-8.0F, -8.0F, -8.0F);
        GlStateManager.func_179152_a(1.000001F, 1.000001F, 1.000001F);
        GlStateManager.func_179109_b(8.0F, 8.0F, 8.0F);
        GlStateManager.func_179111_a(2982, this.field_178597_k);
        GlStateManager.func_179121_F();
    }

    public void func_178572_f()
    {
        GlStateManager.func_179110_a(this.field_178597_k);
    }

    public CompiledChunk getCompiledChunk()
    {
        return this.compiledChunk;
    }

    public void func_178580_a(CompiledChunk p_178580_1_)
    {
        this.field_178598_h.lock();

        try
        {
            this.compiledChunk = p_178580_1_;
        }
        finally
        {
            this.field_178598_h.unlock();
        }
    }

    public void stopCompileTask()
    {
        this.func_178578_b();
        this.compiledChunk = CompiledChunk.DUMMY;
    }

    public void deleteGlResources()
    {
        this.stopCompileTask();
        this.field_178588_d = null;

        for (int i = 0; i < BlockRenderLayer.values().length; ++i)
        {
            if (this.vertexBuffers[i] != null)
            {
                this.vertexBuffers[i].func_177362_c();
            }
        }
    }

    public BlockPos getPosition()
    {
        return this.position;
    }

    public void setNeedsUpdate(boolean immediate)
    {
        if (this.needsUpdate)
        {
            immediate |= this.needsImmediateUpdate;
        }

        this.needsUpdate = true;
        this.needsImmediateUpdate = immediate;
    }

    public void clearNeedsUpdate()
    {
        this.needsUpdate = false;
        this.needsImmediateUpdate = false;
    }

    public boolean needsUpdate()
    {
        return this.needsUpdate;
    }

    public boolean needsImmediateUpdate()
    {
        return this.needsUpdate && this.needsImmediateUpdate;
    }

    public BlockPos getBlockPosOffset16(EnumFacing facing)
    {
        return this.mapEnumFacing[facing.ordinal()];
    }

    public World func_188283_p()
    {
        return this.field_178588_d;
    }
}

package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;

public class CompiledChunk
{
    public static final CompiledChunk DUMMY = new CompiledChunk()
    {
        protected void func_178486_a(BlockRenderLayer p_178486_1_)
        {
            throw new UnsupportedOperationException();
        }
        public void func_178493_c(BlockRenderLayer p_178493_1_)
        {
            throw new UnsupportedOperationException();
        }
        public boolean isVisible(EnumFacing facing, EnumFacing facing2)
        {
            return false;
        }
    };
    private final boolean[] layersUsed = new boolean[BlockRenderLayer.values().length];
    private final boolean[] layersStarted = new boolean[BlockRenderLayer.values().length];
    private boolean empty = true;
    private final List<TileEntity> tileEntities = Lists.<TileEntity>newArrayList();
    private SetVisibility setVisibility = new SetVisibility();
    private BufferBuilder.State state;

    public boolean isEmpty()
    {
        return this.empty;
    }

    protected void func_178486_a(BlockRenderLayer p_178486_1_)
    {
        this.empty = false;
        this.layersUsed[p_178486_1_.ordinal()] = true;
    }

    public boolean func_178491_b(BlockRenderLayer p_178491_1_)
    {
        return !this.layersUsed[p_178491_1_.ordinal()];
    }

    public void func_178493_c(BlockRenderLayer p_178493_1_)
    {
        this.layersStarted[p_178493_1_.ordinal()] = true;
    }

    public boolean func_178492_d(BlockRenderLayer p_178492_1_)
    {
        return this.layersStarted[p_178492_1_.ordinal()];
    }

    public List<TileEntity> getTileEntities()
    {
        return this.tileEntities;
    }

    public void func_178490_a(TileEntity p_178490_1_)
    {
        this.tileEntities.add(p_178490_1_);
    }

    public boolean isVisible(EnumFacing facing, EnumFacing facing2)
    {
        return this.setVisibility.isVisible(facing, facing2);
    }

    public void func_178488_a(SetVisibility p_178488_1_)
    {
        this.setVisibility = p_178488_1_;
    }

    public BufferBuilder.State func_178487_c()
    {
        return this.state;
    }

    public void func_178494_a(BufferBuilder.State p_178494_1_)
    {
        this.state = p_178494_1_;
    }
}

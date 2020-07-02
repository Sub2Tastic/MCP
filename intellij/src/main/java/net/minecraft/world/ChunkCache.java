package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class ChunkCache implements IBlockAccess
{
    protected int chunkX;
    protected int chunkZ;
    protected Chunk[][] chunks;
    protected boolean empty;
    protected World world;

    public ChunkCache(World p_i45746_1_, BlockPos p_i45746_2_, BlockPos p_i45746_3_, int p_i45746_4_)
    {
        this.world = p_i45746_1_;
        this.chunkX = p_i45746_2_.getX() - p_i45746_4_ >> 4;
        this.chunkZ = p_i45746_2_.getZ() - p_i45746_4_ >> 4;
        int i = p_i45746_3_.getX() + p_i45746_4_ >> 4;
        int j = p_i45746_3_.getZ() + p_i45746_4_ >> 4;
        this.chunks = new Chunk[i - this.chunkX + 1][j - this.chunkZ + 1];
        this.empty = true;

        for (int k = this.chunkX; k <= i; ++k)
        {
            for (int l = this.chunkZ; l <= j; ++l)
            {
                this.chunks[k - this.chunkX][l - this.chunkZ] = p_i45746_1_.func_72964_e(k, l);
            }
        }

        for (int i1 = p_i45746_2_.getX() >> 4; i1 <= p_i45746_3_.getX() >> 4; ++i1)
        {
            for (int j1 = p_i45746_2_.getZ() >> 4; j1 <= p_i45746_3_.getZ() >> 4; ++j1)
            {
                Chunk chunk = this.chunks[i1 - this.chunkX][j1 - this.chunkZ];

                if (chunk != null && !chunk.isEmptyBetween(p_i45746_2_.getY(), p_i45746_3_.getY()))
                {
                    this.empty = false;
                }
            }
        }
    }

    public boolean func_72806_N()
    {
        return this.empty;
    }

    @Nullable
    public TileEntity getTileEntity(BlockPos pos)
    {
        return this.func_190300_a(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
    }

    @Nullable
    public TileEntity func_190300_a(BlockPos p_190300_1_, Chunk.EnumCreateEntityType p_190300_2_)
    {
        int i = (p_190300_1_.getX() >> 4) - this.chunkX;
        int j = (p_190300_1_.getZ() >> 4) - this.chunkZ;
        return this.chunks[i][j].getTileEntity(p_190300_1_, p_190300_2_);
    }

    public int func_175626_b(BlockPos p_175626_1_, int p_175626_2_)
    {
        int i = this.func_175629_a(EnumSkyBlock.SKY, p_175626_1_);
        int j = this.func_175629_a(EnumSkyBlock.BLOCK, p_175626_1_);

        if (j < p_175626_2_)
        {
            j = p_175626_2_;
        }

        return i << 20 | j << 4;
    }

    public IBlockState getBlockState(BlockPos pos)
    {
        if (pos.getY() >= 0 && pos.getY() < 256)
        {
            int i = (pos.getX() >> 4) - this.chunkX;
            int j = (pos.getZ() >> 4) - this.chunkZ;

            if (i >= 0 && i < this.chunks.length && j >= 0 && j < this.chunks[i].length)
            {
                Chunk chunk = this.chunks[i][j];

                if (chunk != null)
                {
                    return chunk.func_177435_g(pos);
                }
            }
        }

        return Blocks.AIR.getDefaultState();
    }

    public Biome func_180494_b(BlockPos p_180494_1_)
    {
        int i = (p_180494_1_.getX() >> 4) - this.chunkX;
        int j = (p_180494_1_.getZ() >> 4) - this.chunkZ;
        return this.chunks[i][j].func_177411_a(p_180494_1_, this.world.func_72959_q());
    }

    private int func_175629_a(EnumSkyBlock p_175629_1_, BlockPos p_175629_2_)
    {
        if (p_175629_1_ == EnumSkyBlock.SKY && !this.world.dimension.hasSkyLight())
        {
            return 0;
        }
        else if (p_175629_2_.getY() >= 0 && p_175629_2_.getY() < 256)
        {
            if (this.getBlockState(p_175629_2_).func_185916_f())
            {
                int l = 0;

                for (EnumFacing enumfacing : EnumFacing.values())
                {
                    int k = this.func_175628_b(p_175629_1_, p_175629_2_.offset(enumfacing));

                    if (k > l)
                    {
                        l = k;
                    }

                    if (l >= 15)
                    {
                        return l;
                    }
                }

                return l;
            }
            else
            {
                int i = (p_175629_2_.getX() >> 4) - this.chunkX;
                int j = (p_175629_2_.getZ() >> 4) - this.chunkZ;
                return this.chunks[i][j].func_177413_a(p_175629_1_, p_175629_2_);
            }
        }
        else
        {
            return p_175629_1_.defaultLightValue;
        }
    }

    /**
     * Checks to see if an air block exists at the provided location. Note that this only checks to see if the blocks
     * material is set to air, meaning it is possible for non-vanilla blocks to still pass this check.
     */
    public boolean isAirBlock(BlockPos pos)
    {
        return this.getBlockState(pos).getMaterial() == Material.AIR;
    }

    public int func_175628_b(EnumSkyBlock p_175628_1_, BlockPos p_175628_2_)
    {
        if (p_175628_2_.getY() >= 0 && p_175628_2_.getY() < 256)
        {
            int i = (p_175628_2_.getX() >> 4) - this.chunkX;
            int j = (p_175628_2_.getZ() >> 4) - this.chunkZ;
            return this.chunks[i][j].func_177413_a(p_175628_1_, p_175628_2_);
        }
        else
        {
            return p_175628_1_.defaultLightValue;
        }
    }

    public int getStrongPower(BlockPos pos, EnumFacing direction)
    {
        return this.getBlockState(pos).getStrongPower(this, pos, direction);
    }

    public WorldType getWorldType()
    {
        return this.world.getWorldType();
    }
}

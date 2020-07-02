package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;

public class ChunkGeneratorDebug implements IChunkGenerator
{
    private static final List<IBlockState> ALL_VALID_STATES = Lists.<IBlockState>newArrayList();
    private static final int GRID_WIDTH;
    private static final int GRID_HEIGHT;
    protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    protected static final IBlockState BARRIER = Blocks.BARRIER.getDefaultState();
    private final World field_177463_c;

    public ChunkGeneratorDebug(World p_i45638_1_)
    {
        this.field_177463_c = p_i45638_1_;
    }

    public Chunk func_185932_a(int p_185932_1_, int p_185932_2_)
    {
        ChunkPrimer chunkprimer = new ChunkPrimer();

        for (int i = 0; i < 16; ++i)
        {
            for (int j = 0; j < 16; ++j)
            {
                int k = p_185932_1_ * 16 + i;
                int l = p_185932_2_ * 16 + j;
                chunkprimer.func_177855_a(i, 60, j, BARRIER);
                IBlockState iblockstate = getBlockStateFor(k, l);

                if (iblockstate != null)
                {
                    chunkprimer.func_177855_a(i, 70, j, iblockstate);
                }
            }
        }

        Chunk chunk = new Chunk(this.field_177463_c, chunkprimer, p_185932_1_, p_185932_2_);
        chunk.func_76603_b();
        Biome[] abiome = this.field_177463_c.func_72959_q().func_76933_b((Biome[])null, p_185932_1_ * 16, p_185932_2_ * 16, 16, 16);
        byte[] abyte = chunk.func_76605_m();

        for (int i1 = 0; i1 < abyte.length; ++i1)
        {
            abyte[i1] = (byte)Biome.func_185362_a(abiome[i1]);
        }

        chunk.func_76603_b();
        return chunk;
    }

    public static IBlockState getBlockStateFor(int p_177461_0_, int p_177461_1_)
    {
        IBlockState iblockstate = AIR;

        if (p_177461_0_ > 0 && p_177461_1_ > 0 && p_177461_0_ % 2 != 0 && p_177461_1_ % 2 != 0)
        {
            p_177461_0_ = p_177461_0_ / 2;
            p_177461_1_ = p_177461_1_ / 2;

            if (p_177461_0_ <= GRID_WIDTH && p_177461_1_ <= GRID_HEIGHT)
            {
                int i = MathHelper.abs(p_177461_0_ * GRID_WIDTH + p_177461_1_);

                if (i < ALL_VALID_STATES.size())
                {
                    iblockstate = ALL_VALID_STATES.get(i);
                }
            }
        }

        return iblockstate;
    }

    public void func_185931_b(int p_185931_1_, int p_185931_2_)
    {
    }

    public boolean func_185933_a(Chunk p_185933_1_, int p_185933_2_, int p_185933_3_)
    {
        return false;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        Biome biome = this.field_177463_c.func_180494_b(pos);
        return biome.getSpawns(creatureType);
    }

    @Nullable
    public BlockPos func_180513_a(World p_180513_1_, String p_180513_2_, BlockPos p_180513_3_, boolean p_180513_4_)
    {
        return null;
    }

    public boolean func_193414_a(World p_193414_1_, String p_193414_2_, BlockPos p_193414_3_)
    {
        return false;
    }

    public void func_180514_a(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_)
    {
    }

    static
    {
        for (Block block : Block.field_149771_c)
        {
            ALL_VALID_STATES.addAll(block.getStateContainer().getValidStates());
        }

        GRID_WIDTH = MathHelper.ceil(MathHelper.sqrt((float)ALL_VALID_STATES.size()));
        GRID_HEIGHT = MathHelper.ceil((float)ALL_VALID_STATES.size() / (float)GRID_WIDTH);
    }
}

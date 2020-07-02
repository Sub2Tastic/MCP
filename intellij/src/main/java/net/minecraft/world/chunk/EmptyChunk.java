package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EmptyChunk extends Chunk
{
    public EmptyChunk(World p_i1994_1_, int p_i1994_2_, int p_i1994_3_)
    {
        super(p_i1994_1_, p_i1994_2_, p_i1994_3_);
    }

    public boolean func_76600_a(int p_76600_1_, int p_76600_2_)
    {
        return p_76600_1_ == this.field_76635_g && p_76600_2_ == this.field_76647_h;
    }

    public int func_76611_b(int p_76611_1_, int p_76611_2_)
    {
        return 0;
    }

    public void func_76590_a()
    {
    }

    public void func_76603_b()
    {
    }

    public IBlockState func_177435_g(BlockPos p_177435_1_)
    {
        return Blocks.AIR.getDefaultState();
    }

    public int func_177437_b(BlockPos p_177437_1_)
    {
        return 255;
    }

    public int func_177413_a(EnumSkyBlock p_177413_1_, BlockPos p_177413_2_)
    {
        return p_177413_1_.defaultLightValue;
    }

    public void func_177431_a(EnumSkyBlock p_177431_1_, BlockPos p_177431_2_, int p_177431_3_)
    {
    }

    public int func_177443_a(BlockPos p_177443_1_, int p_177443_2_)
    {
        return 0;
    }

    /**
     * Adds an entity to the chunk.
     */
    public void addEntity(Entity entityIn)
    {
    }

    /**
     * removes entity using its y chunk coordinate as its index
     */
    public void removeEntity(Entity entityIn)
    {
    }

    /**
     * Removes entity at the specified index from the entity array.
     */
    public void removeEntityAtIndex(Entity entityIn, int index)
    {
    }

    public boolean func_177444_d(BlockPos p_177444_1_)
    {
        return false;
    }

    @Nullable
    public TileEntity getTileEntity(BlockPos pos, Chunk.EnumCreateEntityType creationMode)
    {
        return null;
    }

    public void addTileEntity(TileEntity tileEntityIn)
    {
    }

    public void addTileEntity(BlockPos pos, TileEntity tileEntityIn)
    {
    }

    public void removeTileEntity(BlockPos pos)
    {
    }

    public void func_76631_c()
    {
    }

    public void func_76623_d()
    {
    }

    /**
     * Sets the isModified flag for this Chunk
     */
    public void markDirty()
    {
    }

    /**
     * Fills the given list of all entities that intersect within the given bounding box that aren't the passed entity.
     */
    public void getEntitiesWithinAABBForEntity(@Nullable Entity entityIn, AxisAlignedBB aabb, List<Entity> listToFill, Predicate <? super Entity > filter)
    {
    }

    public <T extends Entity> void getEntitiesOfTypeWithinAABB(Class <? extends T > entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate <? super T > filter)
    {
    }

    public boolean func_76601_a(boolean p_76601_1_)
    {
        return false;
    }

    public Random func_76617_a(long p_76617_1_)
    {
        return new Random(this.getWorld().getSeed() + (long)(this.field_76635_g * this.field_76635_g * 4987142) + (long)(this.field_76635_g * 5947611) + (long)(this.field_76647_h * this.field_76647_h) * 4392871L + (long)(this.field_76647_h * 389711) ^ p_76617_1_);
    }

    public boolean isEmpty()
    {
        return true;
    }

    /**
     * Returns whether the ExtendedBlockStorages containing levels (in blocks) from arg 1 to arg 2 are fully empty
     * (true) or not (false).
     */
    public boolean isEmptyBetween(int startY, int endY)
    {
        return true;
    }
}

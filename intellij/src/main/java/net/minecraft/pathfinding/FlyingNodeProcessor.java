package net.minecraft.pathfinding;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class FlyingNodeProcessor extends WalkNodeProcessor
{
    public void func_186315_a(IBlockAccess p_186315_1_, EntityLiving p_186315_2_)
    {
        super.func_186315_a(p_186315_1_, p_186315_2_);
        this.avoidsWater = p_186315_2_.getPathPriority(PathNodeType.WATER);
    }

    /**
     * This method is called when all nodes have been processed and PathEntity is created.
     *  {@link net.minecraft.world.pathfinder.WalkNodeProcessor WalkNodeProcessor} uses this to change its field {@link
     * net.minecraft.world.pathfinder.WalkNodeProcessor#avoidsWater avoidsWater}
     */
    public void postProcess()
    {
        this.entity.setPathPriority(PathNodeType.WATER, this.avoidsWater);
        super.postProcess();
    }

    public PathPoint getStart()
    {
        int i;

        if (this.getCanSwim() && this.entity.isInWater())
        {
            i = (int)this.entity.getBoundingBox().minY;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));

            for (Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock(); block == Blocks.field_150358_i || block == Blocks.WATER; block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock())
            {
                ++i;
                blockpos$mutableblockpos.setPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));
            }
        }
        else
        {
            i = MathHelper.floor(this.entity.getBoundingBox().minY + 0.5D);
        }

        BlockPos blockpos1 = new BlockPos(this.entity);
        PathNodeType pathnodetype1 = this.getPathNodeType(this.entity, blockpos1.getX(), i, blockpos1.getZ());

        if (this.entity.getPathPriority(pathnodetype1) < 0.0F)
        {
            Set<BlockPos> set = Sets.<BlockPos>newHashSet();
            set.add(new BlockPos(this.entity.getBoundingBox().minX, (double)i, this.entity.getBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getBoundingBox().minX, (double)i, this.entity.getBoundingBox().maxZ));
            set.add(new BlockPos(this.entity.getBoundingBox().maxX, (double)i, this.entity.getBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getBoundingBox().maxX, (double)i, this.entity.getBoundingBox().maxZ));

            for (BlockPos blockpos : set)
            {
                PathNodeType pathnodetype = this.getPathNodeType(this.entity, blockpos);

                if (this.entity.getPathPriority(pathnodetype) >= 0.0F)
                {
                    return super.openPoint(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                }
            }
        }

        return super.openPoint(blockpos1.getX(), i, blockpos1.getZ());
    }

    public PathPoint func_186325_a(double p_186325_1_, double p_186325_3_, double p_186325_5_)
    {
        return super.openPoint(MathHelper.floor(p_186325_1_), MathHelper.floor(p_186325_3_), MathHelper.floor(p_186325_5_));
    }

    public int func_186320_a(PathPoint[] p_186320_1_, PathPoint p_186320_2_, PathPoint p_186320_3_, float p_186320_4_)
    {
        int i = 0;
        PathPoint pathpoint = this.openPoint(p_186320_2_.x, p_186320_2_.y, p_186320_2_.z + 1);
        PathPoint pathpoint1 = this.openPoint(p_186320_2_.x - 1, p_186320_2_.y, p_186320_2_.z);
        PathPoint pathpoint2 = this.openPoint(p_186320_2_.x + 1, p_186320_2_.y, p_186320_2_.z);
        PathPoint pathpoint3 = this.openPoint(p_186320_2_.x, p_186320_2_.y, p_186320_2_.z - 1);
        PathPoint pathpoint4 = this.openPoint(p_186320_2_.x, p_186320_2_.y + 1, p_186320_2_.z);
        PathPoint pathpoint5 = this.openPoint(p_186320_2_.x, p_186320_2_.y - 1, p_186320_2_.z);

        if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(p_186320_3_) < p_186320_4_)
        {
            p_186320_1_[i++] = pathpoint;
        }

        if (pathpoint1 != null && !pathpoint1.visited && pathpoint1.distanceTo(p_186320_3_) < p_186320_4_)
        {
            p_186320_1_[i++] = pathpoint1;
        }

        if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(p_186320_3_) < p_186320_4_)
        {
            p_186320_1_[i++] = pathpoint2;
        }

        if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.distanceTo(p_186320_3_) < p_186320_4_)
        {
            p_186320_1_[i++] = pathpoint3;
        }

        if (pathpoint4 != null && !pathpoint4.visited && pathpoint4.distanceTo(p_186320_3_) < p_186320_4_)
        {
            p_186320_1_[i++] = pathpoint4;
        }

        if (pathpoint5 != null && !pathpoint5.visited && pathpoint5.distanceTo(p_186320_3_) < p_186320_4_)
        {
            p_186320_1_[i++] = pathpoint5;
        }

        boolean flag = pathpoint3 == null || pathpoint3.costMalus != 0.0F;
        boolean flag1 = pathpoint == null || pathpoint.costMalus != 0.0F;
        boolean flag2 = pathpoint2 == null || pathpoint2.costMalus != 0.0F;
        boolean flag3 = pathpoint1 == null || pathpoint1.costMalus != 0.0F;
        boolean flag4 = pathpoint4 == null || pathpoint4.costMalus != 0.0F;
        boolean flag5 = pathpoint5 == null || pathpoint5.costMalus != 0.0F;

        if (flag && flag3)
        {
            PathPoint pathpoint6 = this.openPoint(p_186320_2_.x - 1, p_186320_2_.y, p_186320_2_.z - 1);

            if (pathpoint6 != null && !pathpoint6.visited && pathpoint6.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint6;
            }
        }

        if (flag && flag2)
        {
            PathPoint pathpoint7 = this.openPoint(p_186320_2_.x + 1, p_186320_2_.y, p_186320_2_.z - 1);

            if (pathpoint7 != null && !pathpoint7.visited && pathpoint7.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint7;
            }
        }

        if (flag1 && flag3)
        {
            PathPoint pathpoint8 = this.openPoint(p_186320_2_.x - 1, p_186320_2_.y, p_186320_2_.z + 1);

            if (pathpoint8 != null && !pathpoint8.visited && pathpoint8.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint8;
            }
        }

        if (flag1 && flag2)
        {
            PathPoint pathpoint9 = this.openPoint(p_186320_2_.x + 1, p_186320_2_.y, p_186320_2_.z + 1);

            if (pathpoint9 != null && !pathpoint9.visited && pathpoint9.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint9;
            }
        }

        if (flag && flag4)
        {
            PathPoint pathpoint10 = this.openPoint(p_186320_2_.x, p_186320_2_.y + 1, p_186320_2_.z - 1);

            if (pathpoint10 != null && !pathpoint10.visited && pathpoint10.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint10;
            }
        }

        if (flag1 && flag4)
        {
            PathPoint pathpoint11 = this.openPoint(p_186320_2_.x, p_186320_2_.y + 1, p_186320_2_.z + 1);

            if (pathpoint11 != null && !pathpoint11.visited && pathpoint11.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint11;
            }
        }

        if (flag2 && flag4)
        {
            PathPoint pathpoint12 = this.openPoint(p_186320_2_.x + 1, p_186320_2_.y + 1, p_186320_2_.z);

            if (pathpoint12 != null && !pathpoint12.visited && pathpoint12.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint12;
            }
        }

        if (flag3 && flag4)
        {
            PathPoint pathpoint13 = this.openPoint(p_186320_2_.x - 1, p_186320_2_.y + 1, p_186320_2_.z);

            if (pathpoint13 != null && !pathpoint13.visited && pathpoint13.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint13;
            }
        }

        if (flag && flag5)
        {
            PathPoint pathpoint14 = this.openPoint(p_186320_2_.x, p_186320_2_.y - 1, p_186320_2_.z - 1);

            if (pathpoint14 != null && !pathpoint14.visited && pathpoint14.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint14;
            }
        }

        if (flag1 && flag5)
        {
            PathPoint pathpoint15 = this.openPoint(p_186320_2_.x, p_186320_2_.y - 1, p_186320_2_.z + 1);

            if (pathpoint15 != null && !pathpoint15.visited && pathpoint15.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint15;
            }
        }

        if (flag2 && flag5)
        {
            PathPoint pathpoint16 = this.openPoint(p_186320_2_.x + 1, p_186320_2_.y - 1, p_186320_2_.z);

            if (pathpoint16 != null && !pathpoint16.visited && pathpoint16.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint16;
            }
        }

        if (flag3 && flag5)
        {
            PathPoint pathpoint17 = this.openPoint(p_186320_2_.x - 1, p_186320_2_.y - 1, p_186320_2_.z);

            if (pathpoint17 != null && !pathpoint17.visited && pathpoint17.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint17;
            }
        }

        return i;
    }

    @Nullable

    /**
     * Returns a mapped point or creates and adds one
     */
    protected PathPoint openPoint(int x, int y, int z)
    {
        PathPoint pathpoint = null;
        PathNodeType pathnodetype = this.getPathNodeType(this.entity, x, y, z);
        float f = this.entity.getPathPriority(pathnodetype);

        if (f >= 0.0F)
        {
            pathpoint = super.openPoint(x, y, z);
            pathpoint.nodeType = pathnodetype;
            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);

            if (pathnodetype == PathNodeType.WALKABLE)
            {
                ++pathpoint.costMalus;
            }
        }

        return pathnodetype != PathNodeType.OPEN && pathnodetype != PathNodeType.WALKABLE ? pathpoint : pathpoint;
    }

    public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z, EntityLiving entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn)
    {
        EnumSet<PathNodeType> enumset = EnumSet.<PathNodeType>noneOf(PathNodeType.class);
        PathNodeType pathnodetype = PathNodeType.BLOCKED;
        BlockPos blockpos = new BlockPos(entitylivingIn);
        pathnodetype = this.getPathNodeType(blockaccessIn, x, y, z, xSize, ySize, zSize, canBreakDoorsIn, canEnterDoorsIn, enumset, pathnodetype, blockpos);

        if (enumset.contains(PathNodeType.FENCE))
        {
            return PathNodeType.FENCE;
        }
        else
        {
            PathNodeType pathnodetype1 = PathNodeType.BLOCKED;

            for (PathNodeType pathnodetype2 : enumset)
            {
                if (entitylivingIn.getPathPriority(pathnodetype2) < 0.0F)
                {
                    return pathnodetype2;
                }

                if (entitylivingIn.getPathPriority(pathnodetype2) >= entitylivingIn.getPathPriority(pathnodetype1))
                {
                    pathnodetype1 = pathnodetype2;
                }
            }

            if (pathnodetype == PathNodeType.OPEN && entitylivingIn.getPathPriority(pathnodetype1) == 0.0F)
            {
                return PathNodeType.OPEN;
            }
            else
            {
                return pathnodetype1;
            }
        }
    }

    public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z)
    {
        PathNodeType pathnodetype = this.getPathNodeTypeRaw(blockaccessIn, x, y, z);

        if (pathnodetype == PathNodeType.OPEN && y >= 1)
        {
            Block block = blockaccessIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
            PathNodeType pathnodetype1 = this.getPathNodeTypeRaw(blockaccessIn, x, y - 1, z);

            if (pathnodetype1 != PathNodeType.DAMAGE_FIRE && block != Blocks.field_189877_df && pathnodetype1 != PathNodeType.LAVA)
            {
                if (pathnodetype1 == PathNodeType.DAMAGE_CACTUS)
                {
                    pathnodetype = PathNodeType.DAMAGE_CACTUS;
                }
                else
                {
                    pathnodetype = pathnodetype1 != PathNodeType.WALKABLE && pathnodetype1 != PathNodeType.OPEN && pathnodetype1 != PathNodeType.WATER ? PathNodeType.WALKABLE : PathNodeType.OPEN;
                }
            }
            else
            {
                pathnodetype = PathNodeType.DAMAGE_FIRE;
            }
        }

        pathnodetype = this.checkNeighborBlocks(blockaccessIn, x, y, z, pathnodetype);
        return pathnodetype;
    }

    private PathNodeType getPathNodeType(EntityLiving p_192559_1_, BlockPos p_192559_2_)
    {
        return this.getPathNodeType(p_192559_1_, p_192559_2_.getX(), p_192559_2_.getY(), p_192559_2_.getZ());
    }

    private PathNodeType getPathNodeType(EntityLiving p_192558_1_, int p_192558_2_, int p_192558_3_, int p_192558_4_)
    {
        return this.getPathNodeType(this.blockaccess, p_192558_2_, p_192558_3_, p_192558_4_, p_192558_1_, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanOpenDoors(), this.getCanEnterDoors());
    }
}

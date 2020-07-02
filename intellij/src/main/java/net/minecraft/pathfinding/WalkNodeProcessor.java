package net.minecraft.pathfinding;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class WalkNodeProcessor extends NodeProcessor
{
    protected float avoidsWater;

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
        else if (this.entity.onGround)
        {
            i = MathHelper.floor(this.entity.getBoundingBox().minY + 0.5D);
        }
        else
        {
            BlockPos blockpos;

            for (blockpos = new BlockPos(this.entity); (this.blockaccess.getBlockState(blockpos).getMaterial() == Material.AIR || this.blockaccess.getBlockState(blockpos).getBlock().func_176205_b(this.blockaccess, blockpos)) && blockpos.getY() > 0; blockpos = blockpos.down())
            {
                ;
            }

            i = blockpos.up().getY();
        }

        BlockPos blockpos2 = new BlockPos(this.entity);
        PathNodeType pathnodetype1 = this.getPathNodeType(this.entity, blockpos2.getX(), i, blockpos2.getZ());

        if (this.entity.getPathPriority(pathnodetype1) < 0.0F)
        {
            Set<BlockPos> set = Sets.<BlockPos>newHashSet();
            set.add(new BlockPos(this.entity.getBoundingBox().minX, (double)i, this.entity.getBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getBoundingBox().minX, (double)i, this.entity.getBoundingBox().maxZ));
            set.add(new BlockPos(this.entity.getBoundingBox().maxX, (double)i, this.entity.getBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getBoundingBox().maxX, (double)i, this.entity.getBoundingBox().maxZ));

            for (BlockPos blockpos1 : set)
            {
                PathNodeType pathnodetype = this.getPathNodeType(this.entity, blockpos1);

                if (this.entity.getPathPriority(pathnodetype) >= 0.0F)
                {
                    return this.openPoint(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                }
            }
        }

        return this.openPoint(blockpos2.getX(), i, blockpos2.getZ());
    }

    public PathPoint func_186325_a(double p_186325_1_, double p_186325_3_, double p_186325_5_)
    {
        return this.openPoint(MathHelper.floor(p_186325_1_), MathHelper.floor(p_186325_3_), MathHelper.floor(p_186325_5_));
    }

    public int func_186320_a(PathPoint[] p_186320_1_, PathPoint p_186320_2_, PathPoint p_186320_3_, float p_186320_4_)
    {
        int i = 0;
        int j = 0;
        PathNodeType pathnodetype = this.getPathNodeType(this.entity, p_186320_2_.x, p_186320_2_.y + 1, p_186320_2_.z);

        if (this.entity.getPathPriority(pathnodetype) >= 0.0F)
        {
            j = MathHelper.floor(Math.max(1.0F, this.entity.stepHeight));
        }

        BlockPos blockpos = (new BlockPos(p_186320_2_.x, p_186320_2_.y, p_186320_2_.z)).down();
        double d0 = (double)p_186320_2_.y - (1.0D - this.blockaccess.getBlockState(blockpos).func_185900_c(this.blockaccess, blockpos).maxY);
        PathPoint pathpoint = this.getSafePoint(p_186320_2_.x, p_186320_2_.y, p_186320_2_.z + 1, j, d0, EnumFacing.SOUTH);
        PathPoint pathpoint1 = this.getSafePoint(p_186320_2_.x - 1, p_186320_2_.y, p_186320_2_.z, j, d0, EnumFacing.WEST);
        PathPoint pathpoint2 = this.getSafePoint(p_186320_2_.x + 1, p_186320_2_.y, p_186320_2_.z, j, d0, EnumFacing.EAST);
        PathPoint pathpoint3 = this.getSafePoint(p_186320_2_.x, p_186320_2_.y, p_186320_2_.z - 1, j, d0, EnumFacing.NORTH);

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

        boolean flag = pathpoint3 == null || pathpoint3.nodeType == PathNodeType.OPEN || pathpoint3.costMalus != 0.0F;
        boolean flag1 = pathpoint == null || pathpoint.nodeType == PathNodeType.OPEN || pathpoint.costMalus != 0.0F;
        boolean flag2 = pathpoint2 == null || pathpoint2.nodeType == PathNodeType.OPEN || pathpoint2.costMalus != 0.0F;
        boolean flag3 = pathpoint1 == null || pathpoint1.nodeType == PathNodeType.OPEN || pathpoint1.costMalus != 0.0F;

        if (flag && flag3)
        {
            PathPoint pathpoint4 = this.getSafePoint(p_186320_2_.x - 1, p_186320_2_.y, p_186320_2_.z - 1, j, d0, EnumFacing.NORTH);

            if (pathpoint4 != null && !pathpoint4.visited && pathpoint4.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint4;
            }
        }

        if (flag && flag2)
        {
            PathPoint pathpoint5 = this.getSafePoint(p_186320_2_.x + 1, p_186320_2_.y, p_186320_2_.z - 1, j, d0, EnumFacing.NORTH);

            if (pathpoint5 != null && !pathpoint5.visited && pathpoint5.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint5;
            }
        }

        if (flag1 && flag3)
        {
            PathPoint pathpoint6 = this.getSafePoint(p_186320_2_.x - 1, p_186320_2_.y, p_186320_2_.z + 1, j, d0, EnumFacing.SOUTH);

            if (pathpoint6 != null && !pathpoint6.visited && pathpoint6.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint6;
            }
        }

        if (flag1 && flag2)
        {
            PathPoint pathpoint7 = this.getSafePoint(p_186320_2_.x + 1, p_186320_2_.y, p_186320_2_.z + 1, j, d0, EnumFacing.SOUTH);

            if (pathpoint7 != null && !pathpoint7.visited && pathpoint7.distanceTo(p_186320_3_) < p_186320_4_)
            {
                p_186320_1_[i++] = pathpoint7;
            }
        }

        return i;
    }

    @Nullable

    /**
     * Returns a point that the entity can safely move to
     */
    private PathPoint getSafePoint(int x, int y, int z, int stepHeight, double groundYIn, EnumFacing facing)
    {
        PathPoint pathpoint = null;
        BlockPos blockpos = new BlockPos(x, y, z);
        BlockPos blockpos1 = blockpos.down();
        double d0 = (double)y - (1.0D - this.blockaccess.getBlockState(blockpos1).func_185900_c(this.blockaccess, blockpos1).maxY);

        if (d0 - groundYIn > 1.125D)
        {
            return null;
        }
        else
        {
            PathNodeType pathnodetype = this.getPathNodeType(this.entity, x, y, z);
            float f = this.entity.getPathPriority(pathnodetype);
            double d1 = (double)this.entity.field_70130_N / 2.0D;

            if (f >= 0.0F)
            {
                pathpoint = this.openPoint(x, y, z);
                pathpoint.nodeType = pathnodetype;
                pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
            }

            if (pathnodetype == PathNodeType.WALKABLE)
            {
                return pathpoint;
            }
            else
            {
                if (pathpoint == null && stepHeight > 0 && pathnodetype != PathNodeType.FENCE && pathnodetype != PathNodeType.TRAPDOOR)
                {
                    pathpoint = this.getSafePoint(x, y + 1, z, stepHeight - 1, groundYIn, facing);

                    if (pathpoint != null && (pathpoint.nodeType == PathNodeType.OPEN || pathpoint.nodeType == PathNodeType.WALKABLE) && this.entity.field_70130_N < 1.0F)
                    {
                        double d2 = (double)(x - facing.getXOffset()) + 0.5D;
                        double d3 = (double)(z - facing.getZOffset()) + 0.5D;
                        AxisAlignedBB axisalignedbb = new AxisAlignedBB(d2 - d1, (double)y + 0.001D, d3 - d1, d2 + d1, (double)((float)y + this.entity.field_70131_O), d3 + d1);
                        AxisAlignedBB axisalignedbb1 = this.blockaccess.getBlockState(blockpos).func_185900_c(this.blockaccess, blockpos);
                        AxisAlignedBB axisalignedbb2 = axisalignedbb.expand(0.0D, axisalignedbb1.maxY - 0.002D, 0.0D);

                        if (this.entity.world.func_184143_b(axisalignedbb2))
                        {
                            pathpoint = null;
                        }
                    }
                }

                if (pathnodetype == PathNodeType.OPEN)
                {
                    AxisAlignedBB axisalignedbb3 = new AxisAlignedBB((double)x - d1 + 0.5D, (double)y + 0.001D, (double)z - d1 + 0.5D, (double)x + d1 + 0.5D, (double)((float)y + this.entity.field_70131_O), (double)z + d1 + 0.5D);

                    if (this.entity.world.func_184143_b(axisalignedbb3))
                    {
                        return null;
                    }

                    if (this.entity.field_70130_N >= 1.0F)
                    {
                        PathNodeType pathnodetype1 = this.getPathNodeType(this.entity, x, y - 1, z);

                        if (pathnodetype1 == PathNodeType.BLOCKED)
                        {
                            pathpoint = this.openPoint(x, y, z);
                            pathpoint.nodeType = PathNodeType.WALKABLE;
                            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                            return pathpoint;
                        }
                    }

                    int i = 0;

                    while (y > 0 && pathnodetype == PathNodeType.OPEN)
                    {
                        --y;

                        if (i++ >= this.entity.getMaxFallHeight())
                        {
                            return null;
                        }

                        pathnodetype = this.getPathNodeType(this.entity, x, y, z);
                        f = this.entity.getPathPriority(pathnodetype);

                        if (pathnodetype != PathNodeType.OPEN && f >= 0.0F)
                        {
                            pathpoint = this.openPoint(x, y, z);
                            pathpoint.nodeType = pathnodetype;
                            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                            break;
                        }

                        if (f < 0.0F)
                        {
                            return null;
                        }
                    }
                }

                return pathpoint;
            }
        }
    }

    public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z, EntityLiving entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn)
    {
        EnumSet<PathNodeType> enumset = EnumSet.<PathNodeType>noneOf(PathNodeType.class);
        PathNodeType pathnodetype = PathNodeType.BLOCKED;
        double d0 = (double)entitylivingIn.field_70130_N / 2.0D;
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

    public PathNodeType getPathNodeType(IBlockAccess p_193577_1_, int x, int y, int z, int xSize, int ySize, int zSize, boolean canOpenDoorsIn, boolean canEnterDoorsIn, EnumSet<PathNodeType> nodeTypeEnum, PathNodeType nodeType, BlockPos pos)
    {
        for (int i = 0; i < xSize; ++i)
        {
            for (int j = 0; j < ySize; ++j)
            {
                for (int k = 0; k < zSize; ++k)
                {
                    int l = i + x;
                    int i1 = j + y;
                    int j1 = k + z;
                    PathNodeType pathnodetype = this.getPathNodeType(p_193577_1_, l, i1, j1);

                    if (pathnodetype == PathNodeType.DOOR_WOOD_CLOSED && canOpenDoorsIn && canEnterDoorsIn)
                    {
                        pathnodetype = PathNodeType.WALKABLE;
                    }

                    if (pathnodetype == PathNodeType.DOOR_OPEN && !canEnterDoorsIn)
                    {
                        pathnodetype = PathNodeType.BLOCKED;
                    }

                    if (pathnodetype == PathNodeType.RAIL && !(p_193577_1_.getBlockState(pos).getBlock() instanceof BlockRailBase) && !(p_193577_1_.getBlockState(pos.down()).getBlock() instanceof BlockRailBase))
                    {
                        pathnodetype = PathNodeType.FENCE;
                    }

                    if (i == 0 && j == 0 && k == 0)
                    {
                        nodeType = pathnodetype;
                    }

                    nodeTypeEnum.add(pathnodetype);
                }
            }
        }

        return nodeType;
    }

    private PathNodeType getPathNodeType(EntityLiving entitylivingIn, BlockPos pos)
    {
        return this.getPathNodeType(entitylivingIn, pos.getX(), pos.getY(), pos.getZ());
    }

    private PathNodeType getPathNodeType(EntityLiving entitylivingIn, int x, int y, int z)
    {
        return this.getPathNodeType(this.blockaccess, x, y, z, entitylivingIn, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanOpenDoors(), this.getCanEnterDoors());
    }

    public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z)
    {
        PathNodeType pathnodetype = this.getPathNodeTypeRaw(blockaccessIn, x, y, z);

        if (pathnodetype == PathNodeType.OPEN && y >= 1)
        {
            Block block = blockaccessIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
            PathNodeType pathnodetype1 = this.getPathNodeTypeRaw(blockaccessIn, x, y - 1, z);
            pathnodetype = pathnodetype1 != PathNodeType.WALKABLE && pathnodetype1 != PathNodeType.OPEN && pathnodetype1 != PathNodeType.WATER && pathnodetype1 != PathNodeType.LAVA ? PathNodeType.WALKABLE : PathNodeType.OPEN;

            if (pathnodetype1 == PathNodeType.DAMAGE_FIRE || block == Blocks.field_189877_df)
            {
                pathnodetype = PathNodeType.DAMAGE_FIRE;
            }

            if (pathnodetype1 == PathNodeType.DAMAGE_CACTUS)
            {
                pathnodetype = PathNodeType.DAMAGE_CACTUS;
            }
        }

        pathnodetype = this.checkNeighborBlocks(blockaccessIn, x, y, z, pathnodetype);
        return pathnodetype;
    }

    public PathNodeType checkNeighborBlocks(IBlockAccess blockaccessIn, int x, int y, int z, PathNodeType p_193578_5_)
    {
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        if (p_193578_5_ == PathNodeType.WALKABLE)
        {
            for (int i = -1; i <= 1; ++i)
            {
                for (int j = -1; j <= 1; ++j)
                {
                    if (i != 0 || j != 0)
                    {
                        Block block = blockaccessIn.getBlockState(blockpos$pooledmutableblockpos.setPos(i + x, y, j + z)).getBlock();

                        if (block == Blocks.CACTUS)
                        {
                            p_193578_5_ = PathNodeType.DANGER_CACTUS;
                        }
                        else if (block == Blocks.FIRE)
                        {
                            p_193578_5_ = PathNodeType.DANGER_FIRE;
                        }
                    }
                }
            }
        }

        blockpos$pooledmutableblockpos.func_185344_t();
        return p_193578_5_;
    }

    protected PathNodeType getPathNodeTypeRaw(IBlockAccess blockaccessIn, int x, int y, int p_189553_4_)
    {
        BlockPos blockpos = new BlockPos(x, y, p_189553_4_);
        IBlockState iblockstate = blockaccessIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        Material material = iblockstate.getMaterial();

        if (material == Material.AIR)
        {
            return PathNodeType.OPEN;
        }
        else if (block != Blocks.field_150415_aT && block != Blocks.IRON_TRAPDOOR && block != Blocks.field_150392_bi)
        {
            if (block == Blocks.FIRE)
            {
                return PathNodeType.DAMAGE_FIRE;
            }
            else if (block == Blocks.CACTUS)
            {
                return PathNodeType.DAMAGE_CACTUS;
            }
            else if (block instanceof BlockDoor && material == Material.WOOD && !((Boolean)iblockstate.get(BlockDoor.OPEN)).booleanValue())
            {
                return PathNodeType.DOOR_WOOD_CLOSED;
            }
            else if (block instanceof BlockDoor && material == Material.IRON && !((Boolean)iblockstate.get(BlockDoor.OPEN)).booleanValue())
            {
                return PathNodeType.DOOR_IRON_CLOSED;
            }
            else if (block instanceof BlockDoor && ((Boolean)iblockstate.get(BlockDoor.OPEN)).booleanValue())
            {
                return PathNodeType.DOOR_OPEN;
            }
            else if (block instanceof BlockRailBase)
            {
                return PathNodeType.RAIL;
            }
            else if (!(block instanceof BlockFence) && !(block instanceof BlockWall) && (!(block instanceof BlockFenceGate) || ((Boolean)iblockstate.get(BlockFenceGate.OPEN)).booleanValue()))
            {
                if (material == Material.WATER)
                {
                    return PathNodeType.WATER;
                }
                else if (material == Material.LAVA)
                {
                    return PathNodeType.LAVA;
                }
                else
                {
                    return block.func_176205_b(blockaccessIn, blockpos) ? PathNodeType.OPEN : PathNodeType.BLOCKED;
                }
            }
            else
            {
                return PathNodeType.FENCE;
            }
        }
        else
        {
            return PathNodeType.TRAPDOOR;
        }
    }
}

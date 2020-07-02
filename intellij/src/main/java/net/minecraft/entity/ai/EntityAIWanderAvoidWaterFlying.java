package net.minecraft.entity.ai;

import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.EntityCreature;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntityAIWanderAvoidWaterFlying extends EntityAIWanderAvoidWater
{
    public EntityAIWanderAvoidWaterFlying(EntityCreature p_i47413_1_, double p_i47413_2_)
    {
        super(p_i47413_1_, p_i47413_2_);
    }

    @Nullable
    protected Vec3d getPosition()
    {
        Vec3d vec3d = null;

        if (this.creature.isInWater() || this.creature.func_191953_am())
        {
            vec3d = RandomPositionGenerator.getLandPos(this.creature, 15, 15);
        }

        if (this.creature.getRNG().nextFloat() >= this.probability)
        {
            vec3d = this.getTreePos();
        }

        return vec3d == null ? super.getPosition() : vec3d;
    }

    @Nullable
    private Vec3d getTreePos()
    {
        BlockPos blockpos = new BlockPos(this.creature);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
        Iterable<BlockPos.MutableBlockPos> iterable = BlockPos.MutableBlockPos.getAllInBoxMutable(MathHelper.floor(this.creature.posX - 3.0D), MathHelper.floor(this.creature.posY - 6.0D), MathHelper.floor(this.creature.posZ - 3.0D), MathHelper.floor(this.creature.posX + 3.0D), MathHelper.floor(this.creature.posY + 6.0D), MathHelper.floor(this.creature.posZ + 3.0D));
        Iterator iterator = iterable.iterator();
        BlockPos blockpos1;

        while (true)
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            blockpos1 = (BlockPos)iterator.next();

            if (!blockpos.equals(blockpos1))
            {
                Block block = this.creature.world.getBlockState(blockpos$mutableblockpos1.setPos(blockpos1).move(EnumFacing.DOWN)).getBlock();
                boolean flag = block instanceof BlockLeaves || block == Blocks.field_150364_r || block == Blocks.field_150363_s;

                if (flag && this.creature.world.isAirBlock(blockpos1) && this.creature.world.isAirBlock(blockpos$mutableblockpos.setPos(blockpos1).move(EnumFacing.UP)))
                {
                    break;
                }
            }
        }

        return new Vec3d((double)blockpos1.getX(), (double)blockpos1.getY(), (double)blockpos1.getZ());
    }
}
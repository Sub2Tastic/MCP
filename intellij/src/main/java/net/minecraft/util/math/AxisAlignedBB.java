package net.minecraft.util.math;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;

public class AxisAlignedBB
{
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;

    public AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }

    public AxisAlignedBB(BlockPos pos)
    {
        this((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 1), (double)(pos.getZ() + 1));
    }

    public AxisAlignedBB(BlockPos pos1, BlockPos pos2)
    {
        this((double)pos1.getX(), (double)pos1.getY(), (double)pos1.getZ(), (double)pos2.getX(), (double)pos2.getY(), (double)pos2.getZ());
    }

    public AxisAlignedBB(Vec3d min, Vec3d max)
    {
        this(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    public AxisAlignedBB func_186666_e(double p_186666_1_)
    {
        return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, p_186666_1_, this.maxZ);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof AxisAlignedBB))
        {
            return false;
        }
        else
        {
            AxisAlignedBB axisalignedbb = (AxisAlignedBB)p_equals_1_;

            if (Double.compare(axisalignedbb.minX, this.minX) != 0)
            {
                return false;
            }
            else if (Double.compare(axisalignedbb.minY, this.minY) != 0)
            {
                return false;
            }
            else if (Double.compare(axisalignedbb.minZ, this.minZ) != 0)
            {
                return false;
            }
            else if (Double.compare(axisalignedbb.maxX, this.maxX) != 0)
            {
                return false;
            }
            else if (Double.compare(axisalignedbb.maxY, this.maxY) != 0)
            {
                return false;
            }
            else
            {
                return Double.compare(axisalignedbb.maxZ, this.maxZ) == 0;
            }
        }
    }

    public int hashCode()
    {
        long i = Double.doubleToLongBits(this.minX);
        int j = (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.minY);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.minZ);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxX);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxY);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxZ);
        j = 31 * j + (int)(i ^ i >>> 32);
        return j;
    }

    /**
     * Creates a new {@link AxisAlignedBB} that has been contracted by the given amount, with positive changes
     * decreasing max values and negative changes increasing min values.
     * <br/>
     * If the amount to contract by is larger than the length of a side, then the side will wrap (still creating a valid
     * AABB - see last sample).
     *  
     * <h3>Samples:</h3>
     * <table>
     * <tr><th>Input</th><th>Result</th></tr>
     * <tr><td><pre><code>new AxisAlignedBB(0, 0, 0, 4, 4, 4).contract(2, 2, 2)</code></pre></td><td><pre><samp>box[0.0,
     * 0.0, 0.0 -> 2.0, 2.0, 2.0]</samp></pre></td></tr>
     * <tr><td><pre><code>new AxisAlignedBB(0, 0, 0, 4, 4, 4).contract(-2, -2,
     * -2)</code></pre></td><td><pre><samp>box[2.0, 2.0, 2.0 -> 4.0, 4.0, 4.0]</samp></pre></td></tr>
     * <tr><td><pre><code>new AxisAlignedBB(5, 5, 5, 7, 7, 7).contract(0, 1,
     * -1)</code></pre></td><td><pre><samp>box[5.0, 5.0, 6.0 -> 7.0, 6.0, 7.0]</samp></pre></td></tr>
     * <tr><td><pre><code>new AxisAlignedBB(-2, -2, -2, 2, 2, 2).contract(4, -4,
     * 0)</code></pre></td><td><pre><samp>box[-8.0, 2.0, -2.0 -> -2.0, 8.0, 2.0]</samp></pre></td></tr>
     * </table>
     *  
     * <h3>See Also:</h3>
     * <ul>
     * <li>{@link #expand(double, double, double)} - like this, except for expanding.</li>
     * <li>{@link #grow(double, double, double)} and {@link #grow(double)} - expands in all directions.</li>
     * <li>{@link #shrink(double)} - contracts in all directions (like {@link #grow(double)})</li>
     * </ul>
     *  
     * @return A new modified bounding box.
     */
    public AxisAlignedBB contract(double x, double y, double z)
    {
        double d0 = this.minX;
        double d1 = this.minY;
        double d2 = this.minZ;
        double d3 = this.maxX;
        double d4 = this.maxY;
        double d5 = this.maxZ;

        if (x < 0.0D)
        {
            d0 -= x;
        }
        else if (x > 0.0D)
        {
            d3 -= x;
        }

        if (y < 0.0D)
        {
            d1 -= y;
        }
        else if (y > 0.0D)
        {
            d4 -= y;
        }

        if (z < 0.0D)
        {
            d2 -= z;
        }
        else if (z > 0.0D)
        {
            d5 -= z;
        }

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    /**
     * Creates a new {@link AxisAlignedBB} that has been expanded by the given amount, with positive changes increasing
     * max values and negative changes decreasing min values.

     * <h3>Samples:</h3>
     * <table>
     * <tr><th>Input</th><th>Result</th></tr>
     * <tr><td><pre><code>new AxisAlignedBB(0, 0, 0, 1, 1, 1).expand(2, 2, 2)</code></pre></td><td><pre><samp>box[0, 0,
     * 0 -> 3, 3, 3]</samp></pre></td><td>
     * <tr><td><pre><code>new AxisAlignedBB(0, 0, 0, 1, 1, 1).expand(-2, -2, -2)</code></pre></td><td><pre><samp>box[-2,
     * -2, -2 -> 1, 1, 1]</samp></pre></td><td>
     * <tr><td><pre><code>new AxisAlignedBB(5, 5, 5, 7, 7, 7).expand(0, 1, -1)</code></pre></td><td><pre><samp>box[5, 5,
     * 4, 7, 8, 7]</samp></pre></td><td>
     * </table>

     * <h3>See Also:</h3>
     * <ul>
     * <li>{@link #contract(double, double, double)} - like this, except for shrinking.</li>
     * <li>{@link #grow(double, double, double)} and {@link #grow(double)} - expands in all directions.</li>
     * <li>{@link #shrink(double)} - contracts in all directions (like {@link #grow(double)})</li>
     * </ul>

     * @return A modified bounding box that will always be equal or greater in volume to this bounding box.
     */
    public AxisAlignedBB expand(double x, double y, double z)
    {
        double d0 = this.minX;
        double d1 = this.minY;
        double d2 = this.minZ;
        double d3 = this.maxX;
        double d4 = this.maxY;
        double d5 = this.maxZ;

        if (x < 0.0D)
        {
            d0 += x;
        }
        else if (x > 0.0D)
        {
            d3 += x;
        }

        if (y < 0.0D)
        {
            d1 += y;
        }
        else if (y > 0.0D)
        {
            d4 += y;
        }

        if (z < 0.0D)
        {
            d2 += z;
        }
        else if (z > 0.0D)
        {
            d5 += z;
        }

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    /**
     * Creates a new {@link AxisAlignedBB} that has been contracted by the given amount in both directions. Negative
     * values will shrink the AABB instead of expanding it.
     * <br/>
     * Side lengths will be increased by 2 times the value of the parameters, since both min and max are changed.
     * <br/>
     * If contracting and the amount to contract by is larger than the length of a side, then the side will wrap (still
     * creating a valid AABB - see last ample).
     *  
     * <h3>Samples:</h3>
     * <table>
     * <tr><th>Input</th><th>Result</th></tr>
     * <tr><td><pre><code>new AxisAlignedBB(0, 0, 0, 1, 1, 1).grow(2, 2, 2)</code></pre></td><td><pre><samp>box[-2.0,
     * -2.0, -2.0 -> 3.0, 3.0, 3.0]</samp></pre></td></tr>
     * <tr><td><pre><code>new AxisAlignedBB(0, 0, 0, 6, 6, 6).grow(-2, -2, -2)</code></pre></td><td><pre><samp>box[2.0,
     * 2.0, 2.0 -> 4.0, 4.0, 4.0]</samp></pre></td></tr>
     * <tr><td><pre><code>new AxisAlignedBB(5, 5, 5, 7, 7, 7).grow(0, 1, -1)</code></pre></td><td><pre><samp>box[5.0,
     * 4.0, 6.0 -> 7.0, 8.0, 6.0]</samp></pre></td></tr>
     * <tr><td><pre><code>new AxisAlignedBB(1, 1, 1, 3, 3, 3).grow(-4, -2, -3)</code></pre></td><td><pre><samp>box[-1.0,
     * 1.0, 0.0 -> 5.0, 3.0, 4.0]</samp></pre></td></tr>
     * </table>
     *  
     * <h3>See Also:</h3>
     * <ul>
     * <li>{@link #expand(double, double, double)} - expands in only one direction.</li>
     * <li>{@link #contract(double, double, double)} - contracts in only one direction.</li>
     * <lu>{@link #grow(double)} - version of this that expands in all directions from one parameter.</li>
     * <li>{@link #shrink(double)} - contracts in all directions</li>
     * </ul>
     *  
     * @return A modified bounding box.
     */
    public AxisAlignedBB grow(double x, double y, double z)
    {
        double d0 = this.minX - x;
        double d1 = this.minY - y;
        double d2 = this.minZ - z;
        double d3 = this.maxX + x;
        double d4 = this.maxY + y;
        double d5 = this.maxZ + z;
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    /**
     * Creates a new {@link AxisAlignedBB} that is expanded by the given value in all directions. Equivalent to {@link
     * #grow(double, double, double)} with the given value for all 3 params. Negative values will shrink the AABB.
     * <br/>
     * Side lengths will be increased by 2 times the value of the parameter, since both min and max are changed.
     * <br/>
     * If contracting and the amount to contract by is larger than the length of a side, then the side will wrap (still
     * creating a valid AABB - see samples on {@link #grow(double, double, double)}).
     *  
     * @return A modified AABB.
     */
    public AxisAlignedBB grow(double value)
    {
        return this.grow(value, value, value);
    }

    public AxisAlignedBB intersect(AxisAlignedBB other)
    {
        double d0 = Math.max(this.minX, other.minX);
        double d1 = Math.max(this.minY, other.minY);
        double d2 = Math.max(this.minZ, other.minZ);
        double d3 = Math.min(this.maxX, other.maxX);
        double d4 = Math.min(this.maxY, other.maxY);
        double d5 = Math.min(this.maxZ, other.maxZ);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB union(AxisAlignedBB other)
    {
        double d0 = Math.min(this.minX, other.minX);
        double d1 = Math.min(this.minY, other.minY);
        double d2 = Math.min(this.minZ, other.minZ);
        double d3 = Math.max(this.maxX, other.maxX);
        double d4 = Math.max(this.maxY, other.maxY);
        double d5 = Math.max(this.maxZ, other.maxZ);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    /**
     * Offsets the current bounding box by the specified amount.
     */
    public AxisAlignedBB offset(double x, double y, double z)
    {
        return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public AxisAlignedBB offset(BlockPos pos)
    {
        return new AxisAlignedBB(this.minX + (double)pos.getX(), this.minY + (double)pos.getY(), this.minZ + (double)pos.getZ(), this.maxX + (double)pos.getX(), this.maxY + (double)pos.getY(), this.maxZ + (double)pos.getZ());
    }

    public AxisAlignedBB offset(Vec3d vec)
    {
        return this.offset(vec.x, vec.y, vec.z);
    }

    public double func_72316_a(AxisAlignedBB p_72316_1_, double p_72316_2_)
    {
        if (p_72316_1_.maxY > this.minY && p_72316_1_.minY < this.maxY && p_72316_1_.maxZ > this.minZ && p_72316_1_.minZ < this.maxZ)
        {
            if (p_72316_2_ > 0.0D && p_72316_1_.maxX <= this.minX)
            {
                double d1 = this.minX - p_72316_1_.maxX;

                if (d1 < p_72316_2_)
                {
                    p_72316_2_ = d1;
                }
            }
            else if (p_72316_2_ < 0.0D && p_72316_1_.minX >= this.maxX)
            {
                double d0 = this.maxX - p_72316_1_.minX;

                if (d0 > p_72316_2_)
                {
                    p_72316_2_ = d0;
                }
            }

            return p_72316_2_;
        }
        else
        {
            return p_72316_2_;
        }
    }

    public double func_72323_b(AxisAlignedBB p_72323_1_, double p_72323_2_)
    {
        if (p_72323_1_.maxX > this.minX && p_72323_1_.minX < this.maxX && p_72323_1_.maxZ > this.minZ && p_72323_1_.minZ < this.maxZ)
        {
            if (p_72323_2_ > 0.0D && p_72323_1_.maxY <= this.minY)
            {
                double d1 = this.minY - p_72323_1_.maxY;

                if (d1 < p_72323_2_)
                {
                    p_72323_2_ = d1;
                }
            }
            else if (p_72323_2_ < 0.0D && p_72323_1_.minY >= this.maxY)
            {
                double d0 = this.maxY - p_72323_1_.minY;

                if (d0 > p_72323_2_)
                {
                    p_72323_2_ = d0;
                }
            }

            return p_72323_2_;
        }
        else
        {
            return p_72323_2_;
        }
    }

    public double func_72322_c(AxisAlignedBB p_72322_1_, double p_72322_2_)
    {
        if (p_72322_1_.maxX > this.minX && p_72322_1_.minX < this.maxX && p_72322_1_.maxY > this.minY && p_72322_1_.minY < this.maxY)
        {
            if (p_72322_2_ > 0.0D && p_72322_1_.maxZ <= this.minZ)
            {
                double d1 = this.minZ - p_72322_1_.maxZ;

                if (d1 < p_72322_2_)
                {
                    p_72322_2_ = d1;
                }
            }
            else if (p_72322_2_ < 0.0D && p_72322_1_.minZ >= this.maxZ)
            {
                double d0 = this.maxZ - p_72322_1_.minZ;

                if (d0 > p_72322_2_)
                {
                    p_72322_2_ = d0;
                }
            }

            return p_72322_2_;
        }
        else
        {
            return p_72322_2_;
        }
    }

    /**
     * Checks if the bounding box intersects with another.
     */
    public boolean intersects(AxisAlignedBB other)
    {
        return this.intersects(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }

    public boolean intersects(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return this.minX < x2 && this.maxX > x1 && this.minY < y2 && this.maxY > y1 && this.minZ < z2 && this.maxZ > z1;
    }

    public boolean intersects(Vec3d min, Vec3d max)
    {
        return this.intersects(Math.min(min.x, max.x), Math.min(min.y, max.y), Math.min(min.z, max.z), Math.max(min.x, max.x), Math.max(min.y, max.y), Math.max(min.z, max.z));
    }

    /**
     * Returns if the supplied Vec3D is completely inside the bounding box
     */
    public boolean contains(Vec3d vec)
    {
        if (vec.x > this.minX && vec.x < this.maxX)
        {
            if (vec.y > this.minY && vec.y < this.maxY)
            {
                return vec.z > this.minZ && vec.z < this.maxZ;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns the average length of the edges of the bounding box.
     */
    public double getAverageEdgeLength()
    {
        double d0 = this.maxX - this.minX;
        double d1 = this.maxY - this.minY;
        double d2 = this.maxZ - this.minZ;
        return (d0 + d1 + d2) / 3.0D;
    }

    /**
     * Creates a new {@link AxisAlignedBB} that is expanded by the given value in all directions. Equivalent to {@link
     * #grow(double)} with value set to the negative of the value provided here. Passing a negative value to this method
     * values will grow the AABB.
     * <br/>
     * Side lengths will be decreased by 2 times the value of the parameter, since both min and max are changed.
     * <br/>
     * If contracting and the amount to contract by is larger than the length of a side, then the side will wrap (still
     * creating a valid AABB - see samples on {@link #grow(double, double, double)}).
     *  
     * @return A modified AABB.
     */
    public AxisAlignedBB shrink(double value)
    {
        return this.grow(-value);
    }

    @Nullable
    public RayTraceResult func_72327_a(Vec3d p_72327_1_, Vec3d p_72327_2_)
    {
        Vec3d vec3d = this.func_186671_a(this.minX, p_72327_1_, p_72327_2_);
        EnumFacing enumfacing = EnumFacing.WEST;
        Vec3d vec3d1 = this.func_186671_a(this.maxX, p_72327_1_, p_72327_2_);

        if (vec3d1 != null && this.func_186661_a(p_72327_1_, vec3d, vec3d1))
        {
            vec3d = vec3d1;
            enumfacing = EnumFacing.EAST;
        }

        vec3d1 = this.func_186663_b(this.minY, p_72327_1_, p_72327_2_);

        if (vec3d1 != null && this.func_186661_a(p_72327_1_, vec3d, vec3d1))
        {
            vec3d = vec3d1;
            enumfacing = EnumFacing.DOWN;
        }

        vec3d1 = this.func_186663_b(this.maxY, p_72327_1_, p_72327_2_);

        if (vec3d1 != null && this.func_186661_a(p_72327_1_, vec3d, vec3d1))
        {
            vec3d = vec3d1;
            enumfacing = EnumFacing.UP;
        }

        vec3d1 = this.func_186665_c(this.minZ, p_72327_1_, p_72327_2_);

        if (vec3d1 != null && this.func_186661_a(p_72327_1_, vec3d, vec3d1))
        {
            vec3d = vec3d1;
            enumfacing = EnumFacing.NORTH;
        }

        vec3d1 = this.func_186665_c(this.maxZ, p_72327_1_, p_72327_2_);

        if (vec3d1 != null && this.func_186661_a(p_72327_1_, vec3d, vec3d1))
        {
            vec3d = vec3d1;
            enumfacing = EnumFacing.SOUTH;
        }

        return vec3d == null ? null : new RayTraceResult(vec3d, enumfacing);
    }

    @VisibleForTesting
    boolean func_186661_a(Vec3d p_186661_1_, @Nullable Vec3d p_186661_2_, Vec3d p_186661_3_)
    {
        return p_186661_2_ == null || p_186661_1_.squareDistanceTo(p_186661_3_) < p_186661_1_.squareDistanceTo(p_186661_2_);
    }

    @Nullable
    @VisibleForTesting
    Vec3d func_186671_a(double p_186671_1_, Vec3d p_186671_3_, Vec3d p_186671_4_)
    {
        Vec3d vec3d = p_186671_3_.func_72429_b(p_186671_4_, p_186671_1_);
        return vec3d != null && this.func_186660_b(vec3d) ? vec3d : null;
    }

    @Nullable
    @VisibleForTesting
    Vec3d func_186663_b(double p_186663_1_, Vec3d p_186663_3_, Vec3d p_186663_4_)
    {
        Vec3d vec3d = p_186663_3_.func_72435_c(p_186663_4_, p_186663_1_);
        return vec3d != null && this.func_186667_c(vec3d) ? vec3d : null;
    }

    @Nullable
    @VisibleForTesting
    Vec3d func_186665_c(double p_186665_1_, Vec3d p_186665_3_, Vec3d p_186665_4_)
    {
        Vec3d vec3d = p_186665_3_.func_72434_d(p_186665_4_, p_186665_1_);
        return vec3d != null && this.func_186669_d(vec3d) ? vec3d : null;
    }

    @VisibleForTesting
    public boolean func_186660_b(Vec3d p_186660_1_)
    {
        return p_186660_1_.y >= this.minY && p_186660_1_.y <= this.maxY && p_186660_1_.z >= this.minZ && p_186660_1_.z <= this.maxZ;
    }

    @VisibleForTesting
    public boolean func_186667_c(Vec3d p_186667_1_)
    {
        return p_186667_1_.x >= this.minX && p_186667_1_.x <= this.maxX && p_186667_1_.z >= this.minZ && p_186667_1_.z <= this.maxZ;
    }

    @VisibleForTesting
    public boolean func_186669_d(Vec3d p_186669_1_)
    {
        return p_186669_1_.x >= this.minX && p_186669_1_.x <= this.maxX && p_186669_1_.y >= this.minY && p_186669_1_.y <= this.maxY;
    }

    public String toString()
    {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }

    public boolean hasNaN()
    {
        return Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ);
    }

    public Vec3d getCenter()
    {
        return new Vec3d(this.minX + (this.maxX - this.minX) * 0.5D, this.minY + (this.maxY - this.minY) * 0.5D, this.minZ + (this.maxZ - this.minZ) * 0.5D);
    }
}

package net.minecraft.pathfinding;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;

public class Path
{
    private final PathPoint[] points;
    private PathPoint[] openSet = new PathPoint[0];
    private PathPoint[] closedSet = new PathPoint[0];
    private PathPoint target;
    private int currentPathIndex;
    private int field_75883_c;

    public Path(PathPoint[] p_i2136_1_)
    {
        this.points = p_i2136_1_;
        this.field_75883_c = p_i2136_1_.length;
    }

    /**
     * Directs this path to the next point in its array
     */
    public void incrementPathIndex()
    {
        ++this.currentPathIndex;
    }

    /**
     * Returns true if this path has reached the end
     */
    public boolean isFinished()
    {
        return this.currentPathIndex >= this.field_75883_c;
    }

    @Nullable

    /**
     * returns the last PathPoint of the Array
     */
    public PathPoint getFinalPathPoint()
    {
        return this.field_75883_c > 0 ? this.points[this.field_75883_c - 1] : null;
    }

    /**
     * return the PathPoint located at the specified PathIndex, usually the current one
     */
    public PathPoint getPathPointFromIndex(int index)
    {
        return this.points[index];
    }

    public void setPoint(int index, PathPoint point)
    {
        this.points[index] = point;
    }

    public int getCurrentPathLength()
    {
        return this.field_75883_c;
    }

    public void func_75871_b(int p_75871_1_)
    {
        this.field_75883_c = p_75871_1_;
    }

    public int getCurrentPathIndex()
    {
        return this.currentPathIndex;
    }

    public void setCurrentPathIndex(int currentPathIndexIn)
    {
        this.currentPathIndex = currentPathIndexIn;
    }

    /**
     * Gets the vector of the PathPoint associated with the given index.
     */
    public Vec3d getVectorFromIndex(Entity entityIn, int index)
    {
        double d0 = (double)this.points[index].x + (double)((int)(entityIn.field_70130_N + 1.0F)) * 0.5D;
        double d1 = (double)this.points[index].y;
        double d2 = (double)this.points[index].z + (double)((int)(entityIn.field_70130_N + 1.0F)) * 0.5D;
        return new Vec3d(d0, d1, d2);
    }

    /**
     * returns the current PathEntity target node as Vec3D
     */
    public Vec3d getPosition(Entity entityIn)
    {
        return this.getVectorFromIndex(entityIn, this.currentPathIndex);
    }

    public Vec3d getCurrentPos()
    {
        PathPoint pathpoint = this.points[this.currentPathIndex];
        return new Vec3d((double)pathpoint.x, (double)pathpoint.y, (double)pathpoint.z);
    }

    /**
     * Returns true if the EntityPath are the same. Non instance related equals.
     */
    public boolean isSamePath(Path pathentityIn)
    {
        if (pathentityIn == null)
        {
            return false;
        }
        else if (pathentityIn.points.length != this.points.length)
        {
            return false;
        }
        else
        {
            for (int i = 0; i < this.points.length; ++i)
            {
                if (this.points[i].x != pathentityIn.points[i].x || this.points[i].y != pathentityIn.points[i].y || this.points[i].z != pathentityIn.points[i].z)
                {
                    return false;
                }
            }

            return true;
        }
    }

    public PathPoint[] getOpenSet()
    {
        return this.openSet;
    }

    public PathPoint[] getClosedSet()
    {
        return this.closedSet;
    }

    public PathPoint func_189964_i()
    {
        return this.target;
    }

    public static Path read(PacketBuffer buf)
    {
        int i = buf.readInt();
        PathPoint pathpoint = PathPoint.createFromBuffer(buf);
        PathPoint[] apathpoint = new PathPoint[buf.readInt()];

        for (int j = 0; j < apathpoint.length; ++j)
        {
            apathpoint[j] = PathPoint.createFromBuffer(buf);
        }

        PathPoint[] apathpoint1 = new PathPoint[buf.readInt()];

        for (int k = 0; k < apathpoint1.length; ++k)
        {
            apathpoint1[k] = PathPoint.createFromBuffer(buf);
        }

        PathPoint[] apathpoint2 = new PathPoint[buf.readInt()];

        for (int l = 0; l < apathpoint2.length; ++l)
        {
            apathpoint2[l] = PathPoint.createFromBuffer(buf);
        }

        Path path = new Path(apathpoint);
        path.openSet = apathpoint1;
        path.closedSet = apathpoint2;
        path.target = pathpoint;
        path.currentPathIndex = i;
        return path;
    }
}

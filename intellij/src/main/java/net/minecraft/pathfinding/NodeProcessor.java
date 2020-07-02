package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public abstract class NodeProcessor
{
    protected IBlockAccess blockaccess;
    protected EntityLiving entity;
    protected final IntHashMap<PathPoint> pointMap = new IntHashMap<PathPoint>();
    protected int entitySizeX;
    protected int entitySizeY;
    protected int entitySizeZ;
    protected boolean canEnterDoors;
    protected boolean canOpenDoors;
    protected boolean canSwim;

    public void func_186315_a(IBlockAccess p_186315_1_, EntityLiving p_186315_2_)
    {
        this.blockaccess = p_186315_1_;
        this.entity = p_186315_2_;
        this.pointMap.func_76046_c();
        this.entitySizeX = MathHelper.floor(p_186315_2_.field_70130_N + 1.0F);
        this.entitySizeY = MathHelper.floor(p_186315_2_.field_70131_O + 1.0F);
        this.entitySizeZ = MathHelper.floor(p_186315_2_.field_70130_N + 1.0F);
    }

    /**
     * This method is called when all nodes have been processed and PathEntity is created.
     *  {@link net.minecraft.world.pathfinder.WalkNodeProcessor WalkNodeProcessor} uses this to change its field {@link
     * net.minecraft.world.pathfinder.WalkNodeProcessor#avoidsWater avoidsWater}
     */
    public void postProcess()
    {
        this.blockaccess = null;
        this.entity = null;
    }

    /**
     * Returns a mapped point or creates and adds one
     */
    protected PathPoint openPoint(int x, int y, int z)
    {
        int i = PathPoint.makeHash(x, y, z);
        PathPoint pathpoint = this.pointMap.func_76041_a(i);

        if (pathpoint == null)
        {
            pathpoint = new PathPoint(x, y, z);
            this.pointMap.func_76038_a(i, pathpoint);
        }

        return pathpoint;
    }

    public abstract PathPoint getStart();

    public abstract PathPoint func_186325_a(double p_186325_1_, double p_186325_3_, double p_186325_5_);

    public abstract int func_186320_a(PathPoint[] p_186320_1_, PathPoint p_186320_2_, PathPoint p_186320_3_, float p_186320_4_);

    public abstract PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z, EntityLiving entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn);

    public abstract PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z);

    public void setCanEnterDoors(boolean canEnterDoorsIn)
    {
        this.canEnterDoors = canEnterDoorsIn;
    }

    public void setCanOpenDoors(boolean canOpenDoorsIn)
    {
        this.canOpenDoors = canOpenDoorsIn;
    }

    public void setCanSwim(boolean canSwimIn)
    {
        this.canSwim = canSwimIn;
    }

    public boolean getCanEnterDoors()
    {
        return this.canEnterDoors;
    }

    public boolean getCanOpenDoors()
    {
        return this.canOpenDoors;
    }

    public boolean getCanSwim()
    {
        return this.canSwim;
    }
}

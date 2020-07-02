package net.minecraft.village;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;

public class VillageSiege
{
    private final World field_75537_a;
    private boolean hasSetupSiege;
    private int siegeState = -1;
    private int siegeCount;
    private int nextSpawnTime;
    private Village field_75531_f;
    private int spawnX;
    private int spawnY;
    private int spawnZ;

    public VillageSiege(World p_i1676_1_)
    {
        this.field_75537_a = p_i1676_1_;
    }

    public void func_75528_a()
    {
        if (this.field_75537_a.isDaytime())
        {
            this.siegeState = 0;
        }
        else if (this.siegeState != 2)
        {
            if (this.siegeState == 0)
            {
                float f = this.field_75537_a.getCelestialAngle(0.0F);

                if ((double)f < 0.5D || (double)f > 0.501D)
                {
                    return;
                }

                this.siegeState = this.field_75537_a.rand.nextInt(10) == 0 ? 1 : 2;
                this.hasSetupSiege = false;

                if (this.siegeState == 2)
                {
                    return;
                }
            }

            if (this.siegeState != -1)
            {
                if (!this.hasSetupSiege)
                {
                    if (!this.trySetupSiege())
                    {
                        return;
                    }

                    this.hasSetupSiege = true;
                }

                if (this.nextSpawnTime > 0)
                {
                    --this.nextSpawnTime;
                }
                else
                {
                    this.nextSpawnTime = 2;

                    if (this.siegeCount > 0)
                    {
                        this.spawnZombie();
                        --this.siegeCount;
                    }
                    else
                    {
                        this.siegeState = 2;
                    }
                }
            }
        }
    }

    private boolean trySetupSiege()
    {
        List<EntityPlayer> list = this.field_75537_a.field_73010_i;
        Iterator iterator = list.iterator();

        while (true)
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            EntityPlayer entityplayer = (EntityPlayer)iterator.next();

            if (!entityplayer.isSpectator())
            {
                this.field_75531_f = this.field_75537_a.func_175714_ae().func_176056_a(new BlockPos(entityplayer), 1);

                if (this.field_75531_f != null && this.field_75531_f.func_75567_c() >= 10 && this.field_75531_f.func_75561_d() >= 20 && this.field_75531_f.func_75562_e() >= 20)
                {
                    BlockPos blockpos = this.field_75531_f.func_180608_a();
                    float f = (float)this.field_75531_f.func_75568_b();
                    boolean flag = false;

                    for (int i = 0; i < 10; ++i)
                    {
                        float f1 = this.field_75537_a.rand.nextFloat() * ((float)Math.PI * 2F);
                        this.spawnX = blockpos.getX() + (int)((double)(MathHelper.cos(f1) * f) * 0.9D);
                        this.spawnY = blockpos.getY();
                        this.spawnZ = blockpos.getZ() + (int)((double)(MathHelper.sin(f1) * f) * 0.9D);
                        flag = false;

                        for (Village village : this.field_75537_a.func_175714_ae().func_75540_b())
                        {
                            if (village != this.field_75531_f && village.func_179866_a(new BlockPos(this.spawnX, this.spawnY, this.spawnZ)))
                            {
                                flag = true;
                                break;
                            }
                        }

                        if (!flag)
                        {
                            break;
                        }
                    }

                    if (flag)
                    {
                        return false;
                    }

                    Vec3d vec3d = this.func_179867_a(new BlockPos(this.spawnX, this.spawnY, this.spawnZ));

                    if (vec3d != null)
                    {
                        break;
                    }
                }
            }
        }

        this.nextSpawnTime = 0;
        this.siegeCount = 20;
        return true;
    }

    private boolean spawnZombie()
    {
        Vec3d vec3d = this.func_179867_a(new BlockPos(this.spawnX, this.spawnY, this.spawnZ));

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            EntityZombie entityzombie;

            try
            {
                entityzombie = new EntityZombie(this.field_75537_a);
                entityzombie.func_180482_a(this.field_75537_a.getDifficultyForLocation(new BlockPos(entityzombie)), (IEntityLivingData)null);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
                return false;
            }

            entityzombie.setLocationAndAngles(vec3d.x, vec3d.y, vec3d.z, this.field_75537_a.rand.nextFloat() * 360.0F, 0.0F);
            this.field_75537_a.addEntity0(entityzombie);
            BlockPos blockpos = this.field_75531_f.func_180608_a();
            entityzombie.func_175449_a(blockpos, this.field_75531_f.func_75568_b());
            return true;
        }
    }

    @Nullable
    private Vec3d func_179867_a(BlockPos p_179867_1_)
    {
        for (int i = 0; i < 10; ++i)
        {
            BlockPos blockpos = p_179867_1_.add(this.field_75537_a.rand.nextInt(16) - 8, this.field_75537_a.rand.nextInt(6) - 3, this.field_75537_a.rand.nextInt(16) - 8);

            if (this.field_75531_f.func_179866_a(blockpos) && WorldEntitySpawner.func_180267_a(EntityLiving.SpawnPlacementType.ON_GROUND, this.field_75537_a, blockpos))
            {
                return new Vec3d((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
            }
        }

        return null;
    }
}

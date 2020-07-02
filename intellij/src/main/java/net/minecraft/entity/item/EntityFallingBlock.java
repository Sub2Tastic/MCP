package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFallingBlock extends Entity
{
    private IBlockState fallTile;
    public int fallTime;
    public boolean shouldDropItem = true;
    private boolean dontSetBlock;
    private boolean hurtEntities;
    private int fallHurtMax = 40;
    private float fallHurtAmount = 2.0F;
    public NBTTagCompound tileEntityData;
    protected static final DataParameter<BlockPos> ORIGIN = EntityDataManager.<BlockPos>createKey(EntityFallingBlock.class, DataSerializers.BLOCK_POS);

    public EntityFallingBlock(World p_i1706_1_)
    {
        super(p_i1706_1_);
    }

    public EntityFallingBlock(World worldIn, double x, double y, double z, IBlockState fallingBlockState)
    {
        super(worldIn);
        this.fallTile = fallingBlockState;
        this.preventEntitySpawning = true;
        this.func_70105_a(0.98F, 0.98F);
        this.setPosition(x, y + (double)((1.0F - this.field_70131_O) / 2.0F), z);
        this.field_70159_w = 0.0D;
        this.field_70181_x = 0.0D;
        this.field_70179_y = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.setOrigin(new BlockPos(this));
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return false;
    }

    public void setOrigin(BlockPos p_184530_1_)
    {
        this.dataManager.set(ORIGIN, p_184530_1_);
    }

    public BlockPos getOrigin()
    {
        return (BlockPos)this.dataManager.get(ORIGIN);
    }

    protected boolean func_70041_e_()
    {
        return false;
    }

    protected void registerData()
    {
        this.dataManager.register(ORIGIN, BlockPos.ZERO);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.removed;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        Block block = this.fallTile.getBlock();

        if (this.fallTile.getMaterial() == Material.AIR)
        {
            this.remove();
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            if (this.fallTime++ == 0)
            {
                BlockPos blockpos = new BlockPos(this);

                if (this.world.getBlockState(blockpos).getBlock() == block)
                {
                    this.world.func_175698_g(blockpos);
                }
                else if (!this.world.isRemote)
                {
                    this.remove();
                    return;
                }
            }

            if (!this.hasNoGravity())
            {
                this.field_70181_x -= 0.03999999910593033D;
            }

            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);

            if (!this.world.isRemote)
            {
                BlockPos blockpos1 = new BlockPos(this);
                boolean flag = this.fallTile.getBlock() == Blocks.field_192444_dS;
                boolean flag1 = flag && this.world.getBlockState(blockpos1).getMaterial() == Material.WATER;
                double d0 = this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y;

                if (flag && d0 > 1.0D)
                {
                    RayTraceResult raytraceresult = this.world.func_72901_a(new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ), new Vec3d(this.posX, this.posY, this.posZ), true);

                    if (raytraceresult != null && this.world.getBlockState(raytraceresult.func_178782_a()).getMaterial() == Material.WATER)
                    {
                        blockpos1 = raytraceresult.func_178782_a();
                        flag1 = true;
                    }
                }

                if (!this.onGround && !flag1)
                {
                    if (this.fallTime > 100 && !this.world.isRemote && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.fallTime > 600)
                    {
                        if (this.shouldDropItem && this.world.getGameRules().func_82766_b("doEntityDrops"))
                        {
                            this.entityDropItem(new ItemStack(block, 1, block.func_180651_a(this.fallTile)), 0.0F);
                        }

                        this.remove();
                    }
                }
                else
                {
                    IBlockState iblockstate = this.world.getBlockState(blockpos1);

                    if (!flag1 && BlockFalling.canFallThrough(this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ))))
                    {
                        this.onGround = false;
                        return;
                    }

                    this.field_70159_w *= 0.699999988079071D;
                    this.field_70179_y *= 0.699999988079071D;
                    this.field_70181_x *= -0.5D;

                    if (iblockstate.getBlock() != Blocks.field_180384_M)
                    {
                        this.remove();

                        if (!this.dontSetBlock)
                        {
                            if (this.world.func_190527_a(block, blockpos1, true, EnumFacing.UP, (Entity)null) && (flag1 || !BlockFalling.canFallThrough(this.world.getBlockState(blockpos1.down()))) && this.world.setBlockState(blockpos1, this.fallTile, 3))
                            {
                                if (block instanceof BlockFalling)
                                {
                                    ((BlockFalling)block).onEndFalling(this.world, blockpos1, this.fallTile, iblockstate);
                                }

                                if (this.tileEntityData != null && block instanceof ITileEntityProvider)
                                {
                                    TileEntity tileentity = this.world.getTileEntity(blockpos1);

                                    if (tileentity != null)
                                    {
                                        NBTTagCompound nbttagcompound = tileentity.write(new NBTTagCompound());

                                        for (String s : this.tileEntityData.keySet())
                                        {
                                            NBTBase nbtbase = this.tileEntityData.get(s);

                                            if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s))
                                            {
                                                nbttagcompound.func_74782_a(s, nbtbase.copy());
                                            }
                                        }

                                        tileentity.read(nbttagcompound);
                                        tileentity.markDirty();
                                    }
                                }
                            }
                            else if (this.shouldDropItem && this.world.getGameRules().func_82766_b("doEntityDrops"))
                            {
                                this.entityDropItem(new ItemStack(block, 1, block.func_180651_a(this.fallTile)), 0.0F);
                            }
                        }
                        else if (block instanceof BlockFalling)
                        {
                            ((BlockFalling)block).onBroken(this.world, blockpos1);
                        }
                    }
                }
            }

            this.field_70159_w *= 0.9800000190734863D;
            this.field_70181_x *= 0.9800000190734863D;
            this.field_70179_y *= 0.9800000190734863D;
        }
    }

    public void func_180430_e(float p_180430_1_, float p_180430_2_)
    {
        Block block = this.fallTile.getBlock();

        if (this.hurtEntities)
        {
            int i = MathHelper.ceil(p_180430_1_ - 1.0F);

            if (i > 0)
            {
                List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox()));
                boolean flag = block == Blocks.ANVIL;
                DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;

                for (Entity entity : list)
                {
                    entity.attackEntityFrom(damagesource, (float)Math.min(MathHelper.floor((float)i * this.fallHurtAmount), this.fallHurtMax));
                }

                if (flag && (double)this.rand.nextFloat() < 0.05000000074505806D + (double)i * 0.05D)
                {
                    int j = ((Integer)this.fallTile.get(BlockAnvil.field_176505_b)).intValue();
                    ++j;

                    if (j > 2)
                    {
                        this.dontSetBlock = true;
                    }
                    else
                    {
                        this.fallTile = this.fallTile.func_177226_a(BlockAnvil.field_176505_b, Integer.valueOf(j));
                    }
                }
            }
        }
    }

    public static void func_189741_a(DataFixer p_189741_0_)
    {
    }

    protected void func_70014_b(NBTTagCompound p_70014_1_)
    {
        Block block = this.fallTile != null ? this.fallTile.getBlock() : Blocks.AIR;
        ResourceLocation resourcelocation = Block.field_149771_c.getKey(block);
        p_70014_1_.putString("Block", resourcelocation == null ? "" : resourcelocation.toString());
        p_70014_1_.putByte("Data", (byte)block.func_176201_c(this.fallTile));
        p_70014_1_.putInt("Time", this.fallTime);
        p_70014_1_.putBoolean("DropItem", this.shouldDropItem);
        p_70014_1_.putBoolean("HurtEntities", this.hurtEntities);
        p_70014_1_.putFloat("FallHurtAmount", this.fallHurtAmount);
        p_70014_1_.putInt("FallHurtMax", this.fallHurtMax);

        if (this.tileEntityData != null)
        {
            p_70014_1_.func_74782_a("TileEntityData", this.tileEntityData);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(NBTTagCompound compound)
    {
        int i = compound.getByte("Data") & 255;

        if (compound.contains("Block", 8))
        {
            this.fallTile = Block.func_149684_b(compound.getString("Block")).func_176203_a(i);
        }
        else if (compound.contains("TileID", 99))
        {
            this.fallTile = Block.func_149729_e(compound.getInt("TileID")).func_176203_a(i);
        }
        else
        {
            this.fallTile = Block.func_149729_e(compound.getByte("Tile") & 255).func_176203_a(i);
        }

        this.fallTime = compound.getInt("Time");
        Block block = this.fallTile.getBlock();

        if (compound.contains("HurtEntities", 99))
        {
            this.hurtEntities = compound.getBoolean("HurtEntities");
            this.fallHurtAmount = compound.getFloat("FallHurtAmount");
            this.fallHurtMax = compound.getInt("FallHurtMax");
        }
        else if (block == Blocks.ANVIL)
        {
            this.hurtEntities = true;
        }

        if (compound.contains("DropItem", 99))
        {
            this.shouldDropItem = compound.getBoolean("DropItem");
        }

        if (compound.contains("TileEntityData", 10))
        {
            this.tileEntityData = compound.getCompound("TileEntityData");
        }

        if (block == null || block.getDefaultState().getMaterial() == Material.AIR)
        {
            this.fallTile = Blocks.SAND.getDefaultState();
        }
    }

    public World getWorldObj()
    {
        return this.world;
    }

    public void setHurtEntities(boolean hurtEntitiesIn)
    {
        this.hurtEntities = hurtEntitiesIn;
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    public boolean canRenderOnFire()
    {
        return false;
    }

    public void fillCrashReport(CrashReportCategory category)
    {
        super.fillCrashReport(category);

        if (this.fallTile != null)
        {
            Block block = this.fallTile.getBlock();
            category.addDetail("Immitating block ID", Integer.valueOf(Block.func_149682_b(block)));
            category.addDetail("Immitating block data", Integer.valueOf(block.func_176201_c(this.fallTile)));
        }
    }

    @Nullable
    public IBlockState func_175131_l()
    {
        return this.fallTile;
    }

    /**
     * Checks if players can use this entity to access operator (permission level 2) commands either directly or
     * indirectly, such as give or setblock. A similar method exists for entities at {@link
     * net.minecraft.tileentity.TileEntity#onlyOpsCanSetNbt()}.<p>For example, {@link
     * net.minecraft.entity.item.EntityMinecartCommandBlock#ignoreItemEntityData() command block minecarts} and {@link
     * net.minecraft.entity.item.EntityMinecartMobSpawner#ignoreItemEntityData() mob spawner minecarts} (spawning
     * command block minecarts or drops) are considered accessible.</p>@return true if this entity offers ways for
     * unauthorized players to use restricted commands
     */
    public boolean ignoreItemEntityData()
    {
        return true;
    }
}

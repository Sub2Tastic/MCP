package net.minecraft.entity.item;

import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityMinecartFurnace extends EntityMinecart
{
    private static final DataParameter<Boolean> POWERED = EntityDataManager.<Boolean>createKey(EntityMinecartFurnace.class, DataSerializers.BOOLEAN);
    private int fuel;
    public double pushX;
    public double pushZ;

    public EntityMinecartFurnace(World p_i1718_1_)
    {
        super(p_i1718_1_);
    }

    public EntityMinecartFurnace(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static void func_189671_a(DataFixer p_189671_0_)
    {
        EntityMinecart.func_189669_a(p_189671_0_, EntityMinecartFurnace.class);
    }

    public EntityMinecart.Type getMinecartType()
    {
        return EntityMinecart.Type.FURNACE;
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(POWERED, Boolean.valueOf(false));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        super.tick();

        if (this.fuel > 0)
        {
            --this.fuel;
        }

        if (this.fuel <= 0)
        {
            this.pushX = 0.0D;
            this.pushZ = 0.0D;
        }

        this.setMinecartPowered(this.fuel > 0);

        if (this.isMinecartPowered() && this.rand.nextInt(4) == 0)
        {
            this.world.func_175688_a(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    /**
     * Get's the maximum speed for a minecart
     */
    protected double getMaximumSpeed()
    {
        return 0.2D;
    }

    public void killMinecart(DamageSource source)
    {
        super.killMinecart(source);

        if (!source.isExplosion() && this.world.getGameRules().func_82766_b("doEntityDrops"))
        {
            this.entityDropItem(new ItemStack(Blocks.FURNACE, 1), 0.0F);
        }
    }

    protected void moveAlongTrack(BlockPos pos, IBlockState state)
    {
        super.moveAlongTrack(pos, state);
        double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;

        if (d0 > 1.0E-4D && this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y > 0.001D)
        {
            d0 = (double)MathHelper.sqrt(d0);
            this.pushX /= d0;
            this.pushZ /= d0;

            if (this.pushX * this.field_70159_w + this.pushZ * this.field_70179_y < 0.0D)
            {
                this.pushX = 0.0D;
                this.pushZ = 0.0D;
            }
            else
            {
                double d1 = d0 / this.getMaximumSpeed();
                this.pushX *= d1;
                this.pushZ *= d1;
            }
        }
    }

    protected void applyDrag()
    {
        double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;

        if (d0 > 1.0E-4D)
        {
            d0 = (double)MathHelper.sqrt(d0);
            this.pushX /= d0;
            this.pushZ /= d0;
            double d1 = 1.0D;
            this.field_70159_w *= 0.800000011920929D;
            this.field_70181_x *= 0.0D;
            this.field_70179_y *= 0.800000011920929D;
            this.field_70159_w += this.pushX * 1.0D;
            this.field_70179_y += this.pushZ * 1.0D;
        }
        else
        {
            this.field_70159_w *= 0.9800000190734863D;
            this.field_70181_x *= 0.0D;
            this.field_70179_y *= 0.9800000190734863D;
        }

        super.applyDrag();
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() == Items.COAL && this.fuel + 3600 <= 32000)
        {
            if (!player.abilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }

            this.fuel += 3600;
        }

        this.pushX = this.posX - player.posX;
        this.pushZ = this.posZ - player.posZ;
        return true;
    }

    protected void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.putDouble("PushX", this.pushX);
        p_70014_1_.putDouble("PushZ", this.pushZ);
        p_70014_1_.putShort("Fuel", (short)this.fuel);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        this.pushX = compound.getDouble("PushX");
        this.pushZ = compound.getDouble("PushZ");
        this.fuel = compound.getShort("Fuel");
    }

    protected boolean isMinecartPowered()
    {
        return ((Boolean)this.dataManager.get(POWERED)).booleanValue();
    }

    protected void setMinecartPowered(boolean p_94107_1_)
    {
        this.dataManager.set(POWERED, Boolean.valueOf(p_94107_1_));
    }

    public IBlockState getDefaultDisplayTile()
    {
        return (this.isMinecartPowered() ? Blocks.field_150470_am : Blocks.FURNACE).getDefaultState().func_177226_a(BlockFurnace.field_176447_a, EnumFacing.NORTH);
    }
}

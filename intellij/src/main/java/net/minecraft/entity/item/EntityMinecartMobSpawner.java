package net.minecraft.entity.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartMobSpawner extends EntityMinecart
{
    private final MobSpawnerBaseLogic mobSpawnerLogic = new MobSpawnerBaseLogic()
    {
        public void broadcastEvent(int id)
        {
            EntityMinecartMobSpawner.this.world.setEntityState(EntityMinecartMobSpawner.this, (byte)id);
        }
        public World getWorld()
        {
            return EntityMinecartMobSpawner.this.world;
        }
        public BlockPos getSpawnerPosition()
        {
            return new BlockPos(EntityMinecartMobSpawner.this);
        }
    };

    public EntityMinecartMobSpawner(World p_i46752_1_)
    {
        super(p_i46752_1_);
    }

    public EntityMinecartMobSpawner(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static void func_189672_a(DataFixer p_189672_0_)
    {
        func_189669_a(p_189672_0_, EntityMinecartMobSpawner.class);
        p_189672_0_.func_188258_a(FixTypes.ENTITY, new IDataWalker()
        {
            public NBTTagCompound func_188266_a(IDataFixer p_188266_1_, NBTTagCompound p_188266_2_, int p_188266_3_)
            {
                String s = p_188266_2_.getString("id");

                if (EntityList.func_191306_a(EntityMinecartMobSpawner.class).equals(new ResourceLocation(s)))
                {
                    p_188266_2_.putString("id", TileEntity.func_190559_a(TileEntityMobSpawner.class).toString());
                    p_188266_1_.func_188251_a(FixTypes.BLOCK_ENTITY, p_188266_2_, p_188266_3_);
                    p_188266_2_.putString("id", s);
                }

                return p_188266_2_;
            }
        });
    }

    public EntityMinecart.Type getMinecartType()
    {
        return EntityMinecart.Type.SPAWNER;
    }

    public IBlockState getDefaultDisplayTile()
    {
        return Blocks.SPAWNER.getDefaultState();
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        this.mobSpawnerLogic.read(compound);
    }

    protected void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        this.mobSpawnerLogic.write(p_70014_1_);
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    public void handleStatusUpdate(byte id)
    {
        this.mobSpawnerLogic.setDelayToMin(id);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        super.tick();
        this.mobSpawnerLogic.tick();
    }
}

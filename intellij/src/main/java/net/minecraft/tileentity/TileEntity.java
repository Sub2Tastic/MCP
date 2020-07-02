package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TileEntity
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final RegistryNamespaced < ResourceLocation, Class <? extends TileEntity >> field_190562_f = new RegistryNamespaced < ResourceLocation, Class <? extends TileEntity >> ();

    /** the instance of the world the tile entity is in. */
    protected World world;
    protected BlockPos pos = BlockPos.ZERO;
    protected boolean removed;
    private int field_145847_g = -1;
    protected Block field_145854_h;

    private static void func_190560_a(String p_190560_0_, Class <? extends TileEntity > p_190560_1_)
    {
        field_190562_f.func_82595_a(new ResourceLocation(p_190560_0_), p_190560_1_);
    }

    @Nullable
    public static ResourceLocation func_190559_a(Class <? extends TileEntity > p_190559_0_)
    {
        return field_190562_f.getKey(p_190559_0_);
    }

    /**
     * Returns the worldObj for this tileEntity.
     */
    public World getWorld()
    {
        return this.world;
    }

    public void func_145834_a(World p_145834_1_)
    {
        this.world = p_145834_1_;
    }

    /**
     * Returns true if the worldObj isn't null.
     */
    public boolean hasWorld()
    {
        return this.world != null;
    }

    public void read(NBTTagCompound compound)
    {
        this.pos = new BlockPos(compound.getInt("x"), compound.getInt("y"), compound.getInt("z"));
    }

    public NBTTagCompound write(NBTTagCompound compound)
    {
        return this.writeInternal(compound);
    }

    private NBTTagCompound writeInternal(NBTTagCompound compound)
    {
        ResourceLocation resourcelocation = field_190562_f.getKey(this.getClass());

        if (resourcelocation == null)
        {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        }
        else
        {
            compound.putString("id", resourcelocation.toString());
            compound.putInt("x", this.pos.getX());
            compound.putInt("y", this.pos.getY());
            compound.putInt("z", this.pos.getZ());
            return compound;
        }
    }

    @Nullable
    public static TileEntity func_190200_a(World p_190200_0_, NBTTagCompound p_190200_1_)
    {
        TileEntity tileentity = null;
        String s = p_190200_1_.getString("id");

        try
        {
            Class <? extends TileEntity > oclass = (Class)field_190562_f.getOrDefault(new ResourceLocation(s));

            if (oclass != null)
            {
                tileentity = oclass.newInstance();
            }
        }
        catch (Throwable throwable1)
        {
            LOGGER.error("Failed to create block entity {}", s, throwable1);
        }

        if (tileentity != null)
        {
            try
            {
                tileentity.func_190201_b(p_190200_0_);
                tileentity.read(p_190200_1_);
            }
            catch (Throwable throwable)
            {
                LOGGER.error("Failed to load data for block entity {}", s, throwable);
                tileentity = null;
            }
        }
        else
        {
            LOGGER.warn("Skipping BlockEntity with id {}", (Object)s);
        }

        return tileentity;
    }

    protected void func_190201_b(World p_190201_1_)
    {
    }

    public int func_145832_p()
    {
        if (this.field_145847_g == -1)
        {
            IBlockState iblockstate = this.world.getBlockState(this.pos);
            this.field_145847_g = iblockstate.getBlock().func_176201_c(iblockstate);
        }

        return this.field_145847_g;
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty()
    {
        if (this.world != null)
        {
            IBlockState iblockstate = this.world.getBlockState(this.pos);
            this.field_145847_g = iblockstate.getBlock().func_176201_c(iblockstate);
            this.world.markChunkDirty(this.pos, this);

            if (this.func_145838_q() != Blocks.AIR)
            {
                this.world.updateComparatorOutputLevel(this.pos, this.func_145838_q());
            }
        }
    }

    /**
     * Returns the square of the distance between this entity and the passed in coordinates.
     */
    public double getDistanceSq(double x, double y, double z)
    {
        double d0 = (double)this.pos.getX() + 0.5D - x;
        double d1 = (double)this.pos.getY() + 0.5D - y;
        double d2 = (double)this.pos.getZ() + 0.5D - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double getMaxRenderDistanceSquared()
    {
        return 4096.0D;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }

    public Block func_145838_q()
    {
        if (this.field_145854_h == null && this.world != null)
        {
            this.field_145854_h = this.world.getBlockState(this.pos).getBlock();
        }

        return this.field_145854_h;
    }

    @Nullable

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return null;
    }

    /**
     * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
     * many blocks change at once. This compound comes back to you clientside in {@link handleUpdateTag}
     */
    public NBTTagCompound getUpdateTag()
    {
        return this.writeInternal(new NBTTagCompound());
    }

    public boolean isRemoved()
    {
        return this.removed;
    }

    /**
     * invalidates a tile entity
     */
    public void remove()
    {
        this.removed = true;
    }

    /**
     * validates a tile entity
     */
    public void validate()
    {
        this.removed = false;
    }

    /**
     * See {@link Block#eventReceived} for more information. This must return true serverside before it is called
     * clientside.
     */
    public boolean receiveClientEvent(int id, int type)
    {
        return false;
    }

    public void updateContainingBlockInfo()
    {
        this.field_145854_h = null;
        this.field_145847_g = -1;
    }

    public void addInfoToCrashReport(CrashReportCategory reportCategory)
    {
        reportCategory.addDetail("Name", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return TileEntity.field_190562_f.getKey(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
            }
        });

        if (this.world != null)
        {
            CrashReportCategory.func_180523_a(reportCategory, this.pos, this.func_145838_q(), this.func_145832_p());
            reportCategory.addDetail("Actual block type", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    int i = Block.func_149682_b(TileEntity.this.world.getBlockState(TileEntity.this.pos).getBlock());

                    try
                    {
                        return String.format("ID #%d (%s // %s)", i, Block.func_149729_e(i).getTranslationKey(), Block.func_149729_e(i).getClass().getCanonicalName());
                    }
                    catch (Throwable var3)
                    {
                        return "ID #" + i;
                    }
                }
            });
            reportCategory.addDetail("Actual block data value", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    IBlockState iblockstate = TileEntity.this.world.getBlockState(TileEntity.this.pos);
                    int i = iblockstate.getBlock().func_176201_c(iblockstate);

                    if (i < 0)
                    {
                        return "Unknown? (Got " + i + ")";
                    }
                    else
                    {
                        String s = String.format("%4s", Integer.toBinaryString(i)).replace(" ", "0");
                        return String.format("%1$d / 0x%1$X / 0b%2$s", i, s);
                    }
                }
            });
        }
    }

    public void setPos(BlockPos posIn)
    {
        this.pos = posIn.toImmutable();
    }

    /**
     * Checks if players can use this tile entity to access operator (permission level 2) commands either directly or
     * indirectly, such as give or setblock. A similar method exists for entities at {@link
     * net.minecraft.entity.Entity#ignoreItemEntityData()}.<p>For example, {@link
     * net.minecraft.tileentity.TileEntitySign#onlyOpsCanSetNbt() signs} (player right-clicking) and {@link
     * net.minecraft.tileentity.TileEntityCommandBlock#onlyOpsCanSetNbt() command blocks} are considered
     * accessible.</p>@return true if this block entity offers ways for unauthorized players to use restricted commands
     */
    public boolean onlyOpsCanSetNbt()
    {
        return false;
    }

    @Nullable
    public ITextComponent getDisplayName()
    {
        return null;
    }

    public void rotate(Rotation rotationIn)
    {
    }

    public void mirror(Mirror mirrorIn)
    {
    }

    static
    {
        func_190560_a("furnace", TileEntityFurnace.class);
        func_190560_a("chest", TileEntityChest.class);
        func_190560_a("ender_chest", TileEntityEnderChest.class);
        func_190560_a("jukebox", BlockJukebox.TileEntityJukebox.class);
        func_190560_a("dispenser", TileEntityDispenser.class);
        func_190560_a("dropper", TileEntityDropper.class);
        func_190560_a("sign", TileEntitySign.class);
        func_190560_a("mob_spawner", TileEntityMobSpawner.class);
        func_190560_a("noteblock", TileEntityNote.class);
        func_190560_a("piston", TileEntityPiston.class);
        func_190560_a("brewing_stand", TileEntityBrewingStand.class);
        func_190560_a("enchanting_table", TileEntityEnchantmentTable.class);
        func_190560_a("end_portal", TileEntityEndPortal.class);
        func_190560_a("beacon", TileEntityBeacon.class);
        func_190560_a("skull", TileEntitySkull.class);
        func_190560_a("daylight_detector", TileEntityDaylightDetector.class);
        func_190560_a("hopper", TileEntityHopper.class);
        func_190560_a("comparator", TileEntityComparator.class);
        func_190560_a("flower_pot", TileEntityFlowerPot.class);
        func_190560_a("banner", TileEntityBanner.class);
        func_190560_a("structure_block", TileEntityStructure.class);
        func_190560_a("end_gateway", TileEntityEndGateway.class);
        func_190560_a("command_block", TileEntityCommandBlock.class);
        func_190560_a("shulker_box", TileEntityShulkerBox.class);
        func_190560_a("bed", TileEntityBed.class);
    }
}

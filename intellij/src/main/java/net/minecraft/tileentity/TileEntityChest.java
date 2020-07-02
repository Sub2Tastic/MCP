package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityChest extends TileEntityLockableLoot implements ITickable
{
    private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
    public boolean field_145984_a;
    public TileEntityChest field_145992_i;
    public TileEntityChest field_145990_j;
    public TileEntityChest field_145991_k;
    public TileEntityChest field_145988_l;

    /** The current angle of the lid (between 0 and 1) */
    public float lidAngle;

    /** The angle of the lid last tick */
    public float prevLidAngle;

    /** The number of players currently using this chest */
    public int numPlayersUsing;

    /**
     * A counter that is incremented once each tick. Used to determine when to recompute ; this is done every 200 ticks
     * (but staggered between different chests). However, the new value isn't actually sent to clients when it is
     * changed.
     */
    private int ticksSinceSync;
    private BlockChest.Type field_145982_r;

    public TileEntityChest()
    {
    }

    public TileEntityChest(BlockChest.Type p_i46677_1_)
    {
        this.field_145982_r = p_i46677_1_;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 27;
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.chestContents)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public String func_70005_c_()
    {
        return this.hasCustomName() ? this.field_190577_o : "container.chest";
    }

    public static void func_189677_a(DataFixer p_189677_0_)
    {
        p_189677_0_.func_188258_a(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityChest.class, new String[] {"Items"}));
    }

    public void read(NBTTagCompound compound)
    {
        super.read(compound);
        this.chestContents = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound))
        {
            ItemStackHelper.loadAllItems(compound, this.chestContents);
        }

        if (compound.contains("CustomName", 8))
        {
            this.field_190577_o = compound.getString("CustomName");
        }
    }

    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);

        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.chestContents);
        }

        if (this.hasCustomName())
        {
            compound.putString("CustomName", this.field_190577_o);
        }

        return compound;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
        this.field_145984_a = false;
    }

    @SuppressWarnings("incomplete-switch")
    private void func_174910_a(TileEntityChest p_174910_1_, EnumFacing p_174910_2_)
    {
        if (p_174910_1_.isRemoved())
        {
            this.field_145984_a = false;
        }
        else if (this.field_145984_a)
        {
            switch (p_174910_2_)
            {
                case NORTH:
                    if (this.field_145992_i != p_174910_1_)
                    {
                        this.field_145984_a = false;
                    }

                    break;

                case SOUTH:
                    if (this.field_145988_l != p_174910_1_)
                    {
                        this.field_145984_a = false;
                    }

                    break;

                case EAST:
                    if (this.field_145990_j != p_174910_1_)
                    {
                        this.field_145984_a = false;
                    }

                    break;

                case WEST:
                    if (this.field_145991_k != p_174910_1_)
                    {
                        this.field_145984_a = false;
                    }
            }
        }
    }

    public void func_145979_i()
    {
        if (!this.field_145984_a)
        {
            this.field_145984_a = true;
            this.field_145991_k = this.func_174911_a(EnumFacing.WEST);
            this.field_145990_j = this.func_174911_a(EnumFacing.EAST);
            this.field_145992_i = this.func_174911_a(EnumFacing.NORTH);
            this.field_145988_l = this.func_174911_a(EnumFacing.SOUTH);
        }
    }

    @Nullable
    protected TileEntityChest func_174911_a(EnumFacing p_174911_1_)
    {
        BlockPos blockpos = this.pos.offset(p_174911_1_);

        if (this.func_174912_b(blockpos))
        {
            TileEntity tileentity = this.world.getTileEntity(blockpos);

            if (tileentity instanceof TileEntityChest)
            {
                TileEntityChest tileentitychest = (TileEntityChest)tileentity;
                tileentitychest.func_174910_a(this, p_174911_1_.getOpposite());
                return tileentitychest;
            }
        }

        return null;
    }

    private boolean func_174912_b(BlockPos p_174912_1_)
    {
        if (this.world == null)
        {
            return false;
        }
        else
        {
            Block block = this.world.getBlockState(p_174912_1_).getBlock();
            return block instanceof BlockChest && ((BlockChest)block).field_149956_a == this.func_145980_j();
        }
    }

    public void tick()
    {
        this.func_145979_i();
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;

        if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0)
        {
            this.numPlayersUsing = 0;
            float f = 5.0F;

            for (EntityPlayer entityplayer : this.world.func_72872_a(EntityPlayer.class, new AxisAlignedBB((double)((float)i - 5.0F), (double)((float)j - 5.0F), (double)((float)k - 5.0F), (double)((float)(i + 1) + 5.0F), (double)((float)(j + 1) + 5.0F), (double)((float)(k + 1) + 5.0F))))
            {
                if (entityplayer.openContainer instanceof ContainerChest)
                {
                    IInventory iinventory = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory();

                    if (iinventory == this || iinventory instanceof InventoryLargeChest && ((InventoryLargeChest)iinventory).isPartOfLargeChest(this))
                    {
                        ++this.numPlayersUsing;
                    }
                }
            }
        }

        this.prevLidAngle = this.lidAngle;
        float f1 = 0.1F;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.field_145992_i == null && this.field_145991_k == null)
        {
            double d1 = (double)i + 0.5D;
            double d2 = (double)k + 0.5D;

            if (this.field_145988_l != null)
            {
                d2 += 0.5D;
            }

            if (this.field_145990_j != null)
            {
                d1 += 0.5D;
            }

            this.world.playSound((EntityPlayer)null, d1, (double)j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float f2 = this.lidAngle;

            if (this.numPlayersUsing > 0)
            {
                this.lidAngle += 0.1F;
            }
            else
            {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float f3 = 0.5F;

            if (this.lidAngle < 0.5F && f2 >= 0.5F && this.field_145992_i == null && this.field_145991_k == null)
            {
                double d3 = (double)i + 0.5D;
                double d0 = (double)k + 0.5D;

                if (this.field_145988_l != null)
                {
                    d0 += 0.5D;
                }

                if (this.field_145990_j != null)
                {
                    d3 += 0.5D;
                }

                this.world.playSound((EntityPlayer)null, d3, (double)j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }
    }

    /**
     * See {@link Block#eventReceived} for more information. This must return true serverside before it is called
     * clientside.
     */
    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    public void openInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (this.numPlayersUsing < 0)
            {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.func_145838_q(), 1, this.numPlayersUsing);
            this.world.func_175685_c(this.pos, this.func_145838_q(), false);

            if (this.func_145980_j() == BlockChest.Type.TRAP)
            {
                this.world.func_175685_c(this.pos.down(), this.func_145838_q(), false);
            }
        }
    }

    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator() && this.func_145838_q() instanceof BlockChest)
        {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.func_145838_q(), 1, this.numPlayersUsing);
            this.world.func_175685_c(this.pos, this.func_145838_q(), false);

            if (this.func_145980_j() == BlockChest.Type.TRAP)
            {
                this.world.func_175685_c(this.pos.down(), this.func_145838_q(), false);
            }
        }
    }

    /**
     * invalidates a tile entity
     */
    public void remove()
    {
        super.remove();
        this.updateContainingBlockInfo();
        this.func_145979_i();
    }

    public BlockChest.Type func_145980_j()
    {
        if (this.field_145982_r == null)
        {
            if (this.world == null || !(this.func_145838_q() instanceof BlockChest))
            {
                return BlockChest.Type.BASIC;
            }

            this.field_145982_r = ((BlockChest)this.func_145838_q()).field_149956_a;
        }

        return this.field_145982_r;
    }

    public String func_174875_k()
    {
        return "minecraft:chest";
    }

    public Container func_174876_a(InventoryPlayer p_174876_1_, EntityPlayer p_174876_2_)
    {
        this.fillWithLoot(p_174876_2_);
        return new ContainerChest(p_174876_1_, this, p_174876_2_);
    }

    protected NonNullList<ItemStack> getItems()
    {
        return this.chestContents;
    }
}

package net.minecraft.tileentity;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityShulkerBox extends TileEntityLockableLoot implements ITickable, ISidedInventory
{
    private static final int[] SLOTS = new int[27];
    private NonNullList<ItemStack> items;
    private boolean field_190597_g;
    private int openCount;
    private TileEntityShulkerBox.AnimationStatus animationStatus;
    private float progress;
    private float progressOld;
    private EnumDyeColor color;
    private boolean field_190594_p;

    public TileEntityShulkerBox()
    {
        this((EnumDyeColor)null);
    }

    public TileEntityShulkerBox(@Nullable EnumDyeColor colorIn)
    {
        this.items = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
        this.animationStatus = TileEntityShulkerBox.AnimationStatus.CLOSED;
        this.color = colorIn;
    }

    public void tick()
    {
        this.updateAnimation();

        if (this.animationStatus == TileEntityShulkerBox.AnimationStatus.OPENING || this.animationStatus == TileEntityShulkerBox.AnimationStatus.CLOSING)
        {
            this.moveCollidedEntities();
        }
    }

    protected void updateAnimation()
    {
        this.progressOld = this.progress;

        switch (this.animationStatus)
        {
            case CLOSED:
                this.progress = 0.0F;
                break;

            case OPENING:
                this.progress += 0.1F;

                if (this.progress >= 1.0F)
                {
                    this.moveCollidedEntities();
                    this.animationStatus = TileEntityShulkerBox.AnimationStatus.OPENED;
                    this.progress = 1.0F;
                }

                break;

            case CLOSING:
                this.progress -= 0.1F;

                if (this.progress <= 0.0F)
                {
                    this.animationStatus = TileEntityShulkerBox.AnimationStatus.CLOSED;
                    this.progress = 0.0F;
                }

                break;

            case OPENED:
                this.progress = 1.0F;
        }
    }

    public TileEntityShulkerBox.AnimationStatus getAnimationStatus()
    {
        return this.animationStatus;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state)
    {
        return this.getBoundingBox((EnumFacing)state.get(BlockShulkerBox.FACING));
    }

    public AxisAlignedBB getBoundingBox(EnumFacing directionIn)
    {
        return Block.field_185505_j.expand((double)(0.5F * this.getProgress(1.0F) * (float)directionIn.getXOffset()), (double)(0.5F * this.getProgress(1.0F) * (float)directionIn.getYOffset()), (double)(0.5F * this.getProgress(1.0F) * (float)directionIn.getZOffset()));
    }

    private AxisAlignedBB getTopBoundingBox(EnumFacing directionIn)
    {
        EnumFacing enumfacing = directionIn.getOpposite();
        return this.getBoundingBox(directionIn).contract((double)enumfacing.getXOffset(), (double)enumfacing.getYOffset(), (double)enumfacing.getZOffset());
    }

    private void moveCollidedEntities()
    {
        IBlockState iblockstate = this.world.getBlockState(this.getPos());

        if (iblockstate.getBlock() instanceof BlockShulkerBox)
        {
            EnumFacing enumfacing = (EnumFacing)iblockstate.get(BlockShulkerBox.FACING);
            AxisAlignedBB axisalignedbb = this.getTopBoundingBox(enumfacing).offset(this.pos);
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity((Entity)null, axisalignedbb);

            if (!list.isEmpty())
            {
                for (int i = 0; i < list.size(); ++i)
                {
                    Entity entity = list.get(i);

                    if (entity.getPushReaction() != EnumPushReaction.IGNORE)
                    {
                        double d0 = 0.0D;
                        double d1 = 0.0D;
                        double d2 = 0.0D;
                        AxisAlignedBB axisalignedbb1 = entity.getBoundingBox();

                        switch (enumfacing.getAxis())
                        {
                            case X:
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                                {
                                    d0 = axisalignedbb.maxX - axisalignedbb1.minX;
                                }
                                else
                                {
                                    d0 = axisalignedbb1.maxX - axisalignedbb.minX;
                                }

                                d0 = d0 + 0.01D;
                                break;

                            case Y:
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                                {
                                    d1 = axisalignedbb.maxY - axisalignedbb1.minY;
                                }
                                else
                                {
                                    d1 = axisalignedbb1.maxY - axisalignedbb.minY;
                                }

                                d1 = d1 + 0.01D;
                                break;

                            case Z:
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                                {
                                    d2 = axisalignedbb.maxZ - axisalignedbb1.minZ;
                                }
                                else
                                {
                                    d2 = axisalignedbb1.maxZ - axisalignedbb.minZ;
                                }

                                d2 = d2 + 0.01D;
                        }

                        entity.func_70091_d(MoverType.SHULKER_BOX, d0 * (double)enumfacing.getXOffset(), d1 * (double)enumfacing.getYOffset(), d2 * (double)enumfacing.getZOffset());
                    }
                }
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.items.size();
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * See {@link Block#eventReceived} for more information. This must return true serverside before it is called
     * clientside.
     */
    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.openCount = type;

            if (type == 0)
            {
                this.animationStatus = TileEntityShulkerBox.AnimationStatus.CLOSING;
            }

            if (type == 1)
            {
                this.animationStatus = TileEntityShulkerBox.AnimationStatus.OPENING;
            }

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
            if (this.openCount < 0)
            {
                this.openCount = 0;
            }

            ++this.openCount;
            this.world.addBlockEvent(this.pos, this.func_145838_q(), 1, this.openCount);

            if (this.openCount == 1)
            {
                this.world.playSound((EntityPlayer)null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            --this.openCount;
            this.world.addBlockEvent(this.pos, this.func_145838_q(), 1, this.openCount);

            if (this.openCount <= 0)
            {
                this.world.playSound((EntityPlayer)null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    public Container func_174876_a(InventoryPlayer p_174876_1_, EntityPlayer p_174876_2_)
    {
        return new ContainerShulkerBox(p_174876_1_, this, p_174876_2_);
    }

    public String func_174875_k()
    {
        return "minecraft:shulker_box";
    }

    public String func_70005_c_()
    {
        return this.hasCustomName() ? this.field_190577_o : "container.shulkerBox";
    }

    public static void func_190593_a(DataFixer p_190593_0_)
    {
        p_190593_0_.func_188258_a(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityShulkerBox.class, new String[] {"Items"}));
    }

    public void read(NBTTagCompound compound)
    {
        super.read(compound);
        this.loadFromNbt(compound);
    }

    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);
        return this.saveToNbt(compound);
    }

    public void loadFromNbt(NBTTagCompound compound)
    {
        this.items = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound) && compound.contains("Items", 9))
        {
            ItemStackHelper.loadAllItems(compound, this.items);
        }

        if (compound.contains("CustomName", 8))
        {
            this.field_190577_o = compound.getString("CustomName");
        }
    }

    public NBTTagCompound saveToNbt(NBTTagCompound compound)
    {
        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.items, false);
        }

        if (this.hasCustomName())
        {
            compound.putString("CustomName", this.field_190577_o);
        }

        if (!compound.contains("Lock") && this.func_174893_q_())
        {
            this.func_174891_i().write(compound);
        }

        return compound;
    }

    protected NonNullList<ItemStack> getItems()
    {
        return this.items;
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.items)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public int[] getSlotsForFace(EnumFacing side)
    {
        return SLOTS;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return !(Block.getBlockFromItem(itemStackIn.getItem()) instanceof BlockShulkerBox);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return true;
    }

    public void clear()
    {
        this.field_190597_g = true;
        super.clear();
    }

    public boolean func_190590_r()
    {
        return this.field_190597_g;
    }

    public float getProgress(float p_190585_1_)
    {
        return this.progressOld + (this.progress - this.progressOld) * p_190585_1_;
    }

    public EnumDyeColor getColor()
    {
        if (this.color == null)
        {
            this.color = BlockShulkerBox.getColorFromBlock(this.func_145838_q());
        }

        return this.color;
    }

    @Nullable

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 10, this.getUpdateTag());
    }

    public boolean func_190581_E()
    {
        return this.field_190594_p;
    }

    public void func_190579_a(boolean p_190579_1_)
    {
        this.field_190594_p = p_190579_1_;
    }

    public boolean func_190582_F()
    {
        return !this.func_190581_E() || !this.isEmpty() || this.hasCustomName() || this.lootTable != null;
    }

    static
    {
        for (int i = 0; i < SLOTS.length; SLOTS[i] = i++)
        {
            ;
        }
    }

    public static enum AnimationStatus
    {
        CLOSED,
        OPENING,
        OPENED,
        CLOSING;
    }
}

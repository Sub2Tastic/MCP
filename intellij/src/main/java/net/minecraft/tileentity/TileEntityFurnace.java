package net.minecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;

public class TileEntityFurnace extends TileEntityLockable implements ITickable, ISidedInventory
{
    private static final int[] field_145962_k = new int[] {0};
    private static final int[] field_145959_l = new int[] {2, 1};
    private static final int[] field_145960_m = new int[] {1};
    private NonNullList<ItemStack> field_145957_n = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    private int field_145956_a;
    private int field_145963_i;
    private int field_174906_k;
    private int field_174905_l;
    private String field_145958_o;

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.field_145957_n.size();
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.field_145957_n)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index)
    {
        return this.field_145957_n.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.field_145957_n, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.field_145957_n, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = this.field_145957_n.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.field_145957_n.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            this.field_174905_l = this.func_174904_a(stack);
            this.field_174906_k = 0;
            this.markDirty();
        }
    }

    public String func_70005_c_()
    {
        return this.hasCustomName() ? this.field_145958_o : "container.furnace";
    }

    public boolean hasCustomName()
    {
        return this.field_145958_o != null && !this.field_145958_o.isEmpty();
    }

    public void func_145951_a(String p_145951_1_)
    {
        this.field_145958_o = p_145951_1_;
    }

    public static void func_189676_a(DataFixer p_189676_0_)
    {
        p_189676_0_.func_188258_a(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityFurnace.class, new String[] {"Items"}));
    }

    public void read(NBTTagCompound compound)
    {
        super.read(compound);
        this.field_145957_n = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.field_145957_n);
        this.field_145956_a = compound.getShort("BurnTime");
        this.field_174906_k = compound.getShort("CookTime");
        this.field_174905_l = compound.getShort("CookTimeTotal");
        this.field_145963_i = func_145952_a(this.field_145957_n.get(1));

        if (compound.contains("CustomName", 8))
        {
            this.field_145958_o = compound.getString("CustomName");
        }
    }

    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);
        compound.putShort("BurnTime", (short)this.field_145956_a);
        compound.putShort("CookTime", (short)this.field_174906_k);
        compound.putShort("CookTimeTotal", (short)this.field_174905_l);
        ItemStackHelper.saveAllItems(compound, this.field_145957_n);

        if (this.hasCustomName())
        {
            compound.putString("CustomName", this.field_145958_o);
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

    public boolean func_145950_i()
    {
        return this.field_145956_a > 0;
    }

    public static boolean func_174903_a(IInventory p_174903_0_)
    {
        return p_174903_0_.func_174887_a_(0) > 0;
    }

    public void tick()
    {
        boolean flag = this.func_145950_i();
        boolean flag1 = false;

        if (this.func_145950_i())
        {
            --this.field_145956_a;
        }

        if (!this.world.isRemote)
        {
            ItemStack itemstack = this.field_145957_n.get(1);

            if (this.func_145950_i() || !itemstack.isEmpty() && !((ItemStack)this.field_145957_n.get(0)).isEmpty())
            {
                if (!this.func_145950_i() && this.func_145948_k())
                {
                    this.field_145956_a = func_145952_a(itemstack);
                    this.field_145963_i = this.field_145956_a;

                    if (this.func_145950_i())
                    {
                        flag1 = true;

                        if (!itemstack.isEmpty())
                        {
                            Item item = itemstack.getItem();
                            itemstack.shrink(1);

                            if (itemstack.isEmpty())
                            {
                                Item item1 = item.getContainerItem();
                                this.field_145957_n.set(1, item1 == null ? ItemStack.EMPTY : new ItemStack(item1));
                            }
                        }
                    }
                }

                if (this.func_145950_i() && this.func_145948_k())
                {
                    ++this.field_174906_k;

                    if (this.field_174906_k == this.field_174905_l)
                    {
                        this.field_174906_k = 0;
                        this.field_174905_l = this.func_174904_a(this.field_145957_n.get(0));
                        this.func_145949_j();
                        flag1 = true;
                    }
                }
                else
                {
                    this.field_174906_k = 0;
                }
            }
            else if (!this.func_145950_i() && this.field_174906_k > 0)
            {
                this.field_174906_k = MathHelper.clamp(this.field_174906_k - 2, 0, this.field_174905_l);
            }

            if (flag != this.func_145950_i())
            {
                flag1 = true;
                BlockFurnace.func_176446_a(this.func_145950_i(), this.world, this.pos);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    public int func_174904_a(ItemStack p_174904_1_)
    {
        return 200;
    }

    private boolean func_145948_k()
    {
        if (((ItemStack)this.field_145957_n.get(0)).isEmpty())
        {
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.func_77602_a().func_151395_a(this.field_145957_n.get(0));

            if (itemstack.isEmpty())
            {
                return false;
            }
            else
            {
                ItemStack itemstack1 = this.field_145957_n.get(2);

                if (itemstack1.isEmpty())
                {
                    return true;
                }
                else if (!itemstack1.isItemEqual(itemstack))
                {
                    return false;
                }
                else if (itemstack1.getCount() < this.getInventoryStackLimit() && itemstack1.getCount() < itemstack1.getMaxStackSize())
                {
                    return true;
                }
                else
                {
                    return itemstack1.getCount() < itemstack.getMaxStackSize();
                }
            }
        }
    }

    public void func_145949_j()
    {
        if (this.func_145948_k())
        {
            ItemStack itemstack = this.field_145957_n.get(0);
            ItemStack itemstack1 = FurnaceRecipes.func_77602_a().func_151395_a(itemstack);
            ItemStack itemstack2 = this.field_145957_n.get(2);

            if (itemstack2.isEmpty())
            {
                this.field_145957_n.set(2, itemstack1.copy());
            }
            else if (itemstack2.getItem() == itemstack1.getItem())
            {
                itemstack2.grow(1);
            }

            if (itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && itemstack.func_77960_j() == 1 && !((ItemStack)this.field_145957_n.get(1)).isEmpty() && ((ItemStack)this.field_145957_n.get(1)).getItem() == Items.BUCKET)
            {
                this.field_145957_n.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }

    public static int func_145952_a(ItemStack p_145952_0_)
    {
        if (p_145952_0_.isEmpty())
        {
            return 0;
        }
        else
        {
            Item item = p_145952_0_.getItem();

            if (item == Item.getItemFromBlock(Blocks.field_150376_bx))
            {
                return 150;
            }
            else if (item == Item.getItemFromBlock(Blocks.field_150325_L))
            {
                return 100;
            }
            else if (item == Item.getItemFromBlock(Blocks.field_150404_cg))
            {
                return 67;
            }
            else if (item == Item.getItemFromBlock(Blocks.LADDER))
            {
                return 300;
            }
            else if (item == Item.getItemFromBlock(Blocks.field_150471_bO))
            {
                return 100;
            }
            else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD)
            {
                return 300;
            }
            else if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK))
            {
                return 16000;
            }
            else if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).func_77861_e()))
            {
                return 200;
            }
            else if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).func_150932_j()))
            {
                return 200;
            }
            else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).func_77842_f()))
            {
                return 200;
            }
            else if (item == Items.STICK)
            {
                return 100;
            }
            else if (item != Items.BOW && item != Items.FISHING_ROD)
            {
                if (item == Items.field_151155_ap)
                {
                    return 200;
                }
                else if (item == Items.COAL)
                {
                    return 1600;
                }
                else if (item == Items.LAVA_BUCKET)
                {
                    return 20000;
                }
                else if (item != Item.getItemFromBlock(Blocks.field_150345_g) && item != Items.BOWL)
                {
                    if (item == Items.BLAZE_ROD)
                    {
                        return 2400;
                    }
                    else if (item instanceof ItemDoor && item != Items.field_151139_aw)
                    {
                        return 200;
                    }
                    else
                    {
                        return item instanceof ItemBoat ? 400 : 0;
                    }
                }
                else
                {
                    return 100;
                }
            }
            else
            {
                return 300;
            }
        }
    }

    public static boolean func_145954_b(ItemStack p_145954_0_)
    {
        return func_145952_a(p_145954_0_) > 0;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public void openInventory(EntityPlayer player)
    {
    }

    public void closeInventory(EntityPlayer player)
    {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        if (index == 2)
        {
            return false;
        }
        else if (index != 1)
        {
            return true;
        }
        else
        {
            ItemStack itemstack = this.field_145957_n.get(1);
            return func_145954_b(stack) || SlotFurnaceFuel.isBucket(stack) && itemstack.getItem() != Items.BUCKET;
        }
    }

    public int[] getSlotsForFace(EnumFacing side)
    {
        if (side == EnumFacing.DOWN)
        {
            return field_145959_l;
        }
        else
        {
            return side == EnumFacing.UP ? field_145962_k : field_145960_m;
        }
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        if (direction == EnumFacing.DOWN && index == 1)
        {
            Item item = stack.getItem();

            if (item != Items.WATER_BUCKET && item != Items.BUCKET)
            {
                return false;
            }
        }

        return true;
    }

    public String func_174875_k()
    {
        return "minecraft:furnace";
    }

    public Container func_174876_a(InventoryPlayer p_174876_1_, EntityPlayer p_174876_2_)
    {
        return new ContainerFurnace(p_174876_1_, this);
    }

    public int func_174887_a_(int p_174887_1_)
    {
        switch (p_174887_1_)
        {
            case 0:
                return this.field_145956_a;

            case 1:
                return this.field_145963_i;

            case 2:
                return this.field_174906_k;

            case 3:
                return this.field_174905_l;

            default:
                return 0;
        }
    }

    public void func_174885_b(int p_174885_1_, int p_174885_2_)
    {
        switch (p_174885_1_)
        {
            case 0:
                this.field_145956_a = p_174885_2_;
                break;

            case 1:
                this.field_145963_i = p_174885_2_;
                break;

            case 2:
                this.field_174906_k = p_174885_2_;
                break;

            case 3:
                this.field_174905_l = p_174885_2_;
        }
    }

    public int func_174890_g()
    {
        return 4;
    }

    public void clear()
    {
        this.field_145957_n.clear();
    }
}

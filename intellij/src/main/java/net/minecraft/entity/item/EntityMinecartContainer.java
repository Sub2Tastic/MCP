package net.minecraft.entity.item;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public abstract class EntityMinecartContainer extends EntityMinecart implements ILockableContainer, ILootContainer
{
    private NonNullList<ItemStack> minecartContainerItems = NonNullList.<ItemStack>withSize(36, ItemStack.EMPTY);
    private boolean dropContentsWhenDead = true;
    private ResourceLocation lootTable;
    private long lootTableSeed;

    public EntityMinecartContainer(World p_i1716_1_)
    {
        super(p_i1716_1_);
    }

    public EntityMinecartContainer(World p_i1717_1_, double p_i1717_2_, double p_i1717_4_, double p_i1717_6_)
    {
        super(p_i1717_1_, p_i1717_2_, p_i1717_4_, p_i1717_6_);
    }

    public void killMinecart(DamageSource source)
    {
        super.killMinecart(source);

        if (this.world.getGameRules().func_82766_b("doEntityDrops"))
        {
            InventoryHelper.dropInventoryItems(this.world, this, this);
        }
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.minecartContainerItems)
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
        this.addLoot((EntityPlayer)null);
        return this.minecartContainerItems.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        this.addLoot((EntityPlayer)null);
        return ItemStackHelper.getAndSplit(this.minecartContainerItems, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index)
    {
        this.addLoot((EntityPlayer)null);
        ItemStack itemstack = this.minecartContainerItems.get(index);

        if (itemstack.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else
        {
            this.minecartContainerItems.set(index, ItemStack.EMPTY);
            return itemstack;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.addLoot((EntityPlayer)null);
        this.minecartContainerItems.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty()
    {
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.removed)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq(this) <= 64.0D;
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
        return true;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Nullable
    public Entity func_184204_a(int p_184204_1_)
    {
        this.dropContentsWhenDead = false;
        return super.func_184204_a(p_184204_1_);
    }

    /**
     * Queues the entity for removal from the world on the next tick.
     */
    public void remove()
    {
        if (this.dropContentsWhenDead)
        {
            InventoryHelper.dropInventoryItems(this.world, this, this);
        }

        super.remove();
    }

    public void func_184174_b(boolean p_184174_1_)
    {
        this.dropContentsWhenDead = p_184174_1_;
    }

    public static void func_190574_b(DataFixer p_190574_0_, Class<?> p_190574_1_)
    {
        EntityMinecart.func_189669_a(p_190574_0_, p_190574_1_);
        p_190574_0_.func_188258_a(FixTypes.ENTITY, new ItemStackDataLists(p_190574_1_, new String[] {"Items"}));
    }

    protected void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);

        if (this.lootTable != null)
        {
            p_70014_1_.putString("LootTable", this.lootTable.toString());

            if (this.lootTableSeed != 0L)
            {
                p_70014_1_.putLong("LootTableSeed", this.lootTableSeed);
            }
        }
        else
        {
            ItemStackHelper.saveAllItems(p_70014_1_, this.minecartContainerItems);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        this.minecartContainerItems = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (compound.contains("LootTable", 8))
        {
            this.lootTable = new ResourceLocation(compound.getString("LootTable"));
            this.lootTableSeed = compound.getLong("LootTableSeed");
        }
        else
        {
            ItemStackHelper.loadAllItems(compound, this.minecartContainerItems);
        }
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
    {
        if (!this.world.isRemote)
        {
            player.func_71007_a(this);
        }

        return true;
    }

    protected void applyDrag()
    {
        float f = 0.98F;

        if (this.lootTable == null)
        {
            int i = 15 - Container.calcRedstoneFromInventory(this);
            f += (float)i * 0.001F;
        }

        this.field_70159_w *= (double)f;
        this.field_70181_x *= 0.0D;
        this.field_70179_y *= (double)f;
    }

    public int func_174887_a_(int p_174887_1_)
    {
        return 0;
    }

    public void func_174885_b(int p_174885_1_, int p_174885_2_)
    {
    }

    public int func_174890_g()
    {
        return 0;
    }

    public boolean func_174893_q_()
    {
        return false;
    }

    public void func_174892_a(LockCode p_174892_1_)
    {
    }

    public LockCode func_174891_i()
    {
        return LockCode.EMPTY_CODE;
    }

    /**
     * Adds loot to the minecart's contents.
     */
    public void addLoot(@Nullable EntityPlayer player)
    {
        if (this.lootTable != null)
        {
            LootTable loottable = this.world.func_184146_ak().getLootTableFromLocation(this.lootTable);
            this.lootTable = null;
            Random random;

            if (this.lootTableSeed == 0L)
            {
                random = new Random();
            }
            else
            {
                random = new Random(this.lootTableSeed);
            }

            LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.world);

            if (player != null)
            {
                lootcontext$builder.withLuck(player.getLuck());
            }

            loottable.func_186460_a(this, random, lootcontext$builder.func_186471_a());
        }
    }

    public void clear()
    {
        this.addLoot((EntityPlayer)null);
        this.minecartContainerItems.clear();
    }

    public void setLootTable(ResourceLocation lootTableIn, long lootTableSeedIn)
    {
        this.lootTable = lootTableIn;
        this.lootTableSeed = lootTableSeedIn;
    }

    public ResourceLocation func_184276_b()
    {
        return this.lootTable;
    }
}

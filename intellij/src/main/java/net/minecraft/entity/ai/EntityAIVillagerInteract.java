package net.minecraft.entity.ai;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class EntityAIVillagerInteract extends EntityAIWatchClosest2
{
    private int field_179478_e;
    private final EntityVillager field_179477_f;

    public EntityAIVillagerInteract(EntityVillager p_i45886_1_)
    {
        super(p_i45886_1_, EntityVillager.class, 3.0F, 0.02F);
        this.field_179477_f = p_i45886_1_;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        super.startExecuting();

        if (this.field_179477_f.canAbondonItems() && this.closestEntity instanceof EntityVillager && ((EntityVillager)this.closestEntity).wantsMoreFood())
        {
            this.field_179478_e = 10;
        }
        else
        {
            this.field_179478_e = 0;
        }
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick()
    {
        super.tick();

        if (this.field_179478_e > 0)
        {
            --this.field_179478_e;

            if (this.field_179478_e == 0)
            {
                InventoryBasic inventorybasic = this.field_179477_f.func_175551_co();

                for (int i = 0; i < inventorybasic.getSizeInventory(); ++i)
                {
                    ItemStack itemstack = inventorybasic.getStackInSlot(i);
                    ItemStack itemstack1 = ItemStack.EMPTY;

                    if (!itemstack.isEmpty())
                    {
                        Item item = itemstack.getItem();

                        if ((item == Items.BREAD || item == Items.POTATO || item == Items.CARROT || item == Items.BEETROOT) && itemstack.getCount() > 3)
                        {
                            int l = itemstack.getCount() / 2;
                            itemstack.shrink(l);
                            itemstack1 = new ItemStack(item, l, itemstack.func_77960_j());
                        }
                        else if (item == Items.WHEAT && itemstack.getCount() > 5)
                        {
                            int j = itemstack.getCount() / 2 / 3 * 3;
                            int k = j / 3;
                            itemstack.shrink(j);
                            itemstack1 = new ItemStack(Items.BREAD, k, 0);
                        }

                        if (itemstack.isEmpty())
                        {
                            inventorybasic.setInventorySlotContents(i, ItemStack.EMPTY);
                        }
                    }

                    if (!itemstack1.isEmpty())
                    {
                        double d0 = this.field_179477_f.posY - 0.30000001192092896D + (double)this.field_179477_f.getEyeHeight();
                        EntityItem entityitem = new EntityItem(this.field_179477_f.world, this.field_179477_f.posX, d0, this.field_179477_f.posZ, itemstack1);
                        float f = 0.3F;
                        float f1 = this.field_179477_f.rotationYawHead;
                        float f2 = this.field_179477_f.rotationPitch;
                        entityitem.field_70159_w = (double)(-MathHelper.sin(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F) * 0.3F);
                        entityitem.field_70179_y = (double)(MathHelper.cos(f1 * 0.017453292F) * MathHelper.cos(f2 * 0.017453292F) * 0.3F);
                        entityitem.field_70181_x = (double)(-MathHelper.sin(f2 * 0.017453292F) * 0.3F + 0.1F);
                        entityitem.setDefaultPickupDelay();
                        this.field_179477_f.world.addEntity0(entityitem);
                        break;
                    }
                }
            }
        }
    }
}

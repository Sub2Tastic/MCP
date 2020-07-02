package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIHarvestFarmland extends EntityAIMoveToBlock
{
    private final EntityVillager field_179504_c;
    private boolean field_179502_d;
    private boolean field_179503_e;
    private int field_179501_f;

    public EntityAIHarvestFarmland(EntityVillager p_i45889_1_, double p_i45889_2_)
    {
        super(p_i45889_1_, p_i45889_2_, 16);
        this.field_179504_c = p_i45889_1_;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        if (this.runDelay <= 0)
        {
            if (!this.field_179504_c.world.getGameRules().func_82766_b("mobGriefing"))
            {
                return false;
            }

            this.field_179501_f = -1;
            this.field_179502_d = this.field_179504_c.isFarmItemInInventory();
            this.field_179503_e = this.field_179504_c.wantsMoreFood();
        }

        return super.shouldExecute();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return this.field_179501_f >= 0 && super.shouldContinueExecuting();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick()
    {
        super.tick();
        this.field_179504_c.getLookController().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.field_179504_c.getVerticalFaceSpeed());

        if (this.getIsAboveDestination())
        {
            World world = this.field_179504_c.world;
            BlockPos blockpos = this.destinationBlock.up();
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            if (this.field_179501_f == 0 && block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate))
            {
                world.destroyBlock(blockpos, true);
            }
            else if (this.field_179501_f == 1 && iblockstate.getMaterial() == Material.AIR)
            {
                InventoryBasic inventorybasic = this.field_179504_c.func_175551_co();

                for (int i = 0; i < inventorybasic.getSizeInventory(); ++i)
                {
                    ItemStack itemstack = inventorybasic.getStackInSlot(i);
                    boolean flag = false;

                    if (!itemstack.isEmpty())
                    {
                        if (itemstack.getItem() == Items.WHEAT_SEEDS)
                        {
                            world.setBlockState(blockpos, Blocks.WHEAT.getDefaultState(), 3);
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.POTATO)
                        {
                            world.setBlockState(blockpos, Blocks.POTATOES.getDefaultState(), 3);
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.CARROT)
                        {
                            world.setBlockState(blockpos, Blocks.CARROTS.getDefaultState(), 3);
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.BEETROOT_SEEDS)
                        {
                            world.setBlockState(blockpos, Blocks.BEETROOTS.getDefaultState(), 3);
                            flag = true;
                        }
                    }

                    if (flag)
                    {
                        itemstack.shrink(1);

                        if (itemstack.isEmpty())
                        {
                            inventorybasic.setInventorySlotContents(i, ItemStack.EMPTY);
                        }

                        break;
                    }
                }
            }

            this.field_179501_f = -1;
            this.runDelay = 10;
        }
    }

    /**
     * Return true to set given position as destination
     */
    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block == Blocks.FARMLAND)
        {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();

            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate) && this.field_179503_e && (this.field_179501_f == 0 || this.field_179501_f < 0))
            {
                this.field_179501_f = 0;
                return true;
            }

            if (iblockstate.getMaterial() == Material.AIR && this.field_179502_d && (this.field_179501_f == 1 || this.field_179501_f < 0))
            {
                this.field_179501_f = 1;
                return true;
            }
        }

        return false;
    }
}

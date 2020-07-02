package net.minecraft.entity.item;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityMinecartChest extends EntityMinecartContainer
{
    public EntityMinecartChest(World p_i1714_1_)
    {
        super(p_i1714_1_);
    }

    public EntityMinecartChest(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static void func_189681_a(DataFixer p_189681_0_)
    {
        EntityMinecartContainer.func_190574_b(p_189681_0_, EntityMinecartChest.class);
    }

    public void killMinecart(DamageSource source)
    {
        super.killMinecart(source);

        if (this.world.getGameRules().func_82766_b("doEntityDrops"))
        {
            this.func_145778_a(Item.getItemFromBlock(Blocks.CHEST), 1, 0.0F);
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 27;
    }

    public EntityMinecart.Type getMinecartType()
    {
        return EntityMinecart.Type.CHEST;
    }

    public IBlockState getDefaultDisplayTile()
    {
        return Blocks.CHEST.getDefaultState().func_177226_a(BlockChest.FACING, EnumFacing.NORTH);
    }

    public int getDefaultDisplayTileOffset()
    {
        return 8;
    }

    public String func_174875_k()
    {
        return "minecraft:chest";
    }

    public Container func_174876_a(InventoryPlayer p_174876_1_, EntityPlayer p_174876_2_)
    {
        this.addLoot(p_174876_2_);
        return new ContainerChest(p_174876_1_, this, p_174876_2_);
    }
}

package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockJukebox extends BlockContainer
{
    public static final PropertyBool HAS_RECORD = PropertyBool.create("has_record");

    public static void func_189873_a(DataFixer p_189873_0_)
    {
        p_189873_0_.func_188258_a(FixTypes.BLOCK_ENTITY, new ItemStackData(BlockJukebox.TileEntityJukebox.class, new String[] {"RecordItem"}));
    }

    protected BlockJukebox()
    {
        super(Material.WOOD, MapColor.DIRT);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(HAS_RECORD, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.DECORATIONS);
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (((Boolean)p_180639_3_.get(HAS_RECORD)).booleanValue())
        {
            this.func_180678_e(p_180639_1_, p_180639_2_, p_180639_3_);
            p_180639_3_ = p_180639_3_.func_177226_a(HAS_RECORD, Boolean.valueOf(false));
            p_180639_1_.setBlockState(p_180639_2_, p_180639_3_, 2);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void insertRecord(World worldIn, BlockPos pos, IBlockState state, ItemStack recordStack)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof BlockJukebox.TileEntityJukebox)
        {
            ((BlockJukebox.TileEntityJukebox)tileentity).func_145857_a(recordStack.copy());
            worldIn.setBlockState(pos, state.func_177226_a(HAS_RECORD, Boolean.valueOf(true)), 2);
        }
    }

    private void func_180678_e(World p_180678_1_, BlockPos p_180678_2_, IBlockState p_180678_3_)
    {
        if (!p_180678_1_.isRemote)
        {
            TileEntity tileentity = p_180678_1_.getTileEntity(p_180678_2_);

            if (tileentity instanceof BlockJukebox.TileEntityJukebox)
            {
                BlockJukebox.TileEntityJukebox blockjukebox$tileentityjukebox = (BlockJukebox.TileEntityJukebox)tileentity;
                ItemStack itemstack = blockjukebox$tileentityjukebox.func_145856_a();

                if (!itemstack.isEmpty())
                {
                    p_180678_1_.func_175718_b(1010, p_180678_2_, 0);
                    p_180678_1_.func_184149_a(p_180678_2_, (SoundEvent)null);
                    blockjukebox$tileentityjukebox.func_145857_a(ItemStack.EMPTY);
                    float f = 0.7F;
                    double d0 = (double)(p_180678_1_.rand.nextFloat() * 0.7F) + 0.15000000596046448D;
                    double d1 = (double)(p_180678_1_.rand.nextFloat() * 0.7F) + 0.06000000238418579D + 0.6D;
                    double d2 = (double)(p_180678_1_.rand.nextFloat() * 0.7F) + 0.15000000596046448D;
                    ItemStack itemstack1 = itemstack.copy();
                    EntityItem entityitem = new EntityItem(p_180678_1_, (double)p_180678_2_.getX() + d0, (double)p_180678_2_.getY() + d1, (double)p_180678_2_.getZ() + d2, itemstack1);
                    entityitem.setDefaultPickupDelay();
                    p_180678_1_.addEntity0(entityitem);
                }
            }
        }
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        this.func_180678_e(p_180663_1_, p_180663_2_, p_180663_3_);
        super.func_180663_b(p_180663_1_, p_180663_2_, p_180663_3_);
    }

    public void func_180653_a(World p_180653_1_, BlockPos p_180653_2_, IBlockState p_180653_3_, float p_180653_4_, int p_180653_5_)
    {
        if (!p_180653_1_.isRemote)
        {
            super.func_180653_a(p_180653_1_, p_180653_2_, p_180653_3_, p_180653_4_, 0);
        }
    }

    public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_)
    {
        return new BlockJukebox.TileEntityJukebox();
    }

    /**
     * @deprecated call via {@link IBlockState#hasComparatorInputOverride()} whenever possible. Implementing/overriding
     * is fine.
     */
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    /**
     * @deprecated call via {@link IBlockState#getComparatorInputOverride(World,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof BlockJukebox.TileEntityJukebox)
        {
            ItemStack itemstack = ((BlockJukebox.TileEntityJukebox)tileentity).func_145856_a();

            if (!itemstack.isEmpty())
            {
                return Item.getIdFromItem(itemstack.getItem()) + 1 - Item.getIdFromItem(Items.field_151096_cd);
            }
        }

        return 0;
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(HAS_RECORD, Boolean.valueOf(p_176203_1_ > 0));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((Boolean)p_176201_1_.get(HAS_RECORD)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {HAS_RECORD});
    }

    public static class TileEntityJukebox extends TileEntity
    {
        private ItemStack field_145858_a = ItemStack.EMPTY;

        public void read(NBTTagCompound compound)
        {
            super.read(compound);

            if (compound.contains("RecordItem", 10))
            {
                this.func_145857_a(new ItemStack(compound.getCompound("RecordItem")));
            }
            else if (compound.getInt("Record") > 0)
            {
                this.func_145857_a(new ItemStack(Item.getItemById(compound.getInt("Record"))));
            }
        }

        public NBTTagCompound write(NBTTagCompound compound)
        {
            super.write(compound);

            if (!this.func_145856_a().isEmpty())
            {
                compound.func_74782_a("RecordItem", this.func_145856_a().write(new NBTTagCompound()));
            }

            return compound;
        }

        public ItemStack func_145856_a()
        {
            return this.field_145858_a;
        }

        public void func_145857_a(ItemStack p_145857_1_)
        {
            this.field_145858_a = p_145857_1_;
            this.markDirty();
        }
    }
}

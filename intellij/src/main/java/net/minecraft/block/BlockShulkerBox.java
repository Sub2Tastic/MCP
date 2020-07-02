package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockShulkerBox extends BlockContainer
{
    public static final PropertyEnum<EnumFacing> FACING = PropertyDirection.func_177714_a("facing");
    private final EnumDyeColor color;

    public BlockShulkerBox(EnumDyeColor p_i47248_1_)
    {
        super(Material.ROCK, MapColor.AIR);
        this.color = p_i47248_1_;
        this.func_149647_a(CreativeTabs.DECORATIONS);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(FACING, EnumFacing.UP));
    }

    public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityShulkerBox(this.color);
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_176214_u(IBlockState p_176214_1_)
    {
        return true;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public boolean func_190946_v(IBlockState p_190946_1_)
    {
        return true;
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (p_180639_1_.isRemote)
        {
            return true;
        }
        else if (p_180639_4_.isSpectator())
        {
            return true;
        }
        else
        {
            TileEntity tileentity = p_180639_1_.getTileEntity(p_180639_2_);

            if (tileentity instanceof TileEntityShulkerBox)
            {
                EnumFacing enumfacing = (EnumFacing)p_180639_3_.get(FACING);
                boolean flag;

                if (((TileEntityShulkerBox)tileentity).getAnimationStatus() == TileEntityShulkerBox.AnimationStatus.CLOSED)
                {
                    AxisAlignedBB axisalignedbb = field_185505_j.expand((double)(0.5F * (float)enumfacing.getXOffset()), (double)(0.5F * (float)enumfacing.getYOffset()), (double)(0.5F * (float)enumfacing.getZOffset())).contract((double)enumfacing.getXOffset(), (double)enumfacing.getYOffset(), (double)enumfacing.getZOffset());
                    flag = !p_180639_1_.func_184143_b(axisalignedbb.offset(p_180639_2_.offset(enumfacing)));
                }
                else
                {
                    flag = true;
                }

                if (flag)
                {
                    p_180639_4_.addStat(StatList.OPEN_SHULKER_BOX);
                    p_180639_4_.func_71007_a((IInventory)tileentity);
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        return this.getDefaultState().func_177226_a(FACING, p_180642_3_);
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((EnumFacing)p_176201_1_.get(FACING)).getIndex();
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        EnumFacing enumfacing = EnumFacing.byIndex(p_176203_1_);
        return this.getDefaultState().func_177226_a(FACING, enumfacing);
    }

    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually
     * collect this block
     */
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (worldIn.getTileEntity(pos) instanceof TileEntityShulkerBox)
        {
            TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox)worldIn.getTileEntity(pos);
            tileentityshulkerbox.func_190579_a(player.abilities.isCreativeMode);
            tileentityshulkerbox.fillWithLoot(player);
        }
    }

    public void func_180653_a(World p_180653_1_, BlockPos p_180653_2_, IBlockState p_180653_3_, float p_180653_4_, int p_180653_5_)
    {
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityShulkerBox)
            {
                ((TileEntityShulkerBox)tileentity).func_190575_a(stack.func_82833_r());
            }
        }
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        TileEntity tileentity = p_180663_1_.getTileEntity(p_180663_2_);

        if (tileentity instanceof TileEntityShulkerBox)
        {
            TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox)tileentity;

            if (!tileentityshulkerbox.func_190590_r() && tileentityshulkerbox.func_190582_F())
            {
                ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound.func_74782_a("BlockEntityTag", ((TileEntityShulkerBox)tileentity).saveToNbt(nbttagcompound1));
                itemstack.setTag(nbttagcompound);

                if (tileentityshulkerbox.hasCustomName())
                {
                    itemstack.func_151001_c(tileentityshulkerbox.func_70005_c_());
                    tileentityshulkerbox.func_190575_a("");
                }

                spawnAsEntity(p_180663_1_, p_180663_2_, itemstack);
            }

            p_180663_1_.updateComparatorOutputLevel(p_180663_2_, p_180663_3_.getBlock());
        }

        super.func_180663_b(p_180663_1_, p_180663_2_, p_180663_3_);
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        NBTTagCompound nbttagcompound = stack.getTag();

        if (nbttagcompound != null && nbttagcompound.contains("BlockEntityTag", 10))
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("BlockEntityTag");

            if (nbttagcompound1.contains("LootTable", 8))
            {
                tooltip.add("???????");
            }

            if (nbttagcompound1.contains("Items", 9))
            {
                NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(nbttagcompound1, nonnulllist);
                int i = 0;
                int j = 0;

                for (ItemStack itemstack : nonnulllist)
                {
                    if (!itemstack.isEmpty())
                    {
                        ++j;

                        if (i <= 4)
                        {
                            ++i;
                            tooltip.add(String.format("%s x%d", itemstack.func_82833_r(), itemstack.getCount()));
                        }
                    }
                }

                if (j - i > 0)
                {
                    tooltip.add(String.format(TextFormatting.ITALIC + I18n.func_74838_a("container.shulkerBox.more"), j - i));
                }
            }
        }
    }

    /**
     * @deprecated call via {@link IBlockState#getMobilityFlag()} whenever possible. Implementing/overriding is fine.
     */
    public EnumPushReaction getPushReaction(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        TileEntity tileentity = p_185496_2_.getTileEntity(p_185496_3_);
        return tileentity instanceof TileEntityShulkerBox ? ((TileEntityShulkerBox)tileentity).getBoundingBox(p_185496_1_) : field_185505_j;
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
        return Container.calcRedstoneFromInventory((IInventory)worldIn.getTileEntity(pos));
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        ItemStack itemstack = super.getItem(worldIn, pos, state);
        TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox)worldIn.getTileEntity(pos);
        NBTTagCompound nbttagcompound = tileentityshulkerbox.saveToNbt(new NBTTagCompound());

        if (!nbttagcompound.func_82582_d())
        {
            itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
        }

        return itemstack;
    }

    public static EnumDyeColor getColorFromItem(Item itemIn)
    {
        return getColorFromBlock(Block.getBlockFromItem(itemIn));
    }

    public static EnumDyeColor getColorFromBlock(Block blockIn)
    {
        return blockIn instanceof BlockShulkerBox ? ((BlockShulkerBox)blockIn).getColor() : EnumDyeColor.PURPLE;
    }

    public static Block getBlockByColor(EnumDyeColor colorIn)
    {
        switch (colorIn)
        {
            case WHITE:
                return Blocks.WHITE_SHULKER_BOX;

            case ORANGE:
                return Blocks.ORANGE_SHULKER_BOX;

            case MAGENTA:
                return Blocks.MAGENTA_SHULKER_BOX;

            case LIGHT_BLUE:
                return Blocks.LIGHT_BLUE_SHULKER_BOX;

            case YELLOW:
                return Blocks.YELLOW_SHULKER_BOX;

            case LIME:
                return Blocks.LIME_SHULKER_BOX;

            case PINK:
                return Blocks.PINK_SHULKER_BOX;

            case GRAY:
                return Blocks.GRAY_SHULKER_BOX;

            case SILVER:
                return Blocks.field_190985_dt;

            case CYAN:
                return Blocks.CYAN_SHULKER_BOX;

            case PURPLE:
            default:
                return Blocks.PURPLE_SHULKER_BOX;

            case BLUE:
                return Blocks.BLUE_SHULKER_BOX;

            case BROWN:
                return Blocks.BROWN_SHULKER_BOX;

            case GREEN:
                return Blocks.GREEN_SHULKER_BOX;

            case RED:
                return Blocks.RED_SHULKER_BOX;

            case BLACK:
                return Blocks.BLACK_SHULKER_BOX;
        }
    }

    public EnumDyeColor getColor()
    {
        return this.color;
    }

    public static ItemStack getColoredItemStack(EnumDyeColor colorIn)
    {
        return new ItemStack(getBlockByColor(colorIn));
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
        return state.func_177226_a(FACING, rot.rotate((EnumFacing)state.get(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation((EnumFacing)state.get(FACING)));
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        p_193383_2_ = this.func_176221_a(p_193383_2_, p_193383_1_, p_193383_3_);
        EnumFacing enumfacing = (EnumFacing)p_193383_2_.get(FACING);
        TileEntityShulkerBox.AnimationStatus tileentityshulkerbox$animationstatus = ((TileEntityShulkerBox)p_193383_1_.getTileEntity(p_193383_3_)).getAnimationStatus();
        return tileentityshulkerbox$animationstatus != TileEntityShulkerBox.AnimationStatus.CLOSED && (tileentityshulkerbox$animationstatus != TileEntityShulkerBox.AnimationStatus.OPENED || enumfacing != p_193383_4_.getOpposite() && enumfacing != p_193383_4_) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
    }
}

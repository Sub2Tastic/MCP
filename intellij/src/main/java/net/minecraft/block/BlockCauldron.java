package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCauldron extends Block
{
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);
    protected static final AxisAlignedBB field_185596_b = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D);
    protected static final AxisAlignedBB field_185597_c = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
    protected static final AxisAlignedBB field_185598_d = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185599_e = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185600_f = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

    public BlockCauldron()
    {
        super(Material.IRON, MapColor.STONE);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(LEVEL, Integer.valueOf(0)));
    }

    public void func_185477_a(IBlockState p_185477_1_, World p_185477_2_, BlockPos p_185477_3_, AxisAlignedBB p_185477_4_, List<AxisAlignedBB> p_185477_5_, @Nullable Entity p_185477_6_, boolean p_185477_7_)
    {
        func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185596_b);
        func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185600_f);
        func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185597_c);
        func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185599_e);
        func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185598_d);
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185505_j;
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public void func_180634_a(World p_180634_1_, BlockPos p_180634_2_, IBlockState p_180634_3_, Entity p_180634_4_)
    {
        int i = ((Integer)p_180634_3_.get(LEVEL)).intValue();
        float f = (float)p_180634_2_.getY() + (6.0F + (float)(3 * i)) / 16.0F;

        if (!p_180634_1_.isRemote && p_180634_4_.isBurning() && i > 0 && p_180634_4_.getBoundingBox().minY <= (double)f)
        {
            p_180634_4_.extinguish();
            this.setWaterLevel(p_180634_1_, p_180634_2_, p_180634_3_, i - 1);
        }
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        ItemStack itemstack = p_180639_4_.getHeldItem(p_180639_5_);

        if (itemstack.isEmpty())
        {
            return true;
        }
        else
        {
            int i = ((Integer)p_180639_3_.get(LEVEL)).intValue();
            Item item = itemstack.getItem();

            if (item == Items.WATER_BUCKET)
            {
                if (i < 3 && !p_180639_1_.isRemote)
                {
                    if (!p_180639_4_.abilities.isCreativeMode)
                    {
                        p_180639_4_.setHeldItem(p_180639_5_, new ItemStack(Items.BUCKET));
                    }

                    p_180639_4_.addStat(StatList.FILL_CAULDRON);
                    this.setWaterLevel(p_180639_1_, p_180639_2_, p_180639_3_, 3);
                    p_180639_1_.playSound((EntityPlayer)null, p_180639_2_, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return true;
            }
            else if (item == Items.BUCKET)
            {
                if (i == 3 && !p_180639_1_.isRemote)
                {
                    if (!p_180639_4_.abilities.isCreativeMode)
                    {
                        itemstack.shrink(1);

                        if (itemstack.isEmpty())
                        {
                            p_180639_4_.setHeldItem(p_180639_5_, new ItemStack(Items.WATER_BUCKET));
                        }
                        else if (!p_180639_4_.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET)))
                        {
                            p_180639_4_.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                        }
                    }

                    p_180639_4_.addStat(StatList.USE_CAULDRON);
                    this.setWaterLevel(p_180639_1_, p_180639_2_, p_180639_3_, 0);
                    p_180639_1_.playSound((EntityPlayer)null, p_180639_2_, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return true;
            }
            else if (item == Items.GLASS_BOTTLE)
            {
                if (i > 0 && !p_180639_1_.isRemote)
                {
                    if (!p_180639_4_.abilities.isCreativeMode)
                    {
                        ItemStack itemstack3 = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionTypes.WATER);
                        p_180639_4_.addStat(StatList.USE_CAULDRON);
                        itemstack.shrink(1);

                        if (itemstack.isEmpty())
                        {
                            p_180639_4_.setHeldItem(p_180639_5_, itemstack3);
                        }
                        else if (!p_180639_4_.inventory.addItemStackToInventory(itemstack3))
                        {
                            p_180639_4_.dropItem(itemstack3, false);
                        }
                        else if (p_180639_4_ instanceof EntityPlayerMP)
                        {
                            ((EntityPlayerMP)p_180639_4_).sendContainerToPlayer(p_180639_4_.container);
                        }
                    }

                    p_180639_1_.playSound((EntityPlayer)null, p_180639_2_, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.setWaterLevel(p_180639_1_, p_180639_2_, p_180639_3_, i - 1);
                }

                return true;
            }
            else if (item == Items.POTION && PotionUtils.getPotionFromItem(itemstack) == PotionTypes.WATER)
            {
                if (i < 3 && !p_180639_1_.isRemote)
                {
                    if (!p_180639_4_.abilities.isCreativeMode)
                    {
                        ItemStack itemstack2 = new ItemStack(Items.GLASS_BOTTLE);
                        p_180639_4_.addStat(StatList.USE_CAULDRON);
                        p_180639_4_.setHeldItem(p_180639_5_, itemstack2);

                        if (p_180639_4_ instanceof EntityPlayerMP)
                        {
                            ((EntityPlayerMP)p_180639_4_).sendContainerToPlayer(p_180639_4_.container);
                        }
                    }

                    p_180639_1_.playSound((EntityPlayer)null, p_180639_2_, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.setWaterLevel(p_180639_1_, p_180639_2_, p_180639_3_, i + 1);
                }

                return true;
            }
            else
            {
                if (i > 0 && item instanceof ItemArmor)
                {
                    ItemArmor itemarmor = (ItemArmor)item;

                    if (itemarmor.func_82812_d() == ItemArmor.ArmorMaterial.LEATHER && itemarmor.func_82816_b_(itemstack) && !p_180639_1_.isRemote)
                    {
                        itemarmor.func_82815_c(itemstack);
                        this.setWaterLevel(p_180639_1_, p_180639_2_, p_180639_3_, i - 1);
                        p_180639_4_.addStat(StatList.CLEAN_ARMOR);
                        return true;
                    }
                }

                if (i > 0 && item instanceof ItemBanner)
                {
                    if (TileEntityBanner.getPatterns(itemstack) > 0 && !p_180639_1_.isRemote)
                    {
                        ItemStack itemstack1 = itemstack.copy();
                        itemstack1.setCount(1);
                        TileEntityBanner.removeBannerData(itemstack1);
                        p_180639_4_.addStat(StatList.CLEAN_BANNER);

                        if (!p_180639_4_.abilities.isCreativeMode)
                        {
                            itemstack.shrink(1);
                            this.setWaterLevel(p_180639_1_, p_180639_2_, p_180639_3_, i - 1);
                        }

                        if (itemstack.isEmpty())
                        {
                            p_180639_4_.setHeldItem(p_180639_5_, itemstack1);
                        }
                        else if (!p_180639_4_.inventory.addItemStackToInventory(itemstack1))
                        {
                            p_180639_4_.dropItem(itemstack1, false);
                        }
                        else if (p_180639_4_ instanceof EntityPlayerMP)
                        {
                            ((EntityPlayerMP)p_180639_4_).sendContainerToPlayer(p_180639_4_.container);
                        }
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
    }

    public void setWaterLevel(World worldIn, BlockPos pos, IBlockState state, int level)
    {
        worldIn.setBlockState(pos, state.func_177226_a(LEVEL, Integer.valueOf(MathHelper.clamp(level, 0, 3))), 2);
        worldIn.updateComparatorOutputLevel(pos, this);
    }

    /**
     * Called similar to random ticks, but only when it is raining.
     */
    public void fillWithRain(World worldIn, BlockPos pos)
    {
        if (worldIn.rand.nextInt(20) == 1)
        {
            float f = worldIn.func_180494_b(pos).getTemperatureRaw(pos);

            if (worldIn.func_72959_q().func_76939_a(f, pos.getY()) >= 0.15F)
            {
                IBlockState iblockstate = worldIn.getBlockState(pos);

                if (((Integer)iblockstate.get(LEVEL)).intValue() < 3)
                {
                    worldIn.setBlockState(pos, iblockstate.cycle(LEVEL), 2);
                }
            }
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.field_151066_bu;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Items.field_151066_bu);
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
        return ((Integer)blockState.get(LEVEL)).intValue();
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(LEVEL, Integer.valueOf(p_176203_1_));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((Integer)p_176201_1_.get(LEVEL)).intValue();
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {LEVEL});
    }

    public boolean func_176205_b(IBlockAccess p_176205_1_, BlockPos p_176205_2_)
    {
        return true;
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        if (p_193383_4_ == EnumFacing.UP)
        {
            return BlockFaceShape.BOWL;
        }
        else
        {
            return p_193383_4_ == EnumFacing.DOWN ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
        }
    }
}

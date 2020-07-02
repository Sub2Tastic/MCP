package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockOre extends Block
{
    public BlockOre()
    {
        this(Material.ROCK.getColor());
    }

    public BlockOre(MapColor p_i46390_1_)
    {
        super(Material.ROCK, p_i46390_1_);
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        if (this == Blocks.COAL_ORE)
        {
            return Items.COAL;
        }
        else if (this == Blocks.DIAMOND_ORE)
        {
            return Items.DIAMOND;
        }
        else if (this == Blocks.LAPIS_ORE)
        {
            return Items.field_151100_aR;
        }
        else if (this == Blocks.EMERALD_ORE)
        {
            return Items.EMERALD;
        }
        else
        {
            return this == Blocks.field_150449_bY ? Items.QUARTZ : Item.getItemFromBlock(this);
        }
    }

    public int func_149745_a(Random p_149745_1_)
    {
        return this == Blocks.LAPIS_ORE ? 4 + p_149745_1_.nextInt(5) : 1;
    }

    public int func_149679_a(int p_149679_1_, Random p_149679_2_)
    {
        if (p_149679_1_ > 0 && Item.getItemFromBlock(this) != this.func_180660_a((IBlockState)this.getStateContainer().getValidStates().iterator().next(), p_149679_2_, p_149679_1_))
        {
            int i = p_149679_2_.nextInt(p_149679_1_ + 2) - 1;

            if (i < 0)
            {
                i = 0;
            }

            return this.func_149745_a(p_149679_2_) * (i + 1);
        }
        else
        {
            return this.func_149745_a(p_149679_2_);
        }
    }

    public void func_180653_a(World p_180653_1_, BlockPos p_180653_2_, IBlockState p_180653_3_, float p_180653_4_, int p_180653_5_)
    {
        super.func_180653_a(p_180653_1_, p_180653_2_, p_180653_3_, p_180653_4_, p_180653_5_);

        if (this.func_180660_a(p_180653_3_, p_180653_1_.rand, p_180653_5_) != Item.getItemFromBlock(this))
        {
            int i = 0;

            if (this == Blocks.COAL_ORE)
            {
                i = MathHelper.nextInt(p_180653_1_.rand, 0, 2);
            }
            else if (this == Blocks.DIAMOND_ORE)
            {
                i = MathHelper.nextInt(p_180653_1_.rand, 3, 7);
            }
            else if (this == Blocks.EMERALD_ORE)
            {
                i = MathHelper.nextInt(p_180653_1_.rand, 3, 7);
            }
            else if (this == Blocks.LAPIS_ORE)
            {
                i = MathHelper.nextInt(p_180653_1_.rand, 2, 5);
            }
            else if (this == Blocks.field_150449_bY)
            {
                i = MathHelper.nextInt(p_180653_1_.rand, 2, 5);
            }

            this.dropXpOnBlockBreak(p_180653_1_, p_180653_2_, i);
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this);
    }

    public int func_180651_a(IBlockState p_180651_1_)
    {
        return this == Blocks.LAPIS_ORE ? EnumDyeColor.BLUE.func_176767_b() : 0;
    }
}

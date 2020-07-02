package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMycelium extends Block
{
    public static final PropertyBool field_176384_a = PropertyBool.create("snowy");

    protected BlockMycelium()
    {
        super(Material.ORGANIC, MapColor.PURPLE);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176384_a, Boolean.valueOf(false)));
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public IBlockState func_176221_a(IBlockState p_176221_1_, IBlockAccess p_176221_2_, BlockPos p_176221_3_)
    {
        Block block = p_176221_2_.getBlockState(p_176221_3_.up()).getBlock();
        return p_176221_1_.func_177226_a(field_176384_a, Boolean.valueOf(block == Blocks.SNOW || block == Blocks.field_150431_aC));
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (!p_180650_1_.isRemote)
        {
            if (p_180650_1_.func_175671_l(p_180650_2_.up()) < 4 && p_180650_1_.getBlockState(p_180650_2_.up()).func_185891_c() > 2)
            {
                p_180650_1_.setBlockState(p_180650_2_, Blocks.DIRT.getDefaultState().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.DIRT));
            }
            else
            {
                if (p_180650_1_.func_175671_l(p_180650_2_.up()) >= 9)
                {
                    for (int i = 0; i < 4; ++i)
                    {
                        BlockPos blockpos = p_180650_2_.add(p_180650_4_.nextInt(3) - 1, p_180650_4_.nextInt(5) - 3, p_180650_4_.nextInt(3) - 1);
                        IBlockState iblockstate = p_180650_1_.getBlockState(blockpos);
                        IBlockState iblockstate1 = p_180650_1_.getBlockState(blockpos.up());

                        if (iblockstate.getBlock() == Blocks.DIRT && iblockstate.get(BlockDirt.field_176386_a) == BlockDirt.DirtType.DIRT && p_180650_1_.func_175671_l(blockpos.up()) >= 4 && iblockstate1.func_185891_c() <= 2)
                        {
                            p_180650_1_.setBlockState(blockpos, this.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
     * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
     * of whether the block can receive random update ticks
     */
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        super.animateTick(stateIn, worldIn, pos, rand);

        if (rand.nextInt(10) == 0)
        {
            worldIn.func_175688_a(EnumParticleTypes.TOWN_AURA, (double)((float)pos.getX() + rand.nextFloat()), (double)((float)pos.getY() + 1.1F), (double)((float)pos.getZ() + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Blocks.DIRT.func_180660_a(Blocks.DIRT.getDefaultState().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.DIRT), p_180660_2_, p_180660_3_);
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return 0;
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176384_a});
    }
}

package net.minecraft.client.renderer.color;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public class BlockColors
{
    private final ObjectIntIdentityMap<IBlockColor> colors = new ObjectIntIdentityMap<IBlockColor>(32);

    public static BlockColors init()
    {
        final BlockColors blockcolors = new BlockColors();
        blockcolors.register(new IBlockColor()
        {
            public int func_186720_a(IBlockState p_186720_1_, @Nullable IBlockAccess p_186720_2_, @Nullable BlockPos p_186720_3_, int p_186720_4_)
            {
                BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = (BlockDoublePlant.EnumPlantType)p_186720_1_.get(BlockDoublePlant.field_176493_a);
                return p_186720_2_ != null && p_186720_3_ != null && (blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.GRASS || blockdoubleplant$enumplanttype == BlockDoublePlant.EnumPlantType.FERN) ? BiomeColorHelper.func_180286_a(p_186720_2_, p_186720_1_.get(BlockDoublePlant.HALF) == BlockDoublePlant.EnumBlockHalf.UPPER ? p_186720_3_.down() : p_186720_3_) : -1;
            }
        }, Blocks.field_150398_cm);
        blockcolors.register(new IBlockColor()
        {
            public int func_186720_a(IBlockState p_186720_1_, @Nullable IBlockAccess p_186720_2_, @Nullable BlockPos p_186720_3_, int p_186720_4_)
            {
                if (p_186720_2_ != null && p_186720_3_ != null)
                {
                    TileEntity tileentity = p_186720_2_.getTileEntity(p_186720_3_);

                    if (tileentity instanceof TileEntityFlowerPot)
                    {
                        Item item = ((TileEntityFlowerPot)tileentity).func_145965_a();
                        IBlockState iblockstate = Block.getBlockFromItem(item).getDefaultState();
                        return blockcolors.func_186724_a(iblockstate, p_186720_2_, p_186720_3_, p_186720_4_);
                    }
                    else
                    {
                        return -1;
                    }
                }
                else
                {
                    return -1;
                }
            }
        }, Blocks.FLOWER_POT);
        blockcolors.register(new IBlockColor()
        {
            public int func_186720_a(IBlockState p_186720_1_, @Nullable IBlockAccess p_186720_2_, @Nullable BlockPos p_186720_3_, int p_186720_4_)
            {
                return p_186720_2_ != null && p_186720_3_ != null ? BiomeColorHelper.func_180286_a(p_186720_2_, p_186720_3_) : ColorizerGrass.get(0.5D, 1.0D);
            }
        }, Blocks.GRASS);
        blockcolors.register(new IBlockColor()
        {
            public int func_186720_a(IBlockState p_186720_1_, @Nullable IBlockAccess p_186720_2_, @Nullable BlockPos p_186720_3_, int p_186720_4_)
            {
                BlockPlanks.EnumType blockplanks$enumtype = (BlockPlanks.EnumType)p_186720_1_.get(BlockOldLeaf.field_176239_P);

                if (blockplanks$enumtype == BlockPlanks.EnumType.SPRUCE)
                {
                    return ColorizerFoliage.getSpruce();
                }
                else if (blockplanks$enumtype == BlockPlanks.EnumType.BIRCH)
                {
                    return ColorizerFoliage.getBirch();
                }
                else
                {
                    return p_186720_2_ != null && p_186720_3_ != null ? BiomeColorHelper.func_180287_b(p_186720_2_, p_186720_3_) : ColorizerFoliage.getDefault();
                }
            }
        }, Blocks.field_150362_t);
        blockcolors.register(new IBlockColor()
        {
            public int func_186720_a(IBlockState p_186720_1_, @Nullable IBlockAccess p_186720_2_, @Nullable BlockPos p_186720_3_, int p_186720_4_)
            {
                return p_186720_2_ != null && p_186720_3_ != null ? BiomeColorHelper.func_180287_b(p_186720_2_, p_186720_3_) : ColorizerFoliage.getDefault();
            }
        }, Blocks.field_150361_u);
        blockcolors.register(new IBlockColor()
        {
            public int func_186720_a(IBlockState p_186720_1_, @Nullable IBlockAccess p_186720_2_, @Nullable BlockPos p_186720_3_, int p_186720_4_)
            {
                return p_186720_2_ != null && p_186720_3_ != null ? BiomeColorHelper.func_180288_c(p_186720_2_, p_186720_3_) : -1;
            }
        }, Blocks.WATER, Blocks.field_150358_i);
        blockcolors.register(new IBlockColor()
        {
            public int func_186720_a(IBlockState p_186720_1_, @Nullable IBlockAccess p_186720_2_, @Nullable BlockPos p_186720_3_, int p_186720_4_)
            {
                return BlockRedstoneWire.colorMultiplier(((Integer)p_186720_1_.get(BlockRedstoneWire.POWER)).intValue());
            }
        }, Blocks.REDSTONE_WIRE);
        blockcolors.register(new IBlockColor()
        {
            public int func_186720_a(IBlockState p_186720_1_, @Nullable IBlockAccess p_186720_2_, @Nullable BlockPos p_186720_3_, int p_186720_4_)
            {
                return p_186720_2_ != null && p_186720_3_ != null ? BiomeColorHelper.func_180286_a(p_186720_2_, p_186720_3_) : -1;
            }
        }, Blocks.field_150436_aH);
        blockcolors.register(new IBlockColor()
        {
            public int func_186720_a(IBlockState p_186720_1_, @Nullable IBlockAccess p_186720_2_, @Nullable BlockPos p_186720_3_, int p_186720_4_)
            {
                int i = ((Integer)p_186720_1_.get(BlockStem.AGE)).intValue();
                int j = i * 32;
                int k = 255 - i * 8;
                int l = i * 4;
                return j << 16 | k << 8 | l;
            }
        }, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);
        blockcolors.register(new IBlockColor()
        {
            public int func_186720_a(IBlockState p_186720_1_, @Nullable IBlockAccess p_186720_2_, @Nullable BlockPos p_186720_3_, int p_186720_4_)
            {
                if (p_186720_2_ != null && p_186720_3_ != null)
                {
                    return BiomeColorHelper.func_180286_a(p_186720_2_, p_186720_3_);
                }
                else
                {
                    return p_186720_1_.get(BlockTallGrass.field_176497_a) == BlockTallGrass.EnumType.DEAD_BUSH ? 16777215 : ColorizerGrass.get(0.5D, 1.0D);
                }
            }
        }, Blocks.field_150329_H);
        blockcolors.register(new IBlockColor()
        {
            public int func_186720_a(IBlockState p_186720_1_, @Nullable IBlockAccess p_186720_2_, @Nullable BlockPos p_186720_3_, int p_186720_4_)
            {
                return p_186720_2_ != null && p_186720_3_ != null ? BiomeColorHelper.func_180287_b(p_186720_2_, p_186720_3_) : ColorizerFoliage.getDefault();
            }
        }, Blocks.VINE);
        blockcolors.register(new IBlockColor()
        {
            public int func_186720_a(IBlockState p_186720_1_, @Nullable IBlockAccess p_186720_2_, @Nullable BlockPos p_186720_3_, int p_186720_4_)
            {
                return p_186720_2_ != null && p_186720_3_ != null ? 2129968 : 7455580;
            }
        }, Blocks.field_150392_bi);
        return blockcolors;
    }

    public int getColorOrMaterialColor(IBlockState state, World worldIn, BlockPos blockPosIn)
    {
        IBlockColor iblockcolor = this.colors.getByValue(Block.func_149682_b(state.getBlock()));

        if (iblockcolor != null)
        {
            return iblockcolor.func_186720_a(state, (IBlockAccess)null, (BlockPos)null, 0);
        }
        else
        {
            MapColor mapcolor = state.getMaterialColor(worldIn, blockPosIn);
            return mapcolor != null ? mapcolor.colorValue : -1;
        }
    }

    public int func_186724_a(IBlockState p_186724_1_, @Nullable IBlockAccess p_186724_2_, @Nullable BlockPos p_186724_3_, int p_186724_4_)
    {
        IBlockColor iblockcolor = this.colors.getByValue(Block.func_149682_b(p_186724_1_.getBlock()));
        return iblockcolor == null ? -1 : iblockcolor.func_186720_a(p_186724_1_, p_186724_2_, p_186724_3_, p_186724_4_);
    }

    public void register(IBlockColor blockColor, Block... blocksIn)
    {
        for (Block block : blocksIn)
        {
            this.colors.put(blockColor, Block.func_149682_b(block));
        }
    }
}

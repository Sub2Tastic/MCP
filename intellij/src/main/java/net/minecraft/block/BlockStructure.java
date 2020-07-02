package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStructure extends BlockContainer
{
    public static final PropertyEnum<TileEntityStructure.Mode> MODE = PropertyEnum.<TileEntityStructure.Mode>create("mode", TileEntityStructure.Mode.class);

    public BlockStructure()
    {
        super(Material.IRON, MapColor.field_151680_x);
        this.setDefaultState(this.stateContainer.getBaseState());
    }

    public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityStructure();
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        TileEntity tileentity = p_180639_1_.getTileEntity(p_180639_2_);
        return tileentity instanceof TileEntityStructure ? ((TileEntityStructure)tileentity).usedBy(p_180639_4_) : false;
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (!worldIn.isRemote)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityStructure)
            {
                TileEntityStructure tileentitystructure = (TileEntityStructure)tileentity;
                tileentitystructure.createdBy(placer);
            }
        }
    }

    public int func_149745_a(Random p_149745_1_)
    {
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

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        return this.getDefaultState().func_177226_a(MODE, TileEntityStructure.Mode.DATA);
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(MODE, TileEntityStructure.Mode.func_185108_a(p_176203_1_));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((TileEntityStructure.Mode)p_176201_1_.get(MODE)).func_185110_a();
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {MODE});
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (!p_189540_2_.isRemote)
        {
            TileEntity tileentity = p_189540_2_.getTileEntity(p_189540_3_);

            if (tileentity instanceof TileEntityStructure)
            {
                TileEntityStructure tileentitystructure = (TileEntityStructure)tileentity;
                boolean flag = p_189540_2_.isBlockPowered(p_189540_3_);
                boolean flag1 = tileentitystructure.isPowered();

                if (flag && !flag1)
                {
                    tileentitystructure.setPowered(true);
                    this.trigger(tileentitystructure);
                }
                else if (!flag && flag1)
                {
                    tileentitystructure.setPowered(false);
                }
            }
        }
    }

    private void trigger(TileEntityStructure structureIn)
    {
        switch (structureIn.getMode())
        {
            case SAVE:
                structureIn.save(false);
                break;

            case LOAD:
                structureIn.load(false);
                break;

            case CORNER:
                structureIn.unloadStructure();

            case DATA:
        }
    }
}

package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockNote extends BlockContainer
{
    private static final List<SoundEvent> field_176434_a = Lists.newArrayList(SoundEvents.BLOCK_NOTE_BLOCK_HARP, SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundEvents.BLOCK_NOTE_BLOCK_SNARE, SoundEvents.BLOCK_NOTE_BLOCK_HAT, SoundEvents.BLOCK_NOTE_BLOCK_BASS, SoundEvents.BLOCK_NOTE_BLOCK_FLUTE, SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundEvents.BLOCK_NOTE_BLOCK_GUITAR, SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE);

    public BlockNote()
    {
        super(Material.WOOD);
        this.func_149647_a(CreativeTabs.REDSTONE);
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        boolean flag = p_189540_2_.isBlockPowered(p_189540_3_);
        TileEntity tileentity = p_189540_2_.getTileEntity(p_189540_3_);

        if (tileentity instanceof TileEntityNote)
        {
            TileEntityNote tileentitynote = (TileEntityNote)tileentity;

            if (tileentitynote.field_145880_i != flag)
            {
                if (flag)
                {
                    tileentitynote.func_175108_a(p_189540_2_, p_189540_3_);
                }

                tileentitynote.field_145880_i = flag;
            }
        }
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (p_180639_1_.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity tileentity = p_180639_1_.getTileEntity(p_180639_2_);

            if (tileentity instanceof TileEntityNote)
            {
                TileEntityNote tileentitynote = (TileEntityNote)tileentity;
                tileentitynote.func_145877_a();
                tileentitynote.func_175108_a(p_180639_1_, p_180639_2_);
                p_180639_4_.addStat(StatList.TUNE_NOTEBLOCK);
            }

            return true;
        }
    }

    public void func_180649_a(World p_180649_1_, BlockPos p_180649_2_, EntityPlayer p_180649_3_)
    {
        if (!p_180649_1_.isRemote)
        {
            TileEntity tileentity = p_180649_1_.getTileEntity(p_180649_2_);

            if (tileentity instanceof TileEntityNote)
            {
                ((TileEntityNote)tileentity).func_175108_a(p_180649_1_, p_180649_2_);
                p_180649_3_.addStat(StatList.PLAY_NOTEBLOCK);
            }
        }
    }

    public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityNote();
    }

    private SoundEvent func_185576_e(int p_185576_1_)
    {
        if (p_185576_1_ < 0 || p_185576_1_ >= field_176434_a.size())
        {
            p_185576_1_ = 0;
        }

        return field_176434_a.get(p_185576_1_);
    }

    /**
     * Called on server when World#addBlockEvent is called. If server returns true, then also called on the client. On
     * the Server, this may perform additional changes to the world, like pistons replacing the block with an extended
     * base. On the client, the update may involve replacing tile entities or effects such as sounds or particles
     * @deprecated call via {@link IBlockState#onBlockEventReceived(World,BlockPos,int,int)} whenever possible.
     * Implementing/overriding is fine.
     */
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        float f = (float)Math.pow(2.0D, (double)(param - 12) / 12.0D);
        worldIn.playSound((EntityPlayer)null, pos, this.func_185576_e(id), SoundCategory.RECORDS, 3.0F, f);
        worldIn.func_175688_a(EnumParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.2D, (double)pos.getZ() + 0.5D, (double)param / 24.0D, 0.0D, 0.0D);
        return true;
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
}

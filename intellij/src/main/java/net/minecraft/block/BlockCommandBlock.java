package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockCommandBlock extends BlockContainer
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    public static final PropertyBool CONDITIONAL = PropertyBool.create("conditional");

    public BlockCommandBlock(MapColor p_i46688_1_)
    {
        super(Material.IRON, p_i46688_1_);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(FACING, EnumFacing.NORTH).func_177226_a(CONDITIONAL, Boolean.valueOf(false)));
    }

    public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_)
    {
        TileEntityCommandBlock tileentitycommandblock = new TileEntityCommandBlock();
        tileentitycommandblock.setAuto(this == Blocks.CHAIN_COMMAND_BLOCK);
        return tileentitycommandblock;
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (!p_189540_2_.isRemote)
        {
            TileEntity tileentity = p_189540_2_.getTileEntity(p_189540_3_);

            if (tileentity instanceof TileEntityCommandBlock)
            {
                TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;
                boolean flag = p_189540_2_.isBlockPowered(p_189540_3_);
                boolean flag1 = tileentitycommandblock.isPowered();
                tileentitycommandblock.setPowered(flag);

                if (!flag1 && !tileentitycommandblock.isAuto() && tileentitycommandblock.getMode() != TileEntityCommandBlock.Mode.SEQUENCE)
                {
                    if (flag)
                    {
                        tileentitycommandblock.setConditionMet();
                        p_189540_2_.func_175684_a(p_189540_3_, this, this.tickRate(p_189540_2_));
                    }
                }
            }
        }
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (!p_180650_1_.isRemote)
        {
            TileEntity tileentity = p_180650_1_.getTileEntity(p_180650_2_);

            if (tileentity instanceof TileEntityCommandBlock)
            {
                TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;
                CommandBlockBaseLogic commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();
                boolean flag = !StringUtils.isNullOrEmpty(commandblockbaselogic.getCommand());
                TileEntityCommandBlock.Mode tileentitycommandblock$mode = tileentitycommandblock.getMode();
                boolean flag1 = tileentitycommandblock.isConditionMet();

                if (tileentitycommandblock$mode == TileEntityCommandBlock.Mode.AUTO)
                {
                    tileentitycommandblock.setConditionMet();

                    if (flag1)
                    {
                        this.execute(p_180650_3_, p_180650_1_, p_180650_2_, commandblockbaselogic, flag);
                    }
                    else if (tileentitycommandblock.isConditional())
                    {
                        commandblockbaselogic.setSuccessCount(0);
                    }

                    if (tileentitycommandblock.isPowered() || tileentitycommandblock.isAuto())
                    {
                        p_180650_1_.func_175684_a(p_180650_2_, this, this.tickRate(p_180650_1_));
                    }
                }
                else if (tileentitycommandblock$mode == TileEntityCommandBlock.Mode.REDSTONE)
                {
                    if (flag1)
                    {
                        this.execute(p_180650_3_, p_180650_1_, p_180650_2_, commandblockbaselogic, flag);
                    }
                    else if (tileentitycommandblock.isConditional())
                    {
                        commandblockbaselogic.setSuccessCount(0);
                    }
                }

                p_180650_1_.updateComparatorOutputLevel(p_180650_2_, this);
            }
        }
    }

    private void execute(IBlockState p_193387_1_, World p_193387_2_, BlockPos p_193387_3_, CommandBlockBaseLogic p_193387_4_, boolean p_193387_5_)
    {
        if (p_193387_5_)
        {
            p_193387_4_.trigger(p_193387_2_);
        }
        else
        {
            p_193387_4_.setSuccessCount(0);
        }

        executeChain(p_193387_2_, p_193387_3_, (EnumFacing)p_193387_1_.get(FACING));
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 1;
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        TileEntity tileentity = p_180639_1_.getTileEntity(p_180639_2_);

        if (tileentity instanceof TileEntityCommandBlock && p_180639_4_.func_189808_dh())
        {
            p_180639_4_.openCommandBlock((TileEntityCommandBlock)tileentity);
            return true;
        }
        else
        {
            return false;
        }
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
        return tileentity instanceof TileEntityCommandBlock ? ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().getSuccessCount() : 0;
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityCommandBlock)
        {
            TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;
            CommandBlockBaseLogic commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();

            if (stack.hasDisplayName())
            {
                commandblockbaselogic.func_145754_b(stack.func_82833_r());
            }

            if (!worldIn.isRemote)
            {
                NBTTagCompound nbttagcompound = stack.getTag();

                if (nbttagcompound == null || !nbttagcompound.contains("BlockEntityTag", 10))
                {
                    commandblockbaselogic.setTrackOutput(worldIn.getGameRules().func_82766_b("sendCommandFeedback"));
                    tileentitycommandblock.setAuto(this == Blocks.CHAIN_COMMAND_BLOCK);
                }

                if (tileentitycommandblock.getMode() == TileEntityCommandBlock.Mode.SEQUENCE)
                {
                    boolean flag = worldIn.isBlockPowered(pos);
                    tileentitycommandblock.setPowered(flag);
                }
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

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(FACING, EnumFacing.byIndex(p_176203_1_ & 7)).func_177226_a(CONDITIONAL, Boolean.valueOf((p_176203_1_ & 8) != 0));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((EnumFacing)p_176201_1_.get(FACING)).getIndex() | (((Boolean)p_176201_1_.get(CONDITIONAL)).booleanValue() ? 8 : 0);
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

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, CONDITIONAL});
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        return this.getDefaultState().func_177226_a(FACING, EnumFacing.func_190914_a(p_180642_2_, p_180642_8_)).func_177226_a(CONDITIONAL, Boolean.valueOf(false));
    }

    private static void executeChain(World worldIn, BlockPos pos, EnumFacing directionIn)
    {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(pos);
        GameRules gamerules = worldIn.getGameRules();
        int i;
        IBlockState iblockstate;

        for (i = gamerules.func_180263_c("maxCommandChainLength"); i-- > 0; directionIn = (EnumFacing)iblockstate.get(FACING))
        {
            blockpos$mutableblockpos.move(directionIn);
            iblockstate = worldIn.getBlockState(blockpos$mutableblockpos);
            Block block = iblockstate.getBlock();

            if (block != Blocks.CHAIN_COMMAND_BLOCK)
            {
                break;
            }

            TileEntity tileentity = worldIn.getTileEntity(blockpos$mutableblockpos);

            if (!(tileentity instanceof TileEntityCommandBlock))
            {
                break;
            }

            TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;

            if (tileentitycommandblock.getMode() != TileEntityCommandBlock.Mode.SEQUENCE)
            {
                break;
            }

            if (tileentitycommandblock.isPowered() || tileentitycommandblock.isAuto())
            {
                CommandBlockBaseLogic commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();

                if (tileentitycommandblock.setConditionMet())
                {
                    if (!commandblockbaselogic.trigger(worldIn))
                    {
                        break;
                    }

                    worldIn.updateComparatorOutputLevel(blockpos$mutableblockpos, block);
                }
                else if (tileentitycommandblock.isConditional())
                {
                    commandblockbaselogic.setSuccessCount(0);
                }
            }
        }

        if (i <= 0)
        {
            int j = Math.max(gamerules.func_180263_c("maxCommandChainLength"), 0);
            LOGGER.warn("Commandblock chain tried to execure more than " + j + " steps!");
        }
    }
}

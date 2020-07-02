package net.minecraft.server.management;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class PlayerInteractionManager
{
    public World world;
    public EntityPlayerMP player;
    private GameType gameType = GameType.NOT_SET;
    private boolean isDestroyingBlock;
    private int initialDamage;
    private BlockPos destroyPos = BlockPos.ZERO;
    private int ticks;
    private boolean receivedFinishDiggingPacket;
    private BlockPos delayedDestroyPos = BlockPos.ZERO;
    private int initialBlockDamage;
    private int durabilityRemainingOnBlock = -1;

    public PlayerInteractionManager(World p_i1524_1_)
    {
        this.world = p_i1524_1_;
    }

    public void setGameType(GameType type)
    {
        this.gameType = type;
        type.configurePlayerCapabilities(this.player.abilities);
        this.player.sendPlayerAbilities();
        this.player.server.getPlayerList().sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_GAME_MODE, new EntityPlayerMP[] {this.player}));
        this.world.updateAllPlayersSleepingFlag();
    }

    public GameType getGameType()
    {
        return this.gameType;
    }

    public boolean survivalOrAdventure()
    {
        return this.gameType.isSurvivalOrAdventure();
    }

    /**
     * Get if we are in creative game mode.
     */
    public boolean isCreative()
    {
        return this.gameType.isCreative();
    }

    /**
     * if the gameType is currently NOT_SET then change it to par1
     */
    public void initializeGameType(GameType type)
    {
        if (this.gameType == GameType.NOT_SET)
        {
            this.gameType = type;
        }

        this.setGameType(this.gameType);
    }

    public void tick()
    {
        ++this.ticks;

        if (this.receivedFinishDiggingPacket)
        {
            int i = this.ticks - this.initialBlockDamage;
            IBlockState iblockstate = this.world.getBlockState(this.delayedDestroyPos);

            if (iblockstate.getMaterial() == Material.AIR)
            {
                this.receivedFinishDiggingPacket = false;
            }
            else
            {
                float f = iblockstate.getPlayerRelativeBlockHardness(this.player, this.player.world, this.delayedDestroyPos) * (float)(i + 1);
                int j = (int)(f * 10.0F);

                if (j != this.durabilityRemainingOnBlock)
                {
                    this.world.sendBlockBreakProgress(this.player.getEntityId(), this.delayedDestroyPos, j);
                    this.durabilityRemainingOnBlock = j;
                }

                if (f >= 1.0F)
                {
                    this.receivedFinishDiggingPacket = false;
                    this.tryHarvestBlock(this.delayedDestroyPos);
                }
            }
        }
        else if (this.isDestroyingBlock)
        {
            IBlockState iblockstate1 = this.world.getBlockState(this.destroyPos);

            if (iblockstate1.getMaterial() == Material.AIR)
            {
                this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, -1);
                this.durabilityRemainingOnBlock = -1;
                this.isDestroyingBlock = false;
            }
            else
            {
                int k = this.ticks - this.initialDamage;
                float f1 = iblockstate1.getPlayerRelativeBlockHardness(this.player, this.player.world, this.delayedDestroyPos) * (float)(k + 1);
                int l = (int)(f1 * 10.0F);

                if (l != this.durabilityRemainingOnBlock)
                {
                    this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, l);
                    this.durabilityRemainingOnBlock = l;
                }
            }
        }
    }

    public void func_180784_a(BlockPos p_180784_1_, EnumFacing p_180784_2_)
    {
        if (this.isCreative())
        {
            if (!this.world.extinguishFire((EntityPlayer)null, p_180784_1_, p_180784_2_))
            {
                this.tryHarvestBlock(p_180784_1_);
            }
        }
        else
        {
            IBlockState iblockstate = this.world.getBlockState(p_180784_1_);
            Block block = iblockstate.getBlock();

            if (this.gameType.hasLimitedInteractions())
            {
                if (this.gameType == GameType.SPECTATOR)
                {
                    return;
                }

                if (!this.player.isAllowEdit())
                {
                    ItemStack itemstack = this.player.getHeldItemMainhand();

                    if (itemstack.isEmpty())
                    {
                        return;
                    }

                    if (!itemstack.func_179544_c(block))
                    {
                        return;
                    }
                }
            }

            this.world.extinguishFire((EntityPlayer)null, p_180784_1_, p_180784_2_);
            this.initialDamage = this.ticks;
            float f = 1.0F;

            if (iblockstate.getMaterial() != Material.AIR)
            {
                block.func_180649_a(this.world, p_180784_1_, this.player);
                f = iblockstate.getPlayerRelativeBlockHardness(this.player, this.player.world, p_180784_1_);
            }

            if (iblockstate.getMaterial() != Material.AIR && f >= 1.0F)
            {
                this.tryHarvestBlock(p_180784_1_);
            }
            else
            {
                this.isDestroyingBlock = true;
                this.destroyPos = p_180784_1_;
                int i = (int)(f * 10.0F);
                this.world.sendBlockBreakProgress(this.player.getEntityId(), p_180784_1_, i);
                this.durabilityRemainingOnBlock = i;
            }
        }
    }

    public void func_180785_a(BlockPos p_180785_1_)
    {
        if (p_180785_1_.equals(this.destroyPos))
        {
            int i = this.ticks - this.initialDamage;
            IBlockState iblockstate = this.world.getBlockState(p_180785_1_);

            if (iblockstate.getMaterial() != Material.AIR)
            {
                float f = iblockstate.getPlayerRelativeBlockHardness(this.player, this.player.world, p_180785_1_) * (float)(i + 1);

                if (f >= 0.7F)
                {
                    this.isDestroyingBlock = false;
                    this.world.sendBlockBreakProgress(this.player.getEntityId(), p_180785_1_, -1);
                    this.tryHarvestBlock(p_180785_1_);
                }
                else if (!this.receivedFinishDiggingPacket)
                {
                    this.isDestroyingBlock = false;
                    this.receivedFinishDiggingPacket = true;
                    this.delayedDestroyPos = p_180785_1_;
                    this.initialBlockDamage = this.initialDamage;
                }
            }
        }
    }

    public void func_180238_e()
    {
        this.isDestroyingBlock = false;
        this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, -1);
    }

    private boolean func_180235_c(BlockPos p_180235_1_)
    {
        IBlockState iblockstate = this.world.getBlockState(p_180235_1_);
        iblockstate.getBlock().onBlockHarvested(this.world, p_180235_1_, iblockstate, this.player);
        boolean flag = this.world.func_175698_g(p_180235_1_);

        if (flag)
        {
            iblockstate.getBlock().onPlayerDestroy(this.world, p_180235_1_, iblockstate);
        }

        return flag;
    }

    /**
     * Attempts to harvest a block
     */
    public boolean tryHarvestBlock(BlockPos pos)
    {
        if (this.gameType.isCreative() && !this.player.getHeldItemMainhand().isEmpty() && this.player.getHeldItemMainhand().getItem() instanceof ItemSword)
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = this.world.getBlockState(pos);
            TileEntity tileentity = this.world.getTileEntity(pos);
            Block block = iblockstate.getBlock();

            if ((block instanceof BlockCommandBlock || block instanceof BlockStructure) && !this.player.func_189808_dh())
            {
                this.world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
                return false;
            }
            else
            {
                if (this.gameType.hasLimitedInteractions())
                {
                    if (this.gameType == GameType.SPECTATOR)
                    {
                        return false;
                    }

                    if (!this.player.isAllowEdit())
                    {
                        ItemStack itemstack = this.player.getHeldItemMainhand();

                        if (itemstack.isEmpty())
                        {
                            return false;
                        }

                        if (!itemstack.func_179544_c(block))
                        {
                            return false;
                        }
                    }
                }

                this.world.func_180498_a(this.player, 2001, pos, Block.func_176210_f(iblockstate));
                boolean flag1 = this.func_180235_c(pos);

                if (this.isCreative())
                {
                    this.player.connection.sendPacket(new SPacketBlockChange(this.world, pos));
                }
                else
                {
                    ItemStack itemstack1 = this.player.getHeldItemMainhand();
                    ItemStack itemstack2 = itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1.copy();
                    boolean flag = this.player.canHarvestBlock(iblockstate);

                    if (!itemstack1.isEmpty())
                    {
                        itemstack1.onBlockDestroyed(this.world, iblockstate, pos, this.player);
                    }

                    if (flag1 && flag)
                    {
                        iblockstate.getBlock().harvestBlock(this.world, this.player, pos, iblockstate, tileentity, itemstack2);
                    }
                }

                return flag1;
            }
        }
    }

    public EnumActionResult processRightClick(EntityPlayer player, World worldIn, ItemStack stack, EnumHand hand)
    {
        if (this.gameType == GameType.SPECTATOR)
        {
            return EnumActionResult.PASS;
        }
        else if (player.getCooldownTracker().hasCooldown(stack.getItem()))
        {
            return EnumActionResult.PASS;
        }
        else
        {
            int i = stack.getCount();
            int j = stack.func_77960_j();
            ActionResult<ItemStack> actionresult = stack.useItemRightClick(worldIn, player, hand);
            ItemStack itemstack = actionresult.getResult();

            if (itemstack == stack && itemstack.getCount() == i && itemstack.getUseDuration() <= 0 && itemstack.func_77960_j() == j)
            {
                return actionresult.getType();
            }
            else if (actionresult.getType() == EnumActionResult.FAIL && itemstack.getUseDuration() > 0 && !player.isHandActive())
            {
                return actionresult.getType();
            }
            else
            {
                player.setHeldItem(hand, itemstack);

                if (this.isCreative())
                {
                    itemstack.setCount(i);

                    if (itemstack.isDamageable())
                    {
                        itemstack.func_77964_b(j);
                    }
                }

                if (itemstack.isEmpty())
                {
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }

                if (!player.isHandActive())
                {
                    ((EntityPlayerMP)player).sendContainerToPlayer(player.container);
                }

                return actionresult.getType();
            }
        }
    }

    public EnumActionResult func_187251_a(EntityPlayer p_187251_1_, World p_187251_2_, ItemStack p_187251_3_, EnumHand p_187251_4_, BlockPos p_187251_5_, EnumFacing p_187251_6_, float p_187251_7_, float p_187251_8_, float p_187251_9_)
    {
        if (this.gameType == GameType.SPECTATOR)
        {
            TileEntity tileentity = p_187251_2_.getTileEntity(p_187251_5_);

            if (tileentity instanceof ILockableContainer)
            {
                Block block1 = p_187251_2_.getBlockState(p_187251_5_).getBlock();
                ILockableContainer ilockablecontainer = (ILockableContainer)tileentity;

                if (ilockablecontainer instanceof TileEntityChest && block1 instanceof BlockChest)
                {
                    ilockablecontainer = ((BlockChest)block1).func_180676_d(p_187251_2_, p_187251_5_);
                }

                if (ilockablecontainer != null)
                {
                    p_187251_1_.func_71007_a(ilockablecontainer);
                    return EnumActionResult.SUCCESS;
                }
            }
            else if (tileentity instanceof IInventory)
            {
                p_187251_1_.func_71007_a((IInventory)tileentity);
                return EnumActionResult.SUCCESS;
            }

            return EnumActionResult.PASS;
        }
        else
        {
            if (!p_187251_1_.func_70093_af() || p_187251_1_.getHeldItemMainhand().isEmpty() && p_187251_1_.getHeldItemOffhand().isEmpty())
            {
                IBlockState iblockstate = p_187251_2_.getBlockState(p_187251_5_);

                if (iblockstate.getBlock().func_180639_a(p_187251_2_, p_187251_5_, iblockstate, p_187251_1_, p_187251_4_, p_187251_6_, p_187251_7_, p_187251_8_, p_187251_9_))
                {
                    return EnumActionResult.SUCCESS;
                }
            }

            if (p_187251_3_.isEmpty())
            {
                return EnumActionResult.PASS;
            }
            else if (p_187251_1_.getCooldownTracker().hasCooldown(p_187251_3_.getItem()))
            {
                return EnumActionResult.PASS;
            }
            else
            {
                if (p_187251_3_.getItem() instanceof ItemBlock && !p_187251_1_.func_189808_dh())
                {
                    Block block = ((ItemBlock)p_187251_3_.getItem()).getBlock();

                    if (block instanceof BlockCommandBlock || block instanceof BlockStructure)
                    {
                        return EnumActionResult.FAIL;
                    }
                }

                if (this.isCreative())
                {
                    int j = p_187251_3_.func_77960_j();
                    int i = p_187251_3_.getCount();
                    EnumActionResult enumactionresult = p_187251_3_.func_179546_a(p_187251_1_, p_187251_2_, p_187251_5_, p_187251_4_, p_187251_6_, p_187251_7_, p_187251_8_, p_187251_9_);
                    p_187251_3_.func_77964_b(j);
                    p_187251_3_.setCount(i);
                    return enumactionresult;
                }
                else
                {
                    return p_187251_3_.func_179546_a(p_187251_1_, p_187251_2_, p_187251_5_, p_187251_4_, p_187251_6_, p_187251_7_, p_187251_8_, p_187251_9_);
                }
            }
        }
    }

    /**
     * Sets the world instance.
     */
    public void setWorld(WorldServer serverWorld)
    {
        this.world = serverWorld;
    }
}

package net.minecraft.server.management;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class DemoPlayerInteractionManager extends PlayerInteractionManager
{
    private boolean displayedIntro;
    private boolean demoTimeExpired;
    private int demoEndedReminder;
    private int gameModeTicks;

    public DemoPlayerInteractionManager(World p_i1513_1_)
    {
        super(p_i1513_1_);
    }

    public void tick()
    {
        super.tick();
        ++this.gameModeTicks;
        long i = this.world.getGameTime();
        long j = i / 24000L + 1L;

        if (!this.displayedIntro && this.gameModeTicks > 20)
        {
            this.displayedIntro = true;
            this.player.connection.sendPacket(new SPacketChangeGameState(5, 0.0F));
        }

        this.demoTimeExpired = i > 120500L;

        if (this.demoTimeExpired)
        {
            ++this.demoEndedReminder;
        }

        if (i % 24000L == 500L)
        {
            if (j <= 6L)
            {
                this.player.sendMessage(new TextComponentTranslation("demo.day." + j, new Object[0]));
            }
        }
        else if (j == 1L)
        {
            if (i == 100L)
            {
                this.player.connection.sendPacket(new SPacketChangeGameState(5, 101.0F));
            }
            else if (i == 175L)
            {
                this.player.connection.sendPacket(new SPacketChangeGameState(5, 102.0F));
            }
            else if (i == 250L)
            {
                this.player.connection.sendPacket(new SPacketChangeGameState(5, 103.0F));
            }
        }
        else if (j == 5L && i % 24000L == 22000L)
        {
            this.player.sendMessage(new TextComponentTranslation("demo.day.warning", new Object[0]));
        }
    }

    /**
     * Sends a message to the player reminding them that this is the demo version
     */
    private void sendDemoReminder()
    {
        if (this.demoEndedReminder > 100)
        {
            this.player.sendMessage(new TextComponentTranslation("demo.reminder", new Object[0]));
            this.demoEndedReminder = 0;
        }
    }

    public void func_180784_a(BlockPos p_180784_1_, EnumFacing p_180784_2_)
    {
        if (this.demoTimeExpired)
        {
            this.sendDemoReminder();
        }
        else
        {
            super.func_180784_a(p_180784_1_, p_180784_2_);
        }
    }

    public void func_180785_a(BlockPos p_180785_1_)
    {
        if (!this.demoTimeExpired)
        {
            super.func_180785_a(p_180785_1_);
        }
    }

    /**
     * Attempts to harvest a block
     */
    public boolean tryHarvestBlock(BlockPos pos)
    {
        return this.demoTimeExpired ? false : super.tryHarvestBlock(pos);
    }

    public EnumActionResult processRightClick(EntityPlayer player, World worldIn, ItemStack stack, EnumHand hand)
    {
        if (this.demoTimeExpired)
        {
            this.sendDemoReminder();
            return EnumActionResult.PASS;
        }
        else
        {
            return super.processRightClick(player, worldIn, stack, hand);
        }
    }

    public EnumActionResult func_187251_a(EntityPlayer p_187251_1_, World p_187251_2_, ItemStack p_187251_3_, EnumHand p_187251_4_, BlockPos p_187251_5_, EnumFacing p_187251_6_, float p_187251_7_, float p_187251_8_, float p_187251_9_)
    {
        if (this.demoTimeExpired)
        {
            this.sendDemoReminder();
            return EnumActionResult.PASS;
        }
        else
        {
            return super.func_187251_a(p_187251_1_, p_187251_2_, p_187251_3_, p_187251_4_, p_187251_5_, p_187251_6_, p_187251_7_, p_187251_8_, p_187251_9_);
        }
    }
}

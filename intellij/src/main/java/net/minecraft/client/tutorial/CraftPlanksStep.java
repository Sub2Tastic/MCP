package net.minecraft.client.tutorial;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.toasts.TutorialToast;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class CraftPlanksStep implements ITutorialStep
{
    private static final ITextComponent TITLE = new TextComponentTranslation("tutorial.craft_planks.title", new Object[0]);
    private static final ITextComponent DESCRIPTION = new TextComponentTranslation("tutorial.craft_planks.description", new Object[0]);
    private final Tutorial tutorial;
    private TutorialToast toast;
    private int timeWaiting;

    public CraftPlanksStep(Tutorial tutorial)
    {
        this.tutorial = tutorial;
    }

    public void tick()
    {
        ++this.timeWaiting;

        if (this.tutorial.getGameType() != GameType.SURVIVAL)
        {
            this.tutorial.setStep(TutorialSteps.NONE);
        }
        else
        {
            if (this.timeWaiting == 1)
            {
                EntityPlayerSP entityplayersp = this.tutorial.getMinecraft().player;

                if (entityplayersp != null)
                {
                    if (entityplayersp.inventory.hasItemStack(new ItemStack(Blocks.field_150344_f)))
                    {
                        this.tutorial.setStep(TutorialSteps.NONE);
                        return;
                    }

                    if (func_194071_a(entityplayersp))
                    {
                        this.tutorial.setStep(TutorialSteps.NONE);
                        return;
                    }
                }
            }

            if (this.timeWaiting >= 1200 && this.toast == null)
            {
                this.toast = new TutorialToast(TutorialToast.Icons.WOODEN_PLANKS, TITLE, DESCRIPTION, false);
                this.tutorial.getMinecraft().getToastGui().add(this.toast);
            }
        }
    }

    public void onStop()
    {
        if (this.toast != null)
        {
            this.toast.hide();
            this.toast = null;
        }
    }

    /**
     * Called when the player pick up an ItemStack
     */
    public void handleSetSlot(ItemStack stack)
    {
        if (stack.getItem() == Item.getItemFromBlock(Blocks.field_150344_f))
        {
            this.tutorial.setStep(TutorialSteps.NONE);
        }
    }

    public static boolean func_194071_a(EntityPlayerSP p_194071_0_)
    {
        StatBase statbase = StatList.func_188060_a(Item.getItemFromBlock(Blocks.field_150344_f));
        return statbase != null && p_194071_0_.getStats().getValue(statbase) > 0;
    }
}
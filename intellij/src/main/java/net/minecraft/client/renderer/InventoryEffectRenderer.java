package net.minecraft.client.renderer;

import com.google.common.collect.Ordering;
import java.util.Collection;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public abstract class InventoryEffectRenderer extends GuiContainer
{
    /** True if there is some potion effect to display */
    protected boolean hasActivePotionEffects;

    public InventoryEffectRenderer(Container p_i1089_1_)
    {
        super(p_i1089_1_);
    }

    public void func_73866_w_()
    {
        super.func_73866_w_();
        this.updateActivePotionEffects();
    }

    protected void updateActivePotionEffects()
    {
        if (this.field_146297_k.player.getActivePotionEffects().isEmpty())
        {
            this.guiLeft = (this.field_146294_l - this.xSize) / 2;
            this.hasActivePotionEffects = false;
        }
        else
        {
            this.guiLeft = 160 + (this.field_146294_l - this.xSize - 200) / 2;
            this.hasActivePotionEffects = true;
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);

        if (this.hasActivePotionEffects)
        {
            this.drawActivePotionEffects();
        }
    }

    /**
     * Display the potion effects list
     */
    private void drawActivePotionEffects()
    {
        int i = this.guiLeft - 124;
        int j = this.guiTop;
        int k = 166;
        Collection<PotionEffect> collection = this.field_146297_k.player.getActivePotionEffects();

        if (!collection.isEmpty())
        {
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.func_179140_f();
            int l = 33;

            if (collection.size() > 5)
            {
                l = 132 / (collection.size() - 1);
            }

            for (PotionEffect potioneffect : Ordering.natural().sortedCopy(collection))
            {
                Potion potion = potioneffect.getPotion();
                GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                this.field_146297_k.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
                this.func_73729_b(i, j, 0, 166, 140, 32);

                if (potion.func_76400_d())
                {
                    int i1 = potion.func_76392_e();
                    this.func_73729_b(i + 6, j + 7, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                }

                String s1 = I18n.format(potion.getName());

                if (potioneffect.getAmplifier() == 1)
                {
                    s1 = s1 + " " + I18n.format("enchantment.level.2");
                }
                else if (potioneffect.getAmplifier() == 2)
                {
                    s1 = s1 + " " + I18n.format("enchantment.level.3");
                }
                else if (potioneffect.getAmplifier() == 3)
                {
                    s1 = s1 + " " + I18n.format("enchantment.level.4");
                }

                this.field_146289_q.drawStringWithShadow(s1, (float)(i + 10 + 18), (float)(j + 6), 16777215);
                String s = Potion.getPotionDurationString(potioneffect, 1.0F);
                this.field_146289_q.drawStringWithShadow(s, (float)(i + 10 + 18), (float)(j + 6 + 10), 8355711);
                j += l;
            }
        }
    }
}

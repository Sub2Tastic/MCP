package net.minecraft.client.gui.recipebook;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;

public class GuiButtonRecipeTab extends GuiButtonToggle
{
    private final CreativeTabs category;
    private float animationTime;

    public GuiButtonRecipeTab(int p_i47588_1_, CreativeTabs p_i47588_2_)
    {
        super(p_i47588_1_, 0, 0, 35, 27, false);
        this.category = p_i47588_2_;
        this.initTextureValues(153, 2, 35, 0, GuiRecipeBook.RECIPE_BOOK);
    }

    public void startAnimation(Minecraft p_193918_1_)
    {
        RecipeBook recipebook = p_193918_1_.player.func_192035_E();
        label21:

        for (RecipeList recipelist : RecipeBookClient.field_194086_e.get(this.category))
        {
            Iterator iterator = recipelist.getRecipes(recipebook.isFilteringCraftable()).iterator();

            while (true)
            {
                if (!iterator.hasNext())
                {
                    continue label21;
                }

                IRecipe irecipe = (IRecipe)iterator.next();

                if (recipebook.isNew(irecipe))
                {
                    break;
                }
            }

            this.animationTime = 15.0F;
            return;
        }
    }

    public void func_191745_a(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_)
    {
        if (this.field_146125_m)
        {
            if (this.animationTime > 0.0F)
            {
                float f = 1.0F + 0.1F * (float)Math.sin((double)(this.animationTime / 15.0F * (float)Math.PI));
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b((float)(this.field_146128_h + 8), (float)(this.field_146129_i + 12), 0.0F);
                GlStateManager.func_179152_a(1.0F, f, 1.0F);
                GlStateManager.func_179109_b((float)(-(this.field_146128_h + 8)), (float)(-(this.field_146129_i + 12)), 0.0F);
            }

            this.field_146123_n = p_191745_2_ >= this.field_146128_h && p_191745_3_ >= this.field_146129_i && p_191745_2_ < this.field_146128_h + this.field_146120_f && p_191745_3_ < this.field_146129_i + this.field_146121_g;
            p_191745_1_.getTextureManager().bindTexture(this.resourceLocation);
            GlStateManager.func_179097_i();
            int k = this.xTexStart;
            int i = this.yTexStart;

            if (this.stateTriggered)
            {
                k += this.xDiffTex;
            }

            if (this.field_146123_n)
            {
                i += this.yDiffTex;
            }

            int j = this.field_146128_h;

            if (this.stateTriggered)
            {
                j -= 2;
            }

            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            this.func_73729_b(j, this.field_146129_i, k, i, this.field_146120_f, this.field_146121_g);
            GlStateManager.func_179126_j();
            RenderHelper.func_74520_c();
            GlStateManager.func_179140_f();
            this.renderIcon(p_191745_1_.getItemRenderer());
            GlStateManager.func_179145_e();
            RenderHelper.disableStandardItemLighting();

            if (this.animationTime > 0.0F)
            {
                GlStateManager.func_179121_F();
                this.animationTime -= p_191745_4_;
            }
        }
    }

    private void renderIcon(RenderItem p_193920_1_)
    {
        ItemStack itemstack = this.category.getIcon();

        if (this.category == CreativeTabs.TOOLS)
        {
            p_193920_1_.renderItemAndEffectIntoGUI(itemstack, this.field_146128_h + 3, this.field_146129_i + 5);
            p_193920_1_.renderItemAndEffectIntoGUI(CreativeTabs.COMBAT.getIcon(), this.field_146128_h + 14, this.field_146129_i + 5);
        }
        else if (this.category == CreativeTabs.MISC)
        {
            p_193920_1_.renderItemAndEffectIntoGUI(itemstack, this.field_146128_h + 3, this.field_146129_i + 5);
            p_193920_1_.renderItemAndEffectIntoGUI(CreativeTabs.FOOD.getIcon(), this.field_146128_h + 14, this.field_146129_i + 5);
        }
        else
        {
            p_193920_1_.renderItemAndEffectIntoGUI(itemstack, this.field_146128_h + 9, this.field_146129_i + 5);
        }
    }

    public CreativeTabs func_191764_e()
    {
        return this.category;
    }

    public boolean func_193919_e()
    {
        List<RecipeList> list = (List)RecipeBookClient.field_194086_e.get(this.category);
        this.field_146125_m = false;

        for (RecipeList recipelist : list)
        {
            if (recipelist.isNotEmpty() && recipelist.containsValidRecipes())
            {
                this.field_146125_m = true;
                break;
            }
        }

        return this.field_146125_m;
    }
}

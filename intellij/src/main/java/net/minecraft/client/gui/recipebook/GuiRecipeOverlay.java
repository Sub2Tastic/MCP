package net.minecraft.client.gui.recipebook;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiRecipeOverlay extends Gui
{
    private static final ResourceLocation RECIPE_BOOK_TEXTURE = new ResourceLocation("textures/gui/recipe_book.png");
    private final List<GuiRecipeOverlay.Button> buttonList = Lists.<GuiRecipeOverlay.Button>newArrayList();
    private boolean visible;
    private int x;
    private int y;
    private Minecraft mc;
    private RecipeList recipeList;
    private IRecipe lastRecipeClicked;
    private float time;

    public void func_191845_a(Minecraft p_191845_1_, RecipeList p_191845_2_, int p_191845_3_, int p_191845_4_, int p_191845_5_, int p_191845_6_, float p_191845_7_, RecipeBook p_191845_8_)
    {
        this.mc = p_191845_1_;
        this.recipeList = p_191845_2_;
        boolean flag = p_191845_8_.isFilteringCraftable();
        List<IRecipe> list = p_191845_2_.getDisplayRecipes(true);
        List<IRecipe> list1 = flag ? Collections.emptyList() : p_191845_2_.getDisplayRecipes(false);
        int i = list.size();
        int j = i + list1.size();
        int k = j <= 16 ? 4 : 5;
        int l = (int)Math.ceil((double)((float)j / (float)k));
        this.x = p_191845_3_;
        this.y = p_191845_4_;
        int i1 = 25;
        float f = (float)(this.x + Math.min(j, k) * 25);
        float f1 = (float)(p_191845_5_ + 50);

        if (f > f1)
        {
            this.x = (int)((float)this.x - p_191845_7_ * (float)((int)((f - f1) / p_191845_7_)));
        }

        float f2 = (float)(this.y + l * 25);
        float f3 = (float)(p_191845_6_ + 50);

        if (f2 > f3)
        {
            this.y = (int)((float)this.y - p_191845_7_ * (float)MathHelper.ceil((f2 - f3) / p_191845_7_));
        }

        float f4 = (float)this.y;
        float f5 = (float)(p_191845_6_ - 100);

        if (f4 < f5)
        {
            this.y = (int)((float)this.y - p_191845_7_ * (float)MathHelper.ceil((f4 - f5) / p_191845_7_));
        }

        this.visible = true;
        this.buttonList.clear();

        for (int j1 = 0; j1 < j; ++j1)
        {
            boolean flag1 = j1 < i;
            this.buttonList.add(new GuiRecipeOverlay.Button(this.x + 4 + 25 * (j1 % k), this.y + 5 + 25 * (j1 / k), flag1 ? (IRecipe)list.get(j1) : (IRecipe)list1.get(j1 - i), flag1));
        }

        this.lastRecipeClicked = null;
    }

    public RecipeList getRecipeList()
    {
        return this.recipeList;
    }

    public IRecipe getLastRecipeClicked()
    {
        return this.lastRecipeClicked;
    }

    public boolean func_193968_a(int p_193968_1_, int p_193968_2_, int p_193968_3_)
    {
        if (p_193968_3_ != 0)
        {
            return false;
        }
        else
        {
            for (GuiRecipeOverlay.Button guirecipeoverlay$button : this.buttonList)
            {
                if (guirecipeoverlay$button.func_146116_c(this.mc, p_193968_1_, p_193968_2_))
                {
                    this.lastRecipeClicked = guirecipeoverlay$button.recipe;
                    return true;
                }
            }

            return false;
        }
    }

    public void func_191842_a(int p_191842_1_, int p_191842_2_, float p_191842_3_)
    {
        if (this.visible)
        {
            this.time += p_191842_3_;
            RenderHelper.func_74520_c();
            GlStateManager.func_179147_l();
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(RECIPE_BOOK_TEXTURE);
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(0.0F, 0.0F, 170.0F);
            int i = this.buttonList.size() <= 16 ? 4 : 5;
            int j = Math.min(this.buttonList.size(), i);
            int k = MathHelper.ceil((float)this.buttonList.size() / (float)i);
            int l = 24;
            int i1 = 4;
            int j1 = 82;
            int k1 = 208;
            this.nineInchSprite(j, k, 24, 4, 82, 208);
            GlStateManager.func_179084_k();
            RenderHelper.disableStandardItemLighting();

            for (GuiRecipeOverlay.Button guirecipeoverlay$button : this.buttonList)
            {
                guirecipeoverlay$button.func_191745_a(this.mc, p_191842_1_, p_191842_2_, p_191842_3_);
            }

            GlStateManager.func_179121_F();
        }
    }

    private void nineInchSprite(int p_191846_1_, int p_191846_2_, int p_191846_3_, int p_191846_4_, int p_191846_5_, int p_191846_6_)
    {
        this.func_73729_b(this.x, this.y, p_191846_5_, p_191846_6_, p_191846_4_, p_191846_4_);
        this.func_73729_b(this.x + p_191846_4_ * 2 + p_191846_1_ * p_191846_3_, this.y, p_191846_5_ + p_191846_3_ + p_191846_4_, p_191846_6_, p_191846_4_, p_191846_4_);
        this.func_73729_b(this.x, this.y + p_191846_4_ * 2 + p_191846_2_ * p_191846_3_, p_191846_5_, p_191846_6_ + p_191846_3_ + p_191846_4_, p_191846_4_, p_191846_4_);
        this.func_73729_b(this.x + p_191846_4_ * 2 + p_191846_1_ * p_191846_3_, this.y + p_191846_4_ * 2 + p_191846_2_ * p_191846_3_, p_191846_5_ + p_191846_3_ + p_191846_4_, p_191846_6_ + p_191846_3_ + p_191846_4_, p_191846_4_, p_191846_4_);

        for (int i = 0; i < p_191846_1_; ++i)
        {
            this.func_73729_b(this.x + p_191846_4_ + i * p_191846_3_, this.y, p_191846_5_ + p_191846_4_, p_191846_6_, p_191846_3_, p_191846_4_);
            this.func_73729_b(this.x + p_191846_4_ + (i + 1) * p_191846_3_, this.y, p_191846_5_ + p_191846_4_, p_191846_6_, p_191846_4_, p_191846_4_);

            for (int j = 0; j < p_191846_2_; ++j)
            {
                if (i == 0)
                {
                    this.func_73729_b(this.x, this.y + p_191846_4_ + j * p_191846_3_, p_191846_5_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_3_);
                    this.func_73729_b(this.x, this.y + p_191846_4_ + (j + 1) * p_191846_3_, p_191846_5_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_4_);
                }

                this.func_73729_b(this.x + p_191846_4_ + i * p_191846_3_, this.y + p_191846_4_ + j * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_3_, p_191846_3_);
                this.func_73729_b(this.x + p_191846_4_ + (i + 1) * p_191846_3_, this.y + p_191846_4_ + j * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_3_);
                this.func_73729_b(this.x + p_191846_4_ + i * p_191846_3_, this.y + p_191846_4_ + (j + 1) * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_3_, p_191846_4_);
                this.func_73729_b(this.x + p_191846_4_ + (i + 1) * p_191846_3_ - 1, this.y + p_191846_4_ + (j + 1) * p_191846_3_ - 1, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_4_ + 1, p_191846_4_ + 1);

                if (i == p_191846_1_ - 1)
                {
                    this.func_73729_b(this.x + p_191846_4_ * 2 + p_191846_1_ * p_191846_3_, this.y + p_191846_4_ + j * p_191846_3_, p_191846_5_ + p_191846_3_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_3_);
                    this.func_73729_b(this.x + p_191846_4_ * 2 + p_191846_1_ * p_191846_3_, this.y + p_191846_4_ + (j + 1) * p_191846_3_, p_191846_5_ + p_191846_3_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_4_);
                }
            }

            this.func_73729_b(this.x + p_191846_4_ + i * p_191846_3_, this.y + p_191846_4_ * 2 + p_191846_2_ * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_3_ + p_191846_4_, p_191846_3_, p_191846_4_);
            this.func_73729_b(this.x + p_191846_4_ + (i + 1) * p_191846_3_, this.y + p_191846_4_ * 2 + p_191846_2_ * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_3_ + p_191846_4_, p_191846_4_, p_191846_4_);
        }
    }

    public void setVisible(boolean p_192999_1_)
    {
        this.visible = p_192999_1_;
    }

    public boolean isVisible()
    {
        return this.visible;
    }

    class Button extends GuiButton
    {
        private final IRecipe recipe;
        private final boolean isCraftable;

        public Button(int p_i47594_2_, int p_i47594_3_, IRecipe p_i47594_4_, boolean p_i47594_5_)
        {
            super(0, p_i47594_2_, p_i47594_3_, "");
            this.field_146120_f = 24;
            this.field_146121_g = 24;
            this.recipe = p_i47594_4_;
            this.isCraftable = p_i47594_5_;
        }

        public void func_191745_a(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_)
        {
            RenderHelper.func_74520_c();
            GlStateManager.func_179141_d();
            p_191745_1_.getTextureManager().bindTexture(GuiRecipeOverlay.RECIPE_BOOK_TEXTURE);
            this.field_146123_n = p_191745_2_ >= this.field_146128_h && p_191745_3_ >= this.field_146129_i && p_191745_2_ < this.field_146128_h + this.field_146120_f && p_191745_3_ < this.field_146129_i + this.field_146121_g;
            int i = 152;

            if (!this.isCraftable)
            {
                i += 26;
            }

            int j = 78;

            if (this.field_146123_n)
            {
                j += 26;
            }

            this.func_73729_b(this.field_146128_h, this.field_146129_i, i, j, this.field_146120_f, this.field_146121_g);
            int k = 3;
            int l = 3;

            if (this.recipe instanceof ShapedRecipes)
            {
                ShapedRecipes shapedrecipes = (ShapedRecipes)this.recipe;
                k = shapedrecipes.getWidth();
                l = shapedrecipes.getHeight();
            }

            Iterator<Ingredient> iterator = this.recipe.getIngredients().iterator();

            for (int i1 = 0; i1 < l; ++i1)
            {
                int j1 = 3 + i1 * 7;

                for (int k1 = 0; k1 < k; ++k1)
                {
                    if (iterator.hasNext())
                    {
                        ItemStack[] aitemstack = ((Ingredient)iterator.next()).getMatchingStacks();

                        if (aitemstack.length != 0)
                        {
                            int l1 = 3 + k1 * 7;
                            GlStateManager.func_179094_E();
                            float f = 0.42F;
                            int i2 = (int)((float)(this.field_146128_h + l1) / 0.42F - 3.0F);
                            int j2 = (int)((float)(this.field_146129_i + j1) / 0.42F - 3.0F);
                            GlStateManager.func_179152_a(0.42F, 0.42F, 1.0F);
                            GlStateManager.func_179145_e();
                            p_191745_1_.getItemRenderer().renderItemAndEffectIntoGUI(aitemstack[MathHelper.floor(GuiRecipeOverlay.this.time / 30.0F) % aitemstack.length], i2, j2);
                            GlStateManager.func_179140_f();
                            GlStateManager.func_179121_F();
                        }
                    }
                }
            }

            GlStateManager.func_179118_c();
            RenderHelper.disableStandardItemLighting();
        }
    }
}

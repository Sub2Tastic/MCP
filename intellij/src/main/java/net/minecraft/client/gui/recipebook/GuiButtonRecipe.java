package net.minecraft.client.gui.recipebook;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiButtonRecipe extends GuiButton
{
    private static final ResourceLocation RECIPE_BOOK = new ResourceLocation("textures/gui/recipe_book.png");
    private RecipeBook book;
    private RecipeList list;
    private float time;
    private float animationTime;
    private int currentIndex;

    public GuiButtonRecipe()
    {
        super(0, 0, 0, 25, 25, "");
    }

    public void func_193928_a(RecipeList p_193928_1_, RecipeBookPage p_193928_2_, RecipeBook p_193928_3_)
    {
        this.list = p_193928_1_;
        this.book = p_193928_3_;
        List<IRecipe> list = p_193928_1_.getRecipes(p_193928_3_.isFilteringCraftable());

        for (IRecipe irecipe : list)
        {
            if (p_193928_3_.isNew(irecipe))
            {
                p_193928_2_.recipesShown(list);
                this.animationTime = 15.0F;
                break;
            }
        }
    }

    public RecipeList getList()
    {
        return this.list;
    }

    public void setPosition(int p_191770_1_, int p_191770_2_)
    {
        this.field_146128_h = p_191770_1_;
        this.field_146129_i = p_191770_2_;
    }

    public void func_191745_a(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_)
    {
        if (this.field_146125_m)
        {
            if (!GuiScreen.func_146271_m())
            {
                this.time += p_191745_4_;
            }

            this.field_146123_n = p_191745_2_ >= this.field_146128_h && p_191745_3_ >= this.field_146129_i && p_191745_2_ < this.field_146128_h + this.field_146120_f && p_191745_3_ < this.field_146129_i + this.field_146121_g;
            RenderHelper.func_74520_c();
            p_191745_1_.getTextureManager().bindTexture(RECIPE_BOOK);
            GlStateManager.func_179140_f();
            int i = 29;

            if (!this.list.containsCraftableRecipes())
            {
                i += 25;
            }

            int j = 206;

            if (this.list.getRecipes(this.book.isFilteringCraftable()).size() > 1)
            {
                j += 25;
            }

            boolean flag = this.animationTime > 0.0F;

            if (flag)
            {
                float f = 1.0F + 0.1F * (float)Math.sin((double)(this.animationTime / 15.0F * (float)Math.PI));
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b((float)(this.field_146128_h + 8), (float)(this.field_146129_i + 12), 0.0F);
                GlStateManager.func_179152_a(f, f, 1.0F);
                GlStateManager.func_179109_b((float)(-(this.field_146128_h + 8)), (float)(-(this.field_146129_i + 12)), 0.0F);
                this.animationTime -= p_191745_4_;
            }

            this.func_73729_b(this.field_146128_h, this.field_146129_i, i, j, this.field_146120_f, this.field_146121_g);
            List<IRecipe> list = this.getOrderedRecipes();
            this.currentIndex = MathHelper.floor(this.time / 30.0F) % list.size();
            ItemStack itemstack = ((IRecipe)list.get(this.currentIndex)).getRecipeOutput();
            int k = 4;

            if (this.list.hasSingleResultItem() && this.getOrderedRecipes().size() > 1)
            {
                p_191745_1_.getItemRenderer().renderItemAndEffectIntoGUI(itemstack, this.field_146128_h + k + 1, this.field_146129_i + k + 1);
                --k;
            }

            p_191745_1_.getItemRenderer().renderItemAndEffectIntoGUI(itemstack, this.field_146128_h + k, this.field_146129_i + k);

            if (flag)
            {
                GlStateManager.func_179121_F();
            }

            GlStateManager.func_179145_e();
            RenderHelper.disableStandardItemLighting();
        }
    }

    private List<IRecipe> getOrderedRecipes()
    {
        List<IRecipe> list = this.list.getDisplayRecipes(true);

        if (!this.book.isFilteringCraftable())
        {
            list.addAll(this.list.getDisplayRecipes(false));
        }

        return list;
    }

    public boolean isOnlyOption()
    {
        return this.getOrderedRecipes().size() == 1;
    }

    public IRecipe getRecipe()
    {
        List<IRecipe> list = this.getOrderedRecipes();
        return list.get(this.currentIndex);
    }

    public List<String> getToolTipText(GuiScreen p_191772_1_)
    {
        ItemStack itemstack = ((IRecipe)this.getOrderedRecipes().get(this.currentIndex)).getRecipeOutput();
        List<String> list = p_191772_1_.func_191927_a(itemstack);

        if (this.list.getRecipes(this.book.isFilteringCraftable()).size() > 1)
        {
            list.add(I18n.format("gui.recipebook.moreRecipes"));
        }

        return list;
    }

    public int func_146117_b()
    {
        return 25;
    }
}

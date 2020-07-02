package net.minecraft.client.gui.recipebook;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class GuiRecipeBook extends Gui implements IRecipeUpdateListener
{
    protected static final ResourceLocation RECIPE_BOOK = new ResourceLocation("textures/gui/recipe_book.png");
    private int xOffset;
    private int width;
    private int height;
    private final GhostRecipe ghostRecipe = new GhostRecipe();
    private final List<GuiButtonRecipeTab> recipeTabs = Lists.newArrayList(new GuiButtonRecipeTab(0, CreativeTabs.SEARCH), new GuiButtonRecipeTab(0, CreativeTabs.TOOLS), new GuiButtonRecipeTab(0, CreativeTabs.BUILDING_BLOCKS), new GuiButtonRecipeTab(0, CreativeTabs.MISC), new GuiButtonRecipeTab(0, CreativeTabs.REDSTONE));
    private GuiButtonRecipeTab currentTab;
    private GuiButtonToggle toggleRecipesBtn;
    private InventoryCrafting field_193961_o;
    private Minecraft mc;
    private GuiTextField searchBar;
    private String lastSearch = "";
    private RecipeBook recipeBook;
    private final RecipeBookPage recipeBookPage = new RecipeBookPage();
    private RecipeItemHelper stackedContents = new RecipeItemHelper();
    private int timesInventoryChanged;

    public void func_194303_a(int p_194303_1_, int p_194303_2_, Minecraft p_194303_3_, boolean p_194303_4_, InventoryCrafting p_194303_5_)
    {
        this.mc = p_194303_3_;
        this.width = p_194303_1_;
        this.height = p_194303_2_;
        this.field_193961_o = p_194303_5_;
        this.recipeBook = p_194303_3_.player.func_192035_E();
        this.timesInventoryChanged = p_194303_3_.player.inventory.getTimesChanged();
        this.currentTab = this.recipeTabs.get(0);
        this.currentTab.setStateTriggered(true);

        if (this.isVisible())
        {
            this.func_193014_a(p_194303_4_, p_194303_5_);
        }

        Keyboard.enableRepeatEvents(true);
    }

    public void func_193014_a(boolean p_193014_1_, InventoryCrafting p_193014_2_)
    {
        this.xOffset = p_193014_1_ ? 0 : 86;
        int i = (this.width - 147) / 2 - this.xOffset;
        int j = (this.height - 166) / 2;
        this.stackedContents.clear();
        this.mc.player.inventory.func_194016_a(this.stackedContents, false);
        p_193014_2_.fillStackedContents(this.stackedContents);
        this.searchBar = new GuiTextField(0, this.mc.fontRenderer, i + 25, j + 14, 80, this.mc.fontRenderer.FONT_HEIGHT + 5);
        this.searchBar.setMaxStringLength(50);
        this.searchBar.setEnableBackgroundDrawing(false);
        this.searchBar.setVisible(true);
        this.searchBar.setTextColor(16777215);
        this.recipeBookPage.init(this.mc, i, j);
        this.recipeBookPage.addListener(this);
        this.toggleRecipesBtn = new GuiButtonToggle(0, i + 110, j + 12, 26, 16, this.recipeBook.isFilteringCraftable());
        this.toggleRecipesBtn.initTextureValues(152, 41, 28, 18, RECIPE_BOOK);
        this.updateCollections(false);
        this.updateTabs();
    }

    public void removed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    public int updateScreenPosition(boolean p_193011_1_, int p_193011_2_, int p_193011_3_)
    {
        int i;

        if (this.isVisible() && !p_193011_1_)
        {
            i = 177 + (p_193011_2_ - p_193011_3_ - 200) / 2;
        }
        else
        {
            i = (p_193011_2_ - p_193011_3_) / 2;
        }

        return i;
    }

    public void toggleVisibility()
    {
        this.setVisible(!this.isVisible());
    }

    public boolean isVisible()
    {
        return this.recipeBook.isGuiOpen();
    }

    private void setVisible(boolean p_193006_1_)
    {
        this.recipeBook.setGuiOpen(p_193006_1_);

        if (!p_193006_1_)
        {
            this.recipeBookPage.setInvisible();
        }

        this.sendUpdateSettings();
    }

    public void slotClicked(@Nullable Slot slotIn)
    {
        if (slotIn != null && slotIn.slotNumber <= 9)
        {
            this.ghostRecipe.clear();

            if (this.isVisible())
            {
                this.updateStackedContents();
            }
        }
    }

    private void updateCollections(boolean p_193003_1_)
    {
        List<RecipeList> list = (List)RecipeBookClient.field_194086_e.get(this.currentTab.func_191764_e());
        list.forEach((p_193944_1_) ->
        {
            p_193944_1_.canCraft(this.stackedContents, this.field_193961_o.getWidth(), this.field_193961_o.getHeight(), this.recipeBook);
        });
        List<RecipeList> list1 = Lists.newArrayList(list);
        list1.removeIf((p_193952_0_) ->
        {
            return !p_193952_0_.isNotEmpty();
        });
        list1.removeIf((p_193953_0_) ->
        {
            return !p_193953_0_.containsValidRecipes();
        });
        String s = this.searchBar.getText();

        if (!s.isEmpty())
        {
            ObjectSet<RecipeList> objectset = new ObjectLinkedOpenHashSet<RecipeList>(this.mc.func_193987_a(SearchTreeManager.RECIPES).search(s.toLowerCase(Locale.ROOT)));
            list1.removeIf((p_193947_1_) ->
            {
                return !objectset.contains(p_193947_1_);
            });
        }

        if (this.recipeBook.isFilteringCraftable())
        {
            list1.removeIf((p_193958_0_) ->
            {
                return !p_193958_0_.containsCraftableRecipes();
            });
        }

        this.recipeBookPage.updateLists(list1, p_193003_1_);
    }

    private void updateTabs()
    {
        int i = (this.width - 147) / 2 - this.xOffset - 30;
        int j = (this.height - 166) / 2 + 3;
        int k = 27;
        int l = 0;

        for (GuiButtonRecipeTab guibuttonrecipetab : this.recipeTabs)
        {
            CreativeTabs creativetabs = guibuttonrecipetab.func_191764_e();

            if (creativetabs == CreativeTabs.SEARCH)
            {
                guibuttonrecipetab.field_146125_m = true;
                guibuttonrecipetab.setPosition(i, j + 27 * l++);
            }
            else if (guibuttonrecipetab.func_193919_e())
            {
                guibuttonrecipetab.setPosition(i, j + 27 * l++);
                guibuttonrecipetab.startAnimation(this.mc);
            }
        }
    }

    public void tick()
    {
        if (this.isVisible())
        {
            if (this.timesInventoryChanged != this.mc.player.inventory.getTimesChanged())
            {
                this.updateStackedContents();
                this.timesInventoryChanged = this.mc.player.inventory.getTimesChanged();
            }
        }
    }

    private void updateStackedContents()
    {
        this.stackedContents.clear();
        this.mc.player.inventory.func_194016_a(this.stackedContents, false);
        this.field_193961_o.fillStackedContents(this.stackedContents);
        this.updateCollections(false);
    }

    public void func_191861_a(int p_191861_1_, int p_191861_2_, float p_191861_3_)
    {
        if (this.isVisible())
        {
            RenderHelper.func_74520_c();
            GlStateManager.func_179140_f();
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(0.0F, 0.0F, 100.0F);
            this.mc.getTextureManager().bindTexture(RECIPE_BOOK);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            int i = (this.width - 147) / 2 - this.xOffset;
            int j = (this.height - 166) / 2;
            this.func_73729_b(i, j, 1, 1, 147, 166);
            this.searchBar.func_146194_f();
            RenderHelper.disableStandardItemLighting();

            for (GuiButtonRecipeTab guibuttonrecipetab : this.recipeTabs)
            {
                guibuttonrecipetab.func_191745_a(this.mc, p_191861_1_, p_191861_2_, p_191861_3_);
            }

            this.toggleRecipesBtn.func_191745_a(this.mc, p_191861_1_, p_191861_2_, p_191861_3_);
            this.recipeBookPage.render(i, j, p_191861_1_, p_191861_2_, p_191861_3_);
            GlStateManager.func_179121_F();
        }
    }

    public void renderTooltip(int p_191876_1_, int p_191876_2_, int p_191876_3_, int p_191876_4_)
    {
        if (this.isVisible())
        {
            this.recipeBookPage.renderTooltip(p_191876_3_, p_191876_4_);

            if (this.toggleRecipesBtn.func_146115_a())
            {
                String s1 = I18n.format(this.toggleRecipesBtn.isStateTriggered() ? "gui.recipebook.toggleRecipes.craftable" : "gui.recipebook.toggleRecipes.all");

                if (this.mc.currentScreen != null)
                {
                    this.mc.currentScreen.func_146279_a(s1, p_191876_3_, p_191876_4_);
                }
            }

            this.renderGhostRecipeTooltip(p_191876_1_, p_191876_2_, p_191876_3_, p_191876_4_);
        }
    }

    private void renderGhostRecipeTooltip(int p_193015_1_, int p_193015_2_, int p_193015_3_, int p_193015_4_)
    {
        ItemStack itemstack = null;

        for (int i = 0; i < this.ghostRecipe.size(); ++i)
        {
            GhostRecipe.GhostIngredient ghostrecipe$ghostingredient = this.ghostRecipe.get(i);
            int j = ghostrecipe$ghostingredient.getX() + p_193015_1_;
            int k = ghostrecipe$ghostingredient.getY() + p_193015_2_;

            if (p_193015_3_ >= j && p_193015_4_ >= k && p_193015_3_ < j + 16 && p_193015_4_ < k + 16)
            {
                itemstack = ghostrecipe$ghostingredient.getItem();
            }
        }

        if (itemstack != null && this.mc.currentScreen != null)
        {
            this.mc.currentScreen.func_146283_a(this.mc.currentScreen.func_191927_a(itemstack), p_193015_3_, p_193015_4_);
        }
    }

    public void renderGhostRecipe(int p_191864_1_, int p_191864_2_, boolean p_191864_3_, float p_191864_4_)
    {
        this.ghostRecipe.render(this.mc, p_191864_1_, p_191864_2_, p_191864_3_, p_191864_4_);
    }

    public boolean func_191862_a(int p_191862_1_, int p_191862_2_, int p_191862_3_)
    {
        if (this.isVisible() && !this.mc.player.isSpectator())
        {
            if (this.recipeBookPage.func_194196_a(p_191862_1_, p_191862_2_, p_191862_3_, (this.width - 147) / 2 - this.xOffset, (this.height - 166) / 2, 147, 166))
            {
                IRecipe irecipe = this.recipeBookPage.getLastClickedRecipe();
                RecipeList recipelist = this.recipeBookPage.getLastClickedRecipeList();

                if (irecipe != null && recipelist != null)
                {
                    if (!recipelist.isCraftable(irecipe) && this.ghostRecipe.getRecipe() == irecipe)
                    {
                        return false;
                    }

                    this.ghostRecipe.clear();
                    this.mc.playerController.func_194338_a(this.mc.player.openContainer.windowId, irecipe, GuiScreen.func_146272_n(), this.mc.player);

                    if (!this.isOffsetNextToMainGUI() && p_191862_3_ == 0)
                    {
                        this.setVisible(false);
                    }
                }

                return true;
            }
            else if (p_191862_3_ != 0)
            {
                return false;
            }
            else if (this.searchBar.func_146192_a(p_191862_1_, p_191862_2_, p_191862_3_))
            {
                return true;
            }
            else if (this.toggleRecipesBtn.func_146116_c(this.mc, p_191862_1_, p_191862_2_))
            {
                boolean flag = !this.recipeBook.isFilteringCraftable();
                this.recipeBook.setFilteringCraftable(flag);
                this.toggleRecipesBtn.setStateTriggered(flag);
                this.toggleRecipesBtn.func_146113_a(this.mc.getSoundHandler());
                this.sendUpdateSettings();
                this.updateCollections(false);
                return true;
            }
            else
            {
                for (GuiButtonRecipeTab guibuttonrecipetab : this.recipeTabs)
                {
                    if (guibuttonrecipetab.func_146116_c(this.mc, p_191862_1_, p_191862_2_))
                    {
                        if (this.currentTab != guibuttonrecipetab)
                        {
                            guibuttonrecipetab.func_146113_a(this.mc.getSoundHandler());
                            this.currentTab.setStateTriggered(false);
                            this.currentTab = guibuttonrecipetab;
                            this.currentTab.setStateTriggered(true);
                            this.updateCollections(true);
                        }

                        return true;
                    }
                }

                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean func_193955_c(int p_193955_1_, int p_193955_2_, int p_193955_3_, int p_193955_4_, int p_193955_5_, int p_193955_6_)
    {
        if (!this.isVisible())
        {
            return true;
        }
        else
        {
            boolean flag = p_193955_1_ < p_193955_3_ || p_193955_2_ < p_193955_4_ || p_193955_1_ >= p_193955_3_ + p_193955_5_ || p_193955_2_ >= p_193955_4_ + p_193955_6_;
            boolean flag1 = p_193955_3_ - 147 < p_193955_1_ && p_193955_1_ < p_193955_3_ && p_193955_4_ < p_193955_2_ && p_193955_2_ < p_193955_4_ + p_193955_6_;
            return flag && !flag1 && !this.currentTab.func_146116_c(this.mc, p_193955_1_, p_193955_2_);
        }
    }

    public boolean func_191859_a(char p_191859_1_, int p_191859_2_)
    {
        if (this.isVisible() && !this.mc.player.isSpectator())
        {
            if (p_191859_2_ == 1 && !this.isOffsetNextToMainGUI())
            {
                this.setVisible(false);
                return true;
            }
            else
            {
                if (GameSettings.func_100015_a(this.mc.gameSettings.keyBindChat) && !this.searchBar.func_146206_l())
                {
                    this.searchBar.setFocused2(true);
                }
                else if (this.searchBar.func_146201_a(p_191859_1_, p_191859_2_))
                {
                    String s1 = this.searchBar.getText().toLowerCase(Locale.ROOT);
                    this.pirateRecipe(s1);

                    if (!s1.equals(this.lastSearch))
                    {
                        this.updateCollections(false);
                        this.lastSearch = s1;
                    }

                    return true;
                }

                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * "Check if we should activate the pirate speak easter egg"
     */
    private void pirateRecipe(String text)
    {
        if ("excitedze".equals(text))
        {
            LanguageManager languagemanager = this.mc.getLanguageManager();
            Language language = languagemanager.getLanguage("en_pt");

            if (languagemanager.getCurrentLanguage().compareTo(language) == 0)
            {
                return;
            }

            languagemanager.setCurrentLanguage(language);
            this.mc.gameSettings.language = language.func_135034_a();
            this.mc.func_110436_a();
            this.mc.fontRenderer.func_78264_a(this.mc.getLanguageManager().func_135042_a() || this.mc.gameSettings.field_151455_aw);
            this.mc.fontRenderer.setBidiFlag(languagemanager.isCurrentLanguageBidirectional());
            this.mc.gameSettings.saveOptions();
        }
    }

    private boolean isOffsetNextToMainGUI()
    {
        return this.xOffset == 86;
    }

    public void recipesUpdated()
    {
        this.updateTabs();

        if (this.isVisible())
        {
            this.updateCollections(false);
        }
    }

    public void recipesShown(List<IRecipe> recipes)
    {
        for (IRecipe irecipe : recipes)
        {
            this.mc.player.removeRecipeHighlight(irecipe);
        }
    }

    public void setupGhostRecipe(IRecipe p_193951_1_, List<Slot> p_193951_2_)
    {
        ItemStack itemstack = p_193951_1_.getRecipeOutput();
        this.ghostRecipe.setRecipe(p_193951_1_);
        this.ghostRecipe.addIngredient(Ingredient.fromStacks(itemstack), (p_193951_2_.get(0)).xPos, (p_193951_2_.get(0)).yPos);
        int i = this.field_193961_o.getWidth();
        int j = this.field_193961_o.getHeight();
        int k = p_193951_1_ instanceof ShapedRecipes ? ((ShapedRecipes)p_193951_1_).getWidth() : i;
        int l = 1;
        Iterator<Ingredient> iterator = p_193951_1_.getIngredients().iterator();

        for (int i1 = 0; i1 < j; ++i1)
        {
            for (int j1 = 0; j1 < k; ++j1)
            {
                if (!iterator.hasNext())
                {
                    return;
                }

                Ingredient ingredient = iterator.next();

                if (ingredient != Ingredient.EMPTY)
                {
                    Slot slot = p_193951_2_.get(l);
                    this.ghostRecipe.addIngredient(ingredient, slot.xPos, slot.yPos);
                }

                ++l;
            }

            if (k < i)
            {
                l += i - k;
            }
        }
    }

    private void sendUpdateSettings()
    {
        if (this.mc.getConnection() != null)
        {
            this.mc.getConnection().sendPacket(new CPacketRecipeInfo(this.isVisible(), this.recipeBook.isFilteringCraftable()));
        }
    }
}

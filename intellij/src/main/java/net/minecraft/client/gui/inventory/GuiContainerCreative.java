package net.minecraft.client.gui.inventory;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiContainerCreative extends InventoryEffectRenderer
{
    /** The location of the creative inventory tabs texture */
    private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private static final InventoryBasic field_147060_v = new InventoryBasic("tmp", true, 45);

    /** Currently selected creative inventory tab index. */
    private static int selectedTabIndex = CreativeTabs.BUILDING_BLOCKS.getIndex();

    /** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
    private float currentScroll;

    /** True if the scrollbar is being dragged */
    private boolean isScrolling;
    private boolean field_147065_z;
    private GuiTextField searchField;
    private List<Slot> originalSlots;
    private Slot destroyItemSlot;
    private boolean field_147057_D;
    private CreativeCrafting listener;

    public GuiContainerCreative(EntityPlayer player)
    {
        super(new GuiContainerCreative.ContainerCreative(player));
        player.openContainer = this.container;
        this.field_146291_p = true;
        this.ySize = 136;
        this.xSize = 195;
    }

    public void func_73876_c()
    {
        if (!this.field_146297_k.playerController.isInCreativeMode())
        {
            this.field_146297_k.displayGuiScreen(new GuiInventory(this.field_146297_k.player));
        }
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    protected void handleMouseClick(@Nullable Slot slotIn, int slotId, int mouseButton, ClickType type)
    {
        this.field_147057_D = true;
        boolean flag = type == ClickType.QUICK_MOVE;
        type = slotId == -999 && type == ClickType.PICKUP ? ClickType.THROW : type;

        if (slotIn == null && selectedTabIndex != CreativeTabs.INVENTORY.getIndex() && type != ClickType.QUICK_CRAFT)
        {
            InventoryPlayer inventoryplayer1 = this.field_146297_k.player.inventory;

            if (!inventoryplayer1.getItemStack().isEmpty())
            {
                if (mouseButton == 0)
                {
                    this.field_146297_k.player.dropItem(inventoryplayer1.getItemStack(), true);
                    this.field_146297_k.playerController.sendPacketDropItem(inventoryplayer1.getItemStack());
                    inventoryplayer1.setItemStack(ItemStack.EMPTY);
                }

                if (mouseButton == 1)
                {
                    ItemStack itemstack6 = inventoryplayer1.getItemStack().split(1);
                    this.field_146297_k.player.dropItem(itemstack6, true);
                    this.field_146297_k.playerController.sendPacketDropItem(itemstack6);
                }
            }
        }
        else
        {
            if (slotIn != null && !slotIn.canTakeStack(this.field_146297_k.player))
            {
                return;
            }

            if (slotIn == this.destroyItemSlot && flag)
            {
                for (int j = 0; j < this.field_146297_k.player.container.getInventory().size(); ++j)
                {
                    this.field_146297_k.playerController.sendSlotPacket(ItemStack.EMPTY, j);
                }
            }
            else if (selectedTabIndex == CreativeTabs.INVENTORY.getIndex())
            {
                if (slotIn == this.destroyItemSlot)
                {
                    this.field_146297_k.player.inventory.setItemStack(ItemStack.EMPTY);
                }
                else if (type == ClickType.THROW && slotIn != null && slotIn.getHasStack())
                {
                    ItemStack itemstack = slotIn.decrStackSize(mouseButton == 0 ? 1 : slotIn.getStack().getMaxStackSize());
                    ItemStack itemstack1 = slotIn.getStack();
                    this.field_146297_k.player.dropItem(itemstack, true);
                    this.field_146297_k.playerController.sendPacketDropItem(itemstack);
                    this.field_146297_k.playerController.sendSlotPacket(itemstack1, ((GuiContainerCreative.CreativeSlot)slotIn).slot.slotNumber);
                }
                else if (type == ClickType.THROW && !this.field_146297_k.player.inventory.getItemStack().isEmpty())
                {
                    this.field_146297_k.player.dropItem(this.field_146297_k.player.inventory.getItemStack(), true);
                    this.field_146297_k.playerController.sendPacketDropItem(this.field_146297_k.player.inventory.getItemStack());
                    this.field_146297_k.player.inventory.setItemStack(ItemStack.EMPTY);
                }
                else
                {
                    this.field_146297_k.player.container.slotClick(slotIn == null ? slotId : ((GuiContainerCreative.CreativeSlot)slotIn).slot.slotNumber, mouseButton, type, this.field_146297_k.player);
                    this.field_146297_k.player.container.detectAndSendChanges();
                }
            }
            else if (type != ClickType.QUICK_CRAFT && slotIn.inventory == field_147060_v)
            {
                InventoryPlayer inventoryplayer = this.field_146297_k.player.inventory;
                ItemStack itemstack5 = inventoryplayer.getItemStack();
                ItemStack itemstack7 = slotIn.getStack();

                if (type == ClickType.SWAP)
                {
                    if (!itemstack7.isEmpty() && mouseButton >= 0 && mouseButton < 9)
                    {
                        ItemStack itemstack10 = itemstack7.copy();
                        itemstack10.setCount(itemstack10.getMaxStackSize());
                        this.field_146297_k.player.inventory.setInventorySlotContents(mouseButton, itemstack10);
                        this.field_146297_k.player.container.detectAndSendChanges();
                    }

                    return;
                }

                if (type == ClickType.CLONE)
                {
                    if (inventoryplayer.getItemStack().isEmpty() && slotIn.getHasStack())
                    {
                        ItemStack itemstack9 = slotIn.getStack().copy();
                        itemstack9.setCount(itemstack9.getMaxStackSize());
                        inventoryplayer.setItemStack(itemstack9);
                    }

                    return;
                }

                if (type == ClickType.THROW)
                {
                    if (!itemstack7.isEmpty())
                    {
                        ItemStack itemstack8 = itemstack7.copy();
                        itemstack8.setCount(mouseButton == 0 ? 1 : itemstack8.getMaxStackSize());
                        this.field_146297_k.player.dropItem(itemstack8, true);
                        this.field_146297_k.playerController.sendPacketDropItem(itemstack8);
                    }

                    return;
                }

                if (!itemstack5.isEmpty() && !itemstack7.isEmpty() && itemstack5.isItemEqual(itemstack7) && ItemStack.areItemStackTagsEqual(itemstack5, itemstack7))
                {
                    if (mouseButton == 0)
                    {
                        if (flag)
                        {
                            itemstack5.setCount(itemstack5.getMaxStackSize());
                        }
                        else if (itemstack5.getCount() < itemstack5.getMaxStackSize())
                        {
                            itemstack5.grow(1);
                        }
                    }
                    else
                    {
                        itemstack5.shrink(1);
                    }
                }
                else if (!itemstack7.isEmpty() && itemstack5.isEmpty())
                {
                    inventoryplayer.setItemStack(itemstack7.copy());
                    itemstack5 = inventoryplayer.getItemStack();

                    if (flag)
                    {
                        itemstack5.setCount(itemstack5.getMaxStackSize());
                    }
                }
                else if (mouseButton == 0)
                {
                    inventoryplayer.setItemStack(ItemStack.EMPTY);
                }
                else
                {
                    inventoryplayer.getItemStack().shrink(1);
                }
            }
            else if (this.container != null)
            {
                ItemStack itemstack3 = slotIn == null ? ItemStack.EMPTY : this.container.getSlot(slotIn.slotNumber).getStack();
                this.container.slotClick(slotIn == null ? slotId : slotIn.slotNumber, mouseButton, type, this.field_146297_k.player);

                if (Container.getDragEvent(mouseButton) == 2)
                {
                    for (int k = 0; k < 9; ++k)
                    {
                        this.field_146297_k.playerController.sendSlotPacket(this.container.getSlot(45 + k).getStack(), 36 + k);
                    }
                }
                else if (slotIn != null)
                {
                    ItemStack itemstack4 = this.container.getSlot(slotIn.slotNumber).getStack();
                    this.field_146297_k.playerController.sendSlotPacket(itemstack4, slotIn.slotNumber - this.container.inventorySlots.size() + 9 + 36);
                    int i = 45 + mouseButton;

                    if (type == ClickType.SWAP)
                    {
                        this.field_146297_k.playerController.sendSlotPacket(itemstack3, i - this.container.inventorySlots.size() + 9 + 36);
                    }
                    else if (type == ClickType.THROW && !itemstack3.isEmpty())
                    {
                        ItemStack itemstack2 = itemstack3.copy();
                        itemstack2.setCount(mouseButton == 0 ? 1 : itemstack2.getMaxStackSize());
                        this.field_146297_k.player.dropItem(itemstack2, true);
                        this.field_146297_k.playerController.sendPacketDropItem(itemstack2);
                    }

                    this.field_146297_k.player.container.detectAndSendChanges();
                }
            }
        }
    }

    protected void updateActivePotionEffects()
    {
        int i = this.guiLeft;
        super.updateActivePotionEffects();

        if (this.searchField != null && this.guiLeft != i)
        {
            this.searchField.field_146209_f = this.guiLeft + 82;
        }
    }

    public void func_73866_w_()
    {
        if (this.field_146297_k.playerController.isInCreativeMode())
        {
            super.func_73866_w_();
            this.field_146292_n.clear();
            Keyboard.enableRepeatEvents(true);
            this.searchField = new GuiTextField(0, this.field_146289_q, this.guiLeft + 82, this.guiTop + 6, 80, this.field_146289_q.FONT_HEIGHT);
            this.searchField.setMaxStringLength(50);
            this.searchField.setEnableBackgroundDrawing(false);
            this.searchField.setVisible(false);
            this.searchField.setTextColor(16777215);
            int i = selectedTabIndex;
            selectedTabIndex = -1;
            this.setCurrentCreativeTab(CreativeTabs.GROUPS[i]);
            this.listener = new CreativeCrafting(this.field_146297_k);
            this.field_146297_k.player.container.addListener(this.listener);
        }
        else
        {
            this.field_146297_k.displayGuiScreen(new GuiInventory(this.field_146297_k.player));
        }
    }

    public void func_146281_b()
    {
        super.func_146281_b();

        if (this.field_146297_k.player != null && this.field_146297_k.player.inventory != null)
        {
            this.field_146297_k.player.container.removeListener(this.listener);
        }

        Keyboard.enableRepeatEvents(false);
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (selectedTabIndex != CreativeTabs.SEARCH.getIndex())
        {
            if (GameSettings.func_100015_a(this.field_146297_k.gameSettings.keyBindChat))
            {
                this.setCurrentCreativeTab(CreativeTabs.SEARCH);
            }
            else
            {
                super.func_73869_a(p_73869_1_, p_73869_2_);
            }
        }
        else
        {
            if (this.field_147057_D)
            {
                this.field_147057_D = false;
                this.searchField.setText("");
            }

            if (!this.func_146983_a(p_73869_2_))
            {
                if (this.searchField.func_146201_a(p_73869_1_, p_73869_2_))
                {
                    this.updateCreativeSearch();
                }
                else
                {
                    super.func_73869_a(p_73869_1_, p_73869_2_);
                }
            }
        }
    }

    private void updateCreativeSearch()
    {
        GuiContainerCreative.ContainerCreative guicontainercreative$containercreative = (GuiContainerCreative.ContainerCreative)this.container;
        guicontainercreative$containercreative.itemList.clear();

        if (this.searchField.getText().isEmpty())
        {
            for (Item item : Item.field_150901_e)
            {
                item.fillItemGroup(CreativeTabs.SEARCH, guicontainercreative$containercreative.itemList);
            }
        }
        else
        {
            guicontainercreative$containercreative.itemList.addAll(this.field_146297_k.func_193987_a(SearchTreeManager.field_194011_a).search(this.searchField.getText().toLowerCase(Locale.ROOT)));
        }

        this.currentScroll = 0.0F;
        guicontainercreative$containercreative.scrollTo(0.0F);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        CreativeTabs creativetabs = CreativeTabs.GROUPS[selectedTabIndex];

        if (creativetabs.drawInForegroundOfTab())
        {
            GlStateManager.func_179084_k();
            this.field_146289_q.func_78276_b(I18n.format(creativetabs.getTranslationKey()), 8, 6, 4210752);
        }
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        if (p_73864_3_ == 0)
        {
            int i = p_73864_1_ - this.guiLeft;
            int j = p_73864_2_ - this.guiTop;

            for (CreativeTabs creativetabs : CreativeTabs.GROUPS)
            {
                if (this.func_147049_a(creativetabs, i, j))
                {
                    return;
                }
            }
        }

        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    protected void func_146286_b(int p_146286_1_, int p_146286_2_, int p_146286_3_)
    {
        if (p_146286_3_ == 0)
        {
            int i = p_146286_1_ - this.guiLeft;
            int j = p_146286_2_ - this.guiTop;

            for (CreativeTabs creativetabs : CreativeTabs.GROUPS)
            {
                if (this.func_147049_a(creativetabs, i, j))
                {
                    this.setCurrentCreativeTab(creativetabs);
                    return;
                }
            }
        }

        super.func_146286_b(p_146286_1_, p_146286_2_, p_146286_3_);
    }

    /**
     * returns (if you are not on the inventoryTab) and (the flag isn't set) and (you have more than 1 page of items)
     */
    private boolean needsScrollBars()
    {
        return selectedTabIndex != CreativeTabs.INVENTORY.getIndex() && CreativeTabs.GROUPS[selectedTabIndex].hasScrollbar() && ((GuiContainerCreative.ContainerCreative)this.container).canScroll();
    }

    /**
     * Sets the current creative tab, restructuring the GUI as needed.
     */
    private void setCurrentCreativeTab(CreativeTabs tab)
    {
        int i = selectedTabIndex;
        selectedTabIndex = tab.getIndex();
        GuiContainerCreative.ContainerCreative guicontainercreative$containercreative = (GuiContainerCreative.ContainerCreative)this.container;
        this.dragSplittingSlots.clear();
        guicontainercreative$containercreative.itemList.clear();

        if (tab == CreativeTabs.HOTBAR)
        {
            for (int j = 0; j < 9; ++j)
            {
                HotbarSnapshot hotbarsnapshot = this.field_146297_k.creativeSettings.getHotbarSnapshot(j);

                if (hotbarsnapshot.isEmpty())
                {
                    for (int k = 0; k < 9; ++k)
                    {
                        if (k == j)
                        {
                            ItemStack itemstack = new ItemStack(Items.PAPER);
                            itemstack.getOrCreateChildTag("CustomCreativeLock");
                            String s = GameSettings.func_74298_c(this.field_146297_k.gameSettings.keyBindsHotbar[j].func_151463_i());
                            String s1 = GameSettings.func_74298_c(this.field_146297_k.gameSettings.keyBindSaveToolbar.func_151463_i());
                            itemstack.func_151001_c((new TextComponentTranslation("inventory.hotbarInfo", new Object[] {s1, s})).func_150260_c());
                            guicontainercreative$containercreative.itemList.add(itemstack);
                        }
                        else
                        {
                            guicontainercreative$containercreative.itemList.add(ItemStack.EMPTY);
                        }
                    }
                }
                else
                {
                    guicontainercreative$containercreative.itemList.addAll(hotbarsnapshot);
                }
            }
        }
        else if (tab != CreativeTabs.SEARCH)
        {
            tab.fill(guicontainercreative$containercreative.itemList);
        }

        if (tab == CreativeTabs.INVENTORY)
        {
            Container container = this.field_146297_k.player.container;

            if (this.originalSlots == null)
            {
                this.originalSlots = guicontainercreative$containercreative.inventorySlots;
            }

            guicontainercreative$containercreative.inventorySlots = Lists.<Slot>newArrayList();

            for (int l = 0; l < container.inventorySlots.size(); ++l)
            {
                Slot slot = new GuiContainerCreative.CreativeSlot(container.inventorySlots.get(l), l);
                guicontainercreative$containercreative.inventorySlots.add(slot);

                if (l >= 5 && l < 9)
                {
                    int j1 = l - 5;
                    int l1 = j1 / 2;
                    int j2 = j1 % 2;
                    slot.xPos = 54 + l1 * 54;
                    slot.yPos = 6 + j2 * 27;
                }
                else if (l >= 0 && l < 5)
                {
                    slot.xPos = -2000;
                    slot.yPos = -2000;
                }
                else if (l == 45)
                {
                    slot.xPos = 35;
                    slot.yPos = 20;
                }
                else if (l < container.inventorySlots.size())
                {
                    int i1 = l - 9;
                    int k1 = i1 % 9;
                    int i2 = i1 / 9;
                    slot.xPos = 9 + k1 * 18;

                    if (l >= 36)
                    {
                        slot.yPos = 112;
                    }
                    else
                    {
                        slot.yPos = 54 + i2 * 18;
                    }
                }
            }

            this.destroyItemSlot = new Slot(field_147060_v, 0, 173, 112);
            guicontainercreative$containercreative.inventorySlots.add(this.destroyItemSlot);
        }
        else if (i == CreativeTabs.INVENTORY.getIndex())
        {
            guicontainercreative$containercreative.inventorySlots = this.originalSlots;
            this.originalSlots = null;
        }

        if (this.searchField != null)
        {
            if (tab == CreativeTabs.SEARCH)
            {
                this.searchField.setVisible(true);
                this.searchField.setCanLoseFocus(false);
                this.searchField.setFocused2(true);
                this.searchField.setText("");
                this.updateCreativeSearch();
            }
            else
            {
                this.searchField.setVisible(false);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocused2(false);
            }
        }

        this.currentScroll = 0.0F;
        guicontainercreative$containercreative.scrollTo(0.0F);
    }

    public void func_146274_d() throws IOException
    {
        super.func_146274_d();
        int i = Mouse.getEventDWheel();

        if (i != 0 && this.needsScrollBars())
        {
            int j = (((GuiContainerCreative.ContainerCreative)this.container).itemList.size() + 9 - 1) / 9 - 5;

            if (i > 0)
            {
                i = 1;
            }

            if (i < 0)
            {
                i = -1;
            }

            this.currentScroll = (float)((double)this.currentScroll - (double)i / (double)j);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
            ((GuiContainerCreative.ContainerCreative)this.container).scrollTo(this.currentScroll);
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        boolean flag = Mouse.isButtonDown(0);
        int i = this.guiLeft;
        int j = this.guiTop;
        int k = i + 175;
        int l = j + 18;
        int i1 = k + 14;
        int j1 = l + 112;

        if (!this.field_147065_z && flag && p_73863_1_ >= k && p_73863_2_ >= l && p_73863_1_ < i1 && p_73863_2_ < j1)
        {
            this.isScrolling = this.needsScrollBars();
        }

        if (!flag)
        {
            this.isScrolling = false;
        }

        this.field_147065_z = flag;

        if (this.isScrolling)
        {
            this.currentScroll = ((float)(p_73863_2_ - l) - 7.5F) / ((float)(j1 - l) - 15.0F);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
            ((GuiContainerCreative.ContainerCreative)this.container).scrollTo(this.currentScroll);
        }

        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);

        for (CreativeTabs creativetabs : CreativeTabs.GROUPS)
        {
            if (this.renderCreativeInventoryHoveringText(creativetabs, p_73863_1_, p_73863_2_))
            {
                break;
            }
        }

        if (this.destroyItemSlot != null && selectedTabIndex == CreativeTabs.INVENTORY.getIndex() && this.func_146978_c(this.destroyItemSlot.xPos, this.destroyItemSlot.yPos, 16, 16, p_73863_1_, p_73863_2_))
        {
            this.func_146279_a(I18n.format("inventory.binSlot"), p_73863_1_, p_73863_2_);
        }

        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179140_f();
        this.renderHoveredToolTip(p_73863_1_, p_73863_2_);
    }

    protected void func_146285_a(ItemStack p_146285_1_, int p_146285_2_, int p_146285_3_)
    {
        if (selectedTabIndex == CreativeTabs.SEARCH.getIndex())
        {
            List<String> list = p_146285_1_.getTooltip(this.field_146297_k.player, this.field_146297_k.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
            CreativeTabs creativetabs = p_146285_1_.getItem().getGroup();

            if (creativetabs == null && p_146285_1_.getItem() == Items.ENCHANTED_BOOK)
            {
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(p_146285_1_);

                if (map.size() == 1)
                {
                    Enchantment enchantment = map.keySet().iterator().next();

                    for (CreativeTabs creativetabs1 : CreativeTabs.GROUPS)
                    {
                        if (creativetabs1.hasRelevantEnchantmentType(enchantment.type))
                        {
                            creativetabs = creativetabs1;
                            break;
                        }
                    }
                }
            }

            if (creativetabs != null)
            {
                list.add(1, "" + TextFormatting.BOLD + TextFormatting.BLUE + I18n.format(creativetabs.getTranslationKey()));
            }

            for (int i = 0; i < list.size(); ++i)
            {
                if (i == 0)
                {
                    list.set(i, p_146285_1_.getRarity().color + (String)list.get(i));
                }
                else
                {
                    list.set(i, TextFormatting.GRAY + (String)list.get(i));
                }
            }

            this.func_146283_a(list, p_146285_2_, p_146285_3_);
        }
        else
        {
            super.func_146285_a(p_146285_1_, p_146285_2_, p_146285_3_);
        }
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.func_74520_c();
        CreativeTabs creativetabs = CreativeTabs.GROUPS[selectedTabIndex];

        for (CreativeTabs creativetabs1 : CreativeTabs.GROUPS)
        {
            this.field_146297_k.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);

            if (creativetabs1.getIndex() != selectedTabIndex)
            {
                this.drawTab(creativetabs1);
            }
        }

        this.field_146297_k.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName()));
        this.func_73729_b(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.func_146194_f();
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.guiLeft + 175;
        int j = this.guiTop + 18;
        int k = j + 112;
        this.field_146297_k.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);

        if (creativetabs.hasScrollbar())
        {
            this.func_73729_b(i, j + (int)((float)(k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }

        this.drawTab(creativetabs);

        if (creativetabs == CreativeTabs.INVENTORY)
        {
            GuiInventory.func_147046_a(this.guiLeft + 88, this.guiTop + 45, 20, (float)(this.guiLeft + 88 - mouseX), (float)(this.guiTop + 45 - 30 - mouseY), this.field_146297_k.player);
        }
    }

    protected boolean func_147049_a(CreativeTabs p_147049_1_, int p_147049_2_, int p_147049_3_)
    {
        int i = p_147049_1_.getColumn();
        int j = 28 * i;
        int k = 0;

        if (p_147049_1_.isAlignedRight())
        {
            j = this.xSize - 28 * (6 - i) + 2;
        }
        else if (i > 0)
        {
            j += i;
        }

        if (p_147049_1_.isOnTopRow())
        {
            k = k - 32;
        }
        else
        {
            k = k + this.ySize;
        }

        return p_147049_2_ >= j && p_147049_2_ <= j + 28 && p_147049_3_ >= k && p_147049_3_ <= k + 32;
    }

    /**
     * Renders the creative inventory hovering text if mouse is over it. Returns true if did render or false otherwise.
     * Params: current creative tab to be checked, current mouse x position, current mouse y position.
     */
    protected boolean renderCreativeInventoryHoveringText(CreativeTabs tab, int mouseX, int mouseY)
    {
        int i = tab.getColumn();
        int j = 28 * i;
        int k = 0;

        if (tab.isAlignedRight())
        {
            j = this.xSize - 28 * (6 - i) + 2;
        }
        else if (i > 0)
        {
            j += i;
        }

        if (tab.isOnTopRow())
        {
            k = k - 32;
        }
        else
        {
            k = k + this.ySize;
        }

        if (this.func_146978_c(j + 3, k + 3, 23, 27, mouseX, mouseY))
        {
            this.func_146279_a(I18n.format(tab.getTranslationKey()), mouseX, mouseY);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Draws the given tab and its background, deciding whether to highlight the tab or not based off of the selected
     * index.
     */
    protected void drawTab(CreativeTabs tab)
    {
        boolean flag = tab.getIndex() == selectedTabIndex;
        boolean flag1 = tab.isOnTopRow();
        int i = tab.getColumn();
        int j = i * 28;
        int k = 0;
        int l = this.guiLeft + 28 * i;
        int i1 = this.guiTop;
        int j1 = 32;

        if (flag)
        {
            k += 32;
        }

        if (tab.isAlignedRight())
        {
            l = this.guiLeft + this.xSize - 28 * (6 - i);
        }
        else if (i > 0)
        {
            l += i;
        }

        if (flag1)
        {
            i1 = i1 - 28;
        }
        else
        {
            k += 64;
            i1 = i1 + (this.ySize - 4);
        }

        GlStateManager.func_179140_f();
        this.func_73729_b(l, i1, j, k, 28, 32);
        this.field_73735_i = 100.0F;
        this.field_146296_j.zLevel = 100.0F;
        l = l + 6;
        i1 = i1 + 8 + (flag1 ? 1 : -1);
        GlStateManager.func_179145_e();
        GlStateManager.func_179091_B();
        ItemStack itemstack = tab.getIcon();
        this.field_146296_j.renderItemAndEffectIntoGUI(itemstack, l, i1);
        this.field_146296_j.renderItemOverlays(this.field_146289_q, itemstack, l, i1);
        GlStateManager.func_179140_f();
        this.field_146296_j.zLevel = 0.0F;
        this.field_73735_i = 0.0F;
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146127_k == 1)
        {
            this.field_146297_k.displayGuiScreen(new GuiStats(this, this.field_146297_k.player.getStats()));
        }
    }

    /**
     * Returns the index of the currently selected tab.
     */
    public int getSelectedTabIndex()
    {
        return selectedTabIndex;
    }

    public static void handleHotbarSnapshots(Minecraft client, int index, boolean load, boolean save)
    {
        EntityPlayerSP entityplayersp = client.player;
        CreativeSettings creativesettings = client.creativeSettings;
        HotbarSnapshot hotbarsnapshot = creativesettings.getHotbarSnapshot(index);

        if (load)
        {
            for (int i = 0; i < InventoryPlayer.getHotbarSize(); ++i)
            {
                ItemStack itemstack = ((ItemStack)hotbarsnapshot.get(i)).copy();
                entityplayersp.inventory.setInventorySlotContents(i, itemstack);
                client.playerController.sendSlotPacket(itemstack, 36 + i);
            }

            entityplayersp.container.detectAndSendChanges();
        }
        else if (save)
        {
            for (int j = 0; j < InventoryPlayer.getHotbarSize(); ++j)
            {
                hotbarsnapshot.set(j, entityplayersp.inventory.getStackInSlot(j).copy());
            }

            String s = GameSettings.func_74298_c(client.gameSettings.keyBindsHotbar[index].func_151463_i());
            String s1 = GameSettings.func_74298_c(client.gameSettings.keyBindLoadToolbar.func_151463_i());
            client.ingameGUI.setOverlayMessage(new TextComponentTranslation("inventory.hotbarSaved", new Object[] {s1, s}), false);
            creativesettings.save();
        }
    }

    public static class ContainerCreative extends Container
    {
        public NonNullList<ItemStack> itemList = NonNullList.<ItemStack>create();

        public ContainerCreative(EntityPlayer player)
        {
            InventoryPlayer inventoryplayer = player.inventory;

            for (int i = 0; i < 5; ++i)
            {
                for (int j = 0; j < 9; ++j)
                {
                    this.addSlot(new GuiContainerCreative.LockedSlot(GuiContainerCreative.field_147060_v, i * 9 + j, 9 + j * 18, 18 + i * 18));
                }
            }

            for (int k = 0; k < 9; ++k)
            {
                this.addSlot(new Slot(inventoryplayer, k, 9 + k * 18, 112));
            }

            this.scrollTo(0.0F);
        }

        public boolean canInteractWith(EntityPlayer playerIn)
        {
            return true;
        }

        public void scrollTo(float pos)
        {
            int i = (this.itemList.size() + 9 - 1) / 9 - 5;
            int j = (int)((double)(pos * (float)i) + 0.5D);

            if (j < 0)
            {
                j = 0;
            }

            for (int k = 0; k < 5; ++k)
            {
                for (int l = 0; l < 9; ++l)
                {
                    int i1 = l + (k + j) * 9;

                    if (i1 >= 0 && i1 < this.itemList.size())
                    {
                        GuiContainerCreative.field_147060_v.setInventorySlotContents(l + k * 9, this.itemList.get(i1));
                    }
                    else
                    {
                        GuiContainerCreative.field_147060_v.setInventorySlotContents(l + k * 9, ItemStack.EMPTY);
                    }
                }
            }
        }

        public boolean canScroll()
        {
            return this.itemList.size() > 45;
        }

        public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
        {
            if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size())
            {
                Slot slot = this.inventorySlots.get(index);

                if (slot != null && slot.getHasStack())
                {
                    slot.putStack(ItemStack.EMPTY);
                }
            }

            return ItemStack.EMPTY;
        }

        public boolean canMergeSlot(ItemStack stack, Slot slotIn)
        {
            return slotIn.yPos > 90;
        }

        public boolean canDragIntoSlot(Slot slotIn)
        {
            return slotIn.inventory instanceof InventoryPlayer || slotIn.yPos > 90 && slotIn.xPos <= 162;
        }
    }

    class CreativeSlot extends Slot
    {
        private final Slot slot;

        public CreativeSlot(Slot p_i46313_2_, int p_i46313_3_)
        {
            super(p_i46313_2_.inventory, p_i46313_3_, 0, 0);
            this.slot = p_i46313_2_;
        }

        public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
        {
            this.slot.onTake(thePlayer, stack);
            return stack;
        }

        public boolean isItemValid(ItemStack stack)
        {
            return this.slot.isItemValid(stack);
        }

        public ItemStack getStack()
        {
            return this.slot.getStack();
        }

        public boolean getHasStack()
        {
            return this.slot.getHasStack();
        }

        public void putStack(ItemStack stack)
        {
            this.slot.putStack(stack);
        }

        public void onSlotChanged()
        {
            this.slot.onSlotChanged();
        }

        public int getSlotStackLimit()
        {
            return this.slot.getSlotStackLimit();
        }

        public int getItemStackLimit(ItemStack stack)
        {
            return this.slot.getItemStackLimit(stack);
        }

        @Nullable
        public String func_178171_c()
        {
            return this.slot.func_178171_c();
        }

        public ItemStack decrStackSize(int amount)
        {
            return this.slot.decrStackSize(amount);
        }

        public boolean func_75217_a(IInventory p_75217_1_, int p_75217_2_)
        {
            return this.slot.func_75217_a(p_75217_1_, p_75217_2_);
        }

        public boolean isEnabled()
        {
            return this.slot.isEnabled();
        }

        public boolean canTakeStack(EntityPlayer playerIn)
        {
            return this.slot.canTakeStack(playerIn);
        }
    }

    static class LockedSlot extends Slot
    {
        public LockedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
        {
            super(inventoryIn, index, xPosition, yPosition);
        }

        public boolean canTakeStack(EntityPlayer playerIn)
        {
            if (super.canTakeStack(playerIn) && this.getHasStack())
            {
                return this.getStack().getChildTag("CustomCreativeLock") == null;
            }
            else
            {
                return !this.getHasStack();
            }
        }
    }
}

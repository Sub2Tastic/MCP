package net.minecraft.client.gui.inventory;

import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public abstract class GuiContainer extends GuiScreen
{
    /** The location of the inventory background texture */
    public static final ResourceLocation INVENTORY_BACKGROUND = new ResourceLocation("textures/gui/container/inventory.png");

    /** The X size of the inventory window in pixels. */
    protected int xSize = 176;

    /** The Y size of the inventory window in pixels. */
    protected int ySize = 166;

    /** A list of the players inventory slots */
    public Container container;

    /**
     * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiLeft;

    /**
     * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiTop;

    /** Holds the slot currently hovered */
    private Slot hoveredSlot;

    /** Used when touchscreen is enabled */
    private Slot clickedSlot;

    /** Used when touchscreen is enabled. */
    private boolean isRightMouseClick;

    /** Used when touchscreen is enabled */
    private ItemStack draggedStack = ItemStack.EMPTY;
    private int touchUpX;
    private int touchUpY;
    private Slot returningStackDestSlot;
    private long returningStackTime;

    /** Used when touchscreen is enabled */
    private ItemStack returningStack = ItemStack.EMPTY;
    private Slot currentDragTargetSlot;
    private long dragItemDropDelay;
    protected final Set<Slot> dragSplittingSlots = Sets.<Slot>newHashSet();
    protected boolean dragSplitting;
    private int dragSplittingLimit;
    private int dragSplittingButton;
    private boolean ignoreMouseUp;
    private int dragSplittingRemnant;
    private long lastClickTime;
    private Slot lastClickSlot;
    private int lastClickButton;
    private boolean doubleClick;
    private ItemStack shiftClickedSlot = ItemStack.EMPTY;

    public GuiContainer(Container p_i1072_1_)
    {
        this.container = p_i1072_1_;
        this.ignoreMouseUp = true;
    }

    public void func_73866_w_()
    {
        super.func_73866_w_();
        this.field_146297_k.player.openContainer = this.container;
        this.guiLeft = (this.field_146294_l - this.xSize) / 2;
        this.guiTop = (this.field_146295_m - this.ySize) / 2;
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        int i = this.guiLeft;
        int j = this.guiTop;
        this.drawGuiContainerBackgroundLayer(p_73863_3_, p_73863_1_, p_73863_2_);
        GlStateManager.func_179101_C();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.func_179140_f();
        GlStateManager.func_179097_i();
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
        RenderHelper.func_74520_c();
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)i, (float)j, 0.0F);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179091_B();
        this.hoveredSlot = null;
        int k = 240;
        int l = 240;
        OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0F, 240.0F);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i1 = 0; i1 < this.container.inventorySlots.size(); ++i1)
        {
            Slot slot = this.container.inventorySlots.get(i1);

            if (slot.isEnabled())
            {
                this.drawSlot(slot);
            }

            if (this.func_146981_a(slot, p_73863_1_, p_73863_2_) && slot.isEnabled())
            {
                this.hoveredSlot = slot;
                GlStateManager.func_179140_f();
                GlStateManager.func_179097_i();
                int j1 = slot.xPos;
                int k1 = slot.yPos;
                GlStateManager.func_179135_a(true, true, true, false);
                this.func_73733_a(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
                GlStateManager.func_179135_a(true, true, true, true);
                GlStateManager.func_179145_e();
                GlStateManager.func_179126_j();
            }
        }

        RenderHelper.disableStandardItemLighting();
        this.drawGuiContainerForegroundLayer(p_73863_1_, p_73863_2_);
        RenderHelper.func_74520_c();
        InventoryPlayer inventoryplayer = this.field_146297_k.player.inventory;
        ItemStack itemstack = this.draggedStack.isEmpty() ? inventoryplayer.getItemStack() : this.draggedStack;

        if (!itemstack.isEmpty())
        {
            int j2 = 8;
            int k2 = this.draggedStack.isEmpty() ? 8 : 16;
            String s = null;

            if (!this.draggedStack.isEmpty() && this.isRightMouseClick)
            {
                itemstack = itemstack.copy();
                itemstack.setCount(MathHelper.ceil((float)itemstack.getCount() / 2.0F));
            }
            else if (this.dragSplitting && this.dragSplittingSlots.size() > 1)
            {
                itemstack = itemstack.copy();
                itemstack.setCount(this.dragSplittingRemnant);

                if (itemstack.isEmpty())
                {
                    s = "" + TextFormatting.YELLOW + "0";
                }
            }

            this.drawItemStack(itemstack, p_73863_1_ - i - 8, p_73863_2_ - j - k2, s);
        }

        if (!this.returningStack.isEmpty())
        {
            float f = (float)(Minecraft.func_71386_F() - this.returningStackTime) / 100.0F;

            if (f >= 1.0F)
            {
                f = 1.0F;
                this.returningStack = ItemStack.EMPTY;
            }

            int l2 = this.returningStackDestSlot.xPos - this.touchUpX;
            int i3 = this.returningStackDestSlot.yPos - this.touchUpY;
            int l1 = this.touchUpX + (int)((float)l2 * f);
            int i2 = this.touchUpY + (int)((float)i3 * f);
            this.drawItemStack(this.returningStack, l1, i2, (String)null);
        }

        GlStateManager.func_179121_F();
        GlStateManager.func_179145_e();
        GlStateManager.func_179126_j();
        RenderHelper.func_74519_b();
    }

    protected void renderHoveredToolTip(int mouseX, int mouseY)
    {
        if (this.field_146297_k.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack())
        {
            this.func_146285_a(this.hoveredSlot.getStack(), mouseX, mouseY);
        }
    }

    /**
     * Draws an ItemStack.
     *  
     * The z index is increased by 32 (and not decreased afterwards), and the item is then rendered at z=200.
     */
    private void drawItemStack(ItemStack stack, int x, int y, String altText)
    {
        GlStateManager.func_179109_b(0.0F, 0.0F, 32.0F);
        this.field_73735_i = 200.0F;
        this.field_146296_j.zLevel = 200.0F;
        this.field_146296_j.renderItemAndEffectIntoGUI(stack, x, y);
        this.field_146296_j.renderItemOverlayIntoGUI(this.field_146289_q, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
        this.field_73735_i = 0.0F;
        this.field_146296_j.zLevel = 0.0F;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected abstract void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY);

    /**
     * Draws the given slot: any item in it, the slot's background, the hovered highlight, etc.
     */
    private void drawSlot(Slot slotIn)
    {
        int i = slotIn.xPos;
        int j = slotIn.yPos;
        ItemStack itemstack = slotIn.getStack();
        boolean flag = false;
        boolean flag1 = slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
        ItemStack itemstack1 = this.field_146297_k.player.inventory.getItemStack();
        String s = null;

        if (slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick && !itemstack.isEmpty())
        {
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
        }
        else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty())
        {
            if (this.dragSplittingSlots.size() == 1)
            {
                return;
            }

            if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.container.canDragIntoSlot(slotIn))
            {
                itemstack = itemstack1.copy();
                flag = true;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
                int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));

                if (itemstack.getCount() > k)
                {
                    s = TextFormatting.YELLOW.toString() + k;
                    itemstack.setCount(k);
                }
            }
            else
            {
                this.dragSplittingSlots.remove(slotIn);
                this.updateDragSplitting();
            }
        }

        this.field_73735_i = 100.0F;
        this.field_146296_j.zLevel = 100.0F;

        if (itemstack.isEmpty() && slotIn.isEnabled())
        {
            String s1 = slotIn.func_178171_c();

            if (s1 != null)
            {
                TextureAtlasSprite textureatlassprite = this.field_146297_k.func_147117_R().func_110572_b(s1);
                GlStateManager.func_179140_f();
                this.field_146297_k.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                this.func_175175_a(i, j, textureatlassprite, 16, 16);
                GlStateManager.func_179145_e();
                flag1 = true;
            }
        }

        if (!flag1)
        {
            if (flag)
            {
                func_73734_a(i, j, i + 16, j + 16, -2130706433);
            }

            GlStateManager.func_179126_j();
            this.field_146296_j.renderItemAndEffectIntoGUI(this.field_146297_k.player, itemstack, i, j);
            this.field_146296_j.renderItemOverlayIntoGUI(this.field_146289_q, itemstack, i, j, s);
        }

        this.field_146296_j.zLevel = 0.0F;
        this.field_73735_i = 0.0F;
    }

    private void updateDragSplitting()
    {
        ItemStack itemstack = this.field_146297_k.player.inventory.getItemStack();

        if (!itemstack.isEmpty() && this.dragSplitting)
        {
            if (this.dragSplittingLimit == 2)
            {
                this.dragSplittingRemnant = itemstack.getMaxStackSize();
            }
            else
            {
                this.dragSplittingRemnant = itemstack.getCount();

                for (Slot slot : this.dragSplittingSlots)
                {
                    ItemStack itemstack1 = itemstack.copy();
                    ItemStack itemstack2 = slot.getStack();
                    int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
                    Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
                    int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));

                    if (itemstack1.getCount() > j)
                    {
                        itemstack1.setCount(j);
                    }

                    this.dragSplittingRemnant -= itemstack1.getCount() - i;
                }
            }
        }
    }

    private Slot func_146975_c(int p_146975_1_, int p_146975_2_)
    {
        for (int i = 0; i < this.container.inventorySlots.size(); ++i)
        {
            Slot slot = this.container.inventorySlots.get(i);

            if (this.func_146981_a(slot, p_146975_1_, p_146975_2_) && slot.isEnabled())
            {
                return slot;
            }
        }

        return null;
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
        boolean flag = p_73864_3_ == this.field_146297_k.gameSettings.keyBindPickBlock.func_151463_i() + 100;
        Slot slot = this.func_146975_c(p_73864_1_, p_73864_2_);
        long i = Minecraft.func_71386_F();
        this.doubleClick = this.lastClickSlot == slot && i - this.lastClickTime < 250L && this.lastClickButton == p_73864_3_;
        this.ignoreMouseUp = false;

        if (p_73864_3_ == 0 || p_73864_3_ == 1 || flag)
        {
            int j = this.guiLeft;
            int k = this.guiTop;
            boolean flag1 = this.func_193983_c(p_73864_1_, p_73864_2_, j, k);
            int l = -1;

            if (slot != null)
            {
                l = slot.slotNumber;
            }

            if (flag1)
            {
                l = -999;
            }

            if (this.field_146297_k.gameSettings.touchscreen && flag1 && this.field_146297_k.player.inventory.getItemStack().isEmpty())
            {
                this.field_146297_k.displayGuiScreen((GuiScreen)null);
                return;
            }

            if (l != -1)
            {
                if (this.field_146297_k.gameSettings.touchscreen)
                {
                    if (slot != null && slot.getHasStack())
                    {
                        this.clickedSlot = slot;
                        this.draggedStack = ItemStack.EMPTY;
                        this.isRightMouseClick = p_73864_3_ == 1;
                    }
                    else
                    {
                        this.clickedSlot = null;
                    }
                }
                else if (!this.dragSplitting)
                {
                    if (this.field_146297_k.player.inventory.getItemStack().isEmpty())
                    {
                        if (p_73864_3_ == this.field_146297_k.gameSettings.keyBindPickBlock.func_151463_i() + 100)
                        {
                            this.handleMouseClick(slot, l, p_73864_3_, ClickType.CLONE);
                        }
                        else
                        {
                            boolean flag2 = l != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                            ClickType clicktype = ClickType.PICKUP;

                            if (flag2)
                            {
                                this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY;
                                clicktype = ClickType.QUICK_MOVE;
                            }
                            else if (l == -999)
                            {
                                clicktype = ClickType.THROW;
                            }

                            this.handleMouseClick(slot, l, p_73864_3_, clicktype);
                        }

                        this.ignoreMouseUp = true;
                    }
                    else
                    {
                        this.dragSplitting = true;
                        this.dragSplittingButton = p_73864_3_;
                        this.dragSplittingSlots.clear();

                        if (p_73864_3_ == 0)
                        {
                            this.dragSplittingLimit = 0;
                        }
                        else if (p_73864_3_ == 1)
                        {
                            this.dragSplittingLimit = 1;
                        }
                        else if (p_73864_3_ == this.field_146297_k.gameSettings.keyBindPickBlock.func_151463_i() + 100)
                        {
                            this.dragSplittingLimit = 2;
                        }
                    }
                }
            }
        }

        this.lastClickSlot = slot;
        this.lastClickTime = i;
        this.lastClickButton = p_73864_3_;
    }

    protected boolean func_193983_c(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_)
    {
        return p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
    }

    protected void func_146273_a(int p_146273_1_, int p_146273_2_, int p_146273_3_, long p_146273_4_)
    {
        Slot slot = this.func_146975_c(p_146273_1_, p_146273_2_);
        ItemStack itemstack = this.field_146297_k.player.inventory.getItemStack();

        if (this.clickedSlot != null && this.field_146297_k.gameSettings.touchscreen)
        {
            if (p_146273_3_ == 0 || p_146273_3_ == 1)
            {
                if (this.draggedStack.isEmpty())
                {
                    if (slot != this.clickedSlot && !this.clickedSlot.getStack().isEmpty())
                    {
                        this.draggedStack = this.clickedSlot.getStack().copy();
                    }
                }
                else if (this.draggedStack.getCount() > 1 && slot != null && Container.canAddItemToSlot(slot, this.draggedStack, false))
                {
                    long i = Minecraft.func_71386_F();

                    if (this.currentDragTargetSlot == slot)
                    {
                        if (i - this.dragItemDropDelay > 500L)
                        {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                            this.handleMouseClick(slot, slot.slotNumber, 1, ClickType.PICKUP);
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                            this.dragItemDropDelay = i + 750L;
                            this.draggedStack.shrink(1);
                        }
                    }
                    else
                    {
                        this.currentDragTargetSlot = slot;
                        this.dragItemDropDelay = i;
                    }
                }
            }
        }
        else if (this.dragSplitting && slot != null && !itemstack.isEmpty() && (itemstack.getCount() > this.dragSplittingSlots.size() || this.dragSplittingLimit == 2) && Container.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack) && this.container.canDragIntoSlot(slot))
        {
            this.dragSplittingSlots.add(slot);
            this.updateDragSplitting();
        }
    }

    protected void func_146286_b(int p_146286_1_, int p_146286_2_, int p_146286_3_)
    {
        Slot slot = this.func_146975_c(p_146286_1_, p_146286_2_);
        int i = this.guiLeft;
        int j = this.guiTop;
        boolean flag = this.func_193983_c(p_146286_1_, p_146286_2_, i, j);
        int k = -1;

        if (slot != null)
        {
            k = slot.slotNumber;
        }

        if (flag)
        {
            k = -999;
        }

        if (this.doubleClick && slot != null && p_146286_3_ == 0 && this.container.canMergeSlot(ItemStack.EMPTY, slot))
        {
            if (func_146272_n())
            {
                if (!this.shiftClickedSlot.isEmpty())
                {
                    for (Slot slot2 : this.container.inventorySlots)
                    {
                        if (slot2 != null && slot2.canTakeStack(this.field_146297_k.player) && slot2.getHasStack() && slot2.inventory == slot.inventory && Container.canAddItemToSlot(slot2, this.shiftClickedSlot, true))
                        {
                            this.handleMouseClick(slot2, slot2.slotNumber, p_146286_3_, ClickType.QUICK_MOVE);
                        }
                    }
                }
            }
            else
            {
                this.handleMouseClick(slot, k, p_146286_3_, ClickType.PICKUP_ALL);
            }

            this.doubleClick = false;
            this.lastClickTime = 0L;
        }
        else
        {
            if (this.dragSplitting && this.dragSplittingButton != p_146286_3_)
            {
                this.dragSplitting = false;
                this.dragSplittingSlots.clear();
                this.ignoreMouseUp = true;
                return;
            }

            if (this.ignoreMouseUp)
            {
                this.ignoreMouseUp = false;
                return;
            }

            if (this.clickedSlot != null && this.field_146297_k.gameSettings.touchscreen)
            {
                if (p_146286_3_ == 0 || p_146286_3_ == 1)
                {
                    if (this.draggedStack.isEmpty() && slot != this.clickedSlot)
                    {
                        this.draggedStack = this.clickedSlot.getStack();
                    }

                    boolean flag2 = Container.canAddItemToSlot(slot, this.draggedStack, false);

                    if (k != -1 && !this.draggedStack.isEmpty() && flag2)
                    {
                        this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, p_146286_3_, ClickType.PICKUP);
                        this.handleMouseClick(slot, k, 0, ClickType.PICKUP);

                        if (this.field_146297_k.player.inventory.getItemStack().isEmpty())
                        {
                            this.returningStack = ItemStack.EMPTY;
                        }
                        else
                        {
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, p_146286_3_, ClickType.PICKUP);
                            this.touchUpX = p_146286_1_ - i;
                            this.touchUpY = p_146286_2_ - j;
                            this.returningStackDestSlot = this.clickedSlot;
                            this.returningStack = this.draggedStack;
                            this.returningStackTime = Minecraft.func_71386_F();
                        }
                    }
                    else if (!this.draggedStack.isEmpty())
                    {
                        this.touchUpX = p_146286_1_ - i;
                        this.touchUpY = p_146286_2_ - j;
                        this.returningStackDestSlot = this.clickedSlot;
                        this.returningStack = this.draggedStack;
                        this.returningStackTime = Minecraft.func_71386_F();
                    }

                    this.draggedStack = ItemStack.EMPTY;
                    this.clickedSlot = null;
                }
            }
            else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty())
            {
                this.handleMouseClick((Slot)null, -999, Container.getQuickcraftMask(0, this.dragSplittingLimit), ClickType.QUICK_CRAFT);

                for (Slot slot1 : this.dragSplittingSlots)
                {
                    this.handleMouseClick(slot1, slot1.slotNumber, Container.getQuickcraftMask(1, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
                }

                this.handleMouseClick((Slot)null, -999, Container.getQuickcraftMask(2, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
            }
            else if (!this.field_146297_k.player.inventory.getItemStack().isEmpty())
            {
                if (p_146286_3_ == this.field_146297_k.gameSettings.keyBindPickBlock.func_151463_i() + 100)
                {
                    this.handleMouseClick(slot, k, p_146286_3_, ClickType.CLONE);
                }
                else
                {
                    boolean flag1 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));

                    if (flag1)
                    {
                        this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY;
                    }

                    this.handleMouseClick(slot, k, p_146286_3_, flag1 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
                }
            }
        }

        if (this.field_146297_k.player.inventory.getItemStack().isEmpty())
        {
            this.lastClickTime = 0L;
        }

        this.dragSplitting = false;
    }

    private boolean func_146981_a(Slot p_146981_1_, int p_146981_2_, int p_146981_3_)
    {
        return this.func_146978_c(p_146981_1_.xPos, p_146981_1_.yPos, 16, 16, p_146981_2_, p_146981_3_);
    }

    protected boolean func_146978_c(int p_146978_1_, int p_146978_2_, int p_146978_3_, int p_146978_4_, int p_146978_5_, int p_146978_6_)
    {
        int i = this.guiLeft;
        int j = this.guiTop;
        p_146978_5_ = p_146978_5_ - i;
        p_146978_6_ = p_146978_6_ - j;
        return p_146978_5_ >= p_146978_1_ - 1 && p_146978_5_ < p_146978_1_ + p_146978_3_ + 1 && p_146978_6_ >= p_146978_2_ - 1 && p_146978_6_ < p_146978_2_ + p_146978_4_ + 1;
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type)
    {
        if (slotIn != null)
        {
            slotId = slotIn.slotNumber;
        }

        this.field_146297_k.playerController.windowClick(this.container.windowId, slotId, mouseButton, type, this.field_146297_k.player);
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (p_73869_2_ == 1 || p_73869_2_ == this.field_146297_k.gameSettings.keyBindInventory.func_151463_i())
        {
            this.field_146297_k.player.closeScreen();
        }

        this.func_146983_a(p_73869_2_);

        if (this.hoveredSlot != null && this.hoveredSlot.getHasStack())
        {
            if (p_73869_2_ == this.field_146297_k.gameSettings.keyBindPickBlock.func_151463_i())
            {
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, 0, ClickType.CLONE);
            }
            else if (p_73869_2_ == this.field_146297_k.gameSettings.keyBindDrop.func_151463_i())
            {
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, func_146271_m() ? 1 : 0, ClickType.THROW);
            }
        }
    }

    protected boolean func_146983_a(int p_146983_1_)
    {
        if (this.field_146297_k.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null)
        {
            for (int i = 0; i < 9; ++i)
            {
                if (p_146983_1_ == this.field_146297_k.gameSettings.keyBindsHotbar[i].func_151463_i())
                {
                    this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, i, ClickType.SWAP);
                    return true;
                }
            }
        }

        return false;
    }

    public void func_146281_b()
    {
        if (this.field_146297_k.player != null)
        {
            this.container.onContainerClosed(this.field_146297_k.player);
        }
    }

    public boolean func_73868_f()
    {
        return false;
    }

    public void func_73876_c()
    {
        super.func_73876_c();

        if (!this.field_146297_k.player.isAlive() || this.field_146297_k.player.removed)
        {
            this.field_146297_k.player.closeScreen();
        }
    }
}

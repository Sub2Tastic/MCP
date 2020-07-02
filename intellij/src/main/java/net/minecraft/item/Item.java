package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class Item
{
    public static final RegistryNamespaced<ResourceLocation, Item> field_150901_e = new RegistryNamespaced<ResourceLocation, Item>();
    private static final Map<Block, Item> BLOCK_TO_ITEM = Maps.<Block, Item>newHashMap();
    private static final IItemPropertyGetter DAMAGED_GETTER = new IItemPropertyGetter()
    {
        public float func_185085_a(ItemStack p_185085_1_, @Nullable World p_185085_2_, @Nullable EntityLivingBase p_185085_3_)
        {
            return p_185085_1_.isDamaged() ? 1.0F : 0.0F;
        }
    };
    private static final IItemPropertyGetter DAMAGE_GETTER = new IItemPropertyGetter()
    {
        public float func_185085_a(ItemStack p_185085_1_, @Nullable World p_185085_2_, @Nullable EntityLivingBase p_185085_3_)
        {
            return MathHelper.clamp((float)p_185085_1_.getDamage() / (float)p_185085_1_.getMaxDamage(), 0.0F, 1.0F);
        }
    };
    private static final IItemPropertyGetter LEFTHANDED_GETTER = new IItemPropertyGetter()
    {
        public float func_185085_a(ItemStack p_185085_1_, @Nullable World p_185085_2_, @Nullable EntityLivingBase p_185085_3_)
        {
            return p_185085_3_ != null && p_185085_3_.getPrimaryHand() != EnumHandSide.RIGHT ? 1.0F : 0.0F;
        }
    };
    private static final IItemPropertyGetter COOLDOWN_GETTER = new IItemPropertyGetter()
    {
        public float func_185085_a(ItemStack p_185085_1_, @Nullable World p_185085_2_, @Nullable EntityLivingBase p_185085_3_)
        {
            return p_185085_3_ instanceof EntityPlayer ? ((EntityPlayer)p_185085_3_).getCooldownTracker().getCooldown(p_185085_1_.getItem(), 0.0F) : 0.0F;
        }
    };
    private final IRegistry<ResourceLocation, IItemPropertyGetter> properties = new RegistrySimple<ResourceLocation, IItemPropertyGetter>();
    protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    protected static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    private CreativeTabs group;
    protected static Random random = new Random();
    protected int maxStackSize = 64;
    private int maxDamage;
    protected boolean field_77789_bW;
    protected boolean field_77787_bX;
    private Item containerItem;
    private String translationKey;

    public static int getIdFromItem(Item itemIn)
    {
        return itemIn == null ? 0 : field_150901_e.getId(itemIn);
    }

    public static Item getItemById(int id)
    {
        return field_150901_e.func_148754_a(id);
    }

    public static Item getItemFromBlock(Block blockIn)
    {
        Item item = BLOCK_TO_ITEM.get(blockIn);
        return item == null ? Items.AIR : item;
    }

    @Nullable
    public static Item func_111206_d(String p_111206_0_)
    {
        Item item = field_150901_e.getOrDefault(new ResourceLocation(p_111206_0_));

        if (item == null)
        {
            try
            {
                return getItemById(Integer.parseInt(p_111206_0_));
            }
            catch (NumberFormatException var3)
            {
                ;
            }
        }

        return item;
    }

    /**
     * Creates a new override param for item models. See usage in clock, compass, elytra, etc.
     */
    public final void addPropertyOverride(ResourceLocation key, IItemPropertyGetter getter)
    {
        this.properties.func_82595_a(key, getter);
    }

    @Nullable
    public IItemPropertyGetter getPropertyGetter(ResourceLocation key)
    {
        return this.properties.getOrDefault(key);
    }

    public boolean hasCustomProperties()
    {
        return !this.properties.keySet().isEmpty();
    }

    /**
     * Called when an ItemStack with NBT data is read to potentially that ItemStack's NBT data
     */
    public boolean updateItemStackNBT(NBTTagCompound nbt)
    {
        return false;
    }

    public Item()
    {
        this.addPropertyOverride(new ResourceLocation("lefthanded"), LEFTHANDED_GETTER);
        this.addPropertyOverride(new ResourceLocation("cooldown"), COOLDOWN_GETTER);
    }

    public Item func_77625_d(int p_77625_1_)
    {
        this.maxStackSize = p_77625_1_;
        return this;
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        return EnumActionResult.PASS;
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        return 1.0F;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        return stack;
    }

    /**
     * Returns the maximum size of the stack for a specific item.
     */
    public int getMaxStackSize()
    {
        return this.maxStackSize;
    }

    public int func_77647_b(int p_77647_1_)
    {
        return 0;
    }

    public boolean func_77614_k()
    {
        return this.field_77787_bX;
    }

    protected Item func_77627_a(boolean p_77627_1_)
    {
        this.field_77787_bX = p_77627_1_;
        return this;
    }

    /**
     * Returns the maximum damage an item can take.
     */
    public int getMaxDamage()
    {
        return this.maxDamage;
    }

    protected Item func_77656_e(int p_77656_1_)
    {
        this.maxDamage = p_77656_1_;

        if (p_77656_1_ > 0)
        {
            this.addPropertyOverride(new ResourceLocation("damaged"), DAMAGED_GETTER);
            this.addPropertyOverride(new ResourceLocation("damage"), DAMAGE_GETTER);
        }

        return this;
    }

    public boolean isDamageable()
    {
        return this.maxDamage > 0 && (!this.field_77787_bX || this.maxStackSize == 1);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        return false;
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        return false;
    }

    /**
     * Check whether this Item can harvest the given Block
     */
    public boolean canHarvestBlock(IBlockState blockIn)
    {
        return false;
    }

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
        return false;
    }

    public Item func_77664_n()
    {
        this.field_77789_bW = true;
        return this;
    }

    public boolean func_77662_d()
    {
        return this.field_77789_bW;
    }

    public boolean func_77629_n_()
    {
        return false;
    }

    public Item func_77655_b(String p_77655_1_)
    {
        this.translationKey = p_77655_1_;
        return this;
    }

    public String func_77657_g(ItemStack p_77657_1_)
    {
        return I18n.func_74838_a(this.getTranslationKey(p_77657_1_));
    }

    /**
     * Returns the unlocalized name of this item.
     */
    public String getTranslationKey()
    {
        return "item." + this.translationKey;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getTranslationKey(ItemStack stack)
    {
        return "item." + this.translationKey;
    }

    public Item func_77642_a(Item p_77642_1_)
    {
        this.containerItem = p_77642_1_;
        return this;
    }

    /**
     * If this function returns true (or the item is damageable), the ItemStack's NBT tag will be sent to the client.
     */
    public boolean shouldSyncTag()
    {
        return true;
    }

    @Nullable
    public Item getContainerItem()
    {
        return this.containerItem;
    }

    /**
     * True if this Item has a container item (a.k.a. crafting result)
     */
    public boolean hasContainerItem()
    {
        return this.containerItem != null;
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
    }

    /**
     * Called when item is crafted/smelted. Used only by maps so far.
     */
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
    }

    /**
     * Returns {@code true} if this is a complex item.
     */
    public boolean isComplex()
    {
        return false;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getUseAction(ItemStack stack)
    {
        return EnumAction.NONE;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack)
    {
        return 0;
    }

    /**
     * Called when the player stops using an Item (stops holding the right mouse button).
     */
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
    }

    public String func_77653_i(ItemStack p_77653_1_)
    {
        return I18n.func_74838_a(this.func_77657_g(p_77653_1_) + ".name").trim();
    }

    /**
     * Returns true if this item has an enchantment glint. By default, this returns
     * <code>stack.isItemEnchanted()</code>, but other items can override it (for instance, written books always return
     * true).
     *  
     * Note that if you override this method, you generally want to also call the super version (on {@link Item}) to get
     * the glint for enchanted items. Of course, that is unnecessary if the overwritten version always returns true.
     */
    public boolean hasEffect(ItemStack stack)
    {
        return stack.isEnchanted();
    }

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack stack)
    {
        return stack.isEnchanted() ? EnumRarity.RARE : EnumRarity.COMMON;
    }

    /**
     * Checks isDamagable and if it cannot be stacked
     */
    public boolean isEnchantable(ItemStack stack)
    {
        return this.getMaxStackSize() == 1 && this.isDamageable();
    }

    protected RayTraceResult func_77621_a(World p_77621_1_, EntityPlayer p_77621_2_, boolean p_77621_3_)
    {
        float f = p_77621_2_.rotationPitch;
        float f1 = p_77621_2_.rotationYaw;
        double d0 = p_77621_2_.posX;
        double d1 = p_77621_2_.posY + (double)p_77621_2_.getEyeHeight();
        double d2 = p_77621_2_.posZ;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = 5.0D;
        Vec3d vec3d1 = vec3d.add((double)f6 * 5.0D, (double)f5 * 5.0D, (double)f7 * 5.0D);
        return p_77621_1_.func_147447_a(vec3d, vec3d1, p_77621_3_, !p_77621_3_, false);
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return 0;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        if (this.isInGroup(group))
        {
            items.add(new ItemStack(this));
        }
    }

    protected boolean isInGroup(CreativeTabs group)
    {
        CreativeTabs creativetabs = this.getGroup();
        return creativetabs != null && (group == CreativeTabs.SEARCH || group == creativetabs);
    }

    @Nullable

    /**
     * gets the CreativeTab this item is displayed on
     */
    public CreativeTabs getGroup()
    {
        return this.group;
    }

    public Item func_77637_a(CreativeTabs p_77637_1_)
    {
        this.group = p_77637_1_;
        return this;
    }

    public boolean func_82788_x()
    {
        return false;
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return false;
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        return HashMultimap.<String, AttributeModifier>create();
    }

    public static void func_150900_l()
    {
        func_179214_a(Blocks.AIR, new ItemAir(Blocks.AIR));
        func_179214_a(Blocks.STONE, (new ItemMultiTexture(Blocks.STONE, Blocks.STONE, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockStone.EnumType.func_176643_a(p_apply_1_.func_77960_j()).func_176644_c();
            }
        })).func_77655_b("stone"));
        func_179214_a(Blocks.GRASS, new ItemColored(Blocks.GRASS, false));
        func_179214_a(Blocks.DIRT, (new ItemMultiTexture(Blocks.DIRT, Blocks.DIRT, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockDirt.DirtType.func_176924_a(p_apply_1_.func_77960_j()).func_176927_c();
            }
        })).func_77655_b("dirt"));
        func_179216_c(Blocks.COBBLESTONE);
        func_179214_a(Blocks.field_150344_f, (new ItemMultiTexture(Blocks.field_150344_f, Blocks.field_150344_f, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockPlanks.EnumType.func_176837_a(p_apply_1_.func_77960_j()).func_176840_c();
            }
        })).func_77655_b("wood"));
        func_179214_a(Blocks.field_150345_g, (new ItemMultiTexture(Blocks.field_150345_g, Blocks.field_150345_g, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockPlanks.EnumType.func_176837_a(p_apply_1_.func_77960_j()).func_176840_c();
            }
        })).func_77655_b("sapling"));
        func_179216_c(Blocks.BEDROCK);
        func_179214_a(Blocks.SAND, (new ItemMultiTexture(Blocks.SAND, Blocks.SAND, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockSand.EnumType.func_176686_a(p_apply_1_.func_77960_j()).func_176685_d();
            }
        })).func_77655_b("sand"));
        func_179216_c(Blocks.GRAVEL);
        func_179216_c(Blocks.GOLD_ORE);
        func_179216_c(Blocks.IRON_ORE);
        func_179216_c(Blocks.COAL_ORE);
        func_179214_a(Blocks.field_150364_r, (new ItemMultiTexture(Blocks.field_150364_r, Blocks.field_150364_r, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockPlanks.EnumType.func_176837_a(p_apply_1_.func_77960_j()).func_176840_c();
            }
        })).func_77655_b("log"));
        func_179214_a(Blocks.field_150363_s, (new ItemMultiTexture(Blocks.field_150363_s, Blocks.field_150363_s, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockPlanks.EnumType.func_176837_a(p_apply_1_.func_77960_j() + 4).func_176840_c();
            }
        })).func_77655_b("log"));
        func_179214_a(Blocks.field_150362_t, (new ItemLeaves(Blocks.field_150362_t)).func_77655_b("leaves"));
        func_179214_a(Blocks.field_150361_u, (new ItemLeaves(Blocks.field_150361_u)).func_77655_b("leaves"));
        func_179214_a(Blocks.SPONGE, (new ItemMultiTexture(Blocks.SPONGE, Blocks.SPONGE, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return (p_apply_1_.func_77960_j() & 1) == 1 ? "wet" : "dry";
            }
        })).func_77655_b("sponge"));
        func_179216_c(Blocks.GLASS);
        func_179216_c(Blocks.LAPIS_ORE);
        func_179216_c(Blocks.LAPIS_BLOCK);
        func_179216_c(Blocks.DISPENSER);
        func_179214_a(Blocks.SANDSTONE, (new ItemMultiTexture(Blocks.SANDSTONE, Blocks.SANDSTONE, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockSandStone.EnumType.func_176673_a(p_apply_1_.func_77960_j()).func_176676_c();
            }
        })).func_77655_b("sandStone"));
        func_179216_c(Blocks.field_150323_B);
        func_179216_c(Blocks.field_150318_D);
        func_179216_c(Blocks.DETECTOR_RAIL);
        func_179214_a(Blocks.STICKY_PISTON, new ItemPiston(Blocks.STICKY_PISTON));
        func_179216_c(Blocks.field_150321_G);
        func_179214_a(Blocks.field_150329_H, (new ItemColored(Blocks.field_150329_H, true)).func_150943_a(new String[] {"shrub", "grass", "fern"}));
        func_179216_c(Blocks.field_150330_I);
        func_179214_a(Blocks.PISTON, new ItemPiston(Blocks.PISTON));
        func_179214_a(Blocks.field_150325_L, (new ItemCloth(Blocks.field_150325_L)).func_77655_b("cloth"));
        func_179214_a(Blocks.field_150327_N, (new ItemMultiTexture(Blocks.field_150327_N, Blocks.field_150327_N, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockFlower.EnumFlowerType.func_176967_a(BlockFlower.EnumFlowerColor.YELLOW, p_apply_1_.func_77960_j()).func_176963_d();
            }
        })).func_77655_b("flower"));
        func_179214_a(Blocks.field_150328_O, (new ItemMultiTexture(Blocks.field_150328_O, Blocks.field_150328_O, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockFlower.EnumFlowerType.func_176967_a(BlockFlower.EnumFlowerColor.RED, p_apply_1_.func_77960_j()).func_176963_d();
            }
        })).func_77655_b("rose"));
        func_179216_c(Blocks.BROWN_MUSHROOM);
        func_179216_c(Blocks.RED_MUSHROOM);
        func_179216_c(Blocks.GOLD_BLOCK);
        func_179216_c(Blocks.IRON_BLOCK);
        func_179214_a(Blocks.STONE_SLAB, (new ItemSlab(Blocks.STONE_SLAB, Blocks.STONE_SLAB, Blocks.field_150334_T)).func_77655_b("stoneSlab"));
        func_179216_c(Blocks.field_150336_V);
        func_179216_c(Blocks.TNT);
        func_179216_c(Blocks.BOOKSHELF);
        func_179216_c(Blocks.MOSSY_COBBLESTONE);
        func_179216_c(Blocks.OBSIDIAN);
        func_179216_c(Blocks.TORCH);
        func_179216_c(Blocks.END_ROD);
        func_179216_c(Blocks.CHORUS_PLANT);
        func_179216_c(Blocks.CHORUS_FLOWER);
        func_179216_c(Blocks.PURPUR_BLOCK);
        func_179216_c(Blocks.PURPUR_PILLAR);
        func_179216_c(Blocks.PURPUR_STAIRS);
        func_179214_a(Blocks.PURPUR_SLAB, (new ItemSlab(Blocks.PURPUR_SLAB, Blocks.PURPUR_SLAB, Blocks.field_185770_cW)).func_77655_b("purpurSlab"));
        func_179216_c(Blocks.SPAWNER);
        func_179216_c(Blocks.OAK_STAIRS);
        func_179216_c(Blocks.CHEST);
        func_179216_c(Blocks.DIAMOND_ORE);
        func_179216_c(Blocks.DIAMOND_BLOCK);
        func_179216_c(Blocks.CRAFTING_TABLE);
        func_179216_c(Blocks.FARMLAND);
        func_179216_c(Blocks.FURNACE);
        func_179216_c(Blocks.LADDER);
        func_179216_c(Blocks.RAIL);
        func_179216_c(Blocks.field_150446_ar);
        func_179216_c(Blocks.LEVER);
        func_179216_c(Blocks.STONE_PRESSURE_PLATE);
        func_179216_c(Blocks.field_150452_aw);
        func_179216_c(Blocks.REDSTONE_ORE);
        func_179216_c(Blocks.REDSTONE_TORCH);
        func_179216_c(Blocks.STONE_BUTTON);
        func_179214_a(Blocks.field_150431_aC, new ItemSnow(Blocks.field_150431_aC));
        func_179216_c(Blocks.ICE);
        func_179216_c(Blocks.SNOW);
        func_179216_c(Blocks.CACTUS);
        func_179216_c(Blocks.CLAY);
        func_179216_c(Blocks.JUKEBOX);
        func_179216_c(Blocks.OAK_FENCE);
        func_179216_c(Blocks.SPRUCE_FENCE);
        func_179216_c(Blocks.BIRCH_FENCE);
        func_179216_c(Blocks.JUNGLE_FENCE);
        func_179216_c(Blocks.DARK_OAK_FENCE);
        func_179216_c(Blocks.ACACIA_FENCE);
        func_179216_c(Blocks.PUMPKIN);
        func_179216_c(Blocks.NETHERRACK);
        func_179216_c(Blocks.SOUL_SAND);
        func_179216_c(Blocks.GLOWSTONE);
        func_179216_c(Blocks.field_150428_aP);
        func_179216_c(Blocks.field_150415_aT);
        func_179214_a(Blocks.field_150418_aU, (new ItemMultiTexture(Blocks.field_150418_aU, Blocks.field_150418_aU, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockSilverfish.EnumType.func_176879_a(p_apply_1_.func_77960_j()).func_176882_c();
            }
        })).func_77655_b("monsterStoneEgg"));
        func_179214_a(Blocks.field_150417_aV, (new ItemMultiTexture(Blocks.field_150417_aV, Blocks.field_150417_aV, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockStoneBrick.EnumType.func_176613_a(p_apply_1_.func_77960_j()).func_176614_c();
            }
        })).func_77655_b("stonebricksmooth"));
        func_179216_c(Blocks.BROWN_MUSHROOM_BLOCK);
        func_179216_c(Blocks.RED_MUSHROOM_BLOCK);
        func_179216_c(Blocks.IRON_BARS);
        func_179216_c(Blocks.GLASS_PANE);
        func_179216_c(Blocks.MELON);
        func_179214_a(Blocks.VINE, new ItemColored(Blocks.VINE, false));
        func_179216_c(Blocks.OAK_FENCE_GATE);
        func_179216_c(Blocks.SPRUCE_FENCE_GATE);
        func_179216_c(Blocks.BIRCH_FENCE_GATE);
        func_179216_c(Blocks.JUNGLE_FENCE_GATE);
        func_179216_c(Blocks.DARK_OAK_FENCE_GATE);
        func_179216_c(Blocks.ACACIA_FENCE_GATE);
        func_179216_c(Blocks.BRICK_STAIRS);
        func_179216_c(Blocks.STONE_BRICK_STAIRS);
        func_179216_c(Blocks.MYCELIUM);
        func_179214_a(Blocks.field_150392_bi, new ItemLilyPad(Blocks.field_150392_bi));
        func_179216_c(Blocks.field_150385_bj);
        func_179216_c(Blocks.NETHER_BRICK_FENCE);
        func_179216_c(Blocks.NETHER_BRICK_STAIRS);
        func_179216_c(Blocks.ENCHANTING_TABLE);
        func_179216_c(Blocks.END_PORTAL_FRAME);
        func_179216_c(Blocks.END_STONE);
        func_179216_c(Blocks.field_185772_cY);
        func_179216_c(Blocks.DRAGON_EGG);
        func_179216_c(Blocks.REDSTONE_LAMP);
        func_179214_a(Blocks.field_150376_bx, (new ItemSlab(Blocks.field_150376_bx, Blocks.field_150376_bx, Blocks.field_150373_bw)).func_77655_b("woodSlab"));
        func_179216_c(Blocks.SANDSTONE_STAIRS);
        func_179216_c(Blocks.EMERALD_ORE);
        func_179216_c(Blocks.ENDER_CHEST);
        func_179216_c(Blocks.TRIPWIRE_HOOK);
        func_179216_c(Blocks.EMERALD_BLOCK);
        func_179216_c(Blocks.SPRUCE_STAIRS);
        func_179216_c(Blocks.BIRCH_STAIRS);
        func_179216_c(Blocks.JUNGLE_STAIRS);
        func_179216_c(Blocks.COMMAND_BLOCK);
        func_179216_c(Blocks.BEACON);
        func_179214_a(Blocks.COBBLESTONE_WALL, (new ItemMultiTexture(Blocks.COBBLESTONE_WALL, Blocks.COBBLESTONE_WALL, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockWall.EnumType.func_176660_a(p_apply_1_.func_77960_j()).func_176659_c();
            }
        })).func_77655_b("cobbleWall"));
        func_179216_c(Blocks.field_150471_bO);
        func_179214_a(Blocks.ANVIL, (new ItemAnvilBlock(Blocks.ANVIL)).func_77655_b("anvil"));
        func_179216_c(Blocks.TRAPPED_CHEST);
        func_179216_c(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
        func_179216_c(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
        func_179216_c(Blocks.DAYLIGHT_DETECTOR);
        func_179216_c(Blocks.REDSTONE_BLOCK);
        func_179216_c(Blocks.field_150449_bY);
        func_179216_c(Blocks.HOPPER);
        func_179214_a(Blocks.QUARTZ_BLOCK, (new ItemMultiTexture(Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK, new String[] {"default", "chiseled", "lines"})).func_77655_b("quartzBlock"));
        func_179216_c(Blocks.QUARTZ_STAIRS);
        func_179216_c(Blocks.ACTIVATOR_RAIL);
        func_179216_c(Blocks.DROPPER);
        func_179214_a(Blocks.field_150406_ce, (new ItemCloth(Blocks.field_150406_ce)).func_77655_b("clayHardenedStained"));
        func_179216_c(Blocks.BARRIER);
        func_179216_c(Blocks.IRON_TRAPDOOR);
        func_179216_c(Blocks.HAY_BLOCK);
        func_179214_a(Blocks.field_150404_cg, (new ItemCloth(Blocks.field_150404_cg)).func_77655_b("woolCarpet"));
        func_179216_c(Blocks.TERRACOTTA);
        func_179216_c(Blocks.COAL_BLOCK);
        func_179216_c(Blocks.PACKED_ICE);
        func_179216_c(Blocks.ACACIA_STAIRS);
        func_179216_c(Blocks.DARK_OAK_STAIRS);
        func_179216_c(Blocks.SLIME_BLOCK);
        func_179216_c(Blocks.GRASS_PATH);
        func_179214_a(Blocks.field_150398_cm, (new ItemMultiTexture(Blocks.field_150398_cm, Blocks.field_150398_cm, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockDoublePlant.EnumPlantType.func_176938_a(p_apply_1_.func_77960_j()).func_176939_c();
            }
        })).func_77655_b("doublePlant"));
        func_179214_a(Blocks.field_150399_cn, (new ItemCloth(Blocks.field_150399_cn)).func_77655_b("stainedGlass"));
        func_179214_a(Blocks.field_150397_co, (new ItemCloth(Blocks.field_150397_co)).func_77655_b("stainedGlassPane"));
        func_179214_a(Blocks.PRISMARINE, (new ItemMultiTexture(Blocks.PRISMARINE, Blocks.PRISMARINE, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockPrismarine.EnumType.func_176810_a(p_apply_1_.func_77960_j()).func_176809_c();
            }
        })).func_77655_b("prismarine"));
        func_179216_c(Blocks.SEA_LANTERN);
        func_179214_a(Blocks.RED_SANDSTONE, (new ItemMultiTexture(Blocks.RED_SANDSTONE, Blocks.RED_SANDSTONE, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockRedSandstone.EnumType.func_176825_a(p_apply_1_.func_77960_j()).func_176828_c();
            }
        })).func_77655_b("redSandStone"));
        func_179216_c(Blocks.RED_SANDSTONE_STAIRS);
        func_179214_a(Blocks.field_180389_cP, (new ItemSlab(Blocks.field_180389_cP, Blocks.field_180389_cP, Blocks.field_180388_cO)).func_77655_b("stoneSlab2"));
        func_179216_c(Blocks.REPEATING_COMMAND_BLOCK);
        func_179216_c(Blocks.CHAIN_COMMAND_BLOCK);
        func_179216_c(Blocks.field_189877_df);
        func_179216_c(Blocks.NETHER_WART_BLOCK);
        func_179216_c(Blocks.field_189879_dh);
        func_179216_c(Blocks.BONE_BLOCK);
        func_179216_c(Blocks.STRUCTURE_VOID);
        func_179216_c(Blocks.OBSERVER);
        func_179214_a(Blocks.WHITE_SHULKER_BOX, new ItemShulkerBox(Blocks.WHITE_SHULKER_BOX));
        func_179214_a(Blocks.ORANGE_SHULKER_BOX, new ItemShulkerBox(Blocks.ORANGE_SHULKER_BOX));
        func_179214_a(Blocks.MAGENTA_SHULKER_BOX, new ItemShulkerBox(Blocks.MAGENTA_SHULKER_BOX));
        func_179214_a(Blocks.LIGHT_BLUE_SHULKER_BOX, new ItemShulkerBox(Blocks.LIGHT_BLUE_SHULKER_BOX));
        func_179214_a(Blocks.YELLOW_SHULKER_BOX, new ItemShulkerBox(Blocks.YELLOW_SHULKER_BOX));
        func_179214_a(Blocks.LIME_SHULKER_BOX, new ItemShulkerBox(Blocks.LIME_SHULKER_BOX));
        func_179214_a(Blocks.PINK_SHULKER_BOX, new ItemShulkerBox(Blocks.PINK_SHULKER_BOX));
        func_179214_a(Blocks.GRAY_SHULKER_BOX, new ItemShulkerBox(Blocks.GRAY_SHULKER_BOX));
        func_179214_a(Blocks.field_190985_dt, new ItemShulkerBox(Blocks.field_190985_dt));
        func_179214_a(Blocks.CYAN_SHULKER_BOX, new ItemShulkerBox(Blocks.CYAN_SHULKER_BOX));
        func_179214_a(Blocks.PURPLE_SHULKER_BOX, new ItemShulkerBox(Blocks.PURPLE_SHULKER_BOX));
        func_179214_a(Blocks.BLUE_SHULKER_BOX, new ItemShulkerBox(Blocks.BLUE_SHULKER_BOX));
        func_179214_a(Blocks.BROWN_SHULKER_BOX, new ItemShulkerBox(Blocks.BROWN_SHULKER_BOX));
        func_179214_a(Blocks.GREEN_SHULKER_BOX, new ItemShulkerBox(Blocks.GREEN_SHULKER_BOX));
        func_179214_a(Blocks.RED_SHULKER_BOX, new ItemShulkerBox(Blocks.RED_SHULKER_BOX));
        func_179214_a(Blocks.BLACK_SHULKER_BOX, new ItemShulkerBox(Blocks.BLACK_SHULKER_BOX));
        func_179216_c(Blocks.WHITE_GLAZED_TERRACOTTA);
        func_179216_c(Blocks.ORANGE_GLAZED_TERRACOTTA);
        func_179216_c(Blocks.MAGENTA_GLAZED_TERRACOTTA);
        func_179216_c(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA);
        func_179216_c(Blocks.YELLOW_GLAZED_TERRACOTTA);
        func_179216_c(Blocks.LIME_GLAZED_TERRACOTTA);
        func_179216_c(Blocks.PINK_GLAZED_TERRACOTTA);
        func_179216_c(Blocks.GRAY_GLAZED_TERRACOTTA);
        func_179216_c(Blocks.field_192435_dJ);
        func_179216_c(Blocks.CYAN_GLAZED_TERRACOTTA);
        func_179216_c(Blocks.PURPLE_GLAZED_TERRACOTTA);
        func_179216_c(Blocks.BLUE_GLAZED_TERRACOTTA);
        func_179216_c(Blocks.BROWN_GLAZED_TERRACOTTA);
        func_179216_c(Blocks.GREEN_GLAZED_TERRACOTTA);
        func_179216_c(Blocks.RED_GLAZED_TERRACOTTA);
        func_179216_c(Blocks.BLACK_GLAZED_TERRACOTTA);
        func_179214_a(Blocks.field_192443_dR, (new ItemCloth(Blocks.field_192443_dR)).func_77655_b("concrete"));
        func_179214_a(Blocks.field_192444_dS, (new ItemCloth(Blocks.field_192444_dS)).func_77655_b("concrete_powder"));
        func_179216_c(Blocks.STRUCTURE_BLOCK);
        func_179217_a(256, "iron_shovel", (new ItemSpade(Item.ToolMaterial.IRON)).func_77655_b("shovelIron"));
        func_179217_a(257, "iron_pickaxe", (new ItemPickaxe(Item.ToolMaterial.IRON)).func_77655_b("pickaxeIron"));
        func_179217_a(258, "iron_axe", (new ItemAxe(Item.ToolMaterial.IRON)).func_77655_b("hatchetIron"));
        func_179217_a(259, "flint_and_steel", (new ItemFlintAndSteel()).func_77655_b("flintAndSteel"));
        func_179217_a(260, "apple", (new ItemFood(4, 0.3F, false)).func_77655_b("apple"));
        func_179217_a(261, "bow", (new ItemBow()).func_77655_b("bow"));
        func_179217_a(262, "arrow", (new ItemArrow()).func_77655_b("arrow"));
        func_179217_a(263, "coal", (new ItemCoal()).func_77655_b("coal"));
        func_179217_a(264, "diamond", (new Item()).func_77655_b("diamond").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(265, "iron_ingot", (new Item()).func_77655_b("ingotIron").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(266, "gold_ingot", (new Item()).func_77655_b("ingotGold").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(267, "iron_sword", (new ItemSword(Item.ToolMaterial.IRON)).func_77655_b("swordIron"));
        func_179217_a(268, "wooden_sword", (new ItemSword(Item.ToolMaterial.WOOD)).func_77655_b("swordWood"));
        func_179217_a(269, "wooden_shovel", (new ItemSpade(Item.ToolMaterial.WOOD)).func_77655_b("shovelWood"));
        func_179217_a(270, "wooden_pickaxe", (new ItemPickaxe(Item.ToolMaterial.WOOD)).func_77655_b("pickaxeWood"));
        func_179217_a(271, "wooden_axe", (new ItemAxe(Item.ToolMaterial.WOOD)).func_77655_b("hatchetWood"));
        func_179217_a(272, "stone_sword", (new ItemSword(Item.ToolMaterial.STONE)).func_77655_b("swordStone"));
        func_179217_a(273, "stone_shovel", (new ItemSpade(Item.ToolMaterial.STONE)).func_77655_b("shovelStone"));
        func_179217_a(274, "stone_pickaxe", (new ItemPickaxe(Item.ToolMaterial.STONE)).func_77655_b("pickaxeStone"));
        func_179217_a(275, "stone_axe", (new ItemAxe(Item.ToolMaterial.STONE)).func_77655_b("hatchetStone"));
        func_179217_a(276, "diamond_sword", (new ItemSword(Item.ToolMaterial.DIAMOND)).func_77655_b("swordDiamond"));
        func_179217_a(277, "diamond_shovel", (new ItemSpade(Item.ToolMaterial.DIAMOND)).func_77655_b("shovelDiamond"));
        func_179217_a(278, "diamond_pickaxe", (new ItemPickaxe(Item.ToolMaterial.DIAMOND)).func_77655_b("pickaxeDiamond"));
        func_179217_a(279, "diamond_axe", (new ItemAxe(Item.ToolMaterial.DIAMOND)).func_77655_b("hatchetDiamond"));
        func_179217_a(280, "stick", (new Item()).func_77664_n().func_77655_b("stick").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(281, "bowl", (new Item()).func_77655_b("bowl").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(282, "mushroom_stew", (new ItemSoup(6)).func_77655_b("mushroomStew"));
        func_179217_a(283, "golden_sword", (new ItemSword(Item.ToolMaterial.GOLD)).func_77655_b("swordGold"));
        func_179217_a(284, "golden_shovel", (new ItemSpade(Item.ToolMaterial.GOLD)).func_77655_b("shovelGold"));
        func_179217_a(285, "golden_pickaxe", (new ItemPickaxe(Item.ToolMaterial.GOLD)).func_77655_b("pickaxeGold"));
        func_179217_a(286, "golden_axe", (new ItemAxe(Item.ToolMaterial.GOLD)).func_77655_b("hatchetGold"));
        func_179217_a(287, "string", (new ItemBlockSpecial(Blocks.TRIPWIRE)).func_77655_b("string").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(288, "feather", (new Item()).func_77655_b("feather").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(289, "gunpowder", (new Item()).func_77655_b("sulphur").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(290, "wooden_hoe", (new ItemHoe(Item.ToolMaterial.WOOD)).func_77655_b("hoeWood"));
        func_179217_a(291, "stone_hoe", (new ItemHoe(Item.ToolMaterial.STONE)).func_77655_b("hoeStone"));
        func_179217_a(292, "iron_hoe", (new ItemHoe(Item.ToolMaterial.IRON)).func_77655_b("hoeIron"));
        func_179217_a(293, "diamond_hoe", (new ItemHoe(Item.ToolMaterial.DIAMOND)).func_77655_b("hoeDiamond"));
        func_179217_a(294, "golden_hoe", (new ItemHoe(Item.ToolMaterial.GOLD)).func_77655_b("hoeGold"));
        func_179217_a(295, "wheat_seeds", (new ItemSeeds(Blocks.WHEAT, Blocks.FARMLAND)).func_77655_b("seeds"));
        func_179217_a(296, "wheat", (new Item()).func_77655_b("wheat").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(297, "bread", (new ItemFood(5, 0.6F, false)).func_77655_b("bread"));
        func_179217_a(298, "leather_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD)).func_77655_b("helmetCloth"));
        func_179217_a(299, "leather_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.CHEST)).func_77655_b("chestplateCloth"));
        func_179217_a(300, "leather_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.LEGS)).func_77655_b("leggingsCloth"));
        func_179217_a(301, "leather_boots", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.FEET)).func_77655_b("bootsCloth"));
        func_179217_a(302, "chainmail_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.HEAD)).func_77655_b("helmetChain"));
        func_179217_a(303, "chainmail_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.CHEST)).func_77655_b("chestplateChain"));
        func_179217_a(304, "chainmail_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.LEGS)).func_77655_b("leggingsChain"));
        func_179217_a(305, "chainmail_boots", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.FEET)).func_77655_b("bootsChain"));
        func_179217_a(306, "iron_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.HEAD)).func_77655_b("helmetIron"));
        func_179217_a(307, "iron_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.CHEST)).func_77655_b("chestplateIron"));
        func_179217_a(308, "iron_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.LEGS)).func_77655_b("leggingsIron"));
        func_179217_a(309, "iron_boots", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.FEET)).func_77655_b("bootsIron"));
        func_179217_a(310, "diamond_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.HEAD)).func_77655_b("helmetDiamond"));
        func_179217_a(311, "diamond_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.CHEST)).func_77655_b("chestplateDiamond"));
        func_179217_a(312, "diamond_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.LEGS)).func_77655_b("leggingsDiamond"));
        func_179217_a(313, "diamond_boots", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.FEET)).func_77655_b("bootsDiamond"));
        func_179217_a(314, "golden_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.HEAD)).func_77655_b("helmetGold"));
        func_179217_a(315, "golden_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.CHEST)).func_77655_b("chestplateGold"));
        func_179217_a(316, "golden_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.LEGS)).func_77655_b("leggingsGold"));
        func_179217_a(317, "golden_boots", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.FEET)).func_77655_b("bootsGold"));
        func_179217_a(318, "flint", (new Item()).func_77655_b("flint").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(319, "porkchop", (new ItemFood(3, 0.3F, true)).func_77655_b("porkchopRaw"));
        func_179217_a(320, "cooked_porkchop", (new ItemFood(8, 0.8F, true)).func_77655_b("porkchopCooked"));
        func_179217_a(321, "painting", (new ItemHangingEntity(EntityPainting.class)).func_77655_b("painting"));
        func_179217_a(322, "golden_apple", (new ItemAppleGold(4, 1.2F, false)).func_77848_i().func_77655_b("appleGold"));
        func_179217_a(323, "sign", (new ItemSign()).func_77655_b("sign"));
        func_179217_a(324, "wooden_door", (new ItemDoor(Blocks.OAK_DOOR)).func_77655_b("doorOak"));
        Item item = (new ItemBucket(Blocks.AIR)).func_77655_b("bucket").func_77625_d(16);
        func_179217_a(325, "bucket", item);
        func_179217_a(326, "water_bucket", (new ItemBucket(Blocks.field_150358_i)).func_77655_b("bucketWater").func_77642_a(item));
        func_179217_a(327, "lava_bucket", (new ItemBucket(Blocks.field_150356_k)).func_77655_b("bucketLava").func_77642_a(item));
        func_179217_a(328, "minecart", (new ItemMinecart(EntityMinecart.Type.RIDEABLE)).func_77655_b("minecart"));
        func_179217_a(329, "saddle", (new ItemSaddle()).func_77655_b("saddle"));
        func_179217_a(330, "iron_door", (new ItemDoor(Blocks.IRON_DOOR)).func_77655_b("doorIron"));
        func_179217_a(331, "redstone", (new ItemRedstone()).func_77655_b("redstone"));
        func_179217_a(332, "snowball", (new ItemSnowball()).func_77655_b("snowball"));
        func_179217_a(333, "boat", new ItemBoat(EntityBoat.Type.OAK));
        func_179217_a(334, "leather", (new Item()).func_77655_b("leather").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(335, "milk_bucket", (new ItemBucketMilk()).func_77655_b("milk").func_77642_a(item));
        func_179217_a(336, "brick", (new Item()).func_77655_b("brick").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(337, "clay_ball", (new Item()).func_77655_b("clay").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(338, "reeds", (new ItemBlockSpecial(Blocks.field_150436_aH)).func_77655_b("reeds").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(339, "paper", (new Item()).func_77655_b("paper").func_77637_a(CreativeTabs.MISC));
        func_179217_a(340, "book", (new ItemBook()).func_77655_b("book").func_77637_a(CreativeTabs.MISC));
        func_179217_a(341, "slime_ball", (new Item()).func_77655_b("slimeball").func_77637_a(CreativeTabs.MISC));
        func_179217_a(342, "chest_minecart", (new ItemMinecart(EntityMinecart.Type.CHEST)).func_77655_b("minecartChest"));
        func_179217_a(343, "furnace_minecart", (new ItemMinecart(EntityMinecart.Type.FURNACE)).func_77655_b("minecartFurnace"));
        func_179217_a(344, "egg", (new ItemEgg()).func_77655_b("egg"));
        func_179217_a(345, "compass", (new ItemCompass()).func_77655_b("compass").func_77637_a(CreativeTabs.TOOLS));
        func_179217_a(346, "fishing_rod", (new ItemFishingRod()).func_77655_b("fishingRod"));
        func_179217_a(347, "clock", (new ItemClock()).func_77655_b("clock").func_77637_a(CreativeTabs.TOOLS));
        func_179217_a(348, "glowstone_dust", (new Item()).func_77655_b("yellowDust").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(349, "fish", (new ItemFishFood(false)).func_77655_b("fish").func_77627_a(true));
        func_179217_a(350, "cooked_fish", (new ItemFishFood(true)).func_77655_b("fish").func_77627_a(true));
        func_179217_a(351, "dye", (new ItemDye()).func_77655_b("dyePowder"));
        func_179217_a(352, "bone", (new Item()).func_77655_b("bone").func_77664_n().func_77637_a(CreativeTabs.MISC));
        func_179217_a(353, "sugar", (new Item()).func_77655_b("sugar").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(354, "cake", (new ItemBlockSpecial(Blocks.CAKE)).func_77625_d(1).func_77655_b("cake").func_77637_a(CreativeTabs.FOOD));
        func_179217_a(355, "bed", (new ItemBed()).func_77625_d(1).func_77655_b("bed"));
        func_179217_a(356, "repeater", (new ItemBlockSpecial(Blocks.field_150413_aR)).func_77655_b("diode").func_77637_a(CreativeTabs.REDSTONE));
        func_179217_a(357, "cookie", (new ItemFood(2, 0.1F, false)).func_77655_b("cookie"));
        func_179217_a(358, "filled_map", (new ItemMap()).func_77655_b("map"));
        func_179217_a(359, "shears", (new ItemShears()).func_77655_b("shears"));
        func_179217_a(360, "melon", (new ItemFood(2, 0.3F, false)).func_77655_b("melon"));
        func_179217_a(361, "pumpkin_seeds", (new ItemSeeds(Blocks.PUMPKIN_STEM, Blocks.FARMLAND)).func_77655_b("seeds_pumpkin"));
        func_179217_a(362, "melon_seeds", (new ItemSeeds(Blocks.MELON_STEM, Blocks.FARMLAND)).func_77655_b("seeds_melon"));
        func_179217_a(363, "beef", (new ItemFood(3, 0.3F, true)).func_77655_b("beefRaw"));
        func_179217_a(364, "cooked_beef", (new ItemFood(8, 0.8F, true)).func_77655_b("beefCooked"));
        func_179217_a(365, "chicken", (new ItemFood(2, 0.3F, true)).func_185070_a(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.3F).func_77655_b("chickenRaw"));
        func_179217_a(366, "cooked_chicken", (new ItemFood(6, 0.6F, true)).func_77655_b("chickenCooked"));
        func_179217_a(367, "rotten_flesh", (new ItemFood(4, 0.1F, true)).func_185070_a(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.8F).func_77655_b("rottenFlesh"));
        func_179217_a(368, "ender_pearl", (new ItemEnderPearl()).func_77655_b("enderPearl"));
        func_179217_a(369, "blaze_rod", (new Item()).func_77655_b("blazeRod").func_77637_a(CreativeTabs.MATERIALS).func_77664_n());
        func_179217_a(370, "ghast_tear", (new Item()).func_77655_b("ghastTear").func_77637_a(CreativeTabs.BREWING));
        func_179217_a(371, "gold_nugget", (new Item()).func_77655_b("goldNugget").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(372, "nether_wart", (new ItemSeeds(Blocks.NETHER_WART, Blocks.SOUL_SAND)).func_77655_b("netherStalkSeeds"));
        func_179217_a(373, "potion", (new ItemPotion()).func_77655_b("potion"));
        Item item1 = (new ItemGlassBottle()).func_77655_b("glassBottle");
        func_179217_a(374, "glass_bottle", item1);
        func_179217_a(375, "spider_eye", (new ItemFood(2, 0.8F, false)).func_185070_a(new PotionEffect(MobEffects.POISON, 100, 0), 1.0F).func_77655_b("spiderEye"));
        func_179217_a(376, "fermented_spider_eye", (new Item()).func_77655_b("fermentedSpiderEye").func_77637_a(CreativeTabs.BREWING));
        func_179217_a(377, "blaze_powder", (new Item()).func_77655_b("blazePowder").func_77637_a(CreativeTabs.BREWING));
        func_179217_a(378, "magma_cream", (new Item()).func_77655_b("magmaCream").func_77637_a(CreativeTabs.BREWING));
        func_179217_a(379, "brewing_stand", (new ItemBlockSpecial(Blocks.BREWING_STAND)).func_77655_b("brewingStand").func_77637_a(CreativeTabs.BREWING));
        func_179217_a(380, "cauldron", (new ItemBlockSpecial(Blocks.CAULDRON)).func_77655_b("cauldron").func_77637_a(CreativeTabs.BREWING));
        func_179217_a(381, "ender_eye", (new ItemEnderEye()).func_77655_b("eyeOfEnder"));
        func_179217_a(382, "speckled_melon", (new Item()).func_77655_b("speckledMelon").func_77637_a(CreativeTabs.BREWING));
        func_179217_a(383, "spawn_egg", (new ItemMonsterPlacer()).func_77655_b("monsterPlacer"));
        func_179217_a(384, "experience_bottle", (new ItemExpBottle()).func_77655_b("expBottle"));
        func_179217_a(385, "fire_charge", (new ItemFireball()).func_77655_b("fireball"));
        func_179217_a(386, "writable_book", (new ItemWritableBook()).func_77655_b("writingBook").func_77637_a(CreativeTabs.MISC));
        func_179217_a(387, "written_book", (new ItemWrittenBook()).func_77655_b("writtenBook").func_77625_d(16));
        func_179217_a(388, "emerald", (new Item()).func_77655_b("emerald").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(389, "item_frame", (new ItemHangingEntity(EntityItemFrame.class)).func_77655_b("frame"));
        func_179217_a(390, "flower_pot", (new ItemBlockSpecial(Blocks.FLOWER_POT)).func_77655_b("flowerPot").func_77637_a(CreativeTabs.DECORATIONS));
        func_179217_a(391, "carrot", (new ItemSeedFood(3, 0.6F, Blocks.CARROTS, Blocks.FARMLAND)).func_77655_b("carrots"));
        func_179217_a(392, "potato", (new ItemSeedFood(1, 0.3F, Blocks.POTATOES, Blocks.FARMLAND)).func_77655_b("potato"));
        func_179217_a(393, "baked_potato", (new ItemFood(5, 0.6F, false)).func_77655_b("potatoBaked"));
        func_179217_a(394, "poisonous_potato", (new ItemFood(2, 0.3F, false)).func_185070_a(new PotionEffect(MobEffects.POISON, 100, 0), 0.6F).func_77655_b("potatoPoisonous"));
        func_179217_a(395, "map", (new ItemEmptyMap()).func_77655_b("emptyMap"));
        func_179217_a(396, "golden_carrot", (new ItemFood(6, 1.2F, false)).func_77655_b("carrotGolden").func_77637_a(CreativeTabs.BREWING));
        func_179217_a(397, "skull", (new ItemSkull()).func_77655_b("skull"));
        func_179217_a(398, "carrot_on_a_stick", (new ItemCarrotOnAStick()).func_77655_b("carrotOnAStick"));
        func_179217_a(399, "nether_star", (new ItemSimpleFoiled()).func_77655_b("netherStar").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(400, "pumpkin_pie", (new ItemFood(8, 0.3F, false)).func_77655_b("pumpkinPie").func_77637_a(CreativeTabs.FOOD));
        func_179217_a(401, "fireworks", (new ItemFirework()).func_77655_b("fireworks"));
        func_179217_a(402, "firework_charge", (new ItemFireworkCharge()).func_77655_b("fireworksCharge").func_77637_a(CreativeTabs.MISC));
        func_179217_a(403, "enchanted_book", (new ItemEnchantedBook()).func_77625_d(1).func_77655_b("enchantedBook"));
        func_179217_a(404, "comparator", (new ItemBlockSpecial(Blocks.field_150441_bU)).func_77655_b("comparator").func_77637_a(CreativeTabs.REDSTONE));
        func_179217_a(405, "netherbrick", (new Item()).func_77655_b("netherbrick").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(406, "quartz", (new Item()).func_77655_b("netherquartz").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(407, "tnt_minecart", (new ItemMinecart(EntityMinecart.Type.TNT)).func_77655_b("minecartTnt"));
        func_179217_a(408, "hopper_minecart", (new ItemMinecart(EntityMinecart.Type.HOPPER)).func_77655_b("minecartHopper"));
        func_179217_a(409, "prismarine_shard", (new Item()).func_77655_b("prismarineShard").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(410, "prismarine_crystals", (new Item()).func_77655_b("prismarineCrystals").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(411, "rabbit", (new ItemFood(3, 0.3F, true)).func_77655_b("rabbitRaw"));
        func_179217_a(412, "cooked_rabbit", (new ItemFood(5, 0.6F, true)).func_77655_b("rabbitCooked"));
        func_179217_a(413, "rabbit_stew", (new ItemSoup(10)).func_77655_b("rabbitStew"));
        func_179217_a(414, "rabbit_foot", (new Item()).func_77655_b("rabbitFoot").func_77637_a(CreativeTabs.BREWING));
        func_179217_a(415, "rabbit_hide", (new Item()).func_77655_b("rabbitHide").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(416, "armor_stand", (new ItemArmorStand()).func_77655_b("armorStand").func_77625_d(16));
        func_179217_a(417, "iron_horse_armor", (new Item()).func_77655_b("horsearmormetal").func_77625_d(1).func_77637_a(CreativeTabs.MISC));
        func_179217_a(418, "golden_horse_armor", (new Item()).func_77655_b("horsearmorgold").func_77625_d(1).func_77637_a(CreativeTabs.MISC));
        func_179217_a(419, "diamond_horse_armor", (new Item()).func_77655_b("horsearmordiamond").func_77625_d(1).func_77637_a(CreativeTabs.MISC));
        func_179217_a(420, "lead", (new ItemLead()).func_77655_b("leash"));
        func_179217_a(421, "name_tag", (new ItemNameTag()).func_77655_b("nameTag"));
        func_179217_a(422, "command_block_minecart", (new ItemMinecart(EntityMinecart.Type.COMMAND_BLOCK)).func_77655_b("minecartCommandBlock").func_77637_a((CreativeTabs)null));
        func_179217_a(423, "mutton", (new ItemFood(2, 0.3F, true)).func_77655_b("muttonRaw"));
        func_179217_a(424, "cooked_mutton", (new ItemFood(6, 0.8F, true)).func_77655_b("muttonCooked"));
        func_179217_a(425, "banner", (new ItemBanner()).func_77655_b("banner"));
        func_179217_a(426, "end_crystal", new ItemEndCrystal());
        func_179217_a(427, "spruce_door", (new ItemDoor(Blocks.SPRUCE_DOOR)).func_77655_b("doorSpruce"));
        func_179217_a(428, "birch_door", (new ItemDoor(Blocks.BIRCH_DOOR)).func_77655_b("doorBirch"));
        func_179217_a(429, "jungle_door", (new ItemDoor(Blocks.JUNGLE_DOOR)).func_77655_b("doorJungle"));
        func_179217_a(430, "acacia_door", (new ItemDoor(Blocks.ACACIA_DOOR)).func_77655_b("doorAcacia"));
        func_179217_a(431, "dark_oak_door", (new ItemDoor(Blocks.DARK_OAK_DOOR)).func_77655_b("doorDarkOak"));
        func_179217_a(432, "chorus_fruit", (new ItemChorusFruit(4, 0.3F)).func_77848_i().func_77655_b("chorusFruit").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(433, "chorus_fruit_popped", (new Item()).func_77655_b("chorusFruitPopped").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(434, "beetroot", (new ItemFood(1, 0.6F, false)).func_77655_b("beetroot"));
        func_179217_a(435, "beetroot_seeds", (new ItemSeeds(Blocks.BEETROOTS, Blocks.FARMLAND)).func_77655_b("beetroot_seeds"));
        func_179217_a(436, "beetroot_soup", (new ItemSoup(6)).func_77655_b("beetroot_soup"));
        func_179217_a(437, "dragon_breath", (new Item()).func_77637_a(CreativeTabs.BREWING).func_77655_b("dragon_breath").func_77642_a(item1));
        func_179217_a(438, "splash_potion", (new ItemSplashPotion()).func_77655_b("splash_potion"));
        func_179217_a(439, "spectral_arrow", (new ItemSpectralArrow()).func_77655_b("spectral_arrow"));
        func_179217_a(440, "tipped_arrow", (new ItemTippedArrow()).func_77655_b("tipped_arrow"));
        func_179217_a(441, "lingering_potion", (new ItemLingeringPotion()).func_77655_b("lingering_potion"));
        func_179217_a(442, "shield", (new ItemShield()).func_77655_b("shield"));
        func_179217_a(443, "elytra", (new ItemElytra()).func_77655_b("elytra"));
        func_179217_a(444, "spruce_boat", new ItemBoat(EntityBoat.Type.SPRUCE));
        func_179217_a(445, "birch_boat", new ItemBoat(EntityBoat.Type.BIRCH));
        func_179217_a(446, "jungle_boat", new ItemBoat(EntityBoat.Type.JUNGLE));
        func_179217_a(447, "acacia_boat", new ItemBoat(EntityBoat.Type.ACACIA));
        func_179217_a(448, "dark_oak_boat", new ItemBoat(EntityBoat.Type.DARK_OAK));
        func_179217_a(449, "totem_of_undying", (new Item()).func_77655_b("totem").func_77625_d(1).func_77637_a(CreativeTabs.COMBAT));
        func_179217_a(450, "shulker_shell", (new Item()).func_77655_b("shulkerShell").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(452, "iron_nugget", (new Item()).func_77655_b("ironNugget").func_77637_a(CreativeTabs.MATERIALS));
        func_179217_a(453, "knowledge_book", (new ItemKnowledgeBook()).func_77655_b("knowledgeBook"));
        func_179217_a(2256, "record_13", (new ItemRecord("13", SoundEvents.MUSIC_DISC_13)).func_77655_b("record"));
        func_179217_a(2257, "record_cat", (new ItemRecord("cat", SoundEvents.MUSIC_DISC_CAT)).func_77655_b("record"));
        func_179217_a(2258, "record_blocks", (new ItemRecord("blocks", SoundEvents.MUSIC_DISC_BLOCKS)).func_77655_b("record"));
        func_179217_a(2259, "record_chirp", (new ItemRecord("chirp", SoundEvents.MUSIC_DISC_CHIRP)).func_77655_b("record"));
        func_179217_a(2260, "record_far", (new ItemRecord("far", SoundEvents.MUSIC_DISC_FAR)).func_77655_b("record"));
        func_179217_a(2261, "record_mall", (new ItemRecord("mall", SoundEvents.MUSIC_DISC_MALL)).func_77655_b("record"));
        func_179217_a(2262, "record_mellohi", (new ItemRecord("mellohi", SoundEvents.MUSIC_DISC_MELLOHI)).func_77655_b("record"));
        func_179217_a(2263, "record_stal", (new ItemRecord("stal", SoundEvents.MUSIC_DISC_STAL)).func_77655_b("record"));
        func_179217_a(2264, "record_strad", (new ItemRecord("strad", SoundEvents.MUSIC_DISC_STRAD)).func_77655_b("record"));
        func_179217_a(2265, "record_ward", (new ItemRecord("ward", SoundEvents.MUSIC_DISC_WARD)).func_77655_b("record"));
        func_179217_a(2266, "record_11", (new ItemRecord("11", SoundEvents.MUSIC_DISC_11)).func_77655_b("record"));
        func_179217_a(2267, "record_wait", (new ItemRecord("wait", SoundEvents.MUSIC_DISC_WAIT)).func_77655_b("record"));
    }

    private static void func_179216_c(Block p_179216_0_)
    {
        func_179214_a(p_179216_0_, new ItemBlock(p_179216_0_));
    }

    protected static void func_179214_a(Block p_179214_0_, Item p_179214_1_)
    {
        func_179219_a(Block.func_149682_b(p_179214_0_), Block.field_149771_c.getKey(p_179214_0_), p_179214_1_);
        BLOCK_TO_ITEM.put(p_179214_0_, p_179214_1_);
    }

    private static void func_179217_a(int p_179217_0_, String p_179217_1_, Item p_179217_2_)
    {
        func_179219_a(p_179217_0_, new ResourceLocation(p_179217_1_), p_179217_2_);
    }

    private static void func_179219_a(int p_179219_0_, ResourceLocation p_179219_1_, Item p_179219_2_)
    {
        field_150901_e.func_177775_a(p_179219_0_, p_179219_1_, p_179219_2_);
    }

    public ItemStack getDefaultInstance()
    {
        return new ItemStack(this);
    }

    public static enum ToolMaterial
    {
        WOOD(0, 59, 2.0F, 0.0F, 15),
        STONE(1, 131, 4.0F, 1.0F, 5),
        IRON(2, 250, 6.0F, 2.0F, 14),
        DIAMOND(3, 1561, 8.0F, 3.0F, 10),
        GOLD(0, 32, 12.0F, 0.0F, 22);

        private final int harvestLevel;
        private final int maxUses;
        private final float efficiency;
        private final float attackDamage;
        private final int enchantability;

        private ToolMaterial(int p_i1874_3_, int p_i1874_4_, float p_i1874_5_, float p_i1874_6_, int p_i1874_7_)
        {
            this.harvestLevel = p_i1874_3_;
            this.maxUses = p_i1874_4_;
            this.efficiency = p_i1874_5_;
            this.attackDamage = p_i1874_6_;
            this.enchantability = p_i1874_7_;
        }

        public int func_77997_a()
        {
            return this.maxUses;
        }

        public float func_77998_b()
        {
            return this.efficiency;
        }

        public float func_78000_c()
        {
            return this.attackDamage;
        }

        public int func_77996_d()
        {
            return this.harvestLevel;
        }

        public int func_77995_e()
        {
            return this.enchantability;
        }

        public Item func_150995_f()
        {
            if (this == WOOD)
            {
                return Item.getItemFromBlock(Blocks.field_150344_f);
            }
            else if (this == STONE)
            {
                return Item.getItemFromBlock(Blocks.COBBLESTONE);
            }
            else if (this == GOLD)
            {
                return Items.GOLD_INGOT;
            }
            else if (this == IRON)
            {
                return Items.IRON_INGOT;
            }
            else
            {
                return this == DIAMOND ? Items.DIAMOND : null;
            }
        }
    }
}

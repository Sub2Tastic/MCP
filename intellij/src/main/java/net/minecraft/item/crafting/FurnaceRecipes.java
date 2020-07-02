package net.minecraft.item.crafting;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;

public class FurnaceRecipes
{
    private static final FurnaceRecipes field_77606_a = new FurnaceRecipes();
    private final Map<ItemStack, ItemStack> field_77604_b = Maps.<ItemStack, ItemStack>newHashMap();
    private final Map<ItemStack, Float> field_77605_c = Maps.<ItemStack, Float>newHashMap();

    public static FurnaceRecipes func_77602_a()
    {
        return field_77606_a;
    }

    private FurnaceRecipes()
    {
        this.func_151393_a(Blocks.IRON_ORE, new ItemStack(Items.IRON_INGOT), 0.7F);
        this.func_151393_a(Blocks.GOLD_ORE, new ItemStack(Items.GOLD_INGOT), 1.0F);
        this.func_151393_a(Blocks.DIAMOND_ORE, new ItemStack(Items.DIAMOND), 1.0F);
        this.func_151393_a(Blocks.SAND, new ItemStack(Blocks.GLASS), 0.1F);
        this.func_151396_a(Items.PORKCHOP, new ItemStack(Items.COOKED_PORKCHOP), 0.35F);
        this.func_151396_a(Items.BEEF, new ItemStack(Items.COOKED_BEEF), 0.35F);
        this.func_151396_a(Items.CHICKEN, new ItemStack(Items.COOKED_CHICKEN), 0.35F);
        this.func_151396_a(Items.RABBIT, new ItemStack(Items.COOKED_RABBIT), 0.35F);
        this.func_151396_a(Items.MUTTON, new ItemStack(Items.COOKED_MUTTON), 0.35F);
        this.func_151393_a(Blocks.COBBLESTONE, new ItemStack(Blocks.STONE), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150417_aV, 1, BlockStoneBrick.field_176248_b), new ItemStack(Blocks.field_150417_aV, 1, BlockStoneBrick.field_176251_N), 0.1F);
        this.func_151396_a(Items.CLAY_BALL, new ItemStack(Items.BRICK), 0.3F);
        this.func_151393_a(Blocks.CLAY, new ItemStack(Blocks.TERRACOTTA), 0.35F);
        this.func_151393_a(Blocks.CACTUS, new ItemStack(Items.field_151100_aR, 1, EnumDyeColor.GREEN.func_176767_b()), 0.2F);
        this.func_151393_a(Blocks.field_150364_r, new ItemStack(Items.COAL, 1, 1), 0.15F);
        this.func_151393_a(Blocks.field_150363_s, new ItemStack(Items.COAL, 1, 1), 0.15F);
        this.func_151393_a(Blocks.EMERALD_ORE, new ItemStack(Items.EMERALD), 1.0F);
        this.func_151396_a(Items.POTATO, new ItemStack(Items.BAKED_POTATO), 0.35F);
        this.func_151393_a(Blocks.NETHERRACK, new ItemStack(Items.field_151130_bT), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.SPONGE, 1, 1), new ItemStack(Blocks.SPONGE, 1, 0), 0.15F);
        this.func_151396_a(Items.CHORUS_FRUIT, new ItemStack(Items.POPPED_CHORUS_FRUIT), 0.1F);

        for (ItemFishFood.FishType itemfishfood$fishtype : ItemFishFood.FishType.values())
        {
            if (itemfishfood$fishtype.func_150973_i())
            {
                this.func_151394_a(new ItemStack(Items.field_151115_aP, 1, itemfishfood$fishtype.func_150976_a()), new ItemStack(Items.field_179566_aV, 1, itemfishfood$fishtype.func_150976_a()), 0.35F);
            }
        }

        this.func_151393_a(Blocks.COAL_ORE, new ItemStack(Items.COAL), 0.1F);
        this.func_151393_a(Blocks.REDSTONE_ORE, new ItemStack(Items.REDSTONE), 0.7F);
        this.func_151393_a(Blocks.LAPIS_ORE, new ItemStack(Items.field_151100_aR, 1, EnumDyeColor.BLUE.func_176767_b()), 0.2F);
        this.func_151393_a(Blocks.field_150449_bY, new ItemStack(Items.QUARTZ), 0.2F);
        this.func_151396_a(Items.CHAINMAIL_HELMET, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.func_151396_a(Items.CHAINMAIL_CHESTPLATE, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.func_151396_a(Items.CHAINMAIL_LEGGINGS, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.func_151396_a(Items.CHAINMAIL_BOOTS, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.func_151396_a(Items.IRON_PICKAXE, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.func_151396_a(Items.IRON_SHOVEL, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.func_151396_a(Items.IRON_AXE, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.func_151396_a(Items.IRON_HOE, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.func_151396_a(Items.IRON_SWORD, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.func_151396_a(Items.IRON_HELMET, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.func_151396_a(Items.IRON_CHESTPLATE, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.func_151396_a(Items.IRON_LEGGINGS, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.func_151396_a(Items.IRON_BOOTS, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.func_151396_a(Items.IRON_HORSE_ARMOR, new ItemStack(Items.IRON_NUGGET), 0.1F);
        this.func_151396_a(Items.GOLDEN_PICKAXE, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.func_151396_a(Items.GOLDEN_SHOVEL, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.func_151396_a(Items.GOLDEN_AXE, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.func_151396_a(Items.GOLDEN_HOE, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.func_151396_a(Items.GOLDEN_SWORD, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.func_151396_a(Items.GOLDEN_HELMET, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.func_151396_a(Items.GOLDEN_CHESTPLATE, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.func_151396_a(Items.GOLDEN_LEGGINGS, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.func_151396_a(Items.GOLDEN_BOOTS, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.func_151396_a(Items.GOLDEN_HORSE_ARMOR, new ItemStack(Items.GOLD_NUGGET), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.WHITE.func_176765_a()), new ItemStack(Blocks.WHITE_GLAZED_TERRACOTTA), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.ORANGE.func_176765_a()), new ItemStack(Blocks.ORANGE_GLAZED_TERRACOTTA), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.MAGENTA.func_176765_a()), new ItemStack(Blocks.MAGENTA_GLAZED_TERRACOTTA), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.LIGHT_BLUE.func_176765_a()), new ItemStack(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.YELLOW.func_176765_a()), new ItemStack(Blocks.YELLOW_GLAZED_TERRACOTTA), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.LIME.func_176765_a()), new ItemStack(Blocks.LIME_GLAZED_TERRACOTTA), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.PINK.func_176765_a()), new ItemStack(Blocks.PINK_GLAZED_TERRACOTTA), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.GRAY.func_176765_a()), new ItemStack(Blocks.GRAY_GLAZED_TERRACOTTA), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.SILVER.func_176765_a()), new ItemStack(Blocks.field_192435_dJ), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.CYAN.func_176765_a()), new ItemStack(Blocks.CYAN_GLAZED_TERRACOTTA), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.PURPLE.func_176765_a()), new ItemStack(Blocks.PURPLE_GLAZED_TERRACOTTA), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.BLUE.func_176765_a()), new ItemStack(Blocks.BLUE_GLAZED_TERRACOTTA), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.BROWN.func_176765_a()), new ItemStack(Blocks.BROWN_GLAZED_TERRACOTTA), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.GREEN.func_176765_a()), new ItemStack(Blocks.GREEN_GLAZED_TERRACOTTA), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.RED.func_176765_a()), new ItemStack(Blocks.RED_GLAZED_TERRACOTTA), 0.1F);
        this.func_151394_a(new ItemStack(Blocks.field_150406_ce, 1, EnumDyeColor.BLACK.func_176765_a()), new ItemStack(Blocks.BLACK_GLAZED_TERRACOTTA), 0.1F);
    }

    public void func_151393_a(Block p_151393_1_, ItemStack p_151393_2_, float p_151393_3_)
    {
        this.func_151396_a(Item.getItemFromBlock(p_151393_1_), p_151393_2_, p_151393_3_);
    }

    public void func_151396_a(Item p_151396_1_, ItemStack p_151396_2_, float p_151396_3_)
    {
        this.func_151394_a(new ItemStack(p_151396_1_, 1, 32767), p_151396_2_, p_151396_3_);
    }

    public void func_151394_a(ItemStack p_151394_1_, ItemStack p_151394_2_, float p_151394_3_)
    {
        this.field_77604_b.put(p_151394_1_, p_151394_2_);
        this.field_77605_c.put(p_151394_2_, Float.valueOf(p_151394_3_));
    }

    public ItemStack func_151395_a(ItemStack p_151395_1_)
    {
        for (Entry<ItemStack, ItemStack> entry : this.field_77604_b.entrySet())
        {
            if (this.func_151397_a(p_151395_1_, entry.getKey()))
            {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }

    private boolean func_151397_a(ItemStack p_151397_1_, ItemStack p_151397_2_)
    {
        return p_151397_2_.getItem() == p_151397_1_.getItem() && (p_151397_2_.func_77960_j() == 32767 || p_151397_2_.func_77960_j() == p_151397_1_.func_77960_j());
    }

    public Map<ItemStack, ItemStack> func_77599_b()
    {
        return this.field_77604_b;
    }

    public float func_151398_b(ItemStack p_151398_1_)
    {
        for (Entry<ItemStack, Float> entry : this.field_77605_c.entrySet())
        {
            if (this.func_151397_a(p_151398_1_, entry.getKey()))
            {
                return ((Float)entry.getValue()).floatValue();
            }
        }

        return 0.0F;
    }
}

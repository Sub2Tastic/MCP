package net.minecraft.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class StatList
{
    protected static final Map<String, StatBase> field_188093_a = Maps.<String, StatBase>newHashMap();
    public static final List<StatBase> field_75940_b = Lists.<StatBase>newArrayList();
    public static final List<StatBase> field_188094_c = Lists.<StatBase>newArrayList();
    public static final List<StatCrafting> field_188095_d = Lists.<StatCrafting>newArrayList();
    public static final List<StatCrafting> field_188096_e = Lists.<StatCrafting>newArrayList();
    public static final StatBase LEAVE_GAME = (new StatBasic("stat.leaveGame", new TextComponentTranslation("stat.leaveGame", new Object[0]))).func_75966_h().func_75971_g();
    public static final StatBase PLAY_ONE_MINUTE = (new StatBasic("stat.playOneMinute", new TextComponentTranslation("stat.playOneMinute", new Object[0]), StatBase.field_75981_i)).func_75966_h().func_75971_g();
    public static final StatBase TIME_SINCE_DEATH = (new StatBasic("stat.timeSinceDeath", new TextComponentTranslation("stat.timeSinceDeath", new Object[0]), StatBase.field_75981_i)).func_75966_h().func_75971_g();
    public static final StatBase field_188099_i = (new StatBasic("stat.sneakTime", new TextComponentTranslation("stat.sneakTime", new Object[0]), StatBase.field_75981_i)).func_75966_h().func_75971_g();
    public static final StatBase WALK_ONE_CM = (new StatBasic("stat.walkOneCm", new TextComponentTranslation("stat.walkOneCm", new Object[0]), StatBase.field_75979_j)).func_75966_h().func_75971_g();
    public static final StatBase CROUCH_ONE_CM = (new StatBasic("stat.crouchOneCm", new TextComponentTranslation("stat.crouchOneCm", new Object[0]), StatBase.field_75979_j)).func_75966_h().func_75971_g();
    public static final StatBase SPRINT_ONE_CM = (new StatBasic("stat.sprintOneCm", new TextComponentTranslation("stat.sprintOneCm", new Object[0]), StatBase.field_75979_j)).func_75966_h().func_75971_g();
    public static final StatBase SWIM_ONE_CM = (new StatBasic("stat.swimOneCm", new TextComponentTranslation("stat.swimOneCm", new Object[0]), StatBase.field_75979_j)).func_75966_h().func_75971_g();
    public static final StatBase FALL_ONE_CM = (new StatBasic("stat.fallOneCm", new TextComponentTranslation("stat.fallOneCm", new Object[0]), StatBase.field_75979_j)).func_75966_h().func_75971_g();
    public static final StatBase CLIMB_ONE_CM = (new StatBasic("stat.climbOneCm", new TextComponentTranslation("stat.climbOneCm", new Object[0]), StatBase.field_75979_j)).func_75966_h().func_75971_g();
    public static final StatBase FLY_ONE_CM = (new StatBasic("stat.flyOneCm", new TextComponentTranslation("stat.flyOneCm", new Object[0]), StatBase.field_75979_j)).func_75966_h().func_75971_g();
    public static final StatBase field_188105_q = (new StatBasic("stat.diveOneCm", new TextComponentTranslation("stat.diveOneCm", new Object[0]), StatBase.field_75979_j)).func_75966_h().func_75971_g();
    public static final StatBase MINECART_ONE_CM = (new StatBasic("stat.minecartOneCm", new TextComponentTranslation("stat.minecartOneCm", new Object[0]), StatBase.field_75979_j)).func_75966_h().func_75971_g();
    public static final StatBase BOAT_ONE_CM = (new StatBasic("stat.boatOneCm", new TextComponentTranslation("stat.boatOneCm", new Object[0]), StatBase.field_75979_j)).func_75966_h().func_75971_g();
    public static final StatBase PIG_ONE_CM = (new StatBasic("stat.pigOneCm", new TextComponentTranslation("stat.pigOneCm", new Object[0]), StatBase.field_75979_j)).func_75966_h().func_75971_g();
    public static final StatBase HORSE_ONE_CM = (new StatBasic("stat.horseOneCm", new TextComponentTranslation("stat.horseOneCm", new Object[0]), StatBase.field_75979_j)).func_75966_h().func_75971_g();
    public static final StatBase AVIATE_ONE_CM = (new StatBasic("stat.aviateOneCm", new TextComponentTranslation("stat.aviateOneCm", new Object[0]), StatBase.field_75979_j)).func_75966_h().func_75971_g();
    public static final StatBase JUMP = (new StatBasic("stat.jump", new TextComponentTranslation("stat.jump", new Object[0]))).func_75966_h().func_75971_g();
    public static final StatBase DROP = (new StatBasic("stat.drop", new TextComponentTranslation("stat.drop", new Object[0]))).func_75966_h().func_75971_g();
    public static final StatBase DAMAGE_DEALT = (new StatBasic("stat.damageDealt", new TextComponentTranslation("stat.damageDealt", new Object[0]), StatBase.field_111202_k)).func_75971_g();
    public static final StatBase DAMAGE_TAKEN = (new StatBasic("stat.damageTaken", new TextComponentTranslation("stat.damageTaken", new Object[0]), StatBase.field_111202_k)).func_75971_g();
    public static final StatBase DEATHS = (new StatBasic("stat.deaths", new TextComponentTranslation("stat.deaths", new Object[0]))).func_75971_g();
    public static final StatBase MOB_KILLS = (new StatBasic("stat.mobKills", new TextComponentTranslation("stat.mobKills", new Object[0]))).func_75971_g();
    public static final StatBase ANIMALS_BRED = (new StatBasic("stat.animalsBred", new TextComponentTranslation("stat.animalsBred", new Object[0]))).func_75971_g();
    public static final StatBase PLAYER_KILLS = (new StatBasic("stat.playerKills", new TextComponentTranslation("stat.playerKills", new Object[0]))).func_75971_g();
    public static final StatBase FISH_CAUGHT = (new StatBasic("stat.fishCaught", new TextComponentTranslation("stat.fishCaught", new Object[0]))).func_75971_g();
    public static final StatBase TALKED_TO_VILLAGER = (new StatBasic("stat.talkedToVillager", new TextComponentTranslation("stat.talkedToVillager", new Object[0]))).func_75971_g();
    public static final StatBase TRADED_WITH_VILLAGER = (new StatBasic("stat.tradedWithVillager", new TextComponentTranslation("stat.tradedWithVillager", new Object[0]))).func_75971_g();
    public static final StatBase EAT_CAKE_SLICE = (new StatBasic("stat.cakeSlicesEaten", new TextComponentTranslation("stat.cakeSlicesEaten", new Object[0]))).func_75971_g();
    public static final StatBase FILL_CAULDRON = (new StatBasic("stat.cauldronFilled", new TextComponentTranslation("stat.cauldronFilled", new Object[0]))).func_75971_g();
    public static final StatBase USE_CAULDRON = (new StatBasic("stat.cauldronUsed", new TextComponentTranslation("stat.cauldronUsed", new Object[0]))).func_75971_g();
    public static final StatBase CLEAN_ARMOR = (new StatBasic("stat.armorCleaned", new TextComponentTranslation("stat.armorCleaned", new Object[0]))).func_75971_g();
    public static final StatBase CLEAN_BANNER = (new StatBasic("stat.bannerCleaned", new TextComponentTranslation("stat.bannerCleaned", new Object[0]))).func_75971_g();
    public static final StatBase INTERACT_WITH_BREWINGSTAND = (new StatBasic("stat.brewingstandInteraction", new TextComponentTranslation("stat.brewingstandInteraction", new Object[0]))).func_75971_g();
    public static final StatBase INTERACT_WITH_BEACON = (new StatBasic("stat.beaconInteraction", new TextComponentTranslation("stat.beaconInteraction", new Object[0]))).func_75971_g();
    public static final StatBase INSPECT_DROPPER = (new StatBasic("stat.dropperInspected", new TextComponentTranslation("stat.dropperInspected", new Object[0]))).func_75971_g();
    public static final StatBase INSPECT_HOPPER = (new StatBasic("stat.hopperInspected", new TextComponentTranslation("stat.hopperInspected", new Object[0]))).func_75971_g();
    public static final StatBase INSPECT_DISPENSER = (new StatBasic("stat.dispenserInspected", new TextComponentTranslation("stat.dispenserInspected", new Object[0]))).func_75971_g();
    public static final StatBase PLAY_NOTEBLOCK = (new StatBasic("stat.noteblockPlayed", new TextComponentTranslation("stat.noteblockPlayed", new Object[0]))).func_75971_g();
    public static final StatBase TUNE_NOTEBLOCK = (new StatBasic("stat.noteblockTuned", new TextComponentTranslation("stat.noteblockTuned", new Object[0]))).func_75971_g();
    public static final StatBase POT_FLOWER = (new StatBasic("stat.flowerPotted", new TextComponentTranslation("stat.flowerPotted", new Object[0]))).func_75971_g();
    public static final StatBase TRIGGER_TRAPPED_CHEST = (new StatBasic("stat.trappedChestTriggered", new TextComponentTranslation("stat.trappedChestTriggered", new Object[0]))).func_75971_g();
    public static final StatBase OPEN_ENDERCHEST = (new StatBasic("stat.enderchestOpened", new TextComponentTranslation("stat.enderchestOpened", new Object[0]))).func_75971_g();
    public static final StatBase ENCHANT_ITEM = (new StatBasic("stat.itemEnchanted", new TextComponentTranslation("stat.itemEnchanted", new Object[0]))).func_75971_g();
    public static final StatBase PLAY_RECORD = (new StatBasic("stat.recordPlayed", new TextComponentTranslation("stat.recordPlayed", new Object[0]))).func_75971_g();
    public static final StatBase INTERACT_WITH_FURNACE = (new StatBasic("stat.furnaceInteraction", new TextComponentTranslation("stat.furnaceInteraction", new Object[0]))).func_75971_g();
    public static final StatBase INTERACT_WITH_CRAFTING_TABLE = (new StatBasic("stat.craftingTableInteraction", new TextComponentTranslation("stat.workbenchInteraction", new Object[0]))).func_75971_g();
    public static final StatBase OPEN_CHEST = (new StatBasic("stat.chestOpened", new TextComponentTranslation("stat.chestOpened", new Object[0]))).func_75971_g();
    public static final StatBase SLEEP_IN_BED = (new StatBasic("stat.sleepInBed", new TextComponentTranslation("stat.sleepInBed", new Object[0]))).func_75971_g();
    public static final StatBase OPEN_SHULKER_BOX = (new StatBasic("stat.shulkerBoxOpened", new TextComponentTranslation("stat.shulkerBoxOpened", new Object[0]))).func_75971_g();
    private static final StatBase[] BLOCK_MINED = new StatBase[4096];
    private static final StatBase[] ITEM_CRAFTED = new StatBase[32000];
    private static final StatBase[] ITEM_USED = new StatBase[32000];
    private static final StatBase[] field_75930_F = new StatBase[32000];
    private static final StatBase[] field_188067_ai = new StatBase[32000];
    private static final StatBase[] ITEM_DROPPED = new StatBase[32000];

    @Nullable
    public static StatBase func_188055_a(Block p_188055_0_)
    {
        return BLOCK_MINED[Block.func_149682_b(p_188055_0_)];
    }

    @Nullable
    public static StatBase func_188060_a(Item p_188060_0_)
    {
        return ITEM_CRAFTED[Item.getIdFromItem(p_188060_0_)];
    }

    @Nullable
    public static StatBase func_188057_b(Item p_188057_0_)
    {
        return ITEM_USED[Item.getIdFromItem(p_188057_0_)];
    }

    @Nullable
    public static StatBase func_188059_c(Item p_188059_0_)
    {
        return field_75930_F[Item.getIdFromItem(p_188059_0_)];
    }

    @Nullable
    public static StatBase func_188056_d(Item p_188056_0_)
    {
        return field_188067_ai[Item.getIdFromItem(p_188056_0_)];
    }

    @Nullable
    public static StatBase func_188058_e(Item p_188058_0_)
    {
        return ITEM_DROPPED[Item.getIdFromItem(p_188058_0_)];
    }

    public static void func_151178_a()
    {
        func_151181_c();
        func_75925_c();
        func_151179_e();
        func_75918_d();
        func_188054_f();
    }

    private static void func_75918_d()
    {
        Set<Item> set = Sets.<Item>newHashSet();

        for (IRecipe irecipe : CraftingManager.field_193380_a)
        {
            ItemStack itemstack = irecipe.getRecipeOutput();

            if (!itemstack.isEmpty())
            {
                set.add(irecipe.getRecipeOutput().getItem());
            }
        }

        for (ItemStack itemstack1 : FurnaceRecipes.func_77602_a().func_77599_b().values())
        {
            set.add(itemstack1.getItem());
        }

        for (Item item : set)
        {
            if (item != null)
            {
                int i = Item.getIdFromItem(item);
                String s = func_180204_a(item);

                if (s != null)
                {
                    ITEM_CRAFTED[i] = (new StatCrafting("stat.craftItem.", s, new TextComponentTranslation("stat.craftItem", new Object[] {(new ItemStack(item)).getTextComponent()}), item)).func_75971_g();
                }
            }
        }

        func_75924_a(ITEM_CRAFTED);
    }

    private static void func_151181_c()
    {
        for (Block block : Block.field_149771_c)
        {
            Item item = Item.getItemFromBlock(block);

            if (item != Items.AIR)
            {
                int i = Block.func_149682_b(block);
                String s = func_180204_a(item);

                if (s != null && block.func_149652_G())
                {
                    BLOCK_MINED[i] = (new StatCrafting("stat.mineBlock.", s, new TextComponentTranslation("stat.mineBlock", new Object[] {(new ItemStack(block)).getTextComponent()}), item)).func_75971_g();
                    field_188096_e.add((StatCrafting)BLOCK_MINED[i]);
                }
            }
        }

        func_75924_a(BLOCK_MINED);
    }

    private static void func_75925_c()
    {
        for (Item item : Item.field_150901_e)
        {
            if (item != null)
            {
                int i = Item.getIdFromItem(item);
                String s = func_180204_a(item);

                if (s != null)
                {
                    ITEM_USED[i] = (new StatCrafting("stat.useItem.", s, new TextComponentTranslation("stat.useItem", new Object[] {(new ItemStack(item)).getTextComponent()}), item)).func_75971_g();

                    if (!(item instanceof ItemBlock))
                    {
                        field_188095_d.add((StatCrafting)ITEM_USED[i]);
                    }
                }
            }
        }

        func_75924_a(ITEM_USED);
    }

    private static void func_151179_e()
    {
        for (Item item : Item.field_150901_e)
        {
            if (item != null)
            {
                int i = Item.getIdFromItem(item);
                String s = func_180204_a(item);

                if (s != null && item.isDamageable())
                {
                    field_75930_F[i] = (new StatCrafting("stat.breakItem.", s, new TextComponentTranslation("stat.breakItem", new Object[] {(new ItemStack(item)).getTextComponent()}), item)).func_75971_g();
                }
            }
        }

        func_75924_a(field_75930_F);
    }

    private static void func_188054_f()
    {
        for (Item item : Item.field_150901_e)
        {
            if (item != null)
            {
                int i = Item.getIdFromItem(item);
                String s = func_180204_a(item);

                if (s != null)
                {
                    field_188067_ai[i] = (new StatCrafting("stat.pickup.", s, new TextComponentTranslation("stat.pickup", new Object[] {(new ItemStack(item)).getTextComponent()}), item)).func_75971_g();
                    ITEM_DROPPED[i] = (new StatCrafting("stat.drop.", s, new TextComponentTranslation("stat.drop", new Object[] {(new ItemStack(item)).getTextComponent()}), item)).func_75971_g();
                }
            }
        }

        func_75924_a(field_75930_F);
    }

    private static String func_180204_a(Item p_180204_0_)
    {
        ResourceLocation resourcelocation = Item.field_150901_e.getKey(p_180204_0_);
        return resourcelocation != null ? resourcelocation.toString().replace(':', '.') : null;
    }

    private static void func_75924_a(StatBase[] p_75924_0_)
    {
        func_151180_a(p_75924_0_, Blocks.WATER, Blocks.field_150358_i);
        func_151180_a(p_75924_0_, Blocks.LAVA, Blocks.field_150356_k);
        func_151180_a(p_75924_0_, Blocks.field_150428_aP, Blocks.PUMPKIN);
        func_151180_a(p_75924_0_, Blocks.field_150470_am, Blocks.FURNACE);
        func_151180_a(p_75924_0_, Blocks.field_150439_ay, Blocks.REDSTONE_ORE);
        func_151180_a(p_75924_0_, Blocks.field_150416_aS, Blocks.field_150413_aR);
        func_151180_a(p_75924_0_, Blocks.field_150455_bV, Blocks.field_150441_bU);
        func_151180_a(p_75924_0_, Blocks.REDSTONE_TORCH, Blocks.field_150437_az);
        func_151180_a(p_75924_0_, Blocks.field_150374_bv, Blocks.REDSTONE_LAMP);
        func_151180_a(p_75924_0_, Blocks.field_150334_T, Blocks.STONE_SLAB);
        func_151180_a(p_75924_0_, Blocks.field_150373_bw, Blocks.field_150376_bx);
        func_151180_a(p_75924_0_, Blocks.field_180388_cO, Blocks.field_180389_cP);
        func_151180_a(p_75924_0_, Blocks.GRASS, Blocks.DIRT);
        func_151180_a(p_75924_0_, Blocks.FARMLAND, Blocks.DIRT);
    }

    private static void func_151180_a(StatBase[] p_151180_0_, Block p_151180_1_, Block p_151180_2_)
    {
        int i = Block.func_149682_b(p_151180_1_);
        int j = Block.func_149682_b(p_151180_2_);

        if (p_151180_0_[i] != null && p_151180_0_[j] == null)
        {
            p_151180_0_[j] = p_151180_0_[i];
        }
        else
        {
            field_75940_b.remove(p_151180_0_[i]);
            field_188096_e.remove(p_151180_0_[i]);
            field_188094_c.remove(p_151180_0_[i]);
            p_151180_0_[i] = p_151180_0_[j];
        }
    }

    public static StatBase func_151182_a(EntityList.EntityEggInfo p_151182_0_)
    {
        String s = EntityList.func_191302_a(p_151182_0_.field_75613_a);
        return s == null ? null : (new StatBase("stat.killEntity." + s, new TextComponentTranslation("stat.entityKill", new Object[] {new TextComponentTranslation("entity." + s + ".name", new Object[0])}))).func_75971_g();
    }

    public static StatBase func_151176_b(EntityList.EntityEggInfo p_151176_0_)
    {
        String s = EntityList.func_191302_a(p_151176_0_.field_75613_a);
        return s == null ? null : (new StatBase("stat.entityKilledBy." + s, new TextComponentTranslation("stat.entityKilledBy", new Object[] {new TextComponentTranslation("entity." + s + ".name", new Object[0])}))).func_75971_g();
    }

    @Nullable
    public static StatBase func_151177_a(String p_151177_0_)
    {
        return field_188093_a.get(p_151177_0_);
    }
}

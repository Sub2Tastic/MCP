package net.minecraft.entity.passive;

import java.util.Locale;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityVillager extends EntityAgeable implements INpc, IMerchant
{
    private static final Logger field_190674_bx = LogManager.getLogger();
    private static final DataParameter<Integer> field_184752_bw = EntityDataManager.<Integer>createKey(EntityVillager.class, DataSerializers.VARINT);
    private int field_70955_e;
    private boolean field_70952_f;
    private boolean field_70953_g;
    Village field_70954_d;
    @Nullable
    private EntityPlayer field_70962_h;
    @Nullable
    private MerchantRecipeList field_70963_i;
    private int timeUntilReset;
    private boolean field_70959_by;
    private boolean field_175565_bs;
    private int field_70956_bz;
    private String field_82189_bL;
    private int field_175563_bv;
    private int field_175562_bw;
    private boolean field_82190_bM;
    private boolean field_175564_by;
    private final InventoryBasic field_175560_bz;
    private static final EntityVillager.ITradeList[][][][] field_175561_bA = new EntityVillager.ITradeList[][][][] {{{{new EntityVillager.EmeraldForItems(Items.WHEAT, new EntityVillager.PriceInfo(18, 22)), new EntityVillager.EmeraldForItems(Items.POTATO, new EntityVillager.PriceInfo(15, 19)), new EntityVillager.EmeraldForItems(Items.CARROT, new EntityVillager.PriceInfo(15, 19)), new EntityVillager.ListItemForEmeralds(Items.BREAD, new EntityVillager.PriceInfo(-4, -2))}, {new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.PUMPKIN), new EntityVillager.PriceInfo(8, 13)), new EntityVillager.ListItemForEmeralds(Items.PUMPKIN_PIE, new EntityVillager.PriceInfo(-3, -2))}, {new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.MELON), new EntityVillager.PriceInfo(7, 12)), new EntityVillager.ListItemForEmeralds(Items.APPLE, new EntityVillager.PriceInfo(-7, -5))}, {new EntityVillager.ListItemForEmeralds(Items.COOKIE, new EntityVillager.PriceInfo(-10, -6)), new EntityVillager.ListItemForEmeralds(Items.field_151105_aU, new EntityVillager.PriceInfo(1, 1))}}, {{new EntityVillager.EmeraldForItems(Items.STRING, new EntityVillager.PriceInfo(15, 20)), new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ItemAndEmeraldToItem(Items.field_151115_aP, new EntityVillager.PriceInfo(6, 6), Items.field_179566_aV, new EntityVillager.PriceInfo(6, 6))}, {new EntityVillager.ListEnchantedItemForEmeralds(Items.FISHING_ROD, new EntityVillager.PriceInfo(7, 8))}}, {{new EntityVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.field_150325_L), new EntityVillager.PriceInfo(16, 22)), new EntityVillager.ListItemForEmeralds(Items.SHEARS, new EntityVillager.PriceInfo(3, 4))}, {new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L)), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 1), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 2), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 3), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 4), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 5), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 6), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 7), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 8), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 9), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 10), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 11), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 12), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 13), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 14), new EntityVillager.PriceInfo(1, 2)), new EntityVillager.ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.field_150325_L), 1, 15), new EntityVillager.PriceInfo(1, 2))}}, {{new EntityVillager.EmeraldForItems(Items.STRING, new EntityVillager.PriceInfo(15, 20)), new EntityVillager.ListItemForEmeralds(Items.ARROW, new EntityVillager.PriceInfo(-12, -8))}, {new EntityVillager.ListItemForEmeralds(Items.BOW, new EntityVillager.PriceInfo(2, 3)), new EntityVillager.ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.GRAVEL), new EntityVillager.PriceInfo(10, 10), Items.FLINT, new EntityVillager.PriceInfo(6, 10))}}}, {{{new EntityVillager.EmeraldForItems(Items.PAPER, new EntityVillager.PriceInfo(24, 36)), new EntityVillager.ListEnchantedBookForEmeralds()}, {new EntityVillager.EmeraldForItems(Items.BOOK, new EntityVillager.PriceInfo(8, 10)), new EntityVillager.ListItemForEmeralds(Items.COMPASS, new EntityVillager.PriceInfo(10, 12)), new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.BOOKSHELF), new EntityVillager.PriceInfo(3, 4))}, {new EntityVillager.EmeraldForItems(Items.WRITTEN_BOOK, new EntityVillager.PriceInfo(2, 2)), new EntityVillager.ListItemForEmeralds(Items.CLOCK, new EntityVillager.PriceInfo(10, 12)), new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLASS), new EntityVillager.PriceInfo(-5, -3))}, {new EntityVillager.ListEnchantedBookForEmeralds()}, {new EntityVillager.ListEnchantedBookForEmeralds()}, {new EntityVillager.ListItemForEmeralds(Items.NAME_TAG, new EntityVillager.PriceInfo(20, 22))}}, {{new EntityVillager.EmeraldForItems(Items.PAPER, new EntityVillager.PriceInfo(24, 36))}, {new EntityVillager.EmeraldForItems(Items.COMPASS, new EntityVillager.PriceInfo(1, 1))}, {new EntityVillager.ListItemForEmeralds(Items.MAP, new EntityVillager.PriceInfo(7, 11))}, {new EntityVillager.TreasureMapForEmeralds(new EntityVillager.PriceInfo(12, 20), "Monument", MapDecoration.Type.MONUMENT), new EntityVillager.TreasureMapForEmeralds(new EntityVillager.PriceInfo(16, 28), "Mansion", MapDecoration.Type.MANSION)}}}, {{{new EntityVillager.EmeraldForItems(Items.ROTTEN_FLESH, new EntityVillager.PriceInfo(36, 40)), new EntityVillager.EmeraldForItems(Items.GOLD_INGOT, new EntityVillager.PriceInfo(8, 10))}, {new EntityVillager.ListItemForEmeralds(Items.REDSTONE, new EntityVillager.PriceInfo(-4, -1)), new EntityVillager.ListItemForEmeralds(new ItemStack(Items.field_151100_aR, 1, EnumDyeColor.BLUE.func_176767_b()), new EntityVillager.PriceInfo(-2, -1))}, {new EntityVillager.ListItemForEmeralds(Items.ENDER_PEARL, new EntityVillager.PriceInfo(4, 7)), new EntityVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLOWSTONE), new EntityVillager.PriceInfo(-3, -1))}, {new EntityVillager.ListItemForEmeralds(Items.EXPERIENCE_BOTTLE, new EntityVillager.PriceInfo(3, 11))}}}, {{{new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ListItemForEmeralds(Items.IRON_HELMET, new EntityVillager.PriceInfo(4, 6))}, {new EntityVillager.EmeraldForItems(Items.IRON_INGOT, new EntityVillager.PriceInfo(7, 9)), new EntityVillager.ListItemForEmeralds(Items.IRON_CHESTPLATE, new EntityVillager.PriceInfo(10, 14))}, {new EntityVillager.EmeraldForItems(Items.DIAMOND, new EntityVillager.PriceInfo(3, 4)), new EntityVillager.ListEnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, new EntityVillager.PriceInfo(16, 19))}, {new EntityVillager.ListItemForEmeralds(Items.CHAINMAIL_BOOTS, new EntityVillager.PriceInfo(5, 7)), new EntityVillager.ListItemForEmeralds(Items.CHAINMAIL_LEGGINGS, new EntityVillager.PriceInfo(9, 11)), new EntityVillager.ListItemForEmeralds(Items.CHAINMAIL_HELMET, new EntityVillager.PriceInfo(5, 7)), new EntityVillager.ListItemForEmeralds(Items.CHAINMAIL_CHESTPLATE, new EntityVillager.PriceInfo(11, 15))}}, {{new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ListItemForEmeralds(Items.IRON_AXE, new EntityVillager.PriceInfo(6, 8))}, {new EntityVillager.EmeraldForItems(Items.IRON_INGOT, new EntityVillager.PriceInfo(7, 9)), new EntityVillager.ListEnchantedItemForEmeralds(Items.IRON_SWORD, new EntityVillager.PriceInfo(9, 10))}, {new EntityVillager.EmeraldForItems(Items.DIAMOND, new EntityVillager.PriceInfo(3, 4)), new EntityVillager.ListEnchantedItemForEmeralds(Items.DIAMOND_SWORD, new EntityVillager.PriceInfo(12, 15)), new EntityVillager.ListEnchantedItemForEmeralds(Items.DIAMOND_AXE, new EntityVillager.PriceInfo(9, 12))}}, {{new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ListEnchantedItemForEmeralds(Items.IRON_SHOVEL, new EntityVillager.PriceInfo(5, 7))}, {new EntityVillager.EmeraldForItems(Items.IRON_INGOT, new EntityVillager.PriceInfo(7, 9)), new EntityVillager.ListEnchantedItemForEmeralds(Items.IRON_PICKAXE, new EntityVillager.PriceInfo(9, 11))}, {new EntityVillager.EmeraldForItems(Items.DIAMOND, new EntityVillager.PriceInfo(3, 4)), new EntityVillager.ListEnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, new EntityVillager.PriceInfo(12, 15))}}}, {{{new EntityVillager.EmeraldForItems(Items.PORKCHOP, new EntityVillager.PriceInfo(14, 18)), new EntityVillager.EmeraldForItems(Items.CHICKEN, new EntityVillager.PriceInfo(14, 18))}, {new EntityVillager.EmeraldForItems(Items.COAL, new EntityVillager.PriceInfo(16, 24)), new EntityVillager.ListItemForEmeralds(Items.COOKED_PORKCHOP, new EntityVillager.PriceInfo(-7, -5)), new EntityVillager.ListItemForEmeralds(Items.COOKED_CHICKEN, new EntityVillager.PriceInfo(-8, -6))}}, {{new EntityVillager.EmeraldForItems(Items.LEATHER, new EntityVillager.PriceInfo(9, 12)), new EntityVillager.ListItemForEmeralds(Items.LEATHER_LEGGINGS, new EntityVillager.PriceInfo(2, 4))}, {new EntityVillager.ListEnchantedItemForEmeralds(Items.LEATHER_CHESTPLATE, new EntityVillager.PriceInfo(7, 12))}, {new EntityVillager.ListItemForEmeralds(Items.SADDLE, new EntityVillager.PriceInfo(8, 10))}}}, {new EntityVillager.ITradeList[0][]}};

    public EntityVillager(World p_i1747_1_)
    {
        this(p_i1747_1_, 0);
    }

    public EntityVillager(World p_i1748_1_, int p_i1748_2_)
    {
        super(p_i1748_1_);
        this.field_175560_bz = new InventoryBasic("Items", false, 8);
        this.func_70938_b(p_i1748_2_);
        this.func_70105_a(0.6F, 1.95F);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(true);
    }

    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new EntityAISwimming(this));
        this.goalSelector.addGoal(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        this.goalSelector.addGoal(1, new EntityAIAvoidEntity(this, EntityEvoker.class, 12.0F, 0.8D, 0.8D));
        this.goalSelector.addGoal(1, new EntityAIAvoidEntity(this, EntityVindicator.class, 8.0F, 0.8D, 0.8D));
        this.goalSelector.addGoal(1, new EntityAIAvoidEntity(this, EntityVex.class, 8.0F, 0.6D, 0.6D));
        this.goalSelector.addGoal(1, new EntityAITradePlayer(this));
        this.goalSelector.addGoal(1, new EntityAILookAtTradePlayer(this));
        this.goalSelector.addGoal(2, new EntityAIMoveIndoors(this));
        this.goalSelector.addGoal(3, new EntityAIRestrictOpenDoor(this));
        this.goalSelector.addGoal(4, new EntityAIOpenDoor(this, true));
        this.goalSelector.addGoal(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
        this.goalSelector.addGoal(6, new EntityAIVillagerMate(this));
        this.goalSelector.addGoal(7, new EntityAIFollowGolem(this));
        this.goalSelector.addGoal(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(9, new EntityAIVillagerInteract(this));
        this.goalSelector.addGoal(9, new EntityAIWanderAvoidWater(this, 0.6D));
        this.goalSelector.addGoal(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }

    private void func_175552_ct()
    {
        if (!this.field_175564_by)
        {
            this.field_175564_by = true;

            if (this.isChild())
            {
                this.goalSelector.addGoal(8, new EntityAIPlay(this, 0.32D));
            }
            else if (this.func_70946_n() == 0)
            {
                this.goalSelector.addGoal(6, new EntityAIHarvestFarmland(this, 0.6D));
            }
        }
    }

    /**
     * This is called when Entity's growing age timer reaches 0 (negative values are considered as a child, positive as
     * an adult)
     */
    protected void onGrowingAdult()
    {
        if (this.func_70946_n() == 0)
        {
            this.goalSelector.addGoal(8, new EntityAIHarvestFarmland(this, 0.6D));
        }

        super.onGrowingAdult();
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    protected void updateAITasks()
    {
        if (--this.field_70955_e <= 0)
        {
            BlockPos blockpos = new BlockPos(this);
            this.world.func_175714_ae().func_176060_a(blockpos);
            this.field_70955_e = 70 + this.rand.nextInt(50);
            this.field_70954_d = this.world.func_175714_ae().func_176056_a(blockpos, 32);

            if (this.field_70954_d == null)
            {
                this.func_110177_bN();
            }
            else
            {
                BlockPos blockpos1 = this.field_70954_d.func_180608_a();
                this.func_175449_a(blockpos1, this.field_70954_d.func_75568_b());

                if (this.field_82190_bM)
                {
                    this.field_82190_bM = false;
                    this.field_70954_d.func_82683_b(5);
                }
            }
        }

        if (!this.func_70940_q() && this.timeUntilReset > 0)
        {
            --this.timeUntilReset;

            if (this.timeUntilReset <= 0)
            {
                if (this.field_70959_by)
                {
                    for (MerchantRecipe merchantrecipe : this.field_70963_i)
                    {
                        if (merchantrecipe.func_82784_g())
                        {
                            merchantrecipe.func_82783_a(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                        }
                    }

                    this.levelUp();
                    this.field_70959_by = false;

                    if (this.field_70954_d != null && this.field_82189_bL != null)
                    {
                        this.world.setEntityState(this, (byte)14);
                        this.field_70954_d.func_82688_a(this.field_82189_bL, 1);
                    }
                }

                this.func_70690_d(new PotionEffect(MobEffects.REGENERATION, 200, 0));
            }
        }

        super.updateAITasks();
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        boolean flag = itemstack.getItem() == Items.NAME_TAG;

        if (flag)
        {
            itemstack.interactWithEntity(player, this, hand);
            return true;
        }
        else if (!this.func_190669_a(itemstack, this.getClass()) && this.isAlive() && !this.func_70940_q() && !this.isChild())
        {
            if (this.field_70963_i == null)
            {
                this.levelUp();
            }

            if (hand == EnumHand.MAIN_HAND)
            {
                player.addStat(StatList.TALKED_TO_VILLAGER);
            }

            if (!this.world.isRemote && !this.field_70963_i.isEmpty())
            {
                this.setCustomer(player);
                player.func_180472_a(this);
            }
            else if (this.field_70963_i.isEmpty())
            {
                return super.processInteract(player, hand);
            }

            return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(field_184752_bw, Integer.valueOf(0));
    }

    public static void func_189785_b(DataFixer p_189785_0_)
    {
        EntityLiving.func_189752_a(p_189785_0_, EntityVillager.class);
        p_189785_0_.func_188258_a(FixTypes.ENTITY, new ItemStackDataLists(EntityVillager.class, new String[] {"Inventory"}));
        p_189785_0_.func_188258_a(FixTypes.ENTITY, new IDataWalker()
        {
            public NBTTagCompound func_188266_a(IDataFixer p_188266_1_, NBTTagCompound p_188266_2_, int p_188266_3_)
            {
                if (EntityList.func_191306_a(EntityVillager.class).equals(new ResourceLocation(p_188266_2_.getString("id"))) && p_188266_2_.contains("Offers", 10))
                {
                    NBTTagCompound nbttagcompound = p_188266_2_.getCompound("Offers");

                    if (nbttagcompound.contains("Recipes", 9))
                    {
                        NBTTagList nbttaglist = nbttagcompound.getList("Recipes", 10);

                        for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                        {
                            NBTTagCompound nbttagcompound1 = nbttaglist.getCompound(i);
                            DataFixesManager.func_188277_a(p_188266_1_, nbttagcompound1, p_188266_3_, "buy");
                            DataFixesManager.func_188277_a(p_188266_1_, nbttagcompound1, p_188266_3_, "buyB");
                            DataFixesManager.func_188277_a(p_188266_1_, nbttagcompound1, p_188266_3_, "sell");
                            nbttaglist.func_150304_a(i, nbttagcompound1);
                        }
                    }
                }

                return p_188266_2_;
            }
        });
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.putInt("Profession", this.func_70946_n());
        p_70014_1_.putInt("Riches", this.field_70956_bz);
        p_70014_1_.putInt("Career", this.field_175563_bv);
        p_70014_1_.putInt("CareerLevel", this.field_175562_bw);
        p_70014_1_.putBoolean("Willing", this.field_175565_bs);

        if (this.field_70963_i != null)
        {
            p_70014_1_.func_74782_a("Offers", this.field_70963_i.func_77202_a());
        }

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.field_175560_bz.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.field_175560_bz.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                nbttaglist.func_74742_a(itemstack.write(new NBTTagCompound()));
            }
        }

        p_70014_1_.func_74782_a("Inventory", nbttaglist);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        this.func_70938_b(compound.getInt("Profession"));
        this.field_70956_bz = compound.getInt("Riches");
        this.field_175563_bv = compound.getInt("Career");
        this.field_175562_bw = compound.getInt("CareerLevel");
        this.field_175565_bs = compound.getBoolean("Willing");

        if (compound.contains("Offers", 10))
        {
            NBTTagCompound nbttagcompound = compound.getCompound("Offers");
            this.field_70963_i = new MerchantRecipeList(nbttagcompound);
        }

        NBTTagList nbttaglist = compound.getList("Inventory", 10);

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
        {
            ItemStack itemstack = new ItemStack(nbttaglist.getCompound(i));

            if (!itemstack.isEmpty())
            {
                this.field_175560_bz.addItem(itemstack);
            }
        }

        this.setCanPickUpLoot(true);
        this.func_175552_ct();
    }

    protected boolean func_70692_ba()
    {
        return false;
    }

    protected SoundEvent getAmbientSound()
    {
        return this.func_70940_q() ? SoundEvents.ENTITY_VILLAGER_TRADE : SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_191184_at;
    }

    public void func_70938_b(int p_70938_1_)
    {
        this.dataManager.set(field_184752_bw, Integer.valueOf(p_70938_1_));
    }

    public int func_70946_n()
    {
        return Math.max(((Integer)this.dataManager.get(field_184752_bw)).intValue() % 6, 0);
    }

    public boolean func_70941_o()
    {
        return this.field_70952_f;
    }

    public void func_70947_e(boolean p_70947_1_)
    {
        this.field_70952_f = p_70947_1_;
    }

    public void func_70939_f(boolean p_70939_1_)
    {
        this.field_70953_g = p_70939_1_;
    }

    public boolean func_70945_p()
    {
        return this.field_70953_g;
    }

    /**
     * Hint to AI tasks that we were attacked by the passed EntityLivingBase and should retaliate. Is not guaranteed to
     * change our actual active target (for example if we are currently busy attacking someone else)
     */
    public void setRevengeTarget(@Nullable EntityLivingBase livingBase)
    {
        super.setRevengeTarget(livingBase);

        if (this.field_70954_d != null && livingBase != null)
        {
            this.field_70954_d.func_75575_a(livingBase);

            if (livingBase instanceof EntityPlayer)
            {
                int i = -1;

                if (this.isChild())
                {
                    i = -3;
                }

                this.field_70954_d.func_82688_a(livingBase.func_70005_c_(), i);

                if (this.isAlive())
                {
                    this.world.setEntityState(this, (byte)13);
                }
            }
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause)
    {
        if (this.field_70954_d != null)
        {
            Entity entity = cause.getTrueSource();

            if (entity != null)
            {
                if (entity instanceof EntityPlayer)
                {
                    this.field_70954_d.func_82688_a(entity.func_70005_c_(), -2);
                }
                else if (entity instanceof IMob)
                {
                    this.field_70954_d.func_82692_h();
                }
            }
            else
            {
                EntityPlayer entityplayer = this.world.func_72890_a(this, 16.0D);

                if (entityplayer != null)
                {
                    this.field_70954_d.func_82692_h();
                }
            }
        }

        super.onDeath(cause);
    }

    public void setCustomer(@Nullable EntityPlayer player)
    {
        this.field_70962_h = player;
    }

    @Nullable
    public EntityPlayer getCustomer()
    {
        return this.field_70962_h;
    }

    public boolean func_70940_q()
    {
        return this.field_70962_h != null;
    }

    public boolean func_175550_n(boolean p_175550_1_)
    {
        if (!this.field_175565_bs && p_175550_1_ && this.func_175553_cp())
        {
            boolean flag = false;

            for (int i = 0; i < this.field_175560_bz.getSizeInventory(); ++i)
            {
                ItemStack itemstack = this.field_175560_bz.getStackInSlot(i);

                if (!itemstack.isEmpty())
                {
                    if (itemstack.getItem() == Items.BREAD && itemstack.getCount() >= 3)
                    {
                        flag = true;
                        this.field_175560_bz.decrStackSize(i, 3);
                    }
                    else if ((itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.CARROT) && itemstack.getCount() >= 12)
                    {
                        flag = true;
                        this.field_175560_bz.decrStackSize(i, 12);
                    }
                }

                if (flag)
                {
                    this.world.setEntityState(this, (byte)18);
                    this.field_175565_bs = true;
                    break;
                }
            }
        }

        return this.field_175565_bs;
    }

    public void func_175549_o(boolean p_175549_1_)
    {
        this.field_175565_bs = p_175549_1_;
    }

    public void func_70933_a(MerchantRecipe p_70933_1_)
    {
        p_70933_1_.func_77399_f();
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
        int i = 3 + this.rand.nextInt(4);

        if (p_70933_1_.func_180321_e() == 1 || this.rand.nextInt(5) == 0)
        {
            this.timeUntilReset = 40;
            this.field_70959_by = true;
            this.field_175565_bs = true;

            if (this.field_70962_h != null)
            {
                this.field_82189_bL = this.field_70962_h.func_70005_c_();
            }
            else
            {
                this.field_82189_bL = null;
            }

            i += 5;
        }

        if (p_70933_1_.func_77394_a().getItem() == Items.EMERALD)
        {
            this.field_70956_bz += p_70933_1_.func_77394_a().getCount();
        }

        if (p_70933_1_.func_180322_j())
        {
            this.world.addEntity0(new EntityXPOrb(this.world, this.posX, this.posY + 0.5D, this.posZ, i));
        }

        if (this.field_70962_h instanceof EntityPlayerMP)
        {
            CriteriaTriggers.VILLAGER_TRADE.func_192234_a((EntityPlayerMP)this.field_70962_h, this, p_70933_1_.func_77397_d());
        }
    }

    /**
     * Notifies the merchant of a possible merchantrecipe being fulfilled or not. Usually, this is just a sound byte
     * being played depending if the suggested itemstack is not null.
     */
    public void verifySellingItem(ItemStack stack)
    {
        if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20)
        {
            this.livingSoundTime = -this.getTalkInterval();
            this.playSound(stack.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Nullable
    public MerchantRecipeList func_70934_b(EntityPlayer p_70934_1_)
    {
        if (this.field_70963_i == null)
        {
            this.levelUp();
        }

        return this.field_70963_i;
    }

    private void levelUp()
    {
        EntityVillager.ITradeList[][][] aentityvillager$itradelist = field_175561_bA[this.func_70946_n()];

        if (this.field_175563_bv != 0 && this.field_175562_bw != 0)
        {
            ++this.field_175562_bw;
        }
        else
        {
            this.field_175563_bv = this.rand.nextInt(aentityvillager$itradelist.length) + 1;
            this.field_175562_bw = 1;
        }

        if (this.field_70963_i == null)
        {
            this.field_70963_i = new MerchantRecipeList();
        }

        int i = this.field_175563_bv - 1;
        int j = this.field_175562_bw - 1;

        if (i >= 0 && i < aentityvillager$itradelist.length)
        {
            EntityVillager.ITradeList[][] aentityvillager$itradelist1 = aentityvillager$itradelist[i];

            if (j >= 0 && j < aentityvillager$itradelist1.length)
            {
                EntityVillager.ITradeList[] aentityvillager$itradelist2 = aentityvillager$itradelist1[j];

                for (EntityVillager.ITradeList entityvillager$itradelist : aentityvillager$itradelist2)
                {
                    entityvillager$itradelist.func_190888_a(this, this.field_70963_i, this.rand);
                }
            }
        }
    }

    public void func_70930_a(@Nullable MerchantRecipeList p_70930_1_)
    {
    }

    public World getWorld()
    {
        return this.world;
    }

    public BlockPos func_190671_u_()
    {
        return new BlockPos(this);
    }

    public ITextComponent getDisplayName()
    {
        Team team = this.getTeam();
        String s = this.func_95999_t();

        if (s != null && !s.isEmpty())
        {
            TextComponentString textcomponentstring = new TextComponentString(ScorePlayerTeam.func_96667_a(team, s));
            textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
            textcomponentstring.getStyle().setInsertion(this.getCachedUniqueIdString());
            return textcomponentstring;
        }
        else
        {
            if (this.field_70963_i == null)
            {
                this.levelUp();
            }

            String s1 = null;

            switch (this.func_70946_n())
            {
                case 0:
                    if (this.field_175563_bv == 1)
                    {
                        s1 = "farmer";
                    }
                    else if (this.field_175563_bv == 2)
                    {
                        s1 = "fisherman";
                    }
                    else if (this.field_175563_bv == 3)
                    {
                        s1 = "shepherd";
                    }
                    else if (this.field_175563_bv == 4)
                    {
                        s1 = "fletcher";
                    }

                    break;

                case 1:
                    if (this.field_175563_bv == 1)
                    {
                        s1 = "librarian";
                    }
                    else if (this.field_175563_bv == 2)
                    {
                        s1 = "cartographer";
                    }

                    break;

                case 2:
                    s1 = "cleric";
                    break;

                case 3:
                    if (this.field_175563_bv == 1)
                    {
                        s1 = "armor";
                    }
                    else if (this.field_175563_bv == 2)
                    {
                        s1 = "weapon";
                    }
                    else if (this.field_175563_bv == 3)
                    {
                        s1 = "tool";
                    }

                    break;

                case 4:
                    if (this.field_175563_bv == 1)
                    {
                        s1 = "butcher";
                    }
                    else if (this.field_175563_bv == 2)
                    {
                        s1 = "leather";
                    }

                    break;

                case 5:
                    s1 = "nitwit";
            }

            if (s1 != null)
            {
                ITextComponent itextcomponent = new TextComponentTranslation("entity.Villager." + s1, new Object[0]);
                itextcomponent.getStyle().setHoverEvent(this.getHoverEvent());
                itextcomponent.getStyle().setInsertion(this.getCachedUniqueIdString());

                if (team != null)
                {
                    itextcomponent.getStyle().setColor(team.getColor());
                }

                return itextcomponent;
            }
            else
            {
                return super.getDisplayName();
            }
        }
    }

    public float getEyeHeight()
    {
        return this.isChild() ? 0.81F : 1.62F;
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    public void handleStatusUpdate(byte id)
    {
        if (id == 12)
        {
            this.func_180489_a(EnumParticleTypes.HEART);
        }
        else if (id == 13)
        {
            this.func_180489_a(EnumParticleTypes.VILLAGER_ANGRY);
        }
        else if (id == 14)
        {
            this.func_180489_a(EnumParticleTypes.VILLAGER_HAPPY);
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    private void func_180489_a(EnumParticleTypes p_180489_1_)
    {
        for (int i = 0; i < 5; ++i)
        {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.world.func_175688_a(p_180489_1_, this.posX + (double)(this.rand.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N, this.posY + 1.0D + (double)(this.rand.nextFloat() * this.field_70131_O), this.posZ + (double)(this.rand.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N, d0, d1, d2);
        }
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, @Nullable IEntityLivingData p_180482_2_)
    {
        return this.func_190672_a(p_180482_1_, p_180482_2_, true);
    }

    public IEntityLivingData func_190672_a(DifficultyInstance p_190672_1_, @Nullable IEntityLivingData p_190672_2_, boolean p_190672_3_)
    {
        p_190672_2_ = super.func_180482_a(p_190672_1_, p_190672_2_);

        if (p_190672_3_)
        {
            this.func_70938_b(this.world.rand.nextInt(6));
        }

        this.func_175552_ct();
        this.levelUp();
        return p_190672_2_;
    }

    public void func_82187_q()
    {
        this.field_82190_bM = true;
    }

    public EntityVillager createChild(EntityAgeable ageable)
    {
        EntityVillager entityvillager = new EntityVillager(this.world);
        entityvillager.func_180482_a(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null);
        return entityvillager;
    }

    public boolean canBeLeashedTo(EntityPlayer player)
    {
        return false;
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {
        if (!this.world.isRemote && !this.removed)
        {
            EntityWitch entitywitch = new EntityWitch(this.world);
            entitywitch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            entitywitch.func_180482_a(this.world.getDifficultyForLocation(new BlockPos(entitywitch)), (IEntityLivingData)null);
            entitywitch.setNoAI(this.isAIDisabled());

            if (this.hasCustomName())
            {
                entitywitch.func_96094_a(this.func_95999_t());
                entitywitch.setCustomNameVisible(this.isCustomNameVisible());
            }

            this.world.addEntity0(entitywitch);
            this.remove();
        }
    }

    public InventoryBasic func_175551_co()
    {
        return this.field_175560_bz;
    }

    /**
     * Tests if this entity should pickup a weapon or an armor. Entity drops current weapon or armor if the new one is
     * better.
     */
    protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
        ItemStack itemstack = itemEntity.getItem();
        Item item = itemstack.getItem();

        if (this.func_175558_a(item))
        {
            ItemStack itemstack1 = this.field_175560_bz.addItem(itemstack);

            if (itemstack1.isEmpty())
            {
                itemEntity.remove();
            }
            else
            {
                itemstack.setCount(itemstack1.getCount());
            }
        }
    }

    private boolean func_175558_a(Item p_175558_1_)
    {
        return p_175558_1_ == Items.BREAD || p_175558_1_ == Items.POTATO || p_175558_1_ == Items.CARROT || p_175558_1_ == Items.WHEAT || p_175558_1_ == Items.WHEAT_SEEDS || p_175558_1_ == Items.BEETROOT || p_175558_1_ == Items.BEETROOT_SEEDS;
    }

    public boolean func_175553_cp()
    {
        return this.func_175559_s(1);
    }

    /**
     * Used by {@link net.minecraft.entity.ai.EntityAIVillagerInteract EntityAIVillagerInteract} to check if the
     * villager can give some items from an inventory to another villager.
     */
    public boolean canAbondonItems()
    {
        return this.func_175559_s(2);
    }

    public boolean wantsMoreFood()
    {
        boolean flag = this.func_70946_n() == 0;

        if (flag)
        {
            return !this.func_175559_s(5);
        }
        else
        {
            return !this.func_175559_s(1);
        }
    }

    private boolean func_175559_s(int p_175559_1_)
    {
        boolean flag = this.func_70946_n() == 0;

        for (int i = 0; i < this.field_175560_bz.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.field_175560_bz.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                if (itemstack.getItem() == Items.BREAD && itemstack.getCount() >= 3 * p_175559_1_ || itemstack.getItem() == Items.POTATO && itemstack.getCount() >= 12 * p_175559_1_ || itemstack.getItem() == Items.CARROT && itemstack.getCount() >= 12 * p_175559_1_ || itemstack.getItem() == Items.BEETROOT && itemstack.getCount() >= 12 * p_175559_1_)
                {
                    return true;
                }

                if (flag && itemstack.getItem() == Items.WHEAT && itemstack.getCount() >= 9 * p_175559_1_)
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns true if villager has seeds, potatoes or carrots in inventory
     */
    public boolean isFarmItemInInventory()
    {
        for (int i = 0; i < this.field_175560_bz.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.field_175560_bz.getStackInSlot(i);

            if (!itemstack.isEmpty() && (itemstack.getItem() == Items.WHEAT_SEEDS || itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.CARROT || itemstack.getItem() == Items.BEETROOT_SEEDS))
            {
                return true;
            }
        }

        return false;
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
    {
        if (super.replaceItemInInventory(inventorySlot, itemStackIn))
        {
            return true;
        }
        else
        {
            int i = inventorySlot - 300;

            if (i >= 0 && i < this.field_175560_bz.getSizeInventory())
            {
                this.field_175560_bz.setInventorySlotContents(i, itemStackIn);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    static class EmeraldForItems implements EntityVillager.ITradeList
    {
        public Item field_179405_a;
        public EntityVillager.PriceInfo field_179404_b;

        public EmeraldForItems(Item p_i45815_1_, EntityVillager.PriceInfo p_i45815_2_)
        {
            this.field_179405_a = p_i45815_1_;
            this.field_179404_b = p_i45815_2_;
        }

        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_)
        {
            int i = 1;

            if (this.field_179404_b != null)
            {
                i = this.field_179404_b.func_179412_a(p_190888_3_);
            }

            p_190888_2_.add(new MerchantRecipe(new ItemStack(this.field_179405_a, i, 0), Items.EMERALD));
        }
    }

    interface ITradeList
    {
        void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_);
    }

    static class ItemAndEmeraldToItem implements EntityVillager.ITradeList
    {
        public ItemStack field_179411_a;
        public EntityVillager.PriceInfo field_179409_b;
        public ItemStack field_179410_c;
        public EntityVillager.PriceInfo field_179408_d;

        public ItemAndEmeraldToItem(Item p_i45813_1_, EntityVillager.PriceInfo p_i45813_2_, Item p_i45813_3_, EntityVillager.PriceInfo p_i45813_4_)
        {
            this.field_179411_a = new ItemStack(p_i45813_1_);
            this.field_179409_b = p_i45813_2_;
            this.field_179410_c = new ItemStack(p_i45813_3_);
            this.field_179408_d = p_i45813_4_;
        }

        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_)
        {
            int i = this.field_179409_b.func_179412_a(p_190888_3_);
            int j = this.field_179408_d.func_179412_a(p_190888_3_);
            p_190888_2_.add(new MerchantRecipe(new ItemStack(this.field_179411_a.getItem(), i, this.field_179411_a.func_77960_j()), new ItemStack(Items.EMERALD), new ItemStack(this.field_179410_c.getItem(), j, this.field_179410_c.func_77960_j())));
        }
    }

    static class ListEnchantedBookForEmeralds implements EntityVillager.ITradeList
    {
        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_)
        {
            Enchantment enchantment = (Enchantment)Enchantment.field_185264_b.getRandom(p_190888_3_);
            int i = MathHelper.nextInt(p_190888_3_, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack itemstack = ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(enchantment, i));
            int j = 2 + p_190888_3_.nextInt(5 + i * 10) + 3 * i;

            if (enchantment.isTreasureEnchantment())
            {
                j *= 2;
            }

            if (j > 64)
            {
                j = 64;
            }

            p_190888_2_.add(new MerchantRecipe(new ItemStack(Items.BOOK), new ItemStack(Items.EMERALD, j), itemstack));
        }
    }

    static class ListEnchantedItemForEmeralds implements EntityVillager.ITradeList
    {
        public ItemStack field_179407_a;
        public EntityVillager.PriceInfo field_179406_b;

        public ListEnchantedItemForEmeralds(Item p_i45814_1_, EntityVillager.PriceInfo p_i45814_2_)
        {
            this.field_179407_a = new ItemStack(p_i45814_1_);
            this.field_179406_b = p_i45814_2_;
        }

        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_)
        {
            int i = 1;

            if (this.field_179406_b != null)
            {
                i = this.field_179406_b.func_179412_a(p_190888_3_);
            }

            ItemStack itemstack = new ItemStack(Items.EMERALD, i, 0);
            ItemStack itemstack1 = EnchantmentHelper.addRandomEnchantment(p_190888_3_, new ItemStack(this.field_179407_a.getItem(), 1, this.field_179407_a.func_77960_j()), 5 + p_190888_3_.nextInt(15), false);
            p_190888_2_.add(new MerchantRecipe(itemstack, itemstack1));
        }
    }

    static class ListItemForEmeralds implements EntityVillager.ITradeList
    {
        public ItemStack field_179403_a;
        public EntityVillager.PriceInfo field_179402_b;

        public ListItemForEmeralds(Item p_i45811_1_, EntityVillager.PriceInfo p_i45811_2_)
        {
            this.field_179403_a = new ItemStack(p_i45811_1_);
            this.field_179402_b = p_i45811_2_;
        }

        public ListItemForEmeralds(ItemStack p_i45812_1_, EntityVillager.PriceInfo p_i45812_2_)
        {
            this.field_179403_a = p_i45812_1_;
            this.field_179402_b = p_i45812_2_;
        }

        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_)
        {
            int i = 1;

            if (this.field_179402_b != null)
            {
                i = this.field_179402_b.func_179412_a(p_190888_3_);
            }

            ItemStack itemstack;
            ItemStack itemstack1;

            if (i < 0)
            {
                itemstack = new ItemStack(Items.EMERALD);
                itemstack1 = new ItemStack(this.field_179403_a.getItem(), -i, this.field_179403_a.func_77960_j());
            }
            else
            {
                itemstack = new ItemStack(Items.EMERALD, i, 0);
                itemstack1 = new ItemStack(this.field_179403_a.getItem(), 1, this.field_179403_a.func_77960_j());
            }

            p_190888_2_.add(new MerchantRecipe(itemstack, itemstack1));
        }
    }

    static class PriceInfo extends Tuple<Integer, Integer>
    {
        public PriceInfo(int p_i45810_1_, int p_i45810_2_)
        {
            super(Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_));

            if (p_i45810_2_ < p_i45810_1_)
            {
                EntityVillager.field_190674_bx.warn("PriceRange({}, {}) invalid, {} smaller than {}", Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_), Integer.valueOf(p_i45810_2_), Integer.valueOf(p_i45810_1_));
            }
        }

        public int func_179412_a(Random p_179412_1_)
        {
            return ((Integer)this.getA()).intValue() >= ((Integer)this.getB()).intValue() ? ((Integer)this.getA()).intValue() : ((Integer)this.getA()).intValue() + p_179412_1_.nextInt(((Integer)this.getB()).intValue() - ((Integer)this.getA()).intValue() + 1);
        }
    }

    static class TreasureMapForEmeralds implements EntityVillager.ITradeList
    {
        public EntityVillager.PriceInfo field_190889_a;
        public String field_190890_b;
        public MapDecoration.Type field_190891_c;

        public TreasureMapForEmeralds(EntityVillager.PriceInfo p_i47340_1_, String p_i47340_2_, MapDecoration.Type p_i47340_3_)
        {
            this.field_190889_a = p_i47340_1_;
            this.field_190890_b = p_i47340_2_;
            this.field_190891_c = p_i47340_3_;
        }

        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_)
        {
            int i = this.field_190889_a.func_179412_a(p_190888_3_);
            World world = p_190888_1_.getWorld();
            BlockPos blockpos = world.func_190528_a(this.field_190890_b, p_190888_1_.func_190671_u_(), true);

            if (blockpos != null)
            {
                ItemStack itemstack = ItemMap.func_190906_a(world, (double)blockpos.getX(), (double)blockpos.getZ(), (byte)2, true, true);
                ItemMap.func_190905_a(world, itemstack);
                MapData.addTargetDecoration(itemstack, blockpos, "+", this.field_190891_c);
                itemstack.func_190924_f("filled_map." + this.field_190890_b.toLowerCase(Locale.ROOT));
                p_190888_2_.add(new MerchantRecipe(new ItemStack(Items.EMERALD, i), new ItemStack(Items.COMPASS), itemstack));
            }
        }
    }
}

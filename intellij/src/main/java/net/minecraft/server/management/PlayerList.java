package net.minecraft.server.management;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class PlayerList
{
    public static final File FILE_PLAYERBANS = new File("banned-players.json");
    public static final File FILE_IPBANS = new File("banned-ips.json");
    public static final File FILE_OPS = new File("ops.json");
    public static final File FILE_WHITELIST = new File("whitelist.json");
    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    private final MinecraftServer server;
    private final List<EntityPlayerMP> players = Lists.<EntityPlayerMP>newArrayList();
    private final Map<UUID, EntityPlayerMP> uuidToPlayerMap = Maps.<UUID, EntityPlayerMP>newHashMap();
    private final UserListBans bannedPlayers;
    private final UserListIPBans bannedIPs;
    private final UserListOps ops;
    private final UserListWhitelist whiteListedPlayers;
    private final Map<UUID, StatisticsManagerServer> playerStatFiles;
    private final Map<UUID, PlayerAdvancements> advancements;
    private IPlayerFileData playerDataManager;
    private boolean whiteListEnforced;
    protected int maxPlayers;
    private int viewDistance;
    private GameType gameType;
    private boolean commandsAllowedForAll;
    private int playerPingIndex;

    public PlayerList(MinecraftServer p_i1500_1_)
    {
        this.bannedPlayers = new UserListBans(FILE_PLAYERBANS);
        this.bannedIPs = new UserListIPBans(FILE_IPBANS);
        this.ops = new UserListOps(FILE_OPS);
        this.whiteListedPlayers = new UserListWhitelist(FILE_WHITELIST);
        this.playerStatFiles = Maps.<UUID, StatisticsManagerServer>newHashMap();
        this.advancements = Maps.<UUID, PlayerAdvancements>newHashMap();
        this.server = p_i1500_1_;
        this.bannedPlayers.setLanServer(false);
        this.bannedIPs.setLanServer(false);
        this.maxPlayers = 8;
    }

    public void initializeConnectionToPlayer(NetworkManager netManager, EntityPlayerMP playerIn)
    {
        GameProfile gameprofile = playerIn.getGameProfile();
        PlayerProfileCache playerprofilecache = this.server.getPlayerProfileCache();
        GameProfile gameprofile1 = playerprofilecache.getProfileByUUID(gameprofile.getId());
        String s = gameprofile1 == null ? gameprofile.getName() : gameprofile1.getName();
        playerprofilecache.addEntry(gameprofile);
        NBTTagCompound nbttagcompound = this.readPlayerDataFromFile(playerIn);
        playerIn.setWorld(this.server.getWorld(playerIn.dimension));
        playerIn.interactionManager.setWorld((WorldServer)playerIn.world);
        String s1 = "local";

        if (netManager.getRemoteAddress() != null)
        {
            s1 = netManager.getRemoteAddress().toString();
        }

        LOGGER.info("{}[{}] logged in with entity id {} at ({}, {}, {})", playerIn.func_70005_c_(), s1, Integer.valueOf(playerIn.getEntityId()), Double.valueOf(playerIn.posX), Double.valueOf(playerIn.posY), Double.valueOf(playerIn.posZ));
        WorldServer worldserver = this.server.getWorld(playerIn.dimension);
        WorldInfo worldinfo = worldserver.getWorldInfo();
        this.setPlayerGameTypeBasedOnOther(playerIn, (EntityPlayerMP)null, worldserver);
        NetHandlerPlayServer nethandlerplayserver = new NetHandlerPlayServer(this.server, netManager, playerIn);
        nethandlerplayserver.sendPacket(new SPacketJoinGame(playerIn.getEntityId(), playerIn.interactionManager.getGameType(), worldinfo.isHardcore(), worldserver.dimension.getType().getId(), worldserver.getDifficulty(), this.getMaxPlayers(), worldinfo.getGenerator(), worldserver.getGameRules().func_82766_b("reducedDebugInfo")));
        nethandlerplayserver.sendPacket(new SPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(this.getServer().getServerModName())));
        nethandlerplayserver.sendPacket(new SPacketServerDifficulty(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
        nethandlerplayserver.sendPacket(new SPacketPlayerAbilities(playerIn.abilities));
        nethandlerplayserver.sendPacket(new SPacketHeldItemChange(playerIn.inventory.currentItem));
        this.updatePermissionLevel(playerIn);
        playerIn.getStats().markAllDirty();
        playerIn.getRecipeBook().init(playerIn);
        this.sendScoreboard((ServerScoreboard)worldserver.getScoreboard(), playerIn);
        this.server.refreshStatusNextTick();
        TextComponentTranslation textcomponenttranslation;

        if (playerIn.func_70005_c_().equalsIgnoreCase(s))
        {
            textcomponenttranslation = new TextComponentTranslation("multiplayer.player.joined", new Object[] {playerIn.getDisplayName()});
        }
        else
        {
            textcomponenttranslation = new TextComponentTranslation("multiplayer.player.joined.renamed", new Object[] {playerIn.getDisplayName(), s});
        }

        textcomponenttranslation.getStyle().setColor(TextFormatting.YELLOW);
        this.sendMessage(textcomponenttranslation);
        this.func_72377_c(playerIn);
        nethandlerplayserver.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
        this.sendWorldInfo(playerIn, worldserver);

        if (!this.server.getResourcePackUrl().isEmpty())
        {
            playerIn.loadResourcePack(this.server.getResourcePackUrl(), this.server.getResourcePackHash());
        }

        for (PotionEffect potioneffect : playerIn.getActivePotionEffects())
        {
            nethandlerplayserver.sendPacket(new SPacketEntityEffect(playerIn.getEntityId(), potioneffect));
        }

        if (nbttagcompound != null && nbttagcompound.contains("RootVehicle", 10))
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("RootVehicle");
            Entity entity1 = AnvilChunkLoader.func_186051_a(nbttagcompound1.getCompound("Entity"), worldserver, true);

            if (entity1 != null)
            {
                UUID uuid = nbttagcompound1.getUniqueId("Attach");

                if (entity1.getUniqueID().equals(uuid))
                {
                    playerIn.startRiding(entity1, true);
                }
                else
                {
                    for (Entity entity : entity1.getRecursivePassengers())
                    {
                        if (entity.getUniqueID().equals(uuid))
                        {
                            playerIn.startRiding(entity, true);
                            break;
                        }
                    }
                }

                if (!playerIn.isPassenger())
                {
                    LOGGER.warn("Couldn't reattach entity to player");
                    worldserver.func_72973_f(entity1);

                    for (Entity entity2 : entity1.getRecursivePassengers())
                    {
                        worldserver.func_72973_f(entity2);
                    }
                }
            }
        }

        playerIn.addSelfToInternalCraftingInventory();
    }

    protected void sendScoreboard(ServerScoreboard scoreboardIn, EntityPlayerMP playerIn)
    {
        Set<ScoreObjective> set = Sets.<ScoreObjective>newHashSet();

        for (ScorePlayerTeam scoreplayerteam : scoreboardIn.getTeams())
        {
            playerIn.connection.sendPacket(new SPacketTeams(scoreplayerteam, 0));
        }

        for (int i = 0; i < 19; ++i)
        {
            ScoreObjective scoreobjective = scoreboardIn.getObjectiveInDisplaySlot(i);

            if (scoreobjective != null && !set.contains(scoreobjective))
            {
                for (Packet<?> packet : scoreboardIn.getCreatePackets(scoreobjective))
                {
                    playerIn.connection.sendPacket(packet);
                }

                set.add(scoreobjective);
            }
        }
    }

    public void func_72364_a(WorldServer[] p_72364_1_)
    {
        this.playerDataManager = p_72364_1_[0].func_72860_G().func_75756_e();
        p_72364_1_[0].getWorldBorder().addListener(new IBorderListener()
        {
            public void onSizeChanged(WorldBorder border, double newSize)
            {
                PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(border, SPacketWorldBorder.Action.SET_SIZE));
            }
            public void onTransitionStarted(WorldBorder border, double oldSize, double newSize, long time)
            {
                PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(border, SPacketWorldBorder.Action.LERP_SIZE));
            }
            public void onCenterChanged(WorldBorder border, double x, double z)
            {
                PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(border, SPacketWorldBorder.Action.SET_CENTER));
            }
            public void onWarningTimeChanged(WorldBorder border, int newTime)
            {
                PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(border, SPacketWorldBorder.Action.SET_WARNING_TIME));
            }
            public void onWarningDistanceChanged(WorldBorder border, int newDistance)
            {
                PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(border, SPacketWorldBorder.Action.SET_WARNING_BLOCKS));
            }
            public void onDamageAmountChanged(WorldBorder border, double newAmount)
            {
            }
            public void onDamageBufferChanged(WorldBorder border, double newSize)
            {
            }
        });
    }

    public void func_72375_a(EntityPlayerMP p_72375_1_, @Nullable WorldServer p_72375_2_)
    {
        WorldServer worldserver = p_72375_1_.getServerWorld();

        if (p_72375_2_ != null)
        {
            p_72375_2_.func_184164_w().func_72695_c(p_72375_1_);
        }

        worldserver.func_184164_w().func_72683_a(p_72375_1_);
        worldserver.getChunkProvider().func_186025_d((int)p_72375_1_.posX >> 4, (int)p_72375_1_.posZ >> 4);

        if (p_72375_2_ != null)
        {
            CriteriaTriggers.CHANGED_DIMENSION.trigger(p_72375_1_, p_72375_2_.dimension.getType(), worldserver.dimension.getType());

            if (p_72375_2_.dimension.getType() == DimensionType.NETHER && p_72375_1_.world.dimension.getType() == DimensionType.OVERWORLD && p_72375_1_.func_193106_Q() != null)
            {
                CriteriaTriggers.NETHER_TRAVEL.trigger(p_72375_1_, p_72375_1_.func_193106_Q());
            }
        }
    }

    public int func_72372_a()
    {
        return PlayerChunkMap.func_72686_a(this.getViewDistance());
    }

    @Nullable

    /**
     * called during player login. reads the player information from disk.
     */
    public NBTTagCompound readPlayerDataFromFile(EntityPlayerMP playerIn)
    {
        NBTTagCompound nbttagcompound = this.server.worlds[0].getWorldInfo().getPlayerNBTTagCompound();
        NBTTagCompound nbttagcompound1;

        if (playerIn.func_70005_c_().equals(this.server.getServerOwner()) && nbttagcompound != null)
        {
            nbttagcompound1 = nbttagcompound;
            playerIn.read(nbttagcompound);
            LOGGER.debug("loading single player");
        }
        else
        {
            nbttagcompound1 = this.playerDataManager.readPlayerData(playerIn);
        }

        return nbttagcompound1;
    }

    /**
     * also stores the NBTTags if this is an intergratedPlayerList
     */
    protected void writePlayerData(EntityPlayerMP playerIn)
    {
        this.playerDataManager.writePlayerData(playerIn);
        StatisticsManagerServer statisticsmanagerserver = this.playerStatFiles.get(playerIn.getUniqueID());

        if (statisticsmanagerserver != null)
        {
            statisticsmanagerserver.saveStatFile();
        }

        PlayerAdvancements playeradvancements = this.advancements.get(playerIn.getUniqueID());

        if (playeradvancements != null)
        {
            playeradvancements.save();
        }
    }

    public void func_72377_c(EntityPlayerMP p_72377_1_)
    {
        this.players.add(p_72377_1_);
        this.uuidToPlayerMap.put(p_72377_1_.getUniqueID(), p_72377_1_);
        this.sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] {p_72377_1_}));
        WorldServer worldserver = this.server.getWorld(p_72377_1_.dimension);

        for (int i = 0; i < this.players.size(); ++i)
        {
            p_72377_1_.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[] {this.players.get(i)}));
        }

        worldserver.addEntity0(p_72377_1_);
        this.func_72375_a(p_72377_1_, (WorldServer)null);
    }

    public void func_72358_d(EntityPlayerMP p_72358_1_)
    {
        p_72358_1_.getServerWorld().func_184164_w().func_72685_d(p_72358_1_);
    }

    /**
     * Called when a player disconnects from the game. Writes player data to disk and removes them from the world.
     */
    public void playerLoggedOut(EntityPlayerMP playerIn)
    {
        WorldServer worldserver = playerIn.getServerWorld();
        playerIn.addStat(StatList.LEAVE_GAME);
        this.writePlayerData(playerIn);

        if (playerIn.isPassenger())
        {
            Entity entity = playerIn.getLowestRidingEntity();

            if (entity.func_184180_b(EntityPlayerMP.class).size() == 1)
            {
                LOGGER.debug("Removing player mount");
                playerIn.stopRiding();
                worldserver.func_72973_f(entity);

                for (Entity entity1 : entity.getRecursivePassengers())
                {
                    worldserver.func_72973_f(entity1);
                }

                worldserver.func_72964_e(playerIn.chunkCoordX, playerIn.chunkCoordZ).markDirty();
            }
        }

        worldserver.func_72900_e(playerIn);
        worldserver.func_184164_w().func_72695_c(playerIn);
        playerIn.getAdvancements().dispose();
        this.players.remove(playerIn);
        UUID uuid = playerIn.getUniqueID();
        EntityPlayerMP entityplayermp = this.uuidToPlayerMap.get(uuid);

        if (entityplayermp == playerIn)
        {
            this.uuidToPlayerMap.remove(uuid);
            this.playerStatFiles.remove(uuid);
            this.advancements.remove(uuid);
        }

        this.sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER, new EntityPlayerMP[] {playerIn}));
    }

    public String func_148542_a(SocketAddress p_148542_1_, GameProfile p_148542_2_)
    {
        if (this.bannedPlayers.isBanned(p_148542_2_))
        {
            UserListBansEntry userlistbansentry = (UserListBansEntry)this.bannedPlayers.getEntry(p_148542_2_);
            String s1 = "You are banned from this server!\nReason: " + userlistbansentry.getBanReason();

            if (userlistbansentry.getBanEndDate() != null)
            {
                s1 = s1 + "\nYour ban will be removed on " + DATE_FORMAT.format(userlistbansentry.getBanEndDate());
            }

            return s1;
        }
        else if (!this.canJoin(p_148542_2_))
        {
            return "You are not white-listed on this server!";
        }
        else if (this.bannedIPs.isBanned(p_148542_1_))
        {
            UserListIPBansEntry userlistipbansentry = this.bannedIPs.getBanEntry(p_148542_1_);
            String s = "Your IP address is banned from this server!\nReason: " + userlistipbansentry.getBanReason();

            if (userlistipbansentry.getBanEndDate() != null)
            {
                s = s + "\nYour ban will be removed on " + DATE_FORMAT.format(userlistipbansentry.getBanEndDate());
            }

            return s;
        }
        else
        {
            return this.players.size() >= this.maxPlayers && !this.bypassesPlayerLimit(p_148542_2_) ? "The server is full!" : null;
        }
    }

    /**
     * also checks for multiple logins across servers
     */
    public EntityPlayerMP createPlayerForUser(GameProfile profile)
    {
        UUID uuid = EntityPlayer.getUUID(profile);
        List<EntityPlayerMP> list = Lists.<EntityPlayerMP>newArrayList();

        for (int i = 0; i < this.players.size(); ++i)
        {
            EntityPlayerMP entityplayermp = this.players.get(i);

            if (entityplayermp.getUniqueID().equals(uuid))
            {
                list.add(entityplayermp);
            }
        }

        EntityPlayerMP entityplayermp2 = this.uuidToPlayerMap.get(profile.getId());

        if (entityplayermp2 != null && !list.contains(entityplayermp2))
        {
            list.add(entityplayermp2);
        }

        for (EntityPlayerMP entityplayermp1 : list)
        {
            entityplayermp1.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.duplicate_login", new Object[0]));
        }

        PlayerInteractionManager playerinteractionmanager;

        if (this.server.isDemo())
        {
            playerinteractionmanager = new DemoPlayerInteractionManager(this.server.getWorld(0));
        }
        else
        {
            playerinteractionmanager = new PlayerInteractionManager(this.server.getWorld(0));
        }

        return new EntityPlayerMP(this.server, this.server.getWorld(0), profile, playerinteractionmanager);
    }

    /**
     * Destroys the given player entity and recreates another in the given dimension. Used when respawning after death
     * or returning from the End
     */
    public EntityPlayerMP recreatePlayerEntity(EntityPlayerMP playerIn, int dimension, boolean conqueredEnd)
    {
        playerIn.getServerWorld().func_73039_n().func_72787_a(playerIn);
        playerIn.getServerWorld().func_73039_n().func_72790_b(playerIn);
        playerIn.getServerWorld().func_184164_w().func_72695_c(playerIn);
        this.players.remove(playerIn);
        this.server.getWorld(playerIn.dimension).func_72973_f(playerIn);
        BlockPos blockpos = playerIn.getBedLocation();
        boolean flag = playerIn.isSpawnForced();
        playerIn.dimension = dimension;
        PlayerInteractionManager playerinteractionmanager;

        if (this.server.isDemo())
        {
            playerinteractionmanager = new DemoPlayerInteractionManager(this.server.getWorld(playerIn.dimension));
        }
        else
        {
            playerinteractionmanager = new PlayerInteractionManager(this.server.getWorld(playerIn.dimension));
        }

        EntityPlayerMP entityplayermp = new EntityPlayerMP(this.server, this.server.getWorld(playerIn.dimension), playerIn.getGameProfile(), playerinteractionmanager);
        entityplayermp.connection = playerIn.connection;
        entityplayermp.copyFrom(playerIn, conqueredEnd);
        entityplayermp.setEntityId(playerIn.getEntityId());
        entityplayermp.func_174817_o(playerIn);
        entityplayermp.setPrimaryHand(playerIn.getPrimaryHand());

        for (String s : playerIn.getTags())
        {
            entityplayermp.addTag(s);
        }

        WorldServer worldserver = this.server.getWorld(playerIn.dimension);
        this.setPlayerGameTypeBasedOnOther(entityplayermp, playerIn, worldserver);

        if (blockpos != null)
        {
            BlockPos blockpos1 = EntityPlayer.func_180467_a(this.server.getWorld(playerIn.dimension), blockpos, flag);

            if (blockpos1 != null)
            {
                entityplayermp.setLocationAndAngles((double)((float)blockpos1.getX() + 0.5F), (double)((float)blockpos1.getY() + 0.1F), (double)((float)blockpos1.getZ() + 0.5F), 0.0F, 0.0F);
                entityplayermp.func_180473_a(blockpos, flag);
            }
            else
            {
                entityplayermp.connection.sendPacket(new SPacketChangeGameState(0, 0.0F));
            }
        }

        worldserver.getChunkProvider().func_186025_d((int)entityplayermp.posX >> 4, (int)entityplayermp.posZ >> 4);

        while (!worldserver.func_184144_a(entityplayermp, entityplayermp.getBoundingBox()).isEmpty() && entityplayermp.posY < 256.0D)
        {
            entityplayermp.setPosition(entityplayermp.posX, entityplayermp.posY + 1.0D, entityplayermp.posZ);
        }

        entityplayermp.connection.sendPacket(new SPacketRespawn(entityplayermp.dimension, entityplayermp.world.getDifficulty(), entityplayermp.world.getWorldInfo().getGenerator(), entityplayermp.interactionManager.getGameType()));
        BlockPos blockpos2 = worldserver.getSpawnPoint();
        entityplayermp.connection.setPlayerLocation(entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ, entityplayermp.rotationYaw, entityplayermp.rotationPitch);
        entityplayermp.connection.sendPacket(new SPacketSpawnPosition(blockpos2));
        entityplayermp.connection.sendPacket(new SPacketSetExperience(entityplayermp.experience, entityplayermp.experienceTotal, entityplayermp.experienceLevel));
        this.sendWorldInfo(entityplayermp, worldserver);
        this.updatePermissionLevel(entityplayermp);
        worldserver.func_184164_w().func_72683_a(entityplayermp);
        worldserver.addEntity0(entityplayermp);
        this.players.add(entityplayermp);
        this.uuidToPlayerMap.put(entityplayermp.getUniqueID(), entityplayermp);
        entityplayermp.addSelfToInternalCraftingInventory();
        entityplayermp.setHealth(entityplayermp.getHealth());
        return entityplayermp;
    }

    public void updatePermissionLevel(EntityPlayerMP player)
    {
        GameProfile gameprofile = player.getGameProfile();
        int i = this.canSendCommands(gameprofile) ? this.ops.func_187452_a(gameprofile) : 0;
        i = this.server.isSinglePlayer() && this.server.worlds[0].getWorldInfo().areCommandsAllowed() ? 4 : i;
        i = this.commandsAllowedForAll ? 4 : i;
        this.sendPlayerPermissionLevel(player, i);
    }

    public void func_187242_a(EntityPlayerMP p_187242_1_, int p_187242_2_)
    {
        int i = p_187242_1_.dimension;
        WorldServer worldserver = this.server.getWorld(p_187242_1_.dimension);
        p_187242_1_.dimension = p_187242_2_;
        WorldServer worldserver1 = this.server.getWorld(p_187242_1_.dimension);
        p_187242_1_.connection.sendPacket(new SPacketRespawn(p_187242_1_.dimension, p_187242_1_.world.getDifficulty(), p_187242_1_.world.getWorldInfo().getGenerator(), p_187242_1_.interactionManager.getGameType()));
        this.updatePermissionLevel(p_187242_1_);
        worldserver.func_72973_f(p_187242_1_);
        p_187242_1_.removed = false;
        this.func_82448_a(p_187242_1_, i, worldserver, worldserver1);
        this.func_72375_a(p_187242_1_, worldserver);
        p_187242_1_.connection.setPlayerLocation(p_187242_1_.posX, p_187242_1_.posY, p_187242_1_.posZ, p_187242_1_.rotationYaw, p_187242_1_.rotationPitch);
        p_187242_1_.interactionManager.setWorld(worldserver1);
        p_187242_1_.connection.sendPacket(new SPacketPlayerAbilities(p_187242_1_.abilities));
        this.sendWorldInfo(p_187242_1_, worldserver1);
        this.sendInventory(p_187242_1_);

        for (PotionEffect potioneffect : p_187242_1_.getActivePotionEffects())
        {
            p_187242_1_.connection.sendPacket(new SPacketEntityEffect(p_187242_1_.getEntityId(), potioneffect));
        }
    }

    public void func_82448_a(Entity p_82448_1_, int p_82448_2_, WorldServer p_82448_3_, WorldServer p_82448_4_)
    {
        double d0 = p_82448_1_.posX;
        double d1 = p_82448_1_.posZ;
        double d2 = 8.0D;
        float f = p_82448_1_.rotationYaw;
        p_82448_3_.profiler.startSection("moving");

        if (p_82448_1_.dimension == -1)
        {
            d0 = MathHelper.clamp(d0 / 8.0D, p_82448_4_.getWorldBorder().minX() + 16.0D, p_82448_4_.getWorldBorder().maxX() - 16.0D);
            d1 = MathHelper.clamp(d1 / 8.0D, p_82448_4_.getWorldBorder().minZ() + 16.0D, p_82448_4_.getWorldBorder().maxZ() - 16.0D);
            p_82448_1_.setLocationAndAngles(d0, p_82448_1_.posY, d1, p_82448_1_.rotationYaw, p_82448_1_.rotationPitch);

            if (p_82448_1_.isAlive())
            {
                p_82448_3_.func_72866_a(p_82448_1_, false);
            }
        }
        else if (p_82448_1_.dimension == 0)
        {
            d0 = MathHelper.clamp(d0 * 8.0D, p_82448_4_.getWorldBorder().minX() + 16.0D, p_82448_4_.getWorldBorder().maxX() - 16.0D);
            d1 = MathHelper.clamp(d1 * 8.0D, p_82448_4_.getWorldBorder().minZ() + 16.0D, p_82448_4_.getWorldBorder().maxZ() - 16.0D);
            p_82448_1_.setLocationAndAngles(d0, p_82448_1_.posY, d1, p_82448_1_.rotationYaw, p_82448_1_.rotationPitch);

            if (p_82448_1_.isAlive())
            {
                p_82448_3_.func_72866_a(p_82448_1_, false);
            }
        }
        else
        {
            BlockPos blockpos;

            if (p_82448_2_ == 1)
            {
                blockpos = p_82448_4_.getSpawnPoint();
            }
            else
            {
                blockpos = p_82448_4_.getSpawnCoordinate();
            }

            d0 = (double)blockpos.getX();
            p_82448_1_.posY = (double)blockpos.getY();
            d1 = (double)blockpos.getZ();
            p_82448_1_.setLocationAndAngles(d0, p_82448_1_.posY, d1, 90.0F, 0.0F);

            if (p_82448_1_.isAlive())
            {
                p_82448_3_.func_72866_a(p_82448_1_, false);
            }
        }

        p_82448_3_.profiler.endSection();

        if (p_82448_2_ != 1)
        {
            p_82448_3_.profiler.startSection("placing");
            d0 = (double)MathHelper.clamp((int)d0, -29999872, 29999872);
            d1 = (double)MathHelper.clamp((int)d1, -29999872, 29999872);

            if (p_82448_1_.isAlive())
            {
                p_82448_1_.setLocationAndAngles(d0, p_82448_1_.posY, d1, p_82448_1_.rotationYaw, p_82448_1_.rotationPitch);
                p_82448_4_.getDefaultTeleporter().func_180266_a(p_82448_1_, f);
                p_82448_4_.addEntity0(p_82448_1_);
                p_82448_4_.func_72866_a(p_82448_1_, false);
            }

            p_82448_3_.profiler.endSection();
        }

        p_82448_1_.setWorld(p_82448_4_);
    }

    /**
     * self explanitory
     */
    public void tick()
    {
        if (++this.playerPingIndex > 600)
        {
            this.sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_LATENCY, this.players));
            this.playerPingIndex = 0;
        }
    }

    public void sendPacketToAllPlayers(Packet<?> packetIn)
    {
        for (int i = 0; i < this.players.size(); ++i)
        {
            (this.players.get(i)).connection.sendPacket(packetIn);
        }
    }

    public void sendPacketToAllPlayersInDimension(Packet<?> packetIn, int dimension)
    {
        for (int i = 0; i < this.players.size(); ++i)
        {
            EntityPlayerMP entityplayermp = this.players.get(i);

            if (entityplayermp.dimension == dimension)
            {
                entityplayermp.connection.sendPacket(packetIn);
            }
        }
    }

    public void sendMessageToAllTeamMembers(EntityPlayer player, ITextComponent message)
    {
        Team team = player.getTeam();

        if (team != null)
        {
            for (String s : team.getMembershipCollection())
            {
                EntityPlayerMP entityplayermp = this.getPlayerByUsername(s);

                if (entityplayermp != null && entityplayermp != player)
                {
                    entityplayermp.sendMessage(message);
                }
            }
        }
    }

    public void sendMessageToTeamOrAllPlayers(EntityPlayer player, ITextComponent message)
    {
        Team team = player.getTeam();

        if (team == null)
        {
            this.sendMessage(message);
        }
        else
        {
            for (int i = 0; i < this.players.size(); ++i)
            {
                EntityPlayerMP entityplayermp = this.players.get(i);

                if (entityplayermp.getTeam() != team)
                {
                    entityplayermp.sendMessage(message);
                }
            }
        }
    }

    public String func_181058_b(boolean p_181058_1_)
    {
        String s = "";
        List<EntityPlayerMP> list = Lists.newArrayList(this.players);

        for (int i = 0; i < list.size(); ++i)
        {
            if (i > 0)
            {
                s = s + ", ";
            }

            s = s + ((EntityPlayerMP)list.get(i)).func_70005_c_();

            if (p_181058_1_)
            {
                s = s + " (" + ((EntityPlayerMP)list.get(i)).getCachedUniqueIdString() + ")";
            }
        }

        return s;
    }

    /**
     * Returns an array of the usernames of all the connected players.
     */
    public String[] getOnlinePlayerNames()
    {
        String[] astring = new String[this.players.size()];

        for (int i = 0; i < this.players.size(); ++i)
        {
            astring[i] = ((EntityPlayerMP)this.players.get(i)).func_70005_c_();
        }

        return astring;
    }

    public GameProfile[] func_152600_g()
    {
        GameProfile[] agameprofile = new GameProfile[this.players.size()];

        for (int i = 0; i < this.players.size(); ++i)
        {
            agameprofile[i] = ((EntityPlayerMP)this.players.get(i)).getGameProfile();
        }

        return agameprofile;
    }

    public UserListBans getBannedPlayers()
    {
        return this.bannedPlayers;
    }

    public UserListIPBans getBannedIPs()
    {
        return this.bannedIPs;
    }

    public void addOp(GameProfile profile)
    {
        int i = this.server.getOpPermissionLevel();
        this.ops.addEntry(new UserListOpsEntry(profile, this.server.getOpPermissionLevel(), this.ops.bypassesPlayerLimit(profile)));
        this.sendPlayerPermissionLevel(this.getPlayerByUUID(profile.getId()), i);
    }

    public void removeOp(GameProfile profile)
    {
        this.ops.removeEntry(profile);
        this.sendPlayerPermissionLevel(this.getPlayerByUUID(profile.getId()), 0);
    }

    private void sendPlayerPermissionLevel(EntityPlayerMP player, int permLevel)
    {
        if (player != null && player.connection != null)
        {
            byte b0;

            if (permLevel <= 0)
            {
                b0 = 24;
            }
            else if (permLevel >= 4)
            {
                b0 = 28;
            }
            else
            {
                b0 = (byte)(24 + permLevel);
            }

            player.connection.sendPacket(new SPacketEntityStatus(player, b0));
        }
    }

    public boolean canJoin(GameProfile profile)
    {
        return !this.whiteListEnforced || this.ops.hasEntry(profile) || this.whiteListedPlayers.hasEntry(profile);
    }

    public boolean canSendCommands(GameProfile profile)
    {
        return this.ops.hasEntry(profile) || this.server.isSinglePlayer() && this.server.worlds[0].getWorldInfo().areCommandsAllowed() && this.server.getServerOwner().equalsIgnoreCase(profile.getName()) || this.commandsAllowedForAll;
    }

    @Nullable
    public EntityPlayerMP getPlayerByUsername(String username)
    {
        for (EntityPlayerMP entityplayermp : this.players)
        {
            if (entityplayermp.func_70005_c_().equalsIgnoreCase(username))
            {
                return entityplayermp;
            }
        }

        return null;
    }

    /**
     * params: srcPlayer,x,y,z,r,dimension. The packet is not sent to the srcPlayer, but all other players within the
     * search radius
     */
    public void sendToAllNearExcept(@Nullable EntityPlayer except, double x, double y, double z, double radius, int dimension, Packet<?> packetIn)
    {
        for (int i = 0; i < this.players.size(); ++i)
        {
            EntityPlayerMP entityplayermp = this.players.get(i);

            if (entityplayermp != except && entityplayermp.dimension == dimension)
            {
                double d0 = x - entityplayermp.posX;
                double d1 = y - entityplayermp.posY;
                double d2 = z - entityplayermp.posZ;

                if (d0 * d0 + d1 * d1 + d2 * d2 < radius * radius)
                {
                    entityplayermp.connection.sendPacket(packetIn);
                }
            }
        }
    }

    /**
     * Saves all of the players' current states.
     */
    public void saveAllPlayerData()
    {
        for (int i = 0; i < this.players.size(); ++i)
        {
            this.writePlayerData(this.players.get(i));
        }
    }

    public void func_152601_d(GameProfile p_152601_1_)
    {
        this.whiteListedPlayers.addEntry(new UserListWhitelistEntry(p_152601_1_));
    }

    public void func_152597_c(GameProfile p_152597_1_)
    {
        this.whiteListedPlayers.removeEntry(p_152597_1_);
    }

    public UserListWhitelist getWhitelistedPlayers()
    {
        return this.whiteListedPlayers;
    }

    public String[] getWhitelistedPlayerNames()
    {
        return this.whiteListedPlayers.getKeys();
    }

    public UserListOps getOppedPlayers()
    {
        return this.ops;
    }

    public String[] getOppedPlayerNames()
    {
        return this.ops.getKeys();
    }

    public void reloadWhitelist()
    {
    }

    /**
     * Updates the time and weather for the given player to those of the given world
     */
    public void sendWorldInfo(EntityPlayerMP playerIn, WorldServer worldIn)
    {
        WorldBorder worldborder = this.server.worlds[0].getWorldBorder();
        playerIn.connection.sendPacket(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.INITIALIZE));
        playerIn.connection.sendPacket(new SPacketTimeUpdate(worldIn.getGameTime(), worldIn.getDayTime(), worldIn.getGameRules().func_82766_b("doDaylightCycle")));
        BlockPos blockpos = worldIn.getSpawnPoint();
        playerIn.connection.sendPacket(new SPacketSpawnPosition(blockpos));

        if (worldIn.isRaining())
        {
            playerIn.connection.sendPacket(new SPacketChangeGameState(1, 0.0F));
            playerIn.connection.sendPacket(new SPacketChangeGameState(7, worldIn.getRainStrength(1.0F)));
            playerIn.connection.sendPacket(new SPacketChangeGameState(8, worldIn.getThunderStrength(1.0F)));
        }
    }

    /**
     * sends the players inventory to himself
     */
    public void sendInventory(EntityPlayerMP playerIn)
    {
        playerIn.sendContainerToPlayer(playerIn.container);
        playerIn.setPlayerHealthUpdated();
        playerIn.connection.sendPacket(new SPacketHeldItemChange(playerIn.inventory.currentItem));
    }

    /**
     * Returns the number of players currently on the server.
     */
    public int getCurrentPlayerCount()
    {
        return this.players.size();
    }

    /**
     * Returns the maximum number of players allowed on the server.
     */
    public int getMaxPlayers()
    {
        return this.maxPlayers;
    }

    public String[] func_72373_m()
    {
        return this.server.worlds[0].func_72860_G().func_75756_e().func_75754_f();
    }

    public void setWhiteListEnabled(boolean whitelistEnabled)
    {
        this.whiteListEnforced = whitelistEnabled;
    }

    public List<EntityPlayerMP> getPlayersMatchingAddress(String address)
    {
        List<EntityPlayerMP> list = Lists.<EntityPlayerMP>newArrayList();

        for (EntityPlayerMP entityplayermp : this.players)
        {
            if (entityplayermp.getPlayerIP().equals(address))
            {
                list.add(entityplayermp);
            }
        }

        return list;
    }

    /**
     * Gets the view distance, in chunks.
     */
    public int getViewDistance()
    {
        return this.viewDistance;
    }

    public MinecraftServer getServer()
    {
        return this.server;
    }

    /**
     * On integrated servers, returns the host's player data to be written to level.dat.
     */
    public NBTTagCompound getHostPlayerData()
    {
        return null;
    }

    public void setGameType(GameType gameModeIn)
    {
        this.gameType = gameModeIn;
    }

    private void setPlayerGameTypeBasedOnOther(EntityPlayerMP target, EntityPlayerMP source, World worldIn)
    {
        if (source != null)
        {
            target.interactionManager.setGameType(source.interactionManager.getGameType());
        }
        else if (this.gameType != null)
        {
            target.interactionManager.setGameType(this.gameType);
        }

        target.interactionManager.initializeGameType(worldIn.getWorldInfo().getGameType());
    }

    /**
     * Sets whether all players are allowed to use commands (cheats) on the server.
     */
    public void setCommandsAllowedForAll(boolean p_72387_1_)
    {
        this.commandsAllowedForAll = p_72387_1_;
    }

    /**
     * Kicks everyone with "Server closed" as reason.
     */
    public void removeAllPlayers()
    {
        for (int i = 0; i < this.players.size(); ++i)
        {
            (this.players.get(i)).connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.server_shutdown", new Object[0]));
        }
    }

    public void sendMessage(ITextComponent component, boolean isSystem)
    {
        this.server.sendMessage(component);
        ChatType chattype = isSystem ? ChatType.SYSTEM : ChatType.CHAT;
        this.sendPacketToAllPlayers(new SPacketChat(component, chattype));
    }

    /**
     * Sends the given string to every player as chat message.
     */
    public void sendMessage(ITextComponent component)
    {
        this.sendMessage(component, true);
    }

    public StatisticsManagerServer getPlayerStats(EntityPlayer playerIn)
    {
        UUID uuid = playerIn.getUniqueID();
        StatisticsManagerServer statisticsmanagerserver = uuid == null ? null : (StatisticsManagerServer)this.playerStatFiles.get(uuid);

        if (statisticsmanagerserver == null)
        {
            File file1 = new File(this.server.getWorld(0).func_72860_G().getWorldDirectory(), "stats");
            File file2 = new File(file1, uuid + ".json");

            if (!file2.exists())
            {
                File file3 = new File(file1, playerIn.func_70005_c_() + ".json");

                if (file3.exists() && file3.isFile())
                {
                    file3.renameTo(file2);
                }
            }

            statisticsmanagerserver = new StatisticsManagerServer(this.server, file2);
            statisticsmanagerserver.func_150882_a();
            this.playerStatFiles.put(uuid, statisticsmanagerserver);
        }

        return statisticsmanagerserver;
    }

    public PlayerAdvancements getPlayerAdvancements(EntityPlayerMP p_192054_1_)
    {
        UUID uuid = p_192054_1_.getUniqueID();
        PlayerAdvancements playeradvancements = this.advancements.get(uuid);

        if (playeradvancements == null)
        {
            File file1 = new File(this.server.getWorld(0).func_72860_G().getWorldDirectory(), "advancements");
            File file2 = new File(file1, uuid + ".json");
            playeradvancements = new PlayerAdvancements(this.server, file2, p_192054_1_);
            this.advancements.put(uuid, playeradvancements);
        }

        playeradvancements.setPlayer(p_192054_1_);
        return playeradvancements;
    }

    public void func_152611_a(int p_152611_1_)
    {
        this.viewDistance = p_152611_1_;

        if (this.server.worlds != null)
        {
            for (WorldServer worldserver : this.server.worlds)
            {
                if (worldserver != null)
                {
                    worldserver.func_184164_w().func_152622_a(p_152611_1_);
                    worldserver.func_73039_n().func_187252_a(p_152611_1_);
                }
            }
        }
    }

    public List<EntityPlayerMP> getPlayers()
    {
        return this.players;
    }

    /**
     * Get's the EntityPlayerMP object representing the player with the UUID.
     */
    public EntityPlayerMP getPlayerByUUID(UUID playerUUID)
    {
        return this.uuidToPlayerMap.get(playerUUID);
    }

    public boolean bypassesPlayerLimit(GameProfile profile)
    {
        return false;
    }

    public void reloadResources()
    {
        for (PlayerAdvancements playeradvancements : this.advancements.values())
        {
            playeradvancements.reload();
        }
    }
}

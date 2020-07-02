package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.util.concurrent.Futures;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.ServerRecipeBookHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerPlayServer implements INetHandlerPlayServer, ITickable
{
    private static final Logger LOGGER = LogManager.getLogger();
    public final NetworkManager netManager;
    private final MinecraftServer server;
    public EntityPlayerMP player;
    private int networkTickCount;
    private long keepAliveTime;
    private boolean keepAlivePending;
    private long keepAliveKey;

    /**
     * Incremented by 20 each time a user sends a chat message, decreased by one every tick. Non-ops kicked when over
     * 200
     */
    private int chatSpamThresholdCount;
    private int itemDropThreshold;
    private final IntHashMap<Short> pendingTransactions = new IntHashMap<Short>();
    private double firstGoodX;
    private double firstGoodY;
    private double firstGoodZ;
    private double lastGoodX;
    private double lastGoodY;
    private double lastGoodZ;
    private Entity lowestRiddenEnt;
    private double lowestRiddenX;
    private double lowestRiddenY;
    private double lowestRiddenZ;
    private double lowestRiddenX1;
    private double lowestRiddenY1;
    private double lowestRiddenZ1;
    private Vec3d targetPos;
    private int teleportId;
    private int lastPositionUpdate;
    private boolean floating;

    /**
     * Used to keep track of how the player is floating while gamerules should prevent that. Surpassing 80 ticks means
     * kick
     */
    private int floatingTickCount;
    private boolean vehicleFloating;
    private int vehicleFloatingTickCount;
    private int movePacketCounter;
    private int lastMovePacketCounter;
    private ServerRecipeBookHelper field_194309_H = new ServerRecipeBookHelper();

    public NetHandlerPlayServer(MinecraftServer server, NetworkManager networkManagerIn, EntityPlayerMP playerIn)
    {
        this.server = server;
        this.netManager = networkManagerIn;
        networkManagerIn.setNetHandler(this);
        this.player = playerIn;
        playerIn.connection = this;
    }

    public void tick()
    {
        this.captureCurrentPosition();
        this.player.playerTick();
        this.player.setPositionAndRotation(this.firstGoodX, this.firstGoodY, this.firstGoodZ, this.player.rotationYaw, this.player.rotationPitch);
        ++this.networkTickCount;
        this.lastMovePacketCounter = this.movePacketCounter;

        if (this.floating)
        {
            if (++this.floatingTickCount > 80)
            {
                LOGGER.warn("{} was kicked for floating too long!", (Object)this.player.func_70005_c_());
                this.disconnect(new TextComponentTranslation("multiplayer.disconnect.flying", new Object[0]));
                return;
            }
        }
        else
        {
            this.floating = false;
            this.floatingTickCount = 0;
        }

        this.lowestRiddenEnt = this.player.getLowestRidingEntity();

        if (this.lowestRiddenEnt != this.player && this.lowestRiddenEnt.getControllingPassenger() == this.player)
        {
            this.lowestRiddenX = this.lowestRiddenEnt.posX;
            this.lowestRiddenY = this.lowestRiddenEnt.posY;
            this.lowestRiddenZ = this.lowestRiddenEnt.posZ;
            this.lowestRiddenX1 = this.lowestRiddenEnt.posX;
            this.lowestRiddenY1 = this.lowestRiddenEnt.posY;
            this.lowestRiddenZ1 = this.lowestRiddenEnt.posZ;

            if (this.vehicleFloating && this.player.getLowestRidingEntity().getControllingPassenger() == this.player)
            {
                if (++this.vehicleFloatingTickCount > 80)
                {
                    LOGGER.warn("{} was kicked for floating a vehicle too long!", (Object)this.player.func_70005_c_());
                    this.disconnect(new TextComponentTranslation("multiplayer.disconnect.flying", new Object[0]));
                    return;
                }
            }
            else
            {
                this.vehicleFloating = false;
                this.vehicleFloatingTickCount = 0;
            }
        }
        else
        {
            this.lowestRiddenEnt = null;
            this.vehicleFloating = false;
            this.vehicleFloatingTickCount = 0;
        }

        this.server.profiler.startSection("keepAlive");
        long i = this.func_147363_d();

        if (i - this.keepAliveTime >= 15000L)
        {
            if (this.keepAlivePending)
            {
                this.disconnect(new TextComponentTranslation("disconnect.timeout", new Object[0]));
            }
            else
            {
                this.keepAlivePending = true;
                this.keepAliveTime = i;
                this.keepAliveKey = i;
                this.sendPacket(new SPacketKeepAlive(this.keepAliveKey));
            }
        }

        this.server.profiler.endSection();

        if (this.chatSpamThresholdCount > 0)
        {
            --this.chatSpamThresholdCount;
        }

        if (this.itemDropThreshold > 0)
        {
            --this.itemDropThreshold;
        }

        if (this.player.getLastActiveTime() > 0L && this.server.getMaxPlayerIdleMinutes() > 0 && MinecraftServer.func_130071_aq() - this.player.getLastActiveTime() > (long)(this.server.getMaxPlayerIdleMinutes() * 1000 * 60))
        {
            this.disconnect(new TextComponentTranslation("multiplayer.disconnect.idling", new Object[0]));
        }
    }

    private void captureCurrentPosition()
    {
        this.firstGoodX = this.player.posX;
        this.firstGoodY = this.player.posY;
        this.firstGoodZ = this.player.posZ;
        this.lastGoodX = this.player.posX;
        this.lastGoodY = this.player.posY;
        this.lastGoodZ = this.player.posZ;
    }

    public NetworkManager func_147362_b()
    {
        return this.netManager;
    }

    /**
     * Disconnect the player with a specified reason
     */
    public void disconnect(final ITextComponent textComponent)
    {
        this.netManager.func_179288_a(new SPacketDisconnect(textComponent), new GenericFutureListener < Future <? super Void >> ()
        {
            public void operationComplete(Future <? super Void > p_operationComplete_1_) throws Exception
            {
                NetHandlerPlayServer.this.netManager.closeChannel(textComponent);
            }
        });
        this.netManager.disableAutoRead();
        Futures.getUnchecked(this.server.func_152344_a(new Runnable()
        {
            public void run()
            {
                NetHandlerPlayServer.this.netManager.handleDisconnection();
            }
        }));
    }

    /**
     * Processes player movement input. Includes walking, strafing, jumping, sneaking; excludes riding and toggling
     * flying/sprinting
     */
    public void processInput(CPacketInput packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        this.player.setEntityActionState(packetIn.getStrafeSpeed(), packetIn.getForwardSpeed(), packetIn.isJumping(), packetIn.func_149617_f());
    }

    private static boolean isMovePlayerPacketInvalid(CPacketPlayer packetIn)
    {
        if (Doubles.isFinite(packetIn.getX(0.0D)) && Doubles.isFinite(packetIn.getY(0.0D)) && Doubles.isFinite(packetIn.getZ(0.0D)) && Floats.isFinite(packetIn.getPitch(0.0F)) && Floats.isFinite(packetIn.getYaw(0.0F)))
        {
            return Math.abs(packetIn.getX(0.0D)) > 3.0E7D || Math.abs(packetIn.getY(0.0D)) > 3.0E7D || Math.abs(packetIn.getZ(0.0D)) > 3.0E7D;
        }
        else
        {
            return true;
        }
    }

    private static boolean isMoveVehiclePacketInvalid(CPacketVehicleMove packetIn)
    {
        return !Doubles.isFinite(packetIn.getX()) || !Doubles.isFinite(packetIn.getY()) || !Doubles.isFinite(packetIn.getZ()) || !Floats.isFinite(packetIn.getPitch()) || !Floats.isFinite(packetIn.getYaw());
    }

    public void processVehicleMove(CPacketVehicleMove packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());

        if (isMoveVehiclePacketInvalid(packetIn))
        {
            this.disconnect(new TextComponentTranslation("multiplayer.disconnect.invalid_vehicle_movement", new Object[0]));
        }
        else
        {
            Entity entity = this.player.getLowestRidingEntity();

            if (entity != this.player && entity.getControllingPassenger() == this.player && entity == this.lowestRiddenEnt)
            {
                WorldServer worldserver = this.player.getServerWorld();
                double d0 = entity.posX;
                double d1 = entity.posY;
                double d2 = entity.posZ;
                double d3 = packetIn.getX();
                double d4 = packetIn.getY();
                double d5 = packetIn.getZ();
                float f = packetIn.getYaw();
                float f1 = packetIn.getPitch();
                double d6 = d3 - this.lowestRiddenX;
                double d7 = d4 - this.lowestRiddenY;
                double d8 = d5 - this.lowestRiddenZ;
                double d9 = entity.field_70159_w * entity.field_70159_w + entity.field_70181_x * entity.field_70181_x + entity.field_70179_y * entity.field_70179_y;
                double d10 = d6 * d6 + d7 * d7 + d8 * d8;

                if (d10 - d9 > 100.0D && (!this.server.isSinglePlayer() || !this.server.getServerOwner().equals(entity.func_70005_c_())))
                {
                    LOGGER.warn("{} (vehicle of {}) moved too quickly! {},{},{}", entity.func_70005_c_(), this.player.func_70005_c_(), Double.valueOf(d6), Double.valueOf(d7), Double.valueOf(d8));
                    this.netManager.sendPacket(new SPacketMoveVehicle(entity));
                    return;
                }

                boolean flag = worldserver.func_184144_a(entity, entity.getBoundingBox().shrink(0.0625D)).isEmpty();
                d6 = d3 - this.lowestRiddenX1;
                d7 = d4 - this.lowestRiddenY1 - 1.0E-6D;
                d8 = d5 - this.lowestRiddenZ1;
                entity.func_70091_d(MoverType.PLAYER, d6, d7, d8);
                double d11 = d7;
                d6 = d3 - entity.posX;
                d7 = d4 - entity.posY;

                if (d7 > -0.5D || d7 < 0.5D)
                {
                    d7 = 0.0D;
                }

                d8 = d5 - entity.posZ;
                d10 = d6 * d6 + d7 * d7 + d8 * d8;
                boolean flag1 = false;

                if (d10 > 0.0625D)
                {
                    flag1 = true;
                    LOGGER.warn("{} moved wrongly!", (Object)entity.func_70005_c_());
                }

                entity.setPositionAndRotation(d3, d4, d5, f, f1);
                boolean flag2 = worldserver.func_184144_a(entity, entity.getBoundingBox().shrink(0.0625D)).isEmpty();

                if (flag && (flag1 || !flag2))
                {
                    entity.setPositionAndRotation(d0, d1, d2, f, f1);
                    this.netManager.sendPacket(new SPacketMoveVehicle(entity));
                    return;
                }

                this.server.getPlayerList().func_72358_d(this.player);
                this.player.addMovementStat(this.player.posX - d0, this.player.posY - d1, this.player.posZ - d2);
                this.vehicleFloating = d11 >= -0.03125D && !this.server.isFlightAllowed() && !worldserver.checkBlockCollision(entity.getBoundingBox().grow(0.0625D).expand(0.0D, -0.55D, 0.0D));
                this.lowestRiddenX1 = entity.posX;
                this.lowestRiddenY1 = entity.posY;
                this.lowestRiddenZ1 = entity.posZ;
            }
        }
    }

    public void processConfirmTeleport(CPacketConfirmTeleport packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());

        if (packetIn.getTeleportId() == this.teleportId)
        {
            this.player.setPositionAndRotation(this.targetPos.x, this.targetPos.y, this.targetPos.z, this.player.rotationYaw, this.player.rotationPitch);

            if (this.player.isInvulnerableDimensionChange())
            {
                this.lastGoodX = this.targetPos.x;
                this.lastGoodY = this.targetPos.y;
                this.lastGoodZ = this.targetPos.z;
                this.player.clearInvulnerableDimensionChange();
            }

            this.targetPos = null;
        }
    }

    public void handleRecipeBookUpdate(CPacketRecipeInfo packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());

        if (packetIn.getPurpose() == CPacketRecipeInfo.Purpose.SHOWN)
        {
            this.player.getRecipeBook().markSeen(packetIn.func_193648_b());
        }
        else if (packetIn.getPurpose() == CPacketRecipeInfo.Purpose.SETTINGS)
        {
            this.player.getRecipeBook().setGuiOpen(packetIn.isGuiOpen());
            this.player.getRecipeBook().setFilteringCraftable(packetIn.isFilteringCraftable());
        }
    }

    public void handleSeenAdvancements(CPacketSeenAdvancements packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());

        if (packetIn.getAction() == CPacketSeenAdvancements.Action.OPENED_TAB)
        {
            ResourceLocation resourcelocation = packetIn.getTab();
            Advancement advancement = this.server.getAdvancementManager().getAdvancement(resourcelocation);

            if (advancement != null)
            {
                this.player.getAdvancements().setSelectedTab(advancement);
            }
        }
    }

    /**
     * Processes clients perspective on player positioning and/or orientation
     */
    public void processPlayer(CPacketPlayer packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());

        if (isMovePlayerPacketInvalid(packetIn))
        {
            this.disconnect(new TextComponentTranslation("multiplayer.disconnect.invalid_player_movement", new Object[0]));
        }
        else
        {
            WorldServer worldserver = this.server.getWorld(this.player.dimension);

            if (!this.player.queuedEndExit)
            {
                if (this.networkTickCount == 0)
                {
                    this.captureCurrentPosition();
                }

                if (this.targetPos != null)
                {
                    if (this.networkTickCount - this.lastPositionUpdate > 20)
                    {
                        this.lastPositionUpdate = this.networkTickCount;
                        this.setPlayerLocation(this.targetPos.x, this.targetPos.y, this.targetPos.z, this.player.rotationYaw, this.player.rotationPitch);
                    }
                }
                else
                {
                    this.lastPositionUpdate = this.networkTickCount;

                    if (this.player.isPassenger())
                    {
                        this.player.setPositionAndRotation(this.player.posX, this.player.posY, this.player.posZ, packetIn.getYaw(this.player.rotationYaw), packetIn.getPitch(this.player.rotationPitch));
                        this.server.getPlayerList().func_72358_d(this.player);
                    }
                    else
                    {
                        double d0 = this.player.posX;
                        double d1 = this.player.posY;
                        double d2 = this.player.posZ;
                        double d3 = this.player.posY;
                        double d4 = packetIn.getX(this.player.posX);
                        double d5 = packetIn.getY(this.player.posY);
                        double d6 = packetIn.getZ(this.player.posZ);
                        float f = packetIn.getYaw(this.player.rotationYaw);
                        float f1 = packetIn.getPitch(this.player.rotationPitch);
                        double d7 = d4 - this.firstGoodX;
                        double d8 = d5 - this.firstGoodY;
                        double d9 = d6 - this.firstGoodZ;
                        double d10 = this.player.field_70159_w * this.player.field_70159_w + this.player.field_70181_x * this.player.field_70181_x + this.player.field_70179_y * this.player.field_70179_y;
                        double d11 = d7 * d7 + d8 * d8 + d9 * d9;

                        if (this.player.isSleeping())
                        {
                            if (d11 > 1.0D)
                            {
                                this.setPlayerLocation(this.player.posX, this.player.posY, this.player.posZ, packetIn.getYaw(this.player.rotationYaw), packetIn.getPitch(this.player.rotationPitch));
                            }
                        }
                        else
                        {
                            ++this.movePacketCounter;
                            int i = this.movePacketCounter - this.lastMovePacketCounter;

                            if (i > 5)
                            {
                                LOGGER.debug("{} is sending move packets too frequently ({} packets since last tick)", this.player.func_70005_c_(), Integer.valueOf(i));
                                i = 1;
                            }

                            if (!this.player.isInvulnerableDimensionChange() && (!this.player.getServerWorld().getGameRules().func_82766_b("disableElytraMovementCheck") || !this.player.isElytraFlying()))
                            {
                                float f2 = this.player.isElytraFlying() ? 300.0F : 100.0F;

                                if (d11 - d10 > (double)(f2 * (float)i) && (!this.server.isSinglePlayer() || !this.server.getServerOwner().equals(this.player.func_70005_c_())))
                                {
                                    LOGGER.warn("{} moved too quickly! {},{},{}", this.player.func_70005_c_(), Double.valueOf(d7), Double.valueOf(d8), Double.valueOf(d9));
                                    this.setPlayerLocation(this.player.posX, this.player.posY, this.player.posZ, this.player.rotationYaw, this.player.rotationPitch);
                                    return;
                                }
                            }

                            boolean flag2 = worldserver.func_184144_a(this.player, this.player.getBoundingBox().shrink(0.0625D)).isEmpty();
                            d7 = d4 - this.lastGoodX;
                            d8 = d5 - this.lastGoodY;
                            d9 = d6 - this.lastGoodZ;

                            if (this.player.onGround && !packetIn.isOnGround() && d8 > 0.0D)
                            {
                                this.player.jump();
                            }

                            this.player.func_70091_d(MoverType.PLAYER, d7, d8, d9);
                            this.player.onGround = packetIn.isOnGround();
                            double d12 = d8;
                            d7 = d4 - this.player.posX;
                            d8 = d5 - this.player.posY;

                            if (d8 > -0.5D || d8 < 0.5D)
                            {
                                d8 = 0.0D;
                            }

                            d9 = d6 - this.player.posZ;
                            d11 = d7 * d7 + d8 * d8 + d9 * d9;
                            boolean flag = false;

                            if (!this.player.isInvulnerableDimensionChange() && d11 > 0.0625D && !this.player.isSleeping() && !this.player.interactionManager.isCreative() && this.player.interactionManager.getGameType() != GameType.SPECTATOR)
                            {
                                flag = true;
                                LOGGER.warn("{} moved wrongly!", (Object)this.player.func_70005_c_());
                            }

                            this.player.setPositionAndRotation(d4, d5, d6, f, f1);
                            this.player.addMovementStat(this.player.posX - d0, this.player.posY - d1, this.player.posZ - d2);

                            if (!this.player.noClip && !this.player.isSleeping())
                            {
                                boolean flag1 = worldserver.func_184144_a(this.player, this.player.getBoundingBox().shrink(0.0625D)).isEmpty();

                                if (flag2 && (flag || !flag1))
                                {
                                    this.setPlayerLocation(d0, d1, d2, f, f1);
                                    return;
                                }
                            }

                            this.floating = d12 >= -0.03125D;
                            this.floating &= !this.server.isFlightAllowed() && !this.player.abilities.allowFlying;
                            this.floating &= !this.player.isPotionActive(MobEffects.LEVITATION) && !this.player.isElytraFlying() && !worldserver.checkBlockCollision(this.player.getBoundingBox().grow(0.0625D).expand(0.0D, -0.55D, 0.0D));
                            this.player.onGround = packetIn.isOnGround();
                            this.server.getPlayerList().func_72358_d(this.player);
                            this.player.handleFalling(this.player.posY - d3, packetIn.isOnGround());
                            this.lastGoodX = this.player.posX;
                            this.lastGoodY = this.player.posY;
                            this.lastGoodZ = this.player.posZ;
                        }
                    }
                }
            }
        }
    }

    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch)
    {
        this.setPlayerLocation(x, y, z, yaw, pitch, Collections.emptySet());
    }

    /**
     * Teleports the player position to the (relative) values specified, and syncs to the client
     */
    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch, Set<SPacketPlayerPosLook.EnumFlags> relativeSet)
    {
        double d0 = relativeSet.contains(SPacketPlayerPosLook.EnumFlags.X) ? this.player.posX : 0.0D;
        double d1 = relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Y) ? this.player.posY : 0.0D;
        double d2 = relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Z) ? this.player.posZ : 0.0D;
        this.targetPos = new Vec3d(x + d0, y + d1, z + d2);
        float f = yaw;
        float f1 = pitch;

        if (relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Y_ROT))
        {
            f = yaw + this.player.rotationYaw;
        }

        if (relativeSet.contains(SPacketPlayerPosLook.EnumFlags.X_ROT))
        {
            f1 = pitch + this.player.rotationPitch;
        }

        if (++this.teleportId == Integer.MAX_VALUE)
        {
            this.teleportId = 0;
        }

        this.lastPositionUpdate = this.networkTickCount;
        this.player.setPositionAndRotation(this.targetPos.x, this.targetPos.y, this.targetPos.z, f, f1);
        this.player.connection.sendPacket(new SPacketPlayerPosLook(x, y, z, yaw, pitch, relativeSet, this.teleportId));
    }

    /**
     * Processes the player initiating/stopping digging on a particular spot, as well as a player dropping items
     */
    public void processPlayerDigging(CPacketPlayerDigging packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        WorldServer worldserver = this.server.getWorld(this.player.dimension);
        BlockPos blockpos = packetIn.getPosition();
        this.player.markPlayerActive();

        switch (packetIn.getAction())
        {
            case SWAP_HELD_ITEMS:
                if (!this.player.isSpectator())
                {
                    ItemStack itemstack = this.player.getHeldItem(EnumHand.OFF_HAND);
                    this.player.setHeldItem(EnumHand.OFF_HAND, this.player.getHeldItem(EnumHand.MAIN_HAND));
                    this.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
                }

                return;

            case DROP_ITEM:
                if (!this.player.isSpectator())
                {
                    this.player.func_71040_bB(false);
                }

                return;

            case DROP_ALL_ITEMS:
                if (!this.player.isSpectator())
                {
                    this.player.func_71040_bB(true);
                }

                return;

            case RELEASE_USE_ITEM:
                this.player.stopActiveHand();
                return;

            case START_DESTROY_BLOCK:
            case ABORT_DESTROY_BLOCK:
            case STOP_DESTROY_BLOCK:
                double d0 = this.player.posX - ((double)blockpos.getX() + 0.5D);
                double d1 = this.player.posY - ((double)blockpos.getY() + 0.5D) + 1.5D;
                double d2 = this.player.posZ - ((double)blockpos.getZ() + 0.5D);
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > 36.0D)
                {
                    return;
                }
                else if (blockpos.getY() >= this.server.getBuildLimit())
                {
                    return;
                }
                else
                {
                    if (packetIn.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK)
                    {
                        if (!this.server.isBlockProtected(worldserver, blockpos, this.player) && worldserver.getWorldBorder().contains(blockpos))
                        {
                            this.player.interactionManager.func_180784_a(blockpos, packetIn.getFacing());
                        }
                        else
                        {
                            this.player.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos));
                        }
                    }
                    else
                    {
                        if (packetIn.getAction() == CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)
                        {
                            this.player.interactionManager.func_180785_a(blockpos);
                        }
                        else if (packetIn.getAction() == CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK)
                        {
                            this.player.interactionManager.func_180238_e();
                        }

                        if (worldserver.getBlockState(blockpos).getMaterial() != Material.AIR)
                        {
                            this.player.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos));
                        }
                    }

                    return;
                }

            default:
                throw new IllegalArgumentException("Invalid player action");
        }
    }

    public void processTryUseItemOnBlock(CPacketPlayerTryUseItemOnBlock packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        WorldServer worldserver = this.server.getWorld(this.player.dimension);
        EnumHand enumhand = packetIn.getHand();
        ItemStack itemstack = this.player.getHeldItem(enumhand);
        BlockPos blockpos = packetIn.func_187023_a();
        EnumFacing enumfacing = packetIn.func_187024_b();
        this.player.markPlayerActive();

        if (blockpos.getY() < this.server.getBuildLimit() - 1 || enumfacing != EnumFacing.UP && blockpos.getY() < this.server.getBuildLimit())
        {
            if (this.targetPos == null && this.player.getDistanceSq((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D) < 64.0D && !this.server.isBlockProtected(worldserver, blockpos, this.player) && worldserver.getWorldBorder().contains(blockpos))
            {
                this.player.interactionManager.func_187251_a(this.player, worldserver, itemstack, enumhand, blockpos, enumfacing, packetIn.func_187026_d(), packetIn.func_187025_e(), packetIn.func_187020_f());
            }
        }
        else
        {
            TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("build.tooHigh", new Object[] {this.server.getBuildLimit()});
            textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
            this.player.connection.sendPacket(new SPacketChat(textcomponenttranslation, ChatType.GAME_INFO));
        }

        this.player.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos));
        this.player.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos.offset(enumfacing)));
    }

    /**
     * Called when a client is using an item while not pointing at a block, but simply using an item
     */
    public void processTryUseItem(CPacketPlayerTryUseItem packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        WorldServer worldserver = this.server.getWorld(this.player.dimension);
        EnumHand enumhand = packetIn.getHand();
        ItemStack itemstack = this.player.getHeldItem(enumhand);
        this.player.markPlayerActive();

        if (!itemstack.isEmpty())
        {
            this.player.interactionManager.processRightClick(this.player, worldserver, itemstack, enumhand);
        }
    }

    public void handleSpectate(CPacketSpectate packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());

        if (this.player.isSpectator())
        {
            Entity entity = null;

            for (WorldServer worldserver : this.server.worlds)
            {
                if (worldserver != null)
                {
                    entity = packetIn.getEntity(worldserver);

                    if (entity != null)
                    {
                        break;
                    }
                }
            }

            if (entity != null)
            {
                this.player.setSpectatingEntity(this.player);
                this.player.stopRiding();

                if (entity.world == this.player.world)
                {
                    this.player.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
                }
                else
                {
                    WorldServer worldserver1 = this.player.getServerWorld();
                    WorldServer worldserver2 = (WorldServer)entity.world;
                    this.player.dimension = entity.dimension;
                    this.sendPacket(new SPacketRespawn(this.player.dimension, worldserver1.getDifficulty(), worldserver1.getWorldInfo().getGenerator(), this.player.interactionManager.getGameType()));
                    this.server.getPlayerList().updatePermissionLevel(this.player);
                    worldserver1.func_72973_f(this.player);
                    this.player.removed = false;
                    this.player.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);

                    if (this.player.isAlive())
                    {
                        worldserver1.func_72866_a(this.player, false);
                        worldserver2.addEntity0(this.player);
                        worldserver2.func_72866_a(this.player, false);
                    }

                    this.player.setWorld(worldserver2);
                    this.server.getPlayerList().func_72375_a(this.player, worldserver1);
                    this.player.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
                    this.player.interactionManager.setWorld(worldserver2);
                    this.server.getPlayerList().sendWorldInfo(this.player, worldserver2);
                    this.server.getPlayerList().sendInventory(this.player);
                }
            }
        }
    }

    public void handleResourcePackStatus(CPacketResourcePackStatus packetIn)
    {
    }

    public void processSteerBoat(CPacketSteerBoat packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        Entity entity = this.player.getRidingEntity();

        if (entity instanceof EntityBoat)
        {
            ((EntityBoat)entity).setPaddleState(packetIn.getLeft(), packetIn.getRight());
        }
    }

    /**
     * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
     */
    public void onDisconnect(ITextComponent reason)
    {
        LOGGER.info("{} lost connection: {}", this.player.func_70005_c_(), reason.func_150260_c());
        this.server.refreshStatusNextTick();
        TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("multiplayer.player.left", new Object[] {this.player.getDisplayName()});
        textcomponenttranslation.getStyle().setColor(TextFormatting.YELLOW);
        this.server.getPlayerList().sendMessage(textcomponenttranslation);
        this.player.disconnect();
        this.server.getPlayerList().playerLoggedOut(this.player);

        if (this.server.isSinglePlayer() && this.player.func_70005_c_().equals(this.server.getServerOwner()))
        {
            LOGGER.info("Stopping singleplayer server as player logged out");
            this.server.initiateShutdown();
        }
    }

    public void sendPacket(final Packet<?> packetIn)
    {
        if (packetIn instanceof SPacketChat)
        {
            SPacketChat spacketchat = (SPacketChat)packetIn;
            EntityPlayer.EnumChatVisibility entityplayer$enumchatvisibility = this.player.getChatVisibility();

            if (entityplayer$enumchatvisibility == EntityPlayer.EnumChatVisibility.HIDDEN && spacketchat.getType() != ChatType.GAME_INFO)
            {
                return;
            }

            if (entityplayer$enumchatvisibility == EntityPlayer.EnumChatVisibility.SYSTEM && !spacketchat.isSystem())
            {
                return;
            }
        }

        try
        {
            this.netManager.sendPacket(packetIn);
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Sending packet");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Packet being sent");
            crashreportcategory.addDetail("Packet class", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    return packetIn.getClass().getCanonicalName();
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Updates which quickbar slot is selected
     */
    public void processHeldItemChange(CPacketHeldItemChange packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());

        if (packetIn.getSlotId() >= 0 && packetIn.getSlotId() < InventoryPlayer.getHotbarSize())
        {
            this.player.inventory.currentItem = packetIn.getSlotId();
            this.player.markPlayerActive();
        }
        else
        {
            LOGGER.warn("{} tried to set an invalid carried item", (Object)this.player.func_70005_c_());
        }
    }

    /**
     * Process chat messages (broadcast back to clients) and commands (executes)
     */
    public void processChatMessage(CPacketChatMessage packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());

        if (this.player.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN)
        {
            TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("chat.cannotSend", new Object[0]);
            textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
            this.sendPacket(new SPacketChat(textcomponenttranslation));
        }
        else
        {
            this.player.markPlayerActive();
            String s = packetIn.getMessage();
            s = StringUtils.normalizeSpace(s);

            for (int i = 0; i < s.length(); ++i)
            {
                if (!ChatAllowedCharacters.isAllowedCharacter(s.charAt(i)))
                {
                    this.disconnect(new TextComponentTranslation("multiplayer.disconnect.illegal_characters", new Object[0]));
                    return;
                }
            }

            if (s.startsWith("/"))
            {
                this.handleSlashCommand(s);
            }
            else
            {
                ITextComponent itextcomponent = new TextComponentTranslation("chat.type.text", new Object[] {this.player.getDisplayName(), s});
                this.server.getPlayerList().sendMessage(itextcomponent, false);
            }

            this.chatSpamThresholdCount += 20;

            if (this.chatSpamThresholdCount > 200 && !this.server.getPlayerList().canSendCommands(this.player.getGameProfile()))
            {
                this.disconnect(new TextComponentTranslation("disconnect.spam", new Object[0]));
            }
        }
    }

    /**
     * Handle commands that start with a /
     */
    private void handleSlashCommand(String command)
    {
        this.server.func_71187_D().func_71556_a(this.player, command);
    }

    public void handleAnimation(CPacketAnimation packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        this.player.markPlayerActive();
        this.player.swingArm(packetIn.getHand());
    }

    /**
     * Processes a range of action-types: sneaking, sprinting, waking from sleep, opening the inventory or setting jump
     * height of the horse the player is riding
     */
    public void processEntityAction(CPacketEntityAction packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        this.player.markPlayerActive();

        switch (packetIn.getAction())
        {
            case START_SNEAKING:
                this.player.func_70095_a(true);
                break;

            case STOP_SNEAKING:
                this.player.func_70095_a(false);
                break;

            case START_SPRINTING:
                this.player.setSprinting(true);
                break;

            case STOP_SPRINTING:
                this.player.setSprinting(false);
                break;

            case STOP_SLEEPING:
                if (this.player.isSleeping())
                {
                    this.player.func_70999_a(false, true, true);
                    this.targetPos = new Vec3d(this.player.posX, this.player.posY, this.player.posZ);
                }

                break;

            case START_RIDING_JUMP:
                if (this.player.getRidingEntity() instanceof IJumpingMount)
                {
                    IJumpingMount ijumpingmount1 = (IJumpingMount)this.player.getRidingEntity();
                    int i = packetIn.getAuxData();

                    if (ijumpingmount1.canJump() && i > 0)
                    {
                        ijumpingmount1.handleStartJump(i);
                    }
                }

                break;

            case STOP_RIDING_JUMP:
                if (this.player.getRidingEntity() instanceof IJumpingMount)
                {
                    IJumpingMount ijumpingmount = (IJumpingMount)this.player.getRidingEntity();
                    ijumpingmount.handleStopJump();
                }

                break;

            case OPEN_INVENTORY:
                if (this.player.getRidingEntity() instanceof AbstractHorse)
                {
                    ((AbstractHorse)this.player.getRidingEntity()).openGUI(this.player);
                }

                break;

            case START_FALL_FLYING:
                if (!this.player.onGround && this.player.field_70181_x < 0.0D && !this.player.isElytraFlying() && !this.player.isInWater())
                {
                    ItemStack itemstack = this.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

                    if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isUsable(itemstack))
                    {
                        this.player.func_184847_M();
                    }
                }
                else
                {
                    this.player.func_189103_N();
                }

                break;

            default:
                throw new IllegalArgumentException("Invalid client command!");
        }
    }

    /**
     * Processes left and right clicks on entities
     */
    public void processUseEntity(CPacketUseEntity packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        WorldServer worldserver = this.server.getWorld(this.player.dimension);
        Entity entity = packetIn.getEntityFromWorld(worldserver);
        this.player.markPlayerActive();

        if (entity != null)
        {
            boolean flag = this.player.canEntityBeSeen(entity);
            double d0 = 36.0D;

            if (!flag)
            {
                d0 = 9.0D;
            }

            if (this.player.getDistanceSq(entity) < d0)
            {
                if (packetIn.getAction() == CPacketUseEntity.Action.INTERACT)
                {
                    EnumHand enumhand = packetIn.getHand();
                    this.player.interactOn(entity, enumhand);
                }
                else if (packetIn.getAction() == CPacketUseEntity.Action.INTERACT_AT)
                {
                    EnumHand enumhand1 = packetIn.getHand();
                    entity.applyPlayerInteraction(this.player, packetIn.getHitVec(), enumhand1);
                }
                else if (packetIn.getAction() == CPacketUseEntity.Action.ATTACK)
                {
                    if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow || entity == this.player)
                    {
                        this.disconnect(new TextComponentTranslation("multiplayer.disconnect.invalid_entity_attacked", new Object[0]));
                        this.server.logWarning("Player " + this.player.func_70005_c_() + " tried to attack an invalid entity");
                        return;
                    }

                    this.player.attackTargetEntityWithCurrentItem(entity);
                }
            }
        }
    }

    /**
     * Processes the client status updates: respawn attempt from player, opening statistics or achievements, or
     * acquiring 'open inventory' achievement
     */
    public void processClientStatus(CPacketClientStatus packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        this.player.markPlayerActive();
        CPacketClientStatus.State cpacketclientstatus$state = packetIn.getStatus();

        switch (cpacketclientstatus$state)
        {
            case PERFORM_RESPAWN:
                if (this.player.queuedEndExit)
                {
                    this.player.queuedEndExit = false;
                    this.player = this.server.getPlayerList().recreatePlayerEntity(this.player, 0, true);
                    CriteriaTriggers.CHANGED_DIMENSION.trigger(this.player, DimensionType.THE_END, DimensionType.OVERWORLD);
                }
                else
                {
                    if (this.player.getHealth() > 0.0F)
                    {
                        return;
                    }

                    this.player = this.server.getPlayerList().recreatePlayerEntity(this.player, 0, false);

                    if (this.server.isHardcore())
                    {
                        this.player.setGameType(GameType.SPECTATOR);
                        this.player.getServerWorld().getGameRules().func_82764_b("spectatorsGenerateChunks", "false");
                    }
                }

                break;

            case REQUEST_STATS:
                this.player.getStats().sendStats(this.player);
        }
    }

    /**
     * Processes the client closing windows (container)
     */
    public void processCloseWindow(CPacketCloseWindow packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        this.player.closeContainer();
    }

    /**
     * Executes a container/inventory slot manipulation as indicated by the packet. Sends the serverside result if they
     * didn't match the indicated result and prevents further manipulation by the player until he confirms that it has
     * the same open container/inventory
     */
    public void processClickWindow(CPacketClickWindow packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        this.player.markPlayerActive();

        if (this.player.openContainer.windowId == packetIn.getWindowId() && this.player.openContainer.getCanCraft(this.player))
        {
            if (this.player.isSpectator())
            {
                NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>create();

                for (int i = 0; i < this.player.openContainer.inventorySlots.size(); ++i)
                {
                    nonnulllist.add(((Slot)this.player.openContainer.inventorySlots.get(i)).getStack());
                }

                this.player.sendAllContents(this.player.openContainer, nonnulllist);
            }
            else
            {
                ItemStack itemstack2 = this.player.openContainer.slotClick(packetIn.getSlotId(), packetIn.getUsedButton(), packetIn.getClickType(), this.player);

                if (ItemStack.areItemStacksEqual(packetIn.getClickedItem(), itemstack2))
                {
                    this.player.connection.sendPacket(new SPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
                    this.player.isChangingQuantityOnly = true;
                    this.player.openContainer.detectAndSendChanges();
                    this.player.updateHeldItem();
                    this.player.isChangingQuantityOnly = false;
                }
                else
                {
                    this.pendingTransactions.func_76038_a(this.player.openContainer.windowId, Short.valueOf(packetIn.getActionNumber()));
                    this.player.connection.sendPacket(new SPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), false));
                    this.player.openContainer.setCanCraft(this.player, false);
                    NonNullList<ItemStack> nonnulllist1 = NonNullList.<ItemStack>create();

                    for (int j = 0; j < this.player.openContainer.inventorySlots.size(); ++j)
                    {
                        ItemStack itemstack = ((Slot)this.player.openContainer.inventorySlots.get(j)).getStack();
                        ItemStack itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack;
                        nonnulllist1.add(itemstack1);
                    }

                    this.player.sendAllContents(this.player.openContainer, nonnulllist1);
                }
            }
        }
    }

    public void processPlaceRecipe(CPacketPlaceRecipe packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        this.player.markPlayerActive();

        if (!this.player.isSpectator() && this.player.openContainer.windowId == packetIn.getWindowId() && this.player.openContainer.getCanCraft(this.player))
        {
            this.field_194309_H.place(this.player, packetIn.func_194317_b(), packetIn.shouldPlaceAll());
        }
    }

    /**
     * Enchants the item identified by the packet given some convoluted conditions (matching window, which
     * should/shouldn't be in use?)
     */
    public void processEnchantItem(CPacketEnchantItem packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        this.player.markPlayerActive();

        if (this.player.openContainer.windowId == packetIn.getWindowId() && this.player.openContainer.getCanCraft(this.player) && !this.player.isSpectator())
        {
            this.player.openContainer.enchantItem(this.player, packetIn.getButton());
            this.player.openContainer.detectAndSendChanges();
        }
    }

    /**
     * Update the server with an ItemStack in a slot.
     */
    public void processCreativeInventoryAction(CPacketCreativeInventoryAction packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());

        if (this.player.interactionManager.isCreative())
        {
            boolean flag = packetIn.getSlotId() < 0;
            ItemStack itemstack = packetIn.getStack();

            if (!itemstack.isEmpty() && itemstack.hasTag() && itemstack.getTag().contains("BlockEntityTag", 10))
            {
                NBTTagCompound nbttagcompound = itemstack.getTag().getCompound("BlockEntityTag");

                if (nbttagcompound.contains("x") && nbttagcompound.contains("y") && nbttagcompound.contains("z"))
                {
                    BlockPos blockpos = new BlockPos(nbttagcompound.getInt("x"), nbttagcompound.getInt("y"), nbttagcompound.getInt("z"));
                    TileEntity tileentity = this.player.world.getTileEntity(blockpos);

                    if (tileentity != null)
                    {
                        NBTTagCompound nbttagcompound1 = tileentity.write(new NBTTagCompound());
                        nbttagcompound1.remove("x");
                        nbttagcompound1.remove("y");
                        nbttagcompound1.remove("z");
                        itemstack.setTagInfo("BlockEntityTag", nbttagcompound1);
                    }
                }
            }

            boolean flag1 = packetIn.getSlotId() >= 1 && packetIn.getSlotId() <= 45;
            boolean flag2 = itemstack.isEmpty() || itemstack.func_77960_j() >= 0 && itemstack.getCount() <= 64 && !itemstack.isEmpty();

            if (flag1 && flag2)
            {
                if (itemstack.isEmpty())
                {
                    this.player.container.putStackInSlot(packetIn.getSlotId(), ItemStack.EMPTY);
                }
                else
                {
                    this.player.container.putStackInSlot(packetIn.getSlotId(), itemstack);
                }

                this.player.container.setCanCraft(this.player, true);
            }
            else if (flag && flag2 && this.itemDropThreshold < 200)
            {
                this.itemDropThreshold += 20;
                EntityItem entityitem = this.player.dropItem(itemstack, true);

                if (entityitem != null)
                {
                    entityitem.func_70288_d();
                }
            }
        }
    }

    /**
     * Received in response to the server requesting to confirm that the client-side open container matches the servers'
     * after a mismatched container-slot manipulation. It will unlock the player's ability to manipulate the container
     * contents
     */
    public void processConfirmTransaction(CPacketConfirmTransaction packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        Short oshort = this.pendingTransactions.func_76041_a(this.player.openContainer.windowId);

        if (oshort != null && packetIn.getUid() == oshort.shortValue() && this.player.openContainer.windowId == packetIn.getWindowId() && !this.player.openContainer.getCanCraft(this.player) && !this.player.isSpectator())
        {
            this.player.openContainer.setCanCraft(this.player, true);
        }
    }

    public void processUpdateSign(CPacketUpdateSign packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        this.player.markPlayerActive();
        WorldServer worldserver = this.server.getWorld(this.player.dimension);
        BlockPos blockpos = packetIn.getPosition();

        if (worldserver.isBlockLoaded(blockpos))
        {
            IBlockState iblockstate = worldserver.getBlockState(blockpos);
            TileEntity tileentity = worldserver.getTileEntity(blockpos);

            if (!(tileentity instanceof TileEntitySign))
            {
                return;
            }

            TileEntitySign tileentitysign = (TileEntitySign)tileentity;

            if (!tileentitysign.getIsEditable() || tileentitysign.getPlayer() != this.player)
            {
                this.server.logWarning("Player " + this.player.func_70005_c_() + " just tried to change non-editable sign");
                return;
            }

            String[] astring = packetIn.getLines();

            for (int i = 0; i < astring.length; ++i)
            {
                tileentitysign.signText[i] = new TextComponentString(TextFormatting.getTextWithoutFormattingCodes(astring[i]));
            }

            tileentitysign.markDirty();
            worldserver.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);
        }
    }

    /**
     * Updates a players' ping statistics
     */
    public void processKeepAlive(CPacketKeepAlive packetIn)
    {
        if (this.keepAlivePending && packetIn.getKey() == this.keepAliveKey)
        {
            int i = (int)(this.func_147363_d() - this.keepAliveTime);
            this.player.ping = (this.player.ping * 3 + i) / 4;
            this.keepAlivePending = false;
        }
        else if (!this.player.func_70005_c_().equals(this.server.getServerOwner()))
        {
            this.disconnect(new TextComponentTranslation("disconnect.timeout", new Object[0]));
        }
    }

    private long func_147363_d()
    {
        return System.nanoTime() / 1000000L;
    }

    /**
     * Processes a player starting/stopping flying
     */
    public void processPlayerAbilities(CPacketPlayerAbilities packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        this.player.abilities.isFlying = packetIn.isFlying() && this.player.abilities.allowFlying;
    }

    public void func_147341_a(CPacketTabComplete p_147341_1_)
    {
        PacketThreadUtil.func_180031_a(p_147341_1_, this, this.player.getServerWorld());
        List<String> list = Lists.<String>newArrayList();

        for (String s : this.server.func_184104_a(this.player, p_147341_1_.func_149419_c(), p_147341_1_.func_179709_b(), p_147341_1_.func_186989_c()))
        {
            list.add(s);
        }

        this.player.connection.sendPacket(new SPacketTabComplete((String[])list.toArray(new String[list.size()])));
    }

    /**
     * Updates serverside copy of client settings: language, render distance, chat visibility, chat colours, difficulty,
     * and whether to show the cape
     */
    public void processClientSettings(CPacketClientSettings packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        this.player.handleClientSettings(packetIn);
    }

    /**
     * Synchronizes serverside and clientside book contents and signing
     */
    public void processCustomPayload(CPacketCustomPayload packetIn)
    {
        PacketThreadUtil.func_180031_a(packetIn, this, this.player.getServerWorld());
        String s = packetIn.func_149559_c();

        if ("MC|BEdit".equals(s))
        {
            PacketBuffer packetbuffer = packetIn.func_180760_b();

            try
            {
                ItemStack itemstack = packetbuffer.readItemStack();

                if (itemstack.isEmpty())
                {
                    return;
                }

                if (!ItemWritableBook.isNBTValid(itemstack.getTag()))
                {
                    throw new IOException("Invalid book tag!");
                }

                ItemStack itemstack1 = this.player.getHeldItemMainhand();

                if (itemstack1.isEmpty())
                {
                    return;
                }

                if (itemstack.getItem() == Items.WRITABLE_BOOK && itemstack.getItem() == itemstack1.getItem())
                {
                    itemstack1.setTagInfo("pages", itemstack.getTag().getList("pages", 8));
                }
            }
            catch (Exception exception6)
            {
                LOGGER.error("Couldn't handle book info", (Throwable)exception6);
            }
        }
        else if ("MC|BSign".equals(s))
        {
            PacketBuffer packetbuffer1 = packetIn.func_180760_b();

            try
            {
                ItemStack itemstack3 = packetbuffer1.readItemStack();

                if (itemstack3.isEmpty())
                {
                    return;
                }

                if (!ItemWrittenBook.validBookTagContents(itemstack3.getTag()))
                {
                    throw new IOException("Invalid book tag!");
                }

                ItemStack itemstack4 = this.player.getHeldItemMainhand();

                if (itemstack4.isEmpty())
                {
                    return;
                }

                if (itemstack3.getItem() == Items.WRITABLE_BOOK && itemstack4.getItem() == Items.WRITABLE_BOOK)
                {
                    ItemStack itemstack2 = new ItemStack(Items.WRITTEN_BOOK);
                    itemstack2.setTagInfo("author", new NBTTagString(this.player.func_70005_c_()));
                    itemstack2.setTagInfo("title", new NBTTagString(itemstack3.getTag().getString("title")));
                    NBTTagList nbttaglist = itemstack3.getTag().getList("pages", 8);

                    for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                    {
                        String s1 = nbttaglist.getString(i);
                        ITextComponent itextcomponent = new TextComponentString(s1);
                        s1 = ITextComponent.Serializer.toJson(itextcomponent);
                        nbttaglist.func_150304_a(i, new NBTTagString(s1));
                    }

                    itemstack2.setTagInfo("pages", nbttaglist);
                    this.player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemstack2);
                }
            }
            catch (Exception exception7)
            {
                LOGGER.error("Couldn't sign book", (Throwable)exception7);
            }
        }
        else if ("MC|TrSel".equals(s))
        {
            try
            {
                int k = packetIn.func_180760_b().readInt();
                Container container = this.player.openContainer;

                if (container instanceof ContainerMerchant)
                {
                    ((ContainerMerchant)container).setCurrentRecipeIndex(k);
                }
            }
            catch (Exception exception5)
            {
                LOGGER.error("Couldn't select trade", (Throwable)exception5);
            }
        }
        else if ("MC|AdvCmd".equals(s))
        {
            if (!this.server.isCommandBlockEnabled())
            {
                this.player.sendMessage(new TextComponentTranslation("advMode.notEnabled", new Object[0]));
                return;
            }

            if (!this.player.func_189808_dh())
            {
                this.player.sendMessage(new TextComponentTranslation("advMode.notAllowed", new Object[0]));
                return;
            }

            PacketBuffer packetbuffer2 = packetIn.func_180760_b();

            try
            {
                int l = packetbuffer2.readByte();
                CommandBlockBaseLogic commandblockbaselogic1 = null;

                if (l == 0)
                {
                    TileEntity tileentity = this.player.world.getTileEntity(new BlockPos(packetbuffer2.readInt(), packetbuffer2.readInt(), packetbuffer2.readInt()));

                    if (tileentity instanceof TileEntityCommandBlock)
                    {
                        commandblockbaselogic1 = ((TileEntityCommandBlock)tileentity).getCommandBlockLogic();
                    }
                }
                else if (l == 1)
                {
                    Entity entity = this.player.world.getEntityByID(packetbuffer2.readInt());

                    if (entity instanceof EntityMinecartCommandBlock)
                    {
                        commandblockbaselogic1 = ((EntityMinecartCommandBlock)entity).getCommandBlockLogic();
                    }
                }

                String s6 = packetbuffer2.readString(packetbuffer2.readableBytes());
                boolean flag2 = packetbuffer2.readBoolean();

                if (commandblockbaselogic1 != null)
                {
                    commandblockbaselogic1.setCommand(s6);
                    commandblockbaselogic1.setTrackOutput(flag2);

                    if (!flag2)
                    {
                        commandblockbaselogic1.setLastOutput((ITextComponent)null);
                    }

                    commandblockbaselogic1.updateCommand();
                    this.player.sendMessage(new TextComponentTranslation("advMode.setCommand.success", new Object[] {s6}));
                }
            }
            catch (Exception exception4)
            {
                LOGGER.error("Couldn't set command block", (Throwable)exception4);
            }
        }
        else if ("MC|AutoCmd".equals(s))
        {
            if (!this.server.isCommandBlockEnabled())
            {
                this.player.sendMessage(new TextComponentTranslation("advMode.notEnabled", new Object[0]));
                return;
            }

            if (!this.player.func_189808_dh())
            {
                this.player.sendMessage(new TextComponentTranslation("advMode.notAllowed", new Object[0]));
                return;
            }

            PacketBuffer packetbuffer3 = packetIn.func_180760_b();

            try
            {
                CommandBlockBaseLogic commandblockbaselogic = null;
                TileEntityCommandBlock tileentitycommandblock = null;
                BlockPos blockpos1 = new BlockPos(packetbuffer3.readInt(), packetbuffer3.readInt(), packetbuffer3.readInt());
                TileEntity tileentity2 = this.player.world.getTileEntity(blockpos1);

                if (tileentity2 instanceof TileEntityCommandBlock)
                {
                    tileentitycommandblock = (TileEntityCommandBlock)tileentity2;
                    commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();
                }

                String s7 = packetbuffer3.readString(packetbuffer3.readableBytes());
                boolean flag3 = packetbuffer3.readBoolean();
                TileEntityCommandBlock.Mode tileentitycommandblock$mode = TileEntityCommandBlock.Mode.valueOf(packetbuffer3.readString(16));
                boolean flag = packetbuffer3.readBoolean();
                boolean flag1 = packetbuffer3.readBoolean();

                if (commandblockbaselogic != null)
                {
                    EnumFacing enumfacing = (EnumFacing)this.player.world.getBlockState(blockpos1).get(BlockCommandBlock.FACING);

                    switch (tileentitycommandblock$mode)
                    {
                        case SEQUENCE:
                            IBlockState iblockstate3 = Blocks.CHAIN_COMMAND_BLOCK.getDefaultState();
                            this.player.world.setBlockState(blockpos1, iblockstate3.func_177226_a(BlockCommandBlock.FACING, enumfacing).func_177226_a(BlockCommandBlock.CONDITIONAL, Boolean.valueOf(flag)), 2);
                            break;

                        case AUTO:
                            IBlockState iblockstate2 = Blocks.REPEATING_COMMAND_BLOCK.getDefaultState();
                            this.player.world.setBlockState(blockpos1, iblockstate2.func_177226_a(BlockCommandBlock.FACING, enumfacing).func_177226_a(BlockCommandBlock.CONDITIONAL, Boolean.valueOf(flag)), 2);
                            break;

                        case REDSTONE:
                            IBlockState iblockstate = Blocks.COMMAND_BLOCK.getDefaultState();
                            this.player.world.setBlockState(blockpos1, iblockstate.func_177226_a(BlockCommandBlock.FACING, enumfacing).func_177226_a(BlockCommandBlock.CONDITIONAL, Boolean.valueOf(flag)), 2);
                    }

                    tileentity2.validate();
                    this.player.world.setTileEntity(blockpos1, tileentity2);
                    commandblockbaselogic.setCommand(s7);
                    commandblockbaselogic.setTrackOutput(flag3);

                    if (!flag3)
                    {
                        commandblockbaselogic.setLastOutput((ITextComponent)null);
                    }

                    tileentitycommandblock.setAuto(flag1);
                    commandblockbaselogic.updateCommand();

                    if (!net.minecraft.util.StringUtils.isNullOrEmpty(s7))
                    {
                        this.player.sendMessage(new TextComponentTranslation("advMode.setCommand.success", new Object[] {s7}));
                    }
                }
            }
            catch (Exception exception3)
            {
                LOGGER.error("Couldn't set command block", (Throwable)exception3);
            }
        }
        else if ("MC|Beacon".equals(s))
        {
            if (this.player.openContainer instanceof ContainerBeacon)
            {
                try
                {
                    PacketBuffer packetbuffer4 = packetIn.func_180760_b();
                    int i1 = packetbuffer4.readInt();
                    int k1 = packetbuffer4.readInt();
                    ContainerBeacon containerbeacon = (ContainerBeacon)this.player.openContainer;
                    Slot slot = containerbeacon.getSlot(0);

                    if (slot.getHasStack())
                    {
                        slot.decrStackSize(1);
                        IInventory iinventory = containerbeacon.func_180611_e();
                        iinventory.func_174885_b(1, i1);
                        iinventory.func_174885_b(2, k1);
                        iinventory.markDirty();
                    }
                }
                catch (Exception exception2)
                {
                    LOGGER.error("Couldn't set beacon", (Throwable)exception2);
                }
            }
        }
        else if ("MC|ItemName".equals(s))
        {
            if (this.player.openContainer instanceof ContainerRepair)
            {
                ContainerRepair containerrepair = (ContainerRepair)this.player.openContainer;

                if (packetIn.func_180760_b() != null && packetIn.func_180760_b().readableBytes() >= 1)
                {
                    String s5 = ChatAllowedCharacters.filterAllowedCharacters(packetIn.func_180760_b().readString(32767));

                    if (s5.length() <= 35)
                    {
                        containerrepair.updateItemName(s5);
                    }
                }
                else
                {
                    containerrepair.updateItemName("");
                }
            }
        }
        else if ("MC|Struct".equals(s))
        {
            if (!this.player.func_189808_dh())
            {
                return;
            }

            PacketBuffer packetbuffer5 = packetIn.func_180760_b();

            try
            {
                BlockPos blockpos = new BlockPos(packetbuffer5.readInt(), packetbuffer5.readInt(), packetbuffer5.readInt());
                IBlockState iblockstate1 = this.player.world.getBlockState(blockpos);
                TileEntity tileentity1 = this.player.world.getTileEntity(blockpos);

                if (tileentity1 instanceof TileEntityStructure)
                {
                    TileEntityStructure tileentitystructure = (TileEntityStructure)tileentity1;
                    int l1 = packetbuffer5.readByte();
                    String s8 = packetbuffer5.readString(32);
                    tileentitystructure.setMode(TileEntityStructure.Mode.valueOf(s8));
                    tileentitystructure.setName(packetbuffer5.readString(64));
                    int i2 = MathHelper.clamp(packetbuffer5.readInt(), -32, 32);
                    int j2 = MathHelper.clamp(packetbuffer5.readInt(), -32, 32);
                    int k2 = MathHelper.clamp(packetbuffer5.readInt(), -32, 32);
                    tileentitystructure.setPosition(new BlockPos(i2, j2, k2));
                    int l2 = MathHelper.clamp(packetbuffer5.readInt(), 0, 32);
                    int i3 = MathHelper.clamp(packetbuffer5.readInt(), 0, 32);
                    int j = MathHelper.clamp(packetbuffer5.readInt(), 0, 32);
                    tileentitystructure.setSize(new BlockPos(l2, i3, j));
                    String s2 = packetbuffer5.readString(32);
                    tileentitystructure.setMirror(Mirror.valueOf(s2));
                    String s3 = packetbuffer5.readString(32);
                    tileentitystructure.setRotation(Rotation.valueOf(s3));
                    tileentitystructure.setMetadata(packetbuffer5.readString(128));
                    tileentitystructure.setIgnoresEntities(packetbuffer5.readBoolean());
                    tileentitystructure.setShowAir(packetbuffer5.readBoolean());
                    tileentitystructure.setShowBoundingBox(packetbuffer5.readBoolean());
                    tileentitystructure.setIntegrity(MathHelper.clamp(packetbuffer5.readFloat(), 0.0F, 1.0F));
                    tileentitystructure.setSeed(packetbuffer5.readVarLong());
                    String s4 = tileentitystructure.getName();

                    if (l1 == 2)
                    {
                        if (tileentitystructure.save())
                        {
                            this.player.sendStatusMessage(new TextComponentTranslation("structure_block.save_success", new Object[] {s4}), false);
                        }
                        else
                        {
                            this.player.sendStatusMessage(new TextComponentTranslation("structure_block.save_failure", new Object[] {s4}), false);
                        }
                    }
                    else if (l1 == 3)
                    {
                        if (!tileentitystructure.isStructureLoadable())
                        {
                            this.player.sendStatusMessage(new TextComponentTranslation("structure_block.load_not_found", new Object[] {s4}), false);
                        }
                        else if (tileentitystructure.load())
                        {
                            this.player.sendStatusMessage(new TextComponentTranslation("structure_block.load_success", new Object[] {s4}), false);
                        }
                        else
                        {
                            this.player.sendStatusMessage(new TextComponentTranslation("structure_block.load_prepare", new Object[] {s4}), false);
                        }
                    }
                    else if (l1 == 4)
                    {
                        if (tileentitystructure.detectSize())
                        {
                            this.player.sendStatusMessage(new TextComponentTranslation("structure_block.size_success", new Object[] {s4}), false);
                        }
                        else
                        {
                            this.player.sendStatusMessage(new TextComponentTranslation("structure_block.size_failure", new Object[0]), false);
                        }
                    }

                    tileentitystructure.markDirty();
                    this.player.world.notifyBlockUpdate(blockpos, iblockstate1, iblockstate1, 3);
                }
            }
            catch (Exception exception1)
            {
                LOGGER.error("Couldn't set structure block", (Throwable)exception1);
            }
        }
        else if ("MC|PickItem".equals(s))
        {
            PacketBuffer packetbuffer6 = packetIn.func_180760_b();

            try
            {
                int j1 = packetbuffer6.readVarInt();
                this.player.inventory.pickItem(j1);
                this.player.connection.sendPacket(new SPacketSetSlot(-2, this.player.inventory.currentItem, this.player.inventory.getStackInSlot(this.player.inventory.currentItem)));
                this.player.connection.sendPacket(new SPacketSetSlot(-2, j1, this.player.inventory.getStackInSlot(j1)));
                this.player.connection.sendPacket(new SPacketHeldItemChange(this.player.inventory.currentItem));
            }
            catch (Exception exception)
            {
                LOGGER.error("Couldn't pick item", (Throwable)exception);
            }
        }
    }
}
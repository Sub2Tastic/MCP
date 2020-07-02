package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartMobSpawner;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RenderManager
{
    private final Map < Class <? extends Entity > , Render <? extends Entity >> renderers = Maps. < Class <? extends Entity > , Render <? extends Entity >> newHashMap();
    private final Map<String, RenderPlayer> skinMap = Maps.<String, RenderPlayer>newHashMap();
    private final RenderPlayer playerRenderer;
    private FontRenderer textRenderer;
    private double field_78725_b;
    private double field_78726_c;
    private double field_78723_d;
    public TextureManager textureManager;
    public World world;
    public Entity field_78734_h;
    public Entity pointedEntity;
    public float field_78735_i;
    public float field_78732_j;
    public GameSettings options;
    public double field_78730_l;
    public double field_78731_m;
    public double field_78728_n;
    private boolean field_178639_r;
    private boolean renderShadow = true;
    private boolean debugBoundingBox;

    public RenderManager(TextureManager p_i46180_1_, RenderItem p_i46180_2_)
    {
        this.textureManager = p_i46180_1_;
        this.renderers.put(EntityCaveSpider.class, new RenderCaveSpider(this));
        this.renderers.put(EntitySpider.class, new RenderSpider(this));
        this.renderers.put(EntityPig.class, new RenderPig(this));
        this.renderers.put(EntitySheep.class, new RenderSheep(this));
        this.renderers.put(EntityCow.class, new RenderCow(this));
        this.renderers.put(EntityMooshroom.class, new RenderMooshroom(this));
        this.renderers.put(EntityWolf.class, new RenderWolf(this));
        this.renderers.put(EntityChicken.class, new RenderChicken(this));
        this.renderers.put(EntityOcelot.class, new RenderOcelot(this));
        this.renderers.put(EntityRabbit.class, new RenderRabbit(this));
        this.renderers.put(EntityParrot.class, new RenderParrot(this));
        this.renderers.put(EntitySilverfish.class, new RenderSilverfish(this));
        this.renderers.put(EntityEndermite.class, new RenderEndermite(this));
        this.renderers.put(EntityCreeper.class, new RenderCreeper(this));
        this.renderers.put(EntityEnderman.class, new RenderEnderman(this));
        this.renderers.put(EntitySnowman.class, new RenderSnowMan(this));
        this.renderers.put(EntitySkeleton.class, new RenderSkeleton(this));
        this.renderers.put(EntityWitherSkeleton.class, new RenderWitherSkeleton(this));
        this.renderers.put(EntityStray.class, new RenderStray(this));
        this.renderers.put(EntityWitch.class, new RenderWitch(this));
        this.renderers.put(EntityBlaze.class, new RenderBlaze(this));
        this.renderers.put(EntityPigZombie.class, new RenderPigZombie(this));
        this.renderers.put(EntityZombie.class, new RenderZombie(this));
        this.renderers.put(EntityZombieVillager.class, new RenderZombieVillager(this));
        this.renderers.put(EntityHusk.class, new RenderHusk(this));
        this.renderers.put(EntitySlime.class, new RenderSlime(this));
        this.renderers.put(EntityMagmaCube.class, new RenderMagmaCube(this));
        this.renderers.put(EntityGiantZombie.class, new RenderGiantZombie(this, 6.0F));
        this.renderers.put(EntityGhast.class, new RenderGhast(this));
        this.renderers.put(EntitySquid.class, new RenderSquid(this));
        this.renderers.put(EntityVillager.class, new RenderVillager(this));
        this.renderers.put(EntityIronGolem.class, new RenderIronGolem(this));
        this.renderers.put(EntityBat.class, new RenderBat(this));
        this.renderers.put(EntityGuardian.class, new RenderGuardian(this));
        this.renderers.put(EntityElderGuardian.class, new RenderElderGuardian(this));
        this.renderers.put(EntityShulker.class, new RenderShulker(this));
        this.renderers.put(EntityPolarBear.class, new RenderPolarBear(this));
        this.renderers.put(EntityEvoker.class, new RenderEvoker(this));
        this.renderers.put(EntityVindicator.class, new RenderVindicator(this));
        this.renderers.put(EntityVex.class, new RenderVex(this));
        this.renderers.put(EntityIllusionIllager.class, new RenderIllusionIllager(this));
        this.renderers.put(EntityDragon.class, new RenderDragon(this));
        this.renderers.put(EntityEnderCrystal.class, new RenderEnderCrystal(this));
        this.renderers.put(EntityWither.class, new RenderWither(this));
        this.renderers.put(Entity.class, new RenderEntity(this));
        this.renderers.put(EntityPainting.class, new RenderPainting(this));
        this.renderers.put(EntityItemFrame.class, new RenderItemFrame(this, p_i46180_2_));
        this.renderers.put(EntityLeashKnot.class, new RenderLeashKnot(this));
        this.renderers.put(EntityTippedArrow.class, new RenderTippedArrow(this));
        this.renderers.put(EntitySpectralArrow.class, new RenderSpectralArrow(this));
        this.renderers.put(EntitySnowball.class, new RenderSnowball(this, Items.SNOWBALL, p_i46180_2_));
        this.renderers.put(EntityEnderPearl.class, new RenderSnowball(this, Items.ENDER_PEARL, p_i46180_2_));
        this.renderers.put(EntityEnderEye.class, new RenderSnowball(this, Items.ENDER_EYE, p_i46180_2_));
        this.renderers.put(EntityEgg.class, new RenderSnowball(this, Items.EGG, p_i46180_2_));
        this.renderers.put(EntityPotion.class, new RenderPotion(this, p_i46180_2_));
        this.renderers.put(EntityExpBottle.class, new RenderSnowball(this, Items.EXPERIENCE_BOTTLE, p_i46180_2_));
        this.renderers.put(EntityFireworkRocket.class, new RenderSnowball(this, Items.field_151152_bP, p_i46180_2_));
        this.renderers.put(EntityLargeFireball.class, new RenderFireball(this, 2.0F));
        this.renderers.put(EntitySmallFireball.class, new RenderFireball(this, 0.5F));
        this.renderers.put(EntityDragonFireball.class, new RenderDragonFireball(this));
        this.renderers.put(EntityWitherSkull.class, new RenderWitherSkull(this));
        this.renderers.put(EntityShulkerBullet.class, new RenderShulkerBullet(this));
        this.renderers.put(EntityItem.class, new RenderEntityItem(this, p_i46180_2_));
        this.renderers.put(EntityXPOrb.class, new RenderXPOrb(this));
        this.renderers.put(EntityTNTPrimed.class, new RenderTNTPrimed(this));
        this.renderers.put(EntityFallingBlock.class, new RenderFallingBlock(this));
        this.renderers.put(EntityArmorStand.class, new RenderArmorStand(this));
        this.renderers.put(EntityEvokerFangs.class, new RenderEvokerFangs(this));
        this.renderers.put(EntityMinecartTNT.class, new RenderTntMinecart(this));
        this.renderers.put(EntityMinecartMobSpawner.class, new RenderMinecartMobSpawner(this));
        this.renderers.put(EntityMinecart.class, new RenderMinecart(this));
        this.renderers.put(EntityBoat.class, new RenderBoat(this));
        this.renderers.put(EntityFishHook.class, new RenderFish(this));
        this.renderers.put(EntityAreaEffectCloud.class, new RenderAreaEffectCloud(this));
        this.renderers.put(EntityHorse.class, new RenderHorse(this));
        this.renderers.put(EntitySkeletonHorse.class, new RenderAbstractHorse(this));
        this.renderers.put(EntityZombieHorse.class, new RenderAbstractHorse(this));
        this.renderers.put(EntityMule.class, new RenderAbstractHorse(this, 0.92F));
        this.renderers.put(EntityDonkey.class, new RenderAbstractHorse(this, 0.87F));
        this.renderers.put(EntityLlama.class, new RenderLlama(this));
        this.renderers.put(EntityLlamaSpit.class, new RenderLlamaSpit(this));
        this.renderers.put(EntityLightningBolt.class, new RenderLightningBolt(this));
        this.playerRenderer = new RenderPlayer(this);
        this.skinMap.put("default", this.playerRenderer);
        this.skinMap.put("slim", new RenderPlayer(this, true));
    }

    public void func_178628_a(double p_178628_1_, double p_178628_3_, double p_178628_5_)
    {
        this.field_78725_b = p_178628_1_;
        this.field_78726_c = p_178628_3_;
        this.field_78723_d = p_178628_5_;
    }

    public <T extends Entity> Render<T> func_78715_a(Class <? extends Entity > p_78715_1_)
    {
        Render<T> render = (Render)this.renderers.get(p_78715_1_);

        if (render == null && p_78715_1_ != Entity.class)
        {
            render = this.func_78715_a((Class <? extends Entity >)p_78715_1_.getSuperclass());
            this.renderers.put(p_78715_1_, render);
        }

        return render;
    }

    @Nullable
    public <T extends Entity> Render<T> getRenderer(Entity entityIn)
    {
        if (entityIn instanceof AbstractClientPlayer)
        {
            String s = ((AbstractClientPlayer)entityIn).getSkinType();
            RenderPlayer renderplayer = this.skinMap.get(s);
            return (Render<T>)(renderplayer != null ? renderplayer : this.playerRenderer);
        }
        else
        {
            return this.<T>func_78715_a(entityIn.getClass());
        }
    }

    public void func_180597_a(World p_180597_1_, FontRenderer p_180597_2_, Entity p_180597_3_, Entity p_180597_4_, GameSettings p_180597_5_, float p_180597_6_)
    {
        this.world = p_180597_1_;
        this.options = p_180597_5_;
        this.field_78734_h = p_180597_3_;
        this.pointedEntity = p_180597_4_;
        this.textRenderer = p_180597_2_;

        if (p_180597_3_ instanceof EntityLivingBase && ((EntityLivingBase)p_180597_3_).isSleeping())
        {
            IBlockState iblockstate = p_180597_1_.getBlockState(new BlockPos(p_180597_3_));
            Block block = iblockstate.getBlock();

            if (block == Blocks.field_150324_C)
            {
                int i = ((EnumFacing)iblockstate.get(BlockBed.HORIZONTAL_FACING)).getHorizontalIndex();
                this.field_78735_i = (float)(i * 90 + 180);
                this.field_78732_j = 0.0F;
            }
        }
        else
        {
            this.field_78735_i = p_180597_3_.prevRotationYaw + (p_180597_3_.rotationYaw - p_180597_3_.prevRotationYaw) * p_180597_6_;
            this.field_78732_j = p_180597_3_.prevRotationPitch + (p_180597_3_.rotationPitch - p_180597_3_.prevRotationPitch) * p_180597_6_;
        }

        if (p_180597_5_.thirdPersonView == 2)
        {
            this.field_78735_i += 180.0F;
        }

        this.field_78730_l = p_180597_3_.lastTickPosX + (p_180597_3_.posX - p_180597_3_.lastTickPosX) * (double)p_180597_6_;
        this.field_78731_m = p_180597_3_.lastTickPosY + (p_180597_3_.posY - p_180597_3_.lastTickPosY) * (double)p_180597_6_;
        this.field_78728_n = p_180597_3_.lastTickPosZ + (p_180597_3_.posZ - p_180597_3_.lastTickPosZ) * (double)p_180597_6_;
    }

    public void func_178631_a(float p_178631_1_)
    {
        this.field_78735_i = p_178631_1_;
    }

    public boolean func_178627_a()
    {
        return this.renderShadow;
    }

    public void setRenderShadow(boolean renderShadowIn)
    {
        this.renderShadow = renderShadowIn;
    }

    public void setDebugBoundingBox(boolean debugBoundingBoxIn)
    {
        this.debugBoundingBox = debugBoundingBoxIn;
    }

    public boolean isDebugBoundingBox()
    {
        return this.debugBoundingBox;
    }

    public boolean func_188390_b(Entity p_188390_1_)
    {
        return this.getRenderer(p_188390_1_).func_188295_H_();
    }

    public boolean func_178635_a(Entity p_178635_1_, ICamera p_178635_2_, double p_178635_3_, double p_178635_5_, double p_178635_7_)
    {
        Render<Entity> render = this.<Entity>getRenderer(p_178635_1_);
        return render != null && render.func_177071_a(p_178635_1_, p_178635_2_, p_178635_3_, p_178635_5_, p_178635_7_);
    }

    public void func_188388_a(Entity p_188388_1_, float p_188388_2_, boolean p_188388_3_)
    {
        if (p_188388_1_.ticksExisted == 0)
        {
            p_188388_1_.lastTickPosX = p_188388_1_.posX;
            p_188388_1_.lastTickPosY = p_188388_1_.posY;
            p_188388_1_.lastTickPosZ = p_188388_1_.posZ;
        }

        double d0 = p_188388_1_.lastTickPosX + (p_188388_1_.posX - p_188388_1_.lastTickPosX) * (double)p_188388_2_;
        double d1 = p_188388_1_.lastTickPosY + (p_188388_1_.posY - p_188388_1_.lastTickPosY) * (double)p_188388_2_;
        double d2 = p_188388_1_.lastTickPosZ + (p_188388_1_.posZ - p_188388_1_.lastTickPosZ) * (double)p_188388_2_;
        float f = p_188388_1_.prevRotationYaw + (p_188388_1_.rotationYaw - p_188388_1_.prevRotationYaw) * p_188388_2_;
        int i = p_188388_1_.func_70070_b();

        if (p_188388_1_.isBurning())
        {
            i = 15728880;
        }

        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)j, (float)k);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        this.func_188391_a(p_188388_1_, d0 - this.field_78725_b, d1 - this.field_78726_c, d2 - this.field_78723_d, f, p_188388_2_, p_188388_3_);
    }

    public void func_188391_a(Entity p_188391_1_, double p_188391_2_, double p_188391_4_, double p_188391_6_, float p_188391_8_, float p_188391_9_, boolean p_188391_10_)
    {
        Render<Entity> render = null;

        try
        {
            render = this.<Entity>getRenderer(p_188391_1_);

            if (render != null && this.textureManager != null)
            {
                try
                {
                    render.func_188297_a(this.field_178639_r);
                    render.func_76986_a(p_188391_1_, p_188391_2_, p_188391_4_, p_188391_6_, p_188391_8_, p_188391_9_);
                }
                catch (Throwable throwable1)
                {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Rendering entity in world"));
                }

                try
                {
                    if (!this.field_178639_r)
                    {
                        render.func_76979_b(p_188391_1_, p_188391_2_, p_188391_4_, p_188391_6_, p_188391_8_, p_188391_9_);
                    }
                }
                catch (Throwable throwable2)
                {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable2, "Post-rendering entity in world"));
                }

                if (this.debugBoundingBox && !p_188391_1_.isInvisible() && !p_188391_10_ && !Minecraft.getInstance().isReducedDebug())
                {
                    try
                    {
                        this.func_85094_b(p_188391_1_, p_188391_2_, p_188391_4_, p_188391_6_, p_188391_8_, p_188391_9_);
                    }
                    catch (Throwable throwable)
                    {
                        throw new ReportedException(CrashReport.makeCrashReport(throwable, "Rendering entity hitbox in world"));
                    }
                }
            }
        }
        catch (Throwable throwable3)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable3, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
            p_188391_1_.fillCrashReport(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
            crashreportcategory1.addDetail("Assigned renderer", render);
            crashreportcategory1.addDetail("Location", CrashReportCategory.getCoordinateInfo(p_188391_2_, p_188391_4_, p_188391_6_));
            crashreportcategory1.addDetail("Rotation", Float.valueOf(p_188391_8_));
            crashreportcategory1.addDetail("Delta", Float.valueOf(p_188391_9_));
            throw new ReportedException(crashreport);
        }
    }

    public void func_188389_a(Entity p_188389_1_, float p_188389_2_)
    {
        if (p_188389_1_.ticksExisted == 0)
        {
            p_188389_1_.lastTickPosX = p_188389_1_.posX;
            p_188389_1_.lastTickPosY = p_188389_1_.posY;
            p_188389_1_.lastTickPosZ = p_188389_1_.posZ;
        }

        double d0 = p_188389_1_.lastTickPosX + (p_188389_1_.posX - p_188389_1_.lastTickPosX) * (double)p_188389_2_;
        double d1 = p_188389_1_.lastTickPosY + (p_188389_1_.posY - p_188389_1_.lastTickPosY) * (double)p_188389_2_;
        double d2 = p_188389_1_.lastTickPosZ + (p_188389_1_.posZ - p_188389_1_.lastTickPosZ) * (double)p_188389_2_;
        float f = p_188389_1_.prevRotationYaw + (p_188389_1_.rotationYaw - p_188389_1_.prevRotationYaw) * p_188389_2_;
        int i = p_188389_1_.func_70070_b();

        if (p_188389_1_.isBurning())
        {
            i = 15728880;
        }

        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)j, (float)k);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        Render<Entity> render = this.<Entity>getRenderer(p_188389_1_);

        if (render != null && this.textureManager != null)
        {
            render.func_188300_b(p_188389_1_, d0 - this.field_78725_b, d1 - this.field_78726_c, d2 - this.field_78723_d, f, p_188389_2_);
        }
    }

    private void func_85094_b(Entity p_85094_1_, double p_85094_2_, double p_85094_4_, double p_85094_6_, float p_85094_8_, float p_85094_9_)
    {
        GlStateManager.func_179132_a(false);
        GlStateManager.func_179090_x();
        GlStateManager.func_179140_f();
        GlStateManager.func_179129_p();
        GlStateManager.func_179084_k();
        float f = p_85094_1_.field_70130_N / 2.0F;
        AxisAlignedBB axisalignedbb = p_85094_1_.getBoundingBox();
        RenderGlobal.func_189694_a(axisalignedbb.minX - p_85094_1_.posX + p_85094_2_, axisalignedbb.minY - p_85094_1_.posY + p_85094_4_, axisalignedbb.minZ - p_85094_1_.posZ + p_85094_6_, axisalignedbb.maxX - p_85094_1_.posX + p_85094_2_, axisalignedbb.maxY - p_85094_1_.posY + p_85094_4_, axisalignedbb.maxZ - p_85094_1_.posZ + p_85094_6_, 1.0F, 1.0F, 1.0F, 1.0F);
        Entity[] aentity = p_85094_1_.func_70021_al();

        if (aentity != null)
        {
            for (Entity entity : aentity)
            {
                double d0 = (entity.posX - entity.prevPosX) * (double)p_85094_9_;
                double d1 = (entity.posY - entity.prevPosY) * (double)p_85094_9_;
                double d2 = (entity.posZ - entity.prevPosZ) * (double)p_85094_9_;
                AxisAlignedBB axisalignedbb1 = entity.getBoundingBox();
                RenderGlobal.func_189694_a(axisalignedbb1.minX - this.field_78725_b + d0, axisalignedbb1.minY - this.field_78726_c + d1, axisalignedbb1.minZ - this.field_78723_d + d2, axisalignedbb1.maxX - this.field_78725_b + d0, axisalignedbb1.maxY - this.field_78726_c + d1, axisalignedbb1.maxZ - this.field_78723_d + d2, 0.25F, 1.0F, 0.0F, 1.0F);
            }
        }

        if (p_85094_1_ instanceof EntityLivingBase)
        {
            float f1 = 0.01F;
            RenderGlobal.func_189694_a(p_85094_2_ - (double)f, p_85094_4_ + (double)p_85094_1_.getEyeHeight() - 0.009999999776482582D, p_85094_6_ - (double)f, p_85094_2_ + (double)f, p_85094_4_ + (double)p_85094_1_.getEyeHeight() + 0.009999999776482582D, p_85094_6_ + (double)f, 1.0F, 0.0F, 0.0F, 1.0F);
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        Vec3d vec3d = p_85094_1_.getLook(p_85094_9_);
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.func_181662_b(p_85094_2_, p_85094_4_ + (double)p_85094_1_.getEyeHeight(), p_85094_6_).func_181669_b(0, 0, 255, 255).endVertex();
        bufferbuilder.func_181662_b(p_85094_2_ + vec3d.x * 2.0D, p_85094_4_ + (double)p_85094_1_.getEyeHeight() + vec3d.y * 2.0D, p_85094_6_ + vec3d.z * 2.0D).func_181669_b(0, 0, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.func_179098_w();
        GlStateManager.func_179145_e();
        GlStateManager.func_179089_o();
        GlStateManager.func_179084_k();
        GlStateManager.func_179132_a(true);
    }

    /**
     * World sets this RenderManager's worldObj to the world provided
     */
    public void setWorld(@Nullable World worldIn)
    {
        this.world = worldIn;

        if (worldIn == null)
        {
            this.field_78734_h = null;
        }
    }

    public double getDistanceToCamera(double x, double y, double z)
    {
        double d0 = x - this.field_78730_l;
        double d1 = y - this.field_78731_m;
        double d2 = z - this.field_78728_n;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /**
     * Returns the font renderer
     */
    public FontRenderer getFontRenderer()
    {
        return this.textRenderer;
    }

    public void func_178632_c(boolean p_178632_1_)
    {
        this.field_178639_r = p_178632_1_;
    }
}

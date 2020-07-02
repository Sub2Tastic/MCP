package net.minecraft.client.particle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleManager
{
    private static final ResourceLocation field_110737_b = new ResourceLocation("textures/particle/particles.png");
    protected World world;
    private final ArrayDeque<Particle>[][] byType = new ArrayDeque[4][];
    private final Queue<ParticleEmitter> particleEmitters = Queues.<ParticleEmitter>newArrayDeque();
    private final TextureManager renderer;
    private final Random rand = new Random();
    private final Map<Integer, IParticleFactory> factories = Maps.<Integer, IParticleFactory>newHashMap();
    private final Queue<Particle> queue = Queues.<Particle>newArrayDeque();

    public ParticleManager(World worldIn, TextureManager rendererIn)
    {
        this.world = worldIn;
        this.renderer = rendererIn;

        for (int i = 0; i < 4; ++i)
        {
            this.byType[i] = new ArrayDeque[2];

            for (int j = 0; j < 2; ++j)
            {
                this.byType[i][j] = Queues.newArrayDeque();
            }
        }

        this.registerFactories();
    }

    private void registerFactories()
    {
        this.func_178929_a(EnumParticleTypes.EXPLOSION_NORMAL.func_179348_c(), new ParticleExplosion.Factory());
        this.func_178929_a(EnumParticleTypes.SPIT.func_179348_c(), new ParticleSpit.Factory());
        this.func_178929_a(EnumParticleTypes.WATER_BUBBLE.func_179348_c(), new ParticleBubble.Factory());
        this.func_178929_a(EnumParticleTypes.WATER_SPLASH.func_179348_c(), new ParticleSplash.Factory());
        this.func_178929_a(EnumParticleTypes.WATER_WAKE.func_179348_c(), new ParticleWaterWake.Factory());
        this.func_178929_a(EnumParticleTypes.WATER_DROP.func_179348_c(), new ParticleRain.Factory());
        this.func_178929_a(EnumParticleTypes.SUSPENDED.func_179348_c(), new ParticleSuspend.Factory());
        this.func_178929_a(EnumParticleTypes.SUSPENDED_DEPTH.func_179348_c(), new ParticleSuspendedTown.Factory());
        this.func_178929_a(EnumParticleTypes.CRIT.func_179348_c(), new ParticleCrit.Factory());
        this.func_178929_a(EnumParticleTypes.CRIT_MAGIC.func_179348_c(), new ParticleCrit.MagicFactory());
        this.func_178929_a(EnumParticleTypes.SMOKE_NORMAL.func_179348_c(), new ParticleSmokeNormal.Factory());
        this.func_178929_a(EnumParticleTypes.SMOKE_LARGE.func_179348_c(), new ParticleSmokeLarge.Factory());
        this.func_178929_a(EnumParticleTypes.SPELL.func_179348_c(), new ParticleSpell.Factory());
        this.func_178929_a(EnumParticleTypes.SPELL_INSTANT.func_179348_c(), new ParticleSpell.InstantFactory());
        this.func_178929_a(EnumParticleTypes.SPELL_MOB.func_179348_c(), new ParticleSpell.MobFactory());
        this.func_178929_a(EnumParticleTypes.SPELL_MOB_AMBIENT.func_179348_c(), new ParticleSpell.AmbientMobFactory());
        this.func_178929_a(EnumParticleTypes.SPELL_WITCH.func_179348_c(), new ParticleSpell.WitchFactory());
        this.func_178929_a(EnumParticleTypes.DRIP_WATER.func_179348_c(), new ParticleDrip.WaterFactory());
        this.func_178929_a(EnumParticleTypes.DRIP_LAVA.func_179348_c(), new ParticleDrip.LavaFactory());
        this.func_178929_a(EnumParticleTypes.VILLAGER_ANGRY.func_179348_c(), new ParticleHeart.AngryVillagerFactory());
        this.func_178929_a(EnumParticleTypes.VILLAGER_HAPPY.func_179348_c(), new ParticleSuspendedTown.HappyVillagerFactory());
        this.func_178929_a(EnumParticleTypes.TOWN_AURA.func_179348_c(), new ParticleSuspendedTown.Factory());
        this.func_178929_a(EnumParticleTypes.NOTE.func_179348_c(), new ParticleNote.Factory());
        this.func_178929_a(EnumParticleTypes.PORTAL.func_179348_c(), new ParticlePortal.Factory());
        this.func_178929_a(EnumParticleTypes.ENCHANTMENT_TABLE.func_179348_c(), new ParticleEnchantmentTable.EnchantmentTable());
        this.func_178929_a(EnumParticleTypes.FLAME.func_179348_c(), new ParticleFlame.Factory());
        this.func_178929_a(EnumParticleTypes.LAVA.func_179348_c(), new ParticleLava.Factory());
        this.func_178929_a(EnumParticleTypes.FOOTSTEP.func_179348_c(), new ParticleFootStep.Factory());
        this.func_178929_a(EnumParticleTypes.CLOUD.func_179348_c(), new ParticleCloud.Factory());
        this.func_178929_a(EnumParticleTypes.REDSTONE.func_179348_c(), new ParticleRedstone.Factory());
        this.func_178929_a(EnumParticleTypes.FALLING_DUST.func_179348_c(), new ParticleFallingDust.Factory());
        this.func_178929_a(EnumParticleTypes.SNOWBALL.func_179348_c(), new ParticleBreaking.SnowballFactory());
        this.func_178929_a(EnumParticleTypes.SNOW_SHOVEL.func_179348_c(), new ParticleSnowShovel.Factory());
        this.func_178929_a(EnumParticleTypes.SLIME.func_179348_c(), new ParticleBreaking.SlimeFactory());
        this.func_178929_a(EnumParticleTypes.HEART.func_179348_c(), new ParticleHeart.Factory());
        this.func_178929_a(EnumParticleTypes.BARRIER.func_179348_c(), new Barrier.Factory());
        this.func_178929_a(EnumParticleTypes.ITEM_CRACK.func_179348_c(), new ParticleBreaking.Factory());
        this.func_178929_a(EnumParticleTypes.BLOCK_CRACK.func_179348_c(), new ParticleDigging.Factory());
        this.func_178929_a(EnumParticleTypes.BLOCK_DUST.func_179348_c(), new ParticleBlockDust.Factory());
        this.func_178929_a(EnumParticleTypes.EXPLOSION_HUGE.func_179348_c(), new ParticleExplosionHuge.Factory());
        this.func_178929_a(EnumParticleTypes.EXPLOSION_LARGE.func_179348_c(), new ParticleExplosionLarge.Factory());
        this.func_178929_a(EnumParticleTypes.FIREWORKS_SPARK.func_179348_c(), new ParticleFirework.Factory());
        this.func_178929_a(EnumParticleTypes.MOB_APPEARANCE.func_179348_c(), new ParticleMobAppearance.Factory());
        this.func_178929_a(EnumParticleTypes.DRAGON_BREATH.func_179348_c(), new ParticleDragonBreath.Factory());
        this.func_178929_a(EnumParticleTypes.END_ROD.func_179348_c(), new ParticleEndRod.Factory());
        this.func_178929_a(EnumParticleTypes.DAMAGE_INDICATOR.func_179348_c(), new ParticleCrit.DamageIndicatorFactory());
        this.func_178929_a(EnumParticleTypes.SWEEP_ATTACK.func_179348_c(), new ParticleSweepAttack.Factory());
        this.func_178929_a(EnumParticleTypes.TOTEM.func_179348_c(), new ParticleTotem.Factory());
    }

    public void func_178929_a(int p_178929_1_, IParticleFactory p_178929_2_)
    {
        this.factories.put(Integer.valueOf(p_178929_1_), p_178929_2_);
    }

    public void func_178926_a(Entity p_178926_1_, EnumParticleTypes p_178926_2_)
    {
        this.particleEmitters.add(new ParticleEmitter(this.world, p_178926_1_, p_178926_2_));
    }

    public void func_191271_a(Entity p_191271_1_, EnumParticleTypes p_191271_2_, int p_191271_3_)
    {
        this.particleEmitters.add(new ParticleEmitter(this.world, p_191271_1_, p_191271_2_, p_191271_3_));
    }

    @Nullable
    public Particle func_178927_a(int p_178927_1_, double p_178927_2_, double p_178927_4_, double p_178927_6_, double p_178927_8_, double p_178927_10_, double p_178927_12_, int... p_178927_14_)
    {
        IParticleFactory iparticlefactory = this.factories.get(Integer.valueOf(p_178927_1_));

        if (iparticlefactory != null)
        {
            Particle particle = iparticlefactory.func_178902_a(p_178927_1_, this.world, p_178927_2_, p_178927_4_, p_178927_6_, p_178927_8_, p_178927_10_, p_178927_12_, p_178927_14_);

            if (particle != null)
            {
                this.addEffect(particle);
                return particle;
            }
        }

        return null;
    }

    public void addEffect(Particle effect)
    {
        this.queue.add(effect);
    }

    public void tick()
    {
        for (int i = 0; i < 4; ++i)
        {
            this.func_178922_a(i);
        }

        if (!this.particleEmitters.isEmpty())
        {
            List<ParticleEmitter> list = Lists.<ParticleEmitter>newArrayList();

            for (ParticleEmitter particleemitter : this.particleEmitters)
            {
                particleemitter.tick();

                if (!particleemitter.isAlive())
                {
                    list.add(particleemitter);
                }
            }

            this.particleEmitters.removeAll(list);
        }

        if (!this.queue.isEmpty())
        {
            for (Particle particle = this.queue.poll(); particle != null; particle = this.queue.poll())
            {
                int j = particle.func_70537_b();
                int k = particle.func_187111_c() ? 0 : 1;

                if (this.byType[j][k].size() >= 16384)
                {
                    this.byType[j][k].removeFirst();
                }

                this.byType[j][k].add(particle);
            }
        }
    }

    private void func_178922_a(int p_178922_1_)
    {
        this.world.profiler.startSection(String.valueOf(p_178922_1_));

        for (int i = 0; i < 2; ++i)
        {
            this.world.profiler.startSection(String.valueOf(i));
            this.tickParticleList(this.byType[p_178922_1_][i]);
            this.world.profiler.endSection();
        }

        this.world.profiler.endSection();
    }

    private void tickParticleList(Queue<Particle> particlesIn)
    {
        if (!particlesIn.isEmpty())
        {
            Iterator<Particle> iterator = particlesIn.iterator();

            while (iterator.hasNext())
            {
                Particle particle = iterator.next();
                this.tickParticle(particle);

                if (!particle.isAlive())
                {
                    iterator.remove();
                }
            }
        }
    }

    private void tickParticle(final Particle particle)
    {
        try
        {
            particle.tick();
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
            final int i = particle.func_70537_b();
            crashreportcategory.addDetail("Particle", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    return particle.toString();
                }
            });
            crashreportcategory.addDetail("Particle Type", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    if (i == 0)
                    {
                        return "MISC_TEXTURE";
                    }
                    else if (i == 1)
                    {
                        return "TERRAIN_TEXTURE";
                    }
                    else
                    {
                        return i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i;
                    }
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    public void func_78874_a(Entity p_78874_1_, float p_78874_2_)
    {
        float f = ActiveRenderInfo.func_178808_b();
        float f1 = ActiveRenderInfo.func_178803_d();
        float f2 = ActiveRenderInfo.func_178805_e();
        float f3 = ActiveRenderInfo.func_178807_f();
        float f4 = ActiveRenderInfo.func_178809_c();
        Particle.field_70556_an = p_78874_1_.lastTickPosX + (p_78874_1_.posX - p_78874_1_.lastTickPosX) * (double)p_78874_2_;
        Particle.field_70554_ao = p_78874_1_.lastTickPosY + (p_78874_1_.posY - p_78874_1_.lastTickPosY) * (double)p_78874_2_;
        Particle.field_70555_ap = p_78874_1_.lastTickPosZ + (p_78874_1_.posZ - p_78874_1_.lastTickPosZ) * (double)p_78874_2_;
        Particle.field_190016_K = p_78874_1_.getLook(p_78874_2_);
        GlStateManager.func_179147_l();
        GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.func_179092_a(516, 0.003921569F);

        for (int i_nf = 0; i_nf < 3; ++i_nf)
        {
            final int i = i_nf;

            for (int j = 0; j < 2; ++j)
            {
                if (!this.byType[i][j].isEmpty())
                {
                    switch (j)
                    {
                        case 0:
                            GlStateManager.func_179132_a(false);
                            break;

                        case 1:
                            GlStateManager.func_179132_a(true);
                    }

                    switch (i)
                    {
                        case 0:
                        default:
                            this.renderer.bindTexture(field_110737_b);
                            break;

                        case 1:
                            this.renderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    }

                    GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder bufferbuilder = tessellator.getBuffer();
                    bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

                    for (final Particle particle : this.byType[i][j])
                    {
                        try
                        {
                            particle.func_180434_a(bufferbuilder, p_78874_1_, p_78874_2_, f, f4, f1, f2, f3);
                        }
                        catch (Throwable throwable)
                        {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
                            crashreportcategory.addDetail("Particle", new ICrashReportDetail<String>()
                            {
                                public String call() throws Exception
                                {
                                    return particle.toString();
                                }
                            });
                            crashreportcategory.addDetail("Particle Type", new ICrashReportDetail<String>()
                            {
                                public String call() throws Exception
                                {
                                    if (i == 0)
                                    {
                                        return "MISC_TEXTURE";
                                    }
                                    else if (i == 1)
                                    {
                                        return "TERRAIN_TEXTURE";
                                    }
                                    else
                                    {
                                        return i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i;
                                    }
                                }
                            });
                            throw new ReportedException(crashreport);
                        }
                    }

                    tessellator.draw();
                }
            }
        }

        GlStateManager.func_179132_a(true);
        GlStateManager.func_179084_k();
        GlStateManager.func_179092_a(516, 0.1F);
    }

    public void func_78872_b(Entity p_78872_1_, float p_78872_2_)
    {
        float f = 0.017453292F;
        float f1 = MathHelper.cos(p_78872_1_.rotationYaw * 0.017453292F);
        float f2 = MathHelper.sin(p_78872_1_.rotationYaw * 0.017453292F);
        float f3 = -f2 * MathHelper.sin(p_78872_1_.rotationPitch * 0.017453292F);
        float f4 = f1 * MathHelper.sin(p_78872_1_.rotationPitch * 0.017453292F);
        float f5 = MathHelper.cos(p_78872_1_.rotationPitch * 0.017453292F);

        for (int i = 0; i < 2; ++i)
        {
            Queue<Particle> queue = this.byType[3][i];

            if (!queue.isEmpty())
            {
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();

                for (Particle particle : queue)
                {
                    particle.func_180434_a(bufferbuilder, p_78872_1_, p_78872_2_, f1, f5, f2, f3, f4);
                }
            }
        }
    }

    public void clearEffects(@Nullable World worldIn)
    {
        this.world = worldIn;

        for (int i = 0; i < 4; ++i)
        {
            for (int j = 0; j < 2; ++j)
            {
                this.byType[i][j].clear();
            }
        }

        this.particleEmitters.clear();
    }

    public void addBlockDestroyEffects(BlockPos pos, IBlockState state)
    {
        if (state.getMaterial() != Material.AIR)
        {
            state = state.func_185899_b(this.world, pos);
            int i = 4;

            for (int j = 0; j < 4; ++j)
            {
                for (int k = 0; k < 4; ++k)
                {
                    for (int l = 0; l < 4; ++l)
                    {
                        double d0 = ((double)j + 0.5D) / 4.0D;
                        double d1 = ((double)k + 0.5D) / 4.0D;
                        double d2 = ((double)l + 0.5D) / 4.0D;
                        this.addEffect((new ParticleDigging(this.world, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, d0 - 0.5D, d1 - 0.5D, d2 - 0.5D, state)).setBlockPos(pos));
                    }
                }
            }
        }
    }

    /**
     * Adds block hit particles for the specified block
     */
    public void addBlockHitEffects(BlockPos pos, EnumFacing side)
    {
        IBlockState iblockstate = this.world.getBlockState(pos);

        if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE)
        {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = 0.1F;
            AxisAlignedBB axisalignedbb = iblockstate.func_185900_c(this.world, pos);
            double d0 = (double)i + this.rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
            double d1 = (double)j + this.rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
            double d2 = (double)k + this.rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;

            if (side == EnumFacing.DOWN)
            {
                d1 = (double)j + axisalignedbb.minY - 0.10000000149011612D;
            }

            if (side == EnumFacing.UP)
            {
                d1 = (double)j + axisalignedbb.maxY + 0.10000000149011612D;
            }

            if (side == EnumFacing.NORTH)
            {
                d2 = (double)k + axisalignedbb.minZ - 0.10000000149011612D;
            }

            if (side == EnumFacing.SOUTH)
            {
                d2 = (double)k + axisalignedbb.maxZ + 0.10000000149011612D;
            }

            if (side == EnumFacing.WEST)
            {
                d0 = (double)i + axisalignedbb.minX - 0.10000000149011612D;
            }

            if (side == EnumFacing.EAST)
            {
                d0 = (double)i + axisalignedbb.maxX + 0.10000000149011612D;
            }

            this.addEffect((new ParticleDigging(this.world, d0, d1, d2, 0.0D, 0.0D, 0.0D, iblockstate)).setBlockPos(pos).multiplyVelocity(0.2F).multiplyParticleScaleBy(0.6F));
        }
    }

    public String getStatistics()
    {
        int i = 0;

        for (int j = 0; j < 4; ++j)
        {
            for (int k = 0; k < 2; ++k)
            {
                i += this.byType[j][k].size();
            }
        }

        return "" + i;
    }
}

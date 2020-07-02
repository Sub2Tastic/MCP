package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.annotation.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Quaternion;

public class GlStateManager
{
    private static final FloatBuffer field_187450_a = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer field_187451_b = BufferUtils.createFloatBuffer(4);
    private static final GlStateManager.AlphaState field_179160_a = new GlStateManager.AlphaState();
    private static final GlStateManager.BooleanState field_179158_b = new GlStateManager.BooleanState(2896);
    private static final GlStateManager.BooleanState[] field_179159_c = new GlStateManager.BooleanState[8];
    private static final GlStateManager.ColorMaterialState field_179156_d;
    private static final GlStateManager.BlendState field_179157_e;
    private static final GlStateManager.DepthState field_179154_f;
    private static final GlStateManager.FogState field_179155_g;
    private static final GlStateManager.CullState field_179167_h;
    private static final GlStateManager.PolygonOffsetState field_179168_i;
    private static final GlStateManager.ColorLogicState field_179165_j;
    private static final GlStateManager.TexGenState field_179166_k;
    private static final GlStateManager.ClearState field_179163_l;
    private static final GlStateManager.StencilState field_179164_m;
    private static final GlStateManager.BooleanState field_179161_n;
    private static int field_179162_o;
    private static final GlStateManager.TextureState[] field_179174_p;
    private static int field_179173_q;
    private static final GlStateManager.BooleanState field_179172_r;
    private static final GlStateManager.ColorMask field_179171_s;
    private static final GlStateManager.Color field_179170_t;

    public static void func_179123_a()
    {
        GL11.glPushAttrib(8256);
    }

    public static void func_179099_b()
    {
        GL11.glPopAttrib();
    }

    public static void func_179118_c()
    {
        field_179160_a.test.disable();
    }

    public static void func_179141_d()
    {
        field_179160_a.test.enable();
    }

    public static void func_179092_a(int p_179092_0_, float p_179092_1_)
    {
        if (p_179092_0_ != field_179160_a.func || p_179092_1_ != field_179160_a.ref)
        {
            field_179160_a.func = p_179092_0_;
            field_179160_a.ref = p_179092_1_;
            GL11.glAlphaFunc(p_179092_0_, p_179092_1_);
        }
    }

    public static void func_179145_e()
    {
        field_179158_b.enable();
    }

    public static void func_179140_f()
    {
        field_179158_b.disable();
    }

    public static void func_179085_a(int p_179085_0_)
    {
        field_179159_c[p_179085_0_].enable();
    }

    public static void func_179122_b(int p_179122_0_)
    {
        field_179159_c[p_179122_0_].disable();
    }

    public static void func_179142_g()
    {
        field_179156_d.colorMaterial.enable();
    }

    public static void func_179119_h()
    {
        field_179156_d.colorMaterial.disable();
    }

    public static void func_179104_a(int p_179104_0_, int p_179104_1_)
    {
        if (p_179104_0_ != field_179156_d.face || p_179104_1_ != field_179156_d.mode)
        {
            field_179156_d.face = p_179104_0_;
            field_179156_d.mode = p_179104_1_;
            GL11.glColorMaterial(p_179104_0_, p_179104_1_);
        }
    }

    public static void func_187438_a(int p_187438_0_, int p_187438_1_, FloatBuffer p_187438_2_)
    {
        GL11.glLight(p_187438_0_, p_187438_1_, p_187438_2_);
    }

    public static void func_187424_a(int p_187424_0_, FloatBuffer p_187424_1_)
    {
        GL11.glLightModel(p_187424_0_, p_187424_1_);
    }

    public static void func_187432_a(float p_187432_0_, float p_187432_1_, float p_187432_2_)
    {
        GL11.glNormal3f(p_187432_0_, p_187432_1_, p_187432_2_);
    }

    public static void func_179097_i()
    {
        field_179154_f.test.disable();
    }

    public static void func_179126_j()
    {
        field_179154_f.test.enable();
    }

    public static void func_179143_c(int p_179143_0_)
    {
        if (p_179143_0_ != field_179154_f.func)
        {
            field_179154_f.func = p_179143_0_;
            GL11.glDepthFunc(p_179143_0_);
        }
    }

    public static void func_179132_a(boolean p_179132_0_)
    {
        if (p_179132_0_ != field_179154_f.mask)
        {
            field_179154_f.mask = p_179132_0_;
            GL11.glDepthMask(p_179132_0_);
        }
    }

    public static void func_179084_k()
    {
        field_179157_e.blend.disable();
    }

    public static void func_179147_l()
    {
        field_179157_e.blend.enable();
    }

    public static void func_187401_a(GlStateManager.SourceFactor p_187401_0_, GlStateManager.DestFactor p_187401_1_)
    {
        func_179112_b(p_187401_0_.field_187395_p, p_187401_1_.field_187345_o);
    }

    public static void func_179112_b(int p_179112_0_, int p_179112_1_)
    {
        if (p_179112_0_ != field_179157_e.srcFactorRgb || p_179112_1_ != field_179157_e.dstFactorRgb)
        {
            field_179157_e.srcFactorRgb = p_179112_0_;
            field_179157_e.dstFactorRgb = p_179112_1_;
            GL11.glBlendFunc(p_179112_0_, p_179112_1_);
        }
    }

    public static void func_187428_a(GlStateManager.SourceFactor p_187428_0_, GlStateManager.DestFactor p_187428_1_, GlStateManager.SourceFactor p_187428_2_, GlStateManager.DestFactor p_187428_3_)
    {
        func_179120_a(p_187428_0_.field_187395_p, p_187428_1_.field_187345_o, p_187428_2_.field_187395_p, p_187428_3_.field_187345_o);
    }

    public static void func_179120_a(int p_179120_0_, int p_179120_1_, int p_179120_2_, int p_179120_3_)
    {
        if (p_179120_0_ != field_179157_e.srcFactorRgb || p_179120_1_ != field_179157_e.dstFactorRgb || p_179120_2_ != field_179157_e.srcFactorAlpha || p_179120_3_ != field_179157_e.dstFactorAlpha)
        {
            field_179157_e.srcFactorRgb = p_179120_0_;
            field_179157_e.dstFactorRgb = p_179120_1_;
            field_179157_e.srcFactorAlpha = p_179120_2_;
            field_179157_e.dstFactorAlpha = p_179120_3_;
            OpenGlHelper.func_148821_a(p_179120_0_, p_179120_1_, p_179120_2_, p_179120_3_);
        }
    }

    public static void func_187398_d(int p_187398_0_)
    {
        GL14.glBlendEquation(p_187398_0_);
    }

    public static void func_187431_e(int p_187431_0_)
    {
        field_187451_b.put(0, (float)(p_187431_0_ >> 16 & 255) / 255.0F);
        field_187451_b.put(1, (float)(p_187431_0_ >> 8 & 255) / 255.0F);
        field_187451_b.put(2, (float)(p_187431_0_ >> 0 & 255) / 255.0F);
        field_187451_b.put(3, (float)(p_187431_0_ >> 24 & 255) / 255.0F);
        func_187448_b(8960, 8705, field_187451_b);
        func_187399_a(8960, 8704, 34160);
        func_187399_a(8960, 34161, 7681);
        func_187399_a(8960, 34176, 34166);
        func_187399_a(8960, 34192, 768);
        func_187399_a(8960, 34162, 7681);
        func_187399_a(8960, 34184, 5890);
        func_187399_a(8960, 34200, 770);
    }

    public static void func_187417_n()
    {
        func_187399_a(8960, 8704, 8448);
        func_187399_a(8960, 34161, 8448);
        func_187399_a(8960, 34162, 8448);
        func_187399_a(8960, 34176, 5890);
        func_187399_a(8960, 34184, 5890);
        func_187399_a(8960, 34192, 768);
        func_187399_a(8960, 34200, 770);
    }

    public static void func_179127_m()
    {
        field_179155_g.fog.enable();
    }

    public static void func_179106_n()
    {
        field_179155_g.fog.disable();
    }

    public static void func_187430_a(GlStateManager.FogMode p_187430_0_)
    {
        func_179093_d(p_187430_0_.param);
    }

    private static void func_179093_d(int p_179093_0_)
    {
        if (p_179093_0_ != field_179155_g.mode)
        {
            field_179155_g.mode = p_179093_0_;
            GL11.glFogi(GL11.GL_FOG_MODE, p_179093_0_);
        }
    }

    public static void func_179095_a(float p_179095_0_)
    {
        if (p_179095_0_ != field_179155_g.density)
        {
            field_179155_g.density = p_179095_0_;
            GL11.glFogf(GL11.GL_FOG_DENSITY, p_179095_0_);
        }
    }

    public static void func_179102_b(float p_179102_0_)
    {
        if (p_179102_0_ != field_179155_g.start)
        {
            field_179155_g.start = p_179102_0_;
            GL11.glFogf(GL11.GL_FOG_START, p_179102_0_);
        }
    }

    public static void func_179153_c(float p_179153_0_)
    {
        if (p_179153_0_ != field_179155_g.end)
        {
            field_179155_g.end = p_179153_0_;
            GL11.glFogf(GL11.GL_FOG_END, p_179153_0_);
        }
    }

    public static void func_187402_b(int p_187402_0_, FloatBuffer p_187402_1_)
    {
        GL11.glFog(p_187402_0_, p_187402_1_);
    }

    public static void func_187412_c(int p_187412_0_, int p_187412_1_)
    {
        GL11.glFogi(p_187412_0_, p_187412_1_);
    }

    public static void func_179089_o()
    {
        field_179167_h.cullFace.enable();
    }

    public static void func_179129_p()
    {
        field_179167_h.cullFace.disable();
    }

    public static void func_187407_a(GlStateManager.CullFace p_187407_0_)
    {
        func_179107_e(p_187407_0_.field_187328_d);
    }

    private static void func_179107_e(int p_179107_0_)
    {
        if (p_179107_0_ != field_179167_h.mode)
        {
            field_179167_h.mode = p_179107_0_;
            GL11.glCullFace(p_179107_0_);
        }
    }

    public static void func_187409_d(int p_187409_0_, int p_187409_1_)
    {
        GL11.glPolygonMode(p_187409_0_, p_187409_1_);
    }

    public static void func_179088_q()
    {
        field_179168_i.polyOffset.enable();
    }

    public static void func_179113_r()
    {
        field_179168_i.polyOffset.disable();
    }

    public static void func_179136_a(float p_179136_0_, float p_179136_1_)
    {
        if (p_179136_0_ != field_179168_i.factor || p_179136_1_ != field_179168_i.units)
        {
            field_179168_i.factor = p_179136_0_;
            field_179168_i.units = p_179136_1_;
            GL11.glPolygonOffset(p_179136_0_, p_179136_1_);
        }
    }

    public static void func_179115_u()
    {
        field_179165_j.colorLogicOp.enable();
    }

    public static void func_179134_v()
    {
        field_179165_j.colorLogicOp.disable();
    }

    public static void func_187422_a(GlStateManager.LogicOp p_187422_0_)
    {
        func_179116_f(p_187422_0_.opcode);
    }

    public static void func_179116_f(int p_179116_0_)
    {
        if (p_179116_0_ != field_179165_j.logicOpcode)
        {
            field_179165_j.logicOpcode = p_179116_0_;
            GL11.glLogicOp(p_179116_0_);
        }
    }

    public static void func_179087_a(GlStateManager.TexGen p_179087_0_)
    {
        func_179125_c(p_179087_0_).textureGen.enable();
    }

    public static void func_179100_b(GlStateManager.TexGen p_179100_0_)
    {
        func_179125_c(p_179100_0_).textureGen.disable();
    }

    public static void func_179149_a(GlStateManager.TexGen p_179149_0_, int p_179149_1_)
    {
        GlStateManager.TexGenCoord glstatemanager$texgencoord = func_179125_c(p_179149_0_);

        if (p_179149_1_ != glstatemanager$texgencoord.mode)
        {
            glstatemanager$texgencoord.mode = p_179149_1_;
            GL11.glTexGeni(glstatemanager$texgencoord.coord, GL11.GL_TEXTURE_GEN_MODE, p_179149_1_);
        }
    }

    public static void func_179105_a(GlStateManager.TexGen p_179105_0_, int p_179105_1_, FloatBuffer p_179105_2_)
    {
        GL11.glTexGen(func_179125_c(p_179105_0_).coord, p_179105_1_, p_179105_2_);
    }

    private static GlStateManager.TexGenCoord func_179125_c(GlStateManager.TexGen p_179125_0_)
    {
        switch (p_179125_0_)
        {
            case S:
                return field_179166_k.s;

            case T:
                return field_179166_k.t;

            case R:
                return field_179166_k.r;

            case Q:
                return field_179166_k.q;

            default:
                return field_179166_k.s;
        }
    }

    public static void func_179138_g(int p_179138_0_)
    {
        if (field_179162_o != p_179138_0_ - OpenGlHelper.field_77478_a)
        {
            field_179162_o = p_179138_0_ - OpenGlHelper.field_77478_a;
            OpenGlHelper.func_77473_a(p_179138_0_);
        }
    }

    public static void func_179098_w()
    {
        field_179174_p[field_179162_o].texture2DState.enable();
    }

    public static void func_179090_x()
    {
        field_179174_p[field_179162_o].texture2DState.disable();
    }

    public static void func_187448_b(int p_187448_0_, int p_187448_1_, FloatBuffer p_187448_2_)
    {
        GL11.glTexEnv(p_187448_0_, p_187448_1_, p_187448_2_);
    }

    public static void func_187399_a(int p_187399_0_, int p_187399_1_, int p_187399_2_)
    {
        GL11.glTexEnvi(p_187399_0_, p_187399_1_, p_187399_2_);
    }

    public static void func_187436_a(int p_187436_0_, int p_187436_1_, float p_187436_2_)
    {
        GL11.glTexEnvf(p_187436_0_, p_187436_1_, p_187436_2_);
    }

    public static void func_187403_b(int p_187403_0_, int p_187403_1_, float p_187403_2_)
    {
        GL11.glTexParameterf(p_187403_0_, p_187403_1_, p_187403_2_);
    }

    public static void func_187421_b(int p_187421_0_, int p_187421_1_, int p_187421_2_)
    {
        GL11.glTexParameteri(p_187421_0_, p_187421_1_, p_187421_2_);
    }

    public static int func_187411_c(int p_187411_0_, int p_187411_1_, int p_187411_2_)
    {
        return GL11.glGetTexLevelParameteri(p_187411_0_, p_187411_1_, p_187411_2_);
    }

    public static int func_179146_y()
    {
        return GL11.glGenTextures();
    }

    public static void func_179150_h(int p_179150_0_)
    {
        GL11.glDeleteTextures(p_179150_0_);

        for (GlStateManager.TextureState glstatemanager$texturestate : field_179174_p)
        {
            if (glstatemanager$texturestate.textureName == p_179150_0_)
            {
                glstatemanager$texturestate.textureName = -1;
            }
        }
    }

    public static void func_179144_i(int p_179144_0_)
    {
        if (p_179144_0_ != field_179174_p[field_179162_o].textureName)
        {
            field_179174_p[field_179162_o].textureName = p_179144_0_;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, p_179144_0_);
        }
    }

    public static void func_187419_a(int p_187419_0_, int p_187419_1_, int p_187419_2_, int p_187419_3_, int p_187419_4_, int p_187419_5_, int p_187419_6_, int p_187419_7_, @Nullable IntBuffer p_187419_8_)
    {
        GL11.glTexImage2D(p_187419_0_, p_187419_1_, p_187419_2_, p_187419_3_, p_187419_4_, p_187419_5_, p_187419_6_, p_187419_7_, p_187419_8_);
    }

    public static void func_187414_b(int p_187414_0_, int p_187414_1_, int p_187414_2_, int p_187414_3_, int p_187414_4_, int p_187414_5_, int p_187414_6_, int p_187414_7_, IntBuffer p_187414_8_)
    {
        GL11.glTexSubImage2D(p_187414_0_, p_187414_1_, p_187414_2_, p_187414_3_, p_187414_4_, p_187414_5_, p_187414_6_, p_187414_7_, p_187414_8_);
    }

    public static void func_187443_a(int p_187443_0_, int p_187443_1_, int p_187443_2_, int p_187443_3_, int p_187443_4_, int p_187443_5_, int p_187443_6_, int p_187443_7_)
    {
        GL11.glCopyTexSubImage2D(p_187443_0_, p_187443_1_, p_187443_2_, p_187443_3_, p_187443_4_, p_187443_5_, p_187443_6_, p_187443_7_);
    }

    public static void func_187433_a(int p_187433_0_, int p_187433_1_, int p_187433_2_, int p_187433_3_, IntBuffer p_187433_4_)
    {
        GL11.glGetTexImage(p_187433_0_, p_187433_1_, p_187433_2_, p_187433_3_, p_187433_4_);
    }

    public static void func_179108_z()
    {
        field_179161_n.enable();
    }

    public static void func_179133_A()
    {
        field_179161_n.disable();
    }

    public static void func_179103_j(int p_179103_0_)
    {
        if (p_179103_0_ != field_179173_q)
        {
            field_179173_q = p_179103_0_;
            GL11.glShadeModel(p_179103_0_);
        }
    }

    public static void func_179091_B()
    {
        field_179172_r.enable();
    }

    public static void func_179101_C()
    {
        field_179172_r.disable();
    }

    public static void func_179083_b(int p_179083_0_, int p_179083_1_, int p_179083_2_, int p_179083_3_)
    {
        GL11.glViewport(p_179083_0_, p_179083_1_, p_179083_2_, p_179083_3_);
    }

    public static void func_179135_a(boolean p_179135_0_, boolean p_179135_1_, boolean p_179135_2_, boolean p_179135_3_)
    {
        if (p_179135_0_ != field_179171_s.red || p_179135_1_ != field_179171_s.green || p_179135_2_ != field_179171_s.blue || p_179135_3_ != field_179171_s.alpha)
        {
            field_179171_s.red = p_179135_0_;
            field_179171_s.green = p_179135_1_;
            field_179171_s.blue = p_179135_2_;
            field_179171_s.alpha = p_179135_3_;
            GL11.glColorMask(p_179135_0_, p_179135_1_, p_179135_2_, p_179135_3_);
        }
    }

    public static void func_179151_a(double p_179151_0_)
    {
        if (p_179151_0_ != field_179163_l.depth)
        {
            field_179163_l.depth = p_179151_0_;
            GL11.glClearDepth(p_179151_0_);
        }
    }

    public static void func_179082_a(float p_179082_0_, float p_179082_1_, float p_179082_2_, float p_179082_3_)
    {
        if (p_179082_0_ != field_179163_l.color.red || p_179082_1_ != field_179163_l.color.green || p_179082_2_ != field_179163_l.color.blue || p_179082_3_ != field_179163_l.color.alpha)
        {
            field_179163_l.color.red = p_179082_0_;
            field_179163_l.color.green = p_179082_1_;
            field_179163_l.color.blue = p_179082_2_;
            field_179163_l.color.alpha = p_179082_3_;
            GL11.glClearColor(p_179082_0_, p_179082_1_, p_179082_2_, p_179082_3_);
        }
    }

    public static void func_179086_m(int p_179086_0_)
    {
        GL11.glClear(p_179086_0_);
    }

    public static void func_179128_n(int p_179128_0_)
    {
        GL11.glMatrixMode(p_179128_0_);
    }

    public static void func_179096_D()
    {
        GL11.glLoadIdentity();
    }

    public static void func_179094_E()
    {
        GL11.glPushMatrix();
    }

    public static void func_179121_F()
    {
        GL11.glPopMatrix();
    }

    public static void func_179111_a(int p_179111_0_, FloatBuffer p_179111_1_)
    {
        GL11.glGetFloat(p_179111_0_, p_179111_1_);
    }

    public static void func_179130_a(double p_179130_0_, double p_179130_2_, double p_179130_4_, double p_179130_6_, double p_179130_8_, double p_179130_10_)
    {
        GL11.glOrtho(p_179130_0_, p_179130_2_, p_179130_4_, p_179130_6_, p_179130_8_, p_179130_10_);
    }

    public static void func_179114_b(float p_179114_0_, float p_179114_1_, float p_179114_2_, float p_179114_3_)
    {
        GL11.glRotatef(p_179114_0_, p_179114_1_, p_179114_2_, p_179114_3_);
    }

    public static void func_179152_a(float p_179152_0_, float p_179152_1_, float p_179152_2_)
    {
        GL11.glScalef(p_179152_0_, p_179152_1_, p_179152_2_);
    }

    public static void func_179139_a(double p_179139_0_, double p_179139_2_, double p_179139_4_)
    {
        GL11.glScaled(p_179139_0_, p_179139_2_, p_179139_4_);
    }

    public static void func_179109_b(float p_179109_0_, float p_179109_1_, float p_179109_2_)
    {
        GL11.glTranslatef(p_179109_0_, p_179109_1_, p_179109_2_);
    }

    public static void func_179137_b(double p_179137_0_, double p_179137_2_, double p_179137_4_)
    {
        GL11.glTranslated(p_179137_0_, p_179137_2_, p_179137_4_);
    }

    public static void func_179110_a(FloatBuffer p_179110_0_)
    {
        GL11.glMultMatrix(p_179110_0_);
    }

    public static void func_187444_a(Quaternion p_187444_0_)
    {
        func_179110_a(func_187418_a(field_187450_a, p_187444_0_));
    }

    public static FloatBuffer func_187418_a(FloatBuffer p_187418_0_, Quaternion p_187418_1_)
    {
        p_187418_0_.clear();
        float f = p_187418_1_.x * p_187418_1_.x;
        float f1 = p_187418_1_.x * p_187418_1_.y;
        float f2 = p_187418_1_.x * p_187418_1_.z;
        float f3 = p_187418_1_.x * p_187418_1_.w;
        float f4 = p_187418_1_.y * p_187418_1_.y;
        float f5 = p_187418_1_.y * p_187418_1_.z;
        float f6 = p_187418_1_.y * p_187418_1_.w;
        float f7 = p_187418_1_.z * p_187418_1_.z;
        float f8 = p_187418_1_.z * p_187418_1_.w;
        p_187418_0_.put(1.0F - 2.0F * (f4 + f7));
        p_187418_0_.put(2.0F * (f1 + f8));
        p_187418_0_.put(2.0F * (f2 - f6));
        p_187418_0_.put(0.0F);
        p_187418_0_.put(2.0F * (f1 - f8));
        p_187418_0_.put(1.0F - 2.0F * (f + f7));
        p_187418_0_.put(2.0F * (f5 + f3));
        p_187418_0_.put(0.0F);
        p_187418_0_.put(2.0F * (f2 + f6));
        p_187418_0_.put(2.0F * (f5 - f3));
        p_187418_0_.put(1.0F - 2.0F * (f + f4));
        p_187418_0_.put(0.0F);
        p_187418_0_.put(0.0F);
        p_187418_0_.put(0.0F);
        p_187418_0_.put(0.0F);
        p_187418_0_.put(1.0F);
        p_187418_0_.rewind();
        return p_187418_0_;
    }

    public static void func_179131_c(float p_179131_0_, float p_179131_1_, float p_179131_2_, float p_179131_3_)
    {
        if (p_179131_0_ != field_179170_t.red || p_179131_1_ != field_179170_t.green || p_179131_2_ != field_179170_t.blue || p_179131_3_ != field_179170_t.alpha)
        {
            field_179170_t.red = p_179131_0_;
            field_179170_t.green = p_179131_1_;
            field_179170_t.blue = p_179131_2_;
            field_179170_t.alpha = p_179131_3_;
            GL11.glColor4f(p_179131_0_, p_179131_1_, p_179131_2_, p_179131_3_);
        }
    }

    public static void func_179124_c(float p_179124_0_, float p_179124_1_, float p_179124_2_)
    {
        func_179131_c(p_179124_0_, p_179124_1_, p_179124_2_, 1.0F);
    }

    public static void func_187426_b(float p_187426_0_, float p_187426_1_)
    {
        GL11.glTexCoord2f(p_187426_0_, p_187426_1_);
    }

    public static void func_187435_e(float p_187435_0_, float p_187435_1_, float p_187435_2_)
    {
        GL11.glVertex3f(p_187435_0_, p_187435_1_, p_187435_2_);
    }

    public static void func_179117_G()
    {
        field_179170_t.red = -1.0F;
        field_179170_t.green = -1.0F;
        field_179170_t.blue = -1.0F;
        field_179170_t.alpha = -1.0F;
    }

    public static void func_187446_a(int p_187446_0_, int p_187446_1_, ByteBuffer p_187446_2_)
    {
        GL11.glNormalPointer(p_187446_0_, p_187446_1_, p_187446_2_);
    }

    public static void func_187405_c(int p_187405_0_, int p_187405_1_, int p_187405_2_, int p_187405_3_)
    {
        GL11.glTexCoordPointer(p_187405_0_, p_187405_1_, p_187405_2_, (long)p_187405_3_);
    }

    public static void func_187404_a(int p_187404_0_, int p_187404_1_, int p_187404_2_, ByteBuffer p_187404_3_)
    {
        GL11.glTexCoordPointer(p_187404_0_, p_187404_1_, p_187404_2_, p_187404_3_);
    }

    public static void func_187420_d(int p_187420_0_, int p_187420_1_, int p_187420_2_, int p_187420_3_)
    {
        GL11.glVertexPointer(p_187420_0_, p_187420_1_, p_187420_2_, (long)p_187420_3_);
    }

    public static void func_187427_b(int p_187427_0_, int p_187427_1_, int p_187427_2_, ByteBuffer p_187427_3_)
    {
        GL11.glVertexPointer(p_187427_0_, p_187427_1_, p_187427_2_, p_187427_3_);
    }

    public static void func_187406_e(int p_187406_0_, int p_187406_1_, int p_187406_2_, int p_187406_3_)
    {
        GL11.glColorPointer(p_187406_0_, p_187406_1_, p_187406_2_, (long)p_187406_3_);
    }

    public static void func_187400_c(int p_187400_0_, int p_187400_1_, int p_187400_2_, ByteBuffer p_187400_3_)
    {
        GL11.glColorPointer(p_187400_0_, p_187400_1_, p_187400_2_, p_187400_3_);
    }

    public static void func_187429_p(int p_187429_0_)
    {
        GL11.glDisableClientState(p_187429_0_);
    }

    public static void func_187410_q(int p_187410_0_)
    {
        GL11.glEnableClientState(p_187410_0_);
    }

    public static void func_187447_r(int p_187447_0_)
    {
        GL11.glBegin(p_187447_0_);
    }

    public static void func_187437_J()
    {
        GL11.glEnd();
    }

    public static void func_187439_f(int p_187439_0_, int p_187439_1_, int p_187439_2_)
    {
        GL11.glDrawArrays(p_187439_0_, p_187439_1_, p_187439_2_);
    }

    public static void func_187441_d(float p_187441_0_)
    {
        GL11.glLineWidth(p_187441_0_);
    }

    public static void func_179148_o(int p_179148_0_)
    {
        GL11.glCallList(p_179148_0_);
    }

    public static void func_187449_e(int p_187449_0_, int p_187449_1_)
    {
        GL11.glDeleteLists(p_187449_0_, p_187449_1_);
    }

    public static void func_187423_f(int p_187423_0_, int p_187423_1_)
    {
        GL11.glNewList(p_187423_0_, p_187423_1_);
    }

    public static void func_187415_K()
    {
        GL11.glEndList();
    }

    public static int func_187442_t(int p_187442_0_)
    {
        return GL11.glGenLists(p_187442_0_);
    }

    public static void func_187425_g(int p_187425_0_, int p_187425_1_)
    {
        GL11.glPixelStorei(p_187425_0_, p_187425_1_);
    }

    public static void func_187413_a(int p_187413_0_, int p_187413_1_, int p_187413_2_, int p_187413_3_, int p_187413_4_, int p_187413_5_, IntBuffer p_187413_6_)
    {
        GL11.glReadPixels(p_187413_0_, p_187413_1_, p_187413_2_, p_187413_3_, p_187413_4_, p_187413_5_, p_187413_6_);
    }

    public static int func_187434_L()
    {
        return GL11.glGetError();
    }

    public static String func_187416_u(int p_187416_0_)
    {
        return GL11.glGetString(p_187416_0_);
    }

    public static void func_187445_a(int p_187445_0_, IntBuffer p_187445_1_)
    {
        GL11.glGetInteger(p_187445_0_, p_187445_1_);
    }

    public static int func_187397_v(int p_187397_0_)
    {
        return GL11.glGetInteger(p_187397_0_);
    }

    public static void func_187408_a(GlStateManager.Profile p_187408_0_)
    {
        p_187408_0_.func_187373_a();
    }

    public static void func_187440_b(GlStateManager.Profile p_187440_0_)
    {
        p_187440_0_.func_187374_b();
    }

    static
    {
        for (int i = 0; i < 8; ++i)
        {
            field_179159_c[i] = new GlStateManager.BooleanState(16384 + i);
        }

        field_179156_d = new GlStateManager.ColorMaterialState();
        field_179157_e = new GlStateManager.BlendState();
        field_179154_f = new GlStateManager.DepthState();
        field_179155_g = new GlStateManager.FogState();
        field_179167_h = new GlStateManager.CullState();
        field_179168_i = new GlStateManager.PolygonOffsetState();
        field_179165_j = new GlStateManager.ColorLogicState();
        field_179166_k = new GlStateManager.TexGenState();
        field_179163_l = new GlStateManager.ClearState();
        field_179164_m = new GlStateManager.StencilState();
        field_179161_n = new GlStateManager.BooleanState(2977);
        field_179174_p = new GlStateManager.TextureState[8];

        for (int j = 0; j < 8; ++j)
        {
            field_179174_p[j] = new GlStateManager.TextureState();
        }

        field_179173_q = 7425;
        field_179172_r = new GlStateManager.BooleanState(32826);
        field_179171_s = new GlStateManager.ColorMask();
        field_179170_t = new GlStateManager.Color();
    }

    static class AlphaState
    {
        public GlStateManager.BooleanState test;
        public int func;
        public float ref;

        private AlphaState()
        {
            this.test = new GlStateManager.BooleanState(3008);
            this.func = 519;
            this.ref = -1.0F;
        }
    }

    static class BlendState
    {
        public GlStateManager.BooleanState blend;
        public int srcFactorRgb;
        public int dstFactorRgb;
        public int srcFactorAlpha;
        public int dstFactorAlpha;

        private BlendState()
        {
            this.blend = new GlStateManager.BooleanState(3042);
            this.srcFactorRgb = 1;
            this.dstFactorRgb = 0;
            this.srcFactorAlpha = 1;
            this.dstFactorAlpha = 0;
        }
    }

    static class BooleanState
    {
        private final int capability;
        private boolean currentState;

        public BooleanState(int target)
        {
            this.capability = target;
        }

        public void disable()
        {
            this.setEnabled(false);
        }

        public void enable()
        {
            this.setEnabled(true);
        }

        public void setEnabled(boolean enabled)
        {
            if (enabled != this.currentState)
            {
                this.currentState = enabled;

                if (enabled)
                {
                    GL11.glEnable(this.capability);
                }
                else
                {
                    GL11.glDisable(this.capability);
                }
            }
        }
    }

    static class ClearState
    {
        public double depth;
        public GlStateManager.Color color;

        private ClearState()
        {
            this.depth = 1.0D;
            this.color = new GlStateManager.Color(0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    static class Color
    {
        public float red;
        public float green;
        public float blue;
        public float alpha;

        public Color()
        {
            this(1.0F, 1.0F, 1.0F, 1.0F);
        }

        public Color(float red, float green, float blue, float alpha)
        {
            this.red = 1.0F;
            this.green = 1.0F;
            this.blue = 1.0F;
            this.alpha = 1.0F;
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }
    }

    static class ColorLogicState
    {
        public GlStateManager.BooleanState colorLogicOp;
        public int logicOpcode;

        private ColorLogicState()
        {
            this.colorLogicOp = new GlStateManager.BooleanState(3058);
            this.logicOpcode = 5379;
        }
    }

    static class ColorMask
    {
        public boolean red;
        public boolean green;
        public boolean blue;
        public boolean alpha;

        private ColorMask()
        {
            this.red = true;
            this.green = true;
            this.blue = true;
            this.alpha = true;
        }
    }

    static class ColorMaterialState
    {
        public GlStateManager.BooleanState colorMaterial;
        public int face;
        public int mode;

        private ColorMaterialState()
        {
            this.colorMaterial = new GlStateManager.BooleanState(2903);
            this.face = 1032;
            this.mode = 5634;
        }
    }

    public static enum CullFace
    {
        FRONT(1028),
        BACK(1029),
        FRONT_AND_BACK(1032);

        public final int field_187328_d;

        private CullFace(int p_i46520_3_)
        {
            this.field_187328_d = p_i46520_3_;
        }
    }

    static class CullState
    {
        public GlStateManager.BooleanState cullFace;
        public int mode;

        private CullState()
        {
            this.cullFace = new GlStateManager.BooleanState(2884);
            this.mode = 1029;
        }
    }

    static class DepthState
    {
        public GlStateManager.BooleanState test;
        public boolean mask;
        public int func;

        private DepthState()
        {
            this.test = new GlStateManager.BooleanState(2929);
            this.mask = true;
            this.func = 513;
        }
    }

    public static enum DestFactor
    {
        CONSTANT_ALPHA(32771),
        CONSTANT_COLOR(32769),
        DST_ALPHA(772),
        DST_COLOR(774),
        ONE(1),
        ONE_MINUS_CONSTANT_ALPHA(32772),
        ONE_MINUS_CONSTANT_COLOR(32770),
        ONE_MINUS_DST_ALPHA(773),
        ONE_MINUS_DST_COLOR(775),
        ONE_MINUS_SRC_ALPHA(771),
        ONE_MINUS_SRC_COLOR(769),
        SRC_ALPHA(770),
        SRC_COLOR(768),
        ZERO(0);

        public final int field_187345_o;

        private DestFactor(int p_i46519_3_)
        {
            this.field_187345_o = p_i46519_3_;
        }
    }

    public static enum FogMode
    {
        LINEAR(9729),
        EXP(2048),
        EXP2(2049);

        public final int param;

        private FogMode(int p_i46518_3_)
        {
            this.param = p_i46518_3_;
        }
    }

    static class FogState
    {
        public GlStateManager.BooleanState fog;
        public int mode;
        public float density;
        public float start;
        public float end;

        private FogState()
        {
            this.fog = new GlStateManager.BooleanState(2912);
            this.mode = 2048;
            this.density = 1.0F;
            this.end = 1.0F;
        }
    }

    public static enum LogicOp
    {
        AND(5377),
        AND_INVERTED(5380),
        AND_REVERSE(5378),
        CLEAR(5376),
        COPY(5379),
        COPY_INVERTED(5388),
        EQUIV(5385),
        INVERT(5386),
        NAND(5390),
        NOOP(5381),
        NOR(5384),
        OR(5383),
        OR_INVERTED(5389),
        OR_REVERSE(5387),
        SET(5391),
        XOR(5382);

        public final int opcode;

        private LogicOp(int p_i46517_3_)
        {
            this.opcode = p_i46517_3_;
        }
    }

    static class PolygonOffsetState
    {
        public GlStateManager.BooleanState polyOffset;
        public GlStateManager.BooleanState lineOffset;
        public float factor;
        public float units;

        private PolygonOffsetState()
        {
            this.polyOffset = new GlStateManager.BooleanState(32823);
            this.lineOffset = new GlStateManager.BooleanState(10754);
        }
    }

    public static enum Profile
    {
        DEFAULT {
            public void func_187373_a()
            {
                GlStateManager.func_179118_c();
                GlStateManager.func_179092_a(519, 0.0F);
                GlStateManager.func_179140_f();
                GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, RenderHelper.func_74521_a(0.2F, 0.2F, 0.2F, 1.0F));

                for (int i = 0; i < 8; ++i)
                {
                    GlStateManager.func_179122_b(i);
                    GL11.glLight(GL11.GL_LIGHT0 + i, GL11.GL_AMBIENT, RenderHelper.func_74521_a(0.0F, 0.0F, 0.0F, 1.0F));
                    GL11.glLight(GL11.GL_LIGHT0 + i, GL11.GL_POSITION, RenderHelper.func_74521_a(0.0F, 0.0F, 1.0F, 0.0F));

                    if (i == 0)
                    {
                        GL11.glLight(GL11.GL_LIGHT0 + i, GL11.GL_DIFFUSE, RenderHelper.func_74521_a(1.0F, 1.0F, 1.0F, 1.0F));
                        GL11.glLight(GL11.GL_LIGHT0 + i, GL11.GL_SPECULAR, RenderHelper.func_74521_a(1.0F, 1.0F, 1.0F, 1.0F));
                    }
                    else
                    {
                        GL11.glLight(GL11.GL_LIGHT0 + i, GL11.GL_DIFFUSE, RenderHelper.func_74521_a(0.0F, 0.0F, 0.0F, 1.0F));
                        GL11.glLight(GL11.GL_LIGHT0 + i, GL11.GL_SPECULAR, RenderHelper.func_74521_a(0.0F, 0.0F, 0.0F, 1.0F));
                    }
                }

                GlStateManager.func_179119_h();
                GlStateManager.func_179104_a(1032, 5634);
                GlStateManager.func_179097_i();
                GlStateManager.func_179143_c(513);
                GlStateManager.func_179132_a(true);
                GlStateManager.func_179084_k();
                GlStateManager.func_187401_a(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.func_187428_a(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GL14.glBlendEquation(GL14.GL_FUNC_ADD);
                GlStateManager.func_179106_n();
                GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
                GlStateManager.func_179095_a(1.0F);
                GlStateManager.func_179102_b(0.0F);
                GlStateManager.func_179153_c(1.0F);
                GL11.glFog(GL11.GL_FOG_COLOR, RenderHelper.func_74521_a(0.0F, 0.0F, 0.0F, 0.0F));

                if (GLContext.getCapabilities().GL_NV_fog_distance)
                {
                    GL11.glFogi(GL11.GL_FOG_MODE, 34140);
                }

                GlStateManager.func_179136_a(0.0F, 0.0F);
                GlStateManager.func_179134_v();
                GlStateManager.func_179116_f(5379);
                GlStateManager.func_179100_b(GlStateManager.TexGen.S);
                GlStateManager.func_179149_a(GlStateManager.TexGen.S, 9216);
                GlStateManager.func_179105_a(GlStateManager.TexGen.S, 9474, RenderHelper.func_74521_a(1.0F, 0.0F, 0.0F, 0.0F));
                GlStateManager.func_179105_a(GlStateManager.TexGen.S, 9217, RenderHelper.func_74521_a(1.0F, 0.0F, 0.0F, 0.0F));
                GlStateManager.func_179100_b(GlStateManager.TexGen.T);
                GlStateManager.func_179149_a(GlStateManager.TexGen.T, 9216);
                GlStateManager.func_179105_a(GlStateManager.TexGen.T, 9474, RenderHelper.func_74521_a(0.0F, 1.0F, 0.0F, 0.0F));
                GlStateManager.func_179105_a(GlStateManager.TexGen.T, 9217, RenderHelper.func_74521_a(0.0F, 1.0F, 0.0F, 0.0F));
                GlStateManager.func_179100_b(GlStateManager.TexGen.R);
                GlStateManager.func_179149_a(GlStateManager.TexGen.R, 9216);
                GlStateManager.func_179105_a(GlStateManager.TexGen.R, 9474, RenderHelper.func_74521_a(0.0F, 0.0F, 0.0F, 0.0F));
                GlStateManager.func_179105_a(GlStateManager.TexGen.R, 9217, RenderHelper.func_74521_a(0.0F, 0.0F, 0.0F, 0.0F));
                GlStateManager.func_179100_b(GlStateManager.TexGen.Q);
                GlStateManager.func_179149_a(GlStateManager.TexGen.Q, 9216);
                GlStateManager.func_179105_a(GlStateManager.TexGen.Q, 9474, RenderHelper.func_74521_a(0.0F, 0.0F, 0.0F, 0.0F));
                GlStateManager.func_179105_a(GlStateManager.TexGen.Q, 9217, RenderHelper.func_74521_a(0.0F, 0.0F, 0.0F, 0.0F));
                GlStateManager.func_179138_g(0);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 1000);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LOD, 1000);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MIN_LOD, -1000);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0.0F);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
                GL11.glTexEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_COLOR, RenderHelper.func_74521_a(0.0F, 0.0F, 0.0F, 0.0F));
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_COMBINE_RGB, GL11.GL_MODULATE);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_SOURCE0_RGB, GL11.GL_TEXTURE);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_SOURCE1_RGB, GL13.GL_PREVIOUS);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_SOURCE2_RGB, GL13.GL_CONSTANT);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_SOURCE0_ALPHA, GL11.GL_TEXTURE);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_SOURCE1_ALPHA, GL13.GL_PREVIOUS);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_SOURCE2_ALPHA, GL13.GL_CONSTANT);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_OPERAND2_RGB, GL11.GL_SRC_ALPHA);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_OPERAND1_ALPHA, GL11.GL_SRC_ALPHA);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_OPERAND2_ALPHA, GL11.GL_SRC_ALPHA);
                GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL13.GL_RGB_SCALE, 1.0F);
                GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_ALPHA_SCALE, 1.0F);
                GlStateManager.func_179133_A();
                GlStateManager.func_179103_j(7425);
                GlStateManager.func_179101_C();
                GlStateManager.func_179135_a(true, true, true, true);
                GlStateManager.func_179151_a(1.0D);
                GL11.glLineWidth(1.0F);
                GL11.glNormal3f(0.0F, 0.0F, 1.0F);
                GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
                GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_FILL);
            }

            public void func_187374_b()
            {
            }
        },
        PLAYER_SKIN {
            public void func_187373_a()
            {
                GlStateManager.func_179147_l();
                GlStateManager.func_179120_a(770, 771, 1, 0);
            }

            public void func_187374_b()
            {
                GlStateManager.func_179084_k();
            }
        },
        TRANSPARENT_MODEL {
            public void func_187373_a()
            {
                GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.15F);
                GlStateManager.func_179132_a(false);
                GlStateManager.func_179147_l();
                GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.func_179092_a(516, 0.003921569F);
            }

            public void func_187374_b()
            {
                GlStateManager.func_179084_k();
                GlStateManager.func_179092_a(516, 0.1F);
                GlStateManager.func_179132_a(true);
            }
        };

        private Profile()
        {
        }

        public abstract void func_187373_a();

        public abstract void func_187374_b();
    }

    public static enum SourceFactor
    {
        CONSTANT_ALPHA(32771),
        CONSTANT_COLOR(32769),
        DST_ALPHA(772),
        DST_COLOR(774),
        ONE(1),
        ONE_MINUS_CONSTANT_ALPHA(32772),
        ONE_MINUS_CONSTANT_COLOR(32770),
        ONE_MINUS_DST_ALPHA(773),
        ONE_MINUS_DST_COLOR(775),
        ONE_MINUS_SRC_ALPHA(771),
        ONE_MINUS_SRC_COLOR(769),
        SRC_ALPHA(770),
        SRC_ALPHA_SATURATE(776),
        SRC_COLOR(768),
        ZERO(0);

        public final int field_187395_p;

        private SourceFactor(int p_i46514_3_)
        {
            this.field_187395_p = p_i46514_3_;
        }
    }

    static class StencilFunc
    {
        public int func;
        public int mask;

        private StencilFunc()
        {
            this.func = 519;
            this.mask = -1;
        }
    }

    static class StencilState
    {
        public GlStateManager.StencilFunc func;
        public int mask;
        public int sfail;
        public int dpfail;
        public int dppass;

        private StencilState()
        {
            this.func = new GlStateManager.StencilFunc();
            this.mask = -1;
            this.sfail = 7680;
            this.dpfail = 7680;
            this.dppass = 7680;
        }
    }

    public static enum TexGen
    {
        S,
        T,
        R,
        Q;
    }

    static class TexGenCoord
    {
        public GlStateManager.BooleanState textureGen;
        public int coord;
        public int mode = -1;

        public TexGenCoord(int p_i46254_1_, int p_i46254_2_)
        {
            this.coord = p_i46254_1_;
            this.textureGen = new GlStateManager.BooleanState(p_i46254_2_);
        }
    }

    static class TexGenState
    {
        public GlStateManager.TexGenCoord s;
        public GlStateManager.TexGenCoord t;
        public GlStateManager.TexGenCoord r;
        public GlStateManager.TexGenCoord q;

        private TexGenState()
        {
            this.s = new GlStateManager.TexGenCoord(8192, 3168);
            this.t = new GlStateManager.TexGenCoord(8193, 3169);
            this.r = new GlStateManager.TexGenCoord(8194, 3170);
            this.q = new GlStateManager.TexGenCoord(8195, 3171);
        }
    }

    static class TextureState
    {
        public GlStateManager.BooleanState texture2DState;
        public int textureName;

        private TextureState()
        {
            this.texture2DState = new GlStateManager.BooleanState(3553);
        }
    }
}

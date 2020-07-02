package net.minecraft.client.renderer.debug;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class DebugRendererCollisionBox implements DebugRenderer.IDebugRenderer
{
    private final Minecraft minecraft;
    private EntityPlayer field_191313_b;
    private double field_191314_c;
    private double field_191315_d;
    private double field_191316_e;

    public DebugRendererCollisionBox(Minecraft minecraftIn)
    {
        this.minecraft = minecraftIn;
    }

    public void func_190060_a(float p_190060_1_, long p_190060_2_)
    {
        this.field_191313_b = this.minecraft.player;
        this.field_191314_c = this.field_191313_b.lastTickPosX + (this.field_191313_b.posX - this.field_191313_b.lastTickPosX) * (double)p_190060_1_;
        this.field_191315_d = this.field_191313_b.lastTickPosY + (this.field_191313_b.posY - this.field_191313_b.lastTickPosY) * (double)p_190060_1_;
        this.field_191316_e = this.field_191313_b.lastTickPosZ + (this.field_191313_b.posZ - this.field_191313_b.lastTickPosZ) * (double)p_190060_1_;
        World world = this.minecraft.player.world;
        List<AxisAlignedBB> list = world.func_184144_a(this.field_191313_b, this.field_191313_b.getBoundingBox().grow(6.0D));
        GlStateManager.func_179147_l();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.func_187441_d(2.0F);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a(false);

        for (AxisAlignedBB axisalignedbb : list)
        {
            RenderGlobal.func_189697_a(axisalignedbb.grow(0.002D).offset(-this.field_191314_c, -this.field_191315_d, -this.field_191316_e), 1.0F, 1.0F, 1.0F, 1.0F);
        }

        GlStateManager.func_179132_a(true);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
}

package net.minecraft.client.renderer.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

public class DebugRenderer
{
    public final DebugRenderer.IDebugRenderer pathfinding;
    public final DebugRenderer.IDebugRenderer water;
    public final DebugRenderer.IDebugRenderer chunkBorder;
    public final DebugRenderer.IDebugRenderer heightMap;
    public final DebugRenderer.IDebugRenderer collisionBox;
    public final DebugRenderer.IDebugRenderer neighborsUpdate;
    public final DebugRenderer.IDebugRenderer solidFace;
    private boolean chunkBorderEnabled;
    private boolean field_190080_f;
    private boolean field_190081_g;
    private boolean field_190082_h;
    private boolean field_191326_j;
    private boolean field_191558_l;
    private boolean field_193853_n;

    public DebugRenderer(Minecraft clientIn)
    {
        this.pathfinding = new DebugRendererPathfinding(clientIn);
        this.water = new DebugRendererWater(clientIn);
        this.chunkBorder = new DebugRendererChunkBorder(clientIn);
        this.heightMap = new DebugRendererHeightMap(clientIn);
        this.collisionBox = new DebugRendererCollisionBox(clientIn);
        this.neighborsUpdate = new DebugRendererNeighborsUpdate(clientIn);
        this.solidFace = new DebugRendererSolidFace(clientIn);
    }

    public boolean func_190074_a()
    {
        return this.chunkBorderEnabled || this.field_190080_f || this.field_190081_g || this.field_190082_h || this.field_191326_j || this.field_191558_l || this.field_193853_n;
    }

    /**
     * Toggles the debug screen's visibility.
     */
    public boolean toggleChunkBorders()
    {
        this.chunkBorderEnabled = !this.chunkBorderEnabled;
        return this.chunkBorderEnabled;
    }

    public void func_190073_a(float p_190073_1_, long p_190073_2_)
    {
        if (this.field_190080_f)
        {
            this.pathfinding.func_190060_a(p_190073_1_, p_190073_2_);
        }

        if (this.chunkBorderEnabled && !Minecraft.getInstance().isReducedDebug())
        {
            this.chunkBorder.func_190060_a(p_190073_1_, p_190073_2_);
        }

        if (this.field_190081_g)
        {
            this.water.func_190060_a(p_190073_1_, p_190073_2_);
        }

        if (this.field_190082_h)
        {
            this.heightMap.func_190060_a(p_190073_1_, p_190073_2_);
        }

        if (this.field_191326_j)
        {
            this.collisionBox.func_190060_a(p_190073_1_, p_190073_2_);
        }

        if (this.field_191558_l)
        {
            this.neighborsUpdate.func_190060_a(p_190073_1_, p_190073_2_);
        }

        if (this.field_193853_n)
        {
            this.solidFace.func_190060_a(p_190073_1_, p_190073_2_);
        }
    }

    public static void func_191556_a(String p_191556_0_, int p_191556_1_, int p_191556_2_, int p_191556_3_, float p_191556_4_, int p_191556_5_)
    {
        func_190076_a(p_191556_0_, (double)p_191556_1_ + 0.5D, (double)p_191556_2_ + 0.5D, (double)p_191556_3_ + 0.5D, p_191556_4_, p_191556_5_);
    }

    public static void func_190076_a(String p_190076_0_, double p_190076_1_, double p_190076_3_, double p_190076_5_, float p_190076_7_, int p_190076_8_)
    {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player != null && minecraft.getRenderManager() != null && minecraft.getRenderManager().options != null)
        {
            FontRenderer fontrenderer = minecraft.fontRenderer;
            EntityPlayer entityplayer = minecraft.player;
            double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)p_190076_7_;
            double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)p_190076_7_;
            double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)p_190076_7_;
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)(p_190076_1_ - d0), (float)(p_190076_3_ - d1) + 0.07F, (float)(p_190076_5_ - d2));
            GlStateManager.func_187432_a(0.0F, 1.0F, 0.0F);
            GlStateManager.func_179152_a(0.02F, -0.02F, 0.02F);
            RenderManager rendermanager = minecraft.getRenderManager();
            GlStateManager.func_179114_b(-rendermanager.field_78735_i, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b((float)(rendermanager.options.thirdPersonView == 2 ? 1 : -1) * rendermanager.field_78732_j, 1.0F, 0.0F, 0.0F);
            GlStateManager.func_179140_f();
            GlStateManager.func_179098_w();
            GlStateManager.func_179126_j();
            GlStateManager.func_179132_a(true);
            GlStateManager.func_179152_a(-1.0F, 1.0F, 1.0F);
            fontrenderer.func_78276_b(p_190076_0_, -fontrenderer.getStringWidth(p_190076_0_) / 2, 0, p_190076_8_);
            GlStateManager.func_179145_e();
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.func_179121_F();
        }
    }

    public interface IDebugRenderer
    {
        void func_190060_a(float p_190060_1_, long p_190060_2_);
    }
}

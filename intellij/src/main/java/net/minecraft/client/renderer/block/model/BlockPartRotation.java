package net.minecraft.client.renderer.block.model;

import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

public class BlockPartRotation
{
    public final Vector3f origin;
    public final EnumFacing.Axis axis;
    public final float angle;
    public final boolean rescale;

    public BlockPartRotation(Vector3f p_i46229_1_, EnumFacing.Axis p_i46229_2_, float p_i46229_3_, boolean p_i46229_4_)
    {
        this.origin = p_i46229_1_;
        this.axis = p_i46229_2_;
        this.angle = p_i46229_3_;
        this.rescale = p_i46229_4_;
    }
}

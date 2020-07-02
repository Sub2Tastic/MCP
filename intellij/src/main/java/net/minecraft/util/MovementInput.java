package net.minecraft.util;

import net.minecraft.util.math.Vec2f;

public class MovementInput
{
    public float moveStrafe;
    public float moveForward;
    public boolean forwardKeyDown;
    public boolean backKeyDown;
    public boolean leftKeyDown;
    public boolean rightKeyDown;
    public boolean jump;
    public boolean field_78899_d;

    public void func_78898_a()
    {
    }

    public Vec2f getMoveVector()
    {
        return new Vec2f(this.moveStrafe, this.moveForward);
    }
}

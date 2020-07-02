package net.minecraft.world;

import net.minecraft.inventory.IInventory;

public interface ILockableContainer extends IInventory, IInteractionObject
{
    boolean func_174893_q_();

    void func_174892_a(LockCode p_174892_1_);

    LockCode func_174891_i();
}

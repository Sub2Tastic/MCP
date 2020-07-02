package net.minecraft.world;

import net.minecraft.util.text.ITextComponent;

public interface IWorldNameable
{
    String func_70005_c_();

    boolean hasCustomName();

    ITextComponent getDisplayName();
}

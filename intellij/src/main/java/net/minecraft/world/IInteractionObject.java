package net.minecraft.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public interface IInteractionObject extends IWorldNameable
{
    Container func_174876_a(InventoryPlayer p_174876_1_, EntityPlayer p_174876_2_);

    String func_174875_k();
}

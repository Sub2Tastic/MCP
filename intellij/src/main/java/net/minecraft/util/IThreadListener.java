package net.minecraft.util;

import com.google.common.util.concurrent.ListenableFuture;

public interface IThreadListener
{
    ListenableFuture<Object> func_152344_a(Runnable p_152344_1_);

    boolean func_152345_ab();
}

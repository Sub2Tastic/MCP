package net.minecraft.util;

public interface IProgressUpdate
{
    void func_73720_a(String p_73720_1_);

    void func_73721_b(String p_73721_1_);

    void func_73719_c(String p_73719_1_);

    /**
     * Updates the progress bar on the loading screen to the specified amount.
     */
    void setLoadingProgress(int progress);

    void setDoneWorking();
}

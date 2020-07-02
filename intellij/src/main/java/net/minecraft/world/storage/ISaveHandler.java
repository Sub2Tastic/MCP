package net.minecraft.world.storage;

import java.io.File;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;

public interface ISaveHandler
{
    @Nullable

    /**
     * Loads and returns the world info
     */
    WorldInfo loadWorldInfo();

    /**
     * Checks the session lock to prevent save collisions
     */
    void checkSessionLock() throws MinecraftException;

    IChunkLoader func_75763_a(WorldProvider p_75763_1_);

    /**
     * Saves the given World Info with the given NBTTagCompound as the Player.
     */
    void saveWorldInfoWithPlayer(WorldInfo worldInformation, NBTTagCompound tagCompound);

    /**
     * used to update level.dat from old format to MCRegion format
     */
    void saveWorldInfo(WorldInfo worldInformation);

    IPlayerFileData func_75756_e();

    void func_75759_a();

    /**
     * Gets the File object corresponding to the base directory of this world.
     */
    File getWorldDirectory();

    File func_75758_b(String p_75758_1_);

    TemplateManager getStructureTemplateManager();
}

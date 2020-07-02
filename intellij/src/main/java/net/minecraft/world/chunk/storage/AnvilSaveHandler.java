package net.minecraft.world.chunk.storage;

import java.io.File;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraft.world.storage.WorldInfo;

public class AnvilSaveHandler extends SaveHandler
{
    public AnvilSaveHandler(File p_i46650_1_, String p_i46650_2_, boolean p_i46650_3_, DataFixer p_i46650_4_)
    {
        super(p_i46650_1_, p_i46650_2_, p_i46650_3_, p_i46650_4_);
    }

    public IChunkLoader func_75763_a(WorldProvider p_75763_1_)
    {
        File file1 = this.getWorldDirectory();

        if (p_75763_1_ instanceof WorldProviderHell)
        {
            File file3 = new File(file1, "DIM-1");
            file3.mkdirs();
            return new AnvilChunkLoader(file3, this.field_186341_a);
        }
        else if (p_75763_1_ instanceof WorldProviderEnd)
        {
            File file2 = new File(file1, "DIM1");
            file2.mkdirs();
            return new AnvilChunkLoader(file2, this.field_186341_a);
        }
        else
        {
            return new AnvilChunkLoader(file1, this.field_186341_a);
        }
    }

    /**
     * Saves the given World Info with the given NBTTagCompound as the Player.
     */
    public void saveWorldInfoWithPlayer(WorldInfo worldInformation, @Nullable NBTTagCompound tagCompound)
    {
        worldInformation.setSaveVersion(19133);
        super.saveWorldInfoWithPlayer(worldInformation, tagCompound);
    }

    public void func_75759_a()
    {
        try
        {
            ThreadedFileIOBase.func_178779_a().func_75734_a();
        }
        catch (InterruptedException interruptedexception)
        {
            interruptedexception.printStackTrace();
        }

        RegionFileCache.func_76551_a();
    }
}

package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveFormatOld implements ISaveFormat
{
    private static final Logger field_151479_b = LogManager.getLogger();
    protected final File field_75808_a;
    protected final DataFixer field_186354_b;

    public SaveFormatOld(File p_i46647_1_, DataFixer p_i46647_2_)
    {
        this.field_186354_b = p_i46647_2_;

        if (!p_i46647_1_.exists())
        {
            p_i46647_1_.mkdirs();
        }

        this.field_75808_a = p_i46647_1_;
    }

    public String func_154333_a()
    {
        return "Old Format";
    }

    public List<WorldSummary> getSaveList() throws AnvilConverterException
    {
        List<WorldSummary> list = Lists.<WorldSummary>newArrayList();

        for (int i = 0; i < 5; ++i)
        {
            String s = "World" + (i + 1);
            WorldInfo worldinfo = this.getWorldInfo(s);

            if (worldinfo != null)
            {
                list.add(new WorldSummary(worldinfo, s, "", worldinfo.func_76092_g(), false));
            }
        }

        return list;
    }

    public void func_75800_d()
    {
    }

    @Nullable

    /**
     * Returns the world's WorldInfo object
     */
    public WorldInfo getWorldInfo(String saveName)
    {
        File file1 = new File(this.field_75808_a, saveName);

        if (!file1.exists())
        {
            return null;
        }
        else
        {
            File file2 = new File(file1, "level.dat");

            if (file2.exists())
            {
                WorldInfo worldinfo = func_186353_a(file2, this.field_186354_b);

                if (worldinfo != null)
                {
                    return worldinfo;
                }
            }

            file2 = new File(file1, "level.dat_old");
            return file2.exists() ? func_186353_a(file2, this.field_186354_b) : null;
        }
    }

    @Nullable
    public static WorldInfo func_186353_a(File p_186353_0_, DataFixer p_186353_1_)
    {
        try
        {
            NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(p_186353_0_));
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Data");
            return new WorldInfo(p_186353_1_.func_188257_a(FixTypes.LEVEL, nbttagcompound1));
        }
        catch (Exception exception)
        {
            field_151479_b.error("Exception reading {}", p_186353_0_, exception);
            return null;
        }
    }

    /**
     * Renames the world by storing the new name in level.dat. It does *not* rename the directory containing the world
     * data.
     */
    public void renameWorld(String dirName, String newName)
    {
        File file1 = new File(this.field_75808_a, dirName);

        if (file1.exists())
        {
            File file2 = new File(file1, "level.dat");

            if (file2.exists())
            {
                try
                {
                    NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                    NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Data");
                    nbttagcompound1.putString("LevelName", newName);
                    CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file2));
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        }
    }

    public boolean func_154335_d(String p_154335_1_)
    {
        File file1 = new File(this.field_75808_a, p_154335_1_);

        if (file1.exists())
        {
            return false;
        }
        else
        {
            try
            {
                file1.mkdir();
                file1.delete();
                return true;
            }
            catch (Throwable throwable)
            {
                field_151479_b.warn("Couldn't make new level", throwable);
                return false;
            }
        }
    }

    /**
     * Deletes a world directory.
     */
    public boolean deleteWorldDirectory(String saveName)
    {
        File file1 = new File(this.field_75808_a, saveName);

        if (!file1.exists())
        {
            return true;
        }
        else
        {
            field_151479_b.info("Deleting level {}", (Object)saveName);

            for (int i = 1; i <= 5; ++i)
            {
                field_151479_b.info("Attempt {}...", (int)i);

                if (func_75807_a(file1.listFiles()))
                {
                    break;
                }

                field_151479_b.warn("Unsuccessful in deleting contents.");

                if (i < 5)
                {
                    try
                    {
                        Thread.sleep(500L);
                    }
                    catch (InterruptedException var5)
                    {
                        ;
                    }
                }
            }

            return file1.delete();
        }
    }

    protected static boolean func_75807_a(File[] p_75807_0_)
    {
        for (File file1 : p_75807_0_)
        {
            field_151479_b.debug("Deleting {}", (Object)file1);

            if (file1.isDirectory() && !func_75807_a(file1.listFiles()))
            {
                field_151479_b.warn("Couldn't delete directory {}", (Object)file1);
                return false;
            }

            if (!file1.delete())
            {
                field_151479_b.warn("Couldn't delete file {}", (Object)file1);
                return false;
            }
        }

        return true;
    }

    public ISaveHandler func_75804_a(String p_75804_1_, boolean p_75804_2_)
    {
        return new SaveHandler(this.field_75808_a, p_75804_1_, p_75804_2_, this.field_186354_b);
    }

    public boolean func_154334_a(String p_154334_1_)
    {
        return false;
    }

    /**
     * gets if the map is old chunk saving (true) or McRegion (false)
     */
    public boolean isOldMapFormat(String saveName)
    {
        return false;
    }

    /**
     * converts the map to mcRegion
     */
    public boolean convertMapFormat(String filename, IProgressUpdate progressCallback)
    {
        return false;
    }

    /**
     * Return whether the given world can be loaded.
     */
    public boolean canLoadWorld(String saveName)
    {
        File file1 = new File(this.field_75808_a, saveName);
        return file1.isDirectory();
    }

    /**
     * Gets a file within the given world.
     */
    public File getFile(String saveName, String filePath)
    {
        return new File(new File(this.field_75808_a, saveName), filePath);
    }
}

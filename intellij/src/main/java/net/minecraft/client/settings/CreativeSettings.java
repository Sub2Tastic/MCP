package net.minecraft.client.settings;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreativeSettings
{
    private static final Logger LOGGER = LogManager.getLogger();
    protected Minecraft field_192565_a;
    private final File dataFile;
    private final HotbarSnapshot[] hotbarSnapshots = new HotbarSnapshot[9];

    public CreativeSettings(Minecraft p_i47395_1_, File p_i47395_2_)
    {
        this.field_192565_a = p_i47395_1_;
        this.dataFile = new File(p_i47395_2_, "hotbar.nbt");

        for (int i = 0; i < 9; ++i)
        {
            this.hotbarSnapshots[i] = new HotbarSnapshot();
        }

        this.func_192562_a();
    }

    public void func_192562_a()
    {
        try
        {
            NBTTagCompound nbttagcompound = CompressedStreamTools.read(this.dataFile);

            if (nbttagcompound == null)
            {
                return;
            }

            for (int i = 0; i < 9; ++i)
            {
                this.hotbarSnapshots[i].fromTag(nbttagcompound.getList(String.valueOf(i), 10));
            }
        }
        catch (Exception exception)
        {
            LOGGER.error("Failed to load creative mode options", (Throwable)exception);
        }
    }

    public void save()
    {
        try
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            for (int i = 0; i < 9; ++i)
            {
                nbttagcompound.func_74782_a(String.valueOf(i), this.hotbarSnapshots[i].createTag());
            }

            CompressedStreamTools.write(nbttagcompound, this.dataFile);
        }
        catch (Exception exception)
        {
            LOGGER.error("Failed to save creative mode options", (Throwable)exception);
        }
    }

    public HotbarSnapshot getHotbarSnapshot(int index)
    {
        return this.hotbarSnapshots[index];
    }
}

package net.minecraft.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveHandler implements ISaveHandler, IPlayerFileData
{
    private static final Logger field_151478_a = LogManager.getLogger();
    private final File field_75770_b;
    private final File field_75771_c;
    private final File field_75768_d;
    private final long field_75769_e = MinecraftServer.func_130071_aq();
    private final String field_75767_f;
    private final TemplateManager field_186342_h;
    protected final DataFixer field_186341_a;

    public SaveHandler(File p_i46648_1_, String p_i46648_2_, boolean p_i46648_3_, DataFixer p_i46648_4_)
    {
        this.field_186341_a = p_i46648_4_;
        this.field_75770_b = new File(p_i46648_1_, p_i46648_2_);
        this.field_75770_b.mkdirs();
        this.field_75771_c = new File(this.field_75770_b, "playerdata");
        this.field_75768_d = new File(this.field_75770_b, "data");
        this.field_75768_d.mkdirs();
        this.field_75767_f = p_i46648_2_;

        if (p_i46648_3_)
        {
            this.field_75771_c.mkdirs();
            this.field_186342_h = new TemplateManager((new File(this.field_75770_b, "structures")).toString(), p_i46648_4_);
        }
        else
        {
            this.field_186342_h = null;
        }

        this.func_75766_h();
    }

    private void func_75766_h()
    {
        try
        {
            File file1 = new File(this.field_75770_b, "session.lock");
            DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));

            try
            {
                dataoutputstream.writeLong(this.field_75769_e);
            }
            finally
            {
                dataoutputstream.close();
            }
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
            throw new RuntimeException("Failed to check session lock, aborting");
        }
    }

    /**
     * Gets the File object corresponding to the base directory of this world.
     */
    public File getWorldDirectory()
    {
        return this.field_75770_b;
    }

    /**
     * Checks the session lock to prevent save collisions
     */
    public void checkSessionLock() throws MinecraftException
    {
        try
        {
            File file1 = new File(this.field_75770_b, "session.lock");
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));

            try
            {
                if (datainputstream.readLong() != this.field_75769_e)
                {
                    throw new MinecraftException("The save is being accessed from another location, aborting");
                }
            }
            finally
            {
                datainputstream.close();
            }
        }
        catch (IOException var7)
        {
            throw new MinecraftException("Failed to check session lock, aborting");
        }
    }

    public IChunkLoader func_75763_a(WorldProvider p_75763_1_)
    {
        throw new RuntimeException("Old Chunk Storage is no longer supported.");
    }

    @Nullable

    /**
     * Loads and returns the world info
     */
    public WorldInfo loadWorldInfo()
    {
        File file1 = new File(this.field_75770_b, "level.dat");

        if (file1.exists())
        {
            WorldInfo worldinfo = SaveFormatOld.func_186353_a(file1, this.field_186341_a);

            if (worldinfo != null)
            {
                return worldinfo;
            }
        }

        file1 = new File(this.field_75770_b, "level.dat_old");
        return file1.exists() ? SaveFormatOld.func_186353_a(file1, this.field_186341_a) : null;
    }

    /**
     * Saves the given World Info with the given NBTTagCompound as the Player.
     */
    public void saveWorldInfoWithPlayer(WorldInfo worldInformation, @Nullable NBTTagCompound tagCompound)
    {
        NBTTagCompound nbttagcompound = worldInformation.cloneNBTCompound(tagCompound);
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.func_74782_a("Data", nbttagcompound);

        try
        {
            File file1 = new File(this.field_75770_b, "level.dat_new");
            File file2 = new File(this.field_75770_b, "level.dat_old");
            File file3 = new File(this.field_75770_b, "level.dat");
            CompressedStreamTools.writeCompressed(nbttagcompound1, new FileOutputStream(file1));

            if (file2.exists())
            {
                file2.delete();
            }

            file3.renameTo(file2);

            if (file3.exists())
            {
                file3.delete();
            }

            file1.renameTo(file3);

            if (file1.exists())
            {
                file1.delete();
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * used to update level.dat from old format to MCRegion format
     */
    public void saveWorldInfo(WorldInfo worldInformation)
    {
        this.saveWorldInfoWithPlayer(worldInformation, (NBTTagCompound)null);
    }

    /**
     * Writes the player data to disk from the specified PlayerEntityMP.
     */
    public void writePlayerData(EntityPlayer player)
    {
        try
        {
            NBTTagCompound nbttagcompound = player.writeWithoutTypeId(new NBTTagCompound());
            File file1 = new File(this.field_75771_c, player.getCachedUniqueIdString() + ".dat.tmp");
            File file2 = new File(this.field_75771_c, player.getCachedUniqueIdString() + ".dat");
            CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file1));

            if (file2.exists())
            {
                file2.delete();
            }

            file1.renameTo(file2);
        }
        catch (Exception var5)
        {
            field_151478_a.warn("Failed to save player data for {}", (Object)player.func_70005_c_());
        }
    }

    @Nullable

    /**
     * Reads the player data from disk into the specified PlayerEntityMP.
     */
    public NBTTagCompound readPlayerData(EntityPlayer player)
    {
        NBTTagCompound nbttagcompound = null;

        try
        {
            File file1 = new File(this.field_75771_c, player.getCachedUniqueIdString() + ".dat");

            if (file1.exists() && file1.isFile())
            {
                nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file1));
            }
        }
        catch (Exception var4)
        {
            field_151478_a.warn("Failed to load player data for {}", (Object)player.func_70005_c_());
        }

        if (nbttagcompound != null)
        {
            player.read(this.field_186341_a.func_188257_a(FixTypes.PLAYER, nbttagcompound));
        }

        return nbttagcompound;
    }

    public IPlayerFileData func_75756_e()
    {
        return this;
    }

    public String[] func_75754_f()
    {
        String[] astring = this.field_75771_c.list();

        if (astring == null)
        {
            astring = new String[0];
        }

        for (int i = 0; i < astring.length; ++i)
        {
            if (astring[i].endsWith(".dat"))
            {
                astring[i] = astring[i].substring(0, astring[i].length() - 4);
            }
        }

        return astring;
    }

    public void func_75759_a()
    {
    }

    public File func_75758_b(String p_75758_1_)
    {
        return new File(this.field_75768_d, p_75758_1_ + ".dat");
    }

    public TemplateManager getStructureTemplateManager()
    {
        return this.field_186342_h;
    }
}

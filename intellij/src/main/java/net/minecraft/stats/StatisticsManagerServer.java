package net.minecraft.stats;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatisticsManagerServer extends StatisticsManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final MinecraftServer server;
    private final File statsFile;
    private final Set<StatBase> dirty = Sets.<StatBase>newHashSet();
    private int lastStatRequest = -300;

    public StatisticsManagerServer(MinecraftServer serverIn, File statsFileIn)
    {
        this.server = serverIn;
        this.statsFile = statsFileIn;
    }

    public void func_150882_a()
    {
        if (this.statsFile.isFile())
        {
            try
            {
                this.statsData.clear();
                this.statsData.putAll(this.func_150881_a(FileUtils.readFileToString(this.statsFile)));
            }
            catch (IOException ioexception)
            {
                LOGGER.error("Couldn't read statistics file {}", this.statsFile, ioexception);
            }
            catch (JsonParseException jsonparseexception)
            {
                LOGGER.error("Couldn't parse statistics file {}", this.statsFile, jsonparseexception);
            }
        }
    }

    public void saveStatFile()
    {
        try
        {
            FileUtils.writeStringToFile(this.statsFile, func_150880_a(this.statsData));
        }
        catch (IOException ioexception)
        {
            LOGGER.error("Couldn't save stats", (Throwable)ioexception);
        }
    }

    /**
     * Triggers the logging of an achievement and attempts to announce to server
     */
    public void setValue(EntityPlayer playerIn, StatBase statIn, int p_150873_3_)
    {
        super.setValue(playerIn, statIn, p_150873_3_);
        this.dirty.add(statIn);
    }

    private Set<StatBase> getDirty()
    {
        Set<StatBase> set = Sets.newHashSet(this.dirty);
        this.dirty.clear();
        return set;
    }

    public Map<StatBase, TupleIntJsonSerializable> func_150881_a(String p_150881_1_)
    {
        JsonElement jsonelement = (new JsonParser()).parse(p_150881_1_);

        if (!jsonelement.isJsonObject())
        {
            return Maps.<StatBase, TupleIntJsonSerializable>newHashMap();
        }
        else
        {
            JsonObject jsonobject = jsonelement.getAsJsonObject();
            Map<StatBase, TupleIntJsonSerializable> map = Maps.<StatBase, TupleIntJsonSerializable>newHashMap();

            for (Entry<String, JsonElement> entry : jsonobject.entrySet())
            {
                StatBase statbase = StatList.func_151177_a(entry.getKey());

                if (statbase != null)
                {
                    TupleIntJsonSerializable tupleintjsonserializable = new TupleIntJsonSerializable();

                    if (((JsonElement)entry.getValue()).isJsonPrimitive() && ((JsonElement)entry.getValue()).getAsJsonPrimitive().isNumber())
                    {
                        tupleintjsonserializable.func_151188_a(((JsonElement)entry.getValue()).getAsInt());
                    }
                    else if (((JsonElement)entry.getValue()).isJsonObject())
                    {
                        JsonObject jsonobject1 = ((JsonElement)entry.getValue()).getAsJsonObject();

                        if (jsonobject1.has("value") && jsonobject1.get("value").isJsonPrimitive() && jsonobject1.get("value").getAsJsonPrimitive().isNumber())
                        {
                            tupleintjsonserializable.func_151188_a(jsonobject1.getAsJsonPrimitive("value").getAsInt());
                        }

                        if (jsonobject1.has("progress") && statbase.func_150954_l() != null)
                        {
                            try
                            {
                                Constructor <? extends IJsonSerializable > constructor = statbase.func_150954_l().getConstructor();
                                IJsonSerializable ijsonserializable = constructor.newInstance();
                                ijsonserializable.func_152753_a(jsonobject1.get("progress"));
                                tupleintjsonserializable.func_151190_a(ijsonserializable);
                            }
                            catch (Throwable throwable)
                            {
                                LOGGER.warn("Invalid statistic progress in {}", this.statsFile, throwable);
                            }
                        }
                    }

                    map.put(statbase, tupleintjsonserializable);
                }
                else
                {
                    LOGGER.warn("Invalid statistic in {}: Don't know what {} is", this.statsFile, entry.getKey());
                }
            }

            return map;
        }
    }

    public static String func_150880_a(Map<StatBase, TupleIntJsonSerializable> p_150880_0_)
    {
        JsonObject jsonobject = new JsonObject();

        for (Entry<StatBase, TupleIntJsonSerializable> entry : p_150880_0_.entrySet())
        {
            if (((TupleIntJsonSerializable)entry.getValue()).func_151187_b() != null)
            {
                JsonObject jsonobject1 = new JsonObject();
                jsonobject1.addProperty("value", Integer.valueOf(((TupleIntJsonSerializable)entry.getValue()).func_151189_a()));

                try
                {
                    jsonobject1.add("progress", ((TupleIntJsonSerializable)entry.getValue()).func_151187_b().func_151003_a());
                }
                catch (Throwable throwable)
                {
                    LOGGER.warn("Couldn't save statistic {}: error serializing progress", ((StatBase)entry.getKey()).func_150951_e(), throwable);
                }

                jsonobject.add((entry.getKey()).field_75975_e, jsonobject1);
            }
            else
            {
                jsonobject.addProperty((entry.getKey()).field_75975_e, Integer.valueOf(((TupleIntJsonSerializable)entry.getValue()).func_151189_a()));
            }
        }

        return jsonobject.toString();
    }

    public void markAllDirty()
    {
        this.dirty.addAll(this.statsData.keySet());
    }

    public void sendStats(EntityPlayerMP player)
    {
        int i = this.server.getTickCounter();
        Map<StatBase, Integer> map = Maps.<StatBase, Integer>newHashMap();

        if (i - this.lastStatRequest > 300)
        {
            this.lastStatRequest = i;

            for (StatBase statbase : this.getDirty())
            {
                map.put(statbase, Integer.valueOf(this.getValue(statbase)));
            }
        }

        player.connection.sendPacket(new SPacketStatistics(map));
    }
}

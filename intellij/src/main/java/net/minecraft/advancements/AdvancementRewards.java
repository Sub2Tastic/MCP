package net.minecraft.advancements;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Arrays;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;

public class AdvancementRewards
{
    public static final AdvancementRewards EMPTY = new AdvancementRewards(0, new ResourceLocation[0], new ResourceLocation[0], FunctionObject.CacheableFunction.EMPTY);
    private final int experience;
    private final ResourceLocation[] loot;
    private final ResourceLocation[] recipes;
    private final FunctionObject.CacheableFunction function;

    public AdvancementRewards(int experience, ResourceLocation[] loot, ResourceLocation[] recipes, FunctionObject.CacheableFunction function)
    {
        this.experience = experience;
        this.loot = loot;
        this.recipes = recipes;
        this.function = function;
    }

    public void apply(final EntityPlayerMP player)
    {
        player.func_71023_q(this.experience);
        LootContext lootcontext = (new LootContext.Builder(player.getServerWorld())).func_186472_a(player).func_186471_a();
        boolean flag = false;

        for (ResourceLocation resourcelocation : this.loot)
        {
            for (ItemStack itemstack : player.world.func_184146_ak().getLootTableFromLocation(resourcelocation).func_186462_a(player.getRNG(), lootcontext))
            {
                if (player.addItemStackToInventory(itemstack))
                {
                    player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    flag = true;
                }
                else
                {
                    EntityItem entityitem = player.dropItem(itemstack, false);

                    if (entityitem != null)
                    {
                        entityitem.setNoPickupDelay();
                        entityitem.func_145797_a(player.func_70005_c_());
                    }
                }
            }
        }

        if (flag)
        {
            player.container.detectAndSendChanges();
        }

        if (this.recipes.length > 0)
        {
            player.unlockRecipes(this.recipes);
        }

        final MinecraftServer minecraftserver = player.server;
        FunctionObject functionobject = this.function.func_193518_a(minecraftserver.getFunctionManager());

        if (functionobject != null)
        {
            ICommandSender icommandsender = new ICommandSender()
            {
                public String func_70005_c_()
                {
                    return player.func_70005_c_();
                }
                public ITextComponent getDisplayName()
                {
                    return player.getDisplayName();
                }
                public void sendMessage(ITextComponent component)
                {
                }
                public boolean func_70003_b(int p_70003_1_, String p_70003_2_)
                {
                    return p_70003_1_ <= 2;
                }
                public BlockPos getPosition()
                {
                    return player.getPosition();
                }
                public Vec3d getPositionVector()
                {
                    return player.getPositionVector();
                }
                public World getEntityWorld()
                {
                    return player.world;
                }
                public Entity func_174793_f()
                {
                    return player;
                }
                public boolean func_174792_t_()
                {
                    return minecraftserver.worlds[0].getGameRules().func_82766_b("commandBlockOutput");
                }
                public void func_174794_a(CommandResultStats.Type p_174794_1_, int p_174794_2_)
                {
                    player.func_174794_a(p_174794_1_, p_174794_2_);
                }
                public MinecraftServer getServer()
                {
                    return player.getServer();
                }
            };
            minecraftserver.getFunctionManager().func_194019_a(functionobject, icommandsender);
        }
    }

    public String toString()
    {
        return "AdvancementRewards{experience=" + this.experience + ", loot=" + Arrays.toString((Object[])this.loot) + ", recipes=" + Arrays.toString((Object[])this.recipes) + ", function=" + this.function + '}';
    }

    public static class Deserializer implements JsonDeserializer<AdvancementRewards>
    {
        public AdvancementRewards deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
        {
            JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "rewards");
            int i = JsonUtils.getInt(jsonobject, "experience", 0);
            JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "loot", new JsonArray());
            ResourceLocation[] aresourcelocation = new ResourceLocation[jsonarray.size()];

            for (int j = 0; j < aresourcelocation.length; ++j)
            {
                aresourcelocation[j] = new ResourceLocation(JsonUtils.getString(jsonarray.get(j), "loot[" + j + "]"));
            }

            JsonArray jsonarray1 = JsonUtils.getJsonArray(jsonobject, "recipes", new JsonArray());
            ResourceLocation[] aresourcelocation1 = new ResourceLocation[jsonarray1.size()];

            for (int k = 0; k < aresourcelocation1.length; ++k)
            {
                aresourcelocation1[k] = new ResourceLocation(JsonUtils.getString(jsonarray1.get(k), "recipes[" + k + "]"));
                IRecipe irecipe = CraftingManager.func_193373_a(aresourcelocation1[k]);

                if (irecipe == null)
                {
                    throw new JsonSyntaxException("Unknown recipe '" + aresourcelocation1[k] + "'");
                }
            }

            FunctionObject.CacheableFunction functionobject$cacheablefunction;

            if (jsonobject.has("function"))
            {
                functionobject$cacheablefunction = new FunctionObject.CacheableFunction(new ResourceLocation(JsonUtils.getString(jsonobject, "function")));
            }
            else
            {
                functionobject$cacheablefunction = FunctionObject.CacheableFunction.EMPTY;
            }

            return new AdvancementRewards(i, aresourcelocation, aresourcelocation1, functionobject$cacheablefunction);
        }
    }
}

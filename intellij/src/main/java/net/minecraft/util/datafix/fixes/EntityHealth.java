package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class EntityHealth implements IFixableData
{
    private static final Set<String> ENTITY_LIST = Sets.newHashSet("ArmorStand", "Bat", "Blaze", "CaveSpider", "Chicken", "Cow", "Creeper", "EnderDragon", "Enderman", "Endermite", "EntityHorse", "Ghast", "Giant", "Guardian", "LavaSlime", "MushroomCow", "Ozelot", "Pig", "PigZombie", "Rabbit", "Sheep", "Shulker", "Silverfish", "Skeleton", "Slime", "SnowMan", "Spider", "Squid", "Villager", "VillagerGolem", "Witch", "WitherBoss", "Wolf", "Zombie");

    public int func_188216_a()
    {
        return 109;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        if (ENTITY_LIST.contains(p_188217_1_.getString("id")))
        {
            float f;

            if (p_188217_1_.contains("HealF", 99))
            {
                f = p_188217_1_.getFloat("HealF");
                p_188217_1_.remove("HealF");
            }
            else
            {
                if (!p_188217_1_.contains("Health", 99))
                {
                    return p_188217_1_;
                }

                f = p_188217_1_.getFloat("Health");
            }

            p_188217_1_.putFloat("Health", f);
        }

        return p_188217_1_;
    }
}
